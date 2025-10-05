package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Dungeon Mob - Base class for all dungeon mobs
 */
public class DungeonMob extends CustomMob {
    
    private final String dungeonType;
    private final int floor;
    
    public DungeonMob(String mobId, Location spawnLocation, String dungeonType, int floor) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              200.0,   // maxHealth
              50.0,    // damage
              10.0,    // defense
              100.0    // combatXP
        );
        this.dungeonType = dungeonType;
        this.floor = floor;
    }
    
    @Override
    public String getName() {
        return "§cDungeon Mob";
    }
    
    @Override
    public String getLootTableId() {
        return "dungeon_mob_" + dungeonType.toLowerCase() + "_floor_" + floor;
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

