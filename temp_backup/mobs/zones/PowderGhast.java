package de.noctivag.skyblock.mobs.zones;

import de.noctivag.skyblock.enums.ZoneType;
import de.noctivag.skyblock.mobs.ZoneMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Powder Ghast - Dwarven Mines Mob
 * Explodiert und gibt dem Spieler Mining Fatigue
 */
public class PowderGhast extends ZoneMob {
    
    public PowderGhast() {
        super("§ePowder Ghast", EntityType.GHAST, ZoneType.DWARVEN_MINES, 20, 300.0, 60.0, 40, 25);
    }
    
    @Override
    public LivingEntity spawn(Location location) {
        Ghast ghast = (Ghast) location.getWorld().spawnEntity(location, EntityType.GHAST);
        
        // Customize appearance
        ghast.setCustomName("§ePowder Ghast §7[Lv20]");
        ghast.setCustomNameVisible(true);
        ghast.setHealth(health);
        ghast.setMaxHealth(health);
        
        // Make it smaller and more aggressive
        ghast.setExplosionPower(2);
        
        return ghast;
    }
    
    @Override
    public void useAbility(LivingEntity entity) {
        // Powder Ghast ability: Explosion with mining fatigue
        Location loc = entity.getLocation();
        
        // Create explosion particles
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1, 0, 0, 0, 0);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 100, 5, 5, 5, 0.3);
        
        // Play sound
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
        
        // Apply mining fatigue to nearby players
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 8.0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 2));
                player.sendMessage("§eDu hast Mining Fatigue von einem Powder Ghast erhalten!");
                
                // Visual effect on player
                player.getWorld().spawnParticle(Particle.CLOUD, 
                    player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
            }
        }
        
        // Damage nearby entities
        for (LivingEntity nearby : loc.getWorld().getLivingEntities()) {
            if (nearby.getLocation().distance(loc) <= 6.0 && nearby != entity) {
                nearby.damage(damage * 0.5); // Half damage for explosion
            }
        }
    }
    
    @Override
    public void onDeath(LivingEntity entity) {
        Location loc = entity.getLocation();
        
        // Death effects
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1, 0, 0, 0, 0);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 200, 4, 4, 4, 0.2);
        loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_DEATH, 1.0f, 0.8f);
        
        // Drop powder-related items
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.GUNPOWDER, 5));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.GLOWSTONE_DUST, 3));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.REDSTONE, 4));
    }
}
