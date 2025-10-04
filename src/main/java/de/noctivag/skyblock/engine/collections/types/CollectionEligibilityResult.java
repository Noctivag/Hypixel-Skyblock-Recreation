package de.noctivag.skyblock.engine.collections.types;

import java.util.List;

/**
 * Collection Eligibility Result
 * 
 * Contains the result of checking item eligibility for collection progression,
 * including detailed information about each item's eligibility status.
 */
public class CollectionEligibilityResult {
    
    private final List<ItemEligibility> itemEligibilities;
    private final int totalEligibleAmount;
    private final int totalIneligibleAmount;
    
    public CollectionEligibilityResult(List<ItemEligibility> itemEligibilities, 
                                       int totalEligibleAmount, int totalIneligibleAmount) {
        this.itemEligibilities = itemEligibilities;
        this.totalEligibleAmount = totalEligibleAmount;
        this.totalIneligibleAmount = totalIneligibleAmount;
    }
    
    public List<ItemEligibility> getItemEligibilities() {
        return itemEligibilities;
    }
    
    public int getTotalEligibleAmount() {
        return totalEligibleAmount;
    }
    
    public int getTotalIneligibleAmount() {
        return totalIneligibleAmount;
    }
    
    public int getTotalAmount() {
        return totalEligibleAmount + totalIneligibleAmount;
    }
    
    public boolean hasEligibleItems() {
        return totalEligibleAmount > 0;
    }
    
    public boolean hasIneligibleItems() {
        return totalIneligibleAmount > 0;
    }
    
    public double getEligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 100.0;
        return (double) totalEligibleAmount / total * 100.0;
    }
    
    public double getIneligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 0.0;
        return (double) totalIneligibleAmount / total * 100.0;
    }
    
    @Override
    public String toString() {
        return String.format("CollectionEligibilityResult{eligibleAmount=%d, ineligibleAmount=%d, eligiblePercentage=%.1f%%}", 
            totalEligibleAmount, totalIneligibleAmount, getEligiblePercentage());
    }
}
