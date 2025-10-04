package de.noctivag.skyblock.features.dungeons.score;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.dungeons.catacombs.DungeonResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Leaderboard for a specific dungeon floor
 */
public class DungeonLeaderboard {
    
    private final int floor;
    private final Map<UUID, List<LeaderboardEntry>> playerScores = new ConcurrentHashMap<>();
    private final List<LeaderboardEntry> topScores = new ArrayList<>();
    
    public DungeonLeaderboard(int floor) {
        this.floor = floor;
    }
    
    /**
     * Add a score to the leaderboard
     */
    public void addScore(DungeonResult result) {
        for (UUID playerId : result.getPlayers()) {
            LeaderboardEntry entry = new LeaderboardEntry(
                playerId,
                result.getScore(),
                result.getTimeSpent(),
                result.getScoreRating(),
                System.currentTimeMillis()
            );
            
            // Add to player's scores
            playerScores.computeIfAbsent(playerId, k -> new ArrayList<>()).add(entry);
            
            // Add to top scores
            topScores.add(entry);
        }
        
        // Sort and keep only top 1000
        topScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        if (topScores.size() > 1000) {
            topScores.subList(1000, topScores.size()).clear();
        }
    }
    
    /**
     * Get top scores
     */
    public List<LeaderboardEntry> getTopScores(int limit) {
        return topScores.stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Get player's rank
     */
    public int getPlayerRank(UUID playerId) {
        List<LeaderboardEntry> playerEntries = playerScores.get(playerId);
        if (playerEntries == null || playerEntries.isEmpty()) {
            return -1;
        }
        
        // Get player's best score
        int bestScore = playerEntries.stream()
            .mapToInt(LeaderboardEntry::getScore)
            .max()
            .orElse(0);
        
        // Count players with better scores
        int rank = 1;
        for (LeaderboardEntry entry : topScores) {
            if (entry.getScore() > bestScore) {
                rank++;
            } else {
                break;
            }
        }
        
        return rank;
    }
    
    /**
     * Get player's scores
     */
    public List<LeaderboardEntry> getPlayerScores(UUID playerId) {
        return playerScores.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Get player's best score
     */
    public Optional<LeaderboardEntry> getPlayerBestScore(UUID playerId) {
        return playerScores.getOrDefault(playerId, new ArrayList<>())
            .stream()
            .max(Comparator.comparingInt(LeaderboardEntry::getScore));
    }
    
    public int getFloor() {
        return floor;
    }
    
    public int getTotalEntries() {
        return topScores.size();
    }
}
