package de.noctivag.skyblock.dungeons.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Goldor - Floor 7 Wither Lord (Phase 3)
 * The Berserker
 * Features:
 * - Terminal mechanics
 * - Extreme melee damage
 * - Rage stacks
 * - Gold armor requirement
 */
public class Goldor extends DungeonBoss {

    private int terminalsSolved = 0;
    private static final int REQUIRED_TERMINALS = 7;
    private boolean isInvulnerable = true;
    private int rageStacks = 0;
    private List<Location> terminals = new ArrayList<>();

    public Goldor(Location spawnLocation) {
        super("GOLDOR", spawnLocation, "CATACOMBS", 7);

        // Set Goldor-specific stats - Balanced between tankiness and damage
        setMaxHealth(400000.0);
        setDamage(2000.0);
        setDefense(300.0);
        setCombatXP(35000.0);

        // Initialize terminals
        initializeTerminals();
    }

    @Override
    public String getName() {
        return "§c§lGOLDOR §7[Floor 7]";
    }

    @Override
    public String getLootTableId() {
        return "goldor_floor_7";
    }

    /**
     * Initialize terminal locations
     */
    private void initializeTerminals() {
        if (entity == null || !entity.isValid()) return;

        // Create 7 terminal locations around the boss
        for (int i = 0; i < REQUIRED_TERMINALS; i++) {
            double angle = (2 * Math.PI * i) / REQUIRED_TERMINALS;
            double radius = 15;

            Location terminalLoc = entity.getLocation().clone().add(
                Math.cos(angle) * radius,
                0,
                Math.sin(angle) * radius
            );

            terminals.add(terminalLoc);
        }
    }

    /**
     * Start boss fight mechanics
     */
    public void startBossFight() {
        if (entity == null || !entity.isValid()) return;

        // Start invulnerable until terminals are solved
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 255));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10));

        // Display terminals
        displayTerminals();

        // Start rage mechanic
        startRageMechanic();

        // Start gold armor check
        startGoldArmorCheck();

        // Start melee attacks
        startMeleeAttacks();
    }

    /**
     * Display terminal locations
     */
    private void displayTerminals() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || terminalsSolved >= REQUIRED_TERMINALS) {
                    cancel();
                    return;
                }

                // Show particles at unsolved terminal locations
                for (int i = terminalsSolved; i < terminals.size(); i++) {
                    Location termLoc = terminals.get(i);
                    termLoc.getWorld().spawnParticle(
                        Particle.VILLAGER_HAPPY,
                        termLoc.clone().add(0, 2, 0),
                        10,
                        0.5, 0.5, 0.5
                    );
                }

                // Update name with terminal progress
                entity.setCustomName("§c§lGOLDOR §7[§6" + terminalsSolved + "/" + REQUIRED_TERMINALS + " Terminals§7]");
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 0L, 20L);
    }

    /**
     * Solve a terminal
     */
    public void solveTerminal() {
        terminalsSolved++;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§a§lTerminal " + terminalsSolved + "/" + REQUIRED_TERMINALS + " solved!")
        );

        if (terminalsSolved >= REQUIRED_TERMINALS) {
            removeInvulnerability();
        }
    }

    /**
     * Remove invulnerability after all terminals are solved
     */
    private void removeInvulnerability() {
        isInvulnerable = false;

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lGOLDOR: §7You dare challenge me directly? SO BE IT!")
        );

        // Remove invulnerability
        entity.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        entity.removePotionEffect(PotionEffectType.SLOW);

        // Add combat buffs
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5));

        // Explosion effect
        entity.getWorld().spawnParticle(
            Particle.EXPLOSION_HUGE,
            entity.getLocation(),
            20,
            5, 5, 5
        );
    }

    /**
     * Rage mechanic - Goldor gets stronger over time
     */
    private void startRageMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || isInvulnerable) {
                    cancel();
                    return;
                }

                rageStacks++;

                if (rageStacks % 5 == 0) {
                    entity.getWorld().getPlayers().forEach(p ->
                        p.sendMessage("§c§lGOLDOR: §7My RAGE grows! [" + rageStacks + " stacks]")
                    );
                }

                // Increase damage with each stack
                setDamage(getDamage() * 1.05);

                // Visual effect
                entity.getWorld().spawnParticle(
                    Particle.FLAME,
                    entity.getLocation().add(0, 1, 0),
                    20 + rageStacks,
                    1, 1, 1
                );
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 100L, 100L);
    }

    /**
     * Gold armor check - players without gold armor take extra damage
     */
    private void startGoldArmorCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;

                        if (!hasGoldArmor(player)) {
                            // Punish players without gold armor
                            player.damage(getDamage() * 0.5);
                            player.sendMessage("§c§lGOLDOR: §7You dare face me without gold? FOOL!");

                            player.addPotionEffect(new PotionEffect(
                                PotionEffectType.WITHER,
                                100,
                                2
                            ));
                        }
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 60L, 60L);
    }

    /**
     * Check if player has full gold armor
     */
    private boolean hasGoldArmor(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        return helmet != null && helmet.getType() == Material.GOLDEN_HELMET &&
               chestplate != null && chestplate.getType() == Material.GOLDEN_CHESTPLATE &&
               leggings != null && leggings.getType() == Material.GOLDEN_LEGGINGS &&
               boots != null && boots.getType() == Material.GOLDEN_BOOTS;
    }

    /**
     * Melee attacks - Goldor is a melee specialist
     */
    private void startMeleeAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead() || isInvulnerable) {
                    cancel();
                    return;
                }

                // Find nearest player
                Player nearestPlayer = null;
                double nearestDistance = Double.MAX_VALUE;

                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(20, 20, 20)) {
                    if (nearby instanceof Player) {
                        double distance = nearby.getLocation().distance(entity.getLocation());
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestPlayer = (Player) nearby;
                        }
                    }
                }

                if (nearestPlayer != null && nearestDistance < 5) {
                    // Execute powerful melee attack
                    double meleeDamage = getDamage() * (1 + rageStacks * 0.1);
                    nearestPlayer.damage(meleeDamage);

                    // Knockback
                    nearestPlayer.setVelocity(
                        nearestPlayer.getLocation().toVector()
                            .subtract(entity.getLocation().toVector())
                            .normalize()
                            .multiply(2)
                            .setY(1.5)
                    );

                    // Visual effect
                    nearestPlayer.getWorld().spawnParticle(
                        Particle.CRIT,
                        nearestPlayer.getLocation(),
                        50,
                        1, 1, 1
                    );

                    nearestPlayer.sendMessage("§c§lGOLDOR: §7Feel the weight of GOLD!");
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);
    }

    /**
     * Is boss invulnerable?
     */
    public boolean isInvulnerable() {
        return isInvulnerable;
    }
}
