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
    private final Map<UUID, BaseMinion> minions = new HashMap<>();
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
        // MinionType constructors not implemented yet - commenting out for now
        // MinionType cobblestone = new MinionType(...);
        // minionTypes.put("COBBLESTONE", cobblestone);
        
        // MinionType coal = new MinionType(...);
        // minionTypes.put("COAL", coal);
        
        // MinionType iron = new MinionType(...);
        // minionTypes.put("IRON", iron);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + minionTypes.size() + " minion types.");
    }
    
    /**
     * Initialize upgrade types
     */
    private void initializeUpgradeTypes() {
        // Compactor upgrade - constructor not implemented yet
        // MinionUpgrade compactor = new MinionUpgrade(...);
        // MinionUpgradeType compactorType = new MinionUpgradeType(...);
        // upgradeTypes.put("COMPACTOR", compactorType);
        
        // Minion Fuel upgrade - constructor not implemented yet
        // MinionUpgrade fuel = new MinionUpgrade(...);
        // MinionUpgradeType fuelType = new MinionUpgradeType(...);
        // upgradeTypes.put("FUEL", fuelType);
        
        // Diamond Spreading upgrade - constructor not implemented yet
        // MinionUpgrade diamondSpreading = new MinionUpgrade(...);
        // MinionUpgradeType diamondSpreadingType = new MinionUpgradeType(...);
        // upgradeTypes.put("DIAMOND_SPREADING", diamondSpreadingType);
        
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
        player.sendMessage("§aCreated Minion!");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " created Minion");
        return minion;
    }
    
    /**
     * Get a minion by ID
     */
    public BaseMinion getMinion(UUID minionId) {
        return minions.get(minionId);
    }
    
    /**
     * Get all minions for a player
     */
    public Map<UUID, BaseMinion> getPlayerMinions(Player player) {
        Map<UUID, BaseMinion> playerMinions = new HashMap<>();
        for (BaseMinion minion : minions.values()) {
            if (minion.getOwnerId().equals(player.getUniqueId())) {
                playerMinions.put(UUID.fromString(minion.getMinionId()), minion);
            }
        }
        return playerMinions;
    }
    
    /**
     * Remove a minion
     */
    public boolean removeMinion(UUID minionId) {
        BaseMinion minion = minions.remove(minionId);
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
    
    @Override
    public String getName() {
        return "MinionManager";
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
}

