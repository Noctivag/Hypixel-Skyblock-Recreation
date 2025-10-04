package de.noctivag.skyblock.engine.collections.types;

import java.util.Arrays;

/**
 * Collection Types in Hypixel Skyblock
 * 
 * Defines all collection categories with their specific characteristics.
 * Each collection type has different items and milestone requirements.
 */
public enum CollectionType {
    // Mining Collections
    COBBLESTONE("Cobblestone", "⛏️", "Mining cobblestone and stone variants", CollectionCategory.MINING),
    COAL("Coal", "⚫", "Mining coal and coal blocks", CollectionCategory.MINING),
    IRON("Iron", "⚙️", "Mining iron ore and iron blocks", CollectionCategory.MINING),
    GOLD("Gold", "⚱️", "Mining gold ore and gold blocks", CollectionCategory.MINING),
    DIAMOND("Diamond", "💎", "Mining diamonds and diamond blocks", CollectionCategory.MINING),
    LAPIS("Lapis Lazuli", "🔵", "Mining lapis lazuli and lapis blocks", CollectionCategory.MINING),
    EMERALD("Emerald", "💚", "Mining emeralds and emerald blocks", CollectionCategory.MINING),
    REDSTONE("Redstone", "🔴", "Mining redstone and redstone blocks", CollectionCategory.MINING),
    QUARTZ("Nether Quartz", "⚪", "Mining nether quartz and quartz blocks", CollectionCategory.MINING),
    OBSIDIAN("Obsidian", "🖤", "Mining obsidian", CollectionCategory.MINING),
    GLOWSTONE("Glowstone", "💡", "Mining glowstone and glowstone blocks", CollectionCategory.MINING),
    GRAVEL("Gravel", "⚫", "Mining gravel and flint", CollectionCategory.MINING),
    ICE("Ice", "🧊", "Mining ice variants", CollectionCategory.MINING),
    SAND("Sand", "🏖️", "Mining sand and sandstone", CollectionCategory.MINING),
    END_STONE("End Stone", "🌌", "Mining end stone", CollectionCategory.MINING),
    
    // Farming Collections
    WHEAT("Wheat", "🌾", "Growing and harvesting wheat", CollectionCategory.FARMING),
    CARROT("Carrot", "🥕", "Growing and harvesting carrots", CollectionCategory.FARMING),
    POTATO("Potato", "🥔", "Growing and harvesting potatoes", CollectionCategory.FARMING),
    PUMPKIN("Pumpkin", "🎃", "Growing and harvesting pumpkins", CollectionCategory.FARMING),
    MELON("Melon", "🍉", "Growing and harvesting melons", CollectionCategory.FARMING),
    MUSHROOM("Mushroom", "🍄", "Growing and harvesting mushrooms", CollectionCategory.FARMING),
    CACTUS("Cactus", "🌵", "Growing and harvesting cacti", CollectionCategory.FARMING),
    SUGAR_CANE("Sugar Cane", "🎋", "Growing and harvesting sugar cane", CollectionCategory.FARMING),
    NETHER_WART("Nether Wart", "🌿", "Growing and harvesting nether wart", CollectionCategory.FARMING),
    COCOA_BEANS("Cocoa Beans", "🫘", "Growing and harvesting cocoa beans", CollectionCategory.FARMING),
    SEEDS("Seeds", "🌱", "Growing and harvesting various seeds", CollectionCategory.FARMING),
    
    // Combat Collections
    ROTTEN_FLESH("Rotten Flesh", "🧟", "Killing zombies and zombie variants", CollectionCategory.COMBAT),
    BONE("Bone", "🦴", "Killing skeletons and skeleton variants", CollectionCategory.COMBAT),
    STRING("String", "🕸️", "Killing spiders and spider variants", CollectionCategory.COMBAT),
    SPIDER_EYE("Spider Eye", "👁️", "Killing spiders and cave spiders", CollectionCategory.COMBAT),
    SULPHUR("Sulphur", "💥", "Killing creepers", CollectionCategory.COMBAT),
    ENDER_PEARL("Ender Pearl", "🔮", "Killing endermen and enderman variants", CollectionCategory.COMBAT),
    GHAST_TEAR("Ghast Tear", "👻", "Killing ghasts", CollectionCategory.COMBAT),
    SLIME_BALL("Slime Ball", "🟢", "Killing slimes and slime variants", CollectionCategory.COMBAT),
    BLAZE_ROD("Blaze Rod", "🔥", "Killing blazes", CollectionCategory.COMBAT),
    MAGMA_CREAM("Magma Cream", "🌋", "Killing magma cubes", CollectionCategory.COMBAT),
    
    // Foraging Collections
    OAK_LOG("Oak Log", "🌳", "Chopping oak trees", CollectionCategory.FORAGING),
    SPRUCE_LOG("Spruce Log", "🌲", "Chopping spruce trees", CollectionCategory.FORAGING),
    BIRCH_LOG("Birch Log", "🌿", "Chopping birch trees", CollectionCategory.FORAGING),
    DARK_OAK_LOG("Dark Oak Log", "🌰", "Chopping dark oak trees", CollectionCategory.FORAGING),
    ACACIA_LOG("Acacia Log", "🌴", "Chopping acacia trees", CollectionCategory.FORAGING),
    JUNGLE_LOG("Jungle Log", "🌴", "Chopping jungle trees", CollectionCategory.FORAGING),
    
    // Fishing Collections
    RAW_FISH("Raw Fish", "🐟", "Fishing for fish and sea creatures", CollectionCategory.FISHING),
    PRISMARINE_SHARD("Prismarine Shard", "🔷", "Fishing for prismarine shards", CollectionCategory.FISHING),
    PRISMARINE_CRYSTALS("Prismarine Crystals", "💎", "Fishing for prismarine crystals", CollectionCategory.FISHING),
    CLAY_BALL("Clay Ball", "🏺", "Fishing for clay", CollectionCategory.FISHING),
    LILY_PAD("Lily Pad", "🪷", "Fishing for lily pads", CollectionCategory.FISHING),
    INK_SACK("Ink Sack", "🖤", "Fishing for ink sacks", CollectionCategory.FISHING),
    SPONGE("Sponge", "🧽", "Fishing for sponges", CollectionCategory.FISHING);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final CollectionCategory category;
    
    CollectionType(String displayName, String icon, String description, CollectionCategory category) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.category = category;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
    
    public CollectionCategory getCategory() {
        return category;
    }
    
    /**
     * Get collections by category
     */
    public static CollectionType[] getByCategory(CollectionCategory category) {
        return Arrays.stream(values())
            .filter(collection -> collection.getCategory() == category)
            .toArray(CollectionType[]::new);
    }
    
    /**
     * Get all mining collections
     */
    public static CollectionType[] getMiningCollections() {
        return getByCategory(CollectionCategory.MINING);
    }
    
    /**
     * Get all farming collections
     */
    public static CollectionType[] getFarmingCollections() {
        return getByCategory(CollectionCategory.FARMING);
    }
    
    /**
     * Get all combat collections
     */
    public static CollectionType[] getCombatCollections() {
        return getByCategory(CollectionCategory.COMBAT);
    }
    
    /**
     * Get all foraging collections
     */
    public static CollectionType[] getForagingCollections() {
        return getByCategory(CollectionCategory.FORAGING);
    }
    
    /**
     * Get all fishing collections
     */
    public static CollectionType[] getFishingCollections() {
        return getByCategory(CollectionCategory.FISHING);
    }
    
    /**
     * Get collection weight for collection power calculation
     */
    public double getWeight() {
        return switch (this) {
            case DIAMOND, EMERALD -> 2.0; // Most valuable
            case GOLD, IRON -> 1.5; // Very valuable
            case COAL, LAPIS, REDSTONE, QUARTZ -> 1.2; // Moderately valuable
            case COBBLESTONE, GRAVEL, SAND -> 0.8; // Less valuable
            case OBSIDIAN, END_STONE -> 1.0; // Standard
            case GLOWSTONE, ICE -> 0.9; // Slightly less valuable
            case WHEAT, CARROT, POTATO -> 1.0; // Standard farming
            case PUMPKIN, MELON -> 1.1; // Slightly more valuable
            case MUSHROOM, CACTUS -> 0.9; // Slightly less valuable
            case SUGAR_CANE, NETHER_WART -> 1.2; // More valuable
            case COCOA_BEANS, SEEDS -> 0.8; // Less valuable
            case ENDER_PEARL, GHAST_TEAR -> 2.0; // Most valuable combat
            case BLAZE_ROD, MAGMA_CREAM -> 1.5; // Very valuable combat
            case BONE, STRING, SPIDER_EYE -> 1.0; // Standard combat
            case ROTTEN_FLESH, SULPHUR -> 0.8; // Less valuable combat
            case SLIME_BALL -> 1.2; // Moderately valuable combat
            case OAK_LOG, SPRUCE_LOG, BIRCH_LOG -> 1.0; // Standard foraging
            case DARK_OAK_LOG, ACACIA_LOG, JUNGLE_LOG -> 1.1; // Slightly more valuable
            case RAW_FISH -> 1.0; // Standard fishing
            case PRISMARINE_SHARD, PRISMARINE_CRYSTALS -> 1.5; // More valuable fishing
            case CLAY_BALL, LILY_PAD, INK_SACK -> 0.9; // Slightly less valuable fishing
            case SPONGE -> 1.2; // Moderately valuable fishing
        };
    }
    
    /**
     * Get collection difficulty (affects XP gain rate)
     */
    public double getDifficulty() {
        return switch (this) {
            case COBBLESTONE, GRAVEL, SAND -> 0.5; // Very easy
            case COAL, IRON, WHEAT, CARROT, POTATO -> 0.7; // Easy
            case GOLD, LAPIS, REDSTONE, PUMPKIN, MELON -> 0.8; // Moderate
            case DIAMOND, EMERALD, QUARTZ, SUGAR_CANE, NETHER_WART -> 1.0; // Standard
            case OBSIDIAN, END_STONE, BLAZE_ROD, MAGMA_CREAM -> 1.2; // Hard
            case ENDER_PEARL, GHAST_TEAR -> 1.5; // Very hard
            case RAW_FISH, PRISMARINE_SHARD, PRISMARINE_CRYSTALS -> 1.3; // Hard fishing
            default -> 1.0; // Standard difficulty
        };
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
    
    /**
     * Collection categories for organization
     */
    public enum CollectionCategory {
        MINING("Mining"),
        FARMING("Farming"),
        COMBAT("Combat"),
        FORAGING("Foraging"),
        FISHING("Fishing");
        
        private final String displayName;
        
        CollectionCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
