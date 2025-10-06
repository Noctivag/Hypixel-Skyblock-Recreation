package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Sea Walker - Aquatic mob for fishing areas
 */
public class SeaWalker extends CustomMob {
    
    public SeaWalker(String mobId, Location spawnLocation) {
        super(mobId, EntityType.SQUID, spawnLocation, 
              50.0,   // maxHealth
              10.0,   // damage
              1.0,    // defense
              5.0     // combatXP
        );
    }
    
    @Override
    public String getName() {
        return "§bSea Walker";
    }
    
    @Override
    public String getLootTableId() {
        return "sea_walker";
    }
    
    @Override
    public void applyBaseAttributes(org.bukkit.entity.Entity entity) {
        super.applyBaseAttributes(entity);
        
        if (entity instanceof Squid) {
            Squid squid = (Squid) entity;
            // Squid-specific attributes can be set here
        }
    }
    
    /**
     * Spawn sea walker at location
     */
    public void spawn(Location location) {
        // TODO: Implement sea walker spawning
        // Log spawning - getLogger not available in static context
        System.out.println("Spawning Sea Walker at " + location);
    }
}

