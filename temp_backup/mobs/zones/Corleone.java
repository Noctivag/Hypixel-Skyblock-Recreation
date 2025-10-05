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

/**
 * Corleone - Crystal Hollows Mini-Boss
 * Ein seltener Mini-Boss, der wertvolle Items droppt
 */
public class Corleone extends ZoneMob {
    
    public Corleone() {
        super("§6§lCorleone", EntityType.ZOMBIE, ZoneType.CRYSTAL_HOLLOWS, 50, 2000.0, 150.0, 200, 500);
    }
    
    @Override
    public LivingEntity spawn(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        
        // Customize appearance
        zombie.setCustomName("§6§lCorleone §7[Lv50] §cBOSS");
        zombie.setCustomNameVisible(true);
        zombie.setHealth(health);
        zombie.setMaxHealth(health);
        
        // Boss equipment
        zombie.getEquipment().setHelmet(new org.bukkit.inventory.ItemStack(Material.DIAMOND_HELMET));
        zombie.getEquipment().setChestplate(new org.bukkit.inventory.ItemStack(Material.DIAMOND_CHESTPLATE));
        zombie.getEquipment().setLeggings(new org.bukkit.inventory.ItemStack(Material.DIAMOND_LEGGINGS));
        zombie.getEquipment().setBoots(new org.bukkit.inventory.ItemStack(Material.DIAMOND_BOOTS));
        zombie.getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(Material.DIAMOND_SWORD));
        
        // Make it glow
        zombie.setGlowing(true);
        
        return zombie;
    }
    
    @Override
    public void useAbility(LivingEntity entity) {
        // Corleone ability: Summon minions and create crystal barriers
        Location loc = entity.getLocation();
        
        // Create crystal particles
        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 100, 5, 5, 5, 0.1);
        
        // Play sound
        loc.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 0.8f);
        
        // Summon crystal minions
        for (int i = 0; i < 3; i++) {
            Location minionLoc = loc.clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            
            // Spawn crystal minion (simplified as zombie)
            Zombie minion = (Zombie) minionLoc.getWorld().spawnEntity(minionLoc, EntityType.ZOMBIE);
            minion.setCustomName("§bCrystal Minion §7[Lv20]");
            minion.setCustomNameVisible(true);
            minion.setHealth(100.0);
            minion.setMaxHealth(100.0);
            minion.getEquipment().setHelmet(new org.bukkit.inventory.ItemStack(Material.AMETHYST_BLOCK));
        }
        
        // Create crystal barrier
        createCrystalBarrier(loc);
        
        // Notify nearby players
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 15.0) {
                player.sendMessage("§6§lCorleone beschwört Kristall-Minions und erstellt Barrieren!");
            }
        }
    }
    
    private void createCrystalBarrier(Location center) {
        // Create a ring of crystal blocks around the boss
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI * 2) / 8;
            Location barrierLoc = center.clone().add(
                Math.cos(angle) * 5,
                0,
                Math.sin(angle) * 5
            );
            
            // Place crystal block
            barrierLoc.getBlock().setType(Material.AMETHYST_BLOCK);
            
            // Add particles
            barrierLoc.getWorld().spawnParticle(Particle.END_ROD, barrierLoc, 20, 0.5, 0.5, 0.5, 0.1);
        }
    }
    
    @Override
    public void onDeath(LivingEntity entity) {
        Location loc = entity.getLocation();
        
        // Death effects
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 3, 0, 0, 0, 0);
        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 200, 6, 6, 6, 0.3);
        loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_DEATH, 1.0f, 0.8f);
        
        // Drop valuable items
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.DIAMOND, 5));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.EMERALD, 8));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 2));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.AMETHYST_SHARD, 10));
        
        // Drop special boss item
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.ECHO_SHARD, 1));
        
        // Notify all players in the area
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 50.0) {
                player.sendMessage("§6§lCorleone wurde besiegt! Wertvolle Belohnungen freigeschaltet!");
            }
        }
    }
}
