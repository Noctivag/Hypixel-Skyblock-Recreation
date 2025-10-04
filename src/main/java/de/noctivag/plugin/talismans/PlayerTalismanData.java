package de.noctivag.plugin.talismans;
import org.bukkit.inventory.ItemStack;

/**
 * Player's talisman data including level, experience, coins, and statistics
 */
public class PlayerTalismanData {
    
    private int level;
    private int experience;
    private int coins;
    private int purchasedTalismans;
    private int totalExperience;
    
    public PlayerTalismanData() {
        this.level = 1;
        this.experience = 0;
        this.coins = 1000;
        this.purchasedTalismans = 0;
        this.totalExperience = 0;
    }
    
    public PlayerTalismanData(int level, int experience, int coins, int purchasedTalismans, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.coins = coins;
        this.purchasedTalismans = purchasedTalismans;
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
    
    public int getPurchasedTalismans() {
        return purchasedTalismans;
    }
    
    public void setPurchasedTalismans(int purchasedTalismans) {
        this.purchasedTalismans = purchasedTalismans;
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
    
    public void incrementPurchasedTalismans() {
        this.purchasedTalismans++;
    }
    
    private void levelUp() {
        this.level++;
        this.experience = 0;
        
        // Add level up rewards
        this.coins += level * 300;
    }
    
    private int getRequiredExperience(int level) {
        return level * 2500; // 2500 XP per level
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
        return "PlayerTalismanData{" +
                "level=" + level +
                ", experience=" + experience +
                ", coins=" + coins +
                ", purchasedTalismans=" + purchasedTalismans +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
