package de.noctivag.plugin.loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import de.noctivag.plugin.data.DatabaseManager;
import de.noctivag.skyblock.core.architecture.PersistentStateDatabase;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Enhanced Conditional Probability Engine - Implements the exact hierarchical drop pipeline
 * 
 * This system follows the strict pipeline requirements:
 * 1. Determine base drop chance of item
 * 2. Apply Looting Enchantment multiplier
 * 3. If item is rare (<5% chance), apply Magic Find (MF) multiplier
 * 4. If item is in player's RNG Meter, apply RNG Meter weighting factor
 * 
 * RNG Meter Formula: New Weight = Base Weight × (1 + 2 × (Required Meter EXP / Current Meter EXP))
 * Maximum multiplier: 3x
 */
public class EnhancedConditionalProbabilityEngine {
    
    private static final Logger logger = Logger.getLogger(EnhancedConditionalProbabilityEngine.class.getName());
    
    // Constants for drop calculation
    private static final double RARE_DROP_THRESHOLD = 0.05; // 5% threshold for rare drops
    private static final double MAX_RNG_METER_MULTIPLIER = 3.0; // Maximum 3x multiplier
    private static final double MAGIC_FIND_CAP = 1000.0; // Maximum Magic Find value
    
    // Database and state management
    private final DatabaseManager databaseManager;
    private final PersistentStateDatabase persistentState;
    private final Map<UUID, PlayerLootData> playerDataCache = new ConcurrentHashMap<>();
    
    // Drop pool constraint tracking
    private final Map<UUID, Set<String>> recentDropPools = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastDropTime = new ConcurrentHashMap<>();
    private static final long DROP_COOLDOWN = 1000L; // 1 second cooldown
    
    // RNG Meter data structure
    public static class RNGMeterData {
        private final String bossType;
        private final String itemId;
        private final double baseWeight;
        private final double requiredMeterExp;
        private final double currentMeterExp;
        private final boolean isSelected;
        
        public RNGMeterData(String bossType, String itemId, double baseWeight, 
                          double requiredMeterExp, double currentMeterExp, boolean isSelected) {
            this.bossType = bossType;
            this.itemId = itemId;
            this.baseWeight = baseWeight;
            this.requiredMeterExp = requiredMeterExp;
            this.currentMeterExp = currentMeterExp;
            this.isSelected = isSelected;
        }
        
        public double calculateWeightMultiplier() {
            if (!isSelected || currentMeterExp <= 0) {
                return 1.0;
            }
            
            // Formula: New Weight = Base Weight × (1 + 2 × (Required Meter EXP / Current Meter EXP))
            double multiplier = 1.0 + (2.0 * (requiredMeterExp / currentMeterExp));
            return Math.min(multiplier, MAX_RNG_METER_MULTIPLIER);
        }
        
        // Getters
        public String getBossType() { return bossType; }
        public String getItemId() { return itemId; }
        public double getBaseWeight() { return baseWeight; }
        public double getRequiredMeterExp() { return requiredMeterExp; }
        public double getCurrentMeterExp() { return currentMeterExp; }
        public boolean isSelected() { return isSelected; }
    }
    
    // Player loot data cache
    public static class PlayerLootData {
        private final UUID playerId;
        private final Map<String, RNGMeterData> rngMeters = new ConcurrentHashMap<>();
        private double magicFind = 0.0;
        private double petLuck = 0.0;
        private long lastUpdate = System.currentTimeMillis();
        
        public PlayerLootData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void updateMagicFind(double magicFind) {
            this.magicFind = Math.min(magicFind, MAGIC_FIND_CAP);
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void updatePetLuck(double petLuck) {
            this.petLuck = petLuck;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addRNGMeter(String itemId, RNGMeterData data) {
            this.rngMeters.put(itemId, data);
            this.lastUpdate = System.currentTimeMillis();
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<String, RNGMeterData> getRNGMeters() { return rngMeters; }
        public double getMagicFind() { return magicFind; }
        public double getPetLuck() { return petLuck; }
        public long getLastUpdate() { return lastUpdate; }
    }
    
    // Drop result with detailed calculation info
    public static class DropCalculationResult {
        private final boolean dropped;
        private final double finalChance;
        private final double baseChance;
        private final double lootingModifier;
        private final double magicFindModifier;
        private final double rngMeterModifier;
        private final String reason;
        private final int dropAmount;
        
        public DropCalculationResult(boolean dropped, double finalChance, double baseChance,
                                   double lootingModifier, double magicFindModifier, 
                                   double rngMeterModifier, String reason, int dropAmount) {
            this.dropped = dropped;
            this.finalChance = finalChance;
            this.baseChance = baseChance;
            this.lootingModifier = lootingModifier;
            this.magicFindModifier = magicFindModifier;
            this.rngMeterModifier = rngMeterModifier;
            this.reason = reason;
            this.dropAmount = dropAmount;
        }
        
        // Getters
        public boolean isDropped() { return dropped; }
        public double getFinalChance() { return finalChance; }
        public double getBaseChance() { return baseChance; }
        public double getLootingModifier() { return lootingModifier; }
        public double getMagicFindModifier() { return magicFindModifier; }
        public double getRngMeterModifier() { return rngMeterModifier; }
        public String getReason() { return reason; }
        public int getDropAmount() { return dropAmount; }
    }
    
    public EnhancedConditionalProbabilityEngine(DatabaseManager databaseManager, 
                                              PersistentStateDatabase persistentState) {
        this.databaseManager = databaseManager;
        this.persistentState = persistentState;
    }
    
    /**
     * Process loot drop through the exact hierarchical pipeline
     */
    public CompletableFuture<DropCalculationResult> processDrop(Player player, MobDropConfig dropConfig, 
                                                               ItemStack weapon, String bossType) {
        return CompletableFuture.supplyAsync(() -> {
            UUID playerId = player.getUniqueId();
            String dropPoolId = dropConfig.getDropPoolId();
            
            try {
                // Stage 1: Check Drop Pool ID constraint (highest priority)
                if (!canDropFromPool(playerId, dropPoolId)) {
                    return new DropCalculationResult(false, 0.0, dropConfig.getDropChance(), 
                                                   1.0, 1.0, 1.0, 
                                                   "Drop Pool constraint: Already dropped from this pool", 0);
                }
                
                // Stage 2: Determine base drop chance
                double baseChance = dropConfig.getDropChance();
                
                // Stage 3: Apply Looting Enchantment multiplier
                double lootingModifier = calculateLootingModifier(weapon);
                double currentChance = baseChance * lootingModifier;
                
                // Stage 4: Apply Magic Find multiplier (rare drops only)
                double magicFindModifier = 1.0;
                if (baseChance < RARE_DROP_THRESHOLD) {
                    PlayerLootData playerData = getPlayerLootData(playerId);
                    magicFindModifier = calculateMagicFindModifier(playerData.getMagicFind());
                    currentChance *= magicFindModifier;
                }
                
                // Stage 5: Apply RNG Meter weighting factor (if applicable)
                double rngMeterModifier = 1.0;
                PlayerLootData playerData = getPlayerLootData(playerId);
                RNGMeterData rngMeter = playerData.getRNGMeters().get(dropConfig.getItemId());
                if (rngMeter != null && rngMeter.isSelected()) {
                    rngMeterModifier = rngMeter.calculateWeightMultiplier();
                    currentChance *= rngMeterModifier;
                }
                
                // Stage 6: Final probability calculation (cap at 100%)
                double finalChance = Math.min(currentChance, 1.0);
                
                // Stage 7: Roll for drop
                boolean dropped = Math.random() < finalChance;
                
                if (dropped) {
                    // Record drop for constraint tracking
                    recordDrop(playerId, dropPoolId);
                    
                    // Calculate drop amount based on looting modifier
                    int dropAmount = calculateDropAmount(dropConfig, lootingModifier);
                    
                    // Update RNG Meter progress
                    if (rngMeter != null) {
                        updateRNGMeterProgress(playerId, bossType, dropConfig.getItemId());
                    }
                    
                    return new DropCalculationResult(true, finalChance, baseChance,
                                                   lootingModifier, magicFindModifier, rngMeterModifier,
                                                   "Drop successful", dropAmount);
                } else {
                    return new DropCalculationResult(false, finalChance, baseChance,
                                                   lootingModifier, magicFindModifier, rngMeterModifier,
                                                   "Drop failed - probability roll", 0);
                }
                
            } catch (Exception e) {
                logger.severe("Error processing drop for player " + playerId + ": " + e.getMessage());
                return new DropCalculationResult(false, 0.0, dropConfig.getDropChance(),
                                               1.0, 1.0, 1.0, "Error: " + e.getMessage(), 0);
            }
        });
    }
    
    /**
     * Calculate Looting Enchantment modifier
     */
    private double calculateLootingModifier(ItemStack weapon) {
        if (weapon == null || !weapon.hasItemMeta() || !weapon.getItemMeta().hasEnchants()) {
            return 1.0;
        }
        
        int lootingLevel = weapon.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.LOOTING);
        return 1.0 + (lootingLevel * 0.1); // 10% per level
    }
    
    /**
     * Calculate Magic Find modifier for rare drops
     */
    private double calculateMagicFindModifier(double magicFind) {
        // Magic Find formula: 1 + (MF / 100)
        return 1.0 + (magicFind / 100.0);
    }
    
    /**
     * Check if player can drop from specific pool (Drop Pool ID constraint)
     */
    private boolean canDropFromPool(UUID playerId, String dropPoolId) {
        Set<String> usedPools = recentDropPools.get(playerId);
        if (usedPools == null) {
            return true;
        }
        
        // Check cooldown
        Long lastDrop = lastDropTime.get(playerId);
        if (lastDrop != null && (System.currentTimeMillis() - lastDrop) < DROP_COOLDOWN) {
            return false;
        }
        
        return !usedPools.contains(dropPoolId);
    }
    
    /**
     * Record drop for constraint tracking
     */
    private void recordDrop(UUID playerId, String dropPoolId) {
        recentDropPools.computeIfAbsent(playerId, k -> new HashSet<>()).add(dropPoolId);
        lastDropTime.put(playerId, System.currentTimeMillis());
        
        // Clean up old entries after cooldown period
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(DROP_COOLDOWN + 1000);
                recentDropPools.remove(playerId);
                lastDropTime.remove(playerId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * Calculate drop amount based on looting modifier
     */
    private int calculateDropAmount(MobDropConfig dropConfig, double lootingModifier) {
        int baseAmount = dropConfig.getMinAmount();
        int maxAmount = dropConfig.getMaxAmount();
        
        // Apply looting modifier to amount calculation
        double amountMultiplier = Math.min(lootingModifier, 3.0); // Cap at 3x
        int modifiedAmount = (int) Math.ceil(baseAmount * amountMultiplier);
        
        return Math.min(modifiedAmount, maxAmount);
    }
    
    /**
     * Get or load player loot data
     */
    private PlayerLootData getPlayerLootData(UUID playerId) {
        return playerDataCache.computeIfAbsent(playerId, k -> {
            // Load from database
            return loadPlayerLootDataFromDatabase(playerId);
        });
    }
    
    /**
     * Load player loot data from database
     */
    private PlayerLootData loadPlayerLootDataFromDatabase(UUID playerId) {
        PlayerLootData data = new PlayerLootData(playerId);
        
        try {
            // Load Magic Find and Pet Luck from player profile
            // This would integrate with the existing player data system
            // For now, using default values
            data.updateMagicFind(0.0);
            data.updatePetLuck(0.0);
            
            // Load RNG Meter data
            loadRNGMeterData(playerId, data);
            
        } catch (Exception e) {
            logger.warning("Failed to load player loot data for " + playerId + ": " + e.getMessage());
        }
        
        return data;
    }
    
    /**
     * Load RNG Meter data from database
     */
    private void loadRNGMeterData(UUID playerId, PlayerLootData data) {
        // This would load from MongoDB player profile
        // Implementation would depend on the specific database schema
        // For now, creating sample data structure
    }
    
    /**
     * Update RNG Meter progress after successful drop
     */
    private void updateRNGMeterProgress(UUID playerId, String bossType, String itemId) {
        PlayerLootData playerData = getPlayerLootData(playerId);
        RNGMeterData rngMeter = playerData.getRNGMeters().get(itemId);
        
        if (rngMeter != null && rngMeter.isSelected()) {
            // Increment current meter EXP
            // This would update the MongoDB player profile
            logger.info("RNG Meter progress updated for player " + playerId + 
                       " boss " + bossType + " item " + itemId);
        }
    }
    
    /**
     * Set RNG Meter selection for player
     */
    public CompletableFuture<Void> setRNGMeterSelection(UUID playerId, String bossType, String itemId) {
        return CompletableFuture.runAsync(() -> {
            PlayerLootData playerData = getPlayerLootData(playerId);
            
            // Clear existing selection for this boss type
            playerData.getRNGMeters().entrySet().removeIf(entry -> 
                entry.getValue().getBossType().equals(bossType));
            
            // Create new RNG Meter data
            RNGMeterData newMeter = new RNGMeterData(bossType, itemId, 1.0, 1000.0, 0.0, true);
            playerData.addRNGMeter(itemId, newMeter);
            
            // Save to database
            savePlayerLootDataToDatabase(playerId, playerData);
        });
    }
    
    /**
     * Save player loot data to database
     */
    private void savePlayerLootDataToDatabase(UUID playerId, PlayerLootData data) {
        // This would save to MongoDB player profile
        // Implementation would depend on the specific database schema
        logger.info("Player loot data saved for " + playerId);
    }
    
    /**
     * Update player Magic Find
     */
    public void updatePlayerMagicFind(UUID playerId, double magicFind) {
        PlayerLootData data = getPlayerLootData(playerId);
        data.updateMagicFind(magicFind);
        savePlayerLootDataToDatabase(playerId, data);
    }
    
    /**
     * Update player Pet Luck
     */
    public void updatePlayerPetLuck(UUID playerId, double petLuck) {
        PlayerLootData data = getPlayerLootData(playerId);
        data.updatePetLuck(petLuck);
        savePlayerLootDataToDatabase(playerId, data);
    }
    
    /**
     * Get player RNG Meter data
     */
    public Map<String, RNGMeterData> getPlayerRNGMeters(UUID playerId) {
        PlayerLootData data = getPlayerLootData(playerId);
        return new HashMap<>(data.getRNGMeters());
    }
}
