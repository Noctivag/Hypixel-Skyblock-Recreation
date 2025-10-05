package de.noctivag.skyblock.performance;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * MultithreadingManager - Centralized thread pool management for optimal CPU utilization
 * 
 * Features:
 * - Custom thread pools for different operation types
 * - Async database operations
 * - Async file I/O operations
 * - Async heavy computations
 * - CPU core utilization optimization
 * - Performance monitoring
 */
public class MultithreadingManager {
    private final SkyblockPlugin SkyblockPlugin;
    
    // Custom thread pools for different operation types
    private final ExecutorService databaseExecutor;
    private final ExecutorService fileIOExecutor;
    private final ExecutorService computationExecutor;
    private final ExecutorService networkExecutor;
    private final ExecutorService guiExecutor;
    
    // Thread pool monitoring
    private final AtomicInteger activeDatabaseTasks = new AtomicInteger(0);
    private final AtomicInteger activeFileIOTasks = new AtomicInteger(0);
    private final AtomicInteger activeComputationTasks = new AtomicInteger(0);
    private final AtomicInteger activeNetworkTasks = new AtomicInteger(0);
    private final AtomicInteger activeGUITasks = new AtomicInteger(0);
    
    // Performance metrics
    private final ConcurrentHashMap<String, Long> operationTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> operationCounts = new ConcurrentHashMap<>();
    
    public MultithreadingManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        
        // Calculate optimal thread pool sizes based on CPU cores
        int cpuCores = Runtime.getRuntime().availableProcessors();
        int databaseThreads = Math.max(2, cpuCores / 2);
        int fileIOThreads = Math.max(2, cpuCores / 4);
        int computationThreads = Math.max(4, cpuCores);
        int networkThreads = Math.max(2, cpuCores / 3);
        int guiThreads = Math.max(2, cpuCores / 4);
        
        SkyblockPlugin.getLogger().info("Initializing MultithreadingManager with " + cpuCores + " CPU cores");
        SkyblockPlugin.getLogger().info("Thread pool sizes - DB: " + databaseThreads + ", FileIO: " + fileIOThreads + 
                              ", Computation: " + computationThreads + ", Network: " + networkThreads + ", GUI: " + guiThreads);
        
        // Create custom thread pools
        this.databaseExecutor = createThreadPool("Database", databaseThreads, databaseThreads * 2);
        this.fileIOExecutor = createThreadPool("FileIO", fileIOThreads, fileIOThreads * 2);
        this.computationExecutor = createThreadPool("Computation", computationThreads, computationThreads * 2);
        this.networkExecutor = createThreadPool("Network", networkThreads, networkThreads * 2);
        this.guiExecutor = createThreadPool("GUI", guiThreads, guiThreads * 2);
        
        // Start performance monitoring
        startPerformanceMonitoring();
    }
    
    private ThreadPoolExecutor createThreadPool(String name, int coreSize, int maxSize) {
        return new ThreadPoolExecutor(
            coreSize, maxSize, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "BasicsPlugin-" + name + "-" + threadNumber.getAndIncrement());
                    t.setDaemon(true);
                    t.setPriority(Thread.NORM_PRIORITY);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    
    /**
     * Execute database operations asynchronously
     */
    public CompletableFuture<Void> executeDatabaseAsync(Runnable task) {
        activeDatabaseTasks.incrementAndGet();
        return CompletableFuture.runAsync(() -> {
            long startTime = System.nanoTime();
            try {
                task.run();
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("database", endTime - startTime);
                activeDatabaseTasks.decrementAndGet();
            }
        }, databaseExecutor);
    }
    
    /**
     * Execute database operations asynchronously with return value
     */
    public <T> CompletableFuture<T> executeDatabaseAsync(Callable<T> task) {
        activeDatabaseTasks.incrementAndGet();
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            try {
                return task.call();
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Database task failed", e);
                throw new RuntimeException(e);
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("database", endTime - startTime);
                activeDatabaseTasks.decrementAndGet();
            }
        }, databaseExecutor);
    }
    
    /**
     * Execute file I/O operations asynchronously
     */
    public CompletableFuture<Void> executeFileIOAsync(Runnable task) {
        activeFileIOTasks.incrementAndGet();
        return CompletableFuture.runAsync(() -> {
            long startTime = System.nanoTime();
            try {
                task.run();
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("fileio", endTime - startTime);
                activeFileIOTasks.decrementAndGet();
            }
        }, fileIOExecutor);
    }
    
    /**
     * Execute file I/O operations asynchronously with return value
     */
    public <T> CompletableFuture<T> executeFileIOAsync(Callable<T> task) {
        activeFileIOTasks.incrementAndGet();
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            try {
                return task.call();
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "File I/O task failed", e);
                throw new RuntimeException(e);
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("fileio", endTime - startTime);
                activeFileIOTasks.decrementAndGet();
            }
        }, fileIOExecutor);
    }
    
    /**
     * Execute heavy computations asynchronously
     */
    public CompletableFuture<Void> executeComputationAsync(Runnable task) {
        activeComputationTasks.incrementAndGet();
        return CompletableFuture.runAsync(() -> {
            long startTime = System.nanoTime();
            try {
                task.run();
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("computation", endTime - startTime);
                activeComputationTasks.decrementAndGet();
            }
        }, computationExecutor);
    }
    
    /**
     * Execute heavy computations asynchronously with return value
     */
    public <T> CompletableFuture<T> executeComputationAsync(Callable<T> task) {
        activeComputationTasks.incrementAndGet();
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            try {
                return task.call();
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Computation task failed", e);
                throw new RuntimeException(e);
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("computation", endTime - startTime);
                activeComputationTasks.decrementAndGet();
            }
        }, computationExecutor);
    }
    
    /**
     * Execute network operations asynchronously
     */
    public CompletableFuture<Void> executeNetworkAsync(Runnable task) {
        activeNetworkTasks.incrementAndGet();
        return CompletableFuture.runAsync(() -> {
            long startTime = System.nanoTime();
            try {
                task.run();
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("network", endTime - startTime);
                activeNetworkTasks.decrementAndGet();
            }
        }, networkExecutor);
    }
    
    /**
     * Execute network operations asynchronously with return value
     */
    public <T> CompletableFuture<T> executeNetworkAsync(Callable<T> task) {
        activeNetworkTasks.incrementAndGet();
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            try {
                return task.call();
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Network task failed", e);
                throw new RuntimeException(e);
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("network", endTime - startTime);
                activeNetworkTasks.decrementAndGet();
            }
        }, networkExecutor);
    }
    
    /**
     * Execute GUI operations asynchronously (with Bukkit thread safety)
     */
    public CompletableFuture<Void> executeGUIAsync(Runnable task) {
        activeGUITasks.incrementAndGet();
        return CompletableFuture.runAsync(() -> {
            long startTime = System.nanoTime();
            try {
                task.run();
            } finally {
                long endTime = System.nanoTime();
                recordOperationTime("gui", endTime - startTime);
                activeGUITasks.decrementAndGet();
            }
        }, guiExecutor);
    }
    
    /**
     * Execute GUI operations on main thread (for Bukkit thread safety)
     */
    public void executeGUIOnMainThread(Runnable task) {
        if (org.bukkit.Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            org.bukkit.Bukkit.getScheduler().runTask(SkyblockPlugin, task);
        }
    }
    
    /**
     * Record operation performance metrics
     */
    private void recordOperationTime(String operationType, long nanoTime) {
        operationTimes.merge(operationType, nanoTime, Long::sum);
        operationCounts.computeIfAbsent(operationType, k -> new AtomicInteger(0)).incrementAndGet();
    }
    
    /**
     * Start performance monitoring
     */
    private void startPerformanceMonitoring() {
        // Use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(6000L * 50); // Initial delay: 5 minutes = 300,000 ms
                while (SkyblockPlugin.isEnabled()) {
                    // Log thread pool statistics every 5 minutes
                    logThreadPoolStats();
                    
                    // Clean up old metrics
                    cleanupMetrics();
                    
                    Thread.sleep(6000L * 50); // Every 5 minutes = 300,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * Log thread pool statistics
     */
    private void logThreadPoolStats() {
        SkyblockPlugin.getLogger().info("=== Thread Pool Statistics ===");
        SkyblockPlugin.getLogger().info("Active Tasks - DB: " + activeDatabaseTasks.get() + 
                              ", FileIO: " + activeFileIOTasks.get() + 
                              ", Computation: " + activeComputationTasks.get() + 
                              ", Network: " + activeNetworkTasks.get() + 
                              ", GUI: " + activeGUITasks.get());
        
        // Log average operation times
        for (String operationType : operationTimes.keySet()) {
            long totalTime = operationTimes.get(operationType);
            int count = operationCounts.get(operationType).get();
            if (count > 0) {
                double avgTimeMs = (totalTime / (double) count) / 1_000_000.0;
                SkyblockPlugin.getLogger().info(operationType + " operations: " + count + " total, avg: " + 
                                      String.format("%.2f", avgTimeMs) + "ms");
            }
        }
    }
    
    /**
     * Clean up old metrics
     */
    private void cleanupMetrics() {
        // Reset metrics every hour to prevent memory leaks
        operationTimes.clear();
        operationCounts.clear();
    }
    
    /**
     * Get thread pool statistics
     */
    public String getThreadPoolStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Database Pool: ").append(((ThreadPoolExecutor) databaseExecutor).getActiveCount())
             .append("/").append(((ThreadPoolExecutor) databaseExecutor).getPoolSize()).append("\n");
        stats.append("FileIO Pool: ").append(((ThreadPoolExecutor) fileIOExecutor).getActiveCount())
             .append("/").append(((ThreadPoolExecutor) fileIOExecutor).getPoolSize()).append("\n");
        stats.append("Computation Pool: ").append(((ThreadPoolExecutor) computationExecutor).getActiveCount())
             .append("/").append(((ThreadPoolExecutor) computationExecutor).getPoolSize()).append("\n");
        stats.append("Network Pool: ").append(((ThreadPoolExecutor) networkExecutor).getActiveCount())
             .append("/").append(((ThreadPoolExecutor) networkExecutor).getPoolSize()).append("\n");
        stats.append("GUI Pool: ").append(((ThreadPoolExecutor) guiExecutor).getActiveCount())
             .append("/").append(((ThreadPoolExecutor) guiExecutor).getPoolSize());
        return stats.toString();
    }
    
    /**
     * Shutdown all thread pools
     */
    public void shutdown() {
        SkyblockPlugin.getLogger().info("Shutting down MultithreadingManager...");
        
        shutdownExecutor(databaseExecutor, "Database");
        shutdownExecutor(fileIOExecutor, "FileIO");
        shutdownExecutor(computationExecutor, "Computation");
        shutdownExecutor(networkExecutor, "Network");
        shutdownExecutor(guiExecutor, "GUI");
        
        SkyblockPlugin.getLogger().info("MultithreadingManager shutdown complete");
    }
    
    private void shutdownExecutor(ExecutorService executor, String name) {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                SkyblockPlugin.getLogger().warning(name + " executor did not terminate gracefully");
                executor.shutdownNow();
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    SkyblockPlugin.getLogger().severe(name + " executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
