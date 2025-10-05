package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Bonzo - First floor dungeon boss
 */
public class Bonzo extends DungeonBoss {
    
    public Bonzo(Location spawnLocation) {
        super("BONZO", spawnLocation, "CATACOMBS", 1);
        
        // Set Bonzo-specific stats
        setMaxHealth(5000.0);
        setDamage(150.0);
        setDefense(30.0);
        setCombatXP(1000.0);
    }
    
    @Override
    public String getName() {
        return "§cBonzo §7[Floor 1]";
    }
    
    @Override
    public String getLootTableId() {
        return "bonzo_floor_1";
    }
}

