package de.noctivag.skyblock.slayers;

import java.util.UUID;

/**
 * Represents a slayer quest
 */
public class SlayerQuest {
    
    private final UUID playerUuid;
    private final SlayerBoss boss;
    private final int tier;
    private final long startTime;
    private final long timeLimit; // in seconds
    
    public SlayerQuest(UUID playerUuid, SlayerBoss boss, int tier) {
        this.playerUuid = playerUuid;
        this.boss = boss;
        this.tier = tier;
        this.startTime = System.currentTimeMillis();
        this.timeLimit = 300; // 5 minutes default
    }
    
    // Getters
    public UUID getPlayerUuid() { return playerUuid; }
    public SlayerBoss getBoss() { return boss; }
    public int getTier() { return tier; }
    public long getStartTime() { return startTime; }
    public long getTimeLimit() { return timeLimit; }
    
    /**
     * Check if quest is expired
     */
    public boolean isExpired() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return elapsed >= timeLimit;
    }
    
    /**
     * Get time remaining in seconds
     */
    public long getTimeRemaining() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return Math.max(0, timeLimit - elapsed);
    }
    
    /**
     * Get progress percentage (0.0 to 1.0)
     */
    public double getProgress() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return Math.min(1.0, (double) elapsed / timeLimit);
    }
    
    /**
     * Get boss health for this tier
     */
    public double getBossHealth() {
        return boss.getHealthForTier(tier);
    }
    
    /**
     * Get boss damage for this tier
     */
    public double getBossDamage() {
        return boss.getDamageForTier(tier);
    }
    
    /**
     * Get boss defense for this tier
     */
    public double getBossDefense() {
        return boss.getDefenseForTier(tier);
    }
    
    /**
     * Get XP reward for completing this quest
     */
    public double getXpReward() {
        return boss.getXpForTier(tier);
    }
    
    /**
     * Get coin reward for completing this quest
     */
    public double getCoinReward() {
        return boss.getCoinsForTier(tier);
    }
    
    /**
     * Get quest cost for this tier
     */
    public double getQuestCost() {
        return boss.getQuestCostForTier(tier);
    }
}
