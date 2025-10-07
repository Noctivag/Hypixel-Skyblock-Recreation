package de.noctivag.skyblock.cache;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.models.PlayerProfile;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Asynchroner Spielerdaten-Cache zur Performance-Optimierung
 * Eliminiert Datenbankzugriffe auf dem Haupt-Thread
 */
public class PlayerProfileCache {
    
    private final SkyblockPlugin plugin; // Main plugin instance
    private final ConcurrentHashMap<UUID, PlayerProfile> profileCache;
    private final ConcurrentHashMap<UUID, Long> cacheTimestamps;
    private final ScheduledExecutorService cleanupExecutor;

    private final int maxCacheSize;
    private final long cacheExpirationTime;

    public PlayerProfileCache(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.profileCache = new ConcurrentHashMap<>();
        this.cacheTimestamps = new ConcurrentHashMap<>();
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

        // Konfiguration aus Settings laden
        this.maxCacheSize = plugin.getSettingsConfig().getCacheSize();
        this.cacheExpirationTime = plugin.getSettingsConfig().getCacheExpirationTime();

        // Cache-Cleanup alle 5 Minuten
        startCacheCleanup();

        // Log initialization
        plugin.getLogger().info("PlayerProfileCache initialized - Max size: " + maxCacheSize +
                               ", Expiration: " + cacheExpirationTime + "ms");
    }
    
    /**
     * Startet den automatischen Cache-Cleanup
     */
    private void startCacheCleanup() {
        cleanupExecutor.scheduleAtFixedRate(() -> {
            try {
                cleanupExpiredEntries();
            } catch (Exception e) {
                plugin.getLogger().warning("Error during cache cleanup: " + e.getMessage());
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
    
    /**
     * Entfernt abgelaufene Cache-Einträge
     */
    private void cleanupExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        int removedCount = 0;
        
        for (UUID uuid : cacheTimestamps.keySet()) {
            Long timestamp = cacheTimestamps.get(uuid);
            if (timestamp != null && (currentTime - timestamp) > cacheExpirationTime) {
                profileCache.remove(uuid);
                cacheTimestamps.remove(uuid);
                removedCount++;
            }
        }
        
        if (removedCount > 0 && plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Cache cleanup: Removed " + removedCount + " expired entries");
        }
    }
    
    /**
     * Gibt ein Spielerprofil aus dem Cache zurück
     * @param uuid Spieler-UUID
     * @return PlayerProfile oder null wenn nicht im Cache
     */
    public PlayerProfile getProfile(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        
        Long timestamp = cacheTimestamps.get(uuid);
        if (timestamp != null && (System.currentTimeMillis() - timestamp) > cacheExpirationTime) {
            // Cache-Eintrag ist abgelaufen
            profileCache.remove(uuid);
            cacheTimestamps.remove(uuid);
            return null;
        }
        
        PlayerProfile profile = profileCache.get(uuid);
        if (profile != null && plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Cache hit for player: " + uuid);
        }
        
        return profile;
    }
    
    /**
     * Fügt ein Spielerprofil zum Cache hinzu
     * @param uuid Spieler-UUID
     * @param profile PlayerProfile
     */
    public void cacheProfile(UUID uuid, PlayerProfile profile) {
        if (uuid == null || profile == null) {
            return;
        }
        
        // Cache-Größe prüfen
        if (profileCache.size() >= maxCacheSize) {
            evictOldestEntry();
        }
        
        profileCache.put(uuid, profile);
        cacheTimestamps.put(uuid, System.currentTimeMillis());
        
        if (plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Cached profile for player: " + uuid + " (Cache size: " + profileCache.size() + ")");
        }
    }
    
    /**
     * Entfernt einen Spieler aus dem Cache
     * @param uuid Spieler-UUID
     */
    public void invalidate(UUID uuid) {
        if (uuid == null) {
            return;
        }
        
        PlayerProfile removed = profileCache.remove(uuid);
        cacheTimestamps.remove(uuid);
        
        if (removed != null && plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Invalidated cache for player: " + uuid);
        }
    }
    
    /**
     * Entfernt den ältesten Cache-Eintrag
     */
    private void evictOldestEntry() {
        UUID oldestUuid = null;
        long oldestTimestamp = Long.MAX_VALUE;
        
        for (UUID uuid : cacheTimestamps.keySet()) {
            Long timestamp = cacheTimestamps.get(uuid);
            if (timestamp != null && timestamp < oldestTimestamp) {
                oldestTimestamp = timestamp;
                oldestUuid = uuid;
            }
        }
        
        if (oldestUuid != null) {
            profileCache.remove(oldestUuid);
            cacheTimestamps.remove(oldestUuid);
            
            if (plugin.getSettingsConfig().isVerboseLogging()) {
                plugin.getLogger().info("Evicted oldest cache entry for player: " + oldestUuid);
            }
        }
    }
    
    /**
     * Leert den gesamten Cache
     */
    public void clearCache() {
        int size = profileCache.size();
        profileCache.clear();
        cacheTimestamps.clear();
        
        plugin.getLogger().info("Cache cleared - Removed " + size + " entries");
    }
    
    /**
     * Gibt die aktuelle Cache-Größe zurück
     * @return Anzahl der gecachten Profile
     */
    public int getCacheSize() {
        return profileCache.size();
    }
    
    /**
     * Gibt Cache-Statistiken zurück
     * @return Cache-Statistiken als String
     */
    public String getCacheStats() {
        return String.format("Cache Stats - Size: %d/%d, Expiration: %dms", 
                           profileCache.size(), maxCacheSize, cacheExpirationTime);
    }
    
    /**
     * Prüft ob ein Spieler im Cache ist
     * @param uuid Spieler-UUID
     * @return true wenn im Cache
     */
    public boolean isCached(UUID uuid) {
        if (uuid == null) {
            return false;
        }
        
        Long timestamp = cacheTimestamps.get(uuid);
        if (timestamp != null && (System.currentTimeMillis() - timestamp) > cacheExpirationTime) {
            // Cache-Eintrag ist abgelaufen
            profileCache.remove(uuid);
            cacheTimestamps.remove(uuid);
            return false;
        }
        
        return profileCache.containsKey(uuid);
    }
    
    /**
     * Schließt den Cache und alle zugehörigen Ressourcen
     */
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        clearCache();
        plugin.getLogger().info("PlayerProfileCache shutdown completed");
    }
}
