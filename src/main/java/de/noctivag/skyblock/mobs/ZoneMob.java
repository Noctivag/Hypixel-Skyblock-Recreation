package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.enums.ZoneType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * Basis-Klasse für zonen-spezifische Mobs
 */
public abstract class ZoneMob {
    
    protected final String name;
    protected final EntityType entityType;
    protected final ZoneType zone;
    protected final int level;
    protected final double health;
    protected final double damage;
    protected final int xpReward;
    protected final int coinReward;
    
    public ZoneMob(String name, EntityType entityType, ZoneType zone, int level, 
                   double health, double damage, int xpReward, int coinReward) {
        this.name = name;
        this.entityType = entityType;
        this.zone = zone;
        this.level = level;
        this.health = health;
        this.damage = damage;
        this.xpReward = xpReward;
        this.coinReward = coinReward;
    }
    
    /**
     * Spawnt den Mob an der gegebenen Location
     */
    public abstract LivingEntity spawn(Location location);
    
    /**
     * Wendet spezielle Fähigkeiten des Mobs an
     */
    public abstract void useAbility(LivingEntity entity);
    
    /**
     * Wird aufgerufen wenn der Mob stirbt
     */
    public abstract void onDeath(LivingEntity entity);
    
    // Getters
    public String getName() {
        return name;
    }
    
    public EntityType getEntityType() {
        return entityType;
    }
    
    public ZoneType getZone() {
        return zone;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getHealth() {
        return health;
    }
    
    public double getDamage() {
        return damage;
    }
    
    public int getXpReward() {
        return xpReward;
    }
    
    public int getCoinReward() {
        return coinReward;
    }
}
