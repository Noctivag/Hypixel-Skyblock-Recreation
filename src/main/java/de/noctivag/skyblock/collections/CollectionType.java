package de.noctivag.skyblock.collections;

import org.bukkit.Material;

/**
 * Enum representing all collection types in Hypixel Skyblock
 */
public enum CollectionType {
    // Mining Collections
    COBBLESTONE("Cobblestone", "&7", "⛏", "Mining", Material.COBBLESTONE),
    COAL("Coal", "&8", "⛏", "Mining", Material.COAL),
    IRON("Iron", "&f", "⛏", "Mining", Material.IRON_INGOT),
    GOLD("Gold", "&6", "⛏", "Mining", Material.GOLD_INGOT),
    DIAMOND("Diamond", "&b", "⛏", "Mining", Material.DIAMOND),
    LAPIS_LAZULI("Lapis Lazuli", "&9", "⛏", "Mining", Material.LAPIS_LAZULI),
    EMERALD("Emerald", "&a", "⛏", "Mining", Material.EMERALD),
    REDSTONE("Redstone", "&c", "⛏", "Mining", Material.REDSTONE),
    QUARTZ("Quartz", "&f", "⛏", "Mining", Material.QUARTZ),
    OBSIDIAN("Obsidian", "&5", "⛏", "Mining", Material.OBSIDIAN),
    GLOWSTONE("Glowstone", "&e", "⛏", "Mining", Material.GLOWSTONE),
    GRAVEL("Gravel", "&7", "⛏", "Mining", Material.GRAVEL),
    SAND("Sand", "&e", "⛏", "Mining", Material.SAND),
    END_STONE("End Stone", "&f", "⛏", "Mining", Material.END_STONE),
    NETHERRACK("Netherrack", "&c", "⛏", "Mining", Material.NETHERRACK),
    ICE("Ice", "&b", "⛏", "Mining", Material.ICE),
    SNOW("Snow", "&f", "⛏", "Mining", Material.SNOW_BLOCK),
    CLAY("Clay", "&9", "⛏", "Mining", Material.CLAY),
    HARD_STONE("Hard Stone", "&7", "⛏", "Mining", Material.STONE),
    GEMSTONE("Gemstone", "&d", "⛏", "Mining", Material.AMETHYST_SHARD),
    MITHRIL("Mithril", "&b", "⛏", "Mining", Material.PRISMARINE_CRYSTALS),
    TITANIUM("Titanium", "&8", "⛏", "Mining", Material.NETHERITE_INGOT),
    
    // Farming Collections
    WHEAT("Wheat", "&e", "🌾", "Farming", Material.WHEAT),
    CARROT("Carrot", "&6", "🌾", "Farming", Material.CARROT),
    POTATO("Potato", "&e", "🌾", "Farming", Material.POTATO),
    PUMPKIN("Pumpkin", "&6", "🌾", "Farming", Material.PUMPKIN),
    MELON("Melon", "&a", "🌾", "Farming", Material.MELON),
    SEEDS("Seeds", "&e", "🌾", "Farming", Material.WHEAT_SEEDS),
    MUSHROOM("Mushroom", "&c", "🌾", "Farming", Material.RED_MUSHROOM),
    CACTUS("Cactus", "&a", "🌾", "Farming", Material.CACTUS),
    SUGAR_CANE("Sugar Cane", "&a", "🌾", "Farming", Material.SUGAR_CANE),
    NETHER_WART("Nether Wart", "&c", "🌾", "Farming", Material.NETHER_WART),
    COCOA_BEANS("Cocoa Beans", "&6", "🌾", "Farming", Material.COCOA_BEANS),
    
    // Foraging Collections
    OAK_LOG("Oak Log", "&6", "🌲", "Foraging", Material.OAK_LOG),
    BIRCH_LOG("Birch Log", "&f", "🌲", "Foraging", Material.BIRCH_LOG),
    SPRUCE_LOG("Spruce Log", "&8", "🌲", "Foraging", Material.SPRUCE_LOG),
    JUNGLE_LOG("Jungle Log", "&a", "🌲", "Foraging", Material.JUNGLE_LOG),
    ACACIA_LOG("Acacia Log", "&c", "🌲", "Foraging", Material.ACACIA_LOG),
    DARK_OAK_LOG("Dark Oak Log", "&8", "🌲", "Foraging", Material.DARK_OAK_LOG),
    MANGROVE_LOG("Mangrove Log", "&6", "🌲", "Foraging", Material.MANGROVE_LOG),
    CHERRY_LOG("Cherry Log", "&d", "🌲", "Foraging", Material.CHERRY_LOG),
    
    // Fishing Collections
    RAW_FISH("Raw Fish", "&9", "🎣", "Fishing", Material.COD),
    RAW_SALMON("Raw Salmon", "&c", "🎣", "Fishing", Material.SALMON),
    CLOWNFISH("Clownfish", "&e", "🎣", "Fishing", Material.TROPICAL_FISH),
    PUFFERFISH("Pufferfish", "&a", "🎣", "Fishing", Material.PUFFERFISH),
    PRISMARINE_SHARD("Prismarine Shard", "&b", "🎣", "Fishing", Material.PRISMARINE_SHARD),
    PRISMARINE_CRYSTALS("Prismarine Crystals", "&b", "🎣", "Fishing", Material.PRISMARINE_CRYSTALS),
    SPONGE("Sponge", "&e", "🎣", "Fishing", Material.SPONGE),
    LILY_PAD("Lily Pad", "&a", "🎣", "Fishing", Material.LILY_PAD),
    INK_SAC("Ink Sac", "&8", "🎣", "Fishing", Material.INK_SAC),
    
    // Combat Collections
    ROTTEN_FLESH("Rotten Flesh", "&8", "⚔", "Combat", Material.ROTTEN_FLESH),
    BONE("Bone", "&f", "⚔", "Combat", Material.BONE),
    STRING("String", "&f", "⚔", "Combat", Material.STRING),
    SPIDER_EYE("Spider Eye", "&8", "⚔", "Combat", Material.SPIDER_EYE),
    SULPHUR("Sulphur", "&e", "⚔", "Combat", Material.GUNPOWDER),
    ENDER_PEARL("Ender Pearl", "&5", "⚔", "Combat", Material.ENDER_PEARL),
    GHAST_TEAR("Ghast Tear", "&f", "⚔", "Combat", Material.GHAST_TEAR),
    SLIME_BALL("Slime Ball", "&a", "⚔", "Combat", Material.SLIME_BALL),
    BLAZE_ROD("Blaze Rod", "&6", "⚔", "Combat", Material.BLAZE_ROD),
    MAGMA_CREAM("Magma Cream", "&6", "⚔", "Combat", Material.MAGMA_CREAM),
    ENDER_STONE("Ender Stone", "&5", "⚔", "Combat", Material.END_STONE),
    NETHER_STAR("Nether Star", "&f", "⚔", "Combat", Material.NETHER_STAR),
    
    // Special Collections
    FEATHER("Feather", "&f", "✨", "Special", Material.FEATHER),
    LEATHER("Leather", "&6", "✨", "Special", Material.LEATHER),
    RABBIT_FOOT("Rabbit Foot", "&e", "✨", "Special", Material.RABBIT_FOOT),
    RABBIT_HIDE("Rabbit Hide", "&e", "✨", "Special", Material.RABBIT_HIDE),
    WOOL("Wool", "&f", "✨", "Special", Material.WHITE_WOOL);

    private final String displayName;
    private final String color;
    private final String icon;
    private final String category;
    private final Material material;

    CollectionType(String displayName, String color, String icon, String category, Material material) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.category = category;
        this.material = material;
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

    /**
     * Get collection type by material
     */
    public static CollectionType getByMaterial(Material material) {
        for (CollectionType type : values()) {
            if (type.getMaterial() == material) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get all collections by category
     */
    public static CollectionType[] getByCategory(String category) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getCategory().equalsIgnoreCase(category))
                .toArray(CollectionType[]::new);
    }

    /**
     * Get milestone requirements for a collection
     */
    public long[] getMilestoneRequirements() {
        switch (this) {
            case COBBLESTONE:
                return new long[]{50, 100, 250, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2500000000L, 5000000000L, 10000000000L, 25000000000L, 50000000000L, 100000000000L, 250000000000L, 500000000000L, 1000000000000L, 2500000000000L, 5000000000000L, 10000000000000L, 25000000000000L, 50000000000000L, 100000000000000L, 250000000000000L, 500000000000000L, 1000000000000000L, 2500000000000000L, 5000000000000000L, 10000000000000000L, 25000000000000000L, 50000000000000000L, 100000000000000000L, 250000000000000000L, 500000000000000000L, 1000000000000000000L};
            case COAL:
                return new long[]{50, 100, 250, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2500000000L, 5000000000L, 10000000000L, 25000000000L, 50000000000L, 100000000000L, 250000000000L, 500000000000L, 1000000000000L, 2500000000000L, 5000000000000L, 10000000000000L, 25000000000000L, 50000000000000L, 100000000000000L, 250000000000000L, 500000000000000L, 1000000000000000L, 2500000000000000L, 5000000000000000L, 10000000000000000L, 25000000000000000L, 50000000000000000L, 100000000000000000L, 250000000000000000L, 500000000000000000L, 1000000000000000000L};
            case IRON:
                return new long[]{50, 100, 250, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2500000000L, 5000000000L, 10000000000L, 25000000000L, 50000000000L, 100000000000L, 250000000000L, 500000000000L, 1000000000000L, 2500000000000L, 5000000000000L, 10000000000000L, 25000000000000L, 50000000000000L, 100000000000000L, 250000000000000L, 500000000000000L, 1000000000000000L, 2500000000000000L, 5000000000000000L, 10000000000000000L, 25000000000000000L, 50000000000000000L, 100000000000000000L, 250000000000000000L, 500000000000000000L, 1000000000000000000L};
            case GOLD:
                return new long[]{50, 100, 250, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2500000000L, 5000000000L, 10000000000L, 25000000000L, 50000000000L, 100000000000L, 250000000000L, 500000000000L, 1000000000000L, 2500000000000L, 5000000000000L, 10000000000000L, 25000000000000L, 50000000000000L, 100000000000000L, 250000000000000L, 500000000000000L, 1000000000000000L, 2500000000000000L, 5000000000000000L, 10000000000000000L, 25000000000000000L, 50000000000000000L, 100000000000000000L, 250000000000000000L, 500000000000000000L, 1000000000000000000L};
            case DIAMOND:
                return new long[]{50, 100, 250, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2500000000L, 5000000000L, 10000000000L, 25000000000L, 50000000000L, 100000000000L, 250000000000L, 500000000000L, 1000000000000L, 2500000000000L, 5000000000000L, 10000000000000L, 25000000000000L, 50000000000000L, 100000000000000L, 250000000000000L, 500000000000000L, 1000000000000000L, 2500000000000000L, 5000000000000000L, 10000000000000000L, 25000000000000000L, 50000000000000000L, 100000000000000000L, 250000000000000000L, 500000000000000000L, 1000000000000000000L};
            default:
                // Default milestone requirements for other collections
                return new long[]{50, 100, 250, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2500000000L, 5000000000L, 10000000000L, 25000000000L, 50000000000L, 100000000000L, 250000000000L, 500000000000L, 1000000000000L, 2500000000000L, 5000000000000L, 10000000000000L, 25000000000000L, 50000000000000L, 100000000000000L, 250000000000000L, 500000000000000L, 1000000000000000L, 2500000000000000L, 5000000000000000L, 10000000000000000L, 25000000000000000L, 50000000000000000L, 100000000000000000L, 250000000000000000L, 500000000000000000L, 1000000000000000000L};
        }
    }

    /**
     * Get the next milestone requirement
     */
    public long getNextMilestoneRequirement(long currentAmount) {
        long[] milestones = getMilestoneRequirements();
        for (long milestone : milestones) {
            if (currentAmount < milestone) {
                return milestone;
            }
        }
        return -1; // Max milestone reached
    }

    /**
     * Get current milestone level
     */
    public int getCurrentMilestoneLevel(long currentAmount) {
        long[] milestones = getMilestoneRequirements();
        int level = 0;
        for (long milestone : milestones) {
            if (currentAmount >= milestone) {
                level++;
            } else {
                break;
            }
        }
        return level;
    }

    /**
     * Get progress to next milestone
     */
    public long getProgressToNextMilestone(long currentAmount) {
        long nextMilestone = getNextMilestoneRequirement(currentAmount);
        if (nextMilestone == -1) return 0;
        
        long[] milestones = getMilestoneRequirements();
        long previousMilestone = 0;
        for (long milestone : milestones) {
            if (milestone == nextMilestone) {
                break;
            }
            previousMilestone = milestone;
        }
        
        return currentAmount - previousMilestone;
    }

    /**
     * Get total progress needed for next milestone
     */
    public long getTotalProgressNeededForNextMilestone(long currentAmount) {
        long nextMilestone = getNextMilestoneRequirement(currentAmount);
        if (nextMilestone == -1) return 0;
        
        long[] milestones = getMilestoneRequirements();
        long previousMilestone = 0;
        for (long milestone : milestones) {
            if (milestone == nextMilestone) {
                break;
            }
            previousMilestone = milestone;
        }
        
        return nextMilestone - previousMilestone;
    }
}
