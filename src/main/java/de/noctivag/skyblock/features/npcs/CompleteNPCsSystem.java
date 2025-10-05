package de.noctivag.skyblock.features.npcs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.System;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.features.npcs.types.CompleteNPCType;
import de.noctivag.skyblock.features.npcs.types.NPCCategory;
import de.noctivag.skyblock.features.npcs.types.NPCRarity;
import de.noctivag.skyblock.features.npcs.world.NPCWorldManager;
import de.noctivag.skyblock.features.npcs.world.NPCAreaMapping;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class CompleteNPCsSystem implements System {

    private final SkyblockPlugin SkyblockPlugin;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = false;
    private NPCWorldManager worldManager;

    public CompleteNPCsSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.worldManager = new NPCWorldManager(SkyblockPlugin);
    }

    @Override
    public String getName() {
        return "CompleteNPCsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        status = SystemStatus.INITIALIZING;
        SkyblockPlugin.getLogger().info("Initializing Complete NPCs System...");
        try {
            // Load all NPC types
            int totalNPCs = CompleteNPCType.values().length;
            SkyblockPlugin.getLogger().info("Loaded " + totalNPCs + " NPC types.");

            // Initialize world manager for multi-world support
            worldManager.initialize();

            // Register event listeners if any (e.g., for NPC interactions, quests, etc.)
            // SkyblockPlugin.getServer().getPluginManager().registerEvents(new NPCListener(), SkyblockPlugin);

            status = SystemStatus.INITIALIZED;
            SkyblockPlugin.getLogger().info("Complete NPCs System initialized successfully.");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to initialize Complete NPCs System", e);
            status = SystemStatus.ERROR;
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        SkyblockPlugin.getLogger().info("Shutting down Complete NPCs System...");
        
        // Shutdown world manager
        if (worldManager != null) {
            worldManager.shutdown();
        }
        
        status = SystemStatus.SHUTDOWN;
        SkyblockPlugin.getLogger().info("Complete NPCs System shut down.");
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
        SkyblockPlugin.getLogger().info("Complete NPCs System enabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> disable() {
        enabled = false;
        status = SystemStatus.DISABLED;
        SkyblockPlugin.getLogger().info("Complete NPCs System disabled.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void setStatus(SystemStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Get all available NPC types.
     * @return A list of all CompleteNPCType.
     */
    public List<CompleteNPCType> getAllNPCTypes() {
        return Arrays.asList(CompleteNPCType.values());
    }

    /**
     * Get NPCs by category.
     * @param category The category to filter by.
     * @return A list of NPCs in the specified category.
     */
    public List<CompleteNPCType> getNPCsByCategory(NPCCategory category) {
        return Arrays.stream(CompleteNPCType.values())
                .filter(n -> n.getCategory() == category)
                .toList();
    }

    /**
     * Get NPCs by rarity.
     * @param rarity The rarity to filter by.
     * @return A list of NPCs with the specified rarity.
     */
    public List<CompleteNPCType> getNPCsByRarity(NPCRarity rarity) {
        return Arrays.stream(CompleteNPCType.values())
                .filter(n -> n.getRarity() == rarity)
                .toList();
    }

    /**
     * Get NPCs by level range.
     * @param minLevel Minimum level.
     * @param maxLevel Maximum level.
     * @return A list of NPCs within the level range.
     */
    public List<CompleteNPCType> getNPCsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(CompleteNPCType.values())
                .filter(n -> n.getLevel() >= minLevel && n.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total NPC count.
     * @return Total number of NPC types.
     */
    public int getTotalNPCCount() {
        return CompleteNPCType.values().length;
    }

    /**
     * Get NPC count by category.
     * @param category The category to count.
     * @return Number of NPCs in the category.
     */
    public int getNPCCountByCategory(NPCCategory category) {
        return (int) Arrays.stream(CompleteNPCType.values())
                .filter(n -> n.getCategory() == category)
                .count();
    }

    /**
     * Get NPC count by rarity.
     * @param rarity The rarity to count.
     * @return Number of NPCs with the rarity.
     */
    public int getNPCCountByRarity(NPCRarity rarity) {
        return (int) Arrays.stream(CompleteNPCType.values())
                .filter(n -> n.getRarity() == rarity)
                .count();
    }

    /**
     * Get world manager for multi-world support
     * @return The NPC world manager
     */
    public NPCWorldManager getWorldManager() {
        return worldManager;
    }

    /**
     * Set NPC tags for specific NPC types
     * @param worldName The world name
     * @param npcType The NPC type
     * @param tags The tags to apply
     */
    public void setNPCWorldTags(String worldName, CompleteNPCType npcType, List<String> tags) {
        worldManager.setNPCWorldTags(worldName, npcType, tags);
    }

    /**
     * Get world tags for an NPC type
     * @param worldName The world name
     * @param npcType The NPC type
     * @return List of tags for the NPC in that world
     */
    public List<String> getNPCWorldTags(String worldName, CompleteNPCType npcType) {
        return worldManager.getNPCWorldTags(worldName, npcType);
    }

    /**
     * Map an area to a specific NPC type with tags
     * @param worldName The world name
     * @param areaName The area name
     * @param npcType The NPC type
     * @param tags The tags for this area
     */
    public void mapAreaToNPC(String worldName, String areaName, CompleteNPCType npcType, List<String> tags) {
        worldManager.mapAreaToNPC(worldName, areaName, npcType, tags);
    }

    /**
     * Get NPC mapping for an area
     * @param worldName The world name
     * @param areaName The area name
     * @return The NPC area mapping
     */
    public NPCAreaMapping getAreaNPCMapping(String worldName, String areaName) {
        return worldManager.getAreaNPCMapping(worldName, areaName);
    }

    // Additional methods for NPC spawning, interactions, quests, etc. can be added here.
}
