package de.noctivag.skyblock.engine.collections.types;

import java.util.Objects;
import java.util.UUID;

/**
 * Item Provenance Information
 * 
 * Contains all information about how an item was obtained,
 * including source, timestamp, player, and location.
 * This is critical for enforcing collection constraints.
 */
public class ItemProvenance {
    
    private final CollectionSource source;
    private final long timestamp;
    private final UUID playerId;
    private final String location;
    
    public ItemProvenance(CollectionSource source, long timestamp, UUID playerId, String location) {
        this.source = source != null ? source : CollectionSource.UNKNOWN;
        this.timestamp = timestamp;
        this.playerId = playerId;
        this.location = location;
    }
    
    public CollectionSource getSource() {
        return source;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public String getLocation() {
        return location;
    }
    
    /**
     * Check if this item is eligible for collection progression
     */
    public boolean isEligibleForCollections() {
        return source.isAllowedForCollections();
    }
    
    /**
     * Check if this item was obtained through trade
     */
    public boolean isTradeItem() {
        return !source.isAllowedForCollections();
    }
    
    /**
     * Get age of this item in milliseconds
     */
    public long getAge() {
        return System.currentTimeMillis() - timestamp;
    }
    
    /**
     * Get age of this item in days
     */
    public double getAgeInDays() {
        return getAge() / (24.0 * 60 * 60 * 1000);
    }
    
    /**
     * Check if this item is recent (within specified hours)
     */
    public boolean isRecent(int hours) {
        return getAge() <= (hours * 60L * 60 * 1000);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemProvenance that = (ItemProvenance) obj;
        return timestamp == that.timestamp &&
               Objects.equals(source, that.source) &&
               Objects.equals(playerId, that.playerId) &&
               Objects.equals(location, that.location);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(source, timestamp, playerId, location);
    }
    
    @Override
    public String toString() {
        return String.format("ItemProvenance{source=%s, timestamp=%d, playerId=%s, location='%s'}", 
            source, timestamp, playerId, location);
    }
}
