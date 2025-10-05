package de.noctivag.skyblock.engine.rte.data;

/**
 * Market Trend - Analysis of market price trends
 */
public class MarketTrend {
    
    private final TrendType trendType;
    private final double changePercentage;
    private final boolean manipulationDetected;
    
    public MarketTrend(TrendType trendType, double changePercentage, boolean manipulationDetected) {
        this.trendType = trendType;
        this.changePercentage = changePercentage;
        this.manipulationDetected = manipulationDetected;
    }
    
    // Getters
    public TrendType getTrendType() { return trendType; }
    public double getChangePercentage() { return changePercentage; }
    public boolean isManipulationDetected() { return manipulationDetected; }
    
    public enum TrendType {
        RISING, FALLING, STABLE
    }
}
