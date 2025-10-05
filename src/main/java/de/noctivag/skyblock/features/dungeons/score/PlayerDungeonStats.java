package de.noctivag.skyblock.features.dungeons.score;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.dungeons.catacombs.DungeonResult;
import de.noctivag.skyblock.features.dungeons.catacombs.ScoreRating;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player's dungeon statistics
 */
public class PlayerDungeonStats {
    
    private final UUID playerId;
    private final Map<Integer, List<DungeonResult>> floorCompletions = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> bestScores = new ConcurrentHashMap<>();
    private final Map<ScoreRating, Integer> ratingCounts = new ConcurrentHashMap<>();
    
    private int totalCompletions = 0;
    private int totalScore = 0;
    private long totalTimeSpent = 0; // in seconds
    
    public PlayerDungeonStats(UUID playerId) {
        this.playerId = playerId;
        initializeRatingCounts();
    }
    
    /**
     * Add a dungeon completion
     */
    public void addCompletion(DungeonResult result) {
        int floor = result.getFloor();
        
        // Add to floor completions
        floorCompletions.computeIfAbsent(floor, k -> new ArrayList<>()).add(result);
        
        // Update best score
        int currentBest = bestScores.getOrDefault(floor, 0);
        if (result.getScore() > currentBest) {
            bestScores.put(floor, result.getScore());
        }
        
        // Update rating counts
        ScoreRating rating = result.getScoreRating();
        ratingCounts.put(rating, ratingCounts.get(rating) + 1);
        
        // Update totals
        totalCompletions++;
        totalScore += result.getScore();
        totalTimeSpent += result.getTimeSpent();
    }
    
    /**
     * Get best score for a floor
     */
    public OptionalInt getBestScore(int floor) {
        int score = bestScores.getOrDefault(floor, 0);
        return score > 0 ? OptionalInt.of(score) : OptionalInt.empty();
    }
    
    /**
     * Get completions for a floor
     */
    public List<DungeonResult> getFloorCompletions(int floor) {
        return floorCompletions.getOrDefault(floor, new ArrayList<>());
    }
    
    /**
     * Get completion count for a floor
     */
    public int getFloorCompletionCount(int floor) {
        return floorCompletions.getOrDefault(floor, new ArrayList<>()).size();
    }
    
    /**
     * Get average score for a floor
     */
    public double getAverageScore(int floor) {
        List<DungeonResult> completions = getFloorCompletions(floor);
        if (completions.isEmpty()) return 0.0;
        
        return completions.stream()
            .mapToInt(DungeonResult::getScore)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Get average time for a floor
     */
    public double getAverageTime(int floor) {
        List<DungeonResult> completions = getFloorCompletions(floor);
        if (completions.isEmpty()) return 0.0;
        
        return completions.stream()
            .mapToLong(DungeonResult::getTimeSpent)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Get overall average score
     */
    public double getOverallAverageScore() {
        if (totalCompletions == 0) return 0.0;
        return (double) totalScore / totalCompletions;
    }
    
    /**
     * Get overall average time
     */
    public double getOverallAverageTime() {
        if (totalCompletions == 0) return 0.0;
        return (double) totalTimeSpent / totalCompletions;
    }
    
    /**
     * Get count of a specific rating
     */
    public int getRatingCount(ScoreRating rating) {
        return ratingCounts.get(rating);
    }
    
    /**
     * Get total completions
     */
    public int getTotalCompletions() {
        return totalCompletions;
    }
    
    /**
     * Get total score
     */
    public int getTotalScore() {
        return totalScore;
    }
    
    /**
     * Get total time spent
     */
    public long getTotalTimeSpent() {
        return totalTimeSpent;
    }
    
    /**
     * Get player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Initialize rating counts
     */
    private void initializeRatingCounts() {
        for (ScoreRating rating : ScoreRating.values()) {
            ratingCounts.put(rating, 0);
        }
    }
}
