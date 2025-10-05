package de.noctivag.skyblock.worlds;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.HashMap;
import java.util.Map;

/**
 * World Manager - Handles all world operations
 * Folia compatible implementation
 */
public class WorldManager {

    private final SkyblockPlugin plugin;
    private final Map<String, World> worlds = new HashMap<>();

    public WorldManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("World Manager initialized");
    }

    /**
     * Create a world
     */
    public World createWorld(String worldName, World.Environment environment) {
        try {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                plugin.getLogger().info("World " + worldName + " already exists");
                return world;
            }

            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.environment(environment);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generateStructures(false);

            world = worldCreator.createWorld();
            if (world != null) {
                worlds.put(worldName, world);
                plugin.getLogger().info("Created world: " + worldName);
            } else {
                plugin.getLogger().severe("Failed to create world: " + worldName);
            }

            return world;
        } catch (Exception e) {
            plugin.getLogger().severe("Error creating world " + worldName + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Get a world by name
     */
    public World getWorld(String worldName) {
        World world = worlds.get(worldName);
        if (world == null) {
            world = Bukkit.getWorld(worldName);
            if (world != null) {
                worlds.put(worldName, world);
            }
        }
        return world;
    }

    /**
     * Check if a world exists
     */
    public boolean worldExists(String worldName) {
        return getWorld(worldName) != null;
    }

    /**
     * Get all managed worlds
     */
    public Map<String, World> getWorlds() {
        return new HashMap<>(worlds);
    }
}
