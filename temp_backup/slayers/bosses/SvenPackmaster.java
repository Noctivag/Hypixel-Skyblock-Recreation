package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SvenPackmaster extends SlayerBoss {

    private int howlAuraCooldown = 0;
    private int agilityCooldown = 0;
    private int trueDamageCooldown = 0;
    private final List<Wolf> spawnedWolves = new ArrayList<>();
    private boolean isAgile = false;

    public SvenPackmaster(SkyblockPluginRefactored plugin, Location spawnLocation, int tier) {
        super(plugin, "Sven Packmaster", spawnLocation, tier, 600 + (tier * 250), 35 + (tier * 12));
    }

    @Override
    protected void spawnBoss() {
        Wolf wolf = (Wolf) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WOLF);
        
        // Make it look like a packmaster
        wolf.setTamed(false);
        wolf.setAngry(true);
        
        setupEntity(wolf);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§6§lSVEN PACKMASTER §7[Tier " + tier + "] §7ist erwacht!");
        sendMessageToNearbyPlayers("§6§lVORSICHT: §7Dieser Wolf ist extrem schnell und stark!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Sven Packmaster Tier " + tier + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§6§lSVEN PACKMASTER §7wurde von " + killer.getName() + " besiegt!");
        sendMessageToNearbyPlayers("§a§lSlayer-Quest abgeschlossen!");
        
        // Clean up spawned wolves
        cleanupSpawnedWolves();
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Sven Packmaster Tier " + tier + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (howlAuraCooldown > 0) howlAuraCooldown--;
        if (agilityCooldown > 0) agilityCooldown--;
        if (trueDamageCooldown > 0) trueDamageCooldown--;
        
        Player nearestPlayer = getNearestPlayer(20);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.7) {
            // Phase 1: Howl aura
            if (howlAuraCooldown <= 0) {
                castHowlAura();
            }
        } else if (healthPercentage > 0.4) {
            // Phase 2: Add agility
            if (howlAuraCooldown <= 0) {
                castHowlAura();
            }
            if (agilityCooldown <= 0) {
                castAgility();
            }
        } else {
            // Phase 3: All abilities + true damage
            if (howlAuraCooldown <= 0) {
                castHowlAura();
            }
            if (agilityCooldown <= 0) {
                castAgility();
            }
            if (trueDamageCooldown <= 0) {
                castTrueDamage(nearestPlayer);
            }
        }
        
        // Basic attack
        if (isNearPlayer(nearestPlayer, 3)) {
            double attackDamage = isAgile ? damage * 1.5 : damage; // More damage when agile
            damagePlayer(nearestPlayer, attackDamage);
            nearestPlayer.sendMessage("§6Sven Packmaster beißt dich!");
        }
    }

    private void castHowlAura() {
        howlAuraCooldown = 120; // 6 seconds
        
        sendMessageToNearbyPlayers("§6§lSven Packmaster §7heult und ruft ein §cRudel §7herbei!");
        
        // Spawn wolf followers
        for (int i = 0; i < 3; i++) {
            spawnSvenFollower();
        }
    }

    private void spawnSvenFollower() {
        Location spawnLoc = entity.getLocation().clone().add(
            random.nextDouble() * 12 - 6,
            0,
            random.nextDouble() * 12 - 6
        );
        
        Wolf follower = (Wolf) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.WOLF);
        follower.setCustomName("§cSven Follower");
        follower.setCustomNameVisible(true);
        follower.setTamed(false);
        follower.setAngry(true);
        follower.setMaxHealth(40);
        follower.setHealth(40);
        
        spawnedWolves.add(follower);
    }

    private void castAgility() {
        agilityCooldown = 100; // 5 seconds
        
        sendMessageToNearbyPlayers("§6§lSven Packmaster §7wird §eextrem schnell §7!");
        
        // Apply speed and jump boost
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3, false, false));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 200, 2, false, false));
        
        isAgile = true;
        
        // Make boss jump around
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead() || !isAgile) {
                    cancel();
                    return;
                }
                
                Player target = getNearestPlayer(15);
                if (target != null) {
                    // Jump towards player
                    Vector direction = target.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                    entity.setVelocity(direction.multiply(0.8).setY(0.5));
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // Every 0.5 seconds
        
        // End agility after 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                isAgile = false;
                sendMessageToNearbyPlayers("§6§lSven Packmaster §7wird wieder normal schnell.");
            }
        }.runTaskLater(plugin, 200L); // 10 seconds
    }

    private void castTrueDamage(Player target) {
        trueDamageCooldown = 150; // 7.5 seconds
        
        sendMessageToNearbyPlayers("§6§lSven Packmaster §7bereitet einen §cTrue Damage §7Angriff vor!");
        
        // Deal true damage after 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (target.isOnline() && isNearPlayer(target, 5)) {
                    // True damage ignores defense
                    double trueDamage = damage * 2; // Double damage
                    target.damage(trueDamage);
                    target.sendMessage("§c§lTRUE DAMAGE! §7" + trueDamage + " Schaden (ignoriert Verteidigung)!");
                    
                    // Apply weakness effect
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2, false, false));
                }
            }
        }.runTaskLater(plugin, 40L); // 2 seconds
        
        target.sendMessage("§c§lVORSICHT: §7Sven Packmaster bereitet True Damage vor!");
    }

    private void cleanupSpawnedWolves() {
        for (Wolf wolf : spawnedWolves) {
            if (wolf != null && !wolf.isDead()) {
                wolf.remove();
            }
        }
        spawnedWolves.clear();
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Guaranteed drops
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.BONE, 6 + tier * 2));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.LEATHER, 4 + tier));
        
        // RNG Drops
        if (random.nextDouble() < 0.12) { // 12% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.EGG, 1));
            killer.sendMessage("§a§lRNG Drop: §6Red Claw Egg!");
        }
        
        if (random.nextDouble() < 0.08) { // 8% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.CARROT, 1));
            killer.sendMessage("§a§lRNG Drop: §6Grizzly Bait!");
        }
        
        if (random.nextDouble() < 0.03) { // 3% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.REDSTONE, 1));
            killer.sendMessage("§a§lRNG Drop: §6Overflux Capacitor!");
        }
    }
}
