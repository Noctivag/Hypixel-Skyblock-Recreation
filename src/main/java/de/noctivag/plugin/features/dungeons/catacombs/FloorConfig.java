package de.noctivag.plugin.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

/**
 * Configuration for a dungeon floor
 */
public class FloorConfig {
    
    private final int floor;
    private int minimumLevel;
    private int recommendedLevel;
    private int minimumScore;
    private int maxPlayers;
    private int timeLimit; // in seconds
    private int mobLevel;
    private double rewardMultiplier;
    
    public FloorConfig(int floor) {
        this.floor = floor;
        // Set defaults
        this.minimumLevel = 1;
        this.recommendedLevel = 5;
        this.minimumScore = 100;
        this.maxPlayers = 5;
        this.timeLimit = 600;
        this.mobLevel = 1;
        this.rewardMultiplier = 1.0;
    }
    
    // Builder pattern methods
    public FloorConfig setMinimumLevel(int minimumLevel) {
        this.minimumLevel = minimumLevel;
        return this;
    }
    
    public FloorConfig setRecommendedLevel(int recommendedLevel) {
        this.recommendedLevel = recommendedLevel;
        return this;
    }
    
    public FloorConfig setMinimumScore(int minimumScore) {
        this.minimumScore = minimumScore;
        return this;
    }
    
    public FloorConfig setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }
    
    public FloorConfig setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }
    
    public FloorConfig setMobLevel(int mobLevel) {
        this.mobLevel = mobLevel;
        return this;
    }
    
    public FloorConfig setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
        return this;
    }
    
    // Getters
    public int getFloor() {
        return floor;
    }
    
    public int getMinimumLevel() {
        return minimumLevel;
    }
    
    public int getRecommendedLevel() {
        return recommendedLevel;
    }
    
    public int getMinimumScore() {
        return minimumScore;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public int getMobLevel() {
        return mobLevel;
    }
    
    public double getRewardMultiplier() {
        return rewardMultiplier;
    }
}
