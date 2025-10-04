package de.noctivag.skyblock.engine.collections.types;

import java.util.Arrays;

/**
 * Collection Reward Types
 * 
 * Defines the different types of rewards that can be unlocked
 * through collection milestones.
 */
public enum CollectionRewardType {
    SKYBLOCK_XP("SkyBlock XP", "Experience points for SkyBlock progression"),
    RECIPE("Recipe", "Crafting recipe unlock"),
    STAT_BONUS("Stat Bonus", "Permanent stat increase"),
    COIN_BONUS("Coin Bonus", "One-time coin reward"),
    ITEM_REWARD("Item Reward", "Specific item reward"),
    UNLOCK("Unlock", "Feature or area unlock"),
    PERMISSION("Permission", "Special permission or access"),
    TITLE("Title", "Player title or rank"),
    ACHIEVEMENT("Achievement", "Achievement unlock"),
    COSMETIC("Cosmetic", "Cosmetic item or effect");
    
    private final String displayName;
    private final String description;
    
    CollectionRewardType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get reward type by name (case insensitive)
     */
    public static CollectionRewardType getByName(String name) {
        if (name == null) return null;
        
        return Arrays.stream(values())
            .filter(type -> type.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Check if a reward type is a progression reward
     */
    public boolean isProgressionReward() {
        return switch (this) {
            case SKYBLOCK_XP, RECIPE, STAT_BONUS, UNLOCK, PERMISSION -> true;
            default -> false;
        };
    }
    
    /**
     * Check if a reward type is a cosmetic reward
     */
    public boolean isCosmeticReward() {
        return switch (this) {
            case TITLE, ACHIEVEMENT, COSMETIC -> true;
            default -> false;
        };
    }
    
    /**
     * Check if a reward type is a material reward
     */
    public boolean isMaterialReward() {
        return switch (this) {
            case COIN_BONUS, ITEM_REWARD -> true;
            default -> false;
        };
    }
    
    /**
     * Get reward priority (higher = more important)
     */
    public int getPriority() {
        return switch (this) {
            case RECIPE -> 100; // Highest priority
            case STAT_BONUS -> 90; // Very high priority
            case UNLOCK -> 80; // High priority
            case PERMISSION -> 70; // High priority
            case SKYBLOCK_XP -> 60; // Medium-high priority
            case ACHIEVEMENT -> 50; // Medium priority
            case TITLE -> 40; // Medium priority
            case ITEM_REWARD -> 30; // Medium-low priority
            case COIN_BONUS -> 20; // Low priority
            case COSMETIC -> 10; // Lowest priority
        };
    }
    
    /**
     * Get reward rarity
     */
    public RewardRarity getRarity() {
        return switch (this) {
            case RECIPE, STAT_BONUS -> RewardRarity.LEGENDARY;
            case UNLOCK, PERMISSION -> RewardRarity.EPIC;
            case SKYBLOCK_XP, ACHIEVEMENT -> RewardRarity.RARE;
            case TITLE, ITEM_REWARD -> RewardRarity.UNCOMMON;
            case COIN_BONUS, COSMETIC -> RewardRarity.COMMON;
        };
    }
    
    /**
     * Get reward color based on rarity
     */
    public String getRarityColor() {
        return switch (getRarity()) {
            case COMMON -> "§7"; // Gray
            case UNCOMMON -> "§a"; // Green
            case RARE -> "§9"; // Blue
            case EPIC -> "§5"; // Purple
            case LEGENDARY -> "§6"; // Gold
        };
    }
    
    /**
     * Get formatted reward type with rarity color
     */
    public String getFormattedType() {
        return getRarityColor() + displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Reward rarity levels
     */
    public enum RewardRarity {
        COMMON("Common"),
        UNCOMMON("Uncommon"),
        RARE("Rare"),
        EPIC("Epic"),
        LEGENDARY("Legendary");
        
        private final String displayName;
        
        RewardRarity(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
