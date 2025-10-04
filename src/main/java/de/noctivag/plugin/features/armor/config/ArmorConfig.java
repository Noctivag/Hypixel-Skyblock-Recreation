package de.noctivag.plugin.features.armor.config;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import java.util.List;

/**
 * Armor Configuration Class
 */
public class ArmorConfig {
    private final String name;
    private final String displayName;
    private final Material material;
    private final String description;
    private final ArmorCategory category;
    private final ArmorRarity rarity;
    private final int baseDefense;
    private final List<String> features;
    private final List<String> requirements;
    
    public ArmorConfig(String name, String displayName, Material material, String description,
                      ArmorCategory category, ArmorRarity rarity, int baseDefense, 
                      List<String> features, List<String> requirements) {
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
        this.baseDefense = baseDefense;
        this.features = features;
        this.requirements = requirements;
    }
    
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
    public String getDescription() { return description; }
    public ArmorCategory getCategory() { return category; }
    public ArmorRarity getRarity() { return rarity; }
    public int getBaseDefense() { return baseDefense; }
    public List<String> getFeatures() { return features; }
    public List<String> getRequirements() { return requirements; }
    
    public enum ArmorCategory {
        DRAGON, DUNGEON, SLAYER, FISHING, FARMING, MINING, COMBAT, SPECIAL
    }
    
    public enum ArmorRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.2),
        RARE("§9Rare", 1.5),
        EPIC("§5Epic", 2.0),
        LEGENDARY("§6Legendary", 3.0),
        MYTHIC("§dMythic", 4.0),
        DIVINE("§bDivine", 5.0),
        SPECIAL("§cSpecial", 6.0),
        VERY_SPECIAL("§cVery Special", 8.0);
        
        private final String displayName;
        private final double multiplier;
        
        ArmorRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
}
