package de.noctivag.skyblock.dungeons;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.dungeons.classes.ClassManager;
import de.noctivag.skyblock.dungeons.instances.DungeonInstance;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Dungeon Manager - Manages dungeon instances
 */
public class DungeonManager {
    
    private final SkyblockPlugin plugin;
    private final ClassManager classManager;
    private final Map<UUID, DungeonInstance> activeInstances = new HashMap<>();
    
    public DungeonManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = new ClassManager(plugin);
        plugin.getLogger().info("DungeonManager initialized.");
    }
    
    /**
     * Create a new dungeon instance
     */
    public DungeonInstance createDungeonInstance(Player player, String dungeonType) {
        UUID instanceId = UUID.randomUUID();
        DungeonInstance instance = new DungeonInstance(instanceId, dungeonType, player);
        
        activeInstances.put(instanceId, instance);
        plugin.getLogger().log(Level.INFO, "Created dungeon instance " + instanceId + " for player " + player.getName());
        
        return instance;
    }
    
    /**
     * Get a dungeon instance by ID
     */
    public DungeonInstance getDungeonInstance(UUID instanceId) {
        return activeInstances.get(instanceId);
    }
    
    /**
     * Get a player's active dungeon instance
     */
    public DungeonInstance getPlayerDungeonInstance(Player player) {
        for (DungeonInstance instance : activeInstances.values()) {
            if (instance.getPlayers().contains(player.getUniqueId())) {
                return instance;
            }
        }
        return null;
    }
    
    /**
     * Check if a player is in a dungeon
     */
    public boolean isPlayerInDungeon(Player player) {
        return getPlayerDungeonInstance(player) != null;
    }
    
    /**
     * Remove a dungeon instance
     */
    public boolean removeDungeonInstance(UUID instanceId) {
        DungeonInstance instance = activeInstances.remove(instanceId);
        if (instance != null) {
            plugin.getLogger().log(Level.INFO, "Removed dungeon instance " + instanceId);
            return true;
        }
        return false;
    }
    
    /**
     * Get all active instances
     */
    public Map<UUID, DungeonInstance> getActiveInstances() {
        return new HashMap<>(activeInstances);
    }
    
    /**
     * Get the class manager
     */
    public ClassManager getClassManager() {
        return classManager;
    }
}

