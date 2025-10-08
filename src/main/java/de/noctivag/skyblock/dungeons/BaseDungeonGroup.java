package de.noctivag.skyblock.dungeons;

import java.util.List;
import java.util.UUID;

/**
 * Basisklasse für Dungeon-Instanzen und -Sessions
 */
public abstract class BaseDungeonGroup {
    protected String groupId;
    protected String groupType;
    protected long startTime;
    protected List<UUID> players;
    protected String status;

    public BaseDungeonGroup(String groupId, String groupType, long startTime, List<UUID> players, String status) {
        this.groupId = groupId;
        this.groupType = groupType;
        this.startTime = startTime;
        this.players = players;
        this.status = status;
    }

    public String getGroupId() { return groupId; }
    public String getGroupType() { return groupType; }
    public long getStartTime() { return startTime; }
    public List<UUID> getPlayers() { return players; }
    public String getStatus() { return status; }
}
