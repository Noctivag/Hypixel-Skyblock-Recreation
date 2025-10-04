package de.noctivag.skyblock.engine.collections.types;

import org.bukkit.inventory.ItemStack;

/**
 * Item Eligibility Information
 * 
 * Contains information about whether a specific item is eligible
 * for collection progression and why.
 */
public class ItemEligibility {
    
    private final ItemStack item;
    private final ItemProvenance provenance;
    private final boolean isEligible;
    private final int eligibleAmount;
    private final int ineligibleAmount;
    
    public ItemEligibility(ItemStack item, ItemProvenance provenance, boolean isEligible, 
                           int eligibleAmount, int ineligibleAmount) {
        this.item = item;
        this.provenance = provenance;
        this.isEligible = isEligible;
        this.eligibleAmount = eligibleAmount;
        this.ineligibleAmount = ineligibleAmount;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public ItemProvenance getProvenance() {
        return provenance;
    }
    
    public boolean isEligible() {
        return isEligible;
    }
    
    public int getEligibleAmount() {
        return eligibleAmount;
    }
    
    public int getIneligibleAmount() {
        return ineligibleAmount;
    }
    
    public int getTotalAmount() {
        return eligibleAmount + ineligibleAmount;
    }
    
    public String getEligibilityReason() {
        if (provenance == null) {
            return "No provenance data";
        }
        
        if (!provenance.isEligibleForCollections()) {
            return "Item from trade source: " + provenance.getSource().getDisplayName();
        }
        
        return "Eligible for collections";
    }
    
    @Override
    public String toString() {
        return String.format("ItemEligibility{item=%s, eligible=%b, eligibleAmount=%d, ineligibleAmount=%d, reason='%s'}", 
            item.getType(), isEligible, eligibleAmount, ineligibleAmount, getEligibilityReason());
    }
}
