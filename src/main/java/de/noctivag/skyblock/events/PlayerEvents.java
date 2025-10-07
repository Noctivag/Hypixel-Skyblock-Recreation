package de.noctivag.skyblock.events;
import java.util.UUID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Events Data - Individuelle Event-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Event Participation
 * - Event Statistics
 * - Event Rewards
 * - Event Progress
 */
public class PlayerEvents {
    private final UUID playerId;
    private final Map<String, EventParticipation> activeEvents = new ConcurrentHashMap<>();
    private final Map<String, EventParticipation> completedEvents = new ConcurrentHashMap<>();
    private final Map<String, Integer> eventStatistics = new ConcurrentHashMap<>();
    private long lastActivity;
    
    // Event Settings
    private boolean autoJoinEvents;
    private boolean showEventNotifications;
    private boolean showEventProgress;
    
    public PlayerEvents(UUID playerId) {
        this.playerId = playerId;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        // Initialize settings
        this.autoJoinEvents = false;
        this.showEventNotifications = true;
        this.showEventProgress = true;
        
        // Initialize statistics
        this.eventStatistics.put("total_events", 0);
        this.eventStatistics.put("completed_events", 0);
        this.eventStatistics.put("failed_events", 0);
        this.eventStatistics.put("total_rewards", 0);
        this.eventStatistics.put("total_score", 0);
    }
    
    public void joinEvent(String eventId, Event event) {
        if (activeEvents.containsKey(eventId) || completedEvents.containsKey(eventId)) {
            return; // Event already joined or completed
        }
        
        EventParticipation participation = new EventParticipation(eventId, event);
        activeEvents.put(eventId, participation);
        eventStatistics.put("total_events", eventStatistics.get("total_events") + 1);
        lastActivity = java.lang.System.currentTimeMillis();
    }
    
    public void updateEventProgress(String eventId, String objectiveId, int amount) {
        EventParticipation participation = activeEvents.get(eventId);
        if (participation == null) return;
        
        participation.updateObjective(objectiveId, amount);
        lastActivity = java.lang.System.currentTimeMillis();
        
        // Check if event is completed
        if (participation.isCompleted()) {
            completeEvent(eventId);
        }
    }
    
    public void completeEvent(String eventId) {
        EventParticipation participation = activeEvents.remove(eventId);
        if (participation == null) return;
        
        completedEvents.put(eventId, participation);
        eventStatistics.put("completed_events", eventStatistics.get("completed_events") + 1);
        eventStatistics.put("total_rewards", eventStatistics.get("total_rewards") + participation.getEvent().getRewards().size());
        eventStatistics.put("total_score", eventStatistics.get("total_score") + participation.getScore());
        lastActivity = java.lang.System.currentTimeMillis();
    }
    
    public void failEvent(String eventId) {
        EventParticipation participation = activeEvents.remove(eventId);
        if (participation == null) return;
        
        eventStatistics.put("failed_events", eventStatistics.get("failed_events") + 1);
        lastActivity = java.lang.System.currentTimeMillis();
    }
    
    public boolean hasActiveEvent(String eventId) {
        return activeEvents.containsKey(eventId);
    }
    
    public boolean hasCompletedEvent(String eventId) {
        return completedEvents.containsKey(eventId);
    }
    
    public EventParticipation getActiveEvent(String eventId) {
        return activeEvents.get(eventId);
    }
    
    public EventParticipation getCompletedEvent(String eventId) {
        return completedEvents.get(eventId);
    }
    
    public int getActiveEventCount() {
        return activeEvents.size();
    }
    
    public int getCompletedEventCount() {
        return completedEvents.size();
    }
    
    public int getTotalEventCount() {
        return eventStatistics.get("total_events");
    }
    
    public int getFailedEventCount() {
        return eventStatistics.get("failed_events");
    }
    
    public int getTotalScore() {
        return eventStatistics.get("total_score");
    }
    
    public double getEventCompletionRate() {
        int total = getTotalEventCount();
        if (total == 0) return 0.0;
        return (double) getCompletedEventCount() / total;
    }
    
    public double getEventSuccessRate() {
        int total = getTotalEventCount();
        if (total == 0) return 0.0;
        return (double) getCompletedEventCount() / total;
    }
    
    public double getAverageScore() {
        int completed = getCompletedEventCount();
        if (completed == 0) return 0.0;
        return (double) getTotalScore() / completed;
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public Map<String, EventParticipation> getActiveEvents() {
        return new HashMap<>(activeEvents);
    }
    
    public Map<String, EventParticipation> getCompletedEvents() {
        return new HashMap<>(completedEvents);
    }
    
    public Map<String, Integer> getEventStatistics() {
        return new HashMap<>(eventStatistics);
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public boolean isAutoJoinEvents() {
        return autoJoinEvents;
    }
    
    public void setAutoJoinEvents(boolean autoJoinEvents) {
        this.autoJoinEvents = autoJoinEvents;
    }
    
    public boolean isShowEventNotifications() {
        return showEventNotifications;
    }
    
    public void setShowEventNotifications(boolean showEventNotifications) {
        this.showEventNotifications = showEventNotifications;
    }
    
    public boolean isShowEventProgress() {
        return showEventProgress;
    }
    
    public void setShowEventProgress(boolean showEventProgress) {
        this.showEventProgress = showEventProgress;
    }
    
    public void reset() {
        activeEvents.clear();
        completedEvents.clear();
        eventStatistics.clear();
        lastActivity = java.lang.System.currentTimeMillis();
        
        // Reset settings
        this.autoJoinEvents = false;
        this.showEventNotifications = true;
        this.showEventProgress = true;
        
        // Reset statistics
        this.eventStatistics.put("total_events", 0);
        this.eventStatistics.put("completed_events", 0);
        this.eventStatistics.put("failed_events", 0);
        this.eventStatistics.put("total_rewards", 0);
        this.eventStatistics.put("total_score", 0);
    }
    
    public String getEventSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lEvent Summary:\n");
        summary.append("§7Active Events: §e").append(getActiveEventCount()).append("\n");
        summary.append("§7Completed Events: §a").append(getCompletedEventCount()).append("\n");
        summary.append("§7Failed Events: §c").append(getFailedEventCount()).append("\n");
        summary.append("§7Completion Rate: §e").append(String.format("%.1f%%", getEventCompletionRate() * 100)).append("\n");
        summary.append("§7Success Rate: §a").append(String.format("%.1f%%", getEventSuccessRate() * 100)).append("\n");
        summary.append("§7Total Score: §e").append(getTotalScore()).append("\n");
        summary.append("§7Average Score: §e").append(String.format("%.1f", getAverageScore())).append("\n");
        summary.append("§7Total Rewards: §e").append(eventStatistics.get("total_rewards")).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerEvents{" +
                "playerId=" + playerId +
                ", activeEvents=" + getActiveEventCount() +
                ", completedEvents=" + getCompletedEventCount() +
                ", totalEvents=" + getTotalEventCount() +
                ", totalScore=" + getTotalScore() +
                '}';
    }
    
    /**
     * Event Participation - Tracks participation in a specific event
     */
    public static class EventParticipation {
        private final String eventId;
        private final Event event;
        private final Map<String, Integer> objectiveProgress = new ConcurrentHashMap<>();
        private final long startTime;
        private long lastUpdate;
        private int score;
        
        public EventParticipation(String eventId, Event event) {
            this.eventId = eventId;
            this.event = event;
            this.startTime = java.lang.System.currentTimeMillis();
            this.lastUpdate = java.lang.System.currentTimeMillis();
            this.score = 0;
            
            // Initialize objective progress
            for (EventObjective objective : event.getObjectives()) {
                objectiveProgress.put(objective.getId(), 0);
            }
        }
        
        public void updateObjective(String objectiveId, int amount) {
            objectiveProgress.put(objectiveId, objectiveProgress.getOrDefault(objectiveId, 0) + amount);
            lastUpdate = java.lang.System.currentTimeMillis();
            
            // Update score based on objective completion
            EventObjective objective = event.getObjective(objectiveId);
            if (objective != null) {
                int current = objectiveProgress.get(objectiveId);
                if (current >= objective.getRequiredAmount()) {
                    score += objective.getScore();
                }
            }
        }
        
        public boolean isCompleted() {
            for (EventObjective objective : event.getObjectives()) {
                int current = objectiveProgress.getOrDefault(objective.getId(), 0);
                if (current < objective.getRequiredAmount()) {
                    return false;
                }
            }
            return true;
        }
        
        public double getProgress() {
            int totalObjectives = event.getObjectives().size();
            int completedObjectives = 0;
            
            for (EventObjective objective : event.getObjectives()) {
                int current = objectiveProgress.getOrDefault(objective.getId(), 0);
                if (current >= objective.getRequiredAmount()) {
                    completedObjectives++;
                }
            }
            
            return (double) completedObjectives / totalObjectives;
        }
        
        public String getEventId() {
            return eventId;
        }
        
        public Event getEvent() {
            return event;
        }
        
        public Map<String, Integer> getObjectiveProgress() {
            return new HashMap<>(objectiveProgress);
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public long getLastUpdate() {
            return lastUpdate;
        }
        
        public long getDuration() {
            return java.lang.System.currentTimeMillis() - startTime;
        }
        
        public int getScore() {
            return score;
        }
        
        public void setScore(int score) {
            this.score = Math.max(0, score);
        }
    }
}
