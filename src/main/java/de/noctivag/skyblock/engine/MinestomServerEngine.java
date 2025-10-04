package de.noctivag.skyblock.engine;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.network.packet.server.play.TickStepPacket;
import net.minestom.server.timer.TaskSchedule;

import de.noctivag.skyblock.core.architecture.RefactoredArchitecture;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.performance.AsyncIOManager;
import de.noctivag.skyblock.performance.ThreadPoolManager;
import de.noctivag.skyblock.performance.TickOptimizer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Minestom-based Server Engine - Modern Multi-Threaded Architecture
 * 
 * Features:
 * - Multi-threaded tick processing
 * - Asynchronous I/O operations
 * - Thread isolation for different operations
 * - Performance monitoring and optimization
 * - Scalable architecture for high player counts
 */
public class MinestomServerEngine {
    
    private final Logger logger;
    private final MinecraftServer server;
    private final RefactoredArchitecture architecture;
    private final MultiServerDatabaseManager databaseManager;
    
    // Performance Management
    private final AsyncIOManager asyncIOManager;
    private final ThreadPoolManager threadPoolManager;
    private final TickOptimizer tickOptimizer;
    
    // Server State
    private final ConcurrentHashMap<String, Instance> instances = new ConcurrentHashMap<>();
    private final AtomicLong tickCounter = new AtomicLong(0);
    private final AtomicLong lastTickTime = new AtomicLong(System.currentTimeMillis());
    
    // Performance Metrics
    private final AtomicLong totalTickTime = new AtomicLong(0);
    private final AtomicLong maxTickTime = new AtomicLong(0);
    private final AtomicLong tickLagCount = new AtomicLong(0);
    
    public MinestomServerEngine(RefactoredArchitecture architecture, MultiServerDatabaseManager databaseManager) {
        this.logger = Logger.getLogger("MinestomServerEngine");
        this.architecture = architecture;
        this.databaseManager = databaseManager;
        
        // Initialize Minestom server
        this.server = MinecraftServer.init();
        
        // Initialize performance managers
        this.asyncIOManager = new AsyncIOManager();
        this.threadPoolManager = new ThreadPoolManager();
        this.tickOptimizer = new TickOptimizer();
        
        setupServer();
        registerEvents();
        startPerformanceMonitoring();
    }
    
    /**
     * Initialize the Minestom server with optimized settings
     */
    private void setupServer() {
        logger.info("Setting up Minestom server engine...");
        
        // Configure server settings for optimal performance
        server.setBrandName("Hypixel Skyblock Recreation");
        server.setCompressionThreshold(256);
        
        // Initialize instance manager
        InstanceManager instanceManager = server.getInstanceManager();
        
        // Create default instances
        createDefaultInstances(instanceManager);
        
        logger.info("Minestom server engine setup complete");
    }
    
    /**
     * Create default game instances
     */
    private void createDefaultInstances(InstanceManager instanceManager) {
        // Hub instance
        InstanceContainer hubInstance = instanceManager.createInstanceContainer();
        hubInstance.setGenerator(unit -> {
            // Generate flat world for hub
            unit.modifier().fillHeight(0, 40, net.minestom.server.instance.block.Block.STONE);
        });
        instances.put("hub", hubInstance);
        
        // Island instances will be created dynamically
        logger.info("Created default instances: hub");
    }
    
    /**
     * Register server events
     */
    private void registerEvents() {
        GlobalEventHandler globalEventHandler = server.getGlobalEventHandler();
        
        // Player connection events
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            logger.info("Player " + player.getUsername() + " connecting...");
            
            // Handle player login asynchronously
            handlePlayerLoginAsync(player);
        });
        
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();
            logger.info("Player " + player.getUsername() + " disconnecting...");
            
            // Handle player disconnect asynchronously
            handlePlayerDisconnectAsync(player);
        });
        
        // Server tick monitoring
        globalEventHandler.addListener(ServerTickMonitorEvent.class, event -> {
            // Monitor tick performance
            monitorTickPerformance();
        });
    }
    
    /**
     * Handle player login asynchronously
     */
    private void handlePlayerLoginAsync(Player player) {
        threadPoolManager.executeAsync(() -> {
            try {
                // Load player data from database
                CompletableFuture<Void> dataLoad = databaseManager.loadPlayerDataAsync(player.getUuid());
                
                // Set player instance
                player.setInstance(instances.get("hub"), new Pos(0, 100, 0));
                
                // Wait for data loading to complete
                dataLoad.join();
                
                logger.info("Player " + player.getUsername() + " login completed");
                
            } catch (Exception e) {
                logger.severe("Error during player login: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Handle player disconnect asynchronously
     */
    private void handlePlayerDisconnectAsync(Player player) {
        threadPoolManager.executeAsync(() -> {
            try {
                // Save player data to database
                CompletableFuture<Void> dataSave = databaseManager.savePlayerDataAsync(player.getUuid());
                
                // Wait for data saving to complete
                dataSave.join();
                
                logger.info("Player " + player.getUsername() + " disconnect completed");
                
            } catch (Exception e) {
                logger.severe("Error during player disconnect: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Monitor tick performance and optimize
     */
    private void monitorTickPerformance() {
        long currentTime = System.currentTimeMillis();
        long lastTick = lastTickTime.getAndSet(currentTime);
        long tickDuration = currentTime - lastTick;
        
        // Update performance metrics
        totalTickTime.addAndGet(tickDuration);
        tickCounter.incrementAndGet();
        
        if (tickDuration > maxTickTime.get()) {
            maxTickTime.set(tickDuration);
        }
        
        // Detect tick lag (tick taking longer than 50ms)
        if (tickDuration > 50) {
            tickLagCount.incrementAndGet();
            logger.warning("Tick lag detected: " + tickDuration + "ms");
            
            // Trigger optimization
            tickOptimizer.optimizeTickPerformance();
        }
        
        // Log performance statistics every 1000 ticks (50 seconds)
        if (tickCounter.get() % 1000 == 0) {
            logPerformanceStatistics();
        }
    }
    
    /**
     * Log performance statistics
     */
    private void logPerformanceStatistics() {
        long totalTicks = tickCounter.get();
        long totalTime = totalTickTime.get();
        long maxTime = maxTickTime.get();
        long lagCount = tickLagCount.get();
        
        double avgTickTime = (double) totalTime / totalTicks;
        double lagPercentage = (double) lagCount / totalTicks * 100;
        
        logger.info(String.format("Performance Stats - Avg Tick: %.2fms, Max Tick: %dms, Lag: %.2f%% (%d/%d)",
                avgTickTime, maxTime, lagPercentage, lagCount, totalTicks));
    }
    
    /**
     * Start performance monitoring
     */
    private void startPerformanceMonitoring() {
        // Schedule performance monitoring task
        server.schedulerManager().scheduleTask(() -> {
            // Monitor thread pool usage
            threadPoolManager.logThreadPoolStats();
            
            // Monitor async I/O operations
            asyncIOManager.logIOStats();
            
        }, TaskSchedule.tick(100), TaskSchedule.tick(100)); // Every 5 seconds
    }
    
    /**
     * Start the server
     */
    public CompletableFuture<Void> startServer(String host, int port) {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Starting Minestom server on " + host + ":" + port);
                server.start(host, port);
                logger.info("Minestom server started successfully");
            } catch (Exception e) {
                logger.severe("Failed to start Minestom server: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Stop the server
     */
    public CompletableFuture<Void> stopServer() {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Stopping Minestom server...");
                
                // Shutdown thread pools
                threadPoolManager.shutdown();
                asyncIOManager.shutdown();
                
                // Stop server
                server.stop();
                
                logger.info("Minestom server stopped successfully");
            } catch (Exception e) {
                logger.severe("Error stopping Minestom server: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Get server instance
     */
    public MinecraftServer getServer() {
        return server;
    }
    
    /**
     * Get instance by name
     */
    public Instance getInstance(String name) {
        return instances.get(name);
    }
    
    /**
     * Create new instance
     */
    public Instance createInstance(String name) {
        InstanceManager instanceManager = server.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();
        instances.put(name, instance);
        return instance;
    }
    
    /**
     * Get performance metrics
     */
    public PerformanceMetrics getPerformanceMetrics() {
        return new PerformanceMetrics(
            tickCounter.get(),
            totalTickTime.get(),
            maxTickTime.get(),
            tickLagCount.get()
        );
    }
    
    /**
     * Performance metrics data class
     */
    public static class PerformanceMetrics {
        private final long totalTicks;
        private final long totalTickTime;
        private final long maxTickTime;
        private final long lagCount;
        
        public PerformanceMetrics(long totalTicks, long totalTickTime, long maxTickTime, long lagCount) {
            this.totalTicks = totalTicks;
            this.totalTickTime = totalTickTime;
            this.maxTickTime = maxTickTime;
            this.lagCount = lagCount;
        }
        
        public double getAverageTickTime() {
            return totalTicks > 0 ? (double) totalTickTime / totalTicks : 0.0;
        }
        
        public double getLagPercentage() {
            return totalTicks > 0 ? (double) lagCount / totalTicks * 100 : 0.0;
        }
        
        // Getters
        public long getTotalTicks() { return totalTicks; }
        public long getTotalTickTime() { return totalTickTime; }
        public long getMaxTickTime() { return maxTickTime; }
        public long getLagCount() { return lagCount; }
    }
}
