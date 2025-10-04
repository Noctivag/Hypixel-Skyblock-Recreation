package de.noctivag.plugin.slayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Slayer Quest - Represents an active slayer quest for a player
 */
public class SlayerQuest {
    
    private final UUID playerId;
    private final SlayerSystem.SlayerType type;
    private final SlayerSystem.SlayerTier tier;
    private final long startTime;
    private final long timeLimit;
    private int kills;
    private boolean completed;
    private boolean bossSpawned;
    
    public SlayerQuest(UUID playerId, SlayerSystem.SlayerType type, SlayerSystem.SlayerTier tier) {
        this.playerId = playerId;
        this.type = type;
        this.tier = tier;
        this.startTime = System.currentTimeMillis();
        this.timeLimit = tier.getTimeLimit() * 60 * 1000L; // Convert minutes to milliseconds
        this.kills = 0;
        this.completed = false;
        this.bossSpawned = false;
    }
    
    /**
     * Add a kill to the quest
     */
    public void addKill() {
        if (!completed && !bossSpawned) {
            kills++;
        }
    }
    
    /**
     * Check if quest is ready for boss spawn
     */
    public boolean isReadyForBoss() {
        return !completed && !bossSpawned && kills >= tier.getRequiredKills();
    }
    
    /**
     * Mark boss as spawned
     */
    public void markBossSpawned() {
        this.bossSpawned = true;
    }
    
    /**
     * Complete the quest
     */
    public void complete() {
        this.completed = true;
    }
    
    /**
     * Check if quest is expired
     */
    public boolean isExpired() {
        return !completed && (System.currentTimeMillis() - startTime) > timeLimit;
    }
    
    /**
     * Check if target entity is valid for this quest
     */
    public boolean isValidTarget(Entity entity) {
        // Check if entity type matches slayer type
        return switch (type) {
            case ZOMBIE, VAMPIRE -> entity.getType().name().contains("ZOMBIE");
            case SPIDER -> entity.getType().name().contains("SPIDER");
            case WOLF -> entity.getType().name().contains("WOLF");
            case ENDERMAN -> entity.getType().name().contains("ENDERMAN");
            case BLAZE -> entity.getType().name().contains("BLAZE");
        };
    }
    
    /**
     * Update quest state
     */
    public void update() {
        // Quest update logic
    }
    
    // Getters
    public UUID getPlayerId() { return playerId; }
    public SlayerSystem.SlayerType getType() { return type; }
    public SlayerSystem.SlayerTier getTier() { return tier; }
    public long getStartTime() { return startTime; }
    public long getTimeLimit() { return timeLimit; }
    public int getKills() { return kills; }
    public boolean isCompleted() { return completed; }
    public boolean isBossSpawned() { return bossSpawned; }
    
    /**
     * Get remaining time in milliseconds
     */
    public long getRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, timeLimit - elapsed);
    }
    
    /**
     * Get progress percentage
     */
    public double getProgress() {
        return Math.min(1.0, (double) kills / tier.getRequiredKills());
    }
}
