package de.noctivag.skyblock.slayers;

import java.util.Map;

/**
 * Represents a slayer boss
 */
public class SlayerBoss {
    
    private final String id;
    private final String name;
    private final String description;
    private final BossType type;
    private final int maxTier;
    private final double baseHealth;
    private final double baseDamage;
    private final double baseDefense;
    private final double baseXp;
    private final double baseCoins;
    private final double questCost;
    private final Map<String, Boolean> abilities;
    
    public SlayerBoss(String id, String name, String description, BossType type,
                     int maxTier, double baseHealth, double baseDamage, double baseDefense,
                     double baseXp, double baseCoins, double questCost, Map<String, Boolean> abilities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.maxTier = maxTier;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseDefense = baseDefense;
        this.baseXp = baseXp;
        this.baseCoins = baseCoins;
        this.questCost = questCost;
        this.abilities = abilities;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BossType getType() { return type; }
    public int getMaxTier() { return maxTier; }
    public double getBaseHealth() { return baseHealth; }
    public double getBaseDamage() { return baseDamage; }
    public double getBaseDefense() { return baseDefense; }
    public double getBaseXp() { return baseXp; }
    public double getBaseCoins() { return baseCoins; }
    public double getQuestCost() { return questCost; }
    public Map<String, Boolean> getAbilities() { return abilities; }
    
    /**
     * Get health for specific tier
     */
    public double getHealthForTier(int tier) {
        return baseHealth * Math.pow(2, tier - 1);
    }
    
    /**
     * Get damage for specific tier
     */
    public double getDamageForTier(int tier) {
        return baseDamage * Math.pow(1.5, tier - 1);
    }
    
    /**
     * Get defense for specific tier
     */
    public double getDefenseForTier(int tier) {
        return baseDefense * Math.pow(1.3, tier - 1);
    }
    
    /**
     * Get XP reward for specific tier
     */
    public double getXpForTier(int tier) {
        return baseXp * Math.pow(1.8, tier - 1);
    }
    
    /**
     * Get coin reward for specific tier
     */
    public double getCoinsForTier(int tier) {
        return baseCoins * Math.pow(1.6, tier - 1);
    }
    
    /**
     * Get quest cost for specific tier
     */
    public double getQuestCostForTier(int tier) {
        return questCost * Math.pow(1.4, tier - 1);
    }
    
    /**
     * Check if boss has ability
     */
    public boolean hasAbility(String abilityName) {
        return abilities.getOrDefault(abilityName, false);
    }
    
    /**
     * Get loot table ID
     */
    public String getLootTableId() {
        return "slayer_" + id.toLowerCase();
    }
    
    /**
     * Boss type enum
     */
    public enum BossType {
        UNDEAD,
        SPIDER,
        WOLF,
        ENDERMAN,
        BLAZE,
        CUSTOM
    }
}
