package de.noctivag.skyblock.minions;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

/**
 * Minion - Represents a minion
 */
public class Minion extends BaseMinion {
    private final MinionType minionType;
    private long lastActionTime;
    private int storedItems;

    public Minion(UUID minionId, UUID ownerId, MinionType minionType, Location location) {
        super(minionId.toString(), ownerId, minionType.name(), minionType.getDisplayName(), minionType.getMaterial(), 1, true, location);
        this.minionType = minionType;
        this.lastActionTime = System.currentTimeMillis();
        this.storedItems = 0;
    }
    
    /**
     * Get the minion ID
     */
    @Override
    public String getMinionId() {
        return minionId;
    }
    
    /**
     * Get the owner ID
     */
    @Override
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
    @Override
    public Location getLocation() {
        return location;
    }
    
    /**
     * Get the level
     */
    @Override
    public int getLevel() {
        return level;
    }
    
    /**
     * Set the level
     */
    @Override
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
    @Override
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set the minion as active
     */
    @Override
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