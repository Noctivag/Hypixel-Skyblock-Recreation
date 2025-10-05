package de.noctivag.skyblock.brewing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Validator for brewing recipes
 */
public class BrewingValidator {
    
    /**
     * Validate brewing recipe
     */
    public static boolean validateRecipe(BrewingRecipe recipe) {
        if (recipe == null) {
            return false;
        }
        
        // Check if recipe has valid ID
        if (recipe.getId() == null || recipe.getId().isEmpty()) {
            return false;
        }
        
        // Check if recipe has valid name
        if (recipe.getName() == null || recipe.getName().isEmpty()) {
            return false;
        }
        
        // Check if recipe has valid ingredients
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return false;
        }
        
        // Check if all ingredients are valid
        for (ItemStack ingredient : recipe.getIngredients()) {
            if (!isValidIngredient(ingredient)) {
                return false;
            }
        }
        
        // Check if recipe has valid result
        if (recipe.getResult() == null || recipe.getResult().getType() == Material.AIR) {
            return false;
        }
        
        // Check if brewing time is valid
        if (recipe.getBrewingTime() <= 0) {
            return false;
        }
        
        // Check if experience is valid
        if (recipe.getExperience() < 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if ingredient is valid
     */
    private static boolean isValidIngredient(ItemStack ingredient) {
        if (ingredient == null) {
            return false;
        }
        
        if (ingredient.getType() == Material.AIR) {
            return false;
        }
        
        if (ingredient.getAmount() <= 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate ingredient list
     */
    public static boolean validateIngredients(List<ItemStack> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return false;
        }
        
        for (ItemStack ingredient : ingredients) {
            if (!isValidIngredient(ingredient)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Check if recipe can be brewed with given ingredients
     */
    public static boolean canBrewWithIngredients(BrewingRecipe recipe, List<ItemStack> ingredients) {
        if (recipe == null || ingredients == null) {
            return false;
        }
        
        List<ItemStack> recipeIngredients = recipe.getIngredients();
        if (recipeIngredients.size() != ingredients.size()) {
            return false;
        }
        
        for (int i = 0; i < recipeIngredients.size(); i++) {
            ItemStack recipeIngredient = recipeIngredients.get(i);
            ItemStack ingredient = ingredients.get(i);
            
            if (ingredient == null || recipeIngredient == null) {
                return false;
            }
            
            if (ingredient.getType() != recipeIngredient.getType()) {
                return false;
            }
            
            if (ingredient.getAmount() < recipeIngredient.getAmount()) {
                return false;
            }
        }
        
        return true;
    }
}
