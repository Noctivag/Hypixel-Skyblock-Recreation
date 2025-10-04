package de.noctivag.skyblock.accessories;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for accessories
 */
public enum AccessoryCategory {
    RING("Ring"),
    NECKLACE("Necklace"),
    BRACELET("Bracelet"),
    SPECIAL("Special");
    
    private final String displayName;
    
    AccessoryCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
