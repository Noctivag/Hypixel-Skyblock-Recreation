package de.noctivag.skyblock.engine.progression;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents statistics for a specific skill
 */
public class SkillStatistics {
    private final String skillName;
    private final int level;
    private final double experience;
    private final double experienceToNext;
    private final LocalDateTime lastGained;
    private final Map<String, Object> metadata;

    public SkillStatistics(String skillName, int level, double experience, double experienceToNext) {
        this.skillName = skillName;
        this.level = level;
        this.experience = experience;
        this.experienceToNext = experienceToNext;
        this.lastGained = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }

    public SkillStatistics(String skillName, int level, double experience) {
        this.skillName = skillName;
        this.level = level;
        this.experience = experience;
        this.experienceToNext = calculateExpToNext(level);
        this.lastGained = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }

    public String getSkillName() {
        return skillName;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public double getExperienceToNext() {
        return experienceToNext;
    }

    public LocalDateTime getLastGained() {
        return lastGained;
    }

    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public double getProgressPercentage() {
        double currentLevelExp = getExpForLevel(level);
        double nextLevelExp = getExpForLevel(level + 1);
        return (experience - currentLevelExp) / (nextLevelExp - currentLevelExp) * 100.0;
    }

    public double getTotalExpForLevel() {
        return getExpForLevel(level + 1) - getExpForLevel(level);
    }

    public boolean isMaxLevel() {
        return level >= 50; // Assuming max level is 50
    }

    private double getExpForLevel(int level) {
        // Simplified exp calculation - in real implementation this would be more complex
        return level * 100.0 + (level * level * 10.0);
    }

    private double calculateExpToNext(int currentLevel) {
        return getExpForLevel(currentLevel + 1) - getExpForLevel(currentLevel);
    }

    public SkillStatistics addExperience(double expGained) {
        double newExp = experience + expGained;
        int newLevel = level;
        double newExpToNext = experienceToNext;

        // Check for level up
        while (newExp >= getExpForLevel(newLevel + 1) && newLevel < 50) {
            newLevel++;
            newExpToNext = getExpForLevel(newLevel + 1) - newExp;
        }

        return new SkillStatistics(skillName, newLevel, newExp, newExpToNext);
    }

    @Override
    public String toString() {
        return String.format("SkillStatistics{skill='%s', level=%d, exp=%.1f/%.1f (%.1f%%)}", 
                           skillName, level, experience, experience + experienceToNext, getProgressPercentage());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SkillStatistics that = (SkillStatistics) obj;
        return skillName.equals(that.skillName);
    }

    @Override
    public int hashCode() {
        return skillName.hashCode();
    }
}
