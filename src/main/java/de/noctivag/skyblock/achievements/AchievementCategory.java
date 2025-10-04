package de.noctivag.skyblock.achievements;

/**
 * Achievement categories
 */
public enum AchievementCategory {
    COMBAT("Combat", "§c"),
    MINING("Mining", "§6"),
    FORAGING("Foraging", "§a"),
    FISHING("Fishing", "§9"),
    FARMING("Farming", "§2"),
    ENCHANTING("Enchanting", "§d"),
    ALCHEMY("Alchemy", "§5"),
    TAMING("Taming", "§e"),
    SPECIAL("Special", "§b"),
    GENERAL("General", "§f"),
    EXPLORATION("Exploration", "§3"),
    PVP("PvP", "§4");
    
    private final String displayName;
    private final String color;
    
    AchievementCategory(String displayName, String color) {
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
