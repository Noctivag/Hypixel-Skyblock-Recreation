package de.noctivag.plugin.features.skills.rewards;
import org.bukkit.inventory.ItemStack;

/**
 * Types of skill rewards
 */
public enum RewardType {
    COINS("Coins", "💰", "Currency reward"),
    ITEM("Item", "🎁", "Item reward"),
    STAT_BOOST("Stat Boost", "⚡", "Permanent stat increase"),
    RECIPE("Recipe", "📜", "Recipe unlock"),
    ACCESSORY("Accessory", "💍", "Talisman or accessory"),
    PET("Pet", "🐾", "Pet reward");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    RewardType(String displayName, String icon, String description) {
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
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
