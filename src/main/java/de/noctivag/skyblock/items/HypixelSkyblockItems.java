package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import java.util.*;

public class HypixelSkyblockItems {
    
    public static class HypixelItem {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final ItemRarity rarity;
        private final int baseDamage;
        private final List<String> features;
        private final List<String> requirements;
        
        public HypixelItem(String name, String displayName, Material material, String description,
                          ItemRarity rarity, int baseDamage, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.baseDamage = baseDamage;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public ItemRarity getRarity() { return rarity; }
        public int getBaseDamage() { return baseDamage; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public enum ItemRarity {
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
        
        ItemRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static Map<String, HypixelItem> getAllHypixelItems() {
        Map<String, HypixelItem> items = new HashMap<>();
        
        // Swords
        items.put("ASPECT_OF_THE_END", new HypixelItem(
            "Aspect of the End", "§aAspect of the End", Material.DIAMOND_SWORD,
            "§7Teleport behind enemies and deal massive damage.",
            ItemRarity.RARE, 100, Arrays.asList("§7- Teleport Ability", "§7- High Damage"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Diamond Sword")
        ));
        
        items.put("ASPECT_OF_THE_DRAGONS", new HypixelItem(
            "Aspect of the Dragons", "§6Aspect of the Dragons", Material.DIAMOND_SWORD,
            "§7Summon dragon energy to devastate your enemies.",
            ItemRarity.LEGENDARY, 225, Arrays.asList("§7- Dragon Breath", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Diamond Sword")
        ));
        
        items.put("HYPERION", new HypixelItem(
            "Hyperion", "§dHyperion", Material.NETHERITE_SWORD,
            "§7The most powerful sword in existence.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Teleportation", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("SCYLLA", new HypixelItem(
            "Scylla", "§dScylla", Material.NETHERITE_SWORD,
            "§7A powerful sword with healing abilities.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Healing", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("ASTRAEA", new HypixelItem(
            "Astraea", "§dAstraea", Material.NETHERITE_SWORD,
            "§7A defensive sword with protection abilities.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Defense", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("VALKYRIE", new HypixelItem(
            "Valkyrie", "§dValkyrie", Material.NETHERITE_SWORD,
            "§7A balanced sword with all-around abilities.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Balance", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("MIDAS_SWORD", new HypixelItem(
            "Midas Sword", "§6Midas Sword", Material.GOLDEN_SWORD,
            "§7A sword forged from pure gold with incredible power.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Gold Touch", "§7- Wealth Bonus"),
            Arrays.asList("§7- 1x Gold Block", "§7- 1x Golden Sword")
        ));
        
        items.put("SHADOW_FURY", new HypixelItem(
            "Shadow Fury", "§5Shadow Fury", Material.DIAMOND_SWORD,
            "§7A sword that strikes from the shadows.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Shadow Strike", "§7- High Damage"),
            Arrays.asList("§7- 1x Shadow Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("LIVID_DAGGER", new HypixelItem(
            "Livid Dagger", "§5Livid Dagger", Material.DIAMOND_SWORD,
            "§7A dagger that strikes with precision.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Precision Strike", "§7- High Critical"),
            Arrays.asList("§7- 1x Livid Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("GIANTS_SWORD", new HypixelItem(
            "Giants Sword", "§6Giants Sword", Material.DIAMOND_SWORD,
            "§7A massive sword that deals incredible damage.",
            ItemRarity.LEGENDARY, 400, Arrays.asList("§7- Giant Strike", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Giant Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("NECROMANCER_SWORD", new HypixelItem(
            "Necromancer Sword", "§5Necromancer Sword", Material.DIAMOND_SWORD,
            "§7A sword that commands the undead.",
            ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Undead Command", "§7- Necromancy"),
            Arrays.asList("§7- 1x Necromancer Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("WITHER_BLADE", new HypixelItem(
            "Wither Blade", "§dWither Blade", Material.NETHERITE_SWORD,
            "§7The ultimate weapon of destruction.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Ultimate Power"),
            Arrays.asList("§7- 1x Wither Fragment", "§7- 1x Netherite Sword")
        ));
        
        // Bows
        items.put("BONEMERANG", new HypixelItem(
            "Bonemerang", "§fBonemerang", Material.BOW,
            "§7A bow that returns to you after firing.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Returning Arrow", "§7- High Damage"),
            Arrays.asList("§7- 1x Bone", "§7- 1x Bow")
        ));
        
        items.put("LAST_BREATH", new HypixelItem(
            "Last Breath", "§cLast Breath", Material.BOW,
            "§7A bow that takes the last breath of your enemies.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Last Breath", "§7- High Damage"),
            Arrays.asList("§7- 1x Breath Fragment", "§7- 1x Bow")
        ));
        
        items.put("MACHINE_GUN_BOW", new HypixelItem(
            "Machine Gun Bow", "§eMachine Gun Bow", Material.BOW,
            "§7A bow that fires arrows like a machine gun.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Rapid Fire", "§7- High Damage"),
            Arrays.asList("§7- 1x Machine Fragment", "§7- 1x Bow")
        ));
        
        items.put("SAVANNA_BOW", new HypixelItem(
            "Savanna Bow", "§aSavanna Bow", Material.BOW,
            "§7A bow that harnesses the power of the savanna.",
            ItemRarity.LEGENDARY, 275, Arrays.asList("§7- Savanna Power", "§7- High Damage"),
            Arrays.asList("§7- 1x Savanna Fragment", "§7- 1x Bow")
        ));
        
        items.put("END_STONE_BOW", new HypixelItem(
            "End Stone Bow", "§5End Stone Bow", Material.BOW,
            "§7A bow that channels the power of the End.",
            ItemRarity.LEGENDARY, 325, Arrays.asList("§7- End Power", "§7- High Damage"),
            Arrays.asList("§7- 1x End Stone", "§7- 1x Bow")
        ));
        
        items.put("HURRICANE_BOW", new HypixelItem(
            "Hurricane Bow", "§bHurricane Bow", Material.BOW,
            "§7A bow that creates hurricanes with its arrows.",
            ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Hurricane", "§7- High Damage"),
            Arrays.asList("§7- 1x Hurricane Fragment", "§7- 1x Bow")
        ));
        
        items.put("DEATH_BOW", new HypixelItem(
            "Death Bow", "§4Death Bow", Material.BOW,
            "§7A bow that brings death to your enemies.",
            ItemRarity.LEGENDARY, 400, Arrays.asList("§7- Death Strike", "§7- High Damage"),
            Arrays.asList("§7- 1x Death Fragment", "§7- 1x Bow")
        ));
        
        // Staffs
        items.put("BONZO_STAFF", new HypixelItem(
            "Bonzo Staff", "§aBonzo Staff", Material.BLAZE_ROD,
            "§7A staff that summons bonzo balloons.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Bonzo Balloons", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Bonzo Fragment", "§7- 1x Blaze Rod")
        ));
        
        items.put("SCARF_STAFF", new HypixelItem(
            "Scarf Staff", "§cScarf Staff", Material.BLAZE_ROD,
            "§7A staff that creates scarf illusions.",
            ItemRarity.LEGENDARY, 175, Arrays.asList("§7- Scarf Illusions", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Scarf Fragment", "§7- 1x Blaze Rod")
        ));
        
        items.put("PROFESSOR_STAFF", new HypixelItem(
            "Professor Staff", "§eProfessor Staff", Material.BLAZE_ROD,
            "§7A staff that channels professor magic.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Professor Magic", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Professor Fragment", "§7- 1x Blaze Rod")
        ));
        
        items.put("THORN_STAFF", new HypixelItem(
            "Thorn Staff", "§2Thorn Staff", Material.BLAZE_ROD,
            "§7A staff that creates thorn barriers.",
            ItemRarity.LEGENDARY, 225, Arrays.asList("§7- Thorn Barriers", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Thorn Fragment", "§7- 1x Blaze Rod")
        ));
        
        items.put("SPIRIT_SCEPTRE", new HypixelItem(
            "Spirit Sceptre", "§bSpirit Sceptre", Material.BLAZE_ROD,
            "§7A sceptre that channels spirit energy.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Spirit Energy", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Spirit Fragment", "§7- 1x Blaze Rod")
        ));
        
        items.put("MAGE_STAFF", new HypixelItem(
            "Mage Staff", "§dMage Staff", Material.BLAZE_ROD,
            "§7A staff that channels mage energy.",
            ItemRarity.LEGENDARY, 275, Arrays.asList("§7- Mage Energy", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Mage Fragment", "§7- 1x Blaze Rod")
        ));
        
        items.put("YETI_SWORD", new HypixelItem(
            "Yeti Sword", "§fYeti Sword", Material.DIAMOND_SWORD,
            "§7A sword that channels yeti power.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Yeti Power", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Yeti Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("MIDAS_STAFF", new HypixelItem(
            "Midas Staff", "§6Midas Staff", Material.BLAZE_ROD,
            "§7A staff that turns enemies to gold.",
            ItemRarity.LEGENDARY, 325, Arrays.asList("§7- Gold Touch", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Midas Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Tools
        items.put("PICKONIMBUS", new HypixelItem(
            "Pickonimbus", "§ePickonimbus", Material.DIAMOND_PICKAXE,
            "§7A pickaxe that mines with the power of clouds.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Cloud Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Cloud Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("DRILL_ENGINE", new HypixelItem(
            "Drill Engine", "§7Drill Engine", Material.DIAMOND_PICKAXE,
            "§7A drill that mines with engine power.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Engine Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Engine Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("GEMSTONE_DRILL", new HypixelItem(
            "Gemstone Drill", "§bGemstone Drill", Material.DIAMOND_PICKAXE,
            "§7A drill that mines gemstones efficiently.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Gemstone Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("GEMSTONE_GAUNTLET", new HypixelItem(
            "Gemstone Gauntlet", "§bGemstone Gauntlet", Material.DIAMOND_PICKAXE,
            "§7A gauntlet that mines gemstones with power.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Gemstone Power", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("TREE_CAPITATOR", new HypixelItem(
            "Tree Capitator", "§aTree Capitator", Material.DIAMOND_AXE,
            "§7An axe that cuts down entire trees.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Tree Cutting", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tree Fragment", "§7- 1x Diamond Axe")
        ));
        
        items.put("JUNGLE_AXE", new HypixelItem(
            "Jungle Axe", "§2Jungle Axe", Material.DIAMOND_AXE,
            "§7An axe that cuts through jungle trees.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Jungle Cutting", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Jungle Fragment", "§7- 1x Diamond Axe")
        ));
        
        items.put("FORAGING_AXE", new HypixelItem(
            "Foraging Axe", "§6Foraging Axe", Material.DIAMOND_AXE,
            "§7An axe that forages with efficiency.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Foraging", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Foraging Fragment", "§7- 1x Diamond Axe")
        ));
        
        items.put("HOE_OF_GREATER_TILLING", new HypixelItem(
            "Hoe of Greater Tilling", "§eHoe of Greater Tilling", Material.DIAMOND_HOE,
            "§7A hoe that tills with greater efficiency.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Greater Tilling", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Diamond Hoe")
        ));
        
        items.put("HOE_OF_THE_TILLING", new HypixelItem(
            "Hoe of the Tilling", "§eHoe of the Tilling", Material.DIAMOND_HOE,
            "§7A hoe that tills with ultimate efficiency.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Ultimate Tilling", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Diamond Hoe")
        ));
        
        // Fishing Rods
        items.put("ROD_OF_THE_SEA", new HypixelItem(
            "Rod of the Sea", "§bRod of the Sea", Material.FISHING_ROD,
            "§7A rod that fishes from the depths of the sea.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Sea Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Sea Fragment", "§7- 1x Fishing Rod")
        ));
        
        items.put("ROD_OF_LEGENDS", new HypixelItem(
            "Rod of Legends", "§6Rod of Legends", Material.FISHING_ROD,
            "§7A rod that fishes legendary creatures.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Legendary Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Legend Fragment", "§7- 1x Fishing Rod")
        ));
        
        items.put("ROD_OF_CHAMPIONS", new HypixelItem(
            "Rod of Champions", "§cRod of Champions", Material.FISHING_ROD,
            "§7A rod that fishes champion creatures.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Champion Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Champion Fragment", "§7- 1x Fishing Rod")
        ));
        
        // Additional Swords
        items.put("FLOWER_OF_TRUTH", new HypixelItem(
            "Flower of Truth", "§dFlower of Truth", Material.DIAMOND_SWORD,
            "§7A sword that blooms with power.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Blooming Power", "§7- High Damage"),
            Arrays.asList("§7- 1x Flower Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("FEL_SWORD", new HypixelItem(
            "Fel Sword", "§5Fel Sword", Material.DIAMOND_SWORD,
            "§7A sword that channels fel energy.",
            ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Fel Energy", "§7- High Damage"),
            Arrays.asList("§7- 1x Fel Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("SPIRIT_SCEPTRE", new HypixelItem(
            "Spirit Sceptre", "§bSpirit Sceptre", Material.DIAMOND_SWORD,
            "§7A sceptre that channels spirit energy.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Spirit Energy", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Spirit Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("MAGE_STAFF", new HypixelItem(
            "Mage Staff", "§dMage Staff", Material.DIAMOND_SWORD,
            "§7A staff that channels mage energy.",
            ItemRarity.LEGENDARY, 275, Arrays.asList("§7- Mage Energy", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Mage Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("YETI_SWORD", new HypixelItem(
            "Yeti Sword", "§fYeti Sword", Material.DIAMOND_SWORD,
            "§7A sword that channels yeti power.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Yeti Power", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Yeti Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("MIDAS_STAFF", new HypixelItem(
            "Midas Staff", "§6Midas Staff", Material.DIAMOND_SWORD,
            "§7A staff that turns enemies to gold.",
            ItemRarity.LEGENDARY, 325, Arrays.asList("§7- Gold Touch", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Midas Fragment", "§7- 1x Diamond Sword")
        ));
        
        // Additional Bows
        items.put("BONEMERANG", new HypixelItem(
            "Bonemerang", "§fBonemerang", Material.BOW,
            "§7A bow that returns to you after firing.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Returning Arrow", "§7- High Damage"),
            Arrays.asList("§7- 1x Bone", "§7- 1x Bow")
        ));
        
        items.put("LAST_BREATH", new HypixelItem(
            "Last Breath", "§cLast Breath", Material.BOW,
            "§7A bow that takes the last breath of your enemies.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Last Breath", "§7- High Damage"),
            Arrays.asList("§7- 1x Breath Fragment", "§7- 1x Bow")
        ));
        
        items.put("MACHINE_GUN_BOW", new HypixelItem(
            "Machine Gun Bow", "§eMachine Gun Bow", Material.BOW,
            "§7A bow that fires arrows like a machine gun.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Rapid Fire", "§7- High Damage"),
            Arrays.asList("§7- 1x Machine Fragment", "§7- 1x Bow")
        ));
        
        items.put("SAVANNA_BOW", new HypixelItem(
            "Savanna Bow", "§aSavanna Bow", Material.BOW,
            "§7A bow that harnesses the power of the savanna.",
            ItemRarity.LEGENDARY, 275, Arrays.asList("§7- Savanna Power", "§7- High Damage"),
            Arrays.asList("§7- 1x Savanna Fragment", "§7- 1x Bow")
        ));
        
        items.put("END_STONE_BOW", new HypixelItem(
            "End Stone Bow", "§5End Stone Bow", Material.BOW,
            "§7A bow that channels the power of the End.",
            ItemRarity.LEGENDARY, 325, Arrays.asList("§7- End Power", "§7- High Damage"),
            Arrays.asList("§7- 1x End Stone", "§7- 1x Bow")
        ));
        
        items.put("HURRICANE_BOW", new HypixelItem(
            "Hurricane Bow", "§bHurricane Bow", Material.BOW,
            "§7A bow that creates hurricanes with its arrows.",
            ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Hurricane", "§7- High Damage"),
            Arrays.asList("§7- 1x Hurricane Fragment", "§7- 1x Bow")
        ));
        
        items.put("DEATH_BOW", new HypixelItem(
            "Death Bow", "§4Death Bow", Material.BOW,
            "§7A bow that brings death to your enemies.",
            ItemRarity.LEGENDARY, 400, Arrays.asList("§7- Death Strike", "§7- High Damage"),
            Arrays.asList("§7- 1x Death Fragment", "§7- 1x Bow")
        ));
        
        // Additional Tools
        items.put("PICKONIMBUS", new HypixelItem(
            "Pickonimbus", "§ePickonimbus", Material.DIAMOND_PICKAXE,
            "§7A pickaxe that mines with the power of clouds.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Cloud Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Cloud Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("DRILL_ENGINE", new HypixelItem(
            "Drill Engine", "§7Drill Engine", Material.DIAMOND_PICKAXE,
            "§7A drill that mines with engine power.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Engine Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Engine Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("GEMSTONE_DRILL", new HypixelItem(
            "Gemstone Drill", "§bGemstone Drill", Material.DIAMOND_PICKAXE,
            "§7A drill that mines gemstones efficiently.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Gemstone Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("GEMSTONE_GAUNTLET", new HypixelItem(
            "Gemstone Gauntlet", "§bGemstone Gauntlet", Material.DIAMOND_PICKAXE,
            "§7A gauntlet that mines gemstones with power.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Gemstone Power", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        items.put("TREE_CAPITATOR", new HypixelItem(
            "Tree Capitator", "§aTree Capitator", Material.DIAMOND_AXE,
            "§7An axe that cuts down entire trees.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Tree Cutting", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tree Fragment", "§7- 1x Diamond Axe")
        ));
        
        items.put("JUNGLE_AXE", new HypixelItem(
            "Jungle Axe", "§2Jungle Axe", Material.DIAMOND_AXE,
            "§7An axe that cuts through jungle trees.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Jungle Cutting", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Jungle Fragment", "§7- 1x Diamond Axe")
        ));
        
        items.put("FORAGING_AXE", new HypixelItem(
            "Foraging Axe", "§6Foraging Axe", Material.DIAMOND_AXE,
            "§7An axe that forages with efficiency.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Foraging", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Foraging Fragment", "§7- 1x Diamond Axe")
        ));
        
        items.put("HOE_OF_GREATER_TILLING", new HypixelItem(
            "Hoe of Greater Tilling", "§eHoe of Greater Tilling", Material.DIAMOND_HOE,
            "§7A hoe that tills with greater efficiency.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Greater Tilling", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Diamond Hoe")
        ));
        
        items.put("HOE_OF_THE_TILLING", new HypixelItem(
            "Hoe of the Tilling", "§eHoe of the Tilling", Material.DIAMOND_HOE,
            "§7A hoe that tills with ultimate efficiency.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Ultimate Tilling", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Diamond Hoe")
        ));
        
        // Additional Fishing Rods
        items.put("ROD_OF_THE_SEA", new HypixelItem(
            "Rod of the Sea", "§bRod of the Sea", Material.FISHING_ROD,
            "§7A rod that fishes from the depths of the sea.",
            ItemRarity.LEGENDARY, 100, Arrays.asList("§7- Sea Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Sea Fragment", "§7- 1x Fishing Rod")
        ));
        
        items.put("ROD_OF_LEGENDS", new HypixelItem(
            "Rod of Legends", "§6Rod of Legends", Material.FISHING_ROD,
            "§7A rod that fishes legendary creatures.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Legendary Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Legend Fragment", "§7- 1x Fishing Rod")
        ));
        
        items.put("ROD_OF_CHAMPIONS", new HypixelItem(
            "Rod of Champions", "§cRod of Champions", Material.FISHING_ROD,
            "§7A rod that fishes champion creatures.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Champion Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Champion Fragment", "§7- 1x Fishing Rod")
        ));
        
        // Special Items
        items.put("ASPECT_OF_THE_VOID", new HypixelItem(
            "Aspect of the Void", "§5Aspect of the Void", Material.NETHERITE_SWORD,
            "§7A sword that channels the power of the void.",
            ItemRarity.MYTHIC, 400, Arrays.asList("§7- Void Power", "§7- Teleportation"),
            Arrays.asList("§7- 1x Void Fragment", "§7- 1x Netherite Sword")
        ));
        
        items.put("ASPECT_OF_THE_END", new HypixelItem(
            "Aspect of the End", "§aAspect of the End", Material.DIAMOND_SWORD,
            "§7Teleport behind enemies and deal massive damage.",
            ItemRarity.RARE, 100, Arrays.asList("§7- Teleport Ability", "§7- High Damage"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Diamond Sword")
        ));
        
        items.put("ASPECT_OF_THE_DRAGONS", new HypixelItem(
            "Aspect of the Dragons", "§6Aspect of the Dragons", Material.DIAMOND_SWORD,
            "§7Summon dragon energy to devastate your enemies.",
            ItemRarity.LEGENDARY, 225, Arrays.asList("§7- Dragon Breath", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Diamond Sword")
        ));
        
        items.put("HYPERION", new HypixelItem(
            "Hyperion", "§dHyperion", Material.NETHERITE_SWORD,
            "§7The most powerful sword in existence.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Teleportation", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("SCYLLA", new HypixelItem(
            "Scylla", "§dScylla", Material.NETHERITE_SWORD,
            "§7A powerful sword with healing abilities.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Healing", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("ASTRAEA", new HypixelItem(
            "Astraea", "§dAstraea", Material.NETHERITE_SWORD,
            "§7A defensive sword with protection abilities.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Defense", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("VALKYRIE", new HypixelItem(
            "Valkyrie", "§dValkyrie", Material.NETHERITE_SWORD,
            "§7A balanced sword with all-around abilities.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Balance", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        items.put("MIDAS_SWORD", new HypixelItem(
            "Midas Sword", "§6Midas Sword", Material.GOLDEN_SWORD,
            "§7A sword forged from pure gold with incredible power.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Gold Touch", "§7- Wealth Bonus"),
            Arrays.asList("§7- 1x Gold Block", "§7- 1x Golden Sword")
        ));
        
        items.put("SHADOW_FURY", new HypixelItem(
            "Shadow Fury", "§5Shadow Fury", Material.DIAMOND_SWORD,
            "§7A sword that strikes from the shadows.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Shadow Strike", "§7- High Damage"),
            Arrays.asList("§7- 1x Shadow Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("LIVID_DAGGER", new HypixelItem(
            "Livid Dagger", "§5Livid Dagger", Material.DIAMOND_SWORD,
            "§7A dagger that strikes with precision.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Precision Strike", "§7- High Critical"),
            Arrays.asList("§7- 1x Livid Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("GIANTS_SWORD", new HypixelItem(
            "Giants Sword", "§6Giants Sword", Material.DIAMOND_SWORD,
            "§7A massive sword that deals incredible damage.",
            ItemRarity.LEGENDARY, 400, Arrays.asList("§7- Giant Strike", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Giant Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("NECROMANCER_SWORD", new HypixelItem(
            "Necromancer Sword", "§5Necromancer Sword", Material.DIAMOND_SWORD,
            "§7A sword that commands the undead.",
            ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Undead Command", "§7- Necromancy"),
            Arrays.asList("§7- 1x Necromancer Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("WITHER_BLADE", new HypixelItem(
            "Wither Blade", "§dWither Blade", Material.NETHERITE_SWORD,
            "§7The ultimate weapon of destruction.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Wither Impact", "§7- Ultimate Power"),
            Arrays.asList("§7- 1x Wither Fragment", "§7- 1x Netherite Sword")
        ));
        
        // Additional Special Items
        items.put("ASPECT_OF_THE_JERRY", new HypixelItem(
            "Aspect of the Jerry", "§aAspect of the Jerry", Material.WOODEN_SWORD,
            "§7The legendary weapon of Jerry.",
            ItemRarity.SPECIAL, 1, Arrays.asList("§7- Jerry Power", "§7- Ultimate Weakness"),
            Arrays.asList("§7- 1x Jerry Fragment", "§7- 1x Wooden Sword")
        ));
        
        items.put("ASPECT_OF_THE_LEECH", new HypixelItem(
            "Aspect of the Leech", "§cAspect of the Leech", Material.DIAMOND_SWORD,
            "§7A sword that drains life from enemies.",
            ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Life Drain", "§7- Health Steal"),
            Arrays.asList("§7- 1x Leech Fragment", "§7- 1x Diamond Sword")
        ));
        
        items.put("ASPECT_OF_THE_DRAGON_2", new HypixelItem(
            "Aspect of the Dragon II", "§6Aspect of the Dragon II", Material.DIAMOND_SWORD,
            "§7An upgraded version of the dragon sword.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Enhanced Dragon Breath", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Aspect of the Dragons")
        ));
        
        items.put("ASPECT_OF_THE_DRAGON_3", new HypixelItem(
            "Aspect of the Dragon III", "§6Aspect of the Dragon III", Material.DIAMOND_SWORD,
            "§7The ultimate dragon sword.",
            ItemRarity.MYTHIC, 400, Arrays.asList("§7- Ultimate Dragon Breath", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Aspect of the Dragon II")
        ));
        
        items.put("ASPECT_OF_THE_END_2", new HypixelItem(
            "Aspect of the End II", "§aAspect of the End II", Material.DIAMOND_SWORD,
            "§7An upgraded version of the end sword.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Enhanced Teleportation", "§7- High Damage"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Aspect of the End")
        ));
        
        items.put("ASPECT_OF_THE_END_3", new HypixelItem(
            "Aspect of the End III", "§aAspect of the End III", Material.DIAMOND_SWORD,
            "§7The ultimate end sword.",
            ItemRarity.MYTHIC, 200, Arrays.asList("§7- Ultimate Teleportation", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Aspect of the End II")
        ));
        
        items.put("ASPECT_OF_THE_VOID_2", new HypixelItem(
            "Aspect of the Void II", "§5Aspect of the Void II", Material.NETHERITE_SWORD,
            "§7An upgraded version of the void sword.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Enhanced Void Power", "§7- Advanced Teleportation"),
            Arrays.asList("§7- 1x Void Fragment", "§7- 1x Aspect of the Void")
        ));
        
        items.put("ASPECT_OF_THE_VOID_3", new HypixelItem(
            "Aspect of the Void III", "§5Aspect of the Void III", Material.NETHERITE_SWORD,
            "§7The ultimate void sword.",
            ItemRarity.MYTHIC, 600, Arrays.asList("§7- Ultimate Void Power", "§7- Master Teleportation"),
            Arrays.asList("§7- 1x Void Fragment", "§7- 1x Aspect of the Void II")
        ));
        
        // Additional Bows
        items.put("BONEMERANG_2", new HypixelItem(
            "Bonemerang II", "§fBonemerang II", Material.BOW,
            "§7An upgraded bonemerang that returns faster.",
            ItemRarity.LEGENDARY, 250, Arrays.asList("§7- Faster Return", "§7- High Damage"),
            Arrays.asList("§7- 1x Bone", "§7- 1x Bonemerang")
        ));
        
        items.put("BONEMERANG_3", new HypixelItem(
            "Bonemerang III", "§fBonemerang III", Material.BOW,
            "§7The ultimate bonemerang.",
            ItemRarity.MYTHIC, 300, Arrays.asList("§7- Instant Return", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Bone", "§7- 1x Bonemerang II")
        ));
        
        items.put("LAST_BREATH_2", new HypixelItem(
            "Last Breath II", "§cLast Breath II", Material.BOW,
            "§7An upgraded last breath bow.",
            ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Enhanced Last Breath", "§7- High Damage"),
            Arrays.asList("§7- 1x Breath Fragment", "§7- 1x Last Breath")
        ));
        
        items.put("LAST_BREATH_3", new HypixelItem(
            "Last Breath III", "§cLast Breath III", Material.BOW,
            "§7The ultimate last breath bow.",
            ItemRarity.MYTHIC, 350, Arrays.asList("§7- Ultimate Last Breath", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Breath Fragment", "§7- 1x Last Breath II")
        ));
        
        items.put("MACHINE_GUN_BOW_2", new HypixelItem(
            "Machine Gun Bow II", "§eMachine Gun Bow II", Material.BOW,
            "§7An upgraded machine gun bow.",
            ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Faster Fire Rate", "§7- High Damage"),
            Arrays.asList("§7- 1x Machine Fragment", "§7- 1x Machine Gun Bow")
        ));
        
        items.put("MACHINE_GUN_BOW_3", new HypixelItem(
            "Machine Gun Bow III", "§eMachine Gun Bow III", Material.BOW,
            "§7The ultimate machine gun bow.",
            ItemRarity.MYTHIC, 400, Arrays.asList("§7- Maximum Fire Rate", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Machine Fragment", "§7- 1x Machine Gun Bow II")
        ));
        
        items.put("SAVANNA_BOW_2", new HypixelItem(
            "Savanna Bow II", "§aSavanna Bow II", Material.BOW,
            "§7An upgraded savanna bow.",
            ItemRarity.LEGENDARY, 325, Arrays.asList("§7- Enhanced Savanna Power", "§7- High Damage"),
            Arrays.asList("§7- 1x Savanna Fragment", "§7- 1x Savanna Bow")
        ));
        
        items.put("SAVANNA_BOW_3", new HypixelItem(
            "Savanna Bow III", "§aSavanna Bow III", Material.BOW,
            "§7The ultimate savanna bow.",
            ItemRarity.MYTHIC, 375, Arrays.asList("§7- Ultimate Savanna Power", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Savanna Fragment", "§7- 1x Savanna Bow II")
        ));
        
        items.put("END_STONE_BOW_2", new HypixelItem(
            "End Stone Bow II", "§5End Stone Bow II", Material.BOW,
            "§7An upgraded end stone bow.",
            ItemRarity.LEGENDARY, 375, Arrays.asList("§7- Enhanced End Power", "§7- High Damage"),
            Arrays.asList("§7- 1x End Stone", "§7- 1x End Stone Bow")
        ));
        
        items.put("END_STONE_BOW_3", new HypixelItem(
            "End Stone Bow III", "§5End Stone Bow III", Material.BOW,
            "§7The ultimate end stone bow.",
            ItemRarity.MYTHIC, 425, Arrays.asList("§7- Ultimate End Power", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x End Stone", "§7- 1x End Stone Bow II")
        ));
        
        items.put("HURRICANE_BOW_2", new HypixelItem(
            "Hurricane Bow II", "§bHurricane Bow II", Material.BOW,
            "§7An upgraded hurricane bow.",
            ItemRarity.LEGENDARY, 400, Arrays.asList("§7- Enhanced Hurricane", "§7- High Damage"),
            Arrays.asList("§7- 1x Hurricane Fragment", "§7- 1x Hurricane Bow")
        ));
        
        items.put("HURRICANE_BOW_3", new HypixelItem(
            "Hurricane Bow III", "§bHurricane Bow III", Material.BOW,
            "§7The ultimate hurricane bow.",
            ItemRarity.MYTHIC, 450, Arrays.asList("§7- Ultimate Hurricane", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Hurricane Fragment", "§7- 1x Hurricane Bow II")
        ));
        
        items.put("DEATH_BOW_2", new HypixelItem(
            "Death Bow II", "§4Death Bow II", Material.BOW,
            "§7An upgraded death bow.",
            ItemRarity.MYTHIC, 450, Arrays.asList("§7- Enhanced Death Strike", "§7- High Damage"),
            Arrays.asList("§7- 1x Death Fragment", "§7- 1x Death Bow")
        ));
        
        items.put("DEATH_BOW_3", new HypixelItem(
            "Death Bow III", "§4Death Bow III", Material.BOW,
            "§7The ultimate death bow.",
            ItemRarity.MYTHIC, 500, Arrays.asList("§7- Ultimate Death Strike", "§7- Devastating Damage"),
            Arrays.asList("§7- 1x Death Fragment", "§7- 1x Death Bow II")
        ));
        
        // Additional Tools
        items.put("PICKONIMBUS_2", new HypixelItem(
            "Pickonimbus II", "§ePickonimbus II", Material.DIAMOND_PICKAXE,
            "§7An upgraded pickonimbus.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Enhanced Cloud Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Cloud Fragment", "§7- 1x Pickonimbus")
        ));
        
        items.put("PICKONIMBUS_3", new HypixelItem(
            "Pickonimbus III", "§ePickonimbus III", Material.DIAMOND_PICKAXE,
            "§7The ultimate pickonimbus.",
            ItemRarity.MYTHIC, 150, Arrays.asList("§7- Ultimate Cloud Mining", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Cloud Fragment", "§7- 1x Pickonimbus II")
        ));
        
        items.put("DRILL_ENGINE_2", new HypixelItem(
            "Drill Engine II", "§7Drill Engine II", Material.DIAMOND_PICKAXE,
            "§7An upgraded drill engine.",
            ItemRarity.LEGENDARY, 175, Arrays.asList("§7- Enhanced Engine Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Engine Fragment", "§7- 1x Drill Engine")
        ));
        
        items.put("DRILL_ENGINE_3", new HypixelItem(
            "Drill Engine III", "§7Drill Engine III", Material.DIAMOND_PICKAXE,
            "§7The ultimate drill engine.",
            ItemRarity.MYTHIC, 200, Arrays.asList("§7- Ultimate Engine Mining", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Engine Fragment", "§7- 1x Drill Engine II")
        ));
        
        items.put("GEMSTONE_DRILL_2", new HypixelItem(
            "Gemstone Drill II", "§bGemstone Drill II", Material.DIAMOND_PICKAXE,
            "§7An upgraded gemstone drill.",
            ItemRarity.LEGENDARY, 225, Arrays.asList("§7- Enhanced Gemstone Mining", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Gemstone Drill")
        ));
        
        items.put("GEMSTONE_DRILL_3", new HypixelItem(
            "Gemstone Drill III", "§bGemstone Drill III", Material.DIAMOND_PICKAXE,
            "§7The ultimate gemstone drill.",
            ItemRarity.MYTHIC, 250, Arrays.asList("§7- Ultimate Gemstone Mining", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Gemstone Drill II")
        ));
        
        items.put("GEMSTONE_GAUNTLET_2", new HypixelItem(
            "Gemstone Gauntlet II", "§bGemstone Gauntlet II", Material.DIAMOND_PICKAXE,
            "§7An upgraded gemstone gauntlet.",
            ItemRarity.LEGENDARY, 275, Arrays.asList("§7- Enhanced Gemstone Power", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Gemstone Gauntlet")
        ));
        
        items.put("GEMSTONE_GAUNTLET_3", new HypixelItem(
            "Gemstone Gauntlet III", "§bGemstone Gauntlet III", Material.DIAMOND_PICKAXE,
            "§7The ultimate gemstone gauntlet.",
            ItemRarity.MYTHIC, 300, Arrays.asList("§7- Ultimate Gemstone Power", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Gemstone Gauntlet II")
        ));
        
        items.put("TREE_CAPITATOR_2", new HypixelItem(
            "Tree Capitator II", "§aTree Capitator II", Material.DIAMOND_AXE,
            "§7An upgraded tree capitator.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Enhanced Tree Cutting", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tree Fragment", "§7- 1x Tree Capitator")
        ));
        
        items.put("TREE_CAPITATOR_3", new HypixelItem(
            "Tree Capitator III", "§aTree Capitator III", Material.DIAMOND_AXE,
            "§7The ultimate tree capitator.",
            ItemRarity.MYTHIC, 150, Arrays.asList("§7- Ultimate Tree Cutting", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Tree Fragment", "§7- 1x Tree Capitator II")
        ));
        
        items.put("JUNGLE_AXE_2", new HypixelItem(
            "Jungle Axe II", "§2Jungle Axe II", Material.DIAMOND_AXE,
            "§7An upgraded jungle axe.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Enhanced Jungle Cutting", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Jungle Fragment", "§7- 1x Jungle Axe")
        ));
        
        items.put("JUNGLE_AXE_3", new HypixelItem(
            "Jungle Axe III", "§2Jungle Axe III", Material.DIAMOND_AXE,
            "§7The ultimate jungle axe.",
            ItemRarity.MYTHIC, 175, Arrays.asList("§7- Ultimate Jungle Cutting", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Jungle Fragment", "§7- 1x Jungle Axe II")
        ));
        
        items.put("FORAGING_AXE_2", new HypixelItem(
            "Foraging Axe II", "§6Foraging Axe II", Material.DIAMOND_AXE,
            "§7An upgraded foraging axe.",
            ItemRarity.LEGENDARY, 175, Arrays.asList("§7- Enhanced Foraging", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Foraging Fragment", "§7- 1x Foraging Axe")
        ));
        
        items.put("FORAGING_AXE_3", new HypixelItem(
            "Foraging Axe III", "§6Foraging Axe III", Material.DIAMOND_AXE,
            "§7The ultimate foraging axe.",
            ItemRarity.MYTHIC, 200, Arrays.asList("§7- Ultimate Foraging", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Foraging Fragment", "§7- 1x Foraging Axe II")
        ));
        
        items.put("HOE_OF_GREATER_TILLING_2", new HypixelItem(
            "Hoe of Greater Tilling II", "§eHoe of Greater Tilling II", Material.DIAMOND_HOE,
            "§7An upgraded hoe of greater tilling.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Enhanced Greater Tilling", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Hoe of Greater Tilling")
        ));
        
        items.put("HOE_OF_GREATER_TILLING_3", new HypixelItem(
            "Hoe of Greater Tilling III", "§eHoe of Greater Tilling III", Material.DIAMOND_HOE,
            "§7The ultimate hoe of greater tilling.",
            ItemRarity.MYTHIC, 150, Arrays.asList("§7- Ultimate Greater Tilling", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Hoe of Greater Tilling II")
        ));
        
        items.put("HOE_OF_THE_TILLING_2", new HypixelItem(
            "Hoe of the Tilling II", "§eHoe of the Tilling II", Material.DIAMOND_HOE,
            "§7An upgraded hoe of the tilling.",
            ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Enhanced Ultimate Tilling", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Hoe of the Tilling")
        ));
        
        items.put("HOE_OF_THE_TILLING_3", new HypixelItem(
            "Hoe of the Tilling III", "§eHoe of the Tilling III", Material.DIAMOND_HOE,
            "§7The ultimate hoe of the tilling.",
            ItemRarity.MYTHIC, 175, Arrays.asList("§7- Ultimate Tilling Mastery", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Tilling Fragment", "§7- 1x Hoe of the Tilling II")
        ));
        
        // Additional Fishing Rods
        items.put("ROD_OF_THE_SEA_2", new HypixelItem(
            "Rod of the Sea II", "§bRod of the Sea II", Material.FISHING_ROD,
            "§7An upgraded rod of the sea.",
            ItemRarity.LEGENDARY, 125, Arrays.asList("§7- Enhanced Sea Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Sea Fragment", "§7- 1x Rod of the Sea")
        ));
        
        items.put("ROD_OF_THE_SEA_3", new HypixelItem(
            "Rod of the Sea III", "§bRod of the Sea III", Material.FISHING_ROD,
            "§7The ultimate rod of the sea.",
            ItemRarity.MYTHIC, 150, Arrays.asList("§7- Ultimate Sea Fishing", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Sea Fragment", "§7- 1x Rod of the Sea II")
        ));
        
        items.put("ROD_OF_LEGENDS_2", new HypixelItem(
            "Rod of Legends II", "§6Rod of Legends II", Material.FISHING_ROD,
            "§7An upgraded rod of legends.",
            ItemRarity.LEGENDARY, 175, Arrays.asList("§7- Enhanced Legendary Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Legend Fragment", "§7- 1x Rod of Legends")
        ));
        
        items.put("ROD_OF_LEGENDS_3", new HypixelItem(
            "Rod of Legends III", "§6Rod of Legends III", Material.FISHING_ROD,
            "§7The ultimate rod of legends.",
            ItemRarity.MYTHIC, 200, Arrays.asList("§7- Ultimate Legendary Fishing", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Legend Fragment", "§7- 1x Rod of Legends II")
        ));
        
        items.put("ROD_OF_CHAMPIONS_2", new HypixelItem(
            "Rod of Champions II", "§cRod of Champions II", Material.FISHING_ROD,
            "§7An upgraded rod of champions.",
            ItemRarity.LEGENDARY, 225, Arrays.asList("§7- Enhanced Champion Fishing", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Champion Fragment", "§7- 1x Rod of Champions")
        ));
        
        items.put("ROD_OF_CHAMPIONS_3", new HypixelItem(
            "Rod of Champions III", "§cRod of Champions III", Material.FISHING_ROD,
            "§7The ultimate rod of champions.",
            ItemRarity.MYTHIC, 250, Arrays.asList("§7- Ultimate Champion Fishing", "§7- Maximum Efficiency"),
            Arrays.asList("§7- 1x Champion Fragment", "§7- 1x Rod of Champions II")
        ));
        
        return items;
    }
}
