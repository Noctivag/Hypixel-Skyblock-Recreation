package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Dungeon Boss - Base class for all dungeon bosses
 */
public class DungeonBoss extends CustomMob implements de.noctivag.skyblock.dungeons.BaseDungeonEntity {
    
    private final String dungeonType;
    private final int floor;
    private final java.util.UUID entityId;
    
    public DungeonBoss(String mobId, Location spawnLocation, String dungeonType, int floor) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              1000.0,  // maxHealth
              100.0,   // damage
              20.0,    // defense
              500.0    // combatXP
        );
        this.dungeonType = dungeonType;
        this.floor = floor;
        this.entityId = java.util.UUID.randomUUID();
    }
    
    @Override
    @Override
    public String getName() {
        return "§cDungeon Boss";
    }
    @Override
    public java.util.UUID getEntityId() { return entityId; }
    @Override
    public String getType() { return "BOSS"; }
    @Override
    public int getFloor() { return floor; }
    
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
    
}

