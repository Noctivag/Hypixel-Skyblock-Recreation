package de.noctivag.plugin.runes;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for different types of runes
 */
public enum RuneCategory {
    COMBAT("Combat"),
    MINING("Mining"),
    FARMING("Farming"),
    FISHING("Fishing"),
    FORAGING("Foraging"),
    SPECIAL("Special");
    
    private final String displayName;
    
    RuneCategory(String displayName) {
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
