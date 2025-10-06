package de.noctivag.skyblock.zones;

/**
 * Represents a spawn point within a zone
 */
public class ZoneSpawn {
    
    private final String name;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    
    public ZoneSpawn(String name, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    // Getters
    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    
    @Override
    public String toString() {
        return String.format("ZoneSpawn(%s: %.2f,%.2f,%.2f)", name, x, y, z);
    }
}
