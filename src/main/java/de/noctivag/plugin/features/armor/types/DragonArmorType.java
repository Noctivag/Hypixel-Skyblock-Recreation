package de.noctivag.plugin.features.armor.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All dragon armor types in Hypixel Skyblock
 */
public enum DragonArmorType {
    PROTECTOR("Protector Dragon", "🛡️", "High defense and health boost", ArmorRarity.LEGENDARY),
    WISE("Wise Dragon", "🧙", "High intelligence and mana boost", ArmorRarity.LEGENDARY),
    UNSTABLE("Unstable Dragon", "⚡", "High critical hit chance and damage", ArmorRarity.LEGENDARY),
    STRONG("Strong Dragon", "💪", "High strength and damage boost", ArmorRarity.LEGENDARY),
    YOUNG("Young Dragon", "🏃", "High speed and movement boost", ArmorRarity.LEGENDARY),
    OLD("Old Dragon", "👴", "Balanced stats with health boost", ArmorRarity.LEGENDARY),
    SUPERIOR("Superior Dragon", "👑", "Best overall stats and bonuses", ArmorRarity.LEGENDARY);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final ArmorRarity rarity;
    
    DragonArmorType(String displayName, String icon, String description, ArmorRarity rarity) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.rarity = rarity;
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
    
    public ArmorRarity getRarity() {
        return rarity;
    }
    
    /**
     * Get armor set bonus description
     */
    public String getSetBonus() {
        return switch (this) {
            case PROTECTOR -> "Reduces damage taken by 1% per piece (max 4%)";
            case WISE -> "Reduces mana cost by 50% and increases intelligence by 125";
            case UNSTABLE -> "Increases critical hit chance by 10% and critical damage by 50%";
            case STRONG -> "Increases strength by 100 and damage by 25%";
            case YOUNG -> "Increases speed by 100 and reduces fall damage";
            case OLD -> "Increases health by 150 and reduces damage taken by 10%";
            case SUPERIOR -> "Increases all stats by 5% and provides 10% more coins";
        };
    }
    
    /**
     * Get base stats for this armor type
     */
    public ArmorBaseStats getBaseStats() {
        return switch (this) {
            case PROTECTOR -> new ArmorBaseStats(100, 50, 0, 0, 0);
            case WISE -> new ArmorBaseStats(50, 25, 0, 100, 0);
            case UNSTABLE -> new ArmorBaseStats(75, 30, 50, 0, 0);
            case STRONG -> new ArmorBaseStats(80, 40, 75, 0, 0);
            case YOUNG -> new ArmorBaseStats(60, 35, 0, 0, 50);
            case OLD -> new ArmorBaseStats(120, 60, 25, 25, 10);
            case SUPERIOR -> new ArmorBaseStats(100, 50, 50, 50, 25);
        };
    }
    
    /**
     * Get required dragon fragments to craft
     */
    public int getRequiredFragments() {
        return switch (this) {
            case PROTECTOR, WISE, UNSTABLE, STRONG, YOUNG, OLD -> 240; // 60 per piece
            case SUPERIOR -> 400; // 100 per piece (more expensive)
        };
    }
    
    /**
     * Get crafting cost in coins
     */
    public long getCraftingCost() {
        return switch (this) {
            case PROTECTOR, WISE, UNSTABLE, STRONG, YOUNG, OLD -> 1000000L; // 1M coins
            case SUPERIOR -> 5000000L; // 5M coins (more expensive)
        };
    }
    
    /**
     * Get armor tier (for sorting)
     */
    public int getTier() {
        return switch (this) {
            case PROTECTOR, WISE, UNSTABLE, STRONG, YOUNG, OLD -> 1; // Basic dragon armors
            case SUPERIOR -> 2; // Superior is tier 2
        };
    }
    
    /**
     * Get armor by tier
     */
    public static List<DragonArmorType> getByTier(int tier) {
        return Arrays.stream(values())
            .filter(armor -> armor.getTier() == tier)
            .toList();
    }
    
    /**
     * Get basic dragon armors
     */
    public static List<DragonArmorType> getBasicArmors() {
        return getByTier(1);
    }
    
    /**
     * Get superior dragon armor
     */
    public static DragonArmorType getSuperiorArmor() {
        return SUPERIOR;
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
    
    /**
     * Armor base stats
     */
    public static class ArmorBaseStats {
        private final int health;
        private final int defense;
        private final int strength;
        private final int intelligence;
        private final int speed;
        
        public ArmorBaseStats(int health, int defense, int strength, int intelligence, int speed) {
            this.health = health;
            this.defense = defense;
            this.strength = strength;
            this.intelligence = intelligence;
            this.speed = speed;
        }
        
        public int getHealth() {
            return health;
        }
        
        public int getDefense() {
            return defense;
        }
        
        public int getStrength() {
            return strength;
        }
        
        public int getIntelligence() {
            return intelligence;
        }
        
        public int getSpeed() {
            return speed;
        }
    }
}
