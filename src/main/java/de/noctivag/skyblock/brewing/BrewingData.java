package de.noctivag.skyblock.brewing;

import java.util.UUID;

/**
 * Brewing data storage
 */
public class BrewingData {
    
    private final UUID playerUuid;
    private final String recipeId;
    private final long startTime;
    private final int brewingTime;
    private final int experience;
    
    public BrewingData(UUID playerUuid, String recipeId, long startTime, int brewingTime, int experience) {
        this.playerUuid = playerUuid;
        this.recipeId = recipeId;
        this.startTime = startTime;
        this.brewingTime = brewingTime;
        this.experience = experience;
    }
    
    // Getters
    public UUID getPlayerUuid() { return playerUuid; }
    public String getRecipeId() { return recipeId; }
    public long getStartTime() { return startTime; }
    public int getBrewingTime() { return brewingTime; }
    public int getExperience() { return experience; }
    
    /**
     * Check if brewing is completed
     */
    public boolean isCompleted() {
        return System.currentTimeMillis() - startTime >= brewingTime * 50; // Convert ticks to milliseconds
    }
    
    /**
     * Get remaining time in ticks
     */
    public int getRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = (brewingTime * 50) - elapsed;
        return Math.max(0, (int) (remaining / 50)); // Convert back to ticks
    }
    
    /**
     * Get progress percentage (0.0 to 1.0)
     */
    public double getProgress() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.min(1.0, (double) elapsed / (brewingTime * 50));
    }
}
