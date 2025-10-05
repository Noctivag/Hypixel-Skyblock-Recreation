package de.noctivag.skyblock.mobs.zones;

import de.noctivag.skyblock.enums.ZoneType;
import de.noctivag.skyblock.mobs.ZoneMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Ice Walker - Dwarven Mines Mob
 * Greift in Gruppen an und verlangsamt Spieler
 */
public class IceWalker extends ZoneMob {
    
    public IceWalker() {
        super("§bIce Walker", EntityType.ZOMBIE, ZoneType.DWARVEN_MINES, 15, 200.0, 45.0, 25, 15);
    }
    
    @Override
    public LivingEntity spawn(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        
        // Customize appearance
        zombie.setCustomName("§bIce Walker §7[Lv15]");
        zombie.setCustomNameVisible(true);
        zombie.setHealth(health);
        zombie.setMaxHealth(health);
        
        // Ice Walker equipment
        zombie.getEquipment().setHelmet(new org.bukkit.inventory.ItemStack(Material.ICE));
        zombie.getEquipment().setChestplate(new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setLeggings(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS));
        zombie.getEquipment().setBoots(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS));
        
        // Add ice effect
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0));
        
        return zombie;
    }
    
    @Override
    public void useAbility(LivingEntity entity) {
        // Ice Walker ability: Freeze nearby players
        Location loc = entity.getLocation();
        
        // Create ice particles
        loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 50, 2, 2, 2, 0.1);
        
        // Play sound
        loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
        
        // Apply slowness to nearby players
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 5.0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                player.sendMessage("§bDu wurdest von einem Ice Walker verlangsamt!");
            }
        }
    }
    
    @Override
    public void onDeath(LivingEntity entity) {
        Location loc = entity.getLocation();
        
        // Death effects
        loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 100, 3, 3, 3, 0.2);
        loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_DEATH, 1.0f, 0.8f);
        
        // Drop ice-related items
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.ICE, 2));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.SNOWBALL, 3));
    }
}
