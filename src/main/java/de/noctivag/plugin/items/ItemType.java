package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

/**
 * ItemType - Enum for all Hypixel SkyBlock item types
 * 
 * This enum contains all items from Hypixel SkyBlock including:
 * - Dragon Weapons
 * - Dungeon Weapons
 * - Slayer Weapons
 * - Mining Tools
 * - Fishing Rods
 * - Magic Weapons
 * - Bows and Crossbows
 * - Special Items
 * - Tools and Utilities
 */
public enum ItemType {
    // Dragon Weapons
    ASPECT_OF_THE_DRAGONS("Aspect of the Dragons"),
    ASPECT_OF_THE_END("Aspect of the End"),
    ASPECT_OF_THE_VOID("Aspect of the Void"),
    
    // Dungeon Weapons
    HYPERION("Hyperion"),
    SCYLLA("Scylla"),
    ASTRAEA("Astraea"),
    VALKYRIE("Valkyrie"),
    BONEMERANG("Bonemerang"),
    SPIRIT_BOW("Spirit Bow"),
    SPIRIT_SCEPTER("Spirit Scepter"),
    SPIRIT_SWORD("Spirit Sword"),
    ADAPTIVE_BLADE("Adaptive Blade"),
    SHADOW_FURY("Shadow Fury"),
    LIVID_DAGGER("Livid Dagger"),
    
    // Slayer Weapons
    REVENANT_FALCHION("Revenant Falchion"),
    REAPER_FALCHION("Reaper Falchion"),
    REAPER_SCYTHE("Reaper Scythe"),
    AXE_OF_THE_SHREDDED("Axe of the Shredded"),
    VOIDEDGE_KATANA("Voidedge Katana"),
    VOIDWALKER_KATANA("Voidwalker Katana"),
    VOIDLING_KATANA("Voidling Katana"),
    
    // Mining Tools
    DIAMOND_PICKAXE("Diamond Pickaxe"),
    STONK("Stonk"),
    GOLDEN_PICKAXE("Golden Pickaxe"),
    MOLTEN_PICKAXE("Molten Pickaxe"),
    TITANIUM_PICKAXE("Titanium Pickaxe"),
    DRILL_ENGINE("Drill Engine"),
    TITANIUM_DRILL_DR_X355("Titanium Drill DR-X355"),
    TITANIUM_DRILL_DR_X455("Titanium Drill DR-X455"),
    TITANIUM_DRILL_DR_X555("Titanium Drill DR-X555"),
    GAUNTLET("Gauntlet"),
    
    // Fishing Rods
    ROD_OF_THE_SEA("Rod of the Sea"),
    CHALLENGING_ROD("Challenging Rod"),
    ROD_OF_LEGENDS("Rod of Legends"),
    SHARK_BAIT("Shark Bait"),
    SHARK_SCALE_ROD("Shark Scale Rod"),
    AUGER_ROD("Auger Rod"),
    
    // Magic Weapons and Wands
    FIRE_VEIL_WAND("Fire Veil Wand"),
    FROZEN_SCYTHE("Frozen Scythe"),
    VOODOO_DOLL("Voodoo Doll"),
    BONZO_STAFF("Bonzo Staff"),
    SCARF_STUDIES("Scarf's Studies"),
    PROFESSOR_SCARF_STAFF("Professor Scarf Staff"),
    THORN_BOW("Thorn Bow"),
    LAST_BREATH("Last Breath"),
    
    // Bows and Crossbows
    JUJU_SHORTBOW("Juju Shortbow"),
    TERMINATOR("Terminator"),
    ARTISANAL_SHORTBOW("Artisanal Shortbow"),
    MAGMA_BOW("Magma Bow"),
    VENOM_TOUCH("Venom Touch"),
    
    // Special Items and Tools
    GRAPPLING_HOOK("Grappling Hook"),
    ENDER_PEARL("Ender Pearl"),
    AOTE("Aspect of the End"),
    AOTV("Aspect of the Void"),
    PIGMAN_SWORD("Pigman Sword"),
    GOLDEN_APPLE("Golden Apple"),
    ENCHANTED_GOLDEN_APPLE("Enchanted Golden Apple"),
    POTATO_WAR_ARMOR("Potato War Armor");
    
    private final String displayName;
    
    ItemType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get item type by display name
     */
    public static ItemType fromDisplayName(String displayName) {
        for (ItemType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * Get all item types by category
     */
    public static ItemType[] getByCategory(ItemCategory category) {
        switch (category) {
            case DRAGON_WEAPONS:
                return new ItemType[]{
                    ASPECT_OF_THE_DRAGONS, ASPECT_OF_THE_END, ASPECT_OF_THE_VOID
                };
            case DUNGEON_WEAPONS:
                return new ItemType[]{
                    HYPERION, SCYLLA, ASTRAEA, VALKYRIE, BONEMERANG, SPIRIT_BOW,
                    SPIRIT_SCEPTER, SPIRIT_SWORD, ADAPTIVE_BLADE, SHADOW_FURY, LIVID_DAGGER
                };
            case SLAYER_WEAPONS:
                return new ItemType[]{
                    REVENANT_FALCHION, REAPER_FALCHION, REAPER_SCYTHE, AXE_OF_THE_SHREDDED,
                    VOIDEDGE_KATANA, VOIDWALKER_KATANA, VOIDLING_KATANA
                };
            case MINING_TOOLS:
                return new ItemType[]{
                    DIAMOND_PICKAXE, STONK, GOLDEN_PICKAXE, MOLTEN_PICKAXE, TITANIUM_PICKAXE,
                    DRILL_ENGINE, TITANIUM_DRILL_DR_X355, TITANIUM_DRILL_DR_X455, TITANIUM_DRILL_DR_X555, GAUNTLET
                };
            case FISHING_RODS:
                return new ItemType[]{
                    ROD_OF_THE_SEA, CHALLENGING_ROD, ROD_OF_LEGENDS, SHARK_BAIT, SHARK_SCALE_ROD, AUGER_ROD
                };
            case MAGIC_WEAPONS:
                return new ItemType[]{
                    FIRE_VEIL_WAND, FROZEN_SCYTHE, VOODOO_DOLL, BONZO_STAFF, SCARF_STUDIES,
                    PROFESSOR_SCARF_STAFF, THORN_BOW, LAST_BREATH
                };
            case BOWS_CROSSBOWS:
                return new ItemType[]{
                    JUJU_SHORTBOW, TERMINATOR, ARTISANAL_SHORTBOW, MAGMA_BOW, VENOM_TOUCH
                };
            case SPECIAL_ITEMS:
                return new ItemType[]{
                    GRAPPLING_HOOK, ENDER_PEARL, AOTE, AOTV, PIGMAN_SWORD,
                    GOLDEN_APPLE, ENCHANTED_GOLDEN_APPLE, POTATO_WAR_ARMOR
                };
            default:
                return new ItemType[0];
        }
    }
    
    /**
     * Get item rarity
     */
    public ItemRarity getRarity() {
        switch (this) {
            // Mythic
            case HYPERION, SCYLLA, ASTRAEA, VALKYRIE, TERMINATOR, VOIDLING_KATANA, TITANIUM_DRILL_DR_X555:
                return ItemRarity.MYTHIC;
            
            // Legendary
            case ASPECT_OF_THE_DRAGONS, ASPECT_OF_THE_VOID, BONEMERANG, SPIRIT_BOW, SPIRIT_SCEPTER, SPIRIT_SWORD,
                 SHADOW_FURY, LIVID_DAGGER, REAPER_SCYTHE, VOIDEDGE_KATANA, VOIDWALKER_KATANA, TITANIUM_PICKAXE,
                 TITANIUM_DRILL_DR_X355, TITANIUM_DRILL_DR_X455, GAUNTLET, ROD_OF_LEGENDS, SHARK_SCALE_ROD, AUGER_ROD,
                 FIRE_VEIL_WAND, FROZEN_SCYTHE, BONZO_STAFF, SCARF_STUDIES, PROFESSOR_SCARF_STAFF, LAST_BREATH,
                 JUJU_SHORTBOW, AOTV, ENCHANTED_GOLDEN_APPLE, POTATO_WAR_ARMOR:
                return ItemRarity.LEGENDARY;
            
            // Epic
            case ADAPTIVE_BLADE, REVENANT_FALCHION, REAPER_FALCHION, AXE_OF_THE_SHREDDED, MOLTEN_PICKAXE, DRILL_ENGINE,
                 CHALLENGING_ROD, SHARK_BAIT, VOODOO_DOLL, THORN_BOW, ARTISANAL_SHORTBOW, MAGMA_BOW, VENOM_TOUCH,
                 PIGMAN_SWORD, GOLDEN_APPLE:
                return ItemRarity.EPIC;
            
            // Rare
            case ASPECT_OF_THE_END, STONK, GOLDEN_PICKAXE, ROD_OF_THE_SEA, GRAPPLING_HOOK:
                return ItemRarity.RARE;
            
            // Uncommon
            case ENDER_PEARL, AOTE:
                return ItemRarity.UNCOMMON;
            
            // Common
            case DIAMOND_PICKAXE:
                return ItemRarity.COMMON;
            
            default:
                return ItemRarity.COMMON;
        }
    }
    
    /**
     * Get item category
     */
    public ItemCategory getCategory() {
        switch (this) {
            case ASPECT_OF_THE_DRAGONS, ASPECT_OF_THE_END, ASPECT_OF_THE_VOID:
                return ItemCategory.DRAGON_WEAPONS;
            case HYPERION, SCYLLA, ASTRAEA, VALKYRIE, BONEMERANG, SPIRIT_BOW, SPIRIT_SCEPTER, SPIRIT_SWORD, ADAPTIVE_BLADE, SHADOW_FURY, LIVID_DAGGER:
                return ItemCategory.DUNGEON_WEAPONS;
            case REVENANT_FALCHION, REAPER_FALCHION, REAPER_SCYTHE, AXE_OF_THE_SHREDDED, VOIDEDGE_KATANA, VOIDWALKER_KATANA, VOIDLING_KATANA:
                return ItemCategory.SLAYER_WEAPONS;
            case DIAMOND_PICKAXE, STONK, GOLDEN_PICKAXE, MOLTEN_PICKAXE, TITANIUM_PICKAXE, DRILL_ENGINE, TITANIUM_DRILL_DR_X355, TITANIUM_DRILL_DR_X455, TITANIUM_DRILL_DR_X555, GAUNTLET:
                return ItemCategory.MINING_TOOLS;
            case ROD_OF_THE_SEA, CHALLENGING_ROD, ROD_OF_LEGENDS, SHARK_BAIT, SHARK_SCALE_ROD, AUGER_ROD:
                return ItemCategory.FISHING_RODS;
            case FIRE_VEIL_WAND, FROZEN_SCYTHE, VOODOO_DOLL, BONZO_STAFF, SCARF_STUDIES, PROFESSOR_SCARF_STAFF, THORN_BOW, LAST_BREATH:
                return ItemCategory.MAGIC_WEAPONS;
            case JUJU_SHORTBOW, TERMINATOR, ARTISANAL_SHORTBOW, MAGMA_BOW, VENOM_TOUCH:
                return ItemCategory.BOWS_CROSSBOWS;
            case GRAPPLING_HOOK, ENDER_PEARL, AOTE, AOTV, PIGMAN_SWORD, GOLDEN_APPLE, ENCHANTED_GOLDEN_APPLE, POTATO_WAR_ARMOR:
                return ItemCategory.SPECIAL_ITEMS;
            default:
                return ItemCategory.SPECIAL_ITEMS;
        }
    }
}
