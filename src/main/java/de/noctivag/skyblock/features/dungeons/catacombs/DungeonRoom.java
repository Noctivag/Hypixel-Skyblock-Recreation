package de.noctivag.skyblock.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room in a dungeon instance
 */
public class DungeonRoom {
    
    private final String name;
    private final RoomType type;
    private final List<Location> mobSpawns = new ArrayList<>();
    private final List<Location> chestSpawns = new ArrayList<>();
    
    private boolean completed = false;
    private boolean active = false;
    
    public DungeonRoom(String name, RoomType type) {
        this.name = name;
        this.type = type;
    }
    
    /**
     * Activate the room
     */
    public void activate() {
        this.active = true;
        spawnMobs();
    }
    
    /**
     * Complete the room
     */
    public void complete() {
        this.completed = true;
        this.active = false;
        spawnChests();
    }
    
    /**
     * Spawn mobs in the room
     */
    private void spawnMobs() {
        // TODO: Implement mob spawning based on room type and floor
    }
    
    /**
     * Spawn chests in the room
     */
    private void spawnChests() {
        // TODO: Implement chest spawning with rewards
    }
    
    /**
     * Add mob spawn location
     */
    public void addMobSpawn(Location location) {
        mobSpawns.add(location);
    }
    
    /**
     * Add chest spawn location
     */
    public void addChestSpawn(Location location) {
        chestSpawns.add(location);
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public RoomType getType() {
        return type;
    }
    
    public List<Location> getMobSpawns() {
        return new ArrayList<>(mobSpawns);
    }
    
    public List<Location> getChestSpawns() {
        return new ArrayList<>(chestSpawns);
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public boolean isActive() {
        return active;
    }
}
