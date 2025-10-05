package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Brewing system for managing custom brewing recipes
 */
public class BrewingSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, BrewingRecipe> recipes = new ConcurrentHashMap<>();
    
    public BrewingSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing BrewingSystem...");
        
        // Load brewing recipes
        loadBrewingRecipes();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("BrewingSystem initialized with " + recipes.size() + " recipes.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down BrewingSystem...");
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("BrewingSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "BrewingSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load brewing recipes
     */
    private void loadBrewingRecipes() {
        // Example recipe
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.add(new ItemStack(Material.NETHER_WART));
        ingredients.add(new ItemStack(Material.GLOWSTONE_DUST));
        
        BrewingRecipe recipe = new BrewingRecipe(
            "speed_potion",
            "Speed Potion",
            ingredients,
            new ItemStack(Material.POTION),
            200, // 10 seconds
            10
        );
        
        recipes.put("speed_potion", recipe);
        plugin.getLogger().info("Loaded brewing recipe: " + recipe.getName());
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
     * Add brewing recipe
     */
    public void addRecipe(BrewingRecipe recipe) {
        recipes.put(recipe.getId(), recipe);
        plugin.getLogger().info("Added brewing recipe: " + recipe.getName());
    }
    
    /**
     * Remove brewing recipe
     */
    public void removeRecipe(String recipeId) {
        BrewingRecipe removed = recipes.remove(recipeId);
        if (removed != null) {
            plugin.getLogger().info("Removed brewing recipe: " + removed.getName());
        }
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
}
