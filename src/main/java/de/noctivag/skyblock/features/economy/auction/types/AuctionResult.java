package de.noctivag.skyblock.features.economy.auction.types;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Auction Result class for auction outcomes
 */
public class AuctionResult {
    private String auctionId;
    private boolean sold;
    private UUID winnerId;
    private String winnerName;
    private double finalPrice;
    private long endTime;
    private String reason;

    public AuctionResult(String auctionId, boolean sold, UUID winnerId, String winnerName, double finalPrice) {
        this.auctionId = auctionId;
        this.sold = sold;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
        this.finalPrice = finalPrice;
        this.endTime = System.currentTimeMillis();
        this.reason = sold ? "Sold" : "Expired";
    }

    public String getAuctionId() {
        return auctionId;
    }

    public boolean isSold() {
        return sold;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
