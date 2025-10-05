package de.noctivag.skyblock.engine.progression.types;
import java.util.UUID;

import java.util.Map;
import java.util.UUID;

/**
 * Player Skill Statistics
 * 
 * Contains comprehensive statistics about a player's skill progression
 * across all skills, including totals, milestones, and achievements.
 */
public class PlayerSkillStatistics {
    
    private final UUID playerId;
    private final Map<HypixelSkillType, SkillProgress> skills;
    private final int totalLevels;
    private final double totalXP;
    private final int skillsAtMaxLevel;
    private final int milestoneLevelsReached;
    
    public PlayerSkillStatistics(UUID playerId, Map<HypixelSkillType, SkillProgress> skills,
                                 int totalLevels, double totalXP, int skillsAtMaxLevel, 
                                 int milestoneLevelsReached) {
        this.playerId = playerId;
        this.skills = skills;
        this.totalLevels = totalLevels;
        this.totalXP = totalXP;
        this.skillsAtMaxLevel = skillsAtMaxLevel;
        this.milestoneLevelsReached = milestoneLevelsReached;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public Map<HypixelSkillType, SkillProgress> getSkills() {
        return skills;
    }
    
    public int getTotalLevels() {
        return totalLevels;
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public int getSkillsAtMaxLevel() {
        return skillsAtMaxLevel;
    }
    
    public int getMilestoneLevelsReached() {
        return milestoneLevelsReached;
    }
    
    public int getTotalSkills() {
        return skills.size();
    }
    
    public double getAverageLevel() {
        if (skills.isEmpty()) return 0.0;
        return (double) totalLevels / skills.size();
    }
    
    public double getAverageXP() {
        if (skills.isEmpty()) return 0.0;
        return totalXP / skills.size();
    }
    
    public double getMaxLevelPercentage() {
        int totalMaxLevels = skills.values().stream()
            .mapToInt(skill -> skill.getSkillType().getMaxLevel())
            .sum();
        
        if (totalMaxLevels == 0) return 100.0;
        return (double) totalLevels / totalMaxLevels * 100.0;
    }
    
    public HypixelSkillType getHighestLevelSkill() {
        return skills.entrySet().stream()
            .max(Map.Entry.comparingByValue((s1, s2) -> Integer.compare(s1.getLevel(), s2.getLevel())))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    public HypixelSkillType getLowestLevelSkill() {
        return skills.entrySet().stream()
            .min(Map.Entry.comparingByValue((s1, s2) -> Integer.compare(s1.getLevel(), s2.getLevel())))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    public int getSkillLevel(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null ? progress.getLevel() : 0;
    }
    
    public double getSkillXP(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null ? progress.getTotalExperience() : 0.0;
    }
    
    public boolean isSkillAtMaxLevel(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null && progress.getLevel() >= skillType.getMaxLevel();
    }
    
    public double getSkillProgressPercentage(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        if (progress == null) return 0.0;
        return progress.getXPProgressInLevel() * 100.0;
    }
    
    public double getXPToNextLevel(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null ? progress.getExperienceToNextLevel() : 0.0;
    }
    
    @Override
    public String toString() {
        return String.format("PlayerSkillStatistics{playerId=%s, totalLevels=%d, totalXP=%.1f, skillsAtMaxLevel=%d, milestoneLevelsReached=%d, averageLevel=%.1f}", 
            playerId, totalLevels, totalXP, skillsAtMaxLevel, milestoneLevelsReached, getAverageLevel());
    }
}
