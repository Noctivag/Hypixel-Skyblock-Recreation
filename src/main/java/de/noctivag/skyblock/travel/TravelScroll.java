package de.noctivag.skyblock.travel;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a travel scroll with its properties
 */
public class TravelScroll {
    
    private final String name;
    private final Material material;
    private final String locationId;
    private final List<String> description;
    private final long cooldown;
    
    public TravelScroll(String name, Material material, String locationId, List<String> description, long cooldown) {
        this.name = name;
        this.material = material;
        this.locationId = locationId;
        this.description = description;
        this.cooldown = cooldown;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public String getLocationId() {
        return locationId;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public long getCooldown() {
        return cooldown;
    }
    
    @Override
    public String toString() {
        return "TravelScroll{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", locationId='" + locationId + '\'' +
                ", description=" + description +
                ", cooldown=" + cooldown +
                '}';
    }
}
