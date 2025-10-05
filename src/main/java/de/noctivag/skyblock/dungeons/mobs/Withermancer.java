package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Withermancer extends DungeonMob {

    private final Random random = new Random();
    private int deathRayCooldown = 0;
    private int witherAuraCooldown = 0;
    private int summonCooldown = 0;

    public Withermancer(SkyblockPluginRefactored plugin, Location spawnLocation, int floor) {
        super(plugin, "Withermancer", spawnLocation, floor, 250 + (floor * 60), 40 + (floor * 12));
    }

    @Override
    protected void spawnBoss() {
        WitherSkeleton witherSkeleton = (WitherSkeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);
        
        // Make it look like a withermancer
        witherSkeleton.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        witherSkeleton.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        witherSkeleton.getEquipment().setItemInMainHand(new ItemStack(Material.STICK)); // Magic staff
        
        setupEntity(witherSkeleton);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§8§lWithermancer §7[Floor " + floor + "] §7ist erwacht!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Withermancer Floor " + floor + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§8§lWithermancer §7wurde von " + killer.getName() + " besiegt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Withermancer Floor " + floor + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (deathRayCooldown > 0) deathRayCooldown--;
        if (witherAuraCooldown > 0) witherAuraCooldown--;
        if (summonCooldown > 0) summonCooldown--;
        
        Player nearestPlayer = getNearestPlayer(20);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.6) {
            // Phase 1: Death ray
            if (deathRayCooldown <= 0) {
                castDeathRay(nearestPlayer);
            }
        } else if (healthPercentage > 0.3) {
            // Phase 2: Add wither aura
            if (deathRayCooldown <= 0) {
                castDeathRay(nearestPlayer);
            }
            if (witherAuraCooldown <= 0) {
                castWitherAura();
            }
        } else {
            // Phase 3: All abilities + summon
            if (deathRayCooldown <= 0) {
                castDeathRay(nearestPlayer);
            }
            if (witherAuraCooldown <= 0) {
                castWitherAura();
            }
            if (summonCooldown <= 0) {
                summonMinions();
            }
        }
        
        // Basic attack
        if (isNearPlayer(nearestPlayer, 4)) {
            damagePlayer(nearestPlayer, damage);
            nearestPlayer.sendMessage("§8Withermancer greift dich an!");
        }
    }

    private void castDeathRay(Player target) {
        deathRayCooldown = 120; // 6 seconds
        
        sendMessageToNearbyPlayers("§8§lWithermancer §7wirkt §4Todesstrahl §7auf " + target.getName() + "!");
        
        // Apply wither effect to target
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 2, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false));
        
        // Deal damage after 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (target.isOnline() && isNearPlayer(target, 15)) {
                    double rayDamage = damage * 1.5;
                    damagePlayer(target, rayDamage);
                    target.sendMessage("§4§lTODESSTRAHL! §7Du erleidest " + rayDamage + " Schaden!");
                }
            }
        }.runTaskLater(plugin, 40L); // 2 seconds
        
        target.sendMessage("§4§lVORSICHT: §7Withermancer bereitet einen Todesstrahl vor!");
    }

    private void castWitherAura() {
        witherAuraCooldown = 150; // 7.5 seconds
        
        sendMessageToNearbyPlayers("§8§lWithermancer §7erzeugt eine §4Wither-Aura §7!");
        
        // Apply wither effect to all nearby players
        for (Player player : entity.getWorld().getPlayers()) {
            if (isNearPlayer(player, 10)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 300, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 2, false, false));
                player.sendMessage("§4§lWITHER-AURA! §7Du erleidest Wither-Schaden!");
            }
        }
    }

    private void summonMinions() {
        summonCooldown = 200; // 10 seconds
        
        sendMessageToNearbyPlayers("§8§lWithermancer §7beschwört §4Wither-Skelette §7!");
        
        // Spawn wither skeletons
        for (int i = 0; i < 2; i++) {
            Location spawnLoc = entity.getLocation().clone().add(
                random.nextDouble() * 8 - 4,
                0,
                random.nextDouble() * 8 - 4
            );
            
            WitherSkeleton minion = (WitherSkeleton) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
            minion.setCustomName("§4Withermancer's Minion");
            minion.setCustomNameVisible(true);
            minion.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
            minion.setHealth(50);
            minion.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        }
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop wither-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.WITHER_SKELETON_SKULL, 1));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.BONE, 4 + floor));
        
        // Chance for better loot
        if (random.nextDouble() < 0.25) { // 25% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.COAL, 3 + floor));
            killer.sendMessage("§a§lDungeon-Loot: §8Coal!");
        }
        
        if (random.nextDouble() < 0.15) { // 15% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.STICK, 1));
            killer.sendMessage("§a§lDungeon-Loot: §8Withermancer's Staff!");
        }
        
        if (random.nextDouble() < 0.08) { // 8% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ENCHANTED_BOOK, 1));
            killer.sendMessage("§a§lDungeon-Loot: §dWithermancer Enchanted Book!");
        }
        
        if (random.nextDouble() < 0.03) { // 3% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.NETHER_STAR, 1));
            killer.sendMessage("§a§lDungeon-Loot: §dNether Star!");
        }
    }
}
