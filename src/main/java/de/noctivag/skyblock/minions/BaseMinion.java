package de.noctivag.skyblock.minions;

import org.bukkit.Location;
import org.bukkit.Material;
import java.util.UUID;

/**
 * Basisklasse für alle Minion-Objekte
 */
public abstract class BaseMinion {
    protected String minionId;
    protected UUID ownerId;
    protected String name;
    protected String displayName;
    protected Material material;
    protected int level;
    protected boolean active;
    protected Location location;

    public BaseMinion(String minionId, UUID ownerId, String name, String displayName, Material material, int level, boolean active, Location location) {
        this.minionId = minionId;
        this.ownerId = ownerId;
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.level = level;
        this.active = active;
        this.location = location;
    }

    public String getMinionId() { return minionId; }
    public UUID getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
    public int getLevel() { return level; }
    public boolean isActive() { return active; }
    public Location getLocation() { return location; }

    public void setLevel(int level) { this.level = level; }
    public void setActive(boolean active) { this.active = active; }
    public void setLocation(Location location) { this.location = location; }
}
