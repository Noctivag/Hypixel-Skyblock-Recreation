package de.noctivag.skyblock.sacks;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents a sack item owned by a player
 */
public class SackItem {
    
    private final String sackType;
    private final UUID ownerId;
    private final int capacity;
    private final int currentAmount;
    private final boolean autoCollectionEnabled;
    
    public SackItem(String sackType, UUID ownerId, int capacity) {
        this.sackType = sackType;
        this.ownerId = ownerId;
        this.capacity = capacity;
        this.currentAmount = 0;
        this.autoCollectionEnabled = true;
    }
    
    public SackItem(String sackType, UUID ownerId, int capacity, int currentAmount, boolean autoCollectionEnabled) {
        this.sackType = sackType;
        this.ownerId = ownerId;
        this.capacity = capacity;
        this.currentAmount = currentAmount;
        this.autoCollectionEnabled = autoCollectionEnabled;
    }
    
    public String getSackType() {
        return sackType;
    }
    
    public UUID getOwnerId() {
        return ownerId;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getCurrentAmount() {
        return currentAmount;
    }
    
    public boolean isAutoCollectionEnabled() {
        return autoCollectionEnabled;
    }
    
    public boolean isFull() {
        return currentAmount >= capacity;
    }
    
    public int getRemainingCapacity() {
        return capacity - currentAmount;
    }
    
    public double getFillPercentage() {
        return (double) currentAmount / capacity;
    }
    
    public void processAutoCollection(Player player) {
        // Process auto collection for this sack
        // This would check for nearby items and collect them
    }
    
    @Override
    public String toString() {
        return "SackItem{" +
                "sackType='" + sackType + '\'' +
                ", ownerId=" + ownerId +
                ", capacity=" + capacity +
                ", currentAmount=" + currentAmount +
                ", autoCollectionEnabled=" + autoCollectionEnabled +
                '}';
    }
}
