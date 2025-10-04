package de.noctivag.skyblock.features.events.types;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Event Instance class for event instances
 */
public class EventInstance {
    private String id;
    private String eventType;
    private String name;
    private String description;
    private long startTime;
    private long endTime;
    private Duration duration;
    private List<UUID> participants;
    private EventCategory category;
    private EventRarity rarity;
    private boolean isActive;

    public EventInstance(String id, String eventType, String name, String description, Duration duration) {
        this.id = id;
        this.eventType = eventType;
        this.name = name;
        this.description = description;
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
        this.duration = duration;
        this.participants = new java.util.ArrayList<>();
        this.category = EventCategory.GENERAL;
        this.rarity = EventRarity.COMMON;
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public List<UUID> getParticipants() {
        return participants;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public EventRarity getRarity() {
        return rarity;
    }

    public void setRarity(EventRarity rarity) {
        this.rarity = rarity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addParticipant(UUID playerId) {
        if (!participants.contains(playerId)) {
            participants.add(playerId);
        }
    }

    public void removeParticipant(UUID playerId) {
        participants.remove(playerId);
    }

    public long getRemainingTime() {
        if (endTime == 0) {
            return duration.getDuration() - (System.currentTimeMillis() - startTime);
        }
        return 0;
    }
}
