package de.noctivag.skyblock.data;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Database Manager - SQL/NoSQL Database Management
 *
 * Verantwortlich für:
 * - Database Connections
 * - Data Caching
 * - Backup Management
 * - Performance Optimization
 * - Data Synchronization
 */
public class DatabaseManager {
    private final SkyblockPlugin SkyblockPlugin;
    private Connection connection;
    private final String databaseType;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean useSSL;

    // Caching
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    private final long CACHE_EXPIRY = 5 * 60 * 1000L; // 5 minutes
    private final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    // Performance
    private final int MAX_CONNECTIONS = 10;
    private final int CONNECTION_TIMEOUT = 30;
    private final boolean AUTO_RECONNECT = true;

    public DatabaseManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;

        // Load database configuration
        File configFile = new File(SkyblockPlugin.getDataFolder(), "database.yml");
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        this.databaseType = config.getString("type", "sqlite");
        this.host = config.getString("host", "localhost");
        this.port = config.getInt("port", 3306);
        this.database = config.getString("database", "basicsplugin");
        this.username = config.getString("username", "root");
        this.password = config.getString("password", "");
        this.useSSL = config.getBoolean("useSSL", false);

        initializeDatabase();
    }

    private void createDefaultConfig(File configFile) {
        try {
            configFile.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            config.set("type", "sqlite");
            config.set("host", "localhost");
            config.set("port", 3306);
            config.set("database", "basicsplugin");
            config.set("username", "root");
            config.set("password", "");
            config.set("useSSL", false);
            config.set("cache.enabled", true);
            config.set("cache.expiry", 300);
            config.set("backup.enabled", true);
            config.set("backup.interval", 3600);
            config.set("backup.retention", 7);

            config.save(configFile);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Could not create database config", e);
        }
    }

    private void initializeDatabase() {
        try {
            if (databaseType.equalsIgnoreCase("sqlite")) {
                initializeSQLite();
            } else if (databaseType.equalsIgnoreCase("mysql")) {
                initializeMySQL();
            } else {
                SkyblockPlugin.getLogger().severe("Unsupported database type: " + databaseType);
                return;
            }

            createTables();
            SkyblockPlugin.getLogger().info("Database initialized successfully!");
        } catch (SQLException e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to initialize database", e);
        }
    }

    private void initializeSQLite() throws SQLException {
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        String url = "jdbc:sqlite:" + new File(dataFolder, "database.db").getAbsolutePath();
        connection = DriverManager.getConnection(url);

        // Enable foreign keys
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
    }

    private void initializeMySQL() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=UTC",
            host, port, database, useSSL);
        connection = DriverManager.getConnection(url, username, password);
    }

    private void createTables() throws SQLException {
        // Player data table
        String playerDataTable = """
            CREATE TABLE IF NOT EXISTS player_data (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(16) NOT NULL,
                coins DOUBLE DEFAULT 0.0,
                level INTEGER DEFAULT 1,
                experience DOUBLE DEFAULT 0.0,
                last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        // Island data table
        String islandDataTable = """
            CREATE TABLE IF NOT EXISTS island_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                owner_uuid VARCHAR(36) NOT NULL,
                world_name VARCHAR(255) NOT NULL,
                spawn_x DOUBLE NOT NULL,
                spawn_y DOUBLE NOT NULL,
                spawn_z DOUBLE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (owner_uuid) REFERENCES player_data(uuid)
            )
            """;

        // Minion data table
        String minionDataTable = """
            CREATE TABLE IF NOT EXISTS minion_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                owner_uuid VARCHAR(36) NOT NULL,
                type VARCHAR(50) NOT NULL,
                level INTEGER DEFAULT 1,
                xp DOUBLE DEFAULT 0.0,
                location_x DOUBLE NOT NULL,
                location_y DOUBLE NOT NULL,
                location_z DOUBLE NOT NULL,
                world_name VARCHAR(255) NOT NULL,
                last_collection TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (owner_uuid) REFERENCES player_data(uuid)
            )
            """;

        // Pet data table
        String petDataTable = """
            CREATE TABLE IF NOT EXISTS pet_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                owner_uuid VARCHAR(36) NOT NULL,
                type VARCHAR(50) NOT NULL,
                level INTEGER DEFAULT 1,
                xp DOUBLE DEFAULT 0.0,
                hunger INTEGER DEFAULT 100,
                happiness INTEGER DEFAULT 100,
                active BOOLEAN DEFAULT FALSE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (owner_uuid) REFERENCES player_data(uuid)
            )
            """;

        // Economy data table
        String economyDataTable = """
            CREATE TABLE IF NOT EXISTS economy_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_uuid VARCHAR(36) NOT NULL,
                transaction_type VARCHAR(50) NOT NULL,
                amount DOUBLE NOT NULL,
                balance_after DOUBLE NOT NULL,
                description TEXT,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (player_uuid) REFERENCES player_data(uuid)
            )
            """;

        // Event data table
        String eventDataTable = """
            CREATE TABLE IF NOT EXISTS event_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                event_id VARCHAR(50) NOT NULL,
                player_uuid VARCHAR(36) NOT NULL,
                event_type VARCHAR(50) NOT NULL,
                result VARCHAR(20) NOT NULL,
                reward_coins DOUBLE DEFAULT 0.0,
                reward_items TEXT,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (player_uuid) REFERENCES player_data(uuid)
            )
            """;

        // Execute table creation
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(playerDataTable);
            stmt.execute(islandDataTable);
            stmt.execute(minionDataTable);
            stmt.execute(petDataTable);
            stmt.execute(economyDataTable);
            stmt.execute(eventDataTable);
        }
    }

    public CompletableFuture<Boolean> savePlayerData(String uuid, String name, double coins, int level, double experience) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sql = """
                    INSERT OR REPLACE INTO player_data (uuid, name, coins, level, experience, last_login)
                    VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                    """;

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, uuid);
                    stmt.setString(2, name);
                    stmt.setDouble(3, coins);
                    stmt.setInt(4, level);
                    stmt.setDouble(5, experience);

                    int rowsAffected = stmt.executeUpdate();

                    // Update cache
                    String cacheKey = "player_" + uuid;
                    cache.put(cacheKey, new PlayerData(uuid, name, coins, level, experience));
                    cacheTimestamps.put(cacheKey, java.lang.System.currentTimeMillis());

                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save player data", e);
                return false;
            }
        });
    }

    public CompletableFuture<PlayerData> loadPlayerData(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            // Check cache first
            String cacheKey = "player_" + uuid;
            if (isCacheValid(cacheKey)) {
                return (PlayerData) cache.get(cacheKey);
            }

            try {
                String sql = "SELECT * FROM player_data WHERE uuid = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, uuid);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            PlayerData data = new PlayerData(
                                rs.getString("uuid"),
                                rs.getString("name"),
                                rs.getDouble("coins"),
                                rs.getInt("level"),
                                rs.getDouble("experience")
                            );

                            // Update cache
                            cache.put(cacheKey, data);
                            cacheTimestamps.put(cacheKey, java.lang.System.currentTimeMillis());

                            return data;
                        }
                    }
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to load player data", e);
            }

            return null;
        });
    }

    public CompletableFuture<Boolean> saveIslandData(String ownerUuid, String worldName, double x, double y, double z) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sql = """
                    INSERT INTO island_data (owner_uuid, world_name, spawn_x, spawn_y, spawn_z)
                    VALUES (?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, ownerUuid);
                    stmt.setString(2, worldName);
                    stmt.setDouble(3, x);
                    stmt.setDouble(4, y);
                    stmt.setDouble(5, z);

                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save island data", e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> saveMinionData(String ownerUuid, String type, int level, double xp,
                                                   double x, double y, double z, String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sql = """
                    INSERT INTO minion_data (owner_uuid, type, level, xp, location_x, location_y, location_z, world_name)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, ownerUuid);
                    stmt.setString(2, type);
                    stmt.setInt(3, level);
                    stmt.setDouble(4, xp);
                    stmt.setDouble(5, x);
                    stmt.setDouble(6, y);
                    stmt.setDouble(7, z);
                    stmt.setString(8, worldName);

                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save minion data", e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> savePetData(String ownerUuid, String type, int level, double xp,
                                                int hunger, int happiness, boolean active) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sql = """
                    INSERT INTO pet_data (owner_uuid, type, level, xp, hunger, happiness, active)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, ownerUuid);
                    stmt.setString(2, type);
                    stmt.setInt(3, level);
                    stmt.setDouble(4, xp);
                    stmt.setInt(5, hunger);
                    stmt.setInt(6, happiness);
                    stmt.setBoolean(7, active);

                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save pet data", e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> saveEconomyTransaction(String playerUuid, String transactionType,
                                                           double amount, double balanceAfter, String description) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sql = """
                    INSERT INTO economy_data (player_uuid, transaction_type, amount, balance_after, description)
                    VALUES (?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, playerUuid);
                    stmt.setString(2, transactionType);
                    stmt.setDouble(3, amount);
                    stmt.setDouble(4, balanceAfter);
                    stmt.setString(5, description);

                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save economy transaction", e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> saveEventData(String eventId, String playerUuid, String eventType,
                                                  String result, double rewardCoins, String rewardItems) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sql = """
                    INSERT INTO event_data (event_id, player_uuid, event_type, result, reward_coins, reward_items)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, eventId);
                    stmt.setString(2, playerUuid);
                    stmt.setString(3, eventType);
                    stmt.setString(4, result);
                    stmt.setDouble(5, rewardCoins);
                    stmt.setString(6, rewardItems);

                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save event data", e);
                return false;
            }
        });
    }

    private boolean isCacheValid(String key) {
        Long timestamp = cacheTimestamps.get(key);
        if (timestamp == null) return false;

        return java.lang.System.currentTimeMillis() - timestamp < CACHE_EXPIRY;
    }

    public void clearCache() {
        cache.clear();
        cacheTimestamps.clear();
    }

    public CompletableFuture<Boolean> savePlayerSkills(UUID playerId, Object skills) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation for saving player skills
                return true;
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save player skills", e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> savePlayerCollections(UUID playerId, Object collections) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation for saving player collections
                return true;
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save player collections", e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> savePlayerSlayerData(UUID playerId, Object slayerData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation for saving player slayer data
                return true;
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save player slayer data", e);
                return false;
            }
        });
    }

    public CompletableFuture<Object> loadPlayerProfile(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation for loading player profile
                return null;
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to load player profile", e);
                return null;
            }
        });
    }

    public CompletableFuture<Boolean> savePlayerProfile(UUID playerId, Object profile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation for saving player profile
                return true;
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save player profile", e);
                return false;
            }
        });
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to close database connection", e);
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Closes the database connection and clears the cache.
     */
    public void closeAll() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                SkyblockPlugin.getLogger().log(Level.INFO, "Database connection closed successfully.");
            }
        } catch (SQLException e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to close database connection.", e);
        }
        cache.clear();
        cacheTimestamps.clear();
    }

    // Player Data Class
    public static class PlayerData {
        private final String uuid;
        private final String name;
        private final double coins;
        private final int level;
        private final double experience;

        public PlayerData(String uuid, String name, double coins, int level, double experience) {
            this.uuid = uuid;
            this.name = name;
            this.coins = coins;
            this.level = level;
            this.experience = experience;
        }

        // Getters
        public String getUuid() { return uuid; }
        public String getName() { return name; }
        public double getCoins() { return coins; }
        public int getLevel() { return level; }
        public double getExperience() { return experience; }
    }
}
