package de.noctivag.skyblock.features.dungeons.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Dungeon Leaderboard class for dungeon rankings
 */
public class DungeonLeaderboard {
    private String dungeonType;
    private String floor;
    private List<DungeonRanking> rankings;
    private long lastUpdated;

    public DungeonLeaderboard(String dungeonType, String floor) {
        this.dungeonType = dungeonType;
        this.floor = floor;
        this.rankings = new java.util.ArrayList<>();
        this.lastUpdated = java.lang.System.currentTimeMillis();
    }

    public String getDungeonType() {
        return dungeonType;
    }

    public String getFloor() {
        return floor;
    }

    public List<DungeonRanking> getRankings() {
        return rankings;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void addRanking(DungeonRanking ranking) {
        rankings.add(ranking);
    }

    public void removeRanking(UUID playerId) {
        rankings.removeIf(ranking -> ranking.getPlayerId().equals(playerId));
    }

    public DungeonRanking getPlayerRanking(UUID playerId) {
        return rankings.stream()
                .filter(ranking -> ranking.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public void updateRankings() {
        // Sort rankings by score and time
        rankings.sort((a, b) -> {
            if (a.getScore() != b.getScore()) {
                return Integer.compare(b.getScore(), a.getScore());
            }
            return Long.compare(a.getTime(), b.getTime());
        });

        // Update ranks
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }
    }

    public static class DungeonRanking {
        private UUID playerId;
        private String playerName;
        private int score;
        private long time;
        private int rank;

        public DungeonRanking(UUID playerId, String playerName, int score, long time) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.score = score;
            this.time = time;
            this.rank = 0;
        }

        public UUID getPlayerId() {
            return playerId;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public long getTime() {
            return time;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}
