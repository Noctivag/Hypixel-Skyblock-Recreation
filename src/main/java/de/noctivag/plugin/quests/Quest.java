package de.noctivag.plugin.quests;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Quest - Represents a quest in the game
 */
public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private final QuestType type;
    private final QuestDifficulty difficulty;
    private final int level;
    private final List<QuestObjective> objectives;
    private final List<QuestReward> rewards;
    private final long timeLimit;
    private final boolean repeatable;
    
    public Quest(String id, String name, String description, QuestType type, 
                QuestDifficulty difficulty, int level, List<QuestObjective> objectives, 
                List<QuestReward> rewards, long timeLimit, boolean repeatable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.level = level;
        this.objectives = objectives;
        this.rewards = rewards;
        this.timeLimit = timeLimit;
        this.repeatable = repeatable;
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
    
    public QuestType getType() {
        return type;
    }
    
    public QuestDifficulty getDifficulty() {
        return difficulty;
    }
    
    public int getLevel() {
        return level;
    }
    
    public List<QuestObjective> getObjectives() {
        return objectives;
    }
    
    public List<QuestReward> getRewards() {
        return rewards;
    }
    
    public long getTimeLimit() {
        return timeLimit;
    }
    
    public boolean isRepeatable() {
        return repeatable;
    }
    
    public enum QuestType {
        KILL, COLLECT, CRAFT, EXPLORE, DELIVER, ESCORT, SURVIVE, BUILD
    }
    
    public enum QuestDifficulty {
        EASY, MEDIUM, HARD, EXPERT, MASTER
    }
    
    public enum QuestCategory {
        MAIN, SIDE, DAILY, WEEKLY, EVENT, SPECIAL
    }
}
