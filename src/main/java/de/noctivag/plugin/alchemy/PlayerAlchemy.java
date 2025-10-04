package de.noctivag.plugin.alchemy;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Alchemy Data - Individuelle Alchemy-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Alchemy Level und XP
 * - Alchemy Statistics
 * - Alchemy Progress
 * - Alchemy Recipes
 */
public class PlayerAlchemy {
    private final UUID playerId;
    private int alchemyLevel;
    private double alchemyXP;
    private double totalXP;
    private long lastActivity;
    
    // Alchemy Statistics
    private int potionsBrewed;
    private int successfulBrews;
    private int failedBrews;
    private double totalCost;
    private double totalValue;
    private final Map<String, Integer> potionCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> ingredientUsage = new ConcurrentHashMap<>();
    
    // Alchemy Settings
    private boolean autoBrew;
    private boolean showBrewingProgress;
    private double successRate;
    private double costReduction;
    
    public PlayerAlchemy(UUID playerId) {
        this.playerId = playerId;
        this.alchemyLevel = 1;
        this.alchemyXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        // Initialize statistics
        this.potionsBrewed = 0;
        this.successfulBrews = 0;
        this.failedBrews = 0;
        this.totalCost = 0.0;
        this.totalValue = 0.0;
        
        // Initialize settings
        this.autoBrew = false;
        this.showBrewingProgress = true;
        this.successRate = 0.8; // 80% base success rate
        this.costReduction = 0.0; // No cost reduction initially
    }
    
    public void addXP(double xpAmount) {
        this.alchemyXP += xpAmount;
        this.totalXP += xpAmount;
        this.lastActivity = System.currentTimeMillis();
        
        // Check for level up
        int newLevel = calculateLevel(alchemyXP);
        if (newLevel > alchemyLevel) {
            alchemyLevel = newLevel;
            // Increase success rate and cost reduction with level
            this.successRate = Math.min(0.95, 0.8 + (alchemyLevel - 1) * 0.01);
            this.costReduction = Math.min(0.5, (alchemyLevel - 1) * 0.02);
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
        if (level <= 10) return level * 100.0;
        if (level <= 25) return 10 * 100.0 + (level - 10) * 150.0;
        if (level <= 50) return 10 * 100.0 + 15 * 150.0 + (level - 25) * 200.0;
        return 10 * 100.0 + 15 * 150.0 + 25 * 200.0 + (level - 50) * 250.0;
    }
    
    public double getXPToNextLevel() {
        double xpForNextLevel = getXPRequiredForLevel(alchemyLevel + 1);
        return xpForNextLevel - alchemyXP;
    }
    
    public double getXPProgress() {
        double xpForCurrentLevel = getXPRequiredForLevel(alchemyLevel);
        double xpForNextLevel = getXPRequiredForLevel(alchemyLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (alchemyXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public void addPotionBrewed(String potionType, boolean success, double cost, double value) {
        this.potionsBrewed++;
        this.totalCost += cost;
        this.totalValue += value;
        
        if (success) {
            this.successfulBrews++;
            potionCounts.put(potionType, potionCounts.getOrDefault(potionType, 0) + 1);
        } else {
            this.failedBrews++;
        }
    }
    
    public void addIngredientUsage(String ingredient, int amount) {
        ingredientUsage.put(ingredient, ingredientUsage.getOrDefault(ingredient, 0) + amount);
    }
    
    public double getSuccessRate() {
        if (potionsBrewed == 0) return successRate;
        return (double) successfulBrews / potionsBrewed;
    }
    
    public double getProfit() {
        return totalValue - totalCost;
    }
    
    public double getProfitMargin() {
        if (totalCost == 0) return 0.0;
        return (totalValue - totalCost) / totalCost;
    }
    
    public int getPotionCount(String potionType) {
        return potionCounts.getOrDefault(potionType, 0);
    }
    
    public int getIngredientUsage(String ingredient) {
        return ingredientUsage.getOrDefault(ingredient, 0);
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getAlchemyLevel() {
        return alchemyLevel;
    }
    
    public void setAlchemyLevel(int alchemyLevel) {
        this.alchemyLevel = Math.max(1, alchemyLevel);
    }
    
    public double getAlchemyXP() {
        return alchemyXP;
    }
    
    public void setAlchemyXP(double alchemyXP) {
        this.alchemyXP = Math.max(0, alchemyXP);
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public int getPotionsBrewed() {
        return potionsBrewed;
    }
    
    public int getSuccessfulBrews() {
        return successfulBrews;
    }
    
    public int getFailedBrews() {
        return failedBrews;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    public double getTotalValue() {
        return totalValue;
    }
    
    public Map<String, Integer> getPotionCounts() {
        return new HashMap<>(potionCounts);
    }
    
    public Map<String, Integer> getIngredientUsage() {
        return new HashMap<>(ingredientUsage);
    }
    
    public boolean isAutoBrew() {
        return autoBrew;
    }
    
    public void setAutoBrew(boolean autoBrew) {
        this.autoBrew = autoBrew;
    }
    
    public boolean isShowBrewingProgress() {
        return showBrewingProgress;
    }
    
    public void setShowBrewingProgress(boolean showBrewingProgress) {
        this.showBrewingProgress = showBrewingProgress;
    }
    
    public double getBaseSuccessRate() {
        return successRate;
    }
    
    public void setSuccessRate(double successRate) {
        this.successRate = Math.max(0, Math.min(1, successRate));
    }
    
    public double getCostReduction() {
        return costReduction;
    }
    
    public void setCostReduction(double costReduction) {
        this.costReduction = Math.max(0, Math.min(1, costReduction));
    }
    
    public void reset() {
        this.alchemyLevel = 1;
        this.alchemyXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        this.potionsBrewed = 0;
        this.successfulBrews = 0;
        this.failedBrews = 0;
        this.totalCost = 0.0;
        this.totalValue = 0.0;
        
        this.potionCounts.clear();
        this.ingredientUsage.clear();
        
        this.successRate = 0.8;
        this.costReduction = 0.0;
    }
    
    public String getAlchemyProgressBar() {
        double progress = getXPProgress();
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§d");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getAlchemySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lAlchemy Summary:\n");
        summary.append("§7Level: §e").append(alchemyLevel).append("\n");
        summary.append("§7XP: §a").append(String.format("%.1f", alchemyXP)).append("\n");
        summary.append("§7To Next Level: §b").append(String.format("%.1f", getXPToNextLevel())).append("\n");
        summary.append("§7Potions Brewed: §d").append(potionsBrewed).append("\n");
        summary.append("§7Success Rate: §a").append(String.format("%.1f%%", getSuccessRate() * 100)).append("\n");
        summary.append("§7Total Profit: §e").append(String.format("%.2f", getProfit())).append("\n");
        summary.append("§7Profit Margin: §e").append(String.format("%.1f%%", getProfitMargin() * 100)).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerAlchemy{" +
                "playerId=" + playerId +
                ", alchemyLevel=" + alchemyLevel +
                ", alchemyXP=" + alchemyXP +
                ", potionsBrewed=" + potionsBrewed +
                ", successfulBrews=" + successfulBrews +
                '}';
    }
}
