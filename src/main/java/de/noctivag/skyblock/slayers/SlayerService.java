package de.noctivag.skyblock.slayers;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.slayers.quests.SlayerQuest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Slayer Service - Service for managing slayer quests
 */
public class SlayerService implements Service {
    
    private final SkyblockPlugin plugin;
    private final SlayerManager slayerManager;
    private SystemStatus status = SystemStatus.DISABLED;
    
    public SlayerService(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.slayerManager = new SlayerManager(plugin);
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing SlayerService...");
        
        // Initialize slayer manager
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("SlayerService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down SlayerService...");
        
        // Save all active quests
        for (SlayerQuest quest : slayerManager.getActiveQuests().values()) {
            // Save quest data to database
            plugin.getLogger().log(Level.INFO, "Saving quest: " + quest.getQuestId());
        }
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("SlayerService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "SlayerService";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Get the slayer manager
     */
    public SlayerManager getSlayerManager() {
        return slayerManager;
    }
    
    /**
     * Start a slayer quest for a player
     */
    public boolean startSlayerQuest(Player player, String slayerType, int tier) {
        return slayerManager.startSlayerQuest(player, slayerType, tier);
    }
    
    /**
     * Complete a slayer quest for a player
     */
    public boolean completeSlayerQuest(Player player) {
        return slayerManager.completeSlayerQuest(player);
    }
    
    /**
     * Get the active quest for a player
     */
    public SlayerQuest getActiveQuest(Player player) {
        return slayerManager.getActiveQuest(player);
    }
    
    /**
     * Check if a player has an active quest
     */
    public boolean hasActiveQuest(Player player) {
        return slayerManager.hasActiveQuest(player);
    }
    
    /**
     * Cancel a slayer quest for a player
     */
    public boolean cancelSlayerQuest(Player player) {
        return slayerManager.cancelSlayerQuest(player);
    }
}

