package de.noctivag.skyblock.brewing;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Represents a brewing station for a player
 */
public class BrewingStation {
    
    private final Player player;
    private final AdvancedBrewingSystem brewingSystem;
    private boolean isActive;
    private long lastUpdate;
    private BukkitTask brewingTask;
    
    public BrewingStation(Player player, AdvancedBrewingSystem brewingSystem) {
        this.player = player;
        this.brewingSystem = brewingSystem;
        this.isActive = true;
        this.lastUpdate = java.lang.System.currentTimeMillis();
        startBrewingTask();
    }
    
    /**
     * Start the brewing task
     */
    private void startBrewingTask() {
        brewingTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActive) {
                    cancel();
                    return;
                }
                
                update();
            }
        }.runTaskTimer(brewingSystem.getPlugin(), 0L, 20L); // Every second
    }
    
    /**
     * Update the brewing station
     */
    public void update() {
        if (!isActive) return;
        
        lastUpdate = java.lang.System.currentTimeMillis();
        
        // Check if player is still online
        if (!player.isOnline()) {
            deactivate();
            return;
        }
        
        // Update brewing progress
        // This could include checking for completed brews, updating timers, etc.
    }
    
    /**
     * Activate the brewing station
     */
    public void activate() {
        isActive = true;
        if (brewingTask == null || brewingTask.isCancelled()) {
            startBrewingTask();
        }
    }
    
    /**
     * Deactivate the brewing station
     */
    public void deactivate() {
        isActive = false;
        if (brewingTask != null && !brewingTask.isCancelled()) {
            brewingTask.cancel();
        }
    }
    
    /**
     * Check if the brewing station is active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Get the player associated with this brewing station
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get the last update time
     */
    public long getLastUpdate() {
        return lastUpdate;
    }
    
    /**
     * Get the brewing system
     */
    public AdvancedBrewingSystem getBrewingSystem() {
        return brewingSystem;
    }
    
    /**
     * Shutdown the brewing station
     */
    public void shutdown() {
        deactivate();
        brewingSystem.removeBrewingStation(player.getUniqueId());
    }
}
