package de.noctivag.skyblock.mobs.zones;

import de.noctivag.skyblock.enums.ZoneType;
import de.noctivag.skyblock.mobs.ZoneMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;

/**
 * Magma Cube Boss - Crimson Isle Mob
 * Ein riesiger Magma-Würfel, der sich in kleinere teilt
 */
public class MagmaCubeBoss extends ZoneMob {
    
    public MagmaCubeBoss() {
        super("§c§lMagma Cube Boss", EntityType.MAGMA_CUBE, ZoneType.CRIMSON_ISLE, 40, 1500.0, 100.0, 150, 200);
    }
    
    @Override
    public LivingEntity spawn(Location location) {
        MagmaCube magmaCube = (MagmaCube) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
        
        // Customize appearance
        magmaCube.setCustomName("§c§lMagma Cube Boss §7[Lv40] §cBOSS");
        magmaCube.setCustomNameVisible(true);
        magmaCube.setSize(8); // Large size
        magmaCube.setHealth(health);
        magmaCube.setMaxHealth(health);
        
        // Make it glow
        magmaCube.setGlowing(true);
        
        return magmaCube;
    }
    
    @Override
    public void useAbility(LivingEntity entity) {
        // Magma Cube Boss ability: Split into smaller cubes and create lava pools
        Location loc = entity.getLocation();
        
        // Create fire particles
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 100, 4, 4, 4, 0.2);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 50, 3, 3, 3, 0.1);
        
        // Play sound
        loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_SQUISH, 1.0f, 0.5f);
        
        // Split into smaller cubes
        splitIntoS smallerCubes(loc);
        
        // Create lava pools
        createLavaPools(loc);
        
        // Notify nearby players
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 12.0) {
                player.sendMessage("§c§lDer Magma Cube Boss teilt sich und erstellt Lava-Pools!");
            }
        }
    }
    
    private void splitIntoS smallerCubes(Location center) {
        // Spawn 3 smaller magma cubes
        for (int i = 0; i < 3; i++) {
            Location splitLoc = center.clone().add(
                (Math.random() - 0.5) * 8,
                0,
                (Math.random() - 0.5) * 8
            );
            
            MagmaCube smallCube = (MagmaCube) splitLoc.getWorld().spawnEntity(splitLoc, EntityType.MAGMA_CUBE);
            smallCube.setCustomName("§cMagma Cube §7[Lv20]");
            smallCube.setCustomNameVisible(true);
            smallCube.setSize(4); // Medium size
            smallCube.setHealth(300.0);
            smallCube.setMaxHealth(300.0);
        }
    }
    
    private void createLavaPools(Location center) {
        // Create lava pools in a pattern around the boss
        for (int i = 0; i < 6; i++) {
            double angle = (i * Math.PI * 2) / 6;
            Location lavaLoc = center.clone().add(
                Math.cos(angle) * 6,
                -1,
                Math.sin(angle) * 6
            );
            
            // Place lava
            lavaLoc.getBlock().setType(Material.LAVA);
            
            // Add fire particles
            lavaLoc.getWorld().spawnParticle(Particle.LAVA, lavaLoc, 30, 1, 1, 1, 0.1);
        }
    }
    
    @Override
    public void onDeath(LivingEntity entity) {
        Location loc = entity.getLocation();
        
        // Death effects
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 2, 0, 0, 0, 0);
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 200, 6, 6, 6, 0.3);
        loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_DEATH, 1.0f, 0.8f);
        
        // Drop magma-related items
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.MAGMA_CREAM, 8));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.BLAZE_POWDER, 5));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_BRICK, 10));
        
        // Drop special boss item
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.FIRE_CHARGE, 3));
        
        // Notify all players in the area
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 50.0) {
                player.sendMessage("§c§lDer Magma Cube Boss wurde besiegt! Magma-Belohnungen freigeschaltet!");
            }
        }
    }
}
