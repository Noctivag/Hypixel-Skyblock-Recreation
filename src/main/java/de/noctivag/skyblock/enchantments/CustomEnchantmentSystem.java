package de.noctivag.skyblock.enchantments;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * Custom Enchantment System - Hypixel SkyBlock Style Enchantments
 * 
 * Features:
 * - Custom enchantments with special effects
 * - Enchantment levels and rarity system
 * - Enchantment combinations and conflicts
 * - Enchantment books and application
 * - Enchantment GUI and management
 */
public class CustomEnchantmentSystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, CustomEnchantment> enchantments;
    
    public CustomEnchantmentSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.enchantments = new HashMap<>();
        initializeCustomEnchantments();
    }
    
    private void initializeCustomEnchantments() {
        // Combat Enchantments
        registerEnchantment(new CustomEnchantment(
            "Sharpness", "Increases melee damage", 
            EnchantmentTarget.WEAPON, 7, EnchantmentRarity.COMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Power", "Increases bow damage", 
            EnchantmentTarget.BOW, 7, EnchantmentRarity.COMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Protection", "Reduces all damage", 
            EnchantmentTarget.ARMOR, 7, EnchantmentRarity.COMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Growth", "Increases maximum health", 
            EnchantmentTarget.ARMOR, 7, EnchantmentRarity.UNCOMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Giant Killer", "Deals more damage to high-health enemies", 
            EnchantmentTarget.WEAPON, 6, EnchantmentRarity.RARE
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Execute", "Deals more damage to low-health enemies", 
            EnchantmentTarget.WEAPON, 6, EnchantmentRarity.RARE
        ));
        
        registerEnchantment(new CustomEnchantment(
            "First Strike", "Deals extra damage on first hit", 
            EnchantmentTarget.WEAPON, 5, EnchantmentRarity.UNCOMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Triple-Strike", "Has a chance to hit 3 times", 
            EnchantmentTarget.WEAPON, 5, EnchantmentRarity.RARE
        ));
        
        // Mining Enchantments
        registerEnchantment(new CustomEnchantment(
            "Efficiency", "Increases mining speed", 
            EnchantmentTarget.TOOL, 10, EnchantmentRarity.COMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Fortune", "Increases drop rates", 
            EnchantmentTarget.TOOL, 4, EnchantmentRarity.UNCOMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Smelting Touch", "Automatically smelts mined ores", 
            EnchantmentTarget.TOOL, 1, EnchantmentRarity.RARE
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Experience", "Grants bonus mining XP", 
            EnchantmentTarget.TOOL, 4, EnchantmentRarity.UNCOMMON
        ));
        
        // Special Enchantments
        registerEnchantment(new CustomEnchantment(
            "Looting", "Increases mob drop rates", 
            EnchantmentTarget.WEAPON, 4, EnchantmentRarity.UNCOMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Luck", "Increases luck", 
            EnchantmentTarget.ARMOR, 6, EnchantmentRarity.RARE
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Scavenger", "Grants coins on mob kill", 
            EnchantmentTarget.WEAPON, 5, EnchantmentRarity.UNCOMMON
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Vampirism", "Heals on hit", 
            EnchantmentTarget.WEAPON, 6, EnchantmentRarity.RARE
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Life Steal", "Heals based on damage dealt", 
            EnchantmentTarget.WEAPON, 4, EnchantmentRarity.EPIC
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Syphon", "Heals based on critical hits", 
            EnchantmentTarget.WEAPON, 4, EnchantmentRarity.EPIC
        ));
        
        // Legendary Enchantments
        registerEnchantment(new CustomEnchantment(
            "Thunderlord", "Lightning strikes on hit", 
            EnchantmentTarget.WEAPON, 6, EnchantmentRarity.LEGENDARY
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Thorns", "Reflects damage back to attacker", 
            EnchantmentTarget.ARMOR, 4, EnchantmentRarity.LEGENDARY
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Fire Aspect", "Sets enemies on fire", 
            EnchantmentTarget.WEAPON, 3, EnchantmentRarity.LEGENDARY
        ));
        
        registerEnchantment(new CustomEnchantment(
            "Venomous", "Poisons enemies", 
            EnchantmentTarget.WEAPON, 6, EnchantmentRarity.LEGENDARY
        ));
    }
    
    private void registerEnchantment(CustomEnchantment enchantment) {
        enchantments.put(enchantment.getName().toLowerCase(), enchantment);
    }
    
    public CustomEnchantment getEnchantment(String name) {
        return enchantments.get(name.toLowerCase());
    }
    
    public Collection<CustomEnchantment> getAllEnchantments() {
        return enchantments.values();
    }
    
    public Collection<CustomEnchantment> getEnchantmentsByRarity(EnchantmentRarity rarity) {
        return enchantments.values().stream()
            .filter(e -> e.getRarity() == rarity)
            .toList();
    }
    
    public Collection<CustomEnchantment> getEnchantmentsByTarget(EnchantmentTarget target) {
        return enchantments.values().stream()
            .filter(e -> e.getTarget() == target)
            .toList();
    }
    
    public boolean canEnchant(ItemStack item, CustomEnchantment enchantment) {
        return enchantment.getTarget().includes(item);
    }
    
    public void applyEnchantment(ItemStack item, CustomEnchantment enchantment, int level) {
        if (!canEnchant(item, enchantment)) return;
        if (level > enchantment.getMaxLevel()) level = enchantment.getMaxLevel();
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(SkyblockPlugin, enchantment.getName().toLowerCase());
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
            item.setItemMeta(meta);
        }
    }
    
    public int getEnchantmentLevel(ItemStack item, CustomEnchantment enchantment) {
        if (item == null || item.getItemMeta() == null) return 0;
        
        NamespacedKey key = new NamespacedKey(SkyblockPlugin, enchantment.getName().toLowerCase());
        return item.getItemMeta().getPersistentDataContainer()
            .getOrDefault(key, PersistentDataType.INTEGER, 0);
    }
    
    public boolean hasEnchantment(ItemStack item, CustomEnchantment enchantment) {
        return getEnchantmentLevel(item, enchantment) > 0;
    }
    
    public Map<CustomEnchantment, Integer> getItemEnchantments(ItemStack item) {
        Map<CustomEnchantment, Integer> itemEnchantments = new HashMap<>();
        
        for (CustomEnchantment enchantment : enchantments.values()) {
            int level = getEnchantmentLevel(item, enchantment);
            if (level > 0) {
                itemEnchantments.put(enchantment, level);
            }
        }
        
        return itemEnchantments;
    }
    
    public double calculateEnchantmentCost(CustomEnchantment enchantment, int level) {
        double baseCost = switch (enchantment.getRarity()) {
            case COMMON -> 10.0;
            case UNCOMMON -> 25.0;
            case RARE -> 50.0;
            case EPIC -> 100.0;
            case LEGENDARY -> 200.0;
        };
        
        return baseCost * level * level;
    }
    
    public enum EnchantmentRarity {
        COMMON("§fCommon"),
        UNCOMMON("§aUncommon"), 
        RARE("§9Rare"),
        EPIC("§5Epic"),
        LEGENDARY("§6Legendary");
        
        private final String displayName;
        
        EnchantmentRarity(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public static class CustomEnchantment {
        private final String name;
        private final String description;
        private final EnchantmentTarget target;
        private final int maxLevel;
        private final EnchantmentRarity rarity;
        
        public CustomEnchantment(String name, String description, 
                               EnchantmentTarget target, int maxLevel, 
                               EnchantmentRarity rarity) {
            this.name = name;
            this.description = description;
            this.target = target;
            this.maxLevel = maxLevel;
            this.rarity = rarity;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public EnchantmentTarget getTarget() { return target; }
        public int getMaxLevel() { return maxLevel; }
        public EnchantmentRarity getRarity() { return rarity; }
    }
}
