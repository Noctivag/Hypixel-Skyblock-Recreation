package de.noctivag.plugin.features.armor.types;
import org.bukkit.inventory.ItemStack;

/**
 * Armor slots
 */
public enum ArmorSlot {
    HELMET("Helmet", "⛑️", "Head protection"),
    CHESTPLATE("Chestplate", "🛡️", "Body protection"),
    LEGGINGS("Leggings", "👖", "Leg protection"),
    BOOTS("Boots", "👢", "Foot protection");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    ArmorSlot(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get armor slot by index (0-3)
     */
    public static ArmorSlot getByIndex(int index) {
        ArmorSlot[] slots = values();
        if (index >= 0 && index < slots.length) {
            return slots[index];
        }
        throw new IllegalArgumentException("Invalid armor slot index: " + index);
    }
    
    /**
     * Get armor slot index (0-3)
     */
    public int getIndex() {
        return ordinal();
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
