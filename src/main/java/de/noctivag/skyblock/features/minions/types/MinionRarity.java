package de.noctivag.skyblock.features.minions.types;
import org.bukkit.inventory.ItemStack;

public enum MinionRarity {
    COMMON("Common", "§f", 1.0),
    UNCOMMON("Uncommon", "§a", 1.2),
    RARE("Rare", "§9", 1.5),
    EPIC("Epic", "§5", 2.0),
    LEGENDARY("Legendary", "§6", 3.0),
    MYTHIC("Mythic", "§d", 5.0);

    private final String displayName;
    private final String colorCode;
    private final double efficiencyMultiplier;

    MinionRarity(String displayName, String colorCode, double efficiencyMultiplier) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.efficiencyMultiplier = efficiencyMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public double getEfficiencyMultiplier() {
        return efficiencyMultiplier;
    }

    public String getColoredName() {
        return colorCode + displayName;
    }

    @Override
    public String toString() {
        return getColoredName();
    }
}
