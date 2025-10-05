package de.noctivag.skyblock.features.economy.auction.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Auction Bid class for auction bidding
 */
public class AuctionBid {
    private String id;
    private String auctionId;
    private UUID bidderId;
    private String bidderName;
    private double amount;
    private long timestamp;
    private boolean isWinning;

    public AuctionBid(String id, String auctionId, UUID bidderId, String bidderName, double amount) {
        this.id = id;
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.bidderName = bidderName;
        this.amount = amount;
        this.timestamp = java.lang.System.currentTimeMillis();
        this.isWinning = false;
    }

    public String getId() {
        return id;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public UUID getBidderId() {
        return bidderId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public double getAmount() {
        return amount;
    }
    
    public double getBidAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isWinning() {
        return isWinning;
    }

    public void setWinning(boolean winning) {
        isWinning = winning;
    }
}
