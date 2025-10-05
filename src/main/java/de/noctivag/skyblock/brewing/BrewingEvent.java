package de.noctivag.skyblock.brewing;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Custom brewing event
 */
public class BrewingEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final String recipeId;
    private final BrewingRecipe recipe;
    private final boolean success;
    
    public BrewingEvent(Player player, String recipeId, BrewingRecipe recipe, boolean success) {
        this.player = player;
        this.recipeId = recipeId;
        this.recipe = recipe;
        this.success = success;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getRecipeId() {
        return recipeId;
    }
    
    public BrewingRecipe getRecipe() {
        return recipe;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
