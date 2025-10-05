package de.noctivag.skyblock.engine.rte.data;
import java.util.UUID;

import java.util.UUID;

/**
 * Auction Bid - Individual bid in the auction house system
 */
public class AuctionBid {
    
    private final UUID id;
    private final UUID auctionId;
    private final UUID playerId;
    private final double bidAmount;
    private final long timestamp;
    
    public AuctionBid(UUID id, UUID auctionId, UUID playerId, double bidAmount, long timestamp) {
        this.id = id;
        this.auctionId = auctionId;
        this.playerId = playerId;
        this.bidAmount = bidAmount;
        this.timestamp = timestamp;
    }
    
    // Getters
    public UUID getId() { return id; }
    public UUID getAuctionId() { return auctionId; }
    public UUID getPlayerId() { return playerId; }
    public double getBidAmount() { return bidAmount; }
    public long getTimestamp() { return timestamp; }
}
