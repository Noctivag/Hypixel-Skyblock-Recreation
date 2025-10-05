package de.noctivag.skyblock.worlds;

/**
 * World metrics class
 */
public class WorldMetrics {
    
    private final String worldName;
    private final int playerCount;
    private final long uptime;
    private final double memoryUsage;
    private final int chunkCount;
    private final long lastReset;
    
    public WorldMetrics(String worldName, int playerCount, long uptime, double memoryUsage, int chunkCount, long lastReset) {
        this.worldName = worldName;
        this.playerCount = playerCount;
        this.uptime = uptime;
        this.memoryUsage = memoryUsage;
        this.chunkCount = chunkCount;
        this.lastReset = lastReset;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public long getUptime() {
        return uptime;
    }
    
    public double getMemoryUsage() {
        return memoryUsage;
    }
    
    public int getChunkCount() {
        return chunkCount;
    }
    
    public long getLastReset() {
        return lastReset;
    }
}
