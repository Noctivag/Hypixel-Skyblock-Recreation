package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Storm - Floor 7 Wither Lord (Phase 2)
 * The Mage
 * Features:
 * - Extreme magical attacks
 * - Teleportation
 * - Wither skull barrages
 * - Lightning strikes
 */
public class Storm extends DungeonBoss {

    private boolean isChanneling = false;
    private int witherSkullCount = 0;

    public Storm(Location spawnLocation) {
        super("STORM", spawnLocation, "CATACOMBS", 7);

        // Set Storm-specific stats - High damage, lower defense
        setMaxHealth(250000.0);
        setDamage(1500.0);
        setDefense(200.0);
        setCombatXP(30000.0);
    }

    @Override
    public String getName() {
        return "§c§lSTORM §7[Floor 7]";
    }

    @Override
    public String getLootTableId() {
        return "storm_floor_7";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // High speed and regeneration
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));

        // Start wither skull barrage
        startWitherSkullBarrage();

        // Start lightning attacks
        startLightningAttacks();

        // Start teleportation
        startTeleportation();

        // Start channeling attacks
        startChannelingMechanic();
    }

    /**
     * Wither skull barrage
     */
    private void startWitherSkullBarrage() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                if (!(entity instanceof Wither)) return;

                Wither wither = (Wither) entity;

                // Fire 10 wither skulls
                for (int i = 0; i < 10; i++) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (entity == null || !entity.isValid() || entity.isDead()) {
                                cancel();
                                return;
                            }

                            // Find random player
                            Player target = null;
                            for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(50, 50, 50)) {
                                if (nearby instanceof Player) {
                                    target = (Player) nearby;
                                    break;
                                }
                            }

                            if (target != null) {
                                // Shoot wither skull
                                wither.launchProjectile(
                                    org.bukkit.entity.WitherSkull.class,
                                    target.getLocation().toVector()
                                        .subtract(entity.getLocation().toVector())
                                        .normalize()
                                );

                                witherSkullCount++;
                            }
                        }
                    }.runTaskLater(SkyblockPlugin.getInstance(), i * 5L);
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 80L, 80L);
    }

    /**
     * Lightning attacks
     */
    private void startLightningAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Strike 5 random nearby locations
                for (int i = 0; i < 5; i++) {
                    Location strikeLoc = entity.getLocation().clone().add(
                        (Math.random() - 0.5) * 30,
                        0,
                        (Math.random() - 0.5) * 30
                    );

                    strikeLoc.getWorld().strikeLightning(strikeLoc);

                    // Damage nearby players
                    for (org.bukkit.entity.Entity nearby : strikeLoc.getWorld().getNearbyEntities(strikeLoc, 3, 10, 3)) {
                        if (nearby instanceof Player) {
                            Player player = (Player) nearby;
                            player.damage(getDamage() * 0.8);
                            player.sendMessage("§c⚡ Storm strikes you with lightning!");
                        }
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Teleportation mechanic
     */
    private void startTeleportation() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Teleport to random location
                Location newLoc = entity.getLocation().clone().add(
                    (Math.random() - 0.5) * 25,
                    Math.random() * 10,
                    (Math.random() - 0.5) * 25
                );

                // Particle effects
                entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 100);
                entity.teleport(newLoc);
                entity.getWorld().spawnParticle(Particle.PORTAL, newLoc, 100);
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 120L, 120L);
    }

    /**
     * Channeling mechanic - Storm channels a massive attack
     */
    private void startChannelingMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Start channeling
                isChanneling = true;

                entity.getWorld().getPlayers().forEach(p ->
                    p.sendMessage("§c§lSTORM: §7I CHANNEL THE POWER OF THE STORM!")
                );

                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));

                // Charging particles
                new BukkitRunnable() {
                    int ticks = 0;

                    @Override
                    public void run() {
                        if (entity == null || !entity.isValid() || entity.isDead() || ticks >= 5) {
                            cancel();
                            if (ticks >= 5) {
                                unleashChanneledAttack();
                            }
                            return;
                        }

                        entity.getWorld().spawnParticle(
                            Particle.CRIT_MAGIC,
                            entity.getLocation().add(0, 2, 0),
                            100,
                            3, 3, 3
                        );

                        ticks++;
                    }
                }.runTaskTimer(SkyblockPlugin.getInstance(), 0L, 20L);
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 200L, 200L);
    }

    /**
     * Unleash channeled attack
     */
    private void unleashChanneledAttack() {
        if (entity == null || !entity.isValid() || entity.isDead()) return;

        isChanneling = false;
        entity.removePotionEffect(PotionEffectType.SLOW);

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lSTORM: §7WITNESS TRUE POWER!")
        );

        // Massive lightning storm
        for (int i = 0; i < 20; i++) {
            Location strikeLoc = entity.getLocation().clone().add(
                (Math.random() - 0.5) * 40,
                0,
                (Math.random() - 0.5) * 40
            );

            strikeLoc.getWorld().strikeLightning(strikeLoc);
        }

        // Damage all players in the arena
        for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(50, 50, 50)) {
            if (nearby instanceof Player) {
                Player player = (Player) nearby;
                player.damage(getDamage() * 1.5);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 2));
            }
        }

        // Visual effect
        entity.getWorld().spawnParticle(
            Particle.EXPLOSION_HUGE,
            entity.getLocation(),
            50,
            15, 5, 15
        );
    }
}
