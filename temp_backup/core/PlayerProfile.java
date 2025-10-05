package de.noctivag.skyblock.core;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Profile System - Core player data management
 * 
 * Features:
 * - Player statistics and data
 * - Economy integration
 * - Skill levels and experience
 * - Inventory and item management
 * - Settings and preferences
 */
public class PlayerProfile {
    private final UUID playerId;
    private final String playerName;
    private double coins;
    private int level;
    private long experience;
    private final Map<String, Integer> skillLevels;
    private final Map<String, Long> skillExperience;
    private final Map<String, Object> settings;
    private final Map<String, Object> statistics;
    private long lastLogin;
    private long totalPlayTime;
    private boolean isOnline;
    
    public PlayerProfile(UUID playerId) {
        this(playerId, "Unknown");
    }
    
    public PlayerProfile(UUID playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.coins = 0.0;
        this.level = 1;
        this.experience = 0;
        this.skillLevels = new ConcurrentHashMap<>();
        this.skillExperience = new ConcurrentHashMap<>();
        this.settings = new ConcurrentHashMap<>();
        this.statistics = new ConcurrentHashMap<>();
        this.lastLogin = java.lang.System.currentTimeMillis();
        this.totalPlayTime = 0;
        this.isOnline = false;
        
        // Initialize default skill levels
        initializeDefaultSkills();
    }
    
    private void initializeDefaultSkills() {
        skillLevels.put("mining", 1);
        skillLevels.put("farming", 1);
        skillLevels.put("combat", 1);
        skillLevels.put("foraging", 1);
        skillLevels.put("fishing", 1);
        skillLevels.put("enchanting", 1);
        skillLevels.put("alchemy", 1);
        skillLevels.put("taming", 1);
        skillLevels.put("carpentry", 1);
        skillLevels.put("runecrafting", 1);
        
        // Initialize skill experience
        for (String skill : skillLevels.keySet()) {
            skillExperience.put(skill, 0L);
        }
    }
    
    // Getters and Setters
    public UUID getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    
    public double getCoins() { return coins; }
    public void setCoins(double coins) { this.coins = Math.max(0, coins); }
    public void addCoins(double amount) { this.coins += amount; }
    public void removeCoins(double amount) { this.coins = Math.max(0, this.coins - amount); }
    public boolean hasCoins(double amount) { return this.coins >= amount; }
    public boolean tryRemoveCoins(double amount) {
        if (coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    
    public long getExperience() { return experience; }
    public void setExperience(long experience) { this.experience = Math.max(0, experience); }
    public void addExperience(long amount) { this.experience += amount; }
    
    public int getSkillLevel(String skill) { 
        return skillLevels.getOrDefault(skill, 1); 
    }
    public void setSkillLevel(String skill, int level) { 
        skillLevels.put(skill, Math.max(1, level)); 
    }
    public void addSkillLevel(String skill, int amount) {
        int currentLevel = getSkillLevel(skill);
        setSkillLevel(skill, currentLevel + amount);
    }
    
    public long getSkillExperience(String skill) { 
        return skillExperience.getOrDefault(skill, 0L); 
    }
    public void setSkillExperience(String skill, long experience) { 
        skillExperience.put(skill, Math.max(0, experience)); 
    }
    public void addSkillExperience(String skill, long amount) {
        long currentExp = getSkillExperience(skill);
        setSkillExperience(skill, currentExp + amount);
    }
    
    public Object getSetting(String key) { return settings.get(key); }
    public void setSetting(String key, Object value) { settings.put(key, value); }
    public boolean hasSetting(String key) { return settings.containsKey(key); }
    
    public Object getStatistic(String key) { return statistics.get(key); }
    public void setStatistic(String key, Object value) { statistics.put(key, value); }
    public void incrementStatistic(String key) {
        Object current = statistics.get(key);
        if (current instanceof Integer) {
            statistics.put(key, (Integer) current + 1);
        } else if (current instanceof Long) {
            statistics.put(key, (Long) current + 1);
        } else {
            statistics.put(key, 1);
        }
    }
    
    public long getLastLogin() { return lastLogin; }
    public void setLastLogin(long lastLogin) { this.lastLogin = lastLogin; }
    
    public long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
    public void addPlayTime(long time) { this.totalPlayTime += time; }
    
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { this.isOnline = online; }
    
    // Utility methods
    public Map<String, Integer> getAllSkillLevels() { 
        return new HashMap<>(skillLevels); 
    }
    
    public Map<String, Long> getAllSkillExperience() { 
        return new HashMap<>(skillExperience); 
    }
    
    public Map<String, Object> getAllSettings() { 
        return new HashMap<>(settings); 
    }
    
    public Map<String, Object> getAllStatistics() { 
        return new HashMap<>(statistics); 
    }
    
    public void resetProfile() {
        coins = 0.0;
        level = 1;
        experience = 0;
        skillLevels.clear();
        skillExperience.clear();
        settings.clear();
        statistics.clear();
        initializeDefaultSkills();
    }
    
    // Additional methods for compatibility
    
    /**
     * Check if player has enough balance
     */
    public boolean hasBalance(double amount) {
        return coins >= amount;
    }
    
    /**
     * Update player stats
     */
    public void updateStats(Map<String, Double> stats) {
        // Update various stats based on the map
        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            
            switch (key.toLowerCase()) {
                case "coins":
                    this.coins = value;
                    break;
                case "level":
                    this.level = value.intValue();
                    break;
                case "experience":
                    this.experience = value.longValue();
                    break;
                // Add more stat types as needed
            }
        }
    }
    
    @Override
    public String toString() {
        return "PlayerProfile{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", coins=" + coins +
                ", level=" + level +
                ", experience=" + experience +
                ", isOnline=" + isOnline +
                '}';
    }
}
