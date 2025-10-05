package de.noctivag.skyblock.engine.collections;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents the provenance (origin) of an item in collections
 */
public class ItemProvenance {
    private final UUID playerId;
    private final String itemId;
    private final CollectionSource source;
    private final LocalDateTime timestamp;
    private final int quantity;
    private final String location;
    private final String metadata;

    public ItemProvenance(UUID playerId, String itemId, CollectionSource source, 
                         LocalDateTime timestamp, int quantity, String location, String metadata) {
        this.playerId = playerId;
        this.itemId = itemId;
        this.source = source;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.location = location;
        this.metadata = metadata;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getItemId() {
        return itemId;
    }

    public CollectionSource getSource() {
        return source;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public String getMetadata() {
        return metadata;
    }
    
    /**
     * Check if this item is eligible for collections
     */
    public boolean isEligibleForCollections() {
        // Items from certain sources are not eligible for collections
        return source != CollectionSource.ADMIN_GIVE && 
               source != CollectionSource.DIRECT_TRADE &&
               quantity > 0;
    }
    
    public boolean isTradeItem() {
        return source == CollectionSource.DIRECT_TRADE || 
               source == CollectionSource.AUCTION_HOUSE;
    }

    @Override
    public String toString() {
        return String.format("ItemProvenance{itemId='%s', source=%s, quantity=%d, timestamp=%s}", 
                           itemId, source, quantity, timestamp);
    }
}
