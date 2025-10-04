package de.noctivag.plugin.loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Conditional Probability Engine - Implements the multi-stage drop pipeline for loot system
 * 
 * Features:
 * - Multi-stage drop pipeline with proper modifier application order
 * - Looting Enchantment: Increases drop chance for all drops
 * - Magic Find: Increases drop chance only for rare items (<5% drop chance)
 * - Pet Luck: Increases drop chance only for non-boss pet drops
 * - Drop Pool ID Constraint: Maximum 1 item per drop pool per mob kill
 * - Economy throttle mechanism to prevent market flooding
 */
public class ConditionalProbabilityEngine {
    
    // Modifier types and their application order
    private static final List<ModifierType> MODIFIER_ORDER = Arrays.asList(
        ModifierType.LOOTING_ENCHANTMENT,
        ModifierType.MAGIC_FIND,
        ModifierType.PET_LUCK,
        ModifierType.LUCK_ENCHANTMENT
    );
    
    // Drop rate categories
    public enum DropRateCategory {
        GENERAL("General Drops", 5.0, Arrays.asList(ModifierType.LOOTING_ENCHANTMENT)),
        RARE("Rare Drops", 5.0, Arrays.asList(ModifierType.LOOTING_ENCHANTMENT, ModifierType.MAGIC_FIND, ModifierType.PET_LUCK, ModifierType.LUCK_ENCHANTMENT));
        
        private final String name;
        private final double threshold;
        private final List<ModifierType> applicableModifiers;
        
        DropRateCategory(String name, double threshold, List<ModifierType> applicableModifiers) {
            this.name = name;
            this.threshold = threshold;
            this.applicableModifiers = applicableModifiers;
        }
        
        public String getName() { return name; }
        public double getThreshold() { return threshold; }
        public List<ModifierType> getApplicableModifiers() { return applicableModifiers; }
        
        public static DropRateCategory getCategory(double baseChance) {
            return baseChance < 5.0 ? RARE : GENERAL;
        }
    }
    
    // Modifier types
    public enum ModifierType {
        LOOTING_ENCHANTMENT("Looting Enchantment", "Increases drop chance for all drops"),
        MAGIC_FIND("Magic Find", "Increases drop chance only for rare items (<5% drop chance)"),
        PET_LUCK("Pet Luck", "Increases drop chance only for non-boss pet drops"),
        LUCK_ENCHANTMENT("Luck Enchantment", "Increases drop chance for rare items");
        
        private final String name;
        private final String description;
        
        ModifierType(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    // Drop pool tracking for economy regulation
    private final Map<UUID, Set<String>> mobDropPools = new ConcurrentHashMap<>();
    
    /**
     * Process loot drop with conditional probability
     */
    public List<LootDrop> processLootDrop(Player player, UUID mobId, List<LootTableEntry> lootTable) {
        List<LootDrop> drops = new ArrayList<>();
        Set<String> usedDropPools = mobDropPools.computeIfAbsent(mobId, k -> new HashSet<>());
        
        // Process each loot table entry
        for (LootTableEntry entry : lootTable) {
            // Check drop pool constraint
            if (usedDropPools.contains(entry.getDropPoolId())) {
                continue; // Skip if already dropped from this pool
            }
            
            // Calculate final drop chance
            double finalChance = calculateFinalDropChance(player, entry);
            
            // Roll for drop
            if (Math.random() < finalChance) {
                LootDrop drop = new LootDrop(entry, finalChance);
                drops.add(drop);
                usedDropPools.add(entry.getDropPoolId());
            }
        }
        
        // Clean up mob tracking after processing
        mobDropPools.remove(mobId);
        
        return drops;
    }
    
    /**
     * Calculate final drop chance with all modifiers applied in correct order
     */
    private double calculateFinalDropChance(Player player, LootTableEntry entry) {
        double baseChance = entry.getBaseChance();
        double currentChance = baseChance;
        
        // Determine drop category
        DropRateCategory category = DropRateCategory.getCategory(baseChance);
        
        // Apply modifiers in correct order
        for (ModifierType modifierType : MODIFIER_ORDER) {
            // Check if modifier applies to this drop category
            if (!category.getApplicableModifiers().contains(modifierType)) {
                continue;
            }
            
            // Get modifier value from player
            double modifierValue = getModifierValue(player, modifierType, entry);
            
            // Apply modifier
            currentChance = applyModifier(currentChance, modifierValue, modifierType);
        }
        
        // Ensure chance doesn't exceed 100%
        return Math.min(1.0, currentChance);
    }
    
    /**
     * Get modifier value from player
     */
    private double getModifierValue(Player player, ModifierType modifierType, LootTableEntry entry) {
        return switch (modifierType) {
            case LOOTING_ENCHANTMENT -> getLootingEnchantmentBonus(player);
            case MAGIC_FIND -> getMagicFindBonus(player);
            case PET_LUCK -> getPetLuckBonus(player, entry);
            case LUCK_ENCHANTMENT -> getLuckEnchantmentBonus(player);
        };
    }
    
    /**
     * Get Looting Enchantment bonus
     */
    private double getLootingEnchantmentBonus(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.hasItemMeta() && mainHand.getItemMeta().hasEnchants()) {
            int lootingLevel = mainHand.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.LOOTING);
            return lootingLevel * 0.1; // 10% per level
        }
        return 0.0;
    }
    
    /**
     * Get Magic Find bonus
     */
    private double getMagicFindBonus(Player player) {
        // TODO: Implement magic find calculation based on player stats
        // This would typically come from armor, accessories, pets, etc.
        return 0.0; // Placeholder
    }
    
    /**
     * Get Pet Luck bonus
     */
    private double getPetLuckBonus(Player player, LootTableEntry entry) {
        // Only applies to non-boss pet drops
        if (!entry.isPetDrop() || entry.isBossDrop()) {
            return 0.0;
        }
        
        // TODO: Implement pet luck calculation based on active pet
        return 0.0; // Placeholder
    }
    
    /**
     * Get Luck Enchantment bonus
     */
    private double getLuckEnchantmentBonus(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.hasItemMeta() && mainHand.getItemMeta().hasEnchants()) {
            int luckLevel = mainHand.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.LUCK_OF_THE_SEA);
            return luckLevel * 0.05; // 5% per level
        }
        return 0.0;
    }
    
    /**
     * Apply modifier to drop chance
     */
    private double applyModifier(double currentChance, double modifierValue, ModifierType modifierType) {
        return switch (modifierType) {
            case LOOTING_ENCHANTMENT, MAGIC_FIND, PET_LUCK, LUCK_ENCHANTMENT -> {
                // Multiplicative bonus: chance * (1 + modifier)
                yield currentChance * (1.0 + modifierValue);
            }
        };
    }
    
    /**
     * Get modifier information for display
     */
    public List<ModifierInfo> getModifierInfo(Player player, LootTableEntry entry) {
        List<ModifierInfo> modifiers = new ArrayList<>();
        DropRateCategory category = DropRateCategory.getCategory(entry.getBaseChance());
        
        for (ModifierType modifierType : category.getApplicableModifiers()) {
            double value = getModifierValue(player, modifierType, entry);
            if (value > 0) {
                modifiers.add(new ModifierInfo(modifierType, value));
            }
        }
        
        return modifiers;
    }
    
    /**
     * Loot Table Entry class
     */
    public static class LootTableEntry {
        private final String itemId;
        private final String dropPoolId;
        private final double baseChance;
        private final ItemStack item;
        private final boolean isPetDrop;
        private final boolean isBossDrop;
        
        public LootTableEntry(String itemId, String dropPoolId, double baseChance, ItemStack item, 
                             boolean isPetDrop, boolean isBossDrop) {
            this.itemId = itemId;
            this.dropPoolId = dropPoolId;
            this.baseChance = baseChance;
            this.item = item;
            this.isPetDrop = isPetDrop;
            this.isBossDrop = isBossDrop;
        }
        
        public String getItemId() { return itemId; }
        public String getDropPoolId() { return dropPoolId; }
        public double getBaseChance() { return baseChance; }
        public ItemStack getItem() { return item; }
        public boolean isPetDrop() { return isPetDrop; }
        public boolean isBossDrop() { return isBossDrop; }
    }
    
    /**
     * Loot Drop result class
     */
    public static class LootDrop {
        private final LootTableEntry entry;
        private final double finalChance;
        private final long timestamp;
        
        public LootDrop(LootTableEntry entry, double finalChance) {
            this.entry = entry;
            this.finalChance = finalChance;
            this.timestamp = System.currentTimeMillis();
        }
        
        public LootTableEntry getEntry() { return entry; }
        public double getFinalChance() { return finalChance; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Modifier Info class for display
     */
    public static class ModifierInfo {
        private final ModifierType type;
        private final double value;
        
        public ModifierInfo(ModifierType type, double value) {
            this.type = type;
            this.value = value;
        }
        
        public ModifierType getType() { return type; }
        public double getValue() { return value; }
        
        public String getDisplayText() {
            return type.getName() + ": " + String.format("%.1f", value * 100) + "%";
        }
    }
    
    /**
     * Create example loot table for testing
     */
    public static List<LootTableEntry> createExampleLootTable() {
        List<LootTableEntry> lootTable = new ArrayList<>();
        
        // General drops (high chance)
        lootTable.add(new LootTableEntry("rotten_flesh", "general_drops", 0.8, 
            new ItemStack(org.bukkit.Material.ROTTEN_FLESH), false, false));
        
        // Rare drops (low chance)
        lootTable.add(new LootTableEntry("rare_sword", "rare_weapons", 0.02, 
            new ItemStack(org.bukkit.Material.IRON_SWORD), false, false));
        
        // Pet drops
        lootTable.add(new LootTableEntry("wolf_pet", "pet_drops", 0.001, 
            new ItemStack(org.bukkit.Material.WOLF_SPAWN_EGG), true, false));
        
        // Boss pet drops (not affected by pet luck)
        lootTable.add(new LootTableEntry("boss_pet", "boss_pet_drops", 0.0001, 
            new ItemStack(org.bukkit.Material.DRAGON_EGG), true, true));
        
        return lootTable;
    }
    
    /**
     * Get economy regulation status
     */
    public Map<String, Integer> getEconomyRegulationStatus() {
        Map<String, Integer> status = new HashMap<>();
        status.put("active_drop_pools", mobDropPools.size());
        status.put("total_constraints_applied", mobDropPools.values().stream()
            .mapToInt(Set::size).sum());
        return status;
    }
    
    /**
     * Clear all drop pool tracking (for cleanup)
     */
    public void clearDropPoolTracking() {
        mobDropPools.clear();
    }
}
