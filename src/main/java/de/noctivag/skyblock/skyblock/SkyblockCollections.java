package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Skyblock Collections - Player's collection data
 */
public class SkyblockCollections {
    
    private final UUID playerId;
    private Map<String, Integer> collections;
    private Map<String, Boolean> rewards;
    
    public SkyblockCollections(UUID playerId) {
        this.playerId = playerId;
        this.collections = new HashMap<>();
        this.rewards = new HashMap<>();
        
        // Initialize default collections
        initializeDefaultCollections();
    }
    
    /**
     * Initialize default collections
     */
    private void initializeDefaultCollections() {
        String[] items = {
            "cobblestone", "coal", "iron_ingot", "gold_ingot", "diamond",
            "oak_log", "birch_log", "spruce_log", "jungle_log", "acacia_log",
            "wheat", "carrot", "potato", "pumpkin", "melon",
            "rotten_flesh", "bone", "string", "spider_eye", "gunpowder"
        };
        
        for (String item : items) {
            collections.put(item, 0);
            rewards.put(item, false);
        }
    }
    
    // Getters and setters
    public UUID getPlayerId() { return playerId; }
    
    public Map<String, Integer> getCollections() { return collections; }
    public void setCollections(Map<String, Integer> collections) { this.collections = collections; }
    
    public Map<String, Boolean> getRewards() { return rewards; }
    public void setRewards(Map<String, Boolean> rewards) { this.rewards = rewards; }
    
    /**
     * Get collection count
     */
    public int getCollectionCount(String itemName) {
        return collections.getOrDefault(itemName, 0);
    }
    
    /**
     * Set collection count
     */
    public void setCollectionCount(String itemName, int count) {
        collections.put(itemName, count);
    }
    
    /**
     * Add to collection
     */
    public void addToCollection(String itemName, int amount) {
        int currentCount = getCollectionCount(itemName);
        setCollectionCount(itemName, currentCount + amount);
    }
    
    /**
     * Check if reward is claimed
     */
    public boolean isRewardClaimed(String itemName) {
        return rewards.getOrDefault(itemName, false);
    }
    
    /**
     * Set reward claimed
     */
    public void setRewardClaimed(String itemName, boolean claimed) {
        rewards.put(itemName, claimed);
    }
    
    // Compatibility methods for SkyblockManager
    public void addCollection(org.bukkit.Material material, int amount) {
        addToCollection(material.name().toLowerCase(), amount);
    }
    
    public int getCollection(org.bukkit.Material material) {
        return getCollectionCount(material.name().toLowerCase());
    }
    
    public boolean hasMilestone(org.bukkit.Material material, int milestone) {
        String key = material.name().toLowerCase() + "_" + milestone;
        return isRewardClaimed(key);
    }
    
    public void addMilestone(org.bukkit.Material material, int milestone) {
        String key = material.name().toLowerCase() + "_" + milestone;
        setRewardClaimed(key, true);
    }
    
    public void save() {
        // Placeholder save method
    }
}