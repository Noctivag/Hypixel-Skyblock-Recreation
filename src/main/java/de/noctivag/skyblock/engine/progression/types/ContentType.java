package de.noctivag.skyblock.engine.progression.types;

/**
 * Content Type Enumeration
 * 
 * Defines the different types of game content that can have
 * progression requirements and access restrictions.
 */
public enum ContentType {
    DUNGEON("Dungeon", "🏰", "Underground dungeons with bosses and loot"),
    ZONE("Zone", "🗺️", "Different world areas and regions"),
    BOSS("Boss", "👹", "Boss fights and encounters"),
    ISLAND("Island", "🏝️", "Private island features and expansions"),
    MINION("Minion", "🤖", "Minion upgrades and tiers"),
    QUEST("Quest", "📜", "Quests and objectives"),
    RECIPE("Recipe", "📖", "Crafting recipes and unlocks"),
    ITEM("Item", "⚔️", "Items and equipment"),
    ENCHANTMENT("Enchantment", "✨", "Enchantments and upgrades"),
    PET("Pet", "🐾", "Pets and companions"),
    ACCESSORY("Accessory", "💍", "Accessories and talismans"),
    SLAYER("Slayer", "🗡️", "Slayer quests and bosses"),
    FISHING("Fishing", "🎣", "Fishing content and rewards"),
    FARMING("Farming", "🌾", "Farming content and upgrades"),
    MINING("Mining", "⛏️", "Mining content and upgrades"),
    FORAGING("Foraging", "🪓", "Foraging content and upgrades"),
    ALCHEMY("Alchemy", "🧪", "Alchemy content and recipes"),
    ENCHANTING("Enchanting", "📚", "Enchanting content and upgrades"),
    TAMING("Taming", "🐕", "Taming content and upgrades"),
    CARPENTRY("Carpentry", "🔨", "Carpentry content and upgrades"),
    RUNECRAFTING("Runecrafting", "🔮", "Runecrafting content and upgrades");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    ContentType(String displayName, String icon, String description) {
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
