package de.noctivag.skyblock.dungeons;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.dungeons.bosses.Bonzo;
import de.noctivag.skyblock.dungeons.bosses.DungeonBoss;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonInstance {

    private final String dungeonId;
    private final String dungeonType;
    private final int floor;
    private final Player creator;
    private final Set<Player> players = ConcurrentHashMap.newKeySet();
    private final Map<String, Object> dungeonData = new ConcurrentHashMap<>();
    
    private World dungeonWorld;
    private Location spawnLocation;
    private DungeonBoss currentBoss;
    private DungeonState state;
    private long startTime;
    private long endTime;

    public DungeonInstance(String dungeonId, String dungeonType, int floor, Player creator) {
        this.dungeonId = dungeonId;
        this.dungeonType = dungeonType;
        this.floor = floor;
        this.creator = creator;
        this.state = DungeonState.CREATING;
        this.startTime = System.currentTimeMillis();
    }

    public boolean initialize() {
        try {
            // Create or get dungeon world
            if (!createDungeonWorld()) {
                return false;
            }

            // Set spawn location
            setSpawnLocation();

            // Add creator to players
            addPlayer(creator);

            // Generate dungeon structure
            generateDungeonStructure();

            // Set state to active
            state = DungeonState.ACTIVE;

            return true;
        } catch (Exception e) {
            SkyblockPluginRefactored.getInstance().getLogger().severe("Failed to initialize dungeon: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean createDungeonWorld() {
        try {
            // Create a unique world name for this dungeon instance
            String worldName = "dungeon_" + dungeonId;
            
            // Check if world already exists
            dungeonWorld = Bukkit.getWorld(worldName);
            if (dungeonWorld != null) {
                // World exists, use it
                return true;
            }

            // Create new world with dungeon generator
            DungeonGenerator generator = new DungeonGenerator(SkyblockPluginRefactored.getInstance(), dungeonType, floor);
            World world = Bukkit.createWorld(new org.bukkit.WorldCreator(worldName).generator(generator));
            
            if (world == null) {
                SkyblockPluginRefactored.getInstance().getLogger().severe("Failed to create dungeon world: " + worldName);
                return false;
            }

            dungeonWorld = world;
            return true;
        } catch (Exception e) {
            SkyblockPluginRefactored.getInstance().getLogger().severe("Error creating dungeon world: " + e.getMessage());
            return false;
        }
    }

    private void setSpawnLocation() {
        if (dungeonWorld == null) {
            spawnLocation = null;
            return;
        }

        // Set spawn location to world spawn
        spawnLocation = dungeonWorld.getSpawnLocation();
        
        // Adjust spawn location (e.g., center of dungeon room)
        spawnLocation.setX(spawnLocation.getX() + 0.5);
        spawnLocation.setZ(spawnLocation.getZ() + 0.5);
        spawnLocation.setY(spawnLocation.getY() + 1);
    }

    private void generateDungeonStructure() {
        if (dungeonWorld == null || spawnLocation == null) {
            return;
        }

        // Generate the dungeon structure asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    DungeonGenerator generator = new DungeonGenerator(SkyblockPluginRefactored.getInstance(), dungeonType, floor);
                    generator.generateDungeon(dungeonWorld, spawnLocation);
                    
                    // Spawn boss after structure is generated
                    spawnBoss();
                } catch (Exception e) {
                    SkyblockPluginRefactored.getInstance().getLogger().severe("Error generating dungeon structure: " + e.getMessage());
                }
            }
        }.runTaskLater(SkyblockPluginRefactored.getInstance(), 20L); // 1 second delay
    }

    private void spawnBoss() {
        if (spawnLocation == null) {
            return;
        }

        // Spawn boss based on dungeon type and floor
        switch (dungeonType.toLowerCase()) {
            case "catacombs":
                if (floor == 1) {
                    currentBoss = new Bonzo(SkyblockPluginRefactored.getInstance(), spawnLocation.clone().add(0, 0, 10));
                }
                // Add more bosses for higher floors
                break;
            default:
                // Default boss for unknown dungeon types
                currentBoss = new Bonzo(SkyblockPluginRefactored.getInstance(), spawnLocation.clone().add(0, 0, 10));
                break;
        }

        if (currentBoss != null) {
            currentBoss.startBoss();
            
            if (SkyblockPluginRefactored.getInstance().getSettingsConfig().isDebugMode()) {
                SkyblockPluginRefactored.getInstance().getLogger().info("Boss spawned in dungeon " + dungeonId);
            }
        }
    }

    public void addPlayer(Player player) {
        if (player == null) {
            return;
        }

        players.add(player);
        
        // Send welcome message
        player.sendMessage("§aWillkommen im " + dungeonType + " Floor " + floor + "!");
        player.sendMessage("§7Ziel: Besiege den Boss und sammle Loot!");
        
        // Notify other players
        players.forEach(p -> {
            if (!p.equals(player)) {
                p.sendMessage("§e" + player.getName() + " §7ist dem Dungeon beigetreten!");
            }
        });
    }

    public void removePlayer(Player player) {
        if (player == null) {
            return;
        }

        players.remove(player);
        
        // Notify remaining players
        players.forEach(p -> {
            p.sendMessage("§e" + player.getName() + " §7hat das Dungeon verlassen!");
        });
    }

    public boolean canPlayerJoin(Player player) {
        if (player == null) {
            return false;
        }

        // Check if dungeon is still active
        if (state != DungeonState.ACTIVE) {
            return false;
        }

        // Check if dungeon is full (max 5 players)
        if (players.size() >= 5) {
            return false;
        }

        // Check if player meets requirements
        // This could include level checks, item checks, etc.
        return true;
    }

    public void completeDungeon() {
        state = DungeonState.COMPLETED;
        endTime = System.currentTimeMillis();
        
        // Give rewards to all players
        players.forEach(this::giveCompletionRewards);
        
        // Notify players
        players.forEach(player -> {
            player.sendMessage("§a§lDungeon abgeschlossen!");
            player.sendMessage("§7Du erhältst Belohnungen für das Abschließen!");
        });
        
        // Clean up after 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanup();
            }
        }.runTaskLater(SkyblockPluginRefactored.getInstance(), 600L); // 30 seconds
    }

    private void giveCompletionRewards(Player player) {
        // Give coins
        player.sendMessage("§a+1000 Coins");
        
        // Give experience
        player.sendMessage("§a+500 XP");
        
        // Give items (in a real implementation, you would give actual items)
        player.sendMessage("§a+1 Dungeon Key");
        
        // In a real implementation, you would:
        // - Add coins to player's balance
        // - Add experience to player's profile
        // - Give actual items to player's inventory
        // - Update player statistics
    }

    public void cleanup() {
        // Stop boss if active
        if (currentBoss != null) {
            currentBoss.stopBoss();
        }

        // Teleport all players back to hub
        players.forEach(player -> {
            if (player.isOnline()) {
                World hubWorld = Bukkit.getWorld("hub_a");
                if (hubWorld != null) {
                    if (SkyblockPluginRefactored.getInstance().isFoliaServer()) {
                        player.teleportAsync(hubWorld.getSpawnLocation());
                    } else {
                        player.teleport(hubWorld.getSpawnLocation());
                    }
                }
            }
        });

        // Unload world (if not Folia)
        if (dungeonWorld != null && !SkyblockPluginRefactored.getInstance().isFoliaServer()) {
            Bukkit.unloadWorld(dungeonWorld, false);
        }

        // Clear data
        players.clear();
        dungeonData.clear();
        state = DungeonState.CLEANED_UP;
        
        if (SkyblockPluginRefactored.getInstance().getSettingsConfig().isDebugMode()) {
            SkyblockPluginRefactored.getInstance().getLogger().info("Dungeon " + dungeonId + " cleaned up");
        }
    }

    // Getters
    public String getDungeonId() {
        return dungeonId;
    }

    public String getDungeonType() {
        return dungeonType;
    }

    public int getFloor() {
        return floor;
    }

    public Player getCreator() {
        return creator;
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players);
    }

    public World getDungeonWorld() {
        return dungeonWorld;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public DungeonBoss getCurrentBoss() {
        return currentBoss;
    }

    public DungeonState getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getDuration() {
        if (endTime > 0) {
            return endTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }

    public enum DungeonState {
        CREATING,
        ACTIVE,
        COMPLETED,
        CLEANED_UP
    }
}
