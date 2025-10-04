package de.noctivag.plugin.furniture;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Represents a placed furniture item
 */
public class FurnitureItem {
    
    private final FurnitureType furnitureType;
    private final Location location;
    private final UUID ownerId;
    private final long placedTime;
    
    public FurnitureItem(FurnitureType furnitureType, Location location, UUID ownerId) {
        this.furnitureType = furnitureType;
        this.location = location;
        this.ownerId = ownerId;
        this.placedTime = System.currentTimeMillis();
    }
    
    public FurnitureType getFurnitureType() {
        return furnitureType;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public UUID getOwnerId() {
        return ownerId;
    }
    
    public long getPlacedTime() {
        return placedTime;
    }
    
    public boolean isValid() {
        // Check if the furniture is still in the world
        return location.getBlock().getType() == furnitureType.getMaterial();
    }
    
    @Override
    public String toString() {
        return "FurnitureItem{" +
                "furnitureType=" + furnitureType.getName() +
                ", location=" + location +
                ", ownerId=" + ownerId +
                ", placedTime=" + placedTime +
                '}';
    }
}
