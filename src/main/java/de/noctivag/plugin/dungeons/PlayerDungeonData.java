package de.noctivag.plugin.dungeons;

import java.util.UUID;

/**
 * Player Dungeon Data - Tracks individual player progress in dungeons
 */
public class PlayerDungeonData {
    
    private final UUID playerId;
    private final DungeonFloor currentFloor;
    private final long startTime;
    private int score;
    private int secretsFound;
    private int deaths;
    private int damageDealt;
    private int damageTaken;
    private int mobsKilled;
    private int bossDamage;
    
    public PlayerDungeonData(UUID playerId, DungeonFloor currentFloor) {
        this.playerId = playerId;
        this.currentFloor = currentFloor;
        this.startTime = System.currentTimeMillis();
        this.score = 0;
        this.secretsFound = 0;
        this.deaths = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
        this.mobsKilled = 0;
        this.bossDamage = 0;
    }
    
    /**
     * Get player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Get current floor
     */
    public DungeonFloor getCurrentFloor() {
        return currentFloor;
    }
    
    /**
     * Get start time
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Get score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Add score
     */
    public void addScore(int points) {
        this.score += points;
    }
    
    /**
     * Get secrets found
     */
    public int getSecretsFound() {
        return secretsFound;
    }
    
    /**
     * Add secret found
     */
    public void addSecretFound() {
        this.secretsFound++;
    }
    
    /**
     * Get deaths
     */
    public int getDeaths() {
        return deaths;
    }
    
    /**
     * Add death
     */
    public void addDeath() {
        this.deaths++;
    }
    
    /**
     * Get damage dealt
     */
    public int getDamageDealt() {
        return damageDealt;
    }
    
    /**
     * Add damage dealt
     */
    public void addDamageDealt(int damage) {
        this.damageDealt += damage;
    }
    
    /**
     * Get damage taken
     */
    public int getDamageTaken() {
        return damageTaken;
    }
    
    /**
     * Add damage taken
     */
    public void addDamageTaken(int damage) {
        this.damageTaken += damage;
    }
    
    /**
     * Get mobs killed
     */
    public int getMobsKilled() {
        return mobsKilled;
    }
    
    /**
     * Add mob killed
     */
    public void addMobKilled() {
        this.mobsKilled++;
    }
    
    /**
     * Get boss damage
     */
    public int getBossDamage() {
        return bossDamage;
    }
    
    /**
     * Add boss damage
     */
    public void addBossDamage(int damage) {
        this.bossDamage += damage;
    }
    
    /**
     * Get elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Get elapsed time in seconds
     */
    public long getElapsedTimeSeconds() {
        return getElapsedTime() / 1000;
    }
    
    /**
     * Calculate performance score
     */
    public int calculatePerformanceScore() {
        int performance = score;
        
        // Bonus for secrets found
        performance += secretsFound * 100;
        
        // Bonus for mobs killed
        performance += mobsKilled * 10;
        
        // Bonus for boss damage
        performance += bossDamage / 1000;
        
        // Penalty for deaths
        performance -= deaths * 50;
        
        return Math.max(performance, 0);
    }
}
