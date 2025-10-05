package de.noctivag.skyblock.brewing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Utility class for brewing operations
 */
public class BrewingUtils {
    
    /**
     * Check if item is a valid brewing ingredient
     */
    public static boolean isValidIngredient(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        
        // Check if item is a known brewing ingredient
        Material type = item.getType();
        return type == Material.NETHER_WART ||
               type == Material.GLOWSTONE_DUST ||
               type == Material.REDSTONE ||
               type == Material.GUNPOWDER ||
               type == Material.DRAGON_BREATH ||
               type == Material.FERMENTED_SPIDER_EYE ||
               type == Material.GHAST_TEAR ||
               type == Material.MAGMA_CREAM ||
               type == Material.BLAZE_POWDER ||
               type == Material.SUGAR ||
               type == Material.RABBIT_FOOT ||
               type == Material.GOLDEN_CARROT ||
               type == Material.PUFFERFISH ||
               type == Material.TURTLE_HELMET ||
               type == Material.PHANTOM_MEMBRANE;
    }
    
    /**
     * Get brewing time for ingredient
     */
    public static int getBrewingTime(Material ingredient) {
        switch (ingredient) {
            case NETHER_WART:
                return 200; // 10 seconds
            case GLOWSTONE_DUST:
                return 100; // 5 seconds
            case REDSTONE:
                return 150; // 7.5 seconds
            case GUNPOWDER:
                return 300; // 15 seconds
            case DRAGON_BREATH:
                return 500; // 25 seconds
            default:
                return 200; // Default 10 seconds
        }
    }
    
    /**
     * Get experience reward for ingredient
     */
    public static int getExperienceReward(Material ingredient) {
        switch (ingredient) {
            case NETHER_WART:
                return 10;
            case GLOWSTONE_DUST:
                return 5;
            case REDSTONE:
                return 7;
            case GUNPOWDER:
                return 15;
            case DRAGON_BREATH:
                return 25;
            default:
                return 10;
        }
    }
    
    /**
     * Check if ingredients match recipe
     */
    public static boolean ingredientsMatch(List<ItemStack> ingredients, List<ItemStack> recipeIngredients) {
        if (ingredients.size() != recipeIngredients.size()) {
            return false;
        }
        
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack ingredient = ingredients.get(i);
            ItemStack recipeIngredient = recipeIngredients.get(i);
            
            if (ingredient == null || recipeIngredient == null) {
                return false;
            }
            
            if (ingredient.getType() != recipeIngredient.getType() ||
                ingredient.getAmount() < recipeIngredient.getAmount()) {
                return false;
            }
        }
        
        return true;
    }
}
