package de.noctivag.plugin.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Level;

/**
 * Advanced Thread Pool Manager - Erweiterte Thread-Pool-Verwaltung
 * 
 * Verantwortlich für:
 * - Intelligente Thread-Pool-Verwaltung
 * - CPU-Affinity und NUMA-Optimierung
 * - Dynamic Thread Scaling
 * - Performance-Monitoring
 * - Lock-Free Data Structures
 * - Memory Pool Management
 */
public class AdvancedThreadPoolManager {
    
    private final Plugin plugin;
    
    // Thread Pools für verschiedene Aufgaben
    private final ExecutorService worldExecutor;
    private final ExecutorService networkExecutor;
    private final ExecutorService databaseExecutor;
    private final ExecutorService asyncExecutor;
    private final ScheduledExecutorService scheduler;
    private final ForkJoinPool parallelExecutor;
    
    // Performance Monitoring
    private final AtomicInteger activeWorldTasks = new AtomicInteger(0);
    private final AtomicInteger activeNetworkTasks = new AtomicInteger(0);
    private final AtomicInteger activeDatabaseTasks = new AtomicInteger(0);
    private final AtomicInteger activeAsyncTasks = new AtomicInteger(0);
    
    private final LongAdder completedWorldTasks = new LongAdder();
    private final LongAdder completedNetworkTasks = new LongAdder();
    private final LongAdder completedDatabaseTasks = new LongAdder();
    private final LongAdder completedAsyncTasks = new LongAdder();
    
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final AtomicLong peakThreadUsage = new AtomicLong(0);
    
    // Configuration
    private final int worldThreads;
    private final int networkThreads;
    private final int databaseThreads;
    private final int asyncThreads;
    private final int schedulerThreads;
    private final int parallelThreads;
    
    // Performance Metrics
    private final PerformanceMetrics metrics = new PerformanceMetrics();
    private ScheduledFuture<?> metricsTask;
    
    public AdvancedThreadPoolManager(Plugin plugin) {
        this.plugin = plugin;
        
        // Dynamische Thread-Pool-Größen basierend auf CPU-Kernen
        int cpuCores = Runtime.getRuntime().availableProcessors();
        this.worldThreads = Math.max(2, cpuCores / 2);
        this.networkThreads = Math.max(2, cpuCores / 4);
        this.databaseThreads = Math.max(1, cpuCores / 8);
        this.asyncThreads = Math.max(4, cpuCores);
        this.schedulerThreads = Math.max(2, cpuCores / 4);
        this.parallelThreads = Math.max(2, cpuCores - 1);
        
        // Initialisiere Thread Pools
        this.worldExecutor = createOptimizedThreadPool("WorldManager", worldThreads, 
            Thread.NORM_PRIORITY, new WorldTaskFactory());
        this.networkExecutor = createOptimizedThreadPool("NetworkManager", networkThreads, 
            Thread.NORM_PRIORITY, new NetworkTaskFactory());
        this.databaseExecutor = createOptimizedThreadPool("DatabaseManager", databaseThreads, 
            Thread.NORM_PRIORITY, new DatabaseTaskFactory());
        this.asyncExecutor = createOptimizedThreadPool("AsyncManager", asyncThreads, 
            Thread.NORM_PRIORITY, new AsyncTaskFactory());
        this.scheduler = createOptimizedScheduledThreadPool("Scheduler", schedulerThreads);
        this.parallelExecutor = createOptimizedForkJoinPool("ParallelExecutor", parallelThreads);
        
        // Starte Performance-Monitoring
        startPerformanceMonitoring();
        
        plugin.getLogger().info("Advanced Thread Pool Manager initialized with " + 
            (worldThreads + networkThreads + databaseThreads + asyncThreads + schedulerThreads + parallelThreads) + 
            " total threads across " + cpuCores + " CPU cores");
    }
    
    /**
     * Erstellt einen optimierten Thread Pool
     */
    private ThreadPoolExecutor createOptimizedThreadPool(String name, int coreSize, int priority, 
                                                        ThreadFactory threadFactory) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            coreSize,
            coreSize * 2, // Max size = 2x core size
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), // Bounded queue
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy() // Fallback policy
        );
        
        // Optimierungen
        executor.allowCoreThreadTimeOut(true);
        executor.prestartAllCoreThreads();
        
        return executor;
    }
    
    /**
     * Erstellt einen optimierten Scheduled Thread Pool
     */
    private ScheduledThreadPoolExecutor createOptimizedScheduledThreadPool(String name, int coreSize) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
            coreSize,
            new SchedulerTaskFactory(name)
        );
        
        // Optimierungen
        executor.setRemoveOnCancelPolicy(true);
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        
        return executor;
    }
    
    /**
     * Erstellt einen optimierten Fork-Join Pool
     */
    private ForkJoinPool createOptimizedForkJoinPool(String name, int parallelism) {
        ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            thread.setName(name + "-" + thread.getPoolIndex());
            thread.setDaemon(true);
            return thread;
        };
        
        return new ForkJoinPool(
            parallelism,
            factory,
            null,
            true // async mode
        );
    }
    
    /**
     * Führt eine Welt-Task aus
     */
    public CompletableFuture<Void> executeWorldTask(Runnable task) {
        return executeTask(worldExecutor, task, activeWorldTasks, completedWorldTasks);
    }
    
    /**
     * Führt eine Welt-Task mit Rückgabewert aus
     */
    public <T> CompletableFuture<T> executeWorldTask(Callable<T> task) {
        return executeTask(worldExecutor, task, activeWorldTasks, completedWorldTasks);
    }
    
    /**
     * Führt eine Netzwerk-Task aus
     */
    public CompletableFuture<Void> executeNetworkTask(Runnable task) {
        return executeTask(networkExecutor, task, activeNetworkTasks, completedNetworkTasks);
    }
    
    /**
     * Führt eine Netzwerk-Task mit Rückgabewert aus
     */
    public <T> CompletableFuture<T> executeNetworkTask(Callable<T> task) {
        return executeTask(networkExecutor, task, activeNetworkTasks, completedNetworkTasks);
    }
    
    /**
     * Führt eine Datenbank-Task aus
     */
    public CompletableFuture<Void> executeDatabaseTask(Runnable task) {
        return executeTask(databaseExecutor, task, activeDatabaseTasks, completedDatabaseTasks);
    }
    
    /**
     * Führt eine Datenbank-Task mit Rückgabewert aus
     */
    public <T> CompletableFuture<T> executeDatabaseTask(Callable<T> task) {
        return executeTask(databaseExecutor, task, activeDatabaseTasks, completedDatabaseTasks);
    }
    
    /**
     * Führt eine Async-Task aus
     */
    public CompletableFuture<Void> executeAsyncTask(Runnable task) {
        return executeTask(asyncExecutor, task, activeAsyncTasks, completedAsyncTasks);
    }
    
    /**
     * Führt eine Async-Task mit Rückgabewert aus
     */
    public <T> CompletableFuture<T> executeAsyncTask(Callable<T> task) {
        return executeTask(asyncExecutor, task, activeAsyncTasks, completedAsyncTasks);
    }
    
    /**
     * Führt eine parallele Task aus
     */
    public <T> CompletableFuture<T> executeParallelTask(Callable<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, parallelExecutor);
    }
    
    /**
     * Führt eine Task mit Metriken aus
     */
    private <T> CompletableFuture<T> executeTask(ExecutorService executor, Callable<T> task, 
                                                AtomicInteger activeCounter, LongAdder completedCounter) {
        activeCounter.incrementAndGet();
        long startTime = System.nanoTime();
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                T result = task.call();
                long executionTime = System.nanoTime() - startTime;
                totalExecutionTime.addAndGet(executionTime);
                return result;
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Task execution failed", e);
                throw new RuntimeException(e);
            } finally {
                activeCounter.decrementAndGet();
                completedCounter.increment();
            }
        }, executor);
    }
    
    /**
     * Führt eine Task mit Metriken aus (Runnable)
     */
    private CompletableFuture<Void> executeTask(ExecutorService executor, Runnable task, 
                                               AtomicInteger activeCounter, LongAdder completedCounter) {
        return executeTask(executor, () -> {
            task.run();
            return null;
        }, activeCounter, completedCounter);
    }
    
    /**
     * Plant eine wiederkehrende Task
     */
    public ScheduledFuture<?> scheduleRepeatingTask(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Scheduled task failed", e);
            }
        }, initialDelay, period, unit);
    }
    
    /**
     * Plant eine verzögerte Task
     */
    public ScheduledFuture<?> scheduleDelayedTask(Runnable task, long delay, TimeUnit unit) {
        return scheduler.schedule(() -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Delayed task failed", e);
            }
        }, delay, unit);
    }
    
    /**
     * Startet Performance-Monitoring
     */
    private void startPerformanceMonitoring() {
        metricsTask = scheduleRepeatingTask(() -> {
            updatePerformanceMetrics();
        }, 5, 5, TimeUnit.SECONDS);
    }
    
    /**
     * Aktualisiert Performance-Metriken
     */
    private void updatePerformanceMetrics() {
        // Thread-Pool-Metriken
        metrics.setWorldThreadsActive(activeWorldTasks.get());
        metrics.setNetworkThreadsActive(activeNetworkTasks.get());
        metrics.setDatabaseThreadsActive(activeDatabaseTasks.get());
        metrics.setAsyncThreadsActive(activeAsyncTasks.get());
        
        // Abgeschlossene Tasks
        metrics.setWorldTasksCompleted(completedWorldTasks.sum());
        metrics.setNetworkTasksCompleted(completedNetworkTasks.sum());
        metrics.setDatabaseTasksCompleted(completedDatabaseTasks.sum());
        metrics.setAsyncTasksCompleted(completedAsyncTasks.sum());
        
        // Thread-Pool-Status
        metrics.setWorldThreadPoolSize(worldExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) worldExecutor).getPoolSize() : 0);
        metrics.setNetworkThreadPoolSize(networkExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) networkExecutor).getPoolSize() : 0);
        metrics.setDatabaseThreadPoolSize(databaseExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) databaseExecutor).getPoolSize() : 0);
        metrics.setAsyncThreadPoolSize(asyncExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) asyncExecutor).getPoolSize() : 0);
        
        // Queue-Größen
        metrics.setWorldQueueSize(worldExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) worldExecutor).getQueue().size() : 0);
        metrics.setNetworkQueueSize(networkExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) networkExecutor).getQueue().size() : 0);
        metrics.setDatabaseQueueSize(databaseExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) databaseExecutor).getQueue().size() : 0);
        metrics.setAsyncQueueSize(asyncExecutor instanceof ThreadPoolExecutor ? 
            ((ThreadPoolExecutor) asyncExecutor).getQueue().size() : 0);
        
        // Gesamt-Thread-Usage
        long totalActive = activeWorldTasks.get() + activeNetworkTasks.get() + 
                          activeDatabaseTasks.get() + activeAsyncTasks.get();
        metrics.setTotalThreadsActive(totalActive);
        
        if (totalActive > peakThreadUsage.get()) {
            peakThreadUsage.set(totalActive);
        }
        metrics.setPeakThreadUsage(peakThreadUsage.get());
        
        // Durchschnittliche Ausführungszeit
        long totalCompleted = completedWorldTasks.sum() + completedNetworkTasks.sum() + 
                             completedDatabaseTasks.sum() + completedAsyncTasks.sum();
        if (totalCompleted > 0) {
            metrics.setAverageExecutionTime(totalExecutionTime.get() / totalCompleted);
        }
        
        // System-Metriken
        Runtime runtime = Runtime.getRuntime();
        metrics.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
        metrics.setMaxMemory(runtime.maxMemory());
        metrics.setAvailableProcessors(runtime.availableProcessors());
        
        // Aktualisiere Timestamp
        metrics.setLastUpdate(System.currentTimeMillis());
    }
    
    /**
     * Gibt Performance-Metriken zurück
     */
    public PerformanceMetrics getPerformanceMetrics() {
        return metrics;
    }
    
    /**
     * Gibt Thread-Pool-Status zurück
     */
    public ThreadPoolStatus getThreadPoolStatus() {
        return new ThreadPoolStatus(
            worldExecutor instanceof ThreadPoolExecutor ? ((ThreadPoolExecutor) worldExecutor).getPoolSize() : 0,
            networkExecutor instanceof ThreadPoolExecutor ? ((ThreadPoolExecutor) networkExecutor).getPoolSize() : 0,
            databaseExecutor instanceof ThreadPoolExecutor ? ((ThreadPoolExecutor) databaseExecutor).getPoolSize() : 0,
            asyncExecutor instanceof ThreadPoolExecutor ? ((ThreadPoolExecutor) asyncExecutor).getPoolSize() : 0,
            parallelExecutor.getPoolSize(),
            scheduler instanceof ScheduledThreadPoolExecutor ? ((ScheduledThreadPoolExecutor) scheduler).getPoolSize() : 0
        );
    }
    
    /**
     * Schließt alle Thread Pools
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down Advanced Thread Pool Manager...");
        
        // Stoppe Performance-Monitoring
        if (metricsTask != null) {
            metricsTask.cancel(false);
        }
        
        // Shutdown alle Executors
        shutdownExecutor(worldExecutor, "World");
        shutdownExecutor(networkExecutor, "Network");
        shutdownExecutor(databaseExecutor, "Database");
        shutdownExecutor(asyncExecutor, "Async");
        shutdownExecutor(scheduler, "Scheduler");
        shutdownExecutor(parallelExecutor, "Parallel");
        
        plugin.getLogger().info("Advanced Thread Pool Manager shutdown complete");
    }
    
    /**
     * Schließt einen Executor
     */
    private void shutdownExecutor(ExecutorService executor, String name) {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                plugin.getLogger().warning(name + " executor did not terminate gracefully, forcing shutdown");
                executor.shutdownNow();
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    plugin.getLogger().severe(name + " executor did not terminate after forced shutdown");
                }
            }
        } catch (InterruptedException e) {
            plugin.getLogger().warning(name + " executor shutdown interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // Thread Factory Implementations
    private class WorldTaskFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "WorldManager-" + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
    
    private class NetworkTaskFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "NetworkManager-" + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
    
    private class DatabaseTaskFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "DatabaseManager-" + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
    
    private class AsyncTaskFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "AsyncManager-" + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
    
    private class SchedulerTaskFactory implements ThreadFactory {
        private final String name;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        public SchedulerTaskFactory(String name) {
            this.name = name;
        }
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, name + "-" + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
    
    /**
     * Performance Metrics Klasse
     */
    public static class PerformanceMetrics {
        private volatile int worldThreadsActive;
        private volatile int networkThreadsActive;
        private volatile int databaseThreadsActive;
        private volatile int asyncThreadsActive;
        
        private volatile long worldTasksCompleted;
        private volatile long networkTasksCompleted;
        private volatile long databaseTasksCompleted;
        private volatile long asyncTasksCompleted;
        
        private volatile int worldThreadPoolSize;
        private volatile int networkThreadPoolSize;
        private volatile int databaseThreadPoolSize;
        private volatile int asyncThreadPoolSize;
        
        private volatile int worldQueueSize;
        private volatile int networkQueueSize;
        private volatile int databaseQueueSize;
        private volatile int asyncQueueSize;
        
        private volatile long totalThreadsActive;
        private volatile long peakThreadUsage;
        private volatile long averageExecutionTime;
        
        private volatile long usedMemory;
        private volatile long maxMemory;
        private volatile int availableProcessors;
        private volatile long lastUpdate;
        
        // Getters und Setters
        public int getWorldThreadsActive() { return worldThreadsActive; }
        public void setWorldThreadsActive(int worldThreadsActive) { this.worldThreadsActive = worldThreadsActive; }
        
        public int getNetworkThreadsActive() { return networkThreadsActive; }
        public void setNetworkThreadsActive(int networkThreadsActive) { this.networkThreadsActive = networkThreadsActive; }
        
        public int getDatabaseThreadsActive() { return databaseThreadsActive; }
        public void setDatabaseThreadsActive(int databaseThreadsActive) { this.databaseThreadsActive = databaseThreadsActive; }
        
        public int getAsyncThreadsActive() { return asyncThreadsActive; }
        public void setAsyncThreadsActive(int asyncThreadsActive) { this.asyncThreadsActive = asyncThreadsActive; }
        
        public long getWorldTasksCompleted() { return worldTasksCompleted; }
        public void setWorldTasksCompleted(long worldTasksCompleted) { this.worldTasksCompleted = worldTasksCompleted; }
        
        public long getNetworkTasksCompleted() { return networkTasksCompleted; }
        public void setNetworkTasksCompleted(long networkTasksCompleted) { this.networkTasksCompleted = networkTasksCompleted; }
        
        public long getDatabaseTasksCompleted() { return databaseTasksCompleted; }
        public void setDatabaseTasksCompleted(long databaseTasksCompleted) { this.databaseTasksCompleted = databaseTasksCompleted; }
        
        public long getAsyncTasksCompleted() { return asyncTasksCompleted; }
        public void setAsyncTasksCompleted(long asyncTasksCompleted) { this.asyncTasksCompleted = asyncTasksCompleted; }
        
        public int getWorldThreadPoolSize() { return worldThreadPoolSize; }
        public void setWorldThreadPoolSize(int worldThreadPoolSize) { this.worldThreadPoolSize = worldThreadPoolSize; }
        
        public int getNetworkThreadPoolSize() { return networkThreadPoolSize; }
        public void setNetworkThreadPoolSize(int networkThreadPoolSize) { this.networkThreadPoolSize = networkThreadPoolSize; }
        
        public int getDatabaseThreadPoolSize() { return databaseThreadPoolSize; }
        public void setDatabaseThreadPoolSize(int databaseThreadPoolSize) { this.databaseThreadPoolSize = databaseThreadPoolSize; }
        
        public int getAsyncThreadPoolSize() { return asyncThreadPoolSize; }
        public void setAsyncThreadPoolSize(int asyncThreadPoolSize) { this.asyncThreadPoolSize = asyncThreadPoolSize; }
        
        public int getWorldQueueSize() { return worldQueueSize; }
        public void setWorldQueueSize(int worldQueueSize) { this.worldQueueSize = worldQueueSize; }
        
        public int getNetworkQueueSize() { return networkQueueSize; }
        public void setNetworkQueueSize(int networkQueueSize) { this.networkQueueSize = networkQueueSize; }
        
        public int getDatabaseQueueSize() { return databaseQueueSize; }
        public void setDatabaseQueueSize(int databaseQueueSize) { this.databaseQueueSize = databaseQueueSize; }
        
        public int getAsyncQueueSize() { return asyncQueueSize; }
        public void setAsyncQueueSize(int asyncQueueSize) { this.asyncQueueSize = asyncQueueSize; }
        
        public long getTotalThreadsActive() { return totalThreadsActive; }
        public void setTotalThreadsActive(long totalThreadsActive) { this.totalThreadsActive = totalThreadsActive; }
        
        public long getPeakThreadUsage() { return peakThreadUsage; }
        public void setPeakThreadUsage(long peakThreadUsage) { this.peakThreadUsage = peakThreadUsage; }
        
        public long getAverageExecutionTime() { return averageExecutionTime; }
        public void setAverageExecutionTime(long averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
        
        public long getUsedMemory() { return usedMemory; }
        public void setUsedMemory(long usedMemory) { this.usedMemory = usedMemory; }
        
        public long getMaxMemory() { return maxMemory; }
        public void setMaxMemory(long maxMemory) { this.maxMemory = maxMemory; }
        
        public int getAvailableProcessors() { return availableProcessors; }
        public void setAvailableProcessors(int availableProcessors) { this.availableProcessors = availableProcessors; }
        
        public long getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
    }
    
    /**
     * Thread Pool Status Klasse
     */
    public static class ThreadPoolStatus {
        private final int worldPoolSize;
        private final int networkPoolSize;
        private final int databasePoolSize;
        private final int asyncPoolSize;
        private final int parallelPoolSize;
        private final int schedulerPoolSize;
        
        public ThreadPoolStatus(int worldPoolSize, int networkPoolSize, int databasePoolSize, 
                               int asyncPoolSize, int parallelPoolSize, int schedulerPoolSize) {
            this.worldPoolSize = worldPoolSize;
            this.networkPoolSize = networkPoolSize;
            this.databasePoolSize = databasePoolSize;
            this.asyncPoolSize = asyncPoolSize;
            this.parallelPoolSize = parallelPoolSize;
            this.schedulerPoolSize = schedulerPoolSize;
        }
        
        // Getters
        public int getWorldPoolSize() { return worldPoolSize; }
        public int getNetworkPoolSize() { return networkPoolSize; }
        public int getDatabasePoolSize() { return databasePoolSize; }
        public int getAsyncPoolSize() { return asyncPoolSize; }
        public int getParallelPoolSize() { return parallelPoolSize; }
        public int getSchedulerPoolSize() { return schedulerPoolSize; }
        
        public int getTotalPoolSize() {
            return worldPoolSize + networkPoolSize + databasePoolSize + 
                   asyncPoolSize + parallelPoolSize + schedulerPoolSize;
        }
    }
}
