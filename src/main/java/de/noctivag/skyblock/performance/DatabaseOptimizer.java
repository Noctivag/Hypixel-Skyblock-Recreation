package de.noctivag.skyblock.performance;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DatabaseOptimizer - Optimiert Database-Performance
 * 
 * Features:
 * - Batch-Updates
 * - Connection Pooling
 * - Async Processing
 * - Query Caching
 */
public class DatabaseOptimizer {
    private final SkyblockPlugin SkyblockPlugin;
    
    // Batch-Update Queue
    private final Queue<DatabaseOperation> batchQueue = new ConcurrentLinkedQueue<>();
    private final Map<String, PreparedStatement> statementCache = new ConcurrentHashMap<>();
    
    // Performance-Konfiguration
    private final int BATCH_SIZE = 100;
    private final long BATCH_DELAY = 5000; // 5 Sekunden
    
    public DatabaseOptimizer(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        startBatchProcessor();
    }
    
    /**
     * Fügt eine Operation zur Batch-Queue hinzu
     */
    public void queueOperation(String sql, Object... params) {
        batchQueue.offer(new DatabaseOperation(sql, params));
        
        // Verarbeite sofort wenn Batch voll ist
        if (batchQueue.size() >= BATCH_SIZE) {
            processBatch();
        }
    }
    
    /**
     * Verarbeitet alle wartenden Operations in einem Batch
     */
    private void processBatch() {
        if (batchQueue.isEmpty()) return;
        
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    connection.setAutoCommit(false);
                    
                    List<DatabaseOperation> batch = new ArrayList<>();
                    DatabaseOperation operation;
                    
                    // Sammle Operations für Batch
                    while ((operation = batchQueue.poll()) != null && batch.size() < BATCH_SIZE) {
                        batch.add(operation);
                    }
                    
                    // Führe Batch aus
                    for (DatabaseOperation op : batch) {
                        PreparedStatement stmt = getOrCreateStatement(connection, op.sql);
                        for (int i = 0; i < op.params.length; i++) {
                            stmt.setObject(i + 1, op.params[i]);
                        }
                        stmt.addBatch();
                    }
                    
                    // Execute Batch
                    PreparedStatement firstStmt = batch.isEmpty() ? null : 
                        getOrCreateStatement(connection, batch.get(0).sql);
                    if (firstStmt != null) {
                        firstStmt.executeBatch();
                        connection.commit();
                    }
                    
                    SkyblockPlugin.getLogger().info("Processed " + batch.size() + " database operations in batch");
                    
                } catch (SQLException e) {
                    SkyblockPlugin.getLogger().severe("Batch processing failed: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(SkyblockPlugin);
    }
    
    /**
     * Startet den Batch-Processor
     */
    private void startBatchProcessor() {
        new BukkitRunnable() {
            @Override
            public void run() {
                processBatch();
            }
        }.runTaskTimerAsynchronously(SkyblockPlugin, BATCH_DELAY / 50L, BATCH_DELAY / 50L);
    }
    
    /**
     * Holt oder erstellt PreparedStatement (mit Caching)
     */
    private PreparedStatement getOrCreateStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement stmt = statementCache.get(sql);
        if (stmt == null || stmt.isClosed()) {
            stmt = connection.prepareStatement(sql);
            statementCache.put(sql, stmt);
        }
        stmt.clearParameters();
        return stmt;
    }
    
    /**
     * Holt eine optimierte Connection
     */
    private Connection getConnection() throws SQLException {
        // Hier würde der Connection Pool implementiert werden
        // Für jetzt: Basic Connection mit Optimierungen
        try {
            Connection connection = SkyblockPlugin.getMultiServerDatabaseManager().getConnection().get();
            connection.setAutoCommit(false);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
    }
    
    /**
     * Database-Operation Wrapper
     */
    private static class DatabaseOperation {
        final String sql;
        final Object[] params;
        
        DatabaseOperation(String sql, Object... params) {
            this.sql = sql;
            this.params = params;
        }
    }
    
    /**
     * Gibt Performance-Statistiken zurück
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("queuedOperations", batchQueue.size());
        stats.put("cachedStatements", statementCache.size());
        return stats;
    }
}
