package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Shadow Assassin - Dungeon mob
 */
public class ShadowAssassin extends DungeonMob {
    
    public ShadowAssassin(Location spawnLocation, String dungeonType, int floor) {
        super("SHADOW_ASSASSIN", spawnLocation, dungeonType, floor);
        
        // Set Shadow Assassin-specific stats
        setMaxHealth(250.0);
        setDamage(100.0);
        setDefense(5.0);
        setCombatXP(200.0);
    }
    
    @Override
    public String getName() {
        return "§cShadow Assassin §7[Floor " + getFloor() + "]";
    }
    
    @Override
    public String getLootTableId() {
        return "shadow_assassin_floor_" + getFloor();
    }
}

