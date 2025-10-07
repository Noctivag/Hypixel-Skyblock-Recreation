package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Slayer Boss - Base class for all slayer bosses
 */
public class SlayerBoss extends CustomMob {
    
    private final int tier;
    private final String slayerType;
    
    public SlayerBoss(String mobId, Location spawnLocation, int tier, String slayerType) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              100.0,  // maxHealth
              20.0,   // damage
              5.0,    // defense
              25.0    // combatXP
        );
        this.tier = tier;
        this.slayerType = slayerType;
    }
    
    @Override
    public String getName() {
        return "§cSlayer Boss";
    }
    
    @Override
    public String getLootTableId() {
        return "slayer_boss";
    }
    
    /**
     * Get the tier
     */
    public int getTier() {
        return tier;
    }
    
    /**
     * Get the slayer type
     */
    public String getSlayerType() {
        return slayerType;
    }
    
    /**
     * Start the boss fight
     */
    public void startBoss() {
        // TODO: Implement boss start logic
        if (entity != null && entity.isValid()) {
            entity.setCustomNameVisible(true);
        }
    }
    
    /**
     * Stop the boss fight
     */
    public void stopBoss() {
        // TODO: Implement boss stop logic
        if (entity != null && entity.isValid()) {
            entity.remove();
        }
    }
}

