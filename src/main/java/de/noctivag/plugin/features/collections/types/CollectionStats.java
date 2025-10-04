package de.noctivag.plugin.features.collections.types;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * Collection Statistics
 */
public class CollectionStats {
    private final UUID playerId;
    private final String collectionId;
    private final long totalCollected;
    private final long weeklyCollected;
    private final long monthlyCollected;
    private final Map<String, Long> itemStats;
    private final long lastUpdated;
    
    public CollectionStats(UUID playerId, String collectionId, long totalCollected, 
                          long weeklyCollected, long monthlyCollected, 
                          Map<String, Long> itemStats, long lastUpdated) {
        this.playerId = playerId;
        this.collectionId = collectionId;
        this.totalCollected = totalCollected;
        this.weeklyCollected = weeklyCollected;
        this.monthlyCollected = monthlyCollected;
        this.itemStats = itemStats;
        this.lastUpdated = lastUpdated;
    }
    
    public UUID getPlayerId() { return playerId; }
    public String getCollectionId() { return collectionId; }
    public long getTotalCollected() { return totalCollected; }
    public long getWeeklyCollected() { return weeklyCollected; }
    public long getMonthlyCollected() { return monthlyCollected; }
    public Map<String, Long> getItemStats() { return itemStats; }
    public long getLastUpdated() { return lastUpdated; }
}
