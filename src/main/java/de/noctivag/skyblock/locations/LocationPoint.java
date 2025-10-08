package de.noctivag.skyblock.locations;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Gemeinsame Basisklasse für Home und Warp.
 */
public abstract class LocationPoint implements ConfigurationSerializable {
    protected final String name;
    protected Location location;

    public LocationPoint(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("location", location);
        return map;
    }
}
