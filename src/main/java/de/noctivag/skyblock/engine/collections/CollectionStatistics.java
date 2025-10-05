package de.noctivag.skyblock.engine.collections;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents statistics for a player's collection progress
 */
public class CollectionStatistics {
    private final UUID playerId;
    private final Map<String, Integer> itemCounts;
    private final Map<CollectionSource, Integer> sourceCounts;
    private final LocalDateTime lastUpdated;
    private final int totalItems;
    private final int uniqueItems;

    public CollectionStatistics(UUID playerId) {
        this.playerId = playerId;
        this.itemCounts = new HashMap<>();
        this.sourceCounts = new HashMap<>();
        this.lastUpdated = LocalDateTime.now();
        this.totalItems = 0;
        this.uniqueItems = 0;
    }

    public CollectionStatistics(UUID playerId, Map<String, Integer> itemCounts, 
                               Map<CollectionSource, Integer> sourceCounts) {
        this.playerId = playerId;
        this.itemCounts = new HashMap<>(itemCounts);
        this.sourceCounts = new HashMap<>(sourceCounts);
        this.lastUpdated = LocalDateTime.now();
        this.totalItems = itemCounts.values().stream().mapToInt(Integer::intValue).sum();
        this.uniqueItems = itemCounts.size();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<String, Integer> getItemCounts() {
        return new HashMap<>(itemCounts);
    }

    public Map<CollectionSource, Integer> getSourceCounts() {
        return new HashMap<>(sourceCounts);
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getUniqueItems() {
        return uniqueItems;
    }

    public int getItemCount(String itemId) {
        return itemCounts.getOrDefault(itemId, 0);
    }

    public int getSourceCount(CollectionSource source) {
        return sourceCounts.getOrDefault(source, 0);
    }

    public void addItem(String itemId, int quantity, CollectionSource source) {
        itemCounts.merge(itemId, quantity, Integer::sum);
        sourceCounts.merge(source, quantity, Integer::sum);
    }

    @Override
    public String toString() {
        return String.format("CollectionStatistics{playerId=%s, totalItems=%d, uniqueItems=%d, lastUpdated=%s}", 
                           playerId, totalItems, uniqueItems, lastUpdated);
    }
}
