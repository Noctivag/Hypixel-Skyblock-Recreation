package de.noctivag.skyblock.engine.crafting.types;

import de.noctivag.skyblock.engine.collections.types.CollectionType;
import de.noctivag.skyblock.engine.progression.types.HypixelSkillType;

import java.util.UUID;
import java.util.Objects;

/**
 * Recipe Requirement
 * 
 * Represents a requirement that must be met to unlock or craft a recipe.
 * Requirements can be collection-based, skill-based, level-based, or quest-based.
 */
public class RecipeRequirement {
    
    private final RequirementType type;
    private final CollectionType collectionType;
    private final HypixelSkillType skillType;
    private final int requiredAmount;
    private final int requiredLevel;
    private final String questId;
    private final String description;
    
    public RecipeRequirement(CollectionType collectionType, int requiredAmount) {
        this.type = RequirementType.COLLECTION;
        this.collectionType = collectionType;
        this.skillType = null;
        this.requiredAmount = requiredAmount;
        this.requiredLevel = 0;
        this.questId = null;
        this.description = "Collect " + requiredAmount + " " + collectionType.getDisplayName();
    }
    
    public RecipeRequirement(HypixelSkillType skillType, int requiredLevel) {
        this.type = RequirementType.SKILL;
        this.collectionType = null;
        this.skillType = skillType;
        this.requiredAmount = 0;
        this.requiredLevel = requiredLevel;
        this.questId = null;
        this.description = "Reach " + skillType.getDisplayName() + " Level " + requiredLevel;
    }
    
    public RecipeRequirement(int requiredLevel) {
        this.type = RequirementType.LEVEL;
        this.collectionType = null;
        this.skillType = null;
        this.requiredAmount = 0;
        this.requiredLevel = requiredLevel;
        this.questId = null;
        this.description = "Reach Level " + requiredLevel;
    }
    
    public RecipeRequirement(String questId) {
        this.type = RequirementType.QUEST;
        this.collectionType = null;
        this.skillType = null;
        this.requiredAmount = 0;
        this.requiredLevel = 0;
        this.questId = questId;
        this.description = "Complete Quest: " + questId;
    }
    
    public RequirementType getType() {
        return type;
    }
    
    public CollectionType getCollectionType() {
        return collectionType;
    }
    
    public HypixelSkillType getSkillType() {
        return skillType;
    }
    
    public int getRequiredAmount() {
        return requiredAmount;
    }
    
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    public String getQuestId() {
        return questId;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if this requirement is met by a player
     */
    public boolean isMet(UUID playerId) {
        return switch (type) {
            case COLLECTION -> checkCollectionRequirement(playerId);
            case SKILL -> checkSkillRequirement(playerId);
            case LEVEL -> checkLevelRequirement(playerId);
            case QUEST -> checkQuestRequirement(playerId);
        };
    }
    
    /**
     * Check collection requirement
     */
    private boolean checkCollectionRequirement(UUID playerId) {
        if (collectionType == null) return false;
        
        // TODO: Implement collection checking logic
        // This would check if the player has collected the required amount
        return true; // Placeholder
    }
    
    /**
     * Check skill requirement
     */
    private boolean checkSkillRequirement(UUID playerId) {
        if (skillType == null) return false;
        
        // TODO: Implement skill checking logic
        // This would check if the player has reached the required skill level
        return true; // Placeholder
    }
    
    /**
     * Check level requirement
     */
    private boolean checkLevelRequirement(UUID playerId) {
        // TODO: Implement level checking logic
        // This would check if the player has reached the required level
        return true; // Placeholder
    }
    
    /**
     * Check quest requirement
     */
    private boolean checkQuestRequirement(UUID playerId) {
        if (questId == null) return false;
        
        // TODO: Implement quest checking logic
        // This would check if the player has completed the required quest
        return true; // Placeholder
    }
    
    /**
     * Get requirement progress for a player
     */
    public double getProgress(UUID playerId) {
        return switch (type) {
            case COLLECTION -> getCollectionProgress(playerId);
            case SKILL -> getSkillProgress(playerId);
            case LEVEL -> getLevelProgress(playerId);
            case QUEST -> getQuestProgress(playerId);
        };
    }
    
    /**
     * Get collection requirement progress
     */
    private double getCollectionProgress(UUID playerId) {
        if (collectionType == null || requiredAmount <= 0) return 0.0;
        
        // TODO: Implement collection progress checking
        // This would return the percentage of collection progress
        return 0.0; // Placeholder
    }
    
    /**
     * Get skill requirement progress
     */
    private double getSkillProgress(UUID playerId) {
        if (skillType == null || requiredLevel <= 0) return 0.0;
        
        // TODO: Implement skill progress checking
        // This would return the percentage of skill progress
        return 0.0; // Placeholder
    }
    
    /**
     * Get level requirement progress
     */
    private double getLevelProgress(UUID playerId) {
        if (requiredLevel <= 0) return 0.0;
        
        // TODO: Implement level progress checking
        // This would return the percentage of level progress
        return 0.0; // Placeholder
    }
    
    /**
     * Get quest requirement progress
     */
    private double getQuestProgress(UUID playerId) {
        if (questId == null) return 0.0;
        
        // TODO: Implement quest progress checking
        // This would return the percentage of quest progress
        return 0.0; // Placeholder
    }
    
    /**
     * Get formatted requirement description
     */
    public String getFormattedDescription() {
        return switch (type) {
            case COLLECTION -> String.format("§eCollect %,d %s", requiredAmount, collectionType.getDisplayName());
            case SKILL -> String.format("§bReach %s Level %d", skillType.getDisplayName(), requiredLevel);
            case LEVEL -> String.format("§aReach Level %d", requiredLevel);
            case QUEST -> String.format("§dComplete Quest: %s", questId);
        };
    }
    
    /**
     * Get requirement icon based on type
     */
    public String getIcon() {
        return switch (type) {
            case COLLECTION -> "📦";
            case SKILL -> "⭐";
            case LEVEL -> "📈";
            case QUEST -> "📜";
        };
    }
    
    /**
     * Get requirement color based on type
     */
    public String getColor() {
        return switch (type) {
            case COLLECTION -> "§e"; // Yellow
            case SKILL -> "§b"; // Aqua
            case LEVEL -> "§a"; // Green
            case QUEST -> "§d"; // Light Purple
        };
    }
    
    /**
     * Get formatted requirement with icon and color
     */
    public String getFormattedRequirement() {
        return getColor() + getIcon() + " " + getFormattedDescription();
    }
    
    /**
     * Get requirement weight for requirement power calculation
     */
    public double getWeight() {
        return switch (type) {
            case COLLECTION -> requiredAmount / 1000.0; // Based on amount
            case SKILL -> requiredLevel * 2.0; // Based on level
            case LEVEL -> requiredLevel * 1.5; // Based on level
            case QUEST -> 10.0; // Fixed weight for quests
        };
    }
    
    /**
     * Get requirement difficulty
     */
    public RequirementDifficulty getDifficulty() {
        double weight = getWeight();
        
        if (weight >= 50.0) {
            return RequirementDifficulty.EXPERT;
        } else if (weight >= 25.0) {
            return RequirementDifficulty.ADVANCED;
        } else if (weight >= 10.0) {
            return RequirementDifficulty.INTERMEDIATE;
        } else {
            return RequirementDifficulty.BASIC;
        }
    }
    
    /**
     * Check if this is a collection requirement
     */
    public boolean isCollectionRequirement() {
        return type == RequirementType.COLLECTION;
    }
    
    /**
     * Check if this is a skill requirement
     */
    public boolean isSkillRequirement() {
        return type == RequirementType.SKILL;
    }
    
    /**
     * Check if this is a level requirement
     */
    public boolean isLevelRequirement() {
        return type == RequirementType.LEVEL;
    }
    
    /**
     * Check if this is a quest requirement
     */
    public boolean isQuestRequirement() {
        return type == RequirementType.QUEST;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RecipeRequirement that = (RecipeRequirement) obj;
        return type == that.type &&
               requiredAmount == that.requiredAmount &&
               requiredLevel == that.requiredLevel &&
               Objects.equals(collectionType, that.collectionType) &&
               Objects.equals(skillType, that.skillType) &&
               Objects.equals(questId, that.questId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, collectionType, skillType, requiredAmount, requiredLevel, questId);
    }
    
    @Override
    public String toString() {
        return String.format("RecipeRequirement{type=%s, requiredAmount=%d, requiredLevel=%d, questId='%s'}", 
            type, requiredAmount, requiredLevel, questId);
    }
    
    /**
     * Requirement types
     */
    public enum RequirementType {
        COLLECTION("Collection"),
        SKILL("Skill"),
        LEVEL("Level"),
        QUEST("Quest");
        
        private final String displayName;
        
        RequirementType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Requirement difficulty levels
     */
    public enum RequirementDifficulty {
        BASIC("Basic"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced"),
        EXPERT("Expert");
        
        private final String displayName;
        
        RequirementDifficulty(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
