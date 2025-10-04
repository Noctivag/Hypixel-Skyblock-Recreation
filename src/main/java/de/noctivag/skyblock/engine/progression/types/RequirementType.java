package de.noctivag.skyblock.engine.progression.types;

/**
 * Requirement Type Enumeration
 * 
 * Defines the different types of requirements that can be
 * checked for content access and progression.
 */
public enum RequirementType {
    SKILL_LEVEL("Skill Level", "📊", "Minimum skill level requirement"),
    QUEST_COMPLETION("Quest Completion", "✅", "Completion of specific quests"),
    COMBINED_REQUIREMENTS("Combined Requirements", "🔗", "Multiple requirements that must all be met"),
    MINIMUM_COINS("Minimum Coins", "💰", "Minimum coin balance requirement"),
    COLLECTION_LEVEL("Collection Level", "📦", "Minimum collection level requirement"),
    PLAYER_LEVEL("Player Level", "⭐", "Minimum overall player level requirement"),
    ITEM_POSSESSION("Item Possession", "⚔️", "Possession of specific items"),
    ACHIEVEMENT("Achievement", "🏆", "Completion of specific achievements"),
    TIME_GATED("Time Gated", "⏰", "Time-based access restrictions"),
    DAILY_LIMIT("Daily Limit", "📅", "Daily access or completion limits"),
    WEEKLY_LIMIT("Weekly Limit", "📆", "Weekly access or completion limits"),
    GUILD_REQUIREMENT("Guild Requirement", "👥", "Guild membership or level requirements"),
    REPUTATION("Reputation", "⭐", "Reputation with specific factions"),
    PERMISSION("Permission", "🔐", "Server permission requirements"),
    CUSTOM("Custom", "⚙️", "Custom requirement type");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    RequirementType(String displayName, String icon, String description) {
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
