package de.noctivag.plugin.magic;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a spell type with its properties
 */
public class SpellType {
    
    private final String name;
    private final Material material;
    private final SpellCategory category;
    private final List<String> description;
    private final int manaCost;
    private final int duration;
    private final int experienceValue;
    
    public SpellType(String name, Material material, SpellCategory category, List<String> description, int manaCost, int duration, int experienceValue) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.description = description;
        this.manaCost = manaCost;
        this.duration = duration;
        this.experienceValue = experienceValue;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public SpellCategory getCategory() {
        return category;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public int getManaCost() {
        return manaCost;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getExperienceValue() {
        return experienceValue;
    }
    
    @Override
    public String toString() {
        return "SpellType{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", category=" + category +
                ", description=" + description +
                ", manaCost=" + manaCost +
                ", duration=" + duration +
                ", experienceValue=" + experienceValue +
                '}';
    }
}
