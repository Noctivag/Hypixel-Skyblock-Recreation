package de.noctivag.plugin.armor;
import org.bukkit.inventory.ItemStack;

/**
 * ArmorCategory - Categories for organizing armor sets
 * 
 * Categories:
 * - DRAGON: Dragon armor sets
 * - MINING: Mining-focused armor sets
 * - COMBAT: Combat-focused armor sets
 * - FISHING: Fishing-focused armor sets
 * - EVENT: Event and seasonal armor sets
 * - NETHER: Nether and Crimson Isle armor sets
 * - SPECIAL: Special and unique armor sets
 * - BASIC: Basic armor sets
 * - DUNGEON: Dungeon armor sets
 * - SLAYER: Slayer armor sets
 */
public enum ArmorCategory {
    DRAGON("Dragon Armor", "§6Dragon Armor", "Armor sets crafted from dragon fragments"),
    MINING("Mining Armor", "§eMining Armor", "Armor sets that enhance mining capabilities"),
    COMBAT("Combat Armor", "§cCombat Armor", "Armor sets focused on combat and damage"),
    FISHING("Fishing Armor", "§bFishing Armor", "Armor sets that enhance fishing abilities"),
    EVENT("Event Armor", "§dEvent Armor", "Special armor sets for events and seasons"),
    NETHER("Nether Armor", "§4Nether Armor", "Armor sets from the Nether and Crimson Isle"),
    SPECIAL("Special Armor", "§5Special Armor", "Unique and special armor sets"),
    BASIC("Basic Armor", "§7Basic Armor", "Basic armor sets for beginners"),
    DUNGEON("Dungeon Armor", "§8Dungeon Armor", "Armor sets from dungeon bosses"),
    SLAYER("Slayer Armor", "§0Slayer Armor", "Armor sets from slayer bosses");
    
    private final String name;
    private final String displayName;
    private final String description;
    
    ArmorCategory(String name, String displayName, String description) {
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
