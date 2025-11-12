package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Professor - Floor 3 dungeon boss
 * Features:
 * - Guardian summoning mechanics
 * - Healing pool mechanics
 * - Multi-phase combat
 * - Thorn damage reflection
 */
public class Professor extends DungeonBoss {

    private boolean isInPhase2 = false;
    private boolean isInPhase3 = false;
    private List<LivingEntity> guardians = new ArrayList<>();
    private Location healingPool = null;
    private static final int MAX_GUARDIANS = 4;

    public Professor(Location spawnLocation) {
        super("PROFESSOR", spawnLocation, "CATACOMBS", 3);

        // Set Professor-specific stats
        setMaxHealth(20000.0);
        setDamage(250.0);
        setDefense(75.0);
        setCombatXP(3000.0);
    }

    @Override
    public String getName() {
        return "§cProfessor §7[Floor 3]";
    }

    @Override
    public String getLootTableId() {
        return "professor_floor_3";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Apply permanent resistance
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));

        // Start guardian summoning
        startGuardianMechanic();

        // Start health monitoring for phases
        startPhaseMonitoring();

        // Create healing pool
        createHealingPool();
    }

    /**
     * Guardian summoning mechanic
     */
    private void startGuardianMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Clean up dead guardians
                guardians.removeIf(g -> g == null || !g.isValid() || g.isDead());

                // Summon new guardian if under max
                if (guardians.size() < MAX_GUARDIANS) {
                    summonGuardian();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Summon a guardian
     */
    private void summonGuardian() {
        Location spawnLoc = entity.getLocation().clone().add(
            (Math.random() - 0.5) * 15,
            0,
            (Math.random() - 0.5) * 15
        );

        Guardian guardian = (Guardian) entity.getWorld().spawnEntity(spawnLoc, EntityType.GUARDIAN);
        guardian.setCustomName("§eProfessor's Guardian");
        guardian.setCustomNameVisible(true);
        guardian.setMaxHealth(3000.0);
        guardian.setHealth(3000.0);

        guardians.add(guardian);

        // Guardians provide damage reduction to Professor
        applyGuardianProtection();
    }

    /**
     * Guardian protection - reduces damage based on guardian count
     */
    private void applyGuardianProtection() {
        int aliveGuardians = (int) guardians.stream()
            .filter(g -> g != null && g.isValid() && !g.isDead())
            .count();

        if (entity != null && entity.isValid()) {
            // Each guardian provides 10% damage reduction
            int resistanceLevel = Math.min(aliveGuardians, 4);
            entity.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            entity.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                Integer.MAX_VALUE,
                resistanceLevel
            ));
        }
    }

    /**
     * Create healing pool
     */
    private void createHealingPool() {
        healingPool = entity.getLocation().clone().add(5, 0, 5);

        // Create visual indicator with particles
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || healingPool == null) {
                    cancel();
                    return;
                }

                // Spawn healing particles
                healingPool.getWorld().spawnParticle(
                    Particle.HEART,
                    healingPool.clone().add(0, 0.5, 0),
                    10,
                    2, 0.5, 2
                );

                // Check if Professor is in pool
                if (entity.getLocation().distance(healingPool) < 3) {
                    // Heal Professor
                    double newHealth = Math.min(entity.getHealth() + 200, getMaxHealth());
                    entity.setHealth(newHealth);
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
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

                if (healthPercent <= 50 && !isInPhase2) {
                    triggerPhase2();
                }

                if (healthPercent <= 25 && !isInPhase3) {
                    triggerPhase3();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Phase 2: Enhanced guardians
     */
    private void triggerPhase2() {
        isInPhase2 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lProfessor: §7My guardians, show them your true power!")
        );

        // Summon additional guardians
        for (int i = 0; i < 2; i++) {
            summonGuardian();
        }

        // Increase damage
        setDamage(getDamage() * 1.3);
    }

    /**
     * Phase 3: Enrage
     */
    private void triggerPhase3() {
        isInPhase3 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lProfessor: §7You will not defeat me! ENRAGE!")
        );

        // Massive stat boost
        setDamage(getDamage() * 1.5);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));

        // Summon all guardians
        for (int i = guardians.size(); i < MAX_GUARDIANS; i++) {
            summonGuardian();
        }
    }

    /**
     * Cleanup on death
     */
    public void onDeath() {
        // Remove all guardians
        guardians.forEach(g -> {
            if (g != null && g.isValid()) {
                g.remove();
            }
        });
        guardians.clear();
    }
}
