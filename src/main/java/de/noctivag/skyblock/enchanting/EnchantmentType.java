package de.noctivag.skyblock.enchanting;

/**
 * Enchantment types
 */
public enum EnchantmentType {
    COMBAT("Combat", "§c"),
    MINING("Mining", "§6"),
    FARMING("Farming", "§a"),
    FISHING("Fishing", "§9"),
    SPECIAL("Special", "§b");
    
    private final String displayName;
    private final String color;
    
    EnchantmentType(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
}
