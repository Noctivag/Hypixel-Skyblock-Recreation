package de.noctivag.skyblock.combat;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Player Combat Data - Individuelle Combat-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Combat Level und XP
 * - Combat Statistics
 * - Combat Progress
 * - Combat Rewards
 */
public class PlayerCombat {
    private final UUID playerId;
    private int combatLevel;
    private double combatXP;
    private double totalXP;
    private long lastActivity;
    
    // Combat Statistics
    private int totalKills;
    private int totalDeaths;
    private double totalDamageDealt;
    private double totalDamageTaken;
    private int criticalHits;
    private int blocksBroken;
    private int mobsKilled;
    private int playersKilled;
    
    // Combat Settings
    private boolean autoAttack;
    private boolean showDamageNumbers;
    private boolean showHealthBar;
    private double criticalChance;
    private double criticalMultiplier;
    
    public PlayerCombat(UUID playerId) {
        this.playerId = playerId;
        this.combatLevel = 1;
        this.combatXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        // Initialize statistics
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.totalDamageDealt = 0.0;
        this.totalDamageTaken = 0.0;
        this.criticalHits = 0;
        this.blocksBroken = 0;
        this.mobsKilled = 0;
        this.playersKilled = 0;
        
        // Initialize settings
        this.autoAttack = false;
        this.showDamageNumbers = true;
        this.showHealthBar = true;
        this.criticalChance = 0.1; // 10% base critical chance
        this.criticalMultiplier = 1.5; // 150% damage on critical hit
    }
    
    public void addXP(double xpAmount) {
        this.combatXP += xpAmount;
        this.totalXP += xpAmount;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        // Check for level up
        int newLevel = calculateLevel(combatXP);
        if (newLevel > combatLevel) {
            combatLevel = newLevel;
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
        if (level <= 10) return level * 50.0;
        if (level <= 25) return 10 * 50.0 + (level - 10) * 100.0;
        if (level <= 50) return 10 * 50.0 + 15 * 100.0 + (level - 25) * 200.0;
        return 10 * 50.0 + 15 * 100.0 + 25 * 200.0 + (level - 50) * 300.0;
    }
    
    public double getXPToNextLevel() {
        double xpForNextLevel = getXPRequiredForLevel(combatLevel + 1);
        return xpForNextLevel - combatXP;
    }
    
    public double getXPProgress() {
        double xpForCurrentLevel = getXPRequiredForLevel(combatLevel);
        double xpForNextLevel = getXPRequiredForLevel(combatLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (combatXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public void addKill() {
        this.totalKills++;
    }
    
    public void addDeath() {
        this.totalDeaths++;
    }
    
    public void addDamageDealt(double damage) {
        this.totalDamageDealt += damage;
    }
    
    public void addDamageTaken(double damage) {
        this.totalDamageTaken += damage;
    }
    
    public void addCriticalHit() {
        this.criticalHits++;
    }
    
    public void addBlockBroken() {
        this.blocksBroken++;
    }
    
    public void addMobKill() {
        this.mobsKilled++;
    }
    
    public void addPlayerKill() {
        this.playersKilled++;
    }
    
    public double getKillDeathRatio() {
        if (totalDeaths == 0) return totalKills;
        return (double) totalKills / totalDeaths;
    }
    
    public double getCriticalHitRate() {
        if (totalKills == 0) return 0.0;
        return (double) criticalHits / totalKills;
    }
    
    public double getAverageDamageDealt() {
        if (totalKills == 0) return 0.0;
        return totalDamageDealt / totalKills;
    }
    
    public double getAverageDamageTaken() {
        if (totalDeaths == 0) return 0.0;
        return totalDamageTaken / totalDeaths;
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getCombatLevel() {
        return combatLevel;
    }
    
    public void setCombatLevel(int combatLevel) {
        this.combatLevel = Math.max(1, combatLevel);
    }
    
    public double getCombatXP() {
        return combatXP;
    }
    
    public void setCombatXP(double combatXP) {
        this.combatXP = Math.max(0, combatXP);
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public int getTotalKills() {
        return totalKills;
    }
    
    public int getTotalDeaths() {
        return totalDeaths;
    }
    
    public double getTotalDamageDealt() {
        return totalDamageDealt;
    }
    
    public double getTotalDamageTaken() {
        return totalDamageTaken;
    }
    
    public int getCriticalHits() {
        return criticalHits;
    }
    
    public int getBlocksBroken() {
        return blocksBroken;
    }
    
    public int getMobsKilled() {
        return mobsKilled;
    }
    
    public int getPlayersKilled() {
        return playersKilled;
    }
    
    public boolean isAutoAttack() {
        return autoAttack;
    }
    
    public void setAutoAttack(boolean autoAttack) {
        this.autoAttack = autoAttack;
    }
    
    public boolean isShowDamageNumbers() {
        return showDamageNumbers;
    }
    
    public void setShowDamageNumbers(boolean showDamageNumbers) {
        this.showDamageNumbers = showDamageNumbers;
    }
    
    public boolean isShowHealthBar() {
        return showHealthBar;
    }
    
    public void setShowHealthBar(boolean showHealthBar) {
        this.showHealthBar = showHealthBar;
    }
    
    public double getCriticalChance() {
        return criticalChance;
    }
    
    public void setCriticalChance(double criticalChance) {
        this.criticalChance = Math.max(0, Math.min(1, criticalChance));
    }
    
    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }
    
    public void setCriticalMultiplier(double criticalMultiplier) {
        this.criticalMultiplier = Math.max(1, criticalMultiplier);
    }
    
    public void reset() {
        this.combatLevel = 1;
        this.combatXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.totalDamageDealt = 0.0;
        this.totalDamageTaken = 0.0;
        this.criticalHits = 0;
        this.blocksBroken = 0;
        this.mobsKilled = 0;
        this.playersKilled = 0;
    }
    
    public String getCombatProgressBar() {
        double progress = getXPProgress();
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§c");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getCombatSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lCombat Summary:\n");
        summary.append("§7Level: §e").append(combatLevel).append("\n");
        summary.append("§7XP: §a").append(String.format("%.1f", combatXP)).append("\n");
        summary.append("§7To Next Level: §b").append(String.format("%.1f", getXPToNextLevel())).append("\n");
        summary.append("§7Total Kills: §c").append(totalKills).append("\n");
        summary.append("§7Total Deaths: §c").append(totalDeaths).append("\n");
        summary.append("§7K/D Ratio: §e").append(String.format("%.2f", getKillDeathRatio())).append("\n");
        summary.append("§7Critical Hits: §d").append(criticalHits).append("\n");
        summary.append("§7Critical Rate: §d").append(String.format("%.1f%%", getCriticalHitRate() * 100)).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerCombat{" +
                "playerId=" + playerId +
                ", combatLevel=" + combatLevel +
                ", combatXP=" + combatXP +
                ", totalKills=" + totalKills +
                ", totalDeaths=" + totalDeaths +
                '}';
    }
}
