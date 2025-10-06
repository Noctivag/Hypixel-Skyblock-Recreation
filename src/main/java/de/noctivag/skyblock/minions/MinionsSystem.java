package de.noctivag.skyblock.minions;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Minions System - Manages player minions, automation, and production
 */
public class MinionsSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerMinions> playerMinions;
    private final Map<String, PlacedMinion> activeMinions;
    private final BukkitTask minionTask;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public MinionsSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerMinions = new ConcurrentHashMap<>();
        this.activeMinions = new ConcurrentHashMap<>();
        
        // Start minion automation task
        this.minionTask = new BukkitRunnable() {
            @Override
            public void run() {
                processMinionActions();
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing MinionsSystem...");

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Load all online player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerMinions(player.getUniqueId());
        }

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("MinionsSystem initialized successfully!");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down MinionsSystem...");

        // Cancel minion task
        if (minionTask != null && !minionTask.isCancelled()) {
            minionTask.cancel();
        }

        // Save all player data
        saveAllPlayerMinions();

        playerMinions.clear();
        activeMinions.clear();
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("MinionsSystem shutdown complete!");
    }

    @Override
    public String getName() {
        return "MinionsSystem";
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
        // Minions system is always enabled when running
    }

    /**
     * Load player minions from database
     */
    public void loadPlayerMinions(UUID playerId) {
        try {
            // Load from database (placeholder - implement actual database loading)
            PlayerMinions minions = new PlayerMinions(playerId);
            playerMinions.put(playerId, minions);
            plugin.getLogger().info("Loaded minions for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load minions for player " + playerId + ": " + e.getMessage());
            // Create new minions if loading fails
            playerMinions.put(playerId, new PlayerMinions(playerId));
        }
    }

    /**
     * Save player minions to database
     */
    public void savePlayerMinions(UUID playerId) {
        try {
            PlayerMinions minions = playerMinions.get(playerId);
            if (minions != null) {
                // Save to database (placeholder - implement actual database saving)
                plugin.getLogger().info("Saved minions for player: " + playerId);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save minions for player " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player minions
     */
    public void saveAllPlayerMinions() {
        for (UUID playerId : playerMinions.keySet()) {
            savePlayerMinions(playerId);
        }
    }

    /**
     * Get player minions
     */
    public PlayerMinions getPlayerMinions(UUID playerId) {
        return playerMinions.get(playerId);
    }

    /**
     * Get player minions (by Player object)
     */
    public PlayerMinions getPlayerMinions(Player player) {
        return getPlayerMinions(player.getUniqueId());
    }

    /**
     * Place a minion
     */
    public boolean placeMinion(Player player, MinionTier minionTier, Location location) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) {
            loadPlayerMinions(player.getUniqueId());
            playerMinions = getPlayerMinions(player);
        }

        if (!playerMinions.canPlaceMoreMinions()) {
            player.sendMessage("§cYou have reached your minion slot limit!");
            return false;
        }

        // Generate unique minion ID
        String minionId = player.getUniqueId().toString() + "_" + System.currentTimeMillis();
        
        // Create placed minion
        PlacedMinion placedMinion = new PlacedMinion(minionId, player.getUniqueId(), minionTier, location);
        
        // Add to player minions
        if (playerMinions.placeMinion(placedMinion)) {
            // Add to active minions
            activeMinions.put(minionId, placedMinion);
            
            // Add to collections system
            de.noctivag.skyblock.collections.CollectionType collectionType = minionTier.getMinionType().getCollectionType();
            if (collectionType != null) {
                plugin.getCollectionsSystem().addCollection(player, collectionType, 1);
            }
            
            player.sendMessage("§aMinion placed successfully!");
            return true;
        }
        
        return false;
    }

    /**
     * Remove a minion
     */
    public boolean removeMinion(Player player, String minionId) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return false;

        PlacedMinion removedMinion = playerMinions.getPlacedMinion(minionId);
        if (removedMinion == null) return false;

        // Remove from player minions
        if (playerMinions.removeMinion(minionId)) {
            // Remove from active minions
            activeMinions.remove(minionId);
            
            player.sendMessage("§aMinion removed successfully!");
            return true;
        }
        
        return false;
    }

    /**
     * Get placed minion by ID
     */
    public PlacedMinion getPlacedMinion(String minionId) {
        return activeMinions.get(minionId);
    }

    /**
     * Get all active minions
     */
    public Collection<PlacedMinion> getAllActiveMinions() {
        return new ArrayList<>(activeMinions.values());
    }

    /**
     * Get active minions by player
     */
    public List<PlacedMinion> getActiveMinionsByPlayer(UUID playerId) {
        return activeMinions.values().stream()
                .filter(minion -> minion.getOwnerId().equals(playerId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get active minions by type
     */
    public List<PlacedMinion> getActiveMinionsByType(MinionType minionType) {
        return activeMinions.values().stream()
                .filter(minion -> minion.getMinionTier().getMinionType() == minionType)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Process minion actions
     */
    private void processMinionActions() {
        for (PlacedMinion minion : activeMinions.values()) {
            if (minion.canPerformAction()) {
                // Perform minion action
                if (minion.performAction()) {
                    // Add to collections system
                    de.noctivag.skyblock.collections.CollectionType collectionType = 
                        minion.getMinionTier().getMinionType().getCollectionType();
                    if (collectionType != null) {
                        Player player = Bukkit.getPlayer(minion.getOwnerId());
                        if (player != null) {
                            plugin.getCollectionsSystem().addCollection(player, collectionType, 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Get minion statistics for a player
     */
    public PlayerMinions.MinionStatistics getMinionStatistics(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return null;
        
        return playerMinions.getMinionStatistics();
    }

    /**
     * Get total minion production rate for a player
     */
    public double getTotalProductionRate(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return 0.0;
        
        return playerMinions.getTotalMinionEfficiency();
    }

    /**
     * Get minion production rate by category for a player
     */
    public double getProductionRateByCategory(Player player, String category) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return 0.0;
        
        return playerMinions.getMinionEfficiencyByCategory(category);
    }

    /**
     * Get estimated daily production for a player
     */
    public double getEstimatedDailyProduction(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return 0.0;
        
        return playerMinions.getEstimatedDailyProduction();
    }

    /**
     * Get estimated weekly production for a player
     */
    public double getEstimatedWeeklyProduction(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return 0.0;
        
        return playerMinions.getEstimatedWeeklyProduction();
    }

    /**
     * Get minion upgrade by ID
     */
    public MinionUpgrade getMinionUpgrade(String upgradeId) {
        return MinionUpgrade.getById(upgradeId);
    }

    /**
     * Get all available minion upgrades
     */
    public MinionUpgrade[] getAllMinionUpgrades() {
        return MinionUpgrade.createDefaultUpgrades();
    }

    /**
     * Get minion upgrades by type
     */
    public MinionUpgrade[] getMinionUpgradesByType(MinionUpgrade.UpgradeType upgradeType) {
        return MinionUpgrade.getByType(upgradeType);
    }

    /**
     * Get minion fuel by ID
     */
    public MinionFuel getMinionFuel(String fuelId) {
        return MinionFuel.getById(fuelId);
    }

    /**
     * Get all available minion fuels
     */
    public MinionFuel[] getAllMinionFuels() {
        return MinionFuel.createDefaultFuels();
    }

    /**
     * Get minion fuels by type
     */
    public MinionFuel[] getMinionFuelsByType(MinionFuel.FuelType fuelType) {
        return MinionFuel.getByType(fuelType);
    }

    /**
     * Get minion type by material
     */
    public MinionType getMinionTypeByMaterial(Material material) {
        return MinionType.getByMaterial(material);
    }

    /**
     * Get minion types by category
     */
    public MinionType[] getMinionTypesByCategory(String category) {
        return MinionType.getByCategory(category);
    }

    /**
     * Get minion tier for a minion type and level
     */
    public MinionTier getMinionTier(MinionType minionType, int tier) {
        return new MinionTier(minionType, tier);
    }

    /**
     * Check if player can place more minions
     */
    public boolean canPlayerPlaceMoreMinions(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return false;
        
        return playerMinions.canPlaceMoreMinions();
    }

    /**
     * Get available minion slots for a player
     */
    public int getAvailableMinionSlots(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return 0;
        
        return playerMinions.getAvailableMinionSlots();
    }

    /**
     * Get max minion slots for a player
     */
    public int getMaxMinionSlots(Player player) {
        PlayerMinions playerMinions = getPlayerMinions(player);
        if (playerMinions == null) return 5; // Default 5 slots
        
        return playerMinions.getMaxMinionSlots();
    }

    // Event Handlers

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerMinions(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerMinions(player.getUniqueId());
    }
}
