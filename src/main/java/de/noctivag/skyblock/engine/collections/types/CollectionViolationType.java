package de.noctivag.skyblock.engine.collections.types;

/**
 * Collection Violation Type Enumeration
 * 
 * Defines the different types of violations that can occur
 * when attempting to use items for collection progression.
 */
public enum CollectionViolationType {
    MISSING_PROVENANCE("Missing Provenance", "❓", "Item has no provenance data"),
    TRADE_SOURCE("Trade Source", "🏪", "Item obtained through trade (AH/Bazaar)"),
    INVALID_PROVENANCE("Invalid Provenance", "⚠️", "Item has invalid or corrupted provenance"),
    FUTURE_TIMESTAMP("Future Timestamp", "⏰", "Item timestamp is in the future"),
    OLD_TIMESTAMP("Old Timestamp", "📅", "Item timestamp is too old"),
    PLAYER_MISMATCH("Player Mismatch", "👤", "Item provenance doesn't match current player"),
    DUPLICATE_ITEM("Duplicate Item", "🔄", "Item already counted towards collections"),
    ADMIN_ITEM("Admin Item", "👑", "Item given by administrator"),
    GIFT_ITEM("Gift Item", "🎁", "Item received as gift"),
    DIRECT_TRADE_ITEM("Direct Trade Item", "🤝", "Item obtained through direct trade"),
    UNKNOWN_SOURCE("Unknown Source", "❓", "Item from unknown source");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    CollectionViolationType(String displayName, String icon, String description) {
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
