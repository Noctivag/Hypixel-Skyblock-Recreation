package de.noctivag.skyblock.engine.collections.types;
import java.util.UUID;

import java.util.Map;
import java.util.UUID;

/**
 * Collection Progress Information
 * 
 * Contains detailed information about a player's collection progress
 * for a specific collection type, including constraint information.
 */
public class CollectionProgressInfo {
    
    private final UUID playerId;
    private final CollectionType collectionType;
    private final Map<CollectionItem, CollectionItemProgress> collectionProgress;
    private final int totalEligibleAmount;
    private final int totalIneligibleAmount;
    
    public CollectionProgressInfo(UUID playerId, CollectionType collectionType, 
                                  Map<CollectionItem, CollectionItemProgress> collectionProgress,
                                  int totalEligibleAmount, int totalIneligibleAmount) {
        this.playerId = playerId;
        this.collectionType = collectionType;
        this.collectionProgress = collectionProgress;
        this.totalEligibleAmount = totalEligibleAmount;
        this.totalIneligibleAmount = totalIneligibleAmount;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public CollectionType getCollectionType() {
        return collectionType;
    }
    
    public Map<CollectionItem, CollectionItemProgress> getCollectionProgress() {
        return collectionProgress;
    }
    
    public int getTotalEligibleAmount() {
        return totalEligibleAmount;
    }
    
    public int getTotalIneligibleAmount() {
        return totalIneligibleAmount;
    }
    
    public int getTotalAmount() {
        return totalEligibleAmount + totalIneligibleAmount;
    }
    
    public double getEligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 100.0;
        return (double) totalEligibleAmount / total * 100.0;
    }
    
    public double getIneligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 0.0;
        return (double) totalIneligibleAmount / total * 100.0;
    }
    
    public int getUniqueItemsCount() {
        return collectionProgress.size();
    }
    
    public boolean hasProgress() {
        return !collectionProgress.isEmpty();
    }
    
    public boolean hasViolations() {
        return totalIneligibleAmount > 0;
    }
    
    @Override
    public String toString() {
        return String.format("CollectionProgressInfo{playerId=%s, collectionType=%s, eligibleAmount=%d, ineligibleAmount=%d, eligiblePercentage=%.1f%%}", 
            playerId, collectionType, totalEligibleAmount, totalIneligibleAmount, getEligiblePercentage());
    }
}
