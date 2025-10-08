package de.noctivag.skyblock.locations;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;


public class Warp extends LocationPoint {
    private String permission;
    private String description;

    public Warp(String name, Location location, String permission, String description) {
        super(name, location);
        this.permission = permission != null ? permission : "";
        this.description = description != null ? description : "";
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("permission", permission);
        map.put("description", description);
        return map;
    }

    public static Warp deserialize(Map<String, Object> map) {
        return new Warp(
            (String) map.get("name"),
            (Location) map.get("location"),
            (String) map.get("permission"),
            (String) map.get("description")
        );
    }
}
