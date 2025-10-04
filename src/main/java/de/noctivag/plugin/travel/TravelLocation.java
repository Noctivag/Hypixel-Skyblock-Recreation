package de.noctivag.plugin.travel;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a travel location with coordinates and world
 */
public class TravelLocation {
    
    private final String name;
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    
    public TravelLocation(String name, String worldName, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public String getName() {
        return name;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public float getYaw() {
        return yaw;
    }
    
    public float getPitch() {
        return pitch;
    }
    
    @Override
    public String toString() {
        return "TravelLocation{" +
                "name='" + name + '\'' +
                ", worldName='" + worldName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}
