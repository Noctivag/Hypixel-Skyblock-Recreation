package de.noctivag.skyblock.loot;

import org.bukkit.Material;

/**
 * Loot Entry - Represents a single loot entry
 */
public class LootEntry {
    
    private final Material material;
    private final int minAmount;
    private final int maxAmount;
    private final double dropChance;
    
    public LootEntry(Material material, int minAmount, int maxAmount, double dropChance) {
        this.material = material;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.dropChance = dropChance;
    }
    
    /**
     * Get the material
     */
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Get the minimum amount
     */
    public int getMinAmount() {
        return minAmount;
    }
    
    /**
     * Get the maximum amount
     */
    public int getMaxAmount() {
        return maxAmount;
    }
    
    /**
     * Get the drop chance
     */
    public double getDropChance() {
        return dropChance;
    }
}

