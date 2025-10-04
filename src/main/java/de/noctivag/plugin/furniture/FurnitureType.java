package de.noctivag.plugin.furniture;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a furniture type with its properties
 */
public class FurnitureType {
    
    private final String name;
    private final Material material;
    private final FurnitureCategory category;
    private final List<String> description;
    private final int cost;
    
    public FurnitureType(String name, Material material, FurnitureCategory category, List<String> description, int cost) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.description = description;
        this.cost = cost;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public FurnitureCategory getCategory() {
        return category;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public int getCost() {
        return cost;
    }
    
    @Override
    public String toString() {
        return "FurnitureType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", category=" + category +
                ", description=" + description +
                ", cost=" + cost +
                '}';
    }
}
