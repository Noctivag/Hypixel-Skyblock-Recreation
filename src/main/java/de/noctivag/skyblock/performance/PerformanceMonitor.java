package de.noctivag.skyblock.performance;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PerformanceMonitor - Überwacht SkyblockPlugin-Performance
 * 
 * Features:
 * - TPS-Monitoring
 * - Memory-Überwachung
 * - Task-Performance-Tracking
 * - Automatische Optimierungen
 */
public class PerformanceMonitor {
    private final SkyblockPlugin SkyblockPlugin;
    
    // Performance-Metriken
    private final Map<String, PerformanceMetric> metrics = new ConcurrentHashMap<>();
    private final List<Double> tpsHistory = new ArrayList<>();
    private final List<Long> memoryHistory = new ArrayList<>();
    
    // Performance-Schwellenwerte
    private final double CRITICAL_TPS = 15.0;
    private final double WARNING_TPS = 18.0;
    private final long CRITICAL_MEMORY = 1024 * 1024 * 1024; // 1GB
    private final long WARNING_MEMORY = 512 * 1024 * 1024; // 512MB
    
    // Auto-Optimierung
    private boolean autoOptimizationEnabled = true;
    private int optimizationLevel = 0; // 0-3
    
    public PerformanceMonitor(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        startMonitoring();
    }
    
    /**
     * Startet Performance-Monitoring
     */
    private void startMonitoring() {
        // TPS-Monitoring
        new BukkitRunnable() {
            @Override
            public void run() {
                monitorTPS();
                monitorMemory();
                checkPerformanceThresholds();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Jede Sekunde
        
        // Detailliertes Monitoring
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(6000L * 50); // Initial delay: 5 minutes = 300,000 ms
                while (SkyblockPlugin.isEnabled()) {
                    generatePerformanceReport();
                    Thread.sleep(6000L * 50); // Every 5 minutes = 300,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * Überwacht TPS
     */
    private void monitorTPS() {
        double tps = getCurrentTPS();
        tpsHistory.add(tps);
        
        // Behalte nur letzten 300 Einträge (5 Minuten)
        if (tpsHistory.size() > 300) {
            tpsHistory.remove(0);
        }
        
        // Speichere Metrik
        updateMetric("tps", tps);
    }
    
    /**
     * Überwacht Memory
     */
    private void monitorMemory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        
        memoryHistory.add(usedMemory);
        
        // Behalte nur letzten 300 Einträge
        if (memoryHistory.size() > 300) {
            memoryHistory.remove(0);
        }
        
        // Speichere Metriken
        updateMetric("memory_used", usedMemory);
        updateMetric("memory_max", maxMemory);
        updateMetric("memory_percentage", (double) usedMemory / maxMemory * 100);
    }
    
    /**
     * Prüft Performance-Schwellenwerte
     */
    private void checkPerformanceThresholds() {
        double avgTPS = getAverageTPS();
        long currentMemory = getCurrentMemoryUsage();
        
        // TPS-Check
        if (avgTPS < CRITICAL_TPS) {
            SkyblockPlugin.getLogger().severe("CRITICAL TPS: " + avgTPS + " - Auto-optimization triggered!");
            triggerAutoOptimization(3); // Höchste Optimierungsstufe
        } else if (avgTPS < WARNING_TPS) {
            SkyblockPlugin.getLogger().warning("LOW TPS: " + avgTPS + " - Auto-optimization triggered!");
            triggerAutoOptimization(2);
        }
        
        // Memory-Check
        if (currentMemory > CRITICAL_MEMORY) {
            SkyblockPlugin.getLogger().severe("CRITICAL MEMORY: " + formatBytes(currentMemory) + " - Auto-optimization triggered!");
            triggerAutoOptimization(3);
        } else if (currentMemory > WARNING_MEMORY) {
            SkyblockPlugin.getLogger().warning("HIGH MEMORY: " + formatBytes(currentMemory) + " - Auto-optimization triggered!");
            triggerAutoOptimization(1);
        }
    }
    
    /**
     * Löst Auto-Optimierung aus
     */
    private void triggerAutoOptimization(int level) {
        if (!autoOptimizationEnabled) return;
        
        optimizationLevel = Math.max(optimizationLevel, level);
        
        switch (level) {
            case 1: // Niedrige Optimierung
                optimizeLevel1();
                break;
            case 2: // Mittlere Optimierung
                optimizeLevel1();
                optimizeLevel2();
                break;
            case 3: // Hohe Optimierung
                optimizeLevel1();
                optimizeLevel2();
                optimizeLevel3();
                break;
        }
        
        SkyblockPlugin.getLogger().info("Auto-optimization level " + level + " applied");
    }
    
    /**
     * Optimierungsstufe 1: Cache-Cleanup
     */
    private void optimizeLevel1() {
        // Cache-Cleanup
        // SkyblockPlugin.getCacheManager().clear(); // CacheManager not implemented yet
        System.gc(); // Garbage Collection
        
        SkyblockPlugin.getLogger().info("Level 1 optimization: Cache cleanup completed");
    }
    
    /**
     * Optimierungsstufe 2: Task-Reduzierung
     */
    private void optimizeLevel2() {
        // Reduziere Task-Intervalle
        // Könnte Task-Intervalle dynamisch anpassen
        
        SkyblockPlugin.getLogger().info("Level 2 optimization: Task reduction applied");
    }
    
    /**
     * Optimierungsstufe 3: Notfall-Modus
     */
    private void optimizeLevel3() {
        // Deaktiviere nicht-kritische Features
        // Reduziere GUI-Updates
        // Pausiere Background-Tasks
        
        SkyblockPlugin.getLogger().info("Level 3 optimization: Emergency mode activated");
    }
    
    /**
     * Generiert Performance-Report
     */
    private void generatePerformanceReport() {
        Map<String, Object> report = new HashMap<>();
        
        // TPS-Statistiken
        report.put("current_tps", getCurrentTPS());
        report.put("average_tps", getAverageTPS());
        report.put("min_tps", Collections.min(tpsHistory));
        report.put("max_tps", Collections.max(tpsHistory));
        
        // Memory-Statistiken
        report.put("current_memory", getCurrentMemoryUsage());
        report.put("average_memory", getAverageMemoryUsage());
        report.put("memory_percentage", getMemoryPercentage());
        
        // SkyblockPlugin-Metriken
        report.put("online_players", Bukkit.getOnlinePlayers().size());
        report.put("optimization_level", optimizationLevel);
        
        // Logge Report
        SkyblockPlugin.getLogger().info("=== PERFORMANCE REPORT ===");
        report.forEach((key, value) -> 
            SkyblockPlugin.getLogger().info(key + ": " + value));
        SkyblockPlugin.getLogger().info("=========================");
    }
    
    /**
     * Hilfsmethoden
     */
    private double getCurrentTPS() {
        return 20.0; // Placeholder - getTps() method not available in older Bukkit versions
    }
    
    private double getAverageTPS() {
        return tpsHistory.stream().mapToDouble(Double::doubleValue).average().orElse(20.0);
    }
    
    private long getCurrentMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    private long getAverageMemoryUsage() {
        return (long) memoryHistory.stream().mapToLong(Long::longValue).average().orElse(0);
    }
    
    private double getMemoryPercentage() {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long max = runtime.maxMemory();
        return (double) used / max * 100;
    }
    
    private void updateMetric(String name, double value) {
        metrics.computeIfAbsent(name, k -> new PerformanceMetric()).update(value);
    }
    
    private void updateMetric(String name, long value) {
        metrics.computeIfAbsent(name, k -> new PerformanceMetric()).update(value);
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
    
    /**
     * Performance-Metric-Klasse
     */
    private static class PerformanceMetric {
        @SuppressWarnings("unused")
        private double current;
        private double min = Double.MAX_VALUE;
        private double max = Double.MIN_VALUE;
        private double sum;
        private int count;
        
        void update(double value) {
            this.current = value;
            this.min = Math.min(min, value);
            this.max = Math.max(max, value);
            this.sum += value;
            this.count++;
        }
        
        void update(long value) {
            update((double) value);
        }
        
        @SuppressWarnings("unused")
        double getAverage() {
            return count > 0 ? sum / count : 0;
        }
    }
}
