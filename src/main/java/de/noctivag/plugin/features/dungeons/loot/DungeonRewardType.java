package de.noctivag.plugin.features.dungeons.loot;
import org.bukkit.inventory.ItemStack;

/**
 * Types of dungeon rewards
 */
public enum DungeonRewardType {
    EXPERIENCE("Experience", "⭐", "XP for skills and levels"),
    COINS("Coins", "💰", "Standard currency"),
    DUNGEON_ESSENCE("Dungeon Essence", "💎", "Special dungeon currency"),
    ITEM("Item", "🎁", "Special dungeon items"),
    PET("Pet", "🐾", "Dungeon pets"),
    ENCHANTMENT("Enchantment", "✨", "Special enchantments"),
    ACCESSORY("Accessory", "💍", "Talismans and accessories");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    DungeonRewardType(String displayName, String icon, String description) {
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
