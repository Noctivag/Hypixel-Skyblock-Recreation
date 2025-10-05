package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Scaling Response - Represents a response from the GIM system
 * 
 * Contains information about the success/failure of scaling requests
 */
public class ScalingResponse {
    
    private final String eventId;
    private final boolean success;
    private final String message;
    private final int instanceCount;
    private final long timestamp;
    private final String responseId;
    
    public ScalingResponse(String eventId, boolean success, String message, int instanceCount) {
        this.eventId = eventId;
        this.success = success;
        this.message = message;
        this.instanceCount = instanceCount;
        this.timestamp = java.lang.System.currentTimeMillis();
        this.responseId = generateResponseId();
    }
    
    /**
     * Generate unique response ID
     */
    private String generateResponseId() {
        return eventId + "_response_" + timestamp + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Get response age in milliseconds
     */
    public long getAge() {
        return java.lang.System.currentTimeMillis() - timestamp;
    }
    
    /**
     * Check if response is recent (within last 5 minutes)
     */
    public boolean isRecent() {
        return getAge() < 300000L; // 5 minutes
    }
    
    /**
     * Get scaling efficiency (instances per 100% load)
     */
    public double getScalingEfficiency() {
        if (instanceCount == 0) return 0.0;
        return 100.0 / instanceCount; // Load percentage per instance
    }
    
    /**
     * Check if scaling is adequate
     */
    public boolean isAdequateScaling(int expectedLoad) {
        if (!success) return false;
        
        // Each instance should handle ~100% load
        int requiredInstances = Math.max(1, expectedLoad / 100);
        return instanceCount >= requiredInstances;
    }
    
    /**
     * Convert to JSON for Redis storage
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("eventId", eventId);
        json.addProperty("success", success);
        json.addProperty("message", message);
        json.addProperty("instanceCount", instanceCount);
        json.addProperty("timestamp", timestamp);
        json.addProperty("responseId", responseId);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static ScalingResponse fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        return new ScalingResponse(
            obj.get("eventId").getAsString(),
            obj.get("success").getAsBoolean(),
            obj.get("message").getAsString(),
            obj.get("instanceCount").getAsInt()
        );
    }
    
    // Getters
    public String getEventId() { return eventId; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getInstanceCount() { return instanceCount; }
    public long getTimestamp() { return timestamp; }
    public String getResponseId() { return responseId; }
    
    @Override
    public String toString() {
        return String.format("ScalingResponse{eventId='%s', success=%s, instances=%d, message='%s'}", 
                           eventId, success, instanceCount, message);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScalingResponse that = (ScalingResponse) obj;
        return responseId.equals(that.responseId);
    }
    
    @Override
    public int hashCode() {
        return responseId.hashCode();
    }
}
