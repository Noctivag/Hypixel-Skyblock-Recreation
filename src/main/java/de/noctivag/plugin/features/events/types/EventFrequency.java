package de.noctivag.plugin.features.events.types;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

/**
 * Event frequency levels
 */
public enum EventFrequency {
    WEEKLY("Weekly", "📅", Duration.ofDays(7), "Events that occur weekly"),
    BI_WEEKLY("Bi-Weekly", "📆", Duration.ofDays(14), "Events that occur every two weeks"),
    MONTHLY("Monthly", "📊", Duration.ofDays(30), "Events that occur monthly"),
    SEASONAL("Seasonal", "🌸", Duration.ofDays(90), "Events that occur seasonally"),
    RARE("Rare", "💎", Duration.ofDays(180), "Rare events that occur infrequently"),
    SPECIAL("Special", "✨", Duration.ofDays(365), "Special events that occur rarely");
    
    private final String displayName;
    private final String icon;
    private final Duration interval;
    private final String description;
    
    EventFrequency(String displayName, String icon, Duration interval, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.interval = interval;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public Duration getInterval() {
        return interval;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get frequency priority for scheduling
     */
    public int getPriority() {
        return switch (this) {
            case SPECIAL -> 1; // Highest priority
            case RARE -> 2;
            case SEASONAL -> 3;
            case MONTHLY -> 4;
            case BI_WEEKLY -> 5;
            case WEEKLY -> 6; // Lowest priority
        };
    }
    
    /**
     * Get frequency weight for random selection
     */
    public double getWeight() {
        return switch (this) {
            case WEEKLY -> 0.4; // 40% chance
            case BI_WEEKLY -> 0.25; // 25% chance
            case MONTHLY -> 0.2; // 20% chance
            case SEASONAL -> 0.1; // 10% chance
            case RARE -> 0.04; // 4% chance
            case SPECIAL -> 0.01; // 1% chance
        };
    }
    
    /**
     * Check if frequency allows overlapping
     */
    public boolean allowsOverlapping() {
        return switch (this) {
            case WEEKLY, BI_WEEKLY -> true; // Can overlap with others
            default -> false; // Others cannot overlap
        };
    }
    
    /**
     * Get next occurrence time
     */
    public Duration getNextOccurrence() {
        return getInterval();
    }
    
    /**
     * Get frequency color code
     */
    public String getColorCode() {
        return switch (this) {
            case WEEKLY -> "§a"; // Green
            case BI_WEEKLY -> "§b"; // Aqua
            case MONTHLY -> "§9"; // Blue
            case SEASONAL -> "§e"; // Yellow
            case RARE -> "§6"; // Gold
            case SPECIAL -> "§d"; // Light Purple
        };
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
