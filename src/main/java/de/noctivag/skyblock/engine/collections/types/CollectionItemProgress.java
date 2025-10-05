package de.noctivag.skyblock.engine.collections.types;

import java.util.ArrayList;
import java.util.List;
import de.noctivag.skyblock.engine.collections.ItemProvenance;
import de.noctivag.skyblock.engine.collections.CollectionSource;

/**
 * Collection Item Progress
 * 
 * Tracks the progress of a specific collection item for a player,
 * including provenance information for constraint enforcement.
 */
public class CollectionItemProgress {
    
    private final CollectionItem item;
    private int amount;
    private final List<ItemProvenance> provenances;
    private final long firstObtained;
    private long lastObtained;
    
    public CollectionItemProgress(CollectionItem item, int amount, ItemProvenance provenance, long timestamp) {
        this.item = item;
        this.amount = amount;
        this.provenances = new ArrayList<>();
        this.firstObtained = timestamp;
        this.lastObtained = timestamp;
        
        if (provenance != null) {
            this.provenances.add(provenance);
        }
    }
    
    public CollectionItem getItem() {
        return item;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public List<ItemProvenance> getProvenances() {
        return new ArrayList<>(provenances);
    }
    
    public long getFirstObtained() {
        return firstObtained;
    }
    
    public long getLastObtained() {
        return lastObtained;
    }
    
    /**
     * Add progress from multiple items
     */
    public void addProgress(List<CollectionItemProgress> progressItems) {
        for (CollectionItemProgress progressItem : progressItems) {
            this.amount += progressItem.getAmount();
            this.provenances.addAll(progressItem.getProvenances());
            this.lastObtained = Math.max(this.lastObtained, progressItem.getLastObtained());
        }
    }
    
    /**
     * Add single item progress
     */
    public void addProgress(int amount, ItemProvenance provenance, long timestamp) {
        this.amount += amount;
        if (provenance != null) {
            this.provenances.add(provenance);
        }
        this.lastObtained = Math.max(this.lastObtained, timestamp);
    }
    
    /**
     * Get progress by source type
     */
    public int getAmountBySource(CollectionSource source) {
        return provenances.stream()
            .filter(provenance -> provenance.getSource() == source)
            .mapToInt(provenance -> {
                // For now, assume 1 item per provenance entry
                // This could be enhanced to track amounts per provenance
                return 1;
            })
            .sum();
    }
    
    /**
     * Get progress from allowed sources only
     */
    public int getEligibleAmount() {
        return provenances.stream()
            .filter(ItemProvenance::isEligibleForCollections)
            .mapToInt(provenance -> 1) // Simplified for now
            .sum();
    }
    
    /**
     * Get progress from trade sources
     */
    public int getTradeAmount() {
        return provenances.stream()
            .filter(ItemProvenance::isTradeItem)
            .mapToInt(provenance -> 1) // Simplified for now
            .sum();
    }
    
    /**
     * Get age of this progress in days
     */
    public double getAgeInDays() {
        return (java.lang.System.currentTimeMillis() - firstObtained) / (24.0 * 60 * 60 * 1000);
    }
    
    /**
     * Get time since last obtained in hours
     */
    public double getTimeSinceLastObtainedInHours() {
        return (java.lang.System.currentTimeMillis() - lastObtained) / (60.0 * 60 * 1000);
    }
    
    @Override
    public String toString() {
        return String.format("CollectionItemProgress{item=%s, amount=%d, provenances=%d, firstObtained=%d, lastObtained=%d}", 
            item, amount, provenances.size(), firstObtained, lastObtained);
    }
}
