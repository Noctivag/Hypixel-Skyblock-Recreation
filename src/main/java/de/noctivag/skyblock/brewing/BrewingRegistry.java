package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Brewing recipe registry
 */
public class BrewingRegistry {
    
    private final SkyblockPlugin plugin;
    private final Map<String, BrewingRecipe> recipes = new ConcurrentHashMap<>();
    
    public BrewingRegistry(SkyblockPlugin plugin) {
        this.plugin = plugin;
        registerDefaultRecipes();
    }
    
    /**
     * Register default brewing recipes
     */
    private void registerDefaultRecipes() {
        // Speed Potion
        List<ItemStack> speedIngredients = new ArrayList<>();
        speedIngredients.add(new ItemStack(Material.NETHER_WART));
        speedIngredients.add(new ItemStack(Material.SUGAR));
        
        BrewingRecipe speedPotion = new BrewingRecipe(
            "speed_potion",
            "Speed Potion",
            speedIngredients,
            new ItemStack(Material.POTION),
            200,
            10
        );
        
        registerRecipe(speedPotion);
        
        // Strength Potion
        List<ItemStack> strengthIngredients = new ArrayList<>();
        strengthIngredients.add(new ItemStack(Material.NETHER_WART));
        strengthIngredients.add(new ItemStack(Material.BLAZE_POWDER));
        
        BrewingRecipe strengthPotion = new BrewingRecipe(
            "strength_potion",
            "Strength Potion",
            strengthIngredients,
            new ItemStack(Material.POTION),
            200,
            10
        );
        
        registerRecipe(strengthPotion);
        
        // Healing Potion
        List<ItemStack> healingIngredients = new ArrayList<>();
        healingIngredients.add(new ItemStack(Material.NETHER_WART));
        healingIngredients.add(new ItemStack(Material.GLISTERING_MELON_SLICE));
        
        BrewingRecipe healingPotion = new BrewingRecipe(
            "healing_potion",
            "Healing Potion",
            healingIngredients,
            new ItemStack(Material.POTION),
            200,
            10
        );
        
        registerRecipe(healingPotion);
        
        plugin.getLogger().info("Registered " + recipes.size() + " default brewing recipes");
    }
    
    /**
     * Register a brewing recipe
     */
    public void registerRecipe(BrewingRecipe recipe) {
        recipes.put(recipe.getId(), recipe);
        plugin.getLogger().log(Level.INFO, "Registered brewing recipe: " + recipe.getName());
    }
    
    /**
     * Unregister a brewing recipe
     */
    public void unregisterRecipe(String recipeId) {
        BrewingRecipe removed = recipes.remove(recipeId);
        if (removed != null) {
            plugin.getLogger().log(Level.INFO, "Unregistered brewing recipe: " + removed.getName());
        }
    }
    
    /**
     * Get brewing recipe by ID
     */
    public BrewingRecipe getRecipe(String recipeId) {
        return recipes.get(recipeId);
    }
    
    /**
     * Get all brewing recipes
     */
    public Map<String, BrewingRecipe> getRecipes() {
        return new ConcurrentHashMap<>(recipes);
    }
    
    /**
     * Check if recipe exists
     */
    public boolean hasRecipe(String recipeId) {
        return recipes.containsKey(recipeId);
    }
    
    /**
     * Get recipes by ingredient
     */
    public List<BrewingRecipe> getRecipesByIngredient(Material ingredient) {
        List<BrewingRecipe> result = new ArrayList<>();
        for (BrewingRecipe recipe : recipes.values()) {
            for (ItemStack item : recipe.getIngredients()) {
                if (item.getType() == ingredient) {
                    result.add(recipe);
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * Get total recipe count
     */
    public int getRecipeCount() {
        return recipes.size();
    }
    
    /**
     * Clear all recipes
     */
    public void clearRecipes() {
        recipes.clear();
        plugin.getLogger().info("Cleared all brewing recipes");
    }
}
