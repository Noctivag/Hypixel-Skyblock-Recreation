package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LostAdventurer extends DungeonMob {

    private final Random random = new Random();
    private int chargeCooldown = 0;
    private int shieldCooldown = 0;
    private boolean hasShield = false;

    public LostAdventurer(SkyblockPluginRefactored plugin, Location spawnLocation, int floor) {
        super(plugin, "Lost Adventurer", spawnLocation, floor, 200 + (floor * 50), 25 + (floor * 5));
    }

    @Override
    protected void spawnBoss() {
        Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
        
        // Make it look like a lost adventurer
        skeleton.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
        skeleton.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
        
        setupEntity(skeleton);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§7§lLost Adventurer §7[Floor " + floor + "] §7ist erwacht!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Lost Adventurer Floor " + floor + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§7§lLost Adventurer §7wurde von " + killer.getName() + " besiegt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Lost Adventurer Floor " + floor + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (chargeCooldown > 0) chargeCooldown--;
        if (shieldCooldown > 0) shieldCooldown--;
        
        Player nearestPlayer = getNearestPlayer(15);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.5) {
            // Phase 1: Basic attacks and charge
            if (chargeCooldown <= 0) {
                performCharge(nearestPlayer);
            }
        } else {
            // Phase 2: Add shield ability
            if (chargeCooldown <= 0) {
                performCharge(nearestPlayer);
            }
            if (shieldCooldown <= 0 && !hasShield) {
                activateShield();
            }
        }
        
        // Basic attack
        if (isNearPlayer(nearestPlayer, 3)) {
            damagePlayer(nearestPlayer, damage);
            nearestPlayer.sendMessage("§7Lost Adventurer greift dich an!");
        }
    }

    private void performCharge(Player target) {
        chargeCooldown = 80; // 4 seconds
        
        sendMessageToNearbyPlayers("§7§lLost Adventurer §7läuft auf " + target.getName() + " zu!");
        
        // Apply speed effect and charge towards player
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false));
        
        // Charge towards player
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) {
                    cancel();
                    return;
                }
                
                Player target = getNearestPlayer(10);
                if (target != null) {
                    // Move towards target
                    org.bukkit.util.Vector direction = target.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                    entity.setVelocity(direction.multiply(0.5).setY(0.2));
                }
            }
        }.runTaskTimer(plugin, 0L, 5L); // Every 0.25 seconds for 1.5 seconds
        
        target.sendMessage("§7§lVORSICHT: §7Lost Adventurer läuft auf dich zu!");
    }

    private void activateShield() {
        shieldCooldown = 200; // 10 seconds
        hasShield = true;
        
        sendMessageToNearbyPlayers("§7§lLost Adventurer §7aktiviert seinen §bSchutzschild §7!");
        
        // Apply resistance effect
        entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2, false, false));
        
        // Deactivate shield after 8 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                hasShield = false;
                sendMessageToNearbyPlayers("§7§lLost Adventurer §7Schutzschild ist abgelaufen!");
            }
        }.runTaskLater(plugin, 160L); // 8 seconds
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop adventurer-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.IRON_INGOT, 2 + floor));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ARROW, 8 + floor * 2));
        
        // Chance for better loot
        if (random.nextDouble() < 0.15) { // 15% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.DIAMOND, 1));
            killer.sendMessage("§a§lDungeon-Loot: §bDiamond!");
        }
        
        if (random.nextDouble() < 0.05) { // 5% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ENCHANTED_BOOK, 1));
            killer.sendMessage("§a§lDungeon-Loot: §dEnchanted Book!");
        }
    }
}
