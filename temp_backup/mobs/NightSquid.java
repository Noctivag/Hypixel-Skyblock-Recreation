package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightSquid extends CustomMob {

    public NightSquid(SkyblockPluginRefactored plugin) {
        super(plugin, "Night Squid", EntityType.SQUID, 150, 20, 2, 15, 8);
    }

    @Override
    public void spawn(Location location) {
        Squid squid = (Squid) location.getWorld().spawnEntity(location, EntityType.SQUID);
        setupEntity(squid);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§5§lNight Squid §7erscheint aus den Tiefen!");
        
        // Apply night vision effect to make it more menacing
        if (entity != null) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Night Squid spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§5§lNight Squid §7wurde von " + killer.getName() + " besiegt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Night Squid killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Player player : entity.getWorld().getPlayers()) {
            double distance = entity.getLocation().distance(player.getLocation());
            if (distance < nearestDistance && distance <= 15) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        
        if (nearestPlayer != null) {
            if (nearestDistance <= 4) {
                // Attack player with ink effect
                damagePlayer(nearestPlayer, damage);
                nearestPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, false, false));
                nearestPlayer.sendMessage("§5Night Squid spritzt Tinte auf dich!");
            } else if (nearestDistance <= 8) {
                // Ink cloud attack
                if (random.nextDouble() < 0.3) { // 30% chance every second
                    nearestPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false, false));
                    nearestPlayer.sendMessage("§5Night Squid erzeugt eine Tintenwolke!");
                }
            }
        }
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop squid-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.INK_SAC, 3));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.PRISMARINE_CRYSTALS, 1));
        
        // Chance for ink-related items
        if (random.nextDouble() < 0.15) { // 15% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.BLACK_DYE, 2));
            killer.sendMessage("§a§lSeltene Beute: §5Black Dye!");
        }
        
        // Very rare chance for special item
        if (random.nextDouble() < 0.05) { // 5% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.NAUTILUS_SHELL, 1));
            killer.sendMessage("§a§lSehr seltene Beute: §bNautilus Shell!");
        }
    }
}
