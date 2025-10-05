package de.noctivag.skyblock.dungeons.classes;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Class Manager - Manages dungeon classes
 */
public class ClassManager {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, DungeonClass> playerClasses = new HashMap<>();
    private final Map<String, DungeonClass> availableClasses = new HashMap<>();
    
    public ClassManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        initializeClasses();
    }
    
    /**
     * Initialize available classes
     */
    private void initializeClasses() {
        // Tank class
        DungeonClass tank = new DungeonClass(
            "Tank",
            "High health and defense, protects teammates",
            org.bukkit.Material.IRON_CHESTPLATE,
            Arrays.asList("Shield Wall", "Taunt", "Damage Reduction"),
            50
        );
        availableClasses.put("TANK", tank);
        
        // Healer class
        DungeonClass healer = new DungeonClass(
            "Healer",
            "Heals teammates and provides support",
            org.bukkit.Material.GOLDEN_APPLE,
            Arrays.asList("Heal", "Regeneration", "Revive"),
            50
        );
        availableClasses.put("HEALER", healer);
        
        // Mage class
        DungeonClass mage = new DungeonClass(
            "Mage",
            "High magic damage and area effects",
            org.bukkit.Material.BLAZE_ROD,
            Arrays.asList("Fireball", "Lightning", "Teleport"),
            50
        );
        availableClasses.put("MAGE", mage);
        
        // Archer class
        DungeonClass archer = new DungeonClass(
            "Archer",
            "High ranged damage and mobility",
            org.bukkit.Material.BOW,
            Arrays.asList("Multi-Shot", "Explosive Arrow", "Speed Boost"),
            50
        );
        availableClasses.put("ARCHER", archer);
        
        // Berserker class
        DungeonClass berserker = new DungeonClass(
            "Berserker",
            "High melee damage and attack speed",
            org.bukkit.Material.DIAMOND_SWORD,
            Arrays.asList("Berserker Rage", "Bloodlust", "Frenzy"),
            50
        );
        availableClasses.put("BERSERKER", berserker);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + availableClasses.size() + " dungeon classes.");
    }
    
    /**
     * Set a player's class
     */
    public boolean setPlayerClass(Player player, String className) {
        DungeonClass dungeonClass = availableClasses.get(className.toUpperCase());
        if (dungeonClass == null) {
            player.sendMessage("§cUnknown class: " + className);
            return false;
        }
        
        playerClasses.put(player.getUniqueId(), dungeonClass);
        player.sendMessage("§aSelected class: §c" + dungeonClass.getName());
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " selected class: " + dungeonClass.getName());
        
        return true;
    }
    
    /**
     * Get a player's class
     */
    public DungeonClass getPlayerClass(Player player) {
        return playerClasses.get(player.getUniqueId());
    }
    
    /**
     * Check if a player has a class
     */
    public boolean hasPlayerClass(Player player) {
        return playerClasses.containsKey(player.getUniqueId());
    }
    
    /**
     * Get all available classes
     */
    public Map<String, DungeonClass> getAvailableClasses() {
        return new HashMap<>(availableClasses);
    }
    
    /**
     * Get all player classes
     */
    public Map<UUID, DungeonClass> getPlayerClasses() {
        return new HashMap<>(playerClasses);
    }
}

