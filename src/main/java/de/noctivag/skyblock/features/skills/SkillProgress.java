package de.noctivag.skyblock.features.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.skills.types.SkillType;

/**
 * Represents progress in a specific skill
 */
public class SkillProgress {
    
    private final SkillType skillType;
    private double totalExperience;
    private int level;
    private double currentLevelExperience;
    private double experienceToNextLevel;
    private long lastUpdateTime;
    
    public SkillProgress(SkillType skillType) {
        this.skillType = skillType;
        this.totalExperience = 0.0;
        this.level = 0;
        this.currentLevelExperience = 0.0;
        this.experienceToNextLevel = calculateXPToNextLevel(0);
        this.lastUpdateTime = java.lang.System.currentTimeMillis();
    }
    
    /**
     * Add experience to the skill
     */
    public void addExperience(double experience) {
        this.totalExperience += experience;
        this.lastUpdateTime = java.lang.System.currentTimeMillis();
        
        // Update current level experience
        this.currentLevelExperience += experience;
        
        // Check if level increased
        while (currentLevelExperience >= experienceToNextLevel) {
            levelUp();
        }
    }
    
    /**
     * Level up the skill
     */
    private void levelUp() {
        this.level++;
        this.currentLevelExperience -= experienceToNextLevel;
        this.experienceToNextLevel = calculateXPToNextLevel(level);
    }
    
    /**
     * Calculate XP required for next level
     */
    private double calculateXPToNextLevel(int currentLevel) {
        if (currentLevel >= skillType.getMaxLevel()) {
            return Double.MAX_VALUE; // Max level reached
        }
        
        // Hypixel Skyblock XP formula
        double baseXP = skillType.getBaseXPRequirement();
        double multiplier = Math.pow(1.5, currentLevel);
        
        return baseXP * multiplier;
    }
    
    /**
     * Get progress to next level
     */
    public LevelProgress getLevelProgress() {
        double progress = currentLevelExperience;
        double needed = experienceToNextLevel;
        
        return new LevelProgress(level, progress, needed);
    }
    
    /**
     * Get experience needed for next level
     */
    public double getExperienceToNextLevel() {
        return experienceToNextLevel;
    }
    
    /**
     * Get progress percentage to next level
     */
    public double getProgressPercentage() {
        if (experienceToNextLevel <= 0) return 100.0;
        return (currentLevelExperience / experienceToNextLevel) * 100.0;
    }
    
    /**
     * Check if skill is maxed out
     */
    public boolean isMaxedOut() {
        return level >= skillType.getMaxLevel();
    }
    
    /**
     * Get skill efficiency (experience per hour)
     */
    public double getEfficiency() {
        if (lastUpdateTime == 0) return 0.0;
        
        long timeElapsed = java.lang.System.currentTimeMillis() - lastUpdateTime;
        double hoursElapsed = timeElapsed / (1000.0 * 60 * 60);
        
        return hoursElapsed > 0 ? totalExperience / hoursElapsed : 0.0;
    }
    
    /**
     * Get skill bonus based on level
     */
    public double getSkillBonus() {
        return switch (skillType) {
            case COMBAT -> level * 0.04; // 4% damage per level
            case MINING -> level * 0.02; // 2% mining speed per level
            case FORAGING -> level * 0.02; // 2% foraging speed per level
            case FISHING -> level * 0.01; // 1% fishing speed per level
            case FARMING -> level * 0.03; // 3% farming speed per level
            case ENCHANTING -> level * 0.05; // 5% enchanting power per level
            case ALCHEMY -> level * 0.02; // 2% potion duration per level
            case TAMING -> level * 0.03; // 3% pet stats per level
        };
    }
    
    /**
     * Get skill milestone rewards
     */
    public SkillMilestone getCurrentMilestone() {
        return SkillMilestone.getMilestoneForLevel(level);
    }
    
    /**
     * Get next milestone
     */
    public SkillMilestone getNextMilestone() {
        return SkillMilestone.getNextMilestone(level);
    }
    
    // Getters
    public SkillType getSkillType() {
        return skillType;
    }
    
    public double getTotalExperience() {
        return totalExperience;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getCurrentLevelExperience() {
        return currentLevelExperience;
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Level progress information
     */
    public static class LevelProgress {
        private final int currentLevel;
        private final double progress;
        private final double needed;
        
        public LevelProgress(int currentLevel, double progress, double needed) {
            this.currentLevel = currentLevel;
            this.progress = progress;
            this.needed = needed;
        }
        
        public int getCurrentLevel() {
            return currentLevel;
        }
        
        public double getProgress() {
            return progress;
        }
        
        public double getNeeded() {
            return needed;
        }
        
        public double getPercentage() {
            if (needed <= 0) return 100.0;
            return (progress / needed) * 100.0;
        }
        
        @Override
        public String toString() {
            return String.format("Level %d: %.1f/%.1f XP (%.1f%%)", 
                currentLevel, progress, needed, getPercentage());
        }
    }
}
