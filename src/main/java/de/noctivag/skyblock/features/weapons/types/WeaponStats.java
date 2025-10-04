package de.noctivag.skyblock.features.weapons.types;
import org.bukkit.inventory.ItemStack;

/**
 * Weapon Stats class for weapon statistics
 */
public class WeaponStats {
    private WeaponType weaponType;
    private int level;
    private WeaponRarity rarity;
    private double damage;
    private double critChance;
    private double critDamage;
    private double attackSpeed;
    private double strength;
    private double ferocity;

    public WeaponStats(WeaponType weaponType, int level, WeaponRarity rarity) {
        this.weaponType = weaponType;
        this.level = level;
        this.rarity = rarity;
        this.damage = weaponType.getBaseDamage();
        this.critChance = weaponType.getBaseCritChance();
        this.critDamage = weaponType.getBaseCritDamage();
        this.attackSpeed = weaponType.getBaseAttackSpeed();
        this.strength = weaponType.getBaseStrength();
        this.ferocity = weaponType.getBaseFerocity();
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getLevel() {
        return level;
    }

    public WeaponRarity getRarity() {
        return rarity;
    }

    public double getDamage() {
        return damage;
    }

    public double getCritChance() {
        return critChance;
    }

    public double getCritDamage() {
        return critDamage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public double getStrength() {
        return strength;
    }

    public double getFerocity() {
        return ferocity;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRarity(WeaponRarity rarity) {
        this.rarity = rarity;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }

    public void setCritDamage(double critDamage) {
        this.critDamage = critDamage;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setFerocity(double ferocity) {
        this.ferocity = ferocity;
    }
}
