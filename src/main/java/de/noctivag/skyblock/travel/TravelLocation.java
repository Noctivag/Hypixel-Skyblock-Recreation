package de.noctivag.skyblock.travel;

/**
 * Represents a travel location in the fast travel system
 */
public class TravelLocation {
    
    private final String id;
    private final String name;
    private final String color;
    private final String description;
    private final TravelCategory category;
    private final int travelCost;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final boolean unlockedByDefault;
    
    // Full constructor
    public TravelLocation(String id, String name, String color, String description, 
                         TravelCategory category, int travelCost, double x, double y, double z, 
                         float yaw, float pitch, boolean unlockedByDefault) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.category = category;
        this.travelCost = travelCost;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.unlockedByDefault = unlockedByDefault;
    }
    
    // Simple constructor for basic locations (used by AdvancedTravelScrollSystem)
    public TravelLocation(String name, String worldName, double x, double y, double z, float yaw, float pitch) {
        this.id = name.toLowerCase().replace(" ", "_");
        this.name = name;
        this.color = "§f";
        this.description = "Travel location: " + name;
        this.category = TravelCategory.HUB;
        this.travelCost = 0;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.unlockedByDefault = true;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public String getDescription() { return description; }
    public TravelCategory getCategory() { return category; }
    public int getTravelCost() { return travelCost; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public boolean isUnlockedByDefault() { return unlockedByDefault; }
    
    /**
     * Get the display name with color
     */
    public String getDisplayName() {
        return color + name;
    }
    
    /**
     * Get the formatted travel cost
     */
    public String getFormattedCost() {
        if (travelCost == 0) {
            return "&aFree";
        } else {
            return "&e" + travelCost + " coins";
        }
    }
    
    /**
     * Get the unlock requirement description
     */
    public String getUnlockDescription() {
        if (unlockedByDefault) {
            return "&aUnlocked by default";
        } else {
            return "&7Requires unlocking";
        }
    }

    // Placeholder method for AdvancedTravelScrollSystem compatibility
    public String getWorldName() { return "world"; } // Default world name
}