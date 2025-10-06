package de.noctivag.skyblock.items;

/**
 * ItemCategory - Enum for item categories
 */
public enum ItemCategory {
    WEAPONS("Weapons", "⚔"),
    TOOLS("Tools", "⛏"),
    ARMOR("Armor", "🛡"),
    CONSUMABLES("Consumables", "🍎"),
    BLOCKS("Blocks", "🧱"),
    MISC("Miscellaneous", "📦"),
    DRAGON("Dragon", "🐉"),
    DUNGEON("Dungeon", "🏰"),
    SLAYER("Slayer", "💀"),
    MINING("Mining", "⛏"),
    FISHING("Fishing", "🎣"),
    MAGIC("Magic", "✨");

    private final String displayName;
    private final String icon;

    ItemCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() { return displayName; }
    public String getIcon() { return icon; }
    public String getDescription() { return "Items in the " + displayName + " category"; }
}
