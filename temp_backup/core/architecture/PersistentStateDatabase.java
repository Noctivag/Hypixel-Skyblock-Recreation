package de.noctivag.skyblock.core.architecture;
import java.util.UUID;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Persistent State Database - Long-term storage for player data and game state
 * 
 * This component provides:
 * - Persistent storage for player inventories, skills, collections
 * - Guild information and relationships
 * - Historical data and statistics
 * - Transaction logging and audit trails
 * - Data migration and backup capabilities
 * 
 * Database Schema:
 * - Players: Core player data and progression
 * - Inventories: Player inventory snapshots
 * - Skills: Skill levels and experience
 * - Collections: Collection progress and unlocks
 * - Guilds: Guild information and membership
 * - Transactions: Economic transaction history
 */
public class PersistentStateDatabase {
    
    private static final Logger logger = Logger.getLogger(PersistentStateDatabase.class.getName());
    
    // Database configuration
    private static final String DEFAULT_DATABASE_URL = "jdbc:sqlite:data/skyblock.db";
    private static final int CONNECTION_POOL_SIZE = 10;
    private static final int CONNECTION_TIMEOUT = 30000; // 30 seconds
    
    private HikariDataSource dataSource;
    private ExecutorService asyncExecutor;
    
    // Prepared statements cache
    private final Map<String, PreparedStatement> statementCache = new ConcurrentHashMap<>();
    
    // State
    private volatile boolean initialized = false;
    private volatile boolean running = false;
    
    public PersistentStateDatabase() {
        this.asyncExecutor = Executors.newFixedThreadPool(4, r -> new Thread(r, "Database-Thread"));
    }
    
    /**
     * Initialize the persistent state database
     */
    public CompletableFuture<Void> initialize() {
        if (initialized) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Initializing Persistent State Database...");
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Initialize connection pool
                initializeConnectionPool();
                
                // Create database schema
                createDatabaseSchema();
                
                // Prepare statements
                prepareStatements();
                
                initialized = true;
                logger.info("Persistent State Database initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize Persistent State Database: " + e.getMessage());
                throw new RuntimeException("Database initialization failed", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Start the database service
     */
    public void start() {
        if (!initialized) {
            throw new IllegalStateException("Database must be initialized before starting");
        }
        
        running = true;
        logger.info("Persistent State Database started");
    }
    
    /**
     * Stop the database service
     */
    public void stop() {
        running = false;
        
        // Close statement cache
        for (PreparedStatement statement : statementCache.values()) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.warning("Error closing prepared statement: " + e.getMessage());
            }
        }
        statementCache.clear();
        
        // Close connection pool
        if (dataSource != null) {
            dataSource.close();
        }
        
        // Shutdown executor
        asyncExecutor.shutdown();
        
        logger.info("Persistent State Database stopped");
    }
    
    /**
     * Save player progression data
     */
    public CompletableFuture<Void> savePlayerProgression(UUID playerId, StateSynchronizationLayer.PlayerProgression progression) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("savePlayerProgression");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "INSERT OR REPLACE INTO player_progression " +
                        "(player_id, combat_level, mining_level, farming_level, foraging_level, " +
                        "fishing_level, enchanting_level, alchemy_level, taming_level, " +
                        "carpentry_level, runecrafting_level, last_updated) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    statementCache.put("savePlayerProgression", statement);
                }
                
                statement.setString(1, playerId.toString());
                statement.setInt(2, progression.getCombatLevel());
                statement.setInt(3, progression.getMiningLevel());
                statement.setInt(4, progression.getFarmingLevel());
                statement.setInt(5, progression.getForagingLevel());
                statement.setInt(6, progression.getFishingLevel());
                statement.setInt(7, progression.getEnchantingLevel());
                statement.setInt(8, progression.getAlchemyLevel());
                statement.setInt(9, progression.getTamingLevel());
                statement.setInt(10, progression.getCarpentryLevel());
                statement.setInt(11, progression.getRunecraftingLevel());
                statement.setTimestamp(12, new Timestamp(java.lang.System.currentTimeMillis()));
                
                statement.executeUpdate();
                
                logger.fine("Saved player progression for: " + playerId);
                
            } catch (SQLException e) {
                logger.severe("Error saving player progression: " + e.getMessage());
                throw new RuntimeException("Failed to save player progression", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Load player progression data
     */
    public CompletableFuture<Optional<StateSynchronizationLayer.PlayerProgression>> loadPlayerProgression(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("loadPlayerProgression");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "SELECT combat_level, mining_level, farming_level, foraging_level, " +
                        "fishing_level, enchanting_level, alchemy_level, taming_level, " +
                        "carpentry_level, runecrafting_level FROM player_progression WHERE player_id = ?"
                    );
                    statementCache.put("loadPlayerProgression", statement);
                }
                
                statement.setString(1, playerId.toString());
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        StateSynchronizationLayer.PlayerProgression progression = 
                            new StateSynchronizationLayer.PlayerProgression(
                                resultSet.getInt("combat_level"),
                                resultSet.getInt("mining_level"),
                                resultSet.getInt("farming_level"),
                                resultSet.getInt("foraging_level"),
                                resultSet.getInt("fishing_level"),
                                resultSet.getInt("enchanting_level"),
                                resultSet.getInt("alchemy_level"),
                                resultSet.getInt("taming_level"),
                                resultSet.getInt("carpentry_level"),
                                resultSet.getInt("runecrafting_level")
                            );
                        
                        return Optional.of(progression);
                    }
                }
                
                return Optional.empty();
                
            } catch (SQLException e) {
                logger.severe("Error loading player progression: " + e.getMessage());
                throw new RuntimeException("Failed to load player progression", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Save player inventory
     */
    public CompletableFuture<Void> savePlayerInventory(UUID playerId, String inventoryData) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("savePlayerInventory");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "INSERT OR REPLACE INTO player_inventories " +
                        "(player_id, inventory_data, last_updated) VALUES (?, ?, ?)"
                    );
                    statementCache.put("savePlayerInventory", statement);
                }
                
                statement.setString(1, playerId.toString());
                statement.setString(2, inventoryData);
                statement.setTimestamp(3, new Timestamp(java.lang.System.currentTimeMillis()));
                
                statement.executeUpdate();
                
                logger.fine("Saved player inventory for: " + playerId);
                
            } catch (SQLException e) {
                logger.severe("Error saving player inventory: " + e.getMessage());
                throw new RuntimeException("Failed to save player inventory", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Load player inventory
     */
    public CompletableFuture<Optional<String>> loadPlayerInventory(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("loadPlayerInventory");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "SELECT inventory_data FROM player_inventories WHERE player_id = ?"
                    );
                    statementCache.put("loadPlayerInventory", statement);
                }
                
                statement.setString(1, playerId.toString());
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(resultSet.getString("inventory_data"));
                    }
                }
                
                return Optional.empty();
                
            } catch (SQLException e) {
                logger.severe("Error loading player inventory: " + e.getMessage());
                throw new RuntimeException("Failed to load player inventory", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Save collection data
     */
    public CompletableFuture<Void> saveCollectionData(UUID playerId, String collectionType, String collectionData) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("saveCollectionData");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "INSERT OR REPLACE INTO player_collections " +
                        "(player_id, collection_type, collection_data, last_updated) VALUES (?, ?, ?, ?)"
                    );
                    statementCache.put("saveCollectionData", statement);
                }
                
                statement.setString(1, playerId.toString());
                statement.setString(2, collectionType);
                statement.setString(3, collectionData);
                statement.setTimestamp(4, new Timestamp(java.lang.System.currentTimeMillis()));
                
                statement.executeUpdate();
                
                logger.fine("Saved collection data for: " + playerId + " type: " + collectionType);
                
            } catch (SQLException e) {
                logger.severe("Error saving collection data: " + e.getMessage());
                throw new RuntimeException("Failed to save collection data", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Load collection data
     */
    public CompletableFuture<Optional<String>> loadCollectionData(UUID playerId, String collectionType) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("loadCollectionData");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "SELECT collection_data FROM player_collections WHERE player_id = ? AND collection_type = ?"
                    );
                    statementCache.put("loadCollectionData", statement);
                }
                
                statement.setString(1, playerId.toString());
                statement.setString(2, collectionType);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(resultSet.getString("collection_data"));
                    }
                }
                
                return Optional.empty();
                
            } catch (SQLException e) {
                logger.severe("Error loading collection data: " + e.getMessage());
                throw new RuntimeException("Failed to load collection data", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Save guild data
     */
    public CompletableFuture<Void> saveGuildData(String guildId, String guildData) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("saveGuildData");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "INSERT OR REPLACE INTO guilds " +
                        "(guild_id, guild_data, last_updated) VALUES (?, ?, ?)"
                    );
                    statementCache.put("saveGuildData", statement);
                }
                
                statement.setString(1, guildId);
                statement.setString(2, guildData);
                statement.setTimestamp(3, new Timestamp(java.lang.System.currentTimeMillis()));
                
                statement.executeUpdate();
                
                logger.fine("Saved guild data for: " + guildId);
                
            } catch (SQLException e) {
                logger.severe("Error saving guild data: " + e.getMessage());
                throw new RuntimeException("Failed to save guild data", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Load guild data
     */
    public CompletableFuture<Optional<String>> loadGuildData(String guildId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("loadGuildData");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "SELECT guild_data FROM guilds WHERE guild_id = ?"
                    );
                    statementCache.put("loadGuildData", statement);
                }
                
                statement.setString(1, guildId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(resultSet.getString("guild_data"));
                    }
                }
                
                return Optional.empty();
                
            } catch (SQLException e) {
                logger.severe("Error loading guild data: " + e.getMessage());
                throw new RuntimeException("Failed to load guild data", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Log economic transaction
     */
    public CompletableFuture<Void> logTransaction(UUID playerId, String transactionType, String transactionData, double amount) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("logTransaction");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "INSERT INTO transactions " +
                        "(player_id, transaction_type, transaction_data, amount, timestamp) " +
                        "VALUES (?, ?, ?, ?, ?)"
                    );
                    statementCache.put("logTransaction", statement);
                }
                
                statement.setString(1, playerId.toString());
                statement.setString(2, transactionType);
                statement.setString(3, transactionData);
                statement.setDouble(4, amount);
                statement.setTimestamp(5, new Timestamp(java.lang.System.currentTimeMillis()));
                
                statement.executeUpdate();
                
                logger.fine("Logged transaction for: " + playerId + " type: " + transactionType);
                
            } catch (SQLException e) {
                logger.severe("Error logging transaction: " + e.getMessage());
                throw new RuntimeException("Failed to log transaction", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Get transaction history
     */
    public CompletableFuture<List<TransactionRecord>> getTransactionHistory(UUID playerId, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = statementCache.get("getTransactionHistory");
                if (statement == null) {
                    statement = connection.prepareStatement(
                        "SELECT transaction_type, transaction_data, amount, timestamp " +
                        "FROM transactions WHERE player_id = ? ORDER BY timestamp DESC LIMIT ?"
                    );
                    statementCache.put("getTransactionHistory", statement);
                }
                
                statement.setString(1, playerId.toString());
                statement.setInt(2, limit);
                
                List<TransactionRecord> transactions = new ArrayList<>();
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        TransactionRecord record = new TransactionRecord(
                            resultSet.getString("transaction_type"),
                            resultSet.getString("transaction_data"),
                            resultSet.getDouble("amount"),
                            resultSet.getTimestamp("timestamp").getTime()
                        );
                        transactions.add(record);
                    }
                }
                
                return transactions;
                
            } catch (SQLException e) {
                logger.severe("Error getting transaction history: " + e.getMessage());
                throw new RuntimeException("Failed to get transaction history", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Initialize connection pool
     */
    private void initializeConnectionPool() {
        HikariConfig config = new HikariConfig();
        
        // Database URL from configuration or default
        String databaseUrl = java.lang.System.getProperty("skyblock.database.url", DEFAULT_DATABASE_URL);
        
        if (databaseUrl.startsWith("jdbc:sqlite:")) {
            config.setJdbcUrl(databaseUrl);
            config.setDriverClassName("org.sqlite.JDBC");
        } else if (databaseUrl.startsWith("jdbc:mysql:")) {
            config.setJdbcUrl(databaseUrl);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setUsername(java.lang.System.getProperty("skyblock.database.username", "skyblock"));
            config.setPassword(java.lang.System.getProperty("skyblock.database.password", ""));
        } else if (databaseUrl.startsWith("jdbc:mariadb:")) {
            config.setJdbcUrl(databaseUrl);
            config.setDriverClassName("org.mariadb.jdbc.Driver");
            config.setUsername(java.lang.System.getProperty("skyblock.database.username", "skyblock"));
            config.setPassword(java.lang.System.getProperty("skyblock.database.password", ""));
        }
        
        config.setMaximumPoolSize(CONNECTION_POOL_SIZE);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        config.setLeakDetectionThreshold(60000); // 1 minute
        
        dataSource = new HikariDataSource(config);
        
        logger.info("Database connection pool initialized with URL: " + databaseUrl);
    }
    
    /**
     * Create database schema
     */
    private void createDatabaseSchema() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // Create tables
            createPlayerProgressionTable(connection);
            createPlayerInventoriesTable(connection);
            createPlayerCollectionsTable(connection);
            createGuildsTable(connection);
            createTransactionsTable(connection);
            
            logger.info("Database schema created successfully");
        }
    }
    
    /**
     * Create player progression table
     */
    private void createPlayerProgressionTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_progression (
                player_id VARCHAR(36) PRIMARY KEY,
                combat_level INTEGER DEFAULT 0,
                mining_level INTEGER DEFAULT 0,
                farming_level INTEGER DEFAULT 0,
                foraging_level INTEGER DEFAULT 0,
                fishing_level INTEGER DEFAULT 0,
                enchanting_level INTEGER DEFAULT 0,
                alchemy_level INTEGER DEFAULT 0,
                taming_level INTEGER DEFAULT 0,
                carpentry_level INTEGER DEFAULT 0,
                runecrafting_level INTEGER DEFAULT 0,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    
    /**
     * Create player inventories table
     */
    private void createPlayerInventoriesTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_inventories (
                player_id VARCHAR(36) PRIMARY KEY,
                inventory_data TEXT,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    
    /**
     * Create player collections table
     */
    private void createPlayerCollectionsTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_collections (
                player_id VARCHAR(36),
                collection_type VARCHAR(50),
                collection_data TEXT,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (player_id, collection_type)
            )
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    
    /**
     * Create guilds table
     */
    private void createGuildsTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS guilds (
                guild_id VARCHAR(36) PRIMARY KEY,
                guild_data TEXT,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    
    /**
     * Create transactions table
     */
    private void createTransactionsTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_id VARCHAR(36),
                transaction_type VARCHAR(50),
                transaction_data TEXT,
                amount DECIMAL(15,2),
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    
    /**
     * Prepare common statements
     */
    private void prepareStatements() {
        // Statements will be prepared on-demand and cached
        logger.info("Statement preparation ready");
    }
    
    /**
     * Transaction record data structure
     */
    public static class TransactionRecord {
        private final String transactionType;
        private final String transactionData;
        private final double amount;
        private final long timestamp;
        
        public TransactionRecord(String transactionType, String transactionData, double amount, long timestamp) {
            this.transactionType = transactionType;
            this.transactionData = transactionData;
            this.amount = amount;
            this.timestamp = timestamp;
        }
        
        public String getTransactionType() { return transactionType; }
        public String getTransactionData() { return transactionData; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
}
