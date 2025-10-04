package de.noctivag.plugin.experiments;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a research project with its requirements and rewards
 */
public class ResearchProject {
    
    private final String name;
    private final Material material;
    private final List<String> requiredExperiments;
    private final List<String> description;
    private final int cost;
    private final int duration;
    
    public ResearchProject(String name, Material material, List<String> requiredExperiments, List<String> description, int cost, int duration) {
        this.name = name;
        this.material = material;
        this.requiredExperiments = requiredExperiments;
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
    
    public List<String> getRequiredExperiments() {
        return requiredExperiments;
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
        return "ResearchProject{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", requiredExperiments=" + requiredExperiments +
                ", description=" + description +
                ", cost=" + cost +
                ", duration=" + duration +
                '}';
    }
}
