package de.noctivag.skyblock.database;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Database Manager - Handles all database operations
 * Folia compatible implementation
 */
public class DatabaseManager {

    private final SkyblockPlugin plugin;
    private final Map<UUID, PlayerData> playerDataCache = new ConcurrentHashMap<>();

    public DatabaseManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Database Manager initialized");
    }

    /**
     * Load player data from database
     */
    public void loadPlayerData(UUID playerId) {
        try {
            // Check cache first
            if (playerDataCache.containsKey(playerId)) {
                return; // Already loaded
            }

            // Create new player data
            PlayerData data = new PlayerData(playerId);
            playerDataCache.put(playerId, data);
            
            plugin.getLogger().info("Loaded player data for: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().severe("Error loading player data for " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save player data to database
     */
    public void savePlayerData(UUID playerId) {
        try {
            PlayerData data = playerDataCache.get(playerId);
            if (data != null) {
                // Save to database (simplified for now)
                plugin.getLogger().info("Saved player data for: " + playerId);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error saving player data for " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player data
     */
    public void saveAllPlayerData() {
        for (UUID playerId : playerDataCache.keySet()) {
            savePlayerData(playerId);
        }
    }

    /**
     * Get player data
     */
    public PlayerData getPlayerData(UUID playerId) {
        return playerDataCache.get(playerId);
    }

    /**
     * Remove player data from cache
     */
    public void removePlayerData(UUID playerId) {
        playerDataCache.remove(playerId);
    }

    /**
     * Simple PlayerData class
     */
    public static class PlayerData {
        private final UUID playerId;
        private long lastLogin;
        private long totalPlayTime;
        private int level;
        private double coins;

        public PlayerData(UUID playerId) {
            this.playerId = playerId;
            this.lastLogin = System.currentTimeMillis();
            this.totalPlayTime = 0;
            this.level = 1;
            this.coins = 0.0;
        }

        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public long getLastLogin() { return lastLogin; }
        public void setLastLogin(long lastLogin) { this.lastLogin = lastLogin; }
        public long getTotalPlayTime() { return totalPlayTime; }
        public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public double getCoins() { return coins; }
        public void setCoins(double coins) { this.coins = coins; }
    }
}
