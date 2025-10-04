package de.noctivag.skyblock.engine.progression.types;

import java.util.List;
import java.util.UUID;

/**
 * Content Requirement Base Class
 * 
 * Abstract base class for all types of content requirements.
 * Defines the interface for checking if players meet specific
 * requirements for accessing game content.
 */
public abstract class ContentRequirement {
    
    private final String contentId;
    private final String name;
    private final String description;
    private final RequirementType type;
    private final List<ContentRequirement> subRequirements;
    
    protected ContentRequirement(String contentId, String name, String description, RequirementType type) {
        this.contentId = contentId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.subRequirements = List.of();
    }
    
    protected ContentRequirement(String contentId, String name, String description, 
                                RequirementType type, List<ContentRequirement> subRequirements) {
        this.contentId = contentId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.subRequirements = subRequirements != null ? List.copyOf(subRequirements) : List.of();
    }
    
    public String getContentId() {
        return contentId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public RequirementType getType() {
        return type;
    }
    
    public List<ContentRequirement> getSubRequirements() {
        return subRequirements;
    }
    
    /**
     * Get the current value of this requirement for a player
     */
    public abstract Object getCurrentValue(UUID playerId);
    
    /**
     * Get the required value for this requirement
     */
    public abstract Object getRequiredValue();
    
    /**
     * Check if a player meets this requirement
     */
    public abstract boolean isMet(UUID playerId);
    
    @Override
    public String toString() {
        return name + ": " + description;
    }
}
