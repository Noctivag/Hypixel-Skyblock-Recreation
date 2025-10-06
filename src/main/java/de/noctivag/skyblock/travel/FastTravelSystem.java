package de.noctivag.skyblock.travel;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Fast Travel System - Manages quick transportation between locations
 */
public class FastTravelSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Travel locations
    private final Map<String, TravelLocation> locations = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerUnlockedLocations = new ConcurrentHashMap<>();
    private final Map<UUID, Long> playerCooldowns = new ConcurrentHashMap<>();
    
    // Travel settings
    private static final long TRAVEL_COOLDOWN = 5000; // 5 seconds in milliseconds
    private static final int TRAVEL_COST = 100; // Base travel cost
    
    public FastTravelSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing FastTravelSystem...");
        
        try {
            // Load travel locations
            loadTravelLocations();
            
            // Load player data
            loadPlayerData();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("FastTravelSystem initialized with " + locations.size() + " locations.");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize FastTravelSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down FastTravelSystem...");
        
        // Save all player data
        saveAllPlayerData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("FastTravelSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "FastTravelSystem";
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
     * Load all travel locations
     */
    private void loadTravelLocations() {
        // Hub locations
        addLocation(new TravelLocation("hub_spawn", "Hub Spawn", "&a",
            "Main hub spawn area", TravelCategory.HUB, 0, 0, 64, 0, 0, 0, true));
        
        addLocation(new TravelLocation("hub_village", "Hub Village", "&a",
            "Village area in the hub", TravelCategory.HUB, 100, 100, 64, 100, 0, 0, false));
        
        addLocation(new TravelLocation("hub_mountain", "Hub Mountain", "&a",
            "Mountain area in the hub", TravelCategory.HUB, 200, -100, 64, -100, 0, 0, false));
        
        // Island locations
        addLocation(new TravelLocation("private_island", "Private Island", "&e",
            "Your personal island", TravelCategory.ISLAND, 0, 0, 64, 0, 0, 0, true));
        
        addLocation(new TravelLocation("coop_island", "Co-op Island", "&e",
            "Shared island with friends", TravelCategory.ISLAND, 500, 0, 64, 0, 0, 0, false));
        
        // Mining locations
        addLocation(new TravelLocation("gold_mine", "Gold Mine", "&6",
            "Basic mining area", TravelCategory.MINING, 1000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("deep_caverns", "Deep Caverns", "&8",
            "Advanced mining area", TravelCategory.MINING, 5000, 0, 32, 0, 0, 0, false));
        
        addLocation(new TravelLocation("dwarven_mines", "Dwarven Mines", "&7",
            "Expert mining area", TravelCategory.MINING, 10000, 0, 16, 0, 0, 0, false));
        
        addLocation(new TravelLocation("crystal_hollows", "Crystal Hollows", "&b",
            "Procedural mining area", TravelCategory.MINING, 25000, 0, 8, 0, 0, 0, false));
        
        // Combat locations
        addLocation(new TravelLocation("spiders_den", "Spider's Den", "&c",
            "Spider combat area", TravelCategory.COMBAT, 2000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("the_end", "The End", "&5",
            "Enderman combat area", TravelCategory.COMBAT, 5000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("crimson_isle", "Crimson Isle", "&4",
            "High-level combat area", TravelCategory.COMBAT, 15000, 0, 64, 0, 0, 0, false));
        
        // Farming locations
        addLocation(new TravelLocation("the_barn", "The Barn", "&a",
            "Farming area", TravelCategory.FARMING, 1000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("mushroom_desert", "Mushroom Desert", "&a",
            "Advanced farming area", TravelCategory.FARMING, 3000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("the_park", "The Park", "&a",
            "Foraging area", TravelCategory.FARMING, 2000, 0, 64, 0, 0, 0, false));
        
        // Special locations
        addLocation(new TravelLocation("dungeon_hub", "Dungeon Hub", "&d",
            "Dungeon entrance area", TravelCategory.SPECIAL, 10000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("rift_dimension", "Rift Dimension", "&5",
            "Special dimension", TravelCategory.SPECIAL, 20000, 0, 64, 0, 0, 0, false));
        
        addLocation(new TravelLocation("jerry_workshop", "Jerry's Workshop", "&6",
            "Seasonal area", TravelCategory.SPECIAL, 0, 0, 64, 0, 0, 0, false));
        
        plugin.getLogger().info("Loaded " + locations.size() + " travel locations.");
    }
    
    /**
     * Add a travel location
     */
    private void addLocation(TravelLocation location) {
        locations.put(location.getId(), location);
    }
    
    /**
     * Load player data
     */
    private void loadPlayerData() {
        // This would load from database in a real implementation
        // For now, we'll initialize with default unlocked locations
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        // This would save to database in a real implementation
    }
    
    /**
     * Get all travel locations
     */
    public Collection<TravelLocation> getAllLocations() {
        return locations.values();
    }
    
    /**
     * Get locations by category
     */
    public List<TravelLocation> getLocationsByCategory(TravelCategory category) {
        return locations.values().stream()
                .filter(location -> location.getCategory() == category)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get unlocked locations for a player
     */
    public List<TravelLocation> getUnlockedLocations(Player player) {
        Set<String> unlockedIds = playerUnlockedLocations.getOrDefault(player.getUniqueId(), new HashSet<>());
        return locations.values().stream()
                .filter(location -> location.isUnlockedByDefault() || unlockedIds.contains(location.getId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Check if a player has unlocked a location
     */
    public boolean hasUnlockedLocation(Player player, String locationId) {
        TravelLocation location = locations.get(locationId);
        if (location == null) return false;
        
        if (location.isUnlockedByDefault()) return true;
        
        Set<String> unlockedIds = playerUnlockedLocations.get(player.getUniqueId());
        return unlockedIds != null && unlockedIds.contains(locationId);
    }
    
    /**
     * Unlock a location for a player
     */
    public void unlockLocation(Player player, String locationId) {
        TravelLocation location = locations.get(locationId);
        if (location == null) return;
        
        if (location.isUnlockedByDefault()) return;
        
        playerUnlockedLocations.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(locationId);
        player.sendMessage("§aLocation unlocked: " + location.getDisplayName());
    }
    
    /**
     * Travel to a location
     */
    public boolean travelToLocation(Player player, String locationId) {
        TravelLocation location = locations.get(locationId);
        if (location == null) {
            player.sendMessage("§cLocation not found!");
            return false;
        }
        
        if (!hasUnlockedLocation(player, locationId)) {
            player.sendMessage("§cYou haven't unlocked this location yet!");
            return false;
        }
        
        // Check cooldown
        UUID playerId = player.getUniqueId();
        Long lastTravel = playerCooldowns.get(playerId);
        if (lastTravel != null && System.currentTimeMillis() - lastTravel < TRAVEL_COOLDOWN) {
            long remaining = (TRAVEL_COOLDOWN - (System.currentTimeMillis() - lastTravel)) / 1000;
            player.sendMessage("§cYou must wait " + remaining + " seconds before traveling again!");
            return false;
        }
        
        // Check travel cost
        int cost = location.getTravelCost();
        if (cost > 0) {
            // This would check player's money in a real implementation
            // For now, we'll just show the cost
            player.sendMessage("§eTravel cost: " + cost + " coins");
        }
        
        // Create the travel location
        Location travelLocation = createLocation(location);
        if (travelLocation == null) {
            player.sendMessage("§cUnable to create travel location!");
            return false;
        }
        
        // Teleport the player
        player.teleport(travelLocation);
        playerCooldowns.put(playerId, System.currentTimeMillis());
        
        player.sendMessage("§aTraveled to: " + location.getDisplayName());
        return true;
    }
    
    /**
     * Create a location from a TravelLocation
     */
    private Location createLocation(TravelLocation travelLocation) {
        // For now, we'll create locations in the hub world
        World world = Bukkit.getWorld("hub");
        if (world == null) {
            world = Bukkit.getWorld("world");
        }
        
        if (world == null) {
            return null;
        }
        
        // Use the coordinates from the travel location
        return new Location(world, 
            travelLocation.getX(), 
            travelLocation.getY(), 
            travelLocation.getZ(),
            travelLocation.getYaw(),
            travelLocation.getPitch());
    }
    
    /**
     * Get a travel location by ID
     */
    public TravelLocation getLocation(String id) {
        return locations.get(id);
    }
    
    /**
     * Get travel categories
     */
    public TravelCategory[] getCategories() {
        return TravelCategory.values();
    }
    
    /**
     * Check if a player is on cooldown
     */
    public boolean isOnCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        Long lastTravel = playerCooldowns.get(playerId);
        return lastTravel != null && System.currentTimeMillis() - lastTravel < TRAVEL_COOLDOWN;
    }
    
    /**
     * Get remaining cooldown time
     */
    public long getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        Long lastTravel = playerCooldowns.get(playerId);
        if (lastTravel == null) return 0;
        
        long remaining = TRAVEL_COOLDOWN - (System.currentTimeMillis() - lastTravel);
        return Math.max(0, remaining);
    }
}
