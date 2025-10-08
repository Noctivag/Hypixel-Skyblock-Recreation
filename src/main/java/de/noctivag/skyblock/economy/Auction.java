package de.noctivag.skyblock.economy;

import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.Component;

import java.util.UUID;

/**
 * Represents an auction in the auction house
 */
public class Auction {
    private final UUID auctionId;
    private final UUID sellerId;
    private final ItemStack item;
    private final double startingBid;
    private final double binPrice;
    private final long endTime;

    private double currentBid;
    private UUID highestBidder;
    private boolean isActive;

    public Auction(UUID auctionId, UUID sellerId, ItemStack item, double startingBid, double binPrice, long endTime) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.item = item.clone();
        this.startingBid = startingBid;
        this.binPrice = binPrice;
        this.endTime = endTime;
        this.currentBid = startingBid;
        this.isActive = true;
    }

    public UUID getId() { return auctionId; }
    public UUID getSellerId() { return sellerId; }
    public ItemStack getItem() { return item.clone(); }
    public double getStartingBid() { return startingBid; }
    public double getBinPrice() { return binPrice; }
    public double getCurrentBid() { return currentBid; }
    public UUID getHighestBidder() { return highestBidder; }
    public long getEndTime() { return endTime; }
    public boolean isActive() { return isActive; }

    public void setCurrentBid(double bid) { this.currentBid = bid; }
    public void setHighestBidder(UUID bidder) { this.highestBidder = bidder; }
    public void setActive(boolean active) { this.isActive = active; }

    /**
     * Update auction status (check if expired)
     */
    public void update() {
        if (isActive && System.currentTimeMillis() > endTime) {
            isActive = false;
        }
    }

    /**
     * Check if auction is expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > endTime;
    }

    /**
     * Get time remaining in milliseconds
     */
    public long getTimeRemaining() {
        return Math.max(0, endTime - System.currentTimeMillis());
    }

    /**
     * Get formatted time remaining
     */
    public String getFormattedTimeRemaining() {
        long remaining = getTimeRemaining();
        long hours = remaining / (60 * 60 * 1000);
        long minutes = (remaining % (60 * 60 * 1000)) / (60 * 1000);

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    /**
     * Place a bid on this auction
     */
    public boolean placeBid(UUID bidder, double bidAmount) {
        if (!isActive || bidAmount <= currentBid) {
            return false;
        }

        currentBid = bidAmount;
        highestBidder = bidder;
        return true;
    }

    /**
     * Buy it now
     */
    public boolean buyNow(UUID buyer) {
        if (!isActive || binPrice <= 0) {
            return false;
        }

        currentBid = binPrice;
        highestBidder = buyer;
        isActive = false;
        return true;
    }
}
