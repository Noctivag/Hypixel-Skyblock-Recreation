package de.noctivag.skyblock.features.minions.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 50+ minion types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteMinionType {
    // ===== MINING MINIONS (16+) =====
    COBBLESTONE_MINION("Cobblestone Minion", "⛏️", 1, "Cobblestone", 2400, MinionCategory.MINING, MinionRarity.COMMON),
    COAL_MINION("Coal Minion", "⛏️", 1, "Coal", 2400, MinionCategory.MINING, MinionRarity.COMMON),
    IRON_MINION("Iron Minion", "⛏️", 1, "Iron Ingot", 2400, MinionCategory.MINING, MinionRarity.COMMON),
    GOLD_MINION("Gold Minion", "⛏️", 1, "Gold Ingot", 2400, MinionCategory.MINING, MinionRarity.UNCOMMON),
    DIAMOND_MINION("Diamond Minion", "💎", 1, "Diamond", 2400, MinionCategory.MINING, MinionRarity.RARE),
    LAPIS_MINION("Lapis Minion", "💙", 1, "Lapis Lazuli", 2400, MinionCategory.MINING, MinionRarity.UNCOMMON),
    EMERALD_MINION("Emerald Minion", "💚", 1, "Emerald", 2400, MinionCategory.MINING, MinionRarity.RARE),
    REDSTONE_MINION("Redstone Minion", "🔴", 1, "Redstone", 2400, MinionCategory.MINING, MinionRarity.UNCOMMON),
    QUARTZ_MINION("Quartz Minion", "⚪", 1, "Nether Quartz", 2400, MinionCategory.MINING, MinionRarity.UNCOMMON),
    OBSIDIAN_MINION("Obsidian Minion", "🖤", 1, "Obsidian", 2400, MinionCategory.MINING, MinionRarity.RARE),
    GLOWSTONE_MINION("Glowstone Minion", "💡", 1, "Glowstone", 2400, MinionCategory.MINING, MinionRarity.UNCOMMON),
    GRAVEL_MINION("Gravel Minion", "⚫", 1, "Gravel", 2400, MinionCategory.MINING, MinionRarity.COMMON),
    SAND_MINION("Sand Minion", "🟡", 1, "Sand", 2400, MinionCategory.MINING, MinionRarity.COMMON),
    END_STONE_MINION("End Stone Minion", "🟤", 1, "End Stone", 2400, MinionCategory.MINING, MinionRarity.UNCOMMON),
    NETHERRACK_MINION("Netherrack Minion", "🔴", 1, "Netherrack", 2400, MinionCategory.MINING, MinionRarity.COMMON),
    MYCELIUM_MINION("Mycelium Minion", "🟣", 1, "Mycelium", 2400, MinionCategory.MINING, MinionRarity.RARE),

    // ===== FARMING MINIONS (12+) =====
    WHEAT_MINION("Wheat Minion", "🌾", 1, "Wheat", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    CARROT_MINION("Carrot Minion", "🥕", 1, "Carrot", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    POTATO_MINION("Potato Minion", "🥔", 1, "Potato", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    PUMPKIN_MINION("Pumpkin Minion", "🎃", 1, "Pumpkin", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    MELON_MINION("Melon Minion", "🍈", 1, "Melon", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    COCOA_MINION("Cocoa Minion", "🍫", 1, "Cocoa Beans", 2400, MinionCategory.FARMING, MinionRarity.UNCOMMON),
    CACTUS_MINION("Cactus Minion", "🌵", 1, "Cactus", 2400, MinionCategory.FARMING, MinionRarity.UNCOMMON),
    SUGAR_CANE_MINION("Sugar Cane Minion", "🎋", 1, "Sugar Cane", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    NETHER_WART_MINION("Nether Wart Minion", "🍄", 1, "Nether Wart", 2400, MinionCategory.FARMING, MinionRarity.UNCOMMON),
    MUSHROOM_MINION("Mushroom Minion", "🍄", 1, "Mushroom", 2400, MinionCategory.FARMING, MinionRarity.UNCOMMON),
    BEETROOT_MINION("Beetroot Minion", "🥬", 1, "Beetroot", 2400, MinionCategory.FARMING, MinionRarity.COMMON),
    BAMBOO_MINION("Bamboo Minion", "🎋", 1, "Bamboo", 2400, MinionCategory.FARMING, MinionRarity.UNCOMMON),

    // ===== COMBAT MINIONS (12+) =====
    ZOMBIE_MINION("Zombie Minion", "🧟", 1, "Rotten Flesh", 2400, MinionCategory.COMBAT, MinionRarity.COMMON),
    SKELETON_MINION("Skeleton Minion", "💀", 1, "Bones", 2400, MinionCategory.COMBAT, MinionRarity.COMMON),
    SPIDER_MINION("Spider Minion", "🕷️", 1, "String", 2400, MinionCategory.COMBAT, MinionRarity.COMMON),
    CAVE_SPIDER_MINION("Cave Spider Minion", "🕷️", 1, "String", 2400, MinionCategory.COMBAT, MinionRarity.COMMON),
    CREEPER_MINION("Creeper Minion", "💣", 1, "Gunpowder", 2400, MinionCategory.COMBAT, MinionRarity.COMMON),
    ENDERMAN_MINION("Enderman Minion", "🌑", 1, "Ender Pearls", 2400, MinionCategory.COMBAT, MinionRarity.UNCOMMON),
    BLAZE_MINION("Blaze Minion", "🔥", 1, "Blaze Rods", 2400, MinionCategory.COMBAT, MinionRarity.RARE),
    GHAST_MINION("Ghast Minion", "👻", 1, "Ghast Tears", 2400, MinionCategory.COMBAT, MinionRarity.RARE),
    MAGMA_CUBE_MINION("Magma Cube Minion", "🌋", 1, "Magma Cream", 2400, MinionCategory.COMBAT, MinionRarity.UNCOMMON),
    SLIME_MINION("Slime Minion", "🟢", 1, "Slime Balls", 2400, MinionCategory.COMBAT, MinionRarity.COMMON),
    WITHER_SKELETON_MINION("Wither Skeleton Minion", "💀", 1, "Wither Skeleton Skulls", 2400, MinionCategory.COMBAT, MinionRarity.EPIC),
    PIGMAN_MINION("Pigman Minion", "🐷", 1, "Gold Nuggets", 2400, MinionCategory.COMBAT, MinionRarity.UNCOMMON),

    // ===== FORAGING MINIONS (8+) =====
    OAK_MINION("Oak Minion", "🌳", 1, "Oak Wood", 2400, MinionCategory.FORAGING, MinionRarity.COMMON),
    BIRCH_MINION("Birch Minion", "🌲", 1, "Birch Wood", 2400, MinionCategory.FORAGING, MinionRarity.COMMON),
    SPRUCE_MINION("Spruce Minion", "🌲", 1, "Spruce Wood", 2400, MinionCategory.FORAGING, MinionRarity.COMMON),
    DARK_OAK_MINION("Dark Oak Minion", "🌳", 1, "Dark Oak Wood", 2400, MinionCategory.FORAGING, MinionRarity.UNCOMMON),
    ACACIA_MINION("Acacia Minion", "🌳", 1, "Acacia Wood", 2400, MinionCategory.FORAGING, MinionRarity.UNCOMMON),
    JUNGLE_MINION("Jungle Minion", "🌴", 1, "Jungle Wood", 2400, MinionCategory.FORAGING, MinionRarity.UNCOMMON),
    CRIMSON_MINION("Crimson Minion", "🔴", 1, "Crimson Stem", 2400, MinionCategory.FORAGING, MinionRarity.RARE),
    WARPED_MINION("Warped Minion", "🔵", 1, "Warped Stem", 2400, MinionCategory.FORAGING, MinionRarity.RARE),

    // ===== FISHING MINIONS (6+) =====
    FISH_MINION("Fish Minion", "🐟", 1, "Raw Fish", 2400, MinionCategory.FISHING, MinionRarity.COMMON),
    SALMON_MINION("Salmon Minion", "🐟", 1, "Raw Salmon", 2400, MinionCategory.FISHING, MinionRarity.COMMON),
    CLOWNFISH_MINION("Clownfish Minion", "🐠", 1, "Clownfish", 2400, MinionCategory.FISHING, MinionRarity.UNCOMMON),
    PUFFERFISH_MINION("Pufferfish Minion", "🐡", 1, "Pufferfish", 2400, MinionCategory.FISHING, MinionRarity.UNCOMMON),
    TROPICAL_FISH_MINION("Tropical Fish Minion", "🐠", 1, "Tropical Fish", 2400, MinionCategory.FISHING, MinionRarity.UNCOMMON),
    GUARDIAN_MINION("Guardian Minion", "🐟", 1, "Prismarine Shards", 2400, MinionCategory.FISHING, MinionRarity.RARE),

    // ===== SPECIAL MINIONS (6+) =====
    SNOW_MINION("Snow Minion", "❄️", 1, "Snowballs", 2400, MinionCategory.SPECIAL, MinionRarity.UNCOMMON),
    CLAY_MINION("Clay Minion", "🟫", 1, "Clay", 2400, MinionCategory.SPECIAL, MinionRarity.UNCOMMON),
    FLOWER_MINION("Flower Minion", "🌸", 1, "Flowers", 2400, MinionCategory.SPECIAL, MinionRarity.UNCOMMON),
    ICE_MINION("Ice Minion", "🧊", 1, "Ice", 2400, MinionCategory.SPECIAL, MinionRarity.RARE),
    ENDER_STONE_MINION("Ender Stone Minion", "🟤", 1, "End Stone", 2400, MinionCategory.SPECIAL, MinionRarity.RARE),
    REVENANT_MINION("Revenant Minion", "🧟", 1, "Rotten Flesh", 2400, MinionCategory.SPECIAL, MinionRarity.EPIC);

    private final String displayName;
    private final String icon;
    private final int level;
    private final String production;
    private final int baseTimeBetweenActions; // in milliseconds
    private final MinionCategory category;
    private final MinionRarity rarity;

    CompleteMinionType(String displayName, String icon, int level, String production, int baseTimeBetweenActions, MinionCategory category, MinionRarity rarity) {
        this.displayName = displayName;
        this.icon = icon;
        this.level = level;
        this.production = production;
        this.baseTimeBetweenActions = baseTimeBetweenActions;
        this.category = category;
        this.rarity = rarity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public int getLevel() {
        return level;
    }

    public String getProduction() {
        return production;
    }

    public int getBaseTimeBetweenActions() {
        return baseTimeBetweenActions;
    }

    public MinionCategory getCategory() {
        return category;
    }

    public MinionRarity getRarity() {
        return rarity;
    }

    /**
     * Get minions by category
     */
    public static List<CompleteMinionType> getMinionsByCategory(MinionCategory category) {
        return Arrays.stream(values())
                .filter(minion -> minion.getCategory() == category)
                .toList();
    }

    /**
     * Get minions by rarity
     */
    public static List<CompleteMinionType> getMinionsByRarity(MinionRarity rarity) {
        return Arrays.stream(values())
                .filter(minion -> minion.getRarity() == rarity)
                .toList();
    }

    /**
     * Get minions by level range
     */
    public static List<CompleteMinionType> getMinionsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(values())
                .filter(minion -> minion.getLevel() >= minLevel && minion.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total minion count
     */
    public static int getTotalMinionCount() {
        return values().length;
    }

    /**
     * Get minion count by category
     */
    public static int getMinionCountByCategory(MinionCategory category) {
        return (int) Arrays.stream(values())
                .filter(minion -> minion.getCategory() == category)
                .count();
    }

    /**
     * Get minion count by rarity
     */
    public static int getMinionCountByRarity(MinionRarity rarity) {
        return (int) Arrays.stream(values())
                .filter(minion -> minion.getRarity() == rarity)
                .count();
    }

    /**
     * Get mining minions
     */
    public static List<CompleteMinionType> getMiningMinions() {
        return getMinionsByCategory(MinionCategory.MINING);
    }

    /**
     * Get farming minions
     */
    public static List<CompleteMinionType> getFarmingMinions() {
        return getMinionsByCategory(MinionCategory.FARMING);
    }

    /**
     * Get combat minions
     */
    public static List<CompleteMinionType> getCombatMinions() {
        return getMinionsByCategory(MinionCategory.COMBAT);
    }

    /**
     * Get foraging minions
     */
    public static List<CompleteMinionType> getForagingMinions() {
        return getMinionsByCategory(MinionCategory.FORAGING);
    }

    /**
     * Get fishing minions
     */
    public static List<CompleteMinionType> getFishingMinions() {
        return getMinionsByCategory(MinionCategory.FISHING);
    }

    /**
     * Get special minions
     */
    public static List<CompleteMinionType> getSpecialMinions() {
        return getMinionsByCategory(MinionCategory.SPECIAL);
    }

    /**
     * Get minions by production type
     */
    public static List<CompleteMinionType> getMinionsByProduction(String production) {
        return Arrays.stream(values())
                .filter(minion -> minion.getProduction().equalsIgnoreCase(production))
                .toList();
    }

    /**
     * Get minions by time efficiency (faster = lower time)
     */
    public static List<CompleteMinionType> getMinionsByTimeEfficiency(int maxTime) {
        return Arrays.stream(values())
                .filter(minion -> minion.getBaseTimeBetweenActions() <= maxTime)
                .toList();
    }

    /**
     * Get minions suitable for specific areas
     */
    public static List<CompleteMinionType> getMinionsForArea(String areaName) {
        return Arrays.stream(values())
                .filter(minion -> isSuitableForArea(minion, areaName))
                .toList();
    }

    /**
     * Check if minion is suitable for area
     */
    private static boolean isSuitableForArea(CompleteMinionType minion, String areaName) {
        String area = areaName.toLowerCase();
        MinionCategory category = minion.getCategory();
        
        return switch (category) {
            case MINING -> area.contains("mine") || area.contains("cavern") || area.contains("crystal");
            case FARMING -> area.contains("farm") || area.contains("garden") || area.contains("field");
            case COMBAT -> area.contains("combat") || area.contains("arena") || area.contains("spawn");
            case FORAGING -> area.contains("forest") || area.contains("tree") || area.contains("wood");
            case FISHING -> area.contains("water") || area.contains("lake") || area.contains("ocean");
            case SPECIAL -> true; // Special minions can work anywhere
        };
    }

    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
