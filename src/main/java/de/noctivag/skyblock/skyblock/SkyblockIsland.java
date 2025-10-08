package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import org.bukkit.Location;

/**
 * Skyblock Island - Player's private island data
 */
public class SkyblockIsland {
    
    private final UUID ownerId;
    private String islandName;
    private Location spawnLocation;
    private int size;
    private Map<String, Object> settings;
    
    public SkyblockIsland(UUID ownerId) {
        this.ownerId = ownerId;
        this.islandName = "Private Island";
        this.size = 100;
        this.settings = new HashMap<>();
        
        // Initialize default settings
        initializeDefaultSettings();
    }
    
    /**
     * Initialize default island settings
     */
    private void initializeDefaultSettings() {
        settings.put("pvp", false);
        settings.put("visitors", true);
        settings.put("coop", false);
        settings.put("size", 100);
    }
    
    // Getters and setters
    public UUID getOwnerId() { return ownerId; }
    
    public String getIslandName() { return islandName; }
    public void setIslandName(String islandName) { this.islandName = islandName; }
    
    public Location getSpawnLocation() { return spawnLocation; }
    public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public Map<String, Object> getSettings() { return settings; }
    public void setSettings(Map<String, Object> settings) { this.settings = settings; }
    
    /**
     * Get setting value
     */
    public Object getSetting(String key) {
        return settings.get(key);
    }
    
    /**
     * Set setting value
     */
    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }
    
    /**
     * Get owner UUID
     */
    public UUID getOwner() {
        return ownerId;
    }
    
    /**
     * Check if player is trusted
     */
    public boolean isTrusted(UUID playerId) {
        // TODO: Implement trust system
        return false;
    }
    
    /**
     * Check if location is within island bounds
     */
    public boolean isWithinBounds(org.bukkit.Location location) {
        if (spawnLocation == null || location == null) return false;
        if (!location.getWorld().equals(spawnLocation.getWorld())) return false;
        
        double distance = location.distance(spawnLocation);
        return distance <= size;
    }
    
    public void save() {
        // Placeholder save method
    }
}