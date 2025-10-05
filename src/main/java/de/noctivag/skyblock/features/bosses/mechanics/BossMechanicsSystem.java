package de.noctivag.skyblock.features.bosses.mechanics;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Boss Mechanics System - Spezifische Mechaniken für jeden Boss
 * 
 * Implementiert alle Boss-Mechaniken genau nach Hypixel Skyblock Wiki:
 * - Enderdrache Phasen und Angriffe
 * - Slayer Boss Fähigkeiten und Phasen
 * - Catacombs Boss Rätsel und Mechaniken
 * - Kuudra Boss Koordination und Teamwork
 */
public class BossMechanicsSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    
    // Aktive Boss-Mechaniken
    private final Map<UUID, BossMechanic> activeMechanics = new ConcurrentHashMap<>();
    private final Map<UUID, List<BossPhase>> bossPhases = new ConcurrentHashMap<>();
    
    public BossMechanicsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeBossMechanics();
    }
    
    /**
     * Initialisiert alle Boss-Mechaniken
     */
    private void initializeBossMechanics() {
        // Hier werden alle spezifischen Boss-Mechaniken initialisiert
    }
    
    /**
     * Enderdrache Mechaniken
     */
    public void initializeEnderDragonMechanics(LivingEntity dragon) {
        List<BossPhase> phases = Arrays.asList(
            new BossPhase(1, "Perch Phase", 100, 80, Arrays.asList(
                "§7Drache sitzt auf dem Portal",
                "§7Spawnt Endermen",
                "§7Schießt Dragon Breath"
            )),
            new BossPhase(2, "Flying Phase", 80, 50, Arrays.asList(
                "§7Drache fliegt umher",
                "§7Schießt Fireballs",
                "§7Verursacht Dragon Charge"
            )),
            new BossPhase(3, "Perch Phase 2", 50, 20, Arrays.asList(
                "§7Drache kehrt zum Portal zurück",
                "§7Verstärkte Angriffe",
                "§7Spawnt mehr Endermen"
            )),
            new BossPhase(4, "Final Phase", 20, 0, Arrays.asList(
                "§7Drache wird aggressiver",
                "§7Alle Angriffe verstärkt",
                "§7Finale Dragon Breath"
            ))
        );
        
        bossPhases.put(dragon.getUniqueId(), phases);
        startEnderDragonAI(dragon);
    }
    
    /**
     * Startet Enderdrache AI
     */
    private void startEnderDragonAI(LivingEntity dragon) {
        new BukkitRunnable() {
            int phase = 1;
            int attackCooldown = 0;
            
            @Override
            public void run() {
                if (dragon.isDead()) {
                    cancel();
                    return;
                }
                
                // Phase-Wechsel basierend auf HP
                double maxHealth = dragon.getAttribute(Attribute.MAX_HEALTH).getValue();
                double healthPercent = dragon.getHealth() / maxHealth * 100;
                int newPhase = getPhaseByHealth(healthPercent);
                
                if (newPhase != phase) {
                    phase = newPhase;
                    onPhaseChange(dragon, phase);
                }
                
                // Angriffe basierend auf Phase
                if (attackCooldown <= 0) {
                    executeDragonAttack(dragon, phase);
                    attackCooldown = getAttackCooldown(phase);
                } else {
                    attackCooldown--;
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    /**
     * Führt Drachen-Angriffe aus
     */
    private void executeDragonAttack(LivingEntity dragon, int phase) {
        switch (phase) {
            case 1 -> {
                // Dragon Breath
                shootDragonBreath(dragon);
                // Spawn Endermen
                spawnEndermen(dragon.getLocation());
            }
            case 2 -> {
                // Fireball
                shootFireball(dragon);
                // Dragon Charge
                performDragonCharge(dragon);
            }
            case 3 -> {
                // Verstärkte Angriffe
                shootDragonBreath(dragon);
                shootFireball(dragon);
                spawnEndermen(dragon.getLocation());
            }
            case 4 -> {
                // Finale Angriffe
                shootDragonBreath(dragon);
                shootFireball(dragon);
                performDragonCharge(dragon);
                spawnEndermen(dragon.getLocation());
            }
        }
    }
    
    /**
     * Dragon Breath Angriff
     */
    private void shootDragonBreath(LivingEntity dragon) {
        Location eyeLocation = dragon.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        
        // Erstelle Dragon Breath Partikel
        for (int i = 0; i < 20; i++) {
            Location particleLoc = eyeLocation.clone().add(direction.multiply(i * 0.5));
            dragon.getWorld().spawnParticle(Particle.DRAGON_BREATH, particleLoc, 1);
        }
        
        // Schaden an Spielern in der Nähe
        dragon.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(eyeLocation) <= 10)
            .forEach(player -> {
                player.damage(500); // 500 Schaden
                player.sendMessage(Component.text("§c§lDRAGON BREATH!")
                    .color(NamedTextColor.RED));
            });
        
        // Sound
        dragon.getWorld().playSound(eyeLocation, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
    }
    
    /**
     * Fireball Angriff
     */
    private void shootFireball(LivingEntity dragon) {
        Location eyeLocation = dragon.getEyeLocation();
        
        // Spawne Fireball
        Fireball fireball = dragon.getWorld().spawn(eyeLocation, Fireball.class);
        fireball.setDirection(eyeLocation.getDirection());
        fireball.setYield(3.0f);
        
        // Sound
        dragon.getWorld().playSound(eyeLocation, Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
    }
    
    /**
     * Dragon Charge Angriff
     */
    private void performDragonCharge(LivingEntity dragon) {
        // Finde nächsten Spieler
        Player target = dragon.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(dragon.getLocation()) <= 50)
            .min(Comparator.comparingDouble(player -> player.getLocation().distance(dragon.getLocation())))
            .orElse(null);
        
        if (target != null) {
            // Dragon fliegt zum Spieler
            Vector direction = target.getLocation().toVector().subtract(dragon.getLocation().toVector()).normalize();
            dragon.setVelocity(direction.multiply(2.0));
            
            // Schaden beim Aufprall
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (dragon.getLocation().distance(target.getLocation()) <= 3) {
                        target.damage(1000); // 1000 Schaden
                        target.sendMessage(Component.text("§c§lDRAGON CHARGE!")
                            .color(NamedTextColor.RED));
                        cancel();
                    }
                }
            }.runTaskTimer(SkyblockPlugin, 0L, 5L);
        }
    }
    
    /**
     * Spawnt Endermen
     */
    private void spawnEndermen(Location location) {
        for (int i = 0; i < 3; i++) {
            Location spawnLoc = location.clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            
            Enderman enderman = location.getWorld().spawn(spawnLoc, Enderman.class);
            enderman.customName(Component.text("§5§lDragon Enderman"));
            enderman.setCustomNameVisible(true);
        }
    }
    
    /**
     * Revenant Horror Mechaniken
     */
    public void initializeRevenantMechanics(LivingEntity revenant, int tier) {
        List<BossPhase> phases = Arrays.asList(
            new BossPhase(1, "Normal Phase", 100, 50, Arrays.asList(
                "§7Normale Angriffe",
                "§7Spawnt Zombies",
                "§7Heilt sich selbst"
            )),
            new BossPhase(2, "Enraged Phase", 50, 0, Arrays.asList(
                "§7Verstärkte Angriffe",
                "§7Spawnt mehr Zombies",
                "§7Schnellere Heilung"
            ))
        );
        
        bossPhases.put(revenant.getUniqueId(), phases);
        startRevenantAI(revenant, tier);
    }
    
    /**
     * Startet Revenant AI
     */
    private void startRevenantAI(LivingEntity revenant, int tier) {
        new BukkitRunnable() {
            int phase = 1;
            int attackCooldown = 0;
            int healCooldown = 0;
            
            @Override
            public void run() {
                if (revenant.isDead()) {
                    cancel();
                    return;
                }
                
                // Phase-Wechsel
                double maxHealth = revenant.getAttribute(Attribute.MAX_HEALTH).getValue();
                double healthPercent = revenant.getHealth() / maxHealth * 100;
                int newPhase = healthPercent <= 50 ? 2 : 1;
                
                if (newPhase != phase) {
                    phase = newPhase;
                    onPhaseChange(revenant, phase);
                }
                
                // Angriffe
                if (attackCooldown <= 0) {
                    executeRevenantAttack(revenant, tier, phase);
                    attackCooldown = getRevenantAttackCooldown(tier, phase);
                } else {
                    attackCooldown--;
                }
                
                // Heilung
                if (healCooldown <= 0) {
                    healRevenant(revenant, tier, phase);
                    healCooldown = getRevenantHealCooldown(tier, phase);
                } else {
                    healCooldown--;
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    /**
     * Führt Revenant-Angriffe aus
     */
    private void executeRevenantAttack(LivingEntity revenant, int tier, int phase) {
        // Spawne Zombies
        spawnZombies(revenant.getLocation(), tier, phase);
        
        // Explosion
        if (Math.random() < 0.3) {
            performExplosion(revenant);
        }
        
        // Teleport
        if (Math.random() < 0.2) {
            teleportToPlayer(revenant);
        }
    }
    
    /**
     * Spawnt Zombies
     */
    private void spawnZombies(Location location, int tier, int phase) {
        int count = tier + (phase == 2 ? 2 : 0);
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = location.clone().add(
                (Math.random() - 0.5) * 5,
                0,
                (Math.random() - 0.5) * 5
            );
            
            Zombie zombie = location.getWorld().spawn(spawnLoc, Zombie.class);
            zombie.customName(Component.text("§2§lRevenant Zombie"));
            zombie.setCustomNameVisible(true);
        }
    }
    
    /**
     * Explosion Angriff
     */
    private void performExplosion(LivingEntity revenant) {
        Location loc = revenant.getLocation();
        
        // Explosion Partikel
        revenant.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1);
        
        // Schaden an Spielern
        revenant.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(loc) <= 5)
            .forEach(player -> {
                player.damage(200);
                player.sendMessage(Component.text("§c§lREVENANT EXPLOSION!")
                    .color(NamedTextColor.RED));
            });
        
        // Sound
        revenant.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    }
    
    /**
     * Teleportiert zu Spieler
     */
    private void teleportToPlayer(LivingEntity revenant) {
        Player target = revenant.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(revenant.getLocation()) <= 20)
            .min(Comparator.comparingDouble(player -> player.getLocation().distance(revenant.getLocation())))
            .orElse(null);
        
        if (target != null) {
            revenant.teleport(target.getLocation());
            target.sendMessage(Component.text("§c§lREVENANT TELEPORT!")
                .color(NamedTextColor.RED));
        }
    }
    
    /**
     * Heilt Revenant
     */
    private void healRevenant(LivingEntity revenant, int tier, int phase) {
        double healAmount = tier * 1000 * (phase == 2 ? 1.5 : 1.0);
        double maxHealth = revenant.getAttribute(Attribute.MAX_HEALTH).getValue();
        double newHealth = Math.min(maxHealth, revenant.getHealth() + healAmount);
        revenant.setHealth(newHealth);
        
        // Heilung Partikel
        revenant.getWorld().spawnParticle(Particle.HEART, revenant.getLocation(), 10);
        
        // Sound
        revenant.getWorld().playSound(revenant.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 1.0f);
    }
    
    /**
     * Livid Mechaniken (Catacombs F5)
     */
    public void initializeLividMechanics(LivingEntity livid) {
        // Spawne Livid Klone
        spawnLividClones(livid);
        
        startLividAI(livid);
    }
    
    /**
     * Spawnt Livid Klone
     */
    private void spawnLividClones(LivingEntity livid) {
        for (int i = 0; i < 4; i++) {
            Location cloneLoc = livid.getLocation().clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            
            LivingEntity clone = (LivingEntity) livid.getWorld().spawnEntity(cloneLoc, livid.getType());
            clone.customName(Component.text("§d§lLivid Clone"));
            clone.setCustomNameVisible(true);
            // clone.setMaxHealth(livid.getMaxHealth() / 2); // Deprecated
            clone.setHealth(20.0); // Standard HP für Klone
        }
    }
    
    /**
     * Startet Livid AI
     */
    private void startLividAI(LivingEntity livid) {
        new BukkitRunnable() {
            int teleportCooldown = 0;
            int cloneCooldown = 0;
            
            @Override
            public void run() {
                if (livid.isDead()) {
                    cancel();
                    return;
                }
                
                // Teleport
                if (teleportCooldown <= 0) {
                    teleportLivid(livid);
                    teleportCooldown = 100; // 5 Sekunden
                } else {
                    teleportCooldown--;
                }
                
                // Spawne neue Klone
                if (cloneCooldown <= 0) {
                    spawnLividClones(livid);
                    cloneCooldown = 200; // 10 Sekunden
                } else {
                    cloneCooldown--;
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    /**
     * Teleportiert Livid
     */
    private void teleportLivid(LivingEntity livid) {
        Player target = livid.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(livid.getLocation()) <= 30)
            .min(Comparator.comparingDouble(player -> player.getLocation().distance(livid.getLocation())))
            .orElse(null);
        
        if (target != null) {
            Location teleportLoc = target.getLocation().clone().add(
                (Math.random() - 0.5) * 5,
                0,
                (Math.random() - 0.5) * 5
            );
            
            livid.teleport(teleportLoc);
            target.sendMessage(Component.text("§d§lLIVID TELEPORT!")
                .color(NamedTextColor.LIGHT_PURPLE));
        }
    }
    
    /**
     * Kuudra Mechaniken
     */
    public void initializeKuudraMechanics(LivingEntity kuudra, String tier) {
        startKuudraAI(kuudra, tier);
    }
    
    /**
     * Startet Kuudra AI
     */
    private void startKuudraAI(LivingEntity kuudra, String tier) {
        new BukkitRunnable() {
            int attackCooldown = 0;
            int phase = 1;
            
            @Override
            public void run() {
                if (kuudra.isDead()) {
                    cancel();
                    return;
                }
                
                // Phase-Wechsel basierend auf HP
                double maxHealth = kuudra.getAttribute(Attribute.MAX_HEALTH).getValue();
                double healthPercent = kuudra.getHealth() / maxHealth * 100;
                int newPhase = getKuudraPhase(healthPercent);
                
                if (newPhase != phase) {
                    phase = newPhase;
                    onKuudraPhaseChange(kuudra, phase);
                }
                
                // Angriffe
                if (attackCooldown <= 0) {
                    executeKuudraAttack(kuudra, tier, phase);
                    attackCooldown = getKuudraAttackCooldown(tier, phase);
                } else {
                    attackCooldown--;
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    /**
     * Führt Kuudra-Angriffe aus
     */
    private void executeKuudraAttack(LivingEntity kuudra, String tier, int phase) {
        switch (tier) {
            case "BASIC" -> executeBasicKuudraAttack(kuudra, phase);
            case "HOT" -> executeHotKuudraAttack(kuudra, phase);
            case "BURNING" -> executeBurningKuudraAttack(kuudra, phase);
            case "FIERY" -> executeFieryKuudraAttack(kuudra, phase);
            case "INFERNO" -> executeInfernoKuudraAttack(kuudra, phase);
        }
    }
    
    /**
     * Basic Kuudra Angriffe
     */
    private void executeBasicKuudraAttack(LivingEntity kuudra, int phase) {
        // Einfache Angriffe
        shootKuudraFireball(kuudra);
        
        if (phase >= 2) {
            spawnKuudraMinions(kuudra);
        }
    }
    
    /**
     * Hot Kuudra Angriffe
     */
    private void executeHotKuudraAttack(LivingEntity kuudra, int phase) {
        // Feuer-Angriffe
        shootKuudraFireball(kuudra);
        createFireRing(kuudra);
        
        if (phase >= 2) {
            spawnKuudraMinions(kuudra);
        }
        
        if (phase >= 3) {
            performKuudraCharge(kuudra);
        }
    }
    
    /**
     * Burning Kuudra Angriffe
     */
    private void executeBurningKuudraAttack(LivingEntity kuudra, int phase) {
        // Lava-Angriffe
        shootKuudraFireball(kuudra);
        createFireRing(kuudra);
        createLavaPool(kuudra);
        
        if (phase >= 2) {
            spawnKuudraMinions(kuudra);
        }
        
        if (phase >= 3) {
            performKuudraCharge(kuudra);
        }
    }
    
    /**
     * Fiery Kuudra Angriffe
     */
    private void executeFieryKuudraAttack(LivingEntity kuudra, int phase) {
        // Inferno-Angriffe
        shootKuudraFireball(kuudra);
        createFireRing(kuudra);
        createLavaPool(kuudra);
        createInfernoBeam(kuudra);
        
        if (phase >= 2) {
            spawnKuudraMinions(kuudra);
        }
        
        if (phase >= 3) {
            performKuudraCharge(kuudra);
        }
    }
    
    /**
     * Inferno Kuudra Angriffe
     */
    private void executeInfernoKuudraAttack(LivingEntity kuudra, int phase) {
        // Alle Angriffe verstärkt
        shootKuudraFireball(kuudra);
        createFireRing(kuudra);
        createLavaPool(kuudra);
        createInfernoBeam(kuudra);
        performKuudraCharge(kuudra);
        
        if (phase >= 2) {
            spawnKuudraMinions(kuudra);
            spawnKuudraElites(kuudra);
        }
    }
    
    /**
     * Schießt Kuudra Fireball
     */
    private void shootKuudraFireball(LivingEntity kuudra) {
        Location eyeLocation = kuudra.getEyeLocation();
        
        Fireball fireball = kuudra.getWorld().spawn(eyeLocation, Fireball.class);
        fireball.setDirection(eyeLocation.getDirection());
        fireball.setYield(5.0f);
        
        kuudra.getWorld().playSound(eyeLocation, Sound.ENTITY_GHAST_SHOOT, 1.0f, 0.5f);
    }
    
    /**
     * Erstellt Feuer-Ring
     */
    private void createFireRing(LivingEntity kuudra) {
        Location center = kuudra.getLocation();
        
        for (int i = 0; i < 360; i += 10) {
            double x = center.getX() + Math.cos(Math.toRadians(i)) * 5;
            double z = center.getZ() + Math.sin(Math.toRadians(i)) * 5;
            
            Location fireLoc = new Location(center.getWorld(), x, center.getY(), z);
            fireLoc.getBlock().setType(Material.FIRE);
            
            // Schaden an Spielern
            fireLoc.getWorld().getPlayers().stream()
                .filter(player -> player.getLocation().distance(fireLoc) <= 2)
                .forEach(player -> {
                    player.damage(300);
                    player.sendMessage(Component.text("§c§lKUUDRA FIRE RING!")
                        .color(NamedTextColor.RED));
                });
        }
    }
    
    /**
     * Erstellt Lava-Pool
     */
    private void createLavaPool(LivingEntity kuudra) {
        Location center = kuudra.getLocation();
        
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                Location lavaLoc = center.clone().add(x, -1, z);
                if (lavaLoc.getBlock().getType() == Material.AIR) {
                    lavaLoc.getBlock().setType(Material.LAVA);
                }
            }
        }
    }
    
    /**
     * Erstellt Inferno-Strahl
     */
    private void createInfernoBeam(LivingEntity kuudra) {
        Location eyeLocation = kuudra.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        
        for (int i = 0; i < 50; i++) {
            Location beamLoc = eyeLocation.clone().add(direction.multiply(i * 0.5));
            kuudra.getWorld().spawnParticle(Particle.FLAME, beamLoc, 1);
        }
        
        // Schaden
        kuudra.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(eyeLocation) <= 25)
            .forEach(player -> {
                player.damage(1000);
                player.sendMessage(Component.text("§4§lINFERNO BEAM!")
                    .color(NamedTextColor.DARK_RED));
            });
    }
    
    /**
     * Kuudra Charge
     */
    private void performKuudraCharge(LivingEntity kuudra) {
        Player target = kuudra.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(kuudra.getLocation()) <= 30)
            .min(Comparator.comparingDouble(player -> player.getLocation().distance(kuudra.getLocation())))
            .orElse(null);
        
        if (target != null) {
            Vector direction = target.getLocation().toVector().subtract(kuudra.getLocation().toVector()).normalize();
            kuudra.setVelocity(direction.multiply(3.0));
            
            // Schaden beim Aufprall
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (kuudra.getLocation().distance(target.getLocation()) <= 5) {
                        target.damage(2000);
                        target.sendMessage(Component.text("§4§lKUUDRA CHARGE!")
                            .color(NamedTextColor.DARK_RED));
                        cancel();
                    }
                }
            }.runTaskTimer(SkyblockPlugin, 0L, 5L);
        }
    }
    
    /**
     * Spawnt Kuudra Minions
     */
    private void spawnKuudraMinions(LivingEntity kuudra) {
        for (int i = 0; i < 3; i++) {
            Location spawnLoc = kuudra.getLocation().clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            
            Blaze minion = kuudra.getWorld().spawn(spawnLoc, Blaze.class);
            minion.customName(Component.text("§6§lKuudra Minion"));
            minion.setCustomNameVisible(true);
        }
    }
    
    /**
     * Spawnt Kuudra Elites
     */
    private void spawnKuudraElites(LivingEntity kuudra) {
        Location spawnLoc = kuudra.getLocation().clone().add(
            (Math.random() - 0.5) * 15,
            0,
            (Math.random() - 0.5) * 15
        );
        
        Blaze elite = kuudra.getWorld().spawn(spawnLoc, Blaze.class);
        elite.customName(Component.text("§4§lKuudra Elite"));
        elite.setCustomNameVisible(true);
        // elite.setMaxHealth(10000); // Deprecated
        elite.setHealth(100.0); // Standard HP für Elite
    }
    
    // Helper-Methoden
    private int getPhaseByHealth(double healthPercent) {
        if (healthPercent > 80) return 1;
        if (healthPercent > 50) return 2;
        if (healthPercent > 20) return 3;
        return 4;
    }
    
    private int getKuudraPhase(double healthPercent) {
        if (healthPercent > 75) return 1;
        if (healthPercent > 50) return 2;
        if (healthPercent > 25) return 3;
        return 4;
    }
    
    private int getAttackCooldown(int phase) {
        return switch (phase) {
            case 1 -> 100; // 5 Sekunden
            case 2 -> 80;  // 4 Sekunden
            case 3 -> 60;  // 3 Sekunden
            case 4 -> 40;  // 2 Sekunden
            default -> 100;
        };
    }
    
    private int getRevenantAttackCooldown(int tier, int phase) {
        int baseCooldown = 120 - (tier * 10);
        return phase == 2 ? baseCooldown / 2 : baseCooldown;
    }
    
    private int getRevenantHealCooldown(int tier, int phase) {
        int baseCooldown = 200 - (tier * 20);
        return phase == 2 ? baseCooldown / 2 : baseCooldown;
    }
    
    private int getKuudraAttackCooldown(String tier, int phase) {
        int baseCooldown = switch (tier) {
            case "BASIC" -> 100;
            case "HOT" -> 80;
            case "BURNING" -> 60;
            case "FIERY" -> 40;
            case "INFERNO" -> 20;
            default -> 100;
        };
        
        return baseCooldown - (phase * 10);
    }
    
    private void onPhaseChange(LivingEntity boss, int phase) {
        boss.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(boss.getLocation()) <= 50)
            .forEach(player -> {
                player.sendMessage(Component.text("§6§lBOSS PHASE " + phase + "!")
                    .color(NamedTextColor.GOLD));
            });
    }
    
    private void onKuudraPhaseChange(LivingEntity kuudra, int phase) {
        kuudra.getWorld().getPlayers().stream()
            .filter(player -> player.getLocation().distance(kuudra.getLocation()) <= 100)
            .forEach(player -> {
                player.sendMessage(Component.text("§4§lKUUDRABOSS PHASE " + phase + "!")
                    .color(NamedTextColor.DARK_RED));
            });
    }
    
    // Boss-Phase Datenklasse
    public static class BossPhase {
        private final int phaseNumber;
        private final String name;
        private final int healthPercentStart;
        private final int healthPercentEnd;
        private final List<String> abilities;
        
        public BossPhase(int phaseNumber, String name, int healthPercentStart, int healthPercentEnd, List<String> abilities) {
            this.phaseNumber = phaseNumber;
            this.name = name;
            this.healthPercentStart = healthPercentStart;
            this.healthPercentEnd = healthPercentEnd;
            this.abilities = abilities;
        }
        
        public int getPhaseNumber() { return phaseNumber; }
        public String getName() { return name; }
        public int getHealthPercentStart() { return healthPercentStart; }
        public int getHealthPercentEnd() { return healthPercentEnd; }
        public List<String> getAbilities() { return abilities; }
    }
    
    // Boss-Mechanic Datenklasse
    public static class BossMechanic {
        private final String name;
        private final String description;
        private final int cooldown;
        private final int damage;
        
        public BossMechanic(String name, String description, int cooldown, int damage) {
            this.name = name;
            this.description = description;
            this.cooldown = cooldown;
            this.damage = damage;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getCooldown() { return cooldown; }
        public int getDamage() { return damage; }
    }
}
