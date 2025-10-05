package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Teleport Service
 */
public class TeleportService implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = true;
    
    public TeleportService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing TeleportService...");
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("TeleportService initialized successfully!");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down TeleportService...");
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("TeleportService shutdown complete!");
    }
    
    @Override
    public String getName() {
        return "TeleportService";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
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
     * Teleport player to location
     */
    public void teleportPlayer(Player player, Location location) {
        if (enabled) {
            player.teleport(location);
        }
    }
}