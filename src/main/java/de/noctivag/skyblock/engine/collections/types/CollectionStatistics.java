package de.noctivag.skyblock.engine.collections.types;

import de.noctivag.skyblock.engine.collections.HypixelPlayerCollections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Collection Statistics for a player
 * 
 * Provides comprehensive statistics and analysis of a player's collection progress.
 * Used for progress tracking and collection recommendations.
 */
public class CollectionStatistics {
    
    private final HypixelPlayerCollections playerCollections;
    private final Map<CollectionType, Double> collectionEfficiency;
    private final Map<CollectionType.CollectionCategory, Double> categoryProgress;
    
    public CollectionStatistics(HypixelPlayerCollections playerCollections) {
        this.playerCollections = playerCollections;
        this.collectionEfficiency = new HashMap<>();
        this.categoryProgress = new HashMap<>();
        
        calculateStatistics();
    }
    
    /**
     * Calculate all statistics
     */
    private void calculateStatistics() {
        // Calculate collection efficiency for each collection
        for (CollectionType collectionType : CollectionType.values()) {
            double efficiency = calculateCollectionEfficiency(collectionType);
            collectionEfficiency.put(collectionType, efficiency);
        }
        
        // Calculate category progress
        for (CollectionType.CollectionCategory category : CollectionType.CollectionCategory.values()) {
            double progress = calculateCategoryProgress(category);
            categoryProgress.put(category, progress);
        }
    }
    
    /**
     * Calculate collection efficiency for a specific collection
     */
    private double calculateCollectionEfficiency(CollectionType collectionType) {
        int progress = playerCollections.getTotalCollectionProgress(collectionType);
        long lastActivity = playerCollections.getLastActivity(collectionType);
        
        // Base efficiency from progress
        double progressEfficiency = Math.min(progress / 100000.0, 1.0); // Cap at 100K progress
        
        // Activity efficiency (recent activity = higher efficiency)
        long timeSinceActivity = java.lang.System.currentTimeMillis() - lastActivity;
        double activityEfficiency = Math.max(0.1, 1.0 - (timeSinceActivity / 86400000.0)); // Decay over 24 hours
        
        // Difficulty factor
        double difficultyFactor = collectionType.getDifficulty();
        
        return (progressEfficiency + activityEfficiency) / (2.0 * difficultyFactor);
    }
    
    /**
     * Calculate category progress
     */
    private double calculateCategoryProgress(CollectionType.CollectionCategory category) {
        CollectionType[] collections = CollectionType.getByCategory(category);
        if (collections.length == 0) return 0.0;
        
        double totalProgress = 0.0;
        for (CollectionType collection : collections) {
            int progress = playerCollections.getTotalCollectionProgress(collection);
            totalProgress += progress;
        }
        
        return totalProgress / collections.length;
    }
    
    /**
     * Get total collection progress
     */
    public int getTotalCollectionProgress() {
        return playerCollections.getTotalCollectionProgress();
    }
    
    /**
     * Get total unlocked milestones
     */
    public int getTotalUnlockedMilestones() {
        return playerCollections.getTotalUnlockedMilestones();
    }
    
    /**
     * Get overall collection rating
     */
    public int getOverallCollectionRating() {
        return playerCollections.getOverallCollectionRating();
    }
    
    /**
     * Get collection efficiency for a specific collection
     */
    public double getCollectionEfficiency(CollectionType collectionType) {
        return collectionEfficiency.getOrDefault(collectionType, 0.0);
    }
    
    /**
     * Get category progress
     */
    public double getCategoryProgress(CollectionType.CollectionCategory category) {
        return categoryProgress.getOrDefault(category, 0.0);
    }
    
    /**
     * Get most efficient collection
     */
    public CollectionType getMostEfficientCollection() {
        return collectionEfficiency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CollectionType.COBBLESTONE);
    }
    
    /**
     * Get least efficient collection
     */
    public CollectionType getLeastEfficientCollection() {
        return collectionEfficiency.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CollectionType.SPONGE);
    }
    
    /**
     * Get best category
     */
    public CollectionType.CollectionCategory getBestCategory() {
        return playerCollections.getBestCategory();
    }
    
    /**
     * Get worst category
     */
    public CollectionType.CollectionCategory getWorstCategory() {
        return playerCollections.getWorstCategory();
    }
    
    /**
     * Get collections sorted by efficiency
     */
    public List<Map.Entry<CollectionType, Double>> getCollectionsSortedByEfficiency() {
        return collectionEfficiency.entrySet().stream()
            .sorted(Map.Entry.<CollectionType, Double>comparingByValue().reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Get categories sorted by progress
     */
    public List<Map.Entry<CollectionType.CollectionCategory, Double>> getCategoriesSortedByProgress() {
        return categoryProgress.entrySet().stream()
            .sorted(Map.Entry.<CollectionType.CollectionCategory, Double>comparingByValue().reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Get number of active collections (with progress > 0)
     */
    public int getActiveCollections() {
        return (int) CollectionType.values().length - 
               (int) Arrays.stream(CollectionType.values())
                   .filter(collection -> playerCollections.getTotalCollectionProgress(collection) == 0)
                   .count();
    }
    
    /**
     * Get number of collections at specific progress level or higher
     */
    public int getCollectionsAtProgressOrHigher(int progress) {
        return (int) Arrays.stream(CollectionType.values())
            .filter(collection -> playerCollections.getTotalCollectionProgress(collection) >= progress)
            .count();
    }
    
    /**
     * Get collection distribution
     */
    public Map<String, Integer> getCollectionDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        
        for (CollectionType collection : CollectionType.values()) {
            int progress = playerCollections.getTotalCollectionProgress(collection);
            String range = getProgressRange(progress);
            distribution.put(range, distribution.getOrDefault(range, 0) + 1);
        }
        
        return distribution;
    }
    
    /**
     * Get progress range for a collection progress
     */
    private String getProgressRange(int progress) {
        if (progress == 0) return "0";
        if (progress < 100) return "1-99";
        if (progress < 1000) return "100-999";
        if (progress < 10000) return "1K-9.9K";
        if (progress < 100000) return "10K-99.9K";
        if (progress < 1000000) return "100K-999.9K";
        return "1M+";
    }
    
    /**
     * Get collection recommendations
     */
    public List<String> getCollectionRecommendations() {
        return playerCollections.getCollectionRecommendations();
    }
    
    /**
     * Get collection power (for GIM system)
     */
    public double getCollectionPower() {
        double totalPower = 0.0;
        
        for (CollectionType collection : CollectionType.values()) {
            int progress = playerCollections.getTotalCollectionProgress(collection);
            double collectionPower = progress * collection.getWeight();
            totalPower += collectionPower;
        }
        
        return totalPower;
    }
    
    /**
     * Get collection completion percentage
     */
    public double getCollectionCompletionPercentage() {
        int totalCollections = CollectionType.values().length;
        int activeCollections = getActiveCollections();
        
        return (double) activeCollections / totalCollections * 100;
    }
    
    /**
     * Get average collection progress
     */
    public double getAverageCollectionProgress() {
        return Arrays.stream(CollectionType.values())
            .mapToInt(playerCollections::getTotalCollectionProgress)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Get highest collection progress
     */
    public int getHighestCollectionProgress() {
        return Arrays.stream(CollectionType.values())
            .mapToInt(playerCollections::getTotalCollectionProgress)
            .max()
            .orElse(0);
    }
    
    /**
     * Get lowest collection progress
     */
    public int getLowestCollectionProgress() {
        return Arrays.stream(CollectionType.values())
            .mapToInt(playerCollections::getTotalCollectionProgress)
            .min()
            .orElse(0);
    }
    
    /**
     * Get collection with highest progress
     */
    public CollectionType getHighestProgressCollection() {
        return Arrays.stream(CollectionType.values())
            .max(Comparator.comparingInt(playerCollections::getTotalCollectionProgress))
            .orElse(CollectionType.COBBLESTONE);
    }
    
    /**
     * Get collection with lowest progress
     */
    public CollectionType getLowestProgressCollection() {
        return Arrays.stream(CollectionType.values())
            .min(Comparator.comparingInt(playerCollections::getTotalCollectionProgress))
            .orElse(CollectionType.SPONGE);
    }
    
    /**
     * Get formatted statistics summary
     */
    public String getFormattedSummary() {
        return String.format(
            "Collection Statistics Summary:\n" +
            "Total Progress: %,d items\n" +
            "Unlocked Milestones: %d\n" +
            "Active Collections: %d/%d\n" +
            "Overall Rating: %d/100\n" +
            "Collection Power: %.1f\n" +
            "Best Category: %s\n" +
            "Most Efficient: %s",
            getTotalCollectionProgress(),
            getTotalUnlockedMilestones(),
            getActiveCollections(),
            CollectionType.values().length,
            getOverallCollectionRating(),
            getCollectionPower(),
            getBestCategory().getDisplayName(),
            getMostEfficientCollection().getDisplayName()
        );
    }
}
