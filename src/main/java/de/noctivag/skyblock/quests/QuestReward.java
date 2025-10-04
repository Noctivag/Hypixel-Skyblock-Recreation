package de.noctivag.skyblock.quests;
import org.bukkit.inventory.ItemStack;


/**
 * QuestReward - Represents a reward for completing a quest
 */
public class QuestReward {
    private final QuestRewardType type;
    private final String value;
    private final int amount;
    private final ItemStack item;
    private final String description;
    
    public QuestReward(QuestRewardType type, String value, int amount, ItemStack item, String description) {
        this.type = type;
        this.value = value;
        this.amount = amount;
        this.item = item;
        this.description = description;
    }
    
    public QuestRewardType getType() {
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
    
    public enum QuestRewardType {
        EXPERIENCE, COINS, ITEM, COMMAND, PERMISSION, TITLE, ACHIEVEMENT
    }
}
