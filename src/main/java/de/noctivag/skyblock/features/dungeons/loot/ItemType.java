package de.noctivag.skyblock.features.dungeons.loot;
import org.bukkit.inventory.ItemStack;

/**
 * Types of dungeon items
 */
public enum ItemType {
    WEAPON("Weapon", "⚔️"),
    ARMOR("Armor", "🛡️"),
    ACCESSORY("Accessory", "💍"),
    CONSUMABLE("Consumable", "🍯"),
    MATERIAL("Material", "🔧"),
    PET("Pet", "🐾"),
    ENCHANTMENT("Enchantment", "✨");
    
    private final String displayName;
    private final String icon;
    
    ItemType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
