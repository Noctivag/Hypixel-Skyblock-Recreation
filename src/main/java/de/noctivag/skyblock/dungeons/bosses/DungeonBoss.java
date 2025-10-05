package de.noctivag.skyblock.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class DungeonBoss {

    protected final String name;
    protected final Location spawnLocation;
    protected final double maxHealth;
    protected final double damage;
    protected LivingEntity bossEntity;
    protected double currentHealth;

    public DungeonBoss(String name, Location spawnLocation, double maxHealth, double damage) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.currentHealth = maxHealth;
    }

    protected abstract void spawnBoss();

    public void startBoss() {
        spawnBoss();
    }

    public void stopBoss() {
        if (bossEntity != null && !bossEntity.isDead()) {
            bossEntity.remove();
        }
    }

    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getDamage() {
        return damage;
    }

    public LivingEntity getBossEntity() {
        return bossEntity;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public boolean isAlive() {
        return bossEntity != null && !bossEntity.isDead();
    }

    public double getHealthPercentage() {
        return (currentHealth / maxHealth) * 100;
    }
}
