package de.noctivag.skyblock.config;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Zentrale Konfigurationsklasse für Datenbankverbindungen
 * Verwaltet alle Datenbank-spezifischen Einstellungen
 */
public class DatabaseConfig {
    
    private final SkyblockPluginRefactored plugin;
    private FileConfiguration databaseConfig;
    private File databaseFile;
    
    // Datenbank-Verbindungseinstellungen
    private boolean enabled;
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private String connectionUrl;
    
    // Connection Pool Einstellungen
    private int maximumPoolSize;
    private int minimumIdle;
    private long connectionTimeout;
    private long idleTimeout;
    private long maxLifetime;
    
    // Performance-Einstellungen
    private boolean useSSL;
    private boolean autoReconnect;
    private int maxReconnects;
    private long reconnectDelay;
    
    public DatabaseConfig(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        this.databaseFile = new File(plugin.getDataFolder(), "database.yml");
    }
    
    /**
     * Lädt alle Datenbank-Konfigurationswerte
     */
    public void load() {
        // Erstelle database.yml falls sie nicht existiert
        if (!databaseFile.exists()) {
            createDefaultDatabaseConfig();
        }
        
        this.databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);
        
        // Datenbank-Verbindungseinstellungen
        this.enabled = databaseConfig.getBoolean("database.enabled", false);
        this.host = databaseConfig.getString("database.host", "localhost");
        this.port = databaseConfig.getInt("database.port", 3306);
        this.username = databaseConfig.getString("database.username", "root");
        this.password = databaseConfig.getString("database.password", "");
        this.database = databaseConfig.getString("database.database", "skyblock");
        
        // Connection Pool Einstellungen
        this.maximumPoolSize = databaseConfig.getInt("database.pool.maximum-pool-size", 10);
        this.minimumIdle = databaseConfig.getInt("database.pool.minimum-idle", 5);
        this.connectionTimeout = databaseConfig.getLong("database.pool.connection-timeout", 30000);
        this.idleTimeout = databaseConfig.getLong("database.pool.idle-timeout", 600000);
        this.maxLifetime = databaseConfig.getLong("database.pool.max-lifetime", 1800000);
        
        // Performance-Einstellungen
        this.useSSL = databaseConfig.getBoolean("database.performance.use-ssl", false);
        this.autoReconnect = databaseConfig.getBoolean("database.performance.auto-reconnect", true);
        this.maxReconnects = databaseConfig.getInt("database.performance.max-reconnects", 3);
        this.reconnectDelay = databaseConfig.getLong("database.performance.reconnect-delay", 5000);
        
        // Erstelle Connection URL
        this.connectionUrl = buildConnectionUrl();
        
        plugin.getLogger().info("Database configuration loaded successfully");
        
        if (enabled) {
            plugin.getLogger().info("Database enabled - Host: " + host + ":" + port + ", Database: " + database);
        } else {
            plugin.getLogger().info("Database disabled - Using file-based storage");
        }
    }
    
    /**
     * Erstellt eine Standard-Datenbank-Konfiguration
     */
    private void createDefaultDatabaseConfig() {
        try {
            plugin.getDataFolder().mkdirs();
            databaseFile.createNewFile();
            
            databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);
            
            // Standard-Werte setzen
            databaseConfig.set("database.enabled", false);
            databaseConfig.set("database.host", "localhost");
            databaseConfig.set("database.port", 3306);
            databaseConfig.set("database.username", "root");
            databaseConfig.set("database.password", "");
            databaseConfig.set("database.database", "skyblock");
            
            databaseConfig.set("database.pool.maximum-pool-size", 10);
            databaseConfig.set("database.pool.minimum-idle", 5);
            databaseConfig.set("database.pool.connection-timeout", 30000);
            databaseConfig.set("database.pool.idle-timeout", 600000);
            databaseConfig.set("database.pool.max-lifetime", 1800000);
            
            databaseConfig.set("database.performance.use-ssl", false);
            databaseConfig.set("database.performance.auto-reconnect", true);
            databaseConfig.set("database.performance.max-reconnects", 3);
            databaseConfig.set("database.performance.reconnect-delay", 5000);
            
            databaseConfig.save(databaseFile);
            plugin.getLogger().info("Created default database.yml configuration file");
            
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create database.yml: " + e.getMessage());
        }
    }
    
    /**
     * Baut die JDBC Connection URL
     */
    private String buildConnectionUrl() {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(database);
        
        // URL-Parameter hinzufügen
        url.append("?useSSL=").append(useSSL);
        url.append("&autoReconnect=").append(autoReconnect);
        url.append("&maxReconnects=").append(maxReconnects);
        url.append("&reconnectDelay=").append(reconnectDelay);
        url.append("&serverTimezone=UTC");
        url.append("&characterEncoding=utf8");
        url.append("&useUnicode=true");
        
        return url.toString();
    }
    
    /**
     * Speichert die Konfiguration zurück in die Datei
     */
    public void save() {
        try {
            databaseConfig.save(databaseFile);
            plugin.getLogger().info("Database configuration saved successfully");
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save database.yml: " + e.getMessage());
        }
    }
    
    // Getter-Methoden
    public boolean isEnabled() {
        return enabled;
    }
    
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public String getConnectionUrl() {
        return connectionUrl;
    }
    
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }
    
    public int getMinimumIdle() {
        return minimumIdle;
    }
    
    public long getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public long getIdleTimeout() {
        return idleTimeout;
    }
    
    public long getMaxLifetime() {
        return maxLifetime;
    }
    
    public boolean isUseSSL() {
        return useSSL;
    }
    
    public boolean isAutoReconnect() {
        return autoReconnect;
    }
    
    public int getMaxReconnects() {
        return maxReconnects;
    }
    
    public long getReconnectDelay() {
        return reconnectDelay;
    }
    
    // Setter-Methoden für dynamische Änderungen
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setHost(String host) {
        this.host = host;
        this.connectionUrl = buildConnectionUrl();
    }
    
    public void setPort(int port) {
        this.port = port;
        this.connectionUrl = buildConnectionUrl();
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setDatabase(String database) {
        this.database = database;
        this.connectionUrl = buildConnectionUrl();
    }
}
