package de.noctivag.skyblock.minions;

/**
 * Minion Upgrade - Represents a minion upgrade
 */
public class MinionUpgrade {
    
    private final String name;
    private final String description;
    private final int level;
    private final double effect;
    
    public MinionUpgrade(String name, String description, int level, double effect) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.effect = effect;
    }
    
    /**
     * Get the upgrade name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the upgrade description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Get the effect
     */
    public double getEffect() {
        return effect;
    }
}