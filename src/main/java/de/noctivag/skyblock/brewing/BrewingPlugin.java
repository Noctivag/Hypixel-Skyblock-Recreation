package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.plugin.Plugin;

/**
 * Brewing plugin integration
 */
public class BrewingPlugin {
    
    private final SkyblockPlugin plugin;
    private final BrewingManager brewingManager;
    private final BrewingAPI brewingAPI;
    
    public BrewingPlugin(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.brewingManager = new BrewingManager(plugin);
        this.brewingAPI = new BrewingAPI(plugin);
    }
    
    /**
     * Initialize brewing plugin
     */
    public void initialize() {
        brewingManager.initialize();
        plugin.getLogger().info("Brewing plugin initialized");
    }
    
    /**
     * Shutdown brewing plugin
     */
    public void shutdown() {
        brewingManager.shutdown();
        plugin.getLogger().info("Brewing plugin shut down");
    }
    
    /**
     * Get brewing manager
     */
    public BrewingManager getBrewingManager() {
        return brewingManager;
    }
    
    /**
     * Get brewing API
     */
    public BrewingAPI getBrewingAPI() {
        return brewingAPI;
    }
}
