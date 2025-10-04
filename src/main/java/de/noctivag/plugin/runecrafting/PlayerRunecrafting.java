package de.noctivag.plugin.runecrafting;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Runecrafting Data - Individuelle Runecrafting-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Runecrafting Level und XP
 * - Runecrafting Statistics
 * - Runecrafting Progress
 * - Crafted Runes
 */
public class PlayerRunecrafting {
    private final UUID playerId;
    private int runecraftingLevel;
    private double runecraftingXP;
    private double totalXP;
    private long lastActivity;
    
    // Runecrafting Statistics
    private int runesCrafted;
    private int successfulCrafts;
    private int failedCrafts;
    private final Map<String, Integer> runeCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> runeTypes = new ConcurrentHashMap<>();
    
    // Runecrafting Settings
    private boolean autoCraft;
    private boolean showCraftingProgress;
    private double successRate;
    private double craftingSpeed;
    
    public PlayerRunecrafting(UUID playerId) {
        this.playerId = playerId;
        this.runecraftingLevel = 1;
        this.runecraftingXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        // Initialize statistics
        this.runesCrafted = 0;
        this.successfulCrafts = 0;
        this.failedCrafts = 0;
        
        // Initialize settings
        this.autoCraft = false;
        this.showCraftingProgress = true;
        this.successRate = 0.75; // 75% base success rate
        this.craftingSpeed = 1.0; // Normal crafting speed
    }
    
    public void addXP(double xpAmount) {
        this.runecraftingXP += xpAmount;
        this.totalXP += xpAmount;
        this.lastActivity = System.currentTimeMillis();
        
        // Check for level up
        int newLevel = calculateLevel(runecraftingXP);
        if (newLevel > runecraftingLevel) {
            runecraftingLevel = newLevel;
            // Increase success rate and crafting speed with level
            this.successRate = Math.min(0.95, 0.75 + (runecraftingLevel - 1) * 0.012);
            this.craftingSpeed = Math.min(2.0, 1.0 + (runecraftingLevel - 1) * 0.06);
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
        if (level <= 10) return level * 90.0;
        if (level <= 25) return 10 * 90.0 + (level - 10) * 140.0;
        if (level <= 50) return 10 * 90.0 + 15 * 140.0 + (level - 25) * 180.0;
        return 10 * 90.0 + 15 * 140.0 + 25 * 180.0 + (level - 50) * 220.0;
    }
    
    public double getXPToNextLevel() {
        double xpForNextLevel = getXPRequiredForLevel(runecraftingLevel + 1);
        return xpForNextLevel - runecraftingXP;
    }
    
    public double getXPProgress() {
        double xpForCurrentLevel = getXPRequiredForLevel(runecraftingLevel);
        double xpForNextLevel = getXPRequiredForLevel(runecraftingLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (runecraftingXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public void addRuneCrafted(String runeType, boolean success) {
        this.runesCrafted++;
        
        if (success) {
            this.successfulCrafts++;
            runeCounts.put(runeType, runeCounts.getOrDefault(runeType, 0) + 1);
            runeTypes.put(runeType, runeTypes.getOrDefault(runeType, 0) + 1);
        } else {
            this.failedCrafts++;
        }
    }
    
    public double getSuccessRate() {
        if (runesCrafted == 0) return successRate;
        return (double) successfulCrafts / runesCrafted;
    }
    
    public int getRuneCount(String runeType) {
        return runeCounts.getOrDefault(runeType, 0);
    }
    
    public int getTotalRunesCrafted() {
        return runeCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getRunecraftingLevel() {
        return runecraftingLevel;
    }
    
    public void setRunecraftingLevel(int runecraftingLevel) {
        this.runecraftingLevel = Math.max(1, runecraftingLevel);
    }
    
    public double getRunecraftingXP() {
        return runecraftingXP;
    }
    
    public void setRunecraftingXP(double runecraftingXP) {
        this.runecraftingXP = Math.max(0, runecraftingXP);
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public int getRunesCrafted() {
        return runesCrafted;
    }
    
    public int getSuccessfulCrafts() {
        return successfulCrafts;
    }
    
    public int getFailedCrafts() {
        return failedCrafts;
    }
    
    public Map<String, Integer> getRuneCounts() {
        return new HashMap<>(runeCounts);
    }
    
    public Map<String, Integer> getRuneTypes() {
        return new HashMap<>(runeTypes);
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
        this.runecraftingLevel = 1;
        this.runecraftingXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        this.runesCrafted = 0;
        this.successfulCrafts = 0;
        this.failedCrafts = 0;
        
        this.runeCounts.clear();
        this.runeTypes.clear();
        
        this.successRate = 0.75;
        this.craftingSpeed = 1.0;
    }
    
    public String getRunecraftingProgressBar() {
        double progress = getXPProgress();
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§5");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getRunecraftingSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lRunecrafting Summary:\n");
        summary.append("§7Level: §e").append(runecraftingLevel).append("\n");
        summary.append("§7XP: §a").append(String.format("%.1f", runecraftingXP)).append("\n");
        summary.append("§7To Next Level: §b").append(String.format("%.1f", getXPToNextLevel())).append("\n");
        summary.append("§7Runes Crafted: §5").append(runesCrafted).append("\n");
        summary.append("§7Success Rate: §a").append(String.format("%.1f%%", getSuccessRate() * 100)).append("\n");
        summary.append("§7Total Runes: §e").append(getTotalRunesCrafted()).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerRunecrafting{" +
                "playerId=" + playerId +
                ", runecraftingLevel=" + runecraftingLevel +
                ", runecraftingXP=" + runecraftingXP +
                ", runesCrafted=" + runesCrafted +
                ", successfulCrafts=" + successfulCrafts +
                '}';
    }
}
