package de.noctivag.skyblock.features.minions.types;
import org.bukkit.inventory.ItemStack;

public enum MinionCategory {
    MINING("Mining", "⛏️"),
    FARMING("Farming", "🌾"),
    COMBAT("Combat", "⚔️"),
    FORAGING("Foraging", "🌳"),
    FISHING("Fishing", "🎣"),
    SPECIAL("Special", "✨");

    private final String displayName;
    private final String icon;

    MinionCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
