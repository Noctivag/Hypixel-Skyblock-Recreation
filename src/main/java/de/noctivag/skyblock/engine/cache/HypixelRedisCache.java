package de.noctivag.skyblock.engine.cache;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.progression.types.HypixelSkillType;
import de.noctivag.skyblock.engine.collections.types.CollectionType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Hypixel Redis Cache System
 * 
 * Integrates skill data and collection progress with Redis caching
 * for the GIM (Guild Island Management) system. Provides fast access
 * to player progression data across multiple servers.
 */
public class HypixelRedisCache implements Service {
    
    private JedisPool jedisPool;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    // Cache configuration
    private static final String SKILL_DATA_PREFIX = "skill_data:";
    private static final String COLLECTION_DATA_PREFIX = "collection_data:";
    private static final String PLAYER_PROGRESS_PREFIX = "player_progress:";
    private static final int CACHE_TTL = 3600; // 1 hour
    
    public HypixelRedisCache() {
        // Initialize Redis connection
        initializeRedisConnection();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Test Redis connection
            testRedisConnection();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Close Redis connection
            if (jedisPool != null) {
                jedisPool.close();
            }
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "HypixelRedisCache";
    }
    
    /**
     * Cache player skill data
     */
    public CompletableFuture<Boolean> cacheSkillData(UUID playerId, HypixelSkillType skillType, int level, double experience) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = SKILL_DATA_PREFIX + playerId + ":" + skillType.name();
                
                Map<String, String> data = new HashMap<>();
                data.put("level", String.valueOf(level));
                data.put("experience", String.valueOf(experience));
                data.put("timestamp", String.valueOf(System.currentTimeMillis()));
                
                jedis.hset(key, data);
                jedis.expire(key, CACHE_TTL);
                
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    /**
     * Get cached skill data
     */
    public CompletableFuture<Map<String, String>> getCachedSkillData(UUID playerId, HypixelSkillType skillType) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = SKILL_DATA_PREFIX + playerId + ":" + skillType.name();
                return jedis.hgetAll(key);
            } catch (Exception e) {
                return new HashMap<>();
            }
        });
    }
    
    /**
     * Cache player collection data
     */
    public CompletableFuture<Boolean> cacheCollectionData(UUID playerId, CollectionType collectionType, int progress) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = COLLECTION_DATA_PREFIX + playerId + ":" + collectionType.name();
                
                Map<String, String> data = new HashMap<>();
                data.put("progress", String.valueOf(progress));
                data.put("timestamp", String.valueOf(System.currentTimeMillis()));
                
                jedis.hset(key, data);
                jedis.expire(key, CACHE_TTL);
                
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    /**
     * Get cached collection data
     */
    public CompletableFuture<Map<String, String>> getCachedCollectionData(UUID playerId, CollectionType collectionType) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = COLLECTION_DATA_PREFIX + playerId + ":" + collectionType.name();
                return jedis.hgetAll(key);
            } catch (Exception e) {
                return new HashMap<>();
            }
        });
    }
    
    /**
     * Cache player progress summary
     */
    public CompletableFuture<Boolean> cachePlayerProgress(UUID playerId, Map<String, Object> progressData) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = PLAYER_PROGRESS_PREFIX + playerId;
                
                Map<String, String> data = new HashMap<>();
                for (Map.Entry<String, Object> entry : progressData.entrySet()) {
                    data.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
                data.put("timestamp", String.valueOf(System.currentTimeMillis()));
                
                jedis.hset(key, data);
                jedis.expire(key, CACHE_TTL);
                
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    /**
     * Get cached player progress
     */
    public CompletableFuture<Map<String, String>> getCachedPlayerProgress(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = PLAYER_PROGRESS_PREFIX + playerId;
                return jedis.hgetAll(key);
            } catch (Exception e) {
                return new HashMap<>();
            }
        });
    }
    
    /**
     * Initialize Redis connection
     */
    private void initializeRedisConnection() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(20);
        config.setMinIdle(5);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        
        jedisPool = new JedisPool(config, "localhost", 6379);
    }
    
    /**
     * Test Redis connection
     */
    private void testRedisConnection() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Redis", e);
        }
    }
}
