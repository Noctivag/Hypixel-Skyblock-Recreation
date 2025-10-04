package de.noctivag.skyblock.engine.collections.types;

import java.util.Arrays;

/**
 * Collection Source Types
 * 
 * Defines where items come from to determine if they should count
 * towards collection progression. Only natural gameplay sources
 * are allowed for collection progression.
 */
public enum CollectionSource {
    // Allowed sources (count towards collections)
    MINING("Mining", "⛏️", "Items obtained through mining blocks", true),
    FARMING("Farming", "🌾", "Items obtained through farming crops", true),
    MOB_DROP("Mob Drop", "👹", "Items obtained by killing mobs", true),
    MINION("Minion", "🤖", "Items obtained through minions", true),
    FISHING("Fishing", "🎣", "Items obtained through fishing", true),
    FORAGING("Foraging", "🪓", "Items obtained through chopping trees", true),
    NATURAL_GENERATION("Natural Generation", "🌱", "Items obtained through natural world generation", true),
    
    // Disallowed sources (do NOT count towards collections)
    AUCTION_HOUSE("Auction House", "🏛️", "Items purchased from the auction house", false),
    BAZAAR("Bazaar", "🏪", "Items purchased from the bazaar", false),
    DIRECT_TRADE("Direct Trade", "🤝", "Items obtained through direct player trading", false),
    GIFT("Gift", "🎁", "Items received as gifts from other players", false),
    ADMIN_GIVE("Admin Give", "👑", "Items given by administrators", false),
    
    // Unknown source (NOT eligible for collections)
    UNKNOWN("Unknown", "❓", "Items with unknown or missing provenance", false);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final boolean allowedForCollections;
    
    CollectionSource(String displayName, String icon, String description, boolean allowedForCollections) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.allowedForCollections = allowedForCollections;
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
     * Check if this source is allowed for collection progression
     */
    public boolean isAllowedForCollections() {
        return allowedForCollections;
    }
    
    /**
     * Get all allowed sources
     */
    public static CollectionSource[] getAllowedSources() {
        return Arrays.stream(values())
            .filter(CollectionSource::isAllowedForCollections)
            .toArray(CollectionSource[]::new);
    }
    
    /**
     * Get all disallowed sources
     */
    public static CollectionSource[] getDisallowedSources() {
        return Arrays.stream(values())
            .filter(source -> !source.isAllowedForCollections())
            .toArray(CollectionSource[]::new);
    }
    
    /**
     * Check if a source is allowed for collections
     */
    public static boolean isAllowedSource(CollectionSource source) {
        return source != null && source.isAllowedForCollections();
    }
    
    /**
     * Get source by name (case insensitive)
     */
    public static CollectionSource getByName(String name) {
        if (name == null) return null;
        
        return Arrays.stream(values())
            .filter(source -> source.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
