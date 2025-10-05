package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Cult Event - Represents a Cult of the Fallen Star event
 * 
 * Manages meteor showers and other cult-related events
 */
public class CultEvent {
    
    private final String id;
    private final LocalDateTime startTime;
    private final EventType eventType;
    private EventStatus status;
    private final Map<String, Integer> participantContributions;
    private final Map<String, Integer> leaderboard;
    private LocalDateTime endTime;
    private String winner;
    private int totalMeteors;
    private int collectedMeteors;
    
    public CultEvent(String id, LocalDateTime startTime, EventType eventType, EventStatus status) {
        this.id = id;
        this.startTime = startTime;
        this.eventType = eventType;
        this.status = status;
        this.participantContributions = new HashMap<>();
        this.leaderboard = new HashMap<>();
        this.endTime = startTime.plusHours(3); // 3 hour event
        this.totalMeteors = calculateTotalMeteors(eventType);
        this.collectedMeteors = 0;
    }
    
    /**
     * Calculate total meteors based on event type
     */
    private int calculateTotalMeteors(EventType eventType) {
        switch (eventType) {
            case METEOR_SHOWER:
                return 100; // 100 meteors
            case COMET_IMPACT:
                return 50;  // 50 meteors
            case ASTEROID_BELT:
                return 200; // 200 meteors
            case SOLAR_FLARE:
                return 75;  // 75 meteors
            default:
                return 100;
        }
    }
    
    /**
     * Add participant contribution
     */
    public void addContribution(String participantId, int contribution) {
        if (status != EventStatus.ACTIVE) {
            return;
        }
        
        participantContributions.put(participantId, 
            participantContributions.getOrDefault(participantId, 0) + contribution);
        collectedMeteors += contribution;
        updateLeaderboard();
    }
    
    /**
     * Update leaderboard
     */
    private void updateLeaderboard() {
        leaderboard.clear();
        participantContributions.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> leaderboard.put(entry.getKey(), entry.getValue()));
    }
    
    /**
     * Get participant contribution
     */
    public int getParticipantContribution(String participantId) {
        return participantContributions.getOrDefault(participantId, 0);
    }
    
    /**
     * Get leaderboard position
     */
    public int getLeaderboardPosition(String participantId) {
        int position = 1;
        for (String id : leaderboard.keySet()) {
            if (id.equals(participantId)) {
                return position;
            }
            position++;
        }
        return -1; // Not found
    }
    
    /**
     * Get top participants
     */
    public Map<String, Integer> getTopParticipants(int count) {
        Map<String, Integer> top = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            if (i >= count) break;
            top.put(entry.getKey(), entry.getValue());
            i++;
        }
        return top;
    }
    
    /**
     * Get winner
     */
    public String getWinningParticipant() {
        if (status != EventStatus.COMPLETED) {
            return null;
        }
        
        return leaderboard.entrySet().stream()
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Check if event is active
     */
    public boolean isActive() {
        return status == EventStatus.ACTIVE && LocalDateTime.now().isBefore(endTime);
    }
    
    /**
     * Check if event has ended
     */
    public boolean hasEnded() {
        return LocalDateTime.now().isAfter(endTime) || status == EventStatus.COMPLETED;
    }
    
    /**
     * Complete the event
     */
    public void completeEvent() {
        if (hasEnded()) {
            status = EventStatus.COMPLETED;
            winner = getWinningParticipant();
        }
    }
    
    /**
     * Get remaining time in seconds
     */
    public long getRemainingTimeSeconds() {
        if (!isActive()) return 0;
        
        return endTime.toEpochSecond(java.time.ZoneOffset.UTC) - java.lang.System.currentTimeMillis() / 1000;
    }
    
    /**
     * Get event progress percentage
     */
    public double getProgressPercentage() {
        if (!isActive()) return 100.0;
        
        long totalDuration = 3 * 3600; // 3 hours in seconds
        long elapsed = (java.lang.System.currentTimeMillis() / 1000) - startTime.toEpochSecond(java.time.ZoneOffset.UTC);
        
        return Math.min(100.0, (double) elapsed / totalDuration * 100.0);
    }
    
    /**
     * Get meteor collection progress
     */
    public double getMeteorCollectionProgress() {
        if (totalMeteors == 0) return 0.0;
        return Math.min(100.0, (double) collectedMeteors / totalMeteors * 100.0);
    }
    
    /**
     * Check if all meteors have been collected
     */
    public boolean isAllMeteorsCollected() {
        return collectedMeteors >= totalMeteors;
    }
    
    /**
     * Get remaining meteors
     */
    public int getRemainingMeteors() {
        return Math.max(0, totalMeteors - collectedMeteors);
    }
    
    /**
     * Convert to JSON for Redis storage
     */
    public String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        
        json.addProperty("id", id);
        json.addProperty("startTime", startTime.toString());
        json.addProperty("endTime", endTime.toString());
        json.addProperty("eventType", eventType.name());
        json.addProperty("status", status.name());
        json.addProperty("winner", winner);
        json.addProperty("totalMeteors", totalMeteors);
        json.addProperty("collectedMeteors", collectedMeteors);
        
        // Add participant contributions
        JsonObject contributionsObject = new JsonObject();
        for (Map.Entry<String, Integer> entry : participantContributions.entrySet()) {
            contributionsObject.addProperty(entry.getKey(), entry.getValue());
        }
        json.add("participantContributions", contributionsObject);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static CultEvent fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        CultEvent event = new CultEvent(
            obj.get("id").getAsString(),
            LocalDateTime.parse(obj.get("startTime").getAsString()),
            EventType.valueOf(obj.get("eventType").getAsString()),
            EventStatus.valueOf(obj.get("status").getAsString())
        );
        
        event.endTime = LocalDateTime.parse(obj.get("endTime").getAsString());
        event.winner = obj.has("winner") ? obj.get("winner").getAsString() : null;
        event.totalMeteors = obj.get("totalMeteors").getAsInt();
        event.collectedMeteors = obj.get("collectedMeteors").getAsInt();
        
        // Restore participant contributions
        JsonObject contributionsObject = obj.getAsJsonObject("participantContributions");
        for (String participantId : contributionsObject.keySet()) {
            event.participantContributions.put(participantId, contributionsObject.get(participantId).getAsInt());
        }
        event.updateLeaderboard();
        
        return event;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public EventType getEventType() { return eventType; }
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    public int getTotalMeteors() { return totalMeteors; }
    public int getCollectedMeteors() { return collectedMeteors; }
    public Map<String, Integer> getParticipantContributions() { return new HashMap<>(participantContributions); }
    public Map<String, Integer> getLeaderboard() { return new HashMap<>(leaderboard); }
    
    /**
     * Event type enum
     */
    public enum EventType {
        METEOR_SHOWER("Meteor Shower", "Falling meteors across the sky"),
        COMET_IMPACT("Comet Impact", "A large comet impacts the world"),
        ASTEROID_BELT("Asteroid Belt", "Multiple asteroids fall from the sky"),
        SOLAR_FLARE("Solar Flare", "Solar activity affects the world");
        
        private final String displayName;
        private final String description;
        
        EventType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Event status enum
     */
    public enum EventStatus {
        SCHEDULED, ACTIVE, COMPLETED, CANCELLED
    }
    
    @Override
    public String toString() {
        return String.format("CultEvent{id='%s', type=%s, status=%s, meteors=%d/%d, participants=%d}", 
                           id, eventType, status, collectedMeteors, totalMeteors, participantContributions.size());
    }
}
