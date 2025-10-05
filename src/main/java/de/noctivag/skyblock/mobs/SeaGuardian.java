package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Guardian;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SeaGuardian extends CustomMob {

    private int laserCooldown = 0;
    private int shieldCooldown = 0;
    private boolean shieldActive = false;

    public SeaGuardian(SkyblockPluginRefactored plugin) {
        super(plugin, "Sea Guardian", EntityType.GUARDIAN, 300, 25, 3, 25, 15);
    }

    @Override
    public void spawn(Location location) {
        Guardian guardian = (Guardian) location.getWorld().spawnEntity(location, EntityType.GUARDIAN);
        setupEntity(guardian);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§b§lSea Guardian §7erhebt sich aus den Tiefen!");
        sendMessageToNearbyPlayers("§c§lVORSICHT: §7Dieser Boss ist sehr gefährlich!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Sea Guardian (mini-boss) spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§b§lSea Guardian §7wurde von " + killer.getName() + " besiegt!");
        sendMessageToNearbyPlayers("§a§lBoss-Beute wurde gedroppt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Sea Guardian killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (laserCooldown > 0) laserCooldown--;
        if (shieldCooldown > 0) shieldCooldown--;
        
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Player player : entity.getWorld().getPlayers()) {
            double distance = entity.getLocation().distance(player.getLocation());
            if (distance < nearestDistance && distance <= 20) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        
        if (nearestPlayer != null) {
            // Shield ability
            if (shieldCooldown <= 0 && entity.getHealth() < maxHealth * 0.5) {
                activateShield();
            }
            
            // Laser attack
            if (laserCooldown <= 0 && nearestDistance <= 15) {
                performLaserAttack(nearestPlayer);
            }
            
            // Basic attack
            if (nearestDistance <= 3) {
                damagePlayer(nearestPlayer, damage);
                nearestPlayer.sendMessage("§bSea Guardian greift dich an!");
            }
        }
    }

    private void activateShield() {
        shieldActive = true;
        shieldCooldown = 200; // 10 seconds
        
        // Apply resistance effect
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1, false, false));
        
        sendMessageToNearbyPlayers("§b§lSea Guardian §7aktiviert seinen Schutzschild!");
        
        // Deactivate shield after 5 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                shieldActive = false;
                sendMessageToNearbyPlayers("§b§lSea Guardian §7Schutzschild ist abgelaufen!");
            }
        }.runTaskLater(plugin, 100L); // 5 seconds
    }

    private void performLaserAttack(Player target) {
        laserCooldown = 100; // 5 seconds
        
        sendMessageToNearbyPlayers("§b§lSea Guardian §7feuert einen Laser auf " + target.getName() + "!");
        
        // Damage player after 2 seconds (laser charge time)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (target.isOnline() && isNearPlayer(target, 20)) {
                    double laserDamage = damage * 1.5; // 50% more damage
                    damagePlayer(target, laserDamage);
                    target.sendMessage("§c§lLaser-Treffer! §7Du erleidest " + laserDamage + " Schaden!");
                    
                    // Apply mining fatigue effect
                    target.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 1, false, false));
                }
            }
        }.runTaskLater(plugin, 40L); // 2 seconds
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop guardian-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.PRISMARINE_SHARD, 5));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.PRISMARINE_CRYSTALS, 3));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.SPONGE, 2));
        
        // Boss-specific drops
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.HEART_OF_THE_SEA, 1));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.NAUTILUS_SHELL, 1));
        
        // Rare chance for special items
        if (random.nextDouble() < 0.2) { // 20% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.TRIDENT, 1));
            killer.sendMessage("§a§lBoss-Beute: §bTrident!");
        }
        
        if (random.nextDouble() < 0.1) { // 10% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.CONDUIT, 1));
            killer.sendMessage("§a§lSehr seltene Boss-Beute: §bConduit!");
        }
    }
}
