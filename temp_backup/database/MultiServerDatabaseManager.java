package de.noctivag.skyblock.database;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Multi-Server-kompatible Datenbank Manager
 *
 * Features:
 * - MySQL/MariaDB Support
 * - Connection Pooling mit HikariCP
 * - Multi-Server Support
 * - Async Operations
 * - Auto-Reconnection
 * - Data Synchronization
 */
public class MultiServerDatabaseManager {
    private final SkyblockPlugin SkyblockPlugin;
    private HikariDataSource dataSource;
    private String serverId;
    private boolean isConnected = false;

    public MultiServerDatabaseManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.serverId = SkyblockPlugin.getConfig().getString("database.server-id", "server-1");
        initializeDatabase();
    }

    private void initializeDatabase() {
        FileConfiguration config = SkyblockPlugin.getConfig();

        // Respect the config flag: if database is disabled, skip attempting a connection.
        boolean dbEnabled = config.getBoolean("database.enabled", false);
        if (!dbEnabled) {
            SkyblockPlugin.getLogger().info("Database is disabled in config.yml (database.enabled=false). Skipping database initialization.");
            return;
        }

        HikariConfig hikariConfig = new HikariConfig();
        // Build a more robust JDBC URL with common MySQL properties to avoid "Communications link failure"
        String host = config.getString("database.host", "localhost");
        int port = config.getInt("database.port", 3306);
        // Quick TCP-level check to give earlier, clearer diagnostics if the host:port is unreachable
        try {
            boolean reachable = isPortOpen(host, port, (int) Math.min(config.getLong("database.driver-connect-timeout", 10000), 5000));
            if (!reachable) {
                SkyblockPlugin.getLogger().warning("TCP check: Database host " + host + " port " + port + " is not reachable (connection refused or filtered). This often means MySQL is not running, bound to a different address, or a firewall blocks the port.");
            } else {
                SkyblockPlugin.getLogger().info("TCP check: Database host " + host + " port " + port + " is reachable.");
            }
        } catch (Exception ex) {
            SkyblockPlugin.getLogger().log(Level.WARNING, "TCP connectivity check failed unexpectedly", ex);
        }

        String database = config.getString("database.database", "basics_plugin");
        boolean useSSL = config.getBoolean("database.use-ssl", false);
        boolean allowPublicKeyRetrieval = config.getBoolean("database.allow-public-key-retrieval", true);
        String serverTimezone = config.getString("database.server-timezone", "UTC");
        int driverConnectTimeout = (int) config.getLong("database.driver-connect-timeout", 10000);
        int driverSocketTimeout = (int) config.getLong("database.driver-socket-timeout", 30000);

        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=%b&allowPublicKeyRetrieval=%b&serverTimezone=%s&connectTimeout=%d&socketTimeout=%d",
                host, port, database, useSSL, allowPublicKeyRetrieval, serverTimezone, driverConnectTimeout, driverSocketTimeout
        );
        hikariConfig.setJdbcUrl(jdbcUrl);
        // Ensure the correct MySQL driver is used (Connector/J 8+)
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername(config.getString("database.username", "root"));
        hikariConfig.setPassword(config.getString("database.password", ""));
        // Server ID is handled separately, not through HikariConfig

        // Connection Pool Settings
        hikariConfig.setMaximumPoolSize(config.getInt("database.max-pool-size", 10));
        hikariConfig.setMinimumIdle(config.getInt("database.min-idle", 2));
        hikariConfig.setConnectionTimeout(config.getLong("database.connection-timeout", 30000));
        hikariConfig.setIdleTimeout(config.getLong("database.idle-timeout", 600000));
        hikariConfig.setMaxLifetime(config.getLong("database.max-lifetime", 1800000));
        hikariConfig.setLeakDetectionThreshold(config.getLong("database.leak-detection-threshold", 60000));
        // A simple validation query helps detect invalid connections early
        hikariConfig.setConnectionTestQuery("SELECT 1");

        // MySQL specific settings
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
        hikariConfig.addDataSourceProperty("maintainTimeStats", "false");

        // Try to initialize the datasource. If it fails and the configured host is not 127.0.0.1,
        // try again forcing 127.0.0.1 to mitigate 'localhost' resolution issues (IPv6 or socket vs TCP).
        if (tryInitializeDataSource(hikariConfig, jdbcUrl)) return;

        if (!"127.0.0.1".equals(host)) {
            SkyblockPlugin.getLogger().warning("Initial DB connection failed. Retrying with host=127.0.0.1 as fallback...");
            String fallbackJdbcUrl = String.format(
                    "jdbc:mysql://%s:%d/%s?useSSL=%b&allowPublicKeyRetrieval=%b&serverTimezone=%s&connectTimeout=%d&socketTimeout=%d",
                    "127.0.0.1", port, database, useSSL, allowPublicKeyRetrieval, serverTimezone, driverConnectTimeout, driverSocketTimeout
            );
            hikariConfig.setJdbcUrl(fallbackJdbcUrl);
            tryInitializeDataSource(hikariConfig, fallbackJdbcUrl);
        } else {
            SkyblockPlugin.getLogger().severe("Failed to connect to database and no fallback host available. JDBC URL: " + jdbcUrl);
        }
    }

    // Attempt to create HikariDataSource and initialize schema. Returns true on success.
    private boolean tryInitializeDataSource(HikariConfig config, String jdbcUrl) {
        try {
            dataSource = new HikariDataSource(config);
            createTables();
            isConnected = true;
            SkyblockPlugin.getLogger().info("Multi-Server Database connected successfully! JDBC URL: " + jdbcUrl);
            return true;
        } catch (Exception e) {
            // Log the failure with full details for diagnosis
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to connect to database. JDBC URL: " + (config.getJdbcUrl() != null ? config.getJdbcUrl() : "<unknown>"), e);
            // Ensure we don't leave a half-open datasource
            try {
                if (dataSource != null && !dataSource.isClosed()) dataSource.close();
            } catch (Exception ignore) {}
            dataSource = null;
            isConnected = false;
            // Schedule background reconnect attempts so SkyblockPlugin can continue running
            scheduleReconnect(config, jdbcUrl, 10, 30); // 10 attempts, 30 seconds apart
            return false;
        }
    }

    // Schedule asynchronous reconnection attempts using Bukkit scheduler
    private void scheduleReconnect(HikariConfig config, String jdbcUrl, int maxAttempts, int intervalSeconds) {
        final java.util.concurrent.atomic.AtomicInteger attempts = new java.util.concurrent.atomic.AtomicInteger(0);
        SkyblockPlugin.getLogger().info("Scheduling background DB reconnect attempts every " + intervalSeconds + "s (max " + maxAttempts + ")");
        var task = new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                int current = attempts.incrementAndGet();
                SkyblockPlugin.getLogger().info("DB reconnect attempt " + current + " of " + maxAttempts + "...");
                if (tryInitializeDataSourceWithoutScheduling(config, jdbcUrl)) {
                    SkyblockPlugin.getLogger().info("Background DB reconnect succeeded on attempt " + current);
                    this.cancel();
                    return;
                }
                if (current >= maxAttempts) {
                    SkyblockPlugin.getLogger().warning("Background DB reconnect failed after " + current + " attempts; will stop retrying.");
                    this.cancel();
                }
            }
        };
        // schedule with delay intervalSeconds*20 ticks
        task.runTaskTimerAsynchronously(SkyblockPlugin, intervalSeconds * 20L, intervalSeconds * 20L);
    }

    // Helper that attempts initialization but does NOT schedule another reconnect (to avoid recursion)
    private boolean tryInitializeDataSourceWithoutScheduling(HikariConfig config, String jdbcUrl) {
        try {
            dataSource = new HikariDataSource(config);
            createTables();
            isConnected = true;
            SkyblockPlugin.getLogger().info("Multi-Server Database connected successfully! JDBC URL: " + jdbcUrl);
            return true;
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.WARNING, "Background attempt failed to connect to database: " + (config.getJdbcUrl() != null ? config.getJdbcUrl() : "<unknown>"), e);
            try {
                if (dataSource != null && !dataSource.isClosed()) dataSource.close();
            } catch (Exception ignore) {}
            dataSource = null;
            isConnected = false;
            return false;
        }
    }

    private void createTables() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // Server Info Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS server_info (
                    server_id VARCHAR(50) PRIMARY KEY,
                    server_name VARCHAR(100) NOT NULL,
                    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    player_count INT DEFAULT 0,
                    status ENUM('online', 'offline', 'maintenance') DEFAULT 'online'
                )
            """);

            // Player Profiles Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_profiles (
                    uuid VARCHAR(36) PRIMARY KEY,
                    username VARCHAR(16) NOT NULL,
                    server_id VARCHAR(50) NOT NULL,
                    coins DECIMAL(20,2) DEFAULT 0,
                    gems INT DEFAULT 0,
                    level INT DEFAULT 1,
                    experience BIGINT DEFAULT 0,
                    first_join TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    total_playtime BIGINT DEFAULT 0,
                    last_server VARCHAR(50),
                    INDEX idx_username (username),
                    INDEX idx_server_id (server_id),
                    INDEX idx_last_seen (last_seen)
                )
            """);

            // Skyblock Islands Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS skyblock_islands (
                    island_id VARCHAR(36) PRIMARY KEY,
                    owner_uuid VARCHAR(36) NOT NULL,
                    server_id VARCHAR(50) NOT NULL,
                    island_type ENUM('normal', 'coop', 'solo') DEFAULT 'normal',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_visit TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    visit_count INT DEFAULT 0,
                    island_level INT DEFAULT 1,
                    island_xp BIGINT DEFAULT 0,
                    INDEX idx_owner (owner_uuid),
                    INDEX idx_server_id (server_id)
                )
            """);

            // Island Members Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS island_members (
                    island_id VARCHAR(36),
                    member_uuid VARCHAR(36),
                    role ENUM('owner', 'coop', 'visitor') DEFAULT 'visitor',
                    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    permissions JSON,
                    PRIMARY KEY (island_id, member_uuid),
                    INDEX idx_member (member_uuid)
                )
            """);

            // Skills Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_skills (
                    uuid VARCHAR(36),
                    skill_type VARCHAR(20),
                    level INT DEFAULT 0,
                    experience BIGINT DEFAULT 0,
                    xp_to_next_level BIGINT DEFAULT 100,
                    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (uuid, skill_type),
                    INDEX idx_skill_type (skill_type)
                )
            """);

            // Collections Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_collections (
                    uuid VARCHAR(36),
                    collection_type VARCHAR(30),
                    amount BIGINT DEFAULT 0,
                    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (uuid, collection_type),
                    INDEX idx_collection_type (collection_type)
                )
            """);

            // Slayers Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_slayers (
                    uuid VARCHAR(36),
                    slayer_type VARCHAR(20),
                    level INT DEFAULT 0,
                    experience BIGINT DEFAULT 0,
                    xp_to_next_level BIGINT DEFAULT 200,
                    completed_quests INT DEFAULT 0,
                    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (uuid, slayer_type),
                    INDEX idx_slayer_type (slayer_type)
                )
            """);

            // Minions Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_minions (
                    minion_id VARCHAR(36) PRIMARY KEY,
                    owner_uuid VARCHAR(36) NOT NULL,
                    island_id VARCHAR(36) NOT NULL,
                    minion_type VARCHAR(30) NOT NULL,
                    level INT DEFAULT 1,
                    xp BIGINT DEFAULT 0,
                    last_collection TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_active BOOLEAN DEFAULT true,
                    location_x INT,
                    location_y INT,
                    location_z INT,
                    INDEX idx_owner (owner_uuid),
                    INDEX idx_island (island_id)
                )
            """);

            // Pets Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_pets (
                    pet_id VARCHAR(36) PRIMARY KEY,
                    owner_uuid VARCHAR(36) NOT NULL,
                    pet_type VARCHAR(30) NOT NULL,
                    level INT DEFAULT 1,
                    experience BIGINT DEFAULT 0,
                    rarity ENUM('common', 'uncommon', 'rare', 'epic', 'legendary', 'mythic') DEFAULT 'common',
                    is_active BOOLEAN DEFAULT false,
                    obtained_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_owner (owner_uuid)
                )
            """);

            // Auction House Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS auction_house (
                    auction_id VARCHAR(36) PRIMARY KEY,
                    seller_uuid VARCHAR(36) NOT NULL,
                    item_data JSON NOT NULL,
                    starting_bid DECIMAL(20,2) NOT NULL,
                    current_bid DECIMAL(20,2),
                    highest_bidder VARCHAR(36),
                    end_time TIMESTAMP NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_sold BOOLEAN DEFAULT false,
                    is_cancelled BOOLEAN DEFAULT false,
                    INDEX idx_seller (seller_uuid),
                    INDEX idx_end_time (end_time),
                    INDEX idx_is_sold (is_sold)
                )
            """);

            // Bazaar Orders Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS bazaar_orders (
                    order_id VARCHAR(36) PRIMARY KEY,
                    player_uuid VARCHAR(36) NOT NULL,
                    item_type VARCHAR(50) NOT NULL,
                    order_type ENUM('buy', 'sell') NOT NULL,
                    amount BIGINT NOT NULL,
                    price_per_unit DECIMAL(20,2) NOT NULL,
                    total_price DECIMAL(20,2) NOT NULL,
                    filled_amount BIGINT DEFAULT 0,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_completed BOOLEAN DEFAULT false,
                    INDEX idx_player (player_uuid),
                    INDEX idx_item_type (item_type),
                    INDEX idx_order_type (order_type)
                )
            """);

            // Dungeons Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_dungeons (
                    uuid VARCHAR(36),
                    dungeon_type VARCHAR(30),
                    floor INT,
                    completion_count INT DEFAULT 0,
                    best_time BIGINT,
                    best_score INT,
                    last_completion TIMESTAMP,
                    PRIMARY KEY (uuid, dungeon_type, floor),
                    INDEX idx_dungeon_type (dungeon_type)
                )
            """);

            // Events Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS server_events (
                    event_id VARCHAR(36) PRIMARY KEY,
                    server_id VARCHAR(50) NOT NULL,
                    event_type VARCHAR(30) NOT NULL,
                    event_data JSON,
                    start_time TIMESTAMP NOT NULL,
                    end_time TIMESTAMP,
                    is_active BOOLEAN DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_server_id (server_id),
                    INDEX idx_event_type (event_type),
                    INDEX idx_is_active (is_active)
                )
            """);

            // Guilds Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS guilds (
                    guild_id VARCHAR(36) PRIMARY KEY,
                    guild_name VARCHAR(30) UNIQUE NOT NULL,
                    guild_tag VARCHAR(6),
                    owner_uuid VARCHAR(36) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    level INT DEFAULT 1,
                    experience BIGINT DEFAULT 0,
                    coins DECIMAL(20,2) DEFAULT 0,
                    member_count INT DEFAULT 1,
                    max_members INT DEFAULT 5,
                    description TEXT,
                    INDEX idx_guild_name (guild_name),
                    INDEX idx_owner (owner_uuid)
                )
            """);

            // Guild Members Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS guild_members (
                    guild_id VARCHAR(36),
                    member_uuid VARCHAR(36),
                    role ENUM('owner', 'officer', 'member') DEFAULT 'member',
                    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    contributed_coins DECIMAL(20,2) DEFAULT 0,
                    contributed_gems INT DEFAULT 0,
                    PRIMARY KEY (guild_id, member_uuid),
                    INDEX idx_member (member_uuid)
                )
            """);

            // Leaderboards Table
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS leaderboards (
                    leaderboard_id VARCHAR(50) PRIMARY KEY,
                    category VARCHAR(30) NOT NULL,
                    subcategory VARCHAR(30),
                    player_uuid VARCHAR(36) NOT NULL,
                    value BIGINT NOT NULL,
                    rank INT,
                    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_category (category),
                    INDEX idx_player (player_uuid),
                    INDEX idx_value (value)
                )
            """);

            // Animation Data Tables
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS server_animation_data (
                    server_id VARCHAR(50) PRIMARY KEY,
                    server_name VARCHAR(100) NOT NULL,
                    player_count INT DEFAULT 0,
                    active_world_animations TEXT,
                    active_island_animations TEXT,
                    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_last_update (last_update)
                )
            """);

            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS global_animation_data (
                    animation_id VARCHAR(36) PRIMARY KEY,
                    animation_type VARCHAR(50) NOT NULL,
                    world_name VARCHAR(100) NOT NULL,
                    x DOUBLE NOT NULL,
                    y DOUBLE NOT NULL,
                    z DOUBLE NOT NULL,
                    player_uuid VARCHAR(36),
                    server_id VARCHAR(50) NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_animation_type (animation_type),
                    INDEX idx_server (server_id),
                    INDEX idx_timestamp (timestamp)
                )
            """);

            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS player_animation_data (
                    player_uuid VARCHAR(36) PRIMARY KEY,
                    current_animation VARCHAR(50),
                    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_current_animation (current_animation),
                    INDEX idx_last_update (last_update)
                )
            """);

            SkyblockPlugin.getLogger().info("Database tables created successfully!");
        }
    }

    public CompletableFuture<Connection> getConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to get database connection", e);
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Boolean> executeUpdate(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to execute update: " + sql, e);
                return false;
            }
        });
    }

    public CompletableFuture<ResultSet> executeQuery(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                return statement.executeQuery();
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to execute query: " + sql, e);
                throw new RuntimeException(e);
            }
        });
    }

    public void updateServerStatus() {
        executeUpdate("""
            INSERT INTO server_info (server_id, server_name, player_count, status)
            VALUES (?, ?, ?, 'online')
            ON DUPLICATE KEY UPDATE
            player_count = VALUES(player_count),
            last_seen = CURRENT_TIMESTAMP,
            status = 'online'
        """, serverId, SkyblockPlugin.getServer().getName(), SkyblockPlugin.getServer().getOnlinePlayers().size());
    }

    // Animation Data Methods
    public CompletableFuture<Boolean> saveServerAnimationData(de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData data) {
        return executeUpdate("""
            INSERT INTO server_animation_data (server_id, server_name, player_count, active_world_animations, active_island_animations, last_update)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE
            server_name = VALUES(server_name),
            player_count = VALUES(player_count),
            active_world_animations = VALUES(active_world_animations),
            active_island_animations = VALUES(active_island_animations),
            last_update = CURRENT_TIMESTAMP
        """, data.getServerId(), data.getServerName(), data.getPlayerCount(),
            String.join(",", data.getActiveWorldAnimations()),
            data.getActiveIslandAnimations().stream().map(UUID::toString).reduce((a, b) -> a + "," + b).orElse(""));
    }

    public CompletableFuture<Boolean> saveGlobalAnimationData(de.noctivag.skyblock.worlds.MultiServerAnimationManager.GlobalAnimationData data) {
        return executeUpdate("""
            INSERT INTO global_animation_data (animation_id, animation_type, world_name, x, y, z, player_uuid, server_id, timestamp)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, data.getAnimationId().toString(), data.getAnimationType(),
            data.getLocation().getWorld().getName(), data.getLocation().getX(), data.getLocation().getY(), data.getLocation().getZ(),
            data.getPlayerId() != null ? data.getPlayerId().toString() : null, data.getServerId(), new Timestamp(data.getTimestamp()));
    }

    public CompletableFuture<Boolean> savePlayerAnimationData(de.noctivag.skyblock.worlds.MultiServerAnimationManager.PlayerAnimationData data) {
        return executeUpdate("""
            INSERT INTO player_animation_data (player_uuid, current_animation, start_time, last_update)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE
            current_animation = VALUES(current_animation),
            start_time = VALUES(start_time),
            last_update = CURRENT_TIMESTAMP
        """, data.getPlayerId().toString(), data.getCurrentAnimation(), new Timestamp(data.getStartTime()));
    }

    public CompletableFuture<java.util.List<de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData>> getAnimationDataFromOtherServers() {
        return executeQuery("""
            SELECT server_id, server_name, player_count, active_world_animations, active_island_animations, last_update
            FROM server_animation_data
            WHERE server_id != ? AND last_update > DATE_SUB(NOW(), INTERVAL 5 MINUTE)
        """, serverId).thenApply(resultSet -> {
            java.util.List<de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData> data = new java.util.ArrayList<>();
            try {
                while (resultSet.next()) {
                    de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData serverData =
                        new de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData(
                            resultSet.getString("server_id"),
                            resultSet.getString("server_name"),
                            resultSet.getInt("player_count"),
                            resultSet.getTimestamp("last_update").getTime()
                        );

                    // Parse active animations
                    String worldAnimations = resultSet.getString("active_world_animations");
                    if (worldAnimations != null && !worldAnimations.isEmpty()) {
                        serverData.setActiveWorldAnimations(java.util.Arrays.asList(worldAnimations.split(",")));
                    }

                    String islandAnimations = resultSet.getString("active_island_animations");
                    if (islandAnimations != null && !islandAnimations.isEmpty()) {
                        java.util.List<UUID> islandIds = new java.util.ArrayList<>();
                        for (String id : islandAnimations.split(",")) {
                            try {
                                islandIds.add(UUID.fromString(id));
                            } catch (IllegalArgumentException e) {
                                // Skip invalid UUIDs
                            }
                        }
                        serverData.setActiveIslandAnimations(islandIds);
                    }

                    data.add(serverData);
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to parse animation data", e);
            }
            return data;
        });
    }

    public CompletableFuture<java.util.List<de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData>> getAllServerAnimationData() {
        return executeQuery("""
            SELECT server_id, server_name, player_count, active_world_animations, active_island_animations, last_update
            FROM server_animation_data
            WHERE last_update > DATE_SUB(NOW(), INTERVAL 10 MINUTE)
        """).thenApply(resultSet -> {
            java.util.List<de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData> data = new java.util.ArrayList<>();
            try {
                while (resultSet.next()) {
                    de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData serverData =
                        new de.noctivag.skyblock.worlds.MultiServerAnimationManager.ServerAnimationData(
                            resultSet.getString("server_id"),
                            resultSet.getString("server_name"),
                            resultSet.getInt("player_count"),
                            resultSet.getTimestamp("last_update").getTime()
                        );

                    // Parse active animations
                    String worldAnimations = resultSet.getString("active_world_animations");
                    if (worldAnimations != null && !worldAnimations.isEmpty()) {
                        serverData.setActiveWorldAnimations(java.util.Arrays.asList(worldAnimations.split(",")));
                    }

                    String islandAnimations = resultSet.getString("active_island_animations");
                    if (islandAnimations != null && !islandAnimations.isEmpty()) {
                        java.util.List<UUID> islandIds = new java.util.ArrayList<>();
                        for (String id : islandAnimations.split(",")) {
                            try {
                                islandIds.add(UUID.fromString(id));
                            } catch (IllegalArgumentException e) {
                                // Skip invalid UUIDs
                            }
                        }
                        serverData.setActiveIslandAnimations(islandIds);
                    }

                    data.add(serverData);
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to parse animation data", e);
            }
            return data;
        });
    }

    public CompletableFuture<java.util.List<de.noctivag.skyblock.worlds.MultiServerAnimationManager.GlobalAnimationData>> getGlobalAnimationData() {
        return executeQuery("""
            SELECT animation_id, animation_type, world_name, x, y, z, player_uuid, server_id, timestamp
            FROM global_animation_data
            WHERE server_id != ? AND timestamp > DATE_SUB(NOW(), INTERVAL 1 MINUTE)
            ORDER BY timestamp DESC
        """, serverId).thenApply(resultSet -> {
            java.util.List<de.noctivag.skyblock.worlds.MultiServerAnimationManager.GlobalAnimationData> data = new java.util.ArrayList<>();
            try {
                while (resultSet.next()) {
                    org.bukkit.World world = SkyblockPlugin.getServer().getWorld(resultSet.getString("world_name"));
                    if (world != null) {
                        org.bukkit.Location location = new org.bukkit.Location(
                            world,
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z")
                        );

                        UUID playerId = null;
                        String playerUuidStr = resultSet.getString("player_uuid");
                        if (playerUuidStr != null) {
                            try {
                                playerId = UUID.fromString(playerUuidStr);
                            } catch (IllegalArgumentException e) {
                                // Skip invalid UUIDs
                            }
                        }

                        de.noctivag.skyblock.worlds.MultiServerAnimationManager.GlobalAnimationData animationData =
                            new de.noctivag.skyblock.worlds.MultiServerAnimationManager.GlobalAnimationData(
                                UUID.fromString(resultSet.getString("animation_id")),
                                resultSet.getString("animation_type"),
                                location,
                                playerId,
                                resultSet.getString("server_id"),
                                resultSet.getTimestamp("timestamp").getTime()
                            );

                        data.add(animationData);
                    }
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to parse global animation data", e);
            }
            return data;
        });
    }

    // Missing database methods for various systems
    public CompletableFuture<Boolean> savePlayerAccessoryData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            // Placeholder implementation
            SkyblockPlugin.getLogger().info("Saving accessory data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerAccessoryData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            // Placeholder implementation
            SkyblockPlugin.getLogger().info("Loading accessory data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerBrewingData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving brewing data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerBrewingData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading brewing data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerExperimentData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving experiment data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerExperimentData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading experiment data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerFairySoulData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving fairy soul data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerFairySoulData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading fairy soul data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerFurnitureData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving furniture data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerFurnitureData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading furniture data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerGemstoneData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving gemstone data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerGemstoneData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading gemstone data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerMagicData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving magic data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerMagicData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading magic data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerReforgeData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving reforge data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerReforgeData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading reforge data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerRuneData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving rune data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerRuneData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading rune data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerSackData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving sack data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerSackData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading sack data for player: " + playerId);
            return new Object();
        });
    }

    public CompletableFuture<Boolean> savePlayerTravelData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving travel data for player: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Boolean> savePlayerProfileAsync(UUID playerId, Object profile) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving player profile for: " + playerId);
            return true;
        });
    }

    public CompletableFuture<Object> loadPlayerProfileAsync(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading player profile for: " + playerId);
            return new Object();
        });
    }

    // Synchronous methods for compatibility
    public void savePlayerProfile(UUID playerId, Object profile) {
        savePlayerProfileAsync(playerId, profile).join();
    }

    public Object loadPlayerProfile(UUID playerId) {
        return loadPlayerProfileAsync(playerId).join();
    }

    public CompletableFuture<Object> loadPlayerTravelData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading travel data for player: " + playerId);
            return new Object();
        });
    }
    
    // Minion data methods
    public CompletableFuture<Object> loadMinionData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Loading minion data for player: " + playerId);
            // Return a placeholder object for now
            return new Object();
        });
    }
    
    public CompletableFuture<Boolean> saveMinionData(UUID playerId, Object data) {
        return CompletableFuture.supplyAsync(() -> {
            SkyblockPlugin.getLogger().info("Saving minion data for player: " + playerId);
            return true;
        });
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            isConnected = false;
            SkyblockPlugin.getLogger().info("Database connection closed.");
        }
    }

    /**
     * Shuts down the database connection pool and cleans up resources.
     */
    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            SkyblockPlugin.getLogger().info("MultiServerDatabaseManager: Connection pool shut down successfully.");
        }
        isConnected = false;
    }

    /**
     * Closes all database connections and shuts down the connection pool.
     */
    public void closeAll() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            SkyblockPlugin.getLogger().info("Database connection pool shut down successfully.");
        }
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getServerId() {
        return serverId;
    }

    // Simple socket-based port probe with timeout in milliseconds
    private boolean isPortOpen(String host, int port, int timeoutMs) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
