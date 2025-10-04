package de.noctivag.plugin.gemstones;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for gemstones
 */
public enum GemstoneCategory {
    COMBAT("Combat"),
    MINING("Mining"),
    FARMING("Farming"),
    FISHING("Fishing"),
    FORAGING("Foraging"),
    SPECIAL("Special");
    
    private final String displayName;
    
    GemstoneCategory(String displayName) {
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
