package de.noctivag.skyblock.engine.temporal;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.engine.temporal.data.*;
import de.noctivag.skyblock.engine.temporal.services.GIMIntegrationService;
import de.noctivag.skyblock.network.ServerManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Global Time Management Service - Agent V Implementation
 * 
 * Manages the global game calendar, events, and temporal systems.
 * Coordinates with GIM system for proactive scaling during high-load events.
 * 
 * Features:
 * - Global calendar with Mayor elections, Jacob's events, Cult of the Fallen Star
 * - Event scheduling and triggering
 * - GIM integration for proactive server scaling
 * - Time synchronization across all server instances
 */
public class GlobalTimeManagementService {
    
    private final Plugin plugin;
    private final ServerManager serverManager;
    private final JedisPool jedisPool;
    private final GIMIntegrationService gimIntegration;
    
    // Calendar and events
    private final Map<String, GlobalEvent> scheduledEvents = new ConcurrentHashMap<>();
    private final Map<String, GlobalEvent> activeEvents = new ConcurrentHashMap<>();
    private final Map<String, MayorElection> mayorElections = new ConcurrentHashMap<>();
    private final Map<String, JacobsEvent> jacobsEvents = new ConcurrentHashMap<>();
    private final Map<String, CultEvent> cultEvents = new ConcurrentHashMap<>();
    
    // Time management
    private final Map<String, Long> eventTimers = new ConcurrentHashMap<>();
    private final Map<String, Long> lastEventTriggers = new ConcurrentHashMap<>();
    
    // Configuration
    private static final long CALENDAR_UPDATE_INTERVAL = 1000L; // 1 second
    private static final long EVENT_CHECK_INTERVAL = 5000L; // 5 seconds
    private static final long GIM_SCALING_CHECK_INTERVAL = 10000L; // 10 seconds
    
    // Redis keys
    private static final String GLOBAL_TIME_KEY = "temporal:global_time";
    private static final String EVENTS_KEY = "temporal:events";
    private static final String MAYOR_ELECTIONS_KEY = "temporal:mayor_elections";
    private static final String JACOBS_EVENTS_KEY = "temporal:jacobs_events";
    private static final String CULT_EVENTS_KEY = "temporal:cult_events";
    private static final String EVENT_TRIGGERS_CHANNEL = "temporal:event_triggers";
    private static final String TIME_SYNC_CHANNEL = "temporal:time_sync";
    
    public GlobalTimeManagementService(Plugin plugin, ServerManager serverManager) {
        this.plugin = plugin;
        this.serverManager = serverManager;
        this.jedisPool = serverManager.getJedisPool();
        this.gimIntegration = new GIMIntegrationService(this);
        
        initializeService();
    }
    
    /**
     * Initialize the global time management service
     */
    private void initializeService() {
        try {
            // Load existing events from Redis
            loadEventsFromRedis();
            
            // Initialize default events
            initializeDefaultEvents();
            
            // Start time synchronization
            startTimeSynchronization();
            
            // Start event monitoring
            startEventMonitoring();
            
            // Start GIM integration
            gimIntegration.initialize();
            
            plugin.getLogger().info("Global Time Management Service initialized successfully");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize Global Time Management Service", e);
        }
    }
    
    /**
     * Load events from Redis on startup
     */
    private void loadEventsFromRedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            // Load global events
            Map<String, String> eventData = jedis.hgetAll(EVENTS_KEY);
            for (Map.Entry<String, String> entry : eventData.entrySet()) {
                GlobalEvent event = GlobalEvent.fromJson(entry.getValue());
                scheduledEvents.put(entry.getKey(), event);
            }
            
            // Load mayor elections
            Map<String, String> mayorData = jedis.hgetAll(MAYOR_ELECTIONS_KEY);
            for (Map.Entry<String, String> entry : mayorData.entrySet()) {
                MayorElection election = MayorElection.fromJson(entry.getValue());
                mayorElections.put(entry.getKey(), election);
            }
            
            // Load Jacob's events
            Map<String, String> jacobsData = jedis.hgetAll(JACOBS_EVENTS_KEY);
            for (Map.Entry<String, String> entry : jacobsData.entrySet()) {
                JacobsEvent event = JacobsEvent.fromJson(entry.getValue());
                jacobsEvents.put(entry.getKey(), event);
            }
            
            // Load Cult events
            Map<String, String> cultData = jedis.hgetAll(CULT_EVENTS_KEY);
            for (Map.Entry<String, String> entry : cultData.entrySet()) {
                CultEvent event = CultEvent.fromJson(entry.getValue());
                cultEvents.put(entry.getKey(), event);
            }
            
            plugin.getLogger().info("Loaded " + scheduledEvents.size() + " events from Redis");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load events from Redis", e);
        }
    }
    
    /**
     * Initialize default events
     */
    private void initializeDefaultEvents() {
        // Mayor Elections (every 5 days)
        scheduleMayorElection("mayor_election_1", 
            LocalDateTime.now().plusDays(5).withHour(12).withMinute(0),
            Arrays.asList("Diana", "Jerry", "Marina", "Paul", "Cole", "Foxy", "Aatrox", "Dante"));
        
        // Jacob's Farming Contests (every 2 days)
        scheduleJacobsEvent("jacobs_contest_1",
            LocalDateTime.now().plusDays(2).withHour(14).withMinute(0),
            JacobsEvent.ContestType.WHEAT);
        
        scheduleJacobsEvent("jacobs_contest_2",
            LocalDateTime.now().plusDays(4).withHour(14).withMinute(0),
            JacobsEvent.ContestType.CARROT);
        
        // Cult of the Fallen Star (every 7 days)
        scheduleCultEvent("cult_event_1",
            LocalDateTime.now().plusDays(7).withHour(20).withMinute(0),
            CultEvent.EventType.METEOR_SHOWER);
        
        // Special Events
        scheduleGlobalEvent("double_coins_weekend",
            LocalDateTime.now().plusDays(1).withHour(18).withMinute(0),
            GlobalEvent.EventType.DOUBLE_COINS,
            48); // 48 hours duration
        
        scheduleGlobalEvent("mining_fiesta",
            LocalDateTime.now().plusDays(3).withHour(10).withMinute(0),
            GlobalEvent.EventType.MINING_FIESTA,
            24); // 24 hours duration
    }
    
    /**
     * Start time synchronization across all servers
     */
    private void startTimeSynchronization() {
        new BukkitRunnable() {
            @Override
            public void run() {
                synchronizeGlobalTime();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, CALENDAR_UPDATE_INTERVAL / 50L);
    }
    
    /**
     * Start event monitoring and triggering
     */
    private void startEventMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkAndTriggerEvents();
                updateEventStatus();
                cleanupExpiredEvents();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, EVENT_CHECK_INTERVAL / 50L);
    }
    
    /**
     * Synchronize global time across all servers
     */
    private void synchronizeGlobalTime() {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = System.currentTimeMillis();
            LocalDateTime globalTime = LocalDateTime.now(ZoneOffset.UTC);
            
            // Store global time
            jedis.hset(GLOBAL_TIME_KEY, "timestamp", String.valueOf(currentTime));
            jedis.hset(GLOBAL_TIME_KEY, "year", String.valueOf(globalTime.getYear()));
            jedis.hset(GLOBAL_TIME_KEY, "month", String.valueOf(globalTime.getMonthValue()));
            jedis.hset(GLOBAL_TIME_KEY, "day", String.valueOf(globalTime.getDayOfMonth()));
            jedis.hset(GLOBAL_TIME_KEY, "hour", String.valueOf(globalTime.getHour()));
            jedis.hset(GLOBAL_TIME_KEY, "minute", String.valueOf(globalTime.getMinute()));
            jedis.hset(GLOBAL_TIME_KEY, "second", String.valueOf(globalTime.getSecond()));
            
            // Broadcast time sync
            TimeSyncMessage message = new TimeSyncMessage(currentTime, globalTime, serverManager.getServerId());
            jedis.publish(TIME_SYNC_CHANNEL, message.toJson());
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to synchronize global time", e);
        }
    }
    
    /**
     * Check and trigger events
     */
    private void checkAndTriggerEvents() {
        LocalDateTime now = LocalDateTime.now();
        
        for (Map.Entry<String, GlobalEvent> entry : scheduledEvents.entrySet()) {
            String eventId = entry.getKey();
            GlobalEvent event = entry.getValue();
            
            // Check if event should start
            if (shouldTriggerEvent(event, now)) {
                triggerEvent(event);
            }
            
            // Check if event should end
            if (activeEvents.containsKey(eventId) && shouldEndEvent(event, now)) {
                endEvent(event);
            }
        }
        
        // Check mayor elections
        for (Map.Entry<String, MayorElection> entry : mayorElections.entrySet()) {
            MayorElection election = entry.getValue();
            if (shouldTriggerElection(election, now)) {
                triggerMayorElection(election);
            }
        }
        
        // Check Jacob's events
        for (Map.Entry<String, JacobsEvent> entry : jacobsEvents.entrySet()) {
            JacobsEvent event = entry.getValue();
            if (shouldTriggerJacobsEvent(event, now)) {
                triggerJacobsEvent(event);
            }
        }
        
        // Check Cult events
        for (Map.Entry<String, CultEvent> entry : cultEvents.entrySet()) {
            CultEvent event = entry.getValue();
            if (shouldTriggerCultEvent(event, now)) {
                triggerCultEvent(event);
            }
        }
    }
    
    /**
     * Check if event should be triggered
     */
    private boolean shouldTriggerEvent(GlobalEvent event, LocalDateTime now) {
        return !activeEvents.containsKey(event.getId()) && 
               now.isAfter(event.getStartTime()) && 
               now.isBefore(event.getStartTime().plusHours(event.getDuration()));
    }
    
    /**
     * Check if event should end
     */
    private boolean shouldEndEvent(GlobalEvent event, LocalDateTime now) {
        return now.isAfter(event.getStartTime().plusHours(event.getDuration()));
    }
    
    /**
     * Trigger an event
     */
    private void triggerEvent(GlobalEvent event) {
        try {
            // Add to active events
            activeEvents.put(event.getId(), event);
            
            // Request GIM scaling if needed
            if (event.requiresScaling()) {
                gimIntegration.requestScaling(event.getId(), event.getExpectedPlayerLoad());
            }
            
            // Broadcast event trigger
            broadcastEventTrigger(event);
            
            // Announce to players
            announceEventStart(event);
            
            plugin.getLogger().info("Event triggered: " + event.getName());
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to trigger event: " + event.getName(), e);
        }
    }
    
    /**
     * End an event
     */
    private void endEvent(GlobalEvent event) {
        try {
            // Remove from active events
            activeEvents.remove(event.getId());
            
            // Release GIM scaling
            if (event.requiresScaling()) {
                gimIntegration.releaseScaling(event.getId());
            }
            
            // Broadcast event end
            broadcastEventEnd(event);
            
            // Announce to players
            announceEventEnd(event);
            
            plugin.getLogger().info("Event ended: " + event.getName());
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to end event: " + event.getName(), e);
        }
    }
    
    /**
     * Schedule a global event
     */
    public void scheduleGlobalEvent(String eventId, LocalDateTime startTime, GlobalEvent.EventType type, int durationHours) {
        GlobalEvent event = new GlobalEvent(
            eventId,
            "Global Event",
            "A special global event",
            type,
            startTime,
            durationHours,
            false, // Not recurring by default
            false  // No scaling by default
        );
        
        scheduledEvents.put(eventId, event);
        storeEventInRedis(event);
    }
    
    /**
     * Schedule a mayor election
     */
    public void scheduleMayorElection(String electionId, LocalDateTime startTime, List<String> candidates) {
        MayorElection election = new MayorElection(
            electionId,
            startTime,
            candidates,
            MayorElection.ElectionStatus.SCHEDULED
        );
        
        mayorElections.put(electionId, election);
        storeMayorElectionInRedis(election);
    }
    
    /**
     * Schedule a Jacob's event
     */
    public void scheduleJacobsEvent(String eventId, LocalDateTime startTime, JacobsEvent.ContestType contestType) {
        JacobsEvent event = new JacobsEvent(
            eventId,
            startTime,
            contestType,
            JacobsEvent.EventStatus.SCHEDULED
        );
        
        jacobsEvents.put(eventId, event);
        storeJacobsEventInRedis(event);
    }
    
    /**
     * Schedule a Cult event
     */
    public void scheduleCultEvent(String eventId, LocalDateTime startTime, CultEvent.EventType eventType) {
        CultEvent event = new CultEvent(
            eventId,
            startTime,
            eventType,
            CultEvent.EventStatus.SCHEDULED
        );
        
        cultEvents.put(eventId, event);
        storeCultEventInRedis(event);
    }
    
    /**
     * Trigger mayor election
     */
    private void triggerMayorElection(MayorElection election) {
        election.setStatus(MayorElection.ElectionStatus.ACTIVE);
        
        // Request scaling for election
        gimIntegration.requestScaling("mayor_election", 200); // Expect 200% player load
        
        // Announce election
        announceMayorElection(election);
        
        plugin.getLogger().info("Mayor election started: " + election.getId());
    }
    
    /**
     * Trigger Jacob's event
     */
    private void triggerJacobsEvent(JacobsEvent event) {
        event.setStatus(JacobsEvent.EventStatus.ACTIVE);
        
        // Request scaling for farming contest
        gimIntegration.requestScaling("jacobs_contest", 150); // Expect 150% player load
        
        // Announce event
        announceJacobsEvent(event);
        
        plugin.getLogger().info("Jacob's event started: " + event.getId());
    }
    
    /**
     * Trigger Cult event
     */
    private void triggerCultEvent(CultEvent event) {
        event.setStatus(CultEvent.EventStatus.ACTIVE);
        
        // Request scaling for cult event
        gimIntegration.requestScaling("cult_event", 300); // Expect 300% player load
        
        // Announce event
        announceCultEvent(event);
        
        plugin.getLogger().info("Cult event started: " + event.getId());
    }
    
    /**
     * Check if mayor election should be triggered
     */
    private boolean shouldTriggerElection(MayorElection election, LocalDateTime now) {
        return election.getStatus() == MayorElection.ElectionStatus.SCHEDULED &&
               now.isAfter(election.getStartTime());
    }
    
    /**
     * Check if Jacob's event should be triggered
     */
    private boolean shouldTriggerJacobsEvent(JacobsEvent event, LocalDateTime now) {
        return event.getStatus() == JacobsEvent.EventStatus.SCHEDULED &&
               now.isAfter(event.getStartTime());
    }
    
    /**
     * Check if Cult event should be triggered
     */
    private boolean shouldTriggerCultEvent(CultEvent event, LocalDateTime now) {
        return event.getStatus() == CultEvent.EventStatus.SCHEDULED &&
               now.isAfter(event.getStartTime());
    }
    
    /**
     * Broadcast event trigger to all servers
     */
    private void broadcastEventTrigger(GlobalEvent event) {
        try (Jedis jedis = jedisPool.getResource()) {
            EventTriggerMessage message = new EventTriggerMessage(
                event.getId(),
                event.getName(),
                event.getType(),
                EventTriggerMessage.TriggerType.START,
                System.currentTimeMillis()
            );
            
            jedis.publish(EVENT_TRIGGERS_CHANNEL, message.toJson());
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to broadcast event trigger", e);
        }
    }
    
    /**
     * Broadcast event end to all servers
     */
    private void broadcastEventEnd(GlobalEvent event) {
        try (Jedis jedis = jedisPool.getResource()) {
            EventTriggerMessage message = new EventTriggerMessage(
                event.getId(),
                event.getName(),
                event.getType(),
                EventTriggerMessage.TriggerType.END,
                System.currentTimeMillis()
            );
            
            jedis.publish(EVENT_TRIGGERS_CHANNEL, message.toJson());
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to broadcast event end", e);
        }
    }
    
    /**
     * Announce event start to players
     */
    private void announceEventStart(GlobalEvent event) {
        String message = "§6§l[EVENT] §e" + event.getName() + " §7has started!";
        Bukkit.broadcastMessage(message);
        
        // Add event-specific announcements
        switch (event.getType()) {
            case DOUBLE_COINS:
                Bukkit.broadcastMessage("§a• All coin rewards are doubled!");
                break;
            case MINING_FIESTA:
                Bukkit.broadcastMessage("§a• Mining XP and drops are enhanced!");
                break;
        }
    }
    
    /**
     * Announce event end to players
     */
    private void announceEventEnd(GlobalEvent event) {
        String message = "§6§l[EVENT] §e" + event.getName() + " §7has ended.";
        Bukkit.broadcastMessage(message);
    }
    
    /**
     * Announce mayor election
     */
    private void announceMayorElection(MayorElection election) {
        Bukkit.broadcastMessage("§6§l[MAYOR ELECTION] §eVoting has begun!");
        Bukkit.broadcastMessage("§7Candidates: §e" + String.join(", ", election.getCandidates()));
        Bukkit.broadcastMessage("§7Use §e/vote §7to cast your vote!");
    }
    
    /**
     * Announce Jacob's event
     */
    private void announceJacobsEvent(JacobsEvent event) {
        Bukkit.broadcastMessage("§6§l[JACOB'S CONTEST] §e" + event.getContestType().getDisplayName() + " contest has started!");
        Bukkit.broadcastMessage("§7Compete for the highest farming score!");
    }
    
    /**
     * Announce Cult event
     */
    private void announceCultEvent(CultEvent event) {
        Bukkit.broadcastMessage("§6§l[CULT OF THE FALLEN STAR] §e" + event.getEventType().getDisplayName() + " has begun!");
        Bukkit.broadcastMessage("§7Watch the skies for falling stars!");
    }
    
    /**
     * Store event in Redis
     */
    private void storeEventInRedis(GlobalEvent event) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(EVENTS_KEY, event.getId(), event.toJson());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to store event in Redis", e);
        }
    }
    
    /**
     * Store mayor election in Redis
     */
    private void storeMayorElectionInRedis(MayorElection election) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(MAYOR_ELECTIONS_KEY, election.getId(), election.toJson());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to store mayor election in Redis", e);
        }
    }
    
    /**
     * Store Jacob's event in Redis
     */
    private void storeJacobsEventInRedis(JacobsEvent event) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(JACOBS_EVENTS_KEY, event.getId(), event.toJson());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to store Jacob's event in Redis", e);
        }
    }
    
    /**
     * Store Cult event in Redis
     */
    private void storeCultEventInRedis(CultEvent event) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(CULT_EVENTS_KEY, event.getId(), event.toJson());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to store Cult event in Redis", e);
        }
    }
    
    /**
     * Update event status
     */
    private void updateEventStatus() {
        // Update event timers and status
        for (GlobalEvent event : activeEvents.values()) {
            long remainingTime = event.getStartTime().plusHours(event.getDuration())
                .toEpochSecond(ZoneOffset.UTC) - System.currentTimeMillis() / 1000;
            
            if (remainingTime <= 0) {
                endEvent(event);
            }
        }
    }
    
    /**
     * Clean up expired events
     */
    private void cleanupExpiredEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        
        scheduledEvents.entrySet().removeIf(entry -> 
            entry.getValue().getStartTime().isBefore(cutoff) && 
            !activeEvents.containsKey(entry.getKey())
        );
    }
    
    // Getters
    public Map<String, GlobalEvent> getScheduledEvents() {
        return new HashMap<>(scheduledEvents);
    }
    
    public Map<String, GlobalEvent> getActiveEvents() {
        return new HashMap<>(activeEvents);
    }
    
    public Map<String, MayorElection> getMayorElections() {
        return new HashMap<>(mayorElections);
    }
    
    public Map<String, JacobsEvent> getJacobsEvents() {
        return new HashMap<>(jacobsEvents);
    }
    
    public Map<String, CultEvent> getCultEvents() {
        return new HashMap<>(cultEvents);
    }
    
    public GIMIntegrationService getGimIntegration() {
        return gimIntegration;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
