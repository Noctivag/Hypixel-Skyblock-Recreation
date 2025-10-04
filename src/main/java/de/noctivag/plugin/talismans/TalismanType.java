package de.noctivag.plugin.talismans;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a talisman type with its properties
 */
public class TalismanType {
    
    private final String name;
    private final Material material;
    private final TalismanRarity rarity;
    private final TalismanCategory category;
    private final List<String> effects;
    private final int cost;
    private final int level;
    
    public TalismanType(String name, Material material, TalismanRarity rarity, TalismanCategory category, List<String> effects, int cost, int level) {
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
    
    public TalismanRarity getRarity() {
        return rarity;
    }
    
    public TalismanCategory getCategory() {
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
        return "TalismanType{" +
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
