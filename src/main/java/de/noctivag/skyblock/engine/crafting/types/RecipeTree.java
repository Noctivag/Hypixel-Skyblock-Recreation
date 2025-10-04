package de.noctivag.skyblock.engine.crafting.types;

import java.util.*;

/**
 * Recipe Tree
 * 
 * Represents the recipe dependency tree for a player.
 * Shows available recipes and their unlock requirements.
 */
public class RecipeTree {
    
    private final UUID playerId;
    private final HypixelCraftingSystem craftingSystem;
    private final Map<String, RecipeNode> recipeNodes;
    
    public RecipeTree(UUID playerId, HypixelCraftingSystem craftingSystem) {
        this.playerId = playerId;
        this.craftingSystem = craftingSystem;
        this.recipeNodes = new HashMap<>();
        
        buildRecipeTree();
    }
    
    private void buildRecipeTree() {
        // Build recipe dependency tree
        for (CraftingRecipe recipe : craftingSystem.getAllRecipes().values()) {
            RecipeNode node = new RecipeNode(recipe, playerId);
            recipeNodes.put(recipe.getRecipeId(), node);
        }
    }
    
    public Map<String, RecipeNode> getRecipeNodes() {
        return new HashMap<>(recipeNodes);
    }
    
    public RecipeNode getRecipeNode(String recipeId) {
        return recipeNodes.get(recipeId);
    }
    
    public List<RecipeNode> getAvailableRecipes() {
        return recipeNodes.values().stream()
            .filter(RecipeNode::isAvailable)
            .toList();
    }
    
    public List<RecipeNode> getUnlockedRecipes() {
        return recipeNodes.values().stream()
            .filter(RecipeNode::isUnlocked)
            .toList();
    }
    
    public List<RecipeNode> getLockedRecipes() {
        return recipeNodes.values().stream()
            .filter(node -> !node.isUnlocked())
            .toList();
    }
    
    public static class RecipeNode {
        private final CraftingRecipe recipe;
        private final UUID playerId;
        private final boolean unlocked;
        private final boolean available;
        
        public RecipeNode(CraftingRecipe recipe, UUID playerId) {
            this.recipe = recipe;
            this.playerId = playerId;
            this.unlocked = false; // TODO: Check if recipe is unlocked
            this.available = recipe.meetsRequirements(playerId);
        }
        
        public CraftingRecipe getRecipe() {
            return recipe;
        }
        
        public boolean isUnlocked() {
            return unlocked;
        }
        
        public boolean isAvailable() {
            return available;
        }
    }
}
