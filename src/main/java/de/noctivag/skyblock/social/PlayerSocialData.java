package de.noctivag.skyblock.social;
import org.bukkit.inventory.ItemStack;

/**
 * Player's social data including level, experience, and statistics
 */
public class PlayerSocialData {
    
    private int level;
    private int experience;
    private int friends;
    private int sentFriendRequests;
    private int acceptedFriendRequests;
    private int deniedFriendRequests;
    private int createdParties;
    private int joinedParties;
    private int totalExperience;
    
    public PlayerSocialData() {
        this.level = 1;
        this.experience = 0;
        this.friends = 0;
        this.sentFriendRequests = 0;
        this.acceptedFriendRequests = 0;
        this.deniedFriendRequests = 0;
        this.createdParties = 0;
        this.joinedParties = 0;
        this.totalExperience = 0;
    }
    
    public PlayerSocialData(int level, int experience, int friends, int sentFriendRequests, int acceptedFriendRequests, int deniedFriendRequests, int createdParties, int joinedParties, int totalExperience) {
        this.level = level;
        this.experience = experience;
        this.friends = friends;
        this.sentFriendRequests = sentFriendRequests;
        this.acceptedFriendRequests = acceptedFriendRequests;
        this.deniedFriendRequests = deniedFriendRequests;
        this.createdParties = createdParties;
        this.joinedParties = joinedParties;
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
    
    public int getFriends() {
        return friends;
    }
    
    public void setFriends(int friends) {
        this.friends = friends;
    }
    
    public int getSentFriendRequests() {
        return sentFriendRequests;
    }
    
    public void setSentFriendRequests(int sentFriendRequests) {
        this.sentFriendRequests = sentFriendRequests;
    }
    
    public int getAcceptedFriendRequests() {
        return acceptedFriendRequests;
    }
    
    public void setAcceptedFriendRequests(int acceptedFriendRequests) {
        this.acceptedFriendRequests = acceptedFriendRequests;
    }
    
    public int getDeniedFriendRequests() {
        return deniedFriendRequests;
    }
    
    public void setDeniedFriendRequests(int deniedFriendRequests) {
        this.deniedFriendRequests = deniedFriendRequests;
    }
    
    public int getCreatedParties() {
        return createdParties;
    }
    
    public void setCreatedParties(int createdParties) {
        this.createdParties = createdParties;
    }
    
    public int getJoinedParties() {
        return joinedParties;
    }
    
    public void setJoinedParties(int joinedParties) {
        this.joinedParties = joinedParties;
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
    
    public void incrementFriends() {
        this.friends++;
    }
    
    public void decrementFriends() {
        this.friends = Math.max(0, this.friends - 1);
    }
    
    public void incrementSentFriendRequests() {
        this.sentFriendRequests++;
    }
    
    public void incrementAcceptedFriendRequests() {
        this.acceptedFriendRequests++;
    }
    
    public void incrementDeniedFriendRequests() {
        this.deniedFriendRequests++;
    }
    
    public void incrementCreatedParties() {
        this.createdParties++;
    }
    
    public void incrementJoinedParties() {
        this.joinedParties++;
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
        return "PlayerSocialData{" +
                "level=" + level +
                ", experience=" + experience +
                ", friends=" + friends +
                ", sentFriendRequests=" + sentFriendRequests +
                ", acceptedFriendRequests=" + acceptedFriendRequests +
                ", deniedFriendRequests=" + deniedFriendRequests +
                ", createdParties=" + createdParties +
                ", joinedParties=" + joinedParties +
                ", totalExperience=" + totalExperience +
                '}';
    }
}
