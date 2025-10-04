package de.noctivag.skyblock.features.accessories.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 75+ accessory types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteAccessoryType {
    // ===== COMMON TALISMANS (15+) =====
    SPEED_TALISMAN("Speed Talisman", "🏃", "+1 Speed", AccessoryRarity.COMMON, 1),
    ZOMBIE_TALISMAN("Zombie Talisman", "🧟", "-5% Zombie Damage", AccessoryRarity.COMMON, 1),
    SKELETON_TALISMAN("Skeleton Talisman", "💀", "-5% Skeleton Damage", AccessoryRarity.COMMON, 1),
    SPIDER_TALISMAN("Spider Talisman", "🕷️", "-5% Spider Damage", AccessoryRarity.COMMON, 1),
    CREEPER_TALISMAN("Creeper Talisman", "💣", "-5% Creeper Damage", AccessoryRarity.COMMON, 1),
    ENDERMAN_TALISMAN("Enderman Talisman", "🌑", "-5% Enderman Damage", AccessoryRarity.COMMON, 1),
    BAT_TALISMAN("Bat Talisman", "🦇", "-5% Bat Damage", AccessoryRarity.COMMON, 1),
    FIRE_TALISMAN("Fire Talisman", "🔥", "-5% Fire Damage", AccessoryRarity.COMMON, 1),
    INTIMIDATION_TALISMAN("Intimidation Talisman", "😠", "Reduces mob aggro range", AccessoryRarity.COMMON, 1),
    CAMPFIRE_TALISMAN("Campfire Talisman", "🔥", "Regeneration near campfires", AccessoryRarity.COMMON, 1),
    VACCINE_TALISMAN("Vaccine Talisman", "💉", "Immunity to poison", AccessoryRarity.COMMON, 1),
    PIGS_FOOT("Pig's Foot", "🐷", "+1 Speed", AccessoryRarity.COMMON, 1),
    MAGNETIC_TALISMAN("Magnetic Talisman", "🧲", "Pulls items from 3 blocks", AccessoryRarity.COMMON, 1),
    HASTE_RING("Haste Ring", "⚡", "+5 Mining Speed", AccessoryRarity.COMMON, 1),
    POTION_AFFINITY_RING("Potion Affinity Ring", "🧪", "+10% Potion Duration", AccessoryRarity.COMMON, 1),
    
    // ===== UNCOMMON TALISMANS (20+) =====
    SPEED_RING("Speed Ring", "🏃", "+2 Speed", AccessoryRarity.UNCOMMON, 2),
    ZOMBIE_RING("Zombie Ring", "🧟", "-10% Zombie Damage", AccessoryRarity.UNCOMMON, 2),
    SKELETON_RING("Skeleton Ring", "💀", "-10% Skeleton Damage", AccessoryRarity.UNCOMMON, 2),
    SPIDER_RING("Spider Ring", "🕷️", "-10% Spider Damage", AccessoryRarity.UNCOMMON, 2),
    CREEPER_RING("Creeper Ring", "💣", "-10% Creeper Damage", AccessoryRarity.UNCOMMON, 2),
    ENDERMAN_RING("Enderman Ring", "🌑", "-10% Enderman Damage", AccessoryRarity.UNCOMMON, 2),
    BAT_RING("Bat Ring", "🦇", "-10% Bat Damage", AccessoryRarity.UNCOMMON, 2),
    FIRE_RING("Fire Ring", "🔥", "-10% Fire Damage", AccessoryRarity.UNCOMMON, 2),
    INTIMIDATION_RING("Intimidation Ring", "😠", "Further reduces mob aggro", AccessoryRarity.UNCOMMON, 2),
    CAMPFIRE_RING("Campfire Ring", "🔥", "Better regeneration", AccessoryRarity.UNCOMMON, 2),
    VACCINE_RING("Vaccine Ring", "💉", "Immunity to poison + wither", AccessoryRarity.UNCOMMON, 2),
    PIGS_FOOT_RING("Pig's Foot Ring", "🐷", "+2 Speed", AccessoryRarity.UNCOMMON, 2),
    MAGNETIC_RING("Magnetic Ring", "🧲", "Pulls items from 5 blocks", AccessoryRarity.UNCOMMON, 2),
    HASTE_RING_UNCOMMON("Haste Ring", "⚡", "+10 Mining Speed", AccessoryRarity.UNCOMMON, 2),
    POTION_AFFINITY_RING_UNCOMMON("Potion Affinity Ring", "🧪", "+20% Potion Duration", AccessoryRarity.UNCOMMON, 2),
    LAVA_TALISMAN("Lava Talisman", "🌋", "Immunity to lava", AccessoryRarity.UNCOMMON, 2),
    FISHING_TALISMAN("Fishing Talisman", "🎣", "+5% Sea Creature Chance", AccessoryRarity.UNCOMMON, 2),
    WOLF_TALISMAN("Wolf Talisman", "🐺", "+5% Combat XP", AccessoryRarity.UNCOMMON, 2),
    SEA_CREATURE_TALISMAN("Sea Creature Talisman", "🐟", "+3% Sea Creature Chance", AccessoryRarity.UNCOMMON, 2),
    NIGHT_VISION_CHARM("Night Vision Charm", "👁️", "Permanent night vision", AccessoryRarity.UNCOMMON, 2),
    
    // ===== RARE TALISMANS (25+) =====
    SPEED_ARTIFACT("Speed Artifact", "🏃", "+3 Speed", AccessoryRarity.RARE, 3),
    ZOMBIE_ARTIFACT("Zombie Artifact", "🧟", "-15% Zombie Damage", AccessoryRarity.RARE, 3),
    SKELETON_ARTIFACT("Skeleton Artifact", "💀", "-15% Skeleton Damage", AccessoryRarity.RARE, 3),
    SPIDER_ARTIFACT("Spider Artifact", "🕷️", "-15% Spider Damage", AccessoryRarity.RARE, 3),
    CREEPER_ARTIFACT("Creeper Artifact", "💣", "-15% Creeper Damage", AccessoryRarity.RARE, 3),
    ENDERMAN_ARTIFACT("Enderman Artifact", "🌑", "-15% Enderman Damage", AccessoryRarity.RARE, 3),
    BAT_ARTIFACT("Bat Artifact", "🦇", "-15% Bat Damage", AccessoryRarity.RARE, 3),
    FIRE_ARTIFACT("Fire Artifact", "🔥", "-15% Fire Damage", AccessoryRarity.RARE, 3),
    INTIMIDATION_ARTIFACT("Intimidation Artifact", "😠", "Maximum mob aggro reduction", AccessoryRarity.RARE, 3),
    CAMPFIRE_ARTIFACT("Campfire Artifact", "🔥", "Best regeneration", AccessoryRarity.RARE, 3),
    VACCINE_ARTIFACT("Vaccine Artifact", "💉", "Full immunity", AccessoryRarity.RARE, 3),
    PIGS_FOOT_ARTIFACT("Pig's Foot Artifact", "🐷", "+3 Speed", AccessoryRarity.RARE, 3),
    MAGNETIC_ARTIFACT("Magnetic Artifact", "🧲", "Pulls items from 7 blocks", AccessoryRarity.RARE, 3),
    HASTE_ARTIFACT("Haste Artifact", "⚡", "+15 Mining Speed", AccessoryRarity.RARE, 3),
    POTION_AFFINITY_ARTIFACT("Potion Affinity Artifact", "🧪", "+30% Potion Duration", AccessoryRarity.RARE, 3),
    LAVA_RING("Lava Ring", "🌋", "Immunity to lava + fire", AccessoryRarity.RARE, 3),
    FISHING_RING("Fishing Ring", "🎣", "+10% Sea Creature Chance", AccessoryRarity.RARE, 3),
    WOLF_RING("Wolf Ring", "🐺", "+10% Combat XP", AccessoryRarity.RARE, 3),
    SEA_CREATURE_RING("Sea Creature Ring", "🐟", "+6% Sea Creature Chance", AccessoryRarity.RARE, 3),
    NIGHT_VISION_CHARM_RARE("Night Vision Charm", "👁️", "Enhanced night vision", AccessoryRarity.RARE, 3),
    FARMING_TALISMAN("Farming Talisman", "🌾", "+5% Farming XP", AccessoryRarity.RARE, 3),
    MINING_TALISMAN("Mining Talisman", "⛏️", "+5% Mining XP", AccessoryRarity.RARE, 3),
    COMBAT_TALISMAN("Combat Talisman", "⚔️", "+5% Combat XP", AccessoryRarity.RARE, 3),
    FORAGING_TALISMAN("Foraging Talisman", "🪓", "+5% Foraging XP", AccessoryRarity.RARE, 3),
    FISHING_TALISMAN_RARE("Fishing Talisman", "🎣", "+5% Fishing XP", AccessoryRarity.RARE, 3),
    
    // ===== EPIC TALISMANS (15+) =====
    FARMING_RING("Farming Ring", "🌾", "+10% Farming XP", AccessoryRarity.EPIC, 4),
    MINING_RING("Mining Ring", "⛏️", "+10% Mining XP", AccessoryRarity.EPIC, 4),
    COMBAT_RING("Combat Ring", "⚔️", "+10% Combat XP", AccessoryRarity.EPIC, 4),
    FORAGING_RING("Foraging Ring", "🪓", "+10% Foraging XP", AccessoryRarity.EPIC, 4),
    FISHING_RING_EPIC("Fishing Ring", "🎣", "+10% Fishing XP", AccessoryRarity.EPIC, 4),
    HEALING_TALISMAN("Healing Talisman", "❤️", "+5% Health", AccessoryRarity.EPIC, 4),
    MANA_TALISMAN("Mana Talisman", "🔮", "+5% Intelligence", AccessoryRarity.EPIC, 4),
    CRIT_CHANCE_TALISMAN("Crit Chance Talisman", "⚡", "+2% Crit Chance", AccessoryRarity.EPIC, 4),
    CRIT_DAMAGE_TALISMAN("Crit Damage Talisman", "💥", "+10% Crit Damage", AccessoryRarity.EPIC, 4),
    STRENGTH_TALISMAN("Strength Talisman", "💪", "+5 Strength", AccessoryRarity.EPIC, 4),
    DEFENSE_TALISMAN("Defense Talisman", "🛡️", "+5 Defense", AccessoryRarity.EPIC, 4),
    HEALTH_TALISMAN("Health Talisman", "❤️", "+10 Health", AccessoryRarity.EPIC, 4),
    INTELLIGENCE_TALISMAN("Intelligence Talisman", "🧠", "+10 Intelligence", AccessoryRarity.EPIC, 4),
    SPEED_TALISMAN_EPIC("Speed Talisman", "🏃", "+5 Speed", AccessoryRarity.EPIC, 4),
    MAGIC_FIND_TALISMAN("Magic Find Talisman", "✨", "+5% Magic Find", AccessoryRarity.EPIC, 4),
    
    // ===== LEGENDARY TALISMANS (15+) =====
    FARMING_ARTIFACT("Farming Artifact", "🌾", "+15% Farming XP", AccessoryRarity.LEGENDARY, 5),
    MINING_ARTIFACT("Mining Artifact", "⛏️", "+15% Mining XP", AccessoryRarity.LEGENDARY, 5),
    COMBAT_ARTIFACT("Combat Artifact", "⚔️", "+15% Combat XP", AccessoryRarity.LEGENDARY, 5),
    FORAGING_ARTIFACT("Foraging Artifact", "🪓", "+15% Foraging XP", AccessoryRarity.LEGENDARY, 5),
    FISHING_ARTIFACT("Fishing Artifact", "🎣", "+15% Fishing XP", AccessoryRarity.LEGENDARY, 5),
    HEALING_RING("Healing Ring", "❤️", "+10% Health", AccessoryRarity.LEGENDARY, 5),
    MANA_RING("Mana Ring", "🔮", "+10% Intelligence", AccessoryRarity.LEGENDARY, 5),
    CRIT_CHANCE_RING("Crit Chance Ring", "⚡", "+3% Crit Chance", AccessoryRarity.LEGENDARY, 5),
    CRIT_DAMAGE_RING("Crit Damage Ring", "💥", "+15% Crit Damage", AccessoryRarity.LEGENDARY, 5),
    STRENGTH_RING("Strength Ring", "💪", "+10 Strength", AccessoryRarity.LEGENDARY, 5),
    DEFENSE_RING("Defense Ring", "🛡️", "+10 Defense", AccessoryRarity.LEGENDARY, 5),
    HEALTH_RING("Health Ring", "❤️", "+20 Health", AccessoryRarity.LEGENDARY, 5),
    INTELLIGENCE_RING("Intelligence Ring", "🧠", "+20 Intelligence", AccessoryRarity.LEGENDARY, 5),
    SPEED_ARTIFACT_LEGENDARY("Speed Artifact", "🏃", "+7 Speed", AccessoryRarity.LEGENDARY, 5),
    MAGIC_FIND_RING("Magic Find Ring", "✨", "+10% Magic Find", AccessoryRarity.LEGENDARY, 5),
    
    // ===== MYTHIC TALISMANS (10+) =====
    HEALING_ARTIFACT("Healing Artifact", "❤️", "+15% Health", AccessoryRarity.MYTHIC, 6),
    MANA_ARTIFACT("Mana Artifact", "🔮", "+15% Intelligence", AccessoryRarity.MYTHIC, 6),
    CRIT_CHANCE_ARTIFACT("Crit Chance Artifact", "⚡", "+5% Crit Chance", AccessoryRarity.MYTHIC, 6),
    CRIT_DAMAGE_ARTIFACT("Crit Damage Artifact", "💥", "+20% Crit Damage", AccessoryRarity.MYTHIC, 6),
    STRENGTH_ARTIFACT("Strength Artifact", "💪", "+15 Strength", AccessoryRarity.MYTHIC, 6),
    DEFENSE_ARTIFACT("Defense Artifact", "🛡️", "+15 Defense", AccessoryRarity.MYTHIC, 6),
    HEALTH_ARTIFACT("Health Artifact", "❤️", "+30 Health", AccessoryRarity.MYTHIC, 6),
    INTELLIGENCE_ARTIFACT("Intelligence Artifact", "🧠", "+30 Intelligence", AccessoryRarity.MYTHIC, 6),
    SPEED_ARTIFACT_MYTHIC("Speed Artifact", "🏃", "+10 Speed", AccessoryRarity.MYTHIC, 6),
    MAGIC_FIND_ARTIFACT("Magic Find Artifact", "✨", "+15% Magic Find", AccessoryRarity.MYTHIC, 6),
    
    // ===== SPECIAL ACCESSORIES (10+) =====
    POWER_STONE("Power Stone", "💎", "Magical Power Enhancement", AccessoryRarity.SPECIAL, 10),
    ACCESSORY_BAG("Accessory Bag", "🎒", "Storage for accessories", AccessoryRarity.SPECIAL, 5),
    PERSONAL_COMPACTOR("Personal Compactor", "📦", "Auto-compacts items", AccessoryRarity.SPECIAL, 8),
    QUIVER("Quiver", "🏹", "Arrow storage", AccessoryRarity.SPECIAL, 3),
    FISHING_BAG("Fishing Bag", "🎣", "Fishing item storage", AccessoryRarity.SPECIAL, 4),
    POTION_BAG("Potion Bag", "🧪", "Potion storage", AccessoryRarity.SPECIAL, 4),
    SACK_OF_SACKS("Sack of Sacks", "👜", "Storage upgrade", AccessoryRarity.SPECIAL, 6),
    WARDROBE("Wardrobe", "👔", "Armor set storage", AccessoryRarity.SPECIAL, 5),
    ENDER_CHEST("Ender Chest", "🌑", "Personal storage", AccessoryRarity.SPECIAL, 7),
    PERSONAL_VAULT("Personal Vault", "🏦", "Secure storage", AccessoryRarity.SPECIAL, 9);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final AccessoryRarity rarity;
    private final int magicalPowerValue;
    
    CompleteAccessoryType(String displayName, String icon, String description, AccessoryRarity rarity, int magicalPowerValue) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.rarity = rarity;
        this.magicalPowerValue = magicalPowerValue;
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
    
    public AccessoryRarity getRarity() {
        return rarity;
    }
    
    public int getMagicalPowerValue() {
        return magicalPowerValue;
    }
    
    /**
     * Get accessory category
     */
    public AccessoryCategory getCategory() {
        if (name().contains("TALISMAN") && !name().contains("RING") && !name().contains("ARTIFACT")) {
            return AccessoryCategory.COMMON_TALISMANS;
        }
        if (name().contains("RING") && !name().contains("ARTIFACT")) {
            return AccessoryCategory.UNCOMMON_TALISMANS;
        }
        if (name().contains("ARTIFACT")) {
            return AccessoryCategory.RARE_TALISMANS;
        }
        return AccessoryCategory.SPECIAL_ACCESSORIES;
    }
    
    /**
     * Get all accessories by category
     */
    public static List<CompleteAccessoryType> getAccessoriesByCategory(AccessoryCategory category) {
        return Arrays.stream(values())
            .filter(accessory -> accessory.getCategory() == category)
            .toList();
    }
    
    /**
     * Get all accessories by rarity
     */
    public static List<CompleteAccessoryType> getAccessoriesByRarity(AccessoryRarity rarity) {
        return Arrays.stream(values())
            .filter(accessory -> accessory.getRarity() == rarity)
            .toList();
    }
    
    /**
     * Get total accessory count
     */
    public static int getTotalAccessoryCount() {
        return values().length;
    }
    
    /**
     * Get accessories by category name
     */
    public static List<CompleteAccessoryType> getAccessoriesByCategoryName(String categoryName) {
        try {
            AccessoryCategory category = AccessoryCategory.valueOf(categoryName.toUpperCase());
            return getAccessoriesByCategory(category);
        } catch (IllegalArgumentException e) {
            return Arrays.asList(values());
        }
    }
    
    /**
     * Get accessories by rarity name
     */
    public static List<CompleteAccessoryType> getAccessoriesByRarityName(String rarityName) {
        try {
            AccessoryRarity rarity = AccessoryRarity.valueOf(rarityName.toUpperCase());
            return getAccessoriesByRarity(rarity);
        } catch (IllegalArgumentException e) {
            return Arrays.asList(values());
        }
    }
    
    /**
     * Get magical power calculation
     */
    public static int calculateTotalMagicalPower(List<CompleteAccessoryType> accessories) {
        return accessories.stream()
            .mapToInt(CompleteAccessoryType::getMagicalPowerValue)
            .sum();
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
