package de.noctivag.skyblock.gemstones;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a gemstone type with its properties
 */
public class GemstoneType {
    
    private final String name;
    private final Material material;
    private final GemstoneCategory category;
    private final GemstoneRarity rarity;
    private final List<String> effects;
    private final int value;
    private final int experienceValue;
    
    public GemstoneType(String name, Material material, GemstoneCategory category, GemstoneRarity rarity, List<String> effects, int value, int experienceValue) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.rarity = rarity;
        this.effects = effects;
        this.value = value;
        this.experienceValue = experienceValue;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public GemstoneCategory getCategory() {
        return category;
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
    
    public int getExperienceValue() {
        return experienceValue;
    }
    
    @Override
    public String toString() {
        return "GemstoneType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", category=" + category +
                ", rarity=" + rarity +
                ", effects=" + effects +
                ", value=" + value +
                ", experienceValue=" + experienceValue +
                '}';
    }
}
