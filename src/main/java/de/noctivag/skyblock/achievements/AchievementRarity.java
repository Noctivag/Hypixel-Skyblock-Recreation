package de.noctivag.skyblock.achievements;

/**
 * Achievement rarity levels
 */
public enum AchievementRarity {
    COMMON("Common", "§f"),
    UNCOMMON("Uncommon", "§a"),
    RARE("Rare", "§9"),
    EPIC("Epic", "§5"),
    LEGENDARY("Legendary", "§6"),
    MYTHIC("Mythic", "§d");
    
    private final String displayName;
    private final String color;
    
    AchievementRarity(String displayName, String color) {
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
