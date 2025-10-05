package de.noctivag.skyblock.slayers.quests;

import java.util.UUID;

/**
 * Slayer Quest - Represents a slayer quest
 */
public class SlayerQuest {
    
    private final UUID questId;
    private final UUID playerId;
    private final String slayerType;
    private final int tier;
    private final long startTime;
    private final long timeLimit;
    private boolean completed;
    private boolean active;
    
    public SlayerQuest(UUID questId, UUID playerId, String slayerType, int tier, long timeLimit) {
        this.questId = questId;
        this.playerId = playerId;
        this.slayerType = slayerType;
        this.tier = tier;
        this.startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.completed = false;
        this.active = true;
    }
    
    /**
     * Get the quest ID
     */
    public UUID getQuestId() {
        return questId;
    }
    
    /**
     * Get the player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Get the slayer type
     */
    public String getSlayerType() {
        return slayerType;
    }
    
    /**
     * Get the tier
     */
    public int getTier() {
        return tier;
    }
    
    /**
     * Get the start time
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Get the time limit
     */
    public long getTimeLimit() {
        return timeLimit;
    }
    
    /**
     * Check if the quest is completed
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Set the quest as completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    /**
     * Check if the quest is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set the quest as active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Check if the quest has expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > timeLimit;
    }
    
    /**
     * Get the remaining time in milliseconds
     */
    public long getRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, timeLimit - elapsed);
    }
}

