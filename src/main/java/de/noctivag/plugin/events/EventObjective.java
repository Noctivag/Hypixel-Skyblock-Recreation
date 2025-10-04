package de.noctivag.plugin.events;
import org.bukkit.inventory.ItemStack;

/**
 * EventObjective - Represents an objective within an event
 */
public class EventObjective {
    private final String id;
    private final String description;
    private final EventObjectiveType type;
    private final int requiredAmount;
    private final String target;
    private final int score;
    private final boolean optional;
    
    public EventObjective(String id, String description, EventObjectiveType type, 
                         int requiredAmount, String target, int score, boolean optional) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.requiredAmount = requiredAmount;
        this.target = target;
        this.score = score;
        this.optional = optional;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public EventObjectiveType getType() {
        return type;
    }
    
    public int getRequiredAmount() {
        return requiredAmount;
    }
    
    public String getTarget() {
        return target;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isOptional() {
        return optional;
    }
    
    public enum EventObjectiveType {
        KILL_MOB, KILL_PLAYER, COLLECT_ITEM, REACH_LOCATION, SURVIVE_TIME,
        BUILD_STRUCTURE, COMPLETE_TASK, ACHIEVE_SCORE, TEAM_WORK
    }
}
