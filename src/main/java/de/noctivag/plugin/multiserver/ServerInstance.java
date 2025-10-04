package de.noctivag.plugin.multiserver;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Repräsentiert eine Server-Instanz im Multi-Server-System
 */
public class ServerInstance {
    
    private final String instanceId;
    private final ServerType type;
    private ServerStatus status;
    private int playerCount;
    private long createdTime;
    private long lastActivity;
    private long lastRestart;
    private long nextScheduledRestart;
    private boolean isPlayerPersistent;
    private UUID ownerPlayerId; // Für Private Islands und Garden
    
    // Player Management
    private final ConcurrentLinkedQueue<UUID> players = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, Object> metadata = new ConcurrentHashMap<>();
    
    public ServerInstance(String instanceId, ServerType type) {
        this.instanceId = instanceId;
        this.type = type;
        this.status = ServerStatus.STARTING;
        this.playerCount = 0;
        this.createdTime = System.currentTimeMillis();
        this.lastActivity = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getInstanceId() {
        return instanceId;
    }
    
    public ServerType getType() {
        return type;
    }
    
    public ServerStatus getStatus() {
        return status;
    }
    
    public void setStatus(ServerStatus status) {
        this.status = status;
        this.lastActivity = System.currentTimeMillis();
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        this.lastActivity = System.currentTimeMillis();
    }
    
    public long getCreatedTime() {
        return createdTime;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public long getLastRestart() {
        return lastRestart;
    }
    
    public void setLastRestart(long lastRestart) {
        this.lastRestart = lastRestart;
    }
    
    public long getNextScheduledRestart() {
        return nextScheduledRestart;
    }
    
    public void setNextScheduledRestart(long nextScheduledRestart) {
        this.nextScheduledRestart = nextScheduledRestart;
    }
    
    public boolean isPlayerPersistent() {
        return isPlayerPersistent;
    }
    
    public void setPlayerPersistent(boolean playerPersistent) {
        isPlayerPersistent = playerPersistent;
    }
    
    public UUID getOwnerPlayerId() {
        return ownerPlayerId;
    }
    
    public void setOwnerPlayerId(UUID ownerPlayerId) {
        this.ownerPlayerId = ownerPlayerId;
    }
    
    public boolean isFull() {
        return playerCount >= type.getMaxPlayers();
    }
    
    public boolean isEmpty() {
        return playerCount == 0;
    }
    
    public boolean isOnline() {
        return status == ServerStatus.ONLINE;
    }
    
    public boolean isStarting() {
        return status == ServerStatus.STARTING;
    }
    
    public boolean isShuttingDown() {
        return status == ServerStatus.SHUTTING_DOWN;
    }
    
    public boolean needsRestart() {
        if (type.hasRestartCycle()) {
            return System.currentTimeMillis() >= nextScheduledRestart;
        }
        return false;
    }
    
    public boolean canShutdown() {
        return !type.isNeverShutdown() && isEmpty();
    }
    
    public boolean shouldSaveOnShutdown() {
        return isPlayerPersistent || type.isPlayerPersistent();
    }
    
    public void scheduleNextRestart() {
        if (type.hasRestartCycle()) {
            nextScheduledRestart = System.currentTimeMillis() + (type.getRestartIntervalSeconds() * 1000);
        }
    }
    
    // Player Management
    public boolean addPlayer(UUID playerId) {
        if (isFull() || !isOnline()) {
            return false;
        }
        
        if (players.add(playerId)) {
            playerCount++;
            lastActivity = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    public boolean removePlayer(UUID playerId) {
        if (players.remove(playerId)) {
            playerCount = Math.max(0, playerCount - 1);
            lastActivity = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    public boolean hasPlayer(UUID playerId) {
        return players.contains(playerId);
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
    
    public void removeMetadata(String key) {
        metadata.remove(key);
    }
    
    @Override
    public String toString() {
        return String.format("ServerInstance{id='%s', type='%s', status=%s, players=%d/%d}", 
            instanceId, type.getTypeId(), status, playerCount, type.getMaxPlayers());
    }
}
