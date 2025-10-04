package de.noctivag.plugin.experiments;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Represents an active experiment
 */
public class ActiveExperiment {
    
    private final String experimentType;
    private final UUID playerId;
    private final long startTime;
    private final int durationMinutes;
    
    public ActiveExperiment(String experimentType, UUID playerId, int durationMinutes) {
        this.experimentType = experimentType;
        this.playerId = playerId;
        this.startTime = System.currentTimeMillis();
        this.durationMinutes = durationMinutes;
    }
    
    public String getExperimentType() {
        return experimentType;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public boolean isCompleted() {
        long elapsed = System.currentTimeMillis() - startTime;
        return elapsed >= (durationMinutes * 60 * 1000L);
    }
    
    public long getRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        long total = durationMinutes * 60 * 1000L;
        return Math.max(0, total - elapsed);
    }
    
    public double getProgress() {
        long elapsed = System.currentTimeMillis() - startTime;
        long total = durationMinutes * 60 * 1000L;
        return Math.min(1.0, (double) elapsed / total);
    }
    
    public void updateProgress() {
        // Update experiment progress
        // This could include checking for completion, applying effects, etc.
    }
    
    @Override
    public String toString() {
        return "ActiveExperiment{" +
                "experimentType='" + experimentType + '\'' +
                ", playerId=" + playerId +
                ", startTime=" + startTime +
                ", durationMinutes=" + durationMinutes +
                ", completed=" + isCompleted() +
                '}';
    }
}
