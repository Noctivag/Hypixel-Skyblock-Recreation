package de.noctivag.plugin.talismans;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

/**
 * Represents an active talisman effect on a player
 */
public class TalismanEffect {
    
    private final TalismanType talismanType;
    private final int level;
    private final long expirationTime;
    
    public TalismanEffect(TalismanType talismanType, int level, int durationSeconds) {
        this.talismanType = talismanType;
        this.level = level;
        this.expirationTime = System.currentTimeMillis() + (durationSeconds * 1000L);
    }
    
    public TalismanType getTalismanType() {
        return talismanType;
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
        // Apply the talisman effect to the player
        // This would depend on the specific talisman type and level
        // For now, we'll just track the effect
    }
    
    @Override
    public String toString() {
        return "TalismanEffect{" +
                "talismanType=" + talismanType.getName() +
                ", level=" + level +
                ", remainingTime=" + getRemainingTime() +
                '}';
    }
}
