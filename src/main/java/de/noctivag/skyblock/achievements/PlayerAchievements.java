package de.noctivag.skyblock.achievements;
import java.util.UUID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player achievements data
 */
public class PlayerAchievements {
    
    private final UUID playerId;
    private final Set<String> completedAchievements = ConcurrentHashMap.newKeySet();
    private final Map<String, AchievementProgress> progress = new ConcurrentHashMap<>();
    
    public PlayerAchievements(UUID playerId) {
        this.playerId = playerId;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public boolean hasAchievement(String achievementId) {
        return completedAchievements.contains(achievementId);
    }
    
    public void completeAchievement(String achievementId) {
        completedAchievements.add(achievementId);
    }
    
    public Set<String> getCompletedAchievements() {
        return new HashSet<>(completedAchievements);
    }
    
    public AchievementProgress getProgress(String achievementId) {
        return progress.computeIfAbsent(achievementId, k -> new AchievementProgress());
    }
    
    public void updateProgress(String achievementId, int current, int required) {
        AchievementProgress achievementProgress = getProgress(achievementId);
        achievementProgress.setCurrent(current);
        achievementProgress.setRequired(required);
    }
    
    public int getTotalPoints() {
        // This would need to be calculated based on completed achievements
        return completedAchievements.size() * 10; // Placeholder
    }
}
