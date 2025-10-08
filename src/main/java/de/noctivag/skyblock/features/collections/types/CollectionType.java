package de.noctivag.skyblock.features.collections.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Collection Type enum for collection types
 */
public enum CollectionType {
    COBBLESTONE("Cobblestone", "Cobblestone collection", CollectionCategory.MINING, Material.COBBLESTONE),
    COAL("Coal", "Coal collection", CollectionCategory.MINING, Material.COAL),
    IRON("Iron", "Iron collection", CollectionCategory.MINING, Material.IRON_INGOT),
    GOLD("Gold", "Gold collection", CollectionCategory.MINING, Material.GOLD_INGOT),
    DIAMOND("Diamond", "Diamond collection", CollectionCategory.MINING, Material.DIAMOND),
    EMERALD("Emerald", "Emerald collection", CollectionCategory.MINING, Material.EMERALD),
    REDSTONE("Redstone", "Redstone collection", CollectionCategory.MINING, Material.REDSTONE),
    LAPIS("Lapis", "Lapis collection", CollectionCategory.MINING, Material.LAPIS_LAZULI),
    QUARTZ("Quartz", "Quartz collection", CollectionCategory.MINING, Material.QUARTZ),
    OBSIDIAN("Obsidian", "Obsidian collection", CollectionCategory.MINING, Material.OBSIDIAN),
    ICE("Ice", "Ice collection", CollectionCategory.MINING, Material.ICE),
    SAND("Sand", "Sand collection", CollectionCategory.MINING, Material.SAND),
    SNOW("Snow", "Snow collection", CollectionCategory.MINING, Material.SNOW_BLOCK),
    CLAY("Clay", "Clay collection", CollectionCategory.MINING, Material.CLAY),
    HARD_STONE("Hard Stone", "Hard Stone collection", CollectionCategory.MINING, Material.STONE),
    GEMSTONE("Gemstone", "Gemstone collection", CollectionCategory.GEMSTONE, Material.AMETHYST_SHARD),
    MITHRIL("Mithril", "Mithril collection", CollectionCategory.MINING, Material.PRISMARINE_CRYSTALS),
    TITANIUM("Titanium", "Titanium collection", CollectionCategory.MINING, Material.NETHERITE_INGOT),
    WHEAT("Wheat", "Wheat collection", CollectionCategory.FARMING, Material.WHEAT),
    CARROT("Carrot", "Carrot collection", CollectionCategory.FARMING, Material.CARROT),
    POTATO("Potato", "Potato collection", CollectionCategory.FARMING, Material.POTATO),
    SUGAR_CANE("Sugar Cane", "Sugar Cane collection", CollectionCategory.FARMING, Material.SUGAR_CANE),
    PUMPKIN("Pumpkin", "Pumpkin collection", CollectionCategory.FARMING, Material.PUMPKIN),
    MELON("Melon", "Melon collection", CollectionCategory.FARMING, Material.MELON),
    MUSHROOM("Mushroom", "Mushroom collection", CollectionCategory.FARMING, Material.RED_MUSHROOM),
    CACTUS("Cactus", "Cactus collection", CollectionCategory.FARMING, Material.CACTUS),
    COCOA("Cocoa", "Cocoa collection", CollectionCategory.FARMING, Material.COCOA_BEANS),
    NETHER_WART("Nether Wart", "Nether Wart collection", CollectionCategory.FARMING, Material.NETHER_WART),
    SUGAR_BEET("Sugar Beet", "Sugar Beet collection", CollectionCategory.FARMING, Material.BEETROOT),
    MUSHROOM_BROWN("Brown Mushroom", "Brown Mushroom collection", CollectionCategory.FARMING, Material.BROWN_MUSHROOM),
    RABBIT("Rabbit", "Rabbit collection", CollectionCategory.FARMING, Material.RABBIT),
    MUTTON("Mutton", "Mutton collection", CollectionCategory.FARMING, Material.MUTTON),
    OAK_LOG("Oak Log", "Oak Log collection", CollectionCategory.FORAGING, Material.OAK_LOG),
    BIRCH_LOG("Birch Log", "Birch Log collection", CollectionCategory.FORAGING, Material.BIRCH_LOG),
    SPRUCE_LOG("Spruce Log", "Spruce Log collection", CollectionCategory.FORAGING, Material.SPRUCE_LOG),
    JUNGLE_LOG("Jungle Log", "Jungle Log collection", CollectionCategory.FORAGING, Material.JUNGLE_LOG),
    ACACIA_LOG("Acacia Log", "Acacia Log collection", CollectionCategory.FORAGING, Material.ACACIA_LOG),
    DARK_OAK_LOG("Dark Oak Log", "Dark Oak Log collection", CollectionCategory.FORAGING, Material.DARK_OAK_LOG),
    CRIMSON_STEM("Crimson Stem", "Crimson Stem collection", CollectionCategory.FORAGING, Material.CRIMSON_STEM),
    WARPED_STEM("Warped Stem", "Warped Stem collection", CollectionCategory.FORAGING, Material.WARPED_STEM),
    RAW_FISH("Raw Fish", "Raw Fish collection", CollectionCategory.FISHING, Material.COD),
    CLOWNFISH("Clownfish", "Clownfish collection", CollectionCategory.FISHING, Material.TROPICAL_FISH),
    RAW_SALMON("Raw Salmon", "Raw Salmon collection", CollectionCategory.FISHING, Material.SALMON),
    PUFFERFISH("Pufferfish", "Pufferfish collection", CollectionCategory.FISHING, Material.PUFFERFISH),
    TROPICAL_FISH("Tropical Fish", "Tropical Fish collection", CollectionCategory.FISHING, Material.TROPICAL_FISH),
    INK_SAC("Ink Sac", "Ink Sac collection", CollectionCategory.FISHING, Material.INK_SAC),
    LILY_PAD("Lily Pad", "Lily Pad collection", CollectionCategory.FISHING, Material.LILY_PAD),
    CLAY_BALL("Clay Ball", "Clay Ball collection", CollectionCategory.FISHING, Material.CLAY_BALL),
    SPONGE("Sponge", "Sponge collection", CollectionCategory.FISHING, Material.SPONGE),
    PRISMARINE_SHARD("Prismarine Shard", "Prismarine Shard collection", CollectionCategory.FISHING, Material.PRISMARINE_SHARD),
    PRISMARINE_CRYSTALS("Prismarine Crystals", "Prismarine Crystals collection", CollectionCategory.FISHING, Material.PRISMARINE_CRYSTALS),
    RAW_CHICKEN("Raw Chicken", "Raw Chicken collection", CollectionCategory.COMBAT, Material.CHICKEN),
    RAW_PORKCHOP("Raw Porkchop", "Raw Porkchop collection", CollectionCategory.COMBAT, Material.PORKCHOP),
    RAW_BEEF("Raw Beef", "Raw Beef collection", CollectionCategory.COMBAT, Material.BEEF),
    ROTTEN_FLESH("Rotten Flesh", "Rotten Flesh collection", CollectionCategory.COMBAT, Material.ROTTEN_FLESH),
    BONE("Bone", "Bone collection", CollectionCategory.COMBAT, Material.BONE),
    STRING("String", "String collection", CollectionCategory.COMBAT, Material.STRING),
    SPIDER_EYE("Spider Eye", "Spider Eye collection", CollectionCategory.COMBAT, Material.SPIDER_EYE),
    GUNPOWDER("Gunpowder", "Gunpowder collection", CollectionCategory.COMBAT, Material.GUNPOWDER),
    ENDER_PEARL("Ender Pearl", "Ender Pearl collection", CollectionCategory.COMBAT, Material.ENDER_PEARL),
    GHAST_TEAR("Ghast Tear", "Ghast Tear collection", CollectionCategory.COMBAT, Material.GHAST_TEAR),
    SLIME_BALL("Slime Ball", "Slime Ball collection", CollectionCategory.COMBAT, Material.SLIME_BALL),
    RABBIT_FOOT_COMBAT("Rabbit Foot (Combat)", "Rabbit Foot collection (Combat)", CollectionCategory.COMBAT, Material.RABBIT_FOOT),
    RABBIT_HIDE_COMBAT("Rabbit Hide (Combat)", "Rabbit Hide collection (Combat)", CollectionCategory.COMBAT, Material.RABBIT_HIDE),
    MUTTON_COMBAT("Mutton (Combat)", "Mutton collection (Combat)", CollectionCategory.COMBAT, Material.MUTTON),
    BLAZE_ROD("Blaze Rod", "Blaze Rod collection", CollectionCategory.COMBAT, Material.BLAZE_ROD),
    MAGMA_CREAM("Magma Cream", "Magma Cream collection", CollectionCategory.COMBAT, Material.MAGMA_CREAM),
    ENCHANTED_BOOK("Enchanted Book", "Enchanted Book collection", CollectionCategory.ENCHANTING, Material.ENCHANTED_BOOK),
    EXPERIENCE_BOTTLE("Experience Bottle", "Experience Bottle collection", CollectionCategory.ENCHANTING, Material.EXPERIENCE_BOTTLE),
    GLOWSTONE_DUST("Glowstone Dust", "Glowstone Dust collection", CollectionCategory.ALCHEMY, Material.GLOWSTONE_DUST),
    BLAZE_POWDER("Blaze Powder", "Blaze Powder collection", CollectionCategory.ALCHEMY, Material.BLAZE_POWDER),
    FERMENTED_SPIDER_EYE("Fermented Spider Eye", "Fermented Spider Eye collection", CollectionCategory.ALCHEMY, Material.FERMENTED_SPIDER_EYE),
    SUGAR("Sugar", "Sugar collection", CollectionCategory.ALCHEMY, Material.SUGAR),
    RABBIT_FOOT("Rabbit Foot", "Rabbit Foot collection", CollectionCategory.ALCHEMY, Material.RABBIT_FOOT),
    GOLDEN_CARROT("Golden Carrot", "Golden Carrot collection", CollectionCategory.ALCHEMY, Material.GOLDEN_CARROT),
    WOLF_TOOTH("Wolf Tooth", "Wolf Tooth collection", CollectionCategory.TAMING, Material.BONE),
    BONE_MEAL("Bone Meal", "Bone Meal collection", CollectionCategory.TAMING, Material.BONE_MEAL),
    FEATHER("Feather", "Feather collection", CollectionCategory.TAMING, Material.FEATHER),
    LEATHER("Leather", "Leather collection", CollectionCategory.TAMING, Material.LEATHER),
    RABBIT_HIDE("Rabbit Hide", "Rabbit Hide collection", CollectionCategory.TAMING, Material.RABBIT_HIDE),
    WOOL("Wool", "Wool collection", CollectionCategory.TAMING, Material.WHITE_WOOL),
    ENDERMAN("Enderman", "Enderman collection", CollectionCategory.TAMING, Material.ENDER_PEARL),
    SPIDER("Spider", "Spider collection", CollectionCategory.TAMING, Material.SPIDER_EYE),
    CREEPER("Creeper", "Creeper collection", CollectionCategory.TAMING, Material.GUNPOWDER),
    STICK("Stick", "Stick collection", CollectionCategory.CARPENTRY, Material.STICK),
    PLANKS("Planks", "Planks collection", CollectionCategory.CARPENTRY, Material.OAK_PLANKS),
    SAPLING("Sapling", "Sapling collection", CollectionCategory.CARPENTRY, Material.OAK_SAPLING),
    VINE("Vine", "Vine collection", CollectionCategory.CARPENTRY, Material.VINE),
    END_STONE("End Stone", "End Stone collection", CollectionCategory.RUNECRAFTING, Material.END_STONE),
    GLOWSTONE("Glowstone", "Glowstone collection", CollectionCategory.RUNECRAFTING, Material.GLOWSTONE),
    NETHERRACK("Netherrack", "Netherrack collection", CollectionCategory.RUNECRAFTING, Material.NETHERRACK),
    SOUL_SAND("Soul Sand", "Soul Sand collection", CollectionCategory.RUNECRAFTING, Material.SOUL_SAND),
    NETHER_BRICK("Nether Brick", "Nether Brick collection", CollectionCategory.RUNECRAFTING, Material.NETHER_BRICK),
    ENDER_STONE("Ender Stone", "Ender Stone collection", CollectionCategory.RUNECRAFTING, Material.END_STONE),
    PLAYER_HEAD("Player Head", "Player Head collection", CollectionCategory.SOCIAL, Material.PLAYER_HEAD),
    DRAGON_HEAD("Dragon Head", "Dragon Head collection", CollectionCategory.SOCIAL, Material.DRAGON_HEAD),
    WITHER_SKELETON_SKULL("Wither Skeleton Skull", "Wither Skeleton Skull collection", CollectionCategory.SOCIAL, Material.WITHER_SKELETON_SKULL),
    SKELETON_SKULL("Skeleton Skull", "Skeleton Skull collection", CollectionCategory.SOCIAL, Material.SKELETON_SKULL),
    ZOMBIE_HEAD("Zombie Head", "Zombie Head collection", CollectionCategory.SOCIAL, Material.ZOMBIE_HEAD),
    CREEPER_HEAD("Creeper Head", "Creeper Head collection", CollectionCategory.SOCIAL, Material.CREEPER_HEAD),
    WITHER_ROSE("Wither Rose", "Wither Rose collection", CollectionCategory.DUNGEON, Material.WITHER_ROSE);

    private final String name;
    private final String description;
    private final CollectionCategory category;
    private final Material icon;

    CollectionType(String name, String description, CollectionCategory category, Material icon) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CollectionCategory getCategory() {
        return category;
    }

    public Material getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }

    public static CollectionType[] getByCategory(CollectionCategory category) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getCategory() == category)
                .toArray(CollectionType[]::new);
    }

    public static CollectionType getByName(String name) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
