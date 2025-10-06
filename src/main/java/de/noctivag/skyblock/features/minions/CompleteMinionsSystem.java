package de.noctivag.skyblock.features.minions;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.System;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.features.minions.types.CompleteMinionType;
import de.noctivag.skyblock.features.minions.types.MinionCategory;
import de.noctivag.skyblock.features.minions.types.MinionRarity;
import de.noctivag.skyblock.features.minions.world.MinionWorldManager;
import de.noctivag.skyblock.features.minions.world.MinionAreaMapping;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class CompleteMinionsSystem implements System {

    private final SkyblockPlugin SkyblockPlugin;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = false;
    private MinionWorldManager worldManager;

    public CompleteMinionsSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.worldManager = new MinionWorldManager(SkyblockPlugin);
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
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        SkyblockPlugin.getLogger().info("Initializing Complete Minions System...");
        try {
            // Load all minion types
            int totalMinions = CompleteMinionType.values().length;
            SkyblockPlugin.getLogger().info("Loaded " + totalMinions + " minion types.");

            // Initialize world manager for multi-world support
            worldManager.initialize();

            // Register event listeners if any (e.g., for minion spawning, collection, upgrades)
            // SkyblockPlugin.getServer().getPluginManager().registerEvents(new MinionListener(), SkyblockPlugin);

            status = SystemStatus.RUNNING;
            SkyblockPlugin.getLogger().info("Complete Minions System initialized successfully.");
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to initialize Complete Minions System", e);
            status = SystemStatus.ERROR;
        }
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        SkyblockPlugin.getLogger().info("Shutting down Complete Minions System...");
        
        // Shutdown world manager
        if (worldManager != null) {
            worldManager.shutdown();
        }
        
        status = SystemStatus.DISABLED;
        SkyblockPlugin.getLogger().info("Complete Minions System shut down.");
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
