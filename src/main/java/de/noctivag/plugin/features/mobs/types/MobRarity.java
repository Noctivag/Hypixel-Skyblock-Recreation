package de.noctivag.plugin.features.mobs.types;
import org.bukkit.inventory.ItemStack;

public enum MobRarity {
    COMMON("Common", "§f"),
    UNCOMMON("Uncommon", "§a"),
    RARE("Rare", "§9"),
    EPIC("Epic", "§5"),
    LEGENDARY("Legendary", "§6"),
    MYTHIC("Mythic", "§d"),
    SPECIAL("Special", "§c");

    private final String displayName;
    private final String colorCode;

    MobRarity(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColoredName() {
        return colorCode + displayName;
    }

    @Override
    public String toString() {
        return getColoredName();
    }
}
