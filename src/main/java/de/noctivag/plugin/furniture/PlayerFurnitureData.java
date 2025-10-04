package de.noctivag.plugin.furniture;
import org.bukkit.inventory.ItemStack;

/**
 * Player's furniture data including level, experience, coins, and statistics
 */
public class PlayerFurnitureData {
    
    private int level;
    private int experience;
    private int coins;
    private int placedFurniture;
    private int totalExperience;
    
    public PlayerFurnitureData() {
        this.level = 1;
        this.experience = 0;
        this.coins = 1000;
        this.placedFurniture = 0;
        this.totalExperience = 0;
    }
    
    public PlayerFurnitureData(int level, int experience, int coins, int placedFurniture, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.coins = coins;
        this.placedFurniture = placedFurniture;
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
    
    public int getPlacedFurniture() {
        return placedFurniture;
    }
    
    public void setPlacedFurniture(int placedFurniture) {
        this.placedFurniture = placedFurniture;
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
    
    public void incrementPlacedFurniture() {
        this.placedFurniture++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
        
        // Add level up rewards
        this.coins += level * 100;
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
        return "PlayerFurnitureData{" +
                "level=" + level +
                ", experience=" + experience +
                ", coins=" + coins +
                ", placedFurniture=" + placedFurniture +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
