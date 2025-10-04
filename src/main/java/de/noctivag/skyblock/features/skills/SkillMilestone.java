package de.noctivag.skyblock.features.skills;
import org.bukkit.inventory.ItemStack;

/**
 * Skill milestones and their rewards
 */
public enum SkillMilestone {
    BEGINNER(5, "Beginner", "🎯", "Basic skill milestone"),
    APPRENTICE(10, "Apprentice", "📚", "Learning the basics"),
    JOURNEYMAN(15, "Journeyman", "🔨", "Developing skills"),
    EXPERT(20, "Expert", "⚡", "Mastering techniques"),
    MASTER(25, "Master", "👑", "Advanced proficiency"),
    GRANDMASTER(30, "Grandmaster", "🏆", "Exceptional skill"),
    LEGEND(35, "Legend", "⭐", "Legendary status"),
    MYTH(40, "Myth", "💫", "Mythical power"),
    DIVINE(45, "Divine", "✨", "Divine mastery"),
    TRANSCENDENT(50, "Transcendent", "🌟", "Transcendent power");
    
    private final int level;
    private final String displayName;
    private final String icon;
    private final String description;
    
    SkillMilestone(int level, String displayName, String icon, String description) {
        this.level = level;
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
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
     * Get milestone for a specific level
     */
    public static SkillMilestone getMilestoneForLevel(int level) {
        SkillMilestone current = BEGINNER;
        
        for (SkillMilestone milestone : values()) {
            if (level >= milestone.getLevel()) {
                current = milestone;
            } else {
                break;
            }
        }
        
        return current;
    }
    
    /**
     * Get next milestone for a level
     */
    public static SkillMilestone getNextMilestone(int level) {
        for (SkillMilestone milestone : values()) {
            if (level < milestone.getLevel()) {
                return milestone;
            }
        }
        
        return TRANSCENDENT; // Already at max milestone
    }
    
    /**
     * Get progress to next milestone
     */
    public static int getProgressToNextMilestone(int level) {
        SkillMilestone next = getNextMilestone(level);
        return next.getLevel() - level;
    }
    
    /**
     * Check if level is a milestone
     */
    public static boolean isMilestone(int level) {
        for (SkillMilestone milestone : values()) {
            if (milestone.getLevel() == level) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get milestone rewards
     */
    public MilestoneReward getReward() {
        return switch (this) {
            case BEGINNER -> new MilestoneReward("Basic rewards", 100, 1.0);
            case APPRENTICE -> new MilestoneReward("Apprentice rewards", 250, 1.1);
            case JOURNEYMAN -> new MilestoneReward("Journeyman rewards", 500, 1.2);
            case EXPERT -> new MilestoneReward("Expert rewards", 1000, 1.3);
            case MASTER -> new MilestoneReward("Master rewards", 2500, 1.4);
            case GRANDMASTER -> new MilestoneReward("Grandmaster rewards", 5000, 1.5);
            case LEGEND -> new MilestoneReward("Legend rewards", 10000, 1.6);
            case MYTH -> new MilestoneReward("Myth rewards", 25000, 1.7);
            case DIVINE -> new MilestoneReward("Divine rewards", 50000, 1.8);
            case TRANSCENDENT -> new MilestoneReward("Transcendent rewards", 100000, 2.0);
        };
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
    
    /**
     * Milestone reward information
     */
    public static class MilestoneReward {
        private final String description;
        private final int coins;
        private final double multiplier;
        
        public MilestoneReward(String description, int coins, double multiplier) {
            this.description = description;
            this.coins = coins;
            this.multiplier = multiplier;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getCoins() {
            return coins;
        }
        
        public double getMultiplier() {
            return multiplier;
        }
    }
}
