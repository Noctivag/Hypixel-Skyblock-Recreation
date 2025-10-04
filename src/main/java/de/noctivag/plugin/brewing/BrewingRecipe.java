package de.noctivag.plugin.brewing;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.potion.PotionType;

import java.util.List;

/**
 * Represents a brewing recipe for creating potions
 */
public class BrewingRecipe {
    
    private final String name;
    private final List<Material> ingredients;
    private final PotionType potionType;
    private final int level;
    private final int cost;
    
    public BrewingRecipe(String name, List<Material> ingredients, PotionType potionType, int level, int cost) {
        this.name = name;
        this.ingredients = ingredients;
        this.potionType = potionType;
        this.level = level;
        this.cost = cost;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Material> getIngredients() {
        return ingredients;
    }
    
    public PotionType getPotionType() {
        return potionType;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getCost() {
        return cost;
    }
    
    @Override
    public String toString() {
        return "BrewingRecipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", potionType=" + potionType +
                ", level=" + level +
                ", cost=" + cost +
                '}';
    }
}
