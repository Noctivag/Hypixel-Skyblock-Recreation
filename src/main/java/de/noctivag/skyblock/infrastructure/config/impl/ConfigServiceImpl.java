package de.noctivag.skyblock.infrastructure.config.impl;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.infrastructure.config.ConfigService;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import de.noctivag.skyblock.SkyblockPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Configuration service implementation.
 */
public class ConfigServiceImpl implements ConfigService {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Logger logger;
    private final ExecutorService executor;
    private final ConcurrentHashMap<String, FileConfiguration> configs;
    private FileConfiguration mainConfig;
    private boolean initialized = false;
    
    public ConfigServiceImpl(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.logger = SkyblockPlugin.getLogger();
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "ConfigService-Thread");
            thread.setDaemon(true);
            return thread;
        });
        this.configs = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            try {
                // Initialize main config
                SkyblockPlugin.saveDefaultConfig();
                mainConfig = SkyblockPlugin.getConfig();
                
                // Load other configuration files
                loadConfigFiles();
                
                initialized = true;
                logger.info("ConfigService initialized successfully");
            } catch (Exception e) {
                logger.severe("Failed to initialize ConfigService: " + e.getMessage());
                throw new RuntimeException("ConfigService initialization failed", e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            try {
                // Save all configurations
                for (String fileName : configs.keySet()) {
                    saveConfig(fileName).join();
                }
                
                executor.shutdown();
                initialized = false;
                logger.info("ConfigService shutdown completed");
            } catch (Exception e) {
                logger.warning("Error during ConfigService shutdown: " + e.getMessage());
            }
        });
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public String getName() {
        return "ConfigService";
    }
    
    @Override
    public int getPriority() {
        return 10; // High priority - needed by other services
    }
    
    @Override
    public Configuration getMainConfig() {
        return mainConfig;
    }
    
    @Override
    public FileConfiguration getConfig(String fileName) {
        return configs.computeIfAbsent(fileName, this::loadConfigFile);
    }
    
    @Override
    public CompletableFuture<Void> saveConfig(String fileName) {
        return CompletableFuture.runAsync(() -> {
            try {
                FileConfiguration config = configs.get(fileName);
                if (config != null) {
                    File configFile = new File(SkyblockPlugin.getDataFolder(), fileName);
                    config.save(configFile);
                    logger.fine("Saved configuration: " + fileName);
                }
            } catch (IOException e) {
                logger.warning("Failed to save configuration " + fileName + ": " + e.getMessage());
                throw new RuntimeException("Failed to save configuration", e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> reloadConfig(String fileName) {
        return CompletableFuture.runAsync(() -> {
            try {
                FileConfiguration config = loadConfigFile(fileName);
                configs.put(fileName, config);
                logger.info("Reloaded configuration: " + fileName);
            } catch (Exception e) {
                logger.warning("Failed to reload configuration " + fileName + ": " + e.getMessage());
            }
        }, executor);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, T defaultValue) {
        if (mainConfig != null && mainConfig.contains(key)) {
            Object value = mainConfig.get(key);
            if (value != null && defaultValue.getClass().isAssignableFrom(value.getClass())) {
                return (T) value;
            }
        }
        return defaultValue;
    }
    
    @Override
    public void setValue(String key, Object value) {
        if (mainConfig != null) {
            mainConfig.set(key, value);
        }
    }
    
    @Override
    public boolean hasKey(String key) {
        return mainConfig != null && mainConfig.contains(key);
    }
    
    @Override
    public String getConfigPath(String fileName) {
        return new File(SkyblockPlugin.getDataFolder(), fileName).getAbsolutePath();
    }
    
    @Override
    public CompletableFuture<Void> createDefaultConfig(String fileName) {
        return CompletableFuture.runAsync(() -> {
            try {
                File configFile = new File(SkyblockPlugin.getDataFolder(), fileName);
                if (!configFile.exists()) {
                    // Try to copy from resources
                    InputStream resourceStream = SkyblockPlugin.getResource(fileName);
                    if (resourceStream != null) {
                        Files.copy(resourceStream, configFile.toPath());
                        logger.info("Created default configuration: " + fileName);
                    } else {
                        // Create empty config
                        configFile.createNewFile();
                        FileConfiguration config = new YamlConfiguration();
                        config.save(configFile);
                        logger.info("Created empty configuration: " + fileName);
                    }
                }
            } catch (IOException e) {
                logger.warning("Failed to create default configuration " + fileName + ": " + e.getMessage());
                throw new RuntimeException("Failed to create default configuration", e);
            }
        }, executor);
    }
    
    private void loadConfigFiles() {
        // Load common configuration files
        String[] configFiles = {
            "config.yml",
            "messages.yml",
            "economy.yml",
            "skyblock.yml",
            "achievements.yml",
            "cosmetics.yml"
        };
        
        for (String fileName : configFiles) {
            createDefaultConfig(fileName).thenRun(() -> {
                FileConfiguration config = loadConfigFile(fileName);
                configs.put(fileName, config);
            });
        }
    }
    
    private FileConfiguration loadConfigFile(String fileName) {
        try {
            File configFile = new File(SkyblockPlugin.getDataFolder(), fileName);
            if (configFile.exists()) {
                return YamlConfiguration.loadConfiguration(configFile);
            } else {
                // Create default if not exists
                createDefaultConfig(fileName).join();
                return YamlConfiguration.loadConfiguration(configFile);
            }
        } catch (Exception e) {
            logger.warning("Failed to load configuration " + fileName + ": " + e.getMessage());
            return new YamlConfiguration();
        }
    }
}
