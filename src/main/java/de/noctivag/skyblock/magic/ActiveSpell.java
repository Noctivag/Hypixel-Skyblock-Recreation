package de.noctivag.skyblock.magic;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents an active spell
 */
public class ActiveSpell {
    
    private final String spellType;
    private final UUID playerId;
    private final long startTime;
    private final int durationSeconds;
    
    public ActiveSpell(String spellType, UUID playerId, int durationSeconds) {
        this.spellType = spellType;
        this.playerId = playerId;
        this.startTime = java.lang.System.currentTimeMillis();
        this.durationSeconds = durationSeconds;
    }
    
    public String getSpellType() {
        return spellType;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public int getDurationSeconds() {
        return durationSeconds;
    }
    
    public boolean isExpired() {
        long elapsed = java.lang.System.currentTimeMillis() - startTime;
        return elapsed >= (durationSeconds * 1000L);
    }
    
    public long getRemainingTime() {
        long elapsed = java.lang.System.currentTimeMillis() - startTime;
        long total = durationSeconds * 1000L;
        return Math.max(0, total - elapsed);
    }
    
    public double getProgress() {
        long elapsed = java.lang.System.currentTimeMillis() - startTime;
        long total = durationSeconds * 1000L;
        return Math.min(1.0, (double) elapsed / total);
    }
    
    public void apply(Player player) {
        // Apply the spell effect to the player
        // This would depend on the specific spell type
        // For now, we'll just track the spell
    }
    
    @Override
    public String toString() {
        return "ActiveSpell{" +
                "spellType='" + spellType + '\'' +
                ", playerId=" + playerId +
                ", startTime=" + startTime +
                ", durationSeconds=" + durationSeconds +
                ", expired=" + isExpired() +
                '}';
    }
}
