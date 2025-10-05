package de.noctivag.skyblock.pets;

import org.bukkit.Material;

/**
 * Pet Type - Represents a pet type
 */
public class PetType {
    
    private final String name;
    private final String description;
    private final Material icon;
    private final int maxLevel;
    private final double baseHealth;
    private final double baseDamage;
    private final double baseDefense;
    
    public PetType(String name, String description, Material icon, int maxLevel, 
                   double baseHealth, double baseDamage, double baseDefense) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.maxLevel = maxLevel;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseDefense = baseDefense;
    }
    
    /**
     * Get the pet name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the pet description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the pet icon
     */
    public Material getIcon() {
        return icon;
    }
    
    /**
     * Get the max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * Get the base health
     */
    public double getBaseHealth() {
        return baseHealth;
    }
    
    /**
     * Get the base damage
     */
    public double getBaseDamage() {
        return baseDamage;
    }
    
    /**
     * Get the base defense
     */
    public double getBaseDefense() {
        return baseDefense;
    }
}

