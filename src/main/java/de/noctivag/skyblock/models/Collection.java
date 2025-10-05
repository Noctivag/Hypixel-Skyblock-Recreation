package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.CollectionType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert eine Collection mit Items und Fortschritt
 */
public class Collection {
    
    private final CollectionType collectionType;
    private final List<CollectionItem> items;
    private int level;
    private int experience;
    private final List<String> unlockedRecipes;
    
    public Collection(CollectionType collectionType) {
        this.collectionType = collectionType;
        this.items = new ArrayList<>();
        this.level = 1;
        this.experience = 0;
        this.unlockedRecipes = new ArrayList<>();
        
        // Initialize with default items for this collection type
        initializeDefaultItems();
    }
    
    private void initializeDefaultItems() {
        switch (collectionType) {
            case FARMING:
                items.add(new CollectionItem("Wheat", Material.WHEAT, 100));
                items.add(new CollectionItem("Carrot", Material.CARROT, 100));
                items.add(new CollectionItem("Potato", Material.POTATO, 100));
                items.add(new CollectionItem("Pumpkin", Material.PUMPKIN, 100));
                items.add(new CollectionItem("Melon", Material.MELON, 100));
                break;
            case MINING:
                items.add(new CollectionItem("Coal", Material.COAL, 100));
                items.add(new CollectionItem("Iron Ingot", Material.IRON_INGOT, 100));
                items.add(new CollectionItem("Gold Ingot", Material.GOLD_INGOT, 100));
                items.add(new CollectionItem("Diamond", Material.DIAMOND, 100));
                items.add(new CollectionItem("Emerald", Material.EMERALD, 100));
                break;
            case FORAGING:
                items.add(new CollectionItem("Oak Log", Material.OAK_LOG, 100));
                items.add(new CollectionItem("Birch Log", Material.BIRCH_LOG, 100));
                items.add(new CollectionItem("Spruce Log", Material.SPRUCE_LOG, 100));
                items.add(new CollectionItem("Jungle Log", Material.JUNGLE_LOG, 100));
                items.add(new CollectionItem("Acacia Log", Material.ACACIA_LOG, 100));
                break;
            case COMBAT:
                items.add(new CollectionItem("Bone", Material.BONE, 100));
                items.add(new CollectionItem("Rotten Flesh", Material.ROTTEN_FLESH, 100));
                items.add(new CollectionItem("String", Material.STRING, 100));
                items.add(new CollectionItem("Spider Eye", Material.SPIDER_EYE, 100));
                items.add(new CollectionItem("Gunpowder", Material.GUNPOWDER, 100));
                break;
            default:
                // Add basic items for other collection types
                break;
        }
    }
    
    public void addItem(String name, Material material, int amount) {
        CollectionItem existingItem = getItemByName(name);
        if (existingItem != null) {
            existingItem.addCollected(amount);
        } else {
            items.add(new CollectionItem(name, material, amount));
        }
        
        // Check for level up
        checkLevelUp();
    }
    
    public CollectionItem getItemByName(String name) {
        return items.stream()
            .filter(item -> item.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
    
    private void checkLevelUp() {
        int totalCollected = getTotalCollected();
        int requiredForNextLevel = getRequiredForNextLevel();
        
        if (totalCollected >= requiredForNextLevel) {
            level++;
            experience += totalCollected - (requiredForNextLevel - getRequiredForCurrentLevel());
            
            // Unlock recipes for this level
            unlockRecipesForLevel(level);
        }
    }
    
    private int getRequiredForCurrentLevel() {
        return (level - 1) * 1000; // 1000 items per level
    }
    
    private int getRequiredForNextLevel() {
        return level * 1000;
    }
    
    private void unlockRecipesForLevel(int level) {
        // Add some example recipes based on level
        if (level >= 2) {
            unlockedRecipes.add("Basic " + collectionType.getDisplayName() + " Recipe");
        }
        if (level >= 5) {
            unlockedRecipes.add("Advanced " + collectionType.getDisplayName() + " Recipe");
        }
        if (level >= 10) {
            unlockedRecipes.add("Master " + collectionType.getDisplayName() + " Recipe");
        }
    }
    
    public int getTotalCollected() {
        return items.stream()
            .mapToInt(CollectionItem::getCollectedAmount)
            .sum();
    }
    
    public int getTotalItems() {
        return items.size() * 100; // Assuming 100 items per type for simplicity
    }
    
    public CollectionType getCollectionType() {
        return collectionType;
    }
    
    public List<CollectionItem> getItems() {
        return new ArrayList<>(items);
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public List<String> getUnlockedRecipes() {
        return new ArrayList<>(unlockedRecipes);
    }
}
