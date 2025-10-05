package de.noctivag.skyblock.models;

import java.util.UUID;

/**
 * Spielerprofil-Model für das Caching-System
 * Repräsentiert alle wichtigen Spielerdaten
 */
public class PlayerProfile {
    
    private final UUID uuid;
    private String playerName;
    private long lastLogin;
    private long lastSave;
    private long playTime;
    private int level;
    private double coins;
    private String currentWorld;
    private boolean isOnline;
    
    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        this.lastLogin = System.currentTimeMillis();
        this.lastSave = System.currentTimeMillis();
        this.playTime = 0;
        this.level = 1;
        this.coins = 0.0;
        this.currentWorld = "hub_a";
        this.isOnline = false;
    }
    
    public PlayerProfile(UUID uuid, String playerName) {
        this(uuid);
        this.playerName = playerName;
    }
    
    // Getter-Methoden
    public UUID getUuid() {
        return uuid;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public long getLastLogin() {
        return lastLogin;
    }
    
    public long getLastSave() {
        return lastSave;
    }
    
    public long getPlayTime() {
        return playTime;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getCoins() {
        return coins;
    }
    
    public String getCurrentWorld() {
        return currentWorld;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    // Setter-Methoden
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public void setLastSave(long lastSave) {
        this.lastSave = lastSave;
    }
    
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setCoins(double coins) {
        this.coins = coins;
    }
    
    public void setCurrentWorld(String currentWorld) {
        this.currentWorld = currentWorld;
    }
    
    public void setOnline(boolean online) {
        isOnline = online;
    }
    
    // Utility-Methoden
    public void addCoins(double amount) {
        this.coins += amount;
    }
    
    public void removeCoins(double amount) {
        this.coins = Math.max(0, this.coins - amount);
    }
    
    public void addPlayTime(long time) {
        this.playTime += time;
    }
    
    public void levelUp() {
        this.level++;
    }
    
    @Override
    public String toString() {
        return "PlayerProfile{" +
                "uuid=" + uuid +
                ", playerName='" + playerName + '\'' +
                ", level=" + level +
                ", coins=" + coins +
                ", currentWorld='" + currentWorld + '\'' +
                ", isOnline=" + isOnline +
                '}';
    }
}
