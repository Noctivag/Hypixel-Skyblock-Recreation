package de.noctivag.skyblock.features.events.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.events.EventReward;
import java.util.UUID;

/**
 * Player Event Participation class for event participation tracking
 */
public class PlayerEventParticipation {
    private UUID playerId;
    private String eventId;
    private long startTime;
    private long endTime;
    private int score;
    private int rank;
    private boolean isActive;
    private EventReward[] rewards;

    public PlayerEventParticipation(UUID playerId, String eventId) {
        this.playerId = playerId;
        this.eventId = eventId;
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
        this.score = 0;
        this.rank = 0;
        this.isActive = true;
        this.rewards = new EventReward[0];
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getEventId() {
        return eventId;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public EventReward[] getRewards() {
        return rewards;
    }

    public void setRewards(EventReward[] rewards) {
        this.rewards = rewards;
    }

    public long getDuration() {
        if (endTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }
}
