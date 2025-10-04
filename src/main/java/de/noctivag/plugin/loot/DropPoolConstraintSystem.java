package de.noctivag.plugin.loot;

import de.noctivag.plugin.data.DatabaseManager;
import de.noctivag.skyblock.core.architecture.PersistentStateDatabase;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Drop Pool Constraint System - Critical mechanism to prevent market flooding
 * 
 * Priority: HIGHEST CRITICALITY
 * 
 * This system ensures that from each Drop Pool ID, maximum one item can be selected
 * as final drop per mob kill, regardless of how many rolls were successful.
 * 
 * Drop Pool IDs examples:
 * - "LAPIS_ARMOR" - All lapis armor pieces
 * - "NECRON_HANDLE" - Necron's Handle and related items
 * - "DRAGON_FRAGMENTS" - Dragon fragments of all types
 * - "PET_DROPS" - Pet drops from specific bosses
 */
public class DropPoolConstraintSystem {
    
    private static final Logger logger = Logger.getLogger(DropPoolConstraintSystem.class.getName());
    
    // Global drop pool registry
    private final Map<String, DropPoolDefinition> dropPoolRegistry = new ConcurrentHashMap<>();
    
    // Active constraint tracking per mob instance
    private final Map<UUID, MobDropSession> activeDropSessions = new ConcurrentHashMap<>();
    
    // Player drop history for economy analysis
    private final Map<UUID, PlayerDropHistory> playerDropHistories = new ConcurrentHashMap<>();
    
    // Database connections
    private final DatabaseManager databaseManager;
    private final PersistentStateDatabase persistentState;
    
    // Configuration
    private static final long SESSION_TIMEOUT = 300000L; // 5 minutes
    private static final int MAX_DROPS_PER_POOL_PER_DAY = 10; // Economy constraint
    
    /**
     * Drop Pool Definition - Defines a group of items that share drop constraints
     */
    public static class DropPoolDefinition {
        private final String poolId;
        private final String description;
        private final Set<String> itemIds;
        private final double globalWeight;
        private final boolean isEconomyCritical;
        private final int maxDropsPerDay;
        
        public DropPoolDefinition(String poolId, String description, Set<String> itemIds,
                                double globalWeight, boolean isEconomyCritical, int maxDropsPerDay) {
            this.poolId = poolId;
            this.description = description;
            this.itemIds = new HashSet<>(itemIds);
            this.globalWeight = globalWeight;
            this.isEconomyCritical = isEconomyCritical;
            this.maxDropsPerDay = maxDropsPerDay;
        }
        
        // Getters
        public String getPoolId() { return poolId; }
        public String getDescription() { return description; }
        public Set<String> getItemIds() { return itemIds; }
        public double getGlobalWeight() { return globalWeight; }
        public boolean isEconomyCritical() { return isEconomyCritical; }
        public int getMaxDropsPerDay() { return maxDropsPerDay; }
    }
    
    /**
     * Mob Drop Session - Tracks drops for a single mob kill
     */
    public static class MobDropSession {
        private final UUID mobId;
        private final UUID killerId;
        private final long startTime;
        private final Map<String, DropAttempt> dropAttempts = new ConcurrentHashMap<>();
        private final Set<String> usedDropPools = ConcurrentHashMap.newKeySet();
        private final List<LootDrop> finalDrops = new ArrayList<>();
        
        public MobDropSession(UUID mobId, UUID killerId) {
            this.mobId = mobId;
            this.killerId = killerId;
            this.startTime = System.currentTimeMillis();
        }
        
        public boolean canDropFromPool(String dropPoolId) {
            return !usedDropPools.contains(dropPoolId);
        }
        
        public void recordDropAttempt(String dropPoolId, DropAttempt attempt) {
            dropAttempts.put(dropPoolId, attempt);
            if (attempt.isSuccessful()) {
                usedDropPools.add(dropPoolId);
                finalDrops.add(attempt.getLootDrop());
            }
        }
        
        public boolean isExpired() {
            return (System.currentTimeMillis() - startTime) > SESSION_TIMEOUT;
        }
        
        // Getters
        public UUID getMobId() { return mobId; }
        public UUID getKillerId() { return killerId; }
        public long getStartTime() { return startTime; }
        public Map<String, DropAttempt> getDropAttempts() { return dropAttempts; }
        public Set<String> getUsedDropPools() { return usedDropPools; }
        public List<LootDrop> getFinalDrops() { return finalDrops; }
    }
    
    /**
     * Drop Attempt - Records a single drop calculation attempt
     */
    public static class DropAttempt {
        private final String itemId;
        private final String dropPoolId;
        private final double calculatedChance;
        private final boolean successful;
        private final LootDrop lootDrop;
        private final String reason;
        
        public DropAttempt(String itemId, String dropPoolId, double calculatedChance,
                          boolean successful, LootDrop lootDrop, String reason) {
            this.itemId = itemId;
            this.dropPoolId = dropPoolId;
            this.calculatedChance = calculatedChance;
            this.successful = successful;
            this.lootDrop = lootDrop;
            this.reason = reason;
        }
        
        // Getters
        public String getItemId() { return itemId; }
        public String getDropPoolId() { return dropPoolId; }
        public double getCalculatedChance() { return calculatedChance; }
        public boolean isSuccessful() { return successful; }
        public LootDrop getLootDrop() { return lootDrop; }
        public String getReason() { return reason; }
    }
    
    /**
     * Player Drop History - Tracks player drop patterns for economy analysis
     */
    public static class PlayerDropHistory {
        private final UUID playerId;
        private final Map<String, Integer> dailyDrops = new ConcurrentHashMap<>();
        private final Map<String, Long> lastDropTime = new ConcurrentHashMap<>();
        private final List<DropRecord> recentDrops = new ArrayList<>();
        private long lastReset = System.currentTimeMillis();
        
        public PlayerDropHistory(UUID playerId) {
            this.playerId = playerId;
        }
        
        public boolean canDropToday(String dropPoolId, int maxDropsPerDay) {
            // Reset daily counters if new day
            if (isNewDay()) {
                dailyDrops.clear();
                lastDropTime.clear();
                lastReset = System.currentTimeMillis();
            }
            
            int todayDrops = dailyDrops.getOrDefault(dropPoolId, 0);
            return todayDrops < maxDropsPerDay;
        }
        
        public void recordDrop(String dropPoolId, String itemId, int amount) {
            dailyDrops.merge(dropPoolId, 1, Integer::sum);
            lastDropTime.put(dropPoolId, System.currentTimeMillis());
            
            recentDrops.add(new DropRecord(itemId, dropPoolId, amount, System.currentTimeMillis()));
            
            // Keep only last 100 drops
            if (recentDrops.size() > 100) {
                recentDrops.remove(0);
            }
        }
        
        private boolean isNewDay() {
            long now = System.currentTimeMillis();
            return (now - lastReset) >= TimeUnit.DAYS.toMillis(1);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<String, Integer> getDailyDrops() { return dailyDrops; }
        public Map<String, Long> getLastDropTime() { return lastDropTime; }
        public List<DropRecord> getRecentDrops() { return recentDrops; }
    }
    
    /**
     * Drop Record - Historical record of a drop
     */
    public static class DropRecord {
        private final String itemId;
        private final String dropPoolId;
        private final int amount;
        private final long timestamp;
        
        public DropRecord(String itemId, String dropPoolId, int amount, long timestamp) {
            this.itemId = itemId;
            this.dropPoolId = dropPoolId;
            this.amount = amount;
            this.timestamp = timestamp;
        }
        
        // Getters
        public String getItemId() { return itemId; }
        public String getDropPoolId() { return dropPoolId; }
        public int getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
    
    public DropPoolConstraintSystem(DatabaseManager databaseManager, 
                                  PersistentStateDatabase persistentState) {
        this.databaseManager = databaseManager;
        this.persistentState = persistentState;
        
        initializeDropPoolRegistry();
        startCleanupTask();
    }
    
    /**
     * Initialize the drop pool registry with predefined pools
     */
    private void initializeDropPoolRegistry() {
        // Lapis Armor Pool
        registerDropPool(new DropPoolDefinition(
            "LAPIS_ARMOR",
            "Lapis Armor Pieces",
            Set.of("LAPIS_HELMET", "LAPIS_CHESTPLATE", "LAPIS_LEGGINGS", "LAPIS_BOOTS"),
            1.0,
            true,
            5
        ));
        
        // Necron's Handle Pool
        registerDropPool(new DropPoolDefinition(
            "NECRON_HANDLE",
            "Necron's Handle and Related Items",
            Set.of("NECRON_HANDLE", "NECRON_BLADE", "NECRON_FRAGMENT"),
            0.1,
            true,
            1
        ));
        
        // Dragon Fragments Pool
        registerDropPool(new DropPoolDefinition(
            "DRAGON_FRAGMENTS",
            "Dragon Fragments of All Types",
            Set.of("DRAGON_FRAGMENT_WISE", "DRAGON_FRAGMENT_UNSTABLE", "DRAGON_FRAGMENT_STRONG",
                   "DRAGON_FRAGMENT_YOUNG", "DRAGON_FRAGMENT_OLD", "DRAGON_FRAGMENT_PROTECTOR",
                   "DRAGON_FRAGMENT_SUPERIOR"),
            0.5,
            true,
            3
        ));
        
        // Pet Drops Pool
        registerDropPool(new DropPoolDefinition(
            "PET_DROPS",
            "Pet Drops from Bosses",
            Set.of("PET_ENDERMAN", "PET_DRAGON", "PET_SKELETON", "PET_SPIDER", "PET_ZOMBIE"),
            0.2,
            false,
            2
        ));
        
        // Dungeon Chest Pool
        registerDropPool(new DropPoolDefinition(
            "DUNGEON_CHEST",
            "Dungeon Chest Items",
            Set.of("DUNGEON_CHEST_KEY", "DUNGEON_ESSENCE", "DUNGEON_ORB"),
            2.0,
            false,
            10
        ));
        
        logger.info("Initialized " + dropPoolRegistry.size() + " drop pools");
    }
    
    /**
     * Register a new drop pool
     */
    public void registerDropPool(DropPoolDefinition definition) {
        dropPoolRegistry.put(definition.getPoolId(), definition);
        logger.info("Registered drop pool: " + definition.getPoolId());
    }
    
    /**
     * Start a new mob drop session
     */
    public MobDropSession startMobDropSession(UUID mobId, UUID killerId) {
        MobDropSession session = new MobDropSession(mobId, killerId);
        activeDropSessions.put(mobId, session);
        return session;
    }
    
    /**
     * Process drop attempt through constraint system
     */
    public CompletableFuture<DropAttempt> processDropAttempt(UUID mobId, String itemId, 
                                                            String dropPoolId, double calculatedChance,
                                                            LootDrop lootDrop) {
        return CompletableFuture.supplyAsync(() -> {
            MobDropSession session = activeDropSessions.get(mobId);
            if (session == null) {
                return new DropAttempt(itemId, dropPoolId, calculatedChance, false, null,
                                     "No active drop session for mob");
            }
            
            DropPoolDefinition poolDef = dropPoolRegistry.get(dropPoolId);
            if (poolDef == null) {
                return new DropAttempt(itemId, dropPoolId, calculatedChance, false, null,
                                     "Unknown drop pool: " + dropPoolId);
            }
            
            // Check if item belongs to this drop pool
            if (!poolDef.getItemIds().contains(itemId)) {
                return new DropAttempt(itemId, dropPoolId, calculatedChance, false, null,
                                     "Item does not belong to drop pool");
            }
            
            // Check Drop Pool ID constraint (highest priority)
            if (!session.canDropFromPool(dropPoolId)) {
                return new DropAttempt(itemId, dropPoolId, calculatedChance, false, null,
                                     "Drop Pool constraint: Already dropped from this pool");
            }
            
            // Check daily economy constraint
            PlayerDropHistory history = getPlayerDropHistory(session.getKillerId());
            if (!history.canDropToday(dropPoolId, poolDef.getMaxDropsPerDay())) {
                return new DropAttempt(itemId, dropPoolId, calculatedChance, false, null,
                                     "Daily economy constraint: Max drops reached for pool");
            }
            
            // Roll for drop
            boolean successful = Math.random() < calculatedChance;
            
            DropAttempt attempt = new DropAttempt(itemId, dropPoolId, calculatedChance,
                                                successful, successful ? lootDrop : null,
                                                successful ? "Drop successful" : "Drop failed");
            
            // Record the attempt
            session.recordDropAttempt(dropPoolId, attempt);
            
            // Update player history if successful
            if (successful) {
                history.recordDrop(dropPoolId, itemId, lootDrop.getAmount());
            }
            
            return attempt;
        });
    }
    
    /**
     * Finalize mob drop session and return final drops
     */
    public CompletableFuture<List<LootDrop>> finalizeMobDropSession(UUID mobId) {
        return CompletableFuture.supplyAsync(() -> {
            MobDropSession session = activeDropSessions.remove(mobId);
            if (session == null) {
                return new ArrayList<>();
            }
            
            List<LootDrop> finalDrops = new ArrayList<>(session.getFinalDrops());
            
            // Log session summary
            logger.info("Finalized drop session for mob " + mobId + 
                       " - Final drops: " + finalDrops.size() + 
                       " - Attempted pools: " + session.getDropAttempts().size());
            
            return finalDrops;
        });
    }
    
    /**
     * Get player drop history
     */
    private PlayerDropHistory getPlayerDropHistory(UUID playerId) {
        return playerDropHistories.computeIfAbsent(playerId, PlayerDropHistory::new);
    }
    
    /**
     * Get drop pool definition
     */
    public DropPoolDefinition getDropPoolDefinition(String poolId) {
        return dropPoolRegistry.get(poolId);
    }
    
    /**
     * Get all registered drop pools
     */
    public Map<String, DropPoolDefinition> getAllDropPools() {
        return new HashMap<>(dropPoolRegistry);
    }
    
    /**
     * Get player drop statistics
     */
    public PlayerDropHistory getPlayerDropStatistics(UUID playerId) {
        return playerDropHistories.get(playerId);
    }
    
    /**
     * Start cleanup task for expired sessions
     */
    private void startCleanupTask() {
        Timer timer = new Timer("DropPoolConstraintCleanup", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanupExpiredSessions();
            }
        }, 60000, 60000); // Every minute
    }
    
    /**
     * Clean up expired drop sessions
     */
    private void cleanupExpiredSessions() {
        int cleaned = 0;
        Iterator<Map.Entry<UUID, MobDropSession>> iterator = activeDropSessions.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<UUID, MobDropSession> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                cleaned++;
            }
        }
        
        if (cleaned > 0) {
            logger.info("Cleaned up " + cleaned + " expired drop sessions");
        }
    }
    
    /**
     * Get economy statistics for monitoring
     */
    public Map<String, Object> getEconomyStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Session statistics
        stats.put("activeSessions", activeDropSessions.size());
        stats.put("registeredPools", dropPoolRegistry.size());
        stats.put("trackedPlayers", playerDropHistories.size());
        
        // Pool usage statistics
        Map<String, Integer> poolUsage = new HashMap<>();
        for (MobDropSession session : activeDropSessions.values()) {
            for (String poolId : session.getUsedDropPools()) {
                poolUsage.merge(poolId, 1, Integer::sum);
            }
        }
        stats.put("poolUsage", poolUsage);
        
        return stats;
    }
}
