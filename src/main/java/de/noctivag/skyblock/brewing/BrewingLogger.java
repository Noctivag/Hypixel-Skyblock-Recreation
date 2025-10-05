package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Logger for brewing system
 */
public class BrewingLogger {
    
    private final SkyblockPlugin plugin;
    
    public BrewingLogger(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Log brewing start
     */
    public void logBrewingStart(Player player, String recipeId) {
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " started brewing: " + recipeId);
    }
    
    /**
     * Log brewing completion
     */
    public void logBrewingCompletion(Player player, String recipeId, boolean success) {
        String status = success ? "successfully" : "unsuccessfully";
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " completed brewing " + status + ": " + recipeId);
    }
    
    /**
     * Log brewing failure
     */
    public void logBrewingFailure(Player player, String recipeId, String reason) {
        plugin.getLogger().log(Level.WARNING, "Player " + player.getName() + " failed brewing " + recipeId + ": " + reason);
    }
    
    /**
     * Log recipe registration
     */
    public void logRecipeRegistration(String recipeId, String recipeName) {
        plugin.getLogger().log(Level.INFO, "Registered brewing recipe: " + recipeName + " (ID: " + recipeId + ")");
    }
    
    /**
     * Log recipe unregistration
     */
    public void logRecipeUnregistration(String recipeId, String recipeName) {
        plugin.getLogger().log(Level.INFO, "Unregistered brewing recipe: " + recipeName + " (ID: " + recipeId + ")");
    }
    
    /**
     * Log brewing error
     */
    public void logBrewingError(String message, Throwable throwable) {
        plugin.getLogger().log(Level.SEVERE, "Brewing error: " + message, throwable);
    }
    
    /**
     * Log brewing warning
     */
    public void logBrewingWarning(String message) {
        plugin.getLogger().log(Level.WARNING, "Brewing warning: " + message);
    }
    
    /**
     * Log brewing info
     */
    public void logBrewingInfo(String message) {
        plugin.getLogger().log(Level.INFO, "Brewing info: " + message);
    }
}
