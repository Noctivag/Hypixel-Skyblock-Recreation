package de.noctivag.skyblock.worlds;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Rolling Restart World Manager - Manages world rolling restarts
 */
public class RollingRestartWorldManager implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, World> loadedWorlds = new HashMap<>();
    private final Map<String, String> liveWorlds = new HashMap<>();
    
    public RollingRestartWorldManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "RollingRestartWorldManager";
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing RollingRestartWorldManager...");
        
        // Initialize world management
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("RollingRestartWorldManager initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down RollingRestartWorldManager...");
        
        // Unload all worlds
        for (String worldName : loadedWorlds.keySet()) {
            unloadWorld(worldName);
        }
        
        loadedWorlds.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("RollingRestartWorldManager shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Load a world
     */
    public CompletableFuture<World> loadWorld(String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            World world = plugin.getServer().getWorld(worldName);
            if (world == null) {
                plugin.getLogger().log(Level.INFO, "Loading world: " + worldName);
                WorldCreator creator = new WorldCreator(worldName);
                world = creator.createWorld();
                if (world != null) {
                    loadedWorlds.put(worldName, world);
                    plugin.getLogger().log(Level.INFO, "World '" + worldName + "' loaded successfully.");
                } else {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load world: " + worldName);
                }
            } else {
                loadedWorlds.put(worldName, world);
                plugin.getLogger().log(Level.INFO, "World '" + worldName + "' is already loaded.");
            }
            return world;
        });
    }
    
    /**
     * Unload a world
     */
    public CompletableFuture<Boolean> unloadWorld(String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            World world = loadedWorlds.get(worldName);
            if (world != null) {
                plugin.getLogger().log(Level.INFO, "Unloading world: " + worldName);
                boolean success = plugin.getServer().unloadWorld(world, true);
                if (success) {
                    loadedWorlds.remove(worldName);
                    plugin.getLogger().log(Level.INFO, "World '" + worldName + "' unloaded successfully.");
                } else {
                    plugin.getLogger().log(Level.WARNING, "Failed to unload world: " + worldName);
                }
                return success;
            } else {
                plugin.getLogger().log(Level.WARNING, "World '" + worldName + "' is not loaded.");
                return false;
            }
        });
    }
    
    /**
     * Get a world
     */
    public World getWorld(String worldName) {
        return loadedWorlds.get(worldName);
    }
    
    /**
     * Get all loaded world names
     */
    public Set<String> getLoadedWorldNames() {
        return Collections.unmodifiableSet(loadedWorlds.keySet());
    }
    
    /**
     * Set live world
     */
    public void setLiveWorld(String worldType, String worldName) {
        liveWorlds.put(worldType, worldName);
        plugin.getLogger().info("Set live world for " + worldType + ": " + worldName);
    }
    
    /**
     * Get live world name
     */
    public String getLiveWorldName(String worldType) {
        return liveWorlds.getOrDefault(worldType, worldType);
    }
    
    /**
     * Get live world instance
     */
    public World getLiveWorld(String worldType) {
        String worldName = getLiveWorldName(worldType);
        World world = getWorld(worldName);
        if (world == null) {
            // Try to get from server directly
            world = plugin.getServer().getWorld(worldName);
        }
        return world;
    }

    /**
     * Get world by type with fallback logic
     */
    public World getWorldByType(String worldType) {
        World world = getLiveWorld(worldType);
        if (world == null) {
            // Try to load or create the world
            try {
                world = loadOrCreateWorld(worldType);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to get world for type: " + worldType + " - " + e.getMessage());
            }
        }
        return world;
    }

    /**
     * Load or create a world based on type
     */
    private World loadOrCreateWorld(String worldType) {
        String worldName = getDefaultWorldName(worldType);
        return loadWorld(worldName).join();
    }

    /**
     * Get default world name for type
     */
    private String getDefaultWorldName(String worldType) {
        return switch (worldType.toLowerCase()) {
            case "hub" -> "hub_a";
            case "skyblock", "island" -> "skyblock_a";
            case "dungeon" -> "dungeon_a";
            case "mining" -> "mining_a";
            case "farming" -> "farming_a";
            case "combat" -> "combat_a";
            case "fishing" -> "fishing_a";
            case "nether" -> "nether_a";
            case "end" -> "end_a";
            default -> worldType + "_a";
        };
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
}