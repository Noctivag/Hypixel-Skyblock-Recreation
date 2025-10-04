package de.noctivag.plugin.multiserver;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repräsentiert Spielerdaten im Multi-Server-System
 */
public class PlayerData {
    
    private final UUID playerId;
    private String currentServer;
    private String lastServer;
    private long lastSeen;
    private long totalPlayTime;
    
    // Spielerdaten
    private int level;
    private long experience;
    private long coins;
    private long gems;
    
    // Statistiken
    private final ConcurrentHashMap<String, Object> statistics = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> inventory = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> settings = new ConcurrentHashMap<>();
    
    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.lastSeen = System.currentTimeMillis();
        this.totalPlayTime = 0;
        this.level = 1;
        this.experience = 0;
        this.coins = 0;
        this.gems = 0;
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public String getCurrentServer() {
        return currentServer;
    }
    
    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }
    
    public String getLastServer() {
        return lastServer;
    }
    
    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }
    
    public long getLastSeen() {
        return lastSeen;
    }
    
    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    public long getTotalPlayTime() {
        return totalPlayTime;
    }
    
    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public long getExperience() {
        return experience;
    }
    
    public void setExperience(long experience) {
        this.experience = experience;
    }
    
    public long getCoins() {
        return coins;
    }
    
    public void setCoins(long coins) {
        this.coins = coins;
    }
    
    public long getGems() {
        return gems;
    }
    
    public void setGems(long gems) {
        this.gems = gems;
    }
    
    // Statistics Management
    public void setStatistic(String key, Object value) {
        statistics.put(key, value);
    }
    
    public Object getStatistic(String key) {
        return statistics.get(key);
    }
    
    public <T> T getStatistic(String key, Class<T> type) {
        Object value = statistics.get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return type.cast(value);
        }
        return null;
    }
    
    public void incrementStatistic(String key, long amount) {
        Object current = statistics.get(key);
        if (current instanceof Number) {
            long newValue = ((Number) current).longValue() + amount;
            statistics.put(key, newValue);
        } else {
            statistics.put(key, amount);
        }
    }
    
    // Inventory Management
    public void setInventoryData(String key, Object value) {
        inventory.put(key, value);
    }
    
    public Object getInventoryData(String key) {
        return inventory.get(key);
    }
    
    // Settings Management
    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }
    
    public Object getSetting(String key) {
        return settings.get(key);
    }
    
    public <T> T getSetting(String key, Class<T> type) {
        Object value = settings.get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return type.cast(value);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("PlayerData{id=%s, server=%s, level=%d, coins=%d, gems=%d}", 
            playerId, currentServer, level, coins, gems);
    }
}
