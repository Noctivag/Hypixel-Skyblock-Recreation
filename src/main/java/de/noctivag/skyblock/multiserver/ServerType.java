package de.noctivag.skyblock.multiserver;
import org.bukkit.inventory.ItemStack;

/**
 * Definiert einen Server-Typ im Multi-Server-System
 */
public class ServerType {
    
    private final String typeId;
    private final String displayName;
    private final int maxPlayers;
    private final boolean persistent;
    private final boolean gameServer;
    private final boolean neverShutdown;
    private final long restartIntervalSeconds;
    
    public ServerType(String typeId, String displayName, int maxPlayers, boolean persistent, boolean gameServer) {
        this(typeId, displayName, maxPlayers, persistent, gameServer, false, 0);
    }
    
    public ServerType(String typeId, String displayName, int maxPlayers, boolean persistent, boolean gameServer, boolean neverShutdown, long restartIntervalSeconds) {
        this.typeId = typeId;
        this.displayName = displayName;
        this.maxPlayers = maxPlayers;
        this.persistent = persistent;
        this.gameServer = gameServer;
        this.neverShutdown = neverShutdown;
        this.restartIntervalSeconds = restartIntervalSeconds;
    }
    
    public String getTypeId() {
        return typeId;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public boolean isPersistent() {
        return persistent;
    }
    
    public boolean isGameServer() {
        return gameServer;
    }
    
    public boolean isHubServer() {
        return !gameServer && persistent;
    }
    
    public boolean isLobbyServer() {
        return !gameServer && !persistent;
    }
    
    public boolean isNeverShutdown() {
        return neverShutdown;
    }
    
    public long getRestartIntervalSeconds() {
        return restartIntervalSeconds;
    }
    
    public boolean hasRestartCycle() {
        return restartIntervalSeconds > 0;
    }
    
    public boolean isPlayerPersistent() {
        return persistent && !gameServer;
    }
    
    public boolean isTemporary() {
        return !persistent && !neverShutdown && restartIntervalSeconds == 0;
    }
    
    @Override
    public String toString() {
        return String.format("ServerType{id='%s', name='%s', maxPlayers=%d, persistent=%s, gameServer=%s, neverShutdown=%s, restartInterval=%ds}", 
            typeId, displayName, maxPlayers, persistent, gameServer, neverShutdown, restartIntervalSeconds);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServerType that = (ServerType) obj;
        return typeId.equals(that.typeId);
    }
    
    @Override
    public int hashCode() {
        return typeId.hashCode();
    }
}
