package de.noctivag.skyblock.features.npcs.quests;
import org.bukkit.inventory.ItemStack;

public enum QuestStatus {
    AVAILABLE("Available", "§a"),
    ACTIVE("Active", "§e"),
    COMPLETED("Completed", "§2"),
    FAILED("Failed", "§c"),
    ABANDONED("Abandoned", "§7");

    private final String displayName;
    private final String colorCode;

    QuestStatus(String displayName, String colorCode) {
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
