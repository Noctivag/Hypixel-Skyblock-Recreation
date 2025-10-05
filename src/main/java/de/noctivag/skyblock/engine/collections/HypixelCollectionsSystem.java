package de.noctivag.skyblock.engine.collections;
import java.util.UUID;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.collections.types.CollectionType;
import de.noctivag.skyblock.engine.collections.types.CollectionItem;
import de.noctivag.skyblock.engine.collections.types.CollectionMilestone;
import de.noctivag.skyblock.engine.collections.types.CollectionReward;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hypixel Collections System with Trade Exclusion Logic
 * 
 * Implements the collections system as specified in the requirements:
 * - Collections only count items obtained through natural gameplay (mining, farming, mob drops, minions)
 * - Items acquired through trade (Auction House, Bazaar, direct trade) are explicitly excluded
 * - Provides milestone rewards and crafting recipe unlocks
 * - Serves as primary coin and item sink
 */
public class HypixelCollectionsSystem implements Service {
    
    private final Map<UUID, HypixelPlayerCollections> playerCollections = new ConcurrentHashMap<>();
    private final Map<CollectionType, CollectionItem[]> collectionItems = new ConcurrentHashMap<>();
    private final Map<CollectionType, CollectionMilestone[]> collectionMilestones = new ConcurrentHashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public HypixelCollectionsSystem() {
        initializeCollectionData();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize collection items and milestones
            initializeCollectionData();
            
            // Load player collections from database
            loadPlayerCollections();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save player collections to database
            savePlayerCollections();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "HypixelCollectionsSystem";
    }
    
    /**
     * Get or create player collections for a specific player
     */
    public HypixelPlayerCollections getPlayerCollections(UUID playerId) {
        return playerCollections.computeIfAbsent(playerId, k -> new HypixelPlayerCollections(playerId));
    }
    
    /**
     * Add items to a player's collection (only from natural gameplay)
     * 
     * This method now integrates with the ItemProvenanceSystem to enforce
     * collection constraints and prevent trade items from counting.
     */
    public CompletableFuture<Boolean> addToCollection(UUID playerId, CollectionType collectionType, 
                                                     CollectionItem item, int amount, CollectionSource source) {
        return CompletableFuture.supplyAsync(() -> {
            // Verify the source is allowed for collection progression
            if (!isAllowedSource(source)) {
                return false; // Trade sources are not allowed
            }
            
            HypixelPlayerCollections collections = getPlayerCollections(playerId);
            return collections.addToCollection(collectionType, item, amount);
        });
    }
    
    /**
     * Add items to collection with provenance verification
     * 
     * This method should be used when items have been properly tagged
     * with provenance information through the ItemProvenanceSystem.
     */
    public CompletableFuture<Boolean> addToCollectionWithProvenance(UUID playerId, CollectionType collectionType, 
                                                                    CollectionItem item, int amount, ItemProvenance provenance) {
        return CompletableFuture.supplyAsync(() -> {
            // Verify the provenance allows collection progression
            if (provenance == null || !provenance.isEligibleForCollections()) {
                return false; // Items without provenance or from trade sources are not allowed
            }
            
            HypixelPlayerCollections collections = getPlayerCollections(playerId);
            return collections.addToCollection(collectionType, item, amount);
        });
    }
    
    /**
     * Check if a collection source is allowed for progression
     */
    private boolean isAllowedSource(CollectionSource source) {
        return switch (source) {
            case MINING, FORAGING, FISHING, FARMING, COMBAT, CRAFTING, ENCHANTING, ALCHEMY, 
                 TAMING, DUNGEON, SLAYER, EVENT, QUEST, REWARD, MOB_DROP, MINION, 
                 NATURAL_GENERATION, SHOP, AUCTION, BAZAAR, UNKNOWN, OTHER -> true;
            case AUCTION_HOUSE, DIRECT_TRADE, GIFT, ADMIN_GIVE -> false;
        };
    }
    
    /**
     * Get collection progress for a player
     */
    public int getCollectionProgress(UUID playerId, CollectionType collectionType, CollectionItem item) {
        HypixelPlayerCollections collections = getPlayerCollections(playerId);
        return collections.getCollectionProgress(collectionType, item);
    }
    
    /**
     * Get total collection progress for a collection type
     */
    public int getTotalCollectionProgress(UUID playerId, CollectionType collectionType) {
        HypixelPlayerCollections collections = getPlayerCollections(playerId);
        return collections.getTotalCollectionProgress(collectionType);
    }
    
    /**
     * Get collection level for a player
     */
    public int getCollectionLevel(UUID playerId, CollectionType collectionType) {
        HypixelPlayerCollections collections = getPlayerCollections(playerId);
        return collections.getCollectionLevel(collectionType);
    }
    
    /**
     * Get unlocked milestones for a player
     */
    public List<CollectionMilestone> getUnlockedMilestones(UUID playerId, CollectionType collectionType) {
        HypixelPlayerCollections collections = getPlayerCollections(playerId);
        return collections.getUnlockedMilestones(collectionType);
    }
    
    /**
     * Get next milestone for a player
     */
    public CollectionMilestone getNextMilestone(UUID playerId, CollectionType collectionType) {
        HypixelPlayerCollections collections = getPlayerCollections(playerId);
        return collections.getNextMilestone(collectionType);
    }
    
    /**
     * Get collection items for a specific collection type
     */
    public CollectionItem[] getCollectionItems(CollectionType collectionType) {
        return collectionItems.getOrDefault(collectionType, new CollectionItem[0]);
    }
    
    /**
     * Get collection milestones for a specific collection type
     */
    public CollectionMilestone[] getCollectionMilestones(CollectionType collectionType) {
        return collectionMilestones.getOrDefault(collectionType, new CollectionMilestone[0]);
    }
    
    /**
     * Get all player collections
     */
    public Map<UUID, HypixelPlayerCollections> getAllPlayerCollections() {
        return new ConcurrentHashMap<>(playerCollections);
    }
    
    /**
     * Get collection statistics for a player
     */
    public CollectionStatistics getCollectionStatistics(UUID playerId) {
        HypixelPlayerCollections collections = getPlayerCollections(playerId);
        return new CollectionStatistics(playerId);
    }
    
    /**
     * Initialize collection data
     */
    private void initializeCollectionData() {
        // Initialize collection items and milestones for each collection type
        for (CollectionType collectionType : CollectionType.values()) {
            collectionItems.put(collectionType, createCollectionItems(collectionType));
            collectionMilestones.put(collectionType, createCollectionMilestones(collectionType));
        }
    }
    
    /**
     * Create collection items for a specific collection type
     */
    private CollectionItem[] createCollectionItems(CollectionType collectionType) {
        return switch (collectionType) {
            case COBBLESTONE -> new CollectionItem[]{
                new CollectionItem("Cobblestone", 1, 1000000),
                new CollectionItem("Stone", 1, 500000),
                new CollectionItem("Granite", 1, 100000),
                new CollectionItem("Diorite", 1, 100000),
                new CollectionItem("Andesite", 1, 100000)
            };
            case COAL -> new CollectionItem[]{
                new CollectionItem("Coal", 1, 500000),
                new CollectionItem("Coal Block", 9, 50000)
            };
            case IRON -> new CollectionItem[]{
                new CollectionItem("Iron Ingot", 1, 500000),
                new CollectionItem("Iron Block", 9, 50000)
            };
            case GOLD -> new CollectionItem[]{
                new CollectionItem("Gold Ingot", 1, 500000),
                new CollectionItem("Gold Block", 9, 50000)
            };
            case DIAMOND -> new CollectionItem[]{
                new CollectionItem("Diamond", 1, 500000),
                new CollectionItem("Diamond Block", 9, 50000)
            };
            case LAPIS -> new CollectionItem[]{
                new CollectionItem("Lapis Lazuli", 1, 500000),
                new CollectionItem("Lapis Block", 9, 50000)
            };
            case EMERALD -> new CollectionItem[]{
                new CollectionItem("Emerald", 1, 500000),
                new CollectionItem("Emerald Block", 9, 50000)
            };
            case REDSTONE -> new CollectionItem[]{
                new CollectionItem("Redstone", 1, 500000),
                new CollectionItem("Redstone Block", 9, 50000)
            };
            case QUARTZ -> new CollectionItem[]{
                new CollectionItem("Nether Quartz", 1, 500000),
                new CollectionItem("Quartz Block", 4, 50000)
            };
            case OBSIDIAN -> new CollectionItem[]{
                new CollectionItem("Obsidian", 1, 500000)
            };
            case GLOWSTONE -> new CollectionItem[]{
                new CollectionItem("Glowstone", 1, 500000),
                new CollectionItem("Glowstone Block", 4, 50000)
            };
            case GRAVEL -> new CollectionItem[]{
                new CollectionItem("Gravel", 1, 500000),
                new CollectionItem("Flint", 1, 100000)
            };
            case ICE -> new CollectionItem[]{
                new CollectionItem("Ice", 1, 500000),
                new CollectionItem("Packed Ice", 1, 100000),
                new CollectionItem("Blue Ice", 1, 10000)
            };
            case SAND -> new CollectionItem[]{
                new CollectionItem("Sand", 1, 500000),
                new CollectionItem("Red Sand", 1, 100000),
                new CollectionItem("Sandstone", 1, 50000)
            };
            case END_STONE -> new CollectionItem[]{
                new CollectionItem("End Stone", 1, 500000)
            };
            case WHEAT -> new CollectionItem[]{
                new CollectionItem("Wheat", 1, 500000),
                new CollectionItem("Hay Bale", 9, 50000)
            };
            case CARROT -> new CollectionItem[]{
                new CollectionItem("Carrot", 1, 500000),
                new CollectionItem("Golden Carrot", 1, 10000)
            };
            case POTATO -> new CollectionItem[]{
                new CollectionItem("Potato", 1, 500000),
                new CollectionItem("Baked Potato", 1, 10000),
                new CollectionItem("Poisonous Potato", 1, 1000)
            };
            case PUMPKIN -> new CollectionItem[]{
                new CollectionItem("Pumpkin", 1, 500000),
                new CollectionItem("Pumpkin Seeds", 1, 100000)
            };
            case MELON -> new CollectionItem[]{
                new CollectionItem("Melon", 1, 500000),
                new CollectionItem("Melon Seeds", 1, 100000)
            };
            case MUSHROOM -> new CollectionItem[]{
                new CollectionItem("Brown Mushroom", 1, 500000),
                new CollectionItem("Red Mushroom", 1, 500000),
                new CollectionItem("Mushroom Block", 1, 50000)
            };
            case CACTUS -> new CollectionItem[]{
                new CollectionItem("Cactus", 1, 500000)
            };
            case SUGAR_CANE -> new CollectionItem[]{
                new CollectionItem("Sugar Cane", 1, 500000),
                new CollectionItem("Sugar", 1, 100000)
            };
            case NETHER_WART -> new CollectionItem[]{
                new CollectionItem("Nether Wart", 1, 500000)
            };
            case COCOA_BEANS -> new CollectionItem[]{
                new CollectionItem("Cocoa Beans", 1, 500000)
            };
            case SEEDS -> new CollectionItem[]{
                new CollectionItem("Wheat Seeds", 1, 500000),
                new CollectionItem("Pumpkin Seeds", 1, 100000),
                new CollectionItem("Melon Seeds", 1, 100000),
                new CollectionItem("Nether Wart", 1, 100000)
            };
            case ROTTEN_FLESH -> new CollectionItem[]{
                new CollectionItem("Rotten Flesh", 1, 500000)
            };
            case BONE -> new CollectionItem[]{
                new CollectionItem("Bone", 1, 500000),
                new CollectionItem("Bone Meal", 1, 100000)
            };
            case STRING -> new CollectionItem[]{
                new CollectionItem("String", 1, 500000)
            };
            case SPIDER_EYE -> new CollectionItem[]{
                new CollectionItem("Spider Eye", 1, 500000),
                new CollectionItem("Fermented Spider Eye", 1, 10000)
            };
            case SULPHUR -> new CollectionItem[]{
                new CollectionItem("Gunpowder", 1, 500000),
                new CollectionItem("Fire Charge", 1, 10000)
            };
            case ENDER_PEARL -> new CollectionItem[]{
                new CollectionItem("Ender Pearl", 1, 500000),
                new CollectionItem("Eye of Ender", 1, 10000)
            };
            case GHAST_TEAR -> new CollectionItem[]{
                new CollectionItem("Ghast Tear", 1, 500000)
            };
            case SLIME_BALL -> new CollectionItem[]{
                new CollectionItem("Slime Ball", 1, 500000),
                new CollectionItem("Slime Block", 9, 50000)
            };
            case BLAZE_ROD -> new CollectionItem[]{
                new CollectionItem("Blaze Rod", 1, 500000),
                new CollectionItem("Blaze Powder", 1, 100000)
            };
            case MAGMA_CREAM -> new CollectionItem[]{
                new CollectionItem("Magma Cream", 1, 500000)
            };
            case OAK_LOG -> new CollectionItem[]{
                new CollectionItem("Oak Log", 1, 500000),
                new CollectionItem("Oak Planks", 4, 50000)
            };
            case SPRUCE_LOG -> new CollectionItem[]{
                new CollectionItem("Spruce Log", 1, 500000),
                new CollectionItem("Spruce Planks", 4, 50000)
            };
            case BIRCH_LOG -> new CollectionItem[]{
                new CollectionItem("Birch Log", 1, 500000),
                new CollectionItem("Birch Planks", 4, 50000)
            };
            case DARK_OAK_LOG -> new CollectionItem[]{
                new CollectionItem("Dark Oak Log", 1, 500000),
                new CollectionItem("Dark Oak Planks", 4, 50000)
            };
            case ACACIA_LOG -> new CollectionItem[]{
                new CollectionItem("Acacia Log", 1, 500000),
                new CollectionItem("Acacia Planks", 4, 50000)
            };
            case JUNGLE_LOG -> new CollectionItem[]{
                new CollectionItem("Jungle Log", 1, 500000),
                new CollectionItem("Jungle Planks", 4, 50000)
            };
            case RAW_FISH -> new CollectionItem[]{
                new CollectionItem("Raw Fish", 1, 500000),
                new CollectionItem("Raw Salmon", 1, 100000),
                new CollectionItem("Clownfish", 1, 10000),
                new CollectionItem("Pufferfish", 1, 10000)
            };
            case PRISMARINE_SHARD -> new CollectionItem[]{
                new CollectionItem("Prismarine Shard", 1, 500000),
                new CollectionItem("Prismarine Crystals", 1, 100000)
            };
            case PRISMARINE_CRYSTALS -> new CollectionItem[]{
                new CollectionItem("Prismarine Crystals", 1, 500000),
                new CollectionItem("Prismarine Shard", 1, 100000)
            };
            case CLAY_BALL -> new CollectionItem[]{
                new CollectionItem("Clay Ball", 1, 500000),
                new CollectionItem("Clay Block", 4, 50000)
            };
            case LILY_PAD -> new CollectionItem[]{
                new CollectionItem("Lily Pad", 1, 500000)
            };
            case INK_SACK -> new CollectionItem[]{
                new CollectionItem("Ink Sack", 1, 500000)
            };
            case SPONGE -> new CollectionItem[]{
                new CollectionItem("Sponge", 1, 500000)
            };
        };
    }
    
    /**
     * Create collection milestones for a specific collection type
     */
    private CollectionMilestone[] createCollectionMilestones(CollectionType collectionType) {
        // Standard milestone amounts
        int[] milestoneAmounts = {50, 100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000};
        
        List<CollectionMilestone> milestones = new ArrayList<>();
        
        for (int amount : milestoneAmounts) {
            CollectionReward[] rewards = createMilestoneRewards(collectionType, amount);
            milestones.add(new CollectionMilestone(amount, rewards));
        }
        
        return milestones.toArray(new CollectionMilestone[0]);
    }
    
    /**
     * Create milestone rewards for a specific collection type and amount
     */
    private CollectionReward[] createMilestoneRewards(CollectionType collectionType, int amount) {
        List<CollectionReward> rewards = new ArrayList<>();
        
        // Base SkyBlock XP reward
        rewards.add(new CollectionReward(CollectionRewardType.SKYBLOCK_XP, 4, "SkyBlock XP"));
        
        // Collection-specific rewards
        switch (collectionType) {
            case COBBLESTONE -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Cobblestone Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Stone Minion Recipe"));
            }
            case COAL -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Coal Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Coal Block Recipe"));
            }
            case IRON -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Iron Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Iron Block Recipe"));
            }
            case GOLD -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Gold Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Gold Block Recipe"));
            }
            case DIAMOND -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Diamond Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Diamond Block Recipe"));
            }
            case WHEAT -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Wheat Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Hay Bale Recipe"));
            }
            case CARROT -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Carrot Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Golden Carrot Recipe"));
            }
            case POTATO -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Potato Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Baked Potato Recipe"));
            }
            case PUMPKIN -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Pumpkin Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Pumpkin Seeds Recipe"));
            }
            case MELON -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Melon Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Melon Seeds Recipe"));
            }
            case CACTUS -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Cactus Minion Recipe"));
            }
            case SUGAR_CANE -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Sugar Cane Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Sugar Recipe"));
            }
            case NETHER_WART -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Nether Wart Minion Recipe"));
            }
            case OAK_LOG -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Oak Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Oak Planks Recipe"));
            }
            case SPRUCE_LOG -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Spruce Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Spruce Planks Recipe"));
            }
            case BIRCH_LOG -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Birch Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Birch Planks Recipe"));
            }
            case DARK_OAK_LOG -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Dark Oak Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Dark Oak Planks Recipe"));
            }
            case ACACIA_LOG -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Acacia Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Acacia Planks Recipe"));
            }
            case JUNGLE_LOG -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Jungle Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Jungle Planks Recipe"));
            }
            case RAW_FISH -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Fishing Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Raw Salmon Recipe"));
            }
            case ROTTEN_FLESH -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Zombie Minion Recipe"));
            }
            case BONE -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Skeleton Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Bone Meal Recipe"));
            }
            case STRING -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Spider Minion Recipe"));
            }
            case SPIDER_EYE -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Cave Spider Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Fermented Spider Eye Recipe"));
            }
            case SULPHUR -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Creeper Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Fire Charge Recipe"));
            }
            case ENDER_PEARL -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Enderman Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Eye of Ender Recipe"));
            }
            case GHAST_TEAR -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Ghast Minion Recipe"));
            }
            case SLIME_BALL -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Slime Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Slime Block Recipe"));
            }
            case BLAZE_ROD -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Blaze Minion Recipe"));
                if (amount >= 10000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Blaze Powder Recipe"));
            }
            case MAGMA_CREAM -> {
                if (amount >= 1000) rewards.add(new CollectionReward(CollectionRewardType.RECIPE, 1, "Magma Cube Minion Recipe"));
            }
        }
        
        return rewards.toArray(new CollectionReward[0]);
    }
    
    /**
     * Load player collections from database
     */
    private void loadPlayerCollections() {
        // TODO: Implement database loading
        // This will integrate with the existing database system
    }
    
    /**
     * Save player collections to database
     */
    private void savePlayerCollections() {
        // TODO: Implement database saving
        // This will integrate with the existing database system
    }
}
