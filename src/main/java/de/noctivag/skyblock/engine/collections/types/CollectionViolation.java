package de.noctivag.skyblock.engine.collections.types;

import java.util.UUID;

/**
 * Collection Violation
 * 
 * Represents a violation of collection constraints, such as
 * attempting to use trade items for collection progression.
 */
public class CollectionViolation {
    
    private final UUID playerId;
    private final CollectionType collectionType;
    private final CollectionItem item;
    private final int amount;
    private final String reason;
    private final CollectionViolationType type;
    private final long timestamp;
    
    public CollectionViolation(UUID playerId, CollectionType collectionType, CollectionItem item, 
                              int amount, String reason, CollectionViolationType type) {
        this.playerId = playerId;
        this.collectionType = collectionType;
        this.item = item;
        this.amount = amount;
        this.reason = reason;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public CollectionType getCollectionType() {
        return collectionType;
    }
    
    public CollectionItem getItem() {
        return item;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public String getReason() {
        return reason;
    }
    
    public CollectionViolationType getType() {
        return type;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Get age of this violation in hours
     */
    public double getAgeInHours() {
        return (System.currentTimeMillis() - timestamp) / (60.0 * 60 * 1000);
    }
    
    /**
     * Check if this violation is recent (within specified hours)
     */
    public boolean isRecent(int hours) {
        return getAgeInHours() <= hours;
    }
    
    @Override
    public String toString() {
        return String.format("CollectionViolation{playerId=%s, collectionType=%s, item=%s, amount=%d, reason='%s', type=%s, timestamp=%d}", 
            playerId, collectionType, item, amount, reason, type, timestamp);
    }
}
