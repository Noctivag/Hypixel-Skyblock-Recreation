package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

public class SeaWalker extends CustomMob {

    public SeaWalker(SkyblockPluginRefactored plugin) {
        super(plugin, "Sea Walker", EntityType.SKELETON, 100, 15, 1, 10, 5);
    }

    @Override
    public void spawn(Location location) {
        Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
        
        // Make it look like a sea creature
        skeleton.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        
        setupEntity(skeleton);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§b§lSea Walker §7ist aus dem Wasser aufgestiegen!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Sea Walker spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§b§lSea Walker §7wurde von " + killer.getName() + " besiegt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Sea Walker killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        // Sea Walker AI - moves slowly and attacks nearby players
        if (entity == null) return;
        
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Player player : entity.getWorld().getPlayers()) {
            double distance = entity.getLocation().distance(player.getLocation());
            if (distance < nearestDistance && distance <= 10) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        
        if (nearestPlayer != null && nearestDistance <= 3) {
            // Attack player
            damagePlayer(nearestPlayer, damage);
            nearestPlayer.sendMessage("§cSea Walker greift dich an!");
        }
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop sea-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.PRISMARINE_SHARD, 2));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.SPONGE, 1));
        
        // Small chance for rare drops
        if (random.nextDouble() < 0.1) { // 10% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.HEART_OF_THE_SEA, 1));
            killer.sendMessage("§a§lSeltene Beute: §bHeart of the Sea!");
        }
    }
}
