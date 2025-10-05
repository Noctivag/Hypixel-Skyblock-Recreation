package de.noctivag.skyblock.engine.collections;

/**
 * Represents the source of a collection item
 */
public enum CollectionSource {
    MINING("Mining"),
    FORAGING("Foraging"),
    FISHING("Fishing"),
    FARMING("Farming"),
    COMBAT("Combat"),
    CRAFTING("Crafting"),
    ENCHANTING("Enchanting"),
    ALCHEMY("Alchemy"),
    TAMING("Taming"),
    DUNGEON("Dungeon"),
    SLAYER("Slayer"),
    EVENT("Event"),
    SHOP("Shop"),
    AUCTION("Auction"),
    BAZAAR("Bazaar"),
    QUEST("Quest"),
    REWARD("Reward"),
    MOB_DROP("Mob Drop"),
    MINION("Minion"),
    NATURAL_GENERATION("Natural Generation"),
    AUCTION_HOUSE("Auction House"),
    DIRECT_TRADE("Direct Trade"),
    GIFT("Gift"),
    ADMIN_GIVE("Admin Give"),
    UNKNOWN("Unknown"),
    OTHER("Other");

    private final String displayName;

    CollectionSource(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isAllowedForCollections() {
        return this != ADMIN_GIVE && this != DIRECT_TRADE;
    }
    
    public static CollectionSource getByName(String name) {
        for (CollectionSource source : values()) {
            if (source.name().equalsIgnoreCase(name)) {
                return source;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
