package de.noctivag.skyblock.multiserver;
import org.bukkit.inventory.ItemStack;

/**
 * Definiert den Status einer Server-Instanz
 */
public enum ServerStatus {
    STARTING("Starting", "Server is starting up"),
    ONLINE("Online", "Server is online and accepting players"),
    MAINTENANCE("Maintenance", "Server is in maintenance mode"),
    SHUTTING_DOWN("Shutting Down", "Server is shutting down"),
    OFFLINE("Offline", "Server is offline");
    
    private final String displayName;
    private final String description;
    
    ServerStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isOperational() {
        return this == ONLINE;
    }
    
    public boolean isTransitioning() {
        return this == STARTING || this == SHUTTING_DOWN;
    }
    
    public boolean isOffline() {
        return this == OFFLINE;
    }
}
