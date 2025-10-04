package de.noctivag.plugin.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.dungeons.classes.DungeonClassType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

/**
 * Represents an active dungeon instance
 */
public class DungeonInstance {
    
    private final UUID instanceId;
    private final List<UUID> players;
    private final int floor;
    private final DungeonClassType classType;
    private final FloorConfig floorConfig;
    
    private final List<DungeonRoom> rooms = new ArrayList<>();
    private final Map<UUID, Location> playerSpawns = new HashMap<>();
    
    private DungeonState state = DungeonState.WAITING;
    private long startTime;
    private long endTime;
    private int score = 0;
    
    public DungeonInstance(UUID instanceId, List<UUID> players, int floor, DungeonClassType classType, FloorConfig floorConfig) {
        this.instanceId = instanceId;
        this.players = new ArrayList<>(players);
        this.floor = floor;
        this.classType = classType;
        this.floorConfig = floorConfig;
    }
    
    /**
     * Generate dungeon layout
     */
    public void generateLayout() {
        // Generate rooms based on floor
        generateRooms();
        
        // Set player spawn points
        setPlayerSpawns();
    }
    
    /**
     * Start the dungeon
     */
    public void start() {
        this.state = DungeonState.ACTIVE;
        this.startTime = System.currentTimeMillis();
        
        // Teleport players to dungeon
        teleportPlayers();
        
        // Start dungeon timer
        startTimer();
    }
    
    /**
     * Complete the dungeon
     */
    public void complete(boolean success) {
        this.state = success ? DungeonState.COMPLETED : DungeonState.FAILED;
        this.endTime = System.currentTimeMillis();
    }
    
    /**
     * Add score to the dungeon
     */
    public void addScore(int points) {
        this.score += points;
    }
    
    /**
     * Get elapsed time in seconds
     */
    public long getElapsedTime() {
        if (startTime == 0) return 0;
        long end = endTime == 0 ? System.currentTimeMillis() : endTime;
        return (end - startTime) / 1000;
    }
    
    /**
     * Check if dungeon is still within time limit
     */
    public boolean isWithinTimeLimit() {
        return getElapsedTime() < floorConfig.getTimeLimit();
    }
    
    /**
     * Generate rooms for the dungeon
     */
    private void generateRooms() {
        // Generate entry room
        rooms.add(new DungeonRoom("Entry", RoomType.ENTRY));
        
        // Generate puzzle rooms
        int puzzleRooms = Math.min(2, floor);
        for (int i = 0; i < puzzleRooms; i++) {
            rooms.add(new DungeonRoom("Puzzle " + (i + 1), RoomType.PUZZLE));
        }
        
        // Generate combat rooms
        int combatRooms = Math.min(4, floor + 1);
        for (int i = 0; i < combatRooms; i++) {
            rooms.add(new DungeonRoom("Combat " + (i + 1), RoomType.COMBAT));
        }
        
        // Generate boss room
        rooms.add(new DungeonRoom("Boss", RoomType.BOSS));
        
        // Generate exit room
        rooms.add(new DungeonRoom("Exit", RoomType.EXIT));
    }
    
    /**
     * Set player spawn points
     */
    private void setPlayerSpawns() {
        // Get or create dungeon world
        World dungeonWorld = getDungeonWorld();
        
        // Set spawn points for each player
        for (int i = 0; i < players.size(); i++) {
            UUID playerId = players.get(i);
            Location spawn = new Location(dungeonWorld, i * 5, 100, 0);
            playerSpawns.put(playerId, spawn);
        }
    }
    
    /**
     * Teleport players to dungeon
     */
    private void teleportPlayers() {
        for (UUID playerId : players) {
            org.bukkit.entity.Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                Location spawn = playerSpawns.get(playerId);
                if (spawn != null) {
                    player.teleport(spawn);
                    player.sendMessage("§6Entering Catacombs Floor " + floor + "...");
                }
            }
        }
    }
    
    /**
     * Start dungeon timer
     */
    private void startTimer() {
        // TODO: Implement timer that checks time limit and updates score
    }
    
    /**
     * Get dungeon world
     */
    private World getDungeonWorld() {
        // TODO: Get or create dedicated dungeon world
        return Bukkit.getWorlds().get(0);
    }
    
    // Getters
    public UUID getInstanceId() {
        return instanceId;
    }
    
    public List<UUID> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public int getFloor() {
        return floor;
    }
    
    public DungeonClassType getClassType() {
        return classType;
    }
    
    public FloorConfig getFloorConfig() {
        return floorConfig;
    }
    
    public List<DungeonRoom> getRooms() {
        return new ArrayList<>(rooms);
    }
    
    public DungeonState getState() {
        return state;
    }
    
    public int getScore() {
        return score;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
}
