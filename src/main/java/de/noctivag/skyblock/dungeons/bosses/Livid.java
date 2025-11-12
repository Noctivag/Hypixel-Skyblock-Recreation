package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Livid - Floor 5 dungeon boss
 * Features:
 * - Multiple clone mechanics (find the real Livid)
 * - Invisibility phases
 * - Teleportation attacks
 * - Rage mode at low health
 */
public class Livid extends DungeonBoss {

    private boolean isInPhase2 = false;
    private boolean isInPhase3 = false;
    private List<LivingEntity> clones = new ArrayList<>();
    private static final int CLONE_COUNT = 8;
    private boolean isRealLivid = true;

    public Livid(Location spawnLocation) {
        this(spawnLocation, true);
    }

    public Livid(Location spawnLocation, boolean isReal) {
        super("LIVID", spawnLocation, "CATACOMBS", 5);

        this.isRealLivid = isReal;

        // Set Livid-specific stats
        setMaxHealth(50000.0);
        setDamage(500.0);
        setDefense(150.0);
        setCombatXP(8000.0);
    }

    @Override
    public String getName() {
        return "§cLivid §7[Floor 5]";
    }

    @Override
    public String getLootTableId() {
        return "livid_floor_5";
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Create clones immediately
        createClones();

        // Start teleportation mechanic
        startTeleportationAttacks();

        // Start health monitoring for phases
        if (isRealLivid) {
            startPhaseMonitoring();
        }
    }

    /**
     * Create Livid clones
     */
    private void createClones() {
        if (!isRealLivid) return; // Only real Livid creates clones

        for (int i = 0; i < CLONE_COUNT; i++) {
            Location cloneLoc = entity.getLocation().clone().add(
                (Math.random() - 0.5) * 20,
                0,
                (Math.random() - 0.5) * 20
            );

            LivingEntity clone = (LivingEntity) entity.getWorld().spawnEntity(cloneLoc, EntityType.ZOMBIE);
            clone.setCustomName("§cLivid §7[Floor 5]");
            clone.setCustomNameVisible(true);
            clone.setMaxHealth(10000.0);
            clone.setHealth(10000.0);
            clone.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

            clones.add(clone);
        }

        // Broadcast hint
        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lLivid: §7One of us is real... can you find me?")
        );
    }

    /**
     * Teleportation attack mechanic
     */
    private void startTeleportationAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Find random nearby player
                List<Player> nearbyPlayers = new ArrayList<>();
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(40, 40, 40)) {
                    if (nearby instanceof Player) {
                        nearbyPlayers.add((Player) nearby);
                    }
                }

                if (!nearbyPlayers.isEmpty()) {
                    Player target = nearbyPlayers.get((int) (Math.random() * nearbyPlayers.size()));

                    // Teleport behind player
                    Location behindPlayer = target.getLocation().clone()
                        .add(target.getLocation().getDirection().multiply(-3));

                    // Particle effect at old location
                    entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 50);

                    entity.teleport(behindPlayer);

                    // Particle effect at new location
                    entity.getWorld().spawnParticle(Particle.PORTAL, behindPlayer, 50);

                    // Deal backstab damage
                    target.damage(getDamage() * 1.5);
                    target.sendMessage("§c§lLivid: §7Backstab!");
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 80L, 80L);
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

                if (healthPercent <= 20 && !isInPhase3) {
                    triggerPhase3();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Phase 2: Invisibility
     */
    private void triggerPhase2() {
        isInPhase2 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lLivid: §7You can't hit what you can't see!")
        );

        // Apply invisibility intermittently
        new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || count >= 10) {
                    cancel();
                    return;
                }

                if (count % 2 == 0) {
                    // Invisible
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0));
                } else {
                    // Visible
                    entity.removePotionEffect(PotionEffectType.INVISIBILITY);
                }

                count++;
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);

        // Increase damage
        setDamage(getDamage() * 1.5);
    }

    /**
     * Phase 3: Rage mode
     */
    private void triggerPhase3() {
        isInPhase3 = true;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lLivid: §7ENOUGH! I WILL DESTROY YOU ALL!")
        );

        // Remove all clones
        clones.forEach(clone -> {
            if (clone != null && clone.isValid()) {
                clone.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, clone.getLocation(), 1);
                clone.remove();
            }
        });
        clones.clear();

        // Massive stat boost
        setDamage(getDamage() * 2.0);
        entity.removePotionEffect(PotionEffectType.INVISIBILITY);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));

        // Constant area damage
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Damage all nearby players
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(10, 10, 10)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(getDamage() * 0.3);

                        // Fire particles
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            player.getLocation().add(0, 1, 0),
                            20
                        );
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 30L, 30L);
    }

    /**
     * Cleanup on death
     */
    public void onDeath() {
        if (!isRealLivid) return;

        // Remove all clones
        clones.forEach(clone -> {
            if (clone != null && clone.isValid()) {
                clone.remove();
            }
        });
        clones.clear();
    }
}
