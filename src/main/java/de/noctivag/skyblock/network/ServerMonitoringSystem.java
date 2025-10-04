package de.noctivag.skyblock.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Server Monitoring System - Überwachung und Analyse von Server-Performance
 * 
 * Verantwortlich für:
 * - Echtzeit-Performance-Monitoring
 * - Alert-System
 * - Performance-Analytics
 * - Resource-Tracking
 * - Health-Checks
 * - Reporting
 */
public class ServerMonitoringSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final ServerCommunicationManager communicationManager;
    
    private final Map<String, ServerMetrics> serverMetrics = new ConcurrentHashMap<>();
    private final Map<String, Alert> activeAlerts = new ConcurrentHashMap<>();
    private final Map<String, PerformanceReport> performanceReports = new ConcurrentHashMap<>();
    
    private BukkitTask monitoringTask;
    private BukkitTask alertCheckTask;
    private BukkitTask reportGenerationTask;
    
    // Monitoring-Konfiguration
    private final long monitoringInterval = 1000; // 1 Sekunde
    private final long alertCheckInterval = 5000; // 5 Sekunden
    private final long reportInterval = 300000; // 5 Minuten
    
    // Alert-Thresholds
    private final double tpsThreshold = 15.0;
    private final double memoryThreshold = 85.0;
    private final double cpuThreshold = 80.0;
    private final int playerThreshold = 180;
    
    public ServerMonitoringSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                                 ServerCommunicationManager communicationManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.communicationManager = communicationManager;
    }
    
    /**
     * Startet das Monitoring-System
     */
    public void startMonitoring() {
        // Starte Performance-Monitoring
        startPerformanceMonitoring();
        
        // Starte Alert-Checking
        startAlertChecking();
        
        // Starte Report-Generation
        startReportGeneration();
        
        plugin.getLogger().info("Server Monitoring System started");
    }
    
    /**
     * Startet Performance-Monitoring
     */
    private void startPerformanceMonitoring() {
        monitoringTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                // Sammle Metriken für alle Server
                collectServerMetrics();
                
                // Aktualisiere Performance-Reports
                updatePerformanceReports();
                
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in performance monitoring", e);
            }
        }, 0L, monitoringInterval / 50L);
    }
    
    /**
     * Startet Alert-Checking
     */
    private void startAlertChecking() {
        alertCheckTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                // Prüfe Alerts
                checkAlerts();
                
                // Verarbeite aktive Alerts
                processActiveAlerts();
                
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in alert checking", e);
            }
        }, 0L, alertCheckInterval / 50L);
    }
    
    /**
     * Startet Report-Generation
     */
    private void startReportGeneration() {
        reportGenerationTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                // Generiere Performance-Reports
                generatePerformanceReports();
                
                // Speichere Reports in Datenbank
                saveReportsToDatabase();
                
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in report generation", e);
            }
        }, 0L, reportInterval / 50L);
    }
    
    /**
     * Sammelt Server-Metriken
     */
    private void collectServerMetrics() {
        for (ServerCommunicationManager.ServerInfo serverInfo : communicationManager.getConnectedServers().values()) {
            String serverId = serverInfo.getServerId();
            
            ServerMetrics metrics = serverMetrics.computeIfAbsent(serverId, k -> new ServerMetrics(serverId));
            
            // Aktualisiere Metriken
            metrics.updateMetrics(
                serverInfo.getPlayerCount(),
                serverInfo.getTps(),
                serverInfo.getMemoryUsage(),
                serverInfo.getCpuUsage(),
                serverInfo.getLoad(),
                serverInfo.getHealth(),
                System.currentTimeMillis()
            );
            
            // Speichere Metriken in Datenbank
            saveMetricsToDatabase(metrics);
        }
    }
    
    /**
     * Aktualisiert Performance-Reports
     */
    private void updatePerformanceReports() {
        for (Map.Entry<String, ServerMetrics> entry : serverMetrics.entrySet()) {
            String serverId = entry.getKey();
            ServerMetrics metrics = entry.getValue();
            
            PerformanceReport report = performanceReports.computeIfAbsent(serverId, k -> new PerformanceReport(serverId));
            report.updateReport(metrics);
        }
    }
    
    /**
     * Prüft Alerts
     */
    private void checkAlerts() {
        for (Map.Entry<String, ServerMetrics> entry : serverMetrics.entrySet()) {
            String serverId = entry.getKey();
            ServerMetrics metrics = entry.getValue();
            
            // Prüfe TPS-Alert
            if (metrics.getCurrentTps() < tpsThreshold) {
                createAlert(serverId, AlertType.LOW_TPS, "TPS is below threshold: " + metrics.getCurrentTps());
            }
            
            // Prüfe Memory-Alert
            if (metrics.getCurrentMemoryUsage() > memoryThreshold) {
                createAlert(serverId, AlertType.HIGH_MEMORY, "Memory usage is above threshold: " + metrics.getCurrentMemoryUsage() + "%");
            }
            
            // Prüfe CPU-Alert
            if (metrics.getCurrentCpuUsage() > cpuThreshold) {
                createAlert(serverId, AlertType.HIGH_CPU, "CPU usage is above threshold: " + metrics.getCurrentCpuUsage() + "%");
            }
            
            // Prüfe Player-Alert
            if (metrics.getCurrentPlayerCount() > playerThreshold) {
                createAlert(serverId, AlertType.HIGH_PLAYER_COUNT, "Player count is above threshold: " + metrics.getCurrentPlayerCount());
            }
            
            // Prüfe Server-Offline-Alert
            if (!metrics.isServerOnline()) {
                createAlert(serverId, AlertType.SERVER_OFFLINE, "Server is offline");
            }
        }
    }
    
    /**
     * Erstellt einen Alert
     */
    private void createAlert(String serverId, AlertType type, String message) {
        String alertKey = serverId + ":" + type.name();
        
        if (!activeAlerts.containsKey(alertKey)) {
            Alert alert = new Alert(serverId, type, message, System.currentTimeMillis());
            activeAlerts.put(alertKey, alert);
            
            // Sende Alert
            sendAlert(alert);
            
            plugin.getLogger().warning("ALERT: " + serverId + " - " + type.name() + ": " + message);
        }
    }
    
    /**
     * Sendet einen Alert
     */
    private void sendAlert(Alert alert) {
        // Sende Alert an alle Administratoren
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("basics.monitoring.alerts")) {
                player.sendMessage(net.kyori.adventure.text.Component.text(
                    "§c§l[ALERT] §e" + alert.getServerId() + " §7- §c" + alert.getType().name() + 
                    "§7: §f" + alert.getMessage()
                ));
            }
        }
        
        // Speichere Alert in Datenbank
        saveAlertToDatabase(alert);
    }
    
    /**
     * Verarbeitet aktive Alerts
     */
    private void processActiveAlerts() {
        Iterator<Map.Entry<String, Alert>> iterator = activeAlerts.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, Alert> entry = iterator.next();
            Alert alert = entry.getValue();
            
            // Prüfe ob Alert noch relevant ist
            if (isAlertResolved(alert)) {
                // Alert ist aufgelöst
                resolveAlert(alert);
                iterator.remove();
            } else if (System.currentTimeMillis() - alert.getTimestamp() > 300000) { // 5 Minuten
                // Alert ist zu alt
                iterator.remove();
            }
        }
    }
    
    /**
     * Prüft ob Alert aufgelöst ist
     */
    private boolean isAlertResolved(Alert alert) {
        ServerMetrics metrics = serverMetrics.get(alert.getServerId());
        if (metrics == null) return false;
        
        switch (alert.getType()) {
            case LOW_TPS:
                return metrics.getCurrentTps() >= tpsThreshold;
            case HIGH_MEMORY:
                return metrics.getCurrentMemoryUsage() <= memoryThreshold;
            case HIGH_CPU:
                return metrics.getCurrentCpuUsage() <= cpuThreshold;
            case HIGH_PLAYER_COUNT:
                return metrics.getCurrentPlayerCount() <= playerThreshold;
            case SERVER_OFFLINE:
                return metrics.isServerOnline();
            default:
                return false;
        }
    }
    
    /**
     * Löst einen Alert auf
     */
    private void resolveAlert(Alert alert) {
        plugin.getLogger().info("Alert resolved: " + alert.getServerId() + " - " + alert.getType().name());
        
        // Sende Resolution-Nachricht
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("basics.monitoring.alerts")) {
                player.sendMessage(net.kyori.adventure.text.Component.text(
                    "§a§l[RESOLVED] §e" + alert.getServerId() + " §7- §a" + alert.getType().name() + " §7resolved"
                ));
            }
        }
    }
    
    /**
     * Generiert Performance-Reports
     */
    private void generatePerformanceReports() {
        for (Map.Entry<String, PerformanceReport> entry : performanceReports.entrySet()) {
            String serverId = entry.getKey();
            PerformanceReport report = entry.getValue();
            
            // Generiere Report
            report.generateReport();
        }
    }
    
    /**
     * Speichert Metriken in Datenbank
     */
    private void saveMetricsToDatabase(ServerMetrics metrics) {
        databaseManager.executeUpdate("""
            INSERT INTO server_metrics (server_id, timestamp, player_count, tps, memory_usage, cpu_usage, load, health)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """, metrics.getServerId(), metrics.getTimestamp(), metrics.getCurrentPlayerCount(),
            metrics.getCurrentTps(), metrics.getCurrentMemoryUsage(), metrics.getCurrentCpuUsage(),
            metrics.getCurrentLoad(), metrics.getCurrentHealth());
    }
    
    /**
     * Speichert Alert in Datenbank
     */
    private void saveAlertToDatabase(Alert alert) {
        databaseManager.executeUpdate("""
            INSERT INTO server_alerts (server_id, alert_type, message, timestamp, resolved)
            VALUES (?, ?, ?, ?, ?)
        """, alert.getServerId(), alert.getType().name(), alert.getMessage(),
            alert.getTimestamp(), false);
    }
    
    /**
     * Speichert Reports in Datenbank
     */
    private void saveReportsToDatabase() {
        for (PerformanceReport report : performanceReports.values()) {
            databaseManager.executeUpdate("""
                INSERT INTO performance_reports (server_id, timestamp, avg_tps, avg_memory, avg_cpu, avg_load, peak_players)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """, report.getServerId(), report.getTimestamp(), report.getAverageTps(),
                report.getAverageMemoryUsage(), report.getAverageCpuUsage(), report.getAverageLoad(),
                report.getPeakPlayerCount());
        }
    }
    
    /**
     * Gibt Server-Metriken zurück
     */
    public Map<String, ServerMetrics> getServerMetrics() {
        return new HashMap<>(serverMetrics);
    }
    
    /**
     * Gibt aktive Alerts zurück
     */
    public Map<String, Alert> getActiveAlerts() {
        return new HashMap<>(activeAlerts);
    }
    
    /**
     * Gibt Performance-Reports zurück
     */
    public Map<String, PerformanceReport> getPerformanceReports() {
        return new HashMap<>(performanceReports);
    }
    
    /**
     * Stoppt das Monitoring-System
     */
    public void stopMonitoring() {
        if (monitoringTask != null) {
            monitoringTask.cancel();
        }
        if (alertCheckTask != null) {
            alertCheckTask.cancel();
        }
        if (reportGenerationTask != null) {
            reportGenerationTask.cancel();
        }
        
        plugin.getLogger().info("Server Monitoring System stopped");
    }
    
    /**
     * Server Metrics Klasse
     */
    public static class ServerMetrics {
        private final String serverId;
        private final List<MetricPoint> metrics = new ArrayList<>();
        private final int maxMetrics = 1000; // Behalte nur die letzten 1000 Metriken
        
        private int currentPlayerCount;
        private double currentTps;
        private double currentMemoryUsage;
        private double currentCpuUsage;
        private double currentLoad;
        private double currentHealth;
        private long timestamp;
        private boolean serverOnline;
        
        public ServerMetrics(String serverId) {
            this.serverId = serverId;
        }
        
        public void updateMetrics(int playerCount, double tps, double memoryUsage, double cpuUsage,
                                 double load, double health, long timestamp) {
            this.currentPlayerCount = playerCount;
            this.currentTps = tps;
            this.currentMemoryUsage = memoryUsage;
            this.currentCpuUsage = cpuUsage;
            this.currentLoad = load;
            this.currentHealth = health;
            this.timestamp = timestamp;
            this.serverOnline = true;
            
            // Füge Metrik-Punkt hinzu
            metrics.add(new MetricPoint(playerCount, tps, memoryUsage, cpuUsage, load, health, timestamp));
            
            // Behalte nur die neuesten Metriken
            if (metrics.size() > maxMetrics) {
                metrics.remove(0);
            }
        }
        
        public void setServerOffline() {
            this.serverOnline = false;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public String getServerId() { return serverId; }
        public int getCurrentPlayerCount() { return currentPlayerCount; }
        public double getCurrentTps() { return currentTps; }
        public double getCurrentMemoryUsage() { return currentMemoryUsage; }
        public double getCurrentCpuUsage() { return currentCpuUsage; }
        public double getCurrentLoad() { return currentLoad; }
        public double getCurrentHealth() { return currentHealth; }
        public long getTimestamp() { return timestamp; }
        public boolean isServerOnline() { return serverOnline; }
        public List<MetricPoint> getMetrics() { return new ArrayList<>(metrics); }
    }
    
    /**
     * Metric Point Klasse
     */
    public static class MetricPoint {
        private final int playerCount;
        private final double tps;
        private final double memoryUsage;
        private final double cpuUsage;
        private final double load;
        private final double health;
        private final long timestamp;
        
        public MetricPoint(int playerCount, double tps, double memoryUsage, double cpuUsage,
                          double load, double health, long timestamp) {
            this.playerCount = playerCount;
            this.tps = tps;
            this.memoryUsage = memoryUsage;
            this.cpuUsage = cpuUsage;
            this.load = load;
            this.health = health;
            this.timestamp = timestamp;
        }
        
        // Getters
        public int getPlayerCount() { return playerCount; }
        public double getTps() { return tps; }
        public double getMemoryUsage() { return memoryUsage; }
        public double getCpuUsage() { return cpuUsage; }
        public double getLoad() { return load; }
        public double getHealth() { return health; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Alert Klasse
     */
    public static class Alert {
        private final String serverId;
        private final AlertType type;
        private final String message;
        private final long timestamp;
        
        public Alert(String serverId, AlertType type, String message, long timestamp) {
            this.serverId = serverId;
            this.type = type;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        // Getters
        public String getServerId() { return serverId; }
        public AlertType getType() { return type; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Alert Type Enum
     */
    public enum AlertType {
        LOW_TPS,
        HIGH_MEMORY,
        HIGH_CPU,
        HIGH_PLAYER_COUNT,
        SERVER_OFFLINE,
        HIGH_LOAD,
        CONNECTION_ERROR
    }
    
    /**
     * Performance Report Klasse
     */
    public static class PerformanceReport {
        private final String serverId;
        private final long timestamp;
        
        private double averageTps;
        private double averageMemoryUsage;
        private double averageCpuUsage;
        private double averageLoad;
        private int peakPlayerCount;
        private int totalAlerts;
        
        public PerformanceReport(String serverId) {
            this.serverId = serverId;
            this.timestamp = System.currentTimeMillis();
        }
        
        public void updateReport(ServerMetrics metrics) {
            List<MetricPoint> metricPoints = metrics.getMetrics();
            if (metricPoints.isEmpty()) return;
            
            // Berechne Durchschnittswerte
            this.averageTps = metricPoints.stream().mapToDouble(MetricPoint::getTps).average().orElse(0.0);
            this.averageMemoryUsage = metricPoints.stream().mapToDouble(MetricPoint::getMemoryUsage).average().orElse(0.0);
            this.averageCpuUsage = metricPoints.stream().mapToDouble(MetricPoint::getCpuUsage).average().orElse(0.0);
            this.averageLoad = metricPoints.stream().mapToDouble(MetricPoint::getLoad).average().orElse(0.0);
            this.peakPlayerCount = metricPoints.stream().mapToInt(MetricPoint::getPlayerCount).max().orElse(0);
        }
        
        public void generateReport() {
            // Generiere detaillierten Report
            // Hier könnte man weitere Analysen durchführen
        }
        
        // Getters
        public String getServerId() { return serverId; }
        public long getTimestamp() { return timestamp; }
        public double getAverageTps() { return averageTps; }
        public double getAverageMemoryUsage() { return averageMemoryUsage; }
        public double getAverageCpuUsage() { return averageCpuUsage; }
        public double getAverageLoad() { return averageLoad; }
        public int getPeakPlayerCount() { return peakPlayerCount; }
        public int getTotalAlerts() { return totalAlerts; }
    }
}
