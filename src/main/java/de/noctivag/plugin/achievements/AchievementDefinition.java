package de.noctivag.plugin.achievements;

import org.bukkit.Material;

/**
 * Achievement definition class for GUI compatibility
 */
public class AchievementDefinition {
    private final String id;
    private final String name;
    private final String description;
    private final AchievementCategory category;
    private final AchievementRarity rarity;
    private final int points;
    private final int coinReward;

    public AchievementDefinition(String id, String name, String description,
                               AchievementCategory category, AchievementRarity rarity,
                               int points, int coinReward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
        this.points = points;
        this.coinReward = coinReward;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public AchievementCategory getCategory() {
        return category;
    }

    public AchievementRarity getRarity() {
        return rarity;
    }

    public int getPoints() {
        return points;
    }

    public int getCoinReward() {
        return coinReward;
    }

    // Methods expected by EnhancedAchievementGUI
    public org.bukkit.Material getIcon() {
        return Material.NETHER_STAR; // Default icon
    }

    public String getReward() {
        return coinReward + " Coins";
    }

    public int getTarget() {
        return 1; // Default target
    }
}
