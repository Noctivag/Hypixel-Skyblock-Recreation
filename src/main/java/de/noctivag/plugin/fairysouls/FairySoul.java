package de.noctivag.plugin.fairysouls;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

/**
 * Represents a fairy soul with its properties
 */
public class FairySoul {
    
    private final String name;
    private final Material material;
    private final FairySoulCategory category;
    private final Location location;
    private final List<String> description;
    private final int experienceValue;
    
    public FairySoul(String name, Material material, FairySoulCategory category, Location location, List<String> description, int experienceValue) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.location = location;
        this.description = description;
        this.experienceValue = experienceValue;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public FairySoulCategory getCategory() {
        return category;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public int getExperienceValue() {
        return experienceValue;
    }
    
    @Override
    public String toString() {
        return "FairySoul{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", category=" + category +
                ", location=" + location +
                ", description=" + description +
                ", experienceValue=" + experienceValue +
                '}';
    }
}
