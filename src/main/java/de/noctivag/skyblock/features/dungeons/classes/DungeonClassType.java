package de.noctivag.skyblock.features.dungeons.classes;
import org.bukkit.inventory.ItemStack;

/**
 * Enum representing different dungeon class types
 */
public enum DungeonClassType {
    BERSERKER("Berserker", "⚔️", "Melee damage dealer with high health and defense"),
    MAGE("Mage", "🔮", "Magic damage dealer with powerful spells"),
    ARCHER("Archer", "🏹", "Ranged damage dealer with high critical hit chance"),
    TANK("Tank", "🛡️", "Defensive class that protects allies"),
    HEALER("Healer", "❤️", "Support class that heals and buffs allies");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    DungeonClassType(String displayName, String icon, String description) {
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
