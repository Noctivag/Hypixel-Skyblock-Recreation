package de.noctivag.skyblock.skyblock;

import java.util.UUID;

/**
 * Player Combat Data - Stores combat-related information for players
 */
public class PlayerCombatData {

    private final UUID playerUuid;
    private double health;
    private double maxHealth;
    private double defense;
    private double strength;
    private double critChance;
    private double critDamage;
    private long lastDamageTime;

    public PlayerCombatData(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.health = 100.0;
        this.maxHealth = 100.0;
        this.defense = 0.0;
        this.strength = 0.0;
        this.critChance = 0.0;
        this.critDamage = 50.0; // Default crit damage
        this.lastDamageTime = System.currentTimeMillis();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
        this.lastDamageTime = System.currentTimeMillis();
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getCritChance() {
        return critChance;
    }

    public void setCritChance(double critChance) {
        this.critChance = Math.max(0, Math.min(100, critChance));
    }

    public double getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(double critDamage) {
        this.critDamage = Math.max(0, critDamage);
    }

    public long getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void takeDamage(double damage) {
        double actualDamage = Math.max(0, damage - defense);
        setHealth(health - actualDamage);
    }

    public void heal(double amount) {
        setHealth(health + amount);
    }
}
