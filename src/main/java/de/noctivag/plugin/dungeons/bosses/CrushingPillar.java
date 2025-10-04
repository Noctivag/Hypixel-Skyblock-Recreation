package de.noctivag.plugin.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Crushing Pillar - Represents a crushing pillar mechanic for Necron Phase 2
 */
public class CrushingPillar {
    
    private final String id;
    private final Location baseLocation;
    private final int maxHeight;
    private int currentHeight;
    private boolean isCrushing;
    private boolean isRising;
    private long lastCrushTime;
    private long crushCooldown = 5000L; // 5 seconds
    
    public CrushingPillar(String id, Location baseLocation, int maxHeight) {
        this.id = id;
        this.baseLocation = baseLocation;
        this.maxHeight = maxHeight;
        this.currentHeight = 0;
        this.isCrushing = false;
        this.isRising = false;
        this.lastCrushTime = 0L;
    }
    
    /**
     * Update pillar mechanics
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        // Check if it's time to crush
        if (!isCrushing && !isRising && currentTime - lastCrushTime >= crushCooldown) {
            startCrush();
        }
        
        // Handle crushing phase
        if (isCrushing) {
            updateCrushing();
        }
        
        // Handle rising phase
        if (isRising) {
            updateRising();
        }
    }
    
    /**
     * Start crushing sequence
     */
    private void startCrush() {
        isCrushing = true;
        currentHeight = maxHeight;
        
        // Create pillar blocks
        for (int y = 0; y < currentHeight; y++) {
            Location blockLoc = baseLocation.clone().add(0, y, 0);
            if (blockLoc.getBlock().getType() == Material.AIR) {
                blockLoc.getBlock().setType(Material.OBSIDIAN);
            }
        }
        
        // Warning effects
        baseLocation.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, 
            baseLocation.clone().add(0, maxHeight / 2, 0), 50, 1, maxHeight / 2, 1, 0.1);
        baseLocation.getWorld().playSound(baseLocation, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
    }
    
    /**
     * Update crushing mechanics
     */
    private void updateCrushing() {
        // Crush pillar down quickly
        if (currentHeight > 0) {
            currentHeight--;
            
            // Remove top block
            Location removeLoc = baseLocation.clone().add(0, currentHeight, 0);
            if (removeLoc.getBlock().getType() == Material.OBSIDIAN) {
                removeLoc.getBlock().setType(Material.AIR);
                
                // Damage players at this height
                damagePlayersAtHeight(currentHeight);
            }
        } else {
            // Crushing complete, start rising
            isCrushing = false;
            isRising = true;
        }
    }
    
    /**
     * Update rising mechanics
     */
    private void updateRising() {
        // Rise pillar back up slowly
        if (currentHeight < maxHeight) {
            currentHeight++;
            
            // Add block at current height
            Location addLoc = baseLocation.clone().add(0, currentHeight - 1, 0);
            if (addLoc.getBlock().getType() == Material.AIR) {
                addLoc.getBlock().setType(Material.OBSIDIAN);
            }
        } else {
            // Rising complete
            isRising = false;
            lastCrushTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Damage players at specific height
     */
    private void damagePlayersAtHeight(int height) {
        Location damageLocation = baseLocation.clone().add(0, height, 0);
        
        for (Player player : damageLocation.getWorld().getPlayers()) {
            if (player.getLocation().distance(damageLocation) <= 2) {
                player.damage(5000); // 5000 damage
                player.sendMessage("§c§lYou were crushed by a pillar!");
                
                // Visual effects
                damageLocation.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
                damageLocation.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
            }
        }
    }
    
    /**
     * Force crush immediately
     */
    public void forceCrush() {
        if (!isCrushing && !isRising) {
            startCrush();
        }
    }
    
    /**
     * Reset pillar to base state
     */
    public void reset() {
        // Remove all pillar blocks
        for (int y = 0; y < maxHeight; y++) {
            Location blockLoc = baseLocation.clone().add(0, y, 0);
            if (blockLoc.getBlock().getType() == Material.OBSIDIAN) {
                blockLoc.getBlock().setType(Material.AIR);
            }
        }
        
        // Reset state
        currentHeight = 0;
        isCrushing = false;
        isRising = false;
        lastCrushTime = System.currentTimeMillis();
    }
    
    /**
     * Check if pillar is currently crushing
     */
    public boolean isCrushing() {
        return isCrushing;
    }
    
    /**
     * Check if pillar is currently rising
     */
    public boolean isRising() {
        return isRising;
    }
    
    /**
     * Get current height of pillar
     */
    public int getCurrentHeight() {
        return currentHeight;
    }
    
    /**
     * Get maximum height of pillar
     */
    public int getMaxHeight() {
        return maxHeight;
    }
    
    /**
     * Get pillar location
     */
    public Location getLocation() {
        return baseLocation;
    }
    
    /**
     * Get pillar ID
     */
    public String getId() {
        return id;
    }
}
