package de.noctivag.skyblock.brewing;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for brewing data
 */
public class BrewingCache {
    
    private final Map<UUID, BrewingData> brewingData = new ConcurrentHashMap<>();
    private final Map<String, BrewingRecipe> recipeCache = new ConcurrentHashMap<>();
    
    /**
     * Add brewing data
     */
    public void addBrewingData(UUID playerUuid, BrewingData data) {
        brewingData.put(playerUuid, data);
    }
    
    /**
     * Remove brewing data
     */
    public void removeBrewingData(UUID playerUuid) {
        brewingData.remove(playerUuid);
    }
    
    /**
     * Get brewing data
     */
    public BrewingData getBrewingData(UUID playerUuid) {
        return brewingData.get(playerUuid);
    }
    
    /**
     * Check if player is brewing
     */
    public boolean isPlayerBrewing(UUID playerUuid) {
        return brewingData.containsKey(playerUuid);
    }
    
    /**
     * Add recipe to cache
     */
    public void addRecipe(String recipeId, BrewingRecipe recipe) {
        recipeCache.put(recipeId, recipe);
    }
    
    /**
     * Remove recipe from cache
     */
    public void removeRecipe(String recipeId) {
        recipeCache.remove(recipeId);
    }
    
    /**
     * Get recipe from cache
     */
    public BrewingRecipe getRecipe(String recipeId) {
        return recipeCache.get(recipeId);
    }
    
    /**
     * Check if recipe is cached
     */
    public boolean isRecipeCached(String recipeId) {
        return recipeCache.containsKey(recipeId);
    }
    
    /**
     * Clear all brewing data
     */
    public void clearBrewingData() {
        brewingData.clear();
    }
    
    /**
     * Clear all recipe cache
     */
    public void clearRecipeCache() {
        recipeCache.clear();
    }
    
    /**
     * Clear all cache
     */
    public void clearAll() {
        clearBrewingData();
        clearRecipeCache();
    }
    
    /**
     * Get brewing data count
     */
    public int getBrewingDataCount() {
        return brewingData.size();
    }
    
    /**
     * Get recipe cache count
     */
    public int getRecipeCacheCount() {
        return recipeCache.size();
    }

    /**
     * Get all brewing data entries
     */
    public Map<UUID, BrewingData> getAllBrewingData() {
        return new ConcurrentHashMap<>(brewingData);
    }
}
