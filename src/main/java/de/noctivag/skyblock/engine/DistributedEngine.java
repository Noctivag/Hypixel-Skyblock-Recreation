package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.core.architecture.StateSynchronizationLayer;
import de.noctivag.skyblock.core.architecture.GlobalInstanceManager;
import de.noctivag.skyblock.core.architecture.LoadBalancer;
import de.noctivag.skyblock.core.architecture.ThreadPoolManager;
import de.noctivag.skyblock.core.architecture.MongoDBSchema;
import de.noctivag.skyblock.core.architecture.DataMutexService;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.coordinate.Pos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

/**
 * Distributed Engine - Multi-threaded server engine with distributed architecture
 * 
 * This engine provides:
 * - Multi-threaded architecture with separated concerns
 * - Asynchronous I/O operations
 * - Distributed state management
 * - Dynamic instance management
 * - Intelligent load balancing
 * 
 * Key Features:
 * - Game tick isolation from I/O operations
 * - Separate thread pools for different operations
 * - Redis-based state synchronization
 * - Container-based instance isolation
 */
public class DistributedEngine {
    
    private static final Logger logger = Logger.getLogger(DistributedEngine.class.getName());
    
    // Distributed architecture components
    private final StateSynchronizationLayer stateLayer;
    private final GlobalInstanceManager gimSystem;
    private final LoadBalancer loadBalancer;
    private final ThreadPoolManager threadPoolManager;
    private final MongoDBSchema mongoDBSchema;
    private final DataMutexService dataMutexService;
    
    // Thread pools for different operations
    private final ExecutorService gameTickExecutor;
    private final ExecutorService ioExecutor;
    private final ExecutorService worldGenerationExecutor;
    private final ScheduledExecutorService scheduler;
    
    // Minestom server
    private MinecraftServer server;
    private InstanceManager instanceManager;
    
    // Engine state
    private volatile boolean running = false;
    private volatile boolean initialized = false;
    
    public DistributedEngine() {
        // Initialize distributed components
        this.stateLayer = new StateSynchronizationLayer();
        this.gimSystem = new GlobalInstanceManager(this);
        this.loadBalancer = new LoadBalancer(stateLayer, gimSystem);
        this.threadPoolManager = new ThreadPoolManager();
        
        // Initialize MongoDB schema and DataMutex service
        this.mongoDBSchema = new MongoDBSchema();
        
        // Initialize Redis nodes for DataMutex (minimum 3 for Redlock)
        Set<DataMutexService.RedisNode> redisNodes = new HashSet<>();
        redisNodes.add(new DataMutexService.RedisNode("localhost", 7000));
        redisNodes.add(new DataMutexService.RedisNode("localhost", 7001));
        redisNodes.add(new DataMutexService.RedisNode("localhost", 7002));
        this.dataMutexService = new DataMutexService(redisNodes);
        
        // Initialize thread pools
        this.gameTickExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            r -> new Thread(r, "GameTick-Thread")
        );
        
        this.ioExecutor = Executors.newFixedThreadPool(
            4,
            r -> new Thread(r, "IO-Thread")
        );
        
        this.worldGenerationExecutor = Executors.newFixedThreadPool(
            2,
            r -> new Thread(r, "WorldGen-Thread")
        );
        
        this.scheduler = Executors.newScheduledThreadPool(
            2,
            r -> new Thread(r, "Scheduler-Thread")
        );
        
        // Initialize Minestom server
        this.server = MinecraftServer.init();
        this.instanceManager = server.getInstanceManager();
        
        setupEventListeners();
    }
    
    /**
     * Initialize the distributed engine
     */
    public CompletableFuture<Void> initialize() {
        if (initialized) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Initializing Distributed Engine...");
        
        return stateLayer.initialize()
            .thenCompose(v -> gimSystem.initialize())
            .thenCompose(v -> loadBalancer.initialize())
            .thenCompose(v -> threadPoolManager.initialize())
            .thenCompose(v -> mongoDBSchema.initialize("mongodb://localhost:27017"))
            .thenRun(() -> {
                initialized = true;
                logger.info("Distributed Engine initialized successfully");
            })
            .exceptionally(throwable -> {
                logger.severe("Failed to initialize Distributed Engine: " + throwable.getMessage());
                throw new RuntimeException("Engine initialization failed", throwable);
            });
    }
    
    /**
     * Start the distributed engine
     */
    public CompletableFuture<Void> start(String address, int port) {
        if (!initialized) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("Engine must be initialized before starting")
            );
        }
        
        if (running) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Starting Distributed Engine on " + address + ":" + port);
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Start Minestom server
                server.start(address, port);
                
                // Start distributed components
                startDistributedComponents();
                
                running = true;
                logger.info("Distributed Engine started successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to start Distributed Engine: " + e.getMessage());
                throw new RuntimeException("Engine start failed", e);
            }
        }, ioExecutor);
    }
    
    /**
     * Stop the distributed engine
     */
    public CompletableFuture<Void> stop() {
        if (!running) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Stopping Distributed Engine...");
        
        return CompletableFuture.runAsync(() -> {
            try {
                running = false;
                
                // Stop distributed components
                stopDistributedComponents();
                
                // Stop Minestom server
                server.stop();
                
                // Shutdown thread pools
                shutdownThreadPools();
                
                logger.info("Distributed Engine stopped successfully");
                
            } catch (Exception e) {
                logger.severe("Error stopping Distributed Engine: " + e.getMessage());
            }
        }, ioExecutor);
    }
    
    /**
     * Create a new game instance with proper isolation
     */
    public CompletableFuture<Instance> createGameInstance(String instanceId, String instanceType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Create instance using GIM system
                Instance instance = gimSystem.createInstance(instanceId, instanceType);
                
                // Register instance with load balancer
                loadBalancer.registerInstance(instance, instanceType);
                
                logger.info("Created game instance: " + instanceId + " of type: " + instanceType);
                return instance;
                
            } catch (Exception e) {
                logger.severe("Failed to create game instance: " + e.getMessage());
                throw new RuntimeException("Instance creation failed", e);
            }
        }, worldGenerationExecutor);
    }
    
    /**
     * Route player to appropriate instance based on progression
     */
    public CompletableFuture<Instance> routePlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Get player progression data from state layer
                PlayerProgression progression = stateLayer.getPlayerProgression(player.getUuid());
                
                // Route player using load balancer
                Instance targetInstance = loadBalancer.routePlayer(player, progression);
                
                logger.info("Routed player " + player.getUsername() + " to instance: " + 
                    targetInstance.getUniqueId());
                
                return targetInstance;
                
            } catch (Exception e) {
                logger.severe("Failed to route player: " + e.getMessage());
                throw new RuntimeException("Player routing failed", e);
            }
        }, ioExecutor);
    }
    
    /**
     * Execute game tick logic asynchronously
     */
    public void executeGameTick(Runnable tickLogic) {
        if (!running) return;
        
        gameTickExecutor.execute(() -> {
            try {
                tickLogic.run();
            } catch (Exception e) {
                logger.severe("Error in game tick: " + e.getMessage());
            }
        });
    }
    
    /**
     * Execute I/O operation asynchronously
     */
    public <T> CompletableFuture<T> executeIOOperation(java.util.function.Supplier<T> ioOperation) {
        return CompletableFuture.supplyAsync(ioOperation, ioExecutor);
    }
    
    /**
     * Schedule a task to run periodically
     */
    public void scheduleTask(Runnable task, long delay, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(task, delay, period, unit);
    }
    
    /**
     * Get the current server instance
     */
    public MinecraftServer getServer() {
        return server;
    }
    
    /**
     * Get the instance manager
     */
    public InstanceManager getInstanceManager() {
        return instanceManager;
    }
    
    /**
     * Get the state synchronization layer
     */
    public StateSynchronizationLayer getStateLayer() {
        return stateLayer;
    }
    
    /**
     * Get the global instance manager
     */
    public GlobalInstanceManager getGimSystem() {
        return gimSystem;
    }
    
    /**
     * Get the load balancer
     */
    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }
    
    /**
     * Get the MongoDB schema
     */
    public MongoDBSchema getMongoDBSchema() {
        return mongoDBSchema;
    }
    
    /**
     * Get the DataMutex service
     */
    public DataMutexService getDataMutexService() {
        return dataMutexService;
    }
    
    /**
     * Check if the engine is running
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Setup event listeners for distributed operations
     */
    private void setupEventListeners() {
        server.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            
            // Route player asynchronously
            routePlayer(player).thenAccept(instance -> {
                // Set player spawn position
                player.setInstance(instance, new Pos(0, 100, 0));
            }).exceptionally(throwable -> {
                logger.severe("Failed to route player on login: " + throwable.getMessage());
                return null;
            });
        });
        
        server.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();
            
            // Clean up player state asynchronously
            executeIOOperation(() -> {
                stateLayer.cleanupPlayerState(player.getUuid());
                return null;
            });
        });
        
        // Game tick monitoring
        server.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, event -> {
            if (event.getTickTime() > 50) { // 50ms = 1 tick
                logger.warning("Tick lag detected: " + event.getTickTime() + "ms");
            }
        });
    }
    
    /**
     * Start distributed components
     */
    private void startDistributedComponents() {
        // Start state synchronization
        stateLayer.start();
        
        // Start GIM system
        gimSystem.start();
        
        // Start load balancer
        loadBalancer.start();
        
        // Start thread pool manager
        threadPoolManager.start();
    }
    
    /**
     * Stop distributed components
     */
    private void stopDistributedComponents() {
        // Stop components in reverse order
        threadPoolManager.stop();
        loadBalancer.stop();
        gimSystem.stop();
        stateLayer.stop();
    }
    
    /**
     * Shutdown all thread pools
     */
    private void shutdownThreadPools() {
        gameTickExecutor.shutdown();
        ioExecutor.shutdown();
        worldGenerationExecutor.shutdown();
        scheduler.shutdown();
        
        try {
            if (!gameTickExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                gameTickExecutor.shutdownNow();
            }
            if (!ioExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                ioExecutor.shutdownNow();
            }
            if (!worldGenerationExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                worldGenerationExecutor.shutdownNow();
            }
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Player progression data structure
     */
    public static class PlayerProgression {
        private final int combatLevel;
        private final int miningLevel;
        private final int farmingLevel;
        private final int foragingLevel;
        private final int fishingLevel;
        private final int enchantingLevel;
        private final int alchemyLevel;
        private final int tamingLevel;
        private final int carpentryLevel;
        private final int runecraftingLevel;
        
        public PlayerProgression(int combatLevel, int miningLevel, int farmingLevel, 
                               int foragingLevel, int fishingLevel, int enchantingLevel,
                               int alchemyLevel, int tamingLevel, int carpentryLevel, 
                               int runecraftingLevel) {
            this.combatLevel = combatLevel;
            this.miningLevel = miningLevel;
            this.farmingLevel = farmingLevel;
            this.foragingLevel = foragingLevel;
            this.fishingLevel = fishingLevel;
            this.enchantingLevel = enchantingLevel;
            this.alchemyLevel = alchemyLevel;
            this.tamingLevel = tamingLevel;
            this.carpentryLevel = carpentryLevel;
            this.runecraftingLevel = runecraftingLevel;
        }
        
        // Getters
        public int getCombatLevel() { return combatLevel; }
        public int getMiningLevel() { return miningLevel; }
        public int getFarmingLevel() { return farmingLevel; }
        public int getForagingLevel() { return foragingLevel; }
        public int getFishingLevel() { return fishingLevel; }
        public int getEnchantingLevel() { return enchantingLevel; }
        public int getAlchemyLevel() { return alchemyLevel; }
        public int getTamingLevel() { return tamingLevel; }
        public int getCarpentryLevel() { return carpentryLevel; }
        public int getRunecraftingLevel() { return runecraftingLevel; }
        
        /**
         * Get the highest skill level for general progression
         */
        public int getHighestSkillLevel() {
            return Math.max(Math.max(Math.max(combatLevel, miningLevel), 
                Math.max(farmingLevel, foragingLevel)), 
                Math.max(Math.max(fishingLevel, enchantingLevel), 
                Math.max(Math.max(alchemyLevel, tamingLevel), 
                Math.max(carpentryLevel, runecraftingLevel))));
        }
        
        /**
         * Check if player qualifies for endgame content
         */
        public boolean isEndgamePlayer() {
            return getHighestSkillLevel() >= 50;
        }
        
        /**
         * Check if player is beginner
         */
        public boolean isBeginner() {
            return getHighestSkillLevel() < 10;
        }
    }
}
