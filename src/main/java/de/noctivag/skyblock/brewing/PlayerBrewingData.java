package de.noctivag.skyblock.brewing;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Player brewing data storage
 */
public class PlayerBrewingData {
    
    private final UUID playerUuid;
    private int level;
    private int experience;
    private int coins;
    private int brewedPotions;
    
    public PlayerBrewingData(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.level = 1;
        this.experience = 0;
        this.coins = 0;
        this.brewedPotions = 0;
    }
    
    public UUID getPlayerUuid() {
        return playerUuid;
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

    public void addExperience(int amount) {
        this.experience += amount;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public void removeCoins(int amount) {
        this.coins = Math.max(0, this.coins - amount);
    }

    public int getBrewedPotions() {
        return brewedPotions;
    }

    public void setBrewedPotions(int brewedPotions) {
        this.brewedPotions = brewedPotions;
    }

    public void incrementBrewedPotions() {
        this.brewedPotions++;
    }

    public int getTotalExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        // Simple calculation: 100 * level^2
        return (level * level * 100) - experience;
    }

    public double getExperienceProgress() {
        int currentLevelExp = (level - 1) * (level - 1) * 100;
        int nextLevelExp = level * level * 100;
        int expInLevel = experience - currentLevelExp;
        int expNeeded = nextLevelExp - currentLevelExp;
        return (double) expInLevel / expNeeded;
    }

    public CompletableFuture<PlayerBrewingData> thenAccept(java.util.function.Consumer<PlayerBrewingData> action) {
        action.accept(this);
        return CompletableFuture.completedFuture(this);
    }
}