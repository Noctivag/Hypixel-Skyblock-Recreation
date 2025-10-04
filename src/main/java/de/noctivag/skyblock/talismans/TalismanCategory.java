package de.noctivag.skyblock.talismans;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for talismans
 */
public enum TalismanCategory {
    COMBAT("Combat"),
    SPECIAL("Special");
    
    private final String displayName;
    
    TalismanCategory(String displayName) {
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
