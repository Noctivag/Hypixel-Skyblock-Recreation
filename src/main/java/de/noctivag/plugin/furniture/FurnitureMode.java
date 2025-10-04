package de.noctivag.plugin.furniture;
import org.bukkit.inventory.ItemStack;

/**
 * Furniture placement modes
 */
public enum FurnitureMode {
    NORMAL("Normal"),
    PLACE("Place"),
    REMOVE("Remove"),
    EDIT("Edit");
    
    private final String displayName;
    
    FurnitureMode(String displayName) {
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
