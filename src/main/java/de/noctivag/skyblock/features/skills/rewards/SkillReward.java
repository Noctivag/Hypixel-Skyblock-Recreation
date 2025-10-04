package de.noctivag.skyblock.features.skills.rewards;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.skills.types.SkillType;

import java.util.UUID;

/**
 * Represents a skill reward
 */
public class SkillReward {
    
    private final String id;
    private final String name;
    private final String description;
    private final SkillType skillType;
    private final int requiredLevel;
    private final RewardType type;
    private final double amount;
    private final String itemId;
    private final String statType;
    private final String recipeId;
    
    public SkillReward(String id, String name, String description, SkillType skillType, 
                      int requiredLevel, RewardType type, double amount) {
        this(id, name, description, skillType, requiredLevel, type, amount, null, null, null);
    }
    
    public SkillReward(String id, String name, String description, SkillType skillType, 
                      int requiredLevel, RewardType type, double amount, String itemId) {
        this(id, name, description, skillType, requiredLevel, type, amount, itemId, null, null);
    }
    
    public SkillReward(String id, String name, String description, SkillType skillType, 
                      int requiredLevel, RewardType type, double amount, String statType, String itemId) {
        this(id, name, description, skillType, requiredLevel, type, amount, itemId, statType, null);
    }
    
    public SkillReward(String id, String name, String description, SkillType skillType, 
                      int requiredLevel, RewardType type, double amount, String itemId, 
                      String statType, String recipeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.skillType = skillType;
        this.requiredLevel = requiredLevel;
        this.type = type;
        this.amount = amount;
        this.itemId = itemId;
        this.statType = statType;
        this.recipeId = recipeId;
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
    
    public SkillType getSkillType() {
        return skillType;
    }
    
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    public RewardType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getItemId() {
        return itemId;
    }
    
    public String getStatType() {
        return statType;
    }
    
    public String getRecipeId() {
        return recipeId;
    }
    
    /**
     * Get reward display text
     */
    public String getDisplayText() {
        return switch (type) {
            case COINS -> "💰 " + (int) amount + " Coins";
            case ITEM -> "🎁 " + itemId + " x" + (int) amount;
            case STAT_BOOST -> "⚡ " + statType + " +" + amount;
            case RECIPE -> "📜 Recipe: " + recipeId;
            case ACCESSORY -> "💍 " + itemId;
            case PET -> "🐾 Pet: " + itemId;
        };
    }
    
    @Override
    public String toString() {
        return String.format("%s (Level %d) - %s", name, requiredLevel, description);
    }
}
