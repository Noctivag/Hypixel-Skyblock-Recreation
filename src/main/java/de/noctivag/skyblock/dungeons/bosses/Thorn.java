package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Thorn - Floor 4 dungeon boss
 * Features:
 * - Lightning strike attacks
 * - Wither effect application
 * - Multi-phase split mechanics
 * - Spirit animals summoning
 */
public class Thorn extends DungeonBoss {

    private boolean isInPhase2 = false;
    private boolean isInPhase3 = false;
    private List<LivingEntity> spiritAnimals = new ArrayList<>();
    private Thorn splitClone = null;

    public Thorn(Location spawnLocation) {
        super("THORN", spawnLocation, "CATACOMBS", 4);

        // Set Thorn-specific stats
        setMaxHealth(35000.0);
        setDamage(350.0);
        setDefense(100.0);
        setCombatXP(5000.0);
    }

    @Override
    public String getName() {
        return "§cThorn §7[Floor 4]";
    }

    @Override
    public String getLootTableId() {
        return "thorn_floor_4";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Start lightning attack mechanic
        startLightningAttacks();

        // Start wither application
        startWitherMechanic();

        // Start health monitoring for phases
        startPhaseMonitoring();

        // Summon initial spirit animals
        summonSpiritAnimals(2);
    }

    /**
     * Lightning attack mechanic
     */
    private void startLightningAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Find nearby players
                List<Player> nearbyPlayers = new ArrayList<>();
                for (Entity nearby : entity.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player) {
                        nearbyPlayers.add((Player) nearby);
                    }
                }

                if (!nearbyPlayers.isEmpty()) {
                    // Strike random player with lightning
                    Player target = nearbyPlayers.get((int) (Math.random() * nearbyPlayers.size()));

                    // Visual lightning effect
                    target.getWorld().strikeLightningEffect(target.getLocation());

                    // Deal damage
                    target.damage(getDamage() * 0.5);

                    // Warning message
                    target.sendMessage("§c⚡ Thorn calls down lightning upon you!");
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 60L, 60L);
    }

    /**
     * Wither effect mechanic
     */
    private void startWitherMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Apply wither to nearby players
                for (Entity nearby : entity.getNearbyEntities(15, 15, 15)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.addPotionEffect(new PotionEffect(
                            PotionEffectType.WITHER,
                            100,
                            1
                        ));

                        // Wither particles
                        player.getWorld().spawnParticle(
                            Particle.SMOKE_NORMAL,
                            player.getLocation().add(0, 1, 0),
                            20
                        );
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 80L, 80L);
    }

    /**
     * Summon spirit animals
     */
    private void summonSpiritAnimals(int count) {
        EntityType[] animalTypes = {
            EntityType.WOLF,
            EntityType.POLAR_BEAR,
            EntityType.OCELOT
        };

        for (int i = 0; i < count; i++) {
            Location spawnLoc = entity.getLocation().clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );

            EntityType type = animalTypes[(int) (Math.random() * animalTypes.length)];
            LivingEntity animal = (LivingEntity) entity.getWorld().spawnEntity(spawnLoc, type);
            animal.setCustomName("§eSpirit " + type.name());
            animal.setCustomNameVisible(true);
            animal.setMaxHealth(5000.0);
            animal.setHealth(5000.0);
            animal.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            animal.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));

            spiritAnimals.add(animal);
        }
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
     * Phase 2: Split mechanic
     */
    private void triggerPhase2() {
        isInPhase2 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lThorn: §7You think you can defeat me? I split into two!")
        );

        // Create a clone at half health
        Location cloneLoc = entity.getLocation().clone().add(5, 0, 5);
        splitClone = new Thorn(cloneLoc);
        splitClone.setMaxHealth(getMaxHealth() / 2);
        splitClone.spawn();

        // Both gain speed
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

        // Summon more spirit animals
        summonSpiritAnimals(3);
    }

    /**
     * Phase 3: Enrage
     */
    private void triggerPhase3() {
        isInPhase3 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lThorn: §7Feel the wrath of nature itself!")
        );

        // Massive damage boost
        setDamage(getDamage() * 2.0);

        // Constant regeneration
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                double newHealth = Math.min(entity.getHealth() + 500, getMaxHealth());
                entity.setHealth(newHealth);

                // Healing particles
                entity.getWorld().spawnParticle(
                    Particle.VILLAGER_HAPPY,
                    entity.getLocation().add(0, 1, 0),
                    20
                );
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);

        // Summon all remaining spirit animals
        summonSpiritAnimals(5);
    }

    /**
     * Cleanup on death
     */
    public void onDeath() {
        // Remove all spirit animals
        spiritAnimals.forEach(a -> {
            if (a != null && a.isValid()) {
                a.remove();
            }
        });
        spiritAnimals.clear();

        // Remove clone if exists
        if (splitClone != null && splitClone.entity != null && splitClone.entity.isValid()) {
            splitClone.entity.remove();
        }
    }
}
