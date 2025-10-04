package de.noctivag.plugin.features.stats.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of stats
 */
public enum StatCategory {
    COMBAT("Combat", "⚔️", "Combat-related stats"),
    MINING("Mining", "⛏️", "Mining-related stats"),
    FARMING("Farming", "🌾", "Farming-related stats"),
    FISHING("Fishing", "🎣", "Fishing-related stats"),
    FORAGING("Foraging", "🌳", "Foraging-related stats"),
    MAGIC("Magic", "🔮", "Magic-related stats"),
    PETS("Pets", "🐾", "Pet-related stats"),
    DUNGEONS("Dungeons", "🏰", "Dungeon-related stats"),
    SOCIAL("Social", "👥", "Social-related stats"),
    CRAFTING("Crafting", "🔨", "Crafting-related stats"),
    UTILITY("Utility", "🛠️", "Utility-related stats");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    StatCategory(String displayName, String icon, String description) {
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
            case COMBAT -> "§c"; // Red
            case MINING -> "§7"; // Gray
            case FARMING -> "§a"; // Green
            case FISHING -> "§b"; // Aqua
            case FORAGING -> "§2"; // Dark Green
            case MAGIC -> "§5"; // Dark Purple
            case PETS -> "§d"; // Light Purple
            case DUNGEONS -> "§8"; // Dark Gray
            case SOCIAL -> "§e"; // Yellow
            case CRAFTING -> "§6"; // Gold
            case UTILITY -> "§f"; // White
        };
    }
    
    /**
     * Get category priority for sorting
     */
    public int getPriority() {
        return switch (this) {
            case COMBAT -> 1;
            case MINING -> 2;
            case FARMING -> 3;
            case FISHING -> 4;
            case FORAGING -> 5;
            case MAGIC -> 6;
            case PETS -> 7;
            case DUNGEONS -> 8;
            case SOCIAL -> 9;
            case CRAFTING -> 10;
            case UTILITY -> 11;
        };
    }
    
    /**
     * Get category description with formatting
     */
    public String getFormattedDescription() {
        return getColorCode() + description;
    }
    
    /**
     * Check if category is primary
     */
    public boolean isPrimary() {
        return this == COMBAT || this == MAGIC || this == UTILITY;
    }
    
    /**
     * Check if category is secondary
     */
    public boolean isSecondary() {
        return !isPrimary();
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
