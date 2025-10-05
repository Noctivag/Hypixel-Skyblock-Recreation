package de.noctivag.skyblock.events;

import java.util.List;
import java.util.Map;

/**
 * Represents a skyblock event
 */
public class SkyblockEvent {
    
    private final String id;
    private final String name;
    private final String description;
    private final EventType type;
    private final long duration; // in seconds
    private final boolean autoStart;
    private final Map<String, Object> requirements;
    private final Map<String, Object> rewards;
    private final List<String> schedule;
    
    private boolean active = false;
    private long startTime = 0;
    private long endTime = 0;
    
    public SkyblockEvent(String id, String name, String description, EventType type,
                        long duration, boolean autoStart, Map<String, Object> requirements,
                        Map<String, Object> rewards, List<String> schedule) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.autoStart = autoStart;
        this.requirements = requirements;
        this.rewards = rewards;
        this.schedule = schedule;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public EventType getType() { return type; }
    public long getDuration() { return duration; }
    public boolean isAutoStart() { return autoStart; }
    public Map<String, Object> getRequirements() { return requirements; }
    public Map<String, Object> getRewards() { return rewards; }
    public List<String> getSchedule() { return schedule; }
    
    // Status getters
    public boolean isActive() { return active; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    
    // Status setters
    public void setActive(boolean active) { this.active = active; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    /**
     * Check if event is expired
     */
    public boolean isExpired() {
        if (!active || startTime == 0) return false;
        return System.currentTimeMillis() - startTime >= duration * 1000;
    }
    
    /**
     * Get time remaining in seconds
     */
    public long getTimeRemaining() {
        if (!active || startTime == 0) return 0;
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return Math.max(0, duration - elapsed);
    }
    
    /**
     * Get progress percentage (0.0 to 1.0)
     */
    public double getProgress() {
        if (!active || startTime == 0) return 0.0;
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return Math.min(1.0, (double) elapsed / duration);
    }
    
    /**
     * Get requirement value
     */
    public Object getRequirement(String key, Object defaultValue) {
        return requirements.getOrDefault(key, defaultValue);
    }
    
    /**
     * Get requirement as string
     */
    public String getRequirementAsString(String key, String defaultValue) {
        Object value = requirements.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Get requirement as integer
     */
    public int getRequirementAsInt(String key, int defaultValue) {
        Object value = requirements.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    /**
     * Get requirement as double
     */
    public double getRequirementAsDouble(String key, double defaultValue) {
        Object value = requirements.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    /**
     * Get requirement as boolean
     */
    public boolean getRequirementAsBoolean(String key, boolean defaultValue) {
        Object value = requirements.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
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
     * Event type enum
     */
    public enum EventType {
        BOOST,
        FISHING,
        MINING,
        COMBAT,
        FARMING,
        FORAGING,
        ENCHANTING,
        ALCHEMY,
        TAMING,
        DUNGEON,
        SLAYER,
        CUSTOM
    }
}
