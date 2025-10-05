package de.noctivag.skyblock.features.skills;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.skills.types.SkillType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player's skill progress across all skills
 */
public class PlayerSkills {
    
    private final UUID playerId;
    private final Map<SkillType, SkillProgress> skills = new ConcurrentHashMap<>();
    
    public PlayerSkills(UUID playerId) {
        this.playerId = playerId;
        initializeSkills();
    }
    
    /**
     * Get skill progress for a specific skill
     */
    public SkillProgress getSkill(SkillType skillType) {
        return skills.computeIfAbsent(skillType, k -> new SkillProgress(skillType));
    }
    
    /**
     * Get all skills
     */
    public Map<SkillType, SkillProgress> getAllSkills() {
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
        return skills.values().stream()
            .mapToDouble(SkillProgress::getTotalExperience)
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
     * Get skills by level range
     */
    public Map<SkillType, SkillProgress> getSkillsByLevelRange(int minLevel, int maxLevel) {
        Map<SkillType, SkillProgress> filteredSkills = new ConcurrentHashMap<>();
        
        for (Map.Entry<SkillType, SkillProgress> entry : skills.entrySet()) {
            int level = entry.getValue().getLevel();
            if (level >= minLevel && level <= maxLevel) {
                filteredSkills.put(entry.getKey(), entry.getValue());
            }
        }
        
        return filteredSkills;
    }
    
    /**
     * Get skills that need attention (low level)
     */
    public Map<SkillType, SkillProgress> getSkillsNeedingAttention(int maxLevel) {
        return getSkillsByLevelRange(0, maxLevel);
    }
    
    /**
     * Get skills that are well-developed (high level)
     */
    public Map<SkillType, SkillProgress> getWellDevelopedSkills(int minLevel) {
        return getSkillsByLevelRange(minLevel, Integer.MAX_VALUE);
    }
    
    /**
     * Check if player meets skill requirement
     */
    public boolean meetsRequirement(SkillRequirement requirement) {
        if (requirement == null) return true;
        
        SkillProgress skill = getSkill(requirement.getSkillType());
        return skill.getLevel() >= requirement.getRequiredLevel();
    }
    
    /**
     * Get skill power (weighted average based on skill importance)
     */
    public double getSkillPower() {
        double totalWeightedLevel = 0.0;
        double totalWeight = 0.0;
        
        for (SkillProgress progress : skills.values()) {
            double weight = progress.getSkillType().getWeight();
            totalWeightedLevel += progress.getLevel() * weight;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? totalWeightedLevel / totalWeight : 0.0;
    }
    
    /**
     * Get skill distribution
     */
    public Map<String, Integer> getSkillDistribution() {
        Map<String, Integer> distribution = new ConcurrentHashMap<>();
        
        for (SkillProgress progress : skills.values()) {
            String category = progress.getSkillType().getCategory().getDisplayName();
            distribution.put(category, distribution.getOrDefault(category, 0) + progress.getLevel());
        }
        
        return distribution;
    }
    
    /**
     * Get skill efficiency (experience per hour)
     */
    public double getSkillEfficiency() {
        if (skills.isEmpty()) return 0.0;
        
        return skills.values().stream()
            .mapToDouble(SkillProgress::getEfficiency)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Initialize all skills
     */
    private void initializeSkills() {
        for (SkillType skillType : SkillType.values()) {
            skills.put(skillType, new SkillProgress(skillType));
        }
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
}
