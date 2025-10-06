package de.noctivag.skyblock.calendar;

/**
 * Types of calendar events
 */
public enum EventType {
    
    DAILY("Daily", "&a", "&7Recurring daily events"),
    WEEKLY("Weekly", "&6", "&7Recurring weekly events"),
    MONTHLY("Monthly", "&5", "&7Recurring monthly events"),
    SPECIAL("Special", "&d", "&7Special limited-time events"),
    SEASONAL("Seasonal", "&b", "&7Seasonal events"),
    HOLIDAY("Holiday", "&c", "&7Holiday celebrations");
    
    private final String displayName;
    private final String color;
    private final String description;
    
    EventType(String displayName, String color, String description) {
        this.displayName = displayName;
        this.color = color;
        this.description = description;
    }
    
    public String getDisplayName() {
        return color + displayName;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the material icon for this event type
     */
    public org.bukkit.Material getIcon() {
        switch (this) {
            case DAILY: return org.bukkit.Material.SUNFLOWER;
            case WEEKLY: return org.bukkit.Material.CLOCK;
            case MONTHLY: return org.bukkit.Material.CLOCK;
            case SPECIAL: return org.bukkit.Material.FIREWORK_ROCKET;
            case SEASONAL: return org.bukkit.Material.SNOWBALL;
            case HOLIDAY: return org.bukkit.Material.CAKE;
            default: return org.bukkit.Material.BOOK;
        }
    }
}
