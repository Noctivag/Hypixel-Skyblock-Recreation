package de.noctivag.skyblock.engine.collections.types;

import de.noctivag.skyblock.engine.collections.CollectionRewardType;

/**
 * Collection Reward
 * 
 * Represents a reward that can be unlocked through collection milestones.
 * Rewards can be SkyBlock XP, recipes, stat bonuses, or other benefits.
 */
public class CollectionReward {
    
    private final CollectionRewardType type;
    private final int amount;
    private final String description;
    private final String itemName; // For recipe rewards
    
    public CollectionReward(CollectionRewardType type, int amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.itemName = null;
    }
    
    public CollectionReward(CollectionRewardType type, int amount, String description, String itemName) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.itemName = itemName;
    }
    
    public CollectionRewardType getType() {
        return type;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    /**
     * Get formatted reward description
     */
    public String getFormattedDescription() {
        return switch (type) {
            case SKYBLOCK_XP -> String.format("§a+%d SkyBlock XP", amount);
            case RECIPE -> String.format("§e%s Recipe", description);
            case STAT_BONUS -> String.format("§b+%d %s", amount, description);
            case COIN_BONUS -> String.format("§6+%d Coins", amount);
            case ITEM_REWARD -> String.format("§d%s x%d", description, amount);
            case UNLOCK -> String.format("§5%s Unlocked", description);
            case PERMISSION -> String.format("§3%s Permission", description);
            case TITLE -> String.format("§9%s Title", description);
            case ACHIEVEMENT -> String.format("§c%s Achievement", description);
            case COSMETIC -> String.format("§f%s Cosmetic", description);
        };
    }
    
    /**
     * Get reward icon based on type
     */
    public String getIcon() {
        return switch (type) {
            case SKYBLOCK_XP -> "⭐";
            case RECIPE -> "📜";
            case STAT_BONUS -> "⚡";
            case COIN_BONUS -> "💰";
            case ITEM_REWARD -> "🎁";
            case UNLOCK -> "🔓";
            case PERMISSION -> "🔑";
            case TITLE -> "👑";
            case ACHIEVEMENT -> "🏆";
            case COSMETIC -> "✨";
        };
    }
    
    /**
     * Get reward color based on type
     */
    public String getColor() {
        return switch (type) {
            case SKYBLOCK_XP -> "§a"; // Green
            case RECIPE -> "§e"; // Yellow
            case STAT_BONUS -> "§b"; // Aqua
            case COIN_BONUS -> "§6"; // Gold
            case ITEM_REWARD -> "§d"; // Light Purple
            case UNLOCK -> "§5"; // Dark Purple
            case PERMISSION -> "§3"; // Dark Aqua
            case TITLE -> "§9"; // Blue
            case ACHIEVEMENT -> "§c"; // Red
            case COSMETIC -> "§f"; // White
        };
    }
    
    /**
     * Get formatted reward with icon and color
     */
    public String getFormattedReward() {
        return getColor() + getIcon() + " " + getFormattedDescription();
    }
    
    /**
     * Check if this reward is a recipe
     */
    public boolean isRecipe() {
        return type == CollectionRewardType.RECIPE;
    }
    
    /**
     * Check if this reward is a stat bonus
     */
    public boolean isStatBonus() {
        return type == CollectionRewardType.STAT_BONUS;
    }
    
    /**
     * Check if this reward is SkyBlock XP
     */
    public boolean isSkyBlockXP() {
        return type == CollectionRewardType.SKYBLOCK_XP;
    }
    
    /**
     * Check if this reward is a coin bonus
     */
    public boolean isCoinBonus() {
        return type == CollectionRewardType.COIN_BONUS;
    }
    
    /**
     * Check if this reward is an item
     */
    public boolean isItemReward() {
        return type == CollectionRewardType.ITEM_REWARD;
    }
    
    /**
     * Check if this reward is an unlock
     */
    public boolean isUnlock() {
        return type == CollectionRewardType.UNLOCK;
    }
    
    /**
     * Check if this reward is a permission
     */
    public boolean isPermission() {
        return type == CollectionRewardType.PERMISSION;
    }
    
    /**
     * Check if this reward is a title
     */
    public boolean isTitle() {
        return type == CollectionRewardType.TITLE;
    }
    
    /**
     * Check if this reward is an achievement
     */
    public boolean isAchievement() {
        return type == CollectionRewardType.ACHIEVEMENT;
    }
    
    /**
     * Check if this reward is a cosmetic
     */
    public boolean isCosmetic() {
        return type == CollectionRewardType.COSMETIC;
    }
    
    /**
     * Get reward value (for sorting/prioritizing)
     */
    public int getRewardValue() {
        return switch (type) {
            case SKYBLOCK_XP -> amount * 10; // XP is valuable
            case RECIPE -> amount * 100; // Recipes are very valuable
            case STAT_BONUS -> amount * 50; // Stat bonuses are valuable
            case COIN_BONUS -> amount; // Coins are standard
            case ITEM_REWARD -> amount * 25; // Items are moderately valuable
            case UNLOCK -> amount * 75; // Unlocks are valuable
            case PERMISSION -> amount * 60; // Permissions are valuable
            case TITLE -> amount * 30; // Titles are moderately valuable
            case ACHIEVEMENT -> amount * 40; // Achievements are moderately valuable
            case COSMETIC -> amount * 20; // Cosmetics are less valuable
        };
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CollectionReward that = (CollectionReward) obj;
        return type == that.type && 
               amount == that.amount && 
               description.equals(that.description);
    }
    
    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + amount;
        result = 31 * result + description.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("CollectionReward{type=%s, amount=%d, description='%s', itemName='%s'}", 
            type, amount, description, itemName);
    }
}
