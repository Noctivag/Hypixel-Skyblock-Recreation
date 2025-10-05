package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Global Event - Represents a global game event
 * 
 * Used for event scheduling, triggering, and management
 */
public class GlobalEvent {
    
    private final String id;
    private final String name;
    private final String description;
    private final EventType type;
    private final LocalDateTime startTime;
    private final int duration; // in hours
    private final boolean recurring;
    private final boolean requiresScaling;
    private final int expectedPlayerLoad; // percentage increase
    
    public GlobalEvent(String id, String name, String description, EventType type, 
                      LocalDateTime startTime, int duration, boolean recurring, boolean requiresScaling) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.recurring = recurring;
        this.requiresScaling = requiresScaling;
        this.expectedPlayerLoad = calculateExpectedPlayerLoad(type);
    }
    
    /**
     * Calculate expected player load based on event type
     */
    private int calculateExpectedPlayerLoad(EventType type) {
        switch (type) {
            case DOUBLE_COINS:
                return 150; // 150% normal load
            case MINING_FIESTA:
                return 200; // 200% normal load
            case FISHING_FESTIVAL:
                return 180; // 180% normal load
            case COMBAT_ARENA:
                return 250; // 250% normal load
            case DUNGEON_WEEK:
                return 300; // 300% normal load
            case SLAYER_WEEK:
                return 220; // 220% normal load
            case AUCTION_WEEK:
                return 120; // 120% normal load
            default:
                return 100; // 100% normal load
        }
    }
    
    /**
     * Check if event is currently active
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(duration);
        return now.isAfter(startTime) && now.isBefore(endTime);
    }
    
    /**
     * Get remaining time in seconds
     */
    public long getRemainingTimeSeconds() {
        if (!isActive()) return 0;
        
        LocalDateTime endTime = startTime.plusHours(duration);
        return endTime.toEpochSecond(ZoneOffset.UTC) - java.lang.System.currentTimeMillis() / 1000;
    }
    
    /**
     * Get event progress percentage
     */
    public double getProgressPercentage() {
        if (!isActive()) return 100.0;
        
        long totalDuration = duration * 3600; // Convert hours to seconds
        long elapsed = (java.lang.System.currentTimeMillis() / 1000) - startTime.toEpochSecond(ZoneOffset.UTC);
        
        return Math.min(100.0, (double) elapsed / totalDuration * 100.0);
    }
    
    /**
     * Convert to JSON for Redis storage
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("description", description);
        json.addProperty("type", type.name());
        json.addProperty("startTime", startTime.toString());
        json.addProperty("duration", duration);
        json.addProperty("recurring", recurring);
        json.addProperty("requiresScaling", requiresScaling);
        json.addProperty("expectedPlayerLoad", expectedPlayerLoad);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static GlobalEvent fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        return new GlobalEvent(
            obj.get("id").getAsString(),
            obj.get("name").getAsString(),
            obj.get("description").getAsString(),
            EventType.valueOf(obj.get("type").getAsString()),
            LocalDateTime.parse(obj.get("startTime").getAsString()),
            obj.get("duration").getAsInt(),
            obj.get("recurring").getAsBoolean(),
            obj.get("requiresScaling").getAsBoolean()
        );
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public EventType getType() { return type; }
    public LocalDateTime getStartTime() { return startTime; }
    public int getDuration() { return duration; }
    public boolean isRecurring() { return recurring; }
    public boolean requiresScaling() { return requiresScaling; }
    public int getExpectedPlayerLoad() { return expectedPlayerLoad; }
    
    /**
     * Event type enum
     */
    public enum EventType {
        DOUBLE_COINS("Double Coins", "All coin rewards are doubled"),
        MINING_FIESTA("Mining Fiesta", "Enhanced mining XP and drops"),
        FISHING_FESTIVAL("Fishing Festival", "Special fishing events and rewards"),
        COMBAT_ARENA("Combat Arena", "Enhanced combat challenges"),
        DUNGEON_WEEK("Dungeon Week", "Enhanced dungeon rewards"),
        SLAYER_WEEK("Slayer Week", "Enhanced slayer rewards"),
        AUCTION_WEEK("Auction Week", "Reduced auction fees"),
        SEASONAL_EVENT("Seasonal Event", "Special seasonal content");
        
        private final String displayName;
        private final String description;
        
        EventType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    @Override
    public String toString() {
        return String.format("GlobalEvent{id='%s', name='%s', type=%s, startTime=%s, duration=%dh}", 
                           id, name, type, startTime, duration);
    }
}
