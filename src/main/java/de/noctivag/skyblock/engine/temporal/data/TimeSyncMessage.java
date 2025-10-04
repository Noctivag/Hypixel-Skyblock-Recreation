package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;

/**
 * Time Sync Message - Global time synchronization message
 * 
 * Broadcast to all servers for time synchronization
 */
public class TimeSyncMessage {
    
    private final long timestamp;
    private final LocalDateTime globalTime;
    private final String serverId;
    private final long timezoneOffset;
    
    public TimeSyncMessage(long timestamp, LocalDateTime globalTime, String serverId) {
        this.timestamp = timestamp;
        this.globalTime = globalTime;
        this.serverId = serverId;
        this.timezoneOffset = 0; // UTC
    }
    
    public TimeSyncMessage(long timestamp, LocalDateTime globalTime, String serverId, long timezoneOffset) {
        this.timestamp = timestamp;
        this.globalTime = globalTime;
        this.serverId = serverId;
        this.timezoneOffset = timezoneOffset;
    }
    
    /**
     * Convert to JSON for Redis pub/sub
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("type", "TIME_SYNC");
        json.addProperty("timestamp", timestamp);
        json.addProperty("globalTime", globalTime.toString());
        json.addProperty("serverId", serverId);
        json.addProperty("timezoneOffset", timezoneOffset);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static TimeSyncMessage fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        return new TimeSyncMessage(
            obj.get("timestamp").getAsLong(),
            LocalDateTime.parse(obj.get("globalTime").getAsString()),
            obj.get("serverId").getAsString(),
            obj.get("timezoneOffset").getAsLong()
        );
    }
    
    /**
     * Check if message is recent (within last 10 seconds)
     */
    public boolean isRecent() {
        return (System.currentTimeMillis() - timestamp) < 10000L;
    }
    
    /**
     * Get time difference from current time
     */
    public long getTimeDifference() {
        return System.currentTimeMillis() - timestamp;
    }
    
    /**
     * Check if time is synchronized (difference < 1 second)
     */
    public boolean isSynchronized() {
        return Math.abs(getTimeDifference()) < 1000L;
    }
    
    // Getters
    public long getTimestamp() { return timestamp; }
    public LocalDateTime getGlobalTime() { return globalTime; }
    public String getServerId() { return serverId; }
    public long getTimezoneOffset() { return timezoneOffset; }
    
    @Override
    public String toString() {
        return String.format("TimeSyncMessage{timestamp=%d, globalTime=%s, serverId='%s', offset=%d}", 
                           timestamp, globalTime, serverId, timezoneOffset);
    }
}
