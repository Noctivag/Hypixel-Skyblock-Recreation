package de.noctivag.skyblock.core.architecture;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Thread Pool Manager - Centralized thread pool management for distributed operations
 * 
 * This manager provides:
 * - Specialized thread pools for different operation types
 * - Dynamic thread pool sizing based on load
 * - Monitoring and metrics collection
 * - Graceful shutdown and resource cleanup
 * - Deadlock detection and prevention
 * 
 * Thread Pool Types:
 * - Game Tick: Main game logic processing
 * - I/O Operations: Database and network operations
 * - World Generation: World and chunk generation
 * - Async Tasks: General asynchronous operations
 * - Scheduled Tasks: Periodic and delayed tasks
 */
public class ThreadPoolManager {
    
    private static final Logger logger = Logger.getLogger(ThreadPoolManager.class.getName());
    
    // Thread pool configurations
    private static final int GAME_TICK_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int IO_POOL_SIZE = 8;
    private static final int WORLD_GEN_POOL_SIZE = 4;
    private static final int ASYNC_POOL_SIZE = 16;
    private static final int SCHEDULED_POOL_SIZE = 4;
    
    // Thread pool monitoring
    private static final long METRICS_UPDATE_INTERVAL = 30000; // 30 seconds
    private static final long THREAD_TIMEOUT = 60000; // 60 seconds
    
    // Thread pools
    private ThreadPoolExecutor gameTickExecutor;
    private ThreadPoolExecutor ioExecutor;
    private ThreadPoolExecutor worldGenExecutor;
    private ThreadPoolExecutor asyncExecutor;
    private ScheduledThreadPoolExecutor scheduledExecutor;
    
    // Monitoring
    private final Map<String, PoolMetrics> poolMetrics = new ConcurrentHashMap<>();
    private ScheduledExecutorService metricsExecutor;
    
    // State
    private volatile boolean initialized = false;
    private volatile boolean running = false;
    
    /**
     * Initialize the thread pool manager
     */
    public CompletableFuture<Void> initialize() {
        if (initialized) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Initializing Thread Pool Manager...");
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Initialize thread pools
                initializeThreadPools();
                
                // Start monitoring
                startMetricsMonitoring();
                
                initialized = true;
                logger.info("Thread Pool Manager initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize Thread Pool Manager: " + e.getMessage());
                throw new RuntimeException("Thread pool manager initialization failed", e);
            }
        });
    }
    
    /**
     * Start the thread pool manager
     */
    public void start() {
        if (!initialized) {
            throw new IllegalStateException("Thread pool manager must be initialized before starting");
        }
        
        running = true;
        logger.info("Thread Pool Manager started");
    }
    
    /**
     * Stop the thread pool manager
     */
    public void stop() {
        running = false;
        
        logger.info("Stopping Thread Pool Manager...");
        
        // Shutdown metrics monitoring
        if (metricsExecutor != null) {
            metricsExecutor.shutdown();
        }
        
        // Shutdown thread pools in order
        shutdownThreadPool("Scheduled", scheduledExecutor);
        shutdownThreadPool("Async", asyncExecutor);
        shutdownThreadPool("WorldGen", worldGenExecutor);
        shutdownThreadPool("IO", ioExecutor);
        shutdownThreadPool("GameTick", gameTickExecutor);
        
        logger.info("Thread Pool Manager stopped");
    }
    
    /**
     * Execute game tick operation
     */
    public CompletableFuture<Void> executeGameTick(Runnable task) {
        return CompletableFuture.runAsync(task, gameTickExecutor);
    }
    
    /**
     * Execute I/O operation
     */
    public <T> CompletableFuture<T> executeIO(java.util.function.Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, ioExecutor);
    }
    
    /**
     * Execute world generation task
     */
    public CompletableFuture<Void> executeWorldGen(Runnable task) {
        return CompletableFuture.runAsync(task, worldGenExecutor);
    }
    
    /**
     * Execute async task
     */
    public <T> CompletableFuture<T> executeAsync(java.util.function.Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, asyncExecutor);
    }
    
    /**
     * Execute async task without return value
     */
    public CompletableFuture<Void> executeAsync(Runnable task) {
        return CompletableFuture.runAsync(task, asyncExecutor);
    }
    
    /**
     * Schedule a task to run after delay
     */
    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return scheduledExecutor.schedule(task, delay, unit);
    }
    
    /**
     * Schedule a task to run periodically
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduledExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
    
    /**
     * Schedule a task with fixed delay between executions
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        return scheduledExecutor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }
    
    /**
     * Get thread pool metrics
     */
    public PoolMetrics getPoolMetrics(String poolName) {
        return poolMetrics.get(poolName);
    }
    
    /**
     * Get all pool metrics
     */
    public Map<String, PoolMetrics> getAllPoolMetrics() {
        return new HashMap<>(poolMetrics);
    }
    
    /**
     * Check if thread pools are healthy
     */
    public boolean isHealthy() {
        return gameTickExecutor != null && !gameTickExecutor.isShutdown() &&
               ioExecutor != null && !ioExecutor.isShutdown() &&
               worldGenExecutor != null && !worldGenExecutor.isShutdown() &&
               asyncExecutor != null && !asyncExecutor.isShutdown() &&
               scheduledExecutor != null && !scheduledExecutor.isShutdown();
    }
    
    /**
     * Initialize all thread pools
     */
    private void initializeThreadPools() {
        // Game Tick Thread Pool
        gameTickExecutor = new ThreadPoolExecutor(
            GAME_TICK_POOL_SIZE,
            GAME_TICK_POOL_SIZE * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new NamedThreadFactory("GameTick"),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        // I/O Thread Pool
        ioExecutor = new ThreadPoolExecutor(
            IO_POOL_SIZE,
            IO_POOL_SIZE * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            new NamedThreadFactory("IO"),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        // World Generation Thread Pool
        worldGenExecutor = new ThreadPoolExecutor(
            WORLD_GEN_POOL_SIZE,
            WORLD_GEN_POOL_SIZE * 2,
            120L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            new NamedThreadFactory("WorldGen"),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        // Async Task Thread Pool
        asyncExecutor = new ThreadPoolExecutor(
            ASYNC_POOL_SIZE,
            ASYNC_POOL_SIZE * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new NamedThreadFactory("Async"),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        // Scheduled Task Thread Pool
        scheduledExecutor = new ScheduledThreadPoolExecutor(
            SCHEDULED_POOL_SIZE,
            new NamedThreadFactory("Scheduled")
        );
        scheduledExecutor.setRemoveOnCancelPolicy(true);
        
        // Initialize metrics
        poolMetrics.put("GameTick", new PoolMetrics("GameTick", gameTickExecutor));
        poolMetrics.put("IO", new PoolMetrics("IO", ioExecutor));
        poolMetrics.put("WorldGen", new PoolMetrics("WorldGen", worldGenExecutor));
        poolMetrics.put("Async", new PoolMetrics("Async", asyncExecutor));
        poolMetrics.put("Scheduled", new PoolMetrics("Scheduled", scheduledExecutor));
        
        logger.info("Initialized " + poolMetrics.size() + " thread pools");
    }
    
    /**
     * Start metrics monitoring
     */
    private void startMetricsMonitoring() {
        metricsExecutor = Executors.newSingleThreadScheduledExecutor(
            new NamedThreadFactory("ThreadPoolMetrics")
        );
        
        metricsExecutor.scheduleAtFixedRate(() -> {
            try {
                updateMetrics();
                checkForDeadlocks();
                optimizePoolSizes();
            } catch (Exception e) {
                logger.severe("Error during thread pool monitoring: " + e.getMessage());
            }
        }, METRICS_UPDATE_INTERVAL, METRICS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Update thread pool metrics
     */
    private void updateMetrics() {
        for (PoolMetrics metrics : poolMetrics.values()) {
            metrics.update();
        }
    }
    
    /**
     * Check for potential deadlocks
     */
    private void checkForDeadlocks() {
        // Simple deadlock detection based on queue sizes and active threads
        for (PoolMetrics metrics : poolMetrics.values()) {
            if (metrics.getQueueSize() > 100 && metrics.getActiveThreads() == 0) {
                logger.warning("Potential deadlock detected in " + metrics.getPoolName() + 
                    " pool: queue size " + metrics.getQueueSize() + ", active threads 0");
            }
        }
    }
    
    /**
     * Optimize pool sizes based on load
     */
    private void optimizePoolSizes() {
        // Dynamic pool sizing based on metrics
        for (PoolMetrics metrics : poolMetrics.values()) {
            if (metrics.getLoadPercentage() > 0.9 && metrics.getPoolName().equals("GameTick")) {
                // Increase game tick pool size if heavily loaded
                int currentCoreSize = gameTickExecutor.getCorePoolSize();
                if (currentCoreSize < GAME_TICK_POOL_SIZE * 2) {
                    gameTickExecutor.setCorePoolSize(currentCoreSize + 1);
                    gameTickExecutor.setMaximumPoolSize(gameTickExecutor.getMaximumPoolSize() + 1);
                    logger.info("Increased GameTick pool size to " + (currentCoreSize + 1));
                }
            }
        }
    }
    
    /**
     * Shutdown a thread pool gracefully
     */
    private void shutdownThreadPool(String poolName, ExecutorService executor) {
        if (executor == null) return;
        
        try {
            logger.info("Shutting down " + poolName + " thread pool...");
            
            executor.shutdown();
            
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                logger.warning(poolName + " thread pool did not terminate gracefully, forcing shutdown");
                executor.shutdownNow();
                
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    logger.severe("Could not shutdown " + poolName + " thread pool");
                }
            } else {
                logger.info(poolName + " thread pool shutdown successfully");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
            logger.warning("Interrupted while shutting down " + poolName + " thread pool");
        }
    }
    
    /**
     * Named thread factory for better debugging
     */
    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        
        NamedThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix + "-";
        }
        
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
    
    /**
     * Thread pool metrics
     */
    public static class PoolMetrics {
        private final String poolName;
        private final ThreadPoolExecutor executor;
        
        private volatile int activeThreads = 0;
        private volatile int queueSize = 0;
        private volatile long completedTasks = 0;
        private volatile long totalTasks = 0;
        private volatile double loadPercentage = 0.0;
        private volatile long lastUpdateTime = 0;
        
        public PoolMetrics(String poolName, ThreadPoolExecutor executor) {
            this.poolName = poolName;
            this.executor = executor;
            this.lastUpdateTime = System.currentTimeMillis();
        }
        
        public void update() {
            if (executor == null) return;
            
            this.activeThreads = executor.getActiveCount();
            this.queueSize = executor.getQueue().size();
            this.completedTasks = executor.getCompletedTaskCount();
            this.totalTasks = executor.getTaskCount();
            this.loadPercentage = calculateLoadPercentage();
            this.lastUpdateTime = System.currentTimeMillis();
        }
        
        private double calculateLoadPercentage() {
            if (executor.getMaximumPoolSize() == 0) return 0.0;
            
            double threadLoad = (double) activeThreads / executor.getMaximumPoolSize();
            double queueLoad = Math.min(1.0, (double) queueSize / 100.0); // Normalize queue size
            
            return (threadLoad + queueLoad) / 2.0;
        }
        
        // Getters
        public String getPoolName() { return poolName; }
        public int getActiveThreads() { return activeThreads; }
        public int getQueueSize() { return queueSize; }
        public long getCompletedTasks() { return completedTasks; }
        public long getTotalTasks() { return totalTasks; }
        public double getLoadPercentage() { return loadPercentage; }
        public long getLastUpdateTime() { return lastUpdateTime; }
        
        public int getCorePoolSize() { return executor.getCorePoolSize(); }
        public int getMaximumPoolSize() { return executor.getMaximumPoolSize(); }
        public long getKeepAliveTime() { return executor.getKeepAliveTime(TimeUnit.MILLISECONDS); }
        
        @Override
        public String toString() {
            return String.format("%s Pool: %d/%d active, %d queued, %.1f%% load, %d completed/%d total",
                poolName, activeThreads, executor.getMaximumPoolSize(), queueSize, 
                loadPercentage * 100, completedTasks, totalTasks);
        }
    }
}
