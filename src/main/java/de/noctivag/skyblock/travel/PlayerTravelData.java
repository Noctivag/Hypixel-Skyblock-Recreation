package de.noctivag.skyblock.travel;
import org.bukkit.inventory.ItemStack;

/**
 * Player's travel data including level, experience, and statistics
 */
public class PlayerTravelData {
    
    private int level;
    private int experience;
    private int teleports;
    private int totalExperience;
    
    public PlayerTravelData() {
        this.level = 1;
        this.experience = 0;
        this.teleports = 0;
        this.totalExperience = 0;
    }
    
    public PlayerTravelData(int level, int experience, int teleports, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.teleports = teleports;
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
    
    public int getTeleports() {
        return teleports;
    }
    
    public void setTeleports(int teleports) {
        this.teleports = teleports;
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
    
    public void incrementTeleports() {
        this.teleports++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
    }
    
    private int getRequiredExperience(int level) {
        return level * 500; // 500 XP per level
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
        return "PlayerTravelData{" +
                "level=" + level +
                ", experience=" + experience +
                ", teleports=" + teleports +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
