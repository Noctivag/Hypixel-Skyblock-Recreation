package de.noctivag.skyblock;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Refactored Skyblock Plugin - Legacy compatibility class
 */
public class SkyblockPluginRefactored extends JavaPlugin {
    
    private static SkyblockPluginRefactored instance;
    
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("SkyblockPluginRefactored enabled (legacy compatibility)");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SkyblockPluginRefactored disabled");
    }
    
    public static SkyblockPluginRefactored getInstance() {
        return instance;
    }

    private de.noctivag.skyblock.config.SettingsConfig settingsConfig;

    public de.noctivag.skyblock.config.SettingsConfig getSettingsConfig() {
        if (settingsConfig == null) {
            settingsConfig = new de.noctivag.skyblock.config.SettingsConfig(SkyblockPlugin.getInstance());
        }
        return settingsConfig;
    }

    /**
     * Get rolling restart world manager (placeholder)
     */
    public Object getRollingRestartWorldManager() {
        return null; // Placeholder
    }

    /**
     * Get mining area system (placeholder)
     */
    public Object getMiningAreaSystem() {
        return null; // Placeholder
    }

    /**
     * Get skyblock manager (placeholder)
     */
    public Object getSkyblockManager() {
        return null; // Placeholder
    }
}

