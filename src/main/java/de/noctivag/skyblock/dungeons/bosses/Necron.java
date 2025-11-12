package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Necron - Floor 7 Final Boss
 * The Wither King
 * Features:
 * - Multiple phases
 * - Summons all previous Wither Lords
 * - Wither shield mechanic
 * - Ultimate attacks
 * - Hardest boss in dungeons
 */
public class Necron extends DungeonBoss {

    private int currentPhase = 1;
    private boolean hasWitherShield = false;
    private List<DungeonBoss> summonedLords = new ArrayList<>();
    private boolean isInUltimate = false;

    public Necron(Location spawnLocation) {
        super("NECRON", spawnLocation, "CATACOMBS", 7);

        // Set Necron-specific stats - Ultimate boss
        setMaxHealth(1000000.0);
        setDamage(3000.0);
        setDefense(500.0);
        setCombatXP(100000.0);
    }

    @Override
    public String getName() {
        return "§4§lNECRON §7[WITHER KING]";
    }

    @Override
    public String getLootTableId() {
        return "necron_floor_7";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§4§l§kA§r §4§lNECRON§r§7: §4I AM INEVITABLE. YOU CANNOT DEFEAT ME!§r §4§l§kA")
        );

        // Start with all buffs
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 3));

        // Start phase monitoring
        startPhaseMonitoring();

        // Start wither attacks
        startWitherAttacks();

        // Start ultimate ability
        startUltimateAbility();
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

                if (healthPercent <= 75 && currentPhase == 1) {
                    triggerPhase2();
                } else if (healthPercent <= 50 && currentPhase == 2) {
                    triggerPhase3();
                } else if (healthPercent <= 25 && currentPhase == 3) {
                    triggerPhase4();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Phase 2: Summon Maxor
     */
    private void triggerPhase2() {
        currentPhase = 2;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§4§lNECRON: §7MAXOR! Aid your king!")
        );

        // Summon Maxor
        Location summonLoc = entity.getLocation().clone().add(10, 0, 0);
        Maxor maxor = new Maxor(summonLoc);
        maxor.spawn();
        maxor.startBossFight();
        summonedLords.add(maxor);

        // Increase difficulty
        setDamage(getDamage() * 1.3);
    }

    /**
     * Phase 3: Summon Storm
     */
    private void triggerPhase3() {
        currentPhase = 3;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§4§lNECRON: §7STORM! Show them your power!")
        );

        // Summon Storm
        Location summonLoc = entity.getLocation().clone().add(-10, 0, 0);
        Storm storm = new Storm(summonLoc);
        storm.spawn();
        storm.startBossFight();
        summonedLords.add(storm);

        // Activate wither shield
        activateWitherShield();
    }

    /**
     * Phase 4: Summon Goldor and go ENRAGE
     */
    private void triggerPhase4() {
        currentPhase = 4;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§4§lNECRON: §4ENOUGH! GOLDOR, COME FORTH! I WILL DESTROY YOU ALL!")
        );

        // Summon Goldor
        Location summonLoc = entity.getLocation().clone().add(0, 0, 10);
        Goldor goldor = new Goldor(summonLoc);
        goldor.spawn();
        goldor.startBossFight();
        // Auto-solve terminals for Goldor in this phase
        for (int i = 0; i < 7; i++) {
            goldor.solveTerminal();
        }
        summonedLords.add(goldor);

        // ENRAGE - Massive stat boost
        setDamage(getDamage() * 2.5);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 8));

        // Start rapid ultimate casting
        startRapidUltimates();
    }

    /**
     * Wither shield mechanic
     */
    private void activateWitherShield() {
        hasWitherShield = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§4§lNECRON: §7My wither shield protects me!")
        );

        new BukkitRunnable() {
            private int duration = 0;

            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || duration >= 20) {
                    hasWitherShield = false;
                    cancel();
                    return;
                }

                // Shield particles
                entity.getWorld().spawnParticle(
                    Particle.SPELL_WITCH,
                    entity.getLocation().add(0, 2, 0),
                    50,
                    2, 2, 2
                );

                // Absorb damage
                entity.setHealth(Math.min(entity.getHealth() + 5000, getMaxHealth()));

                duration++;
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 0L, 20L);
    }

    /**
     * Wither attacks
     */
    private void startWitherAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Apply wither to all nearby players
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.addPotionEffect(new PotionEffect(
                            PotionEffectType.WITHER,
                            200,
                            3 + currentPhase
                        ));

                        // Wither particles
                        player.getWorld().spawnParticle(
                            Particle.SMOKE_LARGE,
                            player.getLocation().add(0, 1, 0),
                            30
                        );
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 60L, 60L);
    }

    /**
     * Ultimate ability
     */
    private void startUltimateAbility() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                castUltimate();
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 300L, 300L);
    }

    /**
     * Cast ultimate ability
     */
    private void castUltimate() {
        isInUltimate = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§4§l§kA§r §4§lNECRON§r§7: §4WITNESS... THE END!§r §4§l§kA")
        );

        // Slow Necron
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10));

        // Charge up
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || ticks >= 5) {
                    cancel();
                    if (ticks >= 5) {
                        unleashUltimate();
                    }
                    return;
                }

                // Charging particles
                entity.getWorld().spawnParticle(
                    Particle.PORTAL,
                    entity.getLocation().add(0, 3, 0),
                    200,
                    5, 5, 5
                );

                entity.getWorld().spawnParticle(
                    Particle.FLAME,
                    entity.getLocation().add(0, 1, 0),
                    100,
                    3, 3, 3
                );

                ticks++;
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 0L, 20L);
    }

    /**
     * Unleash the ultimate attack
     */
    private void unleashUltimate() {
        isInUltimate = false;
        entity.removePotionEffect(PotionEffectType.SLOW);

        // Massive explosion
        entity.getWorld().spawnParticle(
            Particle.EXPLOSION_HUGE,
            entity.getLocation(),
            100,
            20, 10, 20
        );

        // Damage all players massively
        for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(50, 50, 50)) {
            if (nearby instanceof Player) {
                Player player = (Player) nearby;

                // Calculate distance-based damage
                double distance = player.getLocation().distance(entity.getLocation());
                double damageMultiplier = 1.0 - (distance / 50.0);
                double ultimateDamage = getDamage() * 5 * Math.max(damageMultiplier, 0.3);

                player.damage(ultimateDamage);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 300, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 3));

                player.sendMessage("§4§lNECRON's ultimate ability hit you for " + (int)ultimateDamage + " damage!");
            }
        }

        // Lightning strikes
        for (int i = 0; i < 30; i++) {
            Location strikeLoc = entity.getLocation().clone().add(
                (Math.random() - 0.5) * 50,
                0,
                (Math.random() - 0.5) * 50
            );
            strikeLoc.getWorld().strikeLightning(strikeLoc);
        }
    }

    /**
     * Rapid ultimates in phase 4
     */
    private void startRapidUltimates() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                castUltimate();
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Cleanup on death
     */
    public void onDeath() {
        // Remove all summoned lords
        summonedLords.forEach(lord -> {
            if (lord.entity != null && lord.entity.isValid()) {
                lord.entity.remove();
            }
        });
        summonedLords.clear();

        // Victory message
        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§a§l§kA§r §a§lVICTORY!§r§7 You have defeated Necron, the Wither King! §a§l§kA")
        );
    }
}
