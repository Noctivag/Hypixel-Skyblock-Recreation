package de.noctivag.skyblock.features.dungeons.loot;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a dungeon reward
 */
public class DungeonReward {
    
    private final DungeonRewardType type;
    private final int amount;
    private final DungeonItem item;
    private final int minScore;
    private final double chance;
    
    public DungeonReward(DungeonRewardType type, int amount) {
        this(type, amount, null, 0, 1.0);
    }
    
    public DungeonReward(DungeonRewardType type, int amount, DungeonItem item) {
        this(type, amount, item, 0, 1.0);
    }
    
    public DungeonReward(DungeonRewardType type, int amount, int minScore, double chance) {
        this(type, amount, null, minScore, chance);
    }
    
    public DungeonReward(DungeonRewardType type, int amount, DungeonItem item, int minScore, double chance) {
        this.type = type;
        this.amount = amount;
        this.item = item;
        this.minScore = minScore;
        this.chance = chance;
    }
    
    public DungeonRewardType getType() {
        return type;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public DungeonItem getItem() {
        return item;
    }
    
    public int getMinScore() {
        return minScore;
    }
    
    public double getChance() {
        return chance;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.getIcon()).append(" ");
        
        if (item != null) {
            sb.append(item.getName());
            if (amount > 1) {
                sb.append(" x").append(amount);
            }
        } else {
            sb.append(type.getDisplayName()).append(": ").append(amount);
        }
        
        return sb.toString();
    }
}
