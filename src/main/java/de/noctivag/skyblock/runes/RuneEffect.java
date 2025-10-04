package de.noctivag.skyblock.runes;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

/**
 * Represents an active rune effect on a player
 */
public class RuneEffect {
    
    private final RuneType runeType;
    private final int level;
    private final long expirationTime;
    
    public RuneEffect(RuneType runeType, int level, int durationSeconds) {
        this.runeType = runeType;
        this.level = level;
        this.expirationTime = System.currentTimeMillis() + (durationSeconds * 1000L);
    }
    
    public RuneType getRuneType() {
        return runeType;
    }
    
    public int getLevel() {
        return level;
    }
    
    public long getExpirationTime() {
        return expirationTime;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() >= expirationTime;
    }
    
    public int getRemainingTime() {
        long remaining = expirationTime - System.currentTimeMillis();
        return (int) Math.max(0, remaining / 1000);
    }
    
    public void apply(Player player) {
        // Apply the rune effect to the player
        // This would depend on the specific rune type and level
        // For now, we'll just track the effect
    }
    
    @Override
    public String toString() {
        return "RuneEffect{" +
                "runeType=" + runeType.getName() +
                ", level=" + level +
                ", remainingTime=" + getRemainingTime() +
                '}';
    }
}
