package de.noctivag.plugin.features.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.skills.types.SkillType;

/**
 * Configuration for a skill
 */
public class SkillConfig {
    
    private final SkillType skillType;
    private final String displayName;
    private final String description;
    private final int maxLevel;
    private final double baseXPRequirement;
    
    public SkillConfig(SkillType skillType, String displayName, String description, int maxLevel, double baseXPRequirement) {
        this.skillType = skillType;
        this.displayName = displayName;
        this.description = description;
        this.maxLevel = maxLevel;
        this.baseXPRequirement = baseXPRequirement;
    }
    
    public SkillType getSkillType() {
        return skillType;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public double getBaseXPRequirement() {
        return baseXPRequirement;
    }
    
    /**
     * Calculate XP required for a specific level
     */
    public double calculateXPForLevel(int level) {
        if (level <= 0) return 0;
        if (level > maxLevel) return Double.MAX_VALUE;
        
        double totalXP = 0;
        for (int i = 0; i < level; i++) {
            totalXP += baseXPRequirement * Math.pow(1.5, i);
        }
        
        return totalXP;
    }
    
    /**
     * Calculate total XP required to reach max level
     */
    public double getTotalXPToMaxLevel() {
        return calculateXPForLevel(maxLevel);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - Max Level: %d", displayName, skillType, maxLevel);
    }
}
