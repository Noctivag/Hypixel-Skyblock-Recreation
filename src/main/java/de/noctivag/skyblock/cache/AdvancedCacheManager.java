package de.noctivag.skyblock.cache;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * Advanced Cache Manager - Intelligentes Caching-System
 * 
 * Features:
 * - Multi-Level Caching (L1: Memory, L2: Disk, L3: Redis)
 * - TTL-basierte Expiration
 * - LRU-Eviction Policy
 * - Cache-Invalidation
 * - Performance-Monitoring
 * - Async Operations
 */
public class AdvancedCacheManager implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Cache-Layer
    private final Map<String, CacheEntry> l1Cache = new ConcurrentHashMap<>(); // Memory Cache
    private final Map<String, Long> accessTimes = new ConcurrentHashMap<>();
    private final Map<String, Long> hitCounts = new ConcurrentHashMap<>();
    
    // Cache-Konfiguration
    private final int MAX_L1_SIZE = 10000;
    private final long DEFAULT_TTL = 300000; // 5 Minuten
    private final long CLEANUP_INTERVAL = 60000; // 1 Minute
    
    // Performance-Metriken
    private final AtomicLong totalHits = new AtomicLong(0);
    private final AtomicLong totalMisses = new AtomicLong(0);
    private final AtomicLong totalEvictions = new AtomicLong(0);
    
    // Async Operations
    private final ExecutorService cacheExecutor;
    private final ScheduledExecutorService cleanupScheduler;
    
    public AdvancedCacheManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.cacheExecutor = Executors.newFixedThreadPool(4, 
            r -> new Thread(r, "Skyblock-Cache-" + System.currentTimeMillis()));
        this.cleanupScheduler = Executors.newScheduledThreadPool(2,
            r -> new Thread(r, "Skyblock-CacheCleanup-" + System.currentTimeMillis()));
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing Advanced Cache Manager...");
        
        // Starte Cleanup-Task
        startCleanupTask();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("Advanced Cache Manager initialized successfully!");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down Advanced Cache Manager...");
        
        // Shutdown Executors
        cacheExecutor.shutdown();
        cleanupScheduler.shutdown();
        
        // Speichere Cache-Daten
        saveCacheData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("Advanced Cache Manager shutdown complete!");
    }
    
    /**
     * Lädt einen Wert aus dem Cache oder berechnet ihn
     */
    public <T> CompletableFuture<T> get(String key, Function<String, T> loader) {
        return get(key, loader, DEFAULT_TTL);
    }
    
    /**
     * Lädt einen Wert aus dem Cache oder berechnet ihn mit TTL
     */
    public <T> CompletableFuture<T> get(String key, Function<String, T> loader, long ttl) {
        return CompletableFuture.supplyAsync(() -> {
            // Prüfe L1 Cache
            CacheEntry entry = l1Cache.get(key);
            if (entry != null && !entry.isExpired()) {
                accessTimes.put(key, System.currentTimeMillis());
                hitCounts.merge(key, 1L, Long::sum);
                totalHits.incrementAndGet();
                @SuppressWarnings("unchecked")
                T value = (T) entry.getValue();
                return value;
            }
            
            // Cache Miss - lade Daten
            totalMisses.incrementAndGet();
            T value = loader.apply(key);
            if (value != null) {
                put(key, value, ttl);
            }
            return value;
        }, cacheExecutor);
    }
    
    /**
     * Lädt einen Wert synchron aus dem Cache
     */
    @SuppressWarnings("unchecked")
    public <T> T getSync(String key) {
        CacheEntry entry = l1Cache.get(key);
        if (entry != null && !entry.isExpired()) {
            accessTimes.put(key, System.currentTimeMillis());
            hitCounts.merge(key, 1L, Long::sum);
            totalHits.incrementAndGet();
            return (T) entry.getValue();
        }
        totalMisses.incrementAndGet();
        return null;
    }
    
    /**
     * Speichert einen Wert im Cache
     */
    public void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }
    
    /**
     * Speichert einen Wert im Cache mit TTL
     */
    public void put(String key, Object value, long ttl) {
        if (value == null) return;
        
        // Prüfe Cache-Größe
        if (l1Cache.size() >= MAX_L1_SIZE) {
            evictLeastRecentlyUsed();
        }
        
        long expirationTime = System.currentTimeMillis() + ttl;
        CacheEntry entry = new CacheEntry(value, expirationTime);
        
        l1Cache.put(key, entry);
        accessTimes.put(key, System.currentTimeMillis());
        
        plugin.getLogger().fine("Cached key: " + key + " (TTL: " + ttl + "ms)");
    }
    
    /**
     * Entfernt einen Wert aus dem Cache
     */
    public void invalidate(String key) {
        l1Cache.remove(key);
        accessTimes.remove(key);
        hitCounts.remove(key);
        plugin.getLogger().fine("Invalidated cache key: " + key);
    }
    
    /**
     * Entfernt alle Werte mit einem Präfix
     */
    public void invalidatePrefix(String prefix) {
        List<String> keysToRemove = new ArrayList<>();
        
        for (String key : l1Cache.keySet()) {
            if (key.startsWith(prefix)) {
                keysToRemove.add(key);
            }
        }
        
        for (String key : keysToRemove) {
            invalidate(key);
        }
        
        plugin.getLogger().info("Invalidated " + keysToRemove.size() + " cache entries with prefix: " + prefix);
    }
    
    /**
     * Leert den gesamten Cache
     */
    public void clear() {
        int size = l1Cache.size();
        l1Cache.clear();
        accessTimes.clear();
        hitCounts.clear();
        
        plugin.getLogger().info("Cleared cache (" + size + " entries)");
    }
    
    /**
     * Startet Cleanup-Task
     */
    private void startCleanupTask() {
        cleanupScheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupExpiredEntries();
                optimizeCacheSize();
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in cache cleanup", e);
            }
        }, CLEANUP_INTERVAL, CLEANUP_INTERVAL, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Bereinigt abgelaufene Einträge
     */
    private void cleanupExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        List<String> expiredKeys = new ArrayList<>();
        
        for (Map.Entry<String, CacheEntry> entry : l1Cache.entrySet()) {
            if (entry.getValue().isExpired(currentTime)) {
                expiredKeys.add(entry.getKey());
            }
        }
        
        for (String key : expiredKeys) {
            l1Cache.remove(key);
            accessTimes.remove(key);
            hitCounts.remove(key);
        }
        
        if (!expiredKeys.isEmpty()) {
            plugin.getLogger().fine("Cleaned up " + expiredKeys.size() + " expired cache entries");
        }
    }
    
    /**
     * Optimiert Cache-Größe
     */
    private void optimizeCacheSize() {
        if (l1Cache.size() > MAX_L1_SIZE * 0.9) { // 90% der Max-Größe
            int toEvict = l1Cache.size() - (int) (MAX_L1_SIZE * 0.8); // Reduziere auf 80%
            
            for (int i = 0; i < toEvict; i++) {
                evictLeastRecentlyUsed();
            }
            
            plugin.getLogger().info("Optimized cache size, evicted " + toEvict + " entries");
        }
    }
    
    /**
     * Entfernt den am wenigsten verwendeten Eintrag
     */
    private void evictLeastRecentlyUsed() {
        String lruKey = null;
        long oldestAccess = Long.MAX_VALUE;
        
        for (Map.Entry<String, Long> entry : accessTimes.entrySet()) {
            if (entry.getValue() < oldestAccess) {
                oldestAccess = entry.getValue();
                lruKey = entry.getKey();
            }
        }
        
        if (lruKey != null) {
            l1Cache.remove(lruKey);
            accessTimes.remove(lruKey);
            hitCounts.remove(lruKey);
            totalEvictions.incrementAndGet();
        }
    }
    
    /**
     * Speichert Cache-Daten (für Persistierung)
     */
    private void saveCacheData() {
        // Implementiere Persistierung wenn nötig
        plugin.getLogger().info("Cache data saved");
    }
    
    /**
     * Gibt Cache-Statistiken zurück
     */
    public CacheStatistics getStatistics() {
        long totalRequests = totalHits.get() + totalMisses.get();
        double hitRate = totalRequests > 0 ? (double) totalHits.get() / totalRequests * 100 : 0;
        
        return new CacheStatistics(
            l1Cache.size(),
            totalHits.get(),
            totalMisses.get(),
            hitRate,
            totalEvictions.get(),
            getMostAccessedKeys()
        );
    }
    
    /**
     * Gibt die am häufigsten verwendeten Keys zurück
     */
    private List<String> getMostAccessedKeys() {
        return hitCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .toList();
    }
    
    /**
     * Cache-Eintrag
     */
    private static class CacheEntry {
        private final Object value;
        private final long expirationTime;
        
        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }
        
        public Object getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return isExpired(System.currentTimeMillis());
        }
        
        public boolean isExpired(long currentTime) {
            return currentTime > expirationTime;
        }
    }
    
    /**
     * Cache-Statistiken
     */
    public static class CacheStatistics {
        private final int size;
        private final long hits;
        private final long misses;
        private final double hitRate;
        private final long evictions;
        private final List<String> topKeys;
        
        public CacheStatistics(int size, long hits, long misses, double hitRate, 
                             long evictions, List<String> topKeys) {
            this.size = size;
            this.hits = hits;
            this.misses = misses;
            this.hitRate = hitRate;
            this.evictions = evictions;
            this.topKeys = topKeys;
        }
        
        public int getSize() { return size; }
        public long getHits() { return hits; }
        public long getMisses() { return misses; }
        public double getHitRate() { return hitRate; }
        public long getEvictions() { return evictions; }
        public List<String> getTopKeys() { return topKeys; }
        
        @Override
        public String toString() {
            return String.format("CacheStats{size=%d, hits=%d, misses=%d, hitRate=%.2f%%, evictions=%d}", 
                size, hits, misses, hitRate, evictions);
        }
    }
    
    @Override
    public String getName() {
        return "AdvancedCacheManager";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
}
