package de.noctivag.skyblock.features.locations.types;
import org.bukkit.inventory.ItemStack;

public enum LocationCategory {
    HUB("Hub", "🏠"),
    PRIVATE_ISLAND("Private Island", "🏝️"),
    DUNGEON("Dungeon", "🏰"),
    SPECIAL("Special", "✨"),
    MINING("Mining", "⛏️"),
    COMBAT("Combat", "⚔️"),
    FARMING("Farming", "🌾"),
    FISHING("Fishing", "🎣"),
    FORAGING("Foraging", "🌳"),
    EVENT("Event", "🎪"),
    BOSS("Boss", "👑"),
    SECRET("Secret", "🔍"),
    PORTAL("Portal", "🌀");

    private final String displayName;
    private final String icon;

    LocationCategory(String displayName, String icon) {
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
