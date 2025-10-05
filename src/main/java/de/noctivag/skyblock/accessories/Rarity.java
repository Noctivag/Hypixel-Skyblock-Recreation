package de.noctivag.skyblock.accessories;

/**
 * Rarity - Represents item rarity
 */
public enum Rarity {
    
    COMMON("§fCommon", "§7"),
    UNCOMMON("§aUncommon", "§a"),
    RARE("§9Rare", "§9"),
    EPIC("§5Epic", "§5"),
    LEGENDARY("§6Legendary", "§6"),
    MYTHIC("§dMythic", "§d"),
    DIVINE("§bDivine", "§b"),
    SPECIAL("§cSpecial", "§c"),
    VERY_SPECIAL("§cVery Special", "§c");
    
    private final String displayName;
    private final String colorCode;
    
    Rarity(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }
    
    /**
     * Get the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get the color code
     */
    public String getColorCode() {
        return colorCode;
    }
}
