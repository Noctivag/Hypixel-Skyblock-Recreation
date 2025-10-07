package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * Zone Mob - Mob that spawns in specific zones
 */
public class ZoneMob extends CustomMob {
    
    private final String zoneType;
    private final int level;

    public ZoneMob(String mobId, Location spawnLocation, String zoneType) {
        this(mobId, spawnLocation, zoneType, 1);
    }

    public ZoneMob(String mobId, Location spawnLocation, String zoneType, int level) {
        super(mobId, EntityType.ZOMBIE, spawnLocation,
              50.0,   // maxHealth
              10.0,   // damage
              1.0,    // defense
              5.0     // combatXP
        );
        this.zoneType = zoneType;
        this.level = level;
    }
    
    @Override
    public String getName() {
        return "§7Zone Mob";
    }
    
    @Override
    public String getLootTableId() {
        return "zone_mob";
    }
    
    /**
     * Get the zone type
     */
    public String getZoneType() {
        return zoneType;
    }

    /**
     * Get the mob level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Use mob ability (placeholder)
     */
    public void useAbility(LivingEntity entity) {
        // TODO: Implement mob abilities
    }

    /**
     * Handle mob death (placeholder)
     */
    public void onDeath(LivingEntity entity) {
        // TODO: Implement mob death handling
    }
}

