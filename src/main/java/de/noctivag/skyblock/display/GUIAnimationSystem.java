package de.noctivag.skyblock.display;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * GUI Animation System - Manages GUI animations
 */
public class GUIAnimationSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, String> playerAnimations = new HashMap<>();
    
    public GUIAnimationSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing GUIAnimationSystem...");
        
        // Initialize animation system
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("GUIAnimationSystem initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down GUIAnimationSystem...");
        
        // Clear all animations
        playerAnimations.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("GUIAnimationSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "GUIAnimationSystem";
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
     * Start an animation for a player
     */
    public void startAnimation(Player player, String animationId) {
        playerAnimations.put(player.getUniqueId(), animationId);
        plugin.getLogger().log(Level.INFO, "Started animation for player " + player.getName() + ": " + animationId);
    }
    
    /**
     * Stop an animation for a player
     */
    public void stopAnimation(Player player) {
        playerAnimations.remove(player.getUniqueId());
        plugin.getLogger().log(Level.INFO, "Stopped animation for player " + player.getName());
    }
    
    /**
     * Get a player's current animation
     */
    public String getPlayerAnimation(Player player) {
        return playerAnimations.get(player.getUniqueId());
    }
    
    /**
     * Check if a player has an active animation
     */
    public boolean hasActiveAnimation(Player player) {
        return playerAnimations.containsKey(player.getUniqueId());
    }
    
    /**
     * Get all player animations
     */
    public Map<UUID, String> getPlayerAnimations() {
        return new HashMap<>(playerAnimations);
    }
}