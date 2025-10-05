package de.noctivag.skyblock.features.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.System;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.features.mobs.types.CompleteMobType;
import de.noctivag.skyblock.features.mobs.types.MobCategory;
import de.noctivag.skyblock.features.mobs.types.MobRarity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class CompleteMobsSystem implements System {

    private final SkyblockPlugin SkyblockPlugin;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = false;

    public CompleteMobsSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public String getName() {
        return "CompleteMobsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        status = SystemStatus.INITIALIZING;
        SkyblockPlugin.getLogger().info("Initializing Complete Mobs System...");
        try {
            // Load all mob types
            int totalMobs = CompleteMobType.values().length;
            SkyblockPlugin.getLogger().info("Loaded " + totalMobs + " mob types.");

            // Register event listeners if any (e.g., for mob spawning, combat, drops)
            // SkyblockPlugin.getServer().getPluginManager().registerEvents(new MobListener(), SkyblockPlugin);

            status = SystemStatus.INITIALIZED;
            SkyblockPlugin.getLogger().info("Complete Mobs System initialized successfully.");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to initialize Complete Mobs System", e);
            status = SystemStatus.ERROR;
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        SkyblockPlugin.getLogger().info("Shutting down Complete Mobs System...");
        // Unregister listeners, save data, etc.
        status = SystemStatus.SHUTDOWN;
        SkyblockPlugin.getLogger().info("Complete Mobs System shut down.");
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
        SkyblockPlugin.getLogger().info("Complete Mobs System enabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> disable() {
        enabled = false;
        status = SystemStatus.DISABLED;
        SkyblockPlugin.getLogger().info("Complete Mobs System disabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void setStatus(SystemStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Get all available mob types.
     * @return A list of all CompleteMobType.
     */
    public List<CompleteMobType> getAllMobTypes() {
        return Arrays.asList(CompleteMobType.values());
    }

    /**
     * Get mobs by category.
     * @param category The category to filter by.
     * @return A list of mobs in the specified category.
     */
    public List<CompleteMobType> getMobsByCategory(MobCategory category) {
        return Arrays.stream(CompleteMobType.values())
                .filter(m -> m.getCategory() == category)
                .toList();
    }

    /**
     * Get mobs by rarity.
     * @param rarity The rarity to filter by.
     * @return A list of mobs with the specified rarity.
     */
    public List<CompleteMobType> getMobsByRarity(MobRarity rarity) {
        return Arrays.stream(CompleteMobType.values())
                .filter(m -> m.getRarity() == rarity)
                .toList();
    }

    /**
     * Get mobs by level range.
     * @param minLevel Minimum level.
     * @param maxLevel Maximum level.
     * @return A list of mobs within the level range.
     */
    public List<CompleteMobType> getMobsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(CompleteMobType.values())
                .filter(m -> m.getLevel() >= minLevel && m.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total mob count.
     * @return Total number of mob types.
     */
    public int getTotalMobCount() {
        return CompleteMobType.values().length;
    }

    /**
     * Get mob count by category.
     * @param category The category to count.
     * @return Number of mobs in the category.
     */
    public int getMobCountByCategory(MobCategory category) {
        return (int) Arrays.stream(CompleteMobType.values())
                .filter(m -> m.getCategory() == category)
                .count();
    }

    /**
     * Get mob count by rarity.
     * @param rarity The rarity to count.
     * @return Number of mobs with the rarity.
     */
    public int getMobCountByRarity(MobRarity rarity) {
        return (int) Arrays.stream(CompleteMobType.values())
                .filter(m -> m.getRarity() == rarity)
                .count();
    }

    // Additional methods for mob spawning, combat, drops, etc. can be added here.
}
