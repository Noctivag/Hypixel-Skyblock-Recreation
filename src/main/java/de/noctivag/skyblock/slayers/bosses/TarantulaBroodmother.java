package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TarantulaBroodmother extends SlayerBoss {

    private int webTrapCooldown = 0;
    private int spiderSwarmCooldown = 0;
    private int poisonAuraCooldown = 0;
    private final List<Spider> spawnedSpiders = new ArrayList<>();

    public TarantulaBroodmother(SkyblockPluginRefactored plugin, Location spawnLocation, int tier) {
        super(plugin, "Tarantula Broodmother", spawnLocation, tier, 400 + (tier * 150), 25 + (tier * 8));
    }

    @Override
    protected void spawnBoss() {
        Spider spider = (Spider) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SPIDER);
        
        // Make it look like a tarantula
        spider.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        
        setupEntity(spider);
    }

    @Override
    public void onSpawn() {
        sendMessageToNearbyPlayers("§5§lTARANTULA BROODMOTHER §7[Tier " + tier + "] §7ist erwacht!");
        sendMessageToNearbyPlayers("§5§lVORSICHT: §7Diese Spinne ist extrem giftig!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Tarantula Broodmother Tier " + tier + " spawned at " + spawnLocation);
        }
    }

    @Override
    public void onDeath(Player killer) {
        sendMessageToNearbyPlayers("§5§lTARANTULA BROODMOTHER §7wurde von " + killer.getName() + " besiegt!");
        sendMessageToNearbyPlayers("§a§lSlayer-Quest abgeschlossen!");
        
        // Clean up spawned spiders
        cleanupSpawnedSpiders();
        
        dropLoot(killer);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Tarantula Broodmother Tier " + tier + " killed by " + killer.getName());
        }
    }

    @Override
    public void onTick() {
        if (entity == null) return;
        
        // Update cooldowns
        if (webTrapCooldown > 0) webTrapCooldown--;
        if (spiderSwarmCooldown > 0) spiderSwarmCooldown--;
        if (poisonAuraCooldown > 0) poisonAuraCooldown--;
        
        Player nearestPlayer = getNearestPlayer(15);
        if (nearestPlayer == null) return;
        
        // Use abilities based on health percentage
        double healthPercentage = entity.getHealth() / maxHealth;
        
        if (healthPercentage > 0.6) {
            // Phase 1: Web traps
            if (webTrapCooldown <= 0) {
                castWebTrap(nearestPlayer);
            }
        } else if (healthPercentage > 0.3) {
            // Phase 2: Add spider swarm
            if (webTrapCooldown <= 0) {
                castWebTrap(nearestPlayer);
            }
            if (spiderSwarmCooldown <= 0) {
                summonSpiderSwarm();
            }
        } else {
            // Phase 3: All abilities + poison aura
            if (webTrapCooldown <= 0) {
                castWebTrap(nearestPlayer);
            }
            if (spiderSwarmCooldown <= 0) {
                summonSpiderSwarm();
            }
            if (poisonAuraCooldown <= 0) {
                castPoisonAura();
            }
        }
        
        // Basic attack
        if (isNearPlayer(nearestPlayer, 3)) {
            damagePlayer(nearestPlayer, damage);
            nearestPlayer.sendMessage("§5Tarantula Broodmother beißt dich!");
        }
    }

    private void castWebTrap(Player target) {
        webTrapCooldown = 80; // 4 seconds
        
        sendMessageToNearbyPlayers("§5§lTarantula Broodmother §7schießt §fNetze §7auf " + target.getName() + "!");
        
        // Apply slowness effect
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 100, -3, false, false));
        
        target.sendMessage("§f§lNETZ-FALLE! §7Du bist in Netzen gefangen!");
    }

    private void summonSpiderSwarm() {
        spiderSwarmCooldown = 120; // 6 seconds
        
        sendMessageToNearbyPlayers("§5§lTarantula Broodmother §7beschwört einen §cSpinnen-Schwarm §7!");
        
        // Spawn poisonous spiders
        for (int i = 0; i < 4; i++) {
            spawnTarantulaVermin();
        }
    }

    private void spawnTarantulaVermin() {
        Location spawnLoc = entity.getLocation().clone().add(
            random.nextDouble() * 10 - 5,
            0,
            random.nextDouble() * 10 - 5
        );
        
        Spider vermin = (Spider) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);
        vermin.setCustomName("§cTarantula Vermin");
        vermin.setCustomNameVisible(true);
        vermin.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
        vermin.setHealth(30);
        vermin.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
        
        // Make vermin poisonous
        vermin.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0, false, false));
        
        spawnedSpiders.add(vermin);
    }

    private void castPoisonAura() {
        poisonAuraCooldown = 100; // 5 seconds
        
        sendMessageToNearbyPlayers("§5§lTarantula Broodmother §7erzeugt eine §2Gift-Aura §7!");
        
        // Apply poison to all nearby players
        for (Player player : entity.getWorld().getPlayers()) {
            if (isNearPlayer(player, 8)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 3, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 1, false, false));
                player.sendMessage("§2§lGIFT-AURA! §7Du erleidest starken Giftschaden!");
            }
        }
    }

    private void cleanupSpawnedSpiders() {
        for (Spider spider : spawnedSpiders) {
            if (spider != null && !spider.isDead()) {
                spider.remove();
            }
        }
        spawnedSpiders.clear();
    }

    @Override
    protected void dropCustomLoot(Player killer, Location dropLocation) {
        // Guaranteed drops
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.STRING, 8 + tier * 2));
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.SPIDER_EYE, 3 + tier));
        
        // RNG Drops
        if (random.nextDouble() < 0.15) { // 15% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.ARROW, 16));
            killer.sendMessage("§a§lRNG Drop: §5Toxic Arrow Poison!");
        }
        
        if (random.nextDouble() < 0.08) { // 8% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.SPIDER_EYE, 1));
            killer.sendMessage("§a§lRNG Drop: §5Tarantula Talisman!");
        }
        
        if (random.nextDouble() < 0.05) { // 5% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.STICK, 1));
            killer.sendMessage("§a§lRNG Drop: §5Fly Swatter!");
        }
        
        if (random.nextDouble() < 0.02) { // 2% chance
            dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.FERMENTED_SPIDER_EYE, 1));
            killer.sendMessage("§a§lRNG Drop: §5Digested Mosquito!");
        }
    }
}
