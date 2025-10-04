package de.noctivag.plugin.talismans;
import org.bukkit.inventory.ItemStack;

/**
 * Rarities for talismans
 */
public enum TalismanRarity {
    COMMON("Common", "§f"),
    UNCOMMON("Uncommon", "§a"),
    RARE("Rare", "§9"),
    EPIC("Epic", "§5"),
    LEGENDARY("Legendary", "§6"),
    MYTHIC("Mythic", "§c");
    
    private final String displayName;
    private final String color;
    
    TalismanRarity(String displayName, String color) {
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
