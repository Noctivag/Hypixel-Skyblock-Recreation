package de.noctivag.plugin.multiserver;
import org.bukkit.inventory.ItemStack;

import org.bukkit.WorldType;

import java.util.HashMap;
import java.util.Map;

/**
 * Repräsentiert ein Server-Template für die Erstellung von Welten
 */
public class ServerTemplate {
    
    private final String name;
    private final String displayName;
    private final String description;
    private final WorldType worldType;
    private final boolean persistent;
    private final boolean gameServer;
    private final boolean generateStructures;
    private final Map<String, Object> settings = new HashMap<>();
    
    public ServerTemplate(String name, String displayName, String description, WorldType worldType, boolean persistent, boolean gameServer) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.worldType = worldType;
        this.persistent = persistent;
        this.gameServer = gameServer;
        this.generateStructures = !gameServer; // Game Server haben normalerweise keine Strukturen
        
        // Setze Standard-Einstellungen
        initializeDefaultSettings();
    }
    
    /**
     * Initialisiert Standard-Einstellungen basierend auf Template-Typ
     */
    private void initializeDefaultSettings() {
        if (gameServer) {
            // Game Server Einstellungen
            settings.put("doMobSpawning", true);
            settings.put("doDaylightCycle", false);
            settings.put("doWeatherCycle", false);
            settings.put("keepInventory", true);
            settings.put("pvp", true);
            settings.put("difficulty", "NORMAL");
        } else if (persistent) {
            // Hub/Lobby Einstellungen
            settings.put("doMobSpawning", false);
            settings.put("doDaylightCycle", true);
            settings.put("doWeatherCycle", true);
            settings.put("keepInventory", false);
            settings.put("pvp", false);
            settings.put("difficulty", "PEACEFUL");
        } else {
            // Island Einstellungen
            settings.put("doMobSpawning", true);
            settings.put("doDaylightCycle", true);
            settings.put("doWeatherCycle", true);
            settings.put("keepInventory", false);
            settings.put("pvp", false);
            settings.put("difficulty", "NORMAL");
        }
        
        // Allgemeine Einstellungen
        settings.put("spawnProtection", 16);
        settings.put("maxPlayers", 50);
        settings.put("viewDistance", 10);
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public WorldType getWorldType() {
        return worldType;
    }
    
    public boolean isPersistent() {
        return persistent;
    }
    
    public boolean isGameServer() {
        return gameServer;
    }
    
    public boolean isGenerateStructures() {
        return generateStructures;
    }
    
    public Map<String, Object> getSettings() {
        return new HashMap<>(settings);
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
    
    // Setters
    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }
    
    public void removeSetting(String key) {
        settings.remove(key);
    }
    
    // Utility Methods
    public boolean isHubTemplate() {
        return persistent && !gameServer;
    }
    
    public boolean isIslandTemplate() {
        return !persistent && !gameServer;
    }
    
    public boolean isDungeonTemplate() {
        return name.contains("catacombs") || name.contains("master_mode");
    }
    
    public boolean isPublicIslandTemplate() {
        return !persistent && !gameServer && !name.equals("private_island");
    }
    
    @Override
    public String toString() {
        return String.format("ServerTemplate{name='%s', displayName='%s', type=%s, persistent=%s, gameServer=%s}", 
            name, displayName, worldType, persistent, gameServer);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServerTemplate that = (ServerTemplate) obj;
        return name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
