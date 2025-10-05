package de.noctivag.skyblock.engine.temporal.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Jacob's Event - Represents a Jacob's farming contest
 * 
 * Manages farming contests with different crop types and leaderboards
 */
public class JacobsEvent {
    
    private final String id;
    private final LocalDateTime startTime;
    private final ContestType contestType;
    private EventStatus status;
    private final Map<String, Integer> participantScores;
    private final Map<String, Integer> leaderboard;
    private LocalDateTime endTime;
    private String winner;
    
    public JacobsEvent(String id, LocalDateTime startTime, ContestType contestType, EventStatus status) {
        this.id = id;
        this.startTime = startTime;
        this.contestType = contestType;
        this.status = status;
        this.participantScores = new HashMap<>();
        this.leaderboard = new HashMap<>();
        this.endTime = startTime.plusHours(2); // 2 hour contest
    }
    
    /**
     * Add participant score
     */
    public void addScore(String participantId, int score) {
        if (status != EventStatus.ACTIVE) {
            return;
        }
        
        participantScores.put(participantId, score);
        updateLeaderboard();
    }
    
    /**
     * Update leaderboard
     */
    private void updateLeaderboard() {
        leaderboard.clear();
        participantScores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> leaderboard.put(entry.getKey(), entry.getValue()));
    }
    
    /**
     * Get participant score
     */
    public int getParticipantScore(String participantId) {
        return participantScores.getOrDefault(participantId, 0);
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
        
        long totalDuration = 2 * 3600; // 2 hours in seconds
        long elapsed = (java.lang.System.currentTimeMillis() / 1000) - startTime.toEpochSecond(java.time.ZoneOffset.UTC);
        
        return Math.min(100.0, (double) elapsed / totalDuration * 100.0);
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
        json.addProperty("contestType", contestType.name());
        json.addProperty("status", status.name());
        json.addProperty("winner", winner);
        
        // Add participant scores
        JsonObject scoresObject = new JsonObject();
        for (Map.Entry<String, Integer> entry : participantScores.entrySet()) {
            scoresObject.addProperty(entry.getKey(), entry.getValue());
        }
        json.add("participantScores", scoresObject);
        
        return gson.toJson(json);
    }
    
    /**
     * Create from JSON
     */
    public static JacobsEvent fromJson(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        JacobsEvent event = new JacobsEvent(
            obj.get("id").getAsString(),
            LocalDateTime.parse(obj.get("startTime").getAsString()),
            ContestType.valueOf(obj.get("contestType").getAsString()),
            EventStatus.valueOf(obj.get("status").getAsString())
        );
        
        event.endTime = LocalDateTime.parse(obj.get("endTime").getAsString());
        event.winner = obj.has("winner") ? obj.get("winner").getAsString() : null;
        
        // Restore participant scores
        JsonObject scoresObject = obj.getAsJsonObject("participantScores");
        for (String participantId : scoresObject.keySet()) {
            event.participantScores.put(participantId, scoresObject.get(participantId).getAsInt());
        }
        event.updateLeaderboard();
        
        return event;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public ContestType getContestType() { return contestType; }
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    public Map<String, Integer> getParticipantScores() { return new HashMap<>(participantScores); }
    public Map<String, Integer> getLeaderboard() { return new HashMap<>(leaderboard); }
    
    /**
     * Contest type enum
     */
    public enum ContestType {
        WHEAT("Wheat Contest", "WHEAT"),
        CARROT("Carrot Contest", "CARROT"),
        POTATO("Potato Contest", "POTATO"),
        PUMPKIN("Pumpkin Contest", "PUMPKIN"),
        MELON("Melon Contest", "MELON"),
        COCOA_BEANS("Cocoa Beans Contest", "COCOA_BEANS"),
        CACTUS("Cactus Contest", "CACTUS"),
        SUGAR_CANE("Sugar Cane Contest", "SUGAR_CANE"),
        NETHER_WART("Nether Wart Contest", "NETHER_WART"),
        MUSHROOM("Mushroom Contest", "MUSHROOM");
        
        private final String displayName;
        private final String materialName;
        
        ContestType(String displayName, String materialName) {
            this.displayName = displayName;
            this.materialName = materialName;
        }
        
        public String getDisplayName() { return displayName; }
        public String getMaterialName() { return materialName; }
    }
    
    /**
     * Event status enum
     */
    public enum EventStatus {
        SCHEDULED, ACTIVE, COMPLETED, CANCELLED
    }
    
    @Override
    public String toString() {
        return String.format("JacobsEvent{id='%s', type=%s, status=%s, participants=%d}", 
                           id, contestType, status, participantScores.size());
    }
}
