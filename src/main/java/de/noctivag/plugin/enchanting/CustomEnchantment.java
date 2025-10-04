package de.noctivag.plugin.enchanting;

/**
 * Custom enchantment class
 */
public class CustomEnchantment {
    
    private final String id;
    private final String name;
    private final String description;
    private final EnchantmentType type;
    private final EnchantmentRarity rarity;
    private final int minLevel;
    private final int maxLevel;
    private final int[] xpCosts;
    
    public CustomEnchantment(String id, String name, String description, 
                           EnchantmentType type, EnchantmentRarity rarity, 
                           int minLevel, int maxLevel, int[] xpCosts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.xpCosts = xpCosts;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public EnchantmentType getType() {
        return type;
    }
    
    public EnchantmentRarity getRarity() {
        return rarity;
    }
    
    public String getColor() {
        return rarity.getColor();
    }
    
    public int getMinLevel() {
        return minLevel;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public int getXPCost(int level) {
        if (level < 0 || level >= xpCosts.length) return 0;
        return xpCosts[level];
    }
}