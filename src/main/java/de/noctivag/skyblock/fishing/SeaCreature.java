package de.noctivag.skyblock.fishing;

import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Base class for all sea creatures in Hypixel Skyblock
 */
public abstract class SeaCreature extends CustomMob {

    protected final int fishingLevel;
    private final String rarity;

    public SeaCreature(String mobId, EntityType entityType, Location spawnLocation,
                      double maxHealth, double damage, double defense, double combatXP,
                      int fishingLevel, String rarity) {
        super(mobId, entityType, spawnLocation, maxHealth, damage, defense, combatXP);
        this.fishingLevel = fishingLevel;
        this.rarity = rarity;
    }

    /**
     * Get catch message displayed when creature is caught
     */
    public abstract String getCatchMessage();

    /**
     * Get fishing XP reward
     */
    public abstract int getXPReward();

    /**
     * Get creature rarity
     */
    public String getRarity() {
        return rarity;
    }

    /**
     * Get required fishing level
     */
    public int getRequiredLevel() {
        return fishingLevel;
    }
}
