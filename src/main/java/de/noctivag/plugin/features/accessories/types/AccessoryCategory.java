package de.noctivag.plugin.features.accessories.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of accessories
 */
public enum AccessoryCategory {
    COMMON_TALISMANS("Common Talismans", "⚪", "Basic talismans for beginners"),
    UNCOMMON_TALISMANS("Uncommon Talismans", "🟢", "Improved talismans"),
    RARE_TALISMANS("Rare Talismans", "🔵", "Advanced talismans"),
    EPIC_TALISMANS("Epic Talismans", "🟣", "High-tier talismans"),
    LEGENDARY_TALISMANS("Legendary Talismans", "🟡", "Elite talismans"),
    MYTHIC_TALISMANS("Mythic Talismans", "🔴", "Ultimate talismans"),
    SPECIAL_ACCESSORIES("Special Accessories", "✨", "Unique utility accessories");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    AccessoryCategory(String displayName, String icon, String description) {
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
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
