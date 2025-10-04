package de.noctivag.skyblock.gemstones;
import org.bukkit.inventory.ItemStack;

/**
 * Player's gemstone data including level, experience, and statistics
 */
public class PlayerGemstoneData {
    
    private int level;
    private int experience;
    private int miningLevel;
    private int minedGemstones;
    private int totalExperience;
    
    public PlayerGemstoneData() {
        this.level = 1;
        this.experience = 0;
        this.miningLevel = 1;
        this.minedGemstones = 0;
        this.totalExperience = 0;
    }
    
    public PlayerGemstoneData(int level, int experience, int miningLevel, int minedGemstones, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.miningLevel = miningLevel;
        this.minedGemstones = minedGemstones;
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
    
    public int getMiningLevel() {
        return miningLevel;
    }
    
    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }
    
    public int getMinedGemstones() {
        return minedGemstones;
    }
    
    public void setMinedGemstones(int minedGemstones) {
        this.minedGemstones = minedGemstones;
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
    
    public void incrementMinedGemstones() {
        this.minedGemstones++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
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
        return "PlayerGemstoneData{" +
                "level=" + level +
                ", experience=" + experience +
                ", miningLevel=" + miningLevel +
                ", minedGemstones=" + minedGemstones +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
