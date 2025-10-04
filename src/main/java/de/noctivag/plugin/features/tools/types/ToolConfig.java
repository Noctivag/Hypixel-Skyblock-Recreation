package de.noctivag.plugin.features.tools.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Tool Config class for tool configurations
 */
public class ToolConfig {
    private ToolType toolType;
    private String name;
    private String description;
    private Material material;
    private int maxLevel;
    private long experiencePerLevel;
    private double efficiencyMultiplier;
    private double durabilityMultiplier;

    public ToolConfig(ToolType toolType, String name, String description, Material material, 
                     int maxLevel, long experiencePerLevel, double efficiencyMultiplier, double durabilityMultiplier) {
        this.toolType = toolType;
        this.name = name;
        this.description = description;
        this.material = material;
        this.maxLevel = maxLevel;
        this.experiencePerLevel = experiencePerLevel;
        this.efficiencyMultiplier = efficiencyMultiplier;
        this.durabilityMultiplier = durabilityMultiplier;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public long getExperiencePerLevel() {
        return experiencePerLevel;
    }

    public double getEfficiencyMultiplier() {
        return efficiencyMultiplier;
    }

    public double getDurabilityMultiplier() {
        return durabilityMultiplier;
    }
}
