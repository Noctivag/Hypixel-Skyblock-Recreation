package de.noctivag.skyblock.brewing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.List;

/**
 * Represents a brewing recipe
 */
public class BrewingRecipe {
    
    private final String id;
    private final String name;
    private final List<ItemStack> ingredients;
    private final ItemStack result;
    private final int brewingTime;
    private final int experience;
    private final int level;
    private final int cost;
    private final PotionType potionType;
    
    public BrewingRecipe(String id, String name, List<ItemStack> ingredients, 
                        ItemStack result, int brewingTime, int experience) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.result = result;
        this.brewingTime = brewingTime;
        this.experience = experience;
        this.level = 1;
        this.cost = 100;
        this.potionType = PotionType.AWKWARD;
    }

    // Alternative constructor for the calls in AdvancedBrewingSystem
    public BrewingRecipe(String id, List<Material> materials, PotionType potionType, int level, int cost) {
        this.id = id;
        this.name = id;
        this.ingredients = materials.stream().map(ItemStack::new).toList();
        this.result = new ItemStack(Material.POTION);
        this.brewingTime = 200; // 10 seconds
        this.experience = level * 10;
        this.level = level;
        this.cost = cost;
        this.potionType = potionType;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<ItemStack> getIngredients() { return ingredients; }
    public ItemStack getResult() { return result; }
    public int getBrewingTime() { return brewingTime; }
    public int getExperience() { return experience; }
    public int getLevel() { return level; }
    public int getCost() { return cost; }
    public PotionType getPotionType() { return potionType; }
}