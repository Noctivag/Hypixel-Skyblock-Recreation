package de.noctivag.skyblock.calendar;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Calendar System - Manages in-game events and scheduling
 */
public class CalendarSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Event storage
    private final Map<String, CalendarEvent> events = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerEventParticipation = new ConcurrentHashMap<>();
    
    // Current game time
    private LocalDateTime currentGameTime;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    public CalendarSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.currentGameTime = LocalDateTime.now();
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing CalendarSystem...");
        
        try {
            // Load events
            loadEvents();
            
            // Load player data
            loadPlayerData();
            
            // Start time update task
            startTimeUpdateTask();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("CalendarSystem initialized with " + events.size() + " events.");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize CalendarSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down CalendarSystem...");
        
        // Save all player data
        saveAllPlayerData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("CalendarSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "CalendarSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load all events
     */
    private void loadEvents() {
        // Daily Events
        addEvent(new CalendarEvent("daily_rewards", "Daily Rewards", EventType.DAILY,
            "Collect your daily rewards", "&aDaily rewards are available!",
            "&7Visit the Daily Rewards menu to collect your rewards.",
            Collections.emptyList(), 0, 0, 0, 0));
        
        addEvent(new CalendarEvent("fishing_festival", "Fishing Festival", EventType.SPECIAL,
            "Increased fishing rates and special catches", "&bFishing Festival is active!",
            "&7Enjoy increased fishing rates and special sea creatures.",
            Arrays.asList("&7+50% Fishing XP", "&7Special Sea Creatures", "&7Rare Fish Drops"),
            2, 0, 0, 0)); // 2 hours duration
        
        addEvent(new CalendarEvent("mining_festival", "Mining Festival", EventType.SPECIAL,
            "Increased mining rates and special ores", "&6Mining Festival is active!",
            "&7Enjoy increased mining rates and special ore generation.",
            Arrays.asList("&7+50% Mining XP", "&7Special Ore Generation", "&7Rare Gem Drops"),
            2, 0, 0, 0)); // 2 hours duration
        
        addEvent(new CalendarEvent("combat_festival", "Combat Festival", EventType.SPECIAL,
            "Increased combat rates and special mobs", "&cCombat Festival is active!",
            "&7Enjoy increased combat rates and special mob spawns.",
            Arrays.asList("&7+50% Combat XP", "&7Special Mob Spawns", "&7Rare Drop Rates"),
            2, 0, 0, 0)); // 2 hours duration
        
        // Weekly Events
        addEvent(new CalendarEvent("weekly_auction", "Weekly Auction", EventType.WEEKLY,
            "Special items available in auction house", "&5Weekly Auction is live!",
            "&7Special items are available in the auction house.",
            Arrays.asList("&7Rare Items", "&7Limited Time Offers", "&7Special Deals"),
            24, 0, 0, 0)); // 24 hours duration
        
        addEvent(new CalendarEvent("guild_war", "Guild War", EventType.WEEKLY,
            "Competitive guild battles", "&4Guild War is active!",
            "&7Compete with other guilds in epic battles.",
            Arrays.asList("&7Guild vs Guild", "&7Special Rewards", "&7Leaderboards"),
            6, 0, 0, 0)); // 6 hours duration
        
        // Monthly Events
        addEvent(new CalendarEvent("seasonal_event", "Seasonal Event", EventType.MONTHLY,
            "Special seasonal content and rewards", "&dSeasonal Event is here!",
            "&7Enjoy special seasonal content and exclusive rewards.",
            Arrays.asList("&7Exclusive Items", "&7Special Activities", "&7Limited Rewards"),
            72, 0, 0, 0)); // 72 hours duration
        
        // Special Events
        addEvent(new CalendarEvent("double_xp", "Double XP Weekend", EventType.SPECIAL,
            "Double XP for all activities", "&aDouble XP Weekend!",
            "&7Enjoy double XP for all your activities.",
            Arrays.asList("&7+100% XP Gain", "&7All Skills", "&7Limited Time"),
            48, 0, 0, 0)); // 48 hours duration
        
        addEvent(new CalendarEvent("treasure_hunt", "Treasure Hunt", EventType.SPECIAL,
            "Find hidden treasures across the world", "&6Treasure Hunt is active!",
            "&7Search for hidden treasures across different locations.",
            Arrays.asList("&7Hidden Treasures", "&7Special Rewards", "&7Exploration"),
            12, 0, 0, 0)); // 12 hours duration
        
        plugin.getLogger().info("Loaded " + events.size() + " calendar events.");
    }
    
    /**
     * Add an event to the system
     */
    private void addEvent(CalendarEvent event) {
        events.put(event.getId(), event);
    }
    
    /**
     * Load player data
     */
    private void loadPlayerData() {
        // This would load from database in a real implementation
        // For now, we'll initialize empty sets
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        // This would save to database in a real implementation
    }
    
    /**
     * Start the time update task
     */
    private void startTimeUpdateTask() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            currentGameTime = currentGameTime.plusMinutes(1); // 1 minute = 1 game minute
        }, 0L, 20L); // Run every second (20 ticks)
    }
    
    /**
     * Get all events
     */
    public Collection<CalendarEvent> getAllEvents() {
        return events.values();
    }
    
    /**
     * Get events by type
     */
    public List<CalendarEvent> getEventsByType(EventType type) {
        return events.values().stream()
                .filter(event -> event.getType() == type)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get active events
     */
    public List<CalendarEvent> getActiveEvents() {
        return events.values().stream()
                .filter(this::isEventActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get upcoming events
     */
    public List<CalendarEvent> getUpcomingEvents() {
        return events.values().stream()
                .filter(event -> !isEventActive(event) && isEventUpcoming(event))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Check if an event is currently active
     */
    public boolean isEventActive(CalendarEvent event) {
        // Simplified logic - in a real implementation, this would check actual timing
        return event.getType() == EventType.DAILY || 
               (event.getId().equals("fishing_festival") && currentGameTime.getHour() % 4 == 0) ||
               (event.getId().equals("mining_festival") && currentGameTime.getHour() % 6 == 0) ||
               (event.getId().equals("combat_festival") && currentGameTime.getHour() % 8 == 0);
    }
    
    /**
     * Check if an event is upcoming
     */
    public boolean isEventUpcoming(CalendarEvent event) {
        // Simplified logic - in a real implementation, this would check actual timing
        return !isEventActive(event) && event.getType() != EventType.DAILY;
    }
    
    /**
     * Get current game time
     */
    public LocalDateTime getCurrentGameTime() {
        return currentGameTime;
    }
    
    /**
     * Get formatted current date
     */
    public String getCurrentDate() {
        return currentGameTime.format(dateFormatter);
    }
    
    /**
     * Get formatted current time
     */
    public String getCurrentTime() {
        return currentGameTime.format(timeFormatter);
    }
    
    /**
     * Check if a player has participated in an event
     */
    public boolean hasParticipatedInEvent(Player player, String eventId) {
        Set<String> participatedEvents = playerEventParticipation.get(player.getUniqueId());
        return participatedEvents != null && participatedEvents.contains(eventId);
    }
    
    /**
     * Mark a player as having participated in an event
     */
    public void markEventParticipation(Player player, String eventId) {
        playerEventParticipation.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(eventId);
    }
    
    /**
     * Get event participation for a player
     */
    public Set<String> getEventParticipation(Player player) {
        return playerEventParticipation.getOrDefault(player.getUniqueId(), new HashSet<>());
    }
    
    /**
     * Get an event by ID
     */
    public CalendarEvent getEvent(String id) {
        return events.get(id);
    }
    
    /**
     * Check if there are upcoming events
     */
    public boolean isUpcomingEvents() {
        LocalDate today = currentGameTime.toLocalDate();
        LocalDate nextWeek = today.plusWeeks(1);
        
        return events.values().stream()
                .anyMatch(event -> event.getStartDate().isAfter(today) && 
                                 event.getStartDate().isBefore(nextWeek));
    }
}
