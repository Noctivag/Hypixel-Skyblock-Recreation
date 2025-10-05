package de.noctivag.skyblock.minions;

import org.bukkit.Material;

/**
 * Minion Type - Represents a minion type
 */
public class MinionType {
    
    private final String name;
    private final String description;
    private final Material icon;
    private final Material resourceType;
    private final int maxLevel;
    private final long baseActionInterval;
    private final int baseStorageCapacity;
    
    public MinionType(String name, String description, Material icon, Material resourceType, 
                      int maxLevel, long baseActionInterval, int baseStorageCapacity) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.resourceType = resourceType;
        this.maxLevel = maxLevel;
        this.baseActionInterval = baseActionInterval;
        this.baseStorageCapacity = baseStorageCapacity;
    }
    
    /**
     * Get the minion name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the minion description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the minion icon
     */
    public Material getIcon() {
        return icon;
    }
    
    /**
     * Get the resource type
     */
    public Material getResourceType() {
        return resourceType;
    }
    
    /**
     * Get the max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * Get the base action interval
     */
    public long getBaseActionInterval() {
        return baseActionInterval;
    }
    
    /**
     * Get the base storage capacity
     */
    public int getBaseStorageCapacity() {
        return baseStorageCapacity;
    }
}

