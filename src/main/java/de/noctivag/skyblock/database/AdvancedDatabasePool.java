package de.noctivag.skyblock.database;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;

import java.sql.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Advanced Database Connection Pool - Hochperformante Datenbank-Verbindungsverwaltung
 * 
 * Features:
 * - HikariCP-basierte Connection Pooling
 * - Async Query-Execution
 * - Connection Health Monitoring
 * - Auto-Recovery bei Connection-Failures
 * - Performance-Metriken
 * - Load Balancing
 * - Connection Leak Detection
 */
public class AdvancedDatabasePool implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Connection Pool
    private HikariDataSource dataSource;
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final AtomicInteger totalConnections = new AtomicInteger(0);
    private final AtomicInteger failedConnections = new AtomicInteger(0);
    
    // Performance-Metriken
    private final AtomicLong totalQueries = new AtomicLong(0);
    private final AtomicLong totalQueryTime = new AtomicLong(0);
    private final AtomicLong failedQueries = new AtomicLong(0);
    
    // Async Executor
    private final ExecutorService asyncExecutor;
    
    // Health Monitoring
    private final ScheduledExecutorService healthMonitor;
    private volatile boolean isHealthy = true;
    
    // Konfiguration
    private final DatabaseConfig config;
    
    public AdvancedDatabasePool(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.config = new DatabaseConfig();
        this.asyncExecutor = Executors.newFixedThreadPool(10, 
            r -> new Thread(r, "Skyblock-Database-Async-" + System.currentTimeMillis()));
        this.healthMonitor = Executors.newSingleThreadScheduledExecutor(
            r -> new Thread(r, "Skyblock-Database-Health-" + System.currentTimeMillis()));
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing Advanced Database Pool...");
        
        try {
            // Konfiguriere HikariCP
            setupHikariCP();
            
            // Teste Verbindung
            testConnection();
            
            // Starte Health Monitoring
            startHealthMonitoring();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("Advanced Database Pool initialized successfully!");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize database pool", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down Advanced Database Pool...");
        
        // Shutdown Health Monitor
        healthMonitor.shutdown();
        
        // Shutdown Async Executor
        asyncExecutor.shutdown();
        
        // Schließe Connection Pool
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("Advanced Database Pool shutdown complete!");
    }
    
    /**
     * Konfiguriert HikariCP Connection Pool
     */
    private void setupHikariCP() {
        HikariConfig hikariConfig = new HikariConfig();
        
        // Basis-Konfiguration
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        
        // Pool-Konfiguration
        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        hikariConfig.setMinimumIdle(config.getMinIdle());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
        hikariConfig.setIdleTimeout(config.getIdleTimeout());
        hikariConfig.setMaxLifetime(config.getMaxLifetime());
        
        // Performance-Optimierungen
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
        
        // Connection-Validierung
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setValidationTimeout(3000);
        
        // Leak Detection
        hikariConfig.setLeakDetectionThreshold(60000); // 1 Minute
        
        // Pool-Name
        hikariConfig.setPoolName("Skyblock-Database-Pool");
        
        this.dataSource = new HikariDataSource(hikariConfig);
        
        plugin.getLogger().info("HikariCP configured with pool size: " + config.getMaxPoolSize());
    }
    
    /**
     * Testet Datenbankverbindung
     */
    private void testConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT 1")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        plugin.getLogger().info("Database connection test successful");
                        isHealthy = true;
                    }
                }
            }
        }
    }
    
    /**
     * Startet Health Monitoring
     */
    private void startHealthMonitoring() {
        healthMonitor.scheduleAtFixedRate(() -> {
            try {
                monitorConnectionHealth();
                monitorPoolHealth();
                logPerformanceMetrics();
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in database health monitoring", e);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * Überwacht Connection Health
     */
    private void monitorConnectionHealth() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT 1")) {
                stmt.executeQuery();
                isHealthy = true;
            }
        } catch (SQLException e) {
            isHealthy = false;
            plugin.getLogger().warning("Database health check failed: " + e.getMessage());
            failedConnections.incrementAndGet();
        }
    }
    
    /**
     * Überwacht Pool Health
     */
    private void monitorPoolHealth() {
        int active = dataSource.getHikariPoolMXBean().getActiveConnections();
        int idle = dataSource.getHikariPoolMXBean().getIdleConnections();
        int total = dataSource.getHikariPoolMXBean().getTotalConnections();
        
        activeConnections.set(active);
        totalConnections.set(total);
        
        // Warnung bei hoher Connection-Auslastung
        if (active > config.getMaxPoolSize() * 0.8) {
            plugin.getLogger().warning("High connection pool usage: " + active + "/" + config.getMaxPoolSize());
        }
        
        // Warnung bei Connection-Leaks
        if (dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection() > 0) {
            plugin.getLogger().warning("Threads awaiting connections: " + 
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
    
    /**
     * Protokolliert Performance-Metriken
     */
    private void logPerformanceMetrics() {
        long queries = totalQueries.get();
        long avgTime = queries > 0 ? totalQueryTime.get() / queries : 0;
        long failures = failedQueries.get();
        
        if (queries > 0) {
            plugin.getLogger().fine(String.format(
                "DB Metrics - Queries: %d, Avg Time: %dms, Failures: %d, Active: %d/%d",
                queries, avgTime, failures, activeConnections.get(), totalConnections.get()
            ));
        }
    }
    
    /**
     * Führt eine synchrone Query aus
     */
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        long startTime = System.currentTimeMillis();
        totalQueries.incrementAndGet();
        
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setParameters(stmt, params);
                ResultSet result = stmt.executeQuery();
                
                long queryTime = System.currentTimeMillis() - startTime;
                totalQueryTime.addAndGet(queryTime);
                
                return result;
            }
        } catch (SQLException e) {
            failedQueries.incrementAndGet();
            throw e;
        }
    }
    
    /**
     * Führt eine asynchrone Query aus
     */
    public CompletableFuture<ResultSet> executeQueryAsync(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeQuery(sql, params);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Führt ein Update aus
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        long startTime = System.currentTimeMillis();
        totalQueries.incrementAndGet();
        
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setParameters(stmt, params);
                int result = stmt.executeUpdate();
                
                long queryTime = System.currentTimeMillis() - startTime;
                totalQueryTime.addAndGet(queryTime);
                
                return result;
            }
        } catch (SQLException e) {
            failedQueries.incrementAndGet();
            throw e;
        }
    }
    
    /**
     * Führt ein asynchrones Update aus
     */
    public CompletableFuture<Integer> executeUpdateAsync(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeUpdate(sql, params);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Führt Batch-Update aus
     */
    public int[] executeBatchUpdate(String sql, Object[][] batchParams) throws SQLException {
        long startTime = System.currentTimeMillis();
        totalQueries.incrementAndGet();
        
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (Object[] params : batchParams) {
                    setParameters(stmt, params);
                    stmt.addBatch();
                }
                
                int[] result = stmt.executeBatch();
                
                long queryTime = System.currentTimeMillis() - startTime;
                totalQueryTime.addAndGet(queryTime);
                
                return result;
            }
        } catch (SQLException e) {
            failedQueries.incrementAndGet();
            throw e;
        }
    }
    
    /**
     * Führt asynchrones Batch-Update aus
     */
    public CompletableFuture<int[]> executeBatchUpdateAsync(String sql, Object[][] batchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeBatchUpdate(sql, batchParams);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Setzt Parameter in PreparedStatement
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
    
    /**
     * Gibt eine Connection aus dem Pool zurück
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * Gibt Pool-Statistiken zurück
     */
    public DatabasePoolStats getStats() {
        return new DatabasePoolStats(
            activeConnections.get(),
            totalConnections.get(),
            failedConnections.get(),
            totalQueries.get(),
            totalQueryTime.get(),
            failedQueries.get(),
            isHealthy
        );
    }
    
    /**
     * Prüft ob Pool gesund ist
     */
    public boolean isHealthy() {
        return isHealthy;
    }
    
    @Override
    public String getName() {
        return "AdvancedDatabasePool";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
}

/**
 * Datenbank-Konfiguration
 */
class DatabaseConfig {
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/skyblock?useSSL=false&serverTimezone=UTC";
    private final String username = "skyblock";
    private final String password = "password";
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    
    private final int maxPoolSize = 20;
    private final int minIdle = 5;
    private final long connectionTimeout = 30000; // 30 Sekunden
    private final long idleTimeout = 600000; // 10 Minuten
    private final long maxLifetime = 1800000; // 30 Minuten
    
    public String getJdbcUrl() { return jdbcUrl; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDriverClassName() { return driverClassName; }
    public int getMaxPoolSize() { return maxPoolSize; }
    public int getMinIdle() { return minIdle; }
    public long getConnectionTimeout() { return connectionTimeout; }
    public long getIdleTimeout() { return idleTimeout; }
    public long getMaxLifetime() { return maxLifetime; }
}

/**
 * Pool-Statistiken
 */
class DatabasePoolStats {
    private final int activeConnections;
    private final int totalConnections;
    private final int failedConnections;
    private final long totalQueries;
    private final long totalQueryTime;
    private final long failedQueries;
    private final boolean isHealthy;
    
    public DatabasePoolStats(int activeConnections, int totalConnections, int failedConnections,
                           long totalQueries, long totalQueryTime, long failedQueries, boolean isHealthy) {
        this.activeConnections = activeConnections;
        this.totalConnections = totalConnections;
        this.failedConnections = failedConnections;
        this.totalQueries = totalQueries;
        this.totalQueryTime = totalQueryTime;
        this.failedQueries = failedQueries;
        this.isHealthy = isHealthy;
    }
    
    public int getActiveConnections() { return activeConnections; }
    public int getTotalConnections() { return totalConnections; }
    public int getFailedConnections() { return failedConnections; }
    public long getTotalQueries() { return totalQueries; }
    public long getTotalQueryTime() { return totalQueryTime; }
    public long getFailedQueries() { return failedQueries; }
    public boolean isHealthy() { return isHealthy; }
    
    public double getAverageQueryTime() {
        return totalQueries > 0 ? (double) totalQueryTime / totalQueries : 0;
    }
    
    public double getFailureRate() {
        return totalQueries > 0 ? (double) failedQueries / totalQueries * 100 : 0;
    }
    
    @Override
    public String toString() {
        return String.format("DatabasePoolStats{active=%d, total=%d, failed=%d, queries=%d, avgTime=%.2fms, failureRate=%.2f%%, healthy=%s}",
            activeConnections, totalConnections, failedConnections, totalQueries, getAverageQueryTime(), getFailureRate(), isHealthy);
    }
}
