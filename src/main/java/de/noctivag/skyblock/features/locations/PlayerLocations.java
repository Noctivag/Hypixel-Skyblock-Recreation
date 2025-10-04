package de.noctivag.skyblock.features.locations;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.locations.types.CompleteLocationType;
import de.noctivag.skyblock.features.locations.types.LocationCategory;
import de.noctivag.skyblock.features.locations.types.LocationRarity;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLocations {
    private final UUID playerId;
    private final Map<CompleteLocationType, Boolean> discoveredLocations;
    private final Map<CompleteLocationType, LocalDateTime> firstVisitTime;
    private final Map<CompleteLocationType, LocalDateTime> lastVisitTime;
    private final Map<CompleteLocationType, Integer> visitCount;
    private final Map<CompleteLocationType, Long> timeSpent;
    private final Map<LocationCategory, Integer> categoryProgress;
    private final Map<LocationRarity, Integer> rarityProgress;

    public PlayerLocations(Player player) {
        this.playerId = player.getUniqueId();
        this.discoveredLocations = new HashMap<>();
        this.firstVisitTime = new HashMap<>();
        this.lastVisitTime = new HashMap<>();
        this.visitCount = new HashMap<>();
        this.timeSpent = new HashMap<>();
        this.categoryProgress = new HashMap<>();
        this.rarityProgress = new HashMap<>();
        // Load existing location data from database or file here
    }

    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Discover a new location
     */
    public void discoverLocation(CompleteLocationType locationType) {
        if (!discoveredLocations.containsKey(locationType)) {
            discoveredLocations.put(locationType, true);
            firstVisitTime.put(locationType, LocalDateTime.now());
            lastVisitTime.put(locationType, LocalDateTime.now());
            visitCount.put(locationType, 1);
            timeSpent.put(locationType, 0L);
            
            // Update category and rarity progress
            LocationCategory category = locationType.getCategory();
            LocationRarity rarity = locationType.getRarity();
            
            categoryProgress.put(category, categoryProgress.getOrDefault(category, 0) + 1);
            rarityProgress.put(rarity, rarityProgress.getOrDefault(rarity, 0) + 1);
        }
    }

    /**
     * Visit a location
     */
    public void visitLocation(CompleteLocationType locationType) {
        if (!discoveredLocations.containsKey(locationType)) {
            discoverLocation(locationType);
        } else {
            lastVisitTime.put(locationType, LocalDateTime.now());
            visitCount.put(locationType, visitCount.getOrDefault(locationType, 0) + 1);
        }
    }

    /**
     * Record time spent in a location
     */
    public void recordTimeSpent(CompleteLocationType locationType, long timeSpent) {
        if (discoveredLocations.containsKey(locationType)) {
            this.timeSpent.put(locationType, this.timeSpent.getOrDefault(locationType, 0L) + timeSpent);
        }
    }

    /**
     * Check if a location has been discovered
     */
    public boolean hasDiscoveredLocation(CompleteLocationType locationType) {
        return discoveredLocations.getOrDefault(locationType, false);
    }

    /**
     * Get first visit time for a location
     */
    public LocalDateTime getFirstVisitTime(CompleteLocationType locationType) {
        return firstVisitTime.get(locationType);
    }

    /**
     * Get last visit time for a location
     */
    public LocalDateTime getLastVisitTime(CompleteLocationType locationType) {
        return lastVisitTime.get(locationType);
    }

    /**
     * Get visit count for a location
     */
    public int getVisitCount(CompleteLocationType locationType) {
        return visitCount.getOrDefault(locationType, 0);
    }

    /**
     * Get time spent in a location
     */
    public long getTimeSpent(CompleteLocationType locationType) {
        return timeSpent.getOrDefault(locationType, 0L);
    }

    /**
     * Get all discovered locations
     */
    public Map<CompleteLocationType, Boolean> getAllDiscoveredLocations() {
        return new HashMap<>(discoveredLocations);
    }

    /**
     * Get all visit times
     */
    public Map<CompleteLocationType, LocalDateTime> getAllFirstVisitTimes() {
        return new HashMap<>(firstVisitTime);
    }

    /**
     * Get all last visit times
     */
    public Map<CompleteLocationType, LocalDateTime> getAllLastVisitTimes() {
        return new HashMap<>(lastVisitTime);
    }

    /**
     * Get all visit counts
     */
    public Map<CompleteLocationType, Integer> getAllVisitCounts() {
        return new HashMap<>(visitCount);
    }

    /**
     * Get all time spent
     */
    public Map<CompleteLocationType, Long> getAllTimeSpent() {
        return new HashMap<>(timeSpent);
    }

    /**
     * Get total discovered locations
     */
    public int getTotalDiscoveredLocations() {
        return discoveredLocations.size();
    }

    /**
     * Get total visit count
     */
    public int getTotalVisitCount() {
        return visitCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total time spent
     */
    public long getTotalTimeSpent() {
        return timeSpent.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Get discovered locations by category
     */
    public Map<CompleteLocationType, Boolean> getDiscoveredLocationsByCategory(LocationCategory category) {
        Map<CompleteLocationType, Boolean> result = new HashMap<>();
        for (Map.Entry<CompleteLocationType, Boolean> entry : discoveredLocations.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Get discovered locations by rarity
     */
    public Map<CompleteLocationType, Boolean> getDiscoveredLocationsByRarity(LocationRarity rarity) {
        Map<CompleteLocationType, Boolean> result = new HashMap<>();
        for (Map.Entry<CompleteLocationType, Boolean> entry : discoveredLocations.entrySet()) {
            if (entry.getKey().getRarity() == rarity) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Get category progress
     */
    public int getCategoryProgress(LocationCategory category) {
        return categoryProgress.getOrDefault(category, 0);
    }

    /**
     * Get rarity progress
     */
    public int getRarityProgress(LocationRarity rarity) {
        return rarityProgress.getOrDefault(rarity, 0);
    }

    /**
     * Get completion percentage for a category
     */
    public double getCategoryCompletionPercentage(LocationCategory category) {
        int discoveredInCategory = getCategoryProgress(category);
        int totalInCategory = CompleteLocationType.getLocationCountByCategory(category);
        
        if (totalInCategory == 0) {
            return 0.0;
        }
        
        return (double) discoveredInCategory / totalInCategory * 100.0;
    }

    /**
     * Get completion percentage for a rarity
     */
    public double getRarityCompletionPercentage(LocationRarity rarity) {
        int discoveredOfRarity = getRarityProgress(rarity);
        int totalOfRarity = CompleteLocationType.getLocationCountByRarity(rarity);
        
        if (totalOfRarity == 0) {
            return 0.0;
        }
        
        return (double) discoveredOfRarity / totalOfRarity * 100.0;
    }

    /**
     * Get overall completion percentage
     */
    public double getOverallCompletionPercentage() {
        int totalLocations = CompleteLocationType.getTotalLocationCount();
        if (totalLocations == 0) {
            return 0.0;
        }
        return (double) getTotalDiscoveredLocations() / totalLocations * 100.0;
    }

    /**
     * Get most visited location
     */
    public CompleteLocationType getMostVisitedLocation() {
        return visitCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get location with most time spent
     */
    public CompleteLocationType getLocationWithMostTimeSpent() {
        return timeSpent.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get recently discovered locations (last 24 hours)
     */
    public Map<CompleteLocationType, LocalDateTime> getRecentlyDiscoveredLocations() {
        Map<CompleteLocationType, LocalDateTime> recent = new HashMap<>();
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        
        for (Map.Entry<CompleteLocationType, LocalDateTime> entry : firstVisitTime.entrySet()) {
            if (entry.getValue().isAfter(cutoff)) {
                recent.put(entry.getKey(), entry.getValue());
            }
        }
        
        return recent;
    }

    /**
     * Get locations by visit frequency
     */
    public Map<CompleteLocationType, Integer> getLocationsByVisitFrequency() {
        return new HashMap<>(visitCount);
    }

    /**
     * Get locations by time spent
     */
    public Map<CompleteLocationType, Long> getLocationsByTimeSpent() {
        return new HashMap<>(timeSpent);
    }

    /**
     * Get exploration statistics
     */
    public String getExplorationStatistics() {
        return String.format(
                "Discovered: %d/%d (%.1f%%) | Visits: %d | Time: %d min",
                getTotalDiscoveredLocations(),
                CompleteLocationType.getTotalLocationCount(),
                getOverallCompletionPercentage(),
                getTotalVisitCount(),
                getTotalTimeSpent() / 60000 // Convert to minutes
        );
    }

    /**
     * Get category statistics
     */
    public Map<LocationCategory, String> getCategoryStatistics() {
        Map<LocationCategory, String> stats = new HashMap<>();
        
        for (LocationCategory category : LocationCategory.values()) {
            int discovered = getCategoryProgress(category);
            int total = CompleteLocationType.getLocationCountByCategory(category);
            double percentage = getCategoryCompletionPercentage(category);
            
            stats.put(category, String.format("%d/%d (%.1f%%)", discovered, total, percentage));
        }
        
        return stats;
    }

    /**
     * Get rarity statistics
     */
    public Map<LocationRarity, String> getRarityStatistics() {
        Map<LocationRarity, String> stats = new HashMap<>();
        
        for (LocationRarity rarity : LocationRarity.values()) {
            int discovered = getRarityProgress(rarity);
            int total = CompleteLocationType.getLocationCountByRarity(rarity);
            double percentage = getRarityCompletionPercentage(rarity);
            
            stats.put(rarity, String.format("%d/%d (%.1f%%)", discovered, total, percentage));
        }
        
        return stats;
    }
}
