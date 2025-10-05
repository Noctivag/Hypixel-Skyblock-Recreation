package de.noctivag.skyblock.zones;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Zone Mob - Represents a mob that spawns in a specific zone
 */
public class ZoneMob extends CustomMob {
    
    private final ZoneType zoneType;
    private final int level;
    
    public ZoneMob(String mobId, Location spawnLocation, ZoneType zoneType, int level) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              100.0,   // maxHealth
              25.0,    // damage
              5.0,     // defense
              50.0     // combatXP
        );
        this.zoneType = zoneType;
        this.level = level;
        
        // Set level-based stats
        setLevelStats(level);
    }
    
    @Override
    public String getName() {
        return "§c" + zoneType.getName() + " Mob §7[Lv." + level + "]";
    }
    
    @Override
    public String getLootTableId() {
        return "zone_mob_" + zoneType.getName().toLowerCase().replace(" ", "_") + "_level_" + level;
    }
    
    /**
     * Get the zone type
     */
    public ZoneType getZoneType() {
        return zoneType;
    }
    
    /**
     * Get the level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Set stats based on level
     */
    private void setLevelStats(int level) {
        setMaxHealth(100.0 + (level * 10.0));
        setDamage(25.0 + (level * 2.5));
        setDefense(5.0 + (level * 0.5));
        setCombatXP(50.0 + (level * 5.0));
    }
}

