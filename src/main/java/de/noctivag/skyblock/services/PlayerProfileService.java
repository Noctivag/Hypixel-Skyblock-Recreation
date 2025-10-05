package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.cache.PlayerProfileCache;
import de.noctivag.skyblock.models.PlayerProfile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Asynchroner Service für Spielerprofil-Verwaltung
 * Eliminiert Datenbankzugriffe auf dem Haupt-Thread
 */
public class PlayerProfileService {
    
    private final SkyblockPlugin plugin;
    private final PlayerProfileCache cache;
    private final ExecutorService executorService;
    
    public PlayerProfileService(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.cache = new PlayerProfileCache(plugin);
        this.executorService = Executors.newFixedThreadPool(4);
        
        plugin.getLogger().info("PlayerProfileService initialized with async loading enabled");
    }
    
    /**
     * Lädt ein Spielerprofil asynchron
     * @param uuid Spieler-UUID
     * @return CompletableFuture mit PlayerProfile
     */
    public CompletableFuture<PlayerProfile> loadProfile(UUID uuid) {
        if (uuid == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        // Prüfe zuerst den Cache
        PlayerProfile cachedProfile = cache.getProfile(uuid);
        if (cachedProfile != null) {
            return CompletableFuture.completedFuture(cachedProfile);
        }
        
        // Lade asynchron aus der Datenbank
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Loading profile for player: " + uuid + " from database");
                }
                
                // Simuliere Datenbankabfrage (hier würde die echte DB-Logik stehen)
                PlayerProfile profile = loadProfileFromDatabase(uuid);
                
                if (profile != null) {
                    // Füge zum Cache hinzu
                    cache.cacheProfile(uuid, profile);
                    
                    if (plugin.getSettingsConfig().isVerboseLogging()) {
                        plugin.getLogger().info("Successfully loaded and cached profile for player: " + uuid);
                    }
                } else {
                    if (plugin.getSettingsConfig().isVerboseLogging()) {
                        plugin.getLogger().warning("Failed to load profile for player: " + uuid);
                    }
                }
                
                return profile;
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error loading profile for player " + uuid + ": " + e.getMessage());
                return null;
            }
        }, executorService);
    }
    
    /**
     * Speichert ein Spielerprofil asynchron
     * @param profile PlayerProfile
     * @return CompletableFuture
     */
    public CompletableFuture<Void> saveProfile(PlayerProfile profile) {
        if (profile == null || profile.getUuid() == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        return CompletableFuture.runAsync(() -> {
            try {
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Saving profile for player: " + profile.getUuid());
                }
                
                // Speichere in der Datenbank
                saveProfileToDatabase(profile);
                
                // Aktualisiere den Cache
                cache.cacheProfile(profile.getUuid(), profile);
                
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Successfully saved profile for player: " + profile.getUuid());
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error saving profile for player " + profile.getUuid() + ": " + e.getMessage());
            }
        }, executorService);
    }
    
    /**
     * Lädt ein Profil aus der Datenbank (simuliert)
     * @param uuid Spieler-UUID
     * @return PlayerProfile oder null
     */
    private PlayerProfile loadProfileFromDatabase(UUID uuid) {
        // Simuliere Datenbankabfrage
        try {
            Thread.sleep(50); // Simuliere DB-Latenz
            
            // Erstelle ein neues Profil (hier würde die echte DB-Logik stehen)
            PlayerProfile profile = new PlayerProfile(uuid);
            profile.setLastLogin(System.currentTimeMillis());
            
            return profile;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            plugin.getLogger().severe("Database error loading profile for " + uuid + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Speichert ein Profil in der Datenbank (simuliert)
     * @param profile PlayerProfile
     */
    private void saveProfileToDatabase(PlayerProfile profile) {
        // Simuliere Datenbankspeicherung
        try {
            Thread.sleep(30); // Simuliere DB-Latenz
            
            // Hier würde die echte DB-Speicher-Logik stehen
            profile.setLastSave(System.currentTimeMillis());
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            plugin.getLogger().severe("Database error saving profile for " + profile.getUuid() + ": " + e.getMessage());
        }
    }
    
    /**
     * Entfernt ein Spielerprofil aus dem Cache
     * @param uuid Spieler-UUID
     */
    public void invalidateProfile(UUID uuid) {
        cache.invalidate(uuid);
    }
    
    /**
     * Prüft ob ein Profil im Cache ist
     * @param uuid Spieler-UUID
     * @return true wenn im Cache
     */
    public boolean isProfileCached(UUID uuid) {
        return cache.isCached(uuid);
    }
    
    /**
     * Gibt Cache-Statistiken zurück
     * @return Cache-Statistiken
     */
    public String getCacheStats() {
        return cache.getCacheStats();
    }
    
    /**
     * Leert den Cache
     */
    public void clearCache() {
        cache.clearCache();
    }
    
    /**
     * Schließt den Service und alle zugehörigen Ressourcen
     */
    public void shutdown() {
        executorService.shutdown();
        cache.shutdown();
        
        plugin.getLogger().info("PlayerProfileService shutdown completed");
    }
}
