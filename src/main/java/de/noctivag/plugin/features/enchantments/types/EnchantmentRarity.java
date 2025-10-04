package de.noctivag.plugin.features.enchantments.types;
import org.bukkit.inventory.ItemStack;

/**
 * Rarities of enchantments
 */
public enum EnchantmentRarity {
    COMMON("Common", "⚪", 1),
    UNCOMMON("Uncommon", "🟢", 2),
    RARE("Rare", "🔵", 3),
    EPIC("Epic", "🟣", 4),
    LEGENDARY("Legendary", "🟡", 5),
    MYTHIC("Mythic", "🔴", 6);
    
    private final String displayName;
    private final String icon;
    private final int costMultiplier;
    
    EnchantmentRarity(String displayName, String icon, int costMultiplier) {
        this.displayName = displayName;
        this.icon = icon;
        this.costMultiplier = costMultiplier;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public int getCostMultiplier() {
        return costMultiplier;
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
