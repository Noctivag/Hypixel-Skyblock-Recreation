package de.noctivag.skyblock.engine.progression.types;

import java.util.Arrays;

/**
 * Hypixel Skyblock Skill Types
 * 
 * Defines all 12 core skills with their specific characteristics.
 * Each skill has different XP requirements and progression curves.
 */
public enum HypixelSkillType {
    // Combat Skills
    COMBAT("Combat", "⚔️", "Increases damage dealt to mobs and combat effectiveness", 50),
    CATACOMBS("Catacombs", "🏰", "Increases dungeon effectiveness and rewards", 50),
    SLAYER("Slayer", "🗡️", "Increases slayer boss damage and rewards", 50),
    
    // Gathering Skills
    MINING("Mining", "⛏️", "Increases mining speed, fortune, and mining-related bonuses", 60),
    FORAGING("Foraging", "🪓", "Increases tree chopping speed and foraging efficiency", 50),
    FISHING("Fishing", "🎣", "Increases fishing speed and rare catch chances", 50),
    
    // Production Skills
    FARMING("Farming", "🌾", "Increases crop growth speed and farming yields", 60),
    ENCHANTING("Enchanting", "✨", "Increases enchantment power and enchanting success", 60),
    ALCHEMY("Alchemy", "🧪", "Increases potion duration and alchemy effectiveness", 50),
    
    // Social Skills
    TAMING("Taming", "🐾", "Increases pet stats and taming bonuses", 50),
    CARPENTRY("Carpentry", "🔨", "Increases carpentry efficiency and unlocks", 50),
    RUNECRAFTING("Runecrafting", "🔮", "Increases rune crafting success and power", 25);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final int maxLevel;
    
    HypixelSkillType(String displayName, String icon, String description, int maxLevel) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.maxLevel = maxLevel;
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
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * Get skill category
     */
    public SkillCategory getCategory() {
        return switch (this) {
            case COMBAT, CATACOMBS, SLAYER -> SkillCategory.COMBAT;
            case MINING, FORAGING, FISHING -> SkillCategory.GATHERING;
            case FARMING, ENCHANTING, ALCHEMY -> SkillCategory.PRODUCTION;
            case TAMING, CARPENTRY, RUNECRAFTING -> SkillCategory.SOCIAL;
        };
    }
    
    /**
     * Get skill weight for skill power calculation
     */
    public double getWeight() {
        return switch (this) {
            case COMBAT -> 1.5; // Most important for combat effectiveness
            case MINING -> 1.4; // Very important for resource gathering
            case FARMING -> 1.3; // Important for food production
            case CATACOMBS -> 1.2; // Important for dungeon progression
            case ENCHANTING -> 1.2; // Important for gear enhancement
            case FORAGING -> 1.1; // Moderate importance
            case FISHING -> 1.1; // Moderate importance
            case ALCHEMY -> 1.0; // Standard importance
            case SLAYER -> 1.0; // Standard importance
            case TAMING -> 0.9; // Lower importance
            case CARPENTRY -> 0.8; // Lower importance
            case RUNECRAFTING -> 0.7; // Lowest importance
        };
    }
    
    /**
     * Get XP multiplier for this skill (affects XP gain rate)
     */
    public double getXPMultiplier() {
        return switch (this) {
            case COMBAT -> 1.0; // Standard XP gain
            case MINING -> 1.0; // Standard XP gain
            case FARMING -> 1.2; // Higher XP gain (easier to level)
            case FORAGING -> 1.0; // Standard XP gain
            case FISHING -> 0.8; // Lower XP gain (harder to level)
            case ENCHANTING -> 0.5; // Much lower XP gain (very hard to level)
            case ALCHEMY -> 1.5; // Higher XP gain (easier to level)
            case CATACOMBS -> 0.7; // Lower XP gain (harder to level)
            case SLAYER -> 0.6; // Much lower XP gain (very hard to level)
            case TAMING -> 0.3; // Very low XP gain (extremely hard to level)
            case CARPENTRY -> 1.0; // Standard XP gain
            case RUNECRAFTING -> 0.4; // Very low XP gain (extremely hard to level)
        };
    }
    
    /**
     * Get skills by category
     */
    public static HypixelSkillType[] getByCategory(SkillCategory category) {
        return Arrays.stream(values())
            .filter(skill -> skill.getCategory() == category)
            .toArray(HypixelSkillType[]::new);
    }
    
    /**
     * Get all combat-related skills
     */
    public static HypixelSkillType[] getCombatSkills() {
        return getByCategory(SkillCategory.COMBAT);
    }
    
    /**
     * Get all gathering skills
     */
    public static HypixelSkillType[] getGatheringSkills() {
        return getByCategory(SkillCategory.GATHERING);
    }
    
    /**
     * Get all production skills
     */
    public static HypixelSkillType[] getProductionSkills() {
        return getByCategory(SkillCategory.PRODUCTION);
    }
    
    /**
     * Get all social skills
     */
    public static HypixelSkillType[] getSocialSkills() {
        return getByCategory(SkillCategory.SOCIAL);
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
    
    /**
     * Skill categories for organization
     */
    public enum SkillCategory {
        COMBAT("Combat"),
        GATHERING("Gathering"),
        PRODUCTION("Production"),
        SOCIAL("Social");
        
        private final String displayName;
        
        SkillCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
