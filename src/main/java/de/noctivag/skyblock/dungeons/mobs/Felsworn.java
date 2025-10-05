package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.IronGolem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Felsworn extends DungeonMob {

    private final Random random = new Random();
    private int rockThrowCooldown = 0;
    private int groundSlamCooldown = 0;

    public Felsworn(SkyblockPluginRefactored plugin, Location spawnLocation, int floor) {
        super(plugin, "Felsworn", spawnLocation, floor, 300 + (floor * 80), 35 + (floor * 10));
    }

    @Override
    protected void spawnBoss() {
        IronGolem golem = (IronGolem) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.IRON_GOLEM);
        
        // Make it look like a felsworn
        golem.getEquipment().setHelmet(new ItemStack(Material.STONE_BRICKS));
        
        setupEntity(golem);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§7§lFelsworn §7[Floor " + floor + "] §7ist erwacht!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Felsworn Floor " + floor + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§7§lFelsworn §7wurde von " + killer.getName() + " besiegt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Felsworn Floor " + floor + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (rockThrowCooldown > 0) rockThrowCooldown--;
        if (groundSlamCooldown > 0) groundSlamCooldown--;
        
        Player nearestPlayer = getNearestPlayer(15);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.5) {
            // Phase 1: Rock throw
            if (rockThrowCooldown <= 0) {
                throwRock(nearestPlayer);
            }
        } else {
            // Phase 2: Add ground slam
            if (rockThrowCooldown <= 0) {
                throwRock(nearestPlayer);
            }
            if (groundSlamCooldown <= 0) {
                performGroundSlam();
            }
        }
        
        // Basic attack
        if (isNearPlayer(nearestPlayer, 3)) {
            damagePlayer(nearestPlayer, damage);
            nearestPlayer.sendMessage("§7Felsworn schlägt dich!");
        }
    }

    private void throwRock(Player target) {
        rockThrowCooldown = 100; // 5 seconds
        
        sendMessageToNearbyPlayers("§7§lFelsworn §7wirft einen §8Felsen §7auf " + target.getName() + "!");
        
        // Spawn falling rock (TNT) above target
        Location rockLocation = target.getLocation().clone().add(0, 10, 0);
        target.getWorld().spawnEntity(rockLocation, EntityType.PRIMED_TNT);
        
        // Apply mining fatigue to target
        target.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 1, false, false));
        
        target.sendMessage("§8§lFELSEN! §7Ein Felsen fällt auf dich herab!");
    }

    private void performGroundSlam() {
        groundSlamCooldown = 150; // 7.5 seconds
        
        sendMessageToNearbyPlayers("§7§lFelsworn §7führt einen §4Bodenschlag §7aus!");
        
        // Apply resistance effect to self
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1, false, false));
        
        // Damage all nearby players
        for (Player player : entity.getWorld().getPlayers()) {
            if (isNearPlayer(player, 8)) {
                double slamDamage = damage * 1.5;
                damagePlayer(player, slamDamage);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2, false, false));
                player.sendMessage("§4§lBODENSCHLAG! §7Du erleidest " + slamDamage + " Schaden!");
            }
        }
        
        // Create visual effect (spawn some stone blocks around)
        for (int i = 0; i < 5; i++) {
            Location effectLocation = entity.getLocation().clone().add(
                random.nextDouble() * 6 - 3,
                0,
                random.nextDouble() * 6 - 3
            );
            effectLocation.getBlock().setType(Material.COBBLESTONE);
            
            // Remove the block after 3 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    effectLocation.getBlock().setType(Material.AIR);
                }
            }.runTaskLater(plugin, 60L); // 3 seconds
        }
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop stone-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.COBBLESTONE, 8 + floor * 2));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.STONE, 4 + floor));
        
        // Chance for better loot
        if (random.nextDouble() < 0.2) { // 20% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.IRON_INGOT, 2 + floor));
            killer.sendMessage("§a§lDungeon-Loot: §7Iron Ingot!");
        }
        
        if (random.nextDouble() < 0.1) { // 10% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.STONE_BRICKS, 4));
            killer.sendMessage("§a§lDungeon-Loot: §7Stone Bricks!");
        }
        
        if (random.nextDouble() < 0.05) { // 5% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ENCHANTED_BOOK, 1));
            killer.sendMessage("§a§lDungeon-Loot: §dFelsworn Enchanted Book!");
        }
    }
}
