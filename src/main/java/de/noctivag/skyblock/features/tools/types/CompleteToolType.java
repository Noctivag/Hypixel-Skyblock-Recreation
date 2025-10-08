package de.noctivag.skyblock.features.tools.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 72+ tool types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteToolType {
    CARROT_CANDY("Carrot Candy", "🥕", "Specialized for carrots"),
    FUNGI_CUTTER("Fungi Cutter", "🍄", "Mushroom Farming"),
    TREECAPITATOR("Treecapitator", "🪓", "Fells entire trees with one hit"),
    // ===== MINING TOOLS (Hypixel SkyBlock, vollständig) =====
    WOODEN_PICKAXE("Wooden Pickaxe", "🪓", "Basic mining tool"),
    STONE_PICKAXE("Stone Pickaxe", "🪨", "Improved mining tool"),
    IRON_PICKAXE("Iron Pickaxe", "⚒️", "Good mining tool"),
    GOLDEN_PICKAXE("Golden Pickaxe", "🥇", "Fast but weak mining tool"),
    DIAMOND_PICKAXE("Diamond Pickaxe", "💎", "Standard mining tool"),
    NETHERITE_PICKAXE("Netherite Pickaxe", "⚡", "Best vanilla mining tool"),
    LAPIS_PICKAXE("Lapis Pickaxe", "🔵", "Chance auf doppelte Lapis Drops, Early Game Mining"),
    ZOMBIE_PICKAXE("Zombie Pickaxe", "�", "Mining, Early Game, aus Dungeons"),
    STONKS_PICKAXE("Stonks Pickaxe", "📈", "Specifically for Endstone"),
    GOLDEN_PICKAXE_REFORGED("Golden Pickaxe (Reforged)", "🥇", "Reforged golden pickaxe"),
    DIAMOND_PICKAXE_REFORGED("Diamond Pickaxe (Reforged)", "💎", "Reforged diamond pickaxe"),
    PICKONIMBUS_2000("Pickonimbus 2000", "✨", "2000 Nutzungen, Mining, Crystal Hollows"),
    FRACTURED_MITHRIL_PICKAXE("Fractured Mithril Pickaxe", "⛏️", "Mithril Mining, Early Game"),
    BANDAGED_MITHRIL_PICKAXE("Bandaged Mithril Pickaxe", "⛏️", "Mithril Mining, Upgraded"),
    MITHRIL_PICKAXE("Mithril Pickaxe", "⛏️", "Mithril Mining, Standard"),
    TITANIUM_PICKAXE("Titanium Pickaxe", "⛏️", "Titanium Mining, Standard"),
    DRILL("Drill", "🔩", "Basis Drill, Mining"),
    MITHRIL_DRILL("Mithril Drill", "⛏️", "Efficient mining"),
    TITANIUM_DRILL("Titanium Drill", "🔧", "High-quality mining tool"),
    GEMSTONE_DRILL("Gemstone Drill", "💎", "For Gemstone Mining"),
    DIVAN_DRILL("Divan's Drill", "💎", "Ultimate mining drill"),
    GEMSTONE_GAUNTLET("Gemstone Gauntlet", "👊", "Advanced mining gauntlet"),
    ROCK_GEMSTONE_DRILL("Rock Gemstone Drill", "🪨", "Rock pet enhanced drill"),
    RUBY_DRILL("Ruby Drill", "💎", "Ruby-powered drill"),
    JASPER_DRILL("Jasper Drill", "💎", "Jasper-powered drill"),
    SAPPHIRE_DRILL("Sapphire Drill", "💎", "Sapphire-powered drill"),
    AMETHYST_DRILL("Amethyst Drill", "💎", "Amethyst-powered drill"),
    TOPAZ_DRILL("Topaz Drill", "💎", "Topaz-powered drill"),
    JADE_DRILL("Jade Drill", "💎", "Jade-powered drill"),
    AMBER_DRILL("Amber Drill", "💎", "Amber-powered drill"),
    FLAME_BREAKER_PICKAXE("Flame Breaker Pickaxe", "🔥", "Mining, Nether, Lava Immunity"),
    GLACIAL_SCYTHE("Glacial Scythe", "❄️", "Mining, Ice, AoE Slow"),
    TITANIUM_SHOVEL("Titanium Shovel", "⛏️", "Titanium Mining, Shovel"),
    ENDER_PICKAXE("Ender Pickaxe", "🟣", "Mining, End, Early Game"),
    COBBLESTONE_MINER("Cobblestone Miner", "🪨", "Cobblestone Mining, Early Game"),
    FARMERS_ROD("Farmer's Rod", "🌾", "Farming, Fishing, Hybrid Tool"),
    
    // ===== FISHING TOOLS (vollständig) =====
    WOODEN_FISHING_ROD("Wooden Fishing Rod", "🎣", "Basic fishing rod"),
    FISHING_ROD("Fishing Rod", "🎣", "Standard fishing rod"),
    ENCHANTED_FISHING_ROD("Enchanted Fishing Rod", "🎣", "Enchanted fishing rod"),
    SPONGE_ROD("Sponge Rod", "🧽", "Increases sea creature chance"),
    CHALLENGING_ROD("Challenging Rod", "🎯", "For difficult fish"),
    MAGMA_ROD("Magma Rod", "🌋", "Fire-based fishing rod"),
    YETI_ROD("Yeti Rod", "❄️", "Ice-based fishing rod"),
    PRISMARINE_ROD("Prismarine Rod", "🌊", "Water-based fishing rod"),
    SHARK_FISHING_ROD("Shark Fishing Rod", "🦈", "For catching sharks"),
    DOLPHIN_FISHING_ROD("Dolphin Fishing Rod", "🐬", "Dolphin-enhanced rod"),
    WHALE_FISHING_ROD("Whale Fishing Rod", "🐋", "Whale-enhanced rod"),
    SQUID_FISHING_ROD("Squid Fishing Rod", "🦑", "Squid-enhanced rod"),
    BLUE_WHALE_FISHING_ROD("Blue Whale Fishing Rod", "🐋", "Blue whale enhanced rod"),
    FLYING_FISH_FISHING_ROD("Flying Fish Fishing Rod", "🐟", "Flying fish enhanced rod"),
    GUARDIAN_FISHING_ROD("Guardian Fishing Rod", "🐟", "Guardian enhanced rod"),
    ICE_ROD("Ice Rod", "❄️", "Fishing, Ice, Freezes Water"),
    LAVA_ROD("Lava Rod", "🔥", "Fishing, Lava, Nether"),
    WINTER_ROD("Winter Rod", "⛄", "Fishing, Winter Island"),
    
    // ===== FARMING TOOLS (vollständig) =====
    WOODEN_HOE("Wooden Hoe", "🌾", "Basic farming tool"),
    STONE_HOE("Stone Hoe", "🪨", "Improved farming tool"),
    IRON_HOE("Iron Hoe", "⚒️", "Good farming tool"),
    GOLDEN_HOE("Golden Hoe", "🥇", "Fast but weak farming tool"),
    DIAMOND_HOE("Diamond Hoe", "💎", "Standard farming tool"),
    NETHERITE_HOE("Netherite Hoe", "⚡", "Best vanilla farming tool"),
    FARMING_TOOL("Farming Tool", "🌾", "Standard farming tool"),
    SUGAR_CANE_HOE("Sugar Cane Hoe", "🎋", "Specialized for sugar cane"),
    CACTUS_KNIFE("Cactus Knife", "🌵", "Specialized for cactus"),
    MELON_DICER("Melon Dicer", "🍈", "Specialized for melons"),
    PUMPKIN_DICER("Pumpkin Dicer", "🎃", "Specialized for pumpkins"),
    
    POTATO_TALISMAN("Potato Talisman", "🥔", "Specialized for potatoes"),
    WHEAT_HOE("Wheat Hoe", "🌾", "Specialized for wheat"),
    NETHER_WART_HOE("Nether Wart Hoe", "🍄", "Specialized for nether wart"),
    COCOA_CHOCOLATE("Cocoa Chocolate", "🍫", "Specialized for cocoa beans"),
    
    // ===== FORAGING TOOLS (12+) =====
    WOODEN_AXE("Wooden Axe", "🪓", "Basic foraging tool"),
    STONE_AXE("Stone Axe", "🪨", "Improved foraging tool"),
    IRON_AXE("Iron Axe", "⚒️", "Good foraging tool"),
    GOLDEN_AXE("Golden Axe", "🥇", "Fast but weak foraging tool"),
    DIAMOND_AXE("Diamond Axe", "💎", "Standard foraging tool"),
    NETHERITE_AXE("Netherite Axe", "⚡", "Best vanilla foraging tool"),
    
    JUNGLE_AXE("Jungle Axe", "🌴", "Specialized for jungle wood"),
    OAK_AXE("Oak Axe", "🌳", "Specialized for oak wood"),
    BIRCH_AXE("Birch Axe", "🌲", "Specialized for birch wood"),
    SPRUCE_AXE("Spruce Axe", "🌲", "Specialized for spruce wood"),
    DARK_OAK_AXE("Dark Oak Axe", "🌳", "Specialized for dark oak wood"),
    ACACIA_AXE("Acacia Axe", "🌳", "Specialized for acacia wood"),
    
    // ===== UTILITY TOOLS (10+) =====
    GRAPPLING_HOOK("Grappling Hook", "🪝", "Fast movement, teleportation"),
    ASPECT_OF_THE_END("Aspect of the End", "⚔️", "Short teleportation"),
    BONZOS_STAFF("Bonzo's Staff", "🎭", "Dungeon utility staff"),
    JERRY_CHINE_GUN("Jerry-chine Gun", "🔫", "Fun item"),
    WAND_OF_MENDING("Wand of Mending", "🪄", "Healing utility staff"),
    WAND_OF_ATONEMENT("Wand of Atonement", "🪄", "Advanced healing staff"),
    WAND_OF_RESTORATION("Wand of Restoration", "🪄", "Ultimate healing staff"),
    INFINITE_QUIVER("Infinite Quiver", "🏹", "Infinite arrows"),
    SPIRIT_LEAP("Spirit Leap", "👻", "Spirit movement tool"),
    WARP_SCROLL("Warp Scroll", "📜", "Teleportation scroll");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    CompleteToolType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
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
    
    /**
     * Get mining tools (25+)
     */
    public static List<CompleteToolType> getMiningTools() {
        return Arrays.asList(
            WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE,
            LAPIS_PICKAXE, ZOMBIE_PICKAXE, STONKS_PICKAXE, GOLDEN_PICKAXE_REFORGED, DIAMOND_PICKAXE_REFORGED,
            PICKONIMBUS_2000, FRACTURED_MITHRIL_PICKAXE, BANDAGED_MITHRIL_PICKAXE, MITHRIL_PICKAXE, TITANIUM_PICKAXE,
            DRILL, MITHRIL_DRILL, TITANIUM_DRILL, GEMSTONE_DRILL, DIVAN_DRILL, GEMSTONE_GAUNTLET, ROCK_GEMSTONE_DRILL,
            RUBY_DRILL, JASPER_DRILL, SAPPHIRE_DRILL, AMETHYST_DRILL, TOPAZ_DRILL, JADE_DRILL, AMBER_DRILL,
            FLAME_BREAKER_PICKAXE, GLACIAL_SCYTHE, TITANIUM_SHOVEL, ENDER_PICKAXE, COBBLESTONE_MINER
        );
    }
    
    /**
     * Get fishing tools (15+)
     */
    public static List<CompleteToolType> getFishingTools() {
        return Arrays.asList(
            WOODEN_FISHING_ROD, FISHING_ROD, ENCHANTED_FISHING_ROD, SPONGE_ROD,
            CHALLENGING_ROD, MAGMA_ROD, YETI_ROD, PRISMARINE_ROD,
            SHARK_FISHING_ROD, DOLPHIN_FISHING_ROD, WHALE_FISHING_ROD,
            SQUID_FISHING_ROD, BLUE_WHALE_FISHING_ROD, FLYING_FISH_FISHING_ROD,
            GUARDIAN_FISHING_ROD
        );
    }
    
    /**
     * Get farming tools (15+)
     */
    public static List<CompleteToolType> getFarmingTools() {
        return Arrays.asList(
            WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE, FARMING_TOOL,
            SUGAR_CANE_HOE, CACTUS_KNIFE, MELON_DICER, PUMPKIN_DICER, CARROT_CANDY, POTATO_TALISMAN,
            WHEAT_HOE, NETHER_WART_HOE, COCOA_CHOCOLATE, FUNGI_CUTTER
        );
    }
    
    /**
     * Get foraging tools (12+)
     */
    public static List<CompleteToolType> getForagingTools() {
        return Arrays.asList(
            WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE, JUNGLE_AXE, OAK_AXE,
            BIRCH_AXE, SPRUCE_AXE, DARK_OAK_AXE, ACACIA_AXE, TREECAPITATOR
        );
    }
    
    /**
     * Get utility tools (10+)
     */
    public static List<CompleteToolType> getUtilityTools() {
        return Arrays.asList(
            GRAPPLING_HOOK, ASPECT_OF_THE_END, BONZOS_STAFF, JERRY_CHINE_GUN,
            WAND_OF_MENDING, WAND_OF_ATONEMENT, WAND_OF_RESTORATION,
            INFINITE_QUIVER, SPIRIT_LEAP, WARP_SCROLL
        );
    }
    
    /**
     * Get tools by category
     */
    public static List<CompleteToolType> getToolsByCategory(String category) {
        return switch (category.toLowerCase()) {
            case "mining" -> getMiningTools();
            case "fishing" -> getFishingTools();
            case "utility" -> getUtilityTools();
            case "farming" -> getFarmingTools();
            case "foraging" -> getForagingTools();
            default -> Arrays.asList(values());
        };
    }
    
    /**
     * Get tool category
     */
    public String getCategory() {
        if (name().contains("PICKAXE") || name().contains("DRILL") || name().contains("GAUNTLET")) {
            return "Mining";
        }
        if (name().contains("FISHING") || name().contains("ROD")) {
            return "Fishing";
        }
        if (name().contains("HOE") || name().contains("FARMING") || name().contains("CANDY") || 
            name().contains("DICER") || name().contains("KNIFE") || name().contains("CHOCOLATE")) {
            return "Farming";
        }
        if (name().contains("AXE") || name().contains("TREECAPITATOR")) {
            return "Foraging";
        }
        return "Utility";
    }
    
    /**
     * Get tool rarity
     */
    public String getRarity() {
        if (name().contains("WOODEN") || name().contains("STONE")) return "Common";
        if (name().contains("IRON") || name().contains("GOLDEN")) return "Uncommon";
        if (name().contains("DIAMOND") || name().contains("NETHERITE")) return "Rare";
        if (name().contains("DRILL") || name().contains("GAUNTLET")) return "Epic";
        if (name().contains("DIVAN") || name().contains("GEMSTONE")) return "Legendary";
        return "Rare";
    }
    
    /**
     * Get total tool count
     */
    public static int getTotalToolCount() {
        return values().length;
    }
    
    /**
     * Get tools by rarity
     */
    public static List<CompleteToolType> getToolsByRarity(String rarity) {
        return Arrays.stream(values())
            .filter(tool -> tool.getRarity().equals(rarity))
            .toList();
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
