package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Zone Mob - Mob that spawns in specific zones
 */
public class ZoneMob extends CustomMob {
    
    private final String zoneType;
    
    public ZoneMob(String mobId, Location spawnLocation, String zoneType) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              50.0,   // maxHealth
              10.0,   // damage
              1.0,    // defense
              5.0     // combatXP
        );
        this.zoneType = zoneType;
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
}

