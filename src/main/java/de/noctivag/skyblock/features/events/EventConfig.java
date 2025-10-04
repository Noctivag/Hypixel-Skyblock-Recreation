package de.noctivag.skyblock.features.events;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.events.types.EventType;
import de.noctivag.skyblock.features.events.types.EventCategory;
import de.noctivag.skyblock.features.events.types.EventRarity;

import java.time.Duration;
import java.util.List;

/**
 * Configuration for an event
 */
public class EventConfig {
    
    private final EventType eventType;
    private final String displayName;
    private final String icon;
    private final String description;
    private final EventCategory category;
    private final EventRarity rarity;
    private final Duration duration;
    private final Duration cooldown;
    private final String instructions;
    private final List<EventReward> rewards;
    
    public EventConfig(EventType eventType, String displayName, String icon, String description,
                      EventCategory category, EventRarity rarity, Duration duration, Duration cooldown,
                      String instructions, List<EventReward> rewards) {
        this.eventType = eventType;
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
        this.duration = duration;
        this.cooldown = cooldown;
        this.instructions = instructions;
        this.rewards = rewards;
    }
    
    /**
     * Get event display name with formatting
     */
    public String getFormattedName() {
        return rarity.getColorCode() + icon + " " + displayName;
    }
    
    /**
     * Get event description with formatting
     */
    public String getFormattedDescription() {
        return "§7" + description;
    }
    
    /**
     * Get event instructions with formatting
     */
    public String getFormattedInstructions() {
        return "§e" + instructions;
    }
    
    /**
     * Get event lore for display
     */
    public List<String> getLore() {
        return List.of(
            getFormattedDescription(),
            "",
            "§7Category: " + category.getColorCode() + category.getDisplayName(),
            "§7Rarity: " + rarity.toString(),
            "§7Duration: §a" + formatDuration(duration),
            "§7Cooldown: §c" + formatDuration(cooldown),
            "",
            getFormattedInstructions(),
            "",
            "§6Rewards:"
        );
    }
    
    /**
     * Format duration for display
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    /**
     * Check if event can start now
     */
    public boolean canStartNow() {
        // Simple check - would be more sophisticated in real implementation
        return Math.random() < 0.1; // 10% chance
    }
    
    /**
     * Get event priority for scheduling
     */
    public int getPriority() {
        return category.getPriority();
    }
    
    /**
     * Check if event allows overlapping
     */
    public boolean allowsOverlapping() {
        return category.allowsOverlapping();
    }
    
    /**
     * Get event weight for random selection
     */
    public double getWeight() {
        return rarity.getWeight() * category.getPriority();
    }
    
    // Getters
    public EventType getEventType() {
        return eventType;
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
    
    public EventCategory getCategory() {
        return category;
    }
    
    public EventRarity getRarity() {
        return rarity;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public Duration getCooldown() {
        return cooldown;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public List<EventReward> getRewards() {
        return rewards;
    }
    
    @Override
    public String toString() {
        return getFormattedName();
    }
}
