package de.noctivag.skyblock.magic;
import org.bukkit.inventory.ItemStack;

/**
 * Player's magic data including level, experience, and statistics
 */
public class PlayerMagicData {
    
    private int level;
    private int experience;
    private int castedSpells;
    private int totalExperience;
    
    public PlayerMagicData() {
        this.level = 1;
        this.experience = 0;
        this.castedSpells = 0;
        this.totalExperience = 0;
    }
    
    public PlayerMagicData(int level, int experience, int castedSpells, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.castedSpells = castedSpells;
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
    
    public int getCastedSpells() {
        return castedSpells;
    }
    
    public void setCastedSpells(int castedSpells) {
        this.castedSpells = castedSpells;
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
    
    public void incrementCastedSpells() {
        this.castedSpells++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
    }
    
    private int getRequiredExperience(int level) {
        return level * 1500; // 1500 XP per level
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
        return "PlayerMagicData{" +
                "level=" + level +
                ", experience=" + experience +
                ", castedSpells=" + castedSpells +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
