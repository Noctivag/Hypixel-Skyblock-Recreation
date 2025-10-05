package de.noctivag.skyblock.quests;

import java.util.List;
import java.util.Map;

/**
 * Represents a quest
 */
public class Quest {
    
    private final String id;
    private final String name;
    private final String description;
    private final QuestType type;
    private final Difficulty difficulty;
    private final int requiredLevel;
    private final Map<String, Object> rewards;
    private final List<QuestObjective> objectives;
    
    public Quest(String id, String name, String description, QuestType type,
                Difficulty difficulty, int requiredLevel, Map<String, Object> rewards,
                List<QuestObjective> objectives) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.requiredLevel = requiredLevel;
        this.rewards = rewards;
        this.objectives = objectives;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public QuestType getType() { return type; }
    public Difficulty getDifficulty() { return difficulty; }
    public int getRequiredLevel() { return requiredLevel; }
    public Map<String, Object> getRewards() { return rewards; }
    public List<QuestObjective> getObjectives() { return objectives; }
    
    /**
     * Get reward value
     */
    public Object getReward(String key, Object defaultValue) {
        return rewards.getOrDefault(key, defaultValue);
    }
    
    /**
     * Get reward as string
     */
    public String getRewardAsString(String key, String defaultValue) {
        Object value = rewards.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Get reward as integer
     */
    public int getRewardAsInt(String key, int defaultValue) {
        Object value = rewards.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    /**
     * Get reward as double
     */
    public double getRewardAsDouble(String key, double defaultValue) {
        Object value = rewards.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    /**
     * Get reward as boolean
     */
    public boolean getRewardAsBoolean(String key, boolean defaultValue) {
        Object value = rewards.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    /**
     * Get objective by type
     */
    public QuestObjective getObjectiveByType(QuestObjective.ObjectiveType type) {
        return objectives.stream()
            .filter(obj -> obj.getType() == type)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get objectives by type
     */
    public List<QuestObjective> getObjectivesByType(QuestObjective.ObjectiveType type) {
        return objectives.stream()
            .filter(obj -> obj.getType() == type)
            .toList();
    }
    
    /**
     * Check if quest has objective type
     */
    public boolean hasObjectiveType(QuestObjective.ObjectiveType type) {
        return objectives.stream().anyMatch(obj -> obj.getType() == type);
    }
    
    /**
     * Get total objectives count
     */
    public int getObjectivesCount() {
        return objectives.size();
    }
    
    /**
     * Quest type enum
     */
    public enum QuestType {
        MINING,
        COMBAT,
        FARMING,
        FISHING,
        FORAGING,
        ENCHANTING,
        ALCHEMY,
        TAMING,
        DUNGEON,
        SLAYER,
        CUSTOM
    }
    
    /**
     * Quest difficulty enum
     */
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        EXPERT,
        MASTER
    }
}