package de.noctivag.plugin.features.minions;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.core.api.System;
import de.noctivag.plugin.core.api.SystemStatus;
import de.noctivag.plugin.features.minions.types.CompleteMinionType;
import de.noctivag.plugin.features.minions.types.MinionCategory;
import de.noctivag.plugin.features.minions.types.MinionRarity;
import de.noctivag.plugin.features.minions.world.MinionWorldManager;
import de.noctivag.plugin.features.minions.world.MinionAreaMapping;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class CompleteMinionsSystem implements System {

    private final Plugin plugin;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = false;
    private MinionWorldManager worldManager;

    public CompleteMinionsSystem(Plugin plugin) {
        this.plugin = plugin;
        this.worldManager = new MinionWorldManager(plugin);
    }

    @Override
    public String getName() {
        return "CompleteMinionsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing Complete Minions System...");
        try {
            // Load all minion types
            int totalMinions = CompleteMinionType.values().length;
            plugin.getLogger().info("Loaded " + totalMinions + " minion types.");

            // Initialize world manager for multi-world support
            worldManager.initialize();

            // Register event listeners if any (e.g., for minion spawning, collection, upgrades)
            // plugin.getServer().getPluginManager().registerEvents(new MinionListener(), plugin);

            status = SystemStatus.INITIALIZED;
            plugin.getLogger().info("Complete Minions System initialized successfully.");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize Complete Minions System", e);
            status = SystemStatus.ERROR;
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down Complete Minions System...");
        
        // Shutdown world manager
        if (worldManager != null) {
            worldManager.shutdown();
        }
        
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("Complete Minions System shut down.");
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
        plugin.getLogger().info("Complete Minions System enabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> disable() {
        enabled = false;
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("Complete Minions System disabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void setStatus(SystemStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Get all available minion types.
     * @return A list of all CompleteMinionType.
     */
    public List<CompleteMinionType> getAllMinionTypes() {
        return Arrays.asList(CompleteMinionType.values());
    }

    /**
     * Get minions by category.
     * @param category The category to filter by.
     * @return A list of minions in the specified category.
     */
    public List<CompleteMinionType> getMinionsByCategory(MinionCategory category) {
        return Arrays.stream(CompleteMinionType.values())
                .filter(m -> m.getCategory() == category)
                .toList();
    }

    /**
     * Get minions by rarity.
     * @param rarity The rarity to filter by.
     * @return A list of minions with the specified rarity.
     */
    public List<CompleteMinionType> getMinionsByRarity(MinionRarity rarity) {
        return Arrays.stream(CompleteMinionType.values())
                .filter(m -> m.getRarity() == rarity)
                .toList();
    }

    /**
     * Get minions by level range.
     * @param minLevel Minimum level.
     * @param maxLevel Maximum level.
     * @return A list of minions within the level range.
     */
    public List<CompleteMinionType> getMinionsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(CompleteMinionType.values())
                .filter(m -> m.getLevel() >= minLevel && m.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total minion count.
     * @return Total number of minion types.
     */
    public int getTotalMinionCount() {
        return CompleteMinionType.values().length;
    }

    /**
     * Get minion count by category.
     * @param category The category to count.
     * @return Number of minions in the category.
     */
    public int getMinionCountByCategory(MinionCategory category) {
        return (int) Arrays.stream(CompleteMinionType.values())
                .filter(m -> m.getCategory() == category)
                .count();
    }

    /**
     * Get minion count by rarity.
     * @param rarity The rarity to count.
     * @return Number of minions with the rarity.
     */
    public int getMinionCountByRarity(MinionRarity rarity) {
        return (int) Arrays.stream(CompleteMinionType.values())
                .filter(m -> m.getRarity() == rarity)
                .count();
    }

    /**
     * Get world manager for multi-world support
     * @return The minion world manager
     */
    public MinionWorldManager getWorldManager() {
        return worldManager;
    }

    /**
     * Set world tags for specific minion types
     * @param worldName The world name
     * @param minionType The minion type
     * @param tags The tags to apply
     */
    public void setMinionWorldTags(String worldName, CompleteMinionType minionType, List<String> tags) {
        worldManager.setMinionWorldTags(worldName, minionType, tags);
    }

    /**
     * Get world tags for a minion type
     * @param worldName The world name
     * @param minionType The minion type
     * @return List of tags for the minion in that world
     */
    public List<String> getMinionWorldTags(String worldName, CompleteMinionType minionType) {
        return worldManager.getMinionWorldTags(worldName, minionType);
    }

    /**
     * Map an area to a specific minion type with tags
     * @param worldName The world name
     * @param areaName The area name
     * @param minionType The minion type
     * @param tags The tags for this area
     */
    public void mapAreaToMinion(String worldName, String areaName, CompleteMinionType minionType, List<String> tags) {
        worldManager.mapAreaToMinion(worldName, areaName, minionType, tags);
    }

    /**
     * Get minion mapping for an area
     * @param worldName The world name
     * @param areaName The area name
     * @return The minion area mapping
     */
    public MinionAreaMapping getAreaMinionMapping(String worldName, String areaName) {
        return worldManager.getAreaMinionMapping(worldName, areaName);
    }

    // Additional methods for minion spawning, collection, upgrades, etc. can be added here.
}
