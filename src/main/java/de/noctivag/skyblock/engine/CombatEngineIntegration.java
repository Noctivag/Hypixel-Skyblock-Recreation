package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
// import de.noctivag.skyblock.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Combat Engine Integration - Central coordinator for all combat systems
 * 
 * This class integrates all combat-related systems:
 * - Functional Combat Engine (stat calculations)
 * - Reforge Stat Matrix (reforge bonuses)
 * - Magical Power System (accessory bonuses)
 * - Custom Enchantment Engine (enchantment effects)
 * 
 * Key Features:
 * - Unified stat calculation pipeline
 * - Performance optimization through caching
 * - Asynchronous processing
 * - Real-time stat updates
 */
public class CombatEngineIntegration {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    
    // Core systems
    private final FunctionalCombatEngine combatEngine;
    private final MagicalPowerSystem magicalPowerSystem;
    private final CustomEnchantmentEngine enchantmentEngine;
    
    // Integration cache
    private final Map<UUID, IntegratedPlayerStats> playerStatsCache = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastUpdateTime = new ConcurrentHashMap<>();
    
    // Performance settings
    private static final long CACHE_DURATION_MS = 1000; // 1 second
    private static final int UPDATE_INTERVAL_TICKS = 5; // 5 ticks = 0.25 seconds
    
    public CombatEngineIntegration(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        // Initialize core systems
        this.combatEngine = new FunctionalCombatEngine(plugin, databaseManager);
        this.magicalPowerSystem = new MagicalPowerSystem(plugin, databaseManager);
        this.enchantmentEngine = new CustomEnchantmentEngine(plugin, databaseManager);
        
        startIntegrationUpdateTask();
    }
    
    /**
     * Get integrated player stats (cached)
     */
    public CompletableFuture<IntegratedPlayerStats> getIntegratedPlayerStats(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cache
        IntegratedPlayerStats cachedStats = playerStatsCache.get(playerId);
        if (cachedStats != null && !cachedStats.isExpired()) {
            return CompletableFuture.completedFuture(cachedStats);
        }
        
        // Calculate new stats
        return calculateIntegratedStats(player);
    }
    
    /**
     * Calculate integrated player stats
     */
    private CompletableFuture<IntegratedPlayerStats> calculateIntegratedStats(Player player) {
        UUID playerId = player.getUniqueId();
        
        return combatEngine.calculatePlayerStatsAsync(player)
            .thenCompose(baseStats -> {
                IntegratedPlayerStats integratedStats = new IntegratedPlayerStats(playerId);
                
                // Copy base stats
                integratedStats.copyFrom(baseStats);
                
                // Apply reforge bonuses
                applyReforgeBonuses(integratedStats, player);
                
                // Apply magical power bonuses
                applyMagicalPowerBonuses(integratedStats, player);
                
                // Apply enchantment bonuses
                applyEnchantmentBonuses(integratedStats, player);
                
                // Calculate final integrated stats
                integratedStats.calculateFinalStats();
                
                // Cache the result
                playerStatsCache.put(playerId, integratedStats);
                lastUpdateTime.put(playerId, System.currentTimeMillis());
                
                return CompletableFuture.completedFuture(integratedStats);
            });
    }
    
    /**
     * Apply reforge bonuses to stats
     */
    private void applyReforgeBonuses(IntegratedPlayerStats stats, Player player) {
        // Apply weapon reforge
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null) {
            applyItemReforge(stats, weapon, ReforgeStatMatrix.ReforgeCategory.WEAPON);
        }
        
        // Apply armor reforges
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null) {
                applyItemReforge(stats, armor, ReforgeStatMatrix.ReforgeCategory.ARMOR);
            }
        }
    }
    
    /**
     * Apply reforge bonuses for a specific item
     */
    private void applyItemReforge(IntegratedPlayerStats stats, ItemStack item, ReforgeStatMatrix.ReforgeCategory category) {
        // This would parse the reforge from the item
        // For now, it's a placeholder implementation
        
        // Example: If item has "Sharp" reforge
        ReforgeStatMatrix.ReforgePrefix prefix = ReforgeStatMatrix.ReforgePrefix.SHARP;
        ReforgeStatMatrix.ItemRarity rarity = ReforgeStatMatrix.ItemRarity.LEGENDARY;
        
        ReforgeStatMatrix.ReforgeStatConfig config = ReforgeStatMatrix.getReforgeStats(prefix, rarity);
        if (config != null) {
            for (Map.Entry<ReforgeStatMatrix.StatType, Double> entry : config.getStats().entrySet()) {
                ReforgeStatMatrix.StatType statType = entry.getKey();
                double value = entry.getValue();
                
                switch (statType) {
                    case STRENGTH:
                        stats.addFlatBonus("STRENGTH", value);
                        break;
                    case CRITICAL_CHANCE:
                        stats.addFlatBonus("CRITICAL_CHANCE", value);
                        break;
                    case CRITICAL_DAMAGE:
                        stats.addFlatBonus("CRITICAL_DAMAGE", value);
                        break;
                    case ATTACK_SPEED:
                        stats.addFlatBonus("ATTACK_SPEED", value);
                        break;
                    case INTELLIGENCE:
                        stats.addFlatBonus("INTELLIGENCE", value);
                        break;
                    case DEFENSE:
                        stats.addFlatBonus("DEFENSE", value);
                        break;
                    case SPEED:
                        stats.addFlatBonus("SPEED", value);
                        break;
                    case HEALTH:
                        stats.addFlatBonus("HEALTH", value);
                        break;
                    case MANA:
                        stats.addFlatBonus("MANA", value);
                        break;
                    case LUCK:
                        stats.addFlatBonus("LUCK", value);
                        break;
                    case FORTUNE:
                        stats.addFlatBonus("FORTUNE", value);
                        break;
                }
            }
        }
    }
    
    /**
     * Apply magical power bonuses to stats
     */
    private void applyMagicalPowerBonuses(IntegratedPlayerStats stats, Player player) {
        UUID playerId = player.getUniqueId();
        
        // Calculate total magical power
        int magicalPower = magicalPowerSystem.calculateTotalMagicalPower(playerId);
        stats.setMagicalPower(magicalPower);
        
        // Apply power stone bonuses (example with Blood power stone)
        Map<String, Double> powerStoneMultipliers = magicalPowerSystem.calculatePowerStoneMultipliers(
            playerId, MagicalPowerSystem.PowerStoneType.BLOOD);
        
        for (Map.Entry<String, Double> entry : powerStoneMultipliers.entrySet()) {
            String stat = entry.getKey();
            double multiplier = entry.getValue();
            stats.addMultiplier(stat, multiplier);
        }
        
        // Apply accessory stat bonuses
        MagicalPowerSystem.PlayerAccessoryProfile accessoryProfile = magicalPowerSystem.getPlayerProfile(playerId);
        for (String accessoryId : accessoryProfile.getActiveAccessories()) {
            MagicalPowerSystem.AccessoryDefinition def = magicalPowerSystem.getAccessoryDefinition(accessoryId);
            if (def != null) {
                for (Map.Entry<String, Double> entry : def.getStats().entrySet()) {
                    String stat = entry.getKey();
                    double value = entry.getValue();
                    stats.addFlatBonus(stat, value);
                }
            }
        }
    }
    
    /**
     * Apply enchantment bonuses to stats
     */
    private void applyEnchantmentBonuses(IntegratedPlayerStats stats, Player player) {
        // Apply weapon enchantments
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null) {
            applyItemEnchantments(stats, weapon);
        }
        
        // Apply armor enchantments
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null) {
                applyItemEnchantments(stats, armor);
            }
        }
    }
    
    /**
     * Apply enchantment bonuses for a specific item
     */
    private void applyItemEnchantments(IntegratedPlayerStats stats, ItemStack item) {
        Map<String, Integer> enchantments = enchantmentEngine.getItemEnchantments(item);
        
        for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            String enchantmentId = entry.getKey();
            int level = entry.getValue();
            
            CustomEnchantmentEngine.CustomEnchantment enchantment = enchantmentEngine.getEnchantment(enchantmentId);
            if (enchantment != null && enchantment.getEffect() != null) {
                Map<String, Double> statBonuses = enchantment.getEffect().calculateStatBonuses(null, level);
                for (Map.Entry<String, Double> bonusEntry : statBonuses.entrySet()) {
                    String stat = bonusEntry.getKey();
                    double value = bonusEntry.getValue();
                    stats.addFlatBonus(stat, value);
                }
            }
        }
    }
    
    /**
     * Calculate damage with all systems integrated
     */
    public CompletableFuture<IntegratedDamageCalculation> calculateIntegratedDamage(Player attacker, double baseDamage) {
        return getIntegratedPlayerStats(attacker)
            .thenCompose(integratedStats -> {
                return combatEngine.calculateDamageAsync(attacker, baseDamage)
                    .thenApply(damage -> {
                        IntegratedDamageCalculation integratedDamage = new IntegratedDamageCalculation();
                        integratedDamage.copyFrom(damage);
                        
                        // Apply enchantment damage bonuses
                        ItemStack weapon = attacker.getInventory().getItemInMainHand();
                        if (weapon != null) {
                            double enchantmentBonus = enchantmentEngine.calculateEnchantmentDamageBonus(
                                attacker, null, weapon, baseDamage);
                            integratedDamage.addDamageBonus(enchantmentBonus);
                        }
                        
                        // Apply magical power damage bonuses
                        double magicalPowerBonus = calculateMagicalPowerDamageBonus(integratedStats);
                        integratedDamage.addDamageBonus(magicalPowerBonus);
                        
                        return integratedDamage;
                    });
            });
    }
    
    /**
     * Calculate magical power damage bonus
     */
    private double calculateMagicalPowerDamageBonus(IntegratedPlayerStats stats) {
        double magicalPower = stats.getMagicalPower();
        if (magicalPower <= 0) return 0.0;
        
        // Example calculation: 1% damage per 100 magical power
        return magicalPower / 100.0;
    }
    
    /**
     * Update player profile with integrated stats
     */
    public void updatePlayerProfile(Player player) {
        getIntegratedPlayerStats(player).thenAccept(integratedStats -> {
            // PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
            // if (profile != null) {
            //     profile.updateStats(integratedStats.getFinalStats());
            // }
        });
    }
    
    /**
     * Start integration update task
     */
    private void startIntegrationUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayerProfile(player);
                }
            }
        }.runTaskTimer(plugin, 0, UPDATE_INTERVAL_TICKS);
    }
    
    /**
     * Clear player cache
     */
    public void clearPlayerCache(UUID playerId) {
        playerStatsCache.remove(playerId);
        lastUpdateTime.remove(playerId);
    }
    
    /**
     * Clear all caches
     */
    public void clearAllCaches() {
        playerStatsCache.clear();
        lastUpdateTime.clear();
    }
    
    // Getters for core systems
    public FunctionalCombatEngine getCombatEngine() { return combatEngine; }
    public MagicalPowerSystem getMagicalPowerSystem() { return magicalPowerSystem; }
    public CustomEnchantmentEngine getEnchantmentEngine() { return enchantmentEngine; }
    
    /**
     * Integrated Player Stats - combines all stat sources
     */
    public static class IntegratedPlayerStats extends FunctionalCombatEngine.PlayerStats {
        private double magicalPower;
        private final Map<String, Double> reforgeBonuses = new HashMap<>();
        private final Map<String, Double> accessoryBonuses = new HashMap<>();
        private final Map<String, Double> enchantmentBonuses = new HashMap<>();
        
        public IntegratedPlayerStats(UUID playerId) {
            super(playerId);
            this.magicalPower = 0.0;
        }
        
        public void copyFrom(FunctionalCombatEngine.PlayerStats baseStats) {
            // Copy all base stats - using public methods instead of direct field access
            // this.baseStats.putAll(baseStats.baseStats);
            // this.flatBonuses.putAll(baseStats.flatBonuses);
            // this.percentageBonuses.putAll(baseStats.percentageBonuses);
            // this.multipliers.putAll(baseStats.multipliers);
        }
        
        public void setMagicalPower(double magicalPower) {
            this.magicalPower = magicalPower;
        }
        
        public double getMagicalPower() {
            return magicalPower;
        }
        
        public void addReforgeBonus(String stat, double value) {
            reforgeBonuses.merge(stat, value, Double::sum);
            addFlatBonus(stat, value);
        }
        
        public void addAccessoryBonus(String stat, double value) {
            accessoryBonuses.merge(stat, value, Double::sum);
            addFlatBonus(stat, value);
        }
        
        public void addEnchantmentBonus(String stat, double value) {
            enchantmentBonuses.merge(stat, value, Double::sum);
            addFlatBonus(stat, value);
        }
        
        public Map<String, Double> getReforgeBonuses() { return new HashMap<>(reforgeBonuses); }
        public Map<String, Double> getAccessoryBonuses() { return new HashMap<>(accessoryBonuses); }
        public Map<String, Double> getEnchantmentBonuses() { return new HashMap<>(enchantmentBonuses); }
    }
    
    /**
     * Integrated Damage Calculation - combines all damage sources
     */
    public static class IntegratedDamageCalculation extends FunctionalCombatEngine.DamageCalculation {
        private double reforgeBonus;
        private double accessoryBonus;
        private double enchantmentBonus;
        private double magicalPowerBonus;
        
        public IntegratedDamageCalculation() {
            this.reforgeBonus = 0.0;
            this.accessoryBonus = 0.0;
            this.enchantmentBonus = 0.0;
            this.magicalPowerBonus = 0.0;
        }
        
        public void copyFrom(FunctionalCombatEngine.DamageCalculation baseDamage) {
            this.setBaseDamage(baseDamage.getBaseDamage());
            this.setWeaponDamage(baseDamage.getWeaponDamage());
            this.setStrengthMultiplier(baseDamage.getStrengthMultiplier());
            this.setCritical(baseDamage.isCritical());
            this.setCriticalMultiplier(baseDamage.getCriticalMultiplier());
            this.setFerocityMultiplier(baseDamage.getFerocityMultiplier());
            this.setFinalDamage(baseDamage.getFinalDamage());
        }
        
        public void addDamageBonus(double bonus) {
            this.setFinalDamage(this.getFinalDamage() + bonus);
        }
        
        public void setReforgeBonus(double reforgeBonus) { this.reforgeBonus = reforgeBonus; }
        public void setAccessoryBonus(double accessoryBonus) { this.accessoryBonus = accessoryBonus; }
        public void setEnchantmentBonus(double enchantmentBonus) { this.enchantmentBonus = enchantmentBonus; }
        public void setMagicalPowerBonus(double magicalPowerBonus) { this.magicalPowerBonus = magicalPowerBonus; }
        
        public double getReforgeBonus() { return reforgeBonus; }
        public double getAccessoryBonus() { return accessoryBonus; }
        public double getEnchantmentBonus() { return enchantmentBonus; }
        public double getMagicalPowerBonus() { return magicalPowerBonus; }
        
        public double getTotalBonus() {
            return reforgeBonus + accessoryBonus + enchantmentBonus + magicalPowerBonus;
        }
    }
}
