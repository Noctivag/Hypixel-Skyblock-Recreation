package de.noctivag.skyblock.runes;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a rune type with its properties
 */
public class RuneType {
    
    private final String name;
    private final Material material;
    private final RuneCategory category;
    private final List<String> effects;
    private final int cost;
    
    public RuneType(String name, Material material, RuneCategory category, List<String> effects, int cost) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.effects = effects;
        this.cost = cost;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public RuneCategory getCategory() {
        return category;
    }
    
    public List<String> getEffects() {
        return effects;
    }
    
    public int getCost() {
        return cost;
    }
    
    @Override
    public String toString() {
        return "RuneType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", category=" + category +
                ", effects=" + effects +
                ", cost=" + cost +
                '}';
    }
}
