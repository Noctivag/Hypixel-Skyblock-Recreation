package de.noctivag.skyblock.skyblock.data;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Skyblock Data Class
 */
public class PlayerSkyblockData {
    private final UUID playerId;
    private long joinTime;
    private long totalPlayTime;
    private int skyblockLevel;
    private double skyblockXP;
    private Map<String, Object> customData;
    private String currentIsland;
    private String currentProfile;
    private boolean isOnline;
    
    public PlayerSkyblockData(UUID playerId) {
        this.playerId = playerId;
        this.joinTime = java.lang.System.currentTimeMillis();
        this.totalPlayTime = 0;
        this.skyblockLevel = 1;
        this.skyblockXP = 0.0;
        this.customData = new HashMap<>();
        this.currentIsland = "default";
        this.currentProfile = "default";
        this.isOnline = false;
    }
    
    public UUID getPlayerId() { return playerId; }
    public long getJoinTime() { return joinTime; }
    public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
    public long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
    public int getSkyblockLevel() { return skyblockLevel; }
    public void setSkyblockLevel(int skyblockLevel) { this.skyblockLevel = skyblockLevel; }
    public double getSkyblockXP() { return skyblockXP; }
    public void setSkyblockXP(double skyblockXP) { this.skyblockXP = skyblockXP; }
    public Map<String, Object> getCustomData() { return customData; }
    public String getCurrentIsland() { return currentIsland; }
    public void setCurrentIsland(String currentIsland) { this.currentIsland = currentIsland; }
    public String getCurrentProfile() { return currentProfile; }
    public void setCurrentProfile(String currentProfile) { this.currentProfile = currentProfile; }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
}
