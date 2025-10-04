package de.noctivag.plugin.items;

/**
 * Potato book types
 */
public enum PotatoBookType {
    COMBAT("Combat", "§c"),
    MINING("Mining", "§6"),
    FARMING("Farming", "§a"),
    FORAGING("Foraging", "§2"),
    FISHING("Fishing", "§9"),
    ENCHANTING("Enchanting", "§d"),
    ALCHEMY("Alchemy", "§5"),
    TAMING("Taming", "§e");
    
    private final String displayName;
    private final String color;
    
    PotatoBookType(String displayName, String color) {
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
