package de.noctivag.plugin.events;
import org.bukkit.inventory.ItemStack;


/**
 * EventReward - Represents a reward for participating in an event
 */
public class EventReward {
    private final EventRewardType type;
    private final String value;
    private final int amount;
    private final ItemStack item;
    private final String description;
    private final int minScore;
    
    public EventReward(EventRewardType type, String value, int amount, ItemStack item, 
                      String description, int minScore) {
        this.type = type;
        this.value = value;
        this.amount = amount;
        this.item = item;
        this.description = description;
        this.minScore = minScore;
    }
    
    public EventRewardType getType() {
        return type;
    }
    
    public String getValue() {
        return value;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMinScore() {
        return minScore;
    }
    
    public enum EventRewardType {
        EXPERIENCE, COINS, ITEM, COMMAND, PERMISSION, TITLE, ACHIEVEMENT, SCORE
    }
}
