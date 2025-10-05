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
 * Enhanced Scoreboard System - Enhanced scoreboard management
 */
public class EnhancedScoreboardSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, String> playerScoreboards = new HashMap<>();
    
    public EnhancedScoreboardSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing EnhancedScoreboardSystem...");
        
        // Initialize scoreboard system
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("EnhancedScoreboardSystem initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down EnhancedScoreboardSystem...");
        
        // Clear all scoreboards
        playerScoreboards.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("EnhancedScoreboardSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Set a player's scoreboard
     */
    public void setPlayerScoreboard(Player player, String scoreboardId) {
        playerScoreboards.put(player.getUniqueId(), scoreboardId);
        plugin.getLogger().log(Level.INFO, "Set scoreboard for player " + player.getName() + ": " + scoreboardId);
    }
    
    /**
     * Get a player's scoreboard
     */
    public String getPlayerScoreboard(Player player) {
        return playerScoreboards.get(player.getUniqueId());
    }
    
    /**
     * Remove a player's scoreboard
     */
    public void removePlayerScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        plugin.getLogger().log(Level.INFO, "Removed scoreboard for player " + player.getName());
    }
    
    /**
     * Get all player scoreboards
     */
    public Map<UUID, String> getPlayerScoreboards() {
        return new HashMap<>(playerScoreboards);
    }
}