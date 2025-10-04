package de.noctivag.skyblock.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * AsyncConfigManager - Asynchronous configuration loading and saving
 * 
 * Features:
 * - Async configuration file loading
 * - Async configuration file saving
 * - Configuration caching
 * - File I/O optimization
 * - Error handling and retry logic
 */
public class AsyncConfigManager {
    private final SkyblockPlugin plugin;
    private final MultithreadingManager multithreadingManager;
    private final ConcurrentHashMap<String, FileConfiguration> configCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> configLastModified = new ConcurrentHashMap<>();
    
    public AsyncConfigManager(SkyblockPlugin plugin, MultithreadingManager multithreadingManager) {
        this.plugin = plugin;
        this.multithreadingManager = multithreadingManager;
    }
    
    /**
     * Load configuration file asynchronously
     */
    public CompletableFuture<FileConfiguration> loadConfigAsync(String fileName) {
        return multithreadingManager.executeFileIOAsync(() -> {
            try {
                File configFile = new File(plugin.getDataFolder(), fileName);
                
                // Check if file exists, create if not
                if (!configFile.exists()) {
                    if (plugin.getResource(fileName) != null) {
                        plugin.saveResource(fileName, false);
                    } else {
                        configFile.getParentFile().mkdirs();
                        configFile.createNewFile();
                    }
                }
                
                // Check cache first
                String cacheKey = configFile.getAbsolutePath();
                long lastModified = configFile.lastModified();
                
                if (configCache.containsKey(cacheKey) && 
                    configLastModified.get(cacheKey) != null &&
                    configLastModified.get(cacheKey).equals(lastModified)) {
                    return configCache.get(cacheKey);
                }
                
                // Load configuration
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                
                // Update cache
                configCache.put(cacheKey, config);
                configLastModified.put(cacheKey, lastModified);
                
                return config;
                
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load config file: " + fileName, e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Save configuration file asynchronously
     */
    public CompletableFuture<Boolean> saveConfigAsync(String fileName, FileConfiguration config) {
        return multithreadingManager.executeFileIOAsync(() -> {
            try {
                File configFile = new File(plugin.getDataFolder(), fileName);
                config.save(configFile);
                
                // Update cache
                String cacheKey = configFile.getAbsolutePath();
                configCache.put(cacheKey, config);
                configLastModified.put(cacheKey, configFile.lastModified());
                
                return true;
                
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save config file: " + fileName, e);
                return false;
            }
        });
    }
    
    /**
     * Load multiple configuration files asynchronously
     */
    public CompletableFuture<ConcurrentHashMap<String, FileConfiguration>> loadConfigsAsync(String[] fileNames) {
        @SuppressWarnings("unchecked")
        CompletableFuture<FileConfiguration>[] futures = new CompletableFuture[fileNames.length];
        
        for (int i = 0; i < fileNames.length; i++) {
            final int index = i;
            futures[i] = loadConfigAsync(fileNames[index]);
        }
        
        return CompletableFuture.allOf(futures).thenApply(v -> {
            ConcurrentHashMap<String, FileConfiguration> configs = new ConcurrentHashMap<>();
            for (int i = 0; i < fileNames.length; i++) {
                try {
                    configs.put(fileNames[i], futures[i].get());
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to load config: " + fileNames[i], e);
                }
            }
            return configs;
        });
    }
    
    /**
     * Save multiple configuration files asynchronously
     */
    public CompletableFuture<Boolean> saveConfigsAsync(ConcurrentHashMap<String, FileConfiguration> configs) {
        @SuppressWarnings("unchecked")
        CompletableFuture<Boolean>[] futures = new CompletableFuture[configs.size()];
        int index = 0;
        
        for (String fileName : configs.keySet()) {
            futures[index++] = saveConfigAsync(fileName, configs.get(fileName));
        }
        
        return CompletableFuture.allOf(futures).thenApply(v -> {
            for (CompletableFuture<Boolean> future : futures) {
                try {
                    if (!future.get()) {
                        return false;
                    }
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to save config", e);
                    return false;
                }
            }
            return true;
        });
    }
    
    /**
     * Load player data configuration asynchronously
     */
    public CompletableFuture<FileConfiguration> loadPlayerDataAsync(String playerUUID) {
        return multithreadingManager.executeFileIOAsync(() -> {
            try {
                File playerDataFile = new File(plugin.getDataFolder() + "/playerdata", playerUUID + ".yml");
                
                if (!playerDataFile.exists()) {
                    playerDataFile.getParentFile().mkdirs();
                    playerDataFile.createNewFile();
                }
                
                return YamlConfiguration.loadConfiguration(playerDataFile);
                
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load player data: " + playerUUID, e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Save player data configuration asynchronously
     */
    public CompletableFuture<Boolean> savePlayerDataAsync(String playerUUID, FileConfiguration config) {
        return multithreadingManager.executeFileIOAsync(() -> {
            try {
                File playerDataFile = new File(plugin.getDataFolder() + "/playerdata", playerUUID + ".yml");
                config.save(playerDataFile);
                return true;
                
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save player data: " + playerUUID, e);
                return false;
            }
        });
    }
    
    /**
     * Load world configuration asynchronously
     */
    public CompletableFuture<FileConfiguration> loadWorldConfigAsync(String worldName) {
        return multithreadingManager.executeFileIOAsync(() -> {
            try {
                File worldConfigFile = new File(plugin.getDataFolder() + "/worlds", worldName + ".yml");
                
                if (!worldConfigFile.exists()) {
                    worldConfigFile.getParentFile().mkdirs();
                    worldConfigFile.createNewFile();
                }
                
                return YamlConfiguration.loadConfiguration(worldConfigFile);
                
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load world config: " + worldName, e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Save world configuration asynchronously
     */
    public CompletableFuture<Boolean> saveWorldConfigAsync(String worldName, FileConfiguration config) {
        return multithreadingManager.executeFileIOAsync(() -> {
            try {
                File worldConfigFile = new File(plugin.getDataFolder() + "/worlds", worldName + ".yml");
                config.save(worldConfigFile);
                return true;
                
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save world config: " + worldName, e);
                return false;
            }
        });
    }
    
    /**
     * Clear configuration cache
     */
    public void clearCache() {
        configCache.clear();
        configLastModified.clear();
    }
    
    /**
     * Get cached configuration
     */
    public FileConfiguration getCachedConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        return configCache.get(configFile.getAbsolutePath());
    }
    
    /**
     * Check if configuration is cached
     */
    public boolean isConfigCached(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        return configCache.containsKey(configFile.getAbsolutePath());
    }
}
