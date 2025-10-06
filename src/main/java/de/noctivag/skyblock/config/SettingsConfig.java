package de.noctivag.skyblock.config;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SettingsConfig {
    private final SkyblockPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public SettingsConfig(SkyblockPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "settings.yml");
        if (!configFile.exists()) {
            plugin.saveResource("settings.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save settings config: " + e.getMessage(), e);
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public int getCacheSize() {
        return config.getInt("cache.size", 1000);
    }

    public long getCacheExpirationTime() {
        return config.getLong("cache.expiration", 300000); // 5 minutes
    }

    public boolean isVerboseLogging() {
        return config.getBoolean("logging.verbose", false);
    }
    
    public boolean isDebugMode() {
        return config.getBoolean("debug.enabled", false);
    }
}