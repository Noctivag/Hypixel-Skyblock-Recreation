package de.noctivag.skyblock.carpentry;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Carpentry Data - Individuelle Carpentry-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Carpentry Level und XP
 * - Carpentry Statistics
 * - Carpentry Progress
 * - Crafted Items
 */
public class PlayerCarpentry {
    private final UUID playerId;
    private int carpentryLevel;
    private double carpentryXP;
    private double totalXP;
    private long lastActivity;
    
    // Carpentry Statistics
    private int itemsCrafted;
    private int successfulCrafts;
    private int failedCrafts;
    private final Map<String, Integer> itemCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> recipeCounts = new ConcurrentHashMap<>();
    
    // Carpentry Settings
    private boolean autoCraft;
    private boolean showCraftingProgress;
    private double successRate;
    private double craftingSpeed;
    
    public PlayerCarpentry(UUID playerId) {
        this.playerId = playerId;
        this.carpentryLevel = 1;
        this.carpentryXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        // Initialize statistics
        this.itemsCrafted = 0;
        this.successfulCrafts = 0;
        this.failedCrafts = 0;
        
        // Initialize settings
        this.autoCraft = false;
        this.showCraftingProgress = true;
        this.successRate = 0.85; // 85% base success rate
        this.craftingSpeed = 1.0; // Normal crafting speed
    }
    
    public void addXP(double xpAmount) {
        this.carpentryXP += xpAmount;
        this.totalXP += xpAmount;
        this.lastActivity = System.currentTimeMillis();
        
        // Check for level up
        int newLevel = calculateLevel(carpentryXP);
        if (newLevel > carpentryLevel) {
            carpentryLevel = newLevel;
            // Increase success rate and crafting speed with level
            this.successRate = Math.min(0.98, 0.85 + (carpentryLevel - 1) * 0.008);
            this.craftingSpeed = Math.min(2.5, 1.0 + (carpentryLevel - 1) * 0.08);
        }
    }
    
    public int calculateLevel(double xp) {
        int level = 1;
        double totalXPRequired = 0;
        
        while (totalXPRequired <= xp) {
            level++;
            totalXPRequired += getXPRequiredForLevel(level);
        }
        
        return level - 1;
    }
    
    public double getXPRequiredForLevel(int level) {
        if (level <= 1) return 0;
        if (level <= 10) return level * 80.0;
        if (level <= 25) return 10 * 80.0 + (level - 10) * 120.0;
        if (level <= 50) return 10 * 80.0 + 15 * 120.0 + (level - 25) * 160.0;
        return 10 * 80.0 + 15 * 120.0 + 25 * 160.0 + (level - 50) * 200.0;
    }
    
    public double getXPToNextLevel() {
        double xpForNextLevel = getXPRequiredForLevel(carpentryLevel + 1);
        return xpForNextLevel - carpentryXP;
    }
    
    public double getXPProgress() {
        double xpForCurrentLevel = getXPRequiredForLevel(carpentryLevel);
        double xpForNextLevel = getXPRequiredForLevel(carpentryLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (carpentryXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public void addItemCrafted(String itemType, String recipe, boolean success) {
        this.itemsCrafted++;
        
        if (success) {
            this.successfulCrafts++;
            itemCounts.put(itemType, itemCounts.getOrDefault(itemType, 0) + 1);
            recipeCounts.put(recipe, recipeCounts.getOrDefault(recipe, 0) + 1);
        } else {
            this.failedCrafts++;
        }
    }
    
    public double getSuccessRate() {
        if (itemsCrafted == 0) return successRate;
        return (double) successfulCrafts / itemsCrafted;
    }
    
    public int getItemCount(String itemType) {
        return itemCounts.getOrDefault(itemType, 0);
    }
    
    public int getRecipeCount(String recipe) {
        return recipeCounts.getOrDefault(recipe, 0);
    }
    
    public int getTotalItemsCrafted() {
        return itemCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getCarpentryLevel() {
        return carpentryLevel;
    }
    
    public void setCarpentryLevel(int carpentryLevel) {
        this.carpentryLevel = Math.max(1, carpentryLevel);
    }
    
    public double getCarpentryXP() {
        return carpentryXP;
    }
    
    public void setCarpentryXP(double carpentryXP) {
        this.carpentryXP = Math.max(0, carpentryXP);
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public int getItemsCrafted() {
        return itemsCrafted;
    }
    
    public int getSuccessfulCrafts() {
        return successfulCrafts;
    }
    
    public int getFailedCrafts() {
        return failedCrafts;
    }
    
    public Map<String, Integer> getItemCounts() {
        return new HashMap<>(itemCounts);
    }
    
    public Map<String, Integer> getRecipeCounts() {
        return new HashMap<>(recipeCounts);
    }
    
    public boolean isAutoCraft() {
        return autoCraft;
    }
    
    public void setAutoCraft(boolean autoCraft) {
        this.autoCraft = autoCraft;
    }
    
    public boolean isShowCraftingProgress() {
        return showCraftingProgress;
    }
    
    public void setShowCraftingProgress(boolean showCraftingProgress) {
        this.showCraftingProgress = showCraftingProgress;
    }
    
    public double getBaseSuccessRate() {
        return successRate;
    }
    
    public void setSuccessRate(double successRate) {
        this.successRate = Math.max(0, Math.min(1, successRate));
    }
    
    public double getCraftingSpeed() {
        return craftingSpeed;
    }
    
    public void setCraftingSpeed(double craftingSpeed) {
        this.craftingSpeed = Math.max(0.1, craftingSpeed);
    }
    
    public void reset() {
        this.carpentryLevel = 1;
        this.carpentryXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        this.itemsCrafted = 0;
        this.successfulCrafts = 0;
        this.failedCrafts = 0;
        
        this.itemCounts.clear();
        this.recipeCounts.clear();
        
        this.successRate = 0.85;
        this.craftingSpeed = 1.0;
    }
    
    public String getCarpentryProgressBar() {
        double progress = getXPProgress();
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§e");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getCarpentrySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lCarpentry Summary:\n");
        summary.append("§7Level: §e").append(carpentryLevel).append("\n");
        summary.append("§7XP: §a").append(String.format("%.1f", carpentryXP)).append("\n");
        summary.append("§7To Next Level: §b").append(String.format("%.1f", getXPToNextLevel())).append("\n");
        summary.append("§7Items Crafted: §e").append(itemsCrafted).append("\n");
        summary.append("§7Success Rate: §a").append(String.format("%.1f%%", getSuccessRate() * 100)).append("\n");
        summary.append("§7Total Items: §e").append(getTotalItemsCrafted()).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerCarpentry{" +
                "playerId=" + playerId +
                ", carpentryLevel=" + carpentryLevel +
                ", carpentryXP=" + carpentryXP +
                ", itemsCrafted=" + itemsCrafted +
                ", successfulCrafts=" + successfulCrafts +
                '}';
    }
}
