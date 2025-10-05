package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Felsworn - Dungeon mob
 */
public class Felsworn extends DungeonMob {
    
    public Felsworn(Location spawnLocation, String dungeonType, int floor) {
        super("FELSWORN", spawnLocation, dungeonType, floor);
        
        // Set Felsworn-specific stats
        setMaxHealth(400.0);
        setDamage(80.0);
        setDefense(25.0);
        setCombatXP(180.0);
    }
    
    @Override
    public String getName() {
        return "§cFelsworn §7[Floor " + getFloor() + "]";
    }
    
    @Override
    public String getLootTableId() {
        return "felsworn_floor_" + getFloor();
    }
}

