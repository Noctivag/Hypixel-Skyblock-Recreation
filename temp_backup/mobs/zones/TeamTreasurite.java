package de.noctivag.skyblock.mobs.zones;

import de.noctivag.skyblock.enums.ZoneType;
import de.noctivag.skyblock.mobs.ZoneMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Team Treasurite - Crystal Hollows Mob
 * Golem-artige Mobs, die sich einen gemeinsamen Lebensbalken teilen
 */
public class TeamTreasurite extends ZoneMob {
    
    private static int sharedHealth = 1000; // Shared health pool
    private static int maxSharedHealth = 1000;
    
    public TeamTreasurite() {
        super("§6Team Treasurite", EntityType.IRON_GOLEM, ZoneType.CRYSTAL_HOLLOWS, 25, 200.0, 80.0, 60, 40);
    }
    
    @Override
    public LivingEntity spawn(Location location) {
        IronGolem golem = (IronGolem) location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);
        
        // Customize appearance
        golem.setCustomName("§6Team Treasurite §7[Lv25] §8(" + sharedHealth + "/" + maxSharedHealth + " HP)");
        golem.setCustomNameVisible(true);
        golem.setHealth(health);
        golem.setMaxHealth(health);
        
        // Treasurite equipment
        golem.getEquipment().setHelmet(new org.bukkit.inventory.ItemStack(Material.GOLDEN_HELMET));
        golem.getEquipment().setChestplate(new org.bukkit.inventory.ItemStack(Material.GOLDEN_CHESTPLATE));
        golem.getEquipment().setLeggings(new org.bukkit.inventory.ItemStack(Material.GOLDEN_LEGGINGS));
        golem.getEquipment().setBoots(new org.bukkit.inventory.ItemStack(Material.GOLDEN_BOOTS));
        
        return golem;
    }
    
    @Override
    public void useAbility(LivingEntity entity) {
        // Team Treasurite ability: Shared health regeneration
        Location loc = entity.getLocation();
        
        // Create golden particles
        loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 50, 3, 3, 3, 0.1);
        
        // Play sound
        loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_LAND, 1.0f, 0.8f);
        
        // Regenerate shared health
        if (sharedHealth < maxSharedHealth) {
            sharedHealth = Math.min(sharedHealth + 50, maxSharedHealth);
            
            // Update all Team Treasurite names
            for (LivingEntity nearby : loc.getWorld().getLivingEntities()) {
                if (nearby.getCustomName() != null && nearby.getCustomName().contains("Team Treasurite")) {
                    nearby.setCustomName("§6Team Treasurite §7[Lv25] §8(" + sharedHealth + "/" + maxSharedHealth + " HP)");
                }
            }
            
            // Notify nearby players
            for (Player player : loc.getWorld().getPlayers()) {
                if (player.getLocation().distance(loc) <= 10.0) {
                    player.sendMessage("§6Team Treasurite regeneriert sich!");
                }
            }
        }
    }
    
    @Override
    public void onDeath(LivingEntity entity) {
        Location loc = entity.getLocation();
        
        // Reduce shared health
        sharedHealth = Math.max(sharedHealth - 200, 0);
        
        // Death effects
        loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 100, 4, 4, 4, 0.2);
        loc.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH, 1.0f, 0.8f);
        
        // Drop treasure-related items
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.GOLD_INGOT, 3));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.EMERALD, 2));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.DIAMOND, 1));
        
        // Check if all Team Treasurites are defeated
        if (sharedHealth <= 0) {
            // Spawn special reward
            loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 1));
            
            // Notify all players in the area
            for (Player player : loc.getWorld().getPlayers()) {
                if (player.getLocation().distance(loc) <= 50.0) {
                    player.sendMessage("§6§lAlle Team Treasurites wurden besiegt! Spezielle Belohnung freigeschaltet!");
                }
            }
            
            // Reset shared health for next spawn
            sharedHealth = maxSharedHealth;
        }
    }
    
    /**
     * Gibt die aktuelle geteilte Gesundheit zurück
     */
    public static int getSharedHealth() {
        return sharedHealth;
    }
    
    /**
     * Setzt die geteilte Gesundheit zurück
     */
    public static void resetSharedHealth() {
        sharedHealth = maxSharedHealth;
    }
}
