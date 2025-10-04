package de.noctivag.skyblock.leaderboards;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a single entry in a leaderboard
 */
public class LeaderboardEntry {
    
    private final String playerName;
    private final String uuid;
    private final double value;
    private final int rank;
    
    public LeaderboardEntry(String playerName, String uuid, double value, int rank) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.value = value;
        this.rank = rank;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public double getValue() {
        return value;
    }
    
    public int getRank() {
        return rank;
    }
    
    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "playerName='" + playerName + '\'' +
                ", uuid='" + uuid + '\'' +
                ", value=" + value +
                ", rank=" + rank +
                '}';
    }
}
