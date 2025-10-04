package de.noctivag.plugin.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Weapon Config class for weapon configurations
 */
public class WeaponConfig {
    private WeaponType weaponType;
    private String name;
    private String description;
    private Material material;
    private int maxLevel;
    private long experiencePerLevel;
    private double damageMultiplier;
    private double critChanceMultiplier;
    private double critDamageMultiplier;
    private double attackSpeedMultiplier;

    public WeaponConfig(WeaponType weaponType, String name, String description, Material material, 
                       int maxLevel, long experiencePerLevel, double damageMultiplier, 
                       double critChanceMultiplier, double critDamageMultiplier, double attackSpeedMultiplier) {
        this.weaponType = weaponType;
        this.name = name;
        this.description = description;
        this.material = material;
        this.maxLevel = maxLevel;
        this.experiencePerLevel = experiencePerLevel;
        this.damageMultiplier = damageMultiplier;
        this.critChanceMultiplier = critChanceMultiplier;
        this.critDamageMultiplier = critDamageMultiplier;
        this.attackSpeedMultiplier = attackSpeedMultiplier;
    }

    public WeaponType getWeaponType() {
        return weaponType;
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

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public double getCritChanceMultiplier() {
        return critChanceMultiplier;
    }

    public double getCritDamageMultiplier() {
        return critDamageMultiplier;
    }

    public double getAttackSpeedMultiplier() {
        return attackSpeedMultiplier;
    }
}
