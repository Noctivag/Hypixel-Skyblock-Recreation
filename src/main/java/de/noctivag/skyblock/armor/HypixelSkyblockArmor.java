package de.noctivag.skyblock.armor;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import java.util.*;

public class HypixelSkyblockArmor {
    
    public static class HypixelArmor {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final ArmorRarity rarity;
        private final int baseDefense;
        private final List<String> features;
        private final List<String> requirements;
        
        public HypixelArmor(String name, String displayName, Material material, String description,
                           ArmorRarity rarity, int baseDefense, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.baseDefense = baseDefense;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public ArmorRarity getRarity() { return rarity; }
        public int getBaseDefense() { return baseDefense; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public enum ArmorRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0),
        DIVINE("§bDivine", 20.0),
        SPECIAL("§cSpecial", 50.0),
        VERY_SPECIAL("§4Very Special", 100.0);
        
        private final String displayName;
        private final double multiplier;
        
        ArmorRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static Map<String, HypixelArmor> getAllHypixelArmor() {
        Map<String, HypixelArmor> armor = new HashMap<>();
        
        // Basic Armor Sets
        armor.put("LAPIS_ARMOR", new HypixelArmor(
            "Lapis Armor", "§9Lapis Armor", Material.LAPIS_LAZULI,
            "§7Armor made from lapis lazuli with magical properties.",
            ArmorRarity.RARE, 50, Arrays.asList("§7- Magic Protection", "§7- XP Bonus"),
            Arrays.asList("§7- 1x Lapis Lazuli", "§7- 1x Leather Armor")
        ));
        
        armor.put("ENDER_ARMOR", new HypixelArmor(
            "Ender Armor", "§5Ender Armor", Material.ENDER_PEARL,
            "§7Armor that channels the power of the End.",
            ArmorRarity.EPIC, 75, Arrays.asList("§7- End Power", "§7- Teleportation"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Leather Armor")
        ));
        
        armor.put("GOLEM_ARMOR", new HypixelArmor(
            "Golem Armor", "§7Golem Armor", Material.IRON_INGOT,
            "§7Armor that provides golem-like protection.",
            ArmorRarity.EPIC, 100, Arrays.asList("§7- Golem Protection", "§7- High Defense"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Leather Armor")
        ));
        
        armor.put("DRAGON_ARMOR", new HypixelArmor(
            "Dragon Armor", "§6Dragon Armor", Material.DRAGON_EGG,
            "§7Armor that channels the power of dragons.",
            ArmorRarity.LEGENDARY, 150, Arrays.asList("§7- Dragon Power", "§7- Flight"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Leather Armor")
        ));
        
        armor.put("WITHER_ARMOR", new HypixelArmor(
            "Wither Armor", "§8Wither Armor", Material.WITHER_SKELETON_SKULL,
            "§7Armor that channels the power of the Wither.",
            ArmorRarity.LEGENDARY, 200, Arrays.asList("§7- Wither Power", "§7- Wither Immunity"),
            Arrays.asList("§7- 1x Wither Skull", "§7- 1x Leather Armor")
        ));
        
        armor.put("NECRON_ARMOR", new HypixelArmor(
            "Necron Armor", "§8Necron Armor", Material.NETHERITE_INGOT,
            "§7Armor that channels the power of Necron.",
            ArmorRarity.MYTHIC, 250, Arrays.asList("§7- Necron Power", "§7- Ultimate Defense"),
            Arrays.asList("§7- 1x Netherite Ingot", "§7- 1x Leather Armor")
        ));
        
        armor.put("STORM_ARMOR", new HypixelArmor(
            "Storm Armor", "§bStorm Armor", Material.LIGHTNING_ROD,
            "§7Armor that channels the power of storms.",
            ArmorRarity.MYTHIC, 275, Arrays.asList("§7- Storm Power", "§7- Lightning Immunity"),
            Arrays.asList("§7- 1x Lightning Rod", "§7- 1x Leather Armor")
        ));
        
        armor.put("GOLDOR_ARMOR", new HypixelArmor(
            "Goldor Armor", "§6Goldor Armor", Material.GOLD_INGOT,
            "§7Armor that channels the power of Goldor.",
            ArmorRarity.MYTHIC, 300, Arrays.asList("§7- Goldor Power", "§7- Gold Immunity"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Leather Armor")
        ));
        
        armor.put("MAXOR_ARMOR", new HypixelArmor(
            "Maxor Armor", "§eMaxor Armor", Material.END_CRYSTAL,
            "§7Armor that channels the power of Maxor.",
            ArmorRarity.MYTHIC, 325, Arrays.asList("§7- Maxor Power", "§7- Speed Boost"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Leather Armor")
        ));
        
        armor.put("SADAN_ARMOR", new HypixelArmor(
            "Sadan Armor", "§cSadan Armor", Material.REDSTONE,
            "§7Armor that channels the power of Sadan.",
            ArmorRarity.MYTHIC, 350, Arrays.asList("§7- Sadan Power", "§7- Redstone Immunity"),
            Arrays.asList("§7- 1x Redstone", "§7- 1x Leather Armor")
        ));
        
        // Special Armor Sets
        armor.put("FROZEN_BLAZE_ARMOR", new HypixelArmor(
            "Frozen Blaze Armor", "§bFrozen Blaze Armor", Material.BLUE_ICE,
            "§7Armor that combines ice and fire powers.",
            ArmorRarity.LEGENDARY, 180, Arrays.asList("§7- Ice Fire", "§7- Temperature Immunity"),
            Arrays.asList("§7- 1x Blue Ice", "§7- 1x Blaze Rod")
        ));
        
        armor.put("SHADOW_ASSASSIN_ARMOR", new HypixelArmor(
            "Shadow Assassin Armor", "§5Shadow Assassin Armor", Material.BLACK_DYE,
            "§7Armor that enhances stealth and assassination.",
            ArmorRarity.LEGENDARY, 220, Arrays.asList("§7- Stealth", "§7- Assassination"),
            Arrays.asList("§7- 1x Black Dye", "§7- 1x Leather Armor")
        ));
        
        armor.put("ADAPTIVE_ARMOR", new HypixelArmor(
            "Adaptive Armor", "§aAdaptive Armor", Material.LEATHER_CHESTPLATE,
            "§7Armor that adapts to different situations.",
            ArmorRarity.LEGENDARY, 160, Arrays.asList("§7- Adaptation", "§7- Versatility"),
            Arrays.asList("§7- 1x Chameleon", "§7- 1x Leather Armor")
        ));
        
        armor.put("SPIRIT_ARMOR", new HypixelArmor(
            "Spirit Armor", "§bSpirit Armor", Material.GHAST_TEAR,
            "§7Armor that channels spirit energy.",
            ArmorRarity.LEGENDARY, 190, Arrays.asList("§7- Spirit Energy", "§7- Ghost Immunity"),
            Arrays.asList("§7- 1x Ghast Tear", "§7- 1x Leather Armor")
        ));
        
        armor.put("BONZO_ARMOR", new HypixelArmor(
            "Bonzo Armor", "§aBonzo Armor", Material.LEATHER_CHESTPLATE,
            "§7Armor that provides bonzo-like protection.",
            ArmorRarity.LEGENDARY, 140, Arrays.asList("§7- Bonzo Protection", "§7- Balloon Immunity"),
            Arrays.asList("§7- 1x Balloon", "§7- 1x Leather Armor")
        ));
        
        armor.put("SCARF_ARMOR", new HypixelArmor(
            "Scarf Armor", "§cScarf Armor", Material.RED_WOOL,
            "§7Armor that provides scarf-like protection.",
            ArmorRarity.LEGENDARY, 170, Arrays.asList("§7- Scarf Protection", "§7- Wool Immunity"),
            Arrays.asList("§7- 1x Red Wool", "§7- 1x Leather Armor")
        ));
        
        armor.put("PROFESSOR_ARMOR", new HypixelArmor(
            "Professor Armor", "§eProfessor Armor", Material.BOOK,
            "§7Armor that provides professor-like protection.",
            ArmorRarity.LEGENDARY, 200, Arrays.asList("§7- Professor Protection", "§7- Knowledge Bonus"),
            Arrays.asList("§7- 1x Book", "§7- 1x Leather Armor")
        ));
        
        armor.put("THORN_ARMOR", new HypixelArmor(
            "Thorn Armor", "§2Thorn Armor", Material.CACTUS,
            "§7Armor that provides thorn-like protection.",
            ArmorRarity.LEGENDARY, 210, Arrays.asList("§7- Thorn Protection", "§7- Cactus Immunity"),
            Arrays.asList("§7- 1x Cactus", "§7- 1x Leather Armor")
        ));
        
        armor.put("YETI_ARMOR", new HypixelArmor(
            "Yeti Armor", "§fYeti Armor", Material.SNOW_BLOCK,
            "§7Armor that provides yeti-like protection.",
            ArmorRarity.LEGENDARY, 230, Arrays.asList("§7- Yeti Protection", "§7- Cold Immunity"),
            Arrays.asList("§7- 1x Snow Block", "§7- 1x Leather Armor")
        ));
        
        armor.put("MIDAS_ARMOR", new HypixelArmor(
            "Midas Armor", "§6Midas Armor", Material.GOLD_BLOCK,
            "§7Armor that provides midas-like protection.",
            ArmorRarity.LEGENDARY, 240, Arrays.asList("§7- Midas Protection", "§7- Gold Touch"),
            Arrays.asList("§7- 1x Gold Block", "§7- 1x Leather Armor")
        ));
        
        // Slayer Armor Sets
        armor.put("REVENANT_ARMOR", new HypixelArmor(
            "Revenant Armor", "§2Revenant Armor", Material.ROTTEN_FLESH,
            "§7Armor that provides revenant-like protection.",
            ArmorRarity.LEGENDARY, 120, Arrays.asList("§7- Revenant Protection", "§7- Undead Immunity"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 1x Leather Armor")
        ));
        
        armor.put("TARANTULA_ARMOR", new HypixelArmor(
            "Tarantula Armor", "§8Tarantula Armor", Material.STRING,
            "§7Armor that provides tarantula-like protection.",
            ArmorRarity.LEGENDARY, 130, Arrays.asList("§7- Tarantula Protection", "§7- Spider Immunity"),
            Arrays.asList("§7- 1x String", "§7- 1x Leather Armor")
        ));
        
        armor.put("SVEN_ARMOR", new HypixelArmor(
            "Sven Armor", "§fSven Armor", Material.BONE,
            "§7Armor that provides sven-like protection.",
            ArmorRarity.LEGENDARY, 140, Arrays.asList("§7- Sven Protection", "§7- Wolf Immunity"),
            Arrays.asList("§7- 1x Bone", "§7- 1x Leather Armor")
        ));
        
        armor.put("VOIDGLOOM_ARMOR", new HypixelArmor(
            "Voidgloom Armor", "§5Voidgloom Armor", Material.ENDER_PEARL,
            "§7Armor that provides voidgloom-like protection.",
            ArmorRarity.LEGENDARY, 150, Arrays.asList("§7- Voidgloom Protection", "§7- Enderman Immunity"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Leather Armor")
        ));
        
        armor.put("INFERNO_ARMOR", new HypixelArmor(
            "Inferno Armor", "§6Inferno Armor", Material.BLAZE_ROD,
            "§7Armor that provides inferno-like protection.",
            ArmorRarity.LEGENDARY, 160, Arrays.asList("§7- Inferno Protection", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x Blaze Rod", "§7- 1x Leather Armor")
        ));
        
        // Mining Armor Sets
        armor.put("MINERS_ARMOR", new HypixelArmor(
            "Miners Armor", "§7Miners Armor", Material.IRON_PICKAXE,
            "§7Armor specifically designed for mining operations.",
            ArmorRarity.UNCOMMON, 30, Arrays.asList("§7- Mining Efficiency +25%", "§7- Ore Detection", "§7- Mining Speed Boost"),
            Arrays.asList("§7- 1x Iron Pickaxe", "§7- 1x Leather Armor")
        ));
        
        armor.put("ENCHANTED_MINERS_ARMOR", new HypixelArmor(
            "Enchanted Miners Armor", "§aEnchanted Miners Armor", Material.DIAMOND_PICKAXE,
            "§7Enhanced miners armor with magical properties.",
            ArmorRarity.RARE, 50, Arrays.asList("§7- Mining Efficiency +50%", "§7- Advanced Ore Detection", "§7- Mining Speed Boost +2"),
            Arrays.asList("§7- 1x Diamond Pickaxe", "§7- 1x Enchanted Book")
        ));
        
        armor.put("GEMSTONE_ARMOR", new HypixelArmor(
            "Gemstone Armor", "§dGemstone Armor", Material.EMERALD,
            "§7Armor that enhances gemstone mining capabilities.",
            ArmorRarity.EPIC, 80, Arrays.asList("§7- Gemstone Mining +75%", "§7- Gemstone Detection", "§7- Mining Fortune +3"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Diamond Armor")
        ));
        
        armor.put("DIVANS_ARMOR", new HypixelArmor(
            "Divans Armor", "§bDivans Armor", Material.LAPIS_LAZULI,
            "§7The ultimate mining armor with incredible bonuses.",
            ArmorRarity.LEGENDARY, 120, Arrays.asList("§7- Mining Efficiency +100%", "§7- Ultimate Ore Detection", "§7- Mining Fortune +5"),
            Arrays.asList("§7- 1x Lapis Lazuli", "§7- 1x Diamond Armor")
        ));
        
        armor.put("MINING_SUIT", new HypixelArmor(
            "Mining Suit", "§8Mining Suit", Material.LEATHER_CHESTPLATE,
            "§7A basic mining suit for early game mining.",
            ArmorRarity.COMMON, 15, Arrays.asList("§7- Basic Mining Protection", "§7- Mining Efficiency +10%"),
            Arrays.asList("§7- 1x Leather", "§7- 1x String")
        ));
        
        armor.put("DRILL_ARMOR", new HypixelArmor(
            "Drill Armor", "§eDrill Armor", Material.IRON_INGOT,
            "§7Armor that works with drills for maximum efficiency.",
            ArmorRarity.RARE, 60, Arrays.asList("§7- Drill Compatibility", "§7- Mining Speed +3", "§7- Drill Durability +50%"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Redstone")
        ));
        
        armor.put("PICKONIMBUS_ARMOR", new HypixelArmor(
            "Pickonimbus Armor", "§6Pickonimbus Armor", Material.GOLD_INGOT,
            "§7Armor that enhances pickonimbus effectiveness.",
            ArmorRarity.EPIC, 90, Arrays.asList("§7- Pickonimbus Boost", "§7- Mining Speed +4", "§7- Special Mining Abilities"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Diamond")
        ));
        
        // Farming Armor Sets
        armor.put("FARMERS_ARMOR", new HypixelArmor(
            "Farmers Armor", "§aFarmers Armor", Material.WHEAT,
            "§7Armor specifically designed for farming operations.",
            ArmorRarity.UNCOMMON, 25, Arrays.asList("§7- Farming Efficiency +25%", "§7- Crop Growth Speed", "§7- Harvest Bonus"),
            Arrays.asList("§7- 1x Wheat", "§7- 1x Leather Armor")
        ));
        
        armor.put("ENCHANTED_FARMERS_ARMOR", new HypixelArmor(
            "Enchanted Farmers Armor", "§aEnchanted Farmers Armor", Material.GOLDEN_CARROT,
            "§7Enhanced farmers armor with magical properties.",
            ArmorRarity.RARE, 45, Arrays.asList("§7- Farming Efficiency +50%", "§7- Advanced Crop Growth", "§7- Harvest Bonus +2"),
            Arrays.asList("§7- 1x Golden Carrot", "§7- 1x Enchanted Book")
        ));
        
        armor.put("CROPIE_ARMOR", new HypixelArmor(
            "Cropie Armor", "§2Cropie Armor", Material.CARROT,
            "§7Armor that enhances crop farming capabilities.",
            ArmorRarity.EPIC, 70, Arrays.asList("§7- Crop Farming +75%", "§7- Crop Growth Speed +3", "§7- Harvest Fortune +3"),
            Arrays.asList("§7- 1x Carrot", "§7- 1x Diamond Armor")
        ));
        
        armor.put("SQUASH_ARMOR", new HypixelArmor(
            "Squash Armor", "§6Squash Armor", Material.PUMPKIN,
            "§7Armor that enhances squash farming capabilities.",
            ArmorRarity.EPIC, 85, Arrays.asList("§7- Squash Farming +75%", "§7- Squash Growth Speed +3", "§7- Harvest Fortune +3"),
            Arrays.asList("§7- 1x Pumpkin", "§7- 1x Diamond Armor")
        ));
        
        armor.put("FERMENTO_ARMOR", new HypixelArmor(
            "Fermento Armor", "§aFermento Armor", Material.BROWN_MUSHROOM,
            "§7The ultimate farming armor with incredible bonuses.",
            ArmorRarity.LEGENDARY, 110, Arrays.asList("§7- Farming Efficiency +100%", "§7- Ultimate Crop Growth", "§7- Harvest Fortune +5"),
            Arrays.asList("§7- 1x Brown Mushroom", "§7- 1x Diamond Armor")
        ));
        
        // Fishing Armor Sets
        armor.put("FISHERS_ARMOR", new HypixelArmor(
            "Fishers Armor", "§bFishers Armor", Material.FISHING_ROD,
            "§7Armor specifically designed for fishing operations.",
            ArmorRarity.UNCOMMON, 20, Arrays.asList("§7- Fishing Efficiency +25%", "§7- Fish Detection", "§7- Fishing Speed Boost"),
            Arrays.asList("§7- 1x Fishing Rod", "§7- 1x Leather Armor")
        ));
        
        armor.put("ENCHANTED_FISHERS_ARMOR", new HypixelArmor(
            "Enchanted Fishers Armor", "§bEnchanted Fishers Armor", Material.ENCHANTED_BOOK,
            "§7Enhanced fishers armor with magical properties.",
            ArmorRarity.RARE, 40, Arrays.asList("§7- Fishing Efficiency +50%", "§7- Advanced Fish Detection", "§7- Fishing Speed Boost +2"),
            Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Fishing Rod")
        ));
        
        armor.put("SHARK_ARMOR", new HypixelArmor(
            "Shark Armor", "§9Shark Armor", Material.PRISMARINE_SHARD,
            "§7Armor that enhances shark fishing capabilities.",
            ArmorRarity.EPIC, 65, Arrays.asList("§7- Shark Fishing +75%", "§7- Shark Detection", "§7- Fishing Fortune +3"),
            Arrays.asList("§7- 1x Prismarine Shard", "§7- 1x Diamond Armor")
        ));
        
        armor.put("DIVER_ARMOR", new HypixelArmor(
            "Diver Armor", "§3Diver Armor", Material.HEART_OF_THE_SEA,
            "§7Armor that enhances diving and underwater fishing.",
            ArmorRarity.EPIC, 80, Arrays.asList("§7- Underwater Fishing +75%", "§7- Water Breathing", "§7- Diving Speed +3"),
            Arrays.asList("§7- 1x Heart of the Sea", "§7- 1x Diamond Armor")
        ));
        
        armor.put("SPONGE_ARMOR", new HypixelArmor(
            "Sponge Armor", "§eSponge Armor", Material.SPONGE,
            "§7The ultimate fishing armor with incredible bonuses.",
            ArmorRarity.LEGENDARY, 100, Arrays.asList("§7- Fishing Efficiency +100%", "§7- Ultimate Fish Detection", "§7- Fishing Fortune +5"),
            Arrays.asList("§7- 1x Sponge", "§7- 1x Diamond Armor")
        ));
        
        // Foraging Armor Sets
        armor.put("FORAGERS_ARMOR", new HypixelArmor(
            "Foragers Armor", "§2Foragers Armor", Material.OAK_LOG,
            "§7Armor specifically designed for foraging operations.",
            ArmorRarity.UNCOMMON, 22, Arrays.asList("§7- Foraging Efficiency +25%", "§7- Tree Detection", "§7- Foraging Speed Boost"),
            Arrays.asList("§7- 1x Oak Log", "§7- 1x Leather Armor")
        ));
        
        armor.put("ENCHANTED_FORAGERS_ARMOR", new HypixelArmor(
            "Enchanted Foragers Armor", "§2Enchanted Foragers Armor", Material.ENCHANTED_BOOK,
            "§7Enhanced foragers armor with magical properties.",
            ArmorRarity.RARE, 42, Arrays.asList("§7- Foraging Efficiency +50%", "§7- Advanced Tree Detection", "§7- Foraging Speed Boost +2"),
            Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Oak Log")
        ));
        
        armor.put("LEAF_ARMOR", new HypixelArmor(
            "Leaf Armor", "§aLeaf Armor", Material.OAK_LEAVES,
            "§7Armor that enhances leaf foraging capabilities.",
            ArmorRarity.EPIC, 68, Arrays.asList("§7- Leaf Foraging +75%", "§7- Leaf Detection", "§7- Foraging Fortune +3"),
            Arrays.asList("§7- 1x Oak Leaves", "§7- 1x Diamond Armor")
        ));
        
        armor.put("TREE_CAPITATOR_ARMOR", new HypixelArmor(
            "Tree Capitator Armor", "§6Tree Capitator Armor", Material.IRON_AXE,
            "§7Armor that enhances tree capitator effectiveness.",
            ArmorRarity.EPIC, 88, Arrays.asList("§7- Tree Capitator Boost", "§7- Foraging Speed +4", "§7- Special Foraging Abilities"),
            Arrays.asList("§7- 1x Iron Axe", "§7- 1x Diamond")
        ));
        
        armor.put("FOREST_ARMOR", new HypixelArmor(
            "Forest Armor", "§2Forest Armor", Material.JUNGLE_LOG,
            "§7The ultimate foraging armor with incredible bonuses.",
            ArmorRarity.LEGENDARY, 108, Arrays.asList("§7- Foraging Efficiency +100%", "§7- Ultimate Tree Detection", "§7- Foraging Fortune +5"),
            Arrays.asList("§7- 1x Jungle Log", "§7- 1x Diamond Armor")
        ));
        
        // Combat Armor Sets
        armor.put("COMBAT_ARMOR", new HypixelArmor(
            "Combat Armor", "§cCombat Armor", Material.IRON_SWORD,
            "§7Armor specifically designed for combat operations.",
            ArmorRarity.UNCOMMON, 35, Arrays.asList("§7- Combat Efficiency +25%", "§7- Damage Reduction", "§7- Combat Speed Boost"),
            Arrays.asList("§7- 1x Iron Sword", "§7- 1x Leather Armor")
        ));
        
        armor.put("ENCHANTED_COMBAT_ARMOR", new HypixelArmor(
            "Enchanted Combat Armor", "§cEnchanted Combat Armor", Material.ENCHANTED_BOOK,
            "§7Enhanced combat armor with magical properties.",
            ArmorRarity.RARE, 55, Arrays.asList("§7- Combat Efficiency +50%", "§7- Advanced Damage Reduction", "§7- Combat Speed Boost +2"),
            Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Iron Sword")
        ));
        
        armor.put("BATTLE_ARMOR", new HypixelArmor(
            "Battle Armor", "§4Battle Armor", Material.DIAMOND_SWORD,
            "§7Armor that enhances battle capabilities.",
            ArmorRarity.EPIC, 85, Arrays.asList("§7- Battle Efficiency +75%", "§7- Battle Detection", "§7- Combat Fortune +3"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 1x Diamond Armor")
        ));
        
        armor.put("WARRIOR_ARMOR", new HypixelArmor(
            "Warrior Armor", "§cWarrior Armor", Material.NETHERITE_SWORD,
            "§7The ultimate combat armor with incredible bonuses.",
            ArmorRarity.LEGENDARY, 125, Arrays.asList("§7- Combat Efficiency +100%", "§7- Ultimate Damage Reduction", "§7- Combat Fortune +5"),
            Arrays.asList("§7- 1x Netherite Sword", "§7- 1x Diamond Armor")
        ));
        
        // Special Event Armor Sets
        armor.put("HALLOWEEN_ARMOR", new HypixelArmor(
            "Halloween Armor", "§6Halloween Armor", Material.JACK_O_LANTERN,
            "§7Spooky armor for Halloween events.",
            ArmorRarity.RARE, 60, Arrays.asList("§7- Halloween Bonus", "§7- Spooky Effects", "§7- Event Rewards +50%"),
            Arrays.asList("§7- 1x Jack O'Lantern", "§7- 1x Leather Armor")
        ));
        
        armor.put("CHRISTMAS_ARMOR", new HypixelArmor(
            "Christmas Armor", "§fChristmas Armor", Material.SNOW_BLOCK,
            "§7Festive armor for Christmas events.",
            ArmorRarity.RARE, 60, Arrays.asList("§7- Christmas Bonus", "§7- Festive Effects", "§7- Event Rewards +50%"),
            Arrays.asList("§7- 1x Snow Block", "§7- 1x Leather Armor")
        ));
        
        // Dragon Armor Sets (Complete Collection)
        armor.put("PROTECTOR_DRAGON", new HypixelArmor(
            "Protector Dragon Armor", "§aProtector Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7A dragon armor set that focuses on defense and protection.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +250 Defense", "§7- +50 Health", "§7- Protector's Shield Ability"),
            Arrays.asList("§7- 240x Protector Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        armor.put("OLD_DRAGON", new HypixelArmor(
            "Old Dragon Armor", "§eOld Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7An ancient dragon armor with wisdom and experience.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +200 Defense", "§7- +100 Health", "§7- Ancient Wisdom Ability"),
            Arrays.asList("§7- 240x Old Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        armor.put("WISE_DRAGON", new HypixelArmor(
            "Wise Dragon Armor", "§bWise Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7A dragon armor set that enhances intelligence and magic.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +150 Intelligence", "§7- +50 Defense", "§7- Wise's Intelligence Ability"),
            Arrays.asList("§7- 240x Wise Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        armor.put("YOUNG_DRAGON", new HypixelArmor(
            "Young Dragon Armor", "§fYoung Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7A dragon armor set that provides speed and agility.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +150 Speed", "§7- +50 Defense", "§7- Young's Speed Ability"),
            Arrays.asList("§7- 240x Young Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        armor.put("STRONG_DRAGON", new HypixelArmor(
            "Strong Dragon Armor", "§cStrong Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7A dragon armor set that focuses on raw strength and damage.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +150 Strength", "§7- +50 Defense", "§7- Strong's Strength Ability"),
            Arrays.asList("§7- 240x Strong Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        armor.put("UNSTABLE_DRAGON", new HypixelArmor(
            "Unstable Dragon Armor", "§dUnstable Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7A dragon armor set with chaotic and unpredictable effects.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +100 Critical Chance", "§7- +50 Defense", "§7- Unstable's Chaos Ability"),
            Arrays.asList("§7- 240x Unstable Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        armor.put("SUPERIOR_DRAGON", new HypixelArmor(
            "Superior Dragon Armor", "§6Superior Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7The ultimate dragon armor with superior stats in all areas.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +75 All Stats", "§7- +100 Defense", "§7- Superior's Superiority Ability"),
            Arrays.asList("§7- 240x Superior Dragon Fragment", "§7- 1x Diamond Chestplate")
        ));
        
        // Mining Armor Sets
        armor.put("MINERAL_ARMOR", new HypixelArmor(
            "Mineral Armor", "§6Mineral Armor", Material.IRON_PICKAXE,
            "§7Armor that enhances mining efficiency and provides mining bonuses.",
            ArmorRarity.EPIC, 200, Arrays.asList("§7- +390 Defense", "§7- +50 Speed", "§7- Triple Mining Ability"),
            Arrays.asList("§7- 64x Enchanted Iron", "§7- 32x Enchanted Coal", "§7- 16x Enchanted Redstone")
        ));
        
        armor.put("GLACITE_ARMOR", new HypixelArmor(
            "Glacite Armor", "§bGlacite Armor", Material.BLUE_ICE,
            "§7Armor made from glacite with enhanced mining capabilities.",
            ArmorRarity.EPIC, 250, Arrays.asList("§7- +415 Defense", "§7- +50 Speed", "§7- +50 Mining Speed", "§7- Expert Miner Ability"),
            Arrays.asList("§7- 128x Glacite", "§7- 64x Enchanted Ice", "§7- 32x Enchanted Packed Ice")
        ));
        
        armor.put("SORROW_ARMOR", new HypixelArmor(
            "Sorrow Armor", "§8Sorrow Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor infused with sorrow and despair for ultimate mining power.",
            ArmorRarity.LEGENDARY, 350, Arrays.asList("§7- +500 Defense", "§7- +100 Mining Speed", "§7- +25 Magic Find", "§7- Sorrow's Mining Ability"),
            Arrays.asList("§7- 256x Sorrow", "§7- 128x Enchanted Netherrack", "§7- 64x Enchanted Quartz")
        ));
        
        armor.put("DIVAN_ARMOR", new HypixelArmor(
            "DIVAN's Armor", "§5DIVAN's Armor", Material.NETHERITE_CHESTPLATE,
            "§7The ultimate mining armor created by DIVAN with incredible power.",
            ArmorRarity.LEGENDARY, 500, Arrays.asList("§7- +600 Defense", "§7- +150 Mining Speed", "§7- +50 Magic Find", "§7- DIVAN's Mining Mastery"),
            Arrays.asList("§7- 512x DIVAN's Alloy", "§7- 256x Enchanted Mithril", "§7- 128x Enchanted Titanium")
        ));
        
        // Combat Armor Sets
        armor.put("SHADOW_ASSASSIN_ARMOR", new HypixelArmor(
            "Shadow Assassin Armor", "§5Shadow Assassin Armor", Material.LEATHER_CHESTPLATE,
            "§7Armor worn by shadow assassins with stealth and agility bonuses.",
            ArmorRarity.LEGENDARY, 300, Arrays.asList("§7- +400 Health", "§7- +300 Defense", "§7- +50 Speed", "§7- Shadow Assassin Ability"),
            Arrays.asList("§7- 128x Shadow Assassin Fragment", "§7- 64x Enchanted Leather", "§7- 32x Enchanted String")
        ));
        
        armor.put("NECRON_ARMOR", new HypixelArmor(
            "Necron's Armor", "§8Necron's Armor", Material.NETHERITE_CHESTPLATE,
            "§7The armor of Necron, the Wither Lord, with incredible combat power.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +600 Health", "§7- +500 Defense", "§7- +150 Strength", "§7- Necron's Wither Impact"),
            Arrays.asList("§7- 256x Necron's Handle", "§7- 128x Wither Catalyst", "§7- 64x Enchanted Nether Star")
        ));
        
        armor.put("STORM_ARMOR", new HypixelArmor(
            "Storm's Armor", "§bStorm's Armor", Material.NETHERITE_CHESTPLATE,
            "§7The armor of Storm, the Wither Lord, with incredible magic power.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +500 Health", "§7- +400 Defense", "§7- +200 Intelligence", "§7- Storm's Wither Impact"),
            Arrays.asList("§7- 256x Storm's Handle", "§7- 128x Wither Catalyst", "§7- 64x Enchanted Nether Star")
        ));
        
        armor.put("GOLDOR_ARMOR", new HypixelArmor(
            "Goldor's Armor", "§6Goldor's Armor", Material.NETHERITE_CHESTPLATE,
            "§7The armor of Goldor, the Wither Lord, with incredible defense.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +700 Health", "§7- +600 Defense", "§7- +100 Strength", "§7- Goldor's Wither Impact"),
            Arrays.asList("§7- 256x Goldor's Handle", "§7- 128x Wither Catalyst", "§7- 64x Enchanted Nether Star")
        ));
        
        armor.put("MAXOR_ARMOR", new HypixelArmor(
            "Maxor's Armor", "§fMaxor's Armor", Material.NETHERITE_CHESTPLATE,
            "§7The armor of Maxor, the Wither Lord, with incredible speed.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +500 Health", "§7- +400 Defense", "§7- +150 Speed", "§7- Maxor's Wither Impact"),
            Arrays.asList("§7- 256x Maxor's Handle", "§7- 128x Wither Catalyst", "§7- 64x Enchanted Nether Star")
        ));
        
        // Fishing Armor Sets
        armor.put("SPONGE_ARMOR", new HypixelArmor(
            "Sponge Armor", "§eSponge Armor", Material.SPONGE,
            "§7Armor that enhances fishing capabilities and sea creature encounters.",
            ArmorRarity.EPIC, 150, Arrays.asList("§7- +200 Health", "§7- +150 Defense", "§7- +10 Sea Creature Chance", "§7- Sponge Absorption"),
            Arrays.asList("§7- 64x Enchanted Sponge", "§7- 32x Enchanted Prismarine", "§7- 16x Enchanted Sea Lantern")
        ));
        
        armor.put("SHARK_SCALE_ARMOR", new HypixelArmor(
            "Shark Scale Armor", "§9Shark Scale Armor", Material.NAUTILUS_SHELL,
            "§7Armor made from shark scales with incredible water-based abilities.",
            ArmorRarity.LEGENDARY, 250, Arrays.asList("§7- +545 Health", "§7- +545 Defense", "§7- +10 Sea Creature Chance", "§7- Absorb & Reflect Abilities"),
            Arrays.asList("§7- 128x Shark Fin", "§7- 64x Enchanted Prismarine", "§7- 32x Enchanted Sponge")
        ));
        
        armor.put("FROZEN_BLAZE_ARMOR", new HypixelArmor(
            "Frozen Blaze Armor", "§bFrozen Blaze Armor", Material.BLUE_ICE,
            "§7Armor that combines ice and fire for incredible combat power.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +400 Health", "§7- +400 Defense", "§7- +150 Strength", "§7- Frozen Blaze Ability"),
            Arrays.asList("§7- 256x Frozen Blaze Rod", "§7- 128x Enchanted Blaze Rod", "§7- 64x Enchanted Ice")
        ));
        
        // Event and Seasonal Armor Sets
        armor.put("SPOOKY_ARMOR", new HypixelArmor(
            "Spooky Armor", "§6Spooky Armor", Material.JACK_O_LANTERN,
            "§7Spooky armor that enhances Halloween event rewards.",
            ArmorRarity.EPIC, 135, Arrays.asList("§7- +135 Defense", "§7- +5% Candy Chance per piece", "§7- Candy Man Set Bonus"),
            Arrays.asList("§7- 64x Enchanted Pumpkin", "§7- 32x Enchanted Sugar", "§7- 16x Enchanted Cookie")
        ));
        
        armor.put("SNOW_SUIT", new HypixelArmor(
            "Snow Suit", "§fSnow Suit", Material.SNOW_BLOCK,
            "§7A suit perfect for winter activities and Jerry's Workshop.",
            ArmorRarity.EPIC, 125, Arrays.asList("§7- +310 Health", "§7- +125 Defense", "§7- Cold Thumb Ability", "§7- +5% Gift Chance per piece"),
            Arrays.asList("§7- 64x Enchanted Snow Block", "§7- 32x Enchanted Ice", "§7- 16x Enchanted Packed Ice")
        ));
        
        armor.put("BAT_ARMOR", new HypixelArmor(
            "Bat Person Armor", "§8Bat Person Armor", Material.BLACK_WOOL,
            "§7Armor that transforms you into a bat person with unique abilities.",
            ArmorRarity.LEGENDARY, 300, Arrays.asList("§7- +400 Health", "§7- +300 Defense", "§7- +50 Speed", "§7- Bat Transformation"),
            Arrays.asList("§7- 128x Bat Talisman", "§7- 64x Enchanted Leather", "§7- 32x Enchanted String")
        ));
        
        // Crimson Isle Armor Sets
        armor.put("CRIMSON_ARMOR", new HypixelArmor(
            "Crimson Armor", "§cCrimson Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor forged in the depths of the Crimson Isle with fire resistance.",
            ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- +500 Health", "§7- +400 Defense", "§7- +100 Strength", "§7- Crimson Fire Resistance"),
            Arrays.asList("§7- 256x Crimson Essence", "§7- 128x Enchanted Netherrack", "§7- 64x Enchanted Quartz")
        ));
        
        armor.put("TERROR_ARMOR", new HypixelArmor(
            "Terror Armor", "§4Terror Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that strikes terror into the hearts of enemies.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +600 Health", "§7- +500 Defense", "§7- +150 Strength", "§7- Terror Strike Ability"),
            Arrays.asList("§7- 256x Terror Essence", "§7- 128x Enchanted Netherrack", "§7- 64x Enchanted Quartz")
        ));
        
        armor.put("AURORA_ARMOR", new HypixelArmor(
            "Aurora Armor", "§dAurora Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that channels the power of the aurora borealis.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +500 Health", "§7- +400 Defense", "§7- +200 Intelligence", "§7- Aurora Magic Ability"),
            Arrays.asList("§7- 256x Aurora Essence", "§7- 128x Enchanted Netherrack", "§7- 64x Enchanted Quartz")
        ));
        
        armor.put("HOLLOW_ARMOR", new HypixelArmor(
            "Hollow Armor", "§8Hollow Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that embodies the emptiness and void of the nether.",
            ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- +550 Health", "§7- +450 Defense", "§7- +125 Strength", "§7- Hollow Void Ability"),
            Arrays.asList("§7- 256x Hollow Essence", "§7- 128x Enchanted Netherrack", "§7- 64x Enchanted Quartz")
        ));
        
        // Special and Unique Armor Sets
        armor.put("EMERALD_ARMOR", new HypixelArmor(
            "Emerald Armor", "§aEmerald Armor", Material.EMERALD,
            "§7Armor made from pure emerald with wealth-enhancing properties.",
            ArmorRarity.EPIC, 200, Arrays.asList("§7- +300 Health", "§7- +200 Defense", "§7- +25% Coin Bonus", "§7- Emerald Wealth"),
            Arrays.asList("§7- 64x Enchanted Emerald", "§7- 32x Enchanted Gold", "§7- 16x Enchanted Diamond")
        ));
        
        armor.put("LEAFLET_ARMOR", new HypixelArmor(
            "Leaflet Armor", "§aLeaflet Armor", Material.OAK_LEAVES,
            "§7Armor made from enchanted leaves with nature-based abilities.",
            ArmorRarity.RARE, 100, Arrays.asList("§7- +150 Health", "§7- +100 Defense", "§7- +25 Speed", "§7- Leaflet Nature Power"),
            Arrays.asList("§7- 32x Enchanted Oak Leaves", "§7- 16x Enchanted Stick", "§7- 8x Enchanted String")
        ));
        
        armor.put("ROSETTA_ARMOR", new HypixelArmor(
            "Rosetta's Armor", "§dRosetta's Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor created by Rosetta with unique magical properties.",
            ArmorRarity.LEGENDARY, 350, Arrays.asList("§7- +400 Health", "§7- +350 Defense", "§7- +100 Intelligence", "§7- Rosetta's Magic"),
            Arrays.asList("§7- 128x Rosetta's Essence", "§7- 64x Enchanted Netherrack", "§7- 32x Enchanted Quartz")
        ));
        
        armor.put("EASTER_ARMOR", new HypixelArmor(
            "Easter Armor", "§eEaster Armor", Material.EGG,
            "§7Colorful armor for Easter events.",
            ArmorRarity.RARE, 60, Arrays.asList("§7- Easter Bonus", "§7- Colorful Effects", "§7- Event Rewards +50%"),
            Arrays.asList("§7- 1x Egg", "§7- 1x Leather Armor")
        ));
        
        armor.put("VALENTINES_ARMOR", new HypixelArmor(
            "Valentines Armor", "§dValentines Armor", Material.POPPY,
            "§7Romantic armor for Valentine's events.",
            ArmorRarity.RARE, 60, Arrays.asList("§7- Valentines Bonus", "§7- Romantic Effects", "§7- Event Rewards +50%"),
            Arrays.asList("§7- 1x Poppy", "§7- 1x Leather Armor")
        ));
        
        armor.put("NEW_YEAR_ARMOR", new HypixelArmor(
            "New Year Armor", "§6New Year Armor", Material.FIREWORK_ROCKET,
            "§7Celebratory armor for New Year events.",
            ArmorRarity.RARE, 60, Arrays.asList("§7- New Year Bonus", "§7- Celebratory Effects", "§7- Event Rewards +50%"),
            Arrays.asList("§7- 1x Firework Rocket", "§7- 1x Leather Armor")
        ));
        
        return armor;
    }
}
