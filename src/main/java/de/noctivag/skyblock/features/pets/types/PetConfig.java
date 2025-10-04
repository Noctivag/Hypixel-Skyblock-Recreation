package de.noctivag.skyblock.features.pets.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Pet Config class for pet configurations
 */
public class PetConfig {
    private PetType petType;
    private String name;
    private String description;
    private Material material;
    private int maxLevel;
    private long experiencePerLevel;
    private double healthMultiplier;
    private double defenseMultiplier;
    private double strengthMultiplier;
    private double speedMultiplier;
    private double intelligenceMultiplier;

    public PetConfig(PetType petType, String name, String description, Material material, 
                     int maxLevel, long experiencePerLevel, double healthMultiplier, 
                     double defenseMultiplier, double strengthMultiplier, double speedMultiplier, 
                     double intelligenceMultiplier) {
        this.petType = petType;
        this.name = name;
        this.description = description;
        this.material = material;
        this.maxLevel = maxLevel;
        this.experiencePerLevel = experiencePerLevel;
        this.healthMultiplier = healthMultiplier;
        this.defenseMultiplier = defenseMultiplier;
        this.strengthMultiplier = strengthMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.intelligenceMultiplier = intelligenceMultiplier;
    }

    public PetType getPetType() {
        return petType;
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

    public double getHealthMultiplier() {
        return healthMultiplier;
    }

    public double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public double getStrengthMultiplier() {
        return strengthMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getIntelligenceMultiplier() {
        return intelligenceMultiplier;
    }
}
