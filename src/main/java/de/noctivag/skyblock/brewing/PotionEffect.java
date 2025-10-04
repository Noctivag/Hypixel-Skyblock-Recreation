package de.noctivag.skyblock.brewing;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a potion effect with stats and duration
 */
public class PotionEffect {
    
    private final String name;
    private final int statBoost;
    private final int duration;
    private final double multiplier;
    
    public PotionEffect(String name, int statBoost, int duration, double multiplier) {
        this.name = name;
        this.statBoost = statBoost;
        this.duration = duration;
        this.multiplier = multiplier;
    }
    
    public String getName() {
        return name;
    }
    
    public int getStatBoost() {
        return statBoost;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    @Override
    public String toString() {
        return "PotionEffect{" +
                "name='" + name + '\'' +
                ", statBoost=" + statBoost +
                ", duration=" + duration +
                ", multiplier=" + multiplier +
                '}';
    }
}
