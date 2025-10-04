package de.noctivag.plugin.locations;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;

public class Home implements ConfigurationSerializable {
    private final String name;
    private final String owner;
    private Location location;

    public Home(String name, String owner, Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
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
        map.put("owner", owner);
        map.put("location", location);
        return map;
    }

    public static Home deserialize(Map<String, Object> map) {
        return new Home(
            (String) map.get("name"),
            (String) map.get("owner"),
            (Location) map.get("location")
        );
    }
}
