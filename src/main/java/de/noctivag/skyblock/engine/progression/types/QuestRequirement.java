package de.noctivag.skyblock.engine.progression.types;
import java.util.UUID;

import java.util.UUID;

/**
 * Quest Requirement Implementation
 * 
 * Represents a requirement for the completion of specific quests
 * before accessing certain content.
 */
public class QuestRequirement extends ContentRequirement {
    
    private final String questId;
    private final boolean mustBeCompleted;
    
    public QuestRequirement(String contentId, String name, String description, 
                           String questId, boolean mustBeCompleted) {
        super(contentId, name, description, RequirementType.QUEST_COMPLETION);
        this.questId = questId;
        this.mustBeCompleted = mustBeCompleted;
    }
    
    public String getQuestId() {
        return questId;
    }
    
    public boolean isMustBeCompleted() {
        return mustBeCompleted;
    }
    
    @Override
    public Object getCurrentValue(UUID playerId) {
        // TODO: Integrate with quest system to get actual quest status
        return false; // Placeholder
    }
    
    @Override
    public Object getRequiredValue() {
        return mustBeCompleted;
    }
    
    @Override
    public boolean isMet(UUID playerId) {
        // TODO: Integrate with quest system to check actual quest completion
        return false; // Placeholder
    }
    
    @Override
    public String toString() {
        return String.format("Quest: %s (%s)", 
            questId, mustBeCompleted ? "Completed" : "Not Required");
    }
}
