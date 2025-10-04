package de.noctivag.plugin.dungeons;

import org.bukkit.World;

import java.util.UUID;

/**
 * Dungeon Instance - Represents an active dungeon instance
 */
public class DungeonInstance {
    
    private final UUID instanceId;
    private final DungeonParty party;
    private final DungeonFloor floor;
    private final long startTime;
    private World world;
    private boolean active;
    private int score;
    private int secretsFound;
    private int deaths;
    
    public DungeonInstance(UUID instanceId, DungeonParty party, DungeonFloor floor) {
        this.instanceId = instanceId;
        this.party = party;
        this.floor = floor;
        this.startTime = System.currentTimeMillis();
        this.active = true;
        this.score = 0;
        this.secretsFound = 0;
        this.deaths = 0;
    }
    
    /**
     * Get instance ID
     */
    public UUID getInstanceId() {
        return instanceId;
    }
    
    /**
     * Get party
     */
    public DungeonParty getParty() {
        return party;
    }
    
    /**
     * Get floor
     */
    public DungeonFloor getFloor() {
        return floor;
    }
    
    /**
     * Get start time
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Get world
     */
    public World getWorld() {
        return world;
    }
    
    /**
     * Set world
     */
    public void setWorld(World world) {
        this.world = world;
    }
    
    /**
     * Check if instance is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set active status
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Get score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Add score
     */
    public void addScore(int points) {
        this.score += points;
    }
    
    /**
     * Get secrets found
     */
    public int getSecretsFound() {
        return secretsFound;
    }
    
    /**
     * Add secret found
     */
    public void addSecretFound() {
        this.secretsFound++;
    }
    
    /**
     * Get deaths
     */
    public int getDeaths() {
        return deaths;
    }
    
    /**
     * Add death
     */
    public void addDeath() {
        this.deaths++;
    }
    
    /**
     * Get elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Get elapsed time in seconds
     */
    public long getElapsedTimeSeconds() {
        return getElapsedTime() / 1000;
    }
}
