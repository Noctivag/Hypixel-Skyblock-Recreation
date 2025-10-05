package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collections System - Manages player item collections
 */
public class CollectionsSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCollections> playerCollections = new ConcurrentHashMap<>();
    
    public CollectionsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    /**
     * Initialize player collections
     */
    public void initializePlayer(Player player) {
        UUID playerId = player.getUniqueId();
        
        PlayerCollections collections = playerCollections.computeIfAbsent(playerId, k -> 
            new PlayerCollections(playerId));
        
        plugin.getLogger().info("Initialized collections for player: " + player.getName());
    }
    
    /**
     * Get player collections
     */
    public PlayerCollections getPlayerCollections(UUID playerId) {
        return playerCollections.get(playerId);
    }
    
    /**
     * Add to collection
     */
    public void addToCollection(UUID playerId, String itemName, int amount) {
        PlayerCollections collections = playerCollections.get(playerId);
        if (collections != null) {
            collections.addItem(itemName, amount);
        }
    }
    
    /**
     * Get collection count
     */
    public int getCollectionCount(UUID playerId, String itemName) {
        PlayerCollections collections = playerCollections.get(playerId);
        if (collections != null) {
            return collections.getCount(itemName);
        }
        return 0;
    }
    
    /**
     * Player Collections data
     */
    public static class PlayerCollections {
        private final UUID playerId;
        private final Map<String, Integer> collections;
        
        public PlayerCollections(UUID playerId) {
            this.playerId = playerId;
            this.collections = new ConcurrentHashMap<>();
        }
        
        public void addItem(String itemName, int amount) {
            int currentCount = collections.getOrDefault(itemName, 0);
            collections.put(itemName, currentCount + amount);
        }
        
        public int getCount(String itemName) {
            return collections.getOrDefault(itemName, 0);
        }
        
        public Map<String, Integer> getAllCollections() {
            return new ConcurrentHashMap<>(collections);
        }
    }
}
