package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bonzo extends DungeonBoss implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final List<Player> nearbyPlayers = new ArrayList<>();
    private int phase = 1;
    private boolean isInvulnerable = false;
    private long lastAbilityTime = 0;
    private final Random random = new Random();

    public Bonzo(SkyblockPluginRefactored plugin, Location spawnLocation) {
        super("Bonzo", spawnLocation, 1000, 50); // 1000 HP, 50 damage
        this.plugin = plugin;
        
        // Register this as an event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void spawnBoss() {
        if (spawnLocation == null) {
            plugin.getLogger().severe("Cannot spawn Bonzo: spawn location is null");
            return;
        }

        // Spawn a large slime as Bonzo
        Slime bonzo = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SLIME);
        bonzo.setSize(4); // Large slime
        bonzo.setCustomName("§c§lBonzo §7[§a" + maxHealth + "§7/§a" + maxHealth + "§7]");
        bonzo.setCustomNameVisible(true);
        bonzo.setRemoveWhenFarAway(false);
        
        // Set boss attributes
        bonzo.setMaxHealth(maxHealth);
        bonzo.setHealth(maxHealth);
        // Attribute werden über setMaxHealth gesetzt
        
        this.bossEntity = bonzo;
        this.currentHealth = maxHealth;
        
        // Start boss AI
        startBossAI();
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Bonzo spawned at " + spawnLocation);
        }
    }

    private void startBossAI() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (bossEntity == null || bossEntity.isDead()) {
                    cancel();
                    return;
                }
                
                updateNearbyPlayers();
                performBossAbilities();
                updateBossHealth();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }

    private void updateNearbyPlayers() {
        nearbyPlayers.clear();
        if (bossEntity != null) {
            bossEntity.getNearbyEntities(20, 20, 20).forEach(entity -> {
                if (entity instanceof Player) {
                    nearbyPlayers.add((Player) entity);
                }
            });
        }
    }

    private void performBossAbilities() {
        long currentTime = System.currentTimeMillis();
        
        // Use abilities every 10 seconds
        if (currentTime - lastAbilityTime < 10000) {
            return;
        }
        
        lastAbilityTime = currentTime;
        
        if (nearbyPlayers.isEmpty()) {
            return;
        }
        
        // Choose random ability based on phase
        switch (phase) {
            case 1:
                if (random.nextBoolean()) {
                    spawnMinions();
                } else {
                    throwExplosiveCakes();
                }
                break;
            case 2:
                if (random.nextDouble() < 0.4) {
                    enterInvulnerabilityPhase();
                } else if (random.nextDouble() < 0.7) {
                    spawnMinions();
                } else {
                    throwExplosiveCakes();
                }
                break;
            case 3:
                // Phase 3: All abilities more frequently
                int ability = random.nextInt(3);
                switch (ability) {
                    case 0:
                        enterInvulnerabilityPhase();
                        break;
                    case 1:
                        spawnMinions();
                        break;
                    case 2:
                        throwExplosiveCakes();
                        break;
                }
                break;
        }
    }

    private void spawnMinions() {
        if (bossEntity == null) return;
        
        // Spawn 3-5 small slimes around Bonzo
        int minionCount = 3 + random.nextInt(3);
        
        for (int i = 0; i < minionCount; i++) {
            Location minionSpawn = bossEntity.getLocation().clone().add(
                random.nextDouble() * 10 - 5, // -5 to 5
                0,
                random.nextDouble() * 10 - 5
            );
            
            Slime minion = (Slime) minionSpawn.getWorld().spawnEntity(minionSpawn, EntityType.SLIME);
            minion.setSize(1); // Small slime
            minion.setCustomName("§eBonzo's Minion");
            minion.setCustomNameVisible(true);
            minion.setMaxHealth(50);
            minion.setHealth(50);
            // Attribute werden über setMaxHealth gesetzt
        }
        
        broadcastToNearbyPlayers("§cBonzo beschwört Minions!");
    }

    private void throwExplosiveCakes() {
        if (bossEntity == null) return;
        
        // Throw explosive cakes at nearby players
        for (Player player : nearbyPlayers) {
            if (player.getLocation().distance(bossEntity.getLocation()) <= 15) {
                Location targetLoc = player.getLocation().add(0, 1, 0);
                Vector direction = targetLoc.toVector().subtract(bossEntity.getLocation().toVector()).normalize();
                
                // Spawn a cake entity that will explode
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Create explosion at target location
                        targetLoc.getWorld().createExplosion(targetLoc, 3.0f, false, false);
                        
                        // Damage nearby players
                        targetLoc.getWorld().getNearbyEntities(targetLoc, 3, 3, 3).forEach(entity -> {
                            if (entity instanceof Player) {
                                ((Player) entity).damage(20, bossEntity);
                            }
                        });
                    }
                }.runTaskLater(plugin, 20L); // 1 second delay
            }
        }
        
        broadcastToNearbyPlayers("§cBonzo wirft explosive Kuchen!");
    }

    private void enterInvulnerabilityPhase() {
        if (isInvulnerable) return;
        
        isInvulnerable = true;
        broadcastToNearbyPlayers("§cBonzo wird unverwundbar und heilt sich!");
        
        // Make boss invulnerable for 5 seconds and heal
        new BukkitRunnable() {
            @Override
            public void run() {
                if (bossEntity != null && !bossEntity.isDead()) {
                    // Heal 20% of max health
                    double healAmount = maxHealth * 0.2;
                    double newHealth = Math.min(maxHealth, bossEntity.getHealth() + healAmount);
                    bossEntity.setHealth(newHealth);
                    currentHealth = newHealth;
                    
                    // Spawn healing particles (visual effect)
                    spawnHealingParticles();
                }
                
                isInvulnerable = false;
                broadcastToNearbyPlayers("§aBonzo ist wieder verwundbar!");
            }
        }.runTaskLater(plugin, 100L); // 5 seconds
    }

    private void spawnHealingParticles() {
        if (bossEntity == null) return;
        
        // Spawn some visual particles around the boss
        Location bossLoc = bossEntity.getLocation();
        for (int i = 0; i < 20; i++) {
            Location particleLoc = bossLoc.clone().add(
                random.nextDouble() * 4 - 2,
                random.nextDouble() * 4,
                random.nextDouble() * 4 - 2
            );
            // In a real implementation, you would spawn actual particles here
        }
    }

    private void updateBossHealth() {
        if (bossEntity == null || bossEntity.isDead()) return;
        
        currentHealth = bossEntity.getHealth();
        
        // Update phase based on health
        int newPhase = 1;
        if (currentHealth <= maxHealth * 0.3) {
            newPhase = 3;
        } else if (currentHealth <= maxHealth * 0.6) {
            newPhase = 2;
        }
        
        if (newPhase != phase) {
            phase = newPhase;
            broadcastToNearbyPlayers("§cBonzo erreicht Phase " + phase + "!");
        }
        
        // Update boss name with current health
        bossEntity.setCustomName("§c§lBonzo §7[§a" + String.format("%.0f", currentHealth) + "§7/§a" + maxHealth + "§7]");
    }

    private void broadcastToNearbyPlayers(String message) {
        nearbyPlayers.forEach(player -> player.sendMessage(message));
    }

    @EventHandler
    public void onBossDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() != bossEntity) return;
        
        // Make boss invulnerable during invulnerability phase
        if (isInvulnerable) {
            event.setCancelled(true);
            if (event.getDamager() instanceof Player) {
                ((Player) event.getDamager()).sendMessage("§cBonzo ist derzeit unverwundbar!");
            }
            return;
        }
        
        // Reduce damage in later phases
        double damage = event.getDamage();
        switch (phase) {
            case 2:
                event.setDamage(damage * 0.8); // 20% damage reduction
                break;
            case 3:
                event.setDamage(damage * 0.6); // 40% damage reduction
                break;
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        if (event.getEntity() != bossEntity) return;
        
        // Drop loot and complete dungeon
        dropBossLoot();
        completeDungeon();
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Bonzo defeated! Dungeon completed.");
        }
    }

    private void dropBossLoot() {
        if (bossEntity == null) return;
        
        Location dropLocation = bossEntity.getLocation();
        
        // Drop some coins
        dropLocation.getWorld().dropItem(dropLocation, new org.bukkit.inventory.ItemStack(Material.GOLD_INGOT, 10));
        
        // Drop dungeon-specific items
        dropLocation.getWorld().dropItem(dropLocation, new org.bukkit.inventory.ItemStack(Material.DIAMOND, 5));
        
        // In a real implementation, you would drop specific dungeon loot
        broadcastToNearbyPlayers("§aBonzo wurde besiegt! Loot wurde gedroppt!");
    }

    private void completeDungeon() {
        // Complete the dungeon for all nearby players
        nearbyPlayers.forEach(player -> {
            player.sendMessage("§a§lDungeon abgeschlossen!");
            player.sendMessage("§7Du erhältst Belohnungen für das Abschließen des Dungeons!");
            
            // In a real implementation, you would:
            // - Give dungeon completion rewards
            // - Update player statistics
            // - Teleport players back to hub
        });
    }

    public int getPhase() {
        return phase;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }
}
