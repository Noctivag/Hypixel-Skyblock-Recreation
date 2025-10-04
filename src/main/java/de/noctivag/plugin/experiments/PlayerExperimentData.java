package de.noctivag.plugin.experiments;
import org.bukkit.inventory.ItemStack;

/**
 * Player's experiment data including level, experience, coins, and statistics
 */
public class PlayerExperimentData {
    
    private int level;
    private int experience;
    private int coins;
    private int startedExperiments;
    private int completedExperiments;
    private int totalExperience;
    
    public PlayerExperimentData() {
        this.level = 1;
        this.experience = 0;
        this.coins = 1000;
        this.startedExperiments = 0;
        this.completedExperiments = 0;
        this.totalExperience = 0;
    }
    
    public PlayerExperimentData(int level, int experience, int coins, int startedExperiments, int completedExperiments, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.coins = coins;
        this.startedExperiments = startedExperiments;
        this.completedExperiments = completedExperiments;
        this.totalExperience = totalExperience;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public void setCoins(int coins) {
        this.coins = coins;
    }
    
    public int getStartedExperiments() {
        return startedExperiments;
    }
    
    public void setStartedExperiments(int startedExperiments) {
        this.startedExperiments = startedExperiments;
    }
    
    public int getCompletedExperiments() {
        return completedExperiments;
    }
    
    public void setCompletedExperiments(int completedExperiments) {
        this.completedExperiments = completedExperiments;
    }
    
    public int getTotalExperience() {
        return totalExperience;
    }
    
    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }
    
    public void addExperience(int experience) {
        this.experience += experience;
        this.totalExperience += experience;
        
        // Check for level up
        int requiredExp = getRequiredExperience(level + 1);
        if (this.experience >= requiredExp) {
            levelUp();
        }
    }
    
    public void addCoins(int coins) {
        this.coins += coins;
    }
    
    public void removeCoins(int coins) {
        this.coins = Math.max(0, this.coins - coins);
    }
    
    public void incrementStartedExperiments() {
        this.startedExperiments++;
    }
    
    public void incrementCompletedExperiments() {
        this.completedExperiments++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
        
        // Add level up rewards
        this.coins += level * 200;
    }
    
    private int getRequiredExperience(int level) {
        return level * 2000; // 2000 XP per level
    }
    
    public int getExperienceToNextLevel() {
        return getRequiredExperience(level + 1) - experience;
    }
    
    public double getExperienceProgress() {
        int requiredExp = getRequiredExperience(level + 1);
        return (double) experience / requiredExp;
    }
    
    @Override
    public String toString() {
        return "PlayerExperimentData{" +
                "level=" + level +
                ", experience=" + experience +
                ", coins=" + coins +
                ", startedExperiments=" + startedExperiments +
                ", completedExperiments=" + completedExperiments +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
