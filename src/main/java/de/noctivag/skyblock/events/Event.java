package de.noctivag.skyblock.events;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Event - Represents an event in the game
 */
public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final EventType type;
    private final EventDifficulty difficulty;
    private final int level;
    private final List<EventObjective> objectives;
    private final List<EventReward> rewards;
    private final long duration;
    private final boolean repeatable;
    private org.bukkit.entity.Entity boss;
    private final List<UUID> players = new ArrayList<>();
    private boolean active = false;
    private final String arenaId;
    private final int maxPlayers;
    private final int minPlayers;
    private final double cost;
    
    public Event(String id, String name, String description, EventType type, 
                EventDifficulty difficulty, int level, List<EventObjective> objectives, 
                List<EventReward> rewards, long duration, boolean repeatable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.level = level;
        this.objectives = objectives;
        this.rewards = rewards;
        this.duration = duration;
        this.repeatable = repeatable;
        this.arenaId = "default_arena";
        this.maxPlayers = 10;
        this.minPlayers = 2;
        this.cost = 0.0;
    }
    
    public Event(String id, String name, String arenaId) {
        this.id = id;
        this.name = name;
        this.description = "Boss fight event";
        this.type = EventType.BOSS_FIGHT;
        this.difficulty = EventDifficulty.MEDIUM;
        this.level = 1;
        this.objectives = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.duration = 300000; // 5 minutes
        this.repeatable = true;
        this.arenaId = arenaId;
        this.maxPlayers = 10;
        this.minPlayers = 2;
        this.cost = 0.0;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public EventType getType() {
        return type;
    }
    
    public EventDifficulty getDifficulty() {
        return difficulty;
    }
    
    public int getLevel() {
        return level;
    }
    
    public List<EventObjective> getObjectives() {
        return objectives;
    }
    
    public EventObjective getObjective(String objectiveId) {
        return objectives.stream()
                .filter(obj -> obj.getId().equals(objectiveId))
                .findFirst()
                .orElse(null);
    }
    
    public List<EventReward> getRewards() {
        return rewards;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public boolean isRepeatable() {
        return repeatable;
    }
    
    public List<UUID> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public double getCost() {
        return cost;
    }
    
    public void addPlayer(org.bukkit.entity.Player player) {
        if (!players.contains(player.getUniqueId()) && players.size() < maxPlayers) {
            players.add(player.getUniqueId());
        }
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setBoss(org.bukkit.entity.Blaze boss) {
        this.boss = boss;
    }
    
    public String getArenaId() {
        return arenaId;
    }
    
    public int getMinPlayers() {
        return minPlayers;
    }
    
    public void removePlayer(org.bukkit.entity.Player player) {
        players.remove(player.getUniqueId());
    }
    
    public int getRequiredLevel() {
        return level;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setBoss(org.bukkit.entity.Entity boss) {
        this.boss = boss;
    }
    
    public org.bukkit.entity.Entity getBoss() {
        return this.boss;
    }
    
    public List<EventReward> getReward() {
        return this.rewards;
    }
    
    public enum EventType {
        BOSS_FIGHT, PVP, RACE, BUILDING, COLLECTION, SURVIVAL, PUZZLE, COOPERATIVE
    }
    
    public enum EventDifficulty {
        EASY, MEDIUM, HARD, EXPERT, MASTER
    }
    
    public enum EventCategory {
        COMBAT, SOCIAL, CREATIVE, COMPETITIVE, COOPERATIVE, SEASONAL
    }
}
