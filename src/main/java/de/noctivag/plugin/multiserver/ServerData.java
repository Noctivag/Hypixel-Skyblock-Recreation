package de.noctivag.plugin.multiserver;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Repräsentiert Server-Daten im Multi-Server-System
 */
public class ServerData {
    
    private final String serverId;
    private int playerCount;
    private long lastUpdate;
    private String status;
    
    // Server-Metadaten
    private final ConcurrentHashMap<String, Object> metadata = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> statistics = new ConcurrentHashMap<>();
    
    public ServerData(String serverId) {
        this.serverId = serverId;
        this.playerCount = 0;
        this.lastUpdate = System.currentTimeMillis();
        this.status = "unknown";
    }
    
    // Getters and Setters
    public String getServerId() {
        return serverId;
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public long getLastUpdate() {
        return lastUpdate;
    }
    
    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.lastUpdate = System.currentTimeMillis();
    }
    
    // Metadata Management
    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    public <T> T getMetadata(String key, Class<T> type) {
        Object value = metadata.get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return type.cast(value);
        }
        return null;
    }
    
    // Statistics Management
    public void setStatistic(String key, Object value) {
        statistics.put(key, value);
    }
    
    public Object getStatistic(String key) {
        return statistics.get(key);
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
    
    @Override
    public String toString() {
        return String.format("ServerData{id='%s', players=%d, status='%s', lastUpdate=%d}", 
            serverId, playerCount, status, lastUpdate);
    }
}
