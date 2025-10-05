package de.noctivag.skyblock.data;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Cache Manager - Advanced Caching System
 * 
 * Verantwortlich für:
 * - Data Caching
 * - Cache Invalidation
 * - Memory Management
 * - Performance Optimization
 * - Cache Statistics
 */
public class CacheManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> accessTimes = new ConcurrentHashMap<>();
    private final Map<String, Integer> accessCounts = new ConcurrentHashMap<>();
    
    // Cache Configuration
    private final long DEFAULT_TTL = TimeUnit.MINUTES.toMillis(5); // 5 minutes
    private final int MAX_CACHE_SIZE = 10000;
    private final long CLEANUP_INTERVAL = TimeUnit.MINUTES.toMillis(1); // 1 minute
    private final boolean ENABLE_STATISTICS = true;
    
    // Statistics
    private long totalHits = 0;
    private long totalMisses = 0;
    private long totalEvictions = 0;
    
    public CacheManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        startCleanupTask();
    }
    
    private void startCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupExpiredEntries();
                evictLeastRecentlyUsed();
            }
        }.runTaskTimerAsynchronously(SkyblockPlugin, 0L, CLEANUP_INTERVAL / 50L); // Convert to ticks
    }
    
    public void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }
    
    public void put(String key, Object value, long ttl) {
        long expiryTime = java.lang.System.currentTimeMillis() + ttl;
        cache.put(key, new CacheEntry(value, expiryTime));
        accessTimes.put(key, java.lang.System.currentTimeMillis());
        accessCounts.put(key, 0);
        
        // Check if we need to evict entries
        if (cache.size() > MAX_CACHE_SIZE) {
            evictLeastRecentlyUsed();
        }
    }
    
    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            totalMisses++;
            return null;
        }
        
        // Check if entry has expired
        if (entry.isExpired()) {
            cache.remove(key);
            accessTimes.remove(key);
            accessCounts.remove(key);
            totalMisses++;
            return null;
        }
        
        // Update access statistics
        accessTimes.put(key, java.lang.System.currentTimeMillis());
        accessCounts.put(key, accessCounts.getOrDefault(key, 0) + 1);
        totalHits++;
        
        return entry.getValue();
    }
    
    public boolean contains(String key) {
        return get(key) != null;
    }
    
    public void remove(String key) {
        cache.remove(key);
        accessTimes.remove(key);
        accessCounts.remove(key);
    }
    
    public void clear() {
        cache.clear();
        accessTimes.clear();
        accessCounts.clear();
    }
    
    public int size() {
        return cache.size();
    }
    
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    private void cleanupExpiredEntries() {
        long currentTime = java.lang.System.currentTimeMillis();
        List<String> expiredKeys = new ArrayList<>();
        
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredKeys.add(entry.getKey());
            }
        }
        
        for (String key : expiredKeys) {
            cache.remove(key);
            accessTimes.remove(key);
            accessCounts.remove(key);
        }
        
        if (!expiredKeys.isEmpty()) {
            SkyblockPlugin.getLogger().info("Cleaned up " + expiredKeys.size() + " expired cache entries");
        }
    }
    
    private void evictLeastRecentlyUsed() {
        if (cache.size() <= MAX_CACHE_SIZE) {
            return;
        }
        
        int entriesToEvict = cache.size() - MAX_CACHE_SIZE + 100; // Evict 100 extra entries
        
        // Sort by access time (least recently used first)
        List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(accessTimes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());
        
        for (int i = 0; i < Math.min(entriesToEvict, sortedEntries.size()); i++) {
            String key = sortedEntries.get(i).getKey();
            cache.remove(key);
            accessTimes.remove(key);
            accessCounts.remove(key);
            totalEvictions++;
        }
        
        SkyblockPlugin.getLogger().info("Evicted " + entriesToEvict + " cache entries (LRU)");
    }
    
    public CacheStatistics getStatistics() {
        return new CacheStatistics(
            totalHits,
            totalMisses,
            totalEvictions,
            cache.size(),
            MAX_CACHE_SIZE,
            calculateHitRate()
        );
    }
    
    private double calculateHitRate() {
        long totalRequests = totalHits + totalMisses;
        if (totalRequests == 0) return 0.0;
        return (double) totalHits / totalRequests * 100.0;
    }
    
    public Map<String, Object> getAllEntries() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (!entry.getValue().isExpired()) {
                result.put(entry.getKey(), entry.getValue().getValue());
            }
        }
        return result;
    }
    
    public void invalidatePattern(String pattern) {
        List<String> keysToRemove = new ArrayList<>();
        for (String key : cache.keySet()) {
            if (key.matches(pattern)) {
                keysToRemove.add(key);
            }
        }
        
        for (String key : keysToRemove) {
            remove(key);
        }
        
        SkyblockPlugin.getLogger().info("Invalidated " + keysToRemove.size() + " cache entries matching pattern: " + pattern);
    }
    
    public void warmupCache(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        SkyblockPlugin.getLogger().info("Warmed up cache with " + data.size() + " entries");
    }
    
    // Cache Entry Class
    private static class CacheEntry {
        private final Object value;
        private final long expiryTime;
        
        public CacheEntry(Object value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
        
        public Object getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return java.lang.System.currentTimeMillis() > expiryTime;
        }
        
        public long getExpiryTime() {
            return expiryTime;
        }
    }
    
    // Cache Statistics Class
    public static class CacheStatistics {
        private final long totalHits;
        private final long totalMisses;
        private final long totalEvictions;
        private final int currentSize;
        private final int maxSize;
        private final double hitRate;
        
        public CacheStatistics(long totalHits, long totalMisses, long totalEvictions, 
                             int currentSize, int maxSize, double hitRate) {
            this.totalHits = totalHits;
            this.totalMisses = totalMisses;
            this.totalEvictions = totalEvictions;
            this.currentSize = currentSize;
            this.maxSize = maxSize;
            this.hitRate = hitRate;
        }
        
        public long getTotalHits() { return totalHits; }
        public long getTotalMisses() { return totalMisses; }
        public long getTotalEvictions() { return totalEvictions; }
        public int getCurrentSize() { return currentSize; }
        public int getMaxSize() { return maxSize; }
        public double getHitRate() { return hitRate; }
        
        @Override
        public String toString() {
            return String.format(
                "Cache Statistics: Hits=%d, Misses=%d, Evictions=%d, Size=%d/%d, Hit Rate=%.2f%%",
                totalHits, totalMisses, totalEvictions, currentSize, maxSize, hitRate
            );
        }
    }
}
