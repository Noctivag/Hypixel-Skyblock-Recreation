package de.noctivag.skyblock.features.accessories;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.accessories.types.CompleteAccessoryType;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player's accessory collection
 */
public class PlayerAccessories {
    
    private final Player player;
    private final Set<CompleteAccessoryType> ownedAccessories;
    private final Map<CompleteAccessoryType, Boolean> activeAccessories;
    private final Map<CompleteAccessoryType, Integer> accessoryLevels;
    
    public PlayerAccessories(Player player) {
        this.player = player;
        this.ownedAccessories = ConcurrentHashMap.newKeySet();
        this.activeAccessories = new ConcurrentHashMap<>();
        this.accessoryLevels = new ConcurrentHashMap<>();
    }
    
    /**
     * Add accessory to player's collection
     */
    public void addAccessory(CompleteAccessoryType accessoryType) {
        ownedAccessories.add(accessoryType);
        activeAccessories.put(accessoryType, true);
        accessoryLevels.put(accessoryType, 1);
    }
    
    /**
     * Remove accessory from player's collection
     */
    public void removeAccessory(CompleteAccessoryType accessoryType) {
        ownedAccessories.remove(accessoryType);
        activeAccessories.remove(accessoryType);
        accessoryLevels.remove(accessoryType);
    }
    
    /**
     * Check if player owns accessory
     */
    public boolean ownsAccessory(CompleteAccessoryType accessoryType) {
        return ownedAccessories.contains(accessoryType);
    }
    
    /**
     * Check if accessory is active
     */
    public boolean isAccessoryActive(CompleteAccessoryType accessoryType) {
        return activeAccessories.getOrDefault(accessoryType, false);
    }
    
    /**
     * Toggle accessory active state
     */
    public void toggleAccessory(CompleteAccessoryType accessoryType) {
        if (ownsAccessory(accessoryType)) {
            boolean currentState = isAccessoryActive(accessoryType);
            activeAccessories.put(accessoryType, !currentState);
        }
    }
    
    /**
     * Set accessory level
     */
    public void setAccessoryLevel(CompleteAccessoryType accessoryType, int level) {
        if (ownsAccessory(accessoryType)) {
            accessoryLevels.put(accessoryType, Math.max(1, level));
        }
    }
    
    /**
     * Get accessory level
     */
    public int getAccessoryLevel(CompleteAccessoryType accessoryType) {
        return accessoryLevels.getOrDefault(accessoryType, 1);
    }
    
    /**
     * Get all owned accessories
     */
    public Set<CompleteAccessoryType> getOwnedAccessories() {
        return new HashSet<>(ownedAccessories);
    }
    
    /**
     * Get all active accessories
     */
    public Set<CompleteAccessoryType> getActiveAccessories() {
        return activeAccessories.entrySet().stream()
            .filter(Map.Entry::getValue)
            .map(Map.Entry::getKey)
            .collect(HashSet::new, HashSet::add, HashSet::addAll);
    }
    
    /**
     * Get accessories by rarity
     */
    public Set<CompleteAccessoryType> getAccessoriesByRarity(de.noctivag.skyblock.features.accessories.types.AccessoryRarity rarity) {
        return ownedAccessories.stream()
            .filter(accessory -> accessory.getRarity() == rarity)
            .collect(HashSet::new, HashSet::add, HashSet::addAll);
    }
    
    /**
     * Get total magical power
     */
    public int getTotalMagicalPower() {
        return getActiveAccessories().stream()
            .mapToInt(CompleteAccessoryType::getMagicalPowerValue)
            .sum();
    }
    
    /**
     * Get accessory count by rarity
     */
    public Map<de.noctivag.skyblock.features.accessories.types.AccessoryRarity, Integer> getAccessoryCountByRarity() {
        Map<de.noctivag.skyblock.features.accessories.types.AccessoryRarity, Integer> counts = new HashMap<>();
        
        for (CompleteAccessoryType accessory : ownedAccessories) {
            de.noctivag.skyblock.features.accessories.types.AccessoryRarity rarity = accessory.getRarity();
            counts.put(rarity, counts.getOrDefault(rarity, 0) + 1);
        }
        
        return counts;
    }
    
    /**
     * Get completion percentage for each rarity
     */
    public Map<de.noctivag.skyblock.features.accessories.types.AccessoryRarity, Double> getCompletionPercentageByRarity() {
        Map<de.noctivag.skyblock.features.accessories.types.AccessoryRarity, Double> percentages = new HashMap<>();
        
        for (de.noctivag.skyblock.features.accessories.types.AccessoryRarity rarity : de.noctivag.skyblock.features.accessories.types.AccessoryRarity.values()) {
            List<CompleteAccessoryType> allAccessoriesOfRarity = CompleteAccessoryType.getAccessoriesByRarity(rarity);
            Set<CompleteAccessoryType> ownedAccessoriesOfRarity = getAccessoriesByRarity(rarity);
            
            if (!allAccessoriesOfRarity.isEmpty()) {
                double percentage = (double) ownedAccessoriesOfRarity.size() / allAccessoriesOfRarity.size() * 100.0;
                percentages.put(rarity, percentage);
            }
        }
        
        return percentages;
    }
    
    /**
     * Get total completion percentage
     */
    public double getTotalCompletionPercentage() {
        int totalAccessories = CompleteAccessoryType.getTotalAccessoryCount();
        int ownedAccessories = this.ownedAccessories.size();
        
        if (totalAccessories == 0) return 0.0;
        
        return (double) ownedAccessories / totalAccessories * 100.0;
    }
    
    /**
     * Get player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get accessory statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOwned", ownedAccessories.size());
        stats.put("totalActive", getActiveAccessories().size());
        stats.put("totalMagicalPower", getTotalMagicalPower());
        stats.put("completionPercentage", getTotalCompletionPercentage());
        stats.put("accessoryCountByRarity", getAccessoryCountByRarity());
        stats.put("completionPercentageByRarity", getCompletionPercentageByRarity());
        
        return stats;
    }
}
