package de.noctivag.skyblock.engine.rte.protocols;

import de.noctivag.skyblock.engine.rte.RealTimeEconomyEngine;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.function.Supplier;

/**
 * Transaction Protocol - Ensures atomic operations across distributed systems
 * 
 * Implements robust protocols to prevent inconsistencies during concurrent
 * trading operations across the distributed server architecture.
 */
public class TransactionProtocol {
    
    private final RealTimeEconomyEngine engine;
    private final int maxRetries = 3;
    private final long retryDelay = 100L; // 100ms
    
    public TransactionProtocol(RealTimeEconomyEngine engine) {
        this.engine = engine;
    }
    
    /**
     * Execute an atomic transaction with retry logic
     */
    public <T> T executeAtomicTransaction(Supplier<T> operation) {
        Exception lastException = null;
        
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                return executeWithRedisTransaction(operation);
            } catch (Exception e) {
                lastException = e;
                engine.getPlugin().getLogger().warning(
                    "Transaction attempt " + (attempt + 1) + " failed: " + e.getMessage()
                );
                
                if (attempt < maxRetries - 1) {
                    try {
                        Thread.sleep(retryDelay * (attempt + 1)); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Transaction interrupted", ie);
                    }
                }
            }
        }
        
        throw new RuntimeException("Transaction failed after " + maxRetries + " attempts", lastException);
    }
    
    /**
     * Execute operation with Redis transaction
     */
    private <T> T executeWithRedisTransaction(Supplier<T> operation) {
        try (Jedis jedis = engine.getJedisPool().getResource()) {
            // Start Redis transaction
            Transaction transaction = jedis.multi();
            
            try {
                // Execute the operation
                T result = operation.get();
                
                // Commit transaction
                transaction.exec();
                
                return result;
                
            } catch (Exception e) {
                // Discard transaction on error
                transaction.discard();
                throw e;
            }
        }
    }
    
    /**
     * Execute atomic order creation with conflict resolution
     */
    public <T> T executeOrderTransaction(String orderKey, Supplier<T> orderOperation) {
        return executeAtomicTransaction(() -> {
            try (Jedis jedis = engine.getJedisPool().getResource()) {
                // Check for existing order conflicts
                if (jedis.exists(orderKey)) {
                    throw new ConflictException("Order conflict detected for key: " + orderKey);
                }
                
                // Execute order operation
                return orderOperation.get();
            }
        });
    }
    
    /**
     * Execute atomic price update with version control
     */
    public <T> T executePriceUpdateTransaction(String itemId, double expectedPrice, Supplier<T> updateOperation) {
        return executeAtomicTransaction(() -> {
            try (Jedis jedis = engine.getJedisPool().getResource()) {
                String priceKey = "rte:price:" + itemId;
                String versionKey = "rte:version:" + itemId;
                
                // Check price version
                String currentVersion = jedis.get(versionKey);
                if (currentVersion != null) {
                    double currentPrice = Double.parseDouble(jedis.get(priceKey));
                    if (Math.abs(currentPrice - expectedPrice) > 0.01) {
                        throw new ConflictException("Price conflict detected for " + itemId + 
                                                 ": expected " + expectedPrice + ", got " + currentPrice);
                    }
                }
                
                // Execute update operation
                T result = updateOperation.get();
                
                // Update version
                jedis.incr(versionKey);
                
                return result;
            }
        });
    }
    
    /**
     * Execute atomic balance transfer
     */
    public <T> T executeBalanceTransfer(UUID fromPlayer, UUID toPlayer, double amount, Supplier<T> transferOperation) {
        return executeAtomicTransaction(() -> {
            try (Jedis jedis = engine.getJedisPool().getResource()) {
                String fromKey = "rte:balance:" + fromPlayer;
                String toKey = "rte:balance:" + toPlayer;
                String lockKey = "rte:lock:" + fromPlayer + ":" + toPlayer;
                
                // Acquire distributed lock
                String lockValue = String.valueOf(System.currentTimeMillis());
                if (jedis.setnx(lockKey, lockValue) == 0) {
                    throw new ConflictException("Transfer lock already exists");
                }
                
                try {
                    // Check balance
                    String fromBalanceStr = jedis.get(fromKey);
                    if (fromBalanceStr == null) {
                        throw new InsufficientFundsException("Player balance not found: " + fromPlayer);
                    }
                    
                    double fromBalance = Double.parseDouble(fromBalanceStr);
                    if (fromBalance < amount) {
                        throw new InsufficientFundsException("Insufficient funds: " + fromBalance + " < " + amount);
                    }
                    
                    // Execute transfer operation
                    T result = transferOperation.get();
                    
                    // Update balances atomically
                    double newFromBalance = fromBalance - amount;
                    double newToBalance = Double.parseDouble(jedis.get(toKey)) + amount;
                    jedis.set(fromKey, String.valueOf(newFromBalance));
                    jedis.set(toKey, String.valueOf(newToBalance));
                    
                    return result;
                    
                } finally {
                    // Release lock
                    jedis.del(lockKey);
                }
            }
        });
    }
    
    /**
     * Execute atomic inventory transfer
     */
    public <T> T executeInventoryTransfer(UUID fromPlayer, UUID toPlayer, String itemId, int amount, Supplier<T> transferOperation) {
        return executeAtomicTransaction(() -> {
            try (Jedis jedis = engine.getJedisPool().getResource()) {
                String fromKey = "rte:inventory:" + fromPlayer + ":" + itemId;
                String toKey = "rte:inventory:" + toPlayer + ":" + itemId;
                String lockKey = "rte:inv_lock:" + fromPlayer + ":" + toPlayer + ":" + itemId;
                
                // Acquire distributed lock
                String lockValue = String.valueOf(System.currentTimeMillis());
                if (jedis.setnx(lockKey, lockValue) == 0) {
                    throw new ConflictException("Inventory transfer lock already exists");
                }
                
                try {
                    // Check inventory
                    String fromAmountStr = jedis.get(fromKey);
                    if (fromAmountStr == null) {
                        throw new InsufficientItemsException("Item not found in inventory: " + itemId);
                    }
                    
                    int fromAmount = Integer.parseInt(fromAmountStr);
                    if (fromAmount < amount) {
                        throw new InsufficientItemsException("Insufficient items: " + fromAmount + " < " + amount);
                    }
                    
                    // Execute transfer operation
                    T result = transferOperation.get();
                    
                    // Update inventories atomically
                    jedis.decrBy(fromKey, amount);
                    jedis.incrBy(toKey, amount);
                    
                    return result;
                    
                } finally {
                    // Release lock
                    jedis.del(lockKey);
                }
            }
        });
    }
    
    /**
     * Execute atomic market data update
     */
    public <T> T executeMarketDataUpdate(String itemId, Supplier<T> updateOperation) {
        return executeAtomicTransaction(() -> {
            try (Jedis jedis = engine.getJedisPool().getResource()) {
                String dataKey = "rte:market:" + itemId;
                String lockKey = "rte:market_lock:" + itemId;
                
                // Acquire distributed lock
                String lockValue = String.valueOf(System.currentTimeMillis());
                if (jedis.setnx(lockKey, lockValue) == 0) {
                    throw new ConflictException("Market data lock already exists for " + itemId);
                }
                
                try {
                    // Execute update operation
                    T result = updateOperation.get();
                    
                    // Update market data timestamp
                    jedis.hset(dataKey, "last_update", String.valueOf(System.currentTimeMillis()));
                    
                    return result;
                    
                } finally {
                    // Release lock with timeout
                    jedis.expire(lockKey, 5); // 5 second timeout
                }
            }
        });
    }
    
    /**
     * Execute atomic order matching
     */
    public <T> T executeOrderMatching(String itemId, Supplier<T> matchingOperation) {
        return executeAtomicTransaction(() -> {
            try (Jedis jedis = engine.getJedisPool().getResource()) {
                String matchKey = "rte:match:" + itemId;
                String lockKey = "rte:match_lock:" + itemId;
                
                // Acquire distributed lock
                String lockValue = String.valueOf(System.currentTimeMillis());
                if (jedis.setnx(lockKey, lockValue) == 0) {
                    throw new ConflictException("Order matching lock already exists for " + itemId);
                }
                
                try {
                    // Execute matching operation
                    T result = matchingOperation.get();
                    
                    // Update matching timestamp
                    jedis.hset(matchKey, "last_match", String.valueOf(System.currentTimeMillis()));
                    
                    return result;
                    
                } finally {
                    // Release lock
                    jedis.del(lockKey);
                }
            }
        });
    }
    
    /**
     * Check if operation can be executed without conflicts
     */
    public boolean canExecuteOperation(String operationKey) {
        try (Jedis jedis = engine.getJedisPool().getResource()) {
            String lockKey = "rte:op_lock:" + operationKey;
            return jedis.setnx(lockKey, String.valueOf(System.currentTimeMillis())) == 1;
        }
    }
    
    /**
     * Release operation lock
     */
    public void releaseOperationLock(String operationKey) {
        try (Jedis jedis = engine.getJedisPool().getResource()) {
            String lockKey = "rte:op_lock:" + operationKey;
            jedis.del(lockKey);
        }
    }
    
    /**
     * Custom exception for transaction conflicts
     */
    public static class ConflictException extends RuntimeException {
        public ConflictException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception for insufficient funds
     */
    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception for insufficient items
     */
    public static class InsufficientItemsException extends RuntimeException {
        public InsufficientItemsException(String message) {
            super(message);
        }
    }
}
