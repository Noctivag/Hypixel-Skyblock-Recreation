package de.noctivag.skyblock.features.economy.auction.types;

/**
 * Simple result for auction operations
 */
public class AuctionOperationResult {
    private final String status;
    private final boolean success;
    private final String auctionId;
    private final String message;
    private final double amount;
    
    public AuctionOperationResult(String status, boolean success, String auctionId, String message, double amount) {
        this.status = status;
        this.success = success;
        this.auctionId = auctionId;
        this.message = message;
        this.amount = amount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getAuctionId() {
        return auctionId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public double getAmount() {
        return amount;
    }
}
