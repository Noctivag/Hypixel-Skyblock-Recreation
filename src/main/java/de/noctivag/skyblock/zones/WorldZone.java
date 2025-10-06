package de.noctivag.skyblock.zones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a world with its zones
 */
public class WorldZone {
    
    private final String worldName;
    private final Map<String, Zone> zones = new ConcurrentHashMap<>();
    
    public WorldZone(String worldName) {
        this.worldName = worldName;
    }
    
    /**
     * Add a zone to this world
     */
    public void addZone(Zone zone) {
        zones.put(zone.getName(), zone);
    }
    
    /**
     * Remove a zone from this world
     */
    public void removeZone(String zoneName) {
        zones.remove(zoneName);
    }
    
    /**
     * Get a zone by name
     */
    public Zone getZone(String zoneName) {
        return zones.get(zoneName);
    }
    
    /**
     * Get all zones in this world
     */
    public List<Zone> getZones() {
        return new ArrayList<>(zones.values());
    }
    
    /**
     * Get the number of zones in this world
     */
    public int getZoneCount() {
        return zones.size();
    }
    
    /**
     * Check if a zone exists in this world
     */
    public boolean hasZone(String zoneName) {
        return zones.containsKey(zoneName);
    }
    
    // Getters
    public String getWorldName() { return worldName; }
}
