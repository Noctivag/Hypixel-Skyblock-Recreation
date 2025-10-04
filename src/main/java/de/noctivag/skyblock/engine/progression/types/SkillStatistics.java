package de.noctivag.skyblock.engine.progression.types;

import de.noctivag.skyblock.engine.progression.HypixelPlayerSkills;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Skill Statistics for a player
 * 
 * Provides comprehensive statistics and analysis of a player's skill progression.
 * Used for GIM system integration and player progress tracking.
 */
public class SkillStatistics {
    
    private final HypixelPlayerSkills playerSkills;
    private final Map<HypixelSkillType, Double> skillEfficiency;
    private final Map<HypixelSkillType.SkillCategory, Double> categoryProgress;
    
    public SkillStatistics(HypixelPlayerSkills playerSkills) {
        this.playerSkills = playerSkills;
        this.skillEfficiency = new HashMap<>();
        this.categoryProgress = new HashMap<>();
        
        calculateStatistics();
    }
    
    /**
     * Calculate all statistics
     */
    private void calculateStatistics() {
        // Calculate skill efficiency for each skill
        for (HypixelSkillType skillType : HypixelSkillType.values()) {
            double efficiency = calculateSkillEfficiency(skillType);
            skillEfficiency.put(skillType, efficiency);
        }
        
        // Calculate category progress
        for (HypixelSkillType.SkillCategory category : HypixelSkillType.SkillCategory.values()) {
            double progress = calculateCategoryProgress(category);
            categoryProgress.put(category, progress);
        }
    }
    
    /**
     * Calculate skill efficiency for a specific skill
     */
    private double calculateSkillEfficiency(HypixelSkillType skillType) {
        int level = playerSkills.getSkillLevel(skillType);
        double totalXP = playerSkills.getTotalExperience(skillType);
        long lastActivity = playerSkills.getLastActivity(skillType);
        
        // Base efficiency from level
        double levelEfficiency = level * 0.01; // 1% per level
        
        // XP efficiency (higher XP = higher efficiency)
        double xpEfficiency = Math.min(totalXP / 1000000.0, 1.0); // Cap at 1M XP
        
        // Activity efficiency (recent activity = higher efficiency)
        long timeSinceActivity = System.currentTimeMillis() - lastActivity;
        double activityEfficiency = Math.max(0.5, 1.0 - (timeSinceActivity / 86400000.0)); // Decay over 24 hours
        
        return (levelEfficiency + xpEfficiency + activityEfficiency) / 3.0;
    }
    
    /**
     * Calculate category progress
     */
    private double calculateCategoryProgress(HypixelSkillType.SkillCategory category) {
        HypixelSkillType[] skills = HypixelSkillType.getByCategory(category);
        if (skills.length == 0) return 0.0;
        
        double totalProgress = 0.0;
        for (HypixelSkillType skill : skills) {
            int level = playerSkills.getSkillLevel(skill);
            int maxLevel = skill.getMaxLevel();
            double progress = (double) level / maxLevel;
            totalProgress += progress;
        }
        
        return totalProgress / skills.length;
    }
    
    /**
     * Get total skill level
     */
    public int getTotalSkillLevel() {
        return playerSkills.getTotalSkillLevel();
    }
    
    /**
     * Get total experience
     */
    public double getTotalExperience() {
        return playerSkills.getTotalExperience();
    }
    
    /**
     * Get average skill level
     */
    public double getAverageLevel() {
        return playerSkills.getAverageLevel();
    }
    
    /**
     * Get highest skill level
     */
    public int getHighestSkillLevel() {
        return playerSkills.getHighestSkillLevel();
    }
    
    /**
     * Get lowest skill level
     */
    public int getLowestSkillLevel() {
        return playerSkills.getLowestSkillLevel();
    }
    
    /**
     * Get skill power (for GIM system)
     */
    public double getSkillPower() {
        return playerSkills.calculateSkillPower();
    }
    
    /**
     * Get total skill weight
     */
    public double getTotalSkillWeight() {
        return playerSkills.getTotalSkillWeight();
    }
    
    /**
     * Get skill efficiency for a specific skill
     */
    public double getSkillEfficiency(HypixelSkillType skillType) {
        return skillEfficiency.getOrDefault(skillType, 0.0);
    }
    
    /**
     * Get category progress
     */
    public double getCategoryProgress(HypixelSkillType.SkillCategory category) {
        return categoryProgress.getOrDefault(category, 0.0);
    }
    
    /**
     * Get most efficient skill
     */
    public HypixelSkillType getMostEfficientSkill() {
        return skillEfficiency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(HypixelSkillType.FARMING);
    }
    
    /**
     * Get least efficient skill
     */
    public HypixelSkillType getLeastEfficientSkill() {
        return skillEfficiency.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(HypixelSkillType.FARMING);
    }
    
    /**
     * Get best category
     */
    public HypixelSkillType.SkillCategory getBestCategory() {
        return categoryProgress.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(HypixelSkillType.SkillCategory.GATHERING);
    }
    
    /**
     * Get worst category
     */
    public HypixelSkillType.SkillCategory getWorstCategory() {
        return categoryProgress.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(HypixelSkillType.SkillCategory.SOCIAL);
    }
    
    /**
     * Get skills sorted by efficiency
     */
    public List<Map.Entry<HypixelSkillType, Double>> getSkillsSortedByEfficiency() {
        return skillEfficiency.entrySet().stream()
            .sorted(Map.Entry.<HypixelSkillType, Double>comparingByValue().reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Get categories sorted by progress
     */
    public List<Map.Entry<HypixelSkillType.SkillCategory, Double>> getCategoriesSortedByProgress() {
        return categoryProgress.entrySet().stream()
            .sorted(Map.Entry.<HypixelSkillType.SkillCategory, Double>comparingByValue().reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Get number of skills at max level
     */
    public int getMaxLevelSkills() {
        return playerSkills.getMaxLevelSkills();
    }
    
    /**
     * Get number of skills at specific level or higher
     */
    public int getSkillsAtLevelOrHigher(int level) {
        return (int) HypixelSkillType.values().length - 
               (int) Arrays.stream(HypixelSkillType.values())
                   .filter(skill -> playerSkills.getSkillLevel(skill) < level)
                   .count();
    }
    
    /**
     * Get skill distribution
     */
    public Map<String, Integer> getSkillDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        
        for (HypixelSkillType skill : HypixelSkillType.values()) {
            int level = playerSkills.getSkillLevel(skill);
            String range = getLevelRange(level);
            distribution.put(range, distribution.getOrDefault(range, 0) + 1);
        }
        
        return distribution;
    }
    
    /**
     * Get level range for a skill level
     */
    private String getLevelRange(int level) {
        if (level == 0) return "0";
        if (level <= 10) return "1-10";
        if (level <= 20) return "11-20";
        if (level <= 30) return "21-30";
        if (level <= 40) return "31-40";
        if (level <= 50) return "41-50";
        return "50+";
    }
    
    /**
     * Get overall skill rating (0-100)
     */
    public int getOverallSkillRating() {
        double totalProgress = 0.0;
        int totalSkills = HypixelSkillType.values().length;
        
        for (HypixelSkillType skill : HypixelSkillType.values()) {
            int level = playerSkills.getSkillLevel(skill);
            int maxLevel = skill.getMaxLevel();
            double progress = (double) level / maxLevel;
            totalProgress += progress;
        }
        
        return (int) Math.round((totalProgress / totalSkills) * 100);
    }
    
    /**
     * Get skill recommendations
     */
    public List<String> getSkillRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        // Find skills that need attention
        for (Map.Entry<HypixelSkillType, Double> entry : skillEfficiency.entrySet()) {
            if (entry.getValue() < 0.3) {
                recommendations.add("Focus on " + entry.getKey().getDisplayName() + 
                    " - efficiency is low");
            }
        }
        
        // Find categories that need attention
        for (Map.Entry<HypixelSkillType.SkillCategory, Double> entry : categoryProgress.entrySet()) {
            if (entry.getValue() < 0.2) {
                recommendations.add("Improve " + entry.getKey().getDisplayName() + 
                    " category skills");
            }
        }
        
        return recommendations;
    }
    
    /**
     * Get formatted statistics summary
     */
    public String getFormattedSummary() {
        return String.format(
            "Skill Statistics Summary:\n" +
            "Total Level: %d\n" +
            "Total XP: %.0f\n" +
            "Average Level: %.1f\n" +
            "Skill Power: %.1f\n" +
            "Overall Rating: %d/100\n" +
            "Max Level Skills: %d\n" +
            "Best Category: %s\n" +
            "Most Efficient: %s",
            getTotalSkillLevel(),
            getTotalExperience(),
            getAverageLevel(),
            getSkillPower(),
            getOverallSkillRating(),
            getMaxLevelSkills(),
            getBestCategory().getDisplayName(),
            getMostEfficientSkill().getDisplayName()
        );
    }
}
