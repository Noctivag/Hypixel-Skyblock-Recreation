package de.noctivag.skyblock.engine.collections.types;

import java.util.List;

/**
 * Collection Process Result
 * 
 * Contains the result of processing items for collection progression,
 * including eligible and ineligible amounts, and any violations.
 */
public class CollectionProcessResult {
    
    private final int eligibleAmount;
    private final int ineligibleAmount;
    private final List<CollectionItemProgress> eligibleItems;
    private final List<CollectionViolation> violations;
    
    public CollectionProcessResult(int eligibleAmount, int ineligibleAmount, 
                                  List<CollectionItemProgress> eligibleItems, 
                                  List<CollectionViolation> violations) {
        this.eligibleAmount = eligibleAmount;
        this.ineligibleAmount = ineligibleAmount;
        this.eligibleItems = eligibleItems;
        this.violations = violations;
    }
    
    public int getEligibleAmount() {
        return eligibleAmount;
    }
    
    public int getIneligibleAmount() {
        return ineligibleAmount;
    }
    
    public List<CollectionItemProgress> getEligibleItems() {
        return eligibleItems;
    }
    
    public List<CollectionViolation> getViolations() {
        return violations;
    }
    
    public int getTotalAmount() {
        return eligibleAmount + ineligibleAmount;
    }
    
    public boolean hasEligibleItems() {
        return eligibleAmount > 0;
    }
    
    public boolean hasViolations() {
        return violations != null && !violations.isEmpty();
    }
    
    public double getEligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 100.0;
        return (double) eligibleAmount / total * 100.0;
    }
    
    public double getIneligiblePercentage() {
        int total = getTotalAmount();
        if (total == 0) return 0.0;
        return (double) ineligibleAmount / total * 100.0;
    }
    
    @Override
    public String toString() {
        return String.format("CollectionProcessResult{eligibleAmount=%d, ineligibleAmount=%d, violations=%d, eligiblePercentage=%.1f%%}", 
            eligibleAmount, ineligibleAmount, violations.size(), getEligiblePercentage());
    }
}
