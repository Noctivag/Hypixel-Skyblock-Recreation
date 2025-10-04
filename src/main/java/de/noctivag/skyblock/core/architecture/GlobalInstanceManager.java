package de.noctivag.skyblock.core.architecture;

import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;

import de.noctivag.skyblock.engine.DistributedEngine;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Global Instance Manager (GIM) - Dynamic instancing and process isolation system
 * 
 * This system provides:
 * - Instance categorization and process isolation
 * - Dynamic auto-scaling based on load
 * - Temporary private instance management
 * - Persistent public instance management
 * - Resource optimization and cleanup
 * 
 * Instance Types:
 * - Persistent Public: Hubs, auction houses (always available)
 * - Temporary Private: Player islands, dungeons, private lobbies
 * - Combat Zones: Combat areas with progression-based scaling
 * - Mining Zones: Mining areas with resource management
 * - Special Events: Limited-time instances for events
 */
public class GlobalInstanceManager {
    
    private static final Logger logger = Logger.getLogger(GlobalInstanceManager.class.getName());
    
    // Instance type definitions
    public enum InstanceType {
        PERSISTENT_PUBLIC,    // Always running, shared instances
        TEMPORARY_PRIVATE,    // Player-specific, auto-cleanup
        COMBAT_ZONE,         // Combat areas with scaling
        MINING_ZONE,         // Mining areas with resource management
        SPECIAL_EVENT,       // Limited-time event instances
        DUNGEON_INSTANCE,    // Dungeon runs with isolation
        GUILD_ISLAND,        // Guild-specific instances
        PLAYER_ISLAND        // Individual player islands
    }
    
    // Instance categories for load balancing
    public enum InstanceCategory {
        BEGINNER,           // Levels 1-10
        INTERMEDIATE,       // Levels 11-30
        ADVANCED,          // Levels 31-50
        ENDGAME,           // Levels 51+
        SPECIAL            // Special content
    }
    
    private final DistributedEngine engine;
    private final InstanceManager instanceManager;
    private final StateSynchronizationLayer stateLayer;
    
    // Instance management
    private final Map<String, ManagedInstance> managedInstances = new ConcurrentHashMap<>();
    private final Map<InstanceType, Set<String>> instancesByType = new ConcurrentHashMap<>();
    private final Map<InstanceCategory, Set<String>> instancesByCategory = new ConcurrentHashMap<>();
    
    // Scaling configuration
    private final Map<InstanceType, ScalingConfig> scalingConfigs = new ConcurrentHashMap<>();
    private final Map<InstanceCategory, ScalingConfig> categoryScalingConfigs = new ConcurrentHashMap<>();
    
    // Thread pools
    private final ExecutorService instanceExecutor;
    private final ScheduledExecutorService scalingExecutor;
    private final ScheduledExecutorService cleanupExecutor;
    
    // Metrics and monitoring
    private final Map<String, InstanceMetrics> instanceMetrics = new ConcurrentHashMap<>();
    private final AtomicInteger totalInstances = new AtomicInteger(0);
    private final AtomicInteger activeInstances = new AtomicInteger(0);
    
    // State
    private volatile boolean initialized = false;
    private volatile boolean running = false;
    
    public GlobalInstanceManager(DistributedEngine engine) {
        this.engine = engine;
        this.instanceManager = engine.getInstanceManager();
        this.stateLayer = engine.getStateLayer();
        
        // Initialize thread pools
        this.instanceExecutor = Executors.newFixedThreadPool(8, r -> new Thread(r, "InstanceManager-Thread"));
        this.scalingExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Scaling-Thread"));
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Cleanup-Thread"));
        
        initializeScalingConfigs();
        initializeTypeMaps();
    }
    
    /**
     * Initialize the GIM system
     */
    public CompletableFuture<Void> initialize() {
        if (initialized) {
            return CompletableFuture.completedFuture(null);
        }
        
        logger.info("Initializing Global Instance Manager...");
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Create default persistent instances
                createDefaultPersistentInstances();
                
                // Start scaling and cleanup tasks
                startScalingTasks();
                startCleanupTasks();
                
                initialized = true;
                logger.info("Global Instance Manager initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize Global Instance Manager: " + e.getMessage());
                throw new RuntimeException("GIM initialization failed", e);
            }
        }, instanceExecutor);
    }
    
    /**
     * Start the GIM system
     */
    public void start() {
        if (!initialized) {
            throw new IllegalStateException("GIM must be initialized before starting");
        }
        
        running = true;
        logger.info("Global Instance Manager started");
    }
    
    /**
     * Stop the GIM system
     */
    public void stop() {
        running = false;
        
        // Stop all managed instances
        CompletableFuture.allOf(
            managedInstances.values().stream()
                .map(this::stopInstance)
                .toArray(CompletableFuture[]::new)
        ).join();
        
        // Shutdown thread pools
        instanceExecutor.shutdown();
        scalingExecutor.shutdown();
        cleanupExecutor.shutdown();
        
        logger.info("Global Instance Manager stopped");
    }
    
    /**
     * Create a new game instance
     */
    public Instance createInstance(String instanceId, String instanceTypeStr) {
        try {
            InstanceType type = InstanceType.valueOf(instanceTypeStr.toUpperCase());
            return createInstance(instanceId, type, InstanceCategory.BEGINNER);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid instance type: " + instanceTypeStr + ", defaulting to TEMPORARY_PRIVATE");
            return createInstance(instanceId, InstanceType.TEMPORARY_PRIVATE, InstanceCategory.BEGINNER);
        }
    }
    
    /**
     * Create a new game instance with specific type and category
     */
    public Instance createInstance(String instanceId, InstanceType type, InstanceCategory category) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Check if instance already exists
                if (managedInstances.containsKey(instanceId)) {
                    logger.warning("Instance " + instanceId + " already exists");
                    return managedInstances.get(instanceId).getInstance();
                }
                
                // Create Minestom instance
                InstanceContainer instance = instanceManager.createInstanceContainer(DimensionType.OVERWORLD);
                
                // Create managed instance wrapper
                ManagedInstance managedInstance = new ManagedInstance(
                    instance, instanceId, type, category, System.currentTimeMillis()
                );
                
                // Register instance
                managedInstances.put(instanceId, managedInstance);
                instancesByType.computeIfAbsent(type, k -> ConcurrentHashMap.newKeySet()).add(instanceId);
                instancesByCategory.computeIfAbsent(category, k -> ConcurrentHashMap.newKeySet()).add(instanceId);
                
                // Initialize metrics
                instanceMetrics.put(instanceId, new InstanceMetrics());
                
                // Update counters
                totalInstances.incrementAndGet();
                activeInstances.incrementAndGet();
                
                // Apply instance-specific configuration
                configureInstance(managedInstance);
                
                logger.info("Created instance: " + instanceId + " (type: " + type + ", category: " + category + ")");
                
                return instance;
                
            } catch (Exception e) {
                logger.severe("Failed to create instance: " + e.getMessage());
                throw new RuntimeException("Instance creation failed", e);
            }
        }, instanceExecutor).join();
    }
    
    /**
     * Get instance by ID
     */
    public Optional<ManagedInstance> getInstance(String instanceId) {
        return Optional.ofNullable(managedInstances.get(instanceId));
    }
    
    /**
     * Get instances by type
     */
    public Set<String> getInstancesByType(InstanceType type) {
        return instancesByType.getOrDefault(type, Collections.emptySet());
    }
    
    /**
     * Get instances by category
     */
    public Set<String> getInstancesByCategory(InstanceCategory category) {
        return instancesByCategory.getOrDefault(category, Collections.emptySet());
    }
    
    /**
     * Get best available instance for player
     */
    public CompletableFuture<Instance> getBestInstanceForPlayer(PlayerProgression progression, InstanceType preferredType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InstanceCategory category = determineCategoryForProgression(progression);
                
                // Get available instances for this category and type
                Set<String> candidateInstances = new HashSet<>();
                
                if (preferredType != null) {
                    Set<String> typeInstances = getInstancesByType(preferredType);
                    Set<String> categoryInstances = getInstancesByCategory(category);
                    
                    // Find intersection
                    for (String instanceId : typeInstances) {
                        if (categoryInstances.contains(instanceId)) {
                            candidateInstances.add(instanceId);
                        }
                    }
                } else {
                    candidateInstances.addAll(getInstancesByCategory(category));
                }
                
                // Find instance with lowest load
                String bestInstanceId = null;
                double lowestLoad = Double.MAX_VALUE;
                
                for (String instanceId : candidateInstances) {
                    ManagedInstance instance = managedInstances.get(instanceId);
                    if (instance != null && instance.isActive()) {
                        InstanceMetrics metrics = instanceMetrics.get(instanceId);
                        double load = calculateInstanceLoad(metrics, instance);
                        
                        if (load < lowestLoad) {
                            lowestLoad = load;
                            bestInstanceId = instanceId;
                        }
                    }
                }
                
                // If no suitable instance found, create a new one
                if (bestInstanceId == null) {
                    String newInstanceId = generateInstanceId(preferredType != null ? preferredType : InstanceType.TEMPORARY_PRIVATE, category);
                    return createInstance(newInstanceId, preferredType != null ? preferredType : InstanceType.TEMPORARY_PRIVATE, category);
                }
                
                ManagedInstance bestInstance = managedInstances.get(bestInstanceId);
                return bestInstance.getInstance();
                
            } catch (Exception e) {
                logger.severe("Error finding best instance for player: " + e.getMessage());
                throw new RuntimeException("Instance selection failed", e);
            }
        }, instanceExecutor);
    }
    
    /**
     * Stop and remove an instance
     */
    public CompletableFuture<Void> stopInstance(String instanceId) {
        ManagedInstance instance = managedInstances.get(instanceId);
        if (instance == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        return stopInstance(instance);
    }
    
    /**
     * Stop and remove a managed instance
     */
    private CompletableFuture<Void> stopInstance(ManagedInstance managedInstance) {
        return CompletableFuture.runAsync(() -> {
            try {
                String instanceId = managedInstance.getInstanceId();
                
                // Remove from tracking
                managedInstances.remove(instanceId);
                instancesByType.values().forEach(instances -> instances.remove(instanceId));
                instancesByCategory.values().forEach(instances -> instances.remove(instanceId));
                instanceMetrics.remove(instanceId);
                
                // Update counters
                activeInstances.decrementAndGet();
                
                logger.info("Stopped and removed instance: " + instanceId);
                
            } catch (Exception e) {
                logger.severe("Error stopping instance: " + e.getMessage());
            }
        }, instanceExecutor);
    }
    
    /**
     * Update instance metrics
     */
    public void updateInstanceMetrics(String instanceId, int playerCount, double tps, double memoryUsage) {
        InstanceMetrics metrics = instanceMetrics.get(instanceId);
        if (metrics != null) {
            metrics.update(playerCount, tps, memoryUsage);
        }
    }
    
    /**
     * Get instance metrics
     */
    public Optional<InstanceMetrics> getInstanceMetrics(String instanceId) {
        return Optional.ofNullable(instanceMetrics.get(instanceId));
    }
    
    /**
     * Get all instance metrics
     */
    public Map<String, InstanceMetrics> getAllInstanceMetrics() {
        return new HashMap<>(instanceMetrics);
    }
    
    /**
     * Get total instance count
     */
    public int getTotalInstanceCount() {
        return totalInstances.get();
    }
    
    /**
     * Get active instance count
     */
    public int getActiveInstanceCount() {
        return activeInstances.get();
    }
    
    /**
     * Initialize scaling configurations
     */
    private void initializeScalingConfigs() {
        // Persistent public instances - always keep at least 2 running
        scalingConfigs.put(InstanceType.PERSISTENT_PUBLIC, new ScalingConfig(2, 10, 0.8, 0.3));
        
        // Temporary private instances - scale based on demand
        scalingConfigs.put(InstanceType.TEMPORARY_PRIVATE, new ScalingConfig(0, 100, 0.9, 0.1));
        
        // Combat zones - scale aggressively for load balancing
        scalingConfigs.put(InstanceType.COMBAT_ZONE, new ScalingConfig(1, 50, 0.7, 0.2));
        
        // Mining zones - moderate scaling
        scalingConfigs.put(InstanceType.MINING_ZONE, new ScalingConfig(1, 30, 0.8, 0.2));
        
        // Special events - limited scaling
        scalingConfigs.put(InstanceType.SPECIAL_EVENT, new ScalingConfig(0, 20, 0.9, 0.1));
        
        // Category-based scaling
        categoryScalingConfigs.put(InstanceCategory.BEGINNER, new ScalingConfig(2, 50, 0.8, 0.2));
        categoryScalingConfigs.put(InstanceCategory.INTERMEDIATE, new ScalingConfig(1, 30, 0.8, 0.3));
        categoryScalingConfigs.put(InstanceCategory.ADVANCED, new ScalingConfig(1, 20, 0.9, 0.3));
        categoryScalingConfigs.put(InstanceCategory.ENDGAME, new ScalingConfig(1, 10, 0.9, 0.4));
        categoryScalingConfigs.put(InstanceCategory.SPECIAL, new ScalingConfig(0, 5, 0.95, 0.1));
    }
    
    /**
     * Initialize type maps
     */
    private void initializeTypeMaps() {
        for (InstanceType type : InstanceType.values()) {
            instancesByType.put(type, ConcurrentHashMap.newKeySet());
        }
        
        for (InstanceCategory category : InstanceCategory.values()) {
            instancesByCategory.put(category, ConcurrentHashMap.newKeySet());
        }
    }
    
    /**
     * Create default persistent instances
     */
    private void createDefaultPersistentInstances() {
        // Create hub instance
        createInstance("hub-main", InstanceType.PERSISTENT_PUBLIC, InstanceCategory.BEGINNER);
        
        // Create auction house instance
        createInstance("auction-house", InstanceType.PERSISTENT_PUBLIC, InstanceCategory.BEGINNER);
        
        // Create beginner combat zone
        createInstance("combat-beginner", InstanceType.COMBAT_ZONE, InstanceCategory.BEGINNER);
        
        // Create beginner mining zone
        createInstance("mining-beginner", InstanceType.MINING_ZONE, InstanceCategory.BEGINNER);
    }
    
    /**
     * Start scaling tasks
     */
    private void startScalingTasks() {
        scalingExecutor.scheduleAtFixedRate(() -> {
            try {
                performAutoScaling();
            } catch (Exception e) {
                logger.severe("Error during auto-scaling: " + e.getMessage());
            }
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * Start cleanup tasks
     */
    private void startCleanupTasks() {
        cleanupExecutor.scheduleAtFixedRate(() -> {
            try {
                cleanupInactiveInstances();
            } catch (Exception e) {
                logger.severe("Error during cleanup: " + e.getMessage());
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
    
    /**
     * Perform automatic scaling based on load
     */
    private void performAutoScaling() {
        for (Map.Entry<InstanceType, ScalingConfig> entry : scalingConfigs.entrySet()) {
            InstanceType type = entry.getKey();
            ScalingConfig config = entry.getValue();
            
            Set<String> instances = getInstancesByType(type);
            int activeCount = (int) instances.stream()
                .map(managedInstances::get)
                .filter(Objects::nonNull)
                .filter(ManagedInstance::isActive)
                .count();
            
            // Calculate average load
            double averageLoad = instances.stream()
                .map(instanceMetrics::get)
                .filter(Objects::nonNull)
                .mapToDouble(InstanceMetrics::getAverageLoad)
                .average()
                .orElse(0.0);
            
            // Scale up if needed
            if (averageLoad > config.getScaleUpThreshold() && activeCount < config.getMaxInstances()) {
                createInstance(generateInstanceId(type, InstanceCategory.BEGINNER), type, InstanceCategory.BEGINNER);
                logger.info("Scaled up " + type + " instances due to high load: " + averageLoad);
            }
            
            // Scale down if possible
            if (averageLoad < config.getScaleDownThreshold() && activeCount > config.getMinInstances()) {
                // Find least loaded instance to remove
                String leastLoadedInstance = instances.stream()
                    .min(Comparator.comparingDouble(id -> 
                        instanceMetrics.getOrDefault(id, new InstanceMetrics()).getAverageLoad()))
                    .orElse(null);
                
                if (leastLoadedInstance != null) {
                    stopInstance(leastLoadedInstance);
                    logger.info("Scaled down " + type + " instances due to low load: " + averageLoad);
                }
            }
        }
    }
    
    /**
     * Cleanup inactive instances
     */
    private void cleanupInactiveInstances() {
        long currentTime = System.currentTimeMillis();
        long cleanupThreshold = 300000; // 5 minutes
        
        List<String> toRemove = new ArrayList<>();
        
        for (Map.Entry<String, ManagedInstance> entry : managedInstances.entrySet()) {
            ManagedInstance instance = entry.getValue();
            
            // Cleanup temporary instances that have been inactive
            if (instance.getType() == InstanceType.TEMPORARY_PRIVATE ||
                instance.getType() == InstanceType.PLAYER_ISLAND ||
                instance.getType() == InstanceType.GUILD_ISLAND) {
                
                InstanceMetrics metrics = instanceMetrics.get(instance.getInstanceId());
                if (metrics != null && metrics.getLastActivityTime() < currentTime - cleanupThreshold) {
                    toRemove.add(instance.getInstanceId());
                }
            }
        }
        
        for (String instanceId : toRemove) {
            stopInstance(instanceId);
            logger.info("Cleaned up inactive instance: " + instanceId);
        }
    }
    
    /**
     * Configure instance based on type and category
     */
    private void configureInstance(ManagedInstance instance) {
        // Apply instance-specific configuration
        switch (instance.getType()) {
            case COMBAT_ZONE:
                configureCombatZone(instance);
                break;
            case MINING_ZONE:
                configureMiningZone(instance);
                break;
            case DUNGEON_INSTANCE:
                configureDungeonInstance(instance);
                break;
            case PLAYER_ISLAND:
                configurePlayerIsland(instance);
                break;
            case GUILD_ISLAND:
                configureGuildIsland(instance);
                break;
        }
    }
    
    /**
     * Configure combat zone instance
     */
    private void configureCombatZone(ManagedInstance instance) {
        // Set up combat-specific configurations
        // This would include mob spawning rules, difficulty settings, etc.
        logger.fine("Configured combat zone: " + instance.getInstanceId());
    }
    
    /**
     * Configure mining zone instance
     */
    private void configureMiningZone(ManagedInstance instance) {
        // Set up mining-specific configurations
        // This would include ore generation, mining rates, etc.
        logger.fine("Configured mining zone: " + instance.getInstanceId());
    }
    
    /**
     * Configure dungeon instance
     */
    private void configureDungeonInstance(ManagedInstance instance) {
        // Set up dungeon-specific configurations
        // This would include dungeon layout, mob spawning, etc.
        logger.fine("Configured dungeon instance: " + instance.getInstanceId());
    }
    
    /**
     * Configure player island instance
     */
    private void configurePlayerIsland(ManagedInstance instance) {
        // Set up player island configurations
        // This would include island generation, permissions, etc.
        logger.fine("Configured player island: " + instance.getInstanceId());
    }
    
    /**
     * Configure guild island instance
     */
    private void configureGuildIsland(ManagedInstance instance) {
        // Set up guild island configurations
        // This would include guild-specific features, etc.
        logger.fine("Configured guild island: " + instance.getInstanceId());
    }
    
    /**
     * Determine category for player progression
     */
    private InstanceCategory determineCategoryForProgression(StateSynchronizationLayer.PlayerProgression progression) {
        int highestLevel = progression.getHighestSkillLevel();
        
        if (highestLevel < 10) return InstanceCategory.BEGINNER;
        if (highestLevel < 30) return InstanceCategory.INTERMEDIATE;
        if (highestLevel < 50) return InstanceCategory.ADVANCED;
        return InstanceCategory.ENDGAME;
    }
    
    /**
     * Calculate instance load
     */
    private double calculateInstanceLoad(InstanceMetrics metrics, ManagedInstance instance) {
        if (metrics == null) return 0.0;
        
        double playerLoad = (double) metrics.getPlayerCount() / 20.0; // Normalize to 20 players
        double tpsLoad = Math.max(0, (20.0 - metrics.getTps()) / 20.0); // TPS-based load
        double memoryLoad = metrics.getMemoryUsage() / 100.0; // Memory usage percentage
        
        return (playerLoad + tpsLoad + memoryLoad) / 3.0;
    }
    
    /**
     * Generate unique instance ID
     */
    private String generateInstanceId(InstanceType type, InstanceCategory category) {
        return type.name().toLowerCase() + "-" + category.name().toLowerCase() + "-" + System.currentTimeMillis();
    }
    
    /**
     * Managed instance wrapper
     */
    public static class ManagedInstance {
        private final Instance instance;
        private final String instanceId;
        private final InstanceType type;
        private final InstanceCategory category;
        private final long creationTime;
        private volatile boolean active = true;
        
        public ManagedInstance(Instance instance, String instanceId, InstanceType type, 
                             InstanceCategory category, long creationTime) {
            this.instance = instance;
            this.instanceId = instanceId;
            this.type = type;
            this.category = category;
            this.creationTime = creationTime;
        }
        
        public Instance getInstance() { return instance; }
        public String getInstanceId() { return instanceId; }
        public InstanceType getType() { return type; }
        public InstanceCategory getCategory() { return category; }
        public long getCreationTime() { return creationTime; }
        public boolean isActive() { return active; }
        
        public void setActive(boolean active) { this.active = active; }
    }
    
    /**
     * Scaling configuration
     */
    public static class ScalingConfig {
        private final int minInstances;
        private final int maxInstances;
        private final double scaleUpThreshold;
        private final double scaleDownThreshold;
        
        public ScalingConfig(int minInstances, int maxInstances, double scaleUpThreshold, double scaleDownThreshold) {
            this.minInstances = minInstances;
            this.maxInstances = maxInstances;
            this.scaleUpThreshold = scaleUpThreshold;
            this.scaleDownThreshold = scaleDownThreshold;
        }
        
        public int getMinInstances() { return minInstances; }
        public int getMaxInstances() { return maxInstances; }
        public double getScaleUpThreshold() { return scaleUpThreshold; }
        public double getScaleDownThreshold() { return scaleDownThreshold; }
    }
    
    /**
     * Instance metrics
     */
    public static class InstanceMetrics {
        private volatile int playerCount = 0;
        private volatile double tps = 20.0;
        private volatile double memoryUsage = 0.0;
        private volatile long lastActivityTime = System.currentTimeMillis();
        private volatile double averageLoad = 0.0;
        
        public void update(int playerCount, double tps, double memoryUsage) {
            this.playerCount = playerCount;
            this.tps = tps;
            this.memoryUsage = memoryUsage;
            this.lastActivityTime = System.currentTimeMillis();
            
            // Calculate average load
            this.averageLoad = (playerCount / 20.0 + Math.max(0, (20.0 - tps) / 20.0) + memoryUsage / 100.0) / 3.0;
        }
        
        public int getPlayerCount() { return playerCount; }
        public double getTps() { return tps; }
        public double getMemoryUsage() { return memoryUsage; }
        public long getLastActivityTime() { return lastActivityTime; }
        public double getAverageLoad() { return averageLoad; }
    }
}
