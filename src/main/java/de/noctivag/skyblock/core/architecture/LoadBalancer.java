package de.noctivag.skyblock.core.architecture;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import de.noctivag.skyblock.engine.DistributedEngine;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Intelligent Load Balancer - Progression-based routing system
 * 
 * This system provides:
 * - Intelligent player routing based on progression
 * - Skill-based zone assignment
 * - Dynamic load balancing across instances
 * - Progression-aware instance selection
 * - Anti-camping mechanisms for beginners
 * 
 * Routing Strategy:
 * - Analyze player progression from central state
 * - Route to appropriate skill-level instances
 * - Balance load across available instances
 * - Prevent endgame players from dominating beginner zones
 */
public class LoadBalancer {
    
    private static final Logger logger = Logger.getLogger(LoadBalancer.class.getName());
    
    // Load balancing configuration
    private static final int MAX_PLAYERS_PER_INSTANCE = 20;
    private static final double LOAD_THRESHOLD_HIGH = 0.8;
    private static final double LOAD_THRESHOLD_LOW = 0.3;
    private static final int PROGRESSION_CHECK_INTERVAL = 30; // seconds
    
    private final StateSynchronizationLayer stateLayer;
    private final GlobalInstanceManager gimSystem;
    
    // Load balancing state
    private final Map<String, InstanceLoad> instanceLoads = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerRoutingInfo> playerRouting = new ConcurrentHashMap<>();
    private final Map<String, InstanceRoutingConfig> routingConfigs = new ConcurrentHashMap<>();
    
    // Thread pools
    private final ExecutorService routingExecutor;
    private final ScheduledExecutorService monitoringExecutor;
    
    // State
    private volatile boolean initialized = false;
    private volatile boolean running = false;
    
    public LoadBalancer(StateSynchronizationLayer stateLayer, GlobalInstanceManager gimSystem) {
        this.stateLayer = stateLayer;
        this.gimSystem = gimSystem;
        
        // Initialize thread pools
        this.routingExecutor = Executors.newFixedThreadPool(4, r -> new Thread(r, "LoadBalancer-Thread"));
        this.monitoringExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "LoadMonitor-Thread"));
        
        initializeRoutingConfigs();
    }
    
    /**
     * Initialize the load balancer
     */
    public CompletableFuture<Void> initialize() {
        if (initialized) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Initializing Load Balancer...");
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Start monitoring tasks
                startLoadMonitoring();
                startProgressionMonitoring();
                
                initialized = true;
                logger.info("Load Balancer initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize Load Balancer: " + e.getMessage());
                throw new RuntimeException("Load balancer initialization failed", e);
            }
        }, routingExecutor);
    }
    
    /**
     * Start the load balancer
     */
    public void start() {
        if (!initialized) {
            throw new IllegalStateException("Load balancer must be initialized before starting");
        }
        
        running = true;
        logger.info("Load Balancer started");
    }
    
    /**
     * Stop the load balancer
     */
    public void stop() {
        running = false;
        
        routingExecutor.shutdown();
        monitoringExecutor.shutdown();
        
        logger.info("Load Balancer stopped");
    }
    
    /**
     * Route player to appropriate instance
     */
    public Instance routePlayer(Player player, StateSynchronizationLayer.PlayerProgression progression) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID playerId = player.getUuid();
                
                // Get or create routing info
                PlayerRoutingInfo routingInfo = playerRouting.computeIfAbsent(playerId, 
                    k -> new PlayerRoutingInfo(player, progression));
                
                // Determine target instance type based on progression
                GlobalInstanceManager.InstanceType targetType = determineTargetInstanceType(progression);
                GlobalInstanceManager.InstanceCategory targetCategory = determineTargetCategory(progression);
                
                // Find best available instance
                Instance targetInstance = findBestInstanceForPlayer(progression, targetType, targetCategory);
                
                // Update routing info
                routingInfo.updateTargetInstance(targetInstance, System.currentTimeMillis());
                
                // Register player with instance load tracking
                registerPlayerWithInstance(player, targetInstance);
                
                logger.info("Routed player " + player.getUsername() + " to instance: " + 
                    targetInstance.getUniqueId() + " (type: " + targetType + ", category: " + targetCategory + ")");
                
                return targetInstance;
                
            } catch (Exception e) {
                logger.severe("Error routing player: " + e.getMessage());
                throw new RuntimeException("Player routing failed", e);
            }
        }, routingExecutor).join();
    }
    
    /**
     * Register instance with load balancer
     */
    public void registerInstance(Instance instance, String instanceType) {
        routingExecutor.execute(() -> {
            try {
                String instanceId = instance.getUniqueId().toString();
                
                // Create instance load tracking
                InstanceLoad load = new InstanceLoad(instanceId, instanceType);
                instanceLoads.put(instanceId, load);
                
                logger.info("Registered instance with load balancer: " + instanceId + " (type: " + instanceType + ")");
                
            } catch (Exception e) {
                logger.severe("Error registering instance: " + e.getMessage());
            }
        });
    }
    
    /**
     * Unregister instance from load balancer
     */
    public void unregisterInstance(Instance instance) {
        routingExecutor.execute(() -> {
            try {
                String instanceId = instance.getUniqueId().toString();
                instanceLoads.remove(instanceId);
                
                logger.info("Unregistered instance from load balancer: " + instanceId);
                
            } catch (Exception e) {
                logger.severe("Error unregistering instance: " + e.getMessage());
            }
        });
    }
    
    /**
     * Get instance load information
     */
    public Optional<InstanceLoad> getInstanceLoad(String instanceId) {
        return Optional.ofNullable(instanceLoads.get(instanceId));
    }
    
    /**
     * Get all instance loads
     */
    public Map<String, InstanceLoad> getAllInstanceLoads() {
        return new HashMap<>(instanceLoads);
    }
    
    /**
     * Get player routing information
     */
    public Optional<PlayerRoutingInfo> getPlayerRoutingInfo(UUID playerId) {
        return Optional.ofNullable(playerRouting.get(playerId));
    }
    
    /**
     * Remove player routing information
     */
    public void removePlayerRoutingInfo(UUID playerId) {
        PlayerRoutingInfo routingInfo = playerRouting.remove(playerId);
        if (routingInfo != null && routingInfo.getCurrentInstance() != null) {
            unregisterPlayerFromInstance(playerId, routingInfo.getCurrentInstance());
        }
    }
    
    /**
     * Determine target instance type based on progression
     */
    private GlobalInstanceManager.InstanceType determineTargetInstanceType(StateSynchronizationLayer.PlayerProgression progression) {
        // Analyze player's highest skills to determine preferred content type
        int combatLevel = progression.getCombatLevel();
        int miningLevel = progression.getMiningLevel();
        int farmingLevel = progression.getFarmingLevel();
        int foragingLevel = progression.getForagingLevel();
        int fishingLevel = progression.getFishingLevel();
        
        // Determine primary activity based on highest skill
        int maxSkill = Math.max(Math.max(Math.max(combatLevel, miningLevel), 
            Math.max(farmingLevel, foragingLevel)), fishingLevel);
        
        if (maxSkill == combatLevel && combatLevel >= 10) {
            return GlobalInstanceManager.InstanceType.COMBAT_ZONE;
        } else if (maxSkill == miningLevel && miningLevel >= 10) {
            return GlobalInstanceManager.InstanceType.MINING_ZONE;
        } else if (progression.isEndgamePlayer()) {
            // Endgame players can access all content types
            return GlobalInstanceManager.InstanceType.PERSISTENT_PUBLIC;
        } else {
            // Beginners start with general public instances
            return GlobalInstanceManager.InstanceType.PERSISTENT_PUBLIC;
        }
    }
    
    /**
     * Determine target category based on progression
     */
    private GlobalInstanceManager.InstanceCategory determineTargetCategory(StateSynchronizationLayer.PlayerProgression progression) {
        int highestLevel = progression.getHighestSkillLevel();
        
        if (highestLevel < 10) {
            return GlobalInstanceManager.InstanceCategory.BEGINNER;
        } else if (highestLevel < 30) {
            return GlobalInstanceManager.InstanceCategory.INTERMEDIATE;
        } else if (highestLevel < 50) {
            return GlobalInstanceManager.InstanceCategory.ADVANCED;
        } else {
            return GlobalInstanceManager.InstanceCategory.ENDGAME;
        }
    }
    
    /**
     * Find best instance for player
     */
    private Instance findBestInstanceForPlayer(StateSynchronizationLayer.PlayerProgression progression,
                                             GlobalInstanceManager.InstanceType preferredType,
                                             GlobalInstanceManager.InstanceCategory preferredCategory) {
        
        // Get candidate instances
        List<Instance> candidates = new ArrayList<>();
        
        // First, try to find instances matching both type and category
        Set<String> typeInstances = gimSystem.getInstancesByType(preferredType);
        Set<String> categoryInstances = gimSystem.getInstancesByCategory(preferredCategory);
        
        for (String instanceId : typeInstances) {
            if (categoryInstances.contains(instanceId)) {
                Optional<GlobalInstanceManager.ManagedInstance> managed = gimSystem.getInstance(instanceId);
                managed.ifPresent(mi -> candidates.add(mi.getInstance()));
            }
        }
        
        // If no candidates found, try category-only matches
        if (candidates.isEmpty()) {
            for (String instanceId : categoryInstances) {
                Optional<GlobalInstanceManager.ManagedInstance> managed = gimSystem.getInstance(instanceId);
                managed.ifPresent(mi -> candidates.add(mi.getInstance()));
            }
        }
        
        // If still no candidates, get any available instance
        if (candidates.isEmpty()) {
            for (String instanceId : typeInstances) {
                Optional<GlobalInstanceManager.ManagedInstance> managed = gimSystem.getInstance(instanceId);
                managed.ifPresent(mi -> candidates.add(mi.getInstance()));
            }
        }
        
        // Find best candidate based on load
        Instance bestInstance = null;
        double bestScore = Double.MAX_VALUE;
        
        for (Instance candidate : candidates) {
            String instanceId = candidate.getUniqueId().toString();
            InstanceLoad load = instanceLoads.get(instanceId);
            
            if (load != null && !load.isOverloaded()) {
                double score = calculateRoutingScore(candidate, progression, load);
                
                if (score < bestScore) {
                    bestScore = score;
                    bestInstance = candidate;
                }
            }
        }
        
        // If no suitable instance found, create a new one
        if (bestInstance == null) {
            logger.info("No suitable instance found, creating new one for progression level: " + 
                progression.getHighestSkillLevel());
            
            String newInstanceId = generateInstanceId(preferredType, preferredCategory);
            bestInstance = gimSystem.createInstance(newInstanceId, preferredType, preferredCategory);
        }
        
        return bestInstance;
    }
    
    /**
     * Calculate routing score for an instance
     */
    private double calculateRoutingScore(Object instance, StateSynchronizationLayer.PlayerProgression progression, InstanceLoad load) {
        String instanceId = instance.toString(); // Simplified for now
        
        // Base score from load
        double loadScore = load.getLoadPercentage();
        
        // Progression match bonus/penalty
        double progressionScore = calculateProgressionMatchScore(instance, progression);
        
        // Player count penalty for overcrowding
        double playerCountPenalty = (double) load.getPlayerCount() / MAX_PLAYERS_PER_INSTANCE;
        
        // Combine scores
        return loadScore + progressionScore + playerCountPenalty;
    }
    
    /**
     * Calculate progression match score
     */
    private double calculateProgressionMatchScore(Object instance, StateSynchronizationLayer.PlayerProgression progression) {
        // This would analyze the instance's configuration and player progression
        // to determine how well they match
        
        // For now, return a neutral score
        return 0.0;
    }
    
    /**
     * Register player with instance load tracking
     */
    private void registerPlayerWithInstance(Player player, Instance instance) {
        String instanceId = instance.getUniqueId().toString();
        InstanceLoad load = instanceLoads.get(instanceId);
        
        if (load != null) {
            load.addPlayer(player.getUuid());
        }
    }
    
    /**
     * Unregister player from instance load tracking
     */
    private void unregisterPlayerFromInstance(UUID playerId, Instance instance) {
        String instanceId = instance.getUniqueId().toString();
        InstanceLoad load = instanceLoads.get(instanceId);
        
        if (load != null) {
            load.removePlayer(playerId);
        }
    }
    
    /**
     * Generate instance ID
     */
    private String generateInstanceId(GlobalInstanceManager.InstanceType type, GlobalInstanceManager.InstanceCategory category) {
        return type.name().toLowerCase() + "-" + category.name().toLowerCase() + "-" + System.currentTimeMillis();
    }
    
    /**
     * Initialize routing configurations
     */
    private void initializeRoutingConfigs() {
        // Configure routing rules for different instance types
        routingConfigs.put("combat-zone", new InstanceRoutingConfig(0.7, 0.3, true));
        routingConfigs.put("mining-zone", new InstanceRoutingConfig(0.8, 0.2, true));
        routingConfigs.put("persistent-public", new InstanceRoutingConfig(0.9, 0.1, false));
        routingConfigs.put("temporary-private", new InstanceRoutingConfig(0.6, 0.4, true));
    }
    
    /**
     * Start load monitoring
     */
    private void startLoadMonitoring() {
        monitoringExecutor.scheduleAtFixedRate(() -> {
            try {
                updateInstanceLoads();
                performLoadBalancing();
            } catch (Exception e) {
                logger.severe("Error during load monitoring: " + e.getMessage());
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
    
    /**
     * Start progression monitoring
     */
    private void startProgressionMonitoring() {
        monitoringExecutor.scheduleAtFixedRate(() -> {
            try {
                checkPlayerProgressionUpdates();
            } catch (Exception e) {
                logger.severe("Error during progression monitoring: " + e.getMessage());
            }
        }, PROGRESSION_CHECK_INTERVAL, PROGRESSION_CHECK_INTERVAL, TimeUnit.SECONDS);
    }
    
    /**
     * Update instance loads
     */
    private void updateInstanceLoads() {
        for (InstanceLoad load : instanceLoads.values()) {
            // Get current metrics from GIM
            gimSystem.getInstanceMetrics(load.getInstanceId()).ifPresent(metrics -> {
                load.updateMetrics(metrics.getPlayerCount(), metrics.getTps(), metrics.getMemoryUsage());
            });
        }
    }
    
    /**
     * Perform load balancing
     */
    private void performLoadBalancing() {
        // Identify overloaded instances
        List<InstanceLoad> overloadedInstances = new ArrayList<>();
        List<InstanceLoad> underloadedInstances = new ArrayList<>();
        
        for (InstanceLoad load : instanceLoads.values()) {
            if (load.getLoadPercentage() > LOAD_THRESHOLD_HIGH) {
                overloadedInstances.add(load);
            } else if (load.getLoadPercentage() < LOAD_THRESHOLD_LOW) {
                underloadedInstances.add(load);
            }
        }
        
        // Balance load if possible
        if (!overloadedInstances.isEmpty() && !underloadedInstances.isEmpty()) {
            logger.info("Performing load balancing: " + overloadedInstances.size() + " overloaded, " + 
                underloadedInstances.size() + " underloaded instances");
            
            // This would implement actual load balancing logic
            // For now, just log the situation
        }
    }
    
    /**
     * Check for player progression updates
     */
    private void checkPlayerProgressionUpdates() {
        for (PlayerRoutingInfo routingInfo : playerRouting.values()) {
            // Check if player progression has changed significantly
            // and re-route if necessary
            // This would be implemented with actual progression checking
        }
    }
    
    /**
     * Instance load tracking
     */
    public static class InstanceLoad {
        private final String instanceId;
        private final String instanceType;
        private final Set<UUID> players = ConcurrentHashMap.newKeySet();
        private volatile int playerCount = 0;
        private volatile double tps = 20.0;
        private volatile double memoryUsage = 0.0;
        private volatile double loadPercentage = 0.0;
        
        public InstanceLoad(String instanceId, String instanceType) {
            this.instanceId = instanceId;
            this.instanceType = instanceType;
        }
        
        public void addPlayer(UUID playerId) {
            players.add(playerId);
            updateLoadPercentage();
        }
        
        public void removePlayer(UUID playerId) {
            players.remove(playerId);
            updateLoadPercentage();
        }
        
        public void updateMetrics(int playerCount, double tps, double memoryUsage) {
            this.playerCount = playerCount;
            this.tps = tps;
            this.memoryUsage = memoryUsage;
            updateLoadPercentage();
        }
        
        private void updateLoadPercentage() {
            double playerLoad = (double) playerCount / MAX_PLAYERS_PER_INSTANCE;
            double tpsLoad = Math.max(0, (20.0 - tps) / 20.0);
            double memoryLoad = memoryUsage / 100.0;
            
            this.loadPercentage = (playerLoad + tpsLoad + memoryLoad) / 3.0;
        }
        
        public boolean isOverloaded() {
            return loadPercentage > LOAD_THRESHOLD_HIGH;
        }
        
        public boolean isUnderloaded() {
            return loadPercentage < LOAD_THRESHOLD_LOW;
        }
        
        // Getters
        public String getInstanceId() { return instanceId; }
        public String getInstanceType() { return instanceType; }
        public int getPlayerCount() { return playerCount; }
        public double getTps() { return tps; }
        public double getMemoryUsage() { return memoryUsage; }
        public double getLoadPercentage() { return loadPercentage; }
        public Set<UUID> getPlayers() { return new HashSet<>(players); }
    }
    
    /**
     * Player routing information
     */
    public static class PlayerRoutingInfo {
        private final Player player;
        private final StateSynchronizationLayer.PlayerProgression progression;
        private volatile Instance currentInstance;
        private volatile long lastRouteTime;
        
        public PlayerRoutingInfo(Player player, StateSynchronizationLayer.PlayerProgression progression) {
            this.player = player;
            this.progression = progression;
            this.lastRouteTime = System.currentTimeMillis();
        }
        
        public void updateTargetInstance(Instance instance, long routeTime) {
            this.currentInstance = instance;
            this.lastRouteTime = routeTime;
        }
        
        public Player getPlayer() { return player; }
        public StateSynchronizationLayer.PlayerProgression getProgression() { return progression; }
        public Instance getCurrentInstance() { return currentInstance; }
        public long getLastRouteTime() { return lastRouteTime; }
    }
    
    /**
     * Instance routing configuration
     */
    public static class InstanceRoutingConfig {
        private final double loadThreshold;
        private final double rebalanceThreshold;
        private final boolean allowAutoScaling;
        
        public InstanceRoutingConfig(double loadThreshold, double rebalanceThreshold, boolean allowAutoScaling) {
            this.loadThreshold = loadThreshold;
            this.rebalanceThreshold = rebalanceThreshold;
            this.allowAutoScaling = allowAutoScaling;
        }
        
        public double getLoadThreshold() { return loadThreshold; }
        public double getRebalanceThreshold() { return rebalanceThreshold; }
        public boolean isAllowAutoScaling() { return allowAutoScaling; }
    }
}
