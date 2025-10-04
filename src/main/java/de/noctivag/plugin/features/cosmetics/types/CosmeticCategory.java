package de.noctivag.plugin.features.cosmetics.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of cosmetics
 */
public enum CosmeticCategory {
    HAT("Hat", "🎩", "Head cosmetics"),
    CLOAK("Cloak", "🧥", "Back cosmetics"),
    WINGS("Wings", "🪶", "Wing cosmetics"),
    AURA("Aura", "✨", "Aura cosmetics"),
    TRAIL("Trail", "🌈", "Trail cosmetics"),
    SPECIAL_EFFECT("Special Effect", "🎭", "Special effect cosmetics");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    CosmeticCategory(String displayName, String icon, String description) {
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
            case HAT -> "§e"; // Yellow
            case CLOAK -> "§9"; // Blue
            case WINGS -> "§d"; // Light Purple
            case AURA -> "§5"; // Dark Purple
            case TRAIL -> "§b"; // Aqua
            case SPECIAL_EFFECT -> "§c"; // Red
        };
    }
    
    /**
     * Get category priority for sorting
     */
    public int getPriority() {
        return switch (this) {
            case HAT -> 1;
            case CLOAK -> 2;
            case WINGS -> 3;
            case AURA -> 4;
            case TRAIL -> 5;
            case SPECIAL_EFFECT -> 6;
        };
    }
    
    /**
     * Get category description with formatting
     */
    public String getFormattedDescription() {
        return getColorCode() + description;
    }
    
    /**
     * Check if category allows multiple active items
     */
    public boolean allowsMultiple() {
        return switch (this) {
            case HAT, CLOAK, WINGS -> false; // Only one active at a time
            case AURA, TRAIL, SPECIAL_EFFECT -> true; // Multiple can be active
        };
    }
    
    /**
     * Get category slot limit
     */
    public int getSlotLimit() {
        return switch (this) {
            case HAT, CLOAK, WINGS -> 1; // One slot
            case AURA -> 3; // Three aura slots
            case TRAIL -> 2; // Two trail slots
            case SPECIAL_EFFECT -> 5; // Five special effect slots
        };
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
