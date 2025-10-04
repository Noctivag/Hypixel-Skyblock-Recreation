package de.noctivag.plugin.experiments;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents an experiment type with its properties
 */
public class ExperimentType {
    
    private final String name;
    private final Material material;
    private final ExperimentCategory category;
    private final List<String> description;
    private final int cost;
    private final int duration;
    
    public ExperimentType(String name, Material material, ExperimentCategory category, List<String> description, int cost, int duration) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public ExperimentCategory getCategory() {
        return category;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
        return "ExperimentType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", category=" + category +
                ", description=" + description +
                ", cost=" + cost +
                ", duration=" + duration +
                '}';
    }
}
