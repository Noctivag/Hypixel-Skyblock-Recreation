package de.noctivag.skyblock.taming;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Taming Data - Individuelle Taming-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Taming Level und XP
 * - Taming Statistics
 * - Taming Progress
 * - Tamed Animals
 */
public class PlayerTaming {
    private final UUID playerId;
    private int tamingLevel;
    private double tamingXP;
    private double totalXP;
    private long lastActivity;
    
    // Taming Statistics
    private int animalsTamed;
    private int successfulTames;
    private int failedTames;
    private final Map<String, Integer> animalCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> animalTypes = new ConcurrentHashMap<>();
    
    // Taming Settings
    private boolean autoTame;
    private boolean showTamingProgress;
    private double successRate;
    private double tamingSpeed;
    
    public PlayerTaming(UUID playerId) {
        this.playerId = playerId;
        this.tamingLevel = 1;
        this.tamingXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        // Initialize statistics
        this.animalsTamed = 0;
        this.successfulTames = 0;
        this.failedTames = 0;
        
        // Initialize settings
        this.autoTame = false;
        this.showTamingProgress = true;
        this.successRate = 0.7; // 70% base success rate
        this.tamingSpeed = 1.0; // Normal taming speed
    }
    
    public void addXP(double xpAmount) {
        this.tamingXP += xpAmount;
        this.totalXP += xpAmount;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        // Check for level up
        int newLevel = calculateLevel(tamingXP);
        if (newLevel > tamingLevel) {
            tamingLevel = newLevel;
            // Increase success rate and taming speed with level
            this.successRate = Math.min(0.95, 0.7 + (tamingLevel - 1) * 0.015);
            this.tamingSpeed = Math.min(3.0, 1.0 + (tamingLevel - 1) * 0.1);
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
        if (level <= 10) return level * 75.0;
        if (level <= 25) return 10 * 75.0 + (level - 10) * 125.0;
        if (level <= 50) return 10 * 75.0 + 15 * 125.0 + (level - 25) * 175.0;
        return 10 * 75.0 + 15 * 125.0 + 25 * 175.0 + (level - 50) * 225.0;
    }
    
    public double getXPToNextLevel() {
        double xpForNextLevel = getXPRequiredForLevel(tamingLevel + 1);
        return xpForNextLevel - tamingXP;
    }
    
    public double getXPProgress() {
        double xpForCurrentLevel = getXPRequiredForLevel(tamingLevel);
        double xpForNextLevel = getXPRequiredForLevel(tamingLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (tamingXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public void addAnimalTamed(String animalType, boolean success) {
        this.animalsTamed++;
        
        if (success) {
            this.successfulTames++;
            animalCounts.put(animalType, animalCounts.getOrDefault(animalType, 0) + 1);
            animalTypes.put(animalType, animalTypes.getOrDefault(animalType, 0) + 1);
        } else {
            this.failedTames++;
        }
    }
    
    public double getSuccessRate() {
        if (animalsTamed == 0) return successRate;
        return (double) successfulTames / animalsTamed;
    }
    
    public int getAnimalCount(String animalType) {
        return animalCounts.getOrDefault(animalType, 0);
    }
    
    public int getTotalAnimalsTamed() {
        return animalCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getTamingLevel() {
        return tamingLevel;
    }
    
    public void setTamingLevel(int tamingLevel) {
        this.tamingLevel = Math.max(1, tamingLevel);
    }
    
    public double getTamingXP() {
        return tamingXP;
    }
    
    public void setTamingXP(double tamingXP) {
        this.tamingXP = Math.max(0, tamingXP);
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public int getAnimalsTamed() {
        return animalsTamed;
    }
    
    public int getSuccessfulTames() {
        return successfulTames;
    }
    
    public int getFailedTames() {
        return failedTames;
    }
    
    public Map<String, Integer> getAnimalCounts() {
        return new HashMap<>(animalCounts);
    }
    
    public Map<String, Integer> getAnimalTypes() {
        return new HashMap<>(animalTypes);
    }
    
    public boolean isAutoTame() {
        return autoTame;
    }
    
    public void setAutoTame(boolean autoTame) {
        this.autoTame = autoTame;
    }
    
    public boolean isShowTamingProgress() {
        return showTamingProgress;
    }
    
    public void setShowTamingProgress(boolean showTamingProgress) {
        this.showTamingProgress = showTamingProgress;
    }
    
    public double getBaseSuccessRate() {
        return successRate;
    }
    
    public void setSuccessRate(double successRate) {
        this.successRate = Math.max(0, Math.min(1, successRate));
    }
    
    public double getTamingSpeed() {
        return tamingSpeed;
    }
    
    public void setTamingSpeed(double tamingSpeed) {
        this.tamingSpeed = Math.max(0.1, tamingSpeed);
    }
    
    public void reset() {
        this.tamingLevel = 1;
        this.tamingXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        this.animalsTamed = 0;
        this.successfulTames = 0;
        this.failedTames = 0;
        
        this.animalCounts.clear();
        this.animalTypes.clear();
        
        this.successRate = 0.7;
        this.tamingSpeed = 1.0;
    }
    
    public String getTamingProgressBar() {
        double progress = getXPProgress();
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§6");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getTamingSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lTaming Summary:\n");
        summary.append("§7Level: §e").append(tamingLevel).append("\n");
        summary.append("§7XP: §a").append(String.format("%.1f", tamingXP)).append("\n");
        summary.append("§7To Next Level: §b").append(String.format("%.1f", getXPToNextLevel())).append("\n");
        summary.append("§7Animals Tamed: §6").append(animalsTamed).append("\n");
        summary.append("§7Success Rate: §a").append(String.format("%.1f%%", getSuccessRate() * 100)).append("\n");
        summary.append("§7Total Animals: §e").append(getTotalAnimalsTamed()).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerTaming{" +
                "playerId=" + playerId +
                ", tamingLevel=" + tamingLevel +
                ", tamingXP=" + tamingXP +
                ", animalsTamed=" + animalsTamed +
                ", successfulTames=" + successfulTames +
                '}';
    }
}
