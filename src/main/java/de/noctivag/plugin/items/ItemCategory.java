package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

/**
 * ItemCategory - Categories for organizing item types
 * 
 * Categories:
 * - DRAGON_WEAPONS: Dragon-related weapons
 * - DUNGEON_WEAPONS: Weapons from dungeons
 * - SLAYER_WEAPONS: Weapons for slayer bosses
 * - MINING_TOOLS: Mining tools and drills
 * - FISHING_RODS: Fishing rods and tools
 * - MAGIC_WEAPONS: Magic weapons and wands
 * - BOWS_CROSSBOWS: Bows and crossbows
 * - SPECIAL_ITEMS: Special and unique items
 */
public enum ItemCategory {
    DRAGON_WEAPONS("Dragon Weapons", "§6Dragon Weapons", "Weapons crafted from dragon materials"),
    DUNGEON_WEAPONS("Dungeon Weapons", "§8Dungeon Weapons", "Weapons obtained from dungeon bosses"),
    SLAYER_WEAPONS("Slayer Weapons", "§0Slayer Weapons", "Weapons designed for slayer boss fights"),
    MINING_TOOLS("Mining Tools", "§eMining Tools", "Tools that enhance mining capabilities"),
    FISHING_RODS("Fishing Rods", "§bFishing Rods", "Rods that enhance fishing abilities"),
    MAGIC_WEAPONS("Magic Weapons", "§dMagic Weapons", "Weapons that channel magical energy"),
    BOWS_CROSSBOWS("Bows & Crossbows", "§cBows & Crossbows", "Ranged weapons for combat"),
    SPECIAL_ITEMS("Special Items", "§5Special Items", "Unique and special items with special abilities");
    
    private final String name;
    private final String displayName;
    private final String description;
    
    ItemCategory(String name, String displayName, String description) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
