package de.noctivag.skyblock.config;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Zentrale Konfigurationsklasse für allgemeine Plugin-Einstellungen
 * Entfernt Hardcoding und zentralisiert alle Konfigurationswerte
 */
public class SettingsConfig {
    
    private final SkyblockPlugin plugin;
    private FileConfiguration config;
    
    // Allgemeine Einstellungen
    private boolean hubSpawnSystemEnabled;
    private boolean rollingRestartSystemEnabled;
    private boolean autoWorldCreationEnabled;
    private boolean foliaCompatibilityMode;
    
    // Performance-Einstellungen
    private int cacheSize;
    private long cacheExpirationTime;
    private boolean asyncLoadingEnabled;
    
    // Debug-Einstellungen
    private boolean debugMode;
    private boolean verboseLogging;
    
    // Welt-Einstellungen
    private String defaultHubWorld;
    private int worldResetInterval;
    private boolean autoWorldReset;
    
    public SettingsConfig(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Lädt alle Konfigurationswerte aus der config.yml
     */
    public void load() {
        this.config = plugin.getConfig();
        
        // Allgemeine Einstellungen
        this.hubSpawnSystemEnabled = config.getBoolean("settings.hub-spawn-system.enabled", true);
        this.rollingRestartSystemEnabled = config.getBoolean("settings.rolling-restart-system.enabled", true);
        this.autoWorldCreationEnabled = config.getBoolean("settings.auto-world-creation.enabled", true);
        this.foliaCompatibilityMode = config.getBoolean("settings.folia-compatibility.enabled", true);
        
        // Performance-Einstellungen
        this.cacheSize = config.getInt("performance.cache.size", 1000);
        this.cacheExpirationTime = config.getLong("performance.cache.expiration-time", 300000); // 5 Minuten
        this.asyncLoadingEnabled = config.getBoolean("performance.async-loading.enabled", true);
        
        // Debug-Einstellungen
        this.debugMode = config.getBoolean("debug.enabled", false);
        this.verboseLogging = config.getBoolean("debug.verbose-logging", false);
        
        // Welt-Einstellungen
        this.defaultHubWorld = config.getString("worlds.default-hub", "hub_a");
        this.worldResetInterval = config.getInt("worlds.reset-interval", 14400); // 4 Stunden
        this.autoWorldReset = config.getBoolean("worlds.auto-reset.enabled", true);
        
        plugin.getLogger().info("Settings configuration loaded successfully");
        
        if (debugMode) {
            plugin.getLogger().info("Debug mode enabled - Verbose logging: " + verboseLogging);
        }
    }
    
    /**
     * Speichert die Konfiguration zurück in die Datei
     */
    public void save() {
        // Allgemeine Einstellungen
        config.set("settings.hub-spawn-system.enabled", hubSpawnSystemEnabled);
        config.set("settings.rolling-restart-system.enabled", rollingRestartSystemEnabled);
        config.set("settings.auto-world-creation.enabled", autoWorldCreationEnabled);
        config.set("settings.folia-compatibility.enabled", foliaCompatibilityMode);
        
        // Performance-Einstellungen
        config.set("performance.cache.size", cacheSize);
        config.set("performance.cache.expiration-time", cacheExpirationTime);
        config.set("performance.async-loading.enabled", asyncLoadingEnabled);
        
        // Debug-Einstellungen
        config.set("debug.enabled", debugMode);
        config.set("debug.verbose-logging", verboseLogging);
        
        // Welt-Einstellungen
        config.set("worlds.default-hub", defaultHubWorld);
        config.set("worlds.reset-interval", worldResetInterval);
        config.set("worlds.auto-reset.enabled", autoWorldReset);
        
        plugin.saveConfig();
    }
    
    // Getter-Methoden
    public boolean isHubSpawnSystemEnabled() {
        return hubSpawnSystemEnabled;
    }
    
    public boolean isRollingRestartSystemEnabled() {
        return rollingRestartSystemEnabled;
    }
    
    public boolean isAutoWorldCreationEnabled() {
        return autoWorldCreationEnabled;
    }
    
    public boolean isFoliaCompatibilityMode() {
        return foliaCompatibilityMode;
    }
    
    public int getCacheSize() {
        return cacheSize;
    }
    
    public long getCacheExpirationTime() {
        return cacheExpirationTime;
    }
    
    public boolean isAsyncLoadingEnabled() {
        return asyncLoadingEnabled;
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public boolean isVerboseLogging() {
        return verboseLogging;
    }
    
    public String getDefaultHubWorld() {
        return defaultHubWorld;
    }
    
    public int getWorldResetInterval() {
        return worldResetInterval;
    }
    
    public boolean isAutoWorldReset() {
        return autoWorldReset;
    }
    
    // Setter-Methoden für dynamische Änderungen
    public void setHubSpawnSystemEnabled(boolean enabled) {
        this.hubSpawnSystemEnabled = enabled;
    }
    
    public void setRollingRestartSystemEnabled(boolean enabled) {
        this.rollingRestartSystemEnabled = enabled;
    }
    
    public void setDebugMode(boolean enabled) {
        this.debugMode = enabled;
    }
    
    public void setVerboseLogging(boolean enabled) {
        this.verboseLogging = enabled;
    }
}
