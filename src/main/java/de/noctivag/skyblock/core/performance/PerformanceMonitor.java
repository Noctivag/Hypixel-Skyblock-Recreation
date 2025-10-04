package de.noctivag.skyblock.core.performance;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Performance monitoring system for tracking plugin performance metrics.
 */
public class PerformanceMonitor {
    
    private final JavaSkyblockPlugin plugin;
    private final Logger logger;
    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, PerformanceMetrics> metrics;
    
    private boolean monitoring = false;
    
    public PerformanceMonitor(JavaSkyblockPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread thread = new Thread(r, "PerformanceMonitor-Thread");
            thread.setDaemon(true);
            return thread;
        });
        this.metrics = new ConcurrentHashMap<>();
    }
    
    /**
     * Start performance monitoring
     */
    public void startMonitoring() {
        if (monitoring) {
            return;
        }
        
        monitoring = true;
        
        // Schedule periodic monitoring tasks
        scheduler.scheduleAtFixedRate(this::collectSystemMetrics, 0, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::reportMetrics, 30, 30, TimeUnit.SECONDS);
        
        logger.info("Performance monitoring started");
    }
    
    /**
     * Stop performance monitoring
     */
    public void stopMonitoring() {
        if (!monitoring) {
            return;
        }
        
        monitoring = false;
        scheduler.shutdown();
        
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logger.info("Performance monitoring stopped");
    }
    
    /**
     * Record a performance metric
     * @param name the metric name
     * @param value the metric value
     */
    public void recordMetric(String name, double value) {
        metrics.computeIfAbsent(name, k -> new PerformanceMetrics()).addValue(value);
    }
    
    /**
     * Record a timing metric
     * @param name the metric name
     * @param startTime the start time in milliseconds
     */
    public void recordTiming(String name, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        recordMetric(name, duration);
    }
    
    /**
     * Get performance metrics for a specific name
     * @param name the metric name
     * @return performance metrics or null if not found
     */
    public PerformanceMetrics getMetrics(String name) {
        return metrics.get(name);
    }
    
    /**
     * Get all performance metrics
     * @return map of all metrics
     */
    public ConcurrentHashMap<String, PerformanceMetrics> getAllMetrics() {
        return new ConcurrentHashMap<>(metrics);
    }
    
    /**
     * Collect system-wide performance metrics
     */
    private void collectSystemMetrics() {
        // Memory metrics
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        recordMetric("memory.used", usedMemory);
        recordMetric("memory.total", totalMemory);
        recordMetric("memory.free", freeMemory);
        recordMetric("memory.usage_percent", (double) usedMemory / totalMemory * 100);
        
        // TPS metrics (simplified)
        recordMetric("tps", 20.0); // This would be calculated from actual tick times
        
        // Plugin metrics
        recordMetric("plugin.players", plugin.getServer().getOnlinePlayers().size());
        recordMetric("plugin.worlds", plugin.getServer().getWorlds().size());
    }
    
    /**
     * Report performance metrics
     */
    private void reportMetrics() {
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info("=== Performance Report ===");
            
            // Memory report
            PerformanceMetrics memoryUsage = metrics.get("memory.usage_percent");
            if (memoryUsage != null) {
                logger.info("Memory Usage: " + String.format("%.2f%%", memoryUsage.getAverage()));
            }
            
            // TPS report
            PerformanceMetrics tps = metrics.get("tps");
            if (tps != null) {
                logger.info("Average TPS: " + String.format("%.2f", tps.getAverage()));
            }
            
            // Player count report
            PerformanceMetrics players = metrics.get("plugin.players");
            if (players != null) {
                logger.info("Average Players: " + String.format("%.2f", players.getAverage()));
            }
            
            logger.info("========================");
        }
    }
    
    /**
     * Performance metrics container
     */
    public static class PerformanceMetrics {
        private final java.util.List<Double> values = new java.util.concurrent.CopyOnWriteArrayList<>();
        private double sum = 0.0;
        private double min = Double.MAX_VALUE;
        private double max = Double.MIN_VALUE;
        
        public synchronized void addValue(double value) {
            values.add(value);
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
            
            // Keep only last 100 values to prevent memory leaks
            if (values.size() > 100) {
                double removed = values.remove(0);
                sum -= removed;
            }
        }
        
        public double getAverage() {
            return values.isEmpty() ? 0.0 : sum / values.size();
        }
        
        public double getMin() {
            return min == Double.MAX_VALUE ? 0.0 : min;
        }
        
        public double getMax() {
            return max == Double.MIN_VALUE ? 0.0 : max;
        }
        
        public int getCount() {
            return values.size();
        }
        
        public double getSum() {
            return sum;
        }
    }
}
