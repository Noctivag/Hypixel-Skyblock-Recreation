package de.noctivag.skyblock.slayers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a player's slayer progress
 */
public class SlayerProgress {
    
    private final UUID playerUuid;
    private final Map<String, Integer> bossProgress; // Boss ID -> Highest tier completed
    private final Map<String, Integer> bossKills; // Boss ID -> Total kills
    private final Map<String, Double> bossXp; // Boss ID -> Total XP gained
    private final Map<String, Double> bossCoins; // Boss ID -> Total coins earned
    
    public SlayerProgress(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.bossProgress = new HashMap<>();
        this.bossKills = new HashMap<>();
        this.bossXp = new HashMap<>();
        this.bossCoins = new HashMap<>();
    }
    
    /**
     * Get player UUID
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    /**
     * Update boss progress
     */
    public void updateBossProgress(String bossId, int tier) {
        int currentProgress = bossProgress.getOrDefault(bossId, 0);
        if (tier > currentProgress) {
            bossProgress.put(bossId, tier);
        }
    }
    
    /**
     * Get boss progress
     */
    public int getBossProgress(String bossId) {
        return bossProgress.getOrDefault(bossId, 0);
    }
    
    /**
     * Add boss kill
     */
    public void addBossKill(String bossId) {
        bossKills.put(bossId, bossKills.getOrDefault(bossId, 0) + 1);
    }
    
    /**
     * Get boss kills
     */
    public int getBossKills(String bossId) {
        return bossKills.getOrDefault(bossId, 0);
    }
    
    /**
     * Add boss XP
     */
    public void addBossXp(String bossId, double xp) {
        bossXp.put(bossId, bossXp.getOrDefault(bossId, 0.0) + xp);
    }
    
    /**
     * Get boss XP
     */
    public double getBossXp(String bossId) {
        return bossXp.getOrDefault(bossId, 0.0);
    }
    
    /**
     * Add boss coins
     */
    public void addBossCoins(String bossId, double coins) {
        bossCoins.put(bossId, bossCoins.getOrDefault(bossId, 0.0) + coins);
    }
    
    /**
     * Get boss coins
     */
    public double getBossCoins(String bossId) {
        return bossCoins.getOrDefault(bossId, 0.0);
    }
    
    /**
     * Get total kills across all bosses
     */
    public int getTotalKills() {
        return bossKills.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get total XP across all bosses
     */
    public double getTotalXp() {
        return bossXp.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    /**
     * Get total coins across all bosses
     */
    public double getTotalCoins() {
        return bossCoins.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    /**
     * Get all boss progress
     */
    public Map<String, Integer> getAllBossProgress() {
        return new HashMap<>(bossProgress);
    }
    
    /**
     * Get all boss kills
     */
    public Map<String, Integer> getAllBossKills() {
        return new HashMap<>(bossKills);
    }
    
    /**
     * Get all boss XP
     */
    public Map<String, Double> getAllBossXp() {
        return new HashMap<>(bossXp);
    }
    
    /**
     * Get all boss coins
     */
    public Map<String, Double> getAllBossCoins() {
        return new HashMap<>(bossCoins);
    }
    
    /**
     * Check if player has completed any tier of a boss
     */
    public boolean hasCompletedBoss(String bossId) {
        return bossProgress.getOrDefault(bossId, 0) > 0;
    }
    
    /**
     * Check if player has completed specific tier of a boss
     */
    public boolean hasCompletedBossTier(String bossId, int tier) {
        return bossProgress.getOrDefault(bossId, 0) >= tier;
    }
    
    /**
     * Get highest tier completed for a boss
     */
    public int getHighestTierCompleted(String bossId) {
        return bossProgress.getOrDefault(bossId, 0);
    }
    
    /**
     * Get next tier to complete for a boss
     */
    public int getNextTierToComplete(String bossId) {
        return bossProgress.getOrDefault(bossId, 0) + 1;
    }
    
    /**
     * Check if player can start a tier for a boss
     */
    public boolean canStartTier(String bossId, int tier) {
        int currentProgress = bossProgress.getOrDefault(bossId, 0);
        return tier == currentProgress + 1;
    }
    
    /**
     * Get total progress across all bosses
     */
    public int getTotalProgress() {
        return bossProgress.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get average progress across all bosses
     */
    public double getAverageProgress() {
        if (bossProgress.isEmpty()) return 0.0;
        return (double) getTotalProgress() / bossProgress.size();
    }
    
    /**
     * Clear all progress
     */
    public void clearAllProgress() {
        bossProgress.clear();
        bossKills.clear();
        bossXp.clear();
        bossCoins.clear();
    }
    
    /**
     * Clear progress for specific boss
     */
    public void clearBossProgress(String bossId) {
        bossProgress.remove(bossId);
        bossKills.remove(bossId);
        bossXp.remove(bossId);
        bossCoins.remove(bossId);
    }
}
