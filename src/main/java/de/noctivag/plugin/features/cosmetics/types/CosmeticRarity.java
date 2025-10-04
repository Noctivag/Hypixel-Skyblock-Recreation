package de.noctivag.plugin.features.cosmetics.types;
import org.bukkit.inventory.ItemStack;

/**
 * Rarity levels for cosmetics
 */
public enum CosmeticRarity {
    COMMON("Common", "⚪", 1.0),
    RARE("Rare", "🔵", 2.0),
    EPIC("Epic", "🟣", 3.0),
    LEGENDARY("Legendary", "🟡", 5.0),
    MYTHIC("Mythic", "🔴", 10.0);
    
    private final String displayName;
    private final String icon;
    private final double multiplier;
    
    CosmeticRarity(String displayName, String icon, double multiplier) {
        this.displayName = displayName;
        this.icon = icon;
        this.multiplier = multiplier;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    /**
     * Get rarity color code
     */
    public String getColorCode() {
        return switch (this) {
            case COMMON -> "§f"; // White
            case RARE -> "§9"; // Blue
            case EPIC -> "§5"; // Dark Purple
            case LEGENDARY -> "§6"; // Gold
            case MYTHIC -> "§c"; // Red
        };
    }
    
    /**
     * Get rarity weight for random selection
     */
    public double getWeight() {
        return switch (this) {
            case COMMON -> 0.4; // 40% chance
            case RARE -> 0.3; // 30% chance
            case EPIC -> 0.2; // 20% chance
            case LEGENDARY -> 0.08; // 8% chance
            case MYTHIC -> 0.02; // 2% chance
        };
    }
    
    /**
     * Get rarity description
     */
    public String getDescription() {
        return switch (this) {
            case COMMON -> "Common cosmetics that are easily obtained";
            case RARE -> "Rare cosmetics that are moderately hard to obtain";
            case EPIC -> "Epic cosmetics that are hard to obtain";
            case LEGENDARY -> "Legendary cosmetics that are very hard to obtain";
            case MYTHIC -> "Mythic cosmetics that are extremely rare";
        };
    }
    
    /**
     * Get rarity tier
     */
    public int getTier() {
        return switch (this) {
            case COMMON -> 1;
            case RARE -> 2;
            case EPIC -> 3;
            case LEGENDARY -> 4;
            case MYTHIC -> 5;
        };
    }
    
    /**
     * Check if rarity is higher than another
     */
    public boolean isHigherThan(CosmeticRarity other) {
        return this.getTier() > other.getTier();
    }
    
    /**
     * Check if rarity is lower than another
     */
    public boolean isLowerThan(CosmeticRarity other) {
        return this.getTier() < other.getTier();
    }
    
    /**
     * Get rarity value for sorting
     */
    public int getValue() {
        return getTier() * 100;
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
