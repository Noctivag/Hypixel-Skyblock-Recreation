package de.noctivag.skyblock.features.dungeons.catacombs;
import org.bukkit.inventory.ItemStack;

/**
 * Score ratings for dungeon completion
 */
public enum ScoreRating {
    S_PLUS("S+", "§6", 1.5),
    S("S", "§6", 1.3),
    A_PLUS("A+", "§c", 1.2),
    A("A", "§c", 1.1),
    B_PLUS("B+", "§e", 1.0),
    B("B", "§e", 0.9),
    C_PLUS("C+", "§a", 0.8),
    C("C", "§a", 0.7),
    D("D", "§7", 0.5);
    
    private final String displayName;
    private final String color;
    private final double multiplier;
    
    ScoreRating(String displayName, String color, double multiplier) {
        this.displayName = displayName;
        this.color = color;
        this.multiplier = multiplier;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    @Override
    public String toString() {
        return color + displayName;
    }
}
