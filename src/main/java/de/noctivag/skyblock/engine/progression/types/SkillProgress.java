package de.noctivag.skyblock.engine.progression.types;

/**
 * Skill Progress for a specific skill type
 * 
 * Tracks the current level, experience, and progression for a single skill.
 * Uses the precise XP lookup tables for accurate progression calculation.
 */
public class SkillProgress {
    
    private final HypixelSkillType skillType;
    private final SkillLevelData levelData;
    private double totalExperience;
    private int level;
    private double xpProgressInLevel;
    private double experienceToNextLevel;
    private long lastUpdateTime;
    
    public SkillProgress(HypixelSkillType skillType, double totalExperience) {
        this.skillType = skillType;
        this.levelData = new SkillLevelData(skillType);
        this.totalExperience = totalExperience;
        this.lastUpdateTime = java.lang.System.currentTimeMillis();
        
        // Calculate initial level and progress
        updateLevelAndProgress();
    }
    
    /**
     * Add experience to the skill
     */
    public void addExperience(double experience) {
        if (experience <= 0) return;
        
        this.totalExperience += experience;
        this.lastUpdateTime = java.lang.System.currentTimeMillis();
        
        // Update level and progress
        updateLevelAndProgress();
    }
    
    /**
     * Update level and progress based on current total experience
     */
    private void updateLevelAndProgress() {
        // Calculate current level from total experience
        this.level = levelData.calculateLevelFromXP(totalExperience);
        
        // Calculate progress within current level
        this.xpProgressInLevel = levelData.getXPProgressInLevel(totalExperience);
        
        // Calculate experience required for next level
        this.experienceToNextLevel = levelData.getXPToNextLevel(totalExperience);
    }
    
    /**
     * Get the skill type
     */
    public HypixelSkillType getSkillType() {
        return skillType;
    }
    
    /**
     * Get current level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Get total experience
     */
    public double getTotalExperience() {
        return totalExperience;
    }
    
    /**
     * Get XP progress within current level (0.0 to 1.0)
     */
    public double getXPProgressInLevel() {
        return xpProgressInLevel;
    }
    
    /**
     * Get experience required for next level
     */
    public double getExperienceToNextLevel() {
        return experienceToNextLevel;
    }
    
    /**
     * Get last update time
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Get skill level data
     */
    public SkillLevelData getLevelData() {
        return levelData;
    }
    
    /**
     * Check if skill is at max level
     */
    public boolean isMaxLevel() {
        return level >= skillType.getMaxLevel();
    }
    
    /**
     * Get experience required for a specific level
     */
    public double getXPRequiredForLevel(int targetLevel) {
        return levelData.getXPRequiredForLevel(targetLevel);
    }
    
    /**
     * Get cumulative experience required to reach a specific level
     */
    public double getCumulativeXPForLevel(int targetLevel) {
        return levelData.getCumulativeXPForLevel(targetLevel);
    }
    
    /**
     * Get total experience required to reach max level
     */
    public double getTotalXPRequired() {
        return levelData.getTotalXPRequired();
    }
    
    /**
     * Get progress percentage to max level
     */
    public double getProgressToMaxLevel() {
        double totalRequired = getTotalXPRequired();
        if (totalRequired <= 0) return 0.0;
        
        return Math.min(totalExperience / totalRequired, 1.0);
    }
    
    /**
     * Get milestone levels for this skill
     */
    public int[] getMilestoneLevels() {
        return levelData.getMilestoneLevels();
    }
    
    /**
     * Check if current level is a milestone
     */
    public boolean isCurrentLevelMilestone() {
        return levelData.isMilestoneLevel(level);
    }
    
    /**
     * Get next milestone level
     */
    public int getNextMilestoneLevel() {
        int[] milestones = getMilestoneLevels();
        for (int milestone : milestones) {
            if (milestone > level) {
                return milestone;
            }
        }
        return skillType.getMaxLevel(); // Max level is the final milestone
    }
    
    /**
     * Get experience required for next milestone
     */
    public double getXPToNextMilestone() {
        int nextMilestone = getNextMilestoneLevel();
        double xpForMilestone = getCumulativeXPForLevel(nextMilestone);
        return xpForMilestone - totalExperience;
    }
    
    /**
     * Get skill power (level * weight)
     */
    public double getSkillPower() {
        return level * skillType.getWeight();
    }
    
    /**
     * Get skill efficiency (based on level and type)
     */
    public double getSkillEfficiency() {
        double baseEfficiency = 1.0;
        double levelBonus = level * 0.01; // 1% per level
        double typeMultiplier = skillType.getXPMultiplier();
        
        return baseEfficiency + levelBonus * typeMultiplier;
    }
    
    /**
     * Get skill category
     */
    public HypixelSkillType.SkillCategory getCategory() {
        return skillType.getCategory();
    }
    
    /**
     * Get skill display name
     */
    public String getDisplayName() {
        return skillType.getDisplayName();
    }
    
    /**
     * Get skill icon
     */
    public String getIcon() {
        return skillType.getIcon();
    }
    
    /**
     * Get skill description
     */
    public String getDescription() {
        return skillType.getDescription();
    }
    
    /**
     * Get formatted progress string
     */
    public String getFormattedProgress() {
        if (isMaxLevel()) {
            return String.format("%s %s (Level %d - MAX)", 
                getIcon(), getDisplayName(), level);
        } else {
            return String.format("%s %s (Level %d - %.1f%% to %d)", 
                getIcon(), getDisplayName(), level, 
                xpProgressInLevel * 100, level + 1);
        }
    }
    
    /**
     * Get detailed progress information
     */
    public String getDetailedProgress() {
        if (isMaxLevel()) {
            return String.format("%s %s - Level %d (MAX)\n" +
                "Total XP: %.0f\n" +
                "Progress: 100%%", 
                getIcon(), getDisplayName(), level, totalExperience);
        } else {
            return String.format("%s %s - Level %d\n" +
                "Total XP: %.0f\n" +
                "XP to Next Level: %.0f\n" +
                "Progress: %.1f%%", 
                getIcon(), getDisplayName(), level, 
                totalExperience, experienceToNextLevel, 
                xpProgressInLevel * 100);
        }
    }
    
    @Override
    public String toString() {
        return getFormattedProgress();
    }
}
