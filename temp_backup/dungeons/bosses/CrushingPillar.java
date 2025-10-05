package de.noctivag.skyblock.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a crushing pillar in the Necron boss fight
 */
public class CrushingPillar {
    private final Location location;
    private boolean active;
    private boolean crushing;
    private long lastCrushTime;
    private final long crushCooldown = 3000; // 3 seconds

    public CrushingPillar(Location location) {
        this.location = location;
        this.active = true;
        this.crushing = false;
        this.lastCrushTime = 0;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCrushing() {
        return crushing;
    }

    public void setCrushing(boolean crushing) {
        this.crushing = crushing;
    }

    public long getLastCrushTime() {
        return lastCrushTime;
    }

    public void setLastCrushTime(long lastCrushTime) {
        this.lastCrushTime = lastCrushTime;
    }

    public long getCrushCooldown() {
        return crushCooldown;
    }

    public boolean canCrush() {
        return active && !crushing && (java.lang.System.currentTimeMillis() - lastCrushTime) >= crushCooldown;
    }

    public void crush(Player target) {
        if (!canCrush()) return;
        
        this.crushing = true;
        this.lastCrushTime = java.lang.System.currentTimeMillis();
        
        // TODO: Implement crushing effects and damage
        // target.damage(10.0); // Example damage
        
        // Reset crushing state after animation
        // This would typically be handled by a task
    }

    public void reset() {
        this.crushing = false;
        this.lastCrushTime = 0;
    }

    public void update() {
        // TODO: Implement pillar update logic
    }
}
