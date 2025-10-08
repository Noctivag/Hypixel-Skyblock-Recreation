package de.noctivag.skyblock.dungeons;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a dungeon session with players and progress
 */
public class DungeonSession extends BaseDungeonGroup {
    private final String sessionId;
    private final DungeonFloor floor;
    private final Map<UUID, DungeonPlayer> playerMap;
    private final Location startLocation;
    private final DungeonSessionStatus status;
    private final Map<String, Object> sessionData;

    public DungeonSession(String sessionId, DungeonFloor floor, Location startLocation) {
        super(sessionId, floor.getName(), System.currentTimeMillis(), new ArrayList<>(), DungeonSessionStatus.WAITING.name());
        this.sessionId = sessionId;
        this.floor = floor;
        this.playerMap = new ConcurrentHashMap<>();
        this.startLocation = startLocation.clone();
        this.status = DungeonSessionStatus.WAITING;
        this.sessionData = new ConcurrentHashMap<>();
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public DungeonFloor getFloor() { return floor; }
    public Map<UUID, DungeonPlayer> getPlayerMap() { return new HashMap<>(playerMap); }
    @Override
    public List<UUID> getPlayers() { return new ArrayList<>(playerMap.keySet()); }
    public Location getStartLocation() { return startLocation.clone(); }
    @Override
    public long getStartTime() { return startTime; }
    @Override
    public String getStatus() { return status.name(); }
    public Map<String, Object> getSessionData() { return new HashMap<>(sessionData); }

    /**
     * Add player to session
     */
    public boolean addPlayer(Player player, DungeonClass dungeonClass) {
        if (players.size() >= floor.getMaxPlayers()) {
            return false; // Session is full
        }
        
        if (players.containsKey(player.getUniqueId())) {
            return false; // Player already in session
        }
        
        DungeonPlayer dungeonPlayer = new DungeonPlayer(player, dungeonClass);
        players.put(player.getUniqueId(), dungeonPlayer);
        return true;
    }

    /**
     * Remove player from session
     */
    public boolean removePlayer(UUID playerId) {
        return playerMap.remove(playerId) != null;
    }

    /**
     * Get player from session
     */
    public DungeonPlayer getPlayer(UUID playerId) {
        return players.get(playerId);
    }

    /**
     * Get player count
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Check if session is full
     */
    public boolean isFull() {
        return players.size() >= floor.getMaxPlayers();
    }

    /**
     * Check if session has minimum players
     */
    public boolean hasMinimumPlayers() {
        return players.size() >= floor.getMinPlayers();
    }

    /**
     * Get session duration
     */
    public long getSessionDuration() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Get session duration in seconds
     */
    public long getSessionDurationSeconds() {
        return getSessionDuration() / 1000;
    }

    /**
     * Get session duration in minutes
     */
    public long getSessionDurationMinutes() {
        return getSessionDurationSeconds() / 60;
    }

    /**
     * Get formatted session duration
     */
    public String getFormattedDuration() {
        long minutes = getSessionDurationMinutes();
        long seconds = getSessionDurationSeconds() % 60;
        
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }

    /**
     * Get session progress percentage
     */
    public double getProgressPercentage() {
        // This would be calculated based on dungeon progress
        // For now, return a placeholder
        return 0.0;
    }

    /**
     * Get session statistics
     */
    public DungeonSessionStatistics getSessionStatistics() {
        return new DungeonSessionStatistics(this);
    }

    /**
     * Check if player is in session
     */
    public boolean containsPlayer(UUID playerId) {
        return players.containsKey(playerId);
    }

    /**
     * Get all online players
     */
    public List<Player> getOnlinePlayers() {
        List<Player> onlinePlayers = new ArrayList<>();
        for (DungeonPlayer dungeonPlayer : players.values()) {
            Player player = dungeonPlayer.getPlayer();
            if (player != null && player.isOnline()) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    /**
     * Get players by class
     */
    public Map<DungeonClass, List<DungeonPlayer>> getPlayersByClass() {
        Map<DungeonClass, List<DungeonPlayer>> playersByClass = new HashMap<>();
        
        for (DungeonPlayer dungeonPlayer : players.values()) {
            DungeonClass dungeonClass = dungeonPlayer.getDungeonClass();
            playersByClass.computeIfAbsent(dungeonClass, k -> new ArrayList<>()).add(dungeonPlayer);
        }
        
        return playersByClass;
    }

    /**
     * Get class distribution
     */
    public Map<DungeonClass, Integer> getClassDistribution() {
        Map<DungeonClass, Integer> distribution = new HashMap<>();
        
        for (DungeonPlayer dungeonPlayer : players.values()) {
            DungeonClass dungeonClass = dungeonPlayer.getDungeonClass();
            distribution.put(dungeonClass, distribution.getOrDefault(dungeonClass, 0) + 1);
        }
        
        return distribution;
    }

    /**
     * Get session summary
     */
    public String[] getSessionSummary() {
        return new String[]{
            "&7Dungeon Session: " + floor.getColoredDisplayName(),
            "&7Players: &a" + getPlayerCount() + "/" + floor.getMaxPlayers(),
            "&7Duration: &a" + getFormattedDuration(),
            "&7Status: " + getStatusColor() + status.getDisplayName(),
            "&7Progress: &a" + String.format("%.1f", getProgressPercentage()) + "%"
        };
    }

    /**
     * Get status color
     */
    private String getStatusColor() {
        switch (status) {
            case WAITING: return "&e";
            case STARTING: return "&a";
            case IN_PROGRESS: return "&b";
            case COMPLETED: return "&a";
            case FAILED: return "&c";
            case CANCELLED: return "&7";
            default: return "&7";
        }
    }

    /**
     * Dungeon session statistics inner class
     */
    public static class DungeonSessionStatistics {
        private final DungeonSession session;
        private final int totalPlayers;
        private final long duration;
        private final double progress;
        private final Map<DungeonClass, Integer> classDistribution;

        public DungeonSessionStatistics(DungeonSession session) {
            this.session = session;
            this.totalPlayers = session.getPlayerCount();
            this.duration = session.getSessionDuration();
            this.progress = session.getProgressPercentage();
            this.classDistribution = session.getClassDistribution();
        }

        // Getters
        public DungeonSession getSession() { return session; }
        public int getTotalPlayers() { return totalPlayers; }
        public long getDuration() { return duration; }
        public double getProgress() { return progress; }
        public Map<DungeonClass, Integer> getClassDistribution() { return new HashMap<>(classDistribution); }

        /**
         * Get statistics summary
         */
        public String[] getStatisticsSummary() {
            return new String[]{
                "&7Session Statistics:",
                "",
                "&7Floor: " + session.getFloor().getColoredDisplayName(),
                "&7Players: &a" + totalPlayers,
                "&7Duration: &a" + session.getFormattedDuration(),
                "&7Progress: &a" + String.format("%.1f", progress) + "%",
                "",
                "&7Class Distribution:"
            };
        }
    }

    /**
     * Enum for dungeon session status
     */
    public enum DungeonSessionStatus {
        WAITING("Waiting", "Waiting for players to join"),
        STARTING("Starting", "Session is starting"),
        IN_PROGRESS("In Progress", "Dungeon is in progress"),
        COMPLETED("Completed", "Dungeon completed successfully"),
        FAILED("Failed", "Dungeon failed"),
        CANCELLED("Cancelled", "Session was cancelled");

        private final String displayName;
        private final String description;

        DungeonSessionStatus(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}
