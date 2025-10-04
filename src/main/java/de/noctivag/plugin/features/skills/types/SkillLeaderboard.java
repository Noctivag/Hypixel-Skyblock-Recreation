package de.noctivag.plugin.features.skills.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Skill Leaderboard class for skill rankings
 */
public class SkillLeaderboard implements Service {
    private SkillType skillType;
    private List<SkillRanking> rankings;
    private long lastUpdated;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public SkillLeaderboard(SkillType skillType) {
        this.skillType = skillType;
        this.rankings = new java.util.ArrayList<>();
        this.lastUpdated = System.currentTimeMillis();
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            // Initialize skill leaderboard
            status = SystemStatus.ENABLED;
        });
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            // Shutdown skill leaderboard
            status = SystemStatus.UNINITIALIZED;
        });
    }

    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String getName() {
        return "SkillLeaderboard";
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public List<SkillRanking> getRankings() {
        return rankings;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void addRanking(SkillRanking ranking) {
        rankings.add(ranking);
    }

    public void removeRanking(UUID playerId) {
        rankings.removeIf(ranking -> ranking.getPlayerId().equals(playerId));
    }

    public SkillRanking getPlayerRanking(UUID playerId) {
        return rankings.stream()
                .filter(ranking -> ranking.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public void updateRankings() {
        // Sort rankings by level and experience
        rankings.sort((a, b) -> {
            if (a.getLevel() != b.getLevel()) {
                return Integer.compare(b.getLevel(), a.getLevel());
            }
            return Long.compare(b.getExperience(), a.getExperience());
        });

        // Update ranks
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }
    }

    public static class SkillRanking {
        private UUID playerId;
        private String playerName;
        private int level;
        private long experience;
        private int rank;

        public SkillRanking(UUID playerId, String playerName, int level, long experience) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.level = level;
            this.experience = experience;
            this.rank = 0;
        }

        public UUID getPlayerId() {
            return playerId;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getLevel() {
            return level;
        }

        public long getExperience() {
            return experience;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}
