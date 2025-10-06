package de.noctivag.skyblock.skills;

/**
 * Enum representing all available skills in Hypixel Skyblock
 */
public enum SkillType {
    COMBAT("Combat", "⚔", "&c", "Kampf-Fähigkeit", "Erhöht deine Gesundheit und Schaden"),
    MINING("Mining", "⛏", "&6", "Bergbau-Fähigkeit", "Erhöht deine Verteidigung und Bergbau-Geschwindigkeit"),
    FARMING("Farming", "🌾", "&a", "Landwirtschaft-Fähigkeit", "Erhöht deine Gesundheit und Ernte-Geschwindigkeit"),
    FORAGING("Foraging", "🌲", "&2", "Forstwirtschaft-Fähigkeit", "Erhöht deine Stärke und Holzfäll-Geschwindigkeit"),
    FISHING("Fishing", "🎣", "&9", "Angeln-Fähigkeit", "Erhöht deine Gesundheit und Angel-Geschwindigkeit"),
    ENCHANTING("Enchanting", "✨", "&d", "Verzauberung-Fähigkeit", "Erhöht deine Intelligenz und Verzauberungs-Erfahrung"),
    ALCHEMY("Alchemy", "🧪", "&5", "Alchemie-Fähigkeit", "Erhöht deine Intelligenz und Trank-Effekte"),
    CARPENTRY("Carpentry", "🔨", "&e", "Tischlerei-Fähigkeit", "Erhöht deine Geschwindigkeit und Handwerks-Erfahrung"),
    RUNECRAFTING("Runecrafting", "🔮", "&b", "Runenherstellung-Fähigkeit", "Erhöht deine Intelligenz und Runen-Kraft"),
    TAMING("Taming", "🐾", "&3", "Zähmung-Fähigkeit", "Erhöht deine Pet-Statistiken und Zähmungs-Erfahrung"),
    SOCIAL("Social", "👥", "&f", "Sozial-Fähigkeit", "Erhöht deine Gilden-Erfahrung und soziale Interaktionen"),
    CATACOMBS("Catacombs", "💀", "&8", "Katakomben-Fähigkeit", "Erhöht deine Dungeon-Statistiken und Katakomben-Erfahrung");

    private final String displayName;
    private final String icon;
    private final String color;
    private final String description;
    private final String benefits;

    SkillType(String displayName, String icon, String color, String description, String benefits) {
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
        this.description = description;
        this.benefits = benefits;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getBenefits() {
        return benefits;
    }

    /**
     * Get the XP required to reach a specific level
     * Uses the same formula as Hypixel Skyblock
     */
    public long getXPRequiredForLevel(int level) {
        if (level <= 1) return 0;
        if (level > 60) return Long.MAX_VALUE; // Cap at level 60
        
        // Hypixel Skyblock XP formula
        double xp = 0;
        for (int i = 1; i < level; i++) {
            if (i <= 15) {
                xp += Math.floor(i + 300 * Math.pow(2, i / 7.0));
            } else if (i <= 30) {
                xp += Math.floor(2.5 * Math.pow(i - 15, 2) + 87.5 * (i - 15) + 606);
            } else {
                xp += Math.floor(4.5 * Math.pow(i - 30, 2) + 162.5 * (i - 30) + 2220);
            }
        }
        return (long) xp;
    }

    /**
     * Get the level from total XP
     */
    public int getLevelFromXP(long totalXP) {
        for (int level = 1; level <= 60; level++) {
            if (getXPRequiredForLevel(level + 1) > totalXP) {
                return level;
            }
        }
        return 60;
    }

    /**
     * Get XP progress for current level
     */
    public long getXPProgressForLevel(long totalXP, int level) {
        long xpForCurrentLevel = getXPRequiredForLevel(level);
        long xpForNextLevel = getXPRequiredForLevel(level + 1);
        return totalXP - xpForCurrentLevel;
    }

    /**
     * Get XP required for next level
     */
    public long getXPRequiredForNextLevel(long totalXP, int level) {
        long xpForCurrentLevel = getXPRequiredForLevel(level);
        long xpForNextLevel = getXPRequiredForLevel(level + 1);
        return xpForNextLevel - xpForCurrentLevel;
    }

    /**
     * Get stat bonuses for a specific level
     */
    public SkillBonuses getBonusesForLevel(int level) {
        return new SkillBonuses(this, level);
    }
}