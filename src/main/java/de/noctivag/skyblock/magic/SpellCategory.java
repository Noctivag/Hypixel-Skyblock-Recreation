package de.noctivag.skyblock.magic;
import org.bukkit.inventory.ItemStack;

/**
 * Categories for spells
 */
public enum SpellCategory {
    COMBAT("Combat"),
    HEALING("Healing"),
    UTILITY("Utility"),
    PROTECTION("Protection"),
    SPECIAL("Special");
    
    private final String displayName;
    
    SpellCategory(String displayName) {
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
