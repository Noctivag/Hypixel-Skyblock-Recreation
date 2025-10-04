package de.noctivag.plugin.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the state of a dungeon instance
 */
public enum DungeonState {
    WAITING("Waiting", "⏳"),
    ACTIVE("Active", "⚡"),
    COMPLETED("Completed", "✅"),
    FAILED("Failed", "❌"),
    CANCELLED("Cancelled", "🚫");
    
    private final String displayName;
    private final String icon;
    
    DungeonState(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
