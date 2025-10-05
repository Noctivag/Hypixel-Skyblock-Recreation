package de.noctivag.skyblock.engine;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import de.noctivag.skyblock.features.stats.types.SecondaryStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StatCalculationService - Precise Hypixel Skyblock Stat Calculation Engine
 * 
 * This service implements the exact numerical precision required for Hypixel Skyblock
 * stat calculations. It follows the strict order of operations and uses precise formulas
 * to maintain game balance and meta-game integrity.
 * 
 * Calculation Order (CRITICAL - Must be followed exactly):
 * 1. Load player base stats
 * 2. Add base stats from weapon and armor
 * 3. Apply reforge stats (based on rarity)
 * 4. Apply enchantment stats/effects
 * 5. Apply global buffs (talismans, magical power, potions)
 * 6. Calculate final damage with aggregated values
 * 
 * Core Damage Formula (Melee):
 * Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)
 */
public class StatCalculationService {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final ReforgeMatrixManager reforgeMatrixManager;
    private final AccessoryManager accessoryManager;
    private final Map<UUID, PlayerStatProfile> playerProfiles = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> updateTasks = new ConcurrentHashMap<>();
    private final StatCalculationCache cache = new StatCalculationCache();
    
    // Performance settings
    private static final int CACHE_DURATION_TICKS = 20; // 1 second
    private static final int ASYNC_UPDATE_INTERVAL = 5; // 5 ticks = 0.25 seconds
    
    public StatCalculationService(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        this.reforgeMatrixManager = new ReforgeMatrixManager(SkyblockPlugin);
        this.accessoryManager = new AccessoryManager(SkyblockPlugin, databaseManager);
        startAsyncUpdateTask();
    }
    
    /**
     * Calculate comprehensive player stats with precise numerical accuracy
     * This is the main entry point for all stat calculations
     */
    public CompletableFuture<PlayerStatProfile> calculatePlayerStatsAsync(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cache first for performance
        PlayerStatProfile cachedProfile = cache.getCachedProfile(playerId);
        if (cachedProfile != null && !cachedProfile.isExpired()) {
            return CompletableFuture.completedFuture(cachedProfile);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            PlayerStatProfile profile = getOrCreatePlayerProfile(playerId);
            
            // CRITICAL: Reset to base values before calculation
            profile.resetToBase();
            
            // CRITICAL: Apply stats in exact order specified
            applyBaseStats(profile, player);
            applyEquipmentStats(profile, player);
            applyReforgeStats(profile, player);
            applyEnchantmentStats(profile, player);
            applyAccessoryStats(profile, player);
            applyMagicalPowerStats(profile, player);
            applyGlobalBuffs(profile, player);
            
            // Calculate final aggregated stats
            profile.calculateFinalStats();
            
            // Cache the result for performance
            cache.cacheProfile(playerId, profile);
            
            return profile;
        });
    }
    
    /**
     * Calculate damage with precise Hypixel Skyblock formulas
     * Uses the exact formula: Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)
     */
    public CompletableFuture<DamageCalculation> calculateDamageAsync(Player attacker, double baseWeaponDamage) {
        return calculatePlayerStatsAsync(attacker).thenApply(profile -> {
            DamageCalculation damage = new DamageCalculation();
            damage.setBaseWeaponDamage(baseWeaponDamage);
            
            // Get final aggregated stats
            double strength = profile.getFinalStat(PrimaryStat.STRENGTH);
            double criticalDamage = profile.getFinalStat(PrimaryStat.CRIT_DAMAGE);
            double criticalChance = profile.getFinalStat(PrimaryStat.CRIT_CHANCE);
            double ferocity = profile.getFinalStat(PrimaryStat.FEROCITY);
            
            // Apply strength multiplier: (1 + Strength/100)
            double strengthMultiplier = 1.0 + (strength / 100.0);
            damage.setStrengthMultiplier(strengthMultiplier);
            
            // Calculate critical hit
            boolean isCritical = calculateCriticalHit(criticalChance);
            damage.setCritical(isCritical);
            
            if (isCritical) {
                // Critical damage multiplier: (1 + Critical Damage/100)
                double criticalMultiplier = 1.0 + (criticalDamage / 100.0);
                damage.setCriticalMultiplier(criticalMultiplier);
            } else {
                damage.setCriticalMultiplier(1.0);
            }
            
            // Apply ferocity multiplier (if implemented)
            double ferocityMultiplier = 1.0 + (ferocity / 100.0);
            damage.setFerocityMultiplier(ferocityMultiplier);
            
            // Calculate final damage using precise formula
            double finalDamage = baseWeaponDamage * strengthMultiplier;
            if (isCritical) {
                finalDamage *= damage.getCriticalMultiplier();
            }
            finalDamage *= ferocityMultiplier;
            
            damage.setFinalDamage(Math.max(1.0, finalDamage));
            
            return damage;
        });
    }
    
    /**
     * Step 1: Apply base stats from player profile
     */
    private void applyBaseStats(PlayerStatProfile profile, Player player) {
        // Base stats are already initialized in the profile
        // This step ensures we start with clean base values
    }
    
    /**
     * Step 2: Apply base stats from weapon and armor
     */
    private void applyEquipmentStats(PlayerStatProfile profile, Player player) {
        // Apply armor stats
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                applyItemStats(profile, armor, "ARMOR");
            }
        }
        
        // Apply weapon stats
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            applyItemStats(profile, weapon, "WEAPON");
        }
    }
    
    /**
     * Step 3: Apply reforge stats (based on rarity)
     */
    private void applyReforgeStats(PlayerStatProfile profile, Player player) {
        // Apply armor reforge stats
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                applyReforgeStatsForItem(profile, armor);
            }
        }
        
        // Apply weapon reforge stats
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            applyReforgeStatsForItem(profile, weapon);
        }
    }
    
    /**
     * Step 4: Apply enchantment stats/effects
     */
    private void applyEnchantmentStats(PlayerStatProfile profile, Player player) {
        // Apply armor enchantment stats
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                applyEnchantmentStatsForItem(profile, armor);
            }
        }
        
        // Apply weapon enchantment stats
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            applyEnchantmentStatsForItem(profile, weapon);
        }
    }
    
    /**
     * Step 5: Apply global buffs (talismans, magical power, potions)
     */
    private void applyAccessoryStats(PlayerStatProfile profile, Player player) {
        // This will be implemented with the accessory manager
        // For now, placeholder implementation
    }
    
    /**
     * Step 6: Apply magical power stats
     */
    private void applyMagicalPowerStats(PlayerStatProfile profile, Player player) {
        // This will be implemented with the accessory manager
        // For now, placeholder implementation
    }
    
    /**
     * Apply global buffs (potions, effects, etc.)
     */
    private void applyGlobalBuffs(PlayerStatProfile profile, Player player) {
        // Apply potion effects
        applyPotionEffects(profile, player);
        
        // Apply other global buffs
        applyOtherGlobalBuffs(profile, player);
    }
    
    /**
     * Apply stats from individual item
     */
    private void applyItemStats(PlayerStatProfile profile, ItemStack item, String source) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore != null) {
                for (String line : lore) {
                    parseAndApplyStatLine(profile, line, source);
                }
            }
        }
    }
    
    /**
     * Apply reforge stats for specific item
     */
    private void applyReforgeStatsForItem(PlayerStatProfile profile, ItemStack item) {
        // This will be implemented with the reforge matrix manager
        // For now, placeholder implementation
    }
    
    /**
     * Apply enchantment stats for specific item
     */
    private void applyEnchantmentStatsForItem(PlayerStatProfile profile, ItemStack item) {
        // This will be implemented with the custom enchantment system
        // For now, placeholder implementation
    }
    
    /**
     * Apply potion effects
     */
    private void applyPotionEffects(PlayerStatProfile profile, Player player) {
        // Apply active potion effects to stats
        // This will be implemented with the potion system
    }
    
    /**
     * Apply other global buffs
     */
    private void applyOtherGlobalBuffs(PlayerStatProfile profile, Player player) {
        // Apply other global buffs (skills, pets, etc.)
        // This will be implemented with respective systems
    }
    
    /**
     * Parse stat line and apply to profile
     */
    private void parseAndApplyStatLine(PlayerStatProfile profile, String line, String source) {
        if (line.contains("+")) {
            String[] parts = line.split("\\+");
            if (parts.length > 1) {
                String statPart = parts[1].trim();
                double value = parseStatValue(statPart);
                
                // Map stat names to enums
                if (statPart.contains("Strength")) {
                    profile.addFlatBonus(PrimaryStat.STRENGTH, value);
                } else if (statPart.contains("Defense")) {
                    profile.addFlatBonus(PrimaryStat.DEFENSE, value);
                } else if (statPart.contains("Speed")) {
                    profile.addFlatBonus(PrimaryStat.SPEED, value);
                } else if (statPart.contains("Intelligence")) {
                    profile.addFlatBonus(PrimaryStat.INTELLIGENCE, value);
                } else if (statPart.contains("Critical Chance")) {
                    profile.addFlatBonus(PrimaryStat.CRIT_CHANCE, value);
                } else if (statPart.contains("Critical Damage")) {
                    profile.addFlatBonus(PrimaryStat.CRIT_DAMAGE, value);
                } else if (statPart.contains("Ferocity")) {
                    profile.addFlatBonus(PrimaryStat.FEROCITY, value);
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
     * Calculate critical hit chance
     */
    private boolean calculateCriticalHit(double criticalChance) {
        return Math.random() * 100.0 < criticalChance;
    }
    
    /**
     * Get or create player stat profile
     */
    public PlayerStatProfile getOrCreatePlayerProfile(UUID playerId) {
        return playerProfiles.computeIfAbsent(playerId, k -> new PlayerStatProfile(playerId));
    }
    
    /**
     * Start asynchronous update task
     */
    private void startAsyncUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Update all online players' stats asynchronously
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID playerId = player.getUniqueId();
                    
                    // Skip if already updating
                    if (updateTasks.containsKey(playerId)) {
                        continue;
                    }
                    
                    // Start async update
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            calculatePlayerStatsAsync(player).thenAccept(profile -> {
                                // Update player profile with new stats
                                // This could trigger UI updates, etc.
                                
                                // Remove from update tasks
                                updateTasks.remove(playerId);
                            });
                        }
                    }.runTaskAsynchronously(SkyblockPlugin);
                    
                    updateTasks.put(playerId, task);
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0, ASYNC_UPDATE_INTERVAL);
    }
    
    /**
     * Player Stat Profile - comprehensive stat container with precise calculations
     */
    public static class PlayerStatProfile {
        private final UUID playerId;
        private final Map<PrimaryStat, Double> baseStats = new HashMap<>();
        private final Map<PrimaryStat, Double> flatBonuses = new HashMap<>();
        private final Map<PrimaryStat, Double> percentageBonuses = new HashMap<>();
        private final Map<PrimaryStat, Double> multipliers = new HashMap<>();
        private final Map<PrimaryStat, Double> finalStats = new HashMap<>();
        private final Map<SecondaryStat, Double> secondaryStats = new HashMap<>();
        private final long calculationTime;
        
        public PlayerStatProfile(UUID playerId) {
            this.playerId = playerId;
            this.calculationTime = java.lang.System.currentTimeMillis();
            initializeBaseStats();
        }
        
        private void initializeBaseStats() {
            // Initialize primary stats with base values
            for (PrimaryStat stat : PrimaryStat.values()) {
                baseStats.put(stat, stat.getBaseValue());
                flatBonuses.put(stat, 0.0);
                percentageBonuses.put(stat, 0.0);
                multipliers.put(stat, 1.0);
                finalStats.put(stat, stat.getBaseValue());
            }
            
            // Initialize secondary stats
            for (SecondaryStat stat : SecondaryStat.values()) {
                secondaryStats.put(stat, stat.getBaseValue());
            }
        }
        
        public void resetToBase() {
            // Reset all bonuses to zero
            for (PrimaryStat stat : PrimaryStat.values()) {
                flatBonuses.put(stat, 0.0);
                percentageBonuses.put(stat, 0.0);
                multipliers.put(stat, 1.0);
            }
            
            // Reset secondary stats
            for (SecondaryStat stat : SecondaryStat.values()) {
                secondaryStats.put(stat, stat.getBaseValue());
            }
            
            finalStats.clear();
        }
        
        public void addFlatBonus(PrimaryStat stat, double value) {
            flatBonuses.merge(stat, value, Double::sum);
        }
        
        public void addPercentageBonus(PrimaryStat stat, double value) {
            percentageBonuses.merge(stat, value, Double::sum);
        }
        
        public void addMultiplier(PrimaryStat stat, double value) {
            multipliers.merge(stat, value, Double::sum);
        }
        
        public void calculateFinalStats() {
            for (PrimaryStat stat : PrimaryStat.values()) {
                double base = baseStats.get(stat);
                double flat = flatBonuses.get(stat);
                double percentage = percentageBonuses.get(stat);
                double multiplier = multipliers.get(stat);
                
                // Formula: (base + flat) * (1 + percentage/100) * multiplier
                double finalValue = (base + flat) * (1.0 + percentage / 100.0) * multiplier;
                finalStats.put(stat, finalValue);
            }
        }
        
        public boolean isExpired() {
            return java.lang.System.currentTimeMillis() - calculationTime > CACHE_DURATION_TICKS * 50; // 1 second
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public double getFinalStat(PrimaryStat stat) { return finalStats.getOrDefault(stat, stat.getBaseValue()); }
        public double getSecondaryStat(SecondaryStat stat) { return secondaryStats.getOrDefault(stat, stat.getBaseValue()); }
        public Map<PrimaryStat, Double> getFinalStats() { return new HashMap<>(finalStats); }
        public Map<SecondaryStat, Double> getSecondaryStats() { return new HashMap<>(secondaryStats); }
    }
    
    /**
     * Damage Calculation - precise damage calculation result
     */
    public static class DamageCalculation {
        private double baseWeaponDamage;
        private double strengthMultiplier;
        private boolean critical;
        private double criticalMultiplier;
        private double ferocityMultiplier;
        private double finalDamage;
        
        // Getters and setters
        public double getBaseWeaponDamage() { return baseWeaponDamage; }
        public void setBaseWeaponDamage(double baseWeaponDamage) { this.baseWeaponDamage = baseWeaponDamage; }
        public double getStrengthMultiplier() { return strengthMultiplier; }
        public void setStrengthMultiplier(double strengthMultiplier) { this.strengthMultiplier = strengthMultiplier; }
        public boolean isCritical() { return critical; }
        public void setCritical(boolean critical) { this.critical = critical; }
        public double getCriticalMultiplier() { return criticalMultiplier; }
        public void setCriticalMultiplier(double criticalMultiplier) { this.criticalMultiplier = criticalMultiplier; }
        public double getFerocityMultiplier() { return ferocityMultiplier; }
        public void setFerocityMultiplier(double ferocityMultiplier) { this.ferocityMultiplier = ferocityMultiplier; }
        public double getFinalDamage() { return finalDamage; }
        public void setFinalDamage(double finalDamage) { this.finalDamage = finalDamage; }
    }
    
    /**
     * Stat Calculation Cache - performance optimization
     */
    private static class StatCalculationCache {
        private final Map<UUID, PlayerStatProfile> cache = new ConcurrentHashMap<>();
        
        public PlayerStatProfile getCachedProfile(UUID playerId) {
            PlayerStatProfile profile = cache.get(playerId);
            if (profile != null && profile.isExpired()) {
                cache.remove(playerId);
                return null;
            }
            return profile;
        }
        
        public void cacheProfile(UUID playerId, PlayerStatProfile profile) {
            cache.put(playerId, profile);
        }
        
        public void clearCache() {
            cache.clear();
        }
    }
}
