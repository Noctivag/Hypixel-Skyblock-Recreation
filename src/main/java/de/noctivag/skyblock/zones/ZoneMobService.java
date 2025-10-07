package de.noctivag.skyblock.zones;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

/**
 * Zone Mob Service - Service for managing zone-specific mobs
 */
public class ZoneMobService implements Service {
    
    private final SkyblockPlugin plugin;
    private final Map<String, ZoneType> zoneTypes = new HashMap<>();
    private final Random random = new Random();
    private SystemStatus status = SystemStatus.DISABLED;
    
    public ZoneMobService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing ZoneMobService...");
        
        // Initialize zone types
        initializeZoneTypes();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("ZoneMobService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down ZoneMobService...");
        
        zoneTypes.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("ZoneMobService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "ZoneMobService";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Initialize zone types
     */
    private void initializeZoneTypes() {
        // Forest zone
        ZoneType forest = new ZoneType(
            "Forest",
            "A peaceful forest with various creatures",
            org.bukkit.Material.OAK_LEAVES,
            1,
            10
        );
        zoneTypes.put("FOREST", forest);
        
        // Desert zone
        ZoneType desert = new ZoneType(
            "Desert",
            "A harsh desert with dangerous creatures",
            org.bukkit.Material.SAND,
            5,
            15
        );
        zoneTypes.put("DESERT", desert);
        
        // Mountain zone
        ZoneType mountain = new ZoneType(
            "Mountain",
            "A treacherous mountain with powerful creatures",
            org.bukkit.Material.STONE,
            10,
            20
        );
        zoneTypes.put("MOUNTAIN", mountain);
        
        // Ocean zone
        ZoneType ocean = new ZoneType(
            "Ocean",
            "A deep ocean with aquatic creatures",
            org.bukkit.Material.WATER_BUCKET,
            15,
            25
        );
        zoneTypes.put("OCEAN", ocean);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + zoneTypes.size() + " zone types.");
    }
    
    /**
     * Spawn a zone mob at a location
     */
    public ZoneMob spawnZoneMob(Location location, String zoneTypeName) {
        ZoneType zoneType = zoneTypes.get(zoneTypeName.toUpperCase());
        if (zoneType == null) {
            plugin.getLogger().log(Level.WARNING, "Unknown zone type: " + zoneTypeName);
            return null;
        }
        
        // Generate random level within zone range
        int level = zoneType.getMinLevel() + random.nextInt(zoneType.getMaxLevel() - zoneType.getMinLevel() + 1);
        
        ZoneMob zoneMob = new ZoneMob("ZONE_MOB", location, zoneType, level);
        plugin.getLogger().log(Level.INFO, "Spawned " + zoneType.getName() + " mob at level " + level);
        
        return zoneMob;
    }
    
    /**
     * Get a zone type
     */
    public ZoneType getZoneType(String name) {
        return zoneTypes.get(name.toUpperCase());
    }
    
    /**
     * Get all zone types
     */
    public Map<String, ZoneType> getZoneTypes() {
        return new HashMap<>(zoneTypes);
    }
    
    /**
     * Check if a player is in a specific zone
     */
    public boolean isPlayerInZone(Player player, String zoneTypeName) {
        // Placeholder - would check player's current location against zone boundaries
        return true;
    }
    
    /**
     * Get the zone type for a player's current location
     */
    public ZoneType getPlayerZone(Player player) {
        // Placeholder - would determine zone based on player's location
        return zoneTypes.get("FOREST");
    }
}

