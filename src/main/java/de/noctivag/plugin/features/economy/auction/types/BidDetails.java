package de.noctivag.plugin.features.economy.auction.types;

/**
 * Detailed result for bid operations
 */
public class BidDetails {
    private final boolean success;
    private final String message;
    private final double amount;
    
    public BidDetails(boolean success, String message, double amount) {
        this.success = success;
        this.message = message;
        this.amount = amount;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public double getAmount() {
        return amount;
    }
}
