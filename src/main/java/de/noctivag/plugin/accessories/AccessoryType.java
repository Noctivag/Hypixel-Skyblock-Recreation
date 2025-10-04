package de.noctivag.plugin.accessories;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents an accessory type with its properties
 */
public class AccessoryType {
    
    private final String name;
    private final Material material;
    private final AccessoryRarity rarity;
    private final AccessoryCategory category;
    private final List<String> effects;
    private final int cost;
    private final int level;
    
    public AccessoryType(String name, Material material, AccessoryRarity rarity, AccessoryCategory category, List<String> effects, int cost, int level) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.category = category;
        this.effects = effects;
        this.cost = cost;
        this.level = level;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public AccessoryRarity getRarity() {
        return rarity;
    }
    
    public AccessoryCategory getCategory() {
        return category;
    }
    
    public List<String> getEffects() {
        return effects;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return "AccessoryType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", rarity=" + rarity +
                ", category=" + category +
                ", effects=" + effects +
                ", cost=" + cost +
                ", level=" + level +
                '}';
    }
}
