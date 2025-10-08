package de.noctivag.skyblock.locations;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;


public class Home extends LocationPoint {
    private final String owner;

    public Home(String name, String owner, Location location) {
        super(name, location);
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("owner", owner);
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
