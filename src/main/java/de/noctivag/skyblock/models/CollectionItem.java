package de.noctivag.skyblock.models;

import org.bukkit.Material;

/**
 * Repräsentiert ein Item in einer Collection
 */
public class CollectionItem {
    
    private final String name;
    private final Material material;
    private final int requiredAmount;
    private int collectedAmount;
    private String reward;
    
    public CollectionItem(String name, Material material, int requiredAmount) {
        this.name = name;
        this.material = material;
        this.requiredAmount = requiredAmount;
        this.collectedAmount = 0;
        this.reward = null;
    }
    
    public void addCollected(int amount) {
        this.collectedAmount += amount;
    }
    
    public boolean isUnlocked() {
        return collectedAmount >= requiredAmount;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public int getRequiredAmount() {
        return requiredAmount;
    }
    
    public int getCollectedAmount() {
        return collectedAmount;
    }
    
    public void setCollectedAmount(int collectedAmount) {
        this.collectedAmount = collectedAmount;
    }
    
    public String getReward() {
        return reward;
    }
    
    public void setReward(String reward) {
        this.reward = reward;
    }
}
