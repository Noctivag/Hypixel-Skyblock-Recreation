package de.noctivag.skyblock.engine.rte.services;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.SetParams;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Datamutex Service (TSD-1) - Critical Transaction Safety System
 * 
 * Implements atomic transaction safety with rollback mechanisms for:
 * - Economic operations (Bazaar, Auction House)
 * - Cross-server data consistency
 * - Automatic rollback on failures
 * - Deadlock detection and resolution
 * - Transaction isolation and durability
 * 
 * This service ensures that every economic interaction is either
 * completely successful or completely rolled back, preventing
 * catastrophic exploits and data corruption.
 */
public class DatamutexService {
    
    private static final Logger logger = Logger.getLogger(DatamutexService.class.getName());
    
    private final JedisPool jedisPool;
    
    // Transaction management
    private final Map<String, TransactionContext> activeTransactions = new ConcurrentHashMap<>();
    private final Map<String, Long> transactionLocks = new ConcurrentHashMap<>();
    
    // Configuration
    private static final long TRANSACTION_TIMEOUT = 30000L; // 30 seconds
    private static final long LOCK_TIMEOUT = 10000L; // 10 seconds
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY = 100L; // 100ms
    
    // Redis keys
    private static final String TRANSACTION_PREFIX = "rte:transaction:";
    private static final String LOCK_PREFIX = "rte:lock:";
    private static final String ROLLBACK_PREFIX = "rte:rollback:";
    private static final String DEADLOCK_DETECTION_KEY = "rte:deadlock_detection";
    
    public DatamutexService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        startTransactionCleanup();
        startDeadlockDetection();
    }
    
    /**
     * Execute atomic transaction with automatic rollback on failure
     */
    public <T> CompletableFuture<T> executeAtomicTransaction(String transactionId, 
                                                           TransactionOperation<T> operation) {
        return CompletableFuture.supplyAsync(() -> {
            TransactionContext context = null;
            try {
                // Create transaction context
                context = new TransactionContext(transactionId, java.lang.System.currentTimeMillis());
                activeTransactions.put(transactionId, context);
                
                // Acquire necessary locks
                if (!acquireLocks(context, operation.getRequiredLocks())) {
                    throw new TransactionException("Failed to acquire required locks");
                }
                
                // Execute operation with rollback capability
                T result = executeWithRollback(context, operation);
                
                // Commit transaction
                commitTransaction(context);
                
                return result;
                
            } catch (Exception e) {
                // Rollback transaction on any failure
                if (context != null) {
                    rollbackTransaction(context);
                }
                throw new TransactionException("Transaction failed: " + e.getMessage(), e);
            } finally {
                // Cleanup
                if (context != null) {
                    releaseLocks(context);
                    activeTransactions.remove(transactionId);
                }
            }
        });
    }
    
    /**
     * Execute operation with rollback capability
     */
    private <T> T executeWithRollback(TransactionContext context, TransactionOperation<T> operation) throws Exception {
        try (Jedis jedis = jedisPool.getResource()) {
            Transaction redisTransaction = jedis.multi();
            
            try {
                // Execute the operation
                T result = operation.execute(context, redisTransaction);
                
                // Store rollback information
                storeRollbackData(context, operation.getRollbackData());
                
                // Execute Redis transaction
                List<Object> results = redisTransaction.exec();
                
                if (results == null || results.isEmpty()) {
                    throw new TransactionException("Redis transaction failed");
                }
                
                return result;
                
            } catch (Exception e) {
                redisTransaction.discard();
                throw e;
            }
        }
    }
    
    /**
     * Acquire locks for transaction
     */
    private boolean acquireLocks(TransactionContext context, List<String> lockKeys) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> acquiredLocks = new ArrayList<>();
            
            try {
                for (String lockKey : lockKeys) {
                    String fullLockKey = LOCK_PREFIX + lockKey;
                    String lockValue = context.getTransactionId() + ":" + java.lang.System.currentTimeMillis();
                    
                    // Try to acquire lock with timeout
                    String result = jedis.set(fullLockKey, lockValue, SetParams.setParams().nx().ex((int)(LOCK_TIMEOUT / 1000)));
                    
                    if (!"OK".equals(result)) {
                        // Failed to acquire lock, release already acquired locks
                        releaseAcquiredLocks(jedis, acquiredLocks);
                        return false;
                    }
                    
                    acquiredLocks.add(fullLockKey);
                    context.addLock(fullLockKey, lockValue);
                }
                
                return true;
                
            } catch (Exception e) {
                releaseAcquiredLocks(jedis, acquiredLocks);
                return false;
            }
        }
    }
    
    /**
     * Release acquired locks
     */
    private void releaseAcquiredLocks(Jedis jedis, List<String> lockKeys) {
        for (String lockKey : lockKeys) {
            try {
                jedis.del(lockKey);
            } catch (Exception e) {
                logger.warning("Failed to release lock " + lockKey + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Release all locks for transaction
     */
    private void releaseLocks(TransactionContext context) {
        try (Jedis jedis = jedisPool.getResource()) {
            for (Map.Entry<String, String> entry : context.getLocks().entrySet()) {
                String lockKey = entry.getKey();
                String lockValue = entry.getValue();
                
                // Use Lua script to ensure we only delete our own lock
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                               "return redis.call('del', KEYS[1]) else return 0 end";
                jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
            }
        } catch (Exception e) {
            logger.severe("Failed to release locks for transaction " + context.getTransactionId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Store rollback data for potential rollback
     */
    private void storeRollbackData(TransactionContext context, RollbackData rollbackData) {
        try (Jedis jedis = jedisPool.getResource()) {
            String rollbackKey = ROLLBACK_PREFIX + context.getTransactionId();
            
            if (rollbackData != null) {
                // Store rollback operations
                for (RollbackOperation operation : rollbackData.getOperations()) {
                    jedis.hset(rollbackKey, operation.getKey(), operation.toJson());
                }
                
                // Set expiration
                jedis.expire(rollbackKey, (int)(TRANSACTION_TIMEOUT / 1000));
            }
        } catch (Exception e) {
            logger.severe("Failed to store rollback data: " + e.getMessage());
        }
    }
    
    /**
     * Commit transaction
     */
    private void commitTransaction(TransactionContext context) {
        try (Jedis jedis = jedisPool.getResource()) {
            // Remove rollback data
            String rollbackKey = ROLLBACK_PREFIX + context.getTransactionId();
            jedis.del(rollbackKey);
            
            // Log successful transaction
            logger.info("Transaction " + context.getTransactionId() + " committed successfully");
            
        } catch (Exception e) {
            logger.severe("Failed to commit transaction " + context.getTransactionId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Rollback transaction
     */
    private void rollbackTransaction(TransactionContext context) {
        try (Jedis jedis = jedisPool.getResource()) {
            String rollbackKey = ROLLBACK_PREFIX + context.getTransactionId();
            Map<String, String> rollbackData = jedis.hgetAll(rollbackKey);
            
            if (!rollbackData.isEmpty()) {
                // Execute rollback operations
                for (Map.Entry<String, String> entry : rollbackData.entrySet()) {
                    try {
                        RollbackOperation operation = RollbackOperation.fromJson(entry.getValue());
                        operation.execute(jedis);
                    } catch (Exception e) {
                        logger.severe("Failed to execute rollback operation: " + e.getMessage());
                    }
                }
            }
            
            // Remove rollback data
            jedis.del(rollbackKey);
            
            // Log rollback
            logger.warning("Transaction " + context.getTransactionId() + " rolled back");
            
        } catch (Exception e) {
            logger.severe("Failed to rollback transaction " + context.getTransactionId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Start transaction cleanup task
     */
    private void startTransactionCleanup() {
        Timer timer = new Timer("TransactionCleanup", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanupExpiredTransactions();
            }
        }, 60000L, 60000L); // Run every minute
    }
    
    /**
     * Cleanup expired transactions
     */
    private void cleanupExpiredTransactions() {
        long now = java.lang.System.currentTimeMillis();
        List<String> expiredTransactions = new ArrayList<>();
        
        for (Map.Entry<String, TransactionContext> entry : activeTransactions.entrySet()) {
            if (now - entry.getValue().getStartTime() > TRANSACTION_TIMEOUT) {
                expiredTransactions.add(entry.getKey());
            }
        }
        
        for (String transactionId : expiredTransactions) {
            TransactionContext context = activeTransactions.remove(transactionId);
            if (context != null) {
                rollbackTransaction(context);
                releaseLocks(context);
                logger.warning("Expired transaction " + transactionId + " rolled back");
            }
        }
    }
    
    /**
     * Start deadlock detection
     */
    private void startDeadlockDetection() {
        Timer timer = new Timer("DeadlockDetection", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                detectAndResolveDeadlocks();
            }
        }, 30000L, 30000L); // Run every 30 seconds
    }
    
    /**
     * Detect and resolve deadlocks
     */
    private void detectAndResolveDeadlocks() {
        try (Jedis jedis = jedisPool.getResource()) {
            // Get all active locks
            Set<String> lockKeys = jedis.keys(LOCK_PREFIX + "*");
            
            if (lockKeys.size() < 2) {
                return; // No potential deadlock with less than 2 locks
            }
            
            // Build lock dependency graph
            Map<String, Set<String>> lockDependencies = new HashMap<>();
            
            for (String lockKey : lockKeys) {
                String lockValue = jedis.get(lockKey);
                if (lockValue != null) {
                    String transactionId = lockValue.split(":")[0];
                    lockDependencies.computeIfAbsent(transactionId, k -> new HashSet<>()).add(lockKey);
                }
            }
            
            // Detect circular dependencies (deadlocks)
            List<String> deadlockedTransactions = detectCircularDependencies(lockDependencies);
            
            // Resolve deadlocks by rolling back oldest transactions
            for (String transactionId : deadlockedTransactions) {
                TransactionContext context = activeTransactions.get(transactionId);
                if (context != null) {
                    rollbackTransaction(context);
                    releaseLocks(context);
                    activeTransactions.remove(transactionId);
                    logger.warning("Deadlocked transaction " + transactionId + " rolled back");
                }
            }
            
        } catch (Exception e) {
            logger.severe("Failed to detect deadlocks: " + e.getMessage());
        }
    }
    
    /**
     * Detect circular dependencies in lock graph
     */
    private List<String> detectCircularDependencies(Map<String, Set<String>> lockDependencies) {
        List<String> deadlockedTransactions = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        
        for (String transactionId : lockDependencies.keySet()) {
            if (!visited.contains(transactionId)) {
                if (hasCycle(transactionId, lockDependencies, visited, recursionStack)) {
                    deadlockedTransactions.add(transactionId);
                }
            }
        }
        
        return deadlockedTransactions;
    }
    
    /**
     * Check for cycle in dependency graph
     */
    private boolean hasCycle(String transactionId, Map<String, Set<String>> dependencies, 
                           Set<String> visited, Set<String> recursionStack) {
        visited.add(transactionId);
        recursionStack.add(transactionId);
        
        Set<String> locks = dependencies.get(transactionId);
        if (locks != null) {
            for (String lock : locks) {
                // Find which transaction holds this lock
                String holderTransaction = findLockHolder(lock);
                if (holderTransaction != null && !holderTransaction.equals(transactionId)) {
                    if (recursionStack.contains(holderTransaction)) {
                        return true; // Cycle detected
                    }
                    if (!visited.contains(holderTransaction)) {
                        if (hasCycle(holderTransaction, dependencies, visited, recursionStack)) {
                            return true;
                        }
                    }
                }
            }
        }
        
        recursionStack.remove(transactionId);
        return false;
    }
    
    /**
     * Find which transaction holds a specific lock
     */
    private String findLockHolder(String lockKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            String lockValue = jedis.get(lockKey);
            if (lockValue != null) {
                return lockValue.split(":")[0];
            }
        } catch (Exception e) {
            logger.severe("Failed to find lock holder: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Execute economic transaction with automatic rollback
     */
    public <T> CompletableFuture<T> executeEconomicTransaction(String transactionId, 
                                                             EconomicOperation<T> operation) {
        return executeAtomicTransaction(transactionId, new TransactionOperation<T>() {
            @Override
            public List<String> getRequiredLocks() {
                return operation.getRequiredLocks();
            }
            
            @Override
            public T execute(TransactionContext context, Transaction redisTransaction) throws Exception {
                return operation.execute(context, redisTransaction);
            }
            
            @Override
            public RollbackData getRollbackData() {
                return operation.getRollbackData();
            }
        });
    }
    
    /**
     * Get transaction status
     */
    public CompletableFuture<TransactionStatus> getTransactionStatus(String transactionId) {
        return CompletableFuture.supplyAsync(() -> {
            TransactionContext context = activeTransactions.get(transactionId);
            if (context != null) {
                return new TransactionStatus(transactionId, TransactionStatus.Status.ACTIVE, 
                                           java.lang.System.currentTimeMillis() - context.getStartTime());
            }
            
            // Check if transaction exists in Redis
            try (Jedis jedis = jedisPool.getResource()) {
                String rollbackKey = ROLLBACK_PREFIX + transactionId;
                if (jedis.exists(rollbackKey)) {
                    return new TransactionStatus(transactionId, TransactionStatus.Status.PENDING, 0);
                }
            } catch (Exception e) {
                logger.severe("Failed to check transaction status: " + e.getMessage());
            }
            
            return new TransactionStatus(transactionId, TransactionStatus.Status.COMPLETED, 0);
        });
    }
    
    // Interface definitions
    public interface TransactionOperation<T> {
        List<String> getRequiredLocks();
        T execute(TransactionContext context, Transaction redisTransaction) throws Exception;
        RollbackData getRollbackData();
    }
    
    public interface EconomicOperation<T> {
        List<String> getRequiredLocks();
        T execute(TransactionContext context, Transaction redisTransaction) throws Exception;
        RollbackData getRollbackData();
    }
    
    // Data classes
    public static class TransactionContext {
        private final String transactionId;
        private final long startTime;
        private final Map<String, String> locks = new HashMap<>();
        private final Map<String, Object> metadata = new HashMap<>();
        
        public TransactionContext(String transactionId, long startTime) {
            this.transactionId = transactionId;
            this.startTime = startTime;
        }
        
        public void addLock(String lockKey, String lockValue) {
            locks.put(lockKey, lockValue);
        }
        
        public void setMetadata(String key, Object value) {
            metadata.put(key, value);
        }
        
        public Object getMetadata(String key) {
            return metadata.get(key);
        }
        
        // Getters
        public String getTransactionId() { return transactionId; }
        public long getStartTime() { return startTime; }
        public Map<String, String> getLocks() { return locks; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    public static class RollbackData {
        private final List<RollbackOperation> operations;
        
        public RollbackData(List<RollbackOperation> operations) {
            this.operations = operations;
        }
        
        public List<RollbackOperation> getOperations() { return operations; }
    }
    
    public static class RollbackOperation {
        private final String key;
        private final String operation;
        private final String data;
        
        public RollbackOperation(String key, String operation, String data) {
            this.key = key;
            this.operation = operation;
            this.data = data;
        }
        
        public void execute(Jedis jedis) {
            switch (operation) {
                case "SET":
                    jedis.set(key, data);
                    break;
                case "DEL":
                    jedis.del(key);
                    break;
                case "HSET":
                    // Parse hash data
                    String[] parts = data.split(":");
                    if (parts.length == 2) {
                        jedis.hset(key, parts[0], parts[1]);
                    }
                    break;
                case "HDEL":
                    jedis.hdel(key, data);
                    break;
                case "ZADD":
                    // Parse sorted set data
                    String[] zParts = data.split(":");
                    if (zParts.length == 2) {
                        jedis.zadd(key, Double.parseDouble(zParts[0]), zParts[1]);
                    }
                    break;
                case "ZREM":
                    jedis.zrem(key, data);
                    break;
            }
        }
        
        public String toJson() {
            return String.format("{\"key\":\"%s\",\"operation\":\"%s\",\"data\":\"%s\"}", 
                               key, operation, data);
        }
        
        public static RollbackOperation fromJson(String json) {
            // Simple JSON parsing
            String[] parts = json.replaceAll("[{}\"]", "").split(",");
            String key = parts[0].split(":")[1];
            String operation = parts[1].split(":")[1];
            String data = parts[2].split(":")[1];
            return new RollbackOperation(key, operation, data);
        }
        
        // Getters
        public String getKey() { return key; }
        public String getOperation() { return operation; }
        public String getData() { return data; }
    }
    
    public static class TransactionStatus {
        private final String transactionId;
        private final Status status;
        private final long duration;
        
        public TransactionStatus(String transactionId, Status status, long duration) {
            this.transactionId = transactionId;
            this.status = status;
            this.duration = duration;
        }
        
        // Getters
        public String getTransactionId() { return transactionId; }
        public Status getStatus() { return status; }
        public long getDuration() { return duration; }
        
        public enum Status {
            ACTIVE, PENDING, COMPLETED, FAILED, ROLLED_BACK
        }
    }
    
    public static class TransactionException extends RuntimeException {
        public TransactionException(String message) {
            super(message);
        }
        
        public TransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
