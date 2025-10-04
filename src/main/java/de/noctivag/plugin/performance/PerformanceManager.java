package de.noctivag.plugin.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PerformanceManager - Zentralisiertes Task-Management
 * 
 * Optimiert die Performance durch:
 * - Task-Konsolidierung
 * - Intelligente Update-Intervalle
 * - Batch-Processing
 * - Resource-Management
 */
public class PerformanceManager {
    private final Plugin plugin;
    
    // Task-Konsolidierung
    private final Map<String, Runnable> updateTasks = new ConcurrentHashMap<>();
    private final Map<String, Long> lastUpdate = new ConcurrentHashMap<>();
    private final Map<String, Integer> updateIntervals = new ConcurrentHashMap<>();
    
    // Performance-Metriken
    private long totalUpdateTime = 0;
    private int updateCount = 0;
    private final long MAX_UPDATE_TIME = 50; // 50ms pro Tick
    
    public PerformanceManager(Plugin plugin) {
        this.plugin = plugin;
        startConsolidatedUpdateTask();
    }
    
    /**
     * Registriert ein Update-Task mit intelligentem Intervall
     */
    public void registerUpdateTask(String taskName, Runnable task, int intervalTicks) {
        updateTasks.put(taskName, task);
        updateIntervals.put(taskName, intervalTicks);
        lastUpdate.put(taskName, 0L);
    }
    
    /**
     * Startet konsolidierten Update-Task (alle 20 Ticks)
     */
    private void startConsolidatedUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                
                // Batch-Update aller Tasks
                for (Map.Entry<String, Runnable> entry : updateTasks.entrySet()) {
                    String taskName = entry.getKey();
                    Runnable task = entry.getValue();
                    
                    // Prüfe ob Task ausgeführt werden soll
                    if (shouldExecuteTask(taskName)) {
                        try {
                            task.run();
                            lastUpdate.put(taskName, System.currentTimeMillis());
                        } catch (Exception e) {
                            plugin.getLogger().warning("Error in update task " + taskName + ": " + e.getMessage());
                        }
                    }
                    
                    // Performance-Limit: Max 50ms pro Tick
                    if (System.currentTimeMillis() - startTime > MAX_UPDATE_TIME) {
                        plugin.getLogger().warning("Update cycle took too long, skipping remaining tasks");
                        break;
                    }
                }
                
                // Performance-Metriken
                long updateTime = System.currentTimeMillis() - startTime;
                totalUpdateTime += updateTime;
                updateCount++;
                
                if (updateCount % 100 == 0) { // Alle 5 Sekunden
                    double avgUpdateTime = (double) totalUpdateTime / updateCount;
                    if (avgUpdateTime > 30) { // > 30ms Durchschnitt
                        plugin.getLogger().warning("High update time detected: " + avgUpdateTime + "ms average");
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Jeden Tick prüfen, aber Tasks haben eigene Intervalle
    }
    
    /**
     * Prüft ob ein Task ausgeführt werden soll basierend auf seinem Intervall
     */
    private boolean shouldExecuteTask(String taskName) {
        int interval = updateIntervals.get(taskName);
        long last = lastUpdate.get(taskName);
        long current = System.currentTimeMillis();
        
        // Konvertiere Ticks zu Millisekunden (20 Ticks = 1 Sekunde)
        long intervalMs = interval * 50L;
        
        return (current - last) >= intervalMs;
    }
    
    /**
     * Entfernt einen Update-Task
     */
    public void unregisterUpdateTask(String taskName) {
        updateTasks.remove(taskName);
        updateIntervals.remove(taskName);
        lastUpdate.remove(taskName);
    }
    
    /**
     * Gibt Performance-Statistiken zurück
     */
    public Map<String, Object> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeTasks", updateTasks.size());
        stats.put("averageUpdateTime", updateCount > 0 ? (double) totalUpdateTime / updateCount : 0);
        stats.put("totalUpdateTime", totalUpdateTime);
        stats.put("updateCount", updateCount);
        
        return stats;
    }
}
