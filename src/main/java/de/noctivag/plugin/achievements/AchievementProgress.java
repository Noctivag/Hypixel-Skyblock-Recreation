package de.noctivag.plugin.achievements;

/**
 * Achievement progress tracking
 */
public class AchievementProgress {
    
    private int current = 0;
    private int required = 1;
    
    public AchievementProgress() {}
    
    public AchievementProgress(int current, int required) {
        this.current = current;
        this.required = required;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    public int getRequired() {
        return required;
    }
    
    public void setRequired(int required) {
        this.required = required;
    }
    
    public boolean isCompleted() {
        return current >= required;
    }
    
    public double getPercentage() {
        if (required == 0) return 0.0;
        return Math.min(100.0, (double) current / required * 100.0);
    }
}
