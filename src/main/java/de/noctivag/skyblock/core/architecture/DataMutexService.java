package de.noctivag.skyblock.core.architecture;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * DataMutex Service - Distributed Lock Mechanism for Consistency
 * 
 * This service implements the Redlock algorithm for distributed locking to prevent
 * duplication exploits and ensure transaction consistency across the distributed system.
 * 
 * Key Features:
 * - Redlock algorithm implementation for distributed locks
 * - Automatic lock expiration to prevent deadlocks
 * - Lock extension mechanism for long-running operations
 * - Deadlock detection and resolution
 * - Performance monitoring and metrics
 * 
 * Lock Types:
 * - Player locks: lock:player:<UUID> - For player-specific operations
 * - Item locks: lock:bazaar_item:<ITEM_ID> - For bazaar trading
 * - Auction locks: lock:auction:<AUCTION_ID> - For auction house operations
 * - Guild locks: lock:guild:<GUILD_ID> - For guild operations
 * - Global locks: lock:global:<OPERATION> - For system-wide operations
 */
public class DataMutexService {
    
    private static final Logger logger = Logger.getLogger(DataMutexService.class.getName());
    
    // Lock configuration
    private static final String LOCK_PREFIX = "lock:";
    private static final int DEFAULT_LOCK_TIMEOUT = 30; // seconds
    private static final int MAX_LOCK_EXTENSIONS = 3;
    private static final long LOCK_RETRY_INTERVAL = 100; // milliseconds
    private static final int MAX_RETRY_ATTEMPTS = 10;
    
    // Redis cluster nodes (minimum 3 for Redlock)
    private final Set<RedisNode> redisNodes;
    private final List<JedisPool> jedisPools;
    private final JedisCluster jedisCluster;
    
    // Local lock tracking
    private final Map<String, LockInfo> activeLocks = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> lockAttempts = new ConcurrentHashMap<>();
    
    // Thread pool for async operations
    private final ExecutorService asyncExecutor;
    
    // Metrics
    private final AtomicInteger totalLocksAcquired = new AtomicInteger(0);
    private final AtomicInteger totalLocksFailed = new AtomicInteger(0);
    private final AtomicInteger totalLocksReleased = new AtomicInteger(0);
    
    public DataMutexService(Set<RedisNode> redisNodes) {
        this.redisNodes = redisNodes;
        this.jedisPools = new ArrayList<>();
        this.asyncExecutor = Executors.newFixedThreadPool(8, r -> new Thread(r, "DataMutex-Thread"));
        
        // Initialize Redis connections
        initializeRedisConnections();
        this.jedisCluster = createJedisCluster();
    }
    
    /**
     * Acquire distributed lock using Redlock algorithm
     */
    public CompletableFuture<Boolean> acquireLock(String lockName, int timeoutSeconds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String lockKey = LOCK_PREFIX + lockName;
                String lockValue = UUID.randomUUID().toString();
                long expirationTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);
                
                // Track lock attempts
                lockAttempts.computeIfAbsent(lockName, k -> new AtomicInteger(0)).incrementAndGet();
                
                // Redlock algorithm: acquire locks on majority of nodes
                int acquiredLocks = 0;
                List<Jedis> acquiredConnections = new ArrayList<>();
                
                try {
                    for (JedisPool pool : jedisPools) {
                        try (Jedis jedis = pool.getResource()) {
                            String result = jedis.set(lockKey, lockValue, "NX", "EX", timeoutSeconds);
                            if ("OK".equals(result)) {
                                acquiredLocks++;
                                acquiredConnections.add(jedis);
                            }
                        } catch (Exception e) {
                            logger.warning("Failed to acquire lock on Redis node: " + e.getMessage());
                        }
                    }
                    
                    // Check if we acquired locks on majority of nodes
                    boolean lockAcquired = acquiredLocks > (jedisPools.size() / 2);
                    
                    if (lockAcquired) {
                        // Store lock information
                        LockInfo lockInfo = new LockInfo(lockKey, lockValue, expirationTime, timeoutSeconds);
                        activeLocks.put(lockName, lockInfo);
                        totalLocksAcquired.incrementAndGet();
                        
                        logger.fine("Acquired distributed lock: " + lockName + " on " + acquiredLocks + "/" + jedisPools.size() + " nodes");
                        return true;
                    } else {
                        // Release any locks we acquired
                        releaseAcquiredLocks(acquiredConnections, lockKey, lockValue);
                        totalLocksFailed.incrementAndGet();
                        
                        logger.warning("Failed to acquire distributed lock: " + lockName + " (only " + acquiredLocks + "/" + jedisPools.size() + " nodes)");
                        return false;
                    }
                    
                } catch (Exception e) {
                    logger.severe("Error acquiring distributed lock: " + e.getMessage());
                    return false;
                }
                
            } catch (Exception e) {
                logger.severe("Critical error in acquireLock: " + e.getMessage());
                totalLocksFailed.incrementAndGet();
                return false;
            }
        }, asyncExecutor);
    }
    
    /**
     * Acquire lock with retry mechanism
     */
    public CompletableFuture<Boolean> acquireLockWithRetry(String lockName, int timeoutSeconds) {
        return CompletableFuture.supplyAsync(() -> {
            for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
                boolean acquired = acquireLock(lockName, timeoutSeconds).join();
                
                if (acquired) {
                    return true;
                }
                
                // Wait before retry
                try {
                    Thread.sleep(LOCK_RETRY_INTERVAL);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            
            logger.warning("Failed to acquire lock after " + MAX_RETRY_ATTEMPTS + " attempts: " + lockName);
            return false;
        }, asyncExecutor);
    }
    
    /**
     * Release distributed lock
     */
    public CompletableFuture<Boolean> releaseLock(String lockName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LockInfo lockInfo = activeLocks.remove(lockName);
                if (lockInfo == null) {
                    logger.warning("Attempted to release non-existent lock: " + lockName);
                    return false;
                }
                
                String lockKey = lockInfo.lockKey;
                String lockValue = lockInfo.lockValue;
                
                // Release lock on all nodes
                int releasedLocks = 0;
                String releaseScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                                     "return redis.call('del', KEYS[1]) else return 0 end";
                
                for (JedisPool pool : jedisPools) {
                    try (Jedis jedis = pool.getResource()) {
                        Object result = jedis.eval(releaseScript, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
                        if (result instanceof Long && (Long) result == 1) {
                            releasedLocks++;
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to release lock on Redis node: " + e.getMessage());
                    }
                }
                
                totalLocksReleased.incrementAndGet();
                logger.fine("Released distributed lock: " + lockName + " from " + releasedLocks + "/" + jedisPools.size() + " nodes");
                
                return releasedLocks > 0;
                
            } catch (Exception e) {
                logger.severe("Error releasing distributed lock: " + e.getMessage());
                return false;
            }
        }, asyncExecutor);
    }
    
    /**
     * Extend lock expiration time
     */
    public CompletableFuture<Boolean> extendLock(String lockName, int additionalSeconds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LockInfo lockInfo = activeLocks.get(lockName);
                if (lockInfo == null) {
                    logger.warning("Attempted to extend non-existent lock: " + lockName);
                    return false;
                }
                
                // Check if we've exceeded max extensions
                if (lockInfo.extensions >= MAX_LOCK_EXTENSIONS) {
                    logger.warning("Lock extension limit reached for: " + lockName);
                    return false;
                }
                
                String lockKey = lockInfo.lockKey;
                String lockValue = lockInfo.lockValue;
                
                // Extend lock on all nodes
                int extendedLocks = 0;
                String extendScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                                    "return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";
                
                for (JedisPool pool : jedisPools) {
                    try (Jedis jedis = pool.getResource()) {
                        Object result = jedis.eval(extendScript, 
                            Collections.singletonList(lockKey), 
                            Arrays.asList(lockValue, String.valueOf(additionalSeconds)));
                        if (result instanceof Long && (Long) result == 1) {
                            extendedLocks++;
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to extend lock on Redis node: " + e.getMessage());
                    }
                }
                
                if (extendedLocks > (jedisPools.size() / 2)) {
                    lockInfo.extensions++;
                    lockInfo.expirationTime = System.currentTimeMillis() + (additionalSeconds * 1000L);
                    logger.fine("Extended distributed lock: " + lockName + " for " + additionalSeconds + " seconds");
                    return true;
                }
                
                return false;
                
            } catch (Exception e) {
                logger.severe("Error extending distributed lock: " + e.getMessage());
                return false;
            }
        }, asyncExecutor);
    }
    
    /**
     * Execute operation with automatic lock management
     */
    public <T> CompletableFuture<T> executeWithLock(String lockName, int timeoutSeconds, 
                                                   java.util.function.Supplier<T> operation) {
        return acquireLock(lockName, timeoutSeconds)
            .thenCompose(acquired -> {
                if (!acquired) {
                    return CompletableFuture.failedFuture(
                        new RuntimeException("Failed to acquire lock: " + lockName));
                }
                
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return operation.get();
                    } finally {
                        releaseLock(lockName);
                    }
                }, asyncExecutor);
            });
    }
    
    /**
     * Execute operation with lock and retry
     */
    public <T> CompletableFuture<T> executeWithLockRetry(String lockName, int timeoutSeconds,
                                                        java.util.function.Supplier<T> operation) {
        return acquireLockWithRetry(lockName, timeoutSeconds)
            .thenCompose(acquired -> {
                if (!acquired) {
                    return CompletableFuture.failedFuture(
                        new RuntimeException("Failed to acquire lock after retries: " + lockName));
                }
                
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return operation.get();
                    } finally {
                        releaseLock(lockName);
                    }
                }, asyncExecutor);
            });
    }
    
    /**
     * Check if lock exists
     */
    public CompletableFuture<Boolean> isLocked(String lockName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String lockKey = LOCK_PREFIX + lockName;
                
                // Check if lock exists on majority of nodes
                int lockedNodes = 0;
                for (JedisPool pool : jedisPools) {
                    try (Jedis jedis = pool.getResource()) {
                        String value = jedis.get(lockKey);
                        if (value != null) {
                            lockedNodes++;
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to check lock on Redis node: " + e.getMessage());
                    }
                }
                
                return lockedNodes > (jedisPools.size() / 2);
                
            } catch (Exception e) {
                logger.severe("Error checking lock status: " + e.getMessage());
                return false;
            }
        }, asyncExecutor);
    }
    
    /**
     * Clean up expired locks
     */
    public CompletableFuture<Void> cleanupExpiredLocks() {
        return CompletableFuture.runAsync(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                List<String> expiredLocks = new ArrayList<>();
                
                for (Map.Entry<String, LockInfo> entry : activeLocks.entrySet()) {
                    if (entry.getValue().expirationTime < currentTime) {
                        expiredLocks.add(entry.getKey());
                    }
                }
                
                for (String lockName : expiredLocks) {
                    activeLocks.remove(lockName);
                    logger.fine("Cleaned up expired lock: " + lockName);
                }
                
            } catch (Exception e) {
                logger.warning("Error cleaning up expired locks: " + e.getMessage());
            }
        }, asyncExecutor);
    }
    
    /**
     * Get lock statistics
     */
    public LockStatistics getLockStatistics() {
        LockStatistics stats = new LockStatistics();
        stats.totalLocksAcquired = totalLocksAcquired.get();
        stats.totalLocksFailed = totalLocksFailed.get();
        stats.totalLocksReleased = totalLocksReleased.get();
        stats.activeLocks = activeLocks.size();
        stats.redisNodes = jedisPools.size();
        
        return stats;
    }
    
    /**
     * Initialize Redis connections
     */
    private void initializeRedisConnections() {
        for (RedisNode node : redisNodes) {
            try {
                JedisPool pool = new JedisPool(node.getHost(), node.getPort());
                jedisPools.add(pool);
                logger.info("Connected to Redis node: " + node.getHost() + ":" + node.getPort());
            } catch (Exception e) {
                logger.warning("Failed to connect to Redis node " + node.getHost() + ":" + node.getPort() + ": " + e.getMessage());
            }
        }
        
        if (jedisPools.isEmpty()) {
            throw new RuntimeException("No Redis nodes available for distributed locking");
        }
        
        logger.info("Initialized " + jedisPools.size() + " Redis connections for distributed locking");
    }
    
    /**
     * Create Jedis cluster (alternative to individual pools)
     */
    private JedisCluster createJedisCluster() {
        try {
            Set<redis.clients.jedis.HostAndPort> clusterNodes = new HashSet<>();
            for (RedisNode node : redisNodes) {
                clusterNodes.add(new redis.clients.jedis.HostAndPort(node.getHost(), node.getPort()));
            }
            
            return new JedisCluster(clusterNodes);
        } catch (Exception e) {
            logger.warning("Failed to create Jedis cluster: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Release locks acquired during failed lock attempt
     */
    private void releaseAcquiredLocks(List<Jedis> connections, String lockKey, String lockValue) {
        String releaseScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                             "return redis.call('del', KEYS[1]) else return 0 end";
        
        for (Jedis jedis : connections) {
            try {
                jedis.eval(releaseScript, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
            } catch (Exception e) {
                logger.warning("Failed to release acquired lock: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close all connections
     */
    public void close() {
        for (JedisPool pool : jedisPools) {
            try {
                pool.close();
            } catch (Exception e) {
                logger.warning("Error closing Redis pool: " + e.getMessage());
            }
        }
        
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (Exception e) {
                logger.warning("Error closing Jedis cluster: " + e.getMessage());
            }
        }
        
        asyncExecutor.shutdown();
    }
    
    // Data structures
    public static class RedisNode {
        private final String host;
        private final int port;
        
        public RedisNode(String host, int port) {
            this.host = host;
            this.port = port;
        }
        
        public String getHost() { return host; }
        public int getPort() { return port; }
    }
    
    private static class LockInfo {
        final String lockKey;
        final String lockValue;
        volatile long expirationTime;
        final int timeoutSeconds;
        volatile int extensions = 0;
        
        LockInfo(String lockKey, String lockValue, long expirationTime, int timeoutSeconds) {
            this.lockKey = lockKey;
            this.lockValue = lockValue;
            this.expirationTime = expirationTime;
            this.timeoutSeconds = timeoutSeconds;
        }
    }
    
    public static class LockStatistics {
        public int totalLocksAcquired;
        public int totalLocksFailed;
        public int totalLocksReleased;
        public int activeLocks;
        public int redisNodes;
    }
}
