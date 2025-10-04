package de.noctivag.plugin.features.enchantments;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.enchantments.types.CompleteEnchantmentType;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player's enchantment collection
 */
public class PlayerEnchantments {
    
    private final Player player;
    private final Map<CompleteEnchantmentType, Integer> ownedEnchantments;
    private final Map<CompleteEnchantmentType, Integer> activeEnchantments;
    
    public PlayerEnchantments(Player player) {
        this.player = player;
        this.ownedEnchantments = new ConcurrentHashMap<>();
        this.activeEnchantments = new ConcurrentHashMap<>();
    }
    
    /**
     * Add enchantment to player's collection
     */
    public void addEnchantment(CompleteEnchantmentType enchantment, int level) {
        ownedEnchantments.put(enchantment, Math.max(ownedEnchantments.getOrDefault(enchantment, 0), level));
        activeEnchantments.put(enchantment, level);
    }
    
    /**
     * Remove enchantment from player's collection
     */
    public void removeEnchantment(CompleteEnchantmentType enchantment) {
        ownedEnchantments.remove(enchantment);
        activeEnchantments.remove(enchantment);
    }
    
    /**
     * Check if player owns enchantment
     */
    public boolean ownsEnchantment(CompleteEnchantmentType enchantment) {
        return ownedEnchantments.containsKey(enchantment);
    }
    
    /**
     * Check if enchantment is active
     */
    public boolean isEnchantmentActive(CompleteEnchantmentType enchantment) {
        return activeEnchantments.containsKey(enchantment);
    }
    
    /**
     * Set enchantment level
     */
    public void setEnchantmentLevel(CompleteEnchantmentType enchantment, int level) {
        if (ownsEnchantment(enchantment)) {
            activeEnchantments.put(enchantment, Math.max(1, level));
        }
    }
    
    /**
     * Get enchantment level
     */
    public int getEnchantmentLevel(CompleteEnchantmentType enchantment) {
        return activeEnchantments.getOrDefault(enchantment, 0);
    }
    
    /**
     * Get owned enchantment level
     */
    public int getOwnedEnchantmentLevel(CompleteEnchantmentType enchantment) {
        return ownedEnchantments.getOrDefault(enchantment, 0);
    }
    
    /**
     * Get all owned enchantments
     */
    public Map<CompleteEnchantmentType, Integer> getOwnedEnchantments() {
        return new HashMap<>(ownedEnchantments);
    }
    
    /**
     * Get all active enchantments
     */
    public Map<CompleteEnchantmentType, Integer> getActiveEnchantments() {
        return new HashMap<>(activeEnchantments);
    }
    
    /**
     * Get enchantments by rarity
     */
    public Map<CompleteEnchantmentType, Integer> getEnchantmentsByRarity(de.noctivag.plugin.features.enchantments.types.EnchantmentRarity rarity) {
        Map<CompleteEnchantmentType, Integer> result = new HashMap<>();
        
        for (Map.Entry<CompleteEnchantmentType, Integer> entry : ownedEnchantments.entrySet()) {
            if (entry.getKey().getRarity() == rarity) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        
        return result;
    }
    
    /**
     * Get enchantments by category
     */
    public Map<CompleteEnchantmentType, Integer> getEnchantmentsByCategory(de.noctivag.plugin.features.enchantments.types.EnchantmentCategory category) {
        Map<CompleteEnchantmentType, Integer> result = new HashMap<>();
        
        for (Map.Entry<CompleteEnchantmentType, Integer> entry : ownedEnchantments.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        
        return result;
    }
    
    /**
     * Get total enchantment cost
     */
    public int getTotalEnchantmentCost() {
        int totalCost = 0;
        
        for (Map.Entry<CompleteEnchantmentType, Integer> entry : activeEnchantments.entrySet()) {
            totalCost += CompleteEnchantmentType.calculateEnchantmentCost(entry.getKey(), entry.getValue());
        }
        
        return totalCost;
    }
    
    /**
     * Get total enchantment experience cost
     */
    public int getTotalEnchantmentExperienceCost() {
        int totalCost = 0;
        
        for (Map.Entry<CompleteEnchantmentType, Integer> entry : activeEnchantments.entrySet()) {
            totalCost += CompleteEnchantmentType.calculateEnchantmentExperienceCost(entry.getKey(), entry.getValue());
        }
        
        return totalCost;
    }
    
    /**
     * Get enchantment count by rarity
     */
    public Map<de.noctivag.plugin.features.enchantments.types.EnchantmentRarity, Integer> getEnchantmentCountByRarity() {
        Map<de.noctivag.plugin.features.enchantments.types.EnchantmentRarity, Integer> counts = new HashMap<>();
        
        for (CompleteEnchantmentType enchantment : ownedEnchantments.keySet()) {
            de.noctivag.plugin.features.enchantments.types.EnchantmentRarity rarity = enchantment.getRarity();
            counts.put(rarity, counts.getOrDefault(rarity, 0) + 1);
        }
        
        return counts;
    }
    
    /**
     * Get enchantment count by category
     */
    public Map<de.noctivag.plugin.features.enchantments.types.EnchantmentCategory, Integer> getEnchantmentCountByCategory() {
        Map<de.noctivag.plugin.features.enchantments.types.EnchantmentCategory, Integer> counts = new HashMap<>();
        
        for (CompleteEnchantmentType enchantment : ownedEnchantments.keySet()) {
            de.noctivag.plugin.features.enchantments.types.EnchantmentCategory category = enchantment.getCategory();
            counts.put(category, counts.getOrDefault(category, 0) + 1);
        }
        
        return counts;
    }
    
    /**
     * Get completion percentage for each rarity
     */
    public Map<de.noctivag.plugin.features.enchantments.types.EnchantmentRarity, Double> getCompletionPercentageByRarity() {
        Map<de.noctivag.plugin.features.enchantments.types.EnchantmentRarity, Double> percentages = new HashMap<>();
        
        for (de.noctivag.plugin.features.enchantments.types.EnchantmentRarity rarity : de.noctivag.plugin.features.enchantments.types.EnchantmentRarity.values()) {
            List<CompleteEnchantmentType> allEnchantmentsOfRarity = CompleteEnchantmentType.getEnchantmentsByRarity(rarity);
            Map<CompleteEnchantmentType, Integer> ownedEnchantmentsOfRarity = getEnchantmentsByRarity(rarity);
            
            if (!allEnchantmentsOfRarity.isEmpty()) {
                double percentage = (double) ownedEnchantmentsOfRarity.size() / allEnchantmentsOfRarity.size() * 100.0;
                percentages.put(rarity, percentage);
            }
        }
        
        return percentages;
    }
    
    /**
     * Get completion percentage for each category
     */
    public Map<de.noctivag.plugin.features.enchantments.types.EnchantmentCategory, Double> getCompletionPercentageByCategory() {
        Map<de.noctivag.plugin.features.enchantments.types.EnchantmentCategory, Double> percentages = new HashMap<>();
        
        for (de.noctivag.plugin.features.enchantments.types.EnchantmentCategory category : de.noctivag.plugin.features.enchantments.types.EnchantmentCategory.values()) {
            List<CompleteEnchantmentType> allEnchantmentsOfCategory = CompleteEnchantmentType.getEnchantmentsByCategory(category);
            Map<CompleteEnchantmentType, Integer> ownedEnchantmentsOfCategory = getEnchantmentsByCategory(category);
            
            if (!allEnchantmentsOfCategory.isEmpty()) {
                double percentage = (double) ownedEnchantmentsOfCategory.size() / allEnchantmentsOfCategory.size() * 100.0;
                percentages.put(category, percentage);
            }
        }
        
        return percentages;
    }
    
    /**
     * Get total completion percentage
     */
    public double getTotalCompletionPercentage() {
        int totalEnchantments = CompleteEnchantmentType.getTotalEnchantmentCount();
        int ownedEnchantments = this.ownedEnchantments.size();
        
        if (totalEnchantments == 0) return 0.0;
        
        return (double) ownedEnchantments / totalEnchantments * 100.0;
    }
    
    /**
     * Get player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get enchantment statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOwned", ownedEnchantments.size());
        stats.put("totalActive", activeEnchantments.size());
        stats.put("totalCost", getTotalEnchantmentCost());
        stats.put("totalExperienceCost", getTotalEnchantmentExperienceCost());
        stats.put("completionPercentage", getTotalCompletionPercentage());
        stats.put("enchantmentCountByRarity", getEnchantmentCountByRarity());
        stats.put("enchantmentCountByCategory", getEnchantmentCountByCategory());
        stats.put("completionPercentageByRarity", getCompletionPercentageByRarity());
        stats.put("completionPercentageByCategory", getCompletionPercentageByCategory());
        
        return stats;
    }
}
