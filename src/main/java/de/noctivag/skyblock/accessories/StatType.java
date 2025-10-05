package de.noctivag.skyblock.accessories;

/**
 * Stat Type - Represents stat types
 */
public enum StatType {
    
    HEALTH("Health", "§c"),
    DEFENSE("Defense", "§a"),
    STRENGTH("Strength", "§c"),
    SPEED("Speed", "§f"),
    CRITICAL_CHANCE("Critical Chance", "§9"),
    CRITICAL_DAMAGE("Critical Damage", "§9"),
    INTELLIGENCE("Intelligence", "§b"),
    MINING_SPEED("Mining Speed", "§6"),
    MINING_FORTUNE("Mining Fortune", "§6"),
    FARMING_FORTUNE("Farming Fortune", "§a"),
    FORAGING_FORTUNE("Foraging Fortune", "§a"),
    FISHING_SPEED("Fishing Speed", "§b"),
    FISHING_LUCK("Fishing Luck", "§b"),
    MAGIC_FIND("Magic Find", "§d"),
    PET_LUCK("Pet Luck", "§d");
    
    private final String displayName;
    private final String colorCode;
    
    StatType(String displayName, String colorCode) {
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
