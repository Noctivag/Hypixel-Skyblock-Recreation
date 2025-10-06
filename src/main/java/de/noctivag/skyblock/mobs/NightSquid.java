package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

/**
 * Night Squid - Nocturnal aquatic mob
 */
public class NightSquid extends CustomMob {
    
    public NightSquid(String mobId, Location spawnLocation) {
        super(mobId, EntityType.SQUID, spawnLocation, 
              75.0,   // maxHealth
              15.0,   // damage
              2.0,    // defense
              8.0     // combatXP
        );
    }
    
    @Override
    public String getName() {
        return "§8Night Squid";
    }
    
    @Override
    public String getLootTableId() {
        return "night_squid";
    }
    
    @Override
    public void applyBaseAttributes(org.bukkit.entity.Entity entity) {
        super.applyBaseAttributes(entity);
        
        if (entity instanceof Squid) {
            Squid squid = (Squid) entity;
            // Night Squid-specific attributes
        }
    }
    
    /**
     * Spawn night squid at location
     */
    public void spawn(Location location) {
        // TODO: Implement night squid spawning
        // Log spawning - getLogger not available in static context
        System.out.println("Spawning Night Squid at " + location);
    }
}

