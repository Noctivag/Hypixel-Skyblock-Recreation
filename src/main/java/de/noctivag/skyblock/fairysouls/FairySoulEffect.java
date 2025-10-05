package de.noctivag.skyblock.fairysouls;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

/**
 * Represents an active fairy soul effect on a player
 */
public class FairySoulEffect {
    
    private final FairySoul fairySoul;
    private final int level;
    private final long expirationTime;
    
    public FairySoulEffect(FairySoul fairySoul, int level, int durationSeconds) {
        this.fairySoul = fairySoul;
        this.level = level;
        this.expirationTime = java.lang.System.currentTimeMillis() + (durationSeconds * 1000L);
    }
    
    public FairySoul getFairySoul() {
        return fairySoul;
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
        // Apply the fairy soul effect to the player
        // This would depend on the specific fairy soul type and level
        // For now, we'll just track the effect
    }
    
    @Override
    public String toString() {
        return "FairySoulEffect{" +
                "fairySoul=" + fairySoul.getName() +
                ", level=" + level +
                ", remainingTime=" + getRemainingTime() +
                '}';
    }
}
