package de.noctivag.skyblock.accessories;
import org.bukkit.inventory.ItemStack;

/**
 * Player's accessory data including level, experience, coins, and statistics
 */
public class PlayerAccessoryData {
    
    private int level;
    private int experience;
    private int coins;
    private int purchasedAccessories;
    private int totalExperience;
    
    public PlayerAccessoryData() {
        this.level = 1;
        this.experience = 0;
        this.coins = 1000;
        this.purchasedAccessories = 0;
        this.totalExperience = 0;
    }
    
    public PlayerAccessoryData(int level, int experience, int coins, int purchasedAccessories, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.coins = coins;
        this.purchasedAccessories = purchasedAccessories;
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
    
    public int getPurchasedAccessories() {
        return purchasedAccessories;
    }
    
    public void setPurchasedAccessories(int purchasedAccessories) {
        this.purchasedAccessories = purchasedAccessories;
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
    
    public void incrementPurchasedAccessories() {
        this.purchasedAccessories++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
        
        // Add level up rewards
        this.coins += level * 400;
    }
    
    private int getRequiredExperience(int level) {
        return level * 3000; // 3000 XP per level
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
        return "PlayerAccessoryData{" +
                "level=" + level +
                ", experience=" + experience +
                ", coins=" + coins +
                ", purchasedAccessories=" + purchasedAccessories +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
