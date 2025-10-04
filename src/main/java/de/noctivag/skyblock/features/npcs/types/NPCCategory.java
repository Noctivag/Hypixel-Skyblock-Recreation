package de.noctivag.skyblock.features.npcs.types;
import org.bukkit.inventory.ItemStack;

public enum NPCCategory {
    HUB("Hub", "🏠"),
    SHOP("Shop", "🛒"),
    QUEST("Quest", "📚"),
    INFO("Info", "ℹ️"),
    WARP("Warp", "🌍"),
    BANK("Bank", "🏦"),
    AUCTION("Auction", "🏪"),
    GUILD("Guild", "🏰"),
    PET("Pet", "🐾"),
    COSMETIC("Cosmetic", "✨"),
    ADMIN("Admin", "👑"),
    SPECIAL("Special", "🌟"),
    EVENT("Event", "🎪");

    private final String displayName;
    private final String icon;

    NPCCategory(String displayName, String icon) {
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
