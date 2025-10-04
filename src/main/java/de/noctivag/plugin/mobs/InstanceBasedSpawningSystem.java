package de.noctivag.plugin.mobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instance-Based Spawning System - Implements proximity-based mob respawning
 * 
 * Features:
 * - 8-chunk proximity check for respawning
 * - Instance-based spawning logic
 * - Prevents mob hoarding by absent players
 * - Dynamic spawn management based on player presence
 */
public class InstanceBasedSpawningSystem {
    
    private final org.bukkit.plugin.Plugin plugin;
    private final ZoneBasedSpawningSystem zoneSystem;
    private final Map<Location, SpawnInstance> spawnInstances = new ConcurrentHashMap<>();
    private final Map<UUID, Set<Location>> playerProximity = new ConcurrentHashMap<>();
    
    // Configuration
    private static final int PROXIMITY_CHUNKS = 8; // 8 chunks radius
    private static final int CHUNK_SIZE = 16; // Minecraft chunk size
    private static final long RESPAWN_CHECK_INTERVAL = 5000L; // 5 seconds
    private static final long PROXIMITY_UPDATE_INTERVAL = 2000L; // 2 seconds
    
    public InstanceBasedSpawningSystem(org.bukkit.plugin.Plugin plugin, ZoneBasedSpawningSystem zoneSystem) {
        this.plugin = plugin;
        this.zoneSystem = zoneSystem;
        
        startProximityUpdateTask();
        startRespawnCheckTask();
    }
    
    /**
     * Register a spawn location for instance-based spawning
     */
    public void registerSpawnLocation(Location location, AdvancedMobSystem.MobType mobType, int maxMobs) {
        SpawnInstance instance = new SpawnInstance(location, mobType, maxMobs);
        spawnInstances.put(location, instance);
    }
    
    /**
     * Unregister a spawn location
     */
    public void unregisterSpawnLocation(Location location) {
        SpawnInstance instance = spawnInstances.remove(location);
        if (instance != null) {
            instance.cleanup();
        }
    }
    
    /**
     * Handle mob death for respawn tracking
     */
    public void onMobDeath(LivingEntity mob, Location deathLocation) {
        // Find the spawn instance for this location
        SpawnInstance instance = findSpawnInstance(deathLocation);
        if (instance != null) {
            instance.recordMobDeath(deathLocation);
        }
    }
    
    /**
     * Check if a location is within 8 chunks of any player
     */
    private boolean isWithinPlayerProximity(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isWithinProximity(location, player.getLocation())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if two locations are within 8 chunks of each other
     */
    private boolean isWithinProximity(Location loc1, Location loc2) {
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            return false;
        }
        
        int chunkX1 = loc1.getBlockX() / CHUNK_SIZE;
        int chunkZ1 = loc1.getBlockZ() / CHUNK_SIZE;
        int chunkX2 = loc2.getBlockX() / CHUNK_SIZE;
        int chunkZ2 = loc2.getBlockZ() / CHUNK_SIZE;
        
        int distance = Math.max(Math.abs(chunkX1 - chunkX2), Math.abs(chunkZ1 - chunkZ2));
        return distance <= PROXIMITY_CHUNKS;
    }
    
    /**
     * Find spawn instance for a location
     */
    private SpawnInstance findSpawnInstance(Location location) {
        return spawnInstances.entrySet().stream()
            .filter(entry -> entry.getKey().getWorld().equals(location.getWorld()))
            .filter(entry -> entry.getKey().distance(location) <= 50) // Within 50 blocks
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Start proximity update task
     */
    private void startProximityUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePlayerProximity();
            }
        }.runTaskTimer(plugin, 0L, PROXIMITY_UPDATE_INTERVAL / 50L); // Convert to ticks
    }
    
    /**
     * Start respawn check task
     */
    private void startRespawnCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkForRespawns();
            }
        }.runTaskTimer(plugin, 0L, RESPAWN_CHECK_INTERVAL / 50L); // Convert to ticks
    }
    
    /**
     * Update player proximity tracking
     */
    private void updatePlayerProximity() {
        // Clear old proximity data
        playerProximity.clear();
        
        // Update proximity for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            Set<Location> nearbySpawns = new HashSet<>();
            
            for (Location spawnLocation : spawnInstances.keySet()) {
                if (isWithinProximity(spawnLocation, player.getLocation())) {
                    nearbySpawns.add(spawnLocation);
                }
            }
            
            if (!nearbySpawns.isEmpty()) {
                playerProximity.put(player.getUniqueId(), nearbySpawns);
            }
        }
    }
    
    /**
     * Check for respawns
     */
    private void checkForRespawns() {
        for (SpawnInstance instance : spawnInstances.values()) {
            if (instance.needsRespawn()) {
                Location instanceLocation = instance.getLocation();
                
                // Check if any player is within proximity
                if (isWithinPlayerProximity(instanceLocation)) {
                    instance.respawnMobs();
                }
            }
        }
    }
    
    /**
     * Get spawn instance statistics
     */
    public SpawnInstanceStatistics getStatistics() {
        int totalInstances = spawnInstances.size();
        int activeInstances = (int) spawnInstances.values().stream()
            .filter(SpawnInstance::isActive)
            .count();
        int totalMobs = spawnInstances.values().stream()
            .mapToInt(SpawnInstance::getCurrentMobCount)
            .sum();
        
        return new SpawnInstanceStatistics(totalInstances, activeInstances, totalMobs);
    }
    
    /**
     * Cleanup all spawn instances
     */
    public void cleanup() {
        for (SpawnInstance instance : spawnInstances.values()) {
            instance.cleanup();
        }
        spawnInstances.clear();
        playerProximity.clear();
    }
    
    /**
     * Spawn Instance - Manages individual spawn locations
     */
    private class SpawnInstance {
        private final Location location;
        private final AdvancedMobSystem.MobType mobType;
        private final int maxMobs;
        private final List<LivingEntity> spawnedMobs = new ArrayList<>();
        private final List<DeathRecord> deathRecords = new ArrayList<>();
        private long lastRespawnTime = 0L;
        private static final long RESPAWN_DELAY = 10000L; // 10 seconds
        
        public SpawnInstance(Location location, AdvancedMobSystem.MobType mobType, int maxMobs) {
            this.location = location;
            this.mobType = mobType;
            this.maxMobs = maxMobs;
        }
        
        /**
         * Check if instance needs respawn
         */
        public boolean needsRespawn() {
            // Remove dead mobs from list
            spawnedMobs.removeIf(Entity::isDead);
            
            // Check if we need more mobs
            if (spawnedMobs.size() >= maxMobs) {
                return false; // Already at max capacity
            }
            
            // Check respawn delay
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRespawnTime < RESPAWN_DELAY) {
                return false; // Still in cooldown
            }
            
            // Check if there are death records to respawn
            return !deathRecords.isEmpty();
        }
        
        /**
         * Respawn mobs
         */
        public void respawnMobs() {
            // Remove dead mobs from list
            spawnedMobs.removeIf(Entity::isDead);
            
            // Calculate how many mobs to spawn
            int mobsToSpawn = Math.min(maxMobs - spawnedMobs.size(), deathRecords.size());
            
            for (int i = 0; i < mobsToSpawn; i++) {
                DeathRecord deathRecord = deathRecords.remove(0);
                spawnMobAtLocation(deathRecord.getDeathLocation());
            }
            
            lastRespawnTime = System.currentTimeMillis();
        }
        
        /**
         * Record mob death
         */
        public void recordMobDeath(Location deathLocation) {
            deathRecords.add(new DeathRecord(deathLocation, System.currentTimeMillis()));
            
            // Limit death records to prevent memory issues
            if (deathRecords.size() > maxMobs * 2) {
                deathRecords.remove(0); // Remove oldest record
            }
        }
        
        /**
         * Spawn mob at specific location
         */
        private void spawnMobAtLocation(Location spawnLocation) {
            try {
                // Get mob configuration
                AdvancedMobSystem.MobConfig config = AdvancedMobSystem.getMobConfig(mobType);
                if (config == null) {
                    return;
                }
                
                // Spawn the mob
                LivingEntity mob = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, config.getEntityType());
                
                // Apply mob configuration
                mob.customName(net.kyori.adventure.text.Component.text(config.getDisplayName()));
                mob.setCustomNameVisible(true);
                
                // Set health
                if (mob.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH) != null) {
                    mob.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(config.getHealth());
                    mob.setHealth(config.getHealth());
                }
                
                // Add to spawned mobs list
                spawnedMobs.add(mob);
                
            } catch (Exception e) {
                System.err.println("Error spawning mob: " + e.getMessage());
            }
        }
        
        /**
         * Check if instance is active (has players nearby)
         */
        public boolean isActive() {
            return isWithinPlayerProximity(location);
        }
        
        /**
         * Get current mob count
         */
        public int getCurrentMobCount() {
            spawnedMobs.removeIf(Entity::isDead);
            return spawnedMobs.size();
        }
        
        /**
         * Get location
         */
        public Location getLocation() {
            return location;
        }
        
        /**
         * Cleanup instance
         */
        public void cleanup() {
            // Remove all spawned mobs
            for (LivingEntity mob : spawnedMobs) {
                if (!mob.isDead()) {
                    mob.remove();
                }
            }
            spawnedMobs.clear();
            deathRecords.clear();
        }
    }
    
    /**
     * Death Record - Tracks mob deaths for respawning
     */
    private static class DeathRecord {
        private final Location deathLocation;
        private final long deathTime;
        
        public DeathRecord(Location deathLocation, long deathTime) {
            this.deathLocation = deathLocation;
            this.deathTime = deathTime;
        }
        
        public Location getDeathLocation() { return deathLocation; }
        public long getDeathTime() { return deathTime; }
    }
    
    /**
     * Spawn Instance Statistics
     */
    public static class SpawnInstanceStatistics {
        private final int totalInstances;
        private final int activeInstances;
        private final int totalMobs;
        
        public SpawnInstanceStatistics(int totalInstances, int activeInstances, int totalMobs) {
            this.totalInstances = totalInstances;
            this.activeInstances = activeInstances;
            this.totalMobs = totalMobs;
        }
        
        public int getTotalInstances() { return totalInstances; }
        public int getActiveInstances() { return activeInstances; }
        public int getTotalMobs() { return totalMobs; }
    }
}
