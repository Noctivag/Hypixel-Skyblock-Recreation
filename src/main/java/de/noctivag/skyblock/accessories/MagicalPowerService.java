package de.noctivag.skyblock.accessories;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Magical Power Service - Service for managing magical power
 */
public class MagicalPowerService implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, AccessoryBag> playerAccessoryBags = new HashMap<>();
    private final Map<UUID, Double> playerMagicalPower = new HashMap<>();
    
    public MagicalPowerService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing MagicalPowerService...");
        
        // Initialize magical power system
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("MagicalPowerService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down MagicalPowerService...");
        
        // Save all accessory data
        for (AccessoryBag accessoryBag : playerAccessoryBags.values()) {
            // Save accessory data to database
            plugin.getLogger().log(Level.INFO, "Saving accessory bag for player: " + accessoryBag.getOwnerId());
        }
        
        playerAccessoryBags.clear();
        playerMagicalPower.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("MagicalPowerService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "MagicalPowerService";
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
     * Get a player's accessory bag
     */
    public AccessoryBag getPlayerAccessoryBag(Player player) {
        return playerAccessoryBags.computeIfAbsent(player.getUniqueId(), uuid -> new AccessoryBag(uuid, 9));
    }
    
    /**
     * Get a player's magical power
     */
    public double getPlayerMagicalPower(Player player) {
        return playerMagicalPower.getOrDefault(player.getUniqueId(), 0.0);
    }
    
    /**
     * Set a player's magical power
     */
    public void setPlayerMagicalPower(Player player, double power) {
        playerMagicalPower.put(player.getUniqueId(), power);
        plugin.getLogger().log(Level.INFO, "Set magical power for player " + player.getName() + ": " + power);
    }
    
    /**
     * Add magical power to a player
     */
    public void addMagicalPower(Player player, double amount) {
        double currentPower = getPlayerMagicalPower(player);
        setPlayerMagicalPower(player, currentPower + amount);
    }
    
    /**
     * Remove magical power from a player
     */
    public void removeMagicalPower(Player player, double amount) {
        double currentPower = getPlayerMagicalPower(player);
        setPlayerMagicalPower(player, Math.max(0, currentPower - amount));
    }
    
    /**
     * Get all player accessory bags
     */
    public Map<UUID, AccessoryBag> getPlayerAccessoryBags() {
        return new HashMap<>(playerAccessoryBags);
    }
    
    /**
     * Get all player magical power
     */
    public Map<UUID, Double> getPlayerMagicalPower() {
        return new HashMap<>(playerMagicalPower);
    }
}
