package de.noctivag.skyblock.accessories;

import org.bukkit.Material;

/**
 * Power Stone - Represents a power stone
 */
public class PowerStone {
    
    private final String name;
    private final String description;
    private final Material material;
    private final int level;
    private final double power;
    
    public PowerStone(String name, String description, Material material, int level, double power) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.level = level;
        this.power = power;
    }
    
    /**
     * Get the power stone name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the power stone description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the power stone material
     */
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Get the power stone level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Get the power stone power
     */
    public double getPower() {
        return power;
    }
}
