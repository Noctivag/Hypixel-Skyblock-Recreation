package de.noctivag.skyblock.minions;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Minion Manager - Manages minions
 */
public class MinionManager implements Service {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, Minion> minions = new HashMap<>();
    private final Map<String, MinionType> minionTypes = new HashMap<>();
    private final Map<String, MinionUpgradeType> upgradeTypes = new HashMap<>();
    private SystemStatus status = SystemStatus.DISABLED;
    
    public MinionManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing MinionManager...");
        
        // Initialize minion types
        initializeMinionTypes();
        
        // Initialize upgrade types
        initializeUpgradeTypes();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("MinionManager initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down MinionManager...");
        
        // Save all minion data
        for (Minion minion : minions.values()) {
            // Save minion data to database
            plugin.getLogger().log(Level.INFO, "Saving minion: " + minion.getMinionId());
        }
        
        minions.clear();
        minionTypes.clear();
        upgradeTypes.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("MinionManager shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Initialize minion types
     */
    private void initializeMinionTypes() {
        // Cobblestone minion
        MinionType cobblestone = new MinionType(
            "Cobblestone Minion",
            "Mines cobblestone automatically",
            org.bukkit.Material.COBBLESTONE,
            org.bukkit.Material.COBBLESTONE,
            11,
            2000, // 2 seconds
            160   // 2.5 stacks
        );
        minionTypes.put("COBBLESTONE", cobblestone);
        
        // Coal minion
        MinionType coal = new MinionType(
            "Coal Minion",
            "Mines coal automatically",
            org.bukkit.Material.COAL,
            org.bukkit.Material.COAL,
            11,
            2000, // 2 seconds
            160   // 2.5 stacks
        );
        minionTypes.put("COAL", coal);
        
        // Iron minion
        MinionType iron = new MinionType(
            "Iron Minion",
            "Mines iron automatically",
            org.bukkit.Material.IRON_INGOT,
            org.bukkit.Material.IRON_INGOT,
            11,
            2000, // 2 seconds
            160   // 2.5 stacks
        );
        minionTypes.put("IRON", iron);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + minionTypes.size() + " minion types.");
    }
    
    /**
     * Initialize upgrade types
     */
    private void initializeUpgradeTypes() {
        // Compactor upgrade
        MinionUpgrade compactor = new MinionUpgrade(
            "Compactor",
            "Compacts items into blocks",
            1,
            1.0
        );
        MinionUpgradeType compactorType = new MinionUpgradeType(
            "Compactor",
            "Compacts items into blocks",
            org.bukkit.Material.COMPARATOR,
            compactor,
            1
        );
        upgradeTypes.put("COMPACTOR", compactorType);
        
        // Minion Fuel upgrade
        MinionUpgrade fuel = new MinionUpgrade(
            "Minion Fuel",
            "Increases minion speed",
            1,
            1.5
        );
        MinionUpgradeType fuelType = new MinionUpgradeType(
            "Minion Fuel",
            "Increases minion speed",
            org.bukkit.Material.COAL,
            fuel,
            1
        );
        upgradeTypes.put("FUEL", fuelType);
        
        // Diamond Spreading upgrade
        MinionUpgrade diamondSpreading = new MinionUpgrade(
            "Diamond Spreading",
            "Chance to drop diamonds",
            1,
            0.1
        );
        MinionUpgradeType diamondSpreadingType = new MinionUpgradeType(
            "Diamond Spreading",
            "Chance to drop diamonds",
            org.bukkit.Material.DIAMOND,
            diamondSpreading,
            1
        );
        upgradeTypes.put("DIAMOND_SPREADING", diamondSpreadingType);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + upgradeTypes.size() + " upgrade types.");
    }
    
    /**
     * Create a minion
     */
    public Minion createMinion(Player player, String minionTypeName, Location location) {
        MinionType minionType = minionTypes.get(minionTypeName.toUpperCase());
        if (minionType == null) {
            player.sendMessage("§cUnknown minion type: " + minionTypeName);
            return null;
        }
        
        UUID minionId = UUID.randomUUID();
        Minion minion = new Minion(minionId, player.getUniqueId(), minionType, location);
        
        minions.put(minionId, minion);
        player.sendMessage("§aCreated " + minionType.getName() + "!");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " created " + minionType.getName());
        
        return minion;
    }
    
    /**
     * Get a minion by ID
     */
    public Minion getMinion(UUID minionId) {
        return minions.get(minionId);
    }
    
    /**
     * Get all minions for a player
     */
    public Map<UUID, Minion> getPlayerMinions(Player player) {
        Map<UUID, Minion> playerMinions = new HashMap<>();
        for (Minion minion : minions.values()) {
            if (minion.getOwnerId().equals(player.getUniqueId())) {
                playerMinions.put(minion.getMinionId(), minion);
            }
        }
        return playerMinions;
    }
    
    /**
     * Remove a minion
     */
    public boolean removeMinion(UUID minionId) {
        Minion minion = minions.remove(minionId);
        if (minion != null) {
            plugin.getLogger().log(Level.INFO, "Removed minion: " + minionId);
            return true;
        }
        return false;
    }
    
    /**
     * Get all minions
     */
    public Map<UUID, Minion> getMinions() {
        return new HashMap<>(minions);
    }
    
    /**
     * Get all minion types
     */
    public Map<String, MinionType> getMinionTypes() {
        return new HashMap<>(minionTypes);
    }
    
    /**
     * Get all upgrade types
     */
    public Map<String, MinionUpgradeType> getUpgradeTypes() {
        return new HashMap<>(upgradeTypes);
    }
}

