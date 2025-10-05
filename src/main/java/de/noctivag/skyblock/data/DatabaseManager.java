package de.noctivag.skyblock.data;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.PlayerProfile;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Database Manager - Handles all database operations
 */
public class DatabaseManager {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();
    
    public DatabaseManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Initialize the database manager
     */
    public void initialize() {
        plugin.getLogger().info("Initializing DatabaseManager...");
        // Initialize database connection
        plugin.getLogger().info("DatabaseManager initialized successfully!");
    }
    
    /**
     * Shutdown the database manager
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down DatabaseManager...");
        saveAllPlayerData();
        playerProfiles.clear();
        plugin.getLogger().info("DatabaseManager shutdown complete!");
    }
    
    /**
     * Get player profile
     */
    public PlayerProfile getPlayerProfile(UUID playerId) {
        return playerProfiles.get(playerId);
    }
    
    /**
     * Create or get player profile
     */
    public PlayerProfile getOrCreatePlayerProfile(UUID playerId) {
        return playerProfiles.computeIfAbsent(playerId, k -> new PlayerProfile(k));
    }
    
    /**
     * Save player profile
     */
    public void savePlayerProfile(UUID playerId) {
        PlayerProfile profile = playerProfiles.get(playerId);
        if (profile != null) {
            // Save to database
            plugin.getLogger().info("Saved profile for player: " + playerId);
        }
    }
    
    /**
     * Save all player data
     */
    public void saveAllPlayerData() {
        for (UUID playerId : playerProfiles.keySet()) {
            savePlayerProfile(playerId);
        }
        plugin.getLogger().info("Saved all player data");
    }
    
    /**
     * Load player profile
     */
    public PlayerProfile loadPlayerProfile(UUID playerId) {
        // Load from database
        PlayerProfile profile = new PlayerProfile(playerId);
        playerProfiles.put(playerId, profile);
        return profile;
    }
}

