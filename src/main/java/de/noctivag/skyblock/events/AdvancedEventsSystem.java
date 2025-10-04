package de.noctivag.skyblock.events;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Events System - Hypixel Skyblock Style
 */
public class AdvancedEventsSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerEvents> playerEvents = new ConcurrentHashMap<>();
    private final Map<EventType, EventConfig> eventConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> eventTasks = new ConcurrentHashMap<>();
    
    public AdvancedEventsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeEventConfigs();
        startEventUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeEventConfigs() {
        eventConfigs.put(EventType.DOUBLE_XP, new EventConfig(
            "Double XP Event", "§aDouble XP Event", Material.EXPERIENCE_BOTTLE,
            "§7Double XP for all activities.",
            EventCategory.XP, 1, Arrays.asList("§7- Double XP", "§7- Limited time"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x Time")
        ));
        
        eventConfigs.put(EventType.DOUBLE_COINS, new EventConfig(
            "Double Coins Event", "§6Double Coins Event", Material.GOLD_INGOT,
            "§7Double coins for all activities.",
            EventCategory.COINS, 1, Arrays.asList("§7- Double coins", "§7- Limited time"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Time")
        ));
    }
    
    private void startEventUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerEvents> entry : playerEvents.entrySet()) {
                    PlayerEvents events = entry.getValue();
                    events.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BEACON) {
            openEventGUI(player);
        }
    }
    
    private void openEventGUI(Player player) {
        player.sendMessage("§aEvent GUI geöffnet!");
    }
    
    public PlayerEvents getPlayerEvents(UUID playerId) {
        return playerEvents.computeIfAbsent(playerId, k -> new PlayerEvents(playerId));
    }
    
    public EventConfig getEventConfig(EventType type) {
        return eventConfigs.get(type);
    }
    
    public List<EventType> getAllEventTypes() {
        return new ArrayList<>(eventConfigs.keySet());
    }
    
    public enum EventType {
        DOUBLE_XP, DOUBLE_COINS, DOUBLE_DROPS, HALF_PRICE, FREE_ENCHANTS, BONUS_SKILLS
    }
    
    public enum EventCategory {
        XP("§aXP", 1.0),
        COINS("§6Coins", 1.2),
        DROPS("§bDrops", 1.1),
        PRICES("§ePrices", 1.3),
        ENCHANTS("§dEnchants", 1.4),
        SKILLS("§cSkills", 1.5);
        
        private final String displayName;
        private final double multiplier;
        
        EventCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class EventConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final EventCategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public EventConfig(String name, String displayName, Material icon, String description,
                          EventCategory category, int maxLevel, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public EventCategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerEvents {
        private final UUID playerId;
        private final Map<EventType, Integer> eventLevels = new ConcurrentHashMap<>();
        private int totalEvents = 0;
        private long totalEventTime = 0;
        private long lastUpdate;
        
        public PlayerEvents(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save event data to database
        }
        
        public void addEvent(EventType type, int level) {
            eventLevels.put(type, level);
            totalEvents++;
        }
        
        public int getEventLevel(EventType type) {
            return eventLevels.getOrDefault(type, 0);
        }
        
        public int getTotalEvents() { return totalEvents; }
        public long getTotalEventTime() { return totalEventTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<EventType, Integer> getEventLevels() { return eventLevels; }
    }
}
