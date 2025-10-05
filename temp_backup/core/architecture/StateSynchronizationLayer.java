package de.noctivag.skyblock.core.architecture;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.exceptions.JedisException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * State Synchronization Layer - Centralized cache and state management
 * 
 * This layer provides:
 * - Redis cluster for high-frequency, latency-critical data
 * - Persistent database for long-term storage
 * - Transaction safety for economic operations
 * - Atomic operations and lock mechanisms
 * - Real-time player state synchronization
 * 
 * Key Features:
 * - Distributed Redis cluster with failover
 * - Transaction isolation for economic operations
 * - Player progression caching
 * - Bazaar price synchronization
 * - Server status monitoring
 */
public class StateSynchronizationLayer {
    
    private static final Logger logger = Logger.getLogger(StateSynchronizationLayer.class.getName());
    
    // Redis configuration
    private static final String REDIS_KEY_PREFIX = "hsb:";
    private static final String PLAYER_STATE_PREFIX = REDIS_KEY_PREFIX + "player:";
    private static final String BAZAAR_PRICES_PREFIX = REDIS_KEY_PREFIX + "bazaar:";
    private static final String SERVER_STATUS_PREFIX = REDIS_KEY_PREFIX + "server:";
    private static final String TRANSACTION_LOCK_PREFIX = REDIS_KEY_PREFIX + "lock:";
    
    // Redis cluster configuration
    private final Set<redis.clients.jedis.HostAndPort> redisNodes;
    private JedisCluster jedisCluster;
    private JedisPool jedisPool; // Fallback single instance
    
    // JSON serialization
    private final Gson gson;
    
    // Thread pool for async operations
    private final ExecutorService asyncExecutor;
    
    // Local cache for frequently accessed data
    private final Map<String, CachedData> localCache = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> transactionLocks = new ConcurrentHashMap<>();
    
    // Configuration
    private final StateConfig config;
    
    // State
    private volatile boolean initialized = false;
    private volatile boolean running = false;
    
    public StateSynchronizationLayer() {
        this.config = new StateConfig();
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        this.asyncExecutor = Executors.newFixedThreadPool(4, r -> new Thread(r, "StateSync-Thread"));
        
        // Default Redis nodes (can be configured)
        this.redisNodes = new HashSet<>(Arrays.asList(
            new redis.clients.jedis.HostAndPort("localhost", 7000),
            new redis.clients.jedis.HostAndPort("localhost", 7001),
            new redis.clients.jedis.HostAndPort("localhost", 7002)
        ));
    }
    
    /**
     * Initialize the state synchronization layer
     */
    public CompletableFuture<Void> initialize() {
        if (initialized) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Initializing State Synchronization Layer...");
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Initialize Redis cluster connection
                initializeRedisCluster();
                
                // Test connection
                testRedisConnection();
                
                // Start cache cleanup task
                startCacheCleanupTask();
                
                initialized = true;
                logger.info("State Synchronization Layer initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize State Synchronization Layer: " + e.getMessage());
                throw new RuntimeException("State layer initialization failed", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Start the state synchronization layer
     */
    public void start() {
        if (!initialized) {
            throw new IllegalStateException("State layer must be initialized before starting");
        }
        
        running = true;
        logger.info("State Synchronization Layer started");
    }
    
    /**
     * Stop the state synchronization layer
     */
    public void stop() {
        running = false;
        
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (Exception e) {
                logger.warning("Error closing Redis cluster: " + e.getMessage());
            }
        }
        
        if (jedisPool != null) {
            try {
                jedisPool.close();
            } catch (Exception e) {
                logger.warning("Error closing Redis pool: " + e.getMessage());
            }
        }
        
        asyncExecutor.shutdown();
        logger.info("State Synchronization Layer stopped");
    }
    
    /**
     * Get player progression data
     */
    public CompletableFuture<PlayerProgression> getPlayerProgression(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String cacheKey = PLAYER_STATE_PREFIX + playerId.toString() + ":progression";
                
                // Check local cache first
                CachedData cached = localCache.get(cacheKey);
                if (cached != null && !cached.isExpired()) {
                    return gson.fromJson(cached.getData(), PlayerProgression.class);
                }
                
                // Get from Redis
                String jsonData = jedisCluster.get(cacheKey);
                if (jsonData != null) {
                    PlayerProgression progression = gson.fromJson(jsonData, PlayerProgression.class);
                    
                    // Update local cache
                    localCache.put(cacheKey, new CachedData(jsonData, java.lang.System.currentTimeMillis() + 30000)); // 30s TTL
                    
                    return progression;
                }
                
                // Return default progression if not found
                return new PlayerProgression(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                
            } catch (Exception e) {
                logger.severe("Error getting player progression: " + e.getMessage());
                return new PlayerProgression(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            }
        }, asyncExecutor);
    }
    
    /**
     * Update player progression data
     */
    public CompletableFuture<Void> updatePlayerProgression(UUID playerId, PlayerProgression progression) {
        return CompletableFuture.runAsync(() -> {
            try {
                String cacheKey = PLAYER_STATE_PREFIX + playerId.toString() + ":progression";
                String jsonData = gson.toJson(progression);
                
                // Update Redis with TTL
                jedisCluster.setex(cacheKey, 3600, jsonData); // 1 hour TTL
                
                // Update local cache
                localCache.put(cacheKey, new CachedData(jsonData, java.lang.System.currentTimeMillis() + 30000));
                
                logger.fine("Updated player progression for: " + playerId);
                
            } catch (Exception e) {
                logger.severe("Error updating player progression: " + e.getMessage());
                throw new RuntimeException("Failed to update player progression", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Get bazaar prices
     */
    public CompletableFuture<Map<String, BazaarPrice>> getBazaarPrices() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String cacheKey = BAZAAR_PRICES_PREFIX + "all";
                
                // Check local cache first
                CachedData cached = localCache.get(cacheKey);
                if (cached != null && !cached.isExpired()) {
                    return gson.fromJson(cached.getData(), BazaarPriceMap.class).getPrices();
                }
                
                // Get from Redis
                String jsonData = jedisCluster.get(cacheKey);
                if (jsonData != null) {
                    BazaarPriceMap priceMap = gson.fromJson(jsonData, BazaarPriceMap.class);
                    
                    // Update local cache
                    localCache.put(cacheKey, new CachedData(jsonData, java.lang.System.currentTimeMillis() + 5000)); // 5s TTL
                    
                    return priceMap.getPrices();
                }
                
                return new HashMap<>();
                
            } catch (Exception e) {
                logger.severe("Error getting bazaar prices: " + e.getMessage());
                return new HashMap<>();
            }
        }, asyncExecutor);
    }
    
    /**
     * Update bazaar prices
     */
    public CompletableFuture<Void> updateBazaarPrices(Map<String, BazaarPrice> prices) {
        return CompletableFuture.runAsync(() -> {
            try {
                String cacheKey = BAZAAR_PRICES_PREFIX + "all";
                BazaarPriceMap priceMap = new BazaarPriceMap(prices);
                String jsonData = gson.toJson(priceMap);
                
                // Update Redis with short TTL for real-time data
                jedisCluster.setex(cacheKey, 60, jsonData); // 1 minute TTL
                
                // Update local cache
                localCache.put(cacheKey, new CachedData(jsonData, java.lang.System.currentTimeMillis() + 5000));
                
                logger.fine("Updated bazaar prices for " + prices.size() + " items");
                
            } catch (Exception e) {
                logger.severe("Error updating bazaar prices: " + e.getMessage());
                throw new RuntimeException("Failed to update bazaar prices", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Execute atomic transaction with Redis locks
     */
    public <T> CompletableFuture<T> executeAtomicTransaction(String transactionId, 
                                                           java.util.function.Supplier<T> operation) {
        return CompletableFuture.supplyAsync(() -> {
            String lockKey = TRANSACTION_LOCK_PREFIX + transactionId;
            ReentrantLock localLock = transactionLocks.computeIfAbsent(transactionId, k -> new ReentrantLock());
            
            try {
                // Acquire local lock
                localLock.lock();
                
                // Try to acquire distributed lock
                String lockValue = UUID.randomUUID().toString();
                String result = jedisCluster.set(lockKey, lockValue, SetParams.setParams().nx().ex(30));
                boolean acquired = "OK".equals(result);
                
                if (!acquired) {
                    throw new RuntimeException("Could not acquire distributed lock for transaction: " + transactionId);
                }
                
                try {
                    // Execute operation
                    return operation.get();
                } finally {
                    // Release distributed lock
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                                   "return redis.call('del', KEYS[1]) else return 0 end";
                    jedisCluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
                }
                
            } catch (Exception e) {
                logger.severe("Error in atomic transaction: " + e.getMessage());
                throw new RuntimeException("Atomic transaction failed", e);
            } finally {
                localLock.unlock();
            }
        }, asyncExecutor);
    }
    
    /**
     * Update server status
     */
    public CompletableFuture<Void> updateServerStatus(String serverId, ServerStatus status) {
        return CompletableFuture.runAsync(() -> {
            try {
                String cacheKey = SERVER_STATUS_PREFIX + serverId;
                String jsonData = gson.toJson(status);
                
                // Update Redis with heartbeat TTL
                jedisCluster.setex(cacheKey, 30, jsonData); // 30 seconds TTL
                
                logger.fine("Updated server status for: " + serverId);
                
            } catch (Exception e) {
                logger.severe("Error updating server status: " + e.getMessage());
            }
        }, asyncExecutor);
    }
    
    /**
     * Get all active server statuses
     */
    public CompletableFuture<Map<String, ServerStatus>> getAllServerStatuses() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, ServerStatus> statuses = new HashMap<>();
                
                // Get all server status keys
                Set<String> keys = jedisCluster.keys(SERVER_STATUS_PREFIX + "*");
                
                for (String key : keys) {
                    String jsonData = jedisCluster.get(key);
                    if (jsonData != null) {
                        ServerStatus status = gson.fromJson(jsonData, ServerStatus.class);
                        String serverId = key.substring(SERVER_STATUS_PREFIX.length());
                        statuses.put(serverId, status);
                    }
                }
                
                return statuses;
                
            } catch (Exception e) {
                logger.severe("Error getting server statuses: " + e.getMessage());
                return new HashMap<>();
            }
        }, asyncExecutor);
    }
    
    /**
     * Clean up player state when player disconnects
     */
    public void cleanupPlayerState(UUID playerId) {
        CompletableFuture.runAsync(() -> {
            try {
                // Remove player data from local cache
                String cacheKey = PLAYER_STATE_PREFIX + playerId.toString() + ":progression";
                localCache.remove(cacheKey);
                
                logger.fine("Cleaned up player state for: " + playerId);
                
            } catch (Exception e) {
                logger.warning("Error cleaning up player state: " + e.getMessage());
            }
        }, asyncExecutor);
    }
    
    /**
     * Initialize Redis cluster connection
     */
    private void initializeRedisCluster() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(100);
            poolConfig.setMaxIdle(50);
            poolConfig.setMinIdle(10);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            
            // Try cluster connection first
            Set<redis.clients.jedis.HostAndPort> jedisNodes = new HashSet<>();
            for (redis.clients.jedis.HostAndPort node : redisNodes) {
                jedisNodes.add(new redis.clients.jedis.HostAndPort(node.getHost(), node.getPort()));
            }
            
            jedisCluster = new JedisCluster(jedisNodes);
            
            logger.info("Connected to Redis cluster with " + redisNodes.size() + " nodes");
            
        } catch (Exception e) {
            logger.warning("Failed to connect to Redis cluster, falling back to single instance: " + e.getMessage());
            
            // Fallback to single Redis instance
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(50);
            poolConfig.setMaxIdle(25);
            poolConfig.setMinIdle(5);
            
            jedisPool = new JedisPool(poolConfig, "localhost", 6379);
            logger.info("Connected to single Redis instance");
        }
    }
    
    /**
     * Test Redis connection
     */
    private void testRedisConnection() {
        try {
            if (jedisCluster != null) {
                jedisCluster.set("test:connection", "ok");
                String result = jedisCluster.get("test:connection");
                jedisCluster.del("test:connection");
                
                if (!"ok".equals(result)) {
                    throw new RuntimeException("Redis cluster test failed");
                }
                
                logger.info("Redis cluster connection test successful");
            } else if (jedisPool != null) {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.set("test:connection", "ok");
                    String result = jedis.get("test:connection");
                    jedis.del("test:connection");
                    
                    if (!"ok".equals(result)) {
                        throw new RuntimeException("Redis single instance test failed");
                    }
                    
                    logger.info("Redis single instance connection test successful");
                }
            } else {
                throw new RuntimeException("No Redis connection available");
            }
        } catch (Exception e) {
            logger.severe("Redis connection test failed: " + e.getMessage());
            throw new RuntimeException("Redis connection test failed", e);
        }
    }
    
    /**
     * Start cache cleanup task
     */
    private void startCacheCleanupTask() {
        Timer timer = new Timer("CacheCleanup", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    localCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
                } catch (Exception e) {
                    logger.warning("Error during cache cleanup: " + e.getMessage());
                }
            }
        }, 60000, 60000); // Run every minute
    }
    
    /**
     * Host and port configuration
     */
    public static class HostAndPort {
        private final String host;
        private final int port;
        
        public HostAndPort(String host, int port) {
            this.host = host;
            this.port = port;
        }
        
        public String getHost() { return host; }
        public int getPort() { return port; }
    }
    
    /**
     * Cached data wrapper
     */
    private static class CachedData {
        private final String data;
        private final long expirationTime;
        
        public CachedData(String data, long expirationTime) {
            this.data = data;
            this.expirationTime = expirationTime;
        }
        
        public String getData() { return data; }
        public boolean isExpired() { return java.lang.System.currentTimeMillis() > expirationTime; }
    }
    
    /**
     * State configuration
     */
    public static class StateConfig {
        private int localCacheSize = 1000;
        private long defaultTTL = 300000; // 5 minutes
        
        public int getLocalCacheSize() { return localCacheSize; }
        public long getDefaultTTL() { return defaultTTL; }
    }
    
    /**
     * Player progression data structure
     */
    public static class PlayerProgression {
        private final int combatLevel;
        private final int miningLevel;
        private final int farmingLevel;
        private final int foragingLevel;
        private final int fishingLevel;
        private final int enchantingLevel;
        private final int alchemyLevel;
        private final int tamingLevel;
        private final int carpentryLevel;
        private final int runecraftingLevel;
        
        public PlayerProgression(int combatLevel, int miningLevel, int farmingLevel, 
                               int foragingLevel, int fishingLevel, int enchantingLevel,
                               int alchemyLevel, int tamingLevel, int carpentryLevel, 
                               int runecraftingLevel) {
            this.combatLevel = combatLevel;
            this.miningLevel = miningLevel;
            this.farmingLevel = farmingLevel;
            this.foragingLevel = foragingLevel;
            this.fishingLevel = fishingLevel;
            this.enchantingLevel = enchantingLevel;
            this.alchemyLevel = alchemyLevel;
            this.tamingLevel = tamingLevel;
            this.carpentryLevel = carpentryLevel;
            this.runecraftingLevel = runecraftingLevel;
        }
        
        // Getters
        public int getCombatLevel() { return combatLevel; }
        public int getMiningLevel() { return miningLevel; }
        public int getFarmingLevel() { return farmingLevel; }
        public int getForagingLevel() { return foragingLevel; }
        public int getFishingLevel() { return fishingLevel; }
        public int getEnchantingLevel() { return enchantingLevel; }
        public int getAlchemyLevel() { return alchemyLevel; }
        public int getTamingLevel() { return tamingLevel; }
        public int getCarpentryLevel() { return carpentryLevel; }
        public int getRunecraftingLevel() { return runecraftingLevel; }
        
        public int getHighestSkillLevel() {
            return Math.max(Math.max(Math.max(combatLevel, miningLevel), 
                Math.max(farmingLevel, foragingLevel)), 
                Math.max(Math.max(fishingLevel, enchantingLevel), 
                Math.max(Math.max(alchemyLevel, tamingLevel), 
                Math.max(carpentryLevel, runecraftingLevel))));
        }
        
        public boolean isEndgamePlayer() {
            return getHighestSkillLevel() >= 50;
        }
        
        public boolean isBeginner() {
            return getHighestSkillLevel() < 10;
        }
    }
    
    /**
     * Bazaar price data structure
     */
    public static class BazaarPrice {
        private final String itemId;
        private final double buyPrice;
        private final double sellPrice;
        private final long timestamp;
        
        public BazaarPrice(String itemId, double buyPrice, double sellPrice, long timestamp) {
            this.itemId = itemId;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.timestamp = timestamp;
        }
        
        public String getItemId() { return itemId; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Bazaar price map wrapper
     */
    private static class BazaarPriceMap {
        private final Map<String, BazaarPrice> prices;
        
        public BazaarPriceMap(Map<String, BazaarPrice> prices) {
            this.prices = prices;
        }
        
        public Map<String, BazaarPrice> getPrices() { return prices; }
    }
    
    /**
     * Server status data structure
     */
    public static class ServerStatus {
        private final String serverId;
        private final int playerCount;
        private final double tps;
        private final long timestamp;
        private final String status;
        
        public ServerStatus(String serverId, int playerCount, double tps, String status) {
            this.serverId = serverId;
            this.playerCount = playerCount;
            this.tps = tps;
            this.timestamp = java.lang.System.currentTimeMillis();
            this.status = status;
        }
        
        public String getServerId() { return serverId; }
        public int getPlayerCount() { return playerCount; }
        public double getTps() { return tps; }
        public long getTimestamp() { return timestamp; }
        public String getStatus() { return status; }
    }
}
