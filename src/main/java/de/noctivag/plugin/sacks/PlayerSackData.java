package de.noctivag.plugin.sacks;
import org.bukkit.inventory.ItemStack;

/**
 * Player's sack data including level, experience, coins, and statistics
 */
public class PlayerSackData {
    
    private int level;
    private int experience;
    private int coins;
    private int purchasedSacks;
    private int totalExperience;
    
    public PlayerSackData() {
        this.level = 1;
        this.experience = 0;
        this.coins = 1000;
        this.purchasedSacks = 0;
        this.totalExperience = 0;
    }
    
    public PlayerSackData(int level, int experience, int coins, int purchasedSacks, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.coins = coins;
        this.purchasedSacks = purchasedSacks;
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
    
    public int getPurchasedSacks() {
        return purchasedSacks;
    }
    
    public void setPurchasedSacks(int purchasedSacks) {
        this.purchasedSacks = purchasedSacks;
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
    
    public void incrementPurchasedSacks() {
        this.purchasedSacks++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
        
        // Add level up rewards
        this.coins += level * 150;
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
        return "PlayerSackData{" +
                "level=" + level +
                ", experience=" + experience +
                ", coins=" + coins +
                ", purchasedSacks=" + purchasedSacks +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
