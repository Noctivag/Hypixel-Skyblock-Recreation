package de.noctivag.skyblock.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a player's collection data and progress
 */
public class PlayerCollections {
    private final UUID playerId;
    private final Map<CollectionType, Long> collectionAmounts;
    private final Map<CollectionType, Set<Integer>> unlockedMilestones;
    private final Map<CollectionType, Long> lastUpdated;
    
    public PlayerCollections(UUID playerId) {
        this.playerId = playerId;
        this.collectionAmounts = new HashMap<>();
        this.unlockedMilestones = new HashMap<>();
        this.lastUpdated = new HashMap<>();
        
        // Initialize all collections with 0 amount
        for (CollectionType collection : CollectionType.values()) {
            collectionAmounts.put(collection, 0L);
            unlockedMilestones.put(collection, new HashSet<>());
            lastUpdated.put(collection, System.currentTimeMillis());
        }
    }

    public PlayerCollections(UUID playerId, Map<CollectionType, Long> collectionAmounts, 
                           Map<CollectionType, Set<Integer>> unlockedMilestones) {
        this.playerId = playerId;
        this.collectionAmounts = new HashMap<>(collectionAmounts);
        this.unlockedMilestones = new HashMap<>();
        this.lastUpdated = new HashMap<>();
        
        // Initialize unlockedMilestones and lastUpdated
        for (CollectionType collection : CollectionType.values()) {
            this.unlockedMilestones.put(collection, new HashSet<>(
                unlockedMilestones.getOrDefault(collection, new HashSet<>())
            ));
            this.lastUpdated.put(collection, System.currentTimeMillis());
        }
        
        // Recalculate unlocked milestones based on current amounts
        recalculateUnlockedMilestones();
    }

    /**
     * Add amount to a specific collection
     */
    public boolean addCollection(CollectionType collection, long amount) {
        if (amount <= 0) return false;
        
        long currentAmount = collectionAmounts.getOrDefault(collection, 0L);
        long newAmount = currentAmount + amount;
        
        collectionAmounts.put(collection, newAmount);
        lastUpdated.put(collection, System.currentTimeMillis());
        
        // Check for new milestones
        return checkAndUnlockMilestones(collection, newAmount);
    }

    /**
     * Set amount for a specific collection
     */
    public void setCollection(CollectionType collection, long amount) {
        collectionAmounts.put(collection, Math.max(0, amount));
        lastUpdated.put(collection, System.currentTimeMillis());
        recalculateUnlockedMilestones();
    }

    /**
     * Get amount for a specific collection
     */
    public long getCollection(CollectionType collection) {
        return collectionAmounts.getOrDefault(collection, 0L);
    }

    /**
     * Get unlocked milestones for a specific collection
     */
    public Set<Integer> getUnlockedMilestones(CollectionType collection) {
        return unlockedMilestones.getOrDefault(collection, new HashSet<>());
    }

    /**
     * Check if a specific milestone is unlocked
     */
    public boolean isMilestoneUnlocked(CollectionType collection, int milestoneLevel) {
        return getUnlockedMilestones(collection).contains(milestoneLevel);
    }

    /**
     * Get current milestone level for a collection
     */
    public int getCurrentMilestoneLevel(CollectionType collection) {
        long amount = getCollection(collection);
        return collection.getCurrentMilestoneLevel(amount);
    }

    /**
     * Get next milestone requirement
     */
    public long getNextMilestoneRequirement(CollectionType collection) {
        long amount = getCollection(collection);
        return collection.getNextMilestoneRequirement(amount);
    }

    /**
     * Get progress to next milestone
     */
    public long getProgressToNextMilestone(CollectionType collection) {
        long amount = getCollection(collection);
        return collection.getProgressToNextMilestone(amount);
    }

    /**
     * Get total progress needed for next milestone
     */
    public long getTotalProgressNeededForNextMilestone(CollectionType collection) {
        long amount = getCollection(collection);
        return collection.getTotalProgressNeededForNextMilestone(amount);
    }

    /**
     * Get total collections across all types
     */
    public long getTotalCollections() {
        return collectionAmounts.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Get total unlocked milestones across all collections
     */
    public int getTotalUnlockedMilestones() {
        return unlockedMilestones.values().stream().mapToInt(Set::size).sum();
    }

    /**
     * Get collections by category
     */
    public Map<CollectionType, Long> getCollectionsByCategory(String category) {
        Map<CollectionType, Long> categoryCollections = new HashMap<>();
        for (Map.Entry<CollectionType, Long> entry : collectionAmounts.entrySet()) {
            if (entry.getKey().getCategory().equalsIgnoreCase(category)) {
                categoryCollections.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryCollections;
    }

    /**
     * Get total collections by category
     */
    public long getTotalCollectionsByCategory(String category) {
        return getCollectionsByCategory(category).values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Get the highest collection amount
     */
    public CollectionType getHighestCollection() {
        CollectionType highest = CollectionType.COBBLESTONE;
        long highestAmount = 0;
        
        for (Map.Entry<CollectionType, Long> entry : collectionAmounts.entrySet()) {
            if (entry.getValue() > highestAmount) {
                highestAmount = entry.getValue();
                highest = entry.getKey();
            }
        }
        
        return highest;
    }

    /**
     * Get the lowest collection amount
     */
    public CollectionType getLowestCollection() {
        CollectionType lowest = CollectionType.COBBLESTONE;
        long lowestAmount = Long.MAX_VALUE;
        
        for (Map.Entry<CollectionType, Long> entry : collectionAmounts.entrySet()) {
            if (entry.getValue() < lowestAmount) {
                lowestAmount = entry.getValue();
                lowest = entry.getKey();
            }
        }
        
        return lowest;
    }

    /**
     * Get collections that have reached a specific milestone level
     */
    public Map<CollectionType, Integer> getCollectionsAtMilestoneLevel(int milestoneLevel) {
        Map<CollectionType, Integer> collectionsAtLevel = new HashMap<>();
        
        for (CollectionType collection : CollectionType.values()) {
            if (isMilestoneUnlocked(collection, milestoneLevel)) {
                collectionsAtLevel.put(collection, milestoneLevel);
            }
        }
        
        return collectionsAtLevel;
    }

    /**
     * Get collection progress percentage
     */
    public double getCollectionProgressPercentage(CollectionType collection) {
        long currentAmount = getCollection(collection);
        long nextMilestone = getNextMilestoneRequirement(collection);
        
        if (nextMilestone == -1) return 100.0; // Max milestone reached
        
        long[] milestones = collection.getMilestoneRequirements();
        long previousMilestone = 0;
        for (long milestone : milestones) {
            if (milestone == nextMilestone) {
                break;
            }
            previousMilestone = milestone;
        }
        
        if (nextMilestone == previousMilestone) return 100.0;
        
        return (double) (currentAmount - previousMilestone) / (nextMilestone - previousMilestone) * 100.0;
    }

    /**
     * Check and unlock new milestones for a collection
     */
    private boolean checkAndUnlockMilestones(CollectionType collection, long newAmount) {
        boolean newMilestoneUnlocked = false;
        Set<Integer> unlocked = unlockedMilestones.get(collection);
        
        long[] milestones = collection.getMilestoneRequirements();
        for (int i = 0; i < milestones.length; i++) {
            int milestoneLevel = i + 1;
            if (newAmount >= milestones[i] && !unlocked.contains(milestoneLevel)) {
                unlocked.add(milestoneLevel);
                newMilestoneUnlocked = true;
            }
        }
        
        return newMilestoneUnlocked;
    }

    /**
     * Recalculate all unlocked milestones based on current amounts
     */
    private void recalculateUnlockedMilestones() {
        for (CollectionType collection : CollectionType.values()) {
            long amount = getCollection(collection);
            Set<Integer> unlocked = unlockedMilestones.get(collection);
            unlocked.clear();
            
            long[] milestones = collection.getMilestoneRequirements();
            for (int i = 0; i < milestones.length; i++) {
                int milestoneLevel = i + 1;
                if (amount >= milestones[i]) {
                    unlocked.add(milestoneLevel);
                }
            }
        }
    }

    /**
     * Get all collection data as a map
     */
    public Map<CollectionType, Long> getAllCollections() {
        return new HashMap<>(collectionAmounts);
    }

    /**
     * Get all unlocked milestones as a map
     */
    public Map<CollectionType, Set<Integer>> getAllUnlockedMilestones() {
        Map<CollectionType, Set<Integer>> result = new HashMap<>();
        for (Map.Entry<CollectionType, Set<Integer>> entry : unlockedMilestones.entrySet()) {
            result.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return result;
    }

    /**
     * Get last updated timestamp for a collection
     */
    public long getLastUpdated(CollectionType collection) {
        return lastUpdated.getOrDefault(collection, 0L);
    }

    /**
     * Get collection amount for a specific collection type
     */
    public long getCollectionAmount(CollectionType collection) {
        return collectionAmounts.getOrDefault(collection, 0L);
    }
    
    /**
     * Set collection amount for a specific collection type
     */
    public void setCollectionAmount(CollectionType collection, long amount) {
        collectionAmounts.put(collection, amount);
        lastUpdated.put(collection, System.currentTimeMillis());
    }
    
    /**
     * Check milestone progression for a collection
     */
    public boolean checkMilestoneProgression(CollectionType collection) {
        long currentAmount = getCollectionAmount(collection);
        long[] milestones = collection.getMilestoneRequirements();
        
        // Find the highest milestone reached
        int newMilestoneLevel = -1;
        for (int i = 0; i < milestones.length; i++) {
            if (currentAmount >= milestones[i]) {
                newMilestoneLevel = i;
            } else {
                break;
            }
        }
        
        // Check if we reached a new milestone
        Set<Integer> unlocked = unlockedMilestones.get(collection);
        int currentMilestoneLevel = unlocked.size() - 1;
        if (newMilestoneLevel > currentMilestoneLevel) {
            // Unlock new milestones
            for (int i = currentMilestoneLevel + 1; i <= newMilestoneLevel; i++) {
                unlocked.add(i + 1); // Milestone levels are 1-based
            }
            return true; // New milestone reached
        }
        
        return false; // No new milestone
    }

    // Getters
    public UUID getPlayerId() {
        return playerId;
    }
}