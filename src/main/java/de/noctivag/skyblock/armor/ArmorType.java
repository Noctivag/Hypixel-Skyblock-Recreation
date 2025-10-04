package de.noctivag.skyblock.armor;
import org.bukkit.inventory.ItemStack;

/**
 * ArmorType - Enum for all Hypixel SkyBlock armor types
 * 
 * This enum contains all armor sets from Hypixel SkyBlock including:
 * - Dragon Armor Sets
 * - Mining Armor Sets
 * - Combat Armor Sets
 * - Fishing Armor Sets
 * - Event/Seasonal Armor Sets
 * - Crimson Isle Armor Sets
 * - Special/Unique Armor Sets
 */
public enum ArmorType {
    // Dragon Armor Sets
    PROTECTOR_DRAGON("Protector Dragon Armor"),
    OLD_DRAGON("Old Dragon Armor"),
    WISE_DRAGON("Wise Dragon Armor"),
    YOUNG_DRAGON("Young Dragon Armor"),
    STRONG_DRAGON("Strong Dragon Armor"),
    UNSTABLE_DRAGON("Unstable Dragon Armor"),
    SUPERIOR_DRAGON("Superior Dragon Armor"),
    
    // Mining Armor Sets
    MINERAL_ARMOR("Mineral Armor"),
    GLACITE_ARMOR("Glacite Armor"),
    SORROW_ARMOR("Sorrow Armor"),
    DIVAN_ARMOR("DIVAN's Armor"),
    
    // Combat Armor Sets
    SHADOW_ASSASSIN_ARMOR("Shadow Assassin Armor"),
    NECRON_ARMOR("Necron's Armor"),
    STORM_ARMOR("Storm's Armor"),
    GOLDOR_ARMOR("Goldor's Armor"),
    MAXOR_ARMOR("Maxor's Armor"),
    FROZEN_BLAZE_ARMOR("Frozen Blaze Armor"),
    
    // Fishing Armor Sets
    SPONGE_ARMOR("Sponge Armor"),
    SHARK_SCALE_ARMOR("Shark Scale Armor"),
    
    // Event and Seasonal Armor Sets
    SPOOKY_ARMOR("Spooky Armor"),
    SNOW_SUIT("Snow Suit"),
    BAT_ARMOR("Bat Person Armor"),
    HALLOWEEN_ARMOR("Halloween Armor"),
    CHRISTMAS_ARMOR("Christmas Armor"),
    EASTER_ARMOR("Easter Armor"),
    
    // Crimson Isle Armor Sets
    CRIMSON_ARMOR("Crimson Armor"),
    TERROR_ARMOR("Terror Armor"),
    AURORA_ARMOR("Aurora Armor"),
    HOLLOW_ARMOR("Hollow Armor"),
    
    // Special and Unique Armor Sets
    EMERALD_ARMOR("Emerald Armor"),
    LEAFLET_ARMOR("Leaflet Armor"),
    ROSETTA_ARMOR("Rosetta's Armor"),
    
    // Basic Armor Sets
    LAPIS_ARMOR("Lapis Armor"),
    ENDER_ARMOR("Ender Armor"),
    GOLEM_ARMOR("Golem Armor"),
    DRAGON_ARMOR("Dragon Armor"),
    WITHER_ARMOR("Wither Armor"),
    
    // Dungeon Armor Sets
    BONZO_ARMOR("Bonzo's Armor"),
    SCARF_ARMOR("Scarf's Armor"),
    PROFESSOR_ARMOR("Professor's Armor"),
    THORN_ARMOR("Thorn's Armor"),
    LIFDA_ARMOR("Livid's Armor"),
    SPIRIT_ARMOR("Spirit Armor"),
    
    // Slayer Armor Sets
    REVENANT_ARMOR("Revenant Armor"),
    TARANTULA_ARMOR("Tarantula Armor"),
    VOIDGLOOM_ARMOR("Voidgloom Armor"),
    
    // Special Event Armor Sets
    BATTLE_ARMOR("Battle Armor"),
    WARRIOR_ARMOR("Warrior Armor");
    
    private final String displayName;
    
    ArmorType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get armor type by display name
     */
    public static ArmorType fromDisplayName(String displayName) {
        for (ArmorType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * Get all armor types by category
     */
    public static ArmorType[] getByCategory(ArmorCategory category) {
        switch (category) {
            case DRAGON:
                return new ArmorType[]{
                    PROTECTOR_DRAGON, OLD_DRAGON, WISE_DRAGON, YOUNG_DRAGON,
                    STRONG_DRAGON, UNSTABLE_DRAGON, SUPERIOR_DRAGON
                };
            case MINING:
                return new ArmorType[]{
                    MINERAL_ARMOR, GLACITE_ARMOR, SORROW_ARMOR, DIVAN_ARMOR
                };
            case COMBAT:
                return new ArmorType[]{
                    SHADOW_ASSASSIN_ARMOR, NECRON_ARMOR, STORM_ARMOR,
                    GOLDOR_ARMOR, MAXOR_ARMOR, FROZEN_BLAZE_ARMOR
                };
            case FISHING:
                return new ArmorType[]{
                    SPONGE_ARMOR, SHARK_SCALE_ARMOR
                };
            case EVENT:
                return new ArmorType[]{
                    SPOOKY_ARMOR, SNOW_SUIT, BAT_ARMOR, HALLOWEEN_ARMOR,
                    CHRISTMAS_ARMOR, EASTER_ARMOR
                };
            case NETHER:
                return new ArmorType[]{
                    CRIMSON_ARMOR, TERROR_ARMOR, AURORA_ARMOR, HOLLOW_ARMOR
                };
            case SPECIAL:
                return new ArmorType[]{
                    EMERALD_ARMOR, LEAFLET_ARMOR, ROSETTA_ARMOR
                };
            case BASIC:
                return new ArmorType[]{
                    LAPIS_ARMOR, ENDER_ARMOR, GOLEM_ARMOR, DRAGON_ARMOR, WITHER_ARMOR
                };
            case DUNGEON:
                return new ArmorType[]{
                    BONZO_ARMOR, SCARF_ARMOR, PROFESSOR_ARMOR, THORN_ARMOR,
                    LIFDA_ARMOR, SPIRIT_ARMOR
                };
            case SLAYER:
                return new ArmorType[]{
                    REVENANT_ARMOR, TARANTULA_ARMOR, VOIDGLOOM_ARMOR
                };
            default:
                return new ArmorType[0];
        }
    }
    
    /**
     * Get armor rarity
     */
    public ArmorRarity getRarity() {
        switch (this) {
            // Legendary
            case PROTECTOR_DRAGON, OLD_DRAGON, WISE_DRAGON, YOUNG_DRAGON, STRONG_DRAGON, UNSTABLE_DRAGON, SUPERIOR_DRAGON,
                 SORROW_ARMOR, DIVAN_ARMOR, SHADOW_ASSASSIN_ARMOR, NECRON_ARMOR, STORM_ARMOR, GOLDOR_ARMOR, MAXOR_ARMOR,
                 FROZEN_BLAZE_ARMOR, SHARK_SCALE_ARMOR, BAT_ARMOR, CRIMSON_ARMOR, TERROR_ARMOR, AURORA_ARMOR, HOLLOW_ARMOR,
                 ROSETTA_ARMOR, DRAGON_ARMOR, WITHER_ARMOR, SPIRIT_ARMOR, VOIDGLOOM_ARMOR, WARRIOR_ARMOR:
                return ArmorRarity.LEGENDARY;
            
            // Epic
            case MINERAL_ARMOR, GLACITE_ARMOR, SPONGE_ARMOR, SPOOKY_ARMOR, SNOW_SUIT, EMERALD_ARMOR, GOLEM_ARMOR,
                 BONZO_ARMOR, SCARF_ARMOR, PROFESSOR_ARMOR, THORN_ARMOR, LIFDA_ARMOR, REVENANT_ARMOR, TARANTULA_ARMOR,
                 BATTLE_ARMOR:
                return ArmorRarity.EPIC;
            
            // Rare
            case LEAFLET_ARMOR, LAPIS_ARMOR, ENDER_ARMOR, HALLOWEEN_ARMOR, CHRISTMAS_ARMOR, EASTER_ARMOR:
                return ArmorRarity.RARE;
            
            // Uncommon
            default:
                return ArmorRarity.UNCOMMON;
        }
    }
    
    /**
     * Get armor category
     */
    public ArmorCategory getCategory() {
        switch (this) {
            case PROTECTOR_DRAGON, OLD_DRAGON, WISE_DRAGON, YOUNG_DRAGON, STRONG_DRAGON, UNSTABLE_DRAGON, SUPERIOR_DRAGON:
                return ArmorCategory.DRAGON;
            case MINERAL_ARMOR, GLACITE_ARMOR, SORROW_ARMOR, DIVAN_ARMOR:
                return ArmorCategory.MINING;
            case SHADOW_ASSASSIN_ARMOR, NECRON_ARMOR, STORM_ARMOR, GOLDOR_ARMOR, MAXOR_ARMOR, FROZEN_BLAZE_ARMOR:
                return ArmorCategory.COMBAT;
            case SPONGE_ARMOR, SHARK_SCALE_ARMOR:
                return ArmorCategory.FISHING;
            case SPOOKY_ARMOR, SNOW_SUIT, BAT_ARMOR, HALLOWEEN_ARMOR, CHRISTMAS_ARMOR, EASTER_ARMOR:
                return ArmorCategory.EVENT;
            case CRIMSON_ARMOR, TERROR_ARMOR, AURORA_ARMOR, HOLLOW_ARMOR:
                return ArmorCategory.NETHER;
            case EMERALD_ARMOR, LEAFLET_ARMOR, ROSETTA_ARMOR:
                return ArmorCategory.SPECIAL;
            case LAPIS_ARMOR, ENDER_ARMOR, GOLEM_ARMOR, DRAGON_ARMOR, WITHER_ARMOR:
                return ArmorCategory.BASIC;
            case BONZO_ARMOR, SCARF_ARMOR, PROFESSOR_ARMOR, THORN_ARMOR, LIFDA_ARMOR, SPIRIT_ARMOR:
                return ArmorCategory.DUNGEON;
            case REVENANT_ARMOR, TARANTULA_ARMOR, VOIDGLOOM_ARMOR:
                return ArmorCategory.SLAYER;
            default:
                return ArmorCategory.BASIC;
        }
    }
    
    public enum ArmorRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        ArmorRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
}
