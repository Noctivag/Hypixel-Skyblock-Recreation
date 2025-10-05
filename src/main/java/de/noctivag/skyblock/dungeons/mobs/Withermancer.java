package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Withermancer - Dungeon mob
 */
public class Withermancer extends DungeonMob {
    
    public Withermancer(Location spawnLocation, String dungeonType, int floor) {
        super("WITHERMANCER", spawnLocation, dungeonType, floor);
        
        // Set Withermancer-specific stats
        setMaxHealth(350.0);
        setDamage(90.0);
        setDefense(20.0);
        setCombatXP(220.0);
    }
    
    @Override
    public String getName() {
        return "§cWithermancer §7[Floor " + getFloor() + "]";
    }
    
    @Override
    public String getLootTableId() {
        return "withermancer_floor_" + getFloor();
    }
}

