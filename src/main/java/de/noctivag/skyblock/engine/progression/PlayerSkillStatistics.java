package de.noctivag.skyblock.engine.progression;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents comprehensive skill statistics for a player
 */
public class PlayerSkillStatistics {
    private final UUID playerId;
    private final Map<String, SkillStatistics> skills;
    private final LocalDateTime lastUpdated;
    private final int totalLevels;
    private final double totalExp;
    private final Map<String, Object> achievements;

    public PlayerSkillStatistics(UUID playerId) {
        this.playerId = playerId;
        this.skills = new HashMap<>();
        this.lastUpdated = LocalDateTime.now();
        this.totalLevels = 0;
        this.totalExp = 0.0;
        this.achievements = new HashMap<>();
    }

    public PlayerSkillStatistics(UUID playerId, Map<String, SkillStatistics> skills) {
        this.playerId = playerId;
        this.skills = new HashMap<>(skills);
        this.lastUpdated = LocalDateTime.now();
        this.totalLevels = skills.values().stream().mapToInt(SkillStatistics::getLevel).sum();
        this.totalExp = skills.values().stream().mapToDouble(SkillStatistics::getExperience).sum();
        this.achievements = new HashMap<>();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<String, SkillStatistics> getSkills() {
        return new HashMap<>(skills);
    }

    public SkillStatistics getSkill(String skillName) {
        return skills.get(skillName);
    }

    public void setSkill(String skillName, SkillStatistics statistics) {
        skills.put(skillName, statistics);
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public double getTotalExp() {
        return totalExp;
    }

    public Map<String, Object> getAchievements() {
        return new HashMap<>(achievements);
    }

    public void setAchievement(String achievementId, Object value) {
        achievements.put(achievementId, value);
    }

    public int getSkillLevel(String skillName) {
        SkillStatistics stats = skills.get(skillName);
        return stats != null ? stats.getLevel() : 0;
    }

    public double getSkillExperience(String skillName) {
        SkillStatistics stats = skills.get(skillName);
        return stats != null ? stats.getExperience() : 0.0;
    }

    public double getAverageLevel() {
        if (skills.isEmpty()) return 0.0;
        return (double) totalLevels / skills.size();
    }

    public String getHighestSkill() {
        return skills.entrySet().stream()
                .max(Map.Entry.comparingByValue((s1, s2) -> Integer.compare(s1.getLevel(), s2.getLevel())))
                .map(Map.Entry::getKey)
                .orElse("None");
    }

    public String getLowestSkill() {
        return skills.entrySet().stream()
                .min(Map.Entry.comparingByValue((s1, s2) -> Integer.compare(s1.getLevel(), s2.getLevel())))
                .map(Map.Entry::getKey)
                .orElse("None");
    }

    public boolean hasSkill(String skillName) {
        return skills.containsKey(skillName);
    }

    public int getSkillCount() {
        return skills.size();
    }

    @Override
    public String toString() {
        return String.format("PlayerSkillStatistics{playerId=%s, totalLevels=%d, totalExp=%.1f, skills=%d, lastUpdated=%s}", 
                           playerId, totalLevels, totalExp, skills.size(), lastUpdated);
    }
}
