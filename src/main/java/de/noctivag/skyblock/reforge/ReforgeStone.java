package de.noctivag.skyblock.reforge;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a reforge stone with its properties
 */
public class ReforgeStone {
    
    private final String name;
    private final Material material;
    private final ReforgeRarity rarity;
    private final List<String> description;
    private final int cost;
    private final String reforgeType;
    
    public ReforgeStone(String name, Material material, ReforgeRarity rarity, List<String> description, int cost, String reforgeType) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.description = description;
        this.cost = cost;
        this.reforgeType = reforgeType;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public ReforgeRarity getRarity() {
        return rarity;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public int getCost() {
        return cost;
    }
    
    public String getReforgeType() {
        return reforgeType;
    }
    
    @Override
    public String toString() {
        return "ReforgeStone{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", rarity=" + rarity +
                ", description=" + description +
                ", cost=" + cost +
                ", reforgeType='" + reforgeType + '\'' +
                '}';
    }
}
