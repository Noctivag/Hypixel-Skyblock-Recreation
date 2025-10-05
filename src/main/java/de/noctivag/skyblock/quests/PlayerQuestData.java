package de.noctivag.skyblock.quests;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player quest data storage
 */
public class PlayerQuestData {
    
    private final UUID playerUuid;
    private final Set<String> activeQuests = ConcurrentHashMap.newKeySet();
    private final Set<String> completedQuests = ConcurrentHashMap.newKeySet();
    private final Map<String, Map<String, Integer>> questProgress = new ConcurrentHashMap<>();
    
    public PlayerQuestData(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
    
    /**
     * Get player UUID
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    /**
     * Add quest to player
     */
    public void addQuest(String questId) {
        activeQuests.add(questId);
        questProgress.put(questId, new ConcurrentHashMap<>());
    }
    
    /**
     * Complete quest
     */
    public void completeQuest(String questId) {
        activeQuests.remove(questId);
        completedQuests.add(questId);
    }
    
    /**
     * Check if player has quest
     */
    public boolean hasQuest(String questId) {
        return activeQuests.contains(questId);
    }
    
    /**
     * Check if quest is completed
     */
    public boolean isQuestCompleted(String questId) {
        return completedQuests.contains(questId);
    }
    
    /**
     * Update objective progress
     */
    public void updateObjectiveProgress(String questId, String objectiveType, int amount) {
        Map<String, Integer> progress = questProgress.computeIfAbsent(questId, k -> new ConcurrentHashMap<>());
        progress.put(objectiveType, progress.getOrDefault(objectiveType, 0) + amount);
    }
    
    /**
     * Get objective progress
     */
    public int getObjectiveProgress(String questId, String objectiveType) {
        Map<String, Integer> progress = questProgress.get(questId);
        return progress != null ? progress.getOrDefault(objectiveType, 0) : 0;
    }
    
    /**
     * Check if objective is completed
     */
    public boolean isObjectiveCompleted(String questId, String objectiveType) {
        // This would need access to the quest to check required amount
        // For now, return false as placeholder
        return false;
    }
    
    /**
     * Get all active quests
     */
    public Set<String> getActiveQuests() {
        return new HashSet<>(activeQuests);
    }
    
    /**
     * Get all completed quests
     */
    public Set<String> getCompletedQuests() {
        return new HashSet<>(completedQuests);
    }
    
    /**
     * Get quest progress
     */
    public Map<String, Integer> getQuestProgress(String questId) {
        return new HashMap<>(questProgress.getOrDefault(questId, new HashMap<>()));
    }
    
    /**
     * Get all quest progress
     */
    public Map<String, Map<String, Integer>> getAllQuestProgress() {
        return new HashMap<>(questProgress);
    }
    
    /**
     * Get total active quests count
     */
    public int getActiveQuestsCount() {
        return activeQuests.size();
    }
    
    /**
     * Get total completed quests count
     */
    public int getCompletedQuestsCount() {
        return completedQuests.size();
    }
    
    /**
     * Get total quests count
     */
    public int getTotalQuestsCount() {
        return activeQuests.size() + completedQuests.size();
    }
    
    /**
     * Check if player has any active quests
     */
    public boolean hasActiveQuests() {
        return !activeQuests.isEmpty();
    }
    
    /**
     * Check if player has any completed quests
     */
    public boolean hasCompletedQuests() {
        return !completedQuests.isEmpty();
    }
    
    /**
     * Clear all quest data
     */
    public void clearAllData() {
        activeQuests.clear();
        completedQuests.clear();
        questProgress.clear();
    }
    
    /**
     * Clear quest data for specific quest
     */
    public void clearQuestData(String questId) {
        activeQuests.remove(questId);
        completedQuests.remove(questId);
        questProgress.remove(questId);
    }
}