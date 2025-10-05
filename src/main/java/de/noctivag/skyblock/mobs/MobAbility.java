package de.noctivag.skyblock.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Interface for mob abilities
 * Defines the contract for all mob abilities
 */
public interface MobAbility {
    
    /**
     * Execute the ability
     * @param self The entity that has this ability
     * @param target The target player (can be null for some abilities)
     */
    void execute(Entity self, Player target);
    
    /**
     * Get the trigger that activates this ability
     * @return The ability trigger
     */
    AbilityTrigger getTrigger();
    
    /**
     * Get the cooldown time in milliseconds
     * @return Cooldown time
     */
    long getCooldown();
    
    /**
     * Check if the ability can be executed (cooldown check)
     * @return true if ability can be executed
     */
    boolean canExecute();
    
    /**
     * Called when the mob spawns
     * @param entity The spawned entity
     */
    default void onSpawn(Entity entity) {
        // Default implementation does nothing
    }
    
    /**
     * Called when the mob dies
     * @param entity The dead entity
     */
    default void onDeath(Entity entity) {
        // Default implementation does nothing
    }
}
