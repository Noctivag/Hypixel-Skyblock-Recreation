package de.noctivag.skyblock.items;

import org.bukkit.Material;

/**
 * Custom Item Type - Represents a custom item type
 */
public class CustomItemType {
    
    private final String name;
    private final String description;
    private final Material material;
    private final int customModelData;
    private final boolean glowing;
    
    public CustomItemType(String name, String description, Material material, int customModelData, boolean glowing) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.customModelData = customModelData;
        this.glowing = glowing;
    }
    
    /**
     * Get the item name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the item description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the material
     */
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Get the custom model data
     */
    public int getCustomModelData() {
        return customModelData;
    }
    
    /**
     * Check if the item is glowing
     */
    public boolean isGlowing() {
        return glowing;
    }
}

