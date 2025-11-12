package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Sadan - Floor 6 dungeon boss
 * Features:
 * - Multi-phase transformation
 * - Terracotta summoning
 * - Giant form
 * - Area denial mechanics
 */
public class Sadan extends DungeonBoss {

    private boolean isInPhase2 = false;
    private boolean isInPhase3 = false;
    private boolean isInGiantForm = false;
    private List<LivingEntity> terracottas = new ArrayList<>();
    private Giant giantForm = null;

    public Sadan(Location spawnLocation) {
        super("SADAN", spawnLocation, "CATACOMBS", 6);

        // Set Sadan-specific stats
        setMaxHealth(100000.0);
        setDamage(750.0);
        setDefense(200.0);
        setCombatXP(15000.0);
    }

    @Override
    public String getName() {
        return "§cSadan §7[Floor 6]";
    }

    @Override
    public String getLootTableId() {
        return "sadan_floor_6";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Start with defensive buffs
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));

        // Start terracotta summoning
        startTerracottaSummoning();

        // Start ground slam attacks
        startGroundSlamAttacks();

        // Start health monitoring for phases
        startPhaseMonitoring();
    }

    /**
     * Terracotta summoning mechanic
     */
    private void startTerracottaSummoning() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Clean up dead terracottas
                terracottas.removeIf(t -> t == null || !t.isValid() || t.isDead());

                // Summon terracottas if under limit
                if (terracottas.size() < 8) {
                    summonTerracotta();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Summon a terracotta warrior
     */
    private void summonTerracotta() {
        Location spawnLoc = entity.getLocation().clone().add(
            (Math.random() - 0.5) * 15,
            0,
            (Math.random() - 0.5) * 15
        );

        LivingEntity terracotta = (LivingEntity) entity.getWorld().spawnEntity(
            spawnLoc,
            EntityType.ZOMBIE
        );

        terracotta.setCustomName("§6Terracotta Warrior");
        terracotta.setCustomNameVisible(true);
        terracotta.setMaxHealth(8000.0);
        terracotta.setHealth(8000.0);
        terracotta.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));

        terracottas.add(terracotta);
    }

    /**
     * Ground slam attack mechanic
     */
    private void startGroundSlamAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Create ground slam
                Location slamLoc = entity.getLocation();

                // Visual effect
                slamLoc.getWorld().spawnParticle(
                    Particle.EXPLOSION_LARGE,
                    slamLoc,
                    10,
                    5, 1, 5
                );

                // Damage nearby players
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(8, 8, 8)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(getDamage() * 0.8);
                        player.setVelocity(player.getLocation().toVector()
                            .subtract(slamLoc.toVector())
                            .normalize()
                            .multiply(2)
                            .setY(1));

                        player.sendMessage("§c§lSadan: §7GROUND SLAM!");
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 120L, 120L);
    }

    /**
     * Phase monitoring
     */
    private void startPhaseMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                double healthPercent = (entity.getHealth() / getMaxHealth()) * 100;

                if (healthPercent <= 66 && !isInPhase2) {
                    triggerPhase2();
                }

                if (healthPercent <= 33 && !isInPhase3) {
                    triggerPhase3();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Phase 2: Enhanced terracottas
     */
    private void triggerPhase2() {
        isInPhase2 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lSadan: §7My army will overwhelm you!")
        );

        // Summon wave of terracottas
        for (int i = 0; i < 6; i++) {
            summonTerracotta();
        }

        // Increase damage
        setDamage(getDamage() * 1.4);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    }

    /**
     * Phase 3: Giant transformation
     */
    private void triggerPhase3() {
        isInPhase3 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lSadan: §7WITNESS MY TRUE FORM!")
        );

        // Transform into giant
        Location giantLoc = entity.getLocation().clone();

        // Remove old form
        entity.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, giantLoc, 5);

        // Create giant form
        giantForm = (Giant) entity.getWorld().spawnEntity(giantLoc, EntityType.GIANT);
        giantForm.setCustomName("§c§lSADAN THE GIANT");
        giantForm.setCustomNameVisible(true);
        giantForm.setMaxHealth(150000.0);
        giantForm.setHealth(150000.0);
        giantForm.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        giantForm.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4));
        giantForm.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));

        isInGiantForm = true;

        // Start giant-specific mechanics
        startGiantMechanics();

        // Remove old entity
        if (entity != null && entity.isValid()) {
            entity.remove();
        }
    }

    /**
     * Giant form mechanics
     */
    private void startGiantMechanics() {
        // Constant area damage aura
        new BukkitRunnable() {
            @Override
            public void run() {
                if (giantForm == null || !giantForm.isValid() || giantForm.isDead()) {
                    cancel();
                    return;
                }

                // Damage aura
                for (org.bukkit.entity.Entity nearby : giantForm.getNearbyEntities(15, 15, 15)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(500.0);

                        // Particle effect
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            player.getLocation().add(0, 1, 0),
                            30
                        );
                    }
                }

                // Visual aura
                giantForm.getWorld().spawnParticle(
                    Particle.REDSTONE,
                    giantForm.getLocation().add(0, 5, 0),
                    100,
                    5, 5, 5,
                    new Particle.DustOptions(org.bukkit.Color.RED, 2)
                );
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);

        // Massive ground slams
        new BukkitRunnable() {
            @Override
            public void run() {
                if (giantForm == null || !giantForm.isValid() || giantForm.isDead()) {
                    cancel();
                    return;
                }

                Location slamLoc = giantForm.getLocation();

                // Massive explosion
                slamLoc.getWorld().spawnParticle(
                    Particle.EXPLOSION_HUGE,
                    slamLoc,
                    20,
                    10, 2, 10
                );

                // Damage all nearby players
                for (org.bukkit.entity.Entity nearby : giantForm.getNearbyEntities(20, 20, 20)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(1000.0);
                        player.setVelocity(player.getLocation().toVector()
                            .subtract(slamLoc.toVector())
                            .normalize()
                            .multiply(3)
                            .setY(2));
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 160L, 160L);
    }

    /**
     * Cleanup on death
     */
    public void onDeath() {
        // Remove all terracottas
        terracottas.forEach(t -> {
            if (t != null && t.isValid()) {
                t.remove();
            }
        });
        terracottas.clear();

        // Remove giant form if exists
        if (giantForm != null && giantForm.isValid()) {
            giantForm.getWorld().spawnParticle(
                Particle.EXPLOSION_HUGE,
                giantForm.getLocation(),
                10
            );
            giantForm.remove();
        }
    }
}
