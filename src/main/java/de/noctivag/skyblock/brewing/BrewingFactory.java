package de.noctivag.skyblock.brewing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating brewing recipes
 */
public class BrewingFactory {
    
    /**
     * Create a simple brewing recipe
     */
    public static BrewingRecipe createSimpleRecipe(String id, String name, Material ingredient, 
                                                  Material result, int brewingTime, int experience) {
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.add(new ItemStack(ingredient));
        
        return new BrewingRecipe(id, name, ingredients, new ItemStack(result), brewingTime, experience);
    }
    
    /**
     * Create a complex brewing recipe
     */
    public static BrewingRecipe createComplexRecipe(String id, String name, List<Material> ingredients,
                                                   Material result, int brewingTime, int experience) {
        List<ItemStack> ingredientStacks = new ArrayList<>();
        for (Material ingredient : ingredients) {
            ingredientStacks.add(new ItemStack(ingredient));
        }
        
        return new BrewingRecipe(id, name, ingredientStacks, new ItemStack(result), brewingTime, experience);
    }
    
    /**
     * Create a potion brewing recipe
     */
    public static BrewingRecipe createPotionRecipe(String id, String name, Material baseIngredient,
                                                  Material secondaryIngredient, int brewingTime, int experience) {
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.add(new ItemStack(Material.NETHER_WART));
        ingredients.add(new ItemStack(baseIngredient));
        if (secondaryIngredient != null) {
            ingredients.add(new ItemStack(secondaryIngredient));
        }
        
        return new BrewingRecipe(id, name, ingredients, new ItemStack(Material.POTION), brewingTime, experience);
    }
    
    /**
     * Create a splash potion recipe
     */
    public static BrewingRecipe createSplashPotionRecipe(String id, String name, Material baseIngredient,
                                                        Material secondaryIngredient, int brewingTime, int experience) {
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.add(new ItemStack(Material.NETHER_WART));
        ingredients.add(new ItemStack(baseIngredient));
        if (secondaryIngredient != null) {
            ingredients.add(new ItemStack(secondaryIngredient));
        }
        ingredients.add(new ItemStack(Material.GUNPOWDER));
        
        return new BrewingRecipe(id, name, ingredients, new ItemStack(Material.SPLASH_POTION), brewingTime, experience);
    }
    
    /**
     * Create a lingering potion recipe
     */
    public static BrewingRecipe createLingeringPotionRecipe(String id, String name, Material baseIngredient,
                                                           Material secondaryIngredient, int brewingTime, int experience) {
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.add(new ItemStack(Material.NETHER_WART));
        ingredients.add(new ItemStack(baseIngredient));
        if (secondaryIngredient != null) {
            ingredients.add(new ItemStack(secondaryIngredient));
        }
        ingredients.add(new ItemStack(Material.DRAGON_BREATH));
        
        return new BrewingRecipe(id, name, ingredients, new ItemStack(Material.LINGERING_POTION), brewingTime, experience);
    }
}
