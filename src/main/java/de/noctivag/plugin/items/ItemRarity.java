package de.noctivag.plugin.items;

/**
 * Item rarity levels
 */
public enum ItemRarity {
    COMMON("Common", "§f"),
    UNCOMMON("Uncommon", "§a"),
    RARE("Rare", "§9"),
    EPIC("Epic", "§5"),
    LEGENDARY("Legendary", "§6"),
    MYTHIC("Mythic", "§d"),
    DIVINE("Divine", "§b"),
    SPECIAL("Special", "§c"),
    VERY_SPECIAL("Very Special", "§c");
    
    private final String displayName;
    private final String color;
    
    ItemRarity(String displayName, String color) {
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