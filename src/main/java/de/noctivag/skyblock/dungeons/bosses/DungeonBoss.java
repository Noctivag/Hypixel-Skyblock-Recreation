package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Dungeon Boss - Base class for all dungeon bosses
 */
public class DungeonBoss extends CustomMob {
    
    private final String dungeonType;
    private final int floor;
    
    public DungeonBoss(String mobId, Location spawnLocation, String dungeonType, int floor) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              1000.0,  // maxHealth
              100.0,   // damage
              20.0,    // defense
              500.0    // combatXP
        );
        this.dungeonType = dungeonType;
        this.floor = floor;
    }
    
    @Override
    public String getName() {
        return "§cDungeon Boss";
    }
    
    @Override
    public String getLootTableId() {
        return "dungeon_boss_" + dungeonType.toLowerCase() + "_floor_" + floor;
    }
    
    /**
     * Get the dungeon type
     */
    public String getDungeonType() {
        return dungeonType;
    }
    
    /**
     * Get the floor
     */
    public int getFloor() {
        return floor;
    }
}

