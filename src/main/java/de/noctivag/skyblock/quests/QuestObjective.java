package de.noctivag.skyblock.quests;
import org.bukkit.inventory.ItemStack;

/**
 * QuestObjective - Represents an objective within a quest
 */
public class QuestObjective {
    private final String id;
    private final String description;
    private final QuestObjectiveType type;
    private final int requiredAmount;
    private final String target;
    private final boolean optional;
    
    public QuestObjective(String id, String description, QuestObjectiveType type, 
                         int requiredAmount, String target, boolean optional) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.requiredAmount = requiredAmount;
        this.target = target;
        this.optional = optional;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public QuestObjectiveType getType() {
        return type;
    }
    
    public int getRequiredAmount() {
        return requiredAmount;
    }
    
    public String getTarget() {
        return target;
    }
    
    public boolean isOptional() {
        return optional;
    }
    
    public enum QuestObjectiveType {
        KILL_MOB, KILL_PLAYER, COLLECT_ITEM, CRAFT_ITEM, REACH_LOCATION,
        DELIVER_ITEM, ESCORT_NPC, SURVIVE_TIME, BUILD_STRUCTURE, USE_ITEM
    }
}
