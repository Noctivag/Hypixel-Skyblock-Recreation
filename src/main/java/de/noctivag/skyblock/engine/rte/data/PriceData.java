package de.noctivag.skyblock.engine.rte.data;

/**
 * Price Data - Container for price information
 */
public class PriceData {
    
    private final double instantBuyPrice;
    private final double instantSellPrice;
    private final double averagePrice;
    
    public PriceData(double instantBuyPrice, double instantSellPrice, double averagePrice) {
        this.instantBuyPrice = instantBuyPrice;
        this.instantSellPrice = instantSellPrice;
        this.averagePrice = averagePrice;
    }
    
    // Getters
    public double getInstantBuyPrice() { return instantBuyPrice; }
    public double getInstantSellPrice() { return instantSellPrice; }
    public double getAveragePrice() { return averagePrice; }
}