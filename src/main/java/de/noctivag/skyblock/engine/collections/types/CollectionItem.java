package de.noctivag.skyblock.engine.collections.types;

/**
 * Collection Item
 * 
 * Represents an item that can be collected for a specific collection type.
 * Each item has a name, conversion rate, and maximum collection amount.
 */
public class CollectionItem {
    
    private final String name;
    private final int conversionRate; // How many of this item equal 1 collection point
    private final int maxCollection; // Maximum amount that can be collected
    
    public CollectionItem(String name, int conversionRate, int maxCollection) {
        this.name = name;
        this.conversionRate = conversionRate;
        this.maxCollection = maxCollection;
    }
    
    public String getName() {
        return name;
    }
    
    public int getConversionRate() {
        return conversionRate;
    }
    
    public int getMaxCollection() {
        return maxCollection;
    }
    
    /**
     * Calculate collection points from item amount
     */
    public int calculateCollectionPoints(int itemAmount) {
        return itemAmount / conversionRate;
    }
    
    /**
     * Calculate maximum collection points for this item
     */
    public int getMaxCollectionPoints() {
        return maxCollection / conversionRate;
    }
    
    /**
     * Check if an item amount exceeds the maximum collection
     */
    public boolean exceedsMaxCollection(int itemAmount) {
        return itemAmount > maxCollection;
    }
    
    /**
     * Get remaining collection capacity
     */
    public int getRemainingCapacity(int currentAmount) {
        return Math.max(0, maxCollection - currentAmount);
    }
    
    /**
     * Get collection progress percentage
     */
    public double getProgressPercentage(int currentAmount) {
        if (maxCollection <= 0) return 0.0;
        return Math.min((double) currentAmount / maxCollection, 1.0);
    }
    
    /**
     * Get formatted progress string
     */
    public String getFormattedProgress(int currentAmount) {
        double percentage = getProgressPercentage(currentAmount);
        return String.format("%s: %d/%d (%.1f%%)", 
            name, currentAmount, maxCollection, percentage * 100);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CollectionItem that = (CollectionItem) obj;
        return name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("CollectionItem{name='%s', conversionRate=%d, maxCollection=%d}", 
            name, conversionRate, maxCollection);
    }
}
