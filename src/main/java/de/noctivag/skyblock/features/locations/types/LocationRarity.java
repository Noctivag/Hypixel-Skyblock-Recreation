package de.noctivag.skyblock.features.locations.types;
import org.bukkit.inventory.ItemStack;

public enum LocationRarity {
    COMMON("Common", "§f", 1.0),
    UNCOMMON("Uncommon", "§a", 1.2),
    RARE("Rare", "§9", 1.5),
    EPIC("Epic", "§5", 2.0),
    LEGENDARY("Legendary", "§6", 3.0),
    MYTHIC("Mythic", "§d", 5.0),
    SECRET("Secret", "§8", 10.0);

    private final String displayName;
    private final String colorCode;
    private final double difficultyMultiplier;

    LocationRarity(String displayName, String colorCode, double difficultyMultiplier) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public String getColoredName() {
        return colorCode + displayName;
    }

    @Override
    public String toString() {
        return getColoredName();
    }
}
