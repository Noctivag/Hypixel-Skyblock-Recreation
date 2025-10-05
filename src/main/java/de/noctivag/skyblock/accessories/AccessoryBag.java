package de.noctivag.skyblock.accessories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Accessory Bag - Represents a player's accessory bag
 */
public class AccessoryBag {
    
    private final UUID ownerId;
    private final List<Accessory> accessories;
    private final int maxSlots;
    
    public AccessoryBag(UUID ownerId, int maxSlots) {
        this.ownerId = ownerId;
        this.accessories = new ArrayList<>();
        this.maxSlots = maxSlots;
    }
    
    /**
     * Get the owner ID
     */
    public UUID getOwnerId() {
        return ownerId;
    }
    
    /**
     * Get all accessories
     */
    public List<Accessory> getAccessories() {
        return new ArrayList<>(accessories);
    }
    
    /**
     * Get the max slots
     */
    public int getMaxSlots() {
        return maxSlots;
    }
    
    /**
     * Add an accessory
     */
    public boolean addAccessory(Accessory accessory) {
        if (accessories.size() < maxSlots) {
            accessories.add(accessory);
            return true;
        }
        return false;
    }
    
    /**
     * Remove an accessory
     */
    public boolean removeAccessory(Accessory accessory) {
        return accessories.remove(accessory);
    }
    
    /**
     * Get an accessory by slot
     */
    public Accessory getAccessory(int slot) {
        if (slot >= 0 && slot < accessories.size()) {
            return accessories.get(slot);
        }
        return null;
    }
    
    /**
     * Get the number of accessories
     */
    public int getAccessoryCount() {
        return accessories.size();
    }
    
    /**
     * Check if the bag is full
     */
    public boolean isFull() {
        return accessories.size() >= maxSlots;
    }
    
    /**
     * Check if the bag is empty
     */
    public boolean isEmpty() {
        return accessories.isEmpty();
    }
}
