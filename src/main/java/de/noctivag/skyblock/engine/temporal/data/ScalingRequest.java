package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Scaling Request - Represents a request for server scaling
 * 
 * Used for communicating with the GIM system to request additional server instances
 */
public class ScalingRequest {
    
    private final String eventId;
    private final int expectedPlayerLoad;
    private final long timestamp;
    private final Priority priority;
    private final String requestId;
    
    public ScalingRequest(String eventId, int expectedPlayerLoad, long timestamp, Priority priority) {
        this.eventId = eventId;
        this.expectedPlayerLoad = expectedPlayerLoad;
        this.timestamp = timestamp;
        this.priority = priority;
        this.requestId = generateRequestId();
    }
    
    /**
     * Generate unique request ID
     */
    private String generateRequestId() {
        return eventId + "_" + timestamp + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Get required instance count based on expected load
     */
    public int getRequiredInstanceCount() {
        if (expectedPlayerLoad <= 100) return 1; // Normal load
        if (expectedPlayerLoad <= 200) return 2; // 2x load
        if (expectedPlayerLoad <= 300) return 3; // 3x load
        if (expectedPlayerLoad <= 500) return 5; // 5x load
        return Math.max(10, expectedPlayerLoad / 100); // Scale linearly above 500%
    }
    
    /**
     * Get estimated resource requirements
     */
    public ResourceRequirements getResourceRequirements() {
        int instances = getRequiredInstanceCount();
        
        return new ResourceRequirements(
            instances * 2, // 2GB RAM per instance
            instances * 1, // 1 CPU core per instance
            instances * 10 // 10GB storage per instance
        );
    }
    
    /**
     * Check if request is urgent
     */
    public boolean isUrgent() {
        return priority == Priority.CRITICAL || priority == Priority.HIGH;
    }
    
    /**
     * Get request age in milliseconds
     */
    public long getAge() {
        return java.lang.System.currentTimeMillis() - timestamp;
    }
    
    /**
     * Check if request has expired
     */
    public boolean isExpired(long timeoutMs) {
        return getAge() > timeoutMs;
    }
    
    /**
     * Convert to JSON for Redis storage
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("eventId", eventId);
        json.addProperty("expectedPlayerLoad", expectedPlayerLoad);
        json.addProperty("timestamp", timestamp);
        json.addProperty("priority", priority.name());
        json.addProperty("requestId", requestId);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static ScalingRequest fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        return new ScalingRequest(
            obj.get("eventId").getAsString(),
            obj.get("expectedPlayerLoad").getAsInt(),
            obj.get("timestamp").getAsLong(),
            Priority.valueOf(obj.get("priority").getAsString())
        );
    }
    
    // Getters
    public String getEventId() { return eventId; }
    public int getExpectedPlayerLoad() { return expectedPlayerLoad; }
    public long getTimestamp() { return timestamp; }
    public Priority getPriority() { return priority; }
    public String getRequestId() { return requestId; }
    
    /**
     * Priority enum
     */
    public enum Priority {
        LOW(1), NORMAL(2), HIGH(3), CRITICAL(4);
        
        private final int level;
        
        Priority(int level) {
            this.level = level;
        }
        
        public int getLevel() { return level; }
        
        public boolean isHigherThan(Priority other) {
            return this.level > other.level;
        }
    }
    
    /**
     * Resource Requirements class
     */
    public static class ResourceRequirements {
        private final int ramGB;
        private final int cpuCores;
        private final int storageGB;
        
        public ResourceRequirements(int ramGB, int cpuCores, int storageGB) {
            this.ramGB = ramGB;
            this.cpuCores = cpuCores;
            this.storageGB = storageGB;
        }
        
        public int getRamGB() { return ramGB; }
        public int getCpuCores() { return cpuCores; }
        public int getStorageGB() { return storageGB; }
        
        @Override
        public String toString() {
            return String.format("ResourceRequirements{RAM=%dGB, CPU=%d cores, Storage=%dGB}", 
                               ramGB, cpuCores, storageGB);
        }
    }
    
    @Override
    public String toString() {
        return String.format("ScalingRequest{eventId='%s', load=%d%%, priority=%s, instances=%d}", 
                           eventId, expectedPlayerLoad, priority, getRequiredInstanceCount());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScalingRequest that = (ScalingRequest) obj;
        return requestId.equals(that.requestId);
    }
    
    @Override
    public int hashCode() {
        return requestId.hashCode();
    }
}
