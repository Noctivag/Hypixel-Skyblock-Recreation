package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Inferno Demonlord - Blaze Slayer Boss (Tier I-IV)
 * Features:
 * - Fire shield mechanic
 * - Demon minion summoning
 * - Fire pillar attacks
 * - Meteor shower
 * - Rage mode at low health
 */
public class InfernoDemonlord extends SlayerBoss {

    private boolean hasFireShield = true;
    private int shieldHealth = 10000;
    private List<org.bukkit.entity.LivingEntity> demons = new ArrayList<>();
    private boolean isRageMode = false;

    public InfernoDemonlord(Location spawnLocation, int tier) {
        super("INFERNO_DEMONLORD_" + tier, spawnLocation, tier, "BLAZE");

        // Set tier-specific stats
        switch (tier) {
            case 1:
                setMaxHealth(20000.0);
                setDamage(400.0);
                setDefense(75.0);
                setCombatXP(600.0);
                shieldHealth = 5000;
                break;
            case 2:
                setMaxHealth(75000.0);
                setDamage(1000.0);
                setDefense(200.0);
                setCombatXP(2500.0);
                shieldHealth = 15000;
                break;
            case 3:
                setMaxHealth(250000.0);
                setDamage(2500.0);
                setDefense(500.0);
                setCombatXP(10000.0);
                shieldHealth = 40000;
                break;
            case 4:
                setMaxHealth(750000.0);
                setDamage(6000.0);
                setDefense(1000.0);
                setCombatXP(40000.0);
                shieldHealth = 100000;
                break;
        }
    }

    @Override
    public String getName() {
        String[] tierNames = {"", "§6Inferno Demonlord I", "§6Inferno Demonlord II",
                             "§6Inferno Demonlord III", "§c§lInferno Demonlord IV"};
        return tierNames[getTier()];
    }

    @Override
    public String getLootTableId() {
        return "inferno_demonlord_tier_" + getTier();
    }

    @Override
    public void startBoss() {
        super.startBoss();

        if (entity == null || !entity.isValid()) return;

        // Fire resistance and damage
        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 255));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, getTier()));

        // Start fire shield
        startFireShieldMechanic();

        // Start fire attacks
        startFireAttacks();

        // Start demon summoning
        startDemonSummoning();

        // Start fire pillar attacks (Tier 2+)
        if (getTier() >= 2) {
            startFirePillars();
        }

        // Start meteor shower (Tier 3+)
        if (getTier() >= 3) {
            startMeteorShower();
        }

        // Monitor for rage mode
        startRageMonitoring();
    }

    /**
     * Fire shield mechanic
     */
    private void startFireShieldMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                if (hasFireShield) {
                    // Shield particles
                    entity.getWorld().spawnParticle(
                        Particle.FLAME,
                        entity.getLocation().add(0, 1, 0),
                        50,
                        1.5, 1.5, 1.5
                    );

                    // Damage nearby players
                    for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(4, 4, 4)) {
                        if (nearby instanceof Player) {
                            Player player = (Player) nearby;
                            player.setFireTicks(100);
                            player.damage(getDamage() * 0.2);
                        }
                    }

                    // Update shield display
                    entity.setCustomName(getName() + " §7[§e" + shieldHealth + " Shield§7]");
                } else {
                    entity.setCustomName(getName());
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 10L, 10L);
    }

    /**
     * Damage the fire shield
     */
    public void damageShield(int damage) {
        if (hasFireShield) {
            shieldHealth -= damage;
            if (shieldHealth <= 0) {
                hasFireShield = false;
                entity.getWorld().getPlayers().forEach(p ->
                    p.sendMessage("§c§lInferno Demonlord's §7fire shield has been destroyed!")
                );

                // Explosion when shield breaks
                entity.getWorld().spawnParticle(
                    Particle.EXPLOSION_HUGE,
                    entity.getLocation(),
                    10,
                    2, 2, 2
                );
            }
        }
    }

    /**
     * Fire attacks
     */
    private void startFireAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                if (!(entity instanceof Blaze)) return;

                // Shoot fireballs at nearby players
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(20, 20, 20)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;

                        // Launch fireball
                        org.bukkit.entity.Fireball fireball = entity.getWorld().spawn(
                            entity.getLocation().add(0, 1, 0),
                            org.bukkit.entity.Fireball.class
                        );

                        fireball.setDirection(
                            player.getLocation().toVector()
                                .subtract(entity.getLocation().toVector())
                                .normalize()
                        );

                        fireball.setYield(2.0f + getTier());
                        break; // One at a time
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 60L, 60L);
    }

    /**
     * Summon demons
     */
    private void startDemonSummoning() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Clean up dead demons
                demons.removeIf(d -> d == null || !d.isValid() || d.isDead());

                // Summon new demons if under limit
                if (demons.size() < getTier() * 2) {
                    summonDemon();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 120L, 120L);
    }

    /**
     * Summon a demon
     */
    private void summonDemon() {
        Location spawnLoc = entity.getLocation().clone().add(
            (Math.random() - 0.5) * 12,
            0,
            (Math.random() - 0.5) * 12
        );

        EntityType demonType = Math.random() > 0.5 ? EntityType.BLAZE : EntityType.WITHER_SKELETON;
        org.bukkit.entity.LivingEntity demon = (org.bukkit.entity.LivingEntity) entity.getWorld().spawnEntity(
            spawnLoc,
            demonType
        );

        demon.setCustomName("§6Infernal Demon");
        demon.setCustomNameVisible(true);
        demon.setMaxHealth(2000.0 * getTier());
        demon.setHealth(2000.0 * getTier());
        demon.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 255));
        demon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, getTier()));

        demons.add(demon);
    }

    /**
     * Fire pillar attacks
     */
    private void startFirePillars() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Create 5 fire pillars at random locations
                for (int i = 0; i < 5; i++) {
                    Location pillarLoc = entity.getLocation().clone().add(
                        (Math.random() - 0.5) * 25,
                        0,
                        (Math.random() - 0.5) * 25
                    );

                    createFirePillar(pillarLoc);
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 140L, 140L);
    }

    /**
     * Create a fire pillar
     */
    private void createFirePillar(Location location) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 5) {
                    cancel();
                    return;
                }

                // Fire particles going up
                for (int y = 0; y < 10; y++) {
                    location.getWorld().spawnParticle(
                        Particle.FLAME,
                        location.clone().add(0, y, 0),
                        20,
                        0.5, 0.2, 0.5
                    );
                }

                // Damage players in pillar
                for (org.bukkit.entity.Entity nearby : location.getWorld().getNearbyEntities(location, 2, 10, 2)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(getDamage() * 0.5);
                        player.setFireTicks(100);
                    }
                }

                ticks++;
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 0L, 10L);
    }

    /**
     * Meteor shower attack
     */
    private void startMeteorShower() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                entity.getWorld().getPlayers().forEach(p ->
                    p.sendMessage("§c§lInferno Demonlord: §7METEOR SHOWER!")
                );

                // Spawn 20 meteors
                for (int i = 0; i < 20; i++) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (entity == null || !entity.isValid() || entity.isDead()) {
                                cancel();
                                return;
                            }

                            Location meteorLoc = entity.getLocation().clone().add(
                                (Math.random() - 0.5) * 30,
                                15,
                                (Math.random() - 0.5) * 30
                            );

                            // Launch fireball downward
                            org.bukkit.entity.Fireball meteor = entity.getWorld().spawn(
                                meteorLoc,
                                org.bukkit.entity.Fireball.class
                            );

                            meteor.setDirection(new org.bukkit.util.Vector(0, -1, 0));
                            meteor.setYield(3.0f + getTier());
                        }
                    }.runTaskLater(SkyblockPlugin.getInstance(), i * 5L);
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 300L, 300L);
    }

    /**
     * Monitor for rage mode
     */
    private void startRageMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                double healthPercent = (entity.getHealth() / getMaxHealth()) * 100;

                if (healthPercent <= 20 && !isRageMode) {
                    activateRageMode();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Activate rage mode
     */
    private void activateRageMode() {
        isRageMode = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lInferno Demonlord: §7YOU HAVE ANGERED ME! BURN!")
        );

        // Massive damage boost
        setDamage(getDamage() * 2.5);

        // Speed boost
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));

        // Constant fire aura
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Fire aura particles
                entity.getWorld().spawnParticle(
                    Particle.FLAME,
                    entity.getLocation().add(0, 1, 0),
                    100,
                    3, 3, 3
                );

                // Damage all nearby players
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(8, 8, 8)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(getDamage() * 0.4);
                        player.setFireTicks(200);
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    @Override
    public void stopBoss() {
        super.stopBoss();

        // Remove all demons
        demons.forEach(demon -> {
            if (demon != null && demon.isValid()) {
                demon.remove();
            }
        });
        demons.clear();
    }
}
