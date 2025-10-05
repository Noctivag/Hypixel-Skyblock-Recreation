package de.noctivag.skyblock.engine.collections;
import java.util.UUID;

import de.noctivag.skyblock.engine.collections.types.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hypixel Player Collections
 * 
 * Manages collection progress for a single player across all collection types.
 * Tracks individual item progress and milestone achievements.
 */
public class HypixelPlayerCollections {
    
    private final UUID playerId;
    private final Map<CollectionType, Map<CollectionItem, Integer>> collectionProgress;
    private final Map<CollectionType, Set<CollectionMilestone>> unlockedMilestones;
    private final Map<CollectionType, Long> lastActivity;
    
    public HypixelPlayerCollections(UUID playerId) {
        this.playerId = playerId;
        this.collectionProgress = new ConcurrentHashMap<>();
        this.unlockedMilestones = new ConcurrentHashMap<>();
        this.lastActivity = new ConcurrentHashMap<>();
        
        initializeCollections();
    }
    
    /**
     * Initialize all collections for the player
     */
    private void initializeCollections() {
        for (CollectionType collectionType : CollectionType.values()) {
            collectionProgress.put(collectionType, new ConcurrentHashMap<>());
            unlockedMilestones.put(collectionType, ConcurrentHashMap.newKeySet());
            lastActivity.put(collectionType, java.lang.System.currentTimeMillis());
        }
    }
    
    /**
     * Add items to a collection
     */
    public boolean addToCollection(CollectionType collectionType, CollectionItem item, int amount) {
        if (amount <= 0) return false;
        
        Map<CollectionItem, Integer> progress = collectionProgress.get(collectionType);
        if (progress == null) {
            progress = new ConcurrentHashMap<>();
            collectionProgress.put(collectionType, progress);
        }
        
        // Add to progress
        int currentAmount = progress.getOrDefault(item, 0);
        int newAmount = currentAmount + amount;
        
        // Check if exceeding maximum collection
        if (item.exceedsMaxCollection(newAmount)) {
            newAmount = item.getMaxCollection();
        }
        
        progress.put(item, newAmount);
        lastActivity.put(collectionType, java.lang.System.currentTimeMillis());
        
        // Check for milestone unlocks
        checkMilestoneUnlocks(collectionType);
        
        return true;
    }
    
    /**
     * Get collection progress for a specific item
     */
    public int getCollectionProgress(CollectionType collectionType, CollectionItem item) {
        Map<CollectionItem, Integer> progress = collectionProgress.get(collectionType);
        if (progress == null) return 0;
        
        return progress.getOrDefault(item, 0);
    }
    
    /**
     * Get total collection progress for a collection type
     */
    public int getTotalCollectionProgress(CollectionType collectionType) {
        Map<CollectionItem, Integer> progress = collectionProgress.get(collectionType);
        if (progress == null) return 0;
        
        return progress.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    /**
     * Get collection level based on total progress
     */
    public int getCollectionLevel(CollectionType collectionType) {
        int totalProgress = getTotalCollectionProgress(collectionType);
        
        // Simple level calculation based on total progress
        if (totalProgress >= 1000000) return 10; // 1M+ = Level 10
        if (totalProgress >= 500000) return 9;   // 500K+ = Level 9
        if (totalProgress >= 250000) return 8;   // 250K+ = Level 8
        if (totalProgress >= 100000) return 7;   // 100K+ = Level 7
        if (totalProgress >= 50000) return 6;    // 50K+ = Level 6
        if (totalProgress >= 25000) return 5;    // 25K+ = Level 5
        if (totalProgress >= 10000) return 4;    // 10K+ = Level 4
        if (totalProgress >= 5000) return 3;     // 5K+ = Level 3
        if (totalProgress >= 1000) return 2;     // 1K+ = Level 2
        if (totalProgress >= 100) return 1;      // 100+ = Level 1
        return 0; // Less than 100 = Level 0
    }
    
    /**
     * Get unlocked milestones for a collection type
     */
    public List<CollectionMilestone> getUnlockedMilestones(CollectionType collectionType) {
        Set<CollectionMilestone> milestones = unlockedMilestones.get(collectionType);
        if (milestones == null) return new ArrayList<>();
        
        return new ArrayList<>(milestones);
    }
    
    /**
     * Get next milestone for a collection type
     */
    public CollectionMilestone getNextMilestone(CollectionType collectionType) {
        int totalProgress = getTotalCollectionProgress(collectionType);
        Set<CollectionMilestone> unlocked = unlockedMilestones.get(collectionType);
        
        // Standard milestone amounts
        int[] milestoneAmounts = {50, 100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000};
        
        for (int amount : milestoneAmounts) {
            if (totalProgress < amount) {
                // Create a temporary milestone to check if it's unlocked
                CollectionMilestone milestone = new CollectionMilestone(amount, new CollectionReward[0]);
                if (unlocked == null || !unlocked.contains(milestone)) {
                    return milestone;
                }
            }
        }
        
        return null; // All milestones unlocked
    }
    
    /**
     * Check for milestone unlocks
     */
    private void checkMilestoneUnlocks(CollectionType collectionType) {
        int totalProgress = getTotalCollectionProgress(collectionType);
        Set<CollectionMilestone> unlocked = unlockedMilestones.get(collectionType);
        
        if (unlocked == null) {
            unlocked = ConcurrentHashMap.newKeySet();
            unlockedMilestones.put(collectionType, unlocked);
        }
        
        // Standard milestone amounts
        int[] milestoneAmounts = {50, 100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000};
        
        for (int amount : milestoneAmounts) {
            if (totalProgress >= amount) {
                // Create a temporary milestone to check if it's unlocked
                CollectionMilestone milestone = new CollectionMilestone(amount, new CollectionReward[0]);
                if (!unlocked.contains(milestone)) {
                    unlocked.add(milestone);
                    // TODO: Award milestone rewards
                }
            }
        }
    }
    
    /**
     * Get last activity time for a collection type
     */
    public long getLastActivity(CollectionType collectionType) {
        return lastActivity.getOrDefault(collectionType, 0L);
    }
    
    /**
     * Get all collection progress
     */
    public Map<CollectionType, Map<CollectionItem, Integer>> getAllCollectionProgress() {
        return new ConcurrentHashMap<>(collectionProgress);
    }
    
    /**
     * Get collection progress for a specific collection type
     */
    public Map<CollectionItem, Integer> getCollectionProgress(CollectionType collectionType) {
        return new ConcurrentHashMap<>(collectionProgress.getOrDefault(collectionType, new ConcurrentHashMap<>()));
    }
    
    /**
     * Get total collection progress across all collections
     */
    public int getTotalCollectionProgress() {
        return collectionProgress.values().stream()
            .flatMap(progress -> progress.values().stream())
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    /**
     * Get total unlocked milestones
     */
    public int getTotalUnlockedMilestones() {
        return unlockedMilestones.values().stream()
            .mapToInt(Set::size)
            .sum();
    }
    
    /**
     * Get collection statistics
     */
    public CollectionStatistics getCollectionStatistics() {
        return new CollectionStatistics(playerId);
    }
    
    /**
     * Get player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Get collections sorted by progress (highest first)
     */
    public List<Map.Entry<CollectionType, Integer>> getCollectionsSortedByProgress() {
        return collectionProgress.entrySet().stream()
            .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), 
                entry.getValue().values().stream().mapToInt(Integer::intValue).sum()))
            .sorted(Map.Entry.<CollectionType, Integer>comparingByValue().reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Get collections by category
     */
    public Map<CollectionType, Integer> getCollectionsByCategory(CollectionType.CollectionCategory category) {
        Map<CollectionType, Integer> categoryCollections = new HashMap<>();
        
        for (Map.Entry<CollectionType, Map<CollectionItem, Integer>> entry : collectionProgress.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                int totalProgress = entry.getValue().values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();
                categoryCollections.put(entry.getKey(), totalProgress);
            }
        }
        
        return categoryCollections;
    }
    
    /**
     * Get best collection category
     */
    public CollectionType.CollectionCategory getBestCategory() {
        Map<CollectionType.CollectionCategory, Integer> categoryTotals = new HashMap<>();
        
        for (CollectionType.CollectionCategory category : CollectionType.CollectionCategory.values()) {
            int total = getCollectionsByCategory(category).values().stream()
                .mapToInt(Integer::intValue)
                .sum();
            categoryTotals.put(category, total);
        }
        
        return categoryTotals.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CollectionType.CollectionCategory.MINING);
    }
    
    /**
     * Get worst collection category
     */
    public CollectionType.CollectionCategory getWorstCategory() {
        Map<CollectionType.CollectionCategory, Integer> categoryTotals = new HashMap<>();
        
        for (CollectionType.CollectionCategory category : CollectionType.CollectionCategory.values()) {
            int total = getCollectionsByCategory(category).values().stream()
                .mapToInt(Integer::intValue)
                .sum();
            categoryTotals.put(category, total);
        }
        
        return categoryTotals.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CollectionType.CollectionCategory.FISHING);
    }
    
    /**
     * Get collection efficiency (progress per hour)
     */
    public double getCollectionEfficiency(CollectionType collectionType) {
        long lastActivity = getLastActivity(collectionType);
        if (lastActivity == 0) return 0.0;
        
        long timeSinceActivity = java.lang.System.currentTimeMillis() - lastActivity;
        int totalProgress = getTotalCollectionProgress(collectionType);
        
        // Calculate efficiency based on recent activity
        double hoursSinceActivity = timeSinceActivity / (1000.0 * 60 * 60);
        if (hoursSinceActivity > 24) return 0.0; // No activity in 24+ hours
        
        return totalProgress / Math.max(hoursSinceActivity, 1.0);
    }
    
    /**
     * Get overall collection rating (0-100)
     */
    public int getOverallCollectionRating() {
        int totalCollections = CollectionType.values().length;
        int activeCollections = 0;
        
        for (CollectionType collectionType : CollectionType.values()) {
            if (getTotalCollectionProgress(collectionType) > 0) {
                activeCollections++;
            }
        }
        
        return (int) Math.round((double) activeCollections / totalCollections * 100);
    }
    
    /**
     * Get collection recommendations
     */
    public List<String> getCollectionRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        // Find collections that need attention
        for (CollectionType collectionType : CollectionType.values()) {
            int progress = getTotalCollectionProgress(collectionType);
            if (progress < 100) {
                recommendations.add("Focus on " + collectionType.getDisplayName() + 
                    " - progress is low (" + progress + " items)");
            }
        }
        
        // Find categories that need attention
        CollectionType.CollectionCategory worstCategory = getWorstCategory();
        int worstCategoryTotal = getCollectionsByCategory(worstCategory).values().stream()
            .mapToInt(Integer::intValue)
            .sum();
        
        if (worstCategoryTotal < 1000) {
            recommendations.add("Improve " + worstCategory.getDisplayName() + 
                " category collections");
        }
        
        return recommendations;
    }
}
