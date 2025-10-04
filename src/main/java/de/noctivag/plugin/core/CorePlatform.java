package de.noctivag.plugin.core;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core Platform - Zentrale Plattform für alle Plugin-Systeme
 * 
 * Verantwortlich für:
 * - Player Profile Management
 * - Island Management
 * - Core System Integration
 * - Data Synchronization
 */
public class CorePlatform {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();
    private final Map<UUID, IslandManager> playerIslands = new ConcurrentHashMap<>();
    
    public CorePlatform(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    public PlayerProfile getPlayerProfile(UUID playerId) {
        return playerProfiles.computeIfAbsent(playerId, k -> new PlayerProfile(playerId));
    }
    
    public PlayerProfile getPlayerProfile(Player player) {
        return getPlayerProfile(player.getUniqueId());
    }
    
    public IslandManager getIslandManager(UUID playerId) {
        return playerIslands.computeIfAbsent(playerId, k -> new IslandManager(playerId, this));
    }
    
    public IslandManager getIslandManager(Player player) {
        return getIslandManager(player.getUniqueId());
    }
    
    public void savePlayerProfile(UUID playerId) {
        PlayerProfile profile = playerProfiles.get(playerId);
        if (profile != null) {
            databaseManager.savePlayerProfile(playerId, profile);
        }
    }
    
    public void loadPlayerProfile(UUID playerId) {
        databaseManager.loadPlayerProfileAsync(playerId).thenAccept(profile -> {
            if (profile != null && profile instanceof PlayerProfile) {
                playerProfiles.put(playerId, (PlayerProfile) profile);
            }
        });
    }
    
    public void saveAllProfiles() {
        for (Map.Entry<UUID, PlayerProfile> entry : playerProfiles.entrySet()) {
            savePlayerProfile(entry.getKey());
        }
    }
    
    public Map<UUID, PlayerProfile> getAllPlayerProfiles() {
        return new HashMap<>(playerProfiles);
    }
    
    public Map<UUID, IslandManager> getAllIslandManagers() {
        return new HashMap<>(playerIslands);
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    public MultiServerDatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
