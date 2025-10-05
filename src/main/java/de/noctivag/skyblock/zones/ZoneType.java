package de.noctivag.skyblock.zones;

import org.bukkit.Material;

/**
 * Zone Type - Represents a zone type
 */
public class ZoneType {
    
    private final String name;
    private final String description;
    private final Material icon;
    private final int minLevel;
    private final int maxLevel;
    
    public ZoneType(String name, String description, Material icon, int minLevel, int maxLevel) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }
    
    /**
     * Get the zone name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the zone description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the zone icon
     */
    public Material getIcon() {
        return icon;
    }
    
    /**
     * Get the minimum level
     */
    public int getMinLevel() {
        return minLevel;
    }
    
    /**
     * Get the maximum level
     */
    public int getMaxLevel() {
        return maxLevel;
    }
}

