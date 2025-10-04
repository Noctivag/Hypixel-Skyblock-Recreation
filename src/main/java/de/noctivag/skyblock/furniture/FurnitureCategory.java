package de.noctivag.skyblock.furniture;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for furniture
 */
public enum FurnitureCategory {
    CHAIR("Chair"),
    TABLE("Table"),
    BED("Bed"),
    STORAGE("Storage"),
    DECORATIVE("Decorative"),
    SPECIAL("Special");
    
    private final String displayName;
    
    FurnitureCategory(String displayName) {
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
