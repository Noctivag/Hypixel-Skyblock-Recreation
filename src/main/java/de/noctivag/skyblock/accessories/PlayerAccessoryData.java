package de.noctivag.skyblock.accessories;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Player accessory data storage
 */
public class PlayerAccessoryData {
    
    private final UUID playerUuid;
    private final List<String> purchasedAccessories;
    private int level;
    private int experience;
    private int coins;
    
    public PlayerAccessoryData(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.purchasedAccessories = new ArrayList<>();
        this.level = 1;
        this.experience = 0;
        this.coins = 0;
    }
    
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public List<String> getPurchasedAccessories() {
        return purchasedAccessories;
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

    public void incrementPurchasedAccessories() {
        // This method is called when a new accessory is purchased
        // The actual accessory ID should be passed to addPurchasedAccessory
    }

    public int getTotalExperience() {
        return experience;
    }
}