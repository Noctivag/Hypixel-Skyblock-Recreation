package de.noctivag.skyblock.engine.collections;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.collections.types.CollectionSource;
import de.noctivag.skyblock.engine.collections.types.ItemProvenance;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Item Provenance System - Critical Economic Protection Mechanism
 * 
 * Implements the fundamental Item-Provenance Tracking system as specified
 * in the requirements. This system is CRITICAL for protecting the economy
 * by ensuring only naturally obtained items count towards collections.
 * 
 * Every item created or dropped must receive an NBT tag indicating its origin.
 * Items acquired through trade (AH/Bazaar) are explicitly excluded from
 * collection progression.
 */
public class ItemProvenanceSystem implements Service {
    
    // NBT Keys for item provenance tracking
    private static final NamespacedKey PROVENANCE_KEY = new NamespacedKey("skyblock", "provenance");
    private static final NamespacedKey SOURCE_KEY = new NamespacedKey("skyblock", "source");
    private static final NamespacedKey TIMESTAMP_KEY = new NamespacedKey("skyblock", "timestamp");
    private static final NamespacedKey PLAYER_KEY = new NamespacedKey("skyblock", "player");
    private static final NamespacedKey LOCATION_KEY = new NamespacedKey("skyblock", "location");
    
    private final Map<UUID, List<ItemProvenance>> playerItemHistory;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public ItemProvenanceSystem() {
        this.playerItemHistory = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize system components
            initializeProvenanceTracking();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save provenance data
            saveProvenanceData();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "ItemProvenanceSystem";
    }
    
    /**
     * Tag an item with provenance information
     * 
     * This method MUST be called for every item that is created, dropped,
     * or obtained through any means to ensure proper tracking.
     */
    public CompletableFuture<Boolean> tagItem(ItemStack item, CollectionSource source, UUID playerId, String location) {
        return CompletableFuture.supplyAsync(() -> {
            if (item == null || item.getItemMeta() == null) {
                return false;
            }
            
            try {
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                
                // Set provenance information
                container.set(PROVENANCE_KEY, PersistentDataType.STRING, "true");
                container.set(SOURCE_KEY, PersistentDataType.STRING, source.name());
                container.set(TIMESTAMP_KEY, PersistentDataType.LONG, System.currentTimeMillis());
                
                if (playerId != null) {
                    container.set(PLAYER_KEY, PersistentDataType.STRING, playerId.toString());
                }
                
                if (location != null) {
                    container.set(LOCATION_KEY, PersistentDataType.STRING, location);
                }
                
                item.setItemMeta(meta);
                
                // Log provenance for player history
                logItemProvenance(item, source, playerId, location);
                
                return true;
            } catch (Exception e) {
                // Log error but don't fail the operation
                System.err.println("Failed to tag item with provenance: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Get provenance information from an item
     */
    public CompletableFuture<ItemProvenance> getItemProvenance(ItemStack item) {
        return CompletableFuture.completedFuture(getItemProvenanceSync(item));
    }
    
    /**
     * Check if an item is eligible for collection progression
     * 
     * This is the core constraint logic that ensures only naturally
     * obtained items count towards collections.
     */
    public CompletableFuture<Boolean> isEligibleForCollections(ItemStack item) {
        try {
            ItemProvenance provenance = getItemProvenanceSync(item);
            if (provenance == null) {
                // Items without provenance are NOT eligible
                return CompletableFuture.completedFuture(false);
            }
            
            // Only items from allowed sources are eligible
            return CompletableFuture.completedFuture(provenance.getSource().isAllowedForCollections());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
    
    /**
     * Get provenance information from an item (synchronous version)
     */
    private ItemProvenance getItemProvenanceSync(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return null;
        }
        
        try {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            
            // Check if item has provenance data
            if (!container.has(PROVENANCE_KEY, PersistentDataType.STRING)) {
                return null;
            }
            
            // Extract provenance information
            String sourceStr = container.getOrDefault(SOURCE_KEY, PersistentDataType.STRING, CollectionSource.UNKNOWN.name());
            Long timestamp = container.getOrDefault(TIMESTAMP_KEY, PersistentDataType.LONG, 0L);
            String playerStr = container.getOrDefault(PLAYER_KEY, PersistentDataType.STRING, null);
            String location = container.getOrDefault(LOCATION_KEY, PersistentDataType.STRING, null);
            
            CollectionSource source = CollectionSource.getByName(sourceStr);
            if (source == null) {
                source = CollectionSource.UNKNOWN;
            }
            UUID playerId = playerStr != null ? UUID.fromString(playerStr) : null;
            
            return new ItemProvenance(source, timestamp, playerId, location);
            
        } catch (Exception e) {
            System.err.println("Failed to get item provenance: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Tag item from mining
     */
    public CompletableFuture<Boolean> tagMiningItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.MINING, playerId, location);
    }
    
    /**
     * Tag item from farming
     */
    public CompletableFuture<Boolean> tagFarmingItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.FARMING, playerId, location);
    }
    
    /**
     * Tag item from mob drop
     */
    public CompletableFuture<Boolean> tagMobDropItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.MOB_DROP, playerId, location);
    }
    
    /**
     * Tag item from minion
     */
    public CompletableFuture<Boolean> tagMinionItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.MINION, playerId, location);
    }
    
    /**
     * Tag item from fishing
     */
    public CompletableFuture<Boolean> tagFishingItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.FISHING, playerId, location);
    }
    
    /**
     * Tag item from foraging
     */
    public CompletableFuture<Boolean> tagForagingItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.FORAGING, playerId, location);
    }
    
    /**
     * Tag item from natural generation
     */
    public CompletableFuture<Boolean> tagNaturalGenerationItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.NATURAL_GENERATION, playerId, location);
    }
    
    /**
     * Tag item from auction house (NOT eligible for collections)
     */
    public CompletableFuture<Boolean> tagAuctionHouseItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.AUCTION_HOUSE, playerId, location);
    }
    
    /**
     * Tag item from bazaar (NOT eligible for collections)
     */
    public CompletableFuture<Boolean> tagBazaarItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.BAZAAR, playerId, location);
    }
    
    /**
     * Tag item from direct trade (NOT eligible for collections)
     */
    public CompletableFuture<Boolean> tagDirectTradeItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.DIRECT_TRADE, playerId, location);
    }
    
    /**
     * Tag item from gift (NOT eligible for collections)
     */
    public CompletableFuture<Boolean> tagGiftItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.GIFT, playerId, location);
    }
    
    /**
     * Tag item from admin give (NOT eligible for collections)
     */
    public CompletableFuture<Boolean> tagAdminGiveItem(ItemStack item, UUID playerId, String location) {
        return tagItem(item, CollectionSource.ADMIN_GIVE, playerId, location);
    }
    
    /**
     * Get player's item provenance history
     */
    public List<ItemProvenance> getPlayerItemHistory(UUID playerId) {
        return new ArrayList<>(playerItemHistory.getOrDefault(playerId, new ArrayList<>()));
    }
    
    /**
     * Get items by source for a player
     */
    public List<ItemProvenance> getPlayerItemsBySource(UUID playerId, CollectionSource source) {
        return playerItemHistory.getOrDefault(playerId, new ArrayList<>())
            .stream()
            .filter(provenance -> provenance.getSource() == source)
            .toList();
    }
    
    /**
     * Get collection-eligible items for a player
     */
    public List<ItemProvenance> getPlayerCollectionEligibleItems(UUID playerId) {
        return playerItemHistory.getOrDefault(playerId, new ArrayList<>())
            .stream()
            .filter(provenance -> provenance.getSource().isAllowedForCollections())
            .toList();
    }
    
    /**
     * Get trade items for a player (NOT eligible for collections)
     */
    public List<ItemProvenance> getPlayerTradeItems(UUID playerId) {
        return playerItemHistory.getOrDefault(playerId, new ArrayList<>())
            .stream()
            .filter(provenance -> !provenance.getSource().isAllowedForCollections())
            .toList();
    }
    
    /**
     * Log item provenance for player history
     */
    private void logItemProvenance(ItemStack item, CollectionSource source, UUID playerId, String location) {
        if (playerId == null) return;
        
        ItemProvenance provenance = new ItemProvenance(source, System.currentTimeMillis(), playerId, location);
        
        playerItemHistory.computeIfAbsent(playerId, k -> new ArrayList<>()).add(provenance);
        
        // Keep only last 1000 items per player to prevent memory issues
        List<ItemProvenance> history = playerItemHistory.get(playerId);
        if (history.size() > 1000) {
            history.subList(0, history.size() - 1000).clear();
        }
    }
    
    /**
     * Initialize provenance tracking
     */
    private void initializeProvenanceTracking() {
        // System is ready for tracking
        System.out.println("Item Provenance System initialized - Economic protection active");
    }
    
    /**
     * Save provenance data to persistent storage
     */
    private void saveProvenanceData() {
        // TODO: Implement database persistence for provenance data
        // This will integrate with the existing database system
    }
    
    /**
     * Validate item provenance integrity
     */
    public CompletableFuture<Boolean> validateItemProvenance(ItemStack item) {
        try {
            ItemProvenance provenance = getItemProvenanceSync(item);
            if (provenance == null) {
                return CompletableFuture.completedFuture(false); // Items without provenance are invalid
            }
            
            // Check if timestamp is reasonable (not in the future, not too old)
            long currentTime = System.currentTimeMillis();
            long itemTime = provenance.getTimestamp();
            
            // Allow items from up to 1 year ago
            long oneYearAgo = currentTime - (365L * 24 * 60 * 60 * 1000);
            
            return CompletableFuture.completedFuture(itemTime <= currentTime && itemTime >= oneYearAgo);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
    
    /**
     * Remove provenance from an item (for testing/admin purposes)
     */
    public CompletableFuture<Boolean> removeProvenance(ItemStack item) {
        return CompletableFuture.supplyAsync(() -> {
            if (item == null || item.getItemMeta() == null) {
                return false;
            }
            
            try {
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                
                // Remove all provenance data
                container.remove(PROVENANCE_KEY);
                container.remove(SOURCE_KEY);
                container.remove(TIMESTAMP_KEY);
                container.remove(PLAYER_KEY);
                container.remove(LOCATION_KEY);
                
                item.setItemMeta(meta);
                return true;
            } catch (Exception e) {
                System.err.println("Failed to remove item provenance: " + e.getMessage());
                return false;
            }
        });
    }
}
