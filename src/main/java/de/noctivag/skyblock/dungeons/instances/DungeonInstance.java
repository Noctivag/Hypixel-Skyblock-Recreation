package de.noctivag.skyblock.dungeons.instances;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Dungeon Instance - Represents a dungeon instance
 */
public class DungeonInstance {
    
    private final UUID instanceId;
    private final String dungeonType;
    private final List<UUID> players;
    private final long startTime;
    private boolean active;
    private int currentFloor;
    private int maxFloors;
    
    public DungeonInstance(UUID instanceId, String dungeonType, Player creator) {
        this.instanceId = instanceId;
        this.dungeonType = dungeonType;
        this.players = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
        this.active = true;
        this.currentFloor = 1;
        this.maxFloors = 5; // Default max floors
        
        // Add creator to instance
        this.players.add(creator.getUniqueId());
    }
    
    /**
     * Get the instance ID
     */
    public UUID getInstanceId() {
        return instanceId;
    }
    
    /**
     * Get the dungeon type
     */
    public String getDungeonType() {
        return dungeonType;
    }
    
    /**
     * Get the players in this instance
     */
    public List<UUID> getPlayers() {
        return new ArrayList<>(players);
    }
    
    /**
     * Add a player to this instance
     */
    public boolean addPlayer(Player player) {
        if (players.size() >= 5) { // Max 5 players per instance
            return false;
        }
        
        players.add(player.getUniqueId());
        return true;
    }
    
    /**
     * Remove a player from this instance
     */
    public boolean removePlayer(Player player) {
        return players.remove(player.getUniqueId());
    }
    
    /**
     * Get the start time
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Check if the instance is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set the instance as active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Get the current floor
     */
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    /**
     * Set the current floor
     */
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
    
    /**
     * Get the max floors
     */
    public int getMaxFloors() {
        return maxFloors;
    }
    
    /**
     * Set the max floors
     */
    public void setMaxFloors(int maxFloors) {
        this.maxFloors = maxFloors;
    }
    
    /**
     * Check if the dungeon is completed
     */
    public boolean isCompleted() {
        return currentFloor > maxFloors;
    }
    
    /**
     * Get the elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}

