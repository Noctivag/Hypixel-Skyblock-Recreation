package de.noctivag.skyblock.core;

import java.util.UUID;

/**
 * Player Profile - Represents a player's profile
 */
public class PlayerProfile {
    
    private final UUID uuid;
    private final String name;
    private final long lastLogin;
    private final long totalPlayTime;
    
    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        this.name = "Unknown"; // Placeholder
        this.lastLogin = java.lang.System.currentTimeMillis();
        this.totalPlayTime = 0;
    }
    
    /**
     * Get the player UUID
     */
    public UUID getUuid() {
        return uuid;
    }
    
    /**
     * Get the player name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the last login time
     */
    public long getLastLogin() {
        return lastLogin;
    }
    
    /**
     * Get the total play time
     */
    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    private double coins = 0.0;

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public void addCoins(double amount) {
        this.coins += amount;
    }

    public boolean tryRemoveCoins(double amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Check if player has enough balance
     */
    public boolean hasBalance(double amount) {
        return coins >= amount;
    }
    
    /**
     * Remove coins from the player
     */
    public void removeCoins(double amount) {
        this.coins = Math.max(0, this.coins - amount);
    }
    
    /**
     * Join method for async operations
     */
    public void join() {
        // Placeholder for async operations
    }
    
    /**
     * Set last logout time
     */
    public void setLastLogout(long time) {
        // Placeholder
    }
    
    /**
     * Get skill level
     */
    public int getSkillLevel(String skill) {
        return 1; // Placeholder
    }
    
    /**
     * Get collections
     */
    public java.util.Map<String, Object> getCollections() {
        return new java.util.HashMap<>(); // Placeholder
    }
}