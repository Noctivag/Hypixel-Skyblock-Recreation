package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

/**
 * Sea Guardian - Elite aquatic mob
 */
public class SeaGuardian extends CustomMob {
    
    public SeaGuardian(String mobId, Location spawnLocation) {
        super(mobId, EntityType.GUARDIAN, spawnLocation, 
              150.0,  // maxHealth
              25.0,   // damage
              5.0,    // defense
              20.0    // combatXP
        );
    }
    
    @Override
    public String getName() {
        return "§9Sea Guardian";
    }
    
    @Override
    public String getLootTableId() {
        return "sea_guardian";
    }
    
    @Override
    public void applyBaseAttributes(org.bukkit.entity.Entity entity) {
        super.applyBaseAttributes(entity);
        
        if (entity instanceof Guardian) {
            Guardian guardian = (Guardian) entity;
            // Guardian-specific attributes
        }
    }
    
    /**
     * Spawn sea guardian at location
     */
    public void spawn(Location location) {
        // TODO: Implement sea guardian spawning
        plugin.getLogger().info("Spawning Sea Guardian at " + location);
    }
}

