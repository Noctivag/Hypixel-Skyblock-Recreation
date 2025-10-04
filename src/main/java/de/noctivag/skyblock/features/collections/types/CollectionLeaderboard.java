package de.noctivag.skyblock.features.collections.types;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Collection Leaderboard
 */
public class CollectionLeaderboard {
    private final String collectionId;
    private final LeaderboardType type;
    private final List<LeaderboardEntry> entries;
    private final long lastUpdated;
    
    public CollectionLeaderboard(String collectionId, LeaderboardType type, 
                               List<LeaderboardEntry> entries, long lastUpdated) {
        this.collectionId = collectionId;
        this.type = type;
        this.entries = entries;
        this.lastUpdated = lastUpdated;
    }
    
    public String getCollectionId() { return collectionId; }
    public LeaderboardType getType() { return type; }
    public List<LeaderboardEntry> getEntries() { return entries; }
    public long getLastUpdated() { return lastUpdated; }
    
    public enum LeaderboardType {
        TOTAL_COLLECTED, WEEKLY_COLLECTED, MONTHLY_COLLECTED
    }
    
    public static class LeaderboardEntry {
        private final UUID playerId;
        private final String playerName;
        private final long amount;
        private final long timestamp;
        
        public LeaderboardEntry(UUID playerId, String playerName, long amount, long timestamp) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.amount = amount;
            this.timestamp = timestamp;
        }
        
        public UUID getPlayerId() { return playerId; }
        public String getPlayerName() { return playerName; }
        public long getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
}
