package de.noctivag.skyblock.features.economy.auction.types;
import org.bukkit.inventory.ItemStack;

/**
 * Bid Result enum for bid outcomes
 */
public enum BidResult {
    SUCCESS("Bid placed successfully"),
    INSUFFICIENT_FUNDS("Insufficient funds"),
    BID_TOO_LOW("Bid amount too low"),
    AUCTION_ENDED("Auction has ended"),
    INVALID_AUCTION("Invalid auction"),
    ALREADY_WINNING("Already winning bid"),
    ERROR("An error occurred");

    private final String message;

    BidResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
