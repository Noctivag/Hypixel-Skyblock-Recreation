package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.plugin.Plugin;

/**
 * Brewing system integration
 */
public class BrewingIntegration {
    
    private final SkyblockPlugin plugin;
    private final BrewingPlugin brewingPlugin;
    
    public BrewingIntegration(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.brewingPlugin = new BrewingPlugin(plugin);
    }
    
    /**
     * Initialize brewing integration
     */
    public void initialize() {
        brewingPlugin.initialize();
        plugin.getLogger().info("Brewing integration initialized");
    }
    
    /**
     * Shutdown brewing integration
     */
    public void shutdown() {
        brewingPlugin.shutdown();
        plugin.getLogger().info("Brewing integration shut down");
    }
    
    /**
     * Get brewing plugin
     */
    public BrewingPlugin getBrewingPlugin() {
        return brewingPlugin;
    }
}
