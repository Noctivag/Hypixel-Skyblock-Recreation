package de.noctivag.skyblock.events;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Ultimate event system managing skyblock events
 */
public class UltimateEventSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, SkyblockEvent> events = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> eventTasks = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerEventParticipation = new ConcurrentHashMap<>();
    private final EventScheduler eventScheduler;
    
    public UltimateEventSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.eventScheduler = new EventScheduler(this);
    }
    
    public SkyblockPlugin getPlugin() {
        return plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing UltimateEventSystem...");
        
        // Load events from configuration
        loadEventsFromConfig();
        
        // Register event listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Start event scheduler
        eventScheduler.start();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("UltimateEventSystem initialized with " + events.size() + " events.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down UltimateEventSystem...");
        
        // Stop all event tasks
        for (BukkitTask task : eventTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        eventTasks.clear();
        
        // Stop event scheduler
        eventScheduler.stop();
        
        // End all active events
        for (SkyblockEvent event : events.values()) {
            if (event.isActive()) {
                endEvent(event.getId());
            }
        }
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("UltimateEventSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "UltimateEventSystem";
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
     * Load events from configuration file
     */
    private void loadEventsFromConfig() {
        File configFile = new File(plugin.getDataFolder(), "events.yml");
        if (!configFile.exists()) {
            createDefaultEventsConfig(configFile);
        }
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection eventsSection = config.getConfigurationSection("events");
        
        if (eventsSection == null) {
            plugin.getLogger().warning("No events section found in events.yml");
            return;
        }
        
        for (String eventId : eventsSection.getKeys(false)) {
            try {
                SkyblockEvent event = loadEventFromConfig(eventsSection.getConfigurationSection(eventId));
                if (event != null) {
                    events.put(eventId, event);
                    plugin.getLogger().info("Loaded event: " + event.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to load event " + eventId + ": " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Create default events configuration
     */
    private void createDefaultEventsConfig(File configFile) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            
            // Example mining event
            config.set("events.double_mining_xp.id", "double_mining_xp");
            config.set("events.double_mining_xp.name", "Double Mining XP");
            config.set("events.double_mining_xp.description", "Gain double mining experience!");
            config.set("events.double_mining_xp.type", "BOOST");
            config.set("events.double_mining_xp.duration", 3600); // 1 hour
            config.set("events.double_mining_xp.auto_start", true);
            config.set("events.double_mining_xp.requirements.min_level", 1);
            config.set("events.double_mining_xp.rewards.xp_multiplier", 2.0);
            config.set("events.double_mining_xp.rewards.coins", 1000);
            
            // Example fishing event
            config.set("events.fishing_frenzy.id", "fishing_frenzy");
            config.set("events.fishing_frenzy.name", "Fishing Frenzy");
            config.set("events.fishing_frenzy.description", "Increased fishing rates and rare catches!");
            config.set("events.fishing_frenzy.type", "FISHING");
            config.set("events.fishing_frenzy.duration", 7200); // 2 hours
            config.set("events.fishing_frenzy.auto_start", false);
            config.set("events.fishing_frenzy.requirements.min_level", 5);
            config.set("events.fishing_frenzy.rewards.catch_rate_multiplier", 1.5);
            config.set("events.fishing_frenzy.rewards.rare_catch_chance", 0.1);
            
            config.save(configFile);
            plugin.getLogger().info("Created default events configuration: " + configFile.getName());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create default events configuration", e);
        }
    }
    
    /**
     * Load a single event from configuration
     */
    private SkyblockEvent loadEventFromConfig(ConfigurationSection eventSection) {
        if (eventSection == null) return null;
        
        String id = eventSection.getString("id");
        String name = eventSection.getString("name");
        String description = eventSection.getString("description");
        String typeStr = eventSection.getString("type", "CUSTOM");
        long duration = eventSection.getLong("duration", 3600); // 1 hour default
        boolean autoStart = eventSection.getBoolean("auto_start", false);
        
        // Load requirements
        Map<String, Object> requirements = new HashMap<>();
        ConfigurationSection requirementsSection = eventSection.getConfigurationSection("requirements");
        if (requirementsSection != null) {
            for (String requirementKey : requirementsSection.getKeys(false)) {
                requirements.put(requirementKey, requirementsSection.get(requirementKey));
            }
        }
        
        // Load rewards
        Map<String, Object> rewards = new HashMap<>();
        ConfigurationSection rewardsSection = eventSection.getConfigurationSection("rewards");
        if (rewardsSection != null) {
            for (String rewardKey : rewardsSection.getKeys(false)) {
                rewards.put(rewardKey, rewardsSection.get(rewardKey));
            }
        }
        
        // Load schedule
        List<String> schedule = eventSection.getStringList("schedule");
        
        SkyblockEvent.EventType type;
        try {
            type = SkyblockEvent.EventType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = SkyblockEvent.EventType.CUSTOM;
        }
        
        return new SkyblockEvent(id, name, description, type, duration, autoStart, requirements, rewards, schedule);
    }
    
    /**
     * Start an event
     */
    public boolean startEvent(String eventId) {
        SkyblockEvent event = events.get(eventId);
        if (event == null || event.isActive()) {
            return false;
        }
        
        event.setActive(true);
        event.setStartTime(System.currentTimeMillis());
        
        // Start event task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (event.isExpired()) {
                    endEvent(eventId);
                } else {
                    updateEvent(event);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
        
        eventTasks.put(eventId, task);
        
        // Announce event start
        announceEventStart(event);
        
        plugin.getLogger().info("Started event: " + event.getName());
        return true;
    }
    
    /**
     * End an event
     */
    public boolean endEvent(String eventId) {
        SkyblockEvent event = events.get(eventId);
        if (event == null || !event.isActive()) {
        return false;
        }
        
        event.setActive(false);
        event.setEndTime(System.currentTimeMillis());
        
        // Stop event task
        BukkitTask task = eventTasks.remove(eventId);
        if (task != null) {
            task.cancel();
        }
        
        // Distribute rewards
        distributeEventRewards(event);
        
        // Announce event end
        announceEventEnd(event);
        
        plugin.getLogger().info("Ended event: " + event.getName());
        return true;
    }
    
    /**
     * Update event
     */
    private void updateEvent(SkyblockEvent event) {
        // Update event progress, check conditions, etc.
        long timeRemaining = event.getTimeRemaining();
        
        // Announce time remaining every 5 minutes
        if (timeRemaining > 0 && timeRemaining % 300 == 0) {
            announceEventTimeRemaining(event, timeRemaining);
        }
    }
    
    /**
     * Distribute event rewards
     */
    private void distributeEventRewards(SkyblockEvent event) {
        for (UUID playerUuid : playerEventParticipation.keySet()) {
            if (playerEventParticipation.get(playerUuid).contains(event.getId())) {
                Player player = Bukkit.getPlayer(playerUuid);
                if (player != null) {
                    giveEventRewards(player, event);
                }
            }
        }
    }
    
    /**
     * Give event rewards to player
     */
    private void giveEventRewards(Player player, SkyblockEvent event) {
        Map<String, Object> rewards = event.getRewards();
        
        // Give coins
        if (rewards.containsKey("coins")) {
            double coins = ((Number) rewards.get("coins")).doubleValue();
            // Give coins to player (implementation depends on economy system)
            player.sendMessage("§aYou received " + coins + " coins from the " + event.getName() + " event!");
        }
        
        // Give XP multiplier
        if (rewards.containsKey("xp_multiplier")) {
            double multiplier = ((Number) rewards.get("xp_multiplier")).doubleValue();
            // Apply XP multiplier (implementation depends on skill system)
            player.sendMessage("§aYou received " + multiplier + "x XP multiplier from the " + event.getName() + " event!");
        }
        
        // Add more reward types as needed
    }
    
    /**
     * Announce event start
     */
    private void announceEventStart(SkyblockEvent event) {
        String message = "§6§lEVENT STARTED! §e" + event.getName() + " §7- " + event.getDescription();
        Bukkit.broadcastMessage(message);
    }
    
    /**
     * Announce event end
     */
    private void announceEventEnd(SkyblockEvent event) {
        String message = "§c§lEVENT ENDED! §e" + event.getName() + " §7has finished.";
        Bukkit.broadcastMessage(message);
    }
    
    /**
     * Announce event time remaining
     */
    private void announceEventTimeRemaining(SkyblockEvent event, long timeRemaining) {
        long minutes = timeRemaining / 60;
        String message = "§6§lEVENT! §e" + event.getName() + " §7ends in " + minutes + " minutes!";
        Bukkit.broadcastMessage(message);
    }
    
    /**
     * Add player to event participation
     */
    public void addPlayerParticipation(UUID playerUuid, String eventId) {
        playerEventParticipation.computeIfAbsent(playerUuid, k -> ConcurrentHashMap.newKeySet()).add(eventId);
    }
    
    /**
     * Remove player from event participation
     */
    public void removePlayerParticipation(UUID playerUuid, String eventId) {
        Set<String> participations = playerEventParticipation.get(playerUuid);
        if (participations != null) {
            participations.remove(eventId);
            if (participations.isEmpty()) {
                playerEventParticipation.remove(playerUuid);
            }
        }
    }
    
    /**
     * Check if player is participating in event
     */
    public boolean isPlayerParticipating(UUID playerUuid, String eventId) {
        Set<String> participations = playerEventParticipation.get(playerUuid);
        return participations != null && participations.contains(eventId);
    }
    
    /**
     * Get active events
     */
    public List<SkyblockEvent> getActiveEvents() {
        List<SkyblockEvent> activeEvents = new ArrayList<>();
        for (SkyblockEvent event : events.values()) {
            if (event.isActive()) {
                activeEvents.add(event);
            }
        }
        return activeEvents;
    }
    
    /**
     * Get event by ID
     */
    public SkyblockEvent getEvent(String eventId) {
        return events.get(eventId);
    }
    
    /**
     * Get all events
     */
    public Map<String, SkyblockEvent> getEvents() {
        return new HashMap<>(events);
    }
    
    /**
     * Get event scheduler
     */
    public EventScheduler getEventScheduler() {
        return eventScheduler;
    }
    
    // Event handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Check if player should be added to any active events
        for (SkyblockEvent skyblockEvent : getActiveEvents()) {
            if (meetsEventRequirements(event.getPlayer(), skyblockEvent)) {
                addPlayerParticipation(event.getPlayer().getUniqueId(), skyblockEvent.getId());
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clean up player participation data
        playerEventParticipation.remove(event.getPlayer().getUniqueId());
    }
    
    /**
     * Check if player meets event requirements
     */
    private boolean meetsEventRequirements(Player player, SkyblockEvent event) {
        Map<String, Object> requirements = event.getRequirements();
        
        // Check minimum level requirement
        if (requirements.containsKey("min_level")) {
            int minLevel = ((Number) requirements.get("min_level")).intValue();
            // Check player level (implementation depends on level system)
            // For now, assume all players meet the requirement
        }
        
        // Add more requirement checks as needed
        
        return true; // Placeholder
    }
}