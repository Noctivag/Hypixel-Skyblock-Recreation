package de.noctivag.plugin.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;

/**
 * Crystal Podium - Represents a crystal placement podium for Necron Phase 1
 */
public class CrystalPodium {
    
    private final String id;
    private final Location location;
    private boolean hasCrystal;
    private Block podiumBlock;
    
    public CrystalPodium(String id, Location location, boolean hasCrystal) {
        this.id = id;
        this.location = location;
        this.hasCrystal = hasCrystal;
        
        // Set up the podium block
        podiumBlock = location.getBlock();
        if (!hasCrystal) {
            podiumBlock.setType(Material.END_STONE);
        }
    }
    
    /**
     * Place a crystal on the podium
     */
    public boolean placeCrystal() {
        if (hasCrystal) {
            return false; // Already has a crystal
        }
        
        hasCrystal = true;
        podiumBlock.setType(Material.END_CRYSTAL);
        
        // Visual and sound effects
        location.getWorld().spawnParticle(Particle.END_ROD, location.add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
        location.getWorld().playSound(location, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Remove crystal from podium
     */
    public void removeCrystal() {
        if (!hasCrystal) {
            return;
        }
        
        hasCrystal = false;
        podiumBlock.setType(Material.END_STONE);
        
        // Visual and sound effects
        location.getWorld().spawnParticle(Particle.CRIT, location.add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
        location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
    }
    
    /**
     * Check if podium has a crystal
     */
    public boolean hasCrystal() {
        return hasCrystal;
    }
    
    /**
     * Get podium location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Get podium ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Update podium state
     */
    public void update() {
        // Check if crystal is still there (in case of destruction)
        if (hasCrystal && podiumBlock.getType() != Material.END_CRYSTAL) {
            hasCrystal = false;
        }
    }
}
