package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stat Modification System - Hypixel Skyblock Style
 * 
 * Features:
 * - Stat Types (Strength, Defense, Speed, Intelligence, etc.)
 * - Stat Modifiers and Bonuses
 * - Stat Calculations
 * - Stat Display
 * - Stat Integration with Items
 * - Stat Integration with Pets
 * - Stat Integration with Accessories
 */
public class StatModificationSystem {
    private final SkyblockPlugin plugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    private final Map<String, StatType> statTypes = new HashMap<>();
    private final Map<String, StatModifier> statModifiers = new HashMap<>();
    
    public StatModificationSystem(SkyblockPlugin plugin, CorePlatform corePlatform) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        initializeStatTypes();
        initializeStatModifiers();
    }
    
    private void initializeStatTypes() {
        // Combat Stats
        createStatType("STRENGTH", "Strength", "§c", "Increases melee damage", 0, 1000, 1.0);
        createStatType("DAMAGE", "Damage", "§c", "Increases all damage", 0, 1000, 1.0);
        createStatType("CRITICAL_CHANCE", "Critical Chance", "§9", "Chance to deal critical hits", 0, 100, 1.0);
        createStatType("CRITICAL_DAMAGE", "Critical Damage", "§9", "Multiplier for critical hits", 0, 1000, 1.0);
        createStatType("ATTACK_SPEED", "Attack Speed", "§e", "How fast you can attack", 0, 100, 1.0);
        
        // Defense Stats
        createStatType("DEFENSE", "Defense", "§a", "Reduces incoming damage", 0, 1000, 1.0);
        createStatType("HEALTH", "Health", "§c", "Maximum health points", 100, 2000, 1.0);
        createStatType("RESISTANCE", "Resistance", "§a", "Reduces damage from all sources", 0, 100, 1.0);
        createStatType("ABSORPTION", "Absorption", "§6", "Temporary health that absorbs damage", 0, 100, 1.0);
        
        // Movement Stats
        createStatType("SPEED", "Speed", "§f", "Movement speed", 0, 500, 1.0);
        createStatType("AGILITY", "Agility", "§f", "Dodge chance and movement", 0, 100, 1.0);
        createStatType("JUMP_HEIGHT", "Jump Height", "§f", "How high you can jump", 0, 100, 1.0);
        
        // Magic Stats
        createStatType("INTELLIGENCE", "Intelligence", "§b", "Increases mana and magic damage", 0, 1000, 1.0);
        createStatType("MANA", "Mana", "§b", "Maximum mana points", 100, 2000, 1.0);
        createStatType("MANA_REGENERATION", "Mana Regeneration", "§b", "How fast mana regenerates", 0, 100, 1.0);
        createStatType("MAGIC_FIND", "Magic Find", "§d", "Increases rare item drop chance", 0, 100, 1.0);
        
        // Utility Stats
        createStatType("LUCK", "Luck", "§6", "Increases all drop rates", 0, 100, 1.0);
        createStatType("FORTUNE", "Fortune", "§6", "Increases block drop rates", 0, 100, 1.0);
        createStatType("EFFICIENCY", "Efficiency", "§a", "Increases tool efficiency", 0, 100, 1.0);
        createStatType("EXPERIENCE", "Experience", "§e", "Increases XP gain", 0, 100, 1.0);
        
        // Special Stats
        createStatType("PET_LUCK", "Pet Luck", "§d", "Increases pet drop chance", 0, 100, 1.0);
        createStatType("SEA_CREATURE_CHANCE", "Sea Creature Chance", "§b", "Chance to catch sea creatures", 0, 100, 1.0);
        createStatType("FISHING_SPEED", "Fishing Speed", "§b", "How fast you can fish", 0, 100, 1.0);
        createStatType("MINING_SPEED", "Mining Speed", "§7", "How fast you can mine", 0, 100, 1.0);
        createStatType("FARMING_SPEED", "Farming Speed", "§a", "How fast you can farm", 0, 100, 1.0);
        createStatType("FORAGING_SPEED", "Foraging Speed", "§2", "How fast you can forage", 0, 100, 1.0);
    }
    
    private void initializeStatModifiers() {
        // Base modifiers
        createStatModifier("BASE_STRENGTH", "Base Strength", "STRENGTH", 0, 0, 1.0, false);
        createStatModifier("BASE_DEFENSE", "Base Defense", "DEFENSE", 0, 0, 1.0, false);
        createStatModifier("BASE_SPEED", "Base Speed", "SPEED", 0, 0, 1.0, false);
        createStatModifier("BASE_INTELLIGENCE", "Base Intelligence", "INTELLIGENCE", 0, 0, 1.0, false);
        
        // Item modifiers
        createStatModifier("ITEM_STRENGTH", "Item Strength", "STRENGTH", 0, 0, 1.0, true);
        createStatModifier("ITEM_DEFENSE", "Item Defense", "DEFENSE", 0, 0, 1.0, true);
        createStatModifier("ITEM_SPEED", "Item Speed", "SPEED", 0, 0, 1.0, true);
        createStatModifier("ITEM_INTELLIGENCE", "Item Intelligence", "INTELLIGENCE", 0, 0, 1.0, true);
        
        // Pet modifiers
        createStatModifier("PET_STRENGTH", "Pet Strength", "STRENGTH", 0, 0, 1.0, true);
        createStatModifier("PET_DEFENSE", "Pet Defense", "DEFENSE", 0, 0, 1.0, true);
        createStatModifier("PET_SPEED", "Pet Speed", "SPEED", 0, 0, 1.0, true);
        createStatModifier("PET_INTELLIGENCE", "Pet Intelligence", "INTELLIGENCE", 0, 0, 1.0, true);
        
        // Accessory modifiers
        createStatModifier("ACCESSORY_STRENGTH", "Accessory Strength", "STRENGTH", 0, 0, 1.0, true);
        createStatModifier("ACCESSORY_DEFENSE", "Accessory Defense", "DEFENSE", 0, 0, 1.0, true);
        createStatModifier("ACCESSORY_SPEED", "Accessory Speed", "SPEED", 0, 0, 1.0, true);
        createStatModifier("ACCESSORY_INTELLIGENCE", "Accessory Intelligence", "INTELLIGENCE", 0, 0, 1.0, true);
        
        // Reforge modifiers
        createStatModifier("REFORGE_STRENGTH", "Reforge Strength", "STRENGTH", 0, 0, 1.0, true);
        createStatModifier("REFORGE_DEFENSE", "Reforge Defense", "DEFENSE", 0, 0, 1.0, true);
        createStatModifier("REFORGE_SPEED", "Reforge Speed", "SPEED", 0, 0, 1.0, true);
        createStatModifier("REFORGE_INTELLIGENCE", "Reforge Intelligence", "INTELLIGENCE", 0, 0, 1.0, true);
        
        // Enchantment modifiers
        createStatModifier("ENCHANTMENT_STRENGTH", "Enchantment Strength", "STRENGTH", 0, 0, 1.0, true);
        createStatModifier("ENCHANTMENT_DEFENSE", "Enchantment Defense", "DEFENSE", 0, 0, 1.0, true);
        createStatModifier("ENCHANTMENT_SPEED", "Enchantment Speed", "SPEED", 0, 0, 1.0, true);
        createStatModifier("ENCHANTMENT_INTELLIGENCE", "Enchantment Intelligence", "INTELLIGENCE", 0, 0, 1.0, true);
    }
    
    private void createStatType(String id, String name, String color, String description, 
                              double baseValue, double maxValue, double multiplier) {
        StatType statType = new StatType(id, name, color, description, baseValue, maxValue, multiplier);
        statTypes.put(id, statType);
    }
    
    private void createStatModifier(String id, String name, String statType, 
                                  double flatBonus, double percentageBonus, double multiplier, boolean isAdditive) {
        StatModifier modifier = new StatModifier(id, name, statType, flatBonus, percentageBonus, multiplier, isAdditive);
        statModifiers.put(id, modifier);
    }
    
    public void updatePlayerStats(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerStats stats = getPlayerStats(playerId);
        
        // Reset all stats to base values
        stats.resetToBase();
        
        // Apply item stats
        applyItemStats(player, stats);
        
        // Apply pet stats
        applyPetStats(player, stats);
        
        // Apply accessory stats
        applyAccessoryStats(player, stats);
        
        // Apply reforge stats
        applyReforgeStats(player, stats);
        
        // Apply enchantment stats
        applyEnchantmentStats(player, stats);
        
        // Calculate final stats
        stats.calculateFinalStats();
        
        // Update player profile
        PlayerProfile profile = corePlatform.getPlayerProfile(playerId);
        if (profile != null) {
            profile.updateStats(stats.getFinalStats());
        }
    }
    
    private void applyItemStats(Player player, PlayerStats stats) {
        // Apply stats from equipped items
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.hasItemMeta()) {
                applyItemStatModifiers(item, stats, "ITEM_");
            }
        }
        
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand != null && mainHand.hasItemMeta()) {
            applyItemStatModifiers(mainHand, stats, "ITEM_");
        }
    }
    
    private void applyPetStats(Player player, PlayerStats stats) {
        // This would integrate with the PetSystem
        // For now, we'll just apply some example pet stats
        // In a real implementation, this would get the active pet and apply its stats
    }
    
    private void applyAccessoryStats(Player player, PlayerStats stats) {
        // This would integrate with the AccessorySystem
        // For now, we'll just apply some example accessory stats
        // In a real implementation, this would get equipped accessories and apply their stats
    }
    
    private void applyReforgeStats(Player player, PlayerStats stats) {
        // This would integrate with the ReforgeSystem
        // For now, we'll just apply some example reforge stats
        // In a real implementation, this would get reforge effects and apply them
    }
    
    private void applyEnchantmentStats(Player player, PlayerStats stats) {
        // This would integrate with the EnchantingSystem
        // For now, we'll just apply some example enchantment stats
        // In a real implementation, this would get enchantment effects and apply them
    }
    
    private void applyItemStatModifiers(ItemStack item, PlayerStats stats, String prefix) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;
        
        List<Component> lore = meta.lore();
        if (lore == null) return;
        
        for (Component line : lore) {
            String lineStr = line.toString();
            
            // Parse stat lines (e.g., "§7+10 Strength", "§7+5% Critical Chance")
            if (lineStr.contains("Strength")) {
                double value = parseStatValue(lineStr);
                stats.addStatModifier(prefix + "STRENGTH", value, 0, 1.0);
            } else if (lineStr.contains("Defense")) {
                double value = parseStatValue(lineStr);
                stats.addStatModifier(prefix + "DEFENSE", value, 0, 1.0);
            } else if (lineStr.contains("Speed")) {
                double value = parseStatValue(lineStr);
                stats.addStatModifier(prefix + "SPEED", value, 0, 1.0);
            } else if (lineStr.contains("Intelligence")) {
                double value = parseStatValue(lineStr);
                stats.addStatModifier(prefix + "INTELLIGENCE", value, 0, 1.0);
            }
        }
    }
    
    private double parseStatValue(String line) {
        // Parse stat value from lore line
        // This is a simplified parser - in a real implementation, you'd want more robust parsing
        if (line.contains("+")) {
            String[] parts = line.split("\\+");
            if (parts.length > 1) {
                String valueStr = parts[1].split(" ")[0];
                try {
                    return Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }
    
    public PlayerStats getPlayerStats(UUID playerId) {
        return playerStats.computeIfAbsent(playerId, k -> new PlayerStats(playerId));
    }
    
    public Map<String, StatType> getAllStatTypes() {
        return new HashMap<>(statTypes);
    }
    
    public Map<String, StatModifier> getAllStatModifiers() {
        return new HashMap<>(statModifiers);
    }
    
    // Stat Type Class
    public static class StatType {
        private final String id;
        private final String name;
        private final String color;
        private final String description;
        private final double baseValue;
        private final double maxValue;
        private final double multiplier;
        
        public StatType(String id, String name, String color, String description, 
                       double baseValue, double maxValue, double multiplier) {
            this.id = id;
            this.name = name;
            this.color = color;
            this.description = description;
            this.baseValue = baseValue;
            this.maxValue = maxValue;
            this.multiplier = multiplier;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getColor() { return color; }
        public String getDescription() { return description; }
        public double getBaseValue() { return baseValue; }
        public double getMaxValue() { return maxValue; }
        public double getMultiplier() { return multiplier; }
    }
    
    // Stat Modifier Class
    public static class StatModifier {
        private final String id;
        private final String name;
        private final String statType;
        private final double flatBonus;
        private final double percentageBonus;
        private final double multiplier;
        private final boolean isAdditive;
        
        public StatModifier(String id, String name, String statType, double flatBonus, 
                           double percentageBonus, double multiplier, boolean isAdditive) {
            this.id = id;
            this.name = name;
            this.statType = statType;
            this.flatBonus = flatBonus;
            this.percentageBonus = percentageBonus;
            this.multiplier = multiplier;
            this.isAdditive = isAdditive;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getStatType() { return statType; }
        public double getFlatBonus() { return flatBonus; }
        public double getPercentageBonus() { return percentageBonus; }
        public double getMultiplier() { return multiplier; }
        public boolean isAdditive() { return isAdditive; }
    }
    
    // Player Stats Class
    public static class PlayerStats {
        private final UUID playerId;
        private final Map<String, Double> baseStats = new HashMap<>();
        private final Map<String, Double> finalStats = new HashMap<>();
        private final Map<String, List<StatModifier>> statModifiers = new HashMap<>();
        
        public PlayerStats(UUID playerId) {
            this.playerId = playerId;
            initializeBaseStats();
        }
        
        private void initializeBaseStats() {
            // Initialize base stats
            baseStats.put("STRENGTH", 0.0);
            baseStats.put("DEFENSE", 0.0);
            baseStats.put("SPEED", 0.0);
            baseStats.put("INTELLIGENCE", 0.0);
            baseStats.put("HEALTH", 100.0);
            baseStats.put("MANA", 100.0);
            baseStats.put("CRITICAL_CHANCE", 0.0);
            baseStats.put("CRITICAL_DAMAGE", 0.0);
            baseStats.put("LUCK", 0.0);
            baseStats.put("FORTUNE", 0.0);
        }
        
        public void resetToBase() {
            finalStats.clear();
            finalStats.putAll(baseStats);
            statModifiers.clear();
        }
        
        public void addStatModifier(String modifierId, double flatBonus, double percentageBonus, double multiplier) {
            StatModifier modifier = new StatModifier(modifierId, modifierId, "UNKNOWN", flatBonus, percentageBonus, multiplier, true);
            
            // Determine which stat this modifier affects
            String statType = modifierId.replace("ITEM_", "").replace("PET_", "").replace("ACCESSORY_", "").replace("REFORGE_", "").replace("ENCHANTMENT_", "");
            
            statModifiers.computeIfAbsent(statType, k -> new ArrayList<>()).add(modifier);
        }
        
        public void calculateFinalStats() {
            for (Map.Entry<String, Double> entry : baseStats.entrySet()) {
                String statType = entry.getKey();
                double baseValue = entry.getValue();
                
                // Apply modifiers
                List<StatModifier> modifiers = statModifiers.get(statType);
                if (modifiers != null) {
                    double flatBonus = 0.0;
                    double percentageBonus = 0.0;
                    double multiplier = 1.0;
                    
                    for (StatModifier modifier : modifiers) {
                        flatBonus += modifier.getFlatBonus();
                        percentageBonus += modifier.getPercentageBonus();
                        multiplier *= modifier.getMultiplier();
                    }
                    
                    // Calculate final value
                    double finalValue = (baseValue + flatBonus) * (1.0 + percentageBonus / 100.0) * multiplier;
                    finalStats.put(statType, finalValue);
                } else {
                    finalStats.put(statType, baseValue);
                }
            }
        }
        
        public double getStat(String statType) {
            return finalStats.getOrDefault(statType, 0.0);
        }
        
        public Map<String, Double> getFinalStats() {
            return new HashMap<>(finalStats);
        }
        
        public UUID getPlayerId() { return playerId; }
    }
}
