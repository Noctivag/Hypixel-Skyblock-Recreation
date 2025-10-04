package de.noctivag.plugin.features.enchantments.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of enchantments
 */
public enum EnchantmentCategory {
    SWORD_ENCHANTS("Sword Enchantments", "⚔️", "Enchantments for swords"),
    BOW_ENCHANTS("Bow Enchantments", "🏹", "Enchantments for bows"),
    ARMOR_ENCHANTS("Armor Enchantments", "🛡️", "Enchantments for armor"),
    TOOL_ENCHANTS("Tool Enchantments", "🔧", "Enchantments for tools"),
    ULTIMATE_ENCHANTS("Ultimate Enchantments", "✨", "Ultimate tier enchantments");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    EnchantmentCategory(String displayName, String icon, String description) {
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
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
