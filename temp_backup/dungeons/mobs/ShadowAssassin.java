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

public class ShadowAssassin extends DungeonMob {

    private final Random random = new Random();
    private int stealthCooldown = 0;
    private int teleportCooldown = 0;
    private boolean isStealthed = false;

    public ShadowAssassin(SkyblockPluginRefactored plugin, Location spawnLocation, int floor) {
        super(plugin, "Shadow Assassin", spawnLocation, floor, 150 + (floor * 40), 30 + (floor * 8));
    }

    @Override
    protected void spawnBoss() {
        Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
        
        // Make it look like a shadow assassin
        skeleton.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
        
        setupEntity(skeleton);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§8§lShadow Assassin §7[Floor " + floor + "] §7ist erwacht!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Shadow Assassin Floor " + floor + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§8§lShadow Assassin §7wurde von " + killer.getName() + " besiegt!");
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Shadow Assassin Floor " + floor + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (stealthCooldown > 0) stealthCooldown--;
        if (teleportCooldown > 0) teleportCooldown--;
        
        Player nearestPlayer = getNearestPlayer(20);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.6) {
            // Phase 1: Stealth and basic attacks
            if (stealthCooldown <= 0) {
                activateStealth();
            }
        } else {
            // Phase 2: Add teleportation
            if (stealthCooldown <= 0) {
                activateStealth();
            }
            if (teleportCooldown <= 0) {
                performTeleport(nearestPlayer);
            }
        }
        
        // Basic attack (more damage when stealthed)
        if (isNearPlayer(nearestPlayer, 3)) {
            double attackDamage = isStealthed ? damage * 1.5 : damage;
            damagePlayer(nearestPlayer, attackDamage);
            nearestPlayer.sendMessage("§8Shadow Assassin greift dich an!");
            
            if (isStealthed) {
                nearestPlayer.sendMessage("§c§lKRITISCHER TREFFER! §7Du wurdest aus dem Schatten angegriffen!");
            }
        }
    }

    private void activateStealth() {
        stealthCooldown = 120; // 6 seconds
        isStealthed = true;
        
        sendMessageToNearbyPlayers("§8§lShadow Assassin §7verschwindet im §8Schatten §7!");
        
        // Apply invisibility effect
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, false, false));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false));
        
        // End stealth after 5 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                isStealthed = false;
                sendMessageToNearbyPlayers("§8§lShadow Assassin §7erscheint wieder aus dem Schatten!");
            }
        }.runTaskLater(plugin, 100L); // 5 seconds
    }

    private void performTeleport(Player target) {
        teleportCooldown = 100; // 5 seconds
        
        sendMessageToNearbyPlayers("§8§lShadow Assassin §7teleportiert sich zu " + target.getName() + "!");
        
        // Teleport behind player
        Location teleportLocation = target.getLocation().clone();
        teleportLocation.setYaw(target.getLocation().getYaw() + 180); // Behind player
        teleportLocation.add(target.getLocation().getDirection().multiply(-2)); // 2 blocks behind
        
        entity.teleport(teleportLocation);
        
        // Apply speed effect for quick attack
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false));
        
        target.sendMessage("§8§lVORSICHT: §7Shadow Assassin teleportiert sich zu dir!");
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Drop shadow-related items
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ENDER_PEARL, 1 + floor));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.LEATHER, 3 + floor));
        
        // Chance for shadow assassin armor pieces
        if (random.nextDouble() < 0.2) { // 20% chance
            Material[] armorPieces = {Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
            Material armorPiece = armorPieces[random.nextInt(armorPieces.length)];
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(armorPiece, 1));
            killer.sendMessage("§a§lDungeon-Loot: §8Shadow Assassin " + armorPiece.name() + "!");
        }
        
        if (random.nextDouble() < 0.1) { // 10% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.IRON_SWORD, 1));
            killer.sendMessage("§a§lDungeon-Loot: §8Shadow Assassin Sword!");
        }
        
        if (random.nextDouble() < 0.05) { // 5% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ENCHANTED_BOOK, 1));
            killer.sendMessage("§a§lDungeon-Loot: §dShadow Assassin Enchanted Book!");
        }
    }
}
