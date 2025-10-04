package de.noctivag.skyblock.features.stats.types;
import org.bukkit.inventory.ItemStack;

/**
 * All secondary stats from Hypixel Skyblock
 */
public enum SecondaryStat {
    // Mining Stats
    MINING_SPEED("Mining Speed", "⛏️", "Abbaugeschwindigkeit"),
    MINING_FORTUNE("Mining Fortune", "💎", "Mehrfache Drops"),
    MINING_WISDOM("Mining Wisdom", "📚", "Mining-XP-Bonus"),
    
    // Farming Stats
    FARMING_FORTUNE("Farming Fortune", "🌾", "Mehrfache Ernten"),
    FARMING_WISDOM("Farming Wisdom", "📚", "Farming-XP-Bonus"),
    
    // Fishing Stats
    SEA_CREATURE_CHANCE("Sea Creature Chance", "🐙", "Meereskreatur-Spawn-Chance"),
    FISHING_WISDOM("Fishing Wisdom", "📚", "Fishing-XP-Bonus"),
    
    // Combat Stats
    MAGIC_FIND("Magic Find", "✨", "Seltene Drop-Chance"),
    TRUE_DEFENSE("True Defense", "🛡️", "Unumgehbare Verteidigung"),
    ABILITY_DAMAGE("Ability Damage", "🔮", "Fähigkeits-Schaden"),
    TRUE_DAMAGE("True Damage", "💀", "Unumgehbarer Schaden"),
    COMBAT_WISDOM("Combat Wisdom", "📚", "Combat-XP-Bonus"),
    
    // Pet Stats
    PET_LUCK("Pet Luck", "🐾", "Pet-Drop-Chance"),
    TAMING_WISDOM("Taming Wisdom", "📚", "Taming-XP-Bonus"),
    
    // Foraging Stats
    FORAGING_WISDOM("Foraging Wisdom", "📚", "Foraging-XP-Bonus"),
    
    // Magic Stats
    ENCHANTING_WISDOM("Enchanting Wisdom", "📚", "Enchanting-XP-Bonus"),
    ALCHEMY_WISDOM("Alchemy Wisdom", "📚", "Alchemy-XP-Bonus"),
    RUNECRAFTING_WISDOM("Runecrafting Wisdom", "📚", "Runecrafting-XP-Bonus"),
    
    // Dungeon Stats
    DUNGEONEERING_WISDOM("Dungeoneering Wisdom", "📚", "Dungeoneering-XP-Bonus"),
    
    // Social Stats
    SOCIAL_WISDOM("Social Wisdom", "📚", "Social-XP-Bonus"),
    
    // Crafting Stats
    CARPENTRY_WISDOM("Carpentry Wisdom", "📚", "Carpentry-XP-Bonus");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    SecondaryStat(String displayName, String icon, String description) {
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
     * Get stat color code
     */
    public String getColorCode() {
        return switch (this) {
            case MINING_SPEED, MINING_FORTUNE, MINING_WISDOM -> "§7"; // Gray
            case FARMING_FORTUNE, FARMING_WISDOM -> "§a"; // Green
            case SEA_CREATURE_CHANCE, FISHING_WISDOM -> "§b"; // Aqua
            case MAGIC_FIND, TRUE_DEFENSE, ABILITY_DAMAGE, TRUE_DAMAGE, COMBAT_WISDOM -> "§c"; // Red
            case PET_LUCK, TAMING_WISDOM -> "§d"; // Light Purple
            case FORAGING_WISDOM -> "§2"; // Dark Green
            case ENCHANTING_WISDOM, ALCHEMY_WISDOM, RUNECRAFTING_WISDOM -> "§5"; // Dark Purple
            case DUNGEONEERING_WISDOM -> "§8"; // Dark Gray
            case SOCIAL_WISDOM -> "§e"; // Yellow
            case CARPENTRY_WISDOM -> "§6"; // Gold
        };
    }
    
    /**
     * Get stat category
     */
    public StatCategory getCategory() {
        return switch (this) {
            case MINING_SPEED, MINING_FORTUNE, MINING_WISDOM -> StatCategory.MINING;
            case FARMING_FORTUNE, FARMING_WISDOM -> StatCategory.FARMING;
            case SEA_CREATURE_CHANCE, FISHING_WISDOM -> StatCategory.FISHING;
            case MAGIC_FIND, TRUE_DEFENSE, ABILITY_DAMAGE, TRUE_DAMAGE, COMBAT_WISDOM -> StatCategory.COMBAT;
            case PET_LUCK, TAMING_WISDOM -> StatCategory.PETS;
            case FORAGING_WISDOM -> StatCategory.FORAGING;
            case ENCHANTING_WISDOM, ALCHEMY_WISDOM, RUNECRAFTING_WISDOM -> StatCategory.MAGIC;
            case DUNGEONEERING_WISDOM -> StatCategory.DUNGEONS;
            case SOCIAL_WISDOM -> StatCategory.SOCIAL;
            case CARPENTRY_WISDOM -> StatCategory.CRAFTING;
        };
    }
    
    /**
     * Get base value
     */
    public double getBaseValue() {
        return switch (this) {
            case MINING_SPEED, MINING_FORTUNE, MINING_WISDOM -> 0.0;
            case FARMING_FORTUNE, FARMING_WISDOM -> 0.0;
            case SEA_CREATURE_CHANCE, FISHING_WISDOM -> 0.0;
            case MAGIC_FIND, TRUE_DEFENSE, ABILITY_DAMAGE, TRUE_DAMAGE, COMBAT_WISDOM -> 0.0;
            case PET_LUCK, TAMING_WISDOM -> 0.0;
            case FORAGING_WISDOM -> 0.0;
            case ENCHANTING_WISDOM, ALCHEMY_WISDOM, RUNECRAFTING_WISDOM -> 0.0;
            case DUNGEONEERING_WISDOM -> 0.0;
            case SOCIAL_WISDOM -> 0.0;
            case CARPENTRY_WISDOM -> 0.0;
        };
    }
    
    /**
     * Get max value
     */
    public double getMaxValue() {
        return switch (this) {
            case MINING_SPEED -> 10000.0;
            case MINING_FORTUNE -> 1000.0;
            case MINING_WISDOM -> 500.0;
            case FARMING_FORTUNE -> 1000.0;
            case FARMING_WISDOM -> 500.0;
            case SEA_CREATURE_CHANCE -> 100.0;
            case FISHING_WISDOM -> 500.0;
            case MAGIC_FIND -> 1000.0;
            case TRUE_DEFENSE -> 1000.0;
            case ABILITY_DAMAGE -> 500.0;
            case TRUE_DAMAGE -> 1000.0;
            case COMBAT_WISDOM -> 500.0;
            case PET_LUCK -> 100.0;
            case TAMING_WISDOM -> 500.0;
            case FORAGING_WISDOM -> 500.0;
            case ENCHANTING_WISDOM, ALCHEMY_WISDOM, RUNECRAFTING_WISDOM -> 500.0;
            case DUNGEONEERING_WISDOM -> 500.0;
            case SOCIAL_WISDOM -> 500.0;
            case CARPENTRY_WISDOM -> 500.0;
        };
    }
    
    /**
     * Get stat calculation formula
     */
    public String getCalculationFormula() {
        return switch (this) {
            case MINING_SPEED, MINING_FORTUNE, MINING_WISDOM -> "Equipment + Skills + Pets";
            case FARMING_FORTUNE, FARMING_WISDOM -> "Equipment + Skills + Pets";
            case SEA_CREATURE_CHANCE, FISHING_WISDOM -> "Equipment + Skills + Pets";
            case MAGIC_FIND, TRUE_DEFENSE, ABILITY_DAMAGE, TRUE_DAMAGE, COMBAT_WISDOM -> "Equipment + Skills + Pets";
            case PET_LUCK, TAMING_WISDOM -> "Equipment + Skills + Pets";
            case FORAGING_WISDOM -> "Equipment + Skills + Pets";
            case ENCHANTING_WISDOM, ALCHEMY_WISDOM, RUNECRAFTING_WISDOM -> "Equipment + Skills + Pets";
            case DUNGEONEERING_WISDOM -> "Equipment + Skills + Pets";
            case SOCIAL_WISDOM -> "Equipment + Skills + Pets";
            case CARPENTRY_WISDOM -> "Equipment + Skills + Pets";
        };
    }
    
    /**
     * Get stat description
     */
    public String getStatDescription() {
        return switch (this) {
            case MINING_SPEED -> "Increases mining efficiency";
            case MINING_FORTUNE -> "Chance for multiple mining drops";
            case MINING_WISDOM -> "Increases mining experience gain";
            case FARMING_FORTUNE -> "Chance for multiple farming drops";
            case FARMING_WISDOM -> "Increases farming experience gain";
            case SEA_CREATURE_CHANCE -> "Chance to catch sea creatures";
            case FISHING_WISDOM -> "Increases fishing experience gain";
            case MAGIC_FIND -> "Chance for rare drops";
            case TRUE_DEFENSE -> "Defense that cannot be ignored";
            case ABILITY_DAMAGE -> "Increases ability damage";
            case TRUE_DAMAGE -> "Damage that ignores defense";
            case COMBAT_WISDOM -> "Increases combat experience gain";
            case PET_LUCK -> "Chance to obtain pets";
            case TAMING_WISDOM -> "Increases taming experience gain";
            case FORAGING_WISDOM -> "Increases foraging experience gain";
            case ENCHANTING_WISDOM -> "Increases enchanting experience gain";
            case ALCHEMY_WISDOM -> "Increases alchemy experience gain";
            case RUNECRAFTING_WISDOM -> "Increases runecrafting experience gain";
            case DUNGEONEERING_WISDOM -> "Increases dungeoneering experience gain";
            case SOCIAL_WISDOM -> "Increases social experience gain";
            case CARPENTRY_WISDOM -> "Increases carpentry experience gain";
        };
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
