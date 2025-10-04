package de.noctivag.plugin.fairysouls;
import org.bukkit.inventory.ItemStack;

/**
 * Player's fairy soul data including level, experience, and statistics
 */
public class PlayerFairySoulData {
    
    private int level;
    private int experience;
    private int collectedSouls;
    private int totalExperience;
    
    public PlayerFairySoulData() {
        this.level = 1;
        this.experience = 0;
        this.collectedSouls = 0;
        this.totalExperience = 0;
    }
    
    public PlayerFairySoulData(int level, int experience, int collectedSouls, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.collectedSouls = collectedSouls;
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
    
    public int getCollectedSouls() {
        return collectedSouls;
    }
    
    public void setCollectedSouls(int collectedSouls) {
        this.collectedSouls = collectedSouls;
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
    
    public void incrementCollectedSouls() {
        this.collectedSouls++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
    }
    
    private int getRequiredExperience(int level) {
        return level * 1000; // 1000 XP per level
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
        return "PlayerFairySoulData{" +
                "level=" + level +
                ", experience=" + experience +
                ", collectedSouls=" + collectedSouls +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
