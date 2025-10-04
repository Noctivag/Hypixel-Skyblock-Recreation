package de.noctivag.plugin.infrastructure.config;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Service;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.CompletableFuture;

/**
 * Configuration service interface for managing plugin configuration.
 */
public interface ConfigService extends Service {
    
    /**
     * Get the main plugin configuration
     * @return main configuration
     */
    Configuration getMainConfig();
    
    /**
     * Get a specific configuration file
     * @param fileName the configuration file name
     * @return configuration file
     */
    FileConfiguration getConfig(String fileName);
    
    /**
     * Save a configuration file
     * @param fileName the configuration file name
     * @return CompletableFuture that completes when saved
     */
    CompletableFuture<Void> saveConfig(String fileName);
    
    /**
     * Reload a configuration file
     * @param fileName the configuration file name
     * @return CompletableFuture that completes when reloaded
     */
    CompletableFuture<Void> reloadConfig(String fileName);
    
    /**
     * Get a configuration value
     * @param key the configuration key
     * @param defaultValue the default value
     * @return configuration value
     */
    <T> T getValue(String key, T defaultValue);
    
    /**
     * Set a configuration value
     * @param key the configuration key
     * @param value the value to set
     */
    void setValue(String key, Object value);
    
    /**
     * Check if a configuration key exists
     * @param key the configuration key
     * @return true if exists, false otherwise
     */
    boolean hasKey(String key);
    
    /**
     * Get configuration file path
     * @param fileName the configuration file name
     * @return file path
     */
    String getConfigPath(String fileName);
    
    /**
     * Create default configuration if it doesn't exist
     * @param fileName the configuration file name
     * @return CompletableFuture that completes when created
     */
    CompletableFuture<Void> createDefaultConfig(String fileName);
}
