package de.noctivag.skyblock.recipe;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

/**
 * Represents a Skyblock recipe
 */
public class SkyblockRecipe {
    
    private final String id;
    private final String name;
    private final RecipeCategory category;
    private final Material result;
    private final int amount;
    private final List<String> shape;
    private final Map<Character, Material> ingredients;
    private final int unlockLevel;
    private final String unlockRequirement;
    
    public SkyblockRecipe(String id, String name, RecipeCategory category, Material result, int amount,
                         List<String> shape, Map<Character, Material> ingredients) {
        this(id, name, category, result, amount, shape, ingredients, 1, null);
    }
    
    public SkyblockRecipe(String id, String name, RecipeCategory category, Material result, int amount,
                         List<String> shape, Map<Character, Material> ingredients, int unlockLevel, String unlockRequirement) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.result = result;
        this.amount = amount;
        this.shape = shape;
        this.ingredients = ingredients;
        this.unlockLevel = unlockLevel;
        this.unlockRequirement = unlockRequirement;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public RecipeCategory getCategory() { return category; }
    public Material getResult() { return result; }
    public int getAmount() { return amount; }
    public List<String> getShape() { return shape; }
    public Map<Character, Material> getIngredients() { return ingredients; }
    public int getUnlockLevel() { return unlockLevel; }
    public String getUnlockRequirement() { return unlockRequirement; }
    
    /**
     * Check if this recipe is unlocked by default
     */
    public boolean isUnlockedByDefault() {
        return unlockLevel <= 1 && unlockRequirement == null;
    }
    
    /**
     * Get the display name with category color
     */
    public String getDisplayName() {
        return category.getColor() + name;
    }
    
    /**
     * Get the unlock requirement description
     */
    public String getUnlockDescription() {
        if (isUnlockedByDefault()) {
            return "§aUnlocked by default";
        }
        
        StringBuilder desc = new StringBuilder();
        if (unlockLevel > 1) {
            desc.append("§7Requires Level ").append(unlockLevel);
        }
        if (unlockRequirement != null) {
            if (desc.length() > 0) desc.append(" and ");
            desc.append("§7").append(unlockRequirement);
        }
        
        return desc.toString();
    }
    
    /**
     * Get the recipe description
     */
    public String getDescription() {
        return "§7Craft " + amount + "x " + result.name().toLowerCase().replace("_", " ");
    }
}
