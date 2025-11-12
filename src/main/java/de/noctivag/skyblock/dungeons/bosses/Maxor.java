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
 * Maxor - Floor 7 Wither Lord (Phase 1)
 * The Tank
 * Features:
 * - Extreme defense and health
 * - Knockback immunity
 * - Area denial with explosions
 * - Shield mechanics
 */
public class Maxor extends DungeonBoss {

    private boolean hasShield = true;
    private int shieldHealth = 50000;
    private boolean isEnraged = false;

    public Maxor(Location spawnLocation) {
        super("MAXOR", spawnLocation, "CATACOMBS", 7);

        // Set Maxor-specific stats - Extremely tanky
        setMaxHealth(300000.0);
        setDamage(800.0);
        setDefense(500.0);
        setCombatXP(25000.0);
    }

    @Override
    public String getName() {
        return "§c§lMAXOR §7[Floor 7]";
    }

    @Override
    public String getLootTableId() {
        return "maxor_floor_7";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Make it a Wither for visual effect
        if (entity instanceof Wither) {
            ((Wither) entity).setInvulnerabilityTicks(0);
        }

        // Extreme defensive buffs
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 4));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2)); // Slow but tanky

        // Start shield mechanic
        startShieldMechanic();

        // Start explosion attacks
        startExplosionAttacks();

        // Start knockback attacks
        startKnockbackAttacks();

        // Monitor health for enrage
        startHealthMonitoring();
    }

    /**
     * Shield mechanic - must break shield before damaging Maxor
     */
    private void startShieldMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                if (hasShield) {
                    // Display shield health
                    entity.setCustomName("§c§lMAXOR §7[§b" + shieldHealth + " Shield§7]");

                    // Shield particles
                    entity.getWorld().spawnParticle(
                        Particle.BARRIER,
                        entity.getLocation().add(0, 2, 0),
                        20,
                        1, 1, 1
                    );
                } else {
                    entity.setCustomName("§c§lMAXOR");
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 10L, 10L);
    }

    /**
     * Explosion attack mechanic
     */
    private void startExplosionAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Random explosions around the arena
                for (int i = 0; i < 5; i++) {
                    Location explosionLoc = entity.getLocation().clone().add(
                        (Math.random() - 0.5) * 20,
                        0,
                        (Math.random() - 0.5) * 20
                    );

                    explosionLoc.getWorld().spawnParticle(
                        Particle.EXPLOSION_HUGE,
                        explosionLoc,
                        3
                    );

                    // Damage nearby players
                    for (org.bukkit.entity.Entity nearby : explosionLoc.getWorld().getNearbyEntities(explosionLoc, 5, 5, 5)) {
                        if (nearby instanceof Player) {
                            Player player = (Player) nearby;
                            player.damage(getDamage() * 0.7);
                        }
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Knockback attack mechanic
     */
    private void startKnockbackAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Massive knockback wave
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(15, 15, 15)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;

                        // Push away from Maxor
                        player.setVelocity(
                            player.getLocation().toVector()
                                .subtract(entity.getLocation().toVector())
                                .normalize()
                                .multiply(3)
                                .setY(2)
                        );

                        player.sendMessage("§c§lMAXOR: §7GET BACK!");
                    }
                }

                // Visual effect
                entity.getWorld().spawnParticle(
                    Particle.EXPLOSION_LARGE,
                    entity.getLocation(),
                    20,
                    5, 2, 5
                );
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 140L, 140L);
    }

    /**
     * Health monitoring for enrage
     */
    private void startHealthMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                double healthPercent = (entity.getHealth() / getMaxHealth()) * 100;

                if (healthPercent <= 25 && !isEnraged) {
                    triggerEnrage();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Enrage mode
     */
    private void triggerEnrage() {
        isEnraged = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lMAXOR: §7YOU DARE WOUND ME?! FEEL MY WRATH!")
        );

        // Remove slow, add speed
        entity.removePotionEffect(PotionEffectType.SLOW);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

        // Increase damage significantly
        setDamage(getDamage() * 2.5);

        // Constant explosion aura
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Constant explosions
                entity.getWorld().spawnParticle(
                    Particle.EXPLOSION_LARGE,
                    entity.getLocation(),
                    10,
                    3, 3, 3
                );

                // Damage aura
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(10, 10, 10)) {
                    if (nearby instanceof Player) {
                        ((Player) nearby).damage(getDamage() * 0.5);
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Damage shield
     */
    public void damageShield(int damage) {
        if (hasShield) {
            shieldHealth -= damage;
            if (shieldHealth <= 0) {
                hasShield = false;
                entity.getWorld().getPlayers().forEach(p ->
                    p.sendMessage("§c§lMAXOR's §7shield has been broken!")
                );

                // Massive explosion when shield breaks
                entity.getWorld().spawnParticle(
                    Particle.EXPLOSION_HUGE,
                    entity.getLocation(),
                    20,
                    5, 5, 5
                );
            }
        }
    }

    /**
     * Is invulnerable if shield is up
     */
    public boolean isInvulnerable() {
        return hasShield;
    }
}
