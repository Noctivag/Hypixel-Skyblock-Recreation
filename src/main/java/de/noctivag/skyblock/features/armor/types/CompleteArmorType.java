package de.noctivag.skyblock.features.armor.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 49+ armor types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteArmorType {
    // Standard Armor
    FARM_SUIT("Farm Suit", "🌾", "+100 Defense, +20% Speed when farming"),
    MUSHROOM_ARMOR("Mushroom Armor", "🍄", "+55 Health, +15 Defense, 3x values at night, Night Vision"),
    ANGLER_ARMOR("Angler Armor", "🎣", "+100 Defense, -30% Damage from sea creatures, +4% Spawn Chance"),
    PUMPKIN_ARMOR("Pumpkin Armor", "🎃", "-10% Damage taken, +10% Damage dealt"),
    CACTUS_ARMOR("Cactus Armor", "🌵", "Reflects 33% of damage back"),
    LAPIS_ARMOR("Lapis Armor", "💎", "Increases XP when mining ore"),
    HARDENED_DIAMOND("Hardened Diamond", "💎", "High protection, easy to craft"),
    ENDER_ARMOR("Ender Armor", "🌑", "Double stats in The End"),
    SKELETON_SOLDIER_ARMOR("Skeleton Soldier Armor", "💀", "Increased arrow damage"),
    
    // High-Quality Armor
    PERFECT_ARMOR("Perfect Armor", "💎", "Can be improved in multiple tiers"),
    NECRON_ARMOR("Necron Armor", "💀", "Dungeon armor, high damage"),
    STORM_ARMOR("Storm Armor", "⛈️", "Dungeon armor, magic-based"),
    WITHER_ARMOR("Wither Armor", "💀", "Dungeon armor, tank-oriented"),
    MASTIFF_ARMOR("Mastiff Armor", "🐕", "High health, reduced defense"),
    MAGMA_LORD_ARMOR("Magma Lord Armor", "🌋", "Fire-resistant, enhanced by Flying Fish Pet"),
    
    // Dragon Armor Variants
    SUPERIOR_DRAGON_ARMOR("Superior Dragon Armor", "🐉", "Best Dragon Armor, all stats increased"),
    STRONG_DRAGON_ARMOR("Strong Dragon Armor", "💪", "Increases Strength and Crit Damage"),
    WISE_DRAGON_ARMOR("Wise Dragon Armor", "🧠", "Increases Intelligence and Magic"),
    UNSTABLE_DRAGON_ARMOR("Unstable Dragon Armor", "⚡", "Random lightning, high damage"),
    YOUNG_DRAGON_ARMOR("Young Dragon Armor", "🏃", "Increases Speed"),
    OLD_DRAGON_ARMOR("Old Dragon Armor", "👴", "Increases Defense"),
    PROTECTOR_DRAGON_ARMOR("Protector Dragon Armor", "🛡️", "Highest Defense"),
    HOLY_DRAGON_ARMOR("Holy Dragon Armor", "✨", "Healing and Protection"),
    
    // Dungeon Armor Variants
    NECRONS_HELMET("Necron's Helmet", "💀", "High damage"),
    NECRONS_CHESTPLATE("Necron's Chestplate", "💀", "High damage"),
    NECRONS_LEGGINGS("Necron's Leggings", "💀", "High damage"),
    NECRONS_BOOTS("Necron's Boots", "💀", "High damage"),
    STORMS_HELMET("Storm's Helmet", "⛈️", "Magic-based"),
    STORMS_CHESTPLATE("Storm's Chestplate", "⛈️", "Magic-based"),
    STORMS_LEGGINGS("Storm's Leggings", "⛈️", "Magic-based"),
    STORMS_BOOTS("Storm's Boots", "⛈️", "Magic-based"),
    
    // Additional Special Armor
    GOBLIN_ARMOR("Goblin Armor", "👹", "Mining focus"),
    SPONGE_ARMOR("Sponge Armor", "🧽", "Water resistance"),
    SLIME_ARMOR("Slime Armor", "🟢", "Bouncy effects"),
    CRYSTAL_ARMOR("Crystal Armor", "💎", "Crystal-based"),
    TARANTULA_ARMOR("Tarantula Armor", "🕷️", "Spider-Boss armor"),
    REVENANT_ARMOR("Revenant Armor", "🧟", "Zombie-Boss armor"),
    SKELETON_MASTER_ARMOR("Skeleton Master Armor", "💀", "Skeleton-Boss armor"),
    FAIRY_ARMOR("Fairy Armor", "🧚", "Fairy Soul bonus"),
    SPEEDSTER_ARMOR("Speedster Armor", "🏃", "Speed bonus"),
    
    // Perfect Armor Tiers (T1-T12)
    PERFECT_T1_ARMOR("Perfect Armor T1", "💎", "Tier 1 Perfect Armor"),
    PERFECT_T2_ARMOR("Perfect Armor T2", "💎", "Tier 2 Perfect Armor"),
    PERFECT_T3_ARMOR("Perfect Armor T3", "💎", "Tier 3 Perfect Armor"),
    PERFECT_T4_ARMOR("Perfect Armor T4", "💎", "Tier 4 Perfect Armor"),
    PERFECT_T5_ARMOR("Perfect Armor T5", "💎", "Tier 5 Perfect Armor"),
    PERFECT_T6_ARMOR("Perfect Armor T6", "💎", "Tier 6 Perfect Armor"),
    PERFECT_T7_ARMOR("Perfect Armor T7", "💎", "Tier 7 Perfect Armor"),
    PERFECT_T8_ARMOR("Perfect Armor T8", "💎", "Tier 8 Perfect Armor"),
    PERFECT_T9_ARMOR("Perfect Armor T9", "💎", "Tier 9 Perfect Armor"),
    PERFECT_T10_ARMOR("Perfect Armor T10", "💎", "Tier 10 Perfect Armor"),
    PERFECT_T11_ARMOR("Perfect Armor T11", "💎", "Tier 11 Perfect Armor"),
    PERFECT_T12_ARMOR("Perfect Armor T12", "💎", "Tier 12 Perfect Armor"),
    
    // Additional Dungeon Armor
    GOLDOR_ARMOR("Goldor Armor", "💀", "Tank-oriented dungeon armor"),
    MAXOR_ARMOR("Maxor Armor", "🏹", "Archer-oriented dungeon armor"),
    SHADOW_ASSASSIN_ARMOR("Shadow Assassin Armor", "🗡️", "Assassin-oriented dungeon armor"),
    ADAPTIVE_ARMOR("Adaptive Armor", "🔄", "Adaptive dungeon armor"),
    
    // Tuxedo Armor
    CHEAP_TUXEDO("Cheap Tuxedo", "👔", "Health reduced to 75, +50% damage"),
    FANCY_TUXEDO("Fancy Tuxedo", "👔", "Health reduced to 100, +75% damage"),
    ELEGANT_TUXEDO("Elegant Tuxedo", "👔", "Health reduced to 150, +100% damage"),
    
    // Additional Slayer Armor
    VOIDGLOOM_ARMOR("Voidgloom Armor", "🌑", "Enderman Slayer armor"),
    
    // Additional Specialized Armor
    BLAZE_ARMOR("Blaze Armor", "🔥", "Blazing Aura, fire/lava immunity"),
    ZOMBIE_ARMOR("Zombie Armor", "🧟", "Projectile absorption, no helmet"),
    
    // Dungeon Specialized Armor Sets
    SKELETON_GRUNT_ARMOR("Skeleton Grunt Armor", "💀", "Skeleton Grunt armor"),
    ZOMBIE_SOLDIER_ARMOR("Zombie Soldier Armor", "🧟", "Zombie Soldier armor"),
    ZOMBIE_KNIGHT_ARMOR("Zombie Knight Armor", "🧟", "Zombie Knight armor"),
    ZOMBIE_COMMANDER_ARMOR("Zombie Commander Armor", "🧟", "Zombie Commander armor"),
    ZOMBIE_LORD_ARMOR("Zombie Lord Armor", "🧟", "Zombie Lord armor"),
    SPIRIT_ARMOR("Spirit Armor", "👻", "Spirit armor"),
    NECROMANCER_LORD_ARMOR("Necromancer Lord Armor", "💀", "Necromancer Lord armor");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    CompleteArmorType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
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
    
    /**
     * Get armor by category
     */
    public static List<CompleteArmorType> getStandardArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("FARM") || 
                             armor.name().contains("MUSHROOM") ||
                             armor.name().contains("ANGLER") ||
                             armor.name().contains("PUMPKIN") ||
                             armor.name().contains("CACTUS") ||
                             armor.name().contains("LAPIS") ||
                             armor.name().contains("HARDENED") ||
                             armor.name().contains("ENDER") ||
                             armor.name().contains("SKELETON_SOLDIER"))
            .toList();
    }
    
    /**
     * Get high-quality armor
     */
    public static List<CompleteArmorType> getHighQualityArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("PERFECT") || 
                             armor.name().contains("NECRON") ||
                             armor.name().contains("STORM") ||
                             armor.name().contains("WITHER") ||
                             armor.name().contains("MASTIFF") ||
                             armor.name().contains("MAGMA"))
            .toList();
    }
    
    /**
     * Get dragon armor
     */
    public static List<CompleteArmorType> getDragonArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("DRAGON"))
            .toList();
    }
    
    /**
     * Get dungeon armor
     */
    public static List<CompleteArmorType> getDungeonArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("NECRONS") || 
                             armor.name().contains("STORMS"))
            .toList();
    }
    
    /**
     * Get special armor
     */
    public static List<CompleteArmorType> getSpecialArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("GOBLIN") || 
                             armor.name().contains("SPONGE") ||
                             armor.name().contains("SLIME") ||
                             armor.name().contains("CRYSTAL") ||
                             armor.name().contains("TARANTULA") ||
                             armor.name().contains("REVENANT") ||
                             armor.name().contains("SKELETON_MASTER") ||
                             armor.name().contains("FAIRY") ||
                             armor.name().contains("SPEEDSTER") ||
                             armor.name().contains("BLAZE") ||
                             armor.name().contains("ZOMBIE") ||
                             armor.name().contains("SPIRIT") ||
                             armor.name().contains("NECROMANCER"))
            .toList();
    }
    
    /**
     * Get perfect armor tiers
     */
    public static List<CompleteArmorType> getPerfectArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("PERFECT"))
            .toList();
    }
    
    /**
     * Get tuxedo armor
     */
    public static List<CompleteArmorType> getTuxedoArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("TUXEDO"))
            .toList();
    }
    
    /**
     * Get slayer armor
     */
    public static List<CompleteArmorType> getSlayerArmor() {
        return Arrays.stream(values())
            .filter(armor -> armor.name().contains("TARANTULA") ||
                             armor.name().contains("REVENANT") ||
                             armor.name().contains("VOIDGLOOM"))
            .toList();
    }
    
    /**
     * Get all armor by category
     */
    public String getCategory() {
        if (name().contains("DRAGON")) return "Dragon Armor";
        if (name().contains("NECRON") || name().contains("STORM") || 
            name().contains("GOLDOR") || name().contains("MAXOR")) return "Dungeon Armor";
        if (name().contains("PERFECT")) return "Perfect Armor";
        if (name().contains("TUXEDO")) return "Tuxedo Armor";
        if (name().contains("TARANTULA") || name().contains("REVENANT") || 
            name().contains("VOIDGLOOM")) return "Slayer Armor";
        if (name().contains("FARM") || name().contains("MUSHROOM") || 
            name().contains("ANGLER") || name().contains("PUMPKIN") || 
            name().contains("CACTUS")) return "Utility Armor";
        if (name().contains("LAPIS") || name().contains("HARDENED") || 
            name().contains("GOBLIN")) return "Mining Armor";
        return "Special Armor";
    }
    
    /**
     * Get armor rarity
     */
    public String getRarity() {
        if (name().contains("SUPERIOR")) return "Legendary";
        if (name().contains("PERFECT_T1") || name().contains("PERFECT_T2") || 
            name().contains("PERFECT_T3")) return "Rare";
        if (name().contains("PERFECT_T4") || name().contains("PERFECT_T5") || 
            name().contains("PERFECT_T6")) return "Epic";
        if (name().contains("PERFECT_T7") || name().contains("PERFECT_T8") || 
            name().contains("PERFECT_T9")) return "Legendary";
        if (name().contains("PERFECT_T10") || name().contains("PERFECT_T11") || 
            name().contains("PERFECT_T12")) return "Mythic";
        if (name().contains("FARM") || name().contains("MUSHROOM") || 
            name().contains("ANGLER") || name().contains("PUMPKIN") || 
            name().contains("CACTUS")) return "Common";
        if (name().contains("LAPIS") || name().contains("HARDENED") || 
            name().contains("SKELETON_SOLDIER")) return "Rare";
        return "Epic";
    }
    
    /**
     * Get armor slot
     */
    public String getSlot() {
        if (name().contains("HELMET")) return "HELMET";
        if (name().contains("CHESTPLATE")) return "CHESTPLATE";
        if (name().contains("LEGGINGS")) return "LEGGINGS";
        if (name().contains("BOOTS")) return "BOOTS";
        return "FULL_SET";
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
