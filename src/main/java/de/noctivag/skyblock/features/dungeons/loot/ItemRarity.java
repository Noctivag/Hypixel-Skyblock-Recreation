package de.noctivag.skyblock.features.dungeons.loot;
import org.bukkit.inventory.ItemStack;

/**
 * Item rarity levels
 */
public enum ItemRarity {
    COMMON("Common", "§f", 1.0),
    UNCOMMON("Uncommon", "§a", 1.2),
    RARE("Rare", "§9", 1.5),
    EPIC("Epic", "§5", 2.0),
    LEGENDARY("Legendary", "§6", 3.0),
    MYTHIC("Mythic", "§d", 5.0),
    DIVINE("Divine", "§b", 10.0);
    
    private final String displayName;
    private final String color;
    private final double multiplier;
    
    ItemRarity(String displayName, String color, double multiplier) {
        this.displayName = displayName;
        this.color = color;
        this.multiplier = multiplier;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    @Override
    public String toString() {
        return color + displayName;
    }
}
