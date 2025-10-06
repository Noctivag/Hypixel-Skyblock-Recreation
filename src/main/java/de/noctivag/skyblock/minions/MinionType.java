package de.noctivag.skyblock.minions;

import org.bukkit.Material;

/**
 * Enum representing all minion types in Hypixel Skyblock
 */
public enum MinionType {
    // Mining Minions
    COBBLESTONE_MINION("Cobblestone Minion", "&7", "⛏", "Mining", Material.COBBLESTONE, 1, 0.5),
    COAL_MINION("Coal Minion", "&8", "⛏", "Mining", Material.COAL, 1, 0.5),
    IRON_MINION("Iron Minion", "&f", "⛏", "Mining", Material.IRON_INGOT, 1, 0.5),
    GOLD_MINION("Gold Minion", "&6", "⛏", "Mining", Material.GOLD_INGOT, 1, 0.5),
    DIAMOND_MINION("Diamond Minion", "&b", "⛏", "Mining", Material.DIAMOND, 1, 0.5),
    LAPIS_MINION("Lapis Minion", "&9", "⛏", "Mining", Material.LAPIS_LAZULI, 1, 0.5),
    EMERALD_MINION("Emerald Minion", "&a", "⛏", "Mining", Material.EMERALD, 1, 0.5),
    REDSTONE_MINION("Redstone Minion", "&c", "⛏", "Mining", Material.REDSTONE, 1, 0.5),
    QUARTZ_MINION("Quartz Minion", "&f", "⛏", "Mining", Material.QUARTZ, 1, 0.5),
    OBSIDIAN_MINION("Obsidian Minion", "&5", "⛏", "Mining", Material.OBSIDIAN, 1, 0.5),
    GLOWSTONE_MINION("Glowstone Minion", "&e", "⛏", "Mining", Material.GLOWSTONE, 1, 0.5),
    GRAVEL_MINION("Gravel Minion", "&7", "⛏", "Mining", Material.GRAVEL, 1, 0.5),
    SAND_MINION("Sand Minion", "&e", "⛏", "Mining", Material.SAND, 1, 0.5),
    END_STONE_MINION("End Stone Minion", "&f", "⛏", "Mining", Material.END_STONE, 1, 0.5),
    SNOW_MINION("Snow Minion", "&f", "⛏", "Mining", Material.SNOW_BLOCK, 1, 0.5),
    CLAY_MINION("Clay Minion", "&9", "⛏", "Mining", Material.CLAY, 1, 0.5),
    
    // Farming Minions
    WHEAT_MINION("Wheat Minion", "&e", "🌾", "Farming", Material.WHEAT, 1, 0.5),
    CARROT_MINION("Carrot Minion", "&6", "🌾", "Farming", Material.CARROT, 1, 0.5),
    POTATO_MINION("Potato Minion", "&e", "🌾", "Farming", Material.POTATO, 1, 0.5),
    PUMPKIN_MINION("Pumpkin Minion", "&6", "🌾", "Farming", Material.PUMPKIN, 1, 0.5),
    MELON_MINION("Melon Minion", "&a", "🌾", "Farming", Material.MELON, 1, 0.5),
    MUSHROOM_MINION("Mushroom Minion", "&c", "🌾", "Farming", Material.RED_MUSHROOM, 1, 0.5),
    CACTUS_MINION("Cactus Minion", "&a", "🌾", "Farming", Material.CACTUS, 1, 0.5),
    SUGAR_CANE_MINION("Sugar Cane Minion", "&a", "🌾", "Farming", Material.SUGAR_CANE, 1, 0.5),
    NETHER_WART_MINION("Nether Wart Minion", "&c", "🌾", "Farming", Material.NETHER_WART, 1, 0.5),
    COCOA_MINION("Cocoa Minion", "&6", "🌾", "Farming", Material.COCOA_BEANS, 1, 0.5),
    
    // Foraging Minions
    OAK_MINION("Oak Minion", "&6", "🌲", "Foraging", Material.OAK_LOG, 1, 0.5),
    BIRCH_MINION("Birch Minion", "&f", "🌲", "Foraging", Material.BIRCH_LOG, 1, 0.5),
    SPRUCE_MINION("Spruce Minion", "&8", "🌲", "Foraging", Material.SPRUCE_LOG, 1, 0.5),
    JUNGLE_MINION("Jungle Minion", "&a", "🌲", "Foraging", Material.JUNGLE_LOG, 1, 0.5),
    ACACIA_MINION("Acacia Minion", "&c", "🌲", "Foraging", Material.ACACIA_LOG, 1, 0.5),
    DARK_OAK_MINION("Dark Oak Minion", "&8", "🌲", "Foraging", Material.DARK_OAK_LOG, 1, 0.5),
    MANGROVE_MINION("Mangrove Minion", "&6", "🌲", "Foraging", Material.MANGROVE_LOG, 1, 0.5),
    CHERRY_MINION("Cherry Minion", "&d", "🌲", "Foraging", Material.CHERRY_LOG, 1, 0.5),
    
    // Fishing Minions
    FISHING_MINION("Fishing Minion", "&9", "🎣", "Fishing", Material.COD, 1, 0.5),
    
    // Combat Minions
    ZOMBIE_MINION("Zombie Minion", "&8", "⚔", "Combat", Material.ROTTEN_FLESH, 1, 0.5),
    SKELETON_MINION("Skeleton Minion", "&f", "⚔", "Combat", Material.BONE, 1, 0.5),
    SPIDER_MINION("Spider Minion", "&8", "⚔", "Combat", Material.STRING, 1, 0.5),
    CREEPER_MINION("Creeper Minion", "&a", "⚔", "Combat", Material.GUNPOWDER, 1, 0.5),
    ENDERMAN_MINION("Enderman Minion", "&5", "⚔", "Combat", Material.ENDER_PEARL, 1, 0.5),
    BLAZE_MINION("Blaze Minion", "&6", "⚔", "Combat", Material.BLAZE_ROD, 1, 0.5),
    GHAST_MINION("Ghast Minion", "&f", "⚔", "Combat", Material.GHAST_TEAR, 1, 0.5),
    SLIME_MINION("Slime Minion", "&a", "⚔", "Combat", Material.SLIME_BALL, 1, 0.5),
    MAGMA_CUBE_MINION("Magma Cube Minion", "&6", "⚔", "Combat", Material.MAGMA_CREAM, 1, 0.5),
    
    // Special Minions
    FLOWER_MINION("Flower Minion", "&d", "🌸", "Special", Material.POPPY, 1, 0.5),
    CHICKEN_MINION("Chicken Minion", "&e", "🐔", "Special", Material.FEATHER, 1, 0.5),
    COW_MINION("Cow Minion", "&6", "🐄", "Special", Material.LEATHER, 1, 0.5),
    PIG_MINION("Pig Minion", "&d", "🐷", "Special", Material.PORKCHOP, 1, 0.5),
    SHEEP_MINION("Sheep Minion", "&f", "🐑", "Special", Material.WHITE_WOOL, 1, 0.5),
    RABBIT_MINION("Rabbit Minion", "&e", "🐰", "Special", Material.RABBIT_FOOT, 1, 0.5);

    private final String displayName;
    private final String color;
    private final String icon;
    private final String category;
    private final Material material;
    private final int baseStorage;
    private final double baseSpeed;

    MinionType(String displayName, String color, String icon, String category, Material material, int baseStorage, double baseSpeed) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.category = category;
        this.material = material;
        this.baseStorage = baseStorage;
        this.baseSpeed = baseSpeed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    public String getCategory() {
        return category;
    }

    public Material getMaterial() {
        return material;
    }

    public int getBaseStorage() {
        return baseStorage;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    /**
     * Get minion type by material
     */
    public static MinionType getByMaterial(Material material) {
        for (MinionType type : values()) {
            if (type.getMaterial() == material) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get all minions by category
     */
    public static MinionType[] getByCategory(String category) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getCategory().equalsIgnoreCase(category))
                .toArray(MinionType[]::new);
    }

    /**
     * Get minion tier information
     */
    public MinionTier getTier(int level) {
        return new MinionTier(this, level);
    }

    /**
     * Get max tier for this minion type
     */
    public int getMaxTier() {
        return 12; // All minions go up to tier XII
    }

    /**
     * Get action time for a specific tier
     */
    public double getActionTime(int tier) {
        // Base action time decreases with each tier
        double baseTime = 26.0; // Base time in seconds
        double reduction = (tier - 1) * 0.1; // 10% reduction per tier
        return Math.max(1.0, baseTime - reduction);
    }

    /**
     * Get storage capacity for a specific tier
     */
    public int getStorageCapacity(int tier) {
        // Storage increases with each tier
        return baseStorage + (tier - 1) * 3; // +3 storage per tier
    }

    /**
     * Get fuel slots for a specific tier
     */
    public int getFuelSlots(int tier) {
        if (tier >= 6) return 2; // Tier VI+ gets 2 fuel slots
        return 1; // Lower tiers get 1 fuel slot
    }

    /**
     * Get upgrade slots for a specific tier
     */
    public int getUpgradeSlots(int tier) {
        if (tier >= 8) return 2; // Tier VIII+ gets 2 upgrade slots
        return 1; // Lower tiers get 1 upgrade slot
    }

    /**
     * Get minion slots required for this tier
     */
    public int getMinionSlotsRequired(int tier) {
        if (tier <= 5) return 1;
        if (tier <= 7) return 2;
        if (tier <= 9) return 3;
        if (tier <= 11) return 4;
        return 5; // Tier XII requires 5 minion slots
    }

    /**
     * Get cost to upgrade to next tier
     */
    public long getUpgradeCost(int currentTier) {
        if (currentTier >= getMaxTier()) return -1; // Max tier reached
        
        // Cost increases exponentially with tier
        long baseCost = 1000; // Base cost for tier I -> II
        return (long) (baseCost * Math.pow(2, currentTier - 1));
    }

    /**
     * Get experience reward for upgrading to this tier
     */
    public long getUpgradeExperience(int tier) {
        return tier * 100; // 100 XP per tier
    }

    /**
     * Get collection type this minion produces
     */
    public de.noctivag.skyblock.collections.CollectionType getCollectionType() {
        // Map minion types to collection types
        switch (this) {
            case COBBLESTONE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.COBBLESTONE;
            case COAL_MINION:
                return de.noctivag.skyblock.collections.CollectionType.COAL;
            case IRON_MINION:
                return de.noctivag.skyblock.collections.CollectionType.IRON;
            case GOLD_MINION:
                return de.noctivag.skyblock.collections.CollectionType.GOLD;
            case DIAMOND_MINION:
                return de.noctivag.skyblock.collections.CollectionType.DIAMOND;
            case LAPIS_MINION:
                return de.noctivag.skyblock.collections.CollectionType.LAPIS_LAZULI;
            case EMERALD_MINION:
                return de.noctivag.skyblock.collections.CollectionType.EMERALD;
            case REDSTONE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.REDSTONE;
            case QUARTZ_MINION:
                return de.noctivag.skyblock.collections.CollectionType.QUARTZ;
            case OBSIDIAN_MINION:
                return de.noctivag.skyblock.collections.CollectionType.OBSIDIAN;
            case GLOWSTONE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.GLOWSTONE;
            case GRAVEL_MINION:
                return de.noctivag.skyblock.collections.CollectionType.GRAVEL;
            case SAND_MINION:
                return de.noctivag.skyblock.collections.CollectionType.SAND;
            case END_STONE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.END_STONE;
            case SNOW_MINION:
                return de.noctivag.skyblock.collections.CollectionType.SNOW;
            case CLAY_MINION:
                return de.noctivag.skyblock.collections.CollectionType.CLAY;
            case WHEAT_MINION:
                return de.noctivag.skyblock.collections.CollectionType.WHEAT;
            case CARROT_MINION:
                return de.noctivag.skyblock.collections.CollectionType.CARROT;
            case POTATO_MINION:
                return de.noctivag.skyblock.collections.CollectionType.POTATO;
            case PUMPKIN_MINION:
                return de.noctivag.skyblock.collections.CollectionType.PUMPKIN;
            case MELON_MINION:
                return de.noctivag.skyblock.collections.CollectionType.MELON;
            case MUSHROOM_MINION:
                return de.noctivag.skyblock.collections.CollectionType.MUSHROOM;
            case CACTUS_MINION:
                return de.noctivag.skyblock.collections.CollectionType.CACTUS;
            case SUGAR_CANE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.SUGAR_CANE;
            case NETHER_WART_MINION:
                return de.noctivag.skyblock.collections.CollectionType.NETHER_WART;
            case COCOA_MINION:
                return de.noctivag.skyblock.collections.CollectionType.COCOA_BEANS;
            case OAK_MINION:
                return de.noctivag.skyblock.collections.CollectionType.OAK_LOG;
            case BIRCH_MINION:
                return de.noctivag.skyblock.collections.CollectionType.BIRCH_LOG;
            case SPRUCE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.SPRUCE_LOG;
            case JUNGLE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.JUNGLE_LOG;
            case ACACIA_MINION:
                return de.noctivag.skyblock.collections.CollectionType.ACACIA_LOG;
            case DARK_OAK_MINION:
                return de.noctivag.skyblock.collections.CollectionType.DARK_OAK_LOG;
            case MANGROVE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.MANGROVE_LOG;
            case CHERRY_MINION:
                return de.noctivag.skyblock.collections.CollectionType.CHERRY_LOG;
            case FISHING_MINION:
                return de.noctivag.skyblock.collections.CollectionType.RAW_FISH;
            case ZOMBIE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.ROTTEN_FLESH;
            case SKELETON_MINION:
                return de.noctivag.skyblock.collections.CollectionType.BONE;
            case SPIDER_MINION:
                return de.noctivag.skyblock.collections.CollectionType.STRING;
            case CREEPER_MINION:
                return de.noctivag.skyblock.collections.CollectionType.SULPHUR;
            case ENDERMAN_MINION:
                return de.noctivag.skyblock.collections.CollectionType.ENDER_PEARL;
            case BLAZE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.BLAZE_ROD;
            case GHAST_MINION:
                return de.noctivag.skyblock.collections.CollectionType.GHAST_TEAR;
            case SLIME_MINION:
                return de.noctivag.skyblock.collections.CollectionType.SLIME_BALL;
            case MAGMA_CUBE_MINION:
                return de.noctivag.skyblock.collections.CollectionType.MAGMA_CREAM;
            case FLOWER_MINION:
                return de.noctivag.skyblock.collections.CollectionType.FEATHER;
            case CHICKEN_MINION:
                return de.noctivag.skyblock.collections.CollectionType.FEATHER;
            case COW_MINION:
                return de.noctivag.skyblock.collections.CollectionType.LEATHER;
            case PIG_MINION:
                return de.noctivag.skyblock.collections.CollectionType.FEATHER;
            case SHEEP_MINION:
                return de.noctivag.skyblock.collections.CollectionType.WOOL;
            case RABBIT_MINION:
                return de.noctivag.skyblock.collections.CollectionType.RABBIT_FOOT;
            default:
                return null;
        }
    }
}