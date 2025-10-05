package de.noctivag.skyblock.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import de.noctivag.skyblock.dungeons.bosses.TerminalType;

/**
 * Represents a terminal in the Necron boss fight
 */
public class Terminal {
    private final Location location;
    private final TerminalType type;
    private boolean active;
    private boolean completed;
    private final List<Player> participants;
    private long startTime;
    private final long timeLimit = 30000; // 30 seconds

    public Terminal(Location location, TerminalType type) {
        this.location = location;
        this.type = type;
        this.active = false;
        this.completed = false;
        this.participants = new ArrayList<>();
        this.startTime = 0;
    }

    public Location getLocation() {
        return location;
    }

    public TerminalType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.startTime = java.lang.System.currentTimeMillis();
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.active = false;
    }

    public List<Player> getParticipants() {
        return new ArrayList<>(participants);
    }

    public void addParticipant(Player player) {
        if (!participants.contains(player)) {
            participants.add(player);
        }
    }

    public void removeParticipant(Player player) {
        participants.remove(player);
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public long getRemainingTime() {
        if (!active) return 0;
        long elapsed = java.lang.System.currentTimeMillis() - startTime;
        return Math.max(0, timeLimit - elapsed);
    }

    public boolean isTimeUp() {
        return active && getRemainingTime() <= 0;
    }

    public void start() {
        this.active = true;
        this.completed = false;
        this.startTime = java.lang.System.currentTimeMillis();
        this.participants.clear();
    }

    public void complete() {
        this.completed = true;
        this.active = false;
    }

    public void fail() {
        this.active = false;
        this.completed = false;
    }

    public void update() {
        // TODO: Implement terminal update logic
    }
}
