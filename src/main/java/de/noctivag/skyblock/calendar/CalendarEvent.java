package de.noctivag.skyblock.calendar;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a calendar event
 */
public class CalendarEvent {
    
    private final String id;
    private final String name;
    private final EventType type;
    private final String description;
    private final String announcement;
    private final String details;
    private final List<String> rewards;
    private final int durationHours;
    private final int durationMinutes;
    private final int durationSeconds;
    private final int cooldownHours;
    private final LocalDate startDate;
    
    public CalendarEvent(String id, String name, EventType type, String description, 
                        String announcement, String details, List<String> rewards,
                        int durationHours, int durationMinutes, int durationSeconds, int cooldownHours) {
        this(id, name, type, description, announcement, details, rewards, 
             durationHours, durationMinutes, durationSeconds, cooldownHours, LocalDate.now());
    }
    
    public CalendarEvent(String id, String name, EventType type, String description, 
                        String announcement, String details, List<String> rewards,
                        int durationHours, int durationMinutes, int durationSeconds, int cooldownHours, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.announcement = announcement;
        this.details = details;
        this.rewards = rewards;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
        this.durationSeconds = durationSeconds;
        this.cooldownHours = cooldownHours;
        this.startDate = startDate;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public EventType getType() { return type; }
    public String getDescription() { return description; }
    public String getAnnouncement() { return announcement; }
    public String getDetails() { return details; }
    public List<String> getRewards() { return rewards; }
    public int getDurationHours() { return durationHours; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getDurationSeconds() { return durationSeconds; }
    public int getCooldownHours() { return cooldownHours; }
    public LocalDate getStartDate() { return startDate; }
    
    /**
     * Get the display name with type color
     */
    public String getDisplayName() {
        return type.getColor() + name;
    }
    
    /**
     * Get the formatted duration
     */
    public String getFormattedDuration() {
        if (durationHours > 0) {
            return durationHours + "h " + durationMinutes + "m";
        } else if (durationMinutes > 0) {
            return durationMinutes + "m " + durationSeconds + "s";
        } else {
            return durationSeconds + "s";
        }
    }
    
    /**
     * Get the total duration in seconds
     */
    public long getTotalDurationSeconds() {
        return durationHours * 3600L + durationMinutes * 60L + durationSeconds;
    }
    
    /**
     * Get the total cooldown in seconds
     */
    public long getTotalCooldownSeconds() {
        return cooldownHours * 3600L;
    }
    
    /**
     * Check if this event is active on a specific date
     */
    public boolean isActiveOnDate(LocalDate date) {
        return date.equals(startDate) || date.isAfter(startDate);
    }
}
