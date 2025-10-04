package de.noctivag.skyblock.features.minions.upgrades;
import org.bukkit.inventory.ItemStack;

public enum UpgradeType {
    MINION_FUEL("Minion Fuel", "⛽", "+25% Production Speed"),
    SUPER_COMPACTOR_3000("Super Compactor 3000", "🗜️", "Auto-compacts items"),
    DIAMOND_SPREADING("Diamond Spreading", "💎", "+10% Diamond chance"),
    ENCHANTED_LAVA_BUCKET("Enchanted Lava Bucket", "🪣", "+25% Production Speed"),
    BUDGET_HOPPER("Budget Hopper", "🪣", "Auto-sells items"),
    AUTO_SMELTER("Auto Smelter", "🔥", "Auto-smelts items"),
    MINION_EXPANDER("Minion Expander", "📏", "Increases action radius"),
    COMPACTOR("Compactor", "📦", "Compacts items"),
    MINION_STORAGE("Minion Storage", "📦", "Increases storage capacity"),
    MINION_SKIN("Minion Skin", "🎨", "Changes minion appearance"),
    MINION_FUEL_UPGRADE("Minion Fuel Upgrade", "⛽", "+50% Production Speed"),
    MINION_UPGRADE_STONE("Minion Upgrade Stone", "💎", "Upgrades minion tier"),
    MINION_CATALYST("Minion Catalyst", "⚗️", "+100% Production Speed"),
    MINION_SUPER_FUEL("Minion Super Fuel", "🚀", "+200% Production Speed"),
    MINION_HYPER_FUEL("Minion Hyper Fuel", "🌟", "+500% Production Speed");

    private final String displayName;
    private final String icon;
    private final String description;

    UpgradeType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedName() {
        return icon + " " + displayName;
    }

    @Override
    public String toString() {
        return getFormattedName();
    }
}
