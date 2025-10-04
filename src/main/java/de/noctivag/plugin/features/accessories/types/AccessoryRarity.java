package de.noctivag.plugin.features.accessories.types;
import org.bukkit.inventory.ItemStack;

/**
 * Rarities of accessories
 */
public enum AccessoryRarity {
    COMMON("Common", "⚪", 1),
    UNCOMMON("Uncommon", "🟢", 2),
    RARE("Rare", "🔵", 3),
    EPIC("Epic", "🟣", 4),
    LEGENDARY("Legendary", "🟡", 5),
    MYTHIC("Mythic", "🔴", 6),
    SPECIAL("Special", "✨", 10);
    
    private final String displayName;
    private final String icon;
    private final int magicalPowerMultiplier;
    
    AccessoryRarity(String displayName, String icon, int magicalPowerMultiplier) {
        this.displayName = displayName;
        this.icon = icon;
        this.magicalPowerMultiplier = magicalPowerMultiplier;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public int getMagicalPowerMultiplier() {
        return magicalPowerMultiplier;
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
