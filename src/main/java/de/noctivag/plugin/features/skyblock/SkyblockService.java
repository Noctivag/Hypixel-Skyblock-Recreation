package de.noctivag.plugin.features.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.System;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Skyblock service interface for managing skyblock functionality.
 */
public interface SkyblockService extends System {
    
    /**
     * Create a new island for a player
     * @param playerId the player UUID
     * @return CompletableFuture that completes when island is created
     */
    CompletableFuture<Void> createIsland(UUID playerId);
    
    /**
     * Delete a player's island
     * @param playerId the player UUID
     * @return CompletableFuture that completes when island is deleted
     */
    CompletableFuture<Void> deleteIsland(UUID playerId);
    
    /**
     * Teleport player to their island
     * @param player the player
     * @return CompletableFuture that completes when teleportation is done
     */
    CompletableFuture<Void> teleportToIsland(Player player);
    
    /**
     * Reset a player's island
     * @param playerId the player UUID
     * @return CompletableFuture that completes when island is reset
     */
    CompletableFuture<Void> resetIsland(UUID playerId);
    
    /**
     * Get player's island data
     * @param playerId the player UUID
     * @return island data
     */
    IslandData getIslandData(UUID playerId);
    
    /**
     * Update player's island data
     * @param playerId the player UUID
     * @param islandData the island data
     */
    void updateIslandData(UUID playerId, IslandData islandData);
    
    /**
     * Check if player has an island
     * @param playerId the player UUID
     * @return true if has island, false otherwise
     */
    boolean hasIsland(UUID playerId);
    
    /**
     * Get player's skyblock level
     * @param playerId the player UUID
     * @return skyblock level
     */
    int getSkyblockLevel(UUID playerId);
    
    /**
     * Get player's skyblock coins
     * @param playerId the player UUID
     * @return coins amount
     */
    double getCoins(UUID playerId);
    
    /**
     * Set player's skyblock coins
     * @param playerId the player UUID
     * @param coins the coins amount
     */
    void setCoins(UUID playerId, double coins);
    
    /**
     * Add coins to player's balance
     * @param playerId the player UUID
     * @param amount the amount to add
     */
    void addCoins(UUID playerId, double amount);
    
    /**
     * Remove coins from player's balance
     * @param playerId the player UUID
     * @param amount the amount to remove
     * @return true if successful, false if insufficient funds
     */
    boolean removeCoins(UUID playerId, double amount);
    
    /**
     * Island data container
     */
    class IslandData {
        private final UUID ownerId;
        private final String worldName;
        private final double x, y, z;
        private final long createdAt;
        private final long lastAccessed;
        
        public IslandData(UUID ownerId, String worldName, double x, double y, double z) {
            this.ownerId = ownerId;
            this.worldName = worldName;
            this.x = x;
            this.y = y;
            this.z = z;
            this.createdAt = java.lang.System.currentTimeMillis();
            this.lastAccessed = java.lang.System.currentTimeMillis();
        }
        
        // Getters and setters
        public UUID getOwnerId() { return ownerId; }
        public String getWorldName() { return worldName; }
        public double getX() { return x; }
        public double getY() { return y; }
        public double getZ() { return z; }
        public long getCreatedAt() { return createdAt; }
        public long getLastAccessed() { return lastAccessed; }
    }
}
