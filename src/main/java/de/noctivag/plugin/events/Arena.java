package de.noctivag.plugin.events;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;

public class Arena {
    private final String id;
    private final String name;
    private final Location corner1;
    private final Location corner2;
    
    public Arena(String id, String name, Location corner1, Location corner2) {
        this.id = id;
        this.name = name;
        this.corner1 = corner1;
        this.corner2 = corner2;
    }
    
    public Location getSpawnLocation() {
        // Calculate center of arena
        double x = (corner1.getX() + corner2.getX()) / 2;
        double y = Math.max(corner1.getY(), corner2.getY()) + 1;
        double z = (corner1.getZ() + corner2.getZ()) / 2;
        
        return new Location(corner1.getWorld(), x, y, z);
    }
    
    public Location getBossSpawnLocation() {
        // Boss spawns at one end of the arena
        double x = corner1.getX() + (corner2.getX() - corner1.getX()) * 0.8;
        double y = Math.max(corner1.getY(), corner2.getY()) + 2;
        double z = corner1.getZ() + (corner2.getZ() - corner1.getZ()) * 0.8;
        
        return new Location(corner1.getWorld(), x, y, z);
    }
    
    public boolean isInside(Location location) {
        if (!location.getWorld().equals(corner1.getWorld())) {
            return false;
        }
        
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());
        
        return location.getX() >= minX && location.getX() <= maxX &&
               location.getY() >= minY && location.getY() <= maxY &&
               location.getZ() >= minZ && location.getZ() <= maxZ;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Location getCorner1() { return corner1; }
    public Location getCorner2() { return corner2; }
}
