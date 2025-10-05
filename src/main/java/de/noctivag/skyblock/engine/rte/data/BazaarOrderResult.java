package de.noctivag.skyblock.engine.rte.data;

import java.util.List;

/**
 * Bazaar Order Result - Result of bazaar order processing
 */
public class BazaarOrderResult {
    
    private final boolean success;
    private final String message;
    private final List<OrderMatch> matches;
    
    public BazaarOrderResult(boolean success, String message, List<OrderMatch> matches) {
        this.success = success;
        this.message = message;
        this.matches = matches != null ? matches : new java.util.ArrayList<>();
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<OrderMatch> getMatches() { return matches; }
    
    public static class OrderMatch {
        private final BazaarOrder buyOrder;
        private final BazaarOrder sellOrder;
        private final int amount;
        private final double price;
        private final long timestamp;
        
        public OrderMatch(BazaarOrder buyOrder, BazaarOrder sellOrder, int amount, double price) {
            this.buyOrder = buyOrder;
            this.sellOrder = sellOrder;
            this.amount = amount;
            this.price = price;
            this.timestamp = java.lang.System.currentTimeMillis();
        }
        
        // Getters
        public BazaarOrder getBuyOrder() { return buyOrder; }
        public BazaarOrder getSellOrder() { return sellOrder; }
        public int getAmount() { return amount; }
        public double getPrice() { return price; }
        public long getTimestamp() { return timestamp; }
    }
}
