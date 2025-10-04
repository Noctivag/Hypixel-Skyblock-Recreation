package de.noctivag.skyblock.compatibility;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PerformanceOptimizer - Optimiert Plugin-Performance für bessere Kompatibilität
 */
public class PerformanceOptimizer {
    
    private final SkyblockPlugin plugin;
    private final ConcurrentHashMap<String, AtomicLong> performanceMetrics;
    private final AtomicInteger activeTasks;
    private final AtomicLong memoryUsage;
    private final AtomicLong lastGarbageCollection;
    
    public PerformanceOptimizer(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.performanceMetrics = new ConcurrentHashMap<>();
        this.activeTasks = new AtomicInteger(0);
        this.memoryUsage = new AtomicLong(0);
        this.lastGarbageCollection = new AtomicLong(System.currentTimeMillis());
        
        initializePerformanceMonitoring();
    }
    
    private void initializePerformanceMonitoring() {
        // Performance-Monitoring alle 30 Sekunden
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePerformanceMetrics();
                optimizeMemoryUsage();
                checkTaskOverload();
            }
        }.runTaskTimer(plugin, 0L, 600L); // 30 Sekunden = 600 Ticks
    }
    
    private void updatePerformanceMetrics() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        memoryUsage.set(usedMemory);
        
        // Speicher-Metriken aktualisieren
        performanceMetrics.put("memory_used", new AtomicLong(usedMemory));
        performanceMetrics.put("memory_total", new AtomicLong(totalMemory));
        performanceMetrics.put("memory_free", new AtomicLong(freeMemory));
        
        // Task-Metriken aktualisieren
        performanceMetrics.put("active_tasks", new AtomicLong(activeTasks.get()));
        performanceMetrics.put("bukkit_tasks", new AtomicLong(Bukkit.getScheduler().getPendingTasks().size()));
    }
    
    private void optimizeMemoryUsage() {
        long currentMemory = memoryUsage.get();
        long lastGC = lastGarbageCollection.get();
        long timeSinceLastGC = System.currentTimeMillis() - lastGC;
        
        // Garbage Collection nach 5 Minuten oder bei hohem Speicherverbrauch
        if (timeSinceLastGC > 300000 || currentMemory > 1000000000) { // 1GB
            System.gc();
            lastGarbageCollection.set(System.currentTimeMillis());
            
            plugin.getLogger().info("Performance: Garbage Collection ausgeführt");
        }
    }
    
    private void checkTaskOverload() {
        int tasks = activeTasks.get();
        int bukkitTasks = Bukkit.getScheduler().getPendingTasks().size();
        
        if (tasks > 100 || bukkitTasks > 200) {
            plugin.getLogger().warning("Performance: Hohe Task-Last erkannt! Tasks: " + tasks + ", Bukkit Tasks: " + bukkitTasks);
            
            // Task-Limits setzen
            if (tasks > 150) {
                plugin.getLogger().warning("Performance: Task-Limit erreicht, neue Tasks werden verzögert");
            }
        }
    }
    
    public void startTask(String taskName) {
        activeTasks.incrementAndGet();
        performanceMetrics.put("task_" + taskName + "_start", new AtomicLong(System.currentTimeMillis()));
    }
    
    public void endTask(String taskName) {
        activeTasks.decrementAndGet();
        long startTime = performanceMetrics.getOrDefault("task_" + taskName + "_start", new AtomicLong(0)).get();
        long duration = System.currentTimeMillis() - startTime;
        
        performanceMetrics.put("task_" + taskName + "_duration", new AtomicLong(duration));
        
        // Warnung bei langen Tasks
        if (duration > 1000) { // 1 Sekunde
            plugin.getLogger().warning("Performance: Langsamer Task erkannt: " + taskName + " (" + duration + "ms)");
        }
    }
    
    public long getMemoryUsage() {
        return memoryUsage.get();
    }
    
    public int getActiveTasks() {
        return activeTasks.get();
    }
    
    public ConcurrentHashMap<String, AtomicLong> getPerformanceMetrics() {
        return new ConcurrentHashMap<>(performanceMetrics);
    }
    
    public String getPerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("§6§l=== PERFORMANCE REPORT ===\n");
        
        long memoryUsed = memoryUsage.get();
        long memoryTotal = performanceMetrics.getOrDefault("memory_total", new AtomicLong(0)).get();
        long memoryFree = performanceMetrics.getOrDefault("memory_free", new AtomicLong(0)).get();
        
        report.append("§7Memory Usage: §e").append(formatBytes(memoryUsed)).append(" / ").append(formatBytes(memoryTotal)).append("\n");
        report.append("§7Free Memory: §a").append(formatBytes(memoryFree)).append("\n");
        report.append("§7Active Tasks: §e").append(activeTasks.get()).append("\n");
        report.append("§7Bukkit Tasks: §e").append(Bukkit.getScheduler().getPendingTasks().size()).append("\n");
        
        // Performance-Bewertung
        double memoryUsagePercent = (double) memoryUsed / memoryTotal * 100;
        if (memoryUsagePercent < 50) {
            report.append("§7Performance: §aExcellent\n");
        } else if (memoryUsagePercent < 75) {
            report.append("§7Performance: §eGood\n");
        } else if (memoryUsagePercent < 90) {
            report.append("§7Performance: §6Fair\n");
        } else {
            report.append("§7Performance: §cPoor\n");
        }
        
        report.append("§6==============================");
        return report.toString();
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
