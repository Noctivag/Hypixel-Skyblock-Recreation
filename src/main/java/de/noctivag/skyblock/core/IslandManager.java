package de.noctivag.skyblock.core;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Island Manager - Verwaltung von Spieler-Inseln
 * 
 * Verantwortlich für:
 * - Insel-Erstellung und -Verwaltung
 * - Zugriffskontrolle
 * - Insel-Upgrades
 * - Insel-Statistiken
 */
public class IslandManager {
    private final UUID playerId;
    private final CorePlatform corePlatform;
    private final Map<String, Object> islandData = new ConcurrentHashMap<>();
    private final Set<UUID> allowedPlayers = new HashSet<>();
    private Location islandLocation;
    private World islandWorld;
    private int islandLevel;
    private boolean isPublic;
    
    public IslandManager(UUID playerId, CorePlatform corePlatform) {
        this.playerId = playerId;
        this.corePlatform = corePlatform;
        this.islandLevel = 1;
        this.isPublic = false;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public Location getIslandLocation() {
        return islandLocation;
    }
    
    public void setIslandLocation(Location location) {
        this.islandLocation = location;
        this.islandWorld = location.getWorld();
    }
    
    public World getIslandWorld() {
        return islandWorld;
    }
    
    public int getIslandLevel() {
        return islandLevel;
    }
    
    public void setIslandLevel(int level) {
        this.islandLevel = Math.max(1, level);
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public void addAllowedPlayer(UUID playerId) {
        allowedPlayers.add(playerId);
    }
    
    public void removeAllowedPlayer(UUID playerId) {
        allowedPlayers.remove(playerId);
    }
    
    public boolean isPlayerAllowed(UUID playerId) {
        return playerId.equals(this.playerId) || allowedPlayers.contains(playerId) || isPublic;
    }
    
    public Set<UUID> getAllowedPlayers() {
        return new HashSet<>(allowedPlayers);
    }
    
    public void setIslandData(String key, Object value) {
        islandData.put(key, value);
    }
    
    public Object getIslandData(String key) {
        return islandData.get(key);
    }
    
    public <T> T getIslandData(String key, Class<T> type) {
        Object value = islandData.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }
    
    public Map<String, Object> getAllIslandData() {
        return new ConcurrentHashMap<>(islandData);
    }
    
    public boolean createIsland(Player player) {
        if (islandLocation != null) {
            return false; // Island already exists
        }
        
        // Create island at spawn location for now
        Location spawnLocation = player.getWorld().getSpawnLocation();
        setIslandLocation(spawnLocation);
        
        return true;
    }
    
    public boolean deleteIsland() {
        if (islandLocation == null) {
            return false; // No island to delete
        }
        
        islandLocation = null;
        islandWorld = null;
        islandData.clear();
        allowedPlayers.clear();
        
        return true;
    }
    
    public boolean upgradeIsland() {
        if (islandLevel >= 10) {
            return false; // Max level reached
        }
        
        islandLevel++;
        return true;
    }
    
    public double getUpgradeCost() {
        return islandLevel * 1000.0;
    }
    
    public boolean canUpgrade() {
        PlayerProfile profile = corePlatform.getPlayerProfile(playerId);
        return profile.hasCoins(getUpgradeCost()) && islandLevel < 10;
    }
    
    public boolean upgradeIsland(Player player) {
        if (!canUpgrade()) {
            return false;
        }
        
        PlayerProfile profile = corePlatform.getPlayerProfile(playerId);
        if (profile.tryRemoveCoins(getUpgradeCost())) {
            upgradeIsland();
            return true;
        }
        
        return false;
    }
    
    public CorePlatform getCorePlatform() {
        return corePlatform;
    }
    
    @Override
    public String toString() {
        return "IslandManager{" +
                "playerId=" + playerId +
                ", islandLevel=" + islandLevel +
                ", isPublic=" + isPublic +
                ", hasLocation=" + (islandLocation != null) +
                '}';
    }
}
