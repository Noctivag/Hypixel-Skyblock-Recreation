package de.noctivag.skyblock.reforge;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

/**
 * Represents an active reforge effect on a player
 */
public class ReforgeEffect {
    
    private final ReforgeType reforgeType;
    private final int level;
    private final long expirationTime;
    
    public ReforgeEffect(ReforgeType reforgeType, int level, int durationSeconds) {
        this.reforgeType = reforgeType;
        this.level = level;
        this.expirationTime = java.lang.System.currentTimeMillis() + (durationSeconds * 1000L);
    }
    
    public ReforgeType getReforgeType() {
        return reforgeType;
    }
    
    public int getLevel() {
        return level;
    }
    
    public long getExpirationTime() {
        return expirationTime;
    }
    
    public boolean isExpired() {
        return java.lang.System.currentTimeMillis() >= expirationTime;
    }
    
    public int getRemainingTime() {
        long remaining = expirationTime - java.lang.System.currentTimeMillis();
        return (int) Math.max(0, remaining / 1000);
    }
    
    public void apply(Player player) {
        // Apply the reforge effect to the player
        // This would depend on the specific reforge type and level
        // For now, we'll just track the effect
    }
    
    @Override
    public String toString() {
        return "ReforgeEffect{" +
                "reforgeType=" + reforgeType.getName() +
                ", level=" + level +
                ", remainingTime=" + getRemainingTime() +
                '}';
    }
}
