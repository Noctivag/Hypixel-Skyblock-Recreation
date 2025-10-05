package de.noctivag.skyblock.engine.temporal.services;

import de.noctivag.skyblock.engine.temporal.GlobalTimeManagementService;
import de.noctivag.skyblock.engine.temporal.data.ScalingRequest;
import de.noctivag.skyblock.engine.temporal.data.ScalingResponse;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * GIM Integration Service
 * 
 * Integrates with the Global Instance Management (GIM) system for proactive scaling.
 * Coordinates with the calendar service to provision additional server instances
 * for high-load events like National Mining Month, Mayor Elections, etc.
 */
public class GIMIntegrationService {
    
    private final GlobalTimeManagementService timeService;
    private final Map<String, ScalingRequest> activeRequests = new HashMap<>();
    private final Map<String, ScalingResponse> scalingResponses = new HashMap<>();
    
    // Configuration
    private static final long SCALING_CHECK_INTERVAL = 10000L; // 10 seconds
    private static final long SCALING_TIMEOUT = 300000L; // 5 minutes
    private static final int MAX_SCALING_REQUESTS = 10;
    
    // Redis keys
    private static final String SCALING_REQUESTS_KEY = "gim:scaling_requests";
    private static final String SCALING_RESPONSES_KEY = "gim:scaling_responses";
    private static final String SCALING_CHANNEL = "gim:scaling_updates";
    
    public GIMIntegrationService(GlobalTimeManagementService timeService) {
        this.timeService = timeService;
    }
    
    /**
     * Initialize the GIM integration service
     */
    public void initialize() {
        // Start scaling monitoring
        startScalingMonitoring();
        
        // Load existing scaling requests
        loadScalingRequests();
        
        timeService.getPlugin().getLogger().info("GIM Integration Service initialized");
    }
    
    /**
     * Start scaling monitoring
     */
    private void startScalingMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                monitorScalingRequests();
                checkScalingTimeouts();
                updateScalingStatus();
            }
        }.runTaskTimerAsynchronously(timeService.getPlugin(), 0L, SCALING_CHECK_INTERVAL / 50L);
    }
    
    /**
     * Request scaling for an event
     */
    public CompletableFuture<ScalingResponse> requestScaling(String eventId, int expectedPlayerLoad) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Check if we already have an active request
                if (activeRequests.containsKey(eventId)) {
                    return scalingResponses.get(eventId);
                }
                
                // Check request limit
                if (activeRequests.size() >= MAX_SCALING_REQUESTS) {
                    return new ScalingResponse(eventId, false, "Maximum scaling requests reached", 0);
                }
                
                // Create scaling request
                ScalingRequest request = new ScalingRequest(
                    eventId,
                    expectedPlayerLoad,
                    java.lang.System.currentTimeMillis(),
                    ScalingRequest.Priority.HIGH
                );
                
                // Store request
                activeRequests.put(eventId, request);
                storeScalingRequest(request);
                
                // Send request to GIM system
                sendScalingRequest(request);
                
                // Create pending response
                ScalingResponse response = new ScalingResponse(eventId, false, "Scaling request pending", 0);
                scalingResponses.put(eventId, response);
                
                timeService.getPlugin().getLogger().info(
                    String.format("Scaling request sent for event %s (expected load: %d%%)", 
                        eventId, expectedPlayerLoad)
                );
                
                return response;
                
            } catch (Exception e) {
                timeService.getPlugin().getLogger().log(Level.SEVERE, 
                    "Failed to request scaling for event: " + eventId, e);
                return new ScalingResponse(eventId, false, "Scaling request failed", 0);
            }
        });
    }
    
    /**
     * Release scaling for an event
     */
    public CompletableFuture<Boolean> releaseScaling(String eventId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ScalingRequest request = activeRequests.remove(eventId);
                if (request == null) {
                    return false;
                }
                
                // Send release request to GIM system
                sendScalingRelease(eventId);
                
                // Remove from responses
                scalingResponses.remove(eventId);
                
                // Remove from Redis
                removeScalingRequest(eventId);
                
                timeService.getPlugin().getLogger().info("Scaling released for event: " + eventId);
                
                return true;
                
            } catch (Exception e) {
                timeService.getPlugin().getLogger().log(Level.SEVERE, 
                    "Failed to release scaling for event: " + eventId, e);
                return false;
            }
        });
    }
    
    /**
     * Send scaling request to GIM system
     */
    private void sendScalingRequest(ScalingRequest request) {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            // Store request in Redis for GIM system to process
            jedis.hset(SCALING_REQUESTS_KEY, request.getEventId(), request.toJson());
            
            // Publish scaling request
            jedis.publish(SCALING_CHANNEL, request.toJson());
            
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to send scaling request to GIM system", e);
        }
    }
    
    /**
     * Send scaling release to GIM system
     */
    private void sendScalingRelease(String eventId) {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            // Remove request from Redis
            jedis.hdel(SCALING_REQUESTS_KEY, eventId);
            
            // Publish scaling release
            ScalingRequest releaseRequest = new ScalingRequest(
                eventId, 0, java.lang.System.currentTimeMillis(), ScalingRequest.Priority.LOW
            );
            jedis.publish(SCALING_CHANNEL, releaseRequest.toJson());
            
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to send scaling release to GIM system", e);
        }
    }
    
    /**
     * Monitor scaling requests
     */
    private void monitorScalingRequests() {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            // Check for scaling responses
            Map<String, String> responses = jedis.hgetAll(SCALING_RESPONSES_KEY);
            for (Map.Entry<String, String> entry : responses.entrySet()) {
                String eventId = entry.getKey();
                ScalingResponse response = ScalingResponse.fromJson(entry.getValue());
                
                // Update response if it's new or different
                ScalingResponse existingResponse = scalingResponses.get(eventId);
                if (existingResponse == null || !existingResponse.equals(response)) {
                    scalingResponses.put(eventId, response);
                    
                    if (response.isSuccess()) {
                        timeService.getPlugin().getLogger().info(
                            String.format("Scaling successful for event %s: %d instances", 
                                eventId, response.getInstanceCount())
                        );
                    } else {
                        timeService.getPlugin().getLogger().warning(
                            String.format("Scaling failed for event %s: %s", 
                                eventId, response.getMessage())
                        );
                    }
                }
            }
            
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to monitor scaling requests", e);
        }
    }
    
    /**
     * Check for scaling timeouts
     */
    private void checkScalingTimeouts() {
        long currentTime = java.lang.System.currentTimeMillis();
        
        for (Map.Entry<String, ScalingRequest> entry : activeRequests.entrySet()) {
            String eventId = entry.getKey();
            ScalingRequest request = entry.getValue();
            
            if (currentTime - request.getTimestamp() > SCALING_TIMEOUT) {
                // Timeout reached
                timeService.getPlugin().getLogger().warning(
                    "Scaling request timeout for event: " + eventId
                );
                
                // Remove from active requests
                activeRequests.remove(eventId);
                
                // Update response
                ScalingResponse timeoutResponse = new ScalingResponse(
                    eventId, false, "Scaling request timeout", 0
                );
                scalingResponses.put(eventId, timeoutResponse);
            }
        }
    }
    
    /**
     * Update scaling status
     */
    private void updateScalingStatus() {
        // Update scaling status for active events
        for (String eventId : timeService.getActiveEvents().keySet()) {
            ScalingResponse response = scalingResponses.get(eventId);
            if (response != null && response.isSuccess()) {
                // Scaling is active, monitor instance health
                monitorInstanceHealth(eventId, response.getInstanceCount());
            }
        }
    }
    
    /**
     * Monitor instance health
     */
    private void monitorInstanceHealth(String eventId, int instanceCount) {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            // Check instance health from GIM system
            String healthKey = "gim:instance_health:" + eventId;
            String healthData = jedis.get(healthKey);
            
            if (healthData != null) {
                // Parse health data and take action if needed
                // This would integrate with the actual GIM system
                timeService.getPlugin().getLogger().info(
                    String.format("Instance health for event %s: %s", eventId, healthData)
                );
            }
            
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to monitor instance health for event: " + eventId, e);
        }
    }
    
    /**
     * Store scaling request in Redis
     */
    private void storeScalingRequest(ScalingRequest request) {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            jedis.hset(SCALING_REQUESTS_KEY, request.getEventId(), request.toJson());
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to store scaling request", e);
        }
    }
    
    /**
     * Remove scaling request from Redis
     */
    private void removeScalingRequest(String eventId) {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            jedis.hdel(SCALING_REQUESTS_KEY, eventId);
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to remove scaling request", e);
        }
    }
    
    /**
     * Load existing scaling requests
     */
    private void loadScalingRequests() {
        try (Jedis jedis = timeService.getJedisPool().getResource()) {
            Map<String, String> requests = jedis.hgetAll(SCALING_REQUESTS_KEY);
            for (Map.Entry<String, String> entry : requests.entrySet()) {
                ScalingRequest request = ScalingRequest.fromJson(entry.getValue());
                activeRequests.put(entry.getKey(), request);
            }
            
            Map<String, String> responses = jedis.hgetAll(SCALING_RESPONSES_KEY);
            for (Map.Entry<String, String> entry : responses.entrySet()) {
                ScalingResponse response = ScalingResponse.fromJson(entry.getValue());
                scalingResponses.put(entry.getKey(), response);
            }
            
        } catch (Exception e) {
            timeService.getPlugin().getLogger().log(Level.SEVERE, 
                "Failed to load scaling requests", e);
        }
    }
    
    /**
     * Get scaling status for an event
     */
    public ScalingResponse getScalingStatus(String eventId) {
        return scalingResponses.get(eventId);
    }
    
    /**
     * Get all active scaling requests
     */
    public Map<String, ScalingRequest> getActiveRequests() {
        return new HashMap<>(activeRequests);
    }
    
    /**
     * Get all scaling responses
     */
    public Map<String, ScalingResponse> getScalingResponses() {
        return new HashMap<>(scalingResponses);
    }
    
    /**
     * Check if scaling is active for an event
     */
    public boolean isScalingActive(String eventId) {
        ScalingResponse response = scalingResponses.get(eventId);
        return response != null && response.isSuccess() && response.getInstanceCount() > 0;
    }
    
    /**
     * Get total active instances
     */
    public int getTotalActiveInstances() {
        return scalingResponses.values().stream()
            .filter(ScalingResponse::isSuccess)
            .mapToInt(ScalingResponse::getInstanceCount)
            .sum();
    }
    
    /**
     * Get scaling statistics
     */
    public ScalingStatistics getScalingStatistics() {
        int totalRequests = activeRequests.size();
        int successfulRequests = (int) scalingResponses.values().stream()
            .filter(ScalingResponse::isSuccess)
            .count();
        int totalInstances = getTotalActiveInstances();
        
        return new ScalingStatistics(totalRequests, successfulRequests, totalInstances);
    }
    
    /**
     * Scaling Statistics class
     */
    public static class ScalingStatistics {
        private final int totalRequests;
        private final int successfulRequests;
        private final int totalInstances;
        
        public ScalingStatistics(int totalRequests, int successfulRequests, int totalInstances) {
            this.totalRequests = totalRequests;
            this.successfulRequests = successfulRequests;
            this.totalInstances = totalInstances;
        }
        
        public int getTotalRequests() { return totalRequests; }
        public int getSuccessfulRequests() { return successfulRequests; }
        public int getTotalInstances() { return totalInstances; }
        public double getSuccessRate() { 
            return totalRequests > 0 ? (double) successfulRequests / totalRequests * 100.0 : 0.0; 
        }
        
        @Override
        public String toString() {
            return String.format("ScalingStatistics{requests=%d, successful=%d, instances=%d, successRate=%.1f%%}", 
                               totalRequests, successfulRequests, totalInstances, getSuccessRate());
        }
    }
}
