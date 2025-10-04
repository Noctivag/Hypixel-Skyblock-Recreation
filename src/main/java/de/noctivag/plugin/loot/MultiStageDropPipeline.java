package de.noctivag.plugin.loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-Stage Drop Pipeline - Implements the sequential drop calculation system
 * 
 * This system processes loot drops in multiple stages:
 * 1. Base drop chance calculation
 * 2. Looting enchantment modification
 * 3. Magic Find modification (rare drops only)
 * 4. Pet Luck modification (pet drops only)
 * 5. Economy throttling (Drop Pool ID constraint)
 */
public class MultiStageDropPipeline {
    
    private final ConditionalProbabilityEngine probabilityEngine;
    private final Map<String, Set<String>> recentDrops = new ConcurrentHashMap<>(); // Player -> Drop Pool IDs
    private final Map<String, Long> lastDropTime = new ConcurrentHashMap<>(); // Player -> Last drop timestamp
    
    // Drop categories and thresholds
    private static final double RARE_DROP_THRESHOLD = 0.05; // 5% threshold for rare drops
    private static final long DROP_COOLDOWN = 1000L; // 1 second cooldown between drops
    
    public MultiStageDropPipeline(ConditionalProbabilityEngine probabilityEngine) {
        this.probabilityEngine = probabilityEngine;
    }
    
    /**
     * Process a drop through the multi-stage pipeline
     */
    public DropResult processDrop(Player player, MobDropConfig dropConfig, ItemStack weapon, double magicFind, double petLuck) {
        String playerId = player.getUniqueId().toString();
        String dropPoolId = dropConfig.getDropPoolId();
        
        // Stage 1: Check economy throttling (Drop Pool ID constraint)
        if (!canDropFromPool(playerId, dropPoolId)) {
            return new DropResult(false, 0.0, "Economy throttling: Already dropped from this pool");
        }
        
        // Stage 2: Calculate base drop chance
        double baseChance = dropConfig.getDropChance();
        
        // Stage 3: Apply Looting enchantment modifier
        double lootingModifier = calculateLootingModifier(weapon);
        double modifiedChance = baseChance * lootingModifier;
        
        // Stage 4: Apply Magic Find modifier (rare drops only)
        if (baseChance < RARE_DROP_THRESHOLD) {
            double magicFindModifier = calculateMagicFindModifier(magicFind);
            modifiedChance *= magicFindModifier;
        }
        
        // Stage 5: Apply Pet Luck modifier (pet drops only)
        if (dropConfig.isPetDrop()) {
            double petLuckModifier = calculatePetLuckModifier(petLuck);
            modifiedChance *= petLuckModifier;
        }
        
        // Stage 6: Final probability calculation
        double finalChance = Math.min(modifiedChance, 1.0); // Cap at 100%
        
        // Stage 7: Roll for drop
        boolean dropped = Math.random() < finalChance;
        
        if (dropped) {
            // Record drop for economy throttling
            recordDrop(playerId, dropPoolId);
            
            // Calculate drop amount
            int dropAmount = calculateDropAmount(dropConfig, lootingModifier);
            
            return new DropResult(true, finalChance, "Drop successful", dropAmount);
        } else {
            return new DropResult(false, finalChance, "Drop failed");
        }
    }
    
    /**
     * Check if player can drop from a specific pool (economy throttling)
     */
    private boolean canDropFromPool(String playerId, String dropPoolId) {
        Set<String> playerDrops = recentDrops.get(playerId);
        if (playerDrops == null) {
            return true; // No recent drops, can drop
        }
        
        // Check if already dropped from this pool recently
        if (playerDrops.contains(dropPoolId)) {
            return false; // Already dropped from this pool
        }
        
        // Check drop cooldown
        Long lastDrop = lastDropTime.get(playerId);
        if (lastDrop != null && System.currentTimeMillis() - lastDrop < DROP_COOLDOWN) {
            return false; // Still in cooldown
        }
        
        return true;
    }
    
    /**
     * Record a drop for economy throttling
     */
    private void recordDrop(String playerId, String dropPoolId) {
        recentDrops.computeIfAbsent(playerId, k -> new HashSet<>()).add(dropPoolId);
        lastDropTime.put(playerId, System.currentTimeMillis());
        
        // Clean up old drops (remove after 5 minutes)
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                recentDrops.remove(playerId);
                lastDropTime.remove(playerId);
            }
        }, 300000L); // 5 minutes
    }
    
    /**
     * Calculate Looting enchantment modifier
     */
    private double calculateLootingModifier(ItemStack weapon) {
        if (weapon == null || !weapon.hasItemMeta()) {
            return 1.0;
        }
        
        int lootingLevel = weapon.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.LOOTING);
        return 1.0 + (lootingLevel * 0.15); // 15% increase per level
    }
    
    /**
     * Calculate Magic Find modifier for rare drops
     */
    private double calculateMagicFindModifier(double magicFind) {
        // Magic Find formula: 1 + (Magic Find / 100)
        // Cap at 3x multiplier (300 Magic Find)
        double modifier = 1.0 + (Math.min(magicFind, 200.0) / 100.0);
        return Math.min(modifier, 3.0);
    }
    
    /**
     * Calculate Pet Luck modifier for pet drops
     */
    private double calculatePetLuckModifier(double petLuck) {
        // Pet Luck formula: 1 + (Pet Luck / 50)
        // Cap at 2x multiplier (100 Pet Luck)
        double modifier = 1.0 + (Math.min(petLuck, 100.0) / 50.0);
        return Math.min(modifier, 2.0);
    }
    
    /**
     * Calculate drop amount based on looting modifier
     */
    private int calculateDropAmount(MobDropConfig dropConfig, double lootingModifier) {
        int baseAmount = dropConfig.getMinAmount();
        int maxAmount = dropConfig.getMaxAmount();
        
        // Increase amount based on looting modifier
        double amountMultiplier = Math.min(lootingModifier, 2.0); // Cap at 2x amount
        int modifiedAmount = (int) (baseAmount * amountMultiplier);
        
        return Math.min(modifiedAmount, maxAmount);
    }
    
    /**
     * Process multiple drops for a single mob kill
     */
    public List<DropResult> processMultipleDrops(Player player, List<MobDropConfig> dropConfigs, 
                                                ItemStack weapon, double magicFind, double petLuck) {
        List<DropResult> results = new ArrayList<>();
        
        // Process each drop configuration
        for (MobDropConfig dropConfig : dropConfigs) {
            DropResult result = processDrop(player, dropConfig, weapon, magicFind, petLuck);
            results.add(result);
            
            // If drop succeeded, break to respect economy throttling
            if (result.isSuccess()) {
                break;
            }
        }
        
        return results;
    }
    
    /**
     * Get drop statistics for a player
     */
    public DropStatistics getDropStatistics(Player player) {
        String playerId = player.getUniqueId().toString();
        Set<String> recentDrops = this.recentDrops.get(playerId);
        
        return new DropStatistics(
            recentDrops != null ? recentDrops.size() : 0,
            recentDrops != null ? new HashSet<>(recentDrops) : new HashSet<>(),
            lastDropTime.get(playerId)
        );
    }
    
    /**
     * Reset drop cooldowns for a player
     */
    public void resetPlayerDrops(Player player) {
        String playerId = player.getUniqueId().toString();
        recentDrops.remove(playerId);
        lastDropTime.remove(playerId);
    }
    
    /**
     * Drop Result class
     */
    public static class DropResult {
        private final boolean success;
        private final double finalChance;
        private final String message;
        private final int amount;
        
        public DropResult(boolean success, double finalChance, String message) {
            this(success, finalChance, message, 1);
        }
        
        public DropResult(boolean success, double finalChance, String message, int amount) {
            this.success = success;
            this.finalChance = finalChance;
            this.message = message;
            this.amount = amount;
        }
        
        public boolean isSuccess() { return success; }
        public double getFinalChance() { return finalChance; }
        public String getMessage() { return message; }
        public int getAmount() { return amount; }
    }
    
    /**
     * Drop Statistics class
     */
    public static class DropStatistics {
        private final int totalDrops;
        private final Set<String> dropPools;
        private final Long lastDropTime;
        
        public DropStatistics(int totalDrops, Set<String> dropPools, Long lastDropTime) {
            this.totalDrops = totalDrops;
            this.dropPools = dropPools;
            this.lastDropTime = lastDropTime;
        }
        
        public int getTotalDrops() { return totalDrops; }
        public Set<String> getDropPools() { return dropPools; }
        public Long getLastDropTime() { return lastDropTime; }
    }
}
