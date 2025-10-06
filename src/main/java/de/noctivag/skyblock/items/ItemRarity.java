package de.noctivag.skyblock.items;

/**
 * ItemRarity - Enum for item rarities
 */
public enum ItemRarity {
    COMMON("Common", "&7", "●"),
    UNCOMMON("Uncommon", "&a", "●"),
    RARE("Rare", "&9", "●"),
    EPIC("Epic", "&5", "●"),
    LEGENDARY("Legendary", "&6", "●"),
    MYTHIC("Mythic", "&d", "●"),
    DIVINE("Divine", "&b", "●"),
    SPECIAL("Special", "&c", "●"),
    VERY_SPECIAL("Very Special", "&c", "●");

    private final String displayName;
    private final String color;
    private final String symbol;

    ItemRarity(String displayName, String color, String symbol) {
        this.displayName = displayName;
        this.color = color;
        this.symbol = symbol;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
    public String getSymbol() { return symbol; }
    public double getMultiplier() { 
        switch (this) {
            case COMMON: return 1.0;
            case UNCOMMON: return 1.1;
            case RARE: return 1.2;
            case EPIC: return 1.3;
            case LEGENDARY: return 1.5;
            case MYTHIC: return 1.7;
            case DIVINE: return 2.0;
            case SPECIAL: return 1.4;
            case VERY_SPECIAL: return 1.6;
            default: return 1.0;
        }
    }
}
