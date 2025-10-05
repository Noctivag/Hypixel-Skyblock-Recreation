package de.noctivag.skyblock.bosses;

import de.noctivag.skyblock.enums.SlayerType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Abstrakte Basis-Klasse für alle Slayer-Bosse
 * Definiert das gemeinsame Interface und grundlegende Funktionalität
 */
public abstract class SlayerBoss {
    
    protected final SlayerType slayerType;
    protected final int tier;
    protected final Location spawnLocation;
    protected final Player targetPlayer;
    protected Entity bossEntity;
    protected boolean isAlive;
    protected long spawnTime;
    
    public SlayerBoss(SlayerType slayerType, int tier, Location spawnLocation, Player targetPlayer) {
        this.slayerType = slayerType;
        this.tier = tier;
        this.spawnLocation = spawnLocation;
        this.targetPlayer = targetPlayer;
        this.isAlive = false;
        this.spawnTime = 0;
    }
    
    /**
     * Spawnt den Boss
     * @return true wenn erfolgreich gespawnt
     */
    public abstract boolean spawn();
    
    /**
     * Entfernt den Boss
     */
    public abstract void despawn();
    
    /**
     * Führt die Boss-KI aus
     */
    public abstract void tick();
    
    /**
     * Wird aufgerufen, wenn der Boss stirbt
     */
    public abstract void onDeath();
    
    /**
     * Wird aufgerufen, wenn der Boss Schaden nimmt
     * @param damage Der Schaden
     * @param attacker Der Angreifer
     */
    public abstract void onDamage(double damage, Player attacker);
    
    /**
     * Gibt die Gesundheit des Bosses zurück
     * @return Gesundheit
     */
    public abstract double getHealth();
    
    /**
     * Gibt die maximale Gesundheit des Bosses zurück
     * @return Maximale Gesundheit
     */
    public abstract double getMaxHealth();
    
    /**
     * Setzt die Gesundheit des Bosses
     * @param health Die neue Gesundheit
     */
    public abstract void setHealth(double health);
    
    /**
     * Gibt den Namen des Bosses zurück
     * @return Boss-Name
     */
    public abstract String getBossName();
    
    /**
     * Gibt die Boss-Phasen zurück
     * @return Anzahl der Phasen
     */
    public abstract int getPhases();
    
    /**
     * Gibt die aktuelle Phase zurück
     * @return Aktuelle Phase
     */
    public abstract int getCurrentPhase();
    
    /**
     * Wechselt zur nächsten Phase
     */
    public abstract void nextPhase();
    
    /**
     * Prüft ob der Boss in Reichweite des Spielers ist
     * @param maxDistance Maximale Distanz
     * @return true wenn in Reichweite
     */
    public boolean isInRange(double maxDistance) {
        if (bossEntity == null || targetPlayer == null) {
            return false;
        }
        
        return bossEntity.getLocation().distance(targetPlayer.getLocation()) <= maxDistance;
    }
    
    /**
     * Gibt die Zeit seit dem Spawn zurück
     * @return Zeit in Millisekunden
     */
    public long getTimeAlive() {
        if (spawnTime == 0) {
            return 0;
        }
        
        return System.currentTimeMillis() - spawnTime;
    }
    
    /**
     * Prüft ob der Boss zu lange lebt (Timeout)
     * @param maxTime Maximale Zeit in Millisekunden
     * @return true wenn Timeout erreicht
     */
    public boolean isTimeout(long maxTime) {
        return getTimeAlive() > maxTime;
    }
    
    // Getter-Methoden
    public SlayerType getSlayerType() {
        return slayerType;
    }
    
    public int getTier() {
        return tier;
    }
    
    public Location getSpawnLocation() {
        return spawnLocation;
    }
    
    public Player getTargetPlayer() {
        return targetPlayer;
    }
    
    public Entity getBossEntity() {
        return bossEntity;
    }
    
    public boolean isAlive() {
        return isAlive;
    }
    
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    
    public long getSpawnTime() {
        return spawnTime;
    }
    
    public void setSpawnTime(long spawnTime) {
        this.spawnTime = spawnTime;
    }
}
