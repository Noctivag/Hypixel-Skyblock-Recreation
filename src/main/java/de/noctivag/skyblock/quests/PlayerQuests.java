package de.noctivag.skyblock.quests;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Quests Data - Individuelle Quest-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Quest Progress
 * - Quest Statistics
 * - Quest Rewards
 * - Quest Completion
 */
public class PlayerQuests {
    private final UUID playerId;
    private final Map<String, QuestProgress> activeQuests = new ConcurrentHashMap<>();
    private final Map<String, QuestProgress> completedQuests = new ConcurrentHashMap<>();
    private final Map<String, Integer> questStatistics = new ConcurrentHashMap<>();
    private long lastActivity;
    
    // Quest Settings
    private boolean autoAcceptQuests;
    private boolean showQuestNotifications;
    private boolean showQuestProgress;
    
    public PlayerQuests(UUID playerId) {
        this.playerId = playerId;
        this.lastActivity = java.lang.System.currentTimeMillis();
        
        // Initialize settings
        this.autoAcceptQuests = false;
        this.showQuestNotifications = true;
        this.showQuestProgress = true;
        
        // Initialize statistics
        this.questStatistics.put("total_quests", 0);
        this.questStatistics.put("completed_quests", 0);
        this.questStatistics.put("failed_quests", 0);
        this.questStatistics.put("total_rewards", 0);
    }
    
    public void startQuest(String questId, Quest quest) {
        if (activeQuests.containsKey(questId) || completedQuests.containsKey(questId)) {
            return; // Quest already active or completed
        }
        
        QuestProgress progress = new QuestProgress(questId, quest);
        activeQuests.put(questId, progress);
        questStatistics.put("total_quests", questStatistics.get("total_quests") + 1);
        lastActivity = java.lang.System.currentTimeMillis();
    }
    
    public void updateQuestProgress(String questId, String objectiveId, int amount) {
        QuestProgress progress = activeQuests.get(questId);
        if (progress == null) return;
        
        progress.updateObjective(objectiveId, amount);
        lastActivity = java.lang.System.currentTimeMillis();
        
        // Check if quest is completed
        if (progress.isCompleted()) {
            completeQuest(questId);
        }
    }
    
    public void completeQuest(String questId) {
        QuestProgress progress = activeQuests.remove(questId);
        if (progress == null) return;
        
        completedQuests.put(questId, progress);
        questStatistics.put("completed_quests", questStatistics.get("completed_quests") + 1);
        questStatistics.put("total_rewards", questStatistics.get("total_rewards") + progress.getQuest().getRewards().size());
        lastActivity = java.lang.System.currentTimeMillis();
    }
    
    public void failQuest(String questId) {
        QuestProgress progress = activeQuests.remove(questId);
        if (progress == null) return;
        
        questStatistics.put("failed_quests", questStatistics.get("failed_quests") + 1);
        lastActivity = java.lang.System.currentTimeMillis();
    }
    
    public boolean hasActiveQuest(String questId) {
        return activeQuests.containsKey(questId);
    }
    
    public boolean hasCompletedQuest(String questId) {
        return completedQuests.containsKey(questId);
    }
    
    public QuestProgress getActiveQuest(String questId) {
        return activeQuests.get(questId);
    }
    
    public QuestProgress getCompletedQuest(String questId) {
        return completedQuests.get(questId);
    }
    
    public int getActiveQuestCount() {
        return activeQuests.size();
    }
    
    public int getCompletedQuestCount() {
        return completedQuests.size();
    }
    
    public int getTotalQuestCount() {
        return questStatistics.get("total_quests");
    }
    
    public int getFailedQuestCount() {
        return questStatistics.get("failed_quests");
    }
    
    public double getQuestCompletionRate() {
        int total = getTotalQuestCount();
        if (total == 0) return 0.0;
        return (double) getCompletedQuestCount() / total;
    }
    
    public double getQuestSuccessRate() {
        int total = getTotalQuestCount();
        if (total == 0) return 0.0;
        return (double) getCompletedQuestCount() / total;
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public Map<String, QuestProgress> getActiveQuests() {
        return new HashMap<>(activeQuests);
    }
    
    public Map<String, QuestProgress> getCompletedQuests() {
        return new HashMap<>(completedQuests);
    }
    
    public Map<String, Integer> getQuestStatistics() {
        return new HashMap<>(questStatistics);
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public boolean isAutoAcceptQuests() {
        return autoAcceptQuests;
    }
    
    public void setAutoAcceptQuests(boolean autoAcceptQuests) {
        this.autoAcceptQuests = autoAcceptQuests;
    }
    
    public boolean isShowQuestNotifications() {
        return showQuestNotifications;
    }
    
    public void setShowQuestNotifications(boolean showQuestNotifications) {
        this.showQuestNotifications = showQuestNotifications;
    }
    
    public boolean isShowQuestProgress() {
        return showQuestProgress;
    }
    
    public void setShowQuestProgress(boolean showQuestProgress) {
        this.showQuestProgress = showQuestProgress;
    }
    
    public void reset() {
        activeQuests.clear();
        completedQuests.clear();
        questStatistics.clear();
        lastActivity = java.lang.System.currentTimeMillis();
        
        // Reset settings
        this.autoAcceptQuests = false;
        this.showQuestNotifications = true;
        this.showQuestProgress = true;
        
        // Reset statistics
        this.questStatistics.put("total_quests", 0);
        this.questStatistics.put("completed_quests", 0);
        this.questStatistics.put("failed_quests", 0);
        this.questStatistics.put("total_rewards", 0);
    }
    
    public String getQuestSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lQuest Summary:\n");
        summary.append("§7Active Quests: §e").append(getActiveQuestCount()).append("\n");
        summary.append("§7Completed Quests: §a").append(getCompletedQuestCount()).append("\n");
        summary.append("§7Failed Quests: §c").append(getFailedQuestCount()).append("\n");
        summary.append("§7Completion Rate: §e").append(String.format("%.1f%%", getQuestCompletionRate() * 100)).append("\n");
        summary.append("§7Success Rate: §a").append(String.format("%.1f%%", getQuestSuccessRate() * 100)).append("\n");
        summary.append("§7Total Rewards: §e").append(questStatistics.get("total_rewards")).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerQuests{" +
                "playerId=" + playerId +
                ", activeQuests=" + getActiveQuestCount() +
                ", completedQuests=" + getCompletedQuestCount() +
                ", totalQuests=" + getTotalQuestCount() +
                '}';
    }
    
    /**
     * Quest Progress - Tracks progress for a specific quest
     */
    public static class QuestProgress {
        private final String questId;
        private final Quest quest;
        private final Map<String, Integer> objectiveProgress = new ConcurrentHashMap<>();
        private final long startTime;
        private long lastUpdate;
        
        public QuestProgress(String questId, Quest quest) {
            this.questId = questId;
            this.quest = quest;
            this.startTime = java.lang.System.currentTimeMillis();
            this.lastUpdate = java.lang.System.currentTimeMillis();
            
            // Initialize objective progress
            for (QuestObjective objective : quest.getObjectives()) {
                objectiveProgress.put(objective.getId(), 0);
            }
        }
        
        public void updateObjective(String objectiveId, int amount) {
            objectiveProgress.put(objectiveId, objectiveProgress.getOrDefault(objectiveId, 0) + amount);
            lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public boolean isCompleted() {
            for (QuestObjective objective : quest.getObjectives()) {
                int current = objectiveProgress.getOrDefault(objective.getId(), 0);
                if (current < objective.getRequiredAmount()) {
                    return false;
                }
            }
            return true;
        }
        
        public double getProgress() {
            int totalObjectives = quest.getObjectives().size();
            int completedObjectives = 0;
            
            for (QuestObjective objective : quest.getObjectives()) {
                int current = objectiveProgress.getOrDefault(objective.getId(), 0);
                if (current >= objective.getRequiredAmount()) {
                    completedObjectives++;
                }
            }
            
            return (double) completedObjectives / totalObjectives;
        }
        
        public String getQuestId() {
            return questId;
        }
        
        public Quest getQuest() {
            return quest;
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
    }
}
