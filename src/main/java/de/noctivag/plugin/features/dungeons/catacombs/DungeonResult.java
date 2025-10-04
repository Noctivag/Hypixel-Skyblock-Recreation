package de.noctivag.plugin.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Represents the result of a dungeon completion
 */
public class DungeonResult {
    
    private final UUID instanceId;
    private final List<UUID> players;
    private final int floor;
    private final boolean success;
    private final int score;
    private final long timeSpent; // in seconds
    private final List<UUID> participants;
    
    public DungeonResult(DungeonInstance instance, boolean success, int score) {
        this.instanceId = instance.getInstanceId();
        this.players = instance.getPlayers();
        this.floor = instance.getFloor();
        this.success = success;
        this.score = score;
        this.timeSpent = instance.getElapsedTime();
        this.participants = instance.getPlayers();
    }
    
    public UUID getInstanceId() {
        return instanceId;
    }
    
    public List<UUID> getPlayers() {
        return players;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public int getScore() {
        return score;
    }
    
    public long getTimeSpent() {
        return timeSpent;
    }
    
    public List<UUID> getParticipants() {
        return participants;
    }
    
    /**
     * Get score rating based on score
     */
    public ScoreRating getScoreRating() {
        if (score >= 400) return ScoreRating.S_PLUS;
        if (score >= 350) return ScoreRating.S;
        if (score >= 300) return ScoreRating.A_PLUS;
        if (score >= 250) return ScoreRating.A;
        if (score >= 200) return ScoreRating.B_PLUS;
        if (score >= 150) return ScoreRating.B;
        if (score >= 100) return ScoreRating.C_PLUS;
        if (score >= 50) return ScoreRating.C;
        return ScoreRating.D;
    }
    
    @Override
    public String toString() {
        return String.format("DungeonResult{floor=%d, success=%s, score=%d, time=%ds, rating=%s}", 
            floor, success, score, timeSpent, getScoreRating());
    }
}
