package de.noctivag.skyblock.features.economy.bazaar.types;

/**
 * Detailed result for bazaar order operations
 */
public class BazaarOrderDetails {
    private final boolean success;
    private final String message;
    private final int quantity;
    private final double amount;
    
    public BazaarOrderDetails(boolean success, String message, int quantity, double amount) {
        this.success = success;
        this.message = message;
        this.quantity = quantity;
        this.amount = amount;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getAmount() {
        return amount;
    }
}
