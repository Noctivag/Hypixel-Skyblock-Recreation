package de.noctivag.skyblock.features.locations;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.core.api.System;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.features.locations.types.CompleteLocationType;
import de.noctivag.skyblock.features.locations.types.LocationCategory;
import de.noctivag.skyblock.features.locations.types.LocationRarity;
import de.noctivag.skyblock.features.locations.world.LocationWorldManager;
import de.noctivag.skyblock.features.locations.world.LocationAreaMapping;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class CompleteLocationsSystem implements System {

    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = false;
    private LocationWorldManager worldManager;

    public CompleteLocationsSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.worldManager = new LocationWorldManager(plugin);
    }

    @Override
    public String getName() {
        return "CompleteLocationsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing Complete Locations System...");
        try {
            // Load all location types
            int totalLocations = CompleteLocationType.values().length;
            plugin.getLogger().info("Loaded " + totalLocations + " location types.");

            // Initialize world manager for multi-world support
            worldManager.initialize();

            // Register event listeners if any (e.g., for location discovery, teleportation, etc.)
            // plugin.getServer().getPluginManager().registerEvents(new LocationListener(), plugin);

            status = SystemStatus.INITIALIZED;
            plugin.getLogger().info("Complete Locations System initialized successfully.");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize Complete Locations System", e);
            status = SystemStatus.ERROR;
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down Complete Locations System...");
        
        // Shutdown world manager
        if (worldManager != null) {
            worldManager.shutdown();
        }
        
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("Complete Locations System shut down.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isInitialized() {
        return status == SystemStatus.INITIALIZED || status == SystemStatus.ENABLED || status == SystemStatus.DISABLED;
    }

    @Override
    public CompletableFuture<Void> enable() {
        enabled = true;
        status = SystemStatus.ENABLED;
        plugin.getLogger().info("Complete Locations System enabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> disable() {
        enabled = false;
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("Complete Locations System disabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void setStatus(SystemStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Get all available location types.
     * @return A list of all CompleteLocationType.
     */
    public List<CompleteLocationType> getAllLocationTypes() {
        return Arrays.asList(CompleteLocationType.values());
    }

    /**
     * Get locations by category.
     * @param category The category to filter by.
     * @return A list of locations in the specified category.
     */
    public List<CompleteLocationType> getLocationsByCategory(LocationCategory category) {
        return Arrays.stream(CompleteLocationType.values())
                .filter(l -> l.getCategory() == category)
                .toList();
    }

    /**
     * Get locations by rarity.
     * @param rarity The rarity to filter by.
     * @return A list of locations with the specified rarity.
     */
    public List<CompleteLocationType> getLocationsByRarity(LocationRarity rarity) {
        return Arrays.stream(CompleteLocationType.values())
                .filter(l -> l.getRarity() == rarity)
                .toList();
    }

    /**
     * Get locations by level range.
     * @param minLevel Minimum level.
     * @param maxLevel Maximum level.
     * @return A list of locations within the level range.
     */
    public List<CompleteLocationType> getLocationsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(CompleteLocationType.values())
                .filter(l -> l.getLevel() >= minLevel && l.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total location count.
     * @return Total number of location types.
     */
    public int getTotalLocationCount() {
        return CompleteLocationType.values().length;
    }

    /**
     * Get location count by category.
     * @param category The category to count.
     * @return Number of locations in the category.
     */
    public int getLocationCountByCategory(LocationCategory category) {
        return (int) Arrays.stream(CompleteLocationType.values())
                .filter(l -> l.getCategory() == category)
                .count();
    }

    /**
     * Get location count by rarity.
     * @param rarity The rarity to count.
     * @return Number of locations with the rarity.
     */
    public int getLocationCountByRarity(LocationRarity rarity) {
        return (int) Arrays.stream(CompleteLocationType.values())
                .filter(l -> l.getRarity() == rarity)
                .count();
    }

    /**
     * Get world manager for multi-world support
     * @return The location world manager
     */
    public LocationWorldManager getWorldManager() {
        return worldManager;
    }

    /**
     * Set location tags for specific location types
     * @param worldName The world name
     * @param locationType The location type
     * @param tags The tags to apply
     */
    public void setLocationWorldTags(String worldName, CompleteLocationType locationType, List<String> tags) {
        worldManager.setLocationWorldTags(worldName, locationType, tags);
    }

    /**
     * Get world tags for a location type
     * @param worldName The world name
     * @param locationType The location type
     * @return List of tags for the location in that world
     */
    public List<String> getLocationWorldTags(String worldName, CompleteLocationType locationType) {
        return worldManager.getLocationWorldTags(worldName, locationType);
    }

    /**
     * Map an area to a specific location type with tags
     * @param worldName The world name
     * @param areaName The area name
     * @param locationType The location type
     * @param tags The tags for this area
     */
    public void mapAreaToLocation(String worldName, String areaName, CompleteLocationType locationType, List<String> tags) {
        worldManager.mapAreaToLocation(worldName, areaName, locationType, tags);
    }

    /**
     * Get location mapping for an area
     * @param worldName The world name
     * @param areaName The area name
     * @return The location area mapping
     */
    public LocationAreaMapping getAreaLocationMapping(String worldName, String areaName) {
        return worldManager.getAreaLocationMapping(worldName, areaName);
    }

    // Additional methods for location discovery, teleportation, etc. can be added here.
}
