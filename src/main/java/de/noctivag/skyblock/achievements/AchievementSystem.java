package de.noctivag.skyblock.achievements;

import de.noctivag.skyblock.Plugin;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Achievement system for managing player achievements
 */
public class AchievementSystem {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, Set<Achievement>> playerAchievements;
    private final Map<UUID, Map<Achievement, Integer>> playerProgress;
    
    public AchievementSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.playerAchievements = new ConcurrentHashMap<>();
        this.playerProgress = new ConcurrentHashMap<>();
    }
    
    /**
     * Get all achievements for a player
     */
    public Set<Achievement> getPlayerAchievements(Player player) {
        return playerAchievements.getOrDefault(player.getUniqueId(), new HashSet<>());
    }
    
    /**
     * Get total number of achievements
     */
    public int getTotalAchievements() {
        return Achievement.values().length;
    }
    
    /**
     * Get completed achievements for a player
     */
    public int getCompletedAchievements(Player player) {
        return getPlayerAchievements(player).size();
    }
    
    /**
     * Get completion percentage for a player
     */
    public double getCompletionPercentage(Player player) {
        int total = getTotalAchievements();
        int completed = getCompletedAchievements(player);
        if (total == 0) return 0.0;
        return (double) completed / total * 100.0;
    }

    // Methods expected by EnhancedAchievementGUI
    public AchievementDefinition getAchievementDefinition(String id) {
        Achievement achievement = Achievement.valueOf(id.toUpperCase());
        if (achievement != null) {
            return new AchievementDefinition(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getCategory(),
                achievement.getRarity(),
                achievement.getPoints(),
                achievement.getCoinReward()
            );
        }
        return null;
    }

    public List<AchievementDefinition> getAchievementsByCategory(AchievementCategory category) {
        List<AchievementDefinition> result = new ArrayList<>();
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getCategory() == category) {
                result.add(new AchievementDefinition(
                    achievement.getId(),
                    achievement.getName(),
                    achievement.getDescription(),
                    achievement.getCategory(),
                    achievement.getRarity(),
                    achievement.getPoints(),
                    achievement.getCoinReward()
                ));
            }
        }
        return result;
    }
    
    /**
     * Check if player has an achievement
     */
    public boolean hasAchievement(Player player, Achievement achievement) {
        return getPlayerAchievements(player).contains(achievement);
    }
    
    /**
     * Award an achievement to a player
     */
    public void awardAchievement(Player player, Achievement achievement) {
        if (!hasAchievement(player, achievement)) {
            playerAchievements.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(achievement);
            achievement.award(player);
        }
    }
    
    /**
     * Get player progress for an achievement
     */
    public int getPlayerProgress(Player player, Achievement achievement) {
        return playerProgress.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>())
                .getOrDefault(achievement, 0);
    }
    
    /**
     * Set player progress for an achievement
     */
    public void setPlayerProgress(Player player, Achievement achievement, int progress) {
        playerProgress.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>())
                .put(achievement, progress);
        
        // Check if achievement should be unlocked
        achievement.checkUnlock(player, progress);
    }
    
    
    /**
     * Get all achievements
     */
    public List<Achievement> getAllAchievements() {
        return Arrays.asList(Achievement.values());
    }

    // Methods expected by EnhancedAchievementGUI
    public boolean hasAchievement(Player player, String achievementId) {
        Achievement achievement = Achievement.valueOf(achievementId.toUpperCase());
        return hasAchievement(player, achievement);
    }

    public int getAchievementProgress(Player player, String achievementId) {
        // Placeholder implementation
        return 0;
    }
    
    /**
     * Get achievement by ID
     */
    public Optional<Achievement> getAchievementById(String id) {
        return Arrays.stream(Achievement.values())
                .filter(achievement -> achievement.getId().equals(id))
                .findFirst();
    }
    
}
