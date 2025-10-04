package de.noctivag.plugin.dungeons.bosses;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Boss State Machine System - Complex boss mechanics with finite state machines
 * 
 * Features:
 * - Finite State Machine (FSM) for complex bosses like Necron
 * - Each phase is a separate object with defined logic and transition conditions
 * - State transitions based on conditions (Boss HP < X%, time elapsed, etc.)
 * - Phase-specific mechanics and behaviors
 * - Event-driven state changes
 */
public class BossStateMachineSystem {
    
    private static final Logger logger = Logger.getLogger(BossStateMachineSystem.class.getName());
    
    // State machine scheduler
    private final ScheduledExecutorService stateMachineScheduler;
    
    // Active boss state machines
    private final Map<UUID, BossStateMachine> activeBosses = new ConcurrentHashMap<>();
    
    // Boss type definitions
    public enum BossType {
        NECRON("Necron", 1000.0, Arrays.asList(
            BossPhase.PHASE_1_PYLONS,
            BossPhase.TERMINALS_ACTIVATION,
            BossPhase.BOSS_FIGHT_PHASE_1,
            BossPhase.BOSS_FIGHT_PHASE_2,
            BossPhase.BOSS_FIGHT_PHASE_3,
            BossPhase.BOSS_FIGHT_PHASE_4
        )),
        STORM("Storm", 800.0, Arrays.asList(
            BossPhase.PHASE_1_PREPARATION,
            BossPhase.LIGHTNING_PHASE,
            BossPhase.STORM_PHASE,
            BossPhase.FINAL_PHASE
        )),
        GOLDOR("Goldor", 900.0, Arrays.asList(
            BossPhase.PHASE_1_PREPARATION,
            BossPhase.TANK_PHASE,
            BossPhase.SHIELD_PHASE,
            BossPhase.FINAL_PHASE
        )),
        MAXOR("Maxor", 700.0, Arrays.asList(
            BossPhase.PHASE_1_PREPARATION,
            BossPhase.SPEED_PHASE,
            BossPhase.BERSERK_PHASE,
            BossPhase.FINAL_PHASE
        ));
        
        private final String displayName;
        private final double maxHealth;
        private final List<BossPhase> phases;
        
        BossType(String displayName, double maxHealth, List<BossPhase> phases) {
            this.displayName = displayName;
            this.maxHealth = maxHealth;
            this.phases = new ArrayList<>(phases);
        }
        
        // Getters
        public String getDisplayName() { return displayName; }
        public double getMaxHealth() { return maxHealth; }
        public List<BossPhase> getPhases() { return phases; }
    }
    
    /**
     * Boss Phase Enumeration
     */
    public enum BossPhase {
        PHASE_1_PYLONS("Phase 1: Pylons", 0.8, 120000L), // 2 minutes
        TERMINALS_ACTIVATION("Terminals Activation", 0.6, 90000L), // 1.5 minutes
        BOSS_FIGHT_PHASE_1("Boss Fight Phase 1", 0.4, 180000L), // 3 minutes
        BOSS_FIGHT_PHASE_2("Boss Fight Phase 2", 0.3, 150000L), // 2.5 minutes
        BOSS_FIGHT_PHASE_3("Boss Fight Phase 3", 0.2, 120000L), // 2 minutes
        BOSS_FIGHT_PHASE_4("Boss Fight Phase 4", 0.1, 90000L), // 1.5 minutes
        PHASE_1_PREPARATION("Phase 1: Preparation", 0.8, 60000L), // 1 minute
        LIGHTNING_PHASE("Lightning Phase", 0.6, 120000L), // 2 minutes
        STORM_PHASE("Storm Phase", 0.4, 150000L), // 2.5 minutes
        TANK_PHASE("Tank Phase", 0.6, 120000L), // 2 minutes
        SHIELD_PHASE("Shield Phase", 0.3, 90000L), // 1.5 minutes
        SPEED_PHASE("Speed Phase", 0.7, 100000L), // 1.7 minutes
        BERSERK_PHASE("Berserk Phase", 0.4, 80000L), // 1.3 minutes
        FINAL_PHASE("Final Phase", 0.1, 120000L); // 2 minutes
        
        private final String displayName;
        private final double healthThreshold;
        private final long maxDuration;
        
        BossPhase(String displayName, double healthThreshold, long maxDuration) {
            this.displayName = displayName;
            this.healthThreshold = healthThreshold;
            this.maxDuration = maxDuration;
        }
        
        // Getters
        public String getDisplayName() { return displayName; }
        public double getHealthThreshold() { return healthThreshold; }
        public long getMaxDuration() { return maxDuration; }
    }
    
    /**
     * Boss State Machine
     */
    public static class BossStateMachine {
        private final UUID bossId;
        private final BossType bossType;
        private final List<BossPhase> phases;
        private final Map<UUID, Player> participants;
        private final Location location;
        
        private BossPhase currentPhase;
        private int currentPhaseIndex;
        private double currentHealth;
        private long phaseStartTime;
        private boolean isActive;
        private Map<String, Object> phaseData;
        
        public BossStateMachine(UUID bossId, BossType bossType, List<UUID> participantIds, Location location) {
            this.bossId = bossId;
            this.bossType = bossType;
            this.phases = new ArrayList<>(bossType.getPhases());
            this.participants = new ConcurrentHashMap<>();
            this.location = location;
            
            this.currentPhase = phases.get(0);
            this.currentPhaseIndex = 0;
            this.currentHealth = bossType.getMaxHealth();
            this.phaseStartTime = System.currentTimeMillis();
            this.isActive = true;
            this.phaseData = new ConcurrentHashMap<>();
            
            // Add participants
            for (UUID playerId : participantIds) {
                // This would get the actual player object
                // participants.put(playerId, Bukkit.getPlayer(playerId));
            }
        }
        
        public boolean canTransitionToNextPhase() {
            if (currentPhaseIndex >= phases.size() - 1) {
                return false; // Already in final phase
            }
            
            BossPhase nextPhase = phases.get(currentPhaseIndex + 1);
            
            // Check health threshold
            double healthPercentage = currentHealth / bossType.getMaxHealth();
            if (healthPercentage <= nextPhase.getHealthThreshold()) {
                return true;
            }
            
            // Check time threshold
            long timeInPhase = System.currentTimeMillis() - phaseStartTime;
            if (timeInPhase >= currentPhase.getMaxDuration()) {
                return true;
            }
            
            return false;
        }
        
        public void transitionToNextPhase() {
            if (!canTransitionToNextPhase()) {
                return;
            }
            
            // Execute phase end logic
            executePhaseEndLogic(currentPhase);
            
            // Move to next phase
            currentPhaseIndex++;
            currentPhase = phases.get(currentPhaseIndex);
            phaseStartTime = System.currentTimeMillis();
            phaseData.clear();
            
            // Execute phase start logic
            executePhaseStartLogic(currentPhase);
            
            logger.info("Boss " + bossType.getDisplayName() + " transitioned to " + currentPhase.getDisplayName());
        }
        
        public void updateHealth(double newHealth) {
            this.currentHealth = Math.max(0, newHealth);
            
            // Check for phase transitions
            if (canTransitionToNextPhase()) {
                transitionToNextPhase();
            }
        }
        
        public void executePhaseStartLogic(BossPhase phase) {
            switch (phase) {
                case PHASE_1_PYLONS:
                    spawnPylons();
                    break;
                case TERMINALS_ACTIVATION:
                    activateTerminals();
                    break;
                case BOSS_FIGHT_PHASE_1:
                    startBossFightPhase1();
                    break;
                case BOSS_FIGHT_PHASE_2:
                    startBossFightPhase2();
                    break;
                case BOSS_FIGHT_PHASE_3:
                    startBossFightPhase3();
                    break;
                case BOSS_FIGHT_PHASE_4:
                    startBossFightPhase4();
                    break;
                case LIGHTNING_PHASE:
                    startLightningPhase();
                    break;
                case STORM_PHASE:
                    startStormPhase();
                    break;
                case TANK_PHASE:
                    startTankPhase();
                    break;
                case SHIELD_PHASE:
                    startShieldPhase();
                    break;
                case SPEED_PHASE:
                    startSpeedPhase();
                    break;
                case BERSERK_PHASE:
                    startBerserkPhase();
                    break;
                case FINAL_PHASE:
                    startFinalPhase();
                    break;
            }
        }
        
        public void executePhaseEndLogic(BossPhase phase) {
            switch (phase) {
                case PHASE_1_PYLONS:
                    completePylons();
                    break;
                case TERMINALS_ACTIVATION:
                    completeTerminals();
                    break;
                case BOSS_FIGHT_PHASE_1:
                    completeBossFightPhase1();
                    break;
                case BOSS_FIGHT_PHASE_2:
                    completeBossFightPhase2();
                    break;
                case BOSS_FIGHT_PHASE_3:
                    completeBossFightPhase3();
                    break;
                case BOSS_FIGHT_PHASE_4:
                    completeBossFightPhase4();
                    break;
                case LIGHTNING_PHASE:
                    completeLightningPhase();
                    break;
                case STORM_PHASE:
                    completeStormPhase();
                    break;
                case TANK_PHASE:
                    completeTankPhase();
                    break;
                case SHIELD_PHASE:
                    completeShieldPhase();
                    break;
                case SPEED_PHASE:
                    completeSpeedPhase();
                    break;
                case BERSERK_PHASE:
                    completeBerserkPhase();
                    break;
                case FINAL_PHASE:
                    completeFinalPhase();
                    break;
            }
        }
        
        // Phase-specific implementations for Necron
        private void spawnPylons() {
            logger.info("Spawning pylons for " + bossType.getDisplayName());
            // Implementation for spawning pylons
            phaseData.put("pylonsActive", true);
        }
        
        private void completePylons() {
            logger.info("Completing pylons for " + bossType.getDisplayName());
            phaseData.put("pylonsActive", false);
        }
        
        private void activateTerminals() {
            logger.info("Activating terminals for " + bossType.getDisplayName());
            // Implementation for terminal activation
            phaseData.put("terminalsActive", true);
        }
        
        private void completeTerminals() {
            logger.info("Completing terminals for " + bossType.getDisplayName());
            phaseData.put("terminalsActive", false);
        }
        
        private void startBossFightPhase1() {
            logger.info("Starting Boss Fight Phase 1 for " + bossType.getDisplayName());
            // Implementation for phase 1 mechanics
        }
        
        private void completeBossFightPhase1() {
            logger.info("Completing Boss Fight Phase 1 for " + bossType.getDisplayName());
        }
        
        private void startBossFightPhase2() {
            logger.info("Starting Boss Fight Phase 2 for " + bossType.getDisplayName());
            // Implementation for phase 2 mechanics
        }
        
        private void completeBossFightPhase2() {
            logger.info("Completing Boss Fight Phase 2 for " + bossType.getDisplayName());
        }
        
        private void startBossFightPhase3() {
            logger.info("Starting Boss Fight Phase 3 for " + bossType.getDisplayName());
            // Implementation for phase 3 mechanics
        }
        
        private void completeBossFightPhase3() {
            logger.info("Completing Boss Fight Phase 3 for " + bossType.getDisplayName());
        }
        
        private void startBossFightPhase4() {
            logger.info("Starting Boss Fight Phase 4 for " + bossType.getDisplayName());
            // Implementation for phase 4 mechanics
        }
        
        private void completeBossFightPhase4() {
            logger.info("Completing Boss Fight Phase 4 for " + bossType.getDisplayName());
        }
        
        // Phase-specific implementations for Storm
        private void startLightningPhase() {
            logger.info("Starting Lightning Phase for " + bossType.getDisplayName());
            // Implementation for lightning mechanics
        }
        
        private void completeLightningPhase() {
            logger.info("Completing Lightning Phase for " + bossType.getDisplayName());
        }
        
        private void startStormPhase() {
            logger.info("Starting Storm Phase for " + bossType.getDisplayName());
            // Implementation for storm mechanics
        }
        
        private void completeStormPhase() {
            logger.info("Completing Storm Phase for " + bossType.getDisplayName());
        }
        
        // Phase-specific implementations for Goldor
        private void startTankPhase() {
            logger.info("Starting Tank Phase for " + bossType.getDisplayName());
            // Implementation for tank mechanics
        }
        
        private void completeTankPhase() {
            logger.info("Completing Tank Phase for " + bossType.getDisplayName());
        }
        
        private void startShieldPhase() {
            logger.info("Starting Shield Phase for " + bossType.getDisplayName());
            // Implementation for shield mechanics
        }
        
        private void completeShieldPhase() {
            logger.info("Completing Shield Phase for " + bossType.getDisplayName());
        }
        
        // Phase-specific implementations for Maxor
        private void startSpeedPhase() {
            logger.info("Starting Speed Phase for " + bossType.getDisplayName());
            // Implementation for speed mechanics
        }
        
        private void completeSpeedPhase() {
            logger.info("Completing Speed Phase for " + bossType.getDisplayName());
        }
        
        private void startBerserkPhase() {
            logger.info("Starting Berserk Phase for " + bossType.getDisplayName());
            // Implementation for berserk mechanics
        }
        
        private void completeBerserkPhase() {
            logger.info("Completing Berserk Phase for " + bossType.getDisplayName());
        }
        
        // Final phase implementation
        private void startFinalPhase() {
            logger.info("Starting Final Phase for " + bossType.getDisplayName());
            // Implementation for final phase mechanics
        }
        
        private void completeFinalPhase() {
            logger.info("Completing Final Phase for " + bossType.getDisplayName());
            isActive = false;
        }
        
        // Getters
        public UUID getBossId() { return bossId; }
        public BossType getBossType() { return bossType; }
        public BossPhase getCurrentPhase() { return currentPhase; }
        public int getCurrentPhaseIndex() { return currentPhaseIndex; }
        public double getCurrentHealth() { return currentHealth; }
        public long getPhaseStartTime() { return phaseStartTime; }
        public boolean isActive() { return isActive; }
        public Map<String, Object> getPhaseData() { return phaseData; }
        public List<UUID> getParticipantIds() { return new ArrayList<>(participants.keySet()); }
    }
    
    public BossStateMachineSystem() {
        this.stateMachineScheduler = Executors.newScheduledThreadPool(4, r -> new Thread(r, "BossStateMachine-Thread"));
        startStateMachineUpdateTask();
    }
    
    /**
     * Create a new boss state machine
     */
    public CompletableFuture<BossStateMachine> createBossStateMachine(BossType bossType, 
                                                                     List<UUID> participantIds, 
                                                                     Location location) {
        return CompletableFuture.supplyAsync(() -> {
            UUID bossId = UUID.randomUUID();
            BossStateMachine stateMachine = new BossStateMachine(bossId, bossType, participantIds, location);
            
            activeBosses.put(bossId, stateMachine);
            
            logger.info("Created boss state machine for " + bossType.getDisplayName() + " with " + 
                       participantIds.size() + " participants");
            
            return stateMachine;
        });
    }
    
    /**
     * Update boss health and trigger state transitions
     */
    public CompletableFuture<Void> updateBossHealth(UUID bossId, double newHealth) {
        return CompletableFuture.runAsync(() -> {
            BossStateMachine stateMachine = activeBosses.get(bossId);
            if (stateMachine != null) {
                stateMachine.updateHealth(newHealth);
            }
        });
    }
    
    /**
     * Get boss state machine
     */
    public BossStateMachine getBossStateMachine(UUID bossId) {
        return activeBosses.get(bossId);
    }
    
    /**
     * Remove boss state machine
     */
    public void removeBossStateMachine(UUID bossId) {
        BossStateMachine stateMachine = activeBosses.remove(bossId);
        if (stateMachine != null) {
            logger.info("Removed boss state machine for " + stateMachine.getBossType().getDisplayName());
        }
    }
    
    /**
     * Start state machine update task
     */
    private void startStateMachineUpdateTask() {
        stateMachineScheduler.scheduleAtFixedRate(() -> {
            updateAllStateMachines();
        }, 0, 100, TimeUnit.MILLISECONDS); // Update every 100ms
    }
    
    /**
     * Update all active state machines
     */
    private void updateAllStateMachines() {
        Iterator<Map.Entry<UUID, BossStateMachine>> iterator = activeBosses.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<UUID, BossStateMachine> entry = iterator.next();
            BossStateMachine stateMachine = entry.getValue();
            
            if (!stateMachine.isActive()) {
                iterator.remove();
                continue;
            }
            
            // Check for time-based transitions
            if (stateMachine.canTransitionToNextPhase()) {
                stateMachine.transitionToNextPhase();
            }
        }
    }
    
    /**
     * Get all active boss state machines
     */
    public Map<UUID, BossStateMachine> getActiveBosses() {
        return new HashMap<>(activeBosses);
    }
    
    /**
     * Get boss statistics
     */
    public Map<String, Object> getBossStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("activeBosses", activeBosses.size());
        
        Map<String, Integer> bossTypeCounts = new HashMap<>();
        for (BossStateMachine stateMachine : activeBosses.values()) {
            String bossType = stateMachine.getBossType().name();
            bossTypeCounts.merge(bossType, 1, Integer::sum);
        }
        stats.put("bossTypeCounts", bossTypeCounts);
        
        Map<String, Integer> phaseCounts = new HashMap<>();
        for (BossStateMachine stateMachine : activeBosses.values()) {
            String phase = stateMachine.getCurrentPhase().name();
            phaseCounts.merge(phase, 1, Integer::sum);
        }
        stats.put("phaseCounts", phaseCounts);
        
        return stats;
    }
    
    /**
     * Shutdown the state machine system
     */
    public void shutdown() {
        stateMachineScheduler.shutdown();
        try {
            if (!stateMachineScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                stateMachineScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            stateMachineScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
