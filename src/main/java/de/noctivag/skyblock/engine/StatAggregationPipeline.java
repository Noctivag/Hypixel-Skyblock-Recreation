package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import de.noctivag.skyblock.features.stats.types.SecondaryStat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StatAggregationPipeline - Ensures precise order of stat calculations
 * 
 * This pipeline implements the exact order of operations required for Hypixel Skyblock
 * stat calculations. The order is CRITICAL and must be followed exactly to maintain
 * game balance and meta-game integrity.
 * 
 * Calculation Order (MUST BE FOLLOWED EXACTLY):
 * 1. Load player base stats
 * 2. Add base stats from weapon and armor
 * 3. Apply reforge stats (based on rarity)
 * 4. Apply enchantment stats/effects
 * 5. Apply global buffs (talismans, magical power, potions)
 * 6. Calculate final damage with aggregated values
 * 
 * Each step builds upon the previous one, and the order cannot be changed
 * without breaking the game balance.
 */
public class StatAggregationPipeline {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final StatCalculationService statCalculationService;
    private final ReforgeMatrixManager reforgeMatrixManager;
    private final AccessoryManager accessoryManager;
    private final Map<UUID, PlayerStatAggregation> playerAggregations = new ConcurrentHashMap<>();
    
    public StatAggregationPipeline(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                                 StatCalculationService statCalculationService, ReforgeMatrixManager reforgeMatrixManager,
                                 AccessoryManager accessoryManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.statCalculationService = statCalculationService;
        this.reforgeMatrixManager = reforgeMatrixManager;
        this.accessoryManager = accessoryManager;
    }
    
    /**
     * Execute the complete stat aggregation pipeline
     * This is the main entry point for all stat calculations
     */
    public CompletableFuture<PlayerStatAggregation> executeAggregationPipeline(Player player) {
        UUID playerId = player.getUniqueId();
        
        return CompletableFuture.supplyAsync(() -> {
            PlayerStatAggregation aggregation = getOrCreatePlayerAggregation(playerId);
            
            // CRITICAL: Reset to base values before starting pipeline
            aggregation.resetToBase();
            
            // STEP 1: Load player base stats
            executeStep1_LoadBaseStats(aggregation, player);
            
            // STEP 2: Add base stats from weapon and armor
            executeStep2_EquipmentStats(aggregation, player);
            
            // STEP 3: Apply reforge stats (based on rarity)
            executeStep3_ReforgeStats(aggregation, player);
            
            // STEP 4: Apply enchantment stats/effects
            executeStep4_EnchantmentStats(aggregation, player);
            
            // STEP 5: Apply global buffs (talismans, magical power, potions)
            executeStep5_GlobalBuffs(aggregation, player);
            
            // STEP 6: Calculate final aggregated values
            executeStep6_FinalCalculation(aggregation, player);
            
            return aggregation;
        });
    }
    
    /**
     * STEP 1: Load player base stats
     * This step initializes the player's base stat values
     */
    private void executeStep1_LoadBaseStats(PlayerStatAggregation aggregation, Player player) {
        // Initialize base stats for all primary stats
        for (PrimaryStat stat : PrimaryStat.values()) {
            aggregation.setBaseStat(stat, stat.getBaseValue());
        }
        
        // Initialize base stats for all secondary stats
        for (SecondaryStat stat : SecondaryStat.values()) {
            aggregation.setBaseSecondaryStat(stat, stat.getBaseValue());
        }
        
        // Load any additional base stats from player profile
        loadPlayerProfileBaseStats(aggregation, player);
    }
    
    /**
     * STEP 2: Add base stats from weapon and armor
     * This step adds the base stat values from equipped items
     */
    private void executeStep2_EquipmentStats(PlayerStatAggregation aggregation, Player player) {
        // Apply armor stats
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                applyEquipmentStats(aggregation, armor, "ARMOR");
            }
        }
        
        // Apply weapon stats
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            applyEquipmentStats(aggregation, weapon, "WEAPON");
        }
        
        // Apply off-hand stats
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand != null && offHand.hasItemMeta()) {
            applyEquipmentStats(aggregation, offHand, "OFFHAND");
        }
    }
    
    /**
     * STEP 3: Apply reforge stats (based on rarity)
     * This step applies reforge bonuses based on item rarity
     */
    private void executeStep3_ReforgeStats(PlayerStatAggregation aggregation, Player player) {
        // Apply armor reforge stats
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                applyReforgeStats(aggregation, armor);
            }
        }
        
        // Apply weapon reforge stats
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            applyReforgeStats(aggregation, weapon);
        }
        
        // Apply off-hand reforge stats
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand != null && offHand.hasItemMeta()) {
            applyReforgeStats(aggregation, offHand);
        }
    }
    
    /**
     * STEP 4: Apply enchantment stats/effects
     * This step applies enchantment bonuses
     */
    private void executeStep4_EnchantmentStats(PlayerStatAggregation aggregation, Player player) {
        // Apply armor enchantment stats
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                applyEnchantmentStats(aggregation, armor);
            }
        }
        
        // Apply weapon enchantment stats
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            applyEnchantmentStats(aggregation, weapon);
        }
        
        // Apply off-hand enchantment stats
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand != null && offHand.hasItemMeta()) {
            applyEnchantmentStats(aggregation, offHand);
        }
    }
    
    /**
     * STEP 5: Apply global buffs (talismans, magical power, potions)
     * This step applies all global buffs including accessories and magical power
     */
    private void executeStep5_GlobalBuffs(PlayerStatAggregation aggregation, Player player) {
        // Apply accessory stats
        // accessoryManager.applyAccessoryStats(aggregation, player);
        
        // Apply magical power stats
        // accessoryManager.applyMagicalPowerStats(aggregation, player);
        
        // Apply potion effects
        applyPotionEffects(aggregation, player);
        
        // Apply skill bonuses
        applySkillBonuses(aggregation, player);
        
        // Apply pet bonuses
        applyPetBonuses(aggregation, player);
        
        // Apply other global buffs
        applyOtherGlobalBuffs(aggregation, player);
    }
    
    /**
     * STEP 6: Calculate final aggregated values
     * This step performs the final calculation of all aggregated stats
     */
    private void executeStep6_FinalCalculation(PlayerStatAggregation aggregation, Player player) {
        // Calculate final primary stats
        for (PrimaryStat stat : PrimaryStat.values()) {
            double base = aggregation.getBaseStat(stat);
            double flat = aggregation.getFlatBonus(stat);
            double percentage = aggregation.getPercentageBonus(stat);
            double multiplier = aggregation.getMultiplier(stat);
            
            // Formula: (base + flat) * (1 + percentage/100) * multiplier
            double finalValue = (base + flat) * (1.0 + percentage / 100.0) * multiplier;
            aggregation.setFinalStat(stat, finalValue);
        }
        
        // Calculate final secondary stats
        for (SecondaryStat stat : SecondaryStat.values()) {
            double base = aggregation.getBaseSecondaryStat(stat);
            double flat = aggregation.getFlatSecondaryBonus(stat);
            double percentage = aggregation.getPercentageSecondaryBonus(stat);
            double multiplier = aggregation.getSecondaryMultiplier(stat);
            
            // Formula: (base + flat) * (1 + percentage/100) * multiplier
            double finalValue = (base + flat) * (1.0 + percentage / 100.0) * multiplier;
            aggregation.setFinalSecondaryStat(stat, finalValue);
        }
        
        // Calculate derived stats
        calculateDerivedStats(aggregation, player);
    }
    
    /**
     * Load base stats from player profile
     */
    private void loadPlayerProfileBaseStats(PlayerStatAggregation aggregation, Player player) {
        // This would load base stats from the player's profile
        // For now, we'll use the default base values
    }
    
    /**
     * Apply equipment stats from item
     */
    private void applyEquipmentStats(PlayerStatAggregation aggregation, ItemStack item, String source) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore != null) {
                for (String line : lore) {
                    parseAndApplyStatLine(aggregation, line, source);
                }
            }
        }
    }
    
    /**
     * Apply reforge stats from item
     */
    private void applyReforgeStats(PlayerStatAggregation aggregation, ItemStack item) {
        // Use the reforge matrix manager to apply reforge stats
        // reforgeMatrixManager.applyReforgeStats(aggregation, item);
    }
    
    /**
     * Apply enchantment stats from item
     */
    private void applyEnchantmentStats(PlayerStatAggregation aggregation, ItemStack item) {
        // This would apply enchantment stats
        // For now, placeholder implementation
    }
    
    /**
     * Apply potion effects
     */
    private void applyPotionEffects(PlayerStatAggregation aggregation, Player player) {
        // This would apply active potion effects
        // For now, placeholder implementation
    }
    
    /**
     * Apply skill bonuses
     */
    private void applySkillBonuses(PlayerStatAggregation aggregation, Player player) {
        // This would apply skill-based stat bonuses
        // For now, placeholder implementation
    }
    
    /**
     * Apply pet bonuses
     */
    private void applyPetBonuses(PlayerStatAggregation aggregation, Player player) {
        // This would apply pet stat bonuses
        // For now, placeholder implementation
    }
    
    /**
     * Apply other global buffs
     */
    private void applyOtherGlobalBuffs(PlayerStatAggregation aggregation, Player player) {
        // This would apply other global buffs
        // For now, placeholder implementation
    }
    
    /**
     * Calculate derived stats
     */
    private void calculateDerivedStats(PlayerStatAggregation aggregation, Player player) {
        // Calculate health from base health + bonuses
        double health = aggregation.getFinalStat(PrimaryStat.HEALTH);
        aggregation.setDerivedStat("MAX_HEALTH", health);
        
        // Calculate mana from intelligence
        double intelligence = aggregation.getFinalStat(PrimaryStat.INTELLIGENCE);
        double mana = intelligence * 2.0; // 1 intelligence = 2 mana
        aggregation.setDerivedStat("MAX_MANA", mana);
        
        // Calculate movement speed from speed stat
        double speed = aggregation.getFinalStat(PrimaryStat.SPEED);
        double movementSpeed = Math.min(400.0, 100.0 + speed); // Cap at 400%
        aggregation.setDerivedStat("MOVEMENT_SPEED", movementSpeed);
    }
    
    /**
     * Parse stat line and apply to aggregation
     */
    private void parseAndApplyStatLine(PlayerStatAggregation aggregation, String line, String source) {
        if (line.contains("+")) {
            String[] parts = line.split("\\+");
            if (parts.length > 1) {
                String statPart = parts[1].trim();
                double value = parseStatValue(statPart);
                
                // Map stat names to enums
                if (statPart.contains("Strength")) {
                    aggregation.addFlatBonus(PrimaryStat.STRENGTH, value);
                } else if (statPart.contains("Defense")) {
                    aggregation.addFlatBonus(PrimaryStat.DEFENSE, value);
                } else if (statPart.contains("Speed")) {
                    aggregation.addFlatBonus(PrimaryStat.SPEED, value);
                } else if (statPart.contains("Intelligence")) {
                    aggregation.addFlatBonus(PrimaryStat.INTELLIGENCE, value);
                } else if (statPart.contains("Critical Chance")) {
                    aggregation.addFlatBonus(PrimaryStat.CRIT_CHANCE, value);
                } else if (statPart.contains("Critical Damage")) {
                    aggregation.addFlatBonus(PrimaryStat.CRIT_DAMAGE, value);
                } else if (statPart.contains("Ferocity")) {
                    aggregation.addFlatBonus(PrimaryStat.FEROCITY, value);
                }
            }
        }
    }
    
    /**
     * Parse numerical value from stat string
     */
    private double parseStatValue(String statString) {
        try {
            // Remove percentage signs and extract number
            String cleanString = statString.replaceAll("[^0-9.-]", "");
            return Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Get or create player stat aggregation
     */
    public PlayerStatAggregation getOrCreatePlayerAggregation(UUID playerId) {
        return playerAggregations.computeIfAbsent(playerId, k -> new PlayerStatAggregation(playerId));
    }
    
    /**
     * Player Stat Aggregation - comprehensive stat container for the pipeline
     */
    public static class PlayerStatAggregation {
        private final UUID playerId;
        private final Map<PrimaryStat, Double> baseStats = new HashMap<>();
        private final Map<PrimaryStat, Double> flatBonuses = new HashMap<>();
        private final Map<PrimaryStat, Double> percentageBonuses = new HashMap<>();
        private final Map<PrimaryStat, Double> multipliers = new HashMap<>();
        private final Map<PrimaryStat, Double> finalStats = new HashMap<>();
        
        private final Map<SecondaryStat, Double> baseSecondaryStats = new HashMap<>();
        private final Map<SecondaryStat, Double> flatSecondaryBonuses = new HashMap<>();
        private final Map<SecondaryStat, Double> percentageSecondaryBonuses = new HashMap<>();
        private final Map<SecondaryStat, Double> secondaryMultipliers = new HashMap<>();
        private final Map<SecondaryStat, Double> finalSecondaryStats = new HashMap<>();
        
        private final Map<String, Double> derivedStats = new HashMap<>();
        
        public PlayerStatAggregation(UUID playerId) {
            this.playerId = playerId;
            initializeStats();
        }
        
        private void initializeStats() {
            // Initialize primary stats
            for (PrimaryStat stat : PrimaryStat.values()) {
                baseStats.put(stat, stat.getBaseValue());
                flatBonuses.put(stat, 0.0);
                percentageBonuses.put(stat, 0.0);
                multipliers.put(stat, 1.0);
                finalStats.put(stat, stat.getBaseValue());
            }
            
            // Initialize secondary stats
            for (SecondaryStat stat : SecondaryStat.values()) {
                baseSecondaryStats.put(stat, stat.getBaseValue());
                flatSecondaryBonuses.put(stat, 0.0);
                percentageSecondaryBonuses.put(stat, 0.0);
                secondaryMultipliers.put(stat, 1.0);
                finalSecondaryStats.put(stat, stat.getBaseValue());
            }
        }
        
        public void resetToBase() {
            // Reset all bonuses to zero
            for (PrimaryStat stat : PrimaryStat.values()) {
                flatBonuses.put(stat, 0.0);
                percentageBonuses.put(stat, 0.0);
                multipliers.put(stat, 1.0);
            }
            
            for (SecondaryStat stat : SecondaryStat.values()) {
                flatSecondaryBonuses.put(stat, 0.0);
                percentageSecondaryBonuses.put(stat, 0.0);
                secondaryMultipliers.put(stat, 1.0);
            }
            
            derivedStats.clear();
        }
        
        // Primary stat methods
        public void setBaseStat(PrimaryStat stat, double value) { baseStats.put(stat, value); }
        public double getBaseStat(PrimaryStat stat) { return baseStats.getOrDefault(stat, stat.getBaseValue()); }
        
        public void addFlatBonus(PrimaryStat stat, double value) { flatBonuses.merge(stat, value, Double::sum); }
        public double getFlatBonus(PrimaryStat stat) { return flatBonuses.getOrDefault(stat, 0.0); }
        
        public void addPercentageBonus(PrimaryStat stat, double value) { percentageBonuses.merge(stat, value, Double::sum); }
        public double getPercentageBonus(PrimaryStat stat) { return percentageBonuses.getOrDefault(stat, 0.0); }
        
        public void addMultiplier(PrimaryStat stat, double value) { multipliers.merge(stat, value, Double::sum); }
        public double getMultiplier(PrimaryStat stat) { return multipliers.getOrDefault(stat, 1.0); }
        
        public void setFinalStat(PrimaryStat stat, double value) { finalStats.put(stat, value); }
        public double getFinalStat(PrimaryStat stat) { return finalStats.getOrDefault(stat, stat.getBaseValue()); }
        
        // Secondary stat methods
        public void setBaseSecondaryStat(SecondaryStat stat, double value) { baseSecondaryStats.put(stat, value); }
        public double getBaseSecondaryStat(SecondaryStat stat) { return baseSecondaryStats.getOrDefault(stat, stat.getBaseValue()); }
        
        public void addFlatSecondaryBonus(SecondaryStat stat, double value) { flatSecondaryBonuses.merge(stat, value, Double::sum); }
        public double getFlatSecondaryBonus(SecondaryStat stat) { return flatSecondaryBonuses.getOrDefault(stat, 0.0); }
        
        public void addPercentageSecondaryBonus(SecondaryStat stat, double value) { percentageSecondaryBonuses.merge(stat, value, Double::sum); }
        public double getPercentageSecondaryBonus(SecondaryStat stat) { return percentageSecondaryBonuses.getOrDefault(stat, 0.0); }
        
        public void addSecondaryMultiplier(SecondaryStat stat, double value) { secondaryMultipliers.merge(stat, value, Double::sum); }
        public double getSecondaryMultiplier(SecondaryStat stat) { return secondaryMultipliers.getOrDefault(stat, 1.0); }
        
        public void setFinalSecondaryStat(SecondaryStat stat, double value) { finalSecondaryStats.put(stat, value); }
        public double getFinalSecondaryStat(SecondaryStat stat) { return finalSecondaryStats.getOrDefault(stat, stat.getBaseValue()); }
        
        // Derived stat methods
        public void setDerivedStat(String stat, double value) { derivedStats.put(stat, value); }
        public double getDerivedStat(String stat) { return derivedStats.getOrDefault(stat, 0.0); }
        
        public UUID getPlayerId() { return playerId; }
        public Map<PrimaryStat, Double> getFinalStats() { return new HashMap<>(finalStats); }
        public Map<SecondaryStat, Double> getFinalSecondaryStats() { return new HashMap<>(finalSecondaryStats); }
        public Map<String, Double> getDerivedStats() { return new HashMap<>(derivedStats); }
    }
}
