package de.noctivag.plugin.mining;
import org.bukkit.inventory.ItemStack;

/**
 * Types of mining upgrades
 */
public enum MiningUpgradeType {
    MINING_SPEED("Mining Speed"),
    MINING_FORTUNE("Mining Fortune"),
    GEMSTONE_MINING("Gemstone Mining"),
    MITHRIL_MINING("Mithril Mining"),
    TITANIUM_MINING("Titanium Mining"),
    MINING_XP("Mining XP");
    
    private final String displayName;
    
    MiningUpgradeType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
