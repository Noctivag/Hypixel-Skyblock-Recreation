package de.noctivag.skyblock.engine.collections.types;
import java.util.UUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Collection Constraint Statistics
 * 
 * Contains comprehensive statistics about a player's collection progress
 * and constraint violations across all collection types.
 */
public class CollectionConstraintStatistics {
    
    private final UUID playerId;
    private final Map<CollectionType, CollectionProgressInfo> progressInfo;
    private final List<CollectionViolation> allViolations;
    
    public CollectionConstraintStatistics(UUID playerId, 
                                          Map<CollectionType, CollectionProgressInfo> progressInfo,
                                          List<CollectionViolation> allViolations) {
        this.playerId = playerId;
        this.progressInfo = progressInfo;
        this.allViolations = allViolations;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public Map<CollectionType, CollectionProgressInfo> getProgressInfo() {
        return progressInfo;
    }
    
    public List<CollectionViolation> getAllViolations() {
        return allViolations;
    }
    
    public int getTotalEligibleAmount() {
        return progressInfo.values().stream()
            .mapToInt(CollectionProgressInfo::getTotalEligibleAmount)
            .sum();
    }
    
    public int getTotalIneligibleAmount() {
        return progressInfo.values().stream()
            .mapToInt(CollectionProgressInfo::getTotalIneligibleAmount)
            .sum();
    }
    
    public int getTotalAmount() {
        return getTotalEligibleAmount() + getTotalIneligibleAmount();
    }
    
    public double getOverallEligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 100.0;
        return (double) getTotalEligibleAmount() / total * 100.0;
    }
    
    public double getOverallIneligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 0.0;
        return (double) getTotalIneligibleAmount() / total * 100.0;
    }
    
    public int getTotalViolations() {
        return allViolations.size();
    }
    
    public int getViolationsByType(CollectionViolationType type) {
        return (int) allViolations.stream()
            .filter(violation -> violation.getType() == type)
            .count();
    }
    
    public int getRecentViolations(int hours) {
        return (int) allViolations.stream()
            .filter(violation -> violation.isRecent(hours))
            .count();
    }
    
    public boolean hasViolations() {
        return !allViolations.isEmpty();
    }
    
    public boolean hasRecentViolations(int hours) {
        return getRecentViolations(hours) > 0;
    }
    
    public int getActiveCollectionTypes() {
        return (int) progressInfo.values().stream()
            .filter(CollectionProgressInfo::hasProgress)
            .count();
    }
    
    public int getCollectionTypesWithViolations() {
        return (int) progressInfo.values().stream()
            .filter(CollectionProgressInfo::hasViolations)
            .count();
    }
    
    public double getViolationRate() {
        int totalCollections = progressInfo.size();
        if (totalCollections == 0) return 0.0;
        return (double) getCollectionTypesWithViolations() / totalCollections * 100.0;
    }
    
    @Override
    public String toString() {
        return String.format("CollectionConstraintStatistics{playerId=%s, totalEligibleAmount=%d, totalIneligibleAmount=%d, violations=%d, eligiblePercentage=%.1f%%}", 
            playerId, getTotalEligibleAmount(), getTotalIneligibleAmount(), getTotalViolations(), getOverallEligiblePercentage());
    }
}
