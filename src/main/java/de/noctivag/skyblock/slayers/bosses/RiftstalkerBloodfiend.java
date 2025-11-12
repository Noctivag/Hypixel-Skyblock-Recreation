package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Riftstalker Bloodfiend - Vampire Slayer Boss (Tier I-V)
 * Features:
 * - Blood absorption (heals from damage dealt)
 * - Twinclaws summoning
 * - Mania mechanic (increases attack speed)
 * - Ichor mechanic (DoT effect)
 * - Steaks mechanic (healing items)
 */
public class RiftstalkerBloodfiend extends SlayerBoss {

    private int maniaStacks = 0;
    private static final int MAX_MANIA = 10;
    private List<org.bukkit.entity.LivingEntity> twinclaws = new ArrayList<>();
    private Map<Player, Integer> ichorStacks = new HashMap<>();
    private int steaksDropped = 0;

    public RiftstalkerBloodfiend(Location spawnLocation, int tier) {
        super("RIFTSTALKER_BLOODFIEND_" + tier, spawnLocation, tier, "VAMPIRE");

        // Set tier-specific stats
        switch (tier) {
            case 1:
                setMaxHealth(25000.0);
                setDamage(500.0);
                setDefense(100.0);
                setCombatXP(700.0);
                break;
            case 2:
                setMaxHealth(100000.0);
                setDamage(1200.0);
                setDefense(250.0);
                setCombatXP(3000.0);
                break;
            case 3:
                setMaxHealth(350000.0);
                setDamage(3000.0);
                setDefense(600.0);
                setCombatXP(12000.0);
                break;
            case 4:
                setMaxHealth(1000000.0);
                setDamage(7000.0);
                setDefense(1200.0);
                setCombatXP(50000.0);
                break;
            case 5:
                setMaxHealth(3000000.0);
                setDamage(15000.0);
                setDefense(2000.0);
                setCombatXP(150000.0);
                break;
        }
    }

    @Override
    public String getName() {
        String[] tierNames = {"", "§cRiftstalker Bloodfiend I", "§cRiftstalker Bloodfiend II",
                             "§cRiftstalker Bloodfiend III", "§4Riftstalker Bloodfiend IV",
                             "§4§lRiftstalker Bloodfiend V"};
        return tierNames[getTier()];
    }

    @Override
    public String getLootTableId() {
        return "riftstalker_bloodfiend_tier_" + getTier();
    }

    @Override
    public void startBoss() {
        super.startBoss();

        if (entity == null || !entity.isValid()) return;

        // Vampire effects
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, getTier()));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, getTier()));

        // Start blood absorption
        startBloodAbsorption();

        // Start mania mechanic
        startManiaMechanic();

        // Start ichor application
        startIchorMechanic();

        // Start Twinclaw summoning
        startTwinclawSummoning();

        // Start steak drops
        startSteakDrops();
    }

    /**
     * Blood absorption - heal from damage dealt
     */
    private void startBloodAbsorption() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Attack nearby players and heal
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(6, 6, 6)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;

                        // Deal damage
                        double damage = getDamage() * (1 + maniaStacks * 0.1);
                        player.damage(damage);

                        // Heal self (50% of damage dealt)
                        double heal = damage * 0.5;
                        double newHealth = Math.min(entity.getHealth() + heal, getMaxHealth());
                        entity.setHealth(newHealth);

                        // Blood particles
                        player.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            player.getLocation().add(0, 1, 0),
                            30,
                            0.5, 0.5, 0.5,
                            new Particle.DustOptions(org.bukkit.Color.RED, 2)
                        );

                        // Healing particles to boss
                        entity.getWorld().spawnParticle(
                            Particle.HEART,
                            entity.getLocation().add(0, 2, 0),
                            10
                        );

                        break; // One target at a time
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);
    }

    /**
     * Mania mechanic - increases attack speed
     */
    private void startManiaMechanic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Gain mania when hitting players
                if (maniaStacks < MAX_MANIA) {
                    maniaStacks++;

                    // Update speed based on mania
                    entity.removePotionEffect(PotionEffectType.SPEED);
                    entity.addPotionEffect(new PotionEffect(
                        PotionEffectType.SPEED,
                        Integer.MAX_VALUE,
                        getTier() + maniaStacks
                    ));

                    // Visual indicator
                    entity.getWorld().spawnParticle(
                        Particle.CRIT,
                        entity.getLocation().add(0, 1, 0),
                        20 + maniaStacks * 5,
                        1, 1, 1
                    );

                    if (maniaStacks % 3 == 0) {
                        entity.getWorld().getPlayers().forEach(p ->
                            p.sendMessage("§c§lBloodfiend: §7Mania [" + maniaStacks + "/" + MAX_MANIA + "]")
                        );
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 60L, 60L);
    }

    /**
     * Ichor mechanic - apply DoT
     */
    private void startIchorMechanic() {
        // Apply ichor on hit
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(5, 5, 5)) {
                    if (nearby instanceof Player) {
                        Player player = (Player) nearby;

                        // Add ichor stack
                        ichorStacks.put(player, ichorStacks.getOrDefault(player, 0) + 1);

                        player.sendMessage("§c§lIchor applied! Stacks: " + ichorStacks.get(player));
                    }
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 80L, 80L);

        // Ichor damage over time
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                ichorStacks.forEach((player, stacks) -> {
                    if (player != null && player.isOnline()) {
                        // Deal damage based on stacks
                        double ichorDamage = 50.0 * stacks * getTier();
                        player.damage(ichorDamage);

                        // Ichor particles
                        player.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            player.getLocation().add(0, 1, 0),
                            stacks * 5,
                            0.5, 0.5, 0.5,
                            new Particle.DustOptions(org.bukkit.Color.MAROON, 1.5f)
                        );
                    }
                });
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 40L, 40L);
    }

    /**
     * Summon Twinclaws
     */
    private void startTwinclawSummoning() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                // Clean up dead twinclaws
                twinclaws.removeIf(t -> t == null || !t.isValid() || t.isDead());

                // Summon if under limit
                if (twinclaws.size() < getTier() * 2) {
                    summonTwinclaw();
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 150L, 150L);
    }

    /**
     * Summon a Twinclaw
     */
    private void summonTwinclaw() {
        Location spawnLoc = entity.getLocation().clone().add(
            (Math.random() - 0.5) * 10,
            0,
            (Math.random() - 0.5) * 10
        );

        Zombie twinclaw = (Zombie) entity.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
        twinclaw.setCustomName("§cTwinclaw Thrall");
        twinclaw.setCustomNameVisible(true);
        twinclaw.setMaxHealth(3000.0 * getTier());
        twinclaw.setHealth(3000.0 * getTier());
        twinclaw.setBaby(false);
        twinclaw.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        twinclaw.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));

        twinclaws.add(twinclaw);
    }

    /**
     * Drop healing steaks
     */
    private void startSteakDrops() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || !entity.isValid() || entity.isDead()) {
                    cancel();
                    return;
                }

                double healthPercent = (entity.getHealth() / getMaxHealth()) * 100;

                // Drop steaks at 75%, 50%, 25% health
                if ((healthPercent <= 75 && steaksDropped == 0) ||
                    (healthPercent <= 50 && steaksDropped == 1) ||
                    (healthPercent <= 25 && steaksDropped == 2)) {

                    dropSteak();
                    steaksDropped++;
                }
            }
        }.runTaskTimer(SkyblockPlugin.getInstance(), 20L, 20L);
    }

    /**
     * Drop a healing steak
     */
    private void dropSteak() {
        Location steakLoc = entity.getLocation().clone().add(
            (Math.random() - 0.5) * 8,
            0,
            (Math.random() - 0.5) * 8
        );

        // Drop steak item
        steakLoc.getWorld().dropItem(steakLoc, new org.bukkit.inventory.ItemStack(
            org.bukkit.Material.COOKED_BEEF,
            1
        ));

        entity.getWorld().getPlayers().forEach(p ->
            p.sendMessage("§c§lBloodfiend: §7A Vampire Steak has been dropped! Eat it to heal!")
        );

        // Particles
        steakLoc.getWorld().spawnParticle(
            Particle.VILLAGER_HAPPY,
            steakLoc.add(0, 1, 0),
            50,
            1, 1, 1
        );
    }

    @Override
    public void stopBoss() {
        super.stopBoss();

        // Remove all twinclaws
        twinclaws.forEach(twinclaw -> {
            if (twinclaw != null && twinclaw.isValid()) {
                twinclaw.remove();
            }
        });
        twinclaws.clear();

        // Clear ichor stacks
        ichorStacks.clear();
    }
}
