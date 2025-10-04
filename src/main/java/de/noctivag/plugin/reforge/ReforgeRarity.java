package de.noctivag.plugin.reforge;
import org.bukkit.inventory.ItemStack;

/**
 * Rarities for reforge stones and types
 */
public enum ReforgeRarity {
    COMMON("Common", "§f"),
    UNCOMMON("Uncommon", "§a"),
    RARE("Rare", "§9"),
    EPIC("Epic", "§5"),
    LEGENDARY("Legendary", "§6"),
    MYTHIC("Mythic", "§c");
    
    private final String displayName;
    private final String color;
    
    ReforgeRarity(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        return color + displayName;
    }
}
