package de.noctivag.skyblock.features.dungeons.score;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.dungeons.catacombs.ScoreRating;

import java.util.UUID;

/**
 * Represents an entry in the dungeon leaderboard
 */
public class LeaderboardEntry {
    
    private final UUID playerId;
    private final int score;
    private final long timeSpent; // in seconds
    private final ScoreRating rating;
    private final long timestamp;
    
    public LeaderboardEntry(UUID playerId, int score, long timeSpent, ScoreRating rating, long timestamp) {
        this.playerId = playerId;
        this.score = score;
        this.timeSpent = timeSpent;
        this.rating = rating;
        this.timestamp = timestamp;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getScore() {
        return score;
    }
    
    public long getTimeSpent() {
        return timeSpent;
    }
    
    public ScoreRating getRating() {
        return rating;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Format time as MM:SS
     */
    public String getFormattedTime() {
        long minutes = timeSpent / 60;
        long seconds = timeSpent % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    
    @Override
    public String toString() {
        return String.format("LeaderboardEntry{player=%s, score=%d, time=%s, rating=%s}", 
            playerId, score, getFormattedTime(), rating);
    }
}
