package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RevenantHorror extends SlayerBoss {

    private int pestCooldown = 0;
    private int tntRainCooldown = 0;
    private int zombieArmyCooldown = 0;
    private final List<Zombie> spawnedZombies = new ArrayList<>();

    public RevenantHorror(SkyblockPluginRefactored plugin, Location spawnLocation, int tier) {
        super(plugin, "Revenant Horror", spawnLocation, tier, 500 + (tier * 200), 30 + (tier * 10));
    }

    @Override
    protected void spawnBoss() {
        Zombie revenant = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
        
        // Make it look like a revenant
        revenant.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        revenant.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        revenant.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        revenant.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
        revenant.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
        
        setupEntity(revenant);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§c§lREVENANT HORROR §7[Tier " + tier + "] §7ist erwacht!");
        sendMessageToNearbyPlayers("§c§lVORSICHT: §7Dieser Boss ist extrem gefährlich!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Revenant Horror Tier " + tier + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§c§lREVENANT HORROR §7wurde von " + killer.getName() + " besiegt!");
        sendMessageToNearbyPlayers("§a§lSlayer-Quest abgeschlossen!");
        
        // Clean up spawned zombies
        cleanupSpawnedZombies();
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Revenant Horror Tier " + tier + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (pestCooldown > 0) pestCooldown--;
        if (tntRainCooldown > 0) tntRainCooldown--;
        if (zombieArmyCooldown > 0) zombieArmyCooldown--;
        
        Player nearestPlayer = getNearestPlayer(15);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.7) {
            // Phase 1: Basic attacks and pest
            if (pestCooldown <= 0) {
                castPest(nearestPlayer);
            }
        } else if (healthPercentage > 0.3) {
            // Phase 2: Add TNT Rain
            if (pestCooldown <= 0) {
                castPest(nearestPlayer);
            }
            if (tntRainCooldown <= 0) {
                castTntRain(nearestPlayer);
            }
        } else {
            // Phase 3: All abilities + Zombie Army
            if (pestCooldown <= 0) {
                castPest(nearestPlayer);
            }
            if (tntRainCooldown <= 0) {
                castTntRain(nearestPlayer);
            }
            if (zombieArmyCooldown <= 0) {
                summonZombieArmy();
            }
        }
        
        // Basic attack
        if (isNearPlayer(nearestPlayer, 3)) {
            damagePlayer(nearestPlayer, damage);
            nearestPlayer.sendMessage("§cRevenant Horror greift dich an!");
        }
    }

    private void castPest(Player target) {
        pestCooldown = 100; // 5 seconds
        
        sendMessageToNearbyPlayers("§c§lRevenant Horror §7wirkt §4Pest §7auf " + target.getName() + "!");
        
        // Apply pest effect
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1, false, false));
        
        // Heal boss
        double healAmount = maxHealth * 0.05; // Heal 5% of max health
        entity.setHealth(Math.min(maxHealth, entity.getHealth() + healAmount));
        
        target.sendMessage("§4§lPEST! §7Du erleidest Schaden über Zeit!");
    }

    private void castTntRain(Player target) {
        tntRainCooldown = 150; // 7.5 seconds
        
        sendMessageToNearbyPlayers("§c§lRevenant Horror §7beschwört §6TNT-Regen §7über " + target.getName() + "!");
        
        // Spawn TNT around target
        for (int i = 0; i < 5; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location tntLocation = target.getLocation().clone().add(
                        random.nextDouble() * 10 - 5, // -5 to 5
                        10, // Above player
                        random.nextDouble() * 10 - 5
                    );
                    
                    // Spawn TNT
                    target.getWorld().spawnEntity(tntLocation, EntityType.PRIMED_TNT);
                }
            }.runTaskLater(plugin, i * 10L); // Stagger the TNT spawns
        }
        
        target.sendMessage("§6§lTNT-Regen! §7TNT fällt vom Himmel!");
    }

    private void summonZombieArmy() {
        zombieArmyCooldown = 200; // 10 seconds
        
        sendMessageToNearbyPlayers("§c§lRevenant Horror §7beschwört eine §4Zombie-Armee §7!");
        
        // Spawn different types of zombies
        for (int i = 0; i < 3; i++) {
            spawnRevenantSycophant();
        }
        
        for (int i = 0; i < 2; i++) {
            spawnRevenantChampion();
        }
    }

    private void spawnRevenantSycophant() {
        Location spawnLoc = entity.getLocation().clone().add(
            random.nextDouble() * 8 - 4,
            0,
            random.nextDouble() * 8 - 4
        );
        
        Zombie sycophant = (Zombie) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
        sycophant.setCustomName("§cRevenant Sycophant");
        sycophant.setCustomNameVisible(true);
        sycophant.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
        sycophant.setHealth(50);
        sycophant.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
        
        spawnedZombies.add(sycophant);
    }

    private void spawnRevenantChampion() {
        Location spawnLoc = entity.getLocation().clone().add(
            random.nextDouble() * 8 - 4,
            0,
            random.nextDouble() * 8 - 4
        );
        
        Zombie champion = (Zombie) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
        champion.setCustomName("§4Revenant Champion");
        champion.setCustomNameVisible(true);
        champion.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
        champion.setHealth(100);
        champion.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20);
        
        // Give champion better equipment
        champion.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        champion.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        champion.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
        
        spawnedZombies.add(champion);
    }

    private void cleanupSpawnedZombies() {
        for (Zombie zombie : spawnedZombies) {
            if (zombie != null && !zombie.isDead()) {
                zombie.remove();
            }
        }
        spawnedZombies.clear();
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Guaranteed drops
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ROTTEN_FLESH, 5 + tier));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.POISONOUS_POTATO, 2 + tier));
        
        // RNG Drops
        if (random.nextDouble() < 0.1) { // 10% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.IRON_SWORD, 1));
            killer.sendMessage("§a§lRNG Drop: §cScythe Blade!");
        }
        
        if (random.nextDouble() < 0.05) { // 5% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ENCHANTED_BOOK, 1));
            killer.sendMessage("§a§lRNG Drop: §dSmite VII Book!");
        }
        
        if (random.nextDouble() < 0.02) { // 2% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.SKELETON_SKULL, 1));
            killer.sendMessage("§a§lRNG Drop: §cBeheaded Horror!");
        }
        
        if (random.nextDouble() < 0.01) { // 1% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.HEART_OF_THE_SEA, 1));
            killer.sendMessage("§a§lRNG Drop: §6Warden Heart!");
        }
    }
}
