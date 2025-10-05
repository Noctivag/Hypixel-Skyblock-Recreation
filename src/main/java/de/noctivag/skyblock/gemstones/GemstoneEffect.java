package de.noctivag.skyblock.gemstones;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

/**
 * Represents an active gemstone effect on a player
 */
public class GemstoneEffect {
    
    private final GemstoneType gemstoneType;
    private final int level;
    private final long expirationTime;
    
    public GemstoneEffect(GemstoneType gemstoneType, int level, int durationSeconds) {
        this.gemstoneType = gemstoneType;
        this.level = level;
        this.expirationTime = java.lang.System.currentTimeMillis() + (durationSeconds * 1000L);
    }
    
    public GemstoneType getGemstoneType() {
        return gemstoneType;
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
        // Apply the gemstone effect to the player
        // This would depend on the specific gemstone type and level
        // For now, we'll just track the effect
    }
    
    @Override
    public String toString() {
        return "GemstoneEffect{" +
                "gemstoneType=" + gemstoneType.getName() +
                ", level=" + level +
                ", remainingTime=" + getRemainingTime() +
                '}';
    }
}
