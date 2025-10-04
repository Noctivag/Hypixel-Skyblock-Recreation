package de.noctivag.plugin.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Legendary Weapon class for legendary weapons
 */
public class LegendaryWeapon {
    private LegendaryWeaponType weaponType;
    private String name;
    private String description;
    private Material material;
    private WeaponRarity rarity;
    private double baseDamage;
    private double baseCritChance;
    private double baseCritDamage;
    private double baseAttackSpeed;
    private double baseStrength;
    private double baseFerocity;
    private List<String> abilities;
    private Map<String, Double> stats;

    public LegendaryWeapon(LegendaryWeaponType weaponType, String name, String description, Material material, WeaponRarity rarity) {
        this.weaponType = weaponType;
        this.name = name;
        this.description = description;
        this.material = material;
        this.rarity = rarity;
        this.baseDamage = 0.0;
        this.baseCritChance = 0.0;
        this.baseCritDamage = 0.0;
        this.baseAttackSpeed = 0.0;
        this.baseStrength = 0.0;
        this.baseFerocity = 0.0;
        this.abilities = new ArrayList<>();
        this.stats = new HashMap<>();
    }

    public LegendaryWeaponType getWeaponType() {
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

    public WeaponRarity getRarity() {
        return rarity;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getBaseCritChance() {
        return baseCritChance;
    }

    public double getBaseCritDamage() {
        return baseCritDamage;
    }

    public double getBaseAttackSpeed() {
        return baseAttackSpeed;
    }

    public double getBaseStrength() {
        return baseStrength;
    }

    public double getBaseFerocity() {
        return baseFerocity;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public Map<String, Double> getStats() {
        return stats;
    }

    public void setWeaponType(LegendaryWeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setRarity(WeaponRarity rarity) {
        this.rarity = rarity;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setBaseCritChance(double baseCritChance) {
        this.baseCritChance = baseCritChance;
    }

    public void setBaseCritDamage(double baseCritDamage) {
        this.baseCritDamage = baseCritDamage;
    }

    public void setBaseAttackSpeed(double baseAttackSpeed) {
        this.baseAttackSpeed = baseAttackSpeed;
    }

    public void setBaseStrength(double baseStrength) {
        this.baseStrength = baseStrength;
    }

    public void setBaseFerocity(double baseFerocity) {
        this.baseFerocity = baseFerocity;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public void setStats(Map<String, Double> stats) {
        this.stats = stats;
    }

    public void addAbility(String ability) {
        this.abilities.add(ability);
    }

    public void addStat(String stat, double value) {
        this.stats.put(stat, value);
    }
}
