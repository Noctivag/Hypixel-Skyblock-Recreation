package de.noctivag.skyblock.mobs;

/**
 * Enum representing different triggers for mob abilities
 */
public enum AbilityTrigger {
    
    /**
     * Triggered when the mob attacks a player
     */
    ON_ATTACK,
    
    /**
     * Triggered when the mob takes damage
     */
    ON_DAMAGE,
    
    /**
     * Triggered when the mob dies
     */
    ON_DEATH,
    
    /**
     * Triggered when the mob spawns
     */
    ON_SPAWN,
    
    /**
     * Triggered on a timer (periodic)
     */
    TIMED,
    
    /**
     * Triggered when a player gets close to the mob
     */
    ON_NEARBY,
    
    /**
     * Triggered when the mob's health drops below a certain threshold
     */
    ON_LOW_HEALTH,
    
    /**
     * Triggered when the mob is hit by a specific type of attack
     */
    ON_SPECIFIC_DAMAGE
}
