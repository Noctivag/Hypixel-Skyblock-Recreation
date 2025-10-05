package de.noctivag.skyblock.features.dungeons.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Dungeon Instance class for dungeon runs
 */
public class DungeonInstance {
    private String id;
    private String dungeonType;
    private String floor;
    private List<UUID> players;
    private DungeonClassType playerClass;
    private long startTime;
    private long endTime;
    private int score;
    private boolean isCompleted;
    private boolean isFailed;

    public DungeonInstance(String id, String dungeonType, String floor, List<UUID> players, DungeonClassType playerClass) {
        this.id = id;
        this.dungeonType = dungeonType;
        this.floor = floor;
        this.players = players;
        this.playerClass = playerClass;
        this.startTime = java.lang.System.currentTimeMillis();
        this.endTime = 0;
        this.score = 0;
        this.isCompleted = false;
        this.isFailed = false;
    }

    public String getId() {
        return id;
    }

    public String getDungeonType() {
        return dungeonType;
    }

    public String getFloor() {
        return floor;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public DungeonClassType getPlayerClass() {
        return playerClass;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    public long getDuration() {
        if (endTime == 0) {
            return java.lang.System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }
}
