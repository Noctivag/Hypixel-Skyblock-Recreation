package de.noctivag.skyblock.zones;

import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Zone System - Manages world zones and their properties
 */
public class ZoneSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private final Map<String, WorldZone> worldZones = new ConcurrentHashMap<>();
    private final Map<String, Zone> zones = new ConcurrentHashMap<>();
    private SystemStatus status = SystemStatus.DISABLED;
    
    public ZoneSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing ZoneSystem...");
        
        try {
            // Load zone configurations
            loadZoneConfigurations();
            
            // Initialize world zones
            initializeWorldZones();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("ZoneSystem initialized with " + zones.size() + " zones across " + worldZones.size() + " worlds.");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize ZoneSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down ZoneSystem...");
        
        worldZones.clear();
        zones.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("ZoneSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "ZoneSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load zone configurations from zones.yml
     */
    private void loadZoneConfigurations() {
        // Create default zones.yml if it doesn't exist
        File zonesFile = new File(plugin.getDataFolder(), "zones.yml");
        if (!zonesFile.exists()) {
            createDefaultZonesConfig(zonesFile);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(zonesFile);
        
        ConfigurationSection worldsSection = config.getConfigurationSection("worlds");
        if (worldsSection == null) {
            plugin.getLogger().warning("No worlds section found in zones.yml");
            return;
        }
        
        for (String worldName : worldsSection.getKeys(false)) {
            WorldZone worldZone = new WorldZone(worldName);
            ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);
            
            if (worldSection != null) {
                ConfigurationSection zonesSection = worldSection.getConfigurationSection("zones");
                if (zonesSection != null) {
                    for (String zoneName : zonesSection.getKeys(false)) {
                        Zone zone = loadZone(zonesSection.getConfigurationSection(zoneName), worldName, zoneName);
                        if (zone != null) {
                            worldZone.addZone(zone);
                            zones.put(worldName + ":" + zoneName, zone);
                        }
                    }
                }
            }
            
            worldZones.put(worldName, worldZone);
            plugin.getLogger().info("Loaded " + worldZone.getZones().size() + " zones for world: " + worldName);
        }
    }
    
    /**
     * Load a zone from configuration
     */
    private Zone loadZone(ConfigurationSection zoneSection, String worldName, String zoneName) {
        if (zoneSection == null) {
            return null;
        }
        
        Zone zone = new Zone(worldName, zoneName);
        
        // Load zone properties
        zone.displayName(Component.text(zoneSection.getString("display_name", zoneName)));
        zone.setDescription(zoneSection.getString("description", ""));
        zone.setMinLevel(zoneSection.getInt("min_level", 1));
        zone.setMaxLevel(zoneSection.getInt("max_level", 100));
        zone.setPvpEnabled(zoneSection.getBoolean("pvp_enabled", false));
        zone.setMobSpawningEnabled(zoneSection.getBoolean("mob_spawning_enabled", true));
        zone.setPlayerLimit(zoneSection.getInt("player_limit", -1));
        
        // Load zone boundaries
        ConfigurationSection boundsSection = zoneSection.getConfigurationSection("bounds");
        if (boundsSection != null) {
            ZoneBounds bounds = new ZoneBounds(
                boundsSection.getInt("min_x", -1000),
                boundsSection.getInt("min_y", 0),
                boundsSection.getInt("min_z", -1000),
                boundsSection.getInt("max_x", 1000),
                boundsSection.getInt("max_y", 256),
                boundsSection.getInt("max_z", 1000)
            );
            zone.setBounds(bounds);
        }
        
        // Load spawn points
        ConfigurationSection spawnsSection = zoneSection.getConfigurationSection("spawns");
        if (spawnsSection != null) {
            for (String spawnName : spawnsSection.getKeys(false)) {
                ConfigurationSection spawnSection = spawnsSection.getConfigurationSection(spawnName);
                if (spawnSection != null) {
                    ZoneSpawn spawn = new ZoneSpawn(
                        spawnName,
                        spawnSection.getDouble("x", 0),
                        spawnSection.getDouble("y", 64),
                        spawnSection.getDouble("z", 0),
                        (float) spawnSection.getDouble("yaw", 0),
                        (float) spawnSection.getDouble("pitch", 0)
                    );
                    zone.addSpawn(spawn);
                }
            }
        }
        
        // Load mob spawn configurations
        ConfigurationSection mobsSection = zoneSection.getConfigurationSection("mobs");
        if (mobsSection != null) {
            for (String mobId : mobsSection.getKeys(false)) {
                ConfigurationSection mobSection = mobsSection.getConfigurationSection(mobId);
                if (mobSection != null) {
                    MobSpawnConfig mobConfig = new MobSpawnConfig(
                        mobId,
                        mobSection.getInt("weight", 1),
                        mobSection.getInt("min_level", 1),
                        mobSection.getInt("max_level", 100),
                        mobSection.getInt("spawn_radius", 50),
                        mobSection.getInt("max_count", 10),
                        mobSection.getStringList("spawn_conditions")
                    );
                    zone.addMobSpawn(mobConfig);
                }
            }
        }
        
        return zone;
    }
    
    /**
     * Create default zones configuration
     */
    private void createDefaultZonesConfig(File zonesFile) {
        try {
            plugin.saveResource("zones.yml", false);
            plugin.getLogger().info("Created default zones.yml configuration");
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to create default zones.yml", e);
        }
    }
    
    /**
     * Initialize world zones
     */
    private void initializeWorldZones() {
        // This method can be used to perform any post-loading initialization
        // For example, validating world existence, setting up region protection, etc.
    }
    
    /**
     * Get the zone a player is currently in
     */
    public Zone getPlayerZone(Player player) {
        Location location = player.getLocation();
        return getZoneAt(location);
    }
    
    /**
     * Get the zone at a specific location
     */
    public Zone getZoneAt(Location location) {
        String worldName = location.getWorld().getName();
        WorldZone worldZone = worldZones.get(worldName);
        
        if (worldZone == null) {
            return null;
        }
        
        for (Zone zone : worldZone.getZones()) {
            if (zone.contains(location)) {
                return zone;
            }
        }
        
        return null;
    }
    
    /**
     * Get all zones in a world
     */
    public List<Zone> getZonesInWorld(String worldName) {
        WorldZone worldZone = worldZones.get(worldName);
        if (worldZone == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(worldZone.getZones());
    }
    
    /**
     * Get a specific zone
     */
    public Zone getZone(String worldName, String zoneName) {
        return zones.get(worldName + ":" + zoneName);
    }
    
    /**
     * Get all zones
     */
    public Collection<Zone> getAllZones() {
        return zones.values();
    }
    
    /**
     * Get all world zones
     */
    public Collection<WorldZone> getAllWorldZones() {
        return worldZones.values();
    }
    
    /**
     * Check if a location is in any zone
     */
    public boolean isInZone(Location location) {
        return getZoneAt(location) != null;
    }
    
    /**
     * Get the number of players in a zone
     */
    public int getPlayerCountInZone(Zone zone) {
        int count = 0;
        World world = Bukkit.getWorld(zone.getWorldName());
        if (world != null) {
            for (Player player : world.getPlayers()) {
                if (zone.contains(player.getLocation())) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Get all players in a zone
     */
    public List<Player> getPlayersInZone(Zone zone) {
        List<Player> players = new ArrayList<>();
        World world = Bukkit.getWorld(zone.getWorldName());
        if (world != null) {
            for (Player player : world.getPlayers()) {
                if (zone.contains(player.getLocation())) {
                    players.add(player);
                }
            }
        }
        return players;
    }
    
    /**
     * Teleport a player to a zone spawn
     */
    public boolean teleportToZoneSpawn(Player player, Zone zone, String spawnName) {
        ZoneSpawn spawn = zone.getSpawn(spawnName);
        if (spawn == null) {
            return false;
        }
        
        World world = Bukkit.getWorld(zone.getWorldName());
        if (world == null) {
            return false;
        }
        
        Location location = new Location(world, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch());
        player.teleport(location);
        return true;
    }
    
    /**
     * Get a random spawn in a zone
     */
    public ZoneSpawn getRandomSpawn(Zone zone) {
        List<ZoneSpawn> spawns = zone.getSpawns();
        if (spawns.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return spawns.get(random.nextInt(spawns.size()));
    }
}
