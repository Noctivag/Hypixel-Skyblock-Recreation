package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Voidgloom Seraph - Enderman Slayer Boss (Tier I-IV)
 * Features:
 * - Teleportation mechanics
 * - Void damage
 * - Yang Glyph healing mechanic
 * - Beacon mechanics
 * - Summoning of Voidlings
 */
public class VoidgloomSeraph extends SlayerBoss {

    private boolean hasYangGlyph = false;
    private List<Location> beacons = new ArrayList<>();
    private int voidlingsSpawned = 0;
    private static final int MAX_VOIDLINGS = 6;

    public VoidgloomSeraph(Location spawnLocation, int tier) {
        super("VOIDGLOOM_SERAPH_" + tier, spawnLocation, tier, "ENDERMAN");

        // Set tier-specific stats
        switch (tier) {
            case 1:
                setMaxHealth(15000.0);
                setDamage(300.0);
                setDefense(50.0);
                setCombatXP(500.0);
                break;
            case 2:
                setMaxHealth(50000.0);
                setDamage(800.0);
                setDefense(150.0);
                setCombatXP(2000.0);
                break;
            case 3:
                setMaxHealth(150000.0);
                setDamage(2000.0);
                setDefense(400.0);
                setCombatXP(8000.0);
                break;
            case 4:
                setMaxHealth(500000.0);
                setDamage(5000.0);
                setDefense(800.0);
                setCombatXP(30000.0);
                break;
        }
    }

    @Override
    public String getName() {
        String[] tierNames = {"", "§5Voidgloom Seraph I", "§5Voidgloom Seraph II",
                             "§5Voidgloom Seraph III", "§d§lVoidgloom Seraph IV"};
        return tierNames[getTier()];
    }

    @Override
    public String getLootTableId() {
        return "voidgloom_seraph_tier_" + getTier();
    }

    @Override
    public void startBoss() {
        super.startBoss();

        if (entity == null || !entity.isValid()) return;

        // Make it an actual Enderman
        if (entity instanceof Enderman) {
            Enderman enderman = (Enderman) entity;
            enderman.setAI(true);
        }

        // Apply effects based on tier
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, getTier()));

        // Start teleportation mechanic
        startTeleportationMechanic();

        // Start void damage
        startVoidDamage();

        // Start beacon mechanics (Tier 3+)
        if (getTier() >= 3) {
            startBeaconMechanic();
        }

        // Start Yang Glyph (Tier 4)
        if (getTier() >= 4) {
            activateYangGlyph();
        }

        // Start Voidling summoning
        startVoidlingSummoning();
    }

    /**
     * Teleportation mechanic
     */
    private void startTeleportationMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Teleport frequently
                Location newLoc = entity.getLocation().clone().add(
                    (Math.random() - 0.5) * 15,
                    Math.random() * 5,
                    (Math.random() - 0.5) * 15
                );

                // Particle effect
                entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 50);
                entity.teleport(newLoc);
                entity.getWorld().spawnParticle(Particle.PORTAL, newLoc, 50);
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 60L, 60L);
    }

    /**
     * Void damage mechanic
     */
    private void startVoidDamage() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Deal void damage to nearby players
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(10, 10, 10)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;

                        // Void damage ignores defense
                        player.damage(getDamage() * 0.3);

                        // Apply void effects
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));

                        // Void particles
                        player.getWorld().spawnParticle(
                            Particle.SMOKE_LARGE,
                            player.getLocation().add(0, 1, 0),
                            20
                        );
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 80L, 80L);
    }

    /**
     * Beacon mechanic - Must destroy beacons to damage boss
     */
    private void startBeaconMechanic() {
        // Create 4 beacons around the boss
        for (int i = 0; i < 4; i++) {
            double angle = (2 * Math.PI * i) / 4;
            double radius = 8;

            Location beaconLoc = entity.getLocation().clone().add(
                Math.cos(angle) * radius,
                0,
                Math.sin(angle) * radius
            );

            beacons.add(beaconLoc);
        }

        // Visual indicator for beacons
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                for (Location beacon : beacons) {
                    beacon.getWorld().spawnParticle(
                        Particle.END_ROD,
                        beacon.clone().add(0, 1, 0),
                        10,
                        0.5, 2, 0.5
                    );
                }

                // Boss is invulnerable while beacons are up
                if (!beacons.isEmpty()) {
                    entity.setInvulnerable(true);
                } else {
                    entity.setInvulnerable(false);
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 10L, 10L);
    }

    /**
     * Yang Glyph healing mechanic (Tier 4)
     */
    private void activateYangGlyph() {
        hasYangGlyph = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || !hasYangGlyph) {
                    cancel();
                    return;
                }

                // Heal over time
                double newHealth = Math.min(entity.getHealth() + getMaxHealth() * 0.02, getMaxHealth());
                entity.setHealth(newHealth);

                // Healing particles
                entity.getWorld().spawnParticle(
                    Particle.VILLAGER_HAPPY,
                    entity.getLocation().add(0, 2, 0),
                    30,
                    1, 1, 1
                );

                // Check if Yang Glyph should be destroyed (below 50% HP)
                if ((entity.getHealth() / getMaxHealth()) < 0.5) {
                    destroyYangGlyph();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);
    }

    /**
     * Destroy Yang Glyph
     */
    private void destroyYangGlyph() {
        hasYangGlyph = false;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§d§lVoidgloom Seraph: §7My Yang Glyph has been destroyed!")
        );

        // Explosion effect
        entity.getWorld().spawnParticle(
            Particle.EXPLOSION_LARGE,
            entity.getLocation(),
            10,
            2, 2, 2
        );
    }

    /**
     * Summon Voidlings
     */
    private void startVoidlingSummoning() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                if (voidlingsSpawned < MAX_VOIDLINGS) {
                    summonVoidling();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Summon a Voidling
     */
    private void summonVoidling() {
        Location spawnLoc = entity.getLocation().clone().add(
            (Math.random() - 0.5) * 10,
            0,
            (Math.random() - 0.5) * 10
        );

        Enderman voidling = (Enderman) entity.getWorld().spawnEntity(spawnLoc, EntityType.ENDERMAN);
        voidling.setCustomName("§5Voidling");
        voidling.setCustomNameVisible(true);
        voidling.setMaxHealth(1000.0 * getTier());
        voidling.setHealth(1000.0 * getTier());
        voidling.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

        voidlingsSpawned++;
    }

    /**
     * Remove a beacon (called when player destroys it)
     */
    public void destroyBeacon(Location beaconLoc) {
        beacons.remove(beaconLoc);

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§a§lBeacon destroyed! " + beacons.size() + " remaining.")
        );

        if (beacons.isEmpty()) {
            entity.getWorld().getPlayers().forEach(p ->
                p.sendMessage("§a§lAll beacons destroyed! Voidgloom Seraph is now vulnerable!")
            );
        }
    }
}
