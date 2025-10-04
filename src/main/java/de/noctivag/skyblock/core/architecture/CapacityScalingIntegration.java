package de.noctivag.skyblock.core.architecture;

import de.noctivag.skyblock.core.events.GlobalCalendarEventScheduler;
import de.noctivag.skyblock.core.events.GlobalCalendarEventScheduler.CapacityAdjustmentRequest;
import de.noctivag.skyblock.core.events.GlobalCalendarEventScheduler.EventCategory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Capacity Scaling Integration - Proactive capacity adjustment between Calendar Service and GIM
 * 
 * Features:
 * - Integration between Global Calendar Event Scheduler and Global Instance Manager
 * - Proactive capacity scaling before events
 * - Event-driven instance management
 * - Load prediction and preparation
 * - Automatic scaling down after events
 * - Capacity monitoring and optimization
 */
public class CapacityScalingIntegration {
    
    private static final Logger logger = Logger.getLogger(CapacityScalingIntegration.class.getName());
    
    // Core components
    private final GlobalInstanceManager gim;
    private final GlobalCalendarEventScheduler calendarService;
    private final StateSynchronizationLayer stateLayer;
    
    // Capacity management
    private final Map<String, CapacityScalingSession> activeScalingSessions = new ConcurrentHashMap<>();
    private final Map<EventCategory, ScalingProfile> scalingProfiles = new ConcurrentHashMap<>();
    
    // Monitoring and metrics
    private final Map<String, CapacityMetrics> capacityMetrics = new ConcurrentHashMap<>();
    
    /**
     * Capacity Scaling Session
     */
    public static class CapacityScalingSession {
        private final String sessionId;
        private final String eventId;
        private final EventCategory eventCategory;
        private final ScalingPhase currentPhase;
        private final Map<String, Integer> requestedInstances;
        private final Map<String, Integer> allocatedInstances;
        private final LocalDateTime startTime;
        private final LocalDateTime scheduledEndTime;
        private final Map<String, Object> sessionData;
        
        public CapacityScalingSession(String sessionId, String eventId, EventCategory eventCategory,
                                    Map<String, Integer> requestedInstances, LocalDateTime scheduledEndTime) {
            this.sessionId = sessionId;
            this.eventId = eventId;
            this.eventCategory = eventCategory;
            this.currentPhase = ScalingPhase.PREPARATION;
            this.requestedInstances = new HashMap<>(requestedInstances);
            this.allocatedInstances = new ConcurrentHashMap<>();
            this.startTime = LocalDateTime.now();
            this.scheduledEndTime = scheduledEndTime;
            this.sessionData = new ConcurrentHashMap<>();
        }
        
        // Getters
        public String getSessionId() { return sessionId; }
        public String getEventId() { return eventId; }
        public EventCategory getEventCategory() { return eventCategory; }
        public ScalingPhase getCurrentPhase() { return currentPhase; }
        public Map<String, Integer> getRequestedInstances() { return requestedInstances; }
        public Map<String, Integer> getAllocatedInstances() { return allocatedInstances; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getScheduledEndTime() { return scheduledEndTime; }
        public Map<String, Object> getSessionData() { return sessionData; }
        
        public void setCurrentPhase(ScalingPhase phase) {
            this.currentPhase = phase;
        }
        
        public void addAllocatedInstances(String instanceType, int count) {
            this.allocatedInstances.merge(instanceType, count, Integer::sum);
        }
    }
    
    /**
     * Scaling Phase
     */
    public enum ScalingPhase {
        PREPARATION("Preparation", "Preparing instances for upcoming event"),
        SCALING_UP("Scaling Up", "Adding additional instances"),
        ACTIVE("Active", "Event is running with scaled capacity"),
        SCALING_DOWN("Scaling Down", "Reducing capacity after event"),
        COMPLETED("Completed", "Scaling session completed");
        
        private final String displayName;
        private final String description;
        
        ScalingPhase(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Scaling Profile - Defines scaling behavior for event categories
     */
    public static class ScalingProfile {
        private final EventCategory eventCategory;
        private final Map<String, Integer> baseInstanceCounts;
        private final Map<String, Integer> peakInstanceCounts;
        private final int preparationTimeMinutes;
        private final int rampUpTimeMinutes;
        private final int rampDownTimeMinutes;
        private final double loadMultiplier;
        
        public ScalingProfile(EventCategory eventCategory, Map<String, Integer> baseInstanceCounts,
                            Map<String, Integer> peakInstanceCounts, int preparationTimeMinutes,
                            int rampUpTimeMinutes, int rampDownTimeMinutes, double loadMultiplier) {
            this.eventCategory = eventCategory;
            this.baseInstanceCounts = new HashMap<>(baseInstanceCounts);
            this.peakInstanceCounts = new HashMap<>(peakInstanceCounts);
            this.preparationTimeMinutes = preparationTimeMinutes;
            this.rampUpTimeMinutes = rampUpTimeMinutes;
            this.rampDownTimeMinutes = rampDownTimeMinutes;
            this.loadMultiplier = loadMultiplier;
        }
        
        // Getters
        public EventCategory getEventCategory() { return eventCategory; }
        public Map<String, Integer> getBaseInstanceCounts() { return baseInstanceCounts; }
        public Map<String, Integer> getPeakInstanceCounts() { return peakInstanceCounts; }
        public int getPreparationTimeMinutes() { return preparationTimeMinutes; }
        public int getRampUpTimeMinutes() { return rampUpTimeMinutes; }
        public int getRampDownTimeMinutes() { return rampDownTimeMinutes; }
        public double getLoadMultiplier() { return loadMultiplier; }
    }
    
    /**
     * Capacity Metrics
     */
    public static class CapacityMetrics {
        private final String eventId;
        private final EventCategory eventCategory;
        private final LocalDateTime measurementTime;
        private final Map<String, Integer> instanceCounts;
        private final Map<String, Double> utilizationRates;
        private final int totalPlayers;
        private final double averageLatency;
        
        public CapacityMetrics(String eventId, EventCategory eventCategory, Map<String, Integer> instanceCounts,
                             Map<String, Double> utilizationRates, int totalPlayers, double averageLatency) {
            this.eventId = eventId;
            this.eventCategory = eventCategory;
            this.measurementTime = LocalDateTime.now();
            this.instanceCounts = new HashMap<>(instanceCounts);
            this.utilizationRates = new HashMap<>(utilizationRates);
            this.totalPlayers = totalPlayers;
            this.averageLatency = averageLatency;
        }
        
        // Getters
        public String getEventId() { return eventId; }
        public EventCategory getEventCategory() { return eventCategory; }
        public LocalDateTime getMeasurementTime() { return measurementTime; }
        public Map<String, Integer> getInstanceCounts() { return instanceCounts; }
        public Map<String, Double> getUtilizationRates() { return utilizationRates; }
        public int getTotalPlayers() { return totalPlayers; }
        public double getAverageLatency() { return averageLatency; }
    }
    
    public CapacityScalingIntegration(GlobalInstanceManager gim, GlobalCalendarEventScheduler calendarService,
                                    StateSynchronizationLayer stateLayer) {
        this.gim = gim;
        this.calendarService = calendarService;
        this.stateLayer = stateLayer;
        
        initializeScalingProfiles();
        startMonitoringTasks();
        
        logger.info("Capacity Scaling Integration initialized");
    }
    
    /**
     * Initialize scaling profiles for different event categories
     */
    private void initializeScalingProfiles() {
        // Farming Contest Profile
        Map<String, Integer> farmingBase = new HashMap<>();
        farmingBase.put("MINING_ZONE", 2);
        farmingBase.put("COMBAT_ZONE", 1);
        
        Map<String, Integer> farmingPeak = new HashMap<>();
        farmingPeak.put("MINING_ZONE", 8);
        farmingPeak.put("COMBAT_ZONE", 3);
        
        scalingProfiles.put(EventCategory.FARMING_CONTEST, new ScalingProfile(
            EventCategory.FARMING_CONTEST, farmingBase, farmingPeak, 30, 15, 20, 2.5));
        
        // Mining Contest Profile
        Map<String, Integer> miningBase = new HashMap<>();
        miningBase.put("MINING_ZONE", 1);
        
        Map<String, Integer> miningPeak = new HashMap<>();
        miningPeak.put("MINING_ZONE", 5);
        
        scalingProfiles.put(EventCategory.MINING_CONTEST, new ScalingProfile(
            EventCategory.MINING_CONTEST, miningBase, miningPeak, 15, 10, 15, 2.0));
        
        // Fishing Contest Profile
        Map<String, Integer> fishingBase = new HashMap<>();
        fishingBase.put("MINING_ZONE", 1);
        
        Map<String, Integer> fishingPeak = new HashMap<>();
        fishingPeak.put("MINING_ZONE", 6);
        
        scalingProfiles.put(EventCategory.FISHING_CONTEST, new ScalingProfile(
            EventCategory.FISHING_CONTEST, fishingBase, fishingPeak, 20, 10, 15, 1.8));
        
        // Slayer Event Profile
        Map<String, Integer> slayerBase = new HashMap<>();
        slayerBase.put("COMBAT_ZONE", 1);
        
        Map<String, Integer> slayerPeak = new HashMap<>();
        slayerPeak.put("COMBAT_ZONE", 4);
        
        scalingProfiles.put(EventCategory.SLAYER_EVENT, new ScalingProfile(
            EventCategory.SLAYER_EVENT, slayerBase, slayerPeak, 10, 5, 10, 1.5));
        
        // Dungeon Event Profile
        Map<String, Integer> dungeonBase = new HashMap<>();
        dungeonBase.put("DUNGEON_INSTANCE", 2);
        dungeonBase.put("COMBAT_ZONE", 1);
        
        Map<String, Integer> dungeonPeak = new HashMap<>();
        dungeonPeak.put("DUNGEON_INSTANCE", 10);
        dungeonPeak.put("COMBAT_ZONE", 3);
        
        scalingProfiles.put(EventCategory.DUNGEON_EVENT, new ScalingProfile(
            EventCategory.DUNGEON_EVENT, dungeonBase, dungeonPeak, 5, 5, 10, 3.0));
        
        // Special Event Profile
        Map<String, Integer> specialBase = new HashMap<>();
        specialBase.put("SPECIAL_EVENT", 1);
        specialBase.put("COMBAT_ZONE", 1);
        specialBase.put("MINING_ZONE", 1);
        
        Map<String, Integer> specialPeak = new HashMap<>();
        specialPeak.put("SPECIAL_EVENT", 5);
        specialPeak.put("COMBAT_ZONE", 4);
        specialPeak.put("MINING_ZONE", 3);
        
        scalingProfiles.put(EventCategory.SPECIAL_EVENT, new ScalingProfile(
            EventCategory.SPECIAL_EVENT, specialBase, specialPeak, 60, 20, 30, 4.0));
        
        logger.info("Initialized " + scalingProfiles.size() + " scaling profiles");
    }
    
    /**
     * Process capacity adjustment request from calendar service
     */
    public CompletableFuture<String> processCapacityAdjustmentRequest(CapacityAdjustmentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            String sessionId = UUID.randomUUID().toString();
            
            try {
                // Get scaling profile for event category
                ScalingProfile profile = scalingProfiles.get(request.getCategory());
                if (profile == null) {
                    logger.warning("No scaling profile found for event category: " + request.getCategory());
                    return null;
                }
                
                // Create scaling session
                CapacityScalingSession session = new CapacityScalingSession(
                    sessionId, request.getEventId(), request.getCategory(),
                    request.getInstanceTypes().stream().collect(java.util.stream.Collectors.toMap(
                        type -> type, type -> request.getAdditionalInstances())),
                    request.getRequestedTime().plusMinutes(profile.getPreparationTimeMinutes())
                );
                
                activeScalingSessions.put(sessionId, session);
                
                // Execute scaling based on request phase
                if (request.getReason().contains("preparation")) {
                    executePreparationPhase(session, profile);
                } else if (request.getReason().contains("start")) {
                    executeScalingUpPhase(session, profile);
                } else if (request.getReason().contains("end")) {
                    executeScalingDownPhase(session, profile);
                }
                
                logger.info("Processed capacity adjustment request: " + sessionId + 
                           " for event: " + request.getEventId());
                
                return sessionId;
                
            } catch (Exception e) {
                logger.severe("Error processing capacity adjustment request: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Execute preparation phase
     */
    private void executePreparationPhase(CapacityScalingSession session, ScalingProfile profile) {
        session.setCurrentPhase(ScalingPhase.PREPARATION);
        
        logger.info("Starting preparation phase for session: " + session.getSessionId());
        
        // Pre-allocate base instances
        for (Map.Entry<String, Integer> entry : profile.getBaseInstanceCounts().entrySet()) {
            String instanceType = entry.getKey();
            int count = entry.getValue();
            
            // Request instances from GIM
            requestInstancesFromGIM(session, instanceType, count, "preparation");
        }
        
        // Schedule scaling up for event start
        scheduleScalingUp(session, profile);
    }
    
    /**
     * Execute scaling up phase
     */
    private void executeScalingUpPhase(CapacityScalingSession session, ScalingProfile profile) {
        session.setCurrentPhase(ScalingPhase.SCALING_UP);
        
        logger.info("Starting scaling up phase for session: " + session.getSessionId());
        
        // Scale up to peak capacity
        for (Map.Entry<String, Integer> entry : profile.getPeakInstanceCounts().entrySet()) {
            String instanceType = entry.getKey();
            int peakCount = entry.getValue();
            int currentCount = session.getAllocatedInstances().getOrDefault(instanceType, 0);
            int additionalCount = peakCount - currentCount;
            
            if (additionalCount > 0) {
                requestInstancesFromGIM(session, instanceType, additionalCount, "scaling_up");
            }
        }
        
        session.setCurrentPhase(ScalingPhase.ACTIVE);
        
        // Schedule scaling down for event end
        scheduleScalingDown(session, profile);
    }
    
    /**
     * Execute scaling down phase
     */
    private void executeScalingDownPhase(CapacityScalingSession session, ScalingProfile profile) {
        session.setCurrentPhase(ScalingPhase.SCALING_DOWN);
        
        logger.info("Starting scaling down phase for session: " + session.getSessionId());
        
        // Scale down to base capacity
        for (Map.Entry<String, Integer> entry : session.getAllocatedInstances().entrySet()) {
            String instanceType = entry.getKey();
            int currentCount = entry.getValue();
            int baseCount = profile.getBaseInstanceCounts().getOrDefault(instanceType, 0);
            int reduceCount = currentCount - baseCount;
            
            if (reduceCount > 0) {
                releaseInstancesFromGIM(session, instanceType, reduceCount);
            }
        }
        
        session.setCurrentPhase(ScalingPhase.COMPLETED);
        
        // Clean up session
        cleanupScalingSession(session);
    }
    
    /**
     * Request instances from GIM
     */
    private void requestInstancesFromGIM(CapacityScalingSession session, String instanceType, 
                                       int count, String reason) {
        try {
            // This would integrate with the actual GIM instance creation
            // For now, simulate the request
            
            session.addAllocatedInstances(instanceType, count);
            
            logger.info("Requested " + count + " " + instanceType + " instances for session " + 
                       session.getSessionId() + " (reason: " + reason + ")");
            
            // Update state synchronization layer
            updateCapacityState(session, instanceType, count, "allocated");
            
        } catch (Exception e) {
            logger.severe("Error requesting instances from GIM: " + e.getMessage());
        }
    }
    
    /**
     * Release instances from GIM
     */
    private void releaseInstancesFromGIM(CapacityScalingSession session, String instanceType, int count) {
        try {
            // This would integrate with the actual GIM instance cleanup
            // For now, simulate the release
            
            int currentCount = session.getAllocatedInstances().getOrDefault(instanceType, 0);
            int newCount = Math.max(0, currentCount - count);
            session.getAllocatedInstances().put(instanceType, newCount);
            
            logger.info("Released " + count + " " + instanceType + " instances for session " + 
                       session.getSessionId());
            
            // Update state synchronization layer
            updateCapacityState(session, instanceType, -count, "released");
            
        } catch (Exception e) {
            logger.severe("Error releasing instances from GIM: " + e.getMessage());
        }
    }
    
    /**
     * Schedule scaling up
     */
    private void scheduleScalingUp(CapacityScalingSession session, ScalingProfile profile) {
        // This would schedule the scaling up for event start time
        logger.info("Scheduled scaling up for session: " + session.getSessionId() + 
                   " in " + profile.getRampUpTimeMinutes() + " minutes");
    }
    
    /**
     * Schedule scaling down
     */
    private void scheduleScalingDown(CapacityScalingSession session, ScalingProfile profile) {
        // This would schedule the scaling down for event end time
        logger.info("Scheduled scaling down for session: " + session.getSessionId() + 
                   " in " + profile.getRampDownTimeMinutes() + " minutes");
    }
    
    /**
     * Update capacity state in synchronization layer
     */
    private void updateCapacityState(CapacityScalingSession session, String instanceType, 
                                   int countChange, String action) {
        try {
            Map<String, Object> capacityUpdate = new HashMap<>();
            capacityUpdate.put("sessionId", session.getSessionId());
            capacityUpdate.put("eventId", session.getEventId());
            capacityUpdate.put("instanceType", instanceType);
            capacityUpdate.put("countChange", countChange);
            capacityUpdate.put("action", action);
            capacityUpdate.put("timestamp", System.currentTimeMillis());
            
            // This would update the state synchronization layer
            logger.info("Updated capacity state: " + instanceType + " " + action + " " + countChange);
            
        } catch (Exception e) {
            logger.severe("Error updating capacity state: " + e.getMessage());
        }
    }
    
    /**
     * Clean up scaling session
     */
    private void cleanupScalingSession(CapacityScalingSession session) {
        activeScalingSessions.remove(session.getSessionId());
        
        // Record metrics
        recordCapacityMetrics(session);
        
        logger.info("Cleaned up scaling session: " + session.getSessionId());
    }
    
    /**
     * Record capacity metrics
     */
    private void recordCapacityMetrics(CapacityScalingSession session) {
        try {
            // Collect current metrics
            Map<String, Integer> instanceCounts = new HashMap<>(session.getAllocatedInstances());
            Map<String, Double> utilizationRates = new HashMap<>();
            
            // This would collect actual utilization data
            for (String instanceType : instanceCounts.keySet()) {
                utilizationRates.put(instanceType, 0.75); // Placeholder
            }
            
            CapacityMetrics metrics = new CapacityMetrics(
                session.getEventId(), session.getEventCategory(), instanceCounts,
                utilizationRates, 0, 50.0); // Placeholder values
            
            capacityMetrics.put(session.getEventId(), metrics);
            
        } catch (Exception e) {
            logger.severe("Error recording capacity metrics: " + e.getMessage());
        }
    }
    
    /**
     * Start monitoring tasks
     */
    private void startMonitoringTasks() {
        // This would start periodic monitoring tasks
        logger.info("Started capacity monitoring tasks");
    }
    
    /**
     * Get active scaling sessions
     */
    public Map<String, CapacityScalingSession> getActiveScalingSessions() {
        return new HashMap<>(activeScalingSessions);
    }
    
    /**
     * Get scaling profile for event category
     */
    public ScalingProfile getScalingProfile(EventCategory category) {
        return scalingProfiles.get(category);
    }
    
    /**
     * Get capacity metrics for event
     */
    public CapacityMetrics getCapacityMetrics(String eventId) {
        return capacityMetrics.get(eventId);
    }
    
    /**
     * Get capacity scaling statistics
     */
    public Map<String, Object> getCapacityScalingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("activeScalingSessions", activeScalingSessions.size());
        stats.put("scalingProfiles", scalingProfiles.size());
        stats.put("capacityMetrics", capacityMetrics.size());
        
        Map<String, Integer> phaseCounts = new HashMap<>();
        for (CapacityScalingSession session : activeScalingSessions.values()) {
            String phase = session.getCurrentPhase().name();
            phaseCounts.merge(phase, 1, Integer::sum);
        }
        stats.put("phaseCounts", phaseCounts);
        
        return stats;
    }
}
