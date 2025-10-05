package de.noctivag.skyblock.core;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Core Platform - Central service management
 */
public class CorePlatform implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();
    
    public CorePlatform(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing CorePlatform...");
        
        // Register services here
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("CorePlatform initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down CorePlatform...");
        
        // Unregister services here
        // Additional shutdown logic can be added here
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("CorePlatform shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "CorePlatform";
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
     * Get a player profile
     */
    public PlayerProfile getPlayerProfile(UUID uuid) {
        return playerProfiles.computeIfAbsent(uuid, PlayerProfile::new);
    }
    
    /**
     * Get all player profiles
     */
    public Map<UUID, PlayerProfile> getPlayerProfiles() {
        return new HashMap<>(playerProfiles);
    }
}