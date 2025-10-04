package de.noctivag.skyblock.events;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedEventSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerEventData> playerEventData = new ConcurrentHashMap<>();
    private final Map<EventType, List<Event>> events = new HashMap<>();
    private final Map<String, Event> activeEvents = new HashMap<>();
    
    public AdvancedEventSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeEvents();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeEvents() {
        // Seasonal Events
        List<Event> seasonalEvents = new ArrayList<>();
        seasonalEvents.add(new Event(
            "Halloween Event", "§6Halloween Event", Material.JACK_O_LANTERN,
            "§7A spooky Halloween event with special rewards.",
            EventType.SEASONAL, EventRarity.RARE, 7, Arrays.asList("§7- Spooky decorations", "§7- Halloween mobs"),
            Arrays.asList("§7- Halloween costumes", "§7- Spooky rewards", "§7- Candy")
        ));
        seasonalEvents.add(new Event(
            "Christmas Event", "§fChristmas Event", Material.SNOW_BLOCK,
            "§7A festive Christmas event with holiday rewards.",
            EventType.SEASONAL, EventRarity.RARE, 7, Arrays.asList("§7- Christmas decorations", "§7- Festive mobs"),
            Arrays.asList("§7- Christmas presents", "§7- Holiday rewards", "§7- Snow")
        ));
        events.put(EventType.SEASONAL, seasonalEvents);
        
        // Competition Events
        List<Event> competitionEvents = new ArrayList<>();
        competitionEvents.add(new Event(
            "Mining Competition", "§7Mining Competition", Material.DIAMOND_PICKAXE,
            "§7A competition to see who can mine the most.",
            EventType.COMPETITION, EventRarity.UNCOMMON, 3, Arrays.asList("§7- Mining challenges", "§7- Leaderboards"),
            Arrays.asList("§7- Mining rewards", "§7- Competition prizes", "§7- Mining tools")
        ));
        events.put(EventType.COMPETITION, competitionEvents);
        
        // Special Events
        List<Event> specialEvents = new ArrayList<>();
        specialEvents.add(new Event(
            "Double XP Event", "§aDouble XP Event", Material.EXPERIENCE_BOTTLE,
            "§7An event that doubles all XP gains.",
            EventType.SPECIAL, EventRarity.UNCOMMON, 1, Arrays.asList("§7- Double XP", "§7- XP bonuses"),
            Arrays.asList("§7- XP rewards", "§7- Level bonuses", "§7- Experience")
        ));
        events.put(EventType.SPECIAL, specialEvents);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Event") || displayName.contains("Events")) {
            openEventGUI(player);
        }
    }
    
    public void openEventGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lEvents");
        
        // Add event categories
        addGUIItem(gui, 10, Material.JACK_O_LANTERN, "§6§lSeasonal Events", "§7Seasonal events for special occasions.");
        addGUIItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lCompetition Events", "§7Competition events with leaderboards.");
        addGUIItem(gui, 12, Material.EXPERIENCE_BOTTLE, "§a§lSpecial Events", "§7Special events with unique bonuses.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the event menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aEvent GUI geöffnet!");
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerEventData getPlayerEventData(UUID playerId) {
        return playerEventData.computeIfAbsent(playerId, k -> new PlayerEventData(playerId));
    }
    
    public List<Event> getEvents(EventType category) {
        return events.getOrDefault(category, new ArrayList<>());
    }
    
    public enum EventType {
        SEASONAL("§6Seasonal", "§7Seasonal events for special occasions"),
        COMPETITION("§7Competition", "§7Competition events with leaderboards"),
        SPECIAL("§aSpecial", "§7Special events with unique bonuses");
        
        private final String displayName;
        private final String description;
        
        EventType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum EventRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        EventRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class Event {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final EventType type;
        private final EventRarity rarity;
        private final int duration; // in minutes
        private final List<String> features;
        private final List<String> rewards;
        
        public Event(String name, String displayName, Material material, String description,
                    EventType type, EventRarity rarity, int duration, List<String> features,
                    List<String> rewards) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.duration = duration;
            this.features = features;
            this.rewards = rewards;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public EventType getType() { return type; }
        public EventRarity getRarity() { return rarity; }
        public int getDuration() { return duration; }
        public List<String> getFeatures() { return features; }
        public List<String> getRewards() { return rewards; }
    }
    
    public static class PlayerEventData {
        private final UUID playerId;
        private final Map<String, Integer> eventParticipation = new HashMap<>();
        private final Map<String, Boolean> eventRewards = new HashMap<>();
        private long lastUpdate;
        
        public PlayerEventData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void participateInEvent(String eventName) {
            eventParticipation.put(eventName, eventParticipation.getOrDefault(eventName, 0) + 1);
        }
        
        public void claimEventReward(String eventName) {
            eventRewards.put(eventName, true);
        }
        
        public int getEventParticipation(String eventName) {
            return eventParticipation.getOrDefault(eventName, 0);
        }
        
        public boolean hasEventReward(String eventName) {
            return eventRewards.getOrDefault(eventName, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
