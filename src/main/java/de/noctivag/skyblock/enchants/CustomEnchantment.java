package de.noctivag.skyblock.enchants;

/**
 * All custom enchantments in Hypixel Skyblock
 * Includes both normal and ultimate enchantments
 */
public enum CustomEnchantment {

    // ===== COMBAT ENCHANTMENTS =====
    SHARPNESS("Sharpness", 7, EnchantType.NORMAL, "Increases melee damage by 5% per level", ApplicableTo.SWORD),
    CRITICAL("Critical", 7, EnchantType.NORMAL, "Increases crit damage by 10% per level", ApplicableTo.SWORD),
    ENDER_SLAYER("Ender Slayer", 7, EnchantType.NORMAL, "+12% damage to Endermen per level", ApplicableTo.SWORD),
    CUBISM("Cubism", 6, EnchantType.NORMAL, "+10% damage to Magma Cubes and Slimes per level", ApplicableTo.SWORD),
    IMPALING("Impaling", 3, EnchantType.NORMAL, "+12.5% damage to Ocean mobs per level", ApplicableTo.SWORD),
    GIANT_KILLER("Giant Killer", 7, EnchantType.NORMAL, "Increases damage dealt per 25 HP the enemy has above you", ApplicableTo.SWORD),
    EXECUTE("Execute", 6, EnchantType.NORMAL, "Increases damage dealt to enemies with less than 10% HP", ApplicableTo.SWORD),
    FIRST_STRIKE("First Strike", 5, EnchantType.NORMAL, "+25% damage on first hit per level", ApplicableTo.SWORD),
    TRIPLE_STRIKE("Triple Strike", 5, EnchantType.NORMAL, "+10% chance to strike 3 times per level", ApplicableTo.SWORD),
    VAMPIRISM("Vampirism", 6, EnchantType.NORMAL, "Heals 0.5 HP per hit per level", ApplicableTo.SWORD),
    LIFE_STEAL("Life Steal", 4, EnchantType.NORMAL, "Heals 0.5 HP per percent of damage dealt", ApplicableTo.SWORD),
    VENOMOUS("Venomous", 6, EnchantType.NORMAL, "Grants a chance to poison enemies", ApplicableTo.SWORD),
    SCAVENGER("Scavenger", 5, EnchantType.NORMAL, "Increases coins dropped by monsters by 5-20% per level", ApplicableTo.SWORD),
    LOOTING("Looting", 5, EnchantType.NORMAL, "+1% chance for mobs to drop items per level", ApplicableTo.SWORD),
    LUCK("Luck", 7, EnchantType.NORMAL, "+5 ✯ Magic Find per level", ApplicableTo.SWORD),
    THUNDERLORD("Thunderlord", 7, EnchantType.NORMAL, "Strikes lightning on hit", ApplicableTo.SWORD),
    SMITE("Smite", 7, EnchantType.NORMAL, "+8% damage to Undead per level", ApplicableTo.SWORD),
    BANE_OF_ARTHROPODS("Bane of Arthropods", 7, EnchantType.NORMAL, "+8% damage to Spiders per level", ApplicableTo.SWORD),
    CLEAVE("Cleave", 6, EnchantType.NORMAL, "Deals AOE damage to nearby enemies", ApplicableTo.SWORD),
    PROSECUTE("Prosecute", 6, EnchantType.NORMAL, "+10% damage per level against enemies with full HP", ApplicableTo.SWORD),
    
    // BOW ENCHANTMENTS
    POWER("Power", 7, EnchantType.NORMAL, "+8% bow damage per level", ApplicableTo.BOW),
    AIMING("Aiming", 5, EnchantType.NORMAL, "+6% bow damage per level", ApplicableTo.BOW),
    INFINITE_QUIVER("Infinite Quiver", 10, EnchantType.NORMAL, "Saves arrows", ApplicableTo.BOW),
    SNIPE("Snipe", 4, EnchantType.NORMAL, "+6% damage per 10 blocks travelled", ApplicableTo.BOW),
    PIERCING("Piercing", 1, EnchantType.NORMAL, "Arrows pass through enemies", ApplicableTo.BOW),
    FLAME("Flame", 1, EnchantType.NORMAL, "Lights arrows on fire", ApplicableTo.BOW),
    PUNCH("Punch", 2, EnchantType.NORMAL, "Increases arrow knockback", ApplicableTo.BOW),
    OVERLOAD("Overload", 5, EnchantType.NORMAL, "+20-100% chance to duplicate hits", ApplicableTo.BOW),
    DRAGON_HUNTER("Dragon Hunter", 5, EnchantType.NORMAL, "+25% damage to Dragons per level", ApplicableTo.BOW),
    
    // ARMOR ENCHANTMENTS
    PROTECTION("Protection", 7, EnchantType.NORMAL, "+3% defense per level", ApplicableTo.ARMOR),
    GROWTH("Growth", 7, EnchantType.NORMAL, "+15 HP per level", ApplicableTo.ARMOR),
    FEATHER_FALLING("Feather Falling", 10, EnchantType.NORMAL, "Reduces fall damage", ApplicableTo.BOOTS),
    TRUE_PROTECTION("True Protection", 1, EnchantType.NORMAL, "Increases defense by 5-35", ApplicableTo.ARMOR),
    SUGAR_RUSH("Sugar Rush", 3, EnchantType.NORMAL, "+5-15 Speed", ApplicableTo.BOOTS),
    THORNS("Thorns", 3, EnchantType.NORMAL, "Reflects damage back to attacker", ApplicableTo.ARMOR),
    RESPIRATION("Respiration", 5, EnchantType.NORMAL, "Extends underwater breathing time", ApplicableTo.HELMET),
    AQUA_AFFINITY("Aqua Affinity", 1, EnchantType.NORMAL, "Increases underwater mining speed", ApplicableTo.HELMET),
    DEPTH_STRIDER("Depth Strider", 3, EnchantType.NORMAL, "Increases underwater movement", ApplicableTo.BOOTS),
    FROST_WALKER("Frost Walker", 2, EnchantType.NORMAL, "Creates ice beneath feet", ApplicableTo.BOOTS),
    COUNTER_STRIKE("Counter Strike", 5, EnchantType.NORMAL, "Reflects damage when blocking", ApplicableTo.ARMOR),
    BIG_BRAIN("Big Brain", 5, EnchantType.NORMAL, "+5 Intelligence per level", ApplicableTo.HELMET),
    SMARTY_PANTS("Smarty Pants", 5, EnchantType.NORMAL, "+5 Intelligence per level", ApplicableTo.LEGGINGS),
    MANA_VAMPIRE("Mana Vampire", 3, EnchantType.NORMAL, "Restores mana on killing mobs", ApplicableTo.ARMOR),
    REJUVENATE("Rejuvenate", 5, EnchantType.NORMAL, "Regenerates health outside combat", ApplicableTo.ARMOR),
    RESPITE("Respite", 5, EnchantType.NORMAL, "Regenerates mana outside combat", ApplicableTo.ARMOR),
    
    // TOOL ENCHANTMENTS
    EFFICIENCY("Efficiency", 10, EnchantType.NORMAL, "Increases mining speed", ApplicableTo.TOOL),
    FORTUNE("Fortune", 3, EnchantType.NORMAL, "+33% chance for extra drops per level", ApplicableTo.TOOL),
    SMELTING_TOUCH("Smelting Touch", 1, EnchantType.NORMAL, "Auto-smelts mined blocks", ApplicableTo.TOOL),
    PRISTINE("Pristine", 5, EnchantType.NORMAL, "+4% increased block drops per level", ApplicableTo.TOOL),
    COMPACT("Compact", 10, EnchantType.NORMAL, "Compacts drops into enchanted forms", ApplicableTo.TOOL),
    CULTIVATING("Cultivating", 10, EnchantType.NORMAL, "Grants farming XP", ApplicableTo.HOE),
    TURBO_CROP("Turbo Crops", 5, EnchantType.NORMAL, "+5% farming speed per level", ApplicableTo.HOE),
    HARVESTING("Harvesting", 6, EnchantType.NORMAL, "Increases crop yield", ApplicableTo.HOE),
    REPLENISH("Replenish", 1, EnchantType.NORMAL, "Auto-replants crops", ApplicableTo.HOE),
    
    // FISHING ENCHANTMENTS
    ANGLER("Angler", 6, EnchantType.NORMAL, "Increases fishing speed", ApplicableTo.FISHING_ROD),
    CASTER("Caster", 6, EnchantType.NORMAL, "Increases fishing speed", ApplicableTo.FISHING_ROD),
    LUCK_OF_THE_SEA("Luck of the Sea", 6, EnchantType.NORMAL, "+5% chance for rare sea creatures per level", ApplicableTo.FISHING_ROD),
    LURE("Lure", 6, EnchantType.NORMAL, "+5% chance to catch fish per level", ApplicableTo.FISHING_ROD),
    MAGNET("Magnet", 6, EnchantType.NORMAL, "Pulls caught items towards you", ApplicableTo.FISHING_ROD),
    FRAIL("Frail", 6, EnchantType.NORMAL, "+10% chance to catch treasure per level", ApplicableTo.FISHING_ROD),
    
    // ===== ULTIMATE ENCHANTMENTS =====
    ULTIMATE_WISE("Ultimate Wise", 5, EnchantType.ULTIMATE, "Reduces ability mana cost by 10% per level", ApplicableTo.WEAPON_ARMOR),
    ULTIMATE_BANK("Ultimate Bank", 5, EnchantType.ULTIMATE, "Refunds mana cost when below 25% per level", ApplicableTo.ARMOR),
    ULTIMATE_JERRY("Ultimate Jerry", 5, EnchantType.ULTIMATE, "Increases all stats by 5% per level", ApplicableTo.ARMOR),
    ULTIMATE_LAST_STAND("Ultimate Last Stand", 5, EnchantType.ULTIMATE, "Grants invincibility frames when below 10% HP", ApplicableTo.ARMOR),
    ULTIMATE_LEGION("Ultimate Legion", 5, EnchantType.ULTIMATE, "Grants +2 Strength and Defense per nearby player per level", ApplicableTo.ARMOR),
    ULTIMATE_REND("Ultimate Rend", 5, EnchantType.ULTIMATE, "Deals +5% damage to wounded enemies per level", ApplicableTo.SWORD),
    ULTIMATE_CHIMERA("Ultimate Chimera", 5, EnchantType.ULTIMATE, "Grants +0.2 Speed per 1% HP missing per level", ApplicableTo.ARMOR),
    ULTIMATE_ONE_FOR_ALL("One For All", 1, EnchantType.ULTIMATE, "Removes all other enchants but multiplies damage by 130%", ApplicableTo.SWORD),
    ULTIMATE_COMBO("Combo", 5, EnchantType.ULTIMATE, "Grants +2 damage per combo level per enchant level", ApplicableTo.SWORD),
    ULTIMATE_SOUL_EATER("Soul Eater", 5, EnchantType.ULTIMATE, "Grants +2 Speed and Intelligence per soul per level", ApplicableTo.SWORD),
    ULTIMATE_SWARM("Swarm", 5, EnchantType.ULTIMATE, "+4 Defense and +1 Damage per nearby enemy per level", ApplicableTo.SWORD);

    private final String displayName;
    private final int maxLevel;
    private final EnchantType type;
    private final String description;
    private final ApplicableTo applicableTo;

    CustomEnchantment(String displayName, int maxLevel, EnchantType type, String description, ApplicableTo applicableTo) {
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.type = type;
        this.description = description;
        this.applicableTo = applicableTo;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxLevel() { return maxLevel; }
    public EnchantType getType() { return type; }
    public String getDescription() { return description; }
    public ApplicableTo getApplicableTo() { return applicableTo; }

    public enum EnchantType {
        NORMAL,    // Can have multiple
        ULTIMATE   // Only one per item
    }

    public enum ApplicableTo {
        SWORD,
        BOW,
        ARMOR,
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        TOOL,
        PICKAXE,
        AXE,
        SHOVEL,
        HOE,
        FISHING_ROD,
        WEAPON_ARMOR, // Ultimate enchants that work on both
        ALL
    }

    /**
     * Check if this enchantment can be applied to an item type
     */
    public boolean canApplyTo(String itemType) {
        if (applicableTo == ApplicableTo.ALL) return true;
        
        return switch (applicableTo) {
            case SWORD -> itemType.contains("SWORD") || itemType.contains("AXE");
            case BOW -> itemType.contains("BOW");
            case ARMOR -> itemType.contains("HELMET") || itemType.contains("CHESTPLATE") ||
                         itemType.contains("LEGGINGS") || itemType.contains("BOOTS");
            case HELMET -> itemType.contains("HELMET");
            case CHESTPLATE -> itemType.contains("CHESTPLATE");
            case LEGGINGS -> itemType.contains("LEGGINGS");
            case BOOTS -> itemType.contains("BOOTS");
            case TOOL -> itemType.contains("PICKAXE") || itemType.contains("AXE") ||
                        itemType.contains("SHOVEL") || itemType.contains("HOE");
            case PICKAXE -> itemType.contains("PICKAXE");
            case AXE -> itemType.contains("AXE");
            case SHOVEL -> itemType.contains("SHOVEL");
            case HOE -> itemType.contains("HOE");
            case FISHING_ROD -> itemType.contains("FISHING_ROD");
            case WEAPON_ARMOR -> itemType.contains("SWORD") || itemType.contains("BOW") ||
                               itemType.contains("HELMET") || itemType.contains("CHESTPLATE") ||
                               itemType.contains("LEGGINGS") || itemType.contains("BOOTS");
            default -> false;
        };
    }
}
