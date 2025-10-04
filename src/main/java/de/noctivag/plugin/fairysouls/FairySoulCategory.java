package de.noctivag.plugin.fairysouls;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for fairy souls
 */
public enum FairySoulCategory {
    HUB("Hub"),
    MINING("Mining"),
    FARMING("Farming"),
    FISHING("Fishing"),
    DUNGEON("Dungeon"),
    SPECIAL("Special");
    
    private final String displayName;
    
    FairySoulCategory(String displayName) {
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
