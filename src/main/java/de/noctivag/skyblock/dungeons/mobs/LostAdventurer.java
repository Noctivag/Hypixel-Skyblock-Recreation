package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Lost Adventurer - Dungeon mob
 */
public class LostAdventurer extends DungeonMob {
    
    public LostAdventurer(Location spawnLocation, String dungeonType, int floor) {
        super("LOST_ADVENTURER", spawnLocation, dungeonType, floor);
        
        // Set Lost Adventurer-specific stats
        setMaxHealth(300.0);
        setDamage(75.0);
        setDefense(15.0);
        setCombatXP(150.0);
    }
    
    @Override
    public String getName() {
        return "§cLost Adventurer §7[Floor " + getFloor() + "]";
    }
    
    @Override
    public String getLootTableId() {
        return "lost_adventurer_floor_" + getFloor();
    }
}

