package de.noctivag.skyblock.core.events;

import de.noctivag.skyblock.core.architecture.GlobalInstanceManager;
import de.noctivag.skyblock.core.architecture.StateSynchronizationLayer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Global Calendar and Event Scheduler - Central microservice for time-based events
 * 
 * Features:
 * - Central microservice managing game calendar
 * - Events triggered at precise times
 * - Proactive capacity adjustment communication with GIM
 * - Event categories and priorities
 * - Event history and statistics
 * - Configurable event schedules
 */
public class GlobalCalendarEventScheduler {
    
    private static final Logger logger = Logger.getLogger(GlobalCalendarEventScheduler.class.getName());
    
    // Core components
    private final GlobalInstanceManager gim;
    private final StateSynchronizationLayer stateLayer;
    
    // Scheduling infrastructure
    private final ScheduledExecutorService eventScheduler;
    private final ExecutorService eventExecutor;
    
    // Event management
    private final Map<String, ScheduledGameEvent> scheduledEvents = new ConcurrentHashMap<>();
    private final Map<String, GameEvent> activeEvents = new ConcurrentHashMap<>();
    private final List<GameEvent> eventHistory = new ArrayList<>();
    
    // Event categories
    public enum EventCategory {
        FARMING_CONTEST("Farming Contest", 30, 300), // 30 min prep, 300 min duration
        MINING_CONTEST("Mining Contest", 15, 180),   // 15 min prep, 180 min duration
        FISHING_CONTEST("Fishing Contest", 20, 240), // 20 min prep, 240 min duration
        SLAYER_EVENT("Slayer Event", 10, 120),       // 10 min prep, 120 min duration
        DUNGEON_EVENT("Dungeon Event", 5, 60),       // 5 min prep, 60 min duration
        AUCTION_EVENT("Auction Event", 0, 30),       // No prep, 30 min duration
        SPECIAL_EVENT("Special Event", 60, 480);     // 60 min prep, 480 min duration
        
        private final String displayName;
        private final int preparationMinutes;
        private final int durationMinutes;
        
        EventCategory(String displayName, int preparationMinutes, int durationMinutes) {
            this.displayName = displayName;
            this.preparationMinutes = preparationMinutes;
            this.durationMinutes = durationMinutes;
        }
        
        // Getters
        public String getDisplayName() { return displayName; }
        public int getPreparationMinutes() { return preparationMinutes; }
        public int getDurationMinutes() { return durationMinutes; }
    }
    
    /**
     * Game Event Definition
     */
    public static class GameEvent {
        private final String eventId;
        private final String eventName;
        private final EventCategory category;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final Map<String, Object> eventData;
        private final EventPriority priority;
        private final List<String> affectedServers;
        
        public GameEvent(String eventId, String eventName, EventCategory category,
                        LocalDateTime startTime, LocalDateTime endTime, EventPriority priority) {
            this.eventId = eventId;
            this.eventName = eventName;
            this.category = category;
            this.startTime = startTime;
            this.endTime = endTime;
            this.priority = priority;
            this.eventData = new ConcurrentHashMap<>();
            this.affectedServers = new ArrayList<>();
        }
        
        // Getters
        public String getEventId() { return eventId; }
        public String getEventName() { return eventName; }
        public EventCategory getCategory() { return category; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public Map<String, Object> getEventData() { return eventData; }
        public EventPriority getPriority() { return priority; }
        public List<String> getAffectedServers() { return affectedServers; }
        
        public boolean isActive() {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(startTime) && now.isBefore(endTime);
        }
        
        public boolean isUpcoming() {
            return LocalDateTime.now().isBefore(startTime);
        }
        
        public long getMinutesUntilStart() {
            return ChronoUnit.MINUTES.between(LocalDateTime.now(), startTime);
        }
    }
    
    /**
     * Event Priority Levels
     */
    public enum EventPriority {
        LOW(1, "Low"),
        MEDIUM(2, "Medium"),
        HIGH(3, "High"),
        CRITICAL(4, "Critical");
        
        private final int level;
        private final String displayName;
        
        EventPriority(int level, String displayName) {
            this.level = level;
            this.displayName = displayName;
        }
        
        public int getLevel() { return level; }
        public String getDisplayName() { return displayName; }
        
        public boolean isHigherThan(EventPriority other) {
            return this.level > other.level;
        }
    }
    
    /**
     * Scheduled Game Event
     */
    public static class ScheduledGameEvent {
        private final GameEvent event;
        private final ScheduledFuture<?> preparationTask;
        private final ScheduledFuture<?> startTask;
        private final ScheduledFuture<?> endTask;
        private final LocalDateTime scheduledTime;
        
        public ScheduledGameEvent(GameEvent event, ScheduledFuture<?> preparationTask,
                                ScheduledFuture<?> startTask, ScheduledFuture<?> endTask,
                                LocalDateTime scheduledTime) {
            this.event = event;
            this.preparationTask = preparationTask;
            this.startTask = startTask;
            this.endTask = endTask;
            this.scheduledTime = scheduledTime;
        }
        
        public void cancel() {
            if (preparationTask != null) preparationTask.cancel(false);
            if (startTask != null) startTask.cancel(false);
            if (endTask != null) endTask.cancel(false);
        }
        
        // Getters
        public GameEvent getEvent() { return event; }
        public LocalDateTime getScheduledTime() { return scheduledTime; }
    }
    
    /**
     * Capacity Adjustment Request
     */
    public static class CapacityAdjustmentRequest {
        private final String eventId;
        private final EventCategory category;
        private final int additionalInstances;
        private final List<String> instanceTypes;
        private final String reason;
        private final LocalDateTime requestedTime;
        
        public CapacityAdjustmentRequest(String eventId, EventCategory category,
                                       int additionalInstances, List<String> instanceTypes,
                                       String reason) {
            this.eventId = eventId;
            this.category = category;
            this.additionalInstances = additionalInstances;
            this.instanceTypes = new ArrayList<>(instanceTypes);
            this.reason = reason;
            this.requestedTime = LocalDateTime.now();
        }
        
        // Getters
        public String getEventId() { return eventId; }
        public EventCategory getCategory() { return category; }
        public int getAdditionalInstances() { return additionalInstances; }
        public List<String> getInstanceTypes() { return instanceTypes; }
        public String getReason() { return reason; }
        public LocalDateTime getRequestedTime() { return requestedTime; }
    }
    
    public GlobalCalendarEventScheduler(GlobalInstanceManager gim, StateSynchronizationLayer stateLayer) {
        this.gim = gim;
        this.stateLayer = stateLayer;
        
        // Initialize thread pools
        this.eventScheduler = Executors.newScheduledThreadPool(4, r -> new Thread(r, "EventScheduler-Thread"));
        this.eventExecutor = Executors.newFixedThreadPool(8, r -> new Thread(r, "EventExecutor-Thread"));
        
        // Start background tasks
        startPeriodicTasks();
        
        logger.info("Global Calendar Event Scheduler initialized");
    }
    
    /**
     * Schedule a new game event
     */
    public CompletableFuture<String> scheduleEvent(String eventName, EventCategory category,
                                                   LocalDateTime startTime, EventPriority priority) {
        return CompletableFuture.supplyAsync(() -> {
            String eventId = UUID.randomUUID().toString();
            LocalDateTime endTime = startTime.plusMinutes(category.getDurationMinutes());
            
            GameEvent event = new GameEvent(eventId, eventName, category, startTime, endTime, priority);
            
            // Schedule the event
            scheduleEventTasks(event);
            
            scheduledEvents.put(eventId, new ScheduledGameEvent(event, null, null, null, LocalDateTime.now()));
            
            logger.info("Scheduled event: " + eventName + " (" + eventId + ") for " + startTime);
            
            return eventId;
        });
    }
    
    /**
     * Schedule event-related tasks
     */
    private void scheduleEventTasks(GameEvent event) {
        LocalDateTime now = LocalDateTime.now();
        
        // Schedule preparation phase
        if (event.getCategory().getPreparationMinutes() > 0) {
            LocalDateTime preparationTime = event.getStartTime().minusMinutes(event.getCategory().getPreparationMinutes());
            if (preparationTime.isAfter(now)) {
                long delay = ChronoUnit.MILLISECONDS.between(now, preparationTime);
                eventScheduler.schedule(() -> handleEventPreparation(event), delay, TimeUnit.MILLISECONDS);
            }
        }
        
        // Schedule event start
        if (event.getStartTime().isAfter(now)) {
            long delay = ChronoUnit.MILLISECONDS.between(now, event.getStartTime());
            eventScheduler.schedule(() -> handleEventStart(event), delay, TimeUnit.MILLISECONDS);
        }
        
        // Schedule event end
        if (event.getEndTime().isAfter(now)) {
            long delay = ChronoUnit.MILLISECONDS.between(now, event.getEndTime());
            eventScheduler.schedule(() -> handleEventEnd(event), delay, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Handle event preparation phase
     */
    private void handleEventPreparation(GameEvent event) {
        eventExecutor.submit(() -> {
            logger.info("Starting preparation phase for event: " + event.getEventName());
            
            try {
                // Send capacity adjustment request to GIM
                sendCapacityAdjustmentRequest(event, "preparation");
                
                // Notify affected systems
                notifyEventPreparation(event);
                
                // Update state synchronization layer
                updateEventState(event, "PREPARATION");
                
            } catch (Exception e) {
                logger.severe("Error during event preparation: " + e.getMessage());
            }
        });
    }
    
    /**
     * Handle event start
     */
    private void handleEventStart(GameEvent event) {
        eventExecutor.submit(() -> {
            logger.info("Starting event: " + event.getEventName());
            
            try {
                // Send capacity adjustment request to GIM
                sendCapacityAdjustmentRequest(event, "start");
                
                // Activate the event
                activeEvents.put(event.getEventId(), event);
                
                // Notify affected systems
                notifyEventStart(event);
                
                // Update state synchronization layer
                updateEventState(event, "ACTIVE");
                
            } catch (Exception e) {
                logger.severe("Error during event start: " + e.getMessage());
            }
        });
    }
    
    /**
     * Handle event end
     */
    private void handleEventEnd(GameEvent event) {
        eventExecutor.submit(() -> {
            logger.info("Ending event: " + event.getEventName());
            
            try {
                // Remove from active events
                activeEvents.remove(event.getEventId());
                
                // Add to event history
                eventHistory.add(event);
                
                // Send capacity adjustment request to GIM (scale down)
                sendCapacityAdjustmentRequest(event, "end");
                
                // Notify affected systems
                notifyEventEnd(event);
                
                // Update state synchronization layer
                updateEventState(event, "COMPLETED");
                
                // Clean up scheduled event
                scheduledEvents.remove(event.getEventId());
                
            } catch (Exception e) {
                logger.severe("Error during event end: " + e.getMessage());
            }
        });
    }
    
    /**
     * Send capacity adjustment request to GIM
     */
    private void sendCapacityAdjustmentRequest(GameEvent event, String phase) {
        try {
            int additionalInstances = calculateRequiredInstances(event, phase);
            List<String> instanceTypes = determineInstanceTypes(event);
            
            CapacityAdjustmentRequest request = new CapacityAdjustmentRequest(
                event.getEventId(),
                event.getCategory(),
                additionalInstances,
                instanceTypes,
                "Event " + phase + " for " + event.getEventName()
            );
            
            // Send request to GIM
            sendToGIM(request);
            
            logger.info("Sent capacity adjustment request: " + additionalInstances + 
                       " additional instances for " + event.getEventName());
            
        } catch (Exception e) {
            logger.severe("Error sending capacity adjustment request: " + e.getMessage());
        }
    }
    
    /**
     * Calculate required instances for event
     */
    private int calculateRequiredInstances(GameEvent event, String phase) {
        switch (event.getCategory()) {
            case FARMING_CONTEST:
                return phase.equals("start") ? 5 : 2; // 5 instances during event, 2 for prep
            case MINING_CONTEST:
                return phase.equals("start") ? 3 : 1;
            case FISHING_CONTEST:
                return phase.equals("start") ? 4 : 1;
            case SLAYER_EVENT:
                return phase.equals("start") ? 2 : 1;
            case DUNGEON_EVENT:
                return phase.equals("start") ? 6 : 1;
            case AUCTION_EVENT:
                return phase.equals("start") ? 1 : 0;
            case SPECIAL_EVENT:
                return phase.equals("start") ? 8 : 3;
            default:
                return 1;
        }
    }
    
    /**
     * Determine instance types for event
     */
    private List<String> determineInstanceTypes(GameEvent event) {
        List<String> types = new ArrayList<>();
        
        switch (event.getCategory()) {
            case FARMING_CONTEST:
                types.add("MINING_ZONE");
                types.add("COMBAT_ZONE");
                break;
            case MINING_CONTEST:
                types.add("MINING_ZONE");
                break;
            case FISHING_CONTEST:
                types.add("MINING_ZONE");
                break;
            case SLAYER_EVENT:
                types.add("COMBAT_ZONE");
                break;
            case DUNGEON_EVENT:
                types.add("DUNGEON_INSTANCE");
                types.add("COMBAT_ZONE");
                break;
            case AUCTION_EVENT:
                types.add("PERSISTENT_PUBLIC");
                break;
            case SPECIAL_EVENT:
                types.add("SPECIAL_EVENT");
                types.add("COMBAT_ZONE");
                types.add("MINING_ZONE");
                break;
        }
        
        return types;
    }
    
    /**
     * Send capacity adjustment request to GIM
     */
    private void sendToGIM(CapacityAdjustmentRequest request) {
        // This would integrate with the GIM system
        // For now, just log the request
        logger.info("GIM Capacity Request: " + request.getAdditionalInstances() + 
                   " instances of types " + request.getInstanceTypes() + 
                   " for reason: " + request.getReason());
    }
    
    /**
     * Notify event preparation
     */
    private void notifyEventPreparation(GameEvent event) {
        // Notify relevant systems about upcoming event
        logger.info("Notifying systems about event preparation: " + event.getEventName());
    }
    
    /**
     * Notify event start
     */
    private void notifyEventStart(GameEvent event) {
        // Notify relevant systems about event start
        logger.info("Notifying systems about event start: " + event.getEventName());
    }
    
    /**
     * Notify event end
     */
    private void notifyEventEnd(GameEvent event) {
        // Notify relevant systems about event end
        logger.info("Notifying systems about event end: " + event.getEventName());
    }
    
    /**
     * Update event state in synchronization layer
     */
    private void updateEventState(GameEvent event, String state) {
        try {
            Map<String, Object> eventState = new HashMap<>();
            eventState.put("eventId", event.getEventId());
            eventState.put("eventName", event.getEventName());
            eventState.put("category", event.getCategory().name());
            eventState.put("state", state);
            eventState.put("timestamp", System.currentTimeMillis());
            
            // This would update the state synchronization layer
            logger.info("Updated event state: " + event.getEventName() + " -> " + state);
            
        } catch (Exception e) {
            logger.severe("Error updating event state: " + e.getMessage());
        }
    }
    
    /**
     * Start periodic background tasks
     */
    private void startPeriodicTasks() {
        // Clean up expired scheduled events
        eventScheduler.scheduleAtFixedRate(() -> {
            cleanupExpiredEvents();
        }, 1, 1, TimeUnit.HOURS);
        
        // Monitor active events
        eventScheduler.scheduleAtFixedRate(() -> {
            monitorActiveEvents();
        }, 0, 1, TimeUnit.MINUTES);
    }
    
    /**
     * Clean up expired events
     */
    private void cleanupExpiredEvents() {
        LocalDateTime now = LocalDateTime.now();
        int cleaned = 0;
        
        Iterator<Map.Entry<String, ScheduledGameEvent>> iterator = scheduledEvents.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ScheduledGameEvent> entry = iterator.next();
            GameEvent event = entry.getValue().getEvent();
            
            if (event.getEndTime().isBefore(now)) {
                entry.getValue().cancel();
                iterator.remove();
                cleaned++;
            }
        }
        
        if (cleaned > 0) {
            logger.info("Cleaned up " + cleaned + " expired events");
        }
    }
    
    /**
     * Monitor active events
     */
    private void monitorActiveEvents() {
        for (GameEvent event : activeEvents.values()) {
            if (!event.isActive()) {
                // Event should have ended but is still in active list
                handleEventEnd(event);
            }
        }
    }
    
    /**
     * Get upcoming events
     */
    public List<GameEvent> getUpcomingEvents(int limit) {
        LocalDateTime now = LocalDateTime.now();
        return scheduledEvents.values().stream()
                .map(ScheduledGameEvent::getEvent)
                .filter(GameEvent::isUpcoming)
                .sorted(Comparator.comparing(GameEvent::getStartTime))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get active events
     */
    public List<GameEvent> getActiveEvents() {
        return new ArrayList<>(activeEvents.values());
    }
    
    /**
     * Get event history
     */
    public List<GameEvent> getEventHistory(int limit) {
        return eventHistory.stream()
                .sorted((e1, e2) -> e2.getEndTime().compareTo(e1.getEndTime()))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Cancel an event
     */
    public boolean cancelEvent(String eventId) {
        ScheduledGameEvent scheduledEvent = scheduledEvents.get(eventId);
        if (scheduledEvent != null) {
            scheduledEvent.cancel();
            scheduledEvents.remove(eventId);
            logger.info("Cancelled event: " + eventId);
            return true;
        }
        return false;
    }
    
    /**
     * Get scheduler statistics
     */
    public Map<String, Object> getSchedulerStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("scheduledEvents", scheduledEvents.size());
        stats.put("activeEvents", activeEvents.size());
        stats.put("eventHistory", eventHistory.size());
        
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (GameEvent event : activeEvents.values()) {
            String category = event.getCategory().name();
            categoryCounts.merge(category, 1, Integer::sum);
        }
        stats.put("activeEventCategories", categoryCounts);
        
        return stats;
    }
    
    /**
     * Shutdown the scheduler
     */
    public void shutdown() {
        eventScheduler.shutdown();
        eventExecutor.shutdown();
        
        try {
            if (!eventScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                eventScheduler.shutdownNow();
            }
            if (!eventExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                eventExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            eventScheduler.shutdownNow();
            eventExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
