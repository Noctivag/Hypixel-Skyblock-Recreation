package de.noctivag.plugin.loot;

import de.noctivag.plugin.data.DatabaseManager;
import de.noctivag.skyblock.core.architecture.PersistentStateDatabase;
import de.noctivag.plugin.dungeons.bosses.BossStateMachineSystem;
import de.noctivag.skyblock.core.architecture.GlobalInstanceManager;
import de.noctivag.skyblock.core.events.GlobalCalendarEventScheduler;
import de.noctivag.skyblock.core.architecture.CapacityScalingIntegration;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Endgame Loot System - Master integration of all endgame mechanics
 * 
 * This system integrates:
 * - Enhanced Conditional Probability Engine
 * - RNG Meter System with MongoDB persistence
 * - Drop Pool Constraint System
 * - Boss State Machine System
 * - Global Calendar Event Scheduler
 * - Capacity Scaling Integration
 * 
 * Provides a unified interface for all endgame loot mechanics.
 */
public class EndgameLootSystem {
    
    private static final Logger logger = Logger.getLogger(EndgameLootSystem.class.getName());
    
    // Core components
    private final EnhancedConditionalProbabilityEngine probabilityEngine;
    private final RNGMeterSystem rngMeterSystem;
    private final DropPoolConstraintSystem dropPoolSystem;
    private final BossStateMachineSystem bossStateMachineSystem;
    private final GlobalCalendarEventScheduler calendarScheduler;
    private final CapacityScalingIntegration capacityScaling;
    
    // Database connections
    private final DatabaseManager databaseManager;
    private final PersistentStateDatabase persistentState;
    
    // System state
    private boolean initialized = false;
    
    public EndgameLootSystem(DatabaseManager databaseManager, PersistentStateDatabase persistentState,
                           GlobalInstanceManager gim) {
        this.databaseManager = databaseManager;
        this.persistentState = persistentState;
        
        // Initialize core systems
        this.probabilityEngine = new EnhancedConditionalProbabilityEngine(databaseManager, persistentState);
        this.rngMeterSystem = new RNGMeterSystem(databaseManager, persistentState);
        this.dropPoolSystem = new DropPoolConstraintSystem(databaseManager, persistentState);
        this.bossStateMachineSystem = new BossStateMachineSystem();
        
        // Initialize distributed systems
        this.calendarScheduler = new GlobalCalendarEventScheduler(gim, null);
        this.capacityScaling = new CapacityScalingIntegration(gim, calendarScheduler, null);
        
        logger.info("Endgame Loot System initialized");
    }
    
    /**
     * Initialize the complete endgame system
     */
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            try {
                // Initialize all subsystems
                logger.info("Initializing Endgame Loot System...");
                
                // The systems are already initialized in constructor
                // Additional initialization tasks can be added here
                
                this.initialized = true;
                logger.info("Endgame Loot System fully initialized");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize Endgame Loot System: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Process a complete loot drop with all endgame mechanics
     */
    public CompletableFuture<LootDropResult> processEndgameLootDrop(Player player, UUID mobId, 
                                                                   MobDropConfig dropConfig, 
                                                                   ItemStack weapon, String bossType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Stage 1: Start mob drop session with Drop Pool constraints
                DropPoolConstraintSystem.MobDropSession dropSession = 
                    dropPoolSystem.startMobDropSession(mobId, player.getUniqueId());
                
                // Stage 2: Process through enhanced probability engine
                EnhancedConditionalProbabilityEngine.DropCalculationResult calcResult = 
                    probabilityEngine.processDrop(player, dropConfig, weapon, bossType).join();
                
                // Stage 3: Apply Drop Pool ID constraint
                DropPoolConstraintSystem.DropAttempt dropAttempt = 
                    dropPoolSystem.processDropAttempt(mobId, dropConfig.getItemId(), 
                                                    dropConfig.getDropPoolId(), 
                                                    calcResult.getFinalChance(), 
                                                    calcResult.isDropped() ? new LootDrop(dropConfig, calcResult.getFinalChance()) : null).join();
                
                // Stage 4: Finalize drop session
                List<LootDrop> finalDrops = dropPoolSystem.finalizeMobDropSession(mobId).join();
                
                // Stage 5: Update RNG Meter if applicable
                if (dropAttempt.isSuccessful() && dropAttempt.getLootDrop() != null) {
                    rngMeterSystem.updateMeterProgress(player.getUniqueId(), 
                        RNGMeterSystem.BossType.valueOf(bossType.toUpperCase()), 
                        calculateMeterExp(dropConfig), true).join();
                }
                
                // Create comprehensive result
                LootDropResult result = new LootDropResult(
                    dropAttempt.isSuccessful(),
                    calcResult.getFinalChance(),
                    calcResult.getBaseChance(),
                    calcResult.getLootingModifier(),
                    calcResult.getMagicFindModifier(),
                    calcResult.getRngMeterModifier(),
                    finalDrops,
                    dropAttempt.getReason()
                );
                
                logger.info("Processed endgame loot drop for player " + player.getName() + 
                           " - Success: " + result.isSuccessful() + 
                           " - Final chance: " + String.format("%.2f%%", result.getFinalChance() * 100));
                
                return result;
                
            } catch (Exception e) {
                logger.severe("Error processing endgame loot drop: " + e.getMessage());
                return new LootDropResult(false, 0.0, 0.0, 1.0, 1.0, 1.0, 
                                        new ArrayList<>(), "Error: " + e.getMessage());
            }
        });
    }
    
    /**
     * Create boss state machine for dungeon boss
     */
    public CompletableFuture<BossStateMachineSystem.BossStateMachine> createBossStateMachine(
            BossStateMachineSystem.BossType bossType, List<UUID> participantIds, Location location) {
        return bossStateMachineSystem.createBossStateMachine(bossType, participantIds, location);
    }
    
    /**
     * Update boss health and trigger state transitions
     */
    public CompletableFuture<Void> updateBossHealth(UUID bossId, double newHealth) {
        return bossStateMachineSystem.updateBossHealth(bossId, newHealth);
    }
    
    /**
     * Schedule a game event with capacity scaling
     */
    public CompletableFuture<String> scheduleGameEvent(String eventName, 
                                                      GlobalCalendarEventScheduler.EventCategory category,
                                                      LocalDateTime startTime, 
                                                      GlobalCalendarEventScheduler.EventPriority priority) {
        return calendarScheduler.scheduleEvent(eventName, category, startTime, priority);
    }
    
    /**
     * Set RNG Meter selection for player
     */
    public CompletableFuture<Boolean> setPlayerRNGMeterSelection(UUID playerId, 
                                                               RNGMeterSystem.BossType bossType, 
                                                               String itemId) {
        return rngMeterSystem.setMeterSelection(playerId, bossType, itemId);
    }
    
    /**
     * Get player RNG Meter data
     */
    public CompletableFuture<RNGMeterSystem.PlayerRNGMeterData> getPlayerRNGMeterData(UUID playerId) {
        return rngMeterSystem.getPlayerMeterData(playerId);
    }
    
    /**
     * Update player Magic Find
     */
    public void updatePlayerMagicFind(UUID playerId, double magicFind) {
        probabilityEngine.updatePlayerMagicFind(playerId, magicFind);
    }
    
    /**
     * Update player Pet Luck
     */
    public void updatePlayerPetLuck(UUID playerId, double petLuck) {
        probabilityEngine.updatePlayerPetLuck(playerId, petLuck);
    }
    
    /**
     * Get upcoming events
     */
    public List<GlobalCalendarEventScheduler.GameEvent> getUpcomingEvents(int limit) {
        return calendarScheduler.getUpcomingEvents(limit);
    }
    
    /**
     * Get active events
     */
    public List<GlobalCalendarEventScheduler.GameEvent> getActiveEvents() {
        return calendarScheduler.getActiveEvents();
    }
    
    /**
     * Get all drop pool definitions
     */
    public Map<String, DropPoolConstraintSystem.DropPoolDefinition> getAllDropPools() {
        return dropPoolSystem.getAllDropPools();
    }
    
    /**
     * Get economy statistics
     */
    public Map<String, Object> getEconomyStatistics() {
        return dropPoolSystem.getEconomyStatistics();
    }
    
    /**
     * Get boss state machine statistics
     */
    public Map<String, Object> getBossStatistics() {
        return bossStateMachineSystem.getBossStatistics();
    }
    
    /**
     * Get capacity scaling statistics
     */
    public Map<String, Object> getCapacityScalingStatistics() {
        return capacityScaling.getCapacityScalingStatistics();
    }
    
    /**
     * Calculate meter experience for drop
     */
    private double calculateMeterExp(MobDropConfig dropConfig) {
        // Base exp calculation based on drop rarity and type
        double baseExp = 1.0;
        
        if (dropConfig.getDropChance() < 0.01) { // Very rare drops
            baseExp = 10.0;
        } else if (dropConfig.getDropChance() < 0.05) { // Rare drops
            baseExp = 5.0;
        } else if (dropConfig.getDropChance() < 0.1) { // Uncommon drops
            baseExp = 2.0;
        }
        
        return baseExp;
    }
    
    /**
     * Comprehensive loot drop result
     */
    public static class LootDropResult {
        private final boolean successful;
        private final double finalChance;
        private final double baseChance;
        private final double lootingModifier;
        private final double magicFindModifier;
        private final double rngMeterModifier;
        private final List<LootDrop> finalDrops;
        private final String reason;
        
        public LootDropResult(boolean successful, double finalChance, double baseChance,
                            double lootingModifier, double magicFindModifier, double rngMeterModifier,
                            List<LootDrop> finalDrops, String reason) {
            this.successful = successful;
            this.finalChance = finalChance;
            this.baseChance = baseChance;
            this.lootingModifier = lootingModifier;
            this.magicFindModifier = magicFindModifier;
            this.rngMeterModifier = rngMeterModifier;
            this.finalDrops = new ArrayList<>(finalDrops);
            this.reason = reason;
        }
        
        // Getters
        public boolean isSuccessful() { return successful; }
        public double getFinalChance() { return finalChance; }
        public double getBaseChance() { return baseChance; }
        public double getLootingModifier() { return lootingModifier; }
        public double getMagicFindModifier() { return magicFindModifier; }
        public double getRngMeterModifier() { return rngMeterModifier; }
        public List<LootDrop> getFinalDrops() { return finalDrops; }
        public String getReason() { return reason; }
        
        public String getDetailedBreakdown() {
            return String.format("Drop Result: %s\n" +
                               "Base Chance: %.2f%%\n" +
                               "Looting Modifier: %.2fx\n" +
                               "Magic Find Modifier: %.2fx\n" +
                               "RNG Meter Modifier: %.2fx\n" +
                               "Final Chance: %.2f%%\n" +
                               "Reason: %s",
                               successful ? "SUCCESS" : "FAILED",
                               baseChance * 100,
                               lootingModifier,
                               magicFindModifier,
                               rngMeterModifier,
                               finalChance * 100,
                               reason);
        }
    }
    
    /**
     * Check if system is initialized
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Shutdown the endgame system
     */
    public void shutdown() {
        try {
            calendarScheduler.shutdown();
            bossStateMachineSystem.shutdown();
            logger.info("Endgame Loot System shutdown completed");
        } catch (Exception e) {
            logger.severe("Error during Endgame Loot System shutdown: " + e.getMessage());
        }
    }
}
