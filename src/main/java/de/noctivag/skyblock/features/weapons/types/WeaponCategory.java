package de.noctivag.skyblock.features.weapons.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of weapons
 */
public enum WeaponCategory {
    WITHER_BLADE("Wither Blade", "⚡", "Ultimate dungeon weapons from Floor 7"),
    DUNGEON("Dungeon", "🏰", "Weapons obtained from dungeons"),
    DRAGON("Dragon", "🐉", "Dragon-themed weapons"),
    SPECIAL("Special", "✨", "Special and unique weapons"),
    TOOL("Tool", "🛠️", "Specialized tools for gathering"),
    BOW("Bow", "🏹", "Ranged weapons");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    WeaponCategory(String displayName, String icon, String description) {
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
