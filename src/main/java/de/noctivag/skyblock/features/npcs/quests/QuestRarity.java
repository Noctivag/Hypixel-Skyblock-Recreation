package de.noctivag.skyblock.features.npcs.quests;

public enum QuestRarity {
    COMMON("Common", "§f", 1.0),
    UNCOMMON("Uncommon", "§a", 1.5),
    RARE("Rare", "§9", 2.0),
    EPIC("Epic", "§5", 3.0),
    LEGENDARY("Legendary", "§6", 5.0),
    MYTHIC("Mythic", "§d", 10.0);

    private final String displayName;
    private final String colorCode;
    private final double rewardMultiplier;

    QuestRarity(String displayName, String colorCode, double rewardMultiplier) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getColoredName() {
        return colorCode + displayName;
    }

    @Override
    public String toString() {
        return getColoredName();
    }
}
