package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Brewing configuration manager
 */
public class BrewingConfig {
    
    private final SkyblockPlugin plugin;
    private File configFile;
    private FileConfiguration config;
    
    public BrewingConfig(SkyblockPlugin plugin) {
        this.plugin = plugin;
        setupConfig();
    }
    
    private void setupConfig() {
        configFile = new File(plugin.getDataFolder(), "brewing.yml");
        if (!configFile.exists()) {
            plugin.saveResource("brewing.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save brewing config: " + e.getMessage(), e);
        }
    }
    
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public boolean isBrewingEnabled() {
        return config.getBoolean("brewing.enabled", true);
    }
    
    public int getDefaultBrewingTime() {
        return config.getInt("brewing.default_time", 200);
    }
    
    public int getDefaultExperience() {
        return config.getInt("brewing.default_experience", 10);
    }
    
    public boolean isCustomRecipesEnabled() {
        return config.getBoolean("brewing.custom_recipes", true);
    }
}
