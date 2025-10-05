package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Brewing API for external plugins
 */
public class BrewingAPI {
    
    private final SkyblockPlugin plugin;
    
    public BrewingAPI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Register a custom brewing recipe
     */
    public void registerRecipe(BrewingRecipe recipe) {
        plugin.getBrewingManager().getBrewingSystem().addRecipe(recipe);
    }
    
    /**
     * Unregister a brewing recipe
     */
    public void unregisterRecipe(String recipeId) {
        plugin.getBrewingManager().getBrewingSystem().removeRecipe(recipeId);
    }
    
    /**
     * Get all brewing recipes
     */
    public Map<String, BrewingRecipe> getRecipes() {
        return plugin.getBrewingManager().getBrewingSystem().getRecipes();
    }
    
    /**
     * Get brewing recipe by ID
     */
    public BrewingRecipe getRecipe(String recipeId) {
        return plugin.getBrewingManager().getBrewingSystem().getRecipe(recipeId);
    }
    
    /**
     * Check if recipe exists
     */
    public boolean hasRecipe(String recipeId) {
        return plugin.getBrewingManager().getBrewingSystem().hasRecipe(recipeId);
    }
    
    /**
     * Start brewing for player
     */
    public boolean startBrewing(Player player, String recipeId) {
        return plugin.getBrewingManager().startBrewing(player, recipeId);
    }
    
    /**
     * Get player brewing data
     */
    public PlayerBrewingData getPlayerBrewingData(Player player) {
        return plugin.getBrewingManager().getPlayerBrewingData(player.getUniqueId());
    }
    
    /**
     * Check if player can brew recipe
     */
    public boolean canBrew(Player player, String recipeId) {
        BrewingRecipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        
        // Check if player has required ingredients
        for (ItemStack ingredient : recipe.getIngredients()) {
            if (!player.getInventory().containsAtLeast(ingredient, ingredient.getAmount())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get recipes by ingredient
     */
    public List<BrewingRecipe> getRecipesByIngredient(org.bukkit.Material ingredient) {
        return plugin.getBrewingManager().getBrewingSystem().getRecipesByIngredient(ingredient);
    }
}
