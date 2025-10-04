package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Event Trigger Message - Real-time event synchronization message
 * 
 * Broadcast to all servers when events start or end
 */
public class EventTriggerMessage {
    
    private final String eventId;
    private final String eventName;
    private final GlobalEvent.EventType eventType;
    private final TriggerType triggerType;
    private final long timestamp;
    private final String serverId;
    
    public EventTriggerMessage(String eventId, String eventName, GlobalEvent.EventType eventType, 
                             TriggerType triggerType, long timestamp) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.triggerType = triggerType;
        this.timestamp = timestamp;
        this.serverId = "unknown";
    }
    
    public EventTriggerMessage(String eventId, String eventName, GlobalEvent.EventType eventType, 
                             TriggerType triggerType, long timestamp, String serverId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.triggerType = triggerType;
        this.timestamp = timestamp;
        this.serverId = serverId;
    }
    
    /**
     * Convert to JSON for Redis pub/sub
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("type", "EVENT_TRIGGER");
        json.addProperty("eventId", eventId);
        json.addProperty("eventName", eventName);
        json.addProperty("eventType", eventType.name());
        json.addProperty("triggerType", triggerType.name());
        json.addProperty("timestamp", timestamp);
        json.addProperty("serverId", serverId);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static EventTriggerMessage fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        return new EventTriggerMessage(
            obj.get("eventId").getAsString(),
            obj.get("eventName").getAsString(),
            GlobalEvent.EventType.valueOf(obj.get("eventType").getAsString()),
            TriggerType.valueOf(obj.get("triggerType").getAsString()),
            obj.get("timestamp").getAsLong(),
            obj.get("serverId").getAsString()
        );
    }
    
    /**
     * Check if message is recent (within last 30 seconds)
     */
    public boolean isRecent() {
        return (System.currentTimeMillis() - timestamp) < 30000L;
    }
    
    /**
     * Check if this is an event start message
     */
    public boolean isEventStart() {
        return triggerType == TriggerType.START;
    }
    
    /**
     * Check if this is an event end message
     */
    public boolean isEventEnd() {
        return triggerType == TriggerType.END;
    }
    
    // Getters
    public String getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public GlobalEvent.EventType getEventType() { return eventType; }
    public TriggerType getTriggerType() { return triggerType; }
    public long getTimestamp() { return timestamp; }
    public String getServerId() { return serverId; }
    
    /**
     * Trigger type enum
     */
    public enum TriggerType {
        START, END, PAUSE, RESUME, CANCELLED
    }
    
    @Override
    public String toString() {
        return String.format("EventTriggerMessage{eventId='%s', name='%s', type=%s, trigger=%s, server='%s'}", 
                           eventId, eventName, eventType, triggerType, serverId);
    }
}
