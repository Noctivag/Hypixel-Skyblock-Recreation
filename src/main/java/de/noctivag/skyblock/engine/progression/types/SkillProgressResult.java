package de.noctivag.skyblock.engine.progression.types;

/**
 * Skill Progress Result
 * 
 * Contains the result of adding experience to a skill,
 * including level ups, progress information, and messages.
 */
public class SkillProgressResult {
    
    private final boolean success;
    private final int levelsGained;
    private final double xpProgressInLevel;
    private final double xpToNextLevel;
    private final String message;
    
    public SkillProgressResult(boolean success, int levelsGained, double xpProgressInLevel, 
                               double xpToNextLevel, String message) {
        this.success = success;
        this.levelsGained = levelsGained;
        this.xpProgressInLevel = xpProgressInLevel;
        this.xpToNextLevel = xpToNextLevel;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public int getLevelsGained() {
        return levelsGained;
    }
    
    public double getXPProgressInLevel() {
        return xpProgressInLevel;
    }
    
    public double getXPToNextLevel() {
        return xpToNextLevel;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean leveledUp() {
        return levelsGained > 0;
    }
    
    public double getProgressPercentage() {
        return xpProgressInLevel * 100.0;
    }
    
    @Override
    public String toString() {
        return String.format("SkillProgressResult{success=%b, levelsGained=%d, progress=%.1f%%, xpToNext=%.1f, message='%s'}", 
            success, levelsGained, getProgressPercentage(), xpToNextLevel, message);
    }
}
