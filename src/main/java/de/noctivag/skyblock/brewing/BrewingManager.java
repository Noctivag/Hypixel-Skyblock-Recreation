package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Brewing manager for handling player brewing operations
 */
public class BrewingManager implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, PlayerBrewingData> playerData = new ConcurrentHashMap<>();
    private final BrewingSystem brewingSystem;
    
    public BrewingManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.brewingSystem = new BrewingSystem(plugin);
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing BrewingManager...");
        
        // Initialize brewing system
        brewingSystem.initialize();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("BrewingManager initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down BrewingManager...");
        
        // Shutdown brewing system
        brewingSystem.shutdown();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("BrewingManager shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "BrewingManager";
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
     * Get player brewing data
     */
    public PlayerBrewingData getPlayerBrewingData(UUID playerUuid) {
        return playerData.computeIfAbsent(playerUuid, PlayerBrewingData::new);
    }
    
    /**
     * Start brewing for player
     */
    public boolean startBrewing(Player player, String recipeId) {
        BrewingRecipe recipe = brewingSystem.getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        
        // Check if player has required ingredients
        if (!hasRequiredIngredients(player, recipe)) {
            player.sendMessage("§cYou don't have the required ingredients!");
            return false;
        }
        
        // Remove ingredients
        removeIngredients(player, recipe);
        
        // Start brewing process
        // Implementation would start a brewing task
        
        player.sendMessage("§aStarted brewing: " + recipe.getName());
        plugin.getLogger().info("Player " + player.getName() + " started brewing: " + recipe.getName());
        return true;
    }
    
    /**
     * Check if player has required ingredients
     */
    private boolean hasRequiredIngredients(Player player, BrewingRecipe recipe) {
        for (ItemStack ingredient : recipe.getIngredients()) {
            if (!player.getInventory().containsAtLeast(ingredient, ingredient.getAmount())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Remove ingredients from player inventory
     */
    private void removeIngredients(Player player, BrewingRecipe recipe) {
        for (ItemStack ingredient : recipe.getIngredients()) {
            player.getInventory().removeItem(ingredient);
        }
    }
    
    /**
     * Complete brewing for player
     */
    public void completeBrewing(Player player, String recipeId) {
        BrewingRecipe recipe = brewingSystem.getRecipe(recipeId);
        if (recipe == null) {
            return;
        }
        
        // Give result to player
        player.getInventory().addItem(recipe.getResult());
        
        // Give experience
        player.giveExp(recipe.getExperience());
        
        player.sendMessage("§aBrewing completed: " + recipe.getName());
        plugin.getLogger().info("Player " + player.getName() + " completed brewing: " + recipe.getName());
    }
    
    /**
     * Get brewing system
     */
    public BrewingSystem getBrewingSystem() {
        return brewingSystem;
    }
}
