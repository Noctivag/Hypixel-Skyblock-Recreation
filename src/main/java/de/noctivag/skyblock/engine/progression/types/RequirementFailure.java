package de.noctivag.skyblock.engine.progression.types;

/**
 * Requirement Failure
 * 
 * Represents a specific requirement that was not met,
 * including details about what was required and what the player has.
 */
public class RequirementFailure {
    
    private final RequirementType requirementType;
    private final String description;
    private final Object currentValue;
    private final Object requiredValue;
    
    public RequirementFailure(RequirementType requirementType, String description, 
                             Object currentValue, Object requiredValue) {
        this.requirementType = requirementType;
        this.description = description;
        this.currentValue = currentValue;
        this.requiredValue = requiredValue;
    }
    
    public RequirementType getRequirementType() {
        return requirementType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Object getCurrentValue() {
        return currentValue;
    }
    
    public Object getRequiredValue() {
        return requiredValue;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s (Current: %s, Required: %s)", 
            requirementType.getDisplayName(), description, currentValue, requiredValue);
    }
}
