package de.noctivag.skyblock.collections;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Collections System - Manages player collection progress and milestone rewards
 */
public class CollectionsSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerCollections> playerCollections;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public CollectionsSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerCollections = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing CollectionsSystem...");

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Load all online player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerCollections(player.getUniqueId());
        }

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("CollectionsSystem initialized successfully!");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down CollectionsSystem...");

        // Save all player data
        saveAllPlayerCollections();

        playerCollections.clear();
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("CollectionsSystem shutdown complete!");
    }

    @Override
    public String getName() {
        return "CollectionsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Collections system is always enabled when running
    }

    /**
     * Load player collections from database
     */
    public void loadPlayerCollections(UUID playerId) {
        try {
            // Load from database (placeholder - implement actual database loading)
            PlayerCollections collections = new PlayerCollections(playerId);
            playerCollections.put(playerId, collections);
            plugin.getLogger().info("Loaded collections for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load collections for player " + playerId + ": " + e.getMessage());
            // Create new collections if loading fails
            playerCollections.put(playerId, new PlayerCollections(playerId));
        }
    }

    /**
     * Save player collections to database
     */
    public void savePlayerCollections(UUID playerId) {
        try {
            PlayerCollections collections = playerCollections.get(playerId);
            if (collections != null) {
                // Save to database (placeholder - implement actual database saving)
                plugin.getLogger().info("Saved collections for player: " + playerId);
            }
                } catch (Exception e) {
            plugin.getLogger().warning("Failed to save collections for player " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player collections
     */
    public void saveAllPlayerCollections() {
        for (UUID playerId : playerCollections.keySet()) {
            savePlayerCollections(playerId);
        }
    }

    /**
     * Get player collections
     */
    public PlayerCollections getPlayerCollections(UUID playerId) {
        return playerCollections.get(playerId);
    }

    /**
     * Get player collections (by Player object)
     */
    public PlayerCollections getPlayerCollections(Player player) {
        return getPlayerCollections(player.getUniqueId());
    }

    /**
     * Add collection amount to a player
     */
    public boolean addCollection(Player player, CollectionType collection, long amount) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) {
            loadPlayerCollections(player.getUniqueId());
            collections = getPlayerCollections(player);
        }

        boolean milestoneUnlocked = collections.addCollection(collection, amount);
        
        if (milestoneUnlocked) {
            int newMilestoneLevel = collections.getCurrentMilestoneLevel(collection);
            player.sendMessage("§a§lCOLLECTION MILESTONE! §r" + collection.getColor() + collection.getIcon() + " " + collection.getDisplayName() + " §aLevel " + newMilestoneLevel);
            
            // Get milestone information
            CollectionMilestone[] milestones = CollectionMilestone.createDefaultMilestones(collection);
            if (newMilestoneLevel <= milestones.length) {
                CollectionMilestone milestone = milestones[newMilestoneLevel - 1];
                player.sendMessage("§7" + milestone.getFormattedDescription());
                player.sendMessage("§aReward: " + milestone.getFormattedRewardDescription());
            }
        }

        return milestoneUnlocked;
    }

    /**
     * Add collection from item stack
     */
    public boolean addCollectionFromItem(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        
        CollectionType collection = CollectionType.getByMaterial(item.getType());
        if (collection == null) return false;
        
        long amount = item.getAmount();
        return addCollection(player, collection, amount);
    }

    /**
     * Get collection amount for a player
     */
    public long getCollectionAmount(Player player, CollectionType collection) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) return 0;
        
        return collections.getCollection(collection);
    }

    /**
     * Get milestone level for a player's collection
     */
    public int getMilestoneLevel(Player player, CollectionType collection) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) return 0;
        
        return collections.getCurrentMilestoneLevel(collection);
    }

    /**
     * Check if player has unlocked a specific milestone
     */
    public boolean hasUnlockedMilestone(Player player, CollectionType collection, int milestoneLevel) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) return false;
        
        return collections.isMilestoneUnlocked(collection, milestoneLevel);
    }

    /**
     * Get collections by category for a player
     */
    public Map<CollectionType, Long> getCollectionsByCategory(Player player, String category) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) return new HashMap<>();
        
        return collections.getCollectionsByCategory(category);
    }

    /**
     * Get total collections for a player
     */
    public long getTotalCollections(Player player) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) return 0;
        
        return collections.getTotalCollections();
    }

    /**
     * Get total unlocked milestones for a player
     */
    public int getTotalUnlockedMilestones(Player player) {
        PlayerCollections collections = getPlayerCollections(player);
        if (collections == null) return 0;
        
        return collections.getTotalUnlockedMilestones();
    }

    // Event Handlers

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerCollections(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerCollections(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        
        // Check if the block drops items that can be collected
        CollectionType collection = CollectionType.getByMaterial(material);
        if (collection != null) {
            // Add collection based on block type
            long amount = getCollectionAmountFromBlock(material);
            addCollection(player, collection, amount);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            
            // Add collections from dropped items
            for (ItemStack drop : event.getDrops()) {
                addCollectionFromItem(player, drop);
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            org.bukkit.entity.Entity caughtEntity = event.getCaught();
            
            if (caughtEntity instanceof org.bukkit.entity.Item) {
                org.bukkit.entity.Item itemEntity = (org.bukkit.entity.Item) caughtEntity;
                ItemStack caught = itemEntity.getItemStack();
                addCollectionFromItem(player, caught);
            }
        }
    }

    /**
     * Get collection amount from block type
     */
    private long getCollectionAmountFromBlock(Material material) {
        switch (material) {
            // Mining blocks
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                return 1;
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                return 1;
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                return 1;
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                return 1;
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                return 1;
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                return 4; // Redstone drops 4-5
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return 4; // Lapis drops 4-8
            case NETHER_QUARTZ_ORE:
                return 1;
            case NETHER_GOLD_ORE:
                return 1;
            case ANCIENT_DEBRIS:
                return 1;
            case OBSIDIAN:
                return 1;
            case GLOWSTONE:
                return 2; // Glowstone drops 2-4
            case GRAVEL:
                return 1;
            case SAND:
                return 1;
            case END_STONE:
                return 1;
            case NETHERRACK:
                return 1;
            case ICE:
                return 1;
            case SNOW_BLOCK:
                return 1;
            case CLAY:
                return 4; // Clay drops 4 clay balls
            case STONE:
            case COBBLESTONE:
                return 1;
                
            // Farming blocks
            case WHEAT:
                return 1;
            case CARROTS:
                return 1;
            case POTATOES:
                return 1;
            case BEETROOTS:
                return 1;
            case PUMPKIN:
                return 1;
            case MELON:
                return 3; // Melon drops 3-7 slices
            case SUGAR_CANE:
                return 1;
            case CACTUS:
                return 1;
            case NETHER_WART:
                return 1;
            case COCOA:
                return 3; // Cocoa drops 3 beans
            case RED_MUSHROOM:
            case BROWN_MUSHROOM:
                return 1;
                
            // Foraging blocks
            case OAK_LOG:
            case BIRCH_LOG:
            case SPRUCE_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case DARK_OAK_LOG:
            case MANGROVE_LOG:
            case CHERRY_LOG:
                return 1;
            case OAK_LEAVES:
            case BIRCH_LEAVES:
            case SPRUCE_LEAVES:
            case JUNGLE_LEAVES:
            case ACACIA_LEAVES:
            case DARK_OAK_LEAVES:
                return 1;
                
            default:
                return 1;
        }
    }

    /**
     * Open collections GUI for player
     */
    public void openCollectionsGUI(Player player) {
        de.noctivag.skyblock.gui.features.CollectionsGUI gui = new de.noctivag.skyblock.gui.features.CollectionsGUI(plugin, player);
        gui.open();
    }

    /**
     * Open collection category GUI for player
     */
    public void openCollectionCategoryGUI(Player player, String category) {
        de.noctivag.skyblock.collections.CollectionsGUI gui = new de.noctivag.skyblock.collections.CollectionsGUI(plugin, this);
        
        switch (category.toLowerCase()) {
            case "farming":
                gui.openFarmingCollectionsGUI(player);
                break;
            case "mining":
                gui.openMiningCollectionsGUI(player);
                break;
            case "combat":
                gui.openCombatCollectionsGUI(player);
                break;
            case "foraging":
                gui.openForagingCollectionsGUI(player);
                break;
            case "fishing":
                gui.openFishingCollectionsGUI(player);
                break;
            default:
                openCollectionsGUI(player);
                break;
        }
    }

    /**
     * Get collection config for a collection type
     */
    public CollectionConfig getCollectionConfig(CollectionType type) {
        return new CollectionConfig(type);
    }
    
    /**
     * Add to collection for player
     */
    public void addToCollection(Player player, CollectionType type, Material material, int amount) {
        PlayerCollections playerCollections = getPlayerCollections(player.getUniqueId());
        long currentAmount = playerCollections.getCollectionAmount(type);
        playerCollections.setCollectionAmount(type, currentAmount + amount);
        
        // Check for milestone progression
        checkMilestoneProgression(player, type, playerCollections);
    }
    
    /**
     * Check milestone progression for a collection
     */
    private void checkMilestoneProgression(Player player, CollectionType type, PlayerCollections playerCollections) {
        // Check if any new milestones were unlocked
        boolean newMilestone = playerCollections.checkMilestoneProgression(type);
        if (newMilestone) {
            player.sendMessage("§a§lCollection Milestone Unlocked!");
            player.sendMessage("§7" + type.getDisplayName() + " collection milestone reached!");
        }
    }

    /**
     * Collection config class
     */
    public static class CollectionConfig {
        private final CollectionType type;
        private final long[] milestones;
        private final String[] rewards;

        public CollectionConfig(CollectionType type) {
            this.type = type;
            this.milestones = type.getMilestoneRequirements();
            this.rewards = new String[]{
                "Collection milestone reached!",
                "You've collected " + type.getDisplayName() + "!",
                "Keep collecting to unlock more rewards!"
            };
        }

        public CollectionType getType() { return type; }
        public long[] getMilestones() { return milestones; }
        public String[] getRewards() { return rewards; }
        public String getDisplayName() { return type.getDisplayName(); }
        public String getDescription() { return "Collection for " + type.getDisplayName(); }
        public org.bukkit.Material getMaterial() { return type.getMaterial(); }
    }
}