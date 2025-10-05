package de.noctivag.skyblock.bosses;

import de.noctivag.skyblock.enums.SlayerType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Revenant Horror - Der erste Zombie-Slayer-Boss
 * Implementiert spezielle Fähigkeiten wie AoE-Schrei und verstärkte Zombies
 */
public class RevenantHorror extends SlayerBoss {
    
    private Zombie bossZombie;
    private int currentPhase;
    private long lastScreamTime;
    private long lastMinionSpawnTime;
    private static final long SCREAM_COOLDOWN = 10000; // 10 Sekunden
    private static final long MINION_SPAWN_COOLDOWN = 15000; // 15 Sekunden
    
    public RevenantHorror(int tier, Location spawnLocation, Player targetPlayer) {
        super(SlayerType.REVENANT_HORROR, tier, spawnLocation, targetPlayer);
        this.currentPhase = 1;
        this.lastScreamTime = 0;
        this.lastMinionSpawnTime = 0;
    }
    
    @Override
    public boolean spawn() {
        try {
            // Spawne den Boss-Zombie
            bossZombie = spawnLocation.getWorld().spawn(spawnLocation, Zombie.class);
            bossEntity = bossZombie;
            
            // Konfiguriere den Boss
            configureBoss();
            
            // Setze Status
            isAlive = true;
            spawnTime = System.currentTimeMillis();
            
            // Benachrichtige Spieler
            if (targetPlayer != null) {
                targetPlayer.sendMessage("§c§lRevenant Horror Tier " + tier + " §c§list erschienen!");
                targetPlayer.sendMessage("§7Bereite dich auf den Kampf vor!");
            }
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public void despawn() {
        if (bossZombie != null && !bossZombie.isDead()) {
            bossZombie.remove();
        }
        
        isAlive = false;
        bossEntity = null;
    }
    
    @Override
    public void tick() {
        if (!isAlive || bossZombie == null || bossZombie.isDead()) {
            isAlive = false;
            return;
        }
        
        // Prüfe Timeout (5 Minuten)
        if (isTimeout(300000)) {
            despawn();
            if (targetPlayer != null) {
                targetPlayer.sendMessage("§cDer Revenant Horror ist verschwunden!");
            }
            return;
        }
        
        // Führe Boss-KI aus
        performBossAI();
        
        // Prüfe Phasen-Wechsel
        checkPhaseTransition();
    }
    
    @Override
    public void onDeath() {
        if (targetPlayer != null) {
            targetPlayer.sendMessage("§a§lRevenant Horror besiegt!");
            targetPlayer.sendMessage("§7Tier: §6" + tier);
            targetPlayer.sendMessage("§7Zeit: §6" + (getTimeAlive() / 1000) + "§7 Sekunden");
        }
        
        isAlive = false;
    }
    
    @Override
    public void onDamage(double damage, Player attacker) {
        // Boss nimmt Schaden
        double newHealth = getHealth() - damage;
        setHealth(newHealth);
        
        // Prüfe ob Boss stirbt
        if (newHealth <= 0) {
            onDeath();
        }
    }
    
    @Override
    public double getHealth() {
        if (bossZombie == null) {
            return 0;
        }
        return bossZombie.getHealth();
    }
    
    @Override
    public double getMaxHealth() {
        // Basis-Gesundheit * Tier
        double baseHealth = 100.0;
        return baseHealth * tier;
    }
    
    @Override
    public void setHealth(double health) {
        if (bossZombie != null) {
            bossZombie.setHealth(Math.max(0, Math.min(health, getMaxHealth())));
        }
    }
    
    @Override
    public String getBossName() {
        return "Revenant Horror Tier " + tier;
    }
    
    @Override
    public int getPhases() {
        return 3; // 3 Phasen
    }
    
    @Override
    public int getCurrentPhase() {
        return currentPhase;
    }
    
    @Override
    public void nextPhase() {
        if (currentPhase < getPhases()) {
            currentPhase++;
            
            if (targetPlayer != null) {
                targetPlayer.sendMessage("§c§lRevenant Horror ist in Phase " + currentPhase + " übergegangen!");
            }
            
            // Phase-spezifische Effekte
            applyPhaseEffects();
        }
    }
    
    /**
     * Konfiguriert den Boss beim Spawn
     */
    private void configureBoss() {
        if (bossZombie == null) {
            return;
        }
        
        // Setze Gesundheit
        setHealth(getMaxHealth());
        
        // Setze Namen
        bossZombie.setCustomName("§c§lRevenant Horror Tier " + tier);
        bossZombie.setCustomNameVisible(true);
        
        // Setze Effekte
        bossZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        bossZombie.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
        
        // Mache den Boss erwachsen
        bossZombie.setBaby(false);
        
        // Setze Ausrüstung (vereinfacht)
        // In der echten Implementierung würde man hier spezielle Items geben
    }
    
    /**
     * Führt die Boss-KI aus
     */
    private void performBossAI() {
        long currentTime = System.currentTimeMillis();
        
        // AoE-Schrei
        if (currentTime - lastScreamTime > SCREAM_COOLDOWN) {
            performScream();
            lastScreamTime = currentTime;
        }
        
        // Minion-Spawn
        if (currentTime - lastMinionSpawnTime > MINION_SPAWN_COOLDOWN) {
            spawnMinions();
            lastMinionSpawnTime = currentTime;
        }
        
        // Verfolge den Spieler
        if (targetPlayer != null && isInRange(50)) {
            bossZombie.setTarget(targetPlayer);
        }
    }
    
    /**
     * Führt den AoE-Schrei aus
     */
    private void performScream() {
        if (targetPlayer == null || !isInRange(10)) {
            return;
        }
        
        // Verlangsame den Spieler
        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2));
        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
        
        // Benachrichtige Spieler
        targetPlayer.sendMessage("§c§lDer Revenant Horror schreit! Du fühlst dich schwach!");
        
        // Visueller Effekt (vereinfacht)
        // In der echten Implementierung würde man hier Partikel spawnen
    }
    
    /**
     * Spawnt verstärkte Zombie-Minions
     */
    private void spawnMinions() {
        if (targetPlayer == null) {
            return;
        }
        
        // Spawne 2-4 Minions
        int minionCount = 2 + (int) (Math.random() * 3);
        
        for (int i = 0; i < minionCount; i++) {
            Location minionLocation = spawnLocation.clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            
            Zombie minion = minionLocation.getWorld().spawn(minionLocation, Zombie.class);
            
            // Konfiguriere Minion
            minion.setCustomName("§7Verstärkter Zombie");
            minion.setCustomNameVisible(true);
            minion.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            minion.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            minion.setTarget(targetPlayer);
        }
        
        if (targetPlayer != null) {
            targetPlayer.sendMessage("§c§lDer Revenant Horror beschwört verstärkte Zombies!");
        }
    }
    
    /**
     * Prüft ob ein Phasen-Wechsel stattfinden soll
     */
    private void checkPhaseTransition() {
        double healthPercentage = getHealth() / getMaxHealth();
        
        if (currentPhase == 1 && healthPercentage <= 0.66) {
            nextPhase();
        } else if (currentPhase == 2 && healthPercentage <= 0.33) {
            nextPhase();
        }
    }
    
    /**
     * Wendet Phase-spezifische Effekte an
     */
    private void applyPhaseEffects() {
        if (bossZombie == null) {
            return;
        }
        
        switch (currentPhase) {
            case 2:
                // Phase 2: Wird unverwundbar und schwebt
                bossZombie.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 1));
                bossZombie.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2));
                break;
            case 3:
                // Phase 3: Wirft explosiven Kuchen
                bossZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                bossZombie.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
                break;
        }
    }
}
