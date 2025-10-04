package de.noctivag.skyblock.features.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.skills.types.SkillType;

/**
 * Represents a skill requirement for an item or action
 */
public class SkillRequirement {
    
    private final SkillType skillType;
    private final int requiredLevel;
    private final String description;
    
    public SkillRequirement(SkillType skillType, int requiredLevel) {
        this(skillType, requiredLevel, "Requires " + skillType.getDisplayName() + " level " + requiredLevel);
    }
    
    public SkillRequirement(SkillType skillType, int requiredLevel, String description) {
        this.skillType = skillType;
        this.requiredLevel = requiredLevel;
        this.description = description;
    }
    
    public SkillType getSkillType() {
        return skillType;
    }
    
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if a level meets this requirement
     */
    public boolean meetsRequirement(int level) {
        return level >= requiredLevel;
    }
    
    /**
     * Get missing levels for requirement
     */
    public int getMissingLevels(int currentLevel) {
        return Math.max(0, requiredLevel - currentLevel);
    }
    
    @Override
    public String toString() {
        return skillType.getIcon() + " " + skillType.getDisplayName() + " " + requiredLevel;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SkillRequirement that = (SkillRequirement) obj;
        return requiredLevel == that.requiredLevel && skillType == that.skillType;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(skillType, requiredLevel);
    }
}
