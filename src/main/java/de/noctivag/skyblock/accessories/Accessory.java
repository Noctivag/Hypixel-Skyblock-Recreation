package de.noctivag.skyblock.accessories;

import org.bukkit.Material;

/**
 * Accessory - Represents an accessory
 */
public class Accessory {
    
    private final String name;
    private final String description;
    private final Material material;
    private final Rarity rarity;
    private final StatType statType;
    private final double statValue;
    
    public Accessory(String name, String description, Material material, Rarity rarity, 
                     StatType statType, double statValue) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.rarity = rarity;
        this.statType = statType;
        this.statValue = statValue;
    }
    
    /**
     * Get the accessory name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the accessory description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the accessory material
     */
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Get the accessory rarity
     */
    public Rarity getRarity() {
        return rarity;
    }
    
    /**
     * Get the stat type
     */
    public StatType getStatType() {
        return statType;
    }
    
    /**
     * Get the stat value
     */
    public double getStatValue() {
        return statValue;
    }
}
