package de.noctivag.plugin.features.minions.fuel;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a fuel for minions
 */
public class MinionFuel {
    
    private final String id;
    private final String name;
    private final String description;
    private final FuelType type;
    private final double speedMultiplier;
    private int remainingUses;
    private final boolean infinite;
    
    public MinionFuel(String id, String name, String description, FuelType type, double speedMultiplier, int uses, boolean infinite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.speedMultiplier = speedMultiplier;
        this.remainingUses = uses;
        this.infinite = infinite;
    }
    
    /**
     * Consume one use of the fuel
     */
    public boolean consume() {
        if (infinite) return true;
        if (remainingUses <= 0) return false;
        
        remainingUses--;
        return true;
    }
    
    /**
     * Check if fuel is active (has uses or is infinite)
     */
    public boolean isActive() {
        return infinite || remainingUses > 0;
    }
    
    /**
     * Check if fuel works during specific time
     */
    public boolean worksAtTime(long worldTime) {
        if (type == FuelType.SOLAR) {
            // Solar panel only works during day (0-12000 ticks)
            return worldTime >= 0 && worldTime <= 12000;
        }
        return true;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public FuelType getType() {
        return type;
    }
    
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public int getRemainingUses() {
        return remainingUses;
    }
    
    public boolean isInfinite() {
        return infinite;
    }
    
    /**
     * Get fuel efficiency percentage
     */
    public double getEfficiencyPercentage() {
        return (speedMultiplier - 1.0) * 100;
    }
    
    @Override
    public String toString() {
        if (infinite) {
            return name + " (Infinite)";
        } else {
            return name + " (" + remainingUses + " uses)";
        }
    }
}
