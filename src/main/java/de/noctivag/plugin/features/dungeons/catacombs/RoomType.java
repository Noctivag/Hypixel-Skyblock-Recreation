package de.noctivag.plugin.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

/**
 * Represents different types of dungeon rooms
 */
public enum RoomType {
    ENTRY("Entry", "🚪", "Starting room where players spawn"),
    COMBAT("Combat", "⚔️", "Room with enemies to defeat"),
    PUZZLE("Puzzle", "🧩", "Room with puzzles to solve"),
    BOSS("Boss", "👹", "Room with a boss enemy"),
    EXIT("Exit", "🚪", "Exit room to complete dungeon"),
    SECRET("Secret", "🔍", "Hidden room with extra rewards");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    RoomType(String displayName, String icon, String description) {
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
