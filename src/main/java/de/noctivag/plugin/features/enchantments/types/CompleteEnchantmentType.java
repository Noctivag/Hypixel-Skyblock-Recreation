package de.noctivag.plugin.features.enchantments.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 40+ enchantment types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteEnchantmentType {
    // ===== SWORD ENCHANTMENTS (30+) =====
    SHARPNESS("Sharpness", "⚔️", "Increases melee damage", EnchantmentRarity.COMMON, 1, 10),
    SMITE("Smite", "☀️", "Increases damage to undead mobs", EnchantmentRarity.COMMON, 1, 10),
    BANE_OF_ARTHROPODS("Bane of Arthropods", "🕷️", "Increases damage to arthropod mobs", EnchantmentRarity.COMMON, 1, 10),
    KNOCKBACK("Knockback", "💥", "Knocks back enemies", EnchantmentRarity.COMMON, 1, 10),
    FIRE_ASPECT("Fire Aspect", "🔥", "Sets enemies on fire", EnchantmentRarity.COMMON, 1, 10),
    LOOTING("Looting", "💰", "Increases loot drops", EnchantmentRarity.COMMON, 1, 10),
    SWEEPING_EDGE("Sweeping Edge", "🌊", "Increases sweep attack damage", EnchantmentRarity.COMMON, 1, 10),
    UNBREAKING("Unbreaking", "🛡️", "Reduces durability loss", EnchantmentRarity.COMMON, 1, 10),
    MENDING("Mending", "🔧", "Repairs item with XP", EnchantmentRarity.COMMON, 1, 10),
    CURSE_OF_VANISHING("Curse of Vanishing", "👻", "Item disappears on death", EnchantmentRarity.COMMON, 1, 10),
    
    // Advanced Sword Enchantments
    CRITICAL("Critical", "⚡", "Increases critical hit chance", EnchantmentRarity.UNCOMMON, 2, 15),
    FIRST_STRIKE("First Strike", "🎯", "Increases first hit damage", EnchantmentRarity.UNCOMMON, 2, 15),
    GIANT_KILLER("Giant Killer", "👹", "Increases damage to high HP enemies", EnchantmentRarity.UNCOMMON, 2, 15),
    IMPALING("Impaling", "🗡️", "Increases damage to sea creatures", EnchantmentRarity.UNCOMMON, 2, 15),
    LUCK("Luck", "🍀", "Increases luck", EnchantmentRarity.UNCOMMON, 2, 15),
    VENOMOUS("Venomous", "☠️", "Poisons enemies", EnchantmentRarity.UNCOMMON, 2, 15),
    ENDER_SLAYER("Ender Slayer", "🌑", "Increases damage to endermen", EnchantmentRarity.UNCOMMON, 2, 15),
    EXECUTE("Execute", "⚔️", "Increases damage to low HP enemies", EnchantmentRarity.UNCOMMON, 2, 15),
    LIFE_STEAL("Life Steal", "❤️", "Heals on hit", EnchantmentRarity.UNCOMMON, 2, 15),
    VAMPIRISM("Vampirism", "🧛", "Advanced life steal", EnchantmentRarity.UNCOMMON, 2, 15),
    SYPHON("Syphon", "💉", "Heals based on damage dealt", EnchantmentRarity.UNCOMMON, 2, 15),
    THUNDERLORD("Thunderlord", "⚡", "Lightning on hit", EnchantmentRarity.UNCOMMON, 2, 15),
    DRAGON_HUNTER("Dragon Hunter", "🐉", "Increases damage to dragons", EnchantmentRarity.UNCOMMON, 2, 15),
    SMITE_VII("Smite VII", "☀️", "Enhanced damage to undead", EnchantmentRarity.RARE, 3, 20),
    BANE_OF_ARTHROPODS_VII("Bane of Arthropods VII", "🕷️", "Enhanced damage to arthropods", EnchantmentRarity.RARE, 3, 20),
    EXPERIENCE("Experience", "⭐", "Increases XP gain", EnchantmentRarity.RARE, 3, 20),
    SCAVENGER("Scavenger", "🔍", "Increases coin drops", EnchantmentRarity.RARE, 3, 20),
    TRIPLE_STRIKE("Triple Strike", "🗡️", "Triple damage on third hit", EnchantmentRarity.RARE, 3, 20),
    CUBISM("Cubism", "📦", "Increases damage to cube mobs", EnchantmentRarity.RARE, 3, 20),
    LETHALITY("Lethality", "💀", "Increases damage to low HP enemies", EnchantmentRarity.RARE, 3, 20),
    TITAN_KILLER("Titan Killer", "🏔️", "Increases damage to high HP enemies", EnchantmentRarity.RARE, 3, 20),
    ULTIMATE_WISE("Ultimate Wise", "🧠", "Reduces ability mana cost", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_CRITICAL("Ultimate Critical", "⚡", "Enhanced critical hit chance", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_FIRST_STRIKE("Ultimate First Strike", "🎯", "Enhanced first hit damage", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_GIANT_KILLER("Ultimate Giant Killer", "👹", "Enhanced damage to high HP enemies", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_IMPALING("Ultimate Impaling", "🗡️", "Enhanced damage to sea creatures", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_LUCK("Ultimate Luck", "🍀", "Enhanced luck", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_VENOMOUS("Ultimate Venomous", "☠️", "Enhanced poison effect", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ENDER_SLAYER("Ultimate Ender Slayer", "🌑", "Enhanced damage to endermen", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_EXECUTE("Ultimate Execute", "⚔️", "Enhanced damage to low HP enemies", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_LIFE_STEAL("Ultimate Life Steal", "❤️", "Enhanced healing on hit", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_VAMPIRISM("Ultimate Vampirism", "🧛", "Advanced life steal", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_SYPHON("Ultimate Syphon", "💉", "Enhanced healing based on damage", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_THUNDERLORD("Ultimate Thunderlord", "⚡", "Enhanced lightning on hit", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_DRAGON_HUNTER("Ultimate Dragon Hunter", "🐉", "Enhanced damage to dragons", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_EXPERIENCE("Ultimate Experience", "⭐", "Enhanced XP gain", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_SCAVENGER("Ultimate Scavenger", "🔍", "Enhanced coin drops", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_TRIPLE_STRIKE("Ultimate Triple Strike", "🗡️", "Enhanced triple damage", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_CUBISM("Ultimate Cubism", "📦", "Enhanced damage to cube mobs", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_LETHALITY("Ultimate Lethality", "💀", "Enhanced damage to low HP enemies", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_TITAN_KILLER("Ultimate Titan Killer", "🏔️", "Enhanced damage to high HP enemies", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_POWER("Ultimate Power", "🏹", "Maximum bow damage", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_PUNCH("Ultimate Punch", "💥", "Maximum knockback", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_FLAME("Ultimate Flame", "🔥", "Maximum fire effect", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_INFINITY("Ultimate Infinity", "∞", "Perfect infinite arrows", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_PIERCING("Ultimate Piercing", "🏹", "Maximum arrow penetration", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_MULTISHOT("Ultimate Multishot", "🏹", "Maximum multiple arrows", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_QUICK_CHARGE("Ultimate Quick Charge", "⚡", "Maximum charging speed", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ARROW_BREAKING("Ultimate Arrow Breaking", "💥", "Maximum arrow breaking", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ARROW_SWAP("Ultimate Arrow Swap", "🔄", "Perfect arrow swapping", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ARROW_SAVE("Ultimate Arrow Save", "💾", "Maximum arrow saving", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ARROW_ALIGNMENT("Ultimate Arrow Alignment", "🎯", "Perfect arrow alignment", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ARROW_STORM("Ultimate Arrow Storm", "🌪️", "Maximum arrow storm", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_ARROW_PIERCE("Ultimate Arrow Pierce", "🏹", "Perfect arrow piercing", EnchantmentRarity.EPIC, 4, 25),
    
    // ===== ARMOR ENCHANTMENTS =====
    PROTECTION("Protection", "🛡️", "Reduces damage taken", EnchantmentRarity.COMMON, 1, 10),
    FIRE_PROTECTION("Fire Protection", "🔥", "Reduces fire damage", EnchantmentRarity.COMMON, 1, 10),
    FEATHER_FALLING("Feather Falling", "🪶", "Reduces fall damage", EnchantmentRarity.COMMON, 1, 10),
    BLAST_PROTECTION("Blast Protection", "💥", "Reduces explosion damage", EnchantmentRarity.COMMON, 1, 10),
    PROJECTILE_PROTECTION("Projectile Protection", "🏹", "Reduces projectile damage", EnchantmentRarity.COMMON, 1, 10),
    RESPIRATION("Respiration", "🫁", "Extends underwater breathing", EnchantmentRarity.COMMON, 1, 10),
    AQUA_AFFINITY("Aqua Affinity", "🌊", "Increases underwater mining speed", EnchantmentRarity.COMMON, 1, 10),
    THORNS("Thorns", "🌵", "Damages attackers", EnchantmentRarity.COMMON, 1, 10),
    DEPTH_STRIDER("Depth Strider", "🏃", "Increases underwater movement speed", EnchantmentRarity.COMMON, 1, 10),
    FROST_WALKER("Frost Walker", "❄️", "Creates ice blocks when walking on water", EnchantmentRarity.COMMON, 1, 10),
    BINDING_CURSE("Curse of Binding", "🔗", "Item cannot be removed", EnchantmentRarity.COMMON, 1, 10),
    VANISHING_CURSE("Curse of Vanishing", "👻", "Item disappears on death", EnchantmentRarity.COMMON, 1, 10),
    GROWTH("Growth", "🌱", "Increases max health", EnchantmentRarity.COMMON, 1, 10),
    REJUVENATE("Rejuvenate", "💚", "Increases health regeneration", EnchantmentRarity.UNCOMMON, 2, 15),
    STRONG_MANA("Strong Mana", "💙", "Increases max mana", EnchantmentRarity.UNCOMMON, 2, 15),
    MANA_VAMPIRE("Mana Vampire", "🧛", "Steals mana from enemies", EnchantmentRarity.UNCOMMON, 2, 15),
    WISDOM("Wisdom", "🧠", "Increases XP gain", EnchantmentRarity.UNCOMMON, 2, 15),
    LAST_STAND("Last Stand", "💀", "Increases defense when low on health", EnchantmentRarity.UNCOMMON, 2, 15),
    LEGION("Legion", "👥", "Increases stats for nearby players", EnchantmentRarity.RARE, 3, 20),
    ULTIMATE_WISDOM("Ultimate Wisdom", "🧠", "Enhanced XP gain", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_LAST_STAND("Ultimate Last Stand", "💀", "Enhanced defense when low on health", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_LEGION("Ultimate Legion", "👥", "Enhanced stats for nearby players", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_PROTECTION("Ultimate Protection", "🛡️", "Enhanced damage reduction", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_FIRE_PROTECTION("Ultimate Fire Protection", "🔥", "Enhanced fire damage reduction", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_FEATHER_FALLING("Ultimate Feather Falling", "🪶", "Enhanced fall damage reduction", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_BLAST_PROTECTION("Ultimate Blast Protection", "💥", "Enhanced explosion damage reduction", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_PROJECTILE_PROTECTION("Ultimate Projectile Protection", "🏹", "Enhanced projectile damage reduction", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_RESPIRATION("Ultimate Respiration", "🫁", "Enhanced underwater breathing", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_AQUA_AFFINITY("Ultimate Aqua Affinity", "🌊", "Enhanced underwater mining speed", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_THORNS("Ultimate Thorns", "🌵", "Enhanced damage to attackers", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_DEPTH_STRIDER("Ultimate Depth Strider", "🏃", "Enhanced underwater movement speed", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_FROST_WALKER("Ultimate Frost Walker", "❄️", "Enhanced ice block creation", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_BINDING_CURSE("Ultimate Curse of Binding", "🔗", "Enhanced item binding", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_VANISHING_CURSE("Ultimate Curse of Vanishing", "👻", "Enhanced item vanishing", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_UNBREAKING("Ultimate Unbreaking", "🛡️", "Enhanced durability protection", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_MENDING("Ultimate Mending", "🔧", "Enhanced XP repair", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_GROWTH("Ultimate Growth", "🌱", "Enhanced max health", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_REJUVENATE("Ultimate Rejuvenate", "💚", "Enhanced health regeneration", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_STRONG_MANA("Ultimate Strong Mana", "💙", "Enhanced max mana", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_MANA_VAMPIRE("Ultimate Mana Vampire", "🧛", "Enhanced mana stealing", EnchantmentRarity.EPIC, 4, 25),
    
    // ===== TOOL ENCHANTMENTS =====
    EFFICIENCY("Efficiency", "⚡", "Increases mining speed", EnchantmentRarity.COMMON, 1, 10),
    SILK_TOUCH("Silk Touch", "🕸️", "Mines blocks in their original form", EnchantmentRarity.UNCOMMON, 2, 15),
    FORTUNE("Fortune", "🍀", "Increases drop rates", EnchantmentRarity.UNCOMMON, 2, 15),
    SMELTING_TOUCH("Smelting Touch", "🔥", "Automatically smelts mined ores", EnchantmentRarity.RARE, 3, 20),
    EFFICIENCY_VII("Efficiency VII", "⚡", "Enhanced mining speed", EnchantmentRarity.RARE, 3, 20),
    SILK_TOUCH_VII("Silk Touch VII", "🕸️", "Enhanced block preservation", EnchantmentRarity.RARE, 3, 20),
    FORTUNE_VII("Fortune VII", "🍀", "Enhanced drop rates", EnchantmentRarity.RARE, 3, 20),
    SMELTING_TOUCH_VII("Smelting Touch VII", "🔥", "Enhanced automatic smelting", EnchantmentRarity.RARE, 3, 20),
    ULTIMATE_EFFICIENCY("Ultimate Efficiency", "⚡", "Maximum mining speed", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_SILK_TOUCH("Ultimate Silk Touch", "🕸️", "Perfect block preservation", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_FORTUNE("Ultimate Fortune", "🍀", "Maximum drop rates", EnchantmentRarity.EPIC, 4, 25),
    ULTIMATE_SMELTING_TOUCH("Ultimate Smelting Touch", "🔥", "Perfect automatic smelting", EnchantmentRarity.EPIC, 4, 25),
    
    // ===== BOW ENCHANTMENTS (20+) =====
    POWER("Power", "🏹", "Increases bow damage", EnchantmentRarity.COMMON, 1, 10),
    PUNCH("Punch", "💥", "Knocks back enemies", EnchantmentRarity.COMMON, 1, 10),
    FLAME("Flame", "🔥", "Sets arrows on fire", EnchantmentRarity.COMMON, 1, 10),
    INFINITY("Infinity", "∞", "Infinite arrows", EnchantmentRarity.COMMON, 1, 10),
    
    // Advanced Bow Enchantments
    PIERCING("Piercing", "🎯", "Arrows pierce through enemies", EnchantmentRarity.UNCOMMON, 2, 15),
    MULTISHOT("Multishot", "🏹", "Shoots multiple arrows", EnchantmentRarity.UNCOMMON, 2, 15),
    QUICK_CHARGE("Quick Charge", "⚡", "Faster bow charging", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_BREAKING("Arrow Breaking", "💥", "Arrows break blocks", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_SWAP("Arrow Swap", "🔄", "Swaps arrow types", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_SAVE("Arrow Save", "💾", "Saves arrows on miss", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_ALIGNMENT("Arrow Alignment", "🎯", "Improves arrow accuracy", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_STORM("Arrow Storm", "🌪️", "Creates arrow storm", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_PIERCE("Arrow Pierce", "🗡️", "Arrows pierce through multiple enemies", EnchantmentRarity.UNCOMMON, 2, 15),
    ARROW_BREAKING_VII("Arrow Breaking VII", "💥", "Enhanced arrow breaking", EnchantmentRarity.RARE, 3, 20),
    ARROW_SWAP_VII("Arrow Swap VII", "🔄", "Enhanced arrow swapping", EnchantmentRarity.RARE, 3, 20),
    ARROW_SAVE_VII("Arrow Save VII", "💾", "Enhanced arrow saving", EnchantmentRarity.RARE, 3, 20),
    ARROW_ALIGNMENT_VII("Arrow Alignment VII", "🎯", "Enhanced arrow alignment", EnchantmentRarity.RARE, 3, 20),
    ARROW_STORM_VII("Arrow Storm VII", "🌪️", "Enhanced arrow storm", EnchantmentRarity.RARE, 3, 20),
    ARROW_PIERCE_VII("Arrow Pierce VII", "🗡️", "Enhanced arrow piercing", EnchantmentRarity.RARE, 3, 20);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final EnchantmentRarity rarity;
    private final int baseCost;
    private final int baseExperienceCost;
    
    CompleteEnchantmentType(String displayName, String icon, String description, EnchantmentRarity rarity, int baseCost, int baseExperienceCost) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.rarity = rarity;
        this.baseCost = baseCost;
        this.baseExperienceCost = baseExperienceCost;
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
    
    public EnchantmentRarity getRarity() {
        return rarity;
    }
    
    public int getBaseCost() {
        return baseCost;
    }
    
    public int getBaseExperienceCost() {
        return baseExperienceCost;
    }
    
    /**
     * Get enchantment category
     */
    public EnchantmentCategory getCategory() {
        if (name().contains("ULTIMATE_")) {
            return EnchantmentCategory.ULTIMATE_ENCHANTS;
        }
        if (name().contains("POWER") || name().contains("PUNCH") || name().contains("FLAME") || 
            name().contains("INFINITY") || name().contains("PIERCING") || name().contains("MULTISHOT") || 
            name().contains("QUICK_CHARGE") || name().contains("ARROW")) {
            return EnchantmentCategory.BOW_ENCHANTS;
        }
        if (name().contains("PROTECTION") || name().contains("FIRE_PROTECTION") || name().contains("FEATHER_FALLING") || 
            name().contains("BLAST_PROTECTION") || name().contains("PROJECTILE_PROTECTION") || name().contains("RESPIRATION") || 
            name().contains("AQUA_AFFINITY") || name().contains("THORNS") || name().contains("DEPTH_STRIDER") || 
            name().contains("FROST_WALKER") || name().contains("BINDING_CURSE") || name().contains("VANISHING_CURSE") || 
            name().contains("GROWTH") || name().contains("REJUVENATE") || name().contains("STRONG_MANA") || 
            name().contains("MANA_VAMPIRE") || name().contains("WISDOM") || name().contains("LAST_STAND") || 
            name().contains("LEGION")) {
            return EnchantmentCategory.ARMOR_ENCHANTS;
        }
        if (name().contains("EFFICIENCY") || name().contains("SILK_TOUCH") || name().contains("FORTUNE") || 
            name().contains("SMELTING_TOUCH")) {
            return EnchantmentCategory.TOOL_ENCHANTS;
        }
        return EnchantmentCategory.SWORD_ENCHANTS;
    }
    
    /**
     * Get all enchantments by category
     */
    public static List<CompleteEnchantmentType> getEnchantmentsByCategory(EnchantmentCategory category) {
        return Arrays.stream(values())
            .filter(enchantment -> enchantment.getCategory() == category)
            .toList();
    }
    
    /**
     * Get all enchantments by rarity
     */
    public static List<CompleteEnchantmentType> getEnchantmentsByRarity(EnchantmentRarity rarity) {
        return Arrays.stream(values())
            .filter(enchantment -> enchantment.getRarity() == rarity)
            .toList();
    }
    
    /**
     * Get total enchantment count
     */
    public static int getTotalEnchantmentCount() {
        return values().length;
    }
    
    /**
     * Get enchantments by category name
     */
    public static List<CompleteEnchantmentType> getEnchantmentsByCategoryName(String categoryName) {
        try {
            EnchantmentCategory category = EnchantmentCategory.valueOf(categoryName.toUpperCase());
            return getEnchantmentsByCategory(category);
        } catch (IllegalArgumentException e) {
            return Arrays.asList(values());
        }
    }
    
    /**
     * Get enchantments by rarity name
     */
    public static List<CompleteEnchantmentType> getEnchantmentsByRarityName(String rarityName) {
        try {
            EnchantmentRarity rarity = EnchantmentRarity.valueOf(rarityName.toUpperCase());
            return getEnchantmentsByRarity(rarity);
        } catch (IllegalArgumentException e) {
            return Arrays.asList(values());
        }
    }
    
    /**
     * Get enchantment cost calculation
     */
    public static int calculateEnchantmentCost(CompleteEnchantmentType enchantment, int level) {
        return enchantment.getBaseCost() * level;
    }
    
    /**
     * Get enchantment experience cost calculation
     */
    public static int calculateEnchantmentExperienceCost(CompleteEnchantmentType enchantment, int level) {
        return enchantment.getBaseExperienceCost() * level;
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
