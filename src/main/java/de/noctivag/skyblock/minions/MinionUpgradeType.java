package de.noctivag.skyblock.minions;

import org.bukkit.Material;

/**
 * Minion Upgrade Type - Represents a minion upgrade type
 */
public class MinionUpgradeType {
    
    private final String name;
    private final String description;
    private final Material icon;
    private final MinionUpgrade upgrade;
    private final int maxLevel;
    
    public MinionUpgradeType(String name, String description, Material icon, 
                             MinionUpgrade upgrade, int maxLevel) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.upgrade = upgrade;
        this.maxLevel = maxLevel;
    }
    
    /**
     * Get the upgrade name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the upgrade description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the upgrade icon
     */
    public Material getIcon() {
        return icon;
    }
    
    /**
     * Get the upgrade
     */
    public MinionUpgrade getUpgrade() {
        return upgrade;
    }
    
    /**
     * Get the max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }
}

