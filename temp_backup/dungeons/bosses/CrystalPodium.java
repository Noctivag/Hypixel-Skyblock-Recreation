package de.noctivag.skyblock.dungeons.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a crystal podium in the Necron boss fight
 */
public class CrystalPodium {
    private final Location location;
    private final CrystalType type;
    private boolean active;
    private boolean destroyed;

    public CrystalPodium(Location location, CrystalType type) {
        this.location = location;
        this.type = type;
        this.active = true;
        this.destroyed = false;
    }

    public Location getLocation() {
        return location;
    }

    public CrystalType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void destroy(Player destroyer) {
        this.destroyed = true;
        this.active = false;
        // TODO: Implement destruction effects
    }

    public void placeCrystal() {
        // TODO: Implement crystal placement
    }

    public boolean hasCrystal() {
        return active && !destroyed;
    }

    public enum CrystalType {
        WIND("Wind"),
        ICE("Ice"),
        LIGHTNING("Lightning"),
        FIRE("Fire");

        private final String displayName;

        CrystalType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
