package de.noctivag.plugin.mining;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a gemstone type with its properties
 */
public class GemstoneType {
    
    private final String name;
    private final Material material;
    private final GemstoneRarity rarity;
    private final List<String> effects;
    private final int value;
    
    public GemstoneType(String name, Material material, GemstoneRarity rarity, List<String> effects, int value) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.effects = effects;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public GemstoneRarity getRarity() {
        return rarity;
    }
    
    public List<String> getEffects() {
        return effects;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "GemstoneType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", rarity=" + rarity +
                ", effects=" + effects +
                ", value=" + value +
                '}';
    }
}
