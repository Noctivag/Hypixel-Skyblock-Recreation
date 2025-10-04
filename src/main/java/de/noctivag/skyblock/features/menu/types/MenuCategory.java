package de.noctivag.skyblock.features.menu.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of menus
 */
public enum MenuCategory {
    MAIN("Main", "🏠", "Main navigation menus"),
    COMBAT("Combat", "⚔️", "Combat-related menus"),
    TOOLS("Tools", "🔧", "Tool-related menus"),
    ECONOMY("Economy", "💰", "Economy-related menus"),
    STORAGE("Storage", "📦", "Storage-related menus"),
    GAME_SYSTEMS("Game Systems", "🎮", "Core game system menus"),
    DUNGEONS("Dungeons", "🏰", "Dungeon-related menus"),
    CRAFTING("Crafting", "🔨", "Crafting-related menus"),
    SETTINGS("Settings", "⚙️", "Settings and configuration menus"),
    SPECIAL("Special", "✨", "Special feature menus");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    MenuCategory(String displayName, String icon, String description) {
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
    
    /**
     * Get category color code
     */
    public String getColorCode() {
        return switch (this) {
            case MAIN -> "§6"; // Gold
            case COMBAT -> "§c"; // Red
            case TOOLS -> "§7"; // Gray
            case ECONOMY -> "§a"; // Green
            case STORAGE -> "§9"; // Blue
            case GAME_SYSTEMS -> "§b"; // Aqua
            case DUNGEONS -> "§5"; // Dark Purple
            case CRAFTING -> "§e"; // Yellow
            case SETTINGS -> "§f"; // White
            case SPECIAL -> "§d"; // Light Purple
        };
    }
    
    /**
     * Get category priority for sorting
     */
    public int getPriority() {
        return switch (this) {
            case MAIN -> 1;
            case COMBAT -> 2;
            case TOOLS -> 3;
            case ECONOMY -> 4;
            case STORAGE -> 5;
            case GAME_SYSTEMS -> 6;
            case DUNGEONS -> 7;
            case CRAFTING -> 8;
            case SETTINGS -> 9;
            case SPECIAL -> 10;
        };
    }
    
    /**
     * Get category description with formatting
     */
    public String getFormattedDescription() {
        return getColorCode() + description;
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
