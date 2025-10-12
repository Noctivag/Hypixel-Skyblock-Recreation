package de.noctivag.skyblock.zones;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a zone within a world
 */
public class Zone {
    
    private final String worldName;
    private final String name;
    private String displayName;
    private String description;
    private int minLevel;
    private int maxLevel;
    private boolean pvpEnabled;
    private boolean mobSpawningEnabled;
    private int playerLimit;
    private ZoneBounds bounds;
    private final List<ZoneSpawn> spawns = new ArrayList<>();
    private final Map<String, MobSpawnConfig> mobSpawns = new ConcurrentHashMap<>();
    
    public Zone(String worldName, String name) {
        this.worldName = worldName;
        this.name = name;
        this.displayName = name;
        this.description = "";
        this.minLevel = 1;
        this.maxLevel = 100;
        this.pvpEnabled = false;
        this.mobSpawningEnabled = true;
        this.playerLimit = -1;
    }
    
    /**
     * Check if a location is within this zone
     */
    public boolean contains(Location location) {
        if (!location.getWorld().getName().equals(worldName)) {
            return false;
        }
        
        if (bounds == null) {
            return true; // No bounds means entire world
        }
        
        return bounds.contains(location);
    }
    
    /**
     * Check if a player can enter this zone
     */
    public boolean canPlayerEnter(org.bukkit.entity.Player player) {
        // Check level requirement
        // This would integrate with a level system
        // For now, we'll just return true
        
        // Check player limit
        if (playerLimit > 0) {
            // This would count players in the zone
            // For now, we'll just return true
        }
        
        return true;
    }
    
    /**
     * Get a spawn by name
     */
    public ZoneSpawn getSpawn(String spawnName) {
        for (ZoneSpawn spawn : spawns) {
            if (spawn.getName().equals(spawnName)) {
                return spawn;
            }
        }
        return null;
    }
    
    /**
     * Add a spawn to this zone
     */
    public void addSpawn(ZoneSpawn spawn) {
        spawns.add(spawn);
    }
    
    /**
     * Add a mob spawn configuration
     */
    public void addMobSpawn(MobSpawnConfig mobSpawn) {
        mobSpawns.put(mobSpawn.getMobId(), mobSpawn);
    }
    
    /**
     * Get mob spawn configurations
     */
    public List<MobSpawnConfig> getMobSpawns() {
        return new ArrayList<>(mobSpawns.values());
    }
    
    /**
     * Get a specific mob spawn configuration
     */
    public MobSpawnConfig getMobSpawn(String mobId) {
        return mobSpawns.get(mobId);
    }
    
    // Getters and Setters
    public String getWorldName() { return worldName; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getMinLevel() { return minLevel; }
    public void setMinLevel(int minLevel) { this.minLevel = minLevel; }
    public int getMaxLevel() { return maxLevel; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
    public boolean isPvpEnabled() { return pvpEnabled; }
    public void setPvpEnabled(boolean pvpEnabled) { this.pvpEnabled = pvpEnabled; }
    public boolean isMobSpawningEnabled() { return mobSpawningEnabled; }
    public void setMobSpawningEnabled(boolean mobSpawningEnabled) { this.mobSpawningEnabled = mobSpawningEnabled; }
    public int getPlayerLimit() { return playerLimit; }
    public void setPlayerLimit(int playerLimit) { this.playerLimit = playerLimit; }
    public ZoneBounds getBounds() { return bounds; }
    public void setBounds(ZoneBounds bounds) { this.bounds = bounds; }
    public List<ZoneSpawn> getSpawns() { return new ArrayList<>(spawns); }
}
