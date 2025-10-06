package de.noctivag.skyblock.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a player's skill data and progression
 */
public class PlayerSkills {
    private final UUID playerId;
    private final Map<SkillType, Long> skillXP;
    private final Map<SkillType, Integer> skillLevels;
    private final Map<SkillType, Long> lastUpdated;

    public PlayerSkills(UUID playerId) {
        this.playerId = playerId;
        this.skillXP = new HashMap<>();
        this.skillLevels = new HashMap<>();
        this.lastUpdated = new HashMap<>();
        
        // Initialize all skills with level 0
        for (SkillType skill : SkillType.values()) {
            skillXP.put(skill, 0L);
            skillLevels.put(skill, 0);
            lastUpdated.put(skill, System.currentTimeMillis());
        }
    }

    public PlayerSkills(UUID playerId, Map<SkillType, Long> skillXP, Map<SkillType, Integer> skillLevels) {
        this.playerId = playerId;
        this.skillXP = new HashMap<>(skillXP);
        this.skillLevels = new HashMap<>(skillLevels);
        this.lastUpdated = new HashMap<>();
        
        // Initialize lastUpdated for all skills
        for (SkillType skill : SkillType.values()) {
            lastUpdated.put(skill, System.currentTimeMillis());
        }
        
        // Recalculate levels based on XP
        recalculateAllLevels();
    }

    /**
     * Add XP to a specific skill
     */
    public boolean addXP(SkillType skill, long xp) {
        if (xp <= 0) return false;
        
        long currentXP = skillXP.getOrDefault(skill, 0L);
        int currentLevel = skillLevels.getOrDefault(skill, 0);
        
        long newXP = currentXP + xp;
        int newLevel = skill.getLevelFromXP(newXP);
        
        skillXP.put(skill, newXP);
        skillLevels.put(skill, newLevel);
        lastUpdated.put(skill, System.currentTimeMillis());
        
        // Return true if level increased
        return newLevel > currentLevel;
    }

    /**
     * Set XP for a specific skill
     */
    public void setXP(SkillType skill, long xp) {
        skillXP.put(skill, Math.max(0, xp));
        skillLevels.put(skill, skill.getLevelFromXP(xp));
        lastUpdated.put(skill, System.currentTimeMillis());
    }

    /**
     * Get XP for a specific skill
     */
    public long getXP(SkillType skill) {
        return skillXP.getOrDefault(skill, 0L);
    }

    /**
     * Get level for a specific skill
     */
    public int getLevel(SkillType skill) {
        return skillLevels.getOrDefault(skill, 0);
    }

    /**
     * Get XP progress for current level
     */
    public long getXPProgress(SkillType skill) {
        long totalXP = getXP(skill);
        int level = getLevel(skill);
        return skill.getXPProgressForLevel(totalXP, level);
    }

    /**
     * Get XP required for next level
     */
    public long getXPRequiredForNextLevel(SkillType skill) {
        long totalXP = getXP(skill);
        int level = getLevel(skill);
        return skill.getXPRequiredForNextLevel(totalXP, level);
    }

    /**
     * Get total XP across all skills
     */
    public long getTotalXP() {
        return skillXP.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Get total level across all skills
     */
    public int getTotalLevel() {
        return skillLevels.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get average skill level
     */
    public double getAverageLevel() {
        return skillLevels.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    /**
     * Get skill bonuses for a specific skill
     */
    public SkillBonuses getBonuses(SkillType skill) {
        int level = getLevel(skill);
        return skill.getBonusesForLevel(level);
    }

    /**
     * Get combined bonuses from all skills
     */
    public SkillBonuses getCombinedBonuses() {
        SkillBonuses[] bonuses = new SkillBonuses[SkillType.values().length];
        for (int i = 0; i < SkillType.values().length; i++) {
            bonuses[i] = getBonuses(SkillType.values()[i]);
        }
        return SkillBonuses.combine(bonuses);
    }

    /**
     * Get the highest level skill
     */
    public SkillType getHighestLevelSkill() {
        SkillType highest = SkillType.COMBAT;
        int highestLevel = 0;
        
        for (Map.Entry<SkillType, Integer> entry : skillLevels.entrySet()) {
            if (entry.getValue() > highestLevel) {
                highestLevel = entry.getValue();
                highest = entry.getKey();
            }
        }
        
        return highest;
    }

    /**
     * Get the lowest level skill
     */
    public SkillType getLowestLevelSkill() {
        SkillType lowest = SkillType.COMBAT;
        int lowestLevel = Integer.MAX_VALUE;
        
        for (Map.Entry<SkillType, Integer> entry : skillLevels.entrySet()) {
            if (entry.getValue() < lowestLevel) {
                lowestLevel = entry.getValue();
                lowest = entry.getKey();
            }
        }
        
        return lowest;
    }

    /**
     * Check if player has reached a specific level in any skill
     */
    public boolean hasReachedLevel(int level) {
        return skillLevels.values().stream().anyMatch(l -> l >= level);
    }

    /**
     * Check if player has reached a specific level in a specific skill
     */
    public boolean hasReachedLevel(SkillType skill, int level) {
        return getLevel(skill) >= level;
    }

    /**
     * Get skill level distribution
     */
    public Map<Integer, Integer> getLevelDistribution() {
        Map<Integer, Integer> distribution = new HashMap<>();
        for (int level : skillLevels.values()) {
            distribution.put(level, distribution.getOrDefault(level, 0) + 1);
        }
        return distribution;
    }

    /**
     * Recalculate all skill levels based on current XP
     */
    private void recalculateAllLevels() {
        for (SkillType skill : SkillType.values()) {
            long xp = skillXP.getOrDefault(skill, 0L);
            skillLevels.put(skill, skill.getLevelFromXP(xp));
        }
    }

    /**
     * Get all skill data as a map
     */
    public Map<SkillType, Long> getAllSkillXP() {
        return new HashMap<>(skillXP);
    }

    /**
     * Get all skill levels as a map
     */
    public Map<SkillType, Integer> getAllSkillLevels() {
        return new HashMap<>(skillLevels);
    }

    /**
     * Get last updated timestamp for a skill
     */
    public long getLastUpdated(SkillType skill) {
        return lastUpdated.getOrDefault(skill, 0L);
    }

    // Getters
    public UUID getPlayerId() {
        return playerId;
    }
}