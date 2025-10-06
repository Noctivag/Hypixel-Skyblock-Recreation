package de.noctivag.skyblock.display;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Integrated Menu System - Manages integrated menus
 */
public class IntegratedMenuSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, String> playerMenus = new HashMap<>();
    
    public IntegratedMenuSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing IntegratedMenuSystem...");
        
        // Initialize menu system
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("IntegratedMenuSystem initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down IntegratedMenuSystem...");
        
        // Clear all menus
        playerMenus.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("IntegratedMenuSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "IntegratedMenuSystem";
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
     * Open a menu for a player
     */
    public void openMenu(Player player, String menuId) {
        playerMenus.put(player.getUniqueId(), menuId);
        plugin.getLogger().log(Level.INFO, "Opened menu for player " + player.getName() + ": " + menuId);
    }
    
    /**
     * Close a menu for a player
     */
    public void closeMenu(Player player) {
        playerMenus.remove(player.getUniqueId());
        plugin.getLogger().log(Level.INFO, "Closed menu for player " + player.getName());
    }
    
    /**
     * Get a player's current menu
     */
    public String getPlayerMenu(Player player) {
        return playerMenus.get(player.getUniqueId());
    }
    
    /**
     * Check if a player has an open menu
     */
    public boolean hasOpenMenu(Player player) {
        return playerMenus.containsKey(player.getUniqueId());
    }
    
    /**
     * Get all player menus
     */
    public Map<UUID, String> getPlayerMenus() {
        return new HashMap<>(playerMenus);
    }
}