package de.noctivag.skyblock.skills;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Skills - Individuelle Skill-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Skill-Level und XP
 * - Skill-Fortschritt
 * - Skill-Statistiken
 * - Skill-Boosts
 */
public class PlayerSkills {
    private final UUID playerId;
    private final Map<SkillType, Double> skillXP = new ConcurrentHashMap<>();
    private final Map<SkillType, Integer> skillLevels = new ConcurrentHashMap<>();
    private final Map<SkillType, Double> totalXP = new ConcurrentHashMap<>();
    private final Map<SkillType, Long> lastActivity = new ConcurrentHashMap<>();
    
    // Skill Statistics
    private final Map<SkillType, Integer> skillUses = new ConcurrentHashMap<>();
    private final Map<SkillType, Double> skillEfficiency = new ConcurrentHashMap<>();
    
    public PlayerSkills(UUID playerId) {
        this.playerId = playerId;
        initializeSkills();
    }
    
    private void initializeSkills() {
        for (SkillType skillType : SkillType.values()) {
            skillXP.put(skillType, 0.0);
            skillLevels.put(skillType, 1);
            totalXP.put(skillType, 0.0);
            lastActivity.put(skillType, System.currentTimeMillis());
            skillUses.put(skillType, 0);
            skillEfficiency.put(skillType, 1.0);
        }
    }
    
    public double addXP(SkillType skillType, double xpAmount) {
        double currentXP = skillXP.get(skillType);
        double newXP = currentXP + xpAmount;
        
        // Update XP
        skillXP.put(skillType, newXP);
        totalXP.put(skillType, totalXP.get(skillType) + xpAmount);
        lastActivity.put(skillType, System.currentTimeMillis());
        
        // Update level
        int newLevel = calculateLevel(newXP);
        skillLevels.put(skillType, newLevel);
        
        // Update statistics
        skillUses.put(skillType, skillUses.get(skillType) + 1);
        
        return newXP;
    }
    
    public int getLevel(SkillType skillType) {
        return skillLevels.getOrDefault(skillType, 1);
    }
    
    public double getXP(SkillType skillType) {
        return skillXP.getOrDefault(skillType, 0.0);
    }
    
    public double getTotalXP(SkillType skillType) {
        return totalXP.getOrDefault(skillType, 0.0);
    }
    
    public long getLastActivity(SkillType skillType) {
        return lastActivity.getOrDefault(skillType, 0L);
    }
    
    public int getSkillUses(SkillType skillType) {
        return skillUses.getOrDefault(skillType, 0);
    }
    
    public double getSkillEfficiency(SkillType skillType) {
        return skillEfficiency.getOrDefault(skillType, 1.0);
    }
    
    public void setSkillEfficiency(SkillType skillType, double efficiency) {
        skillEfficiency.put(skillType, efficiency);
    }
    
    public double getXPToNextLevel(SkillType skillType) {
        double currentXP = getXP(skillType);
        int currentLevel = getLevel(skillType);
        double xpForNextLevel = getXPRequiredForLevel(currentLevel + 1);
        return xpForNextLevel - currentXP;
    }
    
    public double getXPProgress(SkillType skillType) {
        double currentXP = getXP(skillType);
        int currentLevel = getLevel(skillType);
        double xpForCurrentLevel = getXPRequiredForLevel(currentLevel);
        double xpForNextLevel = getXPRequiredForLevel(currentLevel + 1);
        
        return (currentXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
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
        if (level <= 15) return level * 2.0;
        if (level <= 30) return 15 * 2.0 + (level - 15) * 3.0;
        if (level <= 50) return 15 * 2.0 + 15 * 3.0 + (level - 30) * 4.0;
        return 15 * 2.0 + 15 * 3.0 + 20 * 4.0 + (level - 50) * 5.0;
    }
    
    public double getTotalXPRequiredForLevel(int level) {
        double total = 0;
        for (int i = 2; i <= level; i++) {
            total += getXPRequiredForLevel(i);
        }
        return total;
    }
    
    public int getTotalLevel() {
        return skillLevels.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getTotalXP() {
        return totalXP.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    public SkillType getHighestSkill() {
        return skillLevels.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(SkillType.FARMING);
    }
    
    public SkillType getLowestSkill() {
        return skillLevels.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(SkillType.FARMING);
    }
    
    public List<SkillType> getSkillsByLevel() {
        List<SkillType> skills = new ArrayList<>(Arrays.asList(SkillType.values()));
        skills.sort((a, b) -> Integer.compare(getLevel(b), getLevel(a)));
        return skills;
    }
    
    public Map<SkillType, Integer> getAllLevels() {
        return new HashMap<>(skillLevels);
    }
    
    public Map<SkillType, Double> getAllXP() {
        return new HashMap<>(skillXP);
    }
    
    public Map<SkillType, Double> getAllTotalXP() {
        return new HashMap<>(totalXP);
    }
    
    public Map<SkillType, Integer> getAllSkillUses() {
        return new HashMap<>(skillUses);
    }
    
    public Map<SkillType, Double> getAllSkillEfficiency() {
        return new HashMap<>(skillEfficiency);
    }
    
    public void resetSkill(SkillType skillType) {
        skillXP.put(skillType, 0.0);
        skillLevels.put(skillType, 1);
        totalXP.put(skillType, 0.0);
        lastActivity.put(skillType, System.currentTimeMillis());
        skillUses.put(skillType, 0);
        skillEfficiency.put(skillType, 1.0);
    }
    
    public void resetAllSkills() {
        for (SkillType skillType : SkillType.values()) {
            resetSkill(skillType);
        }
    }
    
    public boolean hasSkillLevel(SkillType skillType, int requiredLevel) {
        return getLevel(skillType) >= requiredLevel;
    }
    
    public double getSkillBonus(SkillType skillType) {
        int level = getLevel(skillType);
        return level * 0.01; // 1% bonus per level
    }
    
    public double getSkillMultiplier(SkillType skillType) {
        int level = getLevel(skillType);
        return 1.0 + (level * 0.005); // 0.5% multiplier per level
    }
    
    public String getSkillProgressBar(SkillType skillType) {
        double progress = getXPProgress(skillType);
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
    
    public String getSkillSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lSkill Summary:\n");
        
        for (SkillType skillType : getSkillsByLevel()) {
            int level = getLevel(skillType);
            double xp = getXP(skillType);
            double xpToNext = getXPToNextLevel(skillType);
            
            summary.append(skillType.getDisplayName()).append(" §7Level: §e").append(level)
                   .append(" §7XP: §a").append(String.format("%.1f", xp))
                   .append(" §7To Next: §b").append(String.format("%.1f", xpToNext)).append("\n");
        }
        
        return summary.toString();
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    @Override
    public String toString() {
        return "PlayerSkills{" +
                "playerId=" + playerId +
                ", totalLevel=" + getTotalLevel() +
                ", totalXP=" + getTotalXP() +
                ", highestSkill=" + getHighestSkill() +
                '}';
    }
}
