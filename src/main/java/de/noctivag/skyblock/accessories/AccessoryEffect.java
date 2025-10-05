package de.noctivag.skyblock.accessories;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

/**
 * Represents an active accessory effect on a player
 */
public class AccessoryEffect {
    
    private final AccessoryType accessoryType;
    private final int level;
    private final long expirationTime;
    
    public AccessoryEffect(AccessoryType accessoryType, int level, int durationSeconds) {
        this.accessoryType = accessoryType;
        this.level = level;
        this.expirationTime = java.lang.System.currentTimeMillis() + (durationSeconds * 1000L);
    }
    
    public AccessoryType getAccessoryType() {
        return accessoryType;
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
        // Apply the accessory effect to the player
        // This would depend on the specific accessory type and level
        // For now, we'll just track the effect
    }
    
    @Override
    public String toString() {
        return "AccessoryEffect{" +
                "accessoryType=" + accessoryType.getName() +
                ", level=" + level +
                ", remainingTime=" + getRemainingTime() +
                '}';
    }
}
