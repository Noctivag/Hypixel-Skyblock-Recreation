package de.noctivag.plugin.reforges;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * Reforge System - Hypixel SkyBlock Style Item Reforging
 * 
 * Features:
 * - Weapon reforges for damage and stats
 * - Armor reforges for defense and health
 * - Tool reforges for mining efficiency
 * - Accessory reforges for special bonuses
 * - Reforge stones and materials
 * - Reforge GUI and management
 */
public class ReforgeSystem {
    
    private final Plugin plugin;
    private final Map<String, Reforge> reforges;
    
    public ReforgeSystem(Plugin plugin) {
        this.plugin = plugin;
        this.reforges = new HashMap<>();
        initializeReforges();
    }
    
    private void initializeReforges() {
        // Weapon Reforges
        registerReforge(new Reforge(
            "Sharp", "Increases damage and strength",
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, 
                         Material.NETHERITE_SWORD, Material.DIAMOND_AXE, Material.IRON_AXE),
            Arrays.asList(
                new ReforgeStat("Damage", 0.05, 0.25),
                new ReforgeStat("Strength", 0.03, 0.15)
            ),
            ReforgeRarity.COMMON,
            1000
        ));
        
        registerReforge(new Reforge(
            "Spicy", "Increases critical chance and damage",
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, 
                         Material.NETHERITE_SWORD, Material.DIAMOND_AXE, Material.IRON_AXE),
            Arrays.asList(
                new ReforgeStat("Critical Chance", 0.02, 0.1),
                new ReforgeStat("Critical Damage", 0.03, 0.15)
            ),
            ReforgeRarity.UNCOMMON,
            2500
        ));
        
        registerReforge(new Reforge(
            "Fierce", "Increases strength and attack speed",
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, 
                         Material.NETHERITE_SWORD, Material.DIAMOND_AXE, Material.IRON_AXE),
            Arrays.asList(
                new ReforgeStat("Strength", 0.04, 0.2),
                new ReforgeStat("Attack Speed", 0.02, 0.1)
            ),
            ReforgeRarity.RARE,
            5000
        ));
        
        registerReforge(new Reforge(
            "Heroic", "Increases intelligence and ability damage",
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, 
                         Material.NETHERITE_SWORD, Material.DIAMOND_AXE, Material.IRON_AXE),
            Arrays.asList(
                new ReforgeStat("Intelligence", 0.06, 0.3),
                new ReforgeStat("Ability Damage", 0.04, 0.2)
            ),
            ReforgeRarity.EPIC,
            10000
        ));
        
        // Armor Reforges
        registerReforge(new Reforge(
            "Heavy", "Increases health and defense",
            Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
                         Material.NETHERITE_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
                         Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.DIAMOND_HELMET,
                         Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.NETHERITE_HELMET,
                         Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS),
            Arrays.asList(
                new ReforgeStat("Health", 0.08, 0.4),
                new ReforgeStat("Defense", 0.05, 0.25)
            ),
            ReforgeRarity.COMMON,
            800
        ));
        
        registerReforge(new Reforge(
            "Wise", "Increases intelligence and mana",
            Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
                         Material.NETHERITE_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
                         Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.DIAMOND_HELMET,
                         Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.NETHERITE_HELMET,
                         Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS),
            Arrays.asList(
                new ReforgeStat("Intelligence", 0.1, 0.5),
                new ReforgeStat("Mana", 0.06, 0.3)
            ),
            ReforgeRarity.UNCOMMON,
            2000
        ));
        
        registerReforge(new Reforge(
            "Pure", "Increases speed and critical chance",
            Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
                         Material.NETHERITE_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
                         Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.DIAMOND_HELMET,
                         Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.NETHERITE_HELMET,
                         Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS),
            Arrays.asList(
                new ReforgeStat("Speed", 0.04, 0.2),
                new ReforgeStat("Critical Chance", 0.03, 0.15)
            ),
            ReforgeRarity.RARE,
            4000
        ));
        
        registerReforge(new Reforge(
            "Necrotic", "Increases intelligence and ability damage",
            Arrays.asList(Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
                         Material.NETHERITE_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS,
                         Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.DIAMOND_HELMET,
                         Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.NETHERITE_HELMET,
                         Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS),
            Arrays.asList(
                new ReforgeStat("Intelligence", 0.12, 0.6),
                new ReforgeStat("Ability Damage", 0.05, 0.25)
            ),
            ReforgeRarity.EPIC,
            8000
        ));
        
        // Tool Reforges
        registerReforge(new Reforge(
            "Efficient", "Increases mining speed and fortune",
            Arrays.asList(Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE,
                         Material.NETHERITE_PICKAXE, Material.DIAMOND_AXE, Material.IRON_AXE,
                         Material.GOLDEN_AXE, Material.NETHERITE_AXE, Material.DIAMOND_SHOVEL,
                         Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.NETHERITE_SHOVEL),
            Arrays.asList(
                new ReforgeStat("Mining Speed", 0.06, 0.3),
                new ReforgeStat("Fortune", 0.03, 0.15)
            ),
            ReforgeRarity.COMMON,
            600
        ));
        
        registerReforge(new Reforge(
            "Lucky", "Increases fortune and magic find",
            Arrays.asList(Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE,
                         Material.NETHERITE_PICKAXE, Material.DIAMOND_AXE, Material.IRON_AXE,
                         Material.GOLDEN_AXE, Material.NETHERITE_AXE, Material.DIAMOND_SHOVEL,
                         Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.NETHERITE_SHOVEL),
            Arrays.asList(
                new ReforgeStat("Fortune", 0.05, 0.25),
                new ReforgeStat("Magic Find", 0.04, 0.2)
            ),
            ReforgeRarity.UNCOMMON,
            1500
        ));
        
        // Bow Reforges
        registerReforge(new Reforge(
            "Rapid", "Increases attack speed and critical chance",
            Arrays.asList(Material.BOW, Material.CROSSBOW),
            Arrays.asList(
                new ReforgeStat("Attack Speed", 0.08, 0.4),
                new ReforgeStat("Critical Chance", 0.04, 0.2)
            ),
            ReforgeRarity.COMMON,
            800
        ));
        
        registerReforge(new Reforge(
            "Unreal", "Increases damage and critical damage",
            Arrays.asList(Material.BOW, Material.CROSSBOW),
            Arrays.asList(
                new ReforgeStat("Damage", 0.06, 0.3),
                new ReforgeStat("Critical Damage", 0.05, 0.25)
            ),
            ReforgeRarity.UNCOMMON,
            2000
        ));
        
        // Special Reforges
        registerReforge(new Reforge(
            "Mythic", "Increases all stats",
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD,
                         Material.NETHERITE_SWORD, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE,
                         Material.GOLDEN_CHESTPLATE, Material.NETHERITE_CHESTPLATE),
            Arrays.asList(
                new ReforgeStat("All Stats", 0.03, 0.15)
            ),
            ReforgeRarity.LEGENDARY,
            25000
        ));
        
        registerReforge(new Reforge(
            "Ancient", "Increases intelligence and ability damage",
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD,
                         Material.NETHERITE_SWORD, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE,
                         Material.GOLDEN_CHESTPLATE, Material.NETHERITE_CHESTPLATE),
            Arrays.asList(
                new ReforgeStat("Intelligence", 0.15, 0.75),
                new ReforgeStat("Ability Damage", 0.08, 0.4)
            ),
            ReforgeRarity.LEGENDARY,
            30000
        ));
    }
    
    private void registerReforge(Reforge reforge) {
        reforges.put(reforge.getName().toLowerCase(), reforge);
    }
    
    public Reforge getReforge(String name) {
        return reforges.get(name.toLowerCase());
    }
    
    public Collection<Reforge> getAllReforges() {
        return reforges.values();
    }
    
    public Collection<Reforge> getReforgesForItem(Material material) {
        return reforges.values().stream()
            .filter(r -> r.getCompatibleMaterials().contains(material))
            .toList();
    }
    
    public Collection<Reforge> getReforgesByRarity(ReforgeRarity rarity) {
        return reforges.values().stream()
            .filter(r -> r.getRarity() == rarity)
            .toList();
    }
    
    public boolean canReforge(ItemStack item, Reforge reforge) {
        return reforge.getCompatibleMaterials().contains(item.getType());
    }
    
    public void applyReforge(ItemStack item, Reforge reforge) {
        if (!canReforge(item, reforge)) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(plugin, "reforge");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, reforge.getName());
            item.setItemMeta(meta);
        }
    }
    
    public Reforge getItemReforge(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;
        
        NamespacedKey key = new NamespacedKey(plugin, "reforge");
        String reforgeName = item.getItemMeta().getPersistentDataContainer()
            .getOrDefault(key, PersistentDataType.STRING, null);
        
        return reforgeName != null ? getReforge(reforgeName) : null;
    }
    
    public boolean hasReforge(ItemStack item) {
        return getItemReforge(item) != null;
    }
    
    public void removeReforge(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return;
        
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "reforge");
        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
    }
    
    public double calculateReforgeCost(Reforge reforge, ItemStack item) {
        double baseCost = reforge.getBaseCost();
        
        // Adjust cost based on item rarity/type
        Material material = item.getType();
        if (material.name().contains("NETHERITE")) {
            baseCost *= 2.0;
        } else if (material.name().contains("DIAMOND")) {
            baseCost *= 1.5;
        } else if (material.name().contains("GOLD")) {
            baseCost *= 0.8;
        }
        
        return baseCost;
    }
    
    public Map<String, Double> calculateReforgeStats(Reforge reforge, ItemStack item) {
        Map<String, Double> stats = new HashMap<>();
        
        for (ReforgeStat stat : reforge.getStats()) {
            double value = stat.getBaseValue() + (stat.getBonusPerLevel() * getItemLevel(item));
            stats.put(stat.getName(), value);
        }
        
        return stats;
    }
    
    private int getItemLevel(ItemStack item) {
        // Simplified item level calculation
        // In a real implementation, this would check enchantments, rarity, etc.
        return 1;
    }
    
    public enum ReforgeRarity {
        COMMON("§fCommon"),
        UNCOMMON("§aUncommon"),
        RARE("§9Rare"),
        EPIC("§5Epic"),
        LEGENDARY("§6Legendary");
        
        private final String displayName;
        
        ReforgeRarity(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public static class Reforge {
        private final String name;
        private final String description;
        private final List<Material> compatibleMaterials;
        private final List<ReforgeStat> stats;
        private final ReforgeRarity rarity;
        private final double baseCost;
        
        public Reforge(String name, String description, List<Material> compatibleMaterials,
                      List<ReforgeStat> stats, ReforgeRarity rarity, double baseCost) {
            this.name = name;
            this.description = description;
            this.compatibleMaterials = compatibleMaterials;
            this.stats = stats;
            this.rarity = rarity;
            this.baseCost = baseCost;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<Material> getCompatibleMaterials() { return compatibleMaterials; }
        public List<ReforgeStat> getStats() { return stats; }
        public ReforgeRarity getRarity() { return rarity; }
        public double getBaseCost() { return baseCost; }
    }
    
    public static class ReforgeStat {
        private final String name;
        private final double baseValue;
        private final double bonusPerLevel;
        
        public ReforgeStat(String name, double baseValue, double bonusPerLevel) {
            this.name = name;
            this.baseValue = baseValue;
            this.bonusPerLevel = bonusPerLevel;
        }
        
        public String getName() { return name; }
        public double getBaseValue() { return baseValue; }
        public double getBonusPerLevel() { return bonusPerLevel; }
    }
}
