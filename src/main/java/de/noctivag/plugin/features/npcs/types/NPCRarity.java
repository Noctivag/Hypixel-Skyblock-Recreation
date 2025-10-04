package de.noctivag.plugin.features.npcs.types;
import org.bukkit.inventory.ItemStack;

public enum NPCRarity {
    COMMON("Common", "§f", 1.0),
    UNCOMMON("Uncommon", "§a", 1.2),
    RARE("Rare", "§9", 1.5),
    EPIC("Epic", "§5", 2.0),
    LEGENDARY("Legendary", "§6", 3.0),
    MYTHIC("Mythic", "§d", 5.0),
    SPECIAL("Special", "§c", 10.0);

    private final String displayName;
    private final String colorCode;
    private final double importanceMultiplier;

    NPCRarity(String displayName, String colorCode, double importanceMultiplier) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.importanceMultiplier = importanceMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public double getImportanceMultiplier() {
        return importanceMultiplier;
    }

    public String getColoredName() {
        return colorCode + displayName;
    }

    @Override
    public String toString() {
        return getColoredName();
    }
}
