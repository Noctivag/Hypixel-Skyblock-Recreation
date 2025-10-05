package de.noctivag.skyblock.engine.progression.types;
import java.util.UUID;

import java.util.UUID;

/**
 * Skill Requirement Implementation
 * 
 * Represents a requirement for a minimum skill level in a specific skill.
 * This is the most common type of requirement in the progression system.
 */
public class SkillRequirement extends ContentRequirement {
    
    private final HypixelSkillType skillType;
    private final int requiredLevel;
    
    public SkillRequirement(String contentId, String name, String description, 
                           HypixelSkillType skillType, int requiredLevel) {
        super(contentId, name, description, RequirementType.SKILL_LEVEL);
        this.skillType = skillType;
        this.requiredLevel = requiredLevel;
    }
    
    public HypixelSkillType getSkillType() {
        return skillType;
    }
    
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    @Override
    public Object getCurrentValue(UUID playerId) {
        // TODO: Integrate with skill system to get actual player level
        return 0; // Placeholder
    }
    
    @Override
    public Object getRequiredValue() {
        return requiredLevel;
    }
    
    @Override
    public boolean isMet(UUID playerId) {
        // TODO: Integrate with skill system to check actual player level
        return false; // Placeholder
    }
    
    @Override
    public String toString() {
        return String.format("%s Level %d (Currently: %s)", 
            skillType.getDisplayName(), requiredLevel, getCurrentValue(null));
    }
}
