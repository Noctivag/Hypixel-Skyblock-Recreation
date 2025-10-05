package de.noctivag.skyblock.mobs.zones;

import de.noctivag.skyblock.enums.ZoneType;
import de.noctivag.skyblock.mobs.ZoneMob;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.PlayerProfileService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;

/**
 * Goblin - Dwarven Mines Mob
 * Stiehlt Geld vom Spieler bei einem Treffer
 */
public class Goblin extends ZoneMob {
    
    private final Plugin plugin;
    
    public Goblin(Plugin plugin) {
        super("§aGoblin", EntityType.ZOMBIE, ZoneType.DWARVEN_MINES, 8, 80.0, 25.0, 15, 8);
        this.plugin = plugin;
    }
    
    @Override
    public LivingEntity spawn(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        
        // Customize appearance
        zombie.setCustomName("§aGoblin §7[Lv8]");
        zombie.setCustomNameVisible(true);
        zombie.setHealth(health);
        zombie.setMaxHealth(health);
        
        // Goblin equipment
        zombie.getEquipment().setHelmet(new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET));
        zombie.getEquipment().setChestplate(new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setLeggings(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS));
        zombie.getEquipment().setBoots(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS));
        
        // Goblin weapon
        zombie.getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
        
        return zombie;
    }
    
    @Override
    public void useAbility(LivingEntity entity) {
        // Goblin ability: Steal coins from nearby players
        Location loc = entity.getLocation();
        
        // Create gold particles
        loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 30, 2, 2, 2, 0.1);
        
        // Play sound
        loc.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.2f);
        
        // Steal coins from nearby players
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 3.0) {
                stealCoins(player);
            }
        }
    }
    
    private void stealCoins(Player player) {
        try {
            PlayerProfileService playerProfileService = null;
            if (plugin instanceof SkyblockPluginRefactored) {
                playerProfileService = ((SkyblockPluginRefactored) plugin).getServiceManager()
                    .getService(PlayerProfileService.class);
            }
            
            if (playerProfileService != null) {
                PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
                if (profile != null) {
                    double coinsToSteal = Math.min(profile.getCoins() * 0.05, 100); // Steal 5% or max 100 coins
                    if (coinsToSteal > 0) {
                        profile.removeCoins(coinsToSteal);
                        player.sendMessage("§cDer Goblin hat " + String.format("%.0f", coinsToSteal) + " Coins gestohlen!");
                        
                        // Visual effect
                        player.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, 
                            player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error stealing coins from player: " + e.getMessage());
        }
    }
    
    @Override
    public void onDeath(LivingEntity entity) {
        Location loc = entity.getLocation();
        
        // Death effects
        loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 50, 2, 2, 2, 0.2);
        loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_DEATH, 1.0f, 1.5f);
        
        // Drop goblin-related items
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.GOLD_NUGGET, 3));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.EMERALD, 1));
    }
}
