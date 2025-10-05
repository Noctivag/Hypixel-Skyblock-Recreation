package de.noctivag.skyblock.engine.crafting.types;

import de.noctivag.skyblock.engine.crafting.HypixelCraftingSystem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Represents a tree structure for crafting recipes
 */
public class RecipeTree {
    private final Map<String, RecipeNode> recipes;
    private final HypixelCraftingSystem craftingSystem;

    public RecipeTree(HypixelCraftingSystem craftingSystem) {
        this.recipes = new HashMap<>();
        this.craftingSystem = craftingSystem;
    }

    public void addRecipe(String recipeId, RecipeNode node) {
        recipes.put(recipeId, node);
    }

    public RecipeNode getRecipe(String recipeId) {
        return recipes.get(recipeId);
    }

    public Collection<RecipeNode> getAllRecipes() {
        return recipes.values();
    }

    public Set<String> getRecipeIds() {
        return recipes.keySet();
    }

    public boolean hasRecipe(String recipeId) {
        return recipes.containsKey(recipeId);
    }

    public void removeRecipe(String recipeId) {
        recipes.remove(recipeId);
    }

    public void clearRecipes() {
        recipes.clear();
    }

    public int getRecipeCount() {
        return recipes.size();
    }

    /**
     * Represents a node in the recipe tree
     */
    public static class RecipeNode {
        private final String id;
        private final ItemStack result;
        private final Map<Integer, ItemStack> ingredients;
        private final int craftingTime;
        private final String category;
        private final List<String> prerequisites;
        private final Map<String, Object> metadata;

        public RecipeNode(String id, ItemStack result, Map<Integer, ItemStack> ingredients, 
                         int craftingTime, String category) {
            this.id = id;
            this.result = result;
            this.ingredients = new HashMap<>(ingredients);
            this.craftingTime = craftingTime;
            this.category = category;
            this.prerequisites = new ArrayList<>();
            this.metadata = new HashMap<>();
        }

        public String getId() {
            return id;
        }

        public ItemStack getResult() {
            return result.clone();
        }

        public Map<Integer, ItemStack> getIngredients() {
            return new HashMap<>(ingredients);
        }

        public int getCraftingTime() {
            return craftingTime;
        }

        public String getCategory() {
            return category;
        }

        public List<String> getPrerequisites() {
            return new ArrayList<>(prerequisites);
        }

        public void addPrerequisite(String prerequisite) {
            prerequisites.add(prerequisite);
        }

        public Map<String, Object> getMetadata() {
            return new HashMap<>(metadata);
        }

        public void setMetadata(String key, Object value) {
            metadata.put(key, value);
        }

        @Override
        public String toString() {
            return String.format("RecipeNode{id='%s', result=%s, category='%s', craftingTime=%d}", 
                               id, result.getType(), category, craftingTime);
        }
    }
}
