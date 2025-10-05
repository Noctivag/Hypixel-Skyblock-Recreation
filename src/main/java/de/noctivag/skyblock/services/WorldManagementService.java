package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.worlds.WorldManager;

/**
 * World Management Service
 */
public class WorldManagementService implements Service {
    
    private final SkyblockPlugin plugin;
    private final WorldManager worldManager;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = true;
    
    public WorldManagementService(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.worldManager = new WorldManager(plugin);
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing WorldManagementService...");
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("WorldManagementService initialized successfully!");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down WorldManagementService...");
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("WorldManagementService shutdown complete!");
    }
    
    @Override
    public String getName() {
        return "WorldManagementService";
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
     * Get world manager
     */
    public WorldManager getWorldManager() {
        return worldManager;
    }
}