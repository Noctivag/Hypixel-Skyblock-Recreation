package de.noctivag.skyblock.slayers;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Slayer Data - Individuelle Slayer-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Slayer Levels
 * - Slayer XP
 * - Slayer Statistics
 * - Slayer Progress
 */
public class PlayerSlayerData {
    private final UUID playerId;
    private final Map<SlayerType, Integer> slayerLevels = new ConcurrentHashMap<>();
    private final Map<SlayerType, Double> slayerXP = new ConcurrentHashMap<>();
    private final Map<SlayerType, Integer> slayerKills = new ConcurrentHashMap<>();
    private final Map<SlayerType, Double> totalXP = new ConcurrentHashMap<>();
    private final Map<SlayerType, Long> lastActivity = new ConcurrentHashMap<>();
    
    public PlayerSlayerData(UUID playerId) {
        this.playerId = playerId;
        initializeSlayerData();
    }
    
    private void initializeSlayerData() {
        for (SlayerType slayerType : SlayerType.values()) {
            slayerLevels.put(slayerType, 0);
            slayerXP.put(slayerType, 0.0);
            slayerKills.put(slayerType, 0);
            totalXP.put(slayerType, 0.0);
            lastActivity.put(slayerType, java.lang.System.currentTimeMillis());
        }
    }
    
    public void completeQuest(SlayerType slayerType, int tier) {
        // Add XP based on tier
        double xpGained = tier * 100.0;
        addXP(slayerType, xpGained);
        
        // Increment kills
        slayerKills.put(slayerType, slayerKills.get(slayerType) + 1);
        
        // Update last activity
        lastActivity.put(slayerType, java.lang.System.currentTimeMillis());
    }
    
    public void addXP(SlayerType slayerType, double xpAmount) {
        double currentXP = slayerXP.get(slayerType);
        double newXP = currentXP + xpAmount;
        
        // Update XP
        slayerXP.put(slayerType, newXP);
        totalXP.put(slayerType, totalXP.get(slayerType) + xpAmount);
        
        // Update level
        int newLevel = calculateLevel(newXP);
        slayerLevels.put(slayerType, newLevel);
    }
    
    public int getLevel(SlayerType slayerType) {
        return slayerLevels.getOrDefault(slayerType, 0);
    }
    
    public double getXP(SlayerType slayerType) {
        return slayerXP.getOrDefault(slayerType, 0.0);
    }
    
    public double getTotalXP(SlayerType slayerType) {
        return totalXP.getOrDefault(slayerType, 0.0);
    }
    
    public int getKills(SlayerType slayerType) {
        return slayerKills.getOrDefault(slayerType, 0);
    }
    
    public long getLastActivity(SlayerType slayerType) {
        return lastActivity.getOrDefault(slayerType, 0L);
    }
    
    public double getXPToNextLevel(SlayerType slayerType) {
        double currentXP = getXP(slayerType);
        int currentLevel = getLevel(slayerType);
        double xpForNextLevel = getXPRequiredForLevel(currentLevel + 1);
        return xpForNextLevel - currentXP;
    }
    
    public double getXPProgress(SlayerType slayerType) {
        double currentXP = getXP(slayerType);
        int currentLevel = getLevel(slayerType);
        double xpForCurrentLevel = getXPRequiredForLevel(currentLevel);
        double xpForNextLevel = getXPRequiredForLevel(currentLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (currentXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public int calculateLevel(double xp) {
        int level = 0;
        double totalXPRequired = 0;
        
        while (totalXPRequired <= xp) {
            level++;
            totalXPRequired += getXPRequiredForLevel(level);
        }
        
        return level - 1;
    }
    
    public double getXPRequiredForLevel(int level) {
        if (level <= 0) return 0;
        if (level <= 10) return level * 100.0;
        if (level <= 20) return 10 * 100.0 + (level - 10) * 200.0;
        if (level <= 30) return 10 * 100.0 + 10 * 200.0 + (level - 20) * 300.0;
        if (level <= 40) return 10 * 100.0 + 10 * 200.0 + 10 * 300.0 + (level - 30) * 400.0;
        return 10 * 100.0 + 10 * 200.0 + 10 * 300.0 + 10 * 400.0 + (level - 40) * 500.0;
    }
    
    public double getTotalXPRequiredForLevel(int level) {
        double total = 0;
        for (int i = 1; i <= level; i++) {
            total += getXPRequiredForLevel(i);
        }
        return total;
    }
    
    public boolean canStartQuest(SlayerType slayerType, int tier) {
        int currentLevel = getLevel(slayerType);
        return currentLevel >= (tier - 1);
    }
    
    public int getTotalLevel() {
        return slayerLevels.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getTotalXP() {
        return totalXP.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    public int getTotalKills() {
        return slayerKills.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public SlayerType getHighestSlayer() {
        return slayerLevels.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(SlayerType.ZOMBIE);
    }
    
    public SlayerType getLowestSlayer() {
        return slayerLevels.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(SlayerType.ZOMBIE);
    }
    
    public List<SlayerType> getSlayersByLevel() {
        List<SlayerType> slayers = new ArrayList<>(Arrays.asList(SlayerType.values()));
        slayers.sort((a, b) -> Integer.compare(getLevel(b), getLevel(a)));
        return slayers;
    }
    
    public Map<SlayerType, Integer> getAllLevels() {
        return new HashMap<>(slayerLevels);
    }
    
    public Map<SlayerType, Double> getAllXP() {
        return new HashMap<>(slayerXP);
    }
    
    public Map<SlayerType, Integer> getAllKills() {
        return new HashMap<>(slayerKills);
    }
    
    public Map<SlayerType, Double> getAllTotalXP() {
        return new HashMap<>(totalXP);
    }
    
    public void resetSlayer(SlayerType slayerType) {
        slayerLevels.put(slayerType, 0);
        slayerXP.put(slayerType, 0.0);
        slayerKills.put(slayerType, 0);
        totalXP.put(slayerType, 0.0);
        lastActivity.put(slayerType, java.lang.System.currentTimeMillis());
    }
    
    public void resetAllSlayers() {
        for (SlayerType slayerType : SlayerType.values()) {
            resetSlayer(slayerType);
        }
    }
    
    public String getSlayerProgressBar(SlayerType slayerType) {
        double progress = getXPProgress(slayerType);
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§a");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getSlayerSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lSlayer Summary:\n");
        
        for (SlayerType slayerType : getSlayersByLevel()) {
            int level = getLevel(slayerType);
            double xp = getXP(slayerType);
            double xpToNext = getXPToNextLevel(slayerType);
            int kills = getKills(slayerType);
            
            summary.append(slayerType.getDisplayName()).append(" §7Level: §e").append(level)
                   .append(" §7XP: §a").append(String.format("%.1f", xp))
                   .append(" §7To Next: §b").append(String.format("%.1f", xpToNext))
                   .append(" §7Kills: §c").append(kills).append("\n");
        }
        
        return summary.toString();
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    @Override
    public String toString() {
        return "PlayerSlayerData{" +
                "playerId=" + playerId +
                ", totalLevel=" + getTotalLevel() +
                ", totalXP=" + getTotalXP() +
                ", totalKills=" + getTotalKills() +
                ", highestSlayer=" + getHighestSlayer() +
                '}';
    }
}
