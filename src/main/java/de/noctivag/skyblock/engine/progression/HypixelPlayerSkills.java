package de.noctivag.skyblock.engine.progression;

import de.noctivag.skyblock.engine.progression.types.HypixelSkillType;
import de.noctivag.skyblock.engine.progression.types.SkillLevelData;
import de.noctivag.skyblock.engine.progression.types.SkillProgress;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hypixel Player Skills
 * 
 * Manages skill progression for a single player across all skill types.
 * Integrates with Redis caching for GIM system compatibility.
 */
public class HypixelPlayerSkills {
    
    private final UUID playerId;
    private final Map<HypixelSkillType, SkillProgress> skills;
    private final Map<HypixelSkillType, Double> totalExperience;
    private final Map<HypixelSkillType, Long> lastActivity;
    
    public HypixelPlayerSkills(UUID playerId) {
        this.playerId = playerId;
        this.skills = new ConcurrentHashMap<>();
        this.totalExperience = new ConcurrentHashMap<>();
        this.lastActivity = new ConcurrentHashMap<>();
        
        initializeSkills();
    }
    
    /**
     * Initialize all skills for the player
     */
    private void initializeSkills() {
        for (HypixelSkillType skillType : HypixelSkillType.values()) {
            skills.put(skillType, new SkillProgress(skillType, 0.0));
            totalExperience.put(skillType, 0.0);
            lastActivity.put(skillType, System.currentTimeMillis());
        }
    }
    
    /**
     * Add experience to a skill
     */
    public boolean addExperience(HypixelSkillType skillType, double experience) {
        if (experience <= 0) return false;
        
        SkillProgress progress = skills.get(skillType);
        if (progress == null) {
            progress = new SkillProgress(skillType, 0.0);
            skills.put(skillType, progress);
        }
        
        // Apply XP multiplier
        double adjustedExperience = experience * skillType.getXPMultiplier();
        
        // Add experience
        progress.addExperience(adjustedExperience);
        totalExperience.put(skillType, totalExperience.get(skillType) + adjustedExperience);
        lastActivity.put(skillType, System.currentTimeMillis());
        
        return true;
    }
    
    /**
     * Get skill level for a specific skill
     */
    public int getSkillLevel(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null ? progress.getLevel() : 0;
    }
    
    /**
     * Get total experience for a specific skill
     */
    public double getTotalExperience(HypixelSkillType skillType) {
        return totalExperience.getOrDefault(skillType, 0.0);
    }
    
    /**
     * Get experience required for next level
     */
    public double getExperienceToNextLevel(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null ? progress.getExperienceToNextLevel() : 0.0;
    }
    
    /**
     * Get XP progress within current level
     */
    public double getXPProgressInLevel(HypixelSkillType skillType) {
        SkillProgress progress = skills.get(skillType);
        return progress != null ? progress.getXPProgressInLevel() : 0.0;
    }
    
    /**
     * Get last activity time for a skill
     */
    public long getLastActivity(HypixelSkillType skillType) {
        return lastActivity.getOrDefault(skillType, 0L);
    }
    
    /**
     * Get all skills
     */
    public Map<HypixelSkillType, SkillProgress> getAllSkills() {
        return new ConcurrentHashMap<>(skills);
    }
    
    /**
     * Get total skill level across all skills
     */
    public int getTotalSkillLevel() {
        return skills.values().stream()
            .mapToInt(SkillProgress::getLevel)
            .sum();
    }
    
    /**
     * Get total experience across all skills
     */
    public double getTotalExperience() {
        return totalExperience.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }
    
    /**
     * Get average skill level
     */
    public double getAverageLevel() {
        if (skills.isEmpty()) return 0.0;
        
        return skills.values().stream()
            .mapToInt(SkillProgress::getLevel)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Get highest skill level
     */
    public int getHighestSkillLevel() {
        return skills.values().stream()
            .mapToInt(SkillProgress::getLevel)
            .max()
            .orElse(0);
    }
    
    /**
     * Get lowest skill level
     */
    public int getLowestSkillLevel() {
        return skills.values().stream()
            .mapToInt(SkillProgress::getLevel)
            .min()
            .orElse(0);
    }
    
    /**
     * Get skill with highest level
     */
    public HypixelSkillType getHighestSkill() {
        return skills.entrySet().stream()
            .max(Map.Entry.comparingByValue(Comparator.comparingInt(SkillProgress::getLevel)))
            .map(Map.Entry::getKey)
            .orElse(HypixelSkillType.FARMING);
    }
    
    /**
     * Get skill with lowest level
     */
    public HypixelSkillType getLowestSkill() {
        return skills.entrySet().stream()
            .min(Map.Entry.comparingByValue(Comparator.comparingInt(SkillProgress::getLevel)))
            .map(Map.Entry::getKey)
            .orElse(HypixelSkillType.FARMING);
    }
    
    /**
     * Calculate skill power (used in GIM system)
     */
    public double calculateSkillPower() {
        double totalPower = 0.0;
        
        for (Map.Entry<HypixelSkillType, SkillProgress> entry : skills.entrySet()) {
            HypixelSkillType skillType = entry.getKey();
            SkillProgress progress = entry.getValue();
            
            double skillPower = progress.getLevel() * skillType.getWeight();
            totalPower += skillPower;
        }
        
        return totalPower;
    }
    
    /**
     * Get skills by category
     */
    public Map<HypixelSkillType, SkillProgress> getSkillsByCategory(HypixelSkillType.SkillCategory category) {
        Map<HypixelSkillType, SkillProgress> categorySkills = new HashMap<>();
        
        for (Map.Entry<HypixelSkillType, SkillProgress> entry : skills.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                categorySkills.put(entry.getKey(), entry.getValue());
            }
        }
        
        return categorySkills;
    }
    
    /**
     * Get skill statistics
     */
    public SkillStatistics getSkillStatistics() {
        return new SkillStatistics(this);
    }
    
    /**
     * Get player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Get skills sorted by level (highest first)
     */
    public List<Map.Entry<HypixelSkillType, SkillProgress>> getSkillsSortedByLevel() {
        return skills.entrySet().stream()
            .sorted(Map.Entry.<HypixelSkillType, SkillProgress>comparingByValue(
                Comparator.comparingInt(SkillProgress::getLevel)).reversed())
            .toList();
    }
    
    /**
     * Get skills sorted by experience (highest first)
     */
    public List<Map.Entry<HypixelSkillType, Double>> getSkillsSortedByExperience() {
        return totalExperience.entrySet().stream()
            .sorted(Map.Entry.<HypixelSkillType, Double>comparingByValue().reversed())
            .toList();
    }
    
    /**
     * Check if player has reached a specific level in any skill
     */
    public boolean hasReachedLevel(int level) {
        return skills.values().stream()
            .anyMatch(progress -> progress.getLevel() >= level);
    }
    
    /**
     * Check if player has reached a specific level in a specific skill
     */
    public boolean hasReachedLevel(HypixelSkillType skillType, int level) {
        SkillProgress progress = skills.get(skillType);
        return progress != null && progress.getLevel() >= level;
    }
    
    /**
     * Get number of skills at max level
     */
    public int getMaxLevelSkills() {
        return (int) skills.values().stream()
            .filter(progress -> progress.getLevel() >= progress.getSkillType().getMaxLevel())
            .count();
    }
    
    /**
     * Get total skill weight (for GIM system)
     */
    public double getTotalSkillWeight() {
        return skills.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getWeight() * entry.getValue().getLevel())
            .sum();
    }
}
