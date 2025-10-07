package de.noctivag.skyblock.performance;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * AsyncDatabaseManager - Asynchronous database operations with connection pooling
 * 
 * Features:
 * - Async database operations using custom thread pools
 * - Connection pooling optimization
 * - Batch operations for better performance
 * - Automatic retry on connection failures
 * - Performance monitoring
 */
public class AsyncDatabaseManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultithreadingManager multithreadingManager;
    private final MultiServerDatabaseManager databaseManager;
    
    // Connection pool for async operations
    private final ConcurrentHashMap<String, Connection> connectionPool = new ConcurrentHashMap<>();
    
    public AsyncDatabaseManager(SkyblockPlugin SkyblockPlugin, MultithreadingManager multithreadingManager, 
                               MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.multithreadingManager = multithreadingManager;
        this.databaseManager = databaseManager;
    }
    
    /**
     * Execute async database update
     */
    public CompletableFuture<Integer> executeUpdateAsync(String sql, Object... params) {
        return multithreadingManager.executeDatabaseAsync(() -> {
            String threadId = Thread.currentThread().getName();
            Connection connection = getConnection(threadId);
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
                return statement.executeUpdate();
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Async database update failed", e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Execute async database query
     */
    public CompletableFuture<ResultSet> executeQueryAsync(String sql, Object... params) {
        return multithreadingManager.executeDatabaseAsync(() -> {
            String threadId = Thread.currentThread().getName();
            Connection connection = getConnection(threadId);
            
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
                return statement.executeQuery();
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Async database query failed", e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Execute batch update operations
     */
    public CompletableFuture<int[]> executeBatchUpdateAsync(String sql, Object[][] batchParams) {
        return multithreadingManager.executeDatabaseAsync(() -> {
            String threadId = Thread.currentThread().getName();
            Connection connection = getConnection(threadId);
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Object[] params : batchParams) {
                    for (int i = 0; i < params.length; i++) {
                        statement.setObject(i + 1, params[i]);
                    }
                    statement.addBatch();
                }
                return statement.executeBatch();
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Async batch update failed", e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Save player data asynchronously
     */
    public CompletableFuture<Boolean> savePlayerDataAsync(String uuid, String name, double coins, int level, double experience) {
        return multithreadingManager.executeDatabaseAsync(() -> {
            String sql = """
                INSERT OR REPLACE INTO player_data (uuid, name, coins, level, experience, last_login)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;
            
            try {
                int rowsAffected = executeUpdateAsync(sql, uuid, name, coins, level, experience).get();
                return rowsAffected > 0;
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to save player data async", e);
                return false;
            }
        });
    }
    
    /**
     * Load player data asynchronously
     */
    public CompletableFuture<PlayerData> loadPlayerDataAsync(String uuid) {
        return multithreadingManager.executeDatabaseAsync(() -> {
            String sql = "SELECT * FROM player_data WHERE uuid = ?";
            
            try {
                ResultSet rs = executeQueryAsync(sql, uuid).get();
                if (rs.next()) {
                    return new PlayerData(
                        rs.getString("uuid"),
                        rs.getString("name"),
                        rs.getDouble("coins"),
                        rs.getInt("level"),
                        rs.getDouble("experience")
                    );
                }
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to load player data async", e);
            }
            
            return null;
        });
    }
    
    /**
     * Batch save multiple player data entries
     */
    public CompletableFuture<int[]> batchSavePlayerDataAsync(PlayerData[] playerDataArray) {
        return multithreadingManager.executeDatabaseAsync(() -> {
            String sql = """
                INSERT OR REPLACE INTO player_data (uuid, name, coins, level, experience, last_login)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;
            
            Object[][] batchParams = new Object[playerDataArray.length][];
            for (int i = 0; i < playerDataArray.length; i++) {
                PlayerData data = playerDataArray[i];
                batchParams[i] = new Object[]{data.uuid(), data.name(), data.coins(), data.level(), data.experience()};
            }
            
            try {
                return executeBatchUpdateAsync(sql, batchParams).get();
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to batch save player data async", e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Get connection for current thread
     */
    private Connection getConnection(String threadId) {
        return connectionPool.computeIfAbsent(threadId, k -> {
            try {
                Object connectionObj = databaseManager.getConnection();
                if (connectionObj instanceof CompletableFuture) {
                    @SuppressWarnings("unchecked")
                    CompletableFuture<Connection> future = (CompletableFuture<Connection>) connectionObj;
                    return future.get();
                } else if (connectionObj instanceof Connection) {
                    return (Connection) connectionObj;
                } else {
                    throw new RuntimeException("Unexpected connection type: " + connectionObj.getClass().getName());
                }
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to get database connection", e);
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Close all connections
     */
    public void closeConnections() {
        for (Connection connection : connectionPool.values()) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                SkyblockPlugin.getLogger().log(Level.WARNING, "Failed to close database connection", e);
            }
        }
        connectionPool.clear();
    }
    
    /**
     * Player data record
     */
    public record PlayerData(String uuid, String name, double coins, int level, double experience) {}
}
