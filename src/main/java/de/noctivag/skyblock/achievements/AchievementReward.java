package de.noctivag.skyblock.achievements;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Achievement reward class
 */
public class AchievementReward {
    
    private final int coins;
    private final int combatXP;
    private final int miningXP;
    private final int foragingXP;
    private final int fishingXP;
    private final int farmingXP;
    private final int enchantingXP;
    private final int alchemyXP;
    private final int tamingXP;
    private final List<ItemStack> items;
    
    public AchievementReward(int coins, int combatXP, int miningXP, int foragingXP, 
                           int fishingXP, int farmingXP, int enchantingXP, 
                           int alchemyXP, int tamingXP) {
        this(coins, combatXP, miningXP, foragingXP, fishingXP, farmingXP, 
             enchantingXP, alchemyXP, tamingXP, null);
    }
    
    public AchievementReward(int coins, int combatXP, int miningXP, int foragingXP, 
                           int fishingXP, int farmingXP, int enchantingXP, 
                           int alchemyXP, int tamingXP, List<ItemStack> items) {
        this.coins = coins;
        this.combatXP = combatXP;
        this.miningXP = miningXP;
        this.foragingXP = foragingXP;
        this.fishingXP = fishingXP;
        this.farmingXP = farmingXP;
        this.enchantingXP = enchantingXP;
        this.alchemyXP = alchemyXP;
        this.tamingXP = tamingXP;
        this.items = items;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public int getCombatXP() {
        return combatXP;
    }
    
    public int getMiningXP() {
        return miningXP;
    }
    
    public int getForagingXP() {
        return foragingXP;
    }
    
    public int getFishingXP() {
        return fishingXP;
    }
    
    public int getFarmingXP() {
        return farmingXP;
    }
    
    public int getEnchantingXP() {
        return enchantingXP;
    }
    
    public int getAlchemyXP() {
        return alchemyXP;
    }
    
    public int getTamingXP() {
        return tamingXP;
    }
    
    public List<ItemStack> getItems() {
        return items;
    }
}
