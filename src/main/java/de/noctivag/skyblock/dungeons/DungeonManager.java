package de.noctivag.skyblock.dungeons;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.dungeons.bosses.Bonzo;
import de.noctivag.skyblock.dungeons.bosses.DungeonBoss;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.PlayerProfileService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonManager {

    private final SkyblockPluginRefactored plugin;
    private final Map<String, DungeonInstance> activeDungeons = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerDungeons = new ConcurrentHashMap<>(); // Player UUID -> Dungeon ID

    public DungeonManager(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
    }

    public boolean startDungeon(Player player, String dungeonType, int floor) {
        if (player == null) {
            return false;
        }

        // Check if player is already in a dungeon
        if (playerDungeons.containsKey(player.getUniqueId())) {
            player.sendMessage("§cDu bist bereits in einem Dungeon!");
            return false;
        }

        // Check if player meets requirements
        if (!canStartDungeon(player, dungeonType, floor)) {
            player.sendMessage("§cDu erfüllst nicht die Anforderungen für diesen Dungeon!");
            return false;
        }

        // Create dungeon instance
        String dungeonId = generateDungeonId();
        DungeonInstance dungeon = new DungeonInstance(dungeonId, dungeonType, floor, player);
        
        if (!dungeon.initialize()) {
            player.sendMessage("§cFehler beim Erstellen des Dungeons!");
            return false;
        }

        // Register dungeon
        activeDungeons.put(dungeonId, dungeon);
        playerDungeons.put(player.getUniqueId(), dungeonId);

        // Teleport player to dungeon
        teleportPlayerToDungeon(player, dungeon);

        player.sendMessage("§aDungeon gestartet: " + dungeonType + " Floor " + floor);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Player " + player.getName() + " started dungeon " + dungeonType + " Floor " + floor);
        }

        return true;
    }

    public boolean joinDungeon(Player player, String dungeonId) {
        if (player == null || dungeonId == null) {
            return false;
        }

        DungeonInstance dungeon = activeDungeons.get(dungeonId);
        if (dungeon == null) {
            player.sendMessage("§cDungeon nicht gefunden!");
            return false;
        }

        if (playerDungeons.containsKey(player.getUniqueId())) {
            player.sendMessage("§cDu bist bereits in einem Dungeon!");
            return false;
        }

        if (!dungeon.canPlayerJoin(player)) {
            player.sendMessage("§cDu kannst diesem Dungeon nicht beitreten!");
            return false;
        }

        // Add player to dungeon
        dungeon.addPlayer(player);
        playerDungeons.put(player.getUniqueId(), dungeonId);

        // Teleport player to dungeon
        teleportPlayerToDungeon(player, dungeon);

        player.sendMessage("§aDu bist dem Dungeon beigetreten!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Player " + player.getName() + " joined dungeon " + dungeonId);
        }

        return true;
    }

    public boolean leaveDungeon(Player player) {
        if (player == null) {
            return false;
        }

        String dungeonId = playerDungeons.get(player.getUniqueId());
        if (dungeonId == null) {
            player.sendMessage("§cDu bist in keinem Dungeon!");
            return false;
        }

        DungeonInstance dungeon = activeDungeons.get(dungeonId);
        if (dungeon != null) {
            dungeon.removePlayer(player);
            
            // If no players left, clean up dungeon
            if (dungeon.getPlayers().isEmpty()) {
                cleanupDungeon(dungeonId);
            }
        }

        playerDungeons.remove(player.getUniqueId());

        // Teleport player back to hub
        teleportPlayerToHub(player);

        player.sendMessage("§aDu hast das Dungeon verlassen!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Player " + player.getName() + " left dungeon " + dungeonId);
        }

        return true;
    }

    private boolean canStartDungeon(Player player, String dungeonType, int floor) {
        // Check player level, items, etc.
        PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        if (playerProfileService == null) {
            return false;
        }

        PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        if (profile == null) {
            return false;
        }

        // Basic requirements check
        if (profile.getLevel() < getRequiredLevel(dungeonType, floor)) {
            player.sendMessage("§cDu benötigst mindestens Level " + getRequiredLevel(dungeonType, floor) + "!");
            return false;
        }

        // Check if player has required items
        if (!hasRequiredItems(player, dungeonType, floor)) {
            player.sendMessage("§cDu benötigst bestimmte Items für diesen Dungeon!");
            return false;
        }

        return true;
    }

    private int getRequiredLevel(String dungeonType, int floor) {
        // Return required level based on dungeon type and floor
        switch (dungeonType.toLowerCase()) {
            case "catacombs":
                return floor * 5; // Floor 1 = Level 5, Floor 2 = Level 10, etc.
            default:
                return floor * 3;
        }
    }

    private boolean hasRequiredItems(Player player, String dungeonType, int floor) {
        // Check if player has required items in inventory
        // For now, just check if player has some basic items
        return player.getInventory().contains(org.bukkit.Material.IRON_SWORD) ||
               player.getInventory().contains(org.bukkit.Material.DIAMOND_SWORD);
    }

    private String generateDungeonId() {
        return "dungeon_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void teleportPlayerToDungeon(Player player, DungeonInstance dungeon) {
        Location dungeonSpawn = dungeon.getSpawnLocation();
        if (dungeonSpawn != null) {
            if (plugin.isFoliaServer()) {
                player.teleportAsync(dungeonSpawn);
            } else {
                player.teleport(dungeonSpawn);
            }
        }
    }

    private void teleportPlayerToHub(Player player) {
        // Use TeleportService to teleport to hub
        var teleportService = plugin.getServiceManager().getService(de.noctivag.skyblock.services.TeleportService.class);
        if (teleportService != null) {
            teleportService.teleportToHub(player);
        } else {
            // Fallback: teleport to world spawn
            World hubWorld = Bukkit.getWorld("hub_a");
            if (hubWorld != null) {
                if (plugin.isFoliaServer()) {
                    player.teleportAsync(hubWorld.getSpawnLocation());
                } else {
                    player.teleport(hubWorld.getSpawnLocation());
                }
            }
        }
    }

    private void cleanupDungeon(String dungeonId) {
        DungeonInstance dungeon = activeDungeons.remove(dungeonId);
        if (dungeon != null) {
            dungeon.cleanup();
            
            if (plugin.getSettingsConfig().isDebugMode()) {
                plugin.getLogger().info("Cleaned up dungeon " + dungeonId);
            }
        }
    }

    public DungeonInstance getPlayerDungeon(Player player) {
        String dungeonId = playerDungeons.get(player.getUniqueId());
        return dungeonId != null ? activeDungeons.get(dungeonId) : null;
    }

    public boolean isPlayerInDungeon(Player player) {
        return playerDungeons.containsKey(player.getUniqueId());
    }

    public Collection<DungeonInstance> getActiveDungeons() {
        return activeDungeons.values();
    }

    public void cleanup() {
        // Clean up all active dungeons
        for (DungeonInstance dungeon : activeDungeons.values()) {
            dungeon.cleanup();
        }
        activeDungeons.clear();
        playerDungeons.clear();
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("DungeonManager cleaned up all dungeons");
        }
    }
}
