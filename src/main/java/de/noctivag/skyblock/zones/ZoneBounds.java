package de.noctivag.skyblock.zones;

import org.bukkit.Location;

/**
 * Represents the boundaries of a zone
 */
public class ZoneBounds {
    
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    
    public ZoneBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }
    
    /**
     * Check if a location is within these bounds
     */
    public boolean contains(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        
        return x >= minX && x <= maxX &&
               y >= minY && y <= maxY &&
               z >= minZ && z <= maxZ;
    }
    
    /**
     * Get the center point of these bounds
     */
    public Location getCenter(org.bukkit.World world) {
        double centerX = (minX + maxX) / 2.0;
        double centerY = (minY + maxY) / 2.0;
        double centerZ = (minZ + maxZ) / 2.0;
        
        return new Location(world, centerX, centerY, centerZ);
    }
    
    /**
     * Get the volume of these bounds
     */
    public long getVolume() {
        return (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
    }
    
    /**
     * Check if these bounds intersect with another set of bounds
     */
    public boolean intersects(ZoneBounds other) {
        return !(maxX < other.minX || minX > other.maxX ||
                 maxY < other.minY || minY > other.maxY ||
                 maxZ < other.minZ || minZ > other.maxZ);
    }
    
    // Getters
    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMaxZ() { return maxZ; }
    
    @Override
    public String toString() {
        return String.format("ZoneBounds(%d,%d,%d to %d,%d,%d)", minX, minY, minZ, maxX, maxY, maxZ);
    }
}
