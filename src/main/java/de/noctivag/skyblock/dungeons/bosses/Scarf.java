package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Scarf - Floor 2 dungeon boss
 * Features:
 * - Teleportation mechanics
 * - Priest summoning
 * - Healing mechanics
 * - Phase-based combat
 */
public class Scarf extends DungeonBoss {

    private boolean isInPhase2 = false;
    private boolean isInPhase3 = false;
    private int priestsAlive = 0;
    private static final int MAX_PRIESTS = 4;

    public Scarf(Location spawnLocation) {
        super("SCARF", spawnLocation, "CATACOMBS", 2);

        // Set Scarf-specific stats
        setMaxHealth(12000.0);
        setDamage(200.0);
        setDefense(50.0);
        setCombatXP(2000.0);
    }

    @Override
    public String getName() {
        return "§cScarf §7[Floor 2]";
    }

    @Override
    public String getLootTableId() {
        return "scarf_floor_2";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Apply permanent speed effect
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        // Start teleportation mechanic
        startTeleportationMechanic();

        // Start health monitoring for phases
        startPhaseMonitoring();
    }

    /**
     * Teleportation mechanic - Scarf teleports around the arena
     */
    private void startTeleportationMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Teleport every 5 seconds
                Location newLoc = entity.getLocation().clone();
                newLoc.add(
                    (Math.random() - 0.5) * 20,
                    0,
                    (Math.random() - 0.5) * 20
                );

                // Particle effect at old location
                entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 50);

                entity.teleport(newLoc);

                // Particle effect at new location
                entity.getWorld().spawnParticle(Particle.PORTAL, newLoc, 50);
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Phase monitoring - trigger phase changes based on health
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

                // Phase 2 at 66% HP
                if (healthPercent <= 66 && !isInPhase2) {
                    triggerPhase2();
                }

                // Phase 3 at 33% HP
                if (healthPercent <= 33 && !isInPhase3) {
                    triggerPhase3();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Phase 2: Summon priests
     */
    private void triggerPhase2() {
        isInPhase2 = true;

        // Broadcast phase change
        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lScarf: §7You dare challenge me? My priests will aid me!")
        );

        // Summon priests
        summonPriests(2);
    }

    /**
     * Phase 3: Summon more priests and increase difficulty
     */
    private void triggerPhase3() {
        isInPhase3 = true;

        // Broadcast phase change
        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lScarf: §7This cannot be! I summon all my power!")
        );

        // Summon more priests
        summonPriests(2);

        // Increase boss stats
        setDamage(getDamage() * 1.5);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
    }

    /**
     * Summon priest minions
     */
    private void summonPriests(int count) {
        if (priestsAlive >= MAX_PRIESTS) return;

        for (int i = 0; i < count && priestsAlive < MAX_PRIESTS; i++) {
            Location spawnLoc = entity.getLocation().clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );

            LivingEntity priest = (LivingEntity) entity.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            priest.setCustomName("§ePriest of Scarf");
            priest.setCustomNameVisible(true);
            priest.setMaxHealth(2000.0);
            priest.setHealth(2000.0);

            // Priest provides healing to Scarf
            startPriestHealing(priest);

            priestsAlive++;
        }
    }

    /**
     * Priest healing mechanic
     */
    private void startPriestHealing(LivingEntity priest) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (priest == null || !priest.isValid() || priest.isDead()) {
                    priestsAlive = Math.max(0, priestsAlive - 1);
                    cancel();
                    return;
                }

                if (entity != null && entity.isValid() && !entity.isDead()) {
                    // Heal Scarf
                    double newHealth = Math.min(entity.getHealth() + 100, getMaxHealth());
                    entity.setHealth(newHealth);

                    // Healing particles
                    entity.getWorld().spawnParticle(Particle.HEART, entity.getLocation().add(0, 1, 0), 5);
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);
    }
}
