package de.noctivag.plugin.experiments;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for experiments
 */
public enum ExperimentCategory {
    MINING("Mining"),
    FARMING("Farming"),
    COMBAT("Combat"),
    FISHING("Fishing"),
    FORAGING("Foraging"),
    SPECIAL("Special");
    
    private final String displayName;
    
    ExperimentCategory(String displayName) {
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
