package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.CollectionType;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Repräsentiert ein Crafting-Rezept
 */
public class Recipe {
    
    private final String name;
    private final RecipeType type;
    private final Map<Material, Integer> requiredItems;
    private final org.bukkit.inventory.ItemStack result;
    private final CollectionType requiredCollection;
    private final int requiredCollectionLevel;
    
    public Recipe(String name, RecipeType type, Map<Material, Integer> requiredItems, 
                  org.bukkit.inventory.ItemStack result, CollectionType requiredCollection, int requiredCollectionLevel) {
        this.name = name;
        this.type = type;
        this.requiredItems = new HashMap<>(requiredItems);
        this.result = result;
        this.requiredCollection = requiredCollection;
        this.requiredCollectionLevel = requiredCollectionLevel;
    }
    
    public String getName() {
        return name;
    }
    
    public RecipeType getType() {
        return type;
    }
    
    public Map<Material, Integer> getRequiredItems() {
        return new HashMap<>(requiredItems);
    }
    
    public org.bukkit.inventory.ItemStack getResult() {
        return result.clone();
    }
    
    public CollectionType getRequiredCollection() {
        return requiredCollection;
    }
    
    public int getRequiredCollectionLevel() {
        return requiredCollectionLevel;
    }
    
    public enum RecipeType {
        CRAFTING("Crafting"),
        SMELTING("Smelting"),
        BREWING("Brewing"),
        ENCHANTING("Enchanting"),
        SPECIAL("Special");
        
        private final String displayName;
        
        RecipeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
