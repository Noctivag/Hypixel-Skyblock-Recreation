package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.zones.Zone;
import de.noctivag.skyblock.zones.ZoneSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that manages mob spawning based on configuration
 * Reads spawning.yml and spawns mobs in appropriate locations
 */
public class SpawningService implements Service {
    
    private final SkyblockPlugin plugin;
    private final MobManager mobManager;
    private final ZoneSystem zoneSystem;
    private final Map<String, WorldSpawnConfig> worldConfigs = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> playerMobCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> regionMobCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> zoneMobCounts = new ConcurrentHashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = true;
    private BukkitTask spawningTask;
    private BukkitTask cleanupTask;
    
    public SpawningService(SkyblockPlugin plugin, MobManager mobManager, ZoneSystem zoneSystem) {
        this.plugin = plugin;
        this.mobManager = mobManager;
        this.zoneSystem = zoneSystem;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing SpawningService...");
        
        // Load configuration
        loadSpawningConfig();
        
        // Start spawning task
        startSpawningTask();
        
        // Start cleanup task
        startCleanupTask();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("SpawningService initialized successfully!");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down SpawningService...");
        
        // Stop tasks
        if (spawningTask != null) {
            spawningTask.cancel();
        }
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
        
        // Clear data
        worldConfigs.clear();
        playerMobCounts.clear();
        regionMobCounts.clear();
        
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("SpawningService shutdown complete!");
    }
    
    @Override
    public String getName() {
        return "SpawningService";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Load spawning configuration from spawning.yml
     */
    private void loadSpawningConfig() {
        plugin.saveResource("spawning.yml", false);
        FileConfiguration config = plugin.getConfig();
        
        ConfigurationSection worldsSection = config.getConfigurationSection("worlds");
        if (worldsSection == null) {
            plugin.getLogger().warning("No worlds section found in spawning.yml");
            return;
        }
        
        for (String worldName : worldsSection.getKeys(false)) {
            WorldSpawnConfig worldConfig = new WorldSpawnConfig();
            ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);
            
            if (worldSection != null) {
                ConfigurationSection regionsSection = worldSection.getConfigurationSection("regions");
                if (regionsSection != null) {
                    for (String regionName : regionsSection.getKeys(false)) {
                        RegionSpawnConfig regionConfig = loadRegionConfig(regionsSection.getConfigurationSection(regionName));
                        worldConfig.addRegion(regionName, regionConfig);
                    }
                }
            }
            
            worldConfigs.put(worldName, worldConfig);
            plugin.getLogger().info("Loaded spawning config for world: " + worldName);
        }
    }
    
    /**
     * Load region configuration
     */
    private RegionSpawnConfig loadRegionConfig(ConfigurationSection regionSection) {
        RegionSpawnConfig config = new RegionSpawnConfig();
        
        config.spawnLimit = regionSection.getInt("spawn_limit", 10);
        config.spawnRadius = regionSection.getInt("spawn_radius", 50);
        config.spawnInterval = regionSection.getInt("spawn_interval", 30);
        
        ConfigurationSection mobsSection = regionSection.getConfigurationSection("mobs");
        if (mobsSection != null) {
            for (String mobId : mobsSection.getKeys(false)) {
                ConfigurationSection mobSection = mobsSection.getConfigurationSection(mobId);
                if (mobSection != null) {
                    MobSpawnConfig mobConfig = new MobSpawnConfig();
                    mobConfig.amount = mobSection.getInt("amount", 1);
                    mobConfig.minDistance = mobSection.getInt("min_distance", 10);
                    mobConfig.maxDistance = mobSection.getInt("max_distance", 30);
                    mobConfig.spawnConditions = mobSection.getStringList("spawn_conditions");
                    
                    config.addMob(mobId, mobConfig);
                }
            }
        }
        
        return config;
    }
    
    /**
     * Start the spawning task
     */
    private void startSpawningTask() {
        spawningTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!enabled) {
                    return;
                }
                
                for (Player player : Bukkit.getOnlinePlayers()) {
                    handlePlayerSpawning(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }
    
    /**
     * Start the cleanup task
     */
    private void startCleanupTask() {
        cleanupTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!enabled) {
                    return;
                }
                
                cleanupDeadMobs();
            }
        }.runTaskTimer(plugin, 0L, 20L * 300); // Run every 5 minutes
    }
    
    /**
     * Handle spawning for a specific player
     */
    private void handlePlayerSpawning(Player player) {
        // First, try zone-based spawning
        Zone playerZone = zoneSystem.getPlayerZone(player);
        if (playerZone != null && playerZone.isMobSpawningEnabled()) {
            spawnMobsInZone(player, playerZone);
            return;
        }
        
        // Fallback to legacy region-based spawning
        World world = player.getWorld();
        WorldSpawnConfig worldConfig = worldConfigs.get(world.getName());
        
        if (worldConfig == null) {
            return; // No config for this world
        }
        
        for (Map.Entry<String, RegionSpawnConfig> entry : worldConfig.getRegions().entrySet()) {
            String regionName = entry.getKey();
            RegionSpawnConfig regionConfig = entry.getValue();
            
            // Check if player is in this region (simplified - you can add WorldGuard integration)
            if (isPlayerInRegion(player, regionName)) {
                spawnMobsInRegion(player, regionName, regionConfig);
            }
        }
    }
    
    /**
     * Check if player is in a specific region
     * This is a simplified implementation - you can integrate with WorldGuard
     */
    private boolean isPlayerInRegion(Player player, String regionName) {
        // For now, assume all players are in all regions
        // In a real implementation, you would check WorldGuard regions or coordinates
        return true;
    }
    
    /**
     * Spawn mobs in a specific zone
     */
    private void spawnMobsInZone(Player player, Zone zone) {
        String zoneKey = zone.getWorldName() + ":" + zone.getName();
        int currentMobs = zoneMobCounts.getOrDefault(zoneKey, 0);
        
        // Get mob spawn configurations for this zone
        List<de.noctivag.skyblock.zones.MobSpawnConfig> mobSpawns = zone.getMobSpawns();
        if (mobSpawns.isEmpty()) {
            return; // No mobs configured for this zone
        }
        
        // Calculate total spawn limit for this zone
        int totalSpawnLimit = mobSpawns.stream().mapToInt(de.noctivag.skyblock.zones.MobSpawnConfig::getMaxCount).sum();
        
        if (currentMobs >= totalSpawnLimit) {
            return; // Zone is at capacity
        }
        
        // Select a mob to spawn based on weights
        de.noctivag.skyblock.zones.MobSpawnConfig selectedMobConfig = selectMobToSpawn(mobSpawns, player);
        if (selectedMobConfig == null) {
            return;
        }
        
        // Check if this specific mob type is at capacity
        String mobKey = zoneKey + ":" + selectedMobConfig.getMobId();
        int currentMobCount = zoneMobCounts.getOrDefault(mobKey, 0);
        if (currentMobCount >= selectedMobConfig.getMaxCount()) {
            return;
        }
        
        // Find a spawn location
        Location spawnLocation = findSpawnLocation(player, selectedMobConfig);
        if (spawnLocation == null) {
            return;
        }
        
        // Spawn the mob
        CustomMob mob = mobManager.spawnMob(selectedMobConfig.getMobId(), spawnLocation);
        if (mob != null) {
            zoneMobCounts.put(zoneKey, currentMobs + 1);
            zoneMobCounts.put(mobKey, currentMobCount + 1);
            playerMobCounts.put(player.getUniqueId(), playerMobCounts.getOrDefault(player.getUniqueId(), 0) + 1);
        }
    }
    
    /**
     * Spawn mobs in a specific region (legacy method)
     */
    private void spawnMobsInRegion(Player player, String regionName, RegionSpawnConfig regionConfig) {
        String regionKey = player.getWorld().getName() + ":" + regionName;
        int currentMobs = regionMobCounts.getOrDefault(regionKey, 0);
        
        if (currentMobs >= regionConfig.spawnLimit) {
            return; // Region is at capacity
        }
        
        // Check spawn interval
        // You can add more sophisticated timing here
        
        // Select a mob to spawn based on weights
        String selectedMob = selectMobToSpawn(regionConfig);
        if (selectedMob == null) {
            return;
        }
        
        // Find a spawn location
        Location spawnLocation = findSpawnLocationLegacy(player, regionConfig.getMob(selectedMob));
        if (spawnLocation == null) {
            return;
        }
        
        // Spawn the mob
        CustomMob mob = mobManager.spawnMob(selectedMob, spawnLocation);
        if (mob != null) {
            regionMobCounts.put(regionKey, currentMobs + 1);
            playerMobCounts.put(player.getUniqueId(), playerMobCounts.getOrDefault(player.getUniqueId(), 0) + 1);
        }
    }
    
    /**
     * Select a mob to spawn based on weights (zone-based)
     */
    private de.noctivag.skyblock.zones.MobSpawnConfig selectMobToSpawn(List<de.noctivag.skyblock.zones.MobSpawnConfig> mobSpawns, Player player) {
        List<de.noctivag.skyblock.zones.MobSpawnConfig> validMobs = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        
        for (de.noctivag.skyblock.zones.MobSpawnConfig mobConfig : mobSpawns) {
            if (mobConfig.canSpawn(player)) {
                validMobs.add(mobConfig);
                weights.add(mobConfig.getWeight());
            }
        }
        
        if (validMobs.isEmpty()) {
            return null;
        }
        
        // Weighted random selection
        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int random = new Random().nextInt(totalWeight);
        
        int currentWeight = 0;
        for (int i = 0; i < validMobs.size(); i++) {
            currentWeight += weights.get(i);
            if (random < currentWeight) {
                return validMobs.get(i);
            }
        }
        
        return validMobs.get(0); // Fallback
    }
    
    /**
     * Select a mob to spawn based on weights (legacy region-based)
     */
    private String selectMobToSpawn(RegionSpawnConfig regionConfig) {
        List<String> mobs = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        
        for (Map.Entry<String, MobSpawnConfig> entry : regionConfig.getMobs().entrySet()) {
            mobs.add(entry.getKey());
            weights.add(entry.getValue().amount);
        }
        
        if (mobs.isEmpty()) {
            return null;
        }
        
        // Weighted random selection
        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int random = new Random().nextInt(totalWeight);
        
        int currentWeight = 0;
        for (int i = 0; i < mobs.size(); i++) {
            currentWeight += weights.get(i);
            if (random < currentWeight) {
                return mobs.get(i);
            }
        }
        
        return mobs.get(0); // Fallback
    }
    
    /**
     * Find a suitable spawn location near the player (zone-based)
     */
    private Location findSpawnLocation(Player player, de.noctivag.skyblock.zones.MobSpawnConfig mobConfig) {
        Location playerLoc = player.getLocation();
        Random random = new Random();
        
        for (int attempts = 0; attempts < 10; attempts++) {
            // Generate random location within spawn radius
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = random.nextDouble() * mobConfig.getSpawnRadius();
            
            double x = playerLoc.getX() + Math.cos(angle) * distance;
            double z = playerLoc.getZ() + Math.sin(angle) * distance;
            double y = playerLoc.getY();
            
            Location spawnLoc = new Location(player.getWorld(), x, y, z);
            
            // Check if location is safe to spawn
            if (isSafeSpawnLocation(spawnLoc)) {
                return spawnLoc;
            }
        }
        
        return null; // No safe location found
    }
    
    /**
     * Find a suitable spawn location near the player (legacy region-based)
     */
    private Location findSpawnLocationLegacy(Player player, MobSpawnConfig mobConfig) {
        Location playerLoc = player.getLocation();
        Random random = new Random();
        
        for (int attempts = 0; attempts < 10; attempts++) {
            // Generate random location within spawn radius
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = mobConfig.minDistance + random.nextDouble() * (mobConfig.maxDistance - mobConfig.minDistance);
            
            double x = playerLoc.getX() + Math.cos(angle) * distance;
            double z = playerLoc.getZ() + Math.sin(angle) * distance;
            double y = playerLoc.getY();
            
            Location spawnLoc = new Location(player.getWorld(), x, y, z);
            
            // Check if location is safe to spawn
            if (isSafeSpawnLocation(spawnLoc)) {
                return spawnLoc;
            }
        }
        
        return null; // No safe location found
    }
    
    /**
     * Check if a location is safe for spawning
     */
    private boolean isSafeSpawnLocation(Location location) {
        // Check if location is not in a block
        if (location.getBlock().getType().isSolid()) {
            return false;
        }
        
        // Check if there's ground below
        Location below = location.clone().subtract(0, 1, 0);
        if (!below.getBlock().getType().isSolid()) {
            return false;
        }
        
        // Check if there's space above
        Location above = location.clone().add(0, 1, 0);
        if (above.getBlock().getType().isSolid()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Clean up dead mobs
     */
    private void cleanupDeadMobs() {
        // Remove dead mobs from counts
        for (Map.Entry<UUID, CustomMob> entry : mobManager.getActiveMobs().entrySet()) {
            if (entry.getValue().isDead()) {
                mobManager.removeMob(entry.getKey());
            }
        }
        
        // Reset counts periodically
        regionMobCounts.clear();
        zoneMobCounts.clear();
        playerMobCounts.clear();
    }
    
    /**
     * Configuration classes
     */
    private static class WorldSpawnConfig {
        private final Map<String, RegionSpawnConfig> regions = new ConcurrentHashMap<>();
        
        public void addRegion(String name, RegionSpawnConfig config) {
            regions.put(name, config);
        }
        
        public Map<String, RegionSpawnConfig> getRegions() {
            return regions;
        }
    }
    
    private static class RegionSpawnConfig {
        public int spawnLimit = 10;
        public int spawnRadius = 50;
        public int spawnInterval = 30;
        private final Map<String, MobSpawnConfig> mobs = new ConcurrentHashMap<>();
        
        public void addMob(String mobId, MobSpawnConfig config) {
            mobs.put(mobId, config);
        }
        
        public MobSpawnConfig getMob(String mobId) {
            return mobs.get(mobId);
        }
        
        public Map<String, MobSpawnConfig> getMobs() {
            return mobs;
        }
    }
    
    private static class MobSpawnConfig {
        public int amount = 1;
        public int minDistance = 10;
        public int maxDistance = 30;
        public List<String> spawnConditions = new ArrayList<>();
    }
}
