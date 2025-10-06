package de.noctivag.skyblock.minions;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

/**
 * Minion - Represents a minion
 */
public class Minion {
    
    private final UUID minionId;
    private final UUID ownerId;
    private final MinionType minionType;
    private final Location location;
    private int level;
    private long lastActionTime;
    private int storedItems;
    private boolean active;
    
    public Minion(UUID minionId, UUID ownerId, MinionType minionType, Location location) {
        this.minionId = minionId;
        this.ownerId = ownerId;
        this.minionType = minionType;
        this.location = location;
        this.level = 1;
        this.lastActionTime = System.currentTimeMillis();
        this.storedItems = 0;
        this.active = true;
    }
    
    /**
     * Get the minion ID
     */
    public UUID getMinionId() {
        return minionId;
    }
    
    /**
     * Get the owner ID
     */
    public UUID getOwnerId() {
        return ownerId;
    }
    
    /**
     * Get the minion type
     */
    public MinionType getMinionType() {
        return minionType;
    }
    
    /**
     * Get the location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Get the level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Set the level
     */
    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(level, 100));
    }
    
    /**
     * Get the last action time
     */
    public long getLastActionTime() {
        return lastActionTime;
    }
    
    /**
     * Set the last action time
     */
    public void setLastActionTime(long lastActionTime) {
        this.lastActionTime = lastActionTime;
    }
    
    /**
     * Get the stored items count
     */
    public int getStoredItems() {
        return storedItems;
    }
    
    /**
     * Set the stored items count
     */
    public void setStoredItems(int storedItems) {
        this.storedItems = Math.max(0, storedItems);
    }
    
    /**
     * Add items to storage
     */
    public void addStoredItems(int amount) {
        this.storedItems += amount;
    }
    
    /**
     * Remove items from storage
     */
    public void removeStoredItems(int amount) {
        this.storedItems = Math.max(0, this.storedItems - amount);
    }
    
    /**
     * Check if the minion is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set the minion as active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Get the action interval in milliseconds
     */
    public long getActionInterval() {
        return 2000L - (level * 100); // Decrease by 100ms per level
    }
    
    /**
     * Get the storage capacity
     */
    public int getStorageCapacity() {
        return 160 + (level * 10); // Increase by 10 per level
    }
    
    /**
     * Check if the minion can perform an action
     */
    public boolean canPerformAction() {
        return System.currentTimeMillis() - lastActionTime >= getActionInterval();
    }
    
    /**
     * Check if the minion storage is full
     */
    public boolean isStorageFull() {
        return storedItems >= getStorageCapacity();
    }
}