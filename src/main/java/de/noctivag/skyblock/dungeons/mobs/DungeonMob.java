package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Dungeon Mob - Base class for all dungeon mobs
 */
public class DungeonMob extends CustomMob implements de.noctivag.skyblock.dungeons.BaseDungeonEntity {
    
    private final String dungeonType;
    private final int floor;
    private final java.util.UUID entityId;
    
    public DungeonMob(String mobId, Location spawnLocation, String dungeonType, int floor) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              200.0,   // maxHealth
              50.0,    // damage
              10.0,    // defense
              100.0    // combatXP
        );
        this.dungeonType = dungeonType;
        this.floor = floor;
        this.entityId = java.util.UUID.randomUUID();
    }
    
    @Override
    @Override
    public String getName() {
        return "§cDungeon Mob";
    }
    @Override
    public java.util.UUID getEntityId() { return entityId; }
    @Override
    public String getType() { return "MOB"; }
    @Override
    public int getFloor() { return floor; }
    
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
    
}

