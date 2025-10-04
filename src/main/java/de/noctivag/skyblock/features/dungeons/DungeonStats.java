package de.noctivag.skyblock.features.dungeons;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.dungeons.score.PlayerDungeonStats;

import java.util.UUID;

/**
 * Player's dungeon statistics wrapper
 */
public class DungeonStats {
    
    private final UUID playerId;
    private final PlayerDungeonStats stats;
    
    public DungeonStats(UUID playerId) {
        this.playerId = playerId;
        this.stats = new PlayerDungeonStats(playerId);
    }
    
    public DungeonStats(UUID playerId, PlayerDungeonStats stats) {
        this.playerId = playerId;
        this.stats = stats;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public PlayerDungeonStats getStats() {
        return stats;
    }
    
    /**
     * Get player's best score for any floor
     */
    public int getOverallBestScore() {
        return stats.getBestScore(1).orElse(0) +
               stats.getBestScore(2).orElse(0) +
               stats.getBestScore(3).orElse(0) +
               stats.getBestScore(4).orElse(0) +
               stats.getBestScore(5).orElse(0) +
               stats.getBestScore(6).orElse(0) +
               stats.getBestScore(7).orElse(0);
    }
    
    /**
     * Get total floors completed
     */
    public int getTotalFloorsCompleted() {
        int total = 0;
        for (int floor = 1; floor <= 7; floor++) {
            total += stats.getFloorCompletionCount(floor);
        }
        return total;
    }
    
    /**
     * Get highest floor completed
     */
    public int getHighestFloorCompleted() {
        for (int floor = 7; floor >= 1; floor--) {
            if (stats.getFloorCompletionCount(floor) > 0) {
                return floor;
            }
        }
        return 0;
    }
}
