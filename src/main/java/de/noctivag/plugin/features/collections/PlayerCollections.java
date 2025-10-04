package de.noctivag.plugin.features.collections;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.collections.types.CollectionType;
import de.noctivag.plugin.features.collections.types.CollectionCategory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player's collection progress
 */
public class PlayerCollections {
    
    private final UUID playerId;
    private final Map<CollectionType, CollectionProgress> collections = new ConcurrentHashMap<>();
    
    public PlayerCollections(UUID playerId) {
        this.playerId = playerId;
        initializeCollections();
    }
    
    /**
     * Get collection progress for a specific type
     */
    public CollectionProgress getCollection(CollectionType type) {
        return collections.computeIfAbsent(type, k -> new CollectionProgress(type));
    }
    
    /**
     * Get all collections
     */
    public Map<CollectionType, CollectionProgress> getAllCollections() {
        return new ConcurrentHashMap<>(collections);
    }
    
    /**
     * Get total collection level across all types
     */
    public int getTotalCollectionLevel() {
        return collections.values().stream()
            .mapToInt(CollectionProgress::getLevel)
            .sum();
    }
    
    /**
     * Get total items collected across all types
     */
    public long getTotalItemsCollected() {
        return collections.values().stream()
            .mapToLong(CollectionProgress::getTotalItems)
            .sum();
    }
    
    /**
     * Get collections by category
     */
    public Map<CollectionType, CollectionProgress> getCollectionsByCategory(CollectionCategory category) {
        Map<CollectionType, CollectionProgress> categoryCollections = new ConcurrentHashMap<>();
        
        for (Map.Entry<CollectionType, CollectionProgress> entry : collections.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                categoryCollections.put(entry.getKey(), entry.getValue());
            }
        }
        
        return categoryCollections;
    }
    
    /**
     * Get top collections by level
     */
    public Map<CollectionType, CollectionProgress> getTopCollections(int limit) {
        return collections.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue().getLevel(), a.getValue().getLevel()))
            .limit(limit)
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));
    }
    
    /**
     * Get collections that need attention (low level)
     */
    public Map<CollectionType, CollectionProgress> getCollectionsNeedingAttention(int maxLevel) {
        return collections.entrySet().stream()
            .filter(entry -> entry.getValue().getLevel() <= maxLevel)
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));
    }
    
    /**
     * Initialize all collections
     */
    private void initializeCollections() {
        for (CollectionType type : CollectionType.values()) {
            collections.put(type, new CollectionProgress(type));
        }
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
}
