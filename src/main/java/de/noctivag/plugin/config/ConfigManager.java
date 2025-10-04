package de.noctivag.plugin.config;
import org.bukkit.inventory.ItemStack;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Configuration Manager - Centralized config management
 *
 * Features:
 * - Multiple config file support
 * - Auto-save functionality
 * - Default value management
 * - Configuration validation
 * - Hot-reload support
 */
public class ConfigManager {

    private final Plugin plugin;
    private final Map<String, FileConfiguration> configs;
    private final Map<String, File> configFiles;
    private final Map<String, Boolean> autoSave;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
        this.configFiles = new HashMap<>();
        this.autoSave = new HashMap<>();

        // Create plugin data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }

    /**
     * Load a configuration file
     */
    public FileConfiguration loadConfig(String name) {
        return loadConfig(name, true);
    }

    /**
     * Load a configuration file with auto-save option
     */
    public FileConfiguration loadConfig(String name, boolean autoSave) {
        File configFile = new File(plugin.getDataFolder(), name + ".yml");

        if (!configFile.exists()) {
            plugin.saveResource(name + ".yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(name, config);
        configFiles.put(name, configFile);
        this.autoSave.put(name, autoSave);

        return config;
    }

    /**
     * Get a configuration file
     */
    public FileConfiguration getConfig(String name) {
        return configs.get(name);
    }

    /**
     * Get the main plugin configuration
     */
    public FileConfiguration getConfig() {
        // Lazily load the main config if it hasn't been loaded yet to avoid null returns
        if (!configs.containsKey("config")) {
            try {
                loadConfig("config");
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to lazily load config.yml", e);
                return null;
            }
        }
        return getConfig("config");
    }

    /**
     * Save a configuration file
     */
    public void saveConfig(String name) {
        FileConfiguration config = configs.get(name);
        File configFile = configFiles.get(name);

        if (config != null && configFile != null) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config " + name, e);
            }
        }
    }

    /**
     * Save all configuration files
     */
    public void saveAllConfigs() {
        for (String name : configs.keySet()) {
            saveConfig(name);
        }
    }

    /**
     * Reload a configuration file
     */
    public void reloadConfig(String name) {
        File configFile = configFiles.get(name);
        if (configFile != null) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            configs.put(name, config);
        }
    }

    /**
     * Reload the main plugin configuration
     */
    public void reloadConfig() {
        reloadConfig("config");
    }

    /**
     * Reload all configuration files
     */
    public void reloadAllConfigs() {
        for (String name : configs.keySet()) {
            reloadConfig(name);
        }
    }

    // Additional methods for compatibility

    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return getConfig().getBoolean("debug.enabled", false);
    }

    /**
     * Get scoreboard title
     */
    public String getScoreboardTitle() {
        return getConfig().getString("scoreboard.title", "&6&lSkyblock");
    }

    /**
     * Get scoreboard lines
     */
    public List<String> getScoreboardLines() {
        return getConfig().getStringList("scoreboard.lines");
    }

    /**
     * Check if a scoreboard feature is enabled
     */
    public boolean isScoreboardFeatureEnabled(String feature) {
        return getConfig().getBoolean("scoreboard.features." + feature, true);
    }

    /**
     * Set scoreboard feature enabled/disabled
     */
    public void setScoreboardFeature(String feature, boolean enabled) {
        getConfig().set("scoreboard.features." + feature, enabled);
        saveConfig("config");
    }

    /**
     * Check if a general feature is enabled
     */
    public boolean isFeatureEnabled(String feature) {
        return getConfig().getBoolean("features." + feature, true);
    }

    /**
     * Set feature enabled/disabled
     */
    public void setFeature(String feature, boolean enabled) {
        getConfig().set("features." + feature, enabled);
        saveConfig("config");
    }

    /**
     * Get a message from config
     */
    public String getMessage(String key) {
        return getConfig().getString("messages." + key, "&cMessage not found: " + key);
    }

    public String getPlayerRank(Player player) {
        return getConfig().getString("ranks.default", "&7Default");
    }

    /**
     * Get a configuration value with default
     */
    public Object getConfigValue(String configName, String path, Object defaultValue) {
        FileConfiguration config = getConfig(configName);
        if (config == null) {
            return defaultValue;
        }

        if (!config.contains(path)) {
            config.set(path, defaultValue);
            if (autoSave.getOrDefault(configName, true)) {
                saveConfig(configName);
            }
            return defaultValue;
        }

        return config.get(path);
    }

    /**
     * Set a configuration value
     */
    public void setConfigValue(String configName, String path, Object value) {
        FileConfiguration config = getConfig(configName);
        if (config != null) {
            config.set(path, value);
            if (autoSave.getOrDefault(configName, true)) {
                saveConfig(configName);
            }
        }
    }

    /**
     * Check if a configuration path exists
     */
    public boolean hasConfigValue(String configName, String path) {
        FileConfiguration config = getConfig(configName);
        return config != null && config.contains(path);
    }

    /**
     * Get string value with default
     */
    public String getString(String configName, String path, String defaultValue) {
        return (String) getConfigValue(configName, path, defaultValue);
    }

    /**
     * Get integer value with default
     */
    public int getInt(String configName, String path, int defaultValue) {
        Object value = getConfigValue(configName, path, defaultValue);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    /**
     * Get double value with default
     */
    public double getDouble(String configName, String path, double defaultValue) {
        Object value = getConfigValue(configName, path, defaultValue);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }

    /**
     * Get boolean value with default
     */
    public boolean getBoolean(String configName, String path, boolean defaultValue) {
        Object value = getConfigValue(configName, path, defaultValue);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }

    /**
     * Set string value
     */
    public void setString(String configName, String path, String value) {
        setConfigValue(configName, path, value);
    }

    /**
     * Set integer value
     */
    public void setInt(String configName, String path, int value) {
        setConfigValue(configName, path, value);
    }

    /**
     * Set double value
     */
    public void setDouble(String configName, String path, double value) {
        setConfigValue(configName, path, value);
    }

    /**
     * Set boolean value
     */
    public void setBoolean(String configName, String path, boolean value) {
        setConfigValue(configName, path, value);
    }

    /**
     * Enable/disable auto-save for a config
     */
    public void setAutoSave(String configName, boolean autoSave) {
        this.autoSave.put(configName, autoSave);
    }

    /**
     * Check if auto-save is enabled for a config
     */
    public boolean isAutoSaveEnabled(String configName) {
        return autoSave.getOrDefault(configName, true);
    }

    /**
     * Get all loaded config names
     */
    public String[] getLoadedConfigs() {
        return configs.keySet().toArray(new String[0]);
    }

    /**
     * Check if a config is loaded
     */
    public boolean isConfigLoaded(String name) {
        return configs.containsKey(name);
    }

    /**
     * Unload a configuration
     */
    public void unloadConfig(String name) {
        configs.remove(name);
        configFiles.remove(name);
        autoSave.remove(name);
    }

    /**
     * Create a new configuration file
     */
    public FileConfiguration createConfig(String name) {
        File configFile = new File(plugin.getDataFolder(), name + ".yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create config " + name, e);
                return null;
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(name, config);
        configFiles.put(name, configFile);
        autoSave.put(name, true);

        return config;
    }
}
