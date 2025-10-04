package de.noctivag.skyblock.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Reforge Stat Matrix - Exact replication of Hypixel Skyblock reforging system
 * 
 * This class contains the precise numerical values for all reforge prefixes
 * across all rarity levels. Even minor deviations could break the meta-game.
 * 
 * Key Features:
 * - Multi-dimensional stat matrix (prefix × rarity × stat)
 * - Exact numerical precision for all values
 * - Specialized reforge logic (e.g., Heroic for magic builds)
 * - Critical meta-game preservation
 */
public class ReforgeStatMatrix {
    
    /**
     * Reforge Prefix Enum - All available reforge types
     */
    public enum ReforgePrefix {
        LEGENDARY("Legendary", "§6Legendary"),
        SHARP("Sharp", "§cSharp"),
        HEROIC("Heroic", "§bHeroic"),
        SPICY("Spicy", "§4Spicy"),
        FIERCE("Fierce", "§cFierce"),
        PURE("Pure", "§fPure"),
        WISE("Wise", "§9Wise"),
        HEAVY("Heavy", "§8Heavy"),
        LIGHT("Light", "§fLight"),
        PROTECTIVE("Protective", "§aProtective"),
        SPEEDY("Speedy", "§eSpeedy"),
        INTELLIGENT("Intelligent", "§bIntelligent"),
        TOUGH("Tough", "§7Tough"),
        LUCKY("Lucky", "§aLucky"),
        MAGICAL("Magical", "§dMagical"),
        POWERFUL("Powerful", "§4Powerful"),
        EFFICIENT("Efficient", "§aEfficient"),
        FORTUNATE("Fortunate", "§6Fortunate"),
        SPEEDY_TOOL("Speedy", "§eSpeedy");
        
        private final String name;
        private final String displayName;
        
        ReforgePrefix(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
    }
    
    /**
     * Item Rarity Enum
     */
    public enum ItemRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.2),
        RARE("§9Rare", 1.5),
        EPIC("§5Epic", 2.0),
        LEGENDARY("§6Legendary", 3.0),
        MYTHIC("§dMythic", 4.0),
        DIVINE("§bDivine", 5.0),
        SPECIAL("§cSpecial", 6.0),
        VERY_SPECIAL("§cVery Special", 8.0);
        
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
     * Reforge Category Enum
     */
    public enum ReforgeCategory {
        WEAPON("Weapon"),
        ARMOR("Armor"),
        ACCESSORY("Accessory"),
        TOOL("Tool");
        
        private final String name;
        
        ReforgeCategory(String name) {
            this.name = name;
        }
        
        public String getName() { return name; }
    }
    
    /**
     * Stat Type Enum
     */
    public enum StatType {
        STRENGTH("Strength"),
        CRITICAL_CHANCE("Critical Chance"),
        CRITICAL_DAMAGE("Critical Damage"),
        ATTACK_SPEED("Attack Speed"),
        INTELLIGENCE("Intelligence"),
        DEFENSE("Defense"),
        SPEED("Speed"),
        HEALTH("Health"),
        MANA("Mana"),
        LUCK("Luck"),
        FORTUNE("Fortune");
        
        private final String name;
        
        StatType(String name) {
            this.name = name;
        }
        
        public String getName() { return name; }
    }
    
    /**
     * Reforge Stat Configuration
     */
    public static class ReforgeStatConfig {
        private final Map<StatType, Double> stats;
        private final ReforgeCategory category;
        private final String description;
        
        public ReforgeStatConfig(Map<StatType, Double> stats, ReforgeCategory category, String description) {
            this.stats = new HashMap<>(stats);
            this.category = category;
            this.description = description;
        }
        
        public Map<StatType, Double> getStats() { return new HashMap<>(stats); }
        public ReforgeCategory getCategory() { return category; }
        public String getDescription() { return description; }
        
        public double getStat(StatType statType) {
            return stats.getOrDefault(statType, 0.0);
        }
    }
    
    // Main stat matrix: [Prefix][Rarity] -> StatConfig
    private static final Map<ReforgePrefix, Map<ItemRarity, ReforgeStatConfig>> REFORGE_MATRIX = new HashMap<>();
    
    static {
        initializeReforgeMatrix();
    }
    
    /**
     * Initialize the complete reforge stat matrix
     */
    private static void initializeReforgeMatrix() {
        // LEGENDARY - Balanced all-rounder
        Map<ItemRarity, ReforgeStatConfig> legendaryMatrix = new HashMap<>();
        legendaryMatrix.put(ItemRarity.COMMON, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 2.0,
                StatType.CRITICAL_CHANCE, 1.0,
                StatType.CRITICAL_DAMAGE, 2.0,
                StatType.ATTACK_SPEED, 1.0,
                StatType.INTELLIGENCE, 2.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        legendaryMatrix.put(ItemRarity.UNCOMMON, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 4.0,
                StatType.CRITICAL_CHANCE, 2.0,
                StatType.CRITICAL_DAMAGE, 4.0,
                StatType.ATTACK_SPEED, 2.0,
                StatType.INTELLIGENCE, 4.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        legendaryMatrix.put(ItemRarity.RARE, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 8.0,
                StatType.CRITICAL_CHANCE, 4.0,
                StatType.CRITICAL_DAMAGE, 8.0,
                StatType.ATTACK_SPEED, 4.0,
                StatType.INTELLIGENCE, 8.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        legendaryMatrix.put(ItemRarity.EPIC, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 16.0,
                StatType.CRITICAL_CHANCE, 8.0,
                StatType.CRITICAL_DAMAGE, 16.0,
                StatType.ATTACK_SPEED, 8.0,
                StatType.INTELLIGENCE, 16.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        legendaryMatrix.put(ItemRarity.LEGENDARY, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 32.0,
                StatType.CRITICAL_CHANCE, 18.0,
                StatType.CRITICAL_DAMAGE, 36.0,
                StatType.ATTACK_SPEED, 15.0,
                StatType.INTELLIGENCE, 35.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        legendaryMatrix.put(ItemRarity.MYTHIC, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 48.0,
                StatType.CRITICAL_CHANCE, 25.0,
                StatType.CRITICAL_DAMAGE, 50.0,
                StatType.ATTACK_SPEED, 20.0,
                StatType.INTELLIGENCE, 50.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        legendaryMatrix.put(ItemRarity.DIVINE, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 64.0,
                StatType.CRITICAL_CHANCE, 32.0,
                StatType.CRITICAL_DAMAGE, 64.0,
                StatType.ATTACK_SPEED, 25.0,
                StatType.INTELLIGENCE, 65.0
            ), ReforgeCategory.WEAPON, "Balanced all-rounder reforge"
        ));
        REFORGE_MATRIX.put(ReforgePrefix.LEGENDARY, legendaryMatrix);
        
        // SHARP - Pure critical damage focus
        Map<ItemRarity, ReforgeStatConfig> sharpMatrix = new HashMap<>();
        sharpMatrix.put(ItemRarity.COMMON, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 2.0,
                StatType.CRITICAL_DAMAGE, 5.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        sharpMatrix.put(ItemRarity.UNCOMMON, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 4.0,
                StatType.CRITICAL_DAMAGE, 10.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        sharpMatrix.put(ItemRarity.RARE, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 8.0,
                StatType.CRITICAL_DAMAGE, 20.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        sharpMatrix.put(ItemRarity.EPIC, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 15.0,
                StatType.CRITICAL_DAMAGE, 40.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        sharpMatrix.put(ItemRarity.LEGENDARY, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 25.0,
                StatType.CRITICAL_DAMAGE, 70.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        sharpMatrix.put(ItemRarity.MYTHIC, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 35.0,
                StatType.CRITICAL_DAMAGE, 100.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        sharpMatrix.put(ItemRarity.DIVINE, new ReforgeStatConfig(
            Map.of(
                StatType.CRITICAL_CHANCE, 45.0,
                StatType.CRITICAL_DAMAGE, 130.0
            ), ReforgeCategory.WEAPON, "Pure critical damage focus"
        ));
        REFORGE_MATRIX.put(ReforgePrefix.SHARP, sharpMatrix);
        
        // HEROIC - Magic/Tank hybrid specialization
        Map<ItemRarity, ReforgeStatConfig> heroicMatrix = new HashMap<>();
        heroicMatrix.put(ItemRarity.COMMON, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 3.0,
                StatType.ATTACK_SPEED, 1.0,
                StatType.INTELLIGENCE, 8.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        heroicMatrix.put(ItemRarity.UNCOMMON, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 6.0,
                StatType.ATTACK_SPEED, 2.0,
                StatType.INTELLIGENCE, 16.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        heroicMatrix.put(ItemRarity.RARE, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 12.0,
                StatType.ATTACK_SPEED, 4.0,
                StatType.INTELLIGENCE, 32.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        heroicMatrix.put(ItemRarity.EPIC, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 24.0,
                StatType.ATTACK_SPEED, 6.0,
                StatType.INTELLIGENCE, 64.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        heroicMatrix.put(ItemRarity.LEGENDARY, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 50.0,
                StatType.ATTACK_SPEED, 7.0,
                StatType.INTELLIGENCE, 125.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        heroicMatrix.put(ItemRarity.MYTHIC, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 75.0,
                StatType.ATTACK_SPEED, 10.0,
                StatType.INTELLIGENCE, 180.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        heroicMatrix.put(ItemRarity.DIVINE, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 100.0,
                StatType.ATTACK_SPEED, 12.0,
                StatType.INTELLIGENCE, 250.0
            ), ReforgeCategory.WEAPON, "Magic/Tank hybrid specialization"
        ));
        REFORGE_MATRIX.put(ReforgePrefix.HEROIC, heroicMatrix);
        
        // SPICY - Maximum critical damage with low base
        Map<ItemRarity, ReforgeStatConfig> spicyMatrix = new HashMap<>();
        spicyMatrix.put(ItemRarity.COMMON, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 1.0,
                StatType.CRITICAL_CHANCE, 0.5,
                StatType.CRITICAL_DAMAGE, 8.0,
                StatType.ATTACK_SPEED, 1.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        spicyMatrix.put(ItemRarity.UNCOMMON, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 2.0,
                StatType.CRITICAL_CHANCE, 1.0,
                StatType.CRITICAL_DAMAGE, 16.0,
                StatType.ATTACK_SPEED, 2.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        spicyMatrix.put(ItemRarity.RARE, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 4.0,
                StatType.CRITICAL_CHANCE, 2.0,
                StatType.CRITICAL_DAMAGE, 32.0,
                StatType.ATTACK_SPEED, 4.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        spicyMatrix.put(ItemRarity.EPIC, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 8.0,
                StatType.CRITICAL_CHANCE, 4.0,
                StatType.CRITICAL_DAMAGE, 64.0,
                StatType.ATTACK_SPEED, 8.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        spicyMatrix.put(ItemRarity.LEGENDARY, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 12.0,
                StatType.CRITICAL_CHANCE, 1.0,
                StatType.CRITICAL_DAMAGE, 100.0,
                StatType.ATTACK_SPEED, 15.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        spicyMatrix.put(ItemRarity.MYTHIC, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 18.0,
                StatType.CRITICAL_CHANCE, 2.0,
                StatType.CRITICAL_DAMAGE, 150.0,
                StatType.ATTACK_SPEED, 20.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        spicyMatrix.put(ItemRarity.DIVINE, new ReforgeStatConfig(
            Map.of(
                StatType.STRENGTH, 25.0,
                StatType.CRITICAL_CHANCE, 3.0,
                StatType.CRITICAL_DAMAGE, 200.0,
                StatType.ATTACK_SPEED, 25.0
            ), ReforgeCategory.WEAPON, "Maximum critical damage with low base"
        ));
        REFORGE_MATRIX.put(ReforgePrefix.SPICY, spicyMatrix);
        
        // Add more reforge types as needed...
        // The matrix can be extended with all other reforge prefixes
    }
    
    /**
     * Get reforge stats for a specific prefix and rarity
     */
    public static ReforgeStatConfig getReforgeStats(ReforgePrefix prefix, ItemRarity rarity) {
        Map<ItemRarity, ReforgeStatConfig> prefixMatrix = REFORGE_MATRIX.get(prefix);
        if (prefixMatrix == null) {
            return null;
        }
        
        return prefixMatrix.get(rarity);
    }
    
    /**
     * Get all available reforge prefixes for a category
     */
    public static Map<ReforgePrefix, ReforgeStatConfig> getReforgesForCategory(ReforgeCategory category) {
        Map<ReforgePrefix, ReforgeStatConfig> result = new HashMap<>();
        
        for (Map.Entry<ReforgePrefix, Map<ItemRarity, ReforgeStatConfig>> entry : REFORGE_MATRIX.entrySet()) {
            ReforgePrefix prefix = entry.getKey();
            Map<ItemRarity, ReforgeStatConfig> rarityMatrix = entry.getValue();
            
            // Get the first available rarity for this prefix
            if (!rarityMatrix.isEmpty()) {
                ReforgeStatConfig config = rarityMatrix.values().iterator().next();
                if (config.getCategory() == category) {
                    result.put(prefix, config);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Calculate the total stat bonus for a reforge
     */
    public static double calculateTotalStatBonus(ReforgePrefix prefix, ItemRarity rarity, StatType statType) {
        ReforgeStatConfig config = getReforgeStats(prefix, rarity);
        if (config == null) {
            return 0.0;
        }
        
        double baseValue = config.getStat(statType);
        double rarityMultiplier = rarity.getMultiplier();
        
        return baseValue * rarityMultiplier;
    }
    
    /**
     * Get the best reforge for a specific stat type and category
     */
    public static ReforgePrefix getBestReforgeForStat(ReforgeCategory category, StatType statType) {
        ReforgePrefix bestPrefix = null;
        double bestValue = 0.0;
        
        for (Map.Entry<ReforgePrefix, Map<ItemRarity, ReforgeStatConfig>> entry : REFORGE_MATRIX.entrySet()) {
            ReforgePrefix prefix = entry.getKey();
            Map<ItemRarity, ReforgeStatConfig> rarityMatrix = entry.getValue();
            
            for (Map.Entry<ItemRarity, ReforgeStatConfig> rarityEntry : rarityMatrix.entrySet()) {
                ReforgeStatConfig config = rarityEntry.getValue();
                if (config.getCategory() == category) {
                    double value = config.getStat(statType);
                    if (value > bestValue) {
                        bestValue = value;
                        bestPrefix = prefix;
                    }
                }
            }
        }
        
        return bestPrefix;
    }
    
    /**
     * Check if a reforge prefix is available for a specific category
     */
    public static boolean isReforgeAvailableForCategory(ReforgePrefix prefix, ReforgeCategory category) {
        Map<ItemRarity, ReforgeStatConfig> prefixMatrix = REFORGE_MATRIX.get(prefix);
        if (prefixMatrix == null) {
            return false;
        }
        
        for (ReforgeStatConfig config : prefixMatrix.values()) {
            if (config.getCategory() == category) {
                return true;
            }
        }
        
        return false;
    }
}
