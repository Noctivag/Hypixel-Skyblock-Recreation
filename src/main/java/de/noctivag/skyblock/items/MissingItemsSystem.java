package de.noctivag.skyblock.items;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * MissingItemsSystem - Implementiert fehlende Hypixel SkyBlock Items
 * 
 * Features:
 * - Fehlende Waffen (Swords, Bows, Wands, Staffs)
 * - Fehlende Rüstungen (Dragon, Wither, Shadow Assassin, etc.)
 * - Fehlende Accessories (Talismans, Rings, Artifacts)
 * - Fehlende Tools (Drills, Gauntlets, Shears)
 * - Fehlende Special Items (Potato Books, Recombobulators, etc.)
 */
public class MissingItemsSystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, MissingItem> missingItems = new HashMap<>();
    
    public MissingItemsSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeMissingItems();
    }
    
    /**
     * Initialisiert alle fehlenden Items
     */
    private void initializeMissingItems() {
        // Missing Swords
        initializeMissingSwords();
        
        // Missing Bows
        initializeMissingBows();
        
        // Missing Wands
        initializeMissingWands();
        
        // Missing Staffs
        initializeMissingStaffs();
        
        // Missing Armor Sets
        initializeMissingArmorSets();
        
        // Missing Accessories
        initializeMissingAccessories();
        
        // Missing Tools
        initializeMissingTools();
        
        // Missing Special Items
        initializeMissingSpecialItems();
        
        SkyblockPlugin.getLogger().info("§a[MissingItems] Initialized " + missingItems.size() + " missing items");
    }
    
    /**
     * Initialisiert fehlende Schwerter
     */
    private void initializeMissingSwords() {
        // Midas Sword
        addMissingItem("MIDAS_SWORD", new MissingItem(
            "Midas Sword", "§6Midas Sword", Material.GOLDEN_SWORD,
            "§7A sword forged from pure gold with immense power.",
            ItemRarity.LEGENDARY, ItemCategory.SWORD, 200,
            Arrays.asList("§7- Gold Touch", "§7- High Damage", "§7- Wealth Bonus"),
            Arrays.asList("§7- 1x Gold Block", "§7- 1x Diamond Sword", "§7- 1x Enchanted Gold")
        ));
        
        // Valkyrie
        addMissingItem("VALKYRIE", new MissingItem(
            "Valkyrie", "§dValkyrie", Material.NETHERITE_SWORD,
            "§7A powerful sword with healing abilities.",
            ItemRarity.MYTHIC, ItemCategory.SWORD, 500,
            Arrays.asList("§7- Wither Impact", "§7- Healing", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        // Astraea
        addMissingItem("ASTRAEA", new MissingItem(
            "Astraea", "§dAstraea", Material.NETHERITE_SWORD,
            "§7A defensive sword with protective abilities.",
            ItemRarity.MYTHIC, ItemCategory.SWORD, 500,
            Arrays.asList("§7- Wither Impact", "§7- Defense", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Blade", "§7- 1x Netherite Sword")
        ));
        
        // Livid Dagger
        addMissingItem("LIVID_DAGGER", new MissingItem(
            "Livid Dagger", "§5Livid Dagger", Material.DIAMOND_SWORD,
            "§7A dagger that strikes from the shadows.",
            ItemRarity.LEGENDARY, ItemCategory.SWORD, 175,
            Arrays.asList("§7- Backstab", "§7- Stealth", "§7- High Crit Damage"),
            Arrays.asList("§7- 1x Livid Fragment", "§7- 1x Diamond Sword")
        ));
        
        // Shadow Fury
        addMissingItem("SHADOW_FURY", new MissingItem(
            "Shadow Fury", "§5Shadow Fury", Material.DIAMOND_SWORD,
            "§7A sword that channels the power of shadows.",
            ItemRarity.LEGENDARY, ItemCategory.SWORD, 190,
            Arrays.asList("§7- Shadow Strike", "§7- Teleportation", "§7- High Damage"),
            Arrays.asList("§7- 1x Shadow Fragment", "§7- 1x Diamond Sword")
        ));
        
        // Giant's Sword
        addMissingItem("GIANTS_SWORD", new MissingItem(
            "Giant's Sword", "§6Giant's Sword", Material.DIAMOND_SWORD,
            "§7A massive sword with incredible power.",
            ItemRarity.LEGENDARY, ItemCategory.SWORD, 300,
            Arrays.asList("§7- Giant's Strength", "§7- Massive Damage", "§7- Slow Attack"),
            Arrays.asList("§7- 1x Giant's Fragment", "§7- 1x Diamond Sword")
        ));
    }
    
    /**
     * Initialisiert fehlende Bögen
     */
    private void initializeMissingBows() {
        // Runaan's Bow
        addMissingItem("RUNAANS_BOW", new MissingItem(
            "Runaan's Bow", "§aRunaan's Bow", Material.BOW,
            "§7A bow that fires multiple arrows at once.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 150,
            Arrays.asList("§7- Triple Shot", "§7- High Damage", "§7- Multi-Target"),
            Arrays.asList("§7- 1x Runaan's Fragment", "§7- 1x Bow")
        ));
        
        // Mosquito Bow
        addMissingItem("MOSQUITO_BOW", new MissingItem(
            "Mosquito Bow", "§cMosquito Bow", Material.BOW,
            "§7A bow that drains life from enemies.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 140,
            Arrays.asList("§7- Life Drain", "§7- Healing", "§7- High Damage"),
            Arrays.asList("§7- 1x Mosquito Fragment", "§7- 1x Bow")
        ));
        
        // Bonemerang
        addMissingItem("BONEMERANG", new MissingItem(
            "Bonemerang", "§fBonemerang", Material.BOW,
            "§7A bow that returns to the shooter.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 160,
            Arrays.asList("§7- Returning Shot", "§7- High Damage", "§7- Bone Power"),
            Arrays.asList("§7- 1x Bone Fragment", "§7- 1x Bow")
        ));
        
        // Spirit Bow
        addMissingItem("SPIRIT_BOW", new MissingItem(
            "Spirit Bow", "§bSpirit Bow", Material.BOW,
            "§7A bow that channels spiritual energy.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 170,
            Arrays.asList("§7- Spirit Shot", "§7- High Damage", "§7- Spiritual Power"),
            Arrays.asList("§7- 1x Spirit Fragment", "§7- 1x Bow")
        ));
        
        // Juju Shortbow
        addMissingItem("JUJU_SHORTBOW", new MissingItem(
            "Juju Shortbow", "§5Juju Shortbow", Material.BOW,
            "§7A shortbow with incredible speed.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 180,
            Arrays.asList("§7- Rapid Fire", "§7- High Speed", "§7- High Damage"),
            Arrays.asList("§7- 1x Juju Fragment", "§7- 1x Bow")
        ));
    }
    
    /**
     * Initialisiert fehlende Zauberstäbe
     */
    private void initializeMissingWands() {
        // Fire Veil Wand
        addMissingItem("FIRE_VEIL_WAND", new MissingItem(
            "Fire Veil Wand", "§cFire Veil Wand", Material.BLAZE_ROD,
            "§7A wand that creates a veil of fire.",
            ItemRarity.LEGENDARY, ItemCategory.WAND, 120,
            Arrays.asList("§7- Fire Veil", "§7- Area Damage", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x Fire Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Spirit Scepter
        addMissingItem("SPIRIT_SCEPTER", new MissingItem(
            "Spirit Scepter", "§bSpirit Scepter", Material.BLAZE_ROD,
            "§7A scepter that channels spiritual energy.",
            ItemRarity.LEGENDARY, ItemCategory.WAND, 130,
            Arrays.asList("§7- Spirit Blast", "§7- High Damage", "§7- Spiritual Power"),
            Arrays.asList("§7- 1x Spirit Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Frozen Scythe
        addMissingItem("FROZEN_SCYTHE", new MissingItem(
            "Frozen Scythe", "§bFrozen Scythe", Material.BLAZE_ROD,
            "§7A scythe that freezes enemies.",
            ItemRarity.LEGENDARY, ItemCategory.WAND, 140,
            Arrays.asList("§7- Freeze Effect", "§7- Area Damage", "§7- Ice Immunity"),
            Arrays.asList("§7- 1x Ice Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Ice Spray Wand
        addMissingItem("ICE_SPRAY_WAND", new MissingItem(
            "Ice Spray Wand", "§bIce Spray Wand", Material.BLAZE_ROD,
            "§7A wand that sprays ice at enemies.",
            ItemRarity.LEGENDARY, ItemCategory.WAND, 150,
            Arrays.asList("§7- Ice Spray", "§7- Area Damage", "§7- Freeze Effect"),
            Arrays.asList("§7- 1x Ice Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Glacial Scepter
        addMissingItem("GLACIAL_SCEPTER", new MissingItem(
            "Glacial Scepter", "§bGlacial Scepter", Material.BLAZE_ROD,
            "§7A scepter that channels glacial energy.",
            ItemRarity.LEGENDARY, ItemCategory.WAND, 160,
            Arrays.asList("§7- Glacial Blast", "§7- High Damage", "§7- Ice Power"),
            Arrays.asList("§7- 1x Ice Fragment", "§7- 1x Blaze Rod")
        ));
    }
    
    /**
     * Initialisiert fehlende Stäbe
     */
    private void initializeMissingStaffs() {
        // Bonzo Staff
        addMissingItem("BONZO_STAFF", new MissingItem(
            "Bonzo Staff", "§6Bonzo Staff", Material.BLAZE_ROD,
            "§7A staff that channels clown energy.",
            ItemRarity.LEGENDARY, ItemCategory.STAFF, 110,
            Arrays.asList("§7- Clown Blast", "§7- Area Damage", "§7- Clown Power"),
            Arrays.asList("§7- 1x Bonzo Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Scarf Studies
        addMissingItem("SCARF_STUDIES", new MissingItem(
            "Scarf Studies", "§5Scarf Studies", Material.BLAZE_ROD,
            "§7A staff that channels scarf energy.",
            ItemRarity.LEGENDARY, ItemCategory.STAFF, 120,
            Arrays.asList("§7- Scarf Blast", "§7- Area Damage", "§7- Scarf Power"),
            Arrays.asList("§7- 1x Scarf Fragment", "§7- 1x Blaze Rod")
        ));
        
        // Thorn Bow
        addMissingItem("THORN_BOW", new MissingItem(
            "Thorn Bow", "§aThorn Bow", Material.BOW,
            "§7A bow that fires thorn arrows.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 130,
            Arrays.asList("§7- Thorn Shot", "§7- High Damage", "§7- Thorn Power"),
            Arrays.asList("§7- 1x Thorn Fragment", "§7- 1x Bow")
        ));
        
        // Last Breath
        addMissingItem("LAST_BREATH", new MissingItem(
            "Last Breath", "§8Last Breath", Material.BOW,
            "§7A bow that takes the last breath of enemies.",
            ItemRarity.LEGENDARY, ItemCategory.BOW, 140,
            Arrays.asList("§7- Last Breath", "§7- High Damage", "§7- Death Power"),
            Arrays.asList("§7- 1x Death Fragment", "§7- 1x Bow")
        ));
    }
    
    /**
     * Initialisiert fehlende Rüstungssets
     */
    private void initializeMissingArmorSets() {
        // Dragon Armor Sets
        addMissingItem("SUPERIOR_ARMOR", new MissingItem(
            "Superior Dragon Armor", "§6Superior Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7The most powerful dragon armor set.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 200,
            Arrays.asList("§7- Superior Power", "§7- High Defense", "§7- Dragon Abilities"),
            Arrays.asList("§7- 1x Superior Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        addMissingItem("UNSTABLE_ARMOR", new MissingItem(
            "Unstable Dragon Armor", "§5Unstable Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Dragon armor with unstable energy.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 180,
            Arrays.asList("§7- Unstable Power", "§7- High Defense", "§7- Random Effects"),
            Arrays.asList("§7- 1x Unstable Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        addMissingItem("STRONG_ARMOR", new MissingItem(
            "Strong Dragon Armor", "§cStrong Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Dragon armor with incredible strength.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 190,
            Arrays.asList("§7- Strong Power", "§7- High Defense", "§7- Strength Bonus"),
            Arrays.asList("§7- 1x Strong Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        addMissingItem("YOUNG_ARMOR", new MissingItem(
            "Young Dragon Armor", "§fYoung Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Dragon armor that grants incredible speed.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 170,
            Arrays.asList("§7- Young Power", "§7- High Defense", "§7- Speed Bonus"),
            Arrays.asList("§7- 1x Young Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        addMissingItem("OLD_ARMOR", new MissingItem(
            "Old Dragon Armor", "§7Old Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Dragon armor with ancient wisdom.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 175,
            Arrays.asList("§7- Old Power", "§7- High Defense", "§7- Wisdom Bonus"),
            Arrays.asList("§7- 1x Old Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        addMissingItem("PROTECTOR_ARMOR", new MissingItem(
            "Protector Dragon Armor", "§aProtector Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Dragon armor that provides maximum protection.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 185,
            Arrays.asList("§7- Protector Power", "§7- High Defense", "§7- Protection Bonus"),
            Arrays.asList("§7- 1x Protector Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        addMissingItem("WISE_ARMOR", new MissingItem(
            "Wise Dragon Armor", "§bWise Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Dragon armor that grants magical wisdom.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 180,
            Arrays.asList("§7- Wise Power", "§7- High Defense", "§7- Magic Bonus"),
            Arrays.asList("§7- 1x Wise Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        // Wither Armor Sets
        addMissingItem("NECRONS_ARMOR", new MissingItem(
            "Necron's Armor", "§8Necron's Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that channels the power of Necron.",
            ItemRarity.MYTHIC, ItemCategory.ARMOR, 250,
            Arrays.asList("§7- Necron's Power", "§7- High Defense", "§7- Wither Abilities"),
            Arrays.asList("§7- 1x Necron Fragment", "§7- 1x Netherite Armor")
        ));
        
        addMissingItem("STORMS_ARMOR", new MissingItem(
            "Storm's Armor", "§bStorm's Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that channels the power of storms.",
            ItemRarity.MYTHIC, ItemCategory.ARMOR, 240,
            Arrays.asList("§7- Storm's Power", "§7- High Defense", "§7- Storm Abilities"),
            Arrays.asList("§7- 1x Storm Fragment", "§7- 1x Netherite Armor")
        ));
        
        addMissingItem("GOLDORS_ARMOR", new MissingItem(
            "Goldor's Armor", "§6Goldor's Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that channels the power of Goldor.",
            ItemRarity.MYTHIC, ItemCategory.ARMOR, 245,
            Arrays.asList("§7- Goldor's Power", "§7- High Defense", "§7- Gold Abilities"),
            Arrays.asList("§7- 1x Goldor Fragment", "§7- 1x Netherite Armor")
        ));
        
        addMissingItem("MAXORS_ARMOR", new MissingItem(
            "Maxor's Armor", "§fMaxor's Armor", Material.NETHERITE_CHESTPLATE,
            "§7Armor that channels the power of Maxor.",
            ItemRarity.MYTHIC, ItemCategory.ARMOR, 235,
            Arrays.asList("§7- Maxor's Power", "§7- High Defense", "§7- Speed Abilities"),
            Arrays.asList("§7- 1x Maxor Fragment", "§7- 1x Netherite Armor")
        ));
        
        // Shadow Assassin Armor
        addMissingItem("SHADOW_ASSASSIN_ARMOR", new MissingItem(
            "Shadow Assassin Armor", "§5Shadow Assassin Armor", Material.LEATHER_CHESTPLATE,
            "§7Armor that channels the power of shadows.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 200,
            Arrays.asList("§7- Shadow Power", "§7- High Defense", "§7- Stealth Abilities"),
            Arrays.asList("§7- 1x Shadow Fragment", "§7- 1x Leather Armor")
        ));
        
        // Frozen Blaze Armor
        addMissingItem("FROZEN_BLAZE_ARMOR", new MissingItem(
            "Frozen Blaze Armor", "§bFrozen Blaze Armor", Material.LEATHER_CHESTPLATE,
            "§7Armor that channels the power of frozen fire.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 210,
            Arrays.asList("§7- Frozen Blaze Power", "§7- High Defense", "§7- Ice Fire Abilities"),
            Arrays.asList("§7- 1x Frozen Blaze Fragment", "§7- 1x Leather Armor")
        ));
        
        // Crystal Armor
        addMissingItem("CRYSTAL_ARMOR", new MissingItem(
            "Crystal Armor", "§dCrystal Armor", Material.LEATHER_CHESTPLATE,
            "§7Armor that channels the power of crystals.",
            ItemRarity.LEGENDARY, ItemCategory.ARMOR, 190,
            Arrays.asList("§7- Crystal Power", "§7- High Defense", "§7- Crystal Abilities"),
            Arrays.asList("§7- 1x Crystal Fragment", "§7- 1x Leather Armor")
        ));
    }
    
    /**
     * Initialisiert fehlende Accessories
     */
    private void initializeMissingAccessories() {
        // Talismans
        addMissingItem("TALISMAN_OF_POWER", new MissingItem(
            "Talisman of Power", "§6Talisman of Power", Material.GOLD_INGOT,
            "§7A talisman that increases all stats.",
            ItemRarity.UNCOMMON, ItemCategory.ACCESSORY, 50,
            Arrays.asList("§7- All Stats +5%", "§7- XP Boost +10%"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Enchanted Gold")
        ));
        
        addMissingItem("ARTIFACT_OF_POWER", new MissingItem(
            "Artifact of Power", "§6Artifact of Power", Material.GOLD_INGOT,
            "§7An artifact that greatly increases all stats.",
            ItemRarity.RARE, ItemCategory.ACCESSORY, 75,
            Arrays.asList("§7- All Stats +10%", "§7- XP Boost +20%"),
            Arrays.asList("§7- 1x Artifact Fragment", "§7- 1x Enchanted Gold")
        ));
        
        addMissingItem("RELIC_OF_POWER", new MissingItem(
            "Relic of Power", "§6Relic of Power", Material.GOLD_INGOT,
            "§7A relic that massively increases all stats.",
            ItemRarity.EPIC, ItemCategory.ACCESSORY, 100,
            Arrays.asList("§7- All Stats +15%", "§7- XP Boost +30%"),
            Arrays.asList("§7- 1x Relic Fragment", "§7- 1x Enchanted Gold")
        ));
        
        // Rings
        addMissingItem("RING_OF_LOVE", new MissingItem(
            "Ring of Love", "§cRing of Love", Material.GOLD_INGOT,
            "§7A ring that increases health and defense.",
            ItemRarity.RARE, ItemCategory.ACCESSORY, 60,
            Arrays.asList("§7- Health +50", "§7- Defense +5%"),
            Arrays.asList("§7- 1x Love Fragment", "§7- 1x Enchanted Gold")
        ));
        
        addMissingItem("RING_OF_POWER", new MissingItem(
            "Ring of Power", "§6Ring of Power", Material.GOLD_INGOT,
            "§7A ring that increases strength and damage.",
            ItemRarity.RARE, ItemCategory.ACCESSORY, 65,
            Arrays.asList("§7- Strength +25", "§7- Damage +10%"),
            Arrays.asList("§7- 1x Power Fragment", "§7- 1x Enchanted Gold")
        ));
        
        addMissingItem("RING_OF_HEALTH", new MissingItem(
            "Ring of Health", "§aRing of Health", Material.GOLD_INGOT,
            "§7A ring that increases health and regeneration.",
            ItemRarity.RARE, ItemCategory.ACCESSORY, 70,
            Arrays.asList("§7- Health +75", "§7- Regeneration +5%"),
            Arrays.asList("§7- 1x Health Fragment", "§7- 1x Enchanted Gold")
        ));
        
        // Artifacts
        addMissingItem("ARTIFACT_OF_CONTROL", new MissingItem(
            "Artifact of Control", "§bArtifact of Control", Material.GOLD_INGOT,
            "§7An artifact that provides control over elements.",
            ItemRarity.EPIC, ItemCategory.ACCESSORY, 90,
            Arrays.asList("§7- Element Control", "§7- All Stats +8%"),
            Arrays.asList("§7- 1x Control Fragment", "§7- 1x Enchanted Gold")
        ));
        
        addMissingItem("ARTIFACT_OF_UNDYING", new MissingItem(
            "Artifact of Undying", "§8Artifact of Undying", Material.GOLD_INGOT,
            "§7An artifact that prevents death.",
            ItemRarity.EPIC, ItemCategory.ACCESSORY, 95,
            Arrays.asList("§7- Death Prevention", "§7- All Stats +10%"),
            Arrays.asList("§7- 1x Undying Fragment", "§7- 1x Enchanted Gold")
        ));
    }
    
    /**
     * Initialisiert fehlende Tools
     */
    private void initializeMissingTools() {
        // Drills
        addMissingItem("MITHRIL_DRILL", new MissingItem(
            "Mithril Drill", "§bMithril Drill", Material.DIAMOND_PICKAXE,
            "§7A drill that mines mithril efficiently.",
            ItemRarity.RARE, ItemCategory.TOOL, 120,
            Arrays.asList("§7- Mithril Mining", "§7- High Speed", "§7- Fortune +2"),
            Arrays.asList("§7- 1x Mithril Ingot", "§7- 1x Diamond Pickaxe")
        ));
        
        addMissingItem("TITANIUM_DRILL", new MissingItem(
            "Titanium Drill", "§fTitanium Drill", Material.DIAMOND_PICKAXE,
            "§7A drill that mines titanium efficiently.",
            ItemRarity.EPIC, ItemCategory.TOOL, 150,
            Arrays.asList("§7- Titanium Mining", "§7- High Speed", "§7- Fortune +3"),
            Arrays.asList("§7- 1x Titanium Ingot", "§7- 1x Diamond Pickaxe")
        ));
        
        addMissingItem("GEMSTONE_DRILL", new MissingItem(
            "Gemstone Drill", "§dGemstone Drill", Material.DIAMOND_PICKAXE,
            "§7A drill that mines gemstones efficiently.",
            ItemRarity.LEGENDARY, ItemCategory.TOOL, 180,
            Arrays.asList("§7- Gemstone Mining", "§7- High Speed", "§7- Fortune +4"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        // Gauntlets
        addMissingItem("MITHRIL_GAUNTLET", new MissingItem(
            "Mithril Gauntlet", "§bMithril Gauntlet", Material.DIAMOND_PICKAXE,
            "§7A gauntlet that mines mithril with bare hands.",
            ItemRarity.RARE, ItemCategory.TOOL, 130,
            Arrays.asList("§7- Mithril Mining", "§7- High Speed", "§7- Fortune +2"),
            Arrays.asList("§7- 1x Mithril Ingot", "§7- 1x Diamond Pickaxe")
        ));
        
        addMissingItem("TITANIUM_GAUNTLET", new MissingItem(
            "Titanium Gauntlet", "§fTitanium Gauntlet", Material.DIAMOND_PICKAXE,
            "§7A gauntlet that mines titanium with bare hands.",
            ItemRarity.EPIC, ItemCategory.TOOL, 160,
            Arrays.asList("§7- Titanium Mining", "§7- High Speed", "§7- Fortune +3"),
            Arrays.asList("§7- 1x Titanium Ingot", "§7- 1x Diamond Pickaxe")
        ));
        
        addMissingItem("GEMSTONE_GAUNTLET", new MissingItem(
            "Gemstone Gauntlet", "§dGemstone Gauntlet", Material.DIAMOND_PICKAXE,
            "§7A gauntlet that mines gemstones with bare hands.",
            ItemRarity.LEGENDARY, ItemCategory.TOOL, 190,
            Arrays.asList("§7- Gemstone Mining", "§7- High Speed", "§7- Fortune +4"),
            Arrays.asList("§7- 1x Gemstone Fragment", "§7- 1x Diamond Pickaxe")
        ));
        
        // Shears
        addMissingItem("COCOA_CHOPPER", new MissingItem(
            "Cocoa Chopper", "§6Cocoa Chopper", Material.SHEARS,
            "§7Shears that efficiently harvest cocoa beans.",
            ItemRarity.UNCOMMON, ItemCategory.TOOL, 80,
            Arrays.asList("§7- Cocoa Harvesting", "§7- High Speed", "§7- Fortune +1"),
            Arrays.asList("§7- 1x Cocoa Bean", "§7- 1x Shears")
        ));
        
        addMissingItem("FUNGUS_CUTTER", new MissingItem(
            "Fungus Cutter", "§aFungus Cutter", Material.SHEARS,
            "§7Shears that efficiently harvest mushrooms.",
            ItemRarity.UNCOMMON, ItemCategory.TOOL, 85,
            Arrays.asList("§7- Mushroom Harvesting", "§7- High Speed", "§7- Fortune +1"),
            Arrays.asList("§7- 1x Mushroom", "§7- 1x Shears")
        ));
    }
    
    /**
     * Initialisiert fehlende Special Items
     */
    private void initializeMissingSpecialItems() {
        // Potato Books
        addMissingItem("HOT_POTATO_BOOK", new MissingItem(
            "Hot Potato Book", "§cHot Potato Book", Material.BOOK,
            "§7A book that increases item stats.",
            ItemRarity.RARE, ItemCategory.SPECIAL, 100,
            Arrays.asList("§7- Stats +2", "§7- Item Enhancement"),
            Arrays.asList("§7- 1x Potato", "§7- 1x Book")
        ));
        
        addMissingItem("FUMING_POTATO_BOOK", new MissingItem(
            "Fuming Potato Book", "§6Fuming Potato Book", Material.BOOK,
            "§7A book that greatly increases item stats.",
            ItemRarity.EPIC, ItemCategory.SPECIAL, 150,
            Arrays.asList("§7- Stats +5", "§7- Item Enhancement"),
            Arrays.asList("§7- 1x Fuming Potato", "§7- 1x Book")
        ));
        
        // Recombobulators
        addMissingItem("RECOMBOBULATOR_3000", new MissingItem(
            "Recombobulator 3000", "§dRecombobulator 3000", Material.REDSTONE,
            "§7A device that upgrades item rarity.",
            ItemRarity.EPIC, ItemCategory.SPECIAL, 200,
            Arrays.asList("§7- Rarity Upgrade", "§7- Item Enhancement"),
            Arrays.asList("§7- 1x Recombobulator Fragment", "§7- 1x Redstone")
        ));
        
        // Dungeon Stars
        addMissingItem("DUNGEON_STAR_1", new MissingItem(
            "Dungeon Star (1★)", "§6Dungeon Star (1★)", Material.NETHER_STAR,
            "§7A star that enhances dungeon items.",
            ItemRarity.RARE, ItemCategory.SPECIAL, 120,
            Arrays.asList("§7- Dungeon Enhancement", "§7- Stats +10%"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 1x Nether Star")
        ));
        
        addMissingItem("DUNGEON_STAR_2", new MissingItem(
            "Dungeon Star (2★)", "§6Dungeon Star (2★)", Material.NETHER_STAR,
            "§7A star that enhances dungeon items.",
            ItemRarity.RARE, ItemCategory.SPECIAL, 140,
            Arrays.asList("§7- Dungeon Enhancement", "§7- Stats +20%"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 1x Nether Star")
        ));
        
        addMissingItem("DUNGEON_STAR_3", new MissingItem(
            "Dungeon Star (3★)", "§6Dungeon Star (3★)", Material.NETHER_STAR,
            "§7A star that enhances dungeon items.",
            ItemRarity.EPIC, ItemCategory.SPECIAL, 160,
            Arrays.asList("§7- Dungeon Enhancement", "§7- Stats +30%"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 1x Nether Star")
        ));
        
        addMissingItem("DUNGEON_STAR_4", new MissingItem(
            "Dungeon Star (4★)", "§6Dungeon Star (4★)", Material.NETHER_STAR,
            "§7A star that enhances dungeon items.",
            ItemRarity.EPIC, ItemCategory.SPECIAL, 180,
            Arrays.asList("§7- Dungeon Enhancement", "§7- Stats +40%"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 1x Nether Star")
        ));
        
        addMissingItem("DUNGEON_STAR_5", new MissingItem(
            "Dungeon Star (5★)", "§6Dungeon Star (5★)", Material.NETHER_STAR,
            "§7A star that enhances dungeon items.",
            ItemRarity.LEGENDARY, ItemCategory.SPECIAL, 200,
            Arrays.asList("§7- Dungeon Enhancement", "§7- Stats +50%"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 1x Nether Star")
        ));
    }
    
    /**
     * Fügt ein fehlendes Item hinzu
     */
    private void addMissingItem(String id, MissingItem item) {
        missingItems.put(id, item);
    }
    
    /**
     * Gibt alle fehlenden Items zurück
     */
    public Map<String, MissingItem> getAllMissingItems() {
        return new HashMap<>(missingItems);
    }
    
    /**
     * Gibt ein fehlendes Item zurück
     */
    public MissingItem getMissingItem(String id) {
        return missingItems.get(id);
    }
    
    /**
     * Erstellt ein ItemStack für ein fehlendes Item
     */
    public ItemStack createMissingItemStack(String id) {
        MissingItem item = missingItems.get(id);
        if (item == null) {
            return null;
        }
        
        ItemStack itemStack = new ItemStack(item.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(item.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add(item.getDescription());
            lore.add("");
            lore.addAll(item.getFeatures());
            lore.add("");
            lore.add("§7Requirements:");
            lore.addAll(item.getRequirements());
            lore.add("");
            lore.add("§7Rarity: " + item.getRarity().getDisplayName());
            lore.add("§7Category: " + item.getCategory().getDisplayName());
            lore.add("§7Base Damage: " + item.getBaseDamage());
            
            meta.lore(lore.stream().map(line -> Component.text(line)).toList());
            itemStack.setItemMeta(meta);
        }
        
        return itemStack;
    }
    
    /**
     * Gibt die Anzahl der fehlenden Items zurück
     */
    public int getMissingItemCount() {
        return missingItems.size();
    }
    
    /**
     * Gibt fehlende Items nach Kategorie zurück
     */
    public Map<ItemCategory, List<MissingItem>> getMissingItemsByCategory() {
        Map<ItemCategory, List<MissingItem>> categorized = new HashMap<>();
        
        for (MissingItem item : missingItems.values()) {
            categorized.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
        }
        
        return categorized;
    }
    
    /**
     * MissingItem - Repräsentiert ein fehlendes Item
     */
    public static class MissingItem {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final ItemRarity rarity;
        private final ItemCategory category;
        private final int baseDamage;
        private final List<String> features;
        private final List<String> requirements;
        
        public MissingItem(String name, String displayName, Material material, String description,
                          ItemRarity rarity, ItemCategory category, int baseDamage,
                          List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.category = category;
            this.baseDamage = baseDamage;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public ItemRarity getRarity() { return rarity; }
        public ItemCategory getCategory() { return category; }
        public int getBaseDamage() { return baseDamage; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    /**
     * ItemRarity - Repräsentiert die Seltenheit eines Items
     */
    public enum ItemRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.2),
        RARE("§9Rare", 1.5),
        EPIC("§5Epic", 2.0),
        LEGENDARY("§6Legendary", 3.0),
        MYTHIC("§dMythic", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        ItemRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    /**
     * ItemCategory - Repräsentiert die Kategorie eines Items
     */
    public enum ItemCategory {
        SWORD("§cSword"),
        BOW("§aBow"),
        WAND("§bWand"),
        STAFF("§6Staff"),
        ARMOR("§bArmor"),
        ACCESSORY("§6Accessory"),
        TOOL("§eTool"),
        SPECIAL("§dSpecial");
        
        private final String displayName;
        
        ItemCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
}
