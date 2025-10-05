package de.noctivag.skyblock.quests;

import java.util.Map;

/**
 * Represents a quest objective
 */
public class QuestObjective {
    
    private final ObjectiveType type;
    private final int amount;
    private final Map<String, Object> parameters;
    
    public QuestObjective(ObjectiveType type, int amount, Map<String, Object> parameters) {
        this.type = type;
        this.amount = amount;
        this.parameters = parameters;
    }
    
    // Getters
    public ObjectiveType getType() { return type; }
    public int getAmount() { return amount; }
    public Map<String, Object> getParameters() { return parameters; }
    
    /**
     * Get parameter value
     */
    public Object getParameter(String key, Object defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }
    
    /**
     * Get parameter as string
     */
    public String getParameterAsString(String key, String defaultValue) {
        Object value = parameters.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Get parameter as integer
     */
    public int getParameterAsInt(String key, int defaultValue) {
        Object value = parameters.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    /**
     * Get parameter as double
     */
    public double getParameterAsDouble(String key, double defaultValue) {
        Object value = parameters.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    /**
     * Get parameter as boolean
     */
    public boolean getParameterAsBoolean(String key, boolean defaultValue) {
        Object value = parameters.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    /**
     * Objective type enum
     */
    public enum ObjectiveType {
        MINE_BLOCKS,
        KILL_MOBS,
        COLLECT_ITEMS,
        CRAFT_ITEMS,
        ENCHANT_ITEMS,
        BREW_POTIONS,
        FISH_ITEMS,
        FARM_ITEMS,
        FORAGE_ITEMS,
        TAME_PETS,
        COMPLETE_DUNGEONS,
        DEFEAT_SLAYERS,
        CUSTOM
    }
}