package de.noctivag.skyblock.enchanting;
import java.util.UUID;

import java.util.UUID;

/**
 * Player enchanting data
 */
public class PlayerEnchantingData {
    
    private final UUID playerId;
    private int enchantingXP = 0;
    private int enchantingLevel = 0;
    
    public PlayerEnchantingData(UUID playerId) {
        this.playerId = playerId;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getEnchantingXP() {
        return enchantingXP;
    }
    
    public void setEnchantingXP(int enchantingXP) {
        this.enchantingXP = enchantingXP;
        updateLevel();
    }
    
    public void addEnchantingXP(int amount) {
        this.enchantingXP += amount;
        updateLevel();
    }
    
    public int getEnchantingLevel() {
        return enchantingLevel;
    }
    
    private void updateLevel() {
        // Simple level calculation: every 100 XP = 1 level
        this.enchantingLevel = enchantingXP / 100;
    }
    
    public int getXPToNextLevel() {
        int currentLevelXP = enchantingLevel * 100;
        return (currentLevelXP + 100) - enchantingXP;
    }
}
