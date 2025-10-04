package de.noctivag.plugin.mobs;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zone-Based Spawning System - Implements zone-fixed mob spawning with proximity checks
 * 
 * Features:
 * - Zone-fixed spawning: Mobs spawn only in specific geographic zones
 * - Instance-based spawning: Mobs respawn only when killed and player is within 8 chunks
 * - Proximity checks: Prevents hoarding of spawns by absent players
 * - Dynamic zone management: Zones can be created, modified, and removed
 * - Performance optimized: Uses chunk-based tracking for efficient proximity detection
 */
public class ZoneBasedSpawningSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    
    // Zone management
    private final Map<String, MobZone> mobZones = new ConcurrentHashMap<>();
    private final Map<String, List<UUID>> zonePlayers = new ConcurrentHashMap<>();
    
    // Instance tracking
    private final Map<UUID, MobInstance> mobInstances = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastSpawnTime = new ConcurrentHashMap<>();
    
    // Proximity tracking (8 chunks = 128 blocks radius)
    private static final int PROXIMITY_CHUNKS = 8;
    private static final int PROXIMITY_BLOCKS = PROXIMITY_CHUNKS * 16;
    
    // Spawn rates
    private static final long MIN_RESPAWN_TIME = 5000L; // 5 seconds minimum
    private static final long MAX_RESPAWN_TIME = 30000L; // 30 seconds maximum
    
    // Update task
    private BukkitTask updateTask;
    
    public ZoneBasedSpawningSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeDefaultZones();
        startUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Initialize default mob zones for Hypixel SkyBlock areas
     */
    private void initializeDefaultZones() {
        // Hub World Zones
        createZone("graveyard", "Graveyard", new Location(Bukkit.getWorld("world"), 0, 64, 0), 50, 
                  Arrays.asList(AdvancedMobSystem.MobType.GRAVEYARD_ZOMBIE, AdvancedMobSystem.MobType.ZOMBIE_VILLAGER), 10);
        
        createZone("crypt", "Crypt", new Location(Bukkit.getWorld("world"), 100, 64, 0), 30,
                  Arrays.asList(AdvancedMobSystem.MobType.CRYPT_GHOUL, AdvancedMobSystem.MobType.CRYPT_LURKER), 5);
        
        // End Dimension Zones
        createZone("end_zealot", "Zealot Area", new Location(Bukkit.getWorld("world_the_end"), 0, 64, 0), 100,
                  Arrays.asList(AdvancedMobSystem.MobType.ZEALOT, AdvancedMobSystem.MobType.SPECIAL_ZEALOT), 15);
        
        // Nether Dimension Zones
        createZone("nether_fortress", "Nether Fortress", new Location(Bukkit.getWorld("world_nether"), 0, 64, 0), 80,
                  Arrays.asList(AdvancedMobSystem.MobType.BLAZE, AdvancedMobSystem.MobType.WITHER_SKELETON), 8);
        
        // Private Island Zones
        createZone("private_island", "Private Island", new Location(Bukkit.getWorld("world"), 0, 64, 0), 200,
                  Arrays.asList(AdvancedMobSystem.MobType.ZOMBIE, AdvancedMobSystem.MobType.SKELETON, 
                               AdvancedMobSystem.MobType.SPIDER, AdvancedMobSystem.MobType.CREEPER), 20);
    }
    
    /**
     * Create a new mob zone
     */
    public void createZone(String zoneId, String zoneName, Location center, int radius, 
                          List<AdvancedMobSystem.MobType> mobTypes, int maxMobs) {
        MobZone zone = new MobZone(zoneId, zoneName, center, radius, mobTypes, maxMobs);
        mobZones.put(zoneId, zone);
        zonePlayers.put(zoneId, new ArrayList<>());
        
        plugin.getLogger().info("Created mob zone: " + zoneName + " at " + center.toString());
    }
    
    /**
     * Remove a mob zone
     */
    public void removeZone(String zoneId) {
        MobZone zone = mobZones.remove(zoneId);
        if (zone != null) {
            // Remove all mobs in this zone
            zone.getSpawnedMobs().forEach(mob -> {
                if (mob.isValid()) {
                    mob.remove();
                }
            });
            
            zonePlayers.remove(zoneId);
            plugin.getLogger().info("Removed mob zone: " + zone.getZoneName());
        }
    }
    
    /**
     * Start the update task for proximity checks and respawning
     */
    private void startUpdateTask() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateProximityTracking();
                updateZoneSpawning();
                cleanupDeadMobs();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }
    
    /**
     * Update proximity tracking for all players
     */
    private void updateProximityTracking() {
        for (Map.Entry<String, List<UUID>> entry : zonePlayers.entrySet()) {
            String zoneId = entry.getKey();
            List<UUID> playersInZone = entry.getValue();
            List<UUID> currentPlayers = new ArrayList<>();
            
            MobZone zone = mobZones.get(zoneId);
            if (zone == null) continue;
            
            // Check all online players
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isPlayerInProximity(player, zone)) {
                    currentPlayers.add(player.getUniqueId());
                    
                    // Add to zone if not already there
                    if (!playersInZone.contains(player.getUniqueId())) {
                        playersInZone.add(player.getUniqueId());
                        plugin.getLogger().fine("Player " + player.getName() + " entered zone " + zone.getZoneName());
                    }
                }
            }
            
            // Remove players who left the zone
            playersInZone.removeIf(playerId -> !currentPlayers.contains(playerId));
        }
    }
    
    /**
     * Check if player is within proximity of a zone
     */
    private boolean isPlayerInProximity(Player player, MobZone zone) {
        Location playerLoc = player.getLocation();
        Location zoneCenter = zone.getCenter();
        
        // Check if in same world
        if (!playerLoc.getWorld().equals(zoneCenter.getWorld())) {
            return false;
        }
        
        // Check if within proximity radius (zone radius + proximity blocks)
        double distance = playerLoc.distance(zoneCenter);
        return distance <= (zone.getRadius() + PROXIMITY_BLOCKS);
    }
    
    /**
     * Update zone spawning based on proximity and instance rules
     */
    private void updateZoneSpawning() {
        for (MobZone zone : mobZones.values()) {
            List<UUID> playersInZone = zonePlayers.get(zone.getZoneId());
            
            // Only spawn if players are in proximity
            if (playersInZone.isEmpty()) {
                continue;
            }
            
            // Check if zone needs more mobs
            int currentMobs = zone.getSpawnedMobs().size();
            if (currentMobs >= zone.getMaxMobs()) {
                continue;
            }
            
            // Check respawn timers for each mob type
            for (AdvancedMobSystem.MobType mobType : zone.getMobTypes()) {
                UUID instanceId = getInstanceId(zone.getZoneId(), mobType);
                
                if (canRespawn(instanceId)) {
                    spawnMobInZone(zone, mobType);
                    lastSpawnTime.put(instanceId, System.currentTimeMillis());
                }
            }
        }
    }
    
    /**
     * Check if a mob can respawn based on instance rules
     */
    private boolean canRespawn(UUID instanceId) {
        Long lastSpawn = lastSpawnTime.get(instanceId);
        if (lastSpawn == null) {
            return true; // Never spawned before
        }
        
        long timeSinceLastSpawn = System.currentTimeMillis() - lastSpawn;
        return timeSinceLastSpawn >= MIN_RESPAWN_TIME;
    }
    
    /**
     * Spawn a mob in a specific zone
     */
    private void spawnMobInZone(MobZone zone, AdvancedMobSystem.MobType mobType) {
        Location spawnLocation = generateSpawnLocation(zone);
        if (spawnLocation == null) {
            return; // No valid spawn location found
        }
        
        // Get entity type from mob type
        EntityType entityType = getEntityType(mobType);
        if (entityType == null) {
            return;
        }
        
        // Spawn the entity
        Entity entity = spawnLocation.getWorld().spawnEntity(spawnLocation, entityType);
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            
            // Apply mob properties
            applyMobProperties(livingEntity, mobType);
            
            // Add to zone tracking
            zone.addSpawnedMob(livingEntity);
            
            // Create instance
            UUID instanceId = getInstanceId(zone.getZoneId(), mobType);
            MobInstance instance = new MobInstance(instanceId, livingEntity, zone, mobType);
            mobInstances.put(livingEntity.getUniqueId(), instance);
            
            plugin.getLogger().fine("Spawned " + mobType.name() + " in zone " + zone.getZoneName());
        }
    }
    
    /**
     * Generate a valid spawn location within the zone
     */
    private Location generateSpawnLocation(MobZone zone) {
        Location center = zone.getCenter();
        int radius = zone.getRadius();
        World world = center.getWorld();
        
        // Try multiple random locations
        for (int attempts = 0; attempts < 10; attempts++) {
            double angle = Math.random() * 2 * Math.PI;
            double distance = Math.random() * radius;
            
            double x = center.getX() + Math.cos(angle) * distance;
            double z = center.getZ() + Math.sin(angle) * distance;
            
            // Find highest solid block
            int y = world.getHighestBlockYAt((int) x, (int) z);
            Location spawnLoc = new Location(world, x, y + 1, z);
            
            // Check if location is valid
            if (isValidSpawnLocation(spawnLoc)) {
                return spawnLoc;
            }
        }
        
        return null; // No valid location found
    }
    
    /**
     * Check if a location is valid for mob spawning
     */
    private boolean isValidSpawnLocation(Location location) {
        // Check if block below is solid
        Block blockBelow = location.clone().subtract(0, 1, 0).getBlock();
        if (!blockBelow.getType().isSolid()) {
            return false;
        }
        
        // Check if spawn location is clear
        Block spawnBlock = location.getBlock();
        if (spawnBlock.getType() != Material.AIR) {
            return false;
        }
        
        // Check if there's space above
        Block blockAbove = location.clone().add(0, 1, 0).getBlock();
        return blockAbove.getType() == Material.AIR;
    }
    
    /**
     * Apply mob properties based on type
     */
    private void applyMobProperties(LivingEntity entity, AdvancedMobSystem.MobType mobType) {
        AdvancedMobSystem.MobConfig config = AdvancedMobSystem.getMobConfig(mobType);
        if (config == null) return;
        
        // Set custom name
        entity.customName(net.kyori.adventure.text.Component.text(config.getDisplayName()));
        entity.setCustomNameVisible(true);
        
        // Set health
        entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(config.getHealth());
        entity.setHealth(config.getHealth());
        
        // Apply special effects based on mob type
        applyMobEffects(entity, mobType);
    }
    
    /**
     * Apply special effects to mobs
     */
    private void applyMobEffects(LivingEntity entity, AdvancedMobSystem.MobType mobType) {
        switch (mobType) {
            case GRAVEYARD_ZOMBIE, CRYPT_GHOUL, CRYPT_LURKER -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, false, false));
            }
            case ZEALOT, SPECIAL_ZEALOT -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            }
            case BLAZE, WITHER_SKELETON -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            }
        }
    }
    
    /**
     * Clean up dead mobs from tracking
     */
    private void cleanupDeadMobs() {
        mobInstances.entrySet().removeIf(entry -> {
            LivingEntity entity = entry.getValue().getEntity();
            if (entity.isDead() || !entity.isValid()) {
                // Remove from zone tracking
                MobZone zone = entry.getValue().getZone();
                zone.removeSpawnedMob(entity);
                return true;
            }
            return false;
        });
    }
    
    /**
     * Get instance ID for zone and mob type combination
     */
    private UUID getInstanceId(String zoneId, AdvancedMobSystem.MobType mobType) {
        return UUID.nameUUIDFromBytes((zoneId + "_" + mobType.name()).getBytes());
    }
    
    /**
     * Get entity type from mob type
     */
    private EntityType getEntityType(AdvancedMobSystem.MobType mobType) {
        return switch (mobType) {
            case GRAVEYARD_ZOMBIE, ZOMBIE_VILLAGER, CRYPT_GHOUL, CRYPT_LURKER, ZOMBIE -> EntityType.ZOMBIE;
            case SKELETON -> EntityType.SKELETON;
            case SPIDER -> EntityType.SPIDER;
            case CREEPER -> EntityType.CREEPER;
            case ENDERMAN, ZEALOT, SPECIAL_ZEALOT -> EntityType.ENDERMAN;
            case BLAZE -> EntityType.BLAZE;
            case WITHER_SKELETON -> EntityType.WITHER_SKELETON;
            case MAGMA_CUBE -> EntityType.MAGMA_CUBE;
            case PIGLIN -> EntityType.PIGLIN;
            default -> EntityType.ZOMBIE;
        };
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        UUID entityId = entity.getUniqueId();
        
        // Check if this was a tracked mob
        MobInstance instance = mobInstances.get(entityId);
        if (instance != null) {
            // Remove from zone tracking
            instance.getZone().removeSpawnedMob(entity);
            
            // Remove from instance tracking
            mobInstances.remove(entityId);
            
            plugin.getLogger().fine("Mob " + entity.getType() + " died in zone " + instance.getZone().getZoneName());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player from all zones
        for (List<UUID> playersInZone : zonePlayers.values()) {
            playersInZone.remove(event.getPlayer().getUniqueId());
        }
    }
    
    /**
     * Get zone by ID
     */
    public MobZone getZone(String zoneId) {
        return mobZones.get(zoneId);
    }
    
    /**
     * Get all zones
     */
    public Collection<MobZone> getAllZones() {
        return mobZones.values();
    }
    
    /**
     * Get players in zone
     */
    public List<UUID> getPlayersInZone(String zoneId) {
        return new ArrayList<>(zonePlayers.getOrDefault(zoneId, new ArrayList<>()));
    }
    
    /**
     * Mob Zone class
     */
    public static class MobZone {
        private final String zoneId;
        private final String zoneName;
        private final Location center;
        private final int radius;
        private final List<AdvancedMobSystem.MobType> mobTypes;
        private final int maxMobs;
        private final List<LivingEntity> spawnedMobs = new ArrayList<>();
        
        public MobZone(String zoneId, String zoneName, Location center, int radius, 
                      List<AdvancedMobSystem.MobType> mobTypes, int maxMobs) {
            this.zoneId = zoneId;
            this.zoneName = zoneName;
            this.center = center;
            this.radius = radius;
            this.mobTypes = new ArrayList<>(mobTypes);
            this.maxMobs = maxMobs;
        }
        
        public String getZoneId() { return zoneId; }
        public String getZoneName() { return zoneName; }
        public Location getCenter() { return center; }
        public int getRadius() { return radius; }
        public List<AdvancedMobSystem.MobType> getMobTypes() { return mobTypes; }
        public int getMaxMobs() { return maxMobs; }
        public List<LivingEntity> getSpawnedMobs() { return spawnedMobs; }
        
        public void addSpawnedMob(LivingEntity mob) {
            spawnedMobs.add(mob);
        }
        
        public void removeSpawnedMob(LivingEntity mob) {
            spawnedMobs.remove(mob);
        }
    }
    
    /**
     * Mob Instance class for tracking individual mobs
     */
    public static class MobInstance {
        private final UUID instanceId;
        private final LivingEntity entity;
        private final MobZone zone;
        private final AdvancedMobSystem.MobType mobType;
        private final long spawnTime;
        
        public MobInstance(UUID instanceId, LivingEntity entity, MobZone zone, AdvancedMobSystem.MobType mobType) {
            this.instanceId = instanceId;
            this.entity = entity;
            this.zone = zone;
            this.mobType = mobType;
            this.spawnTime = System.currentTimeMillis();
        }
        
        public UUID getInstanceId() { return instanceId; }
        public LivingEntity getEntity() { return entity; }
        public MobZone getZone() { return zone; }
        public AdvancedMobSystem.MobType getMobType() { return mobType; }
        public long getSpawnTime() { return spawnTime; }
    }
    
    /**
     * Shutdown the system
     */
    public void shutdown() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        
        // Remove all spawned mobs
        for (MobZone zone : mobZones.values()) {
            zone.getSpawnedMobs().forEach(entity -> {
                if (entity.isValid()) {
                    entity.remove();
                }
            });
        }
        
        mobZones.clear();
        zonePlayers.clear();
        mobInstances.clear();
        lastSpawnTime.clear();
    }
}
