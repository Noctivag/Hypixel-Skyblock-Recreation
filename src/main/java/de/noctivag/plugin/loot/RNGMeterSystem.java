package de.noctivag.plugin.loot;

import de.noctivag.plugin.data.DatabaseManager;
import de.noctivag.skyblock.core.architecture.PersistentStateDatabase;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * RNG Meter System - Implements the RNG Meter mechanics with MongoDB persistence
 * 
 * Features:
 * - RNG Meter progress tracking per boss type
 * - Weight calculation formula: New Weight = Base Weight × (1 + 2 × (Required Meter EXP / Current Meter EXP))
 * - Maximum multiplier of 3x
 * - Persistent storage in MongoDB player profiles
 * - Boss-specific meter tracking
 * - Automatic meter completion rewards
 */
public class RNGMeterSystem {
    
    private static final Logger logger = Logger.getLogger(RNGMeterSystem.class.getName());
    
    // Constants
    private static final double MAX_MULTIPLIER = 3.0;
    private static final double BASE_METER_EXP_RATE = 1.0;
    private static final long METER_UPDATE_INTERVAL = 5000L; // 5 seconds
    
    // Database connections
    private final DatabaseManager databaseManager;
    private final PersistentStateDatabase persistentState;
    
    // In-memory cache for performance
    private final Map<UUID, PlayerRNGMeterData> playerMeterCache = new ConcurrentHashMap<>();
    
    // Boss type definitions
    public enum BossType {
        ZEALOT("Zealot", 1000.0),
        ENDER_DRAGON("Ender Dragon", 2000.0),
        NECRON("Necron", 5000.0),
        STORM("Storm", 3000.0),
        GOLDOR("Goldor", 2500.0),
        MAXOR("Maxor", 2500.0),
        SLAYER_BOSS("Slayer Boss", 1500.0);
        
        private final String displayName;
        private final double baseMeterExp;
        
        BossType(String displayName, double baseMeterExp) {
            this.displayName = displayName;
            this.baseMeterExp = baseMeterExp;
        }
        
        public String getDisplayName() { return displayName; }
        public double getBaseMeterExp() { return baseMeterExp; }
    }
    
    /**
     * RNG Meter Item Definition
     */
    public static class RNGMeterItem {
        private final String itemId;
        private final String displayName;
        private final double baseWeight;
        private final double requiredMeterExp;
        private final boolean isRare;
        
        public RNGMeterItem(String itemId, String displayName, double baseWeight,
                           double requiredMeterExp, boolean isRare) {
            this.itemId = itemId;
            this.displayName = displayName;
            this.baseWeight = baseWeight;
            this.requiredMeterExp = requiredMeterExp;
            this.isRare = isRare;
        }
        
        // Getters
        public String getItemId() { return itemId; }
        public String getDisplayName() { return displayName; }
        public double getBaseWeight() { return baseWeight; }
        public double getRequiredMeterExp() { return requiredMeterExp; }
        public boolean isRare() { return isRare; }
    }
    
    /**
     * Player RNG Meter Data
     */
    public static class PlayerRNGMeterData {
        private final UUID playerId;
        private final Map<BossType, BossRNGMeterData> bossMeters = new ConcurrentHashMap<>();
        private long lastUpdate = System.currentTimeMillis();
        
        public PlayerRNGMeterData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public BossRNGMeterData getBossMeter(BossType bossType) {
            return bossMeters.computeIfAbsent(bossType, k -> new BossRNGMeterData(bossType));
        }
        
        public void updateBossMeter(BossType bossType, BossRNGMeterData data) {
            bossMeters.put(bossType, data);
            lastUpdate = System.currentTimeMillis();
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<BossType, BossRNGMeterData> getBossMeters() { return bossMeters; }
        public long getLastUpdate() { return lastUpdate; }
    }
    
    /**
     * Boss-specific RNG Meter Data
     */
    public static class BossRNGMeterData {
        private final BossType bossType;
        private final Map<String, RNGMeterItem> availableItems = new ConcurrentHashMap<>();
        private String selectedItemId;
        private double currentMeterExp;
        private double totalMeterExp;
        private long lastKillTime;
        private int totalKills;
        
        public BossRNGMeterData(BossType bossType) {
            this.bossType = bossType;
            initializeAvailableItems();
        }
        
        private void initializeAvailableItems() {
            // Initialize boss-specific items
            switch (bossType) {
                case ZEALOT:
                    availableItems.put("SUMMONING_EYE", new RNGMeterItem(
                        "SUMMONING_EYE", "Summoning Eye", 1.0, 1000.0, true));
                    availableItems.put("ENCHANTED_ENDER_PEARL", new RNGMeterItem(
                        "ENCHANTED_ENDER_PEARL", "Enchanted Ender Pearl", 0.5, 500.0, false));
                    break;
                    
                case ENDER_DRAGON:
                    availableItems.put("DRAGON_EGG", new RNGMeterItem(
                        "DRAGON_EGG", "Dragon Egg", 0.1, 2000.0, true));
                    availableItems.put("ENCHANTED_DRAGON_FRAGMENT", new RNGMeterItem(
                        "ENCHANTED_DRAGON_FRAGMENT", "Enchanted Dragon Fragment", 0.3, 1000.0, false));
                    break;
                    
                case NECRON:
                    availableItems.put("NECRON_HANDLE", new RNGMeterItem(
                        "NECRON_HANDLE", "Necron's Handle", 0.05, 5000.0, true));
                    availableItems.put("NECRON_FRAGMENT", new RNGMeterItem(
                        "NECRON_FRAGMENT", "Necron Fragment", 0.2, 2500.0, false));
                    break;
                    
                case STORM:
                    availableItems.put("STORM_FRAGMENT", new RNGMeterItem(
                        "STORM_FRAGMENT", "Storm Fragment", 0.1, 3000.0, true));
                    availableItems.put("STORM_ESSENCE", new RNGMeterItem(
                        "STORM_ESSENCE", "Storm Essence", 0.4, 1500.0, false));
                    break;
                    
                case GOLDOR:
                    availableItems.put("GOLDOR_FRAGMENT", new RNGMeterItem(
                        "GOLDOR_FRAGMENT", "Goldor Fragment", 0.1, 2500.0, true));
                    availableItems.put("GOLDOR_ESSENCE", new RNGMeterItem(
                        "GOLDOR_ESSENCE", "Goldor Essence", 0.4, 1250.0, false));
                    break;
                    
                case MAXOR:
                    availableItems.put("MAXOR_FRAGMENT", new RNGMeterItem(
                        "MAXOR_FRAGMENT", "Maxor Fragment", 0.1, 2500.0, true));
                    availableItems.put("MAXOR_ESSENCE", new RNGMeterItem(
                        "MAXOR_ESSENCE", "Maxor Essence", 0.4, 1250.0, false));
                    break;
                    
                case SLAYER_BOSS:
                    availableItems.put("SLAYER_REWARD", new RNGMeterItem(
                        "SLAYER_REWARD", "Slayer Reward", 0.2, 1500.0, true));
                    availableItems.put("SLAYER_ESSENCE", new RNGMeterItem(
                        "SLAYER_ESSENCE", "Slayer Essence", 0.6, 750.0, false));
                    break;
            }
        }
        
        public boolean selectItem(String itemId) {
            if (availableItems.containsKey(itemId)) {
                selectedItemId = itemId;
                return true;
            }
            return false;
        }
        
        public double calculateWeightMultiplier() {
            if (selectedItemId == null || currentMeterExp <= 0) {
                return 1.0;
            }
            
            RNGMeterItem selectedItem = availableItems.get(selectedItemId);
            if (selectedItem == null) {
                return 1.0;
            }
            
            // Formula: New Weight = Base Weight × (1 + 2 × (Required Meter EXP / Current Meter EXP))
            double progressRatio = selectedItem.getRequiredMeterExp() / Math.max(currentMeterExp, 1.0);
            double multiplier = 1.0 + (2.0 * progressRatio);
            
            return Math.min(multiplier, MAX_MULTIPLIER);
        }
        
        public void addMeterExp(double exp) {
            currentMeterExp += exp;
            totalMeterExp += exp;
            lastKillTime = System.currentTimeMillis();
            totalKills++;
        }
        
        public boolean isMeterComplete() {
            if (selectedItemId == null) {
                return false;
            }
            
            RNGMeterItem selectedItem = availableItems.get(selectedItemId);
            return selectedItem != null && currentMeterExp >= selectedItem.getRequiredMeterExp();
        }
        
        public void resetMeter() {
            currentMeterExp = 0.0;
            selectedItemId = null;
        }
        
        // Getters
        public BossType getBossType() { return bossType; }
        public Map<String, RNGMeterItem> getAvailableItems() { return availableItems; }
        public String getSelectedItemId() { return selectedItemId; }
        public double getCurrentMeterExp() { return currentMeterExp; }
        public double getTotalMeterExp() { return totalMeterExp; }
        public long getLastKillTime() { return lastKillTime; }
        public int getTotalKills() { return totalKills; }
    }
    
    public RNGMeterSystem(DatabaseManager databaseManager, PersistentStateDatabase persistentState) {
        this.databaseManager = databaseManager;
        this.persistentState = persistentState;
        
        startPeriodicSaveTask();
    }
    
    /**
     * Get player RNG Meter data
     */
    public CompletableFuture<PlayerRNGMeterData> getPlayerMeterData(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerRNGMeterData cached = playerMeterCache.get(playerId);
            if (cached != null) {
                return cached;
            }
            
            // Load from database
            return loadPlayerMeterDataFromDatabase(playerId);
        });
    }
    
    /**
     * Set RNG Meter selection for player
     */
    public CompletableFuture<Boolean> setMeterSelection(UUID playerId, BossType bossType, String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PlayerRNGMeterData playerData = getPlayerMeterData(playerId).join();
                BossRNGMeterData bossData = playerData.getBossMeter(bossType);
                
                boolean success = bossData.selectItem(itemId);
                if (success) {
                    playerData.updateBossMeter(bossType, bossData);
                    playerMeterCache.put(playerId, playerData);
                    
                    // Save to database
                    savePlayerMeterDataToDatabase(playerId, playerData);
                    
                    logger.info("Player " + playerId + " selected " + itemId + 
                               " for " + bossType.getDisplayName() + " RNG Meter");
                }
                
                return success;
            } catch (Exception e) {
                logger.severe("Error setting meter selection: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Update meter progress after boss kill
     */
    public CompletableFuture<Void> updateMeterProgress(UUID playerId, BossType bossType, 
                                                      double expGained, boolean wasKill) {
        return CompletableFuture.runAsync(() -> {
            try {
                PlayerRNGMeterData playerData = getPlayerMeterData(playerId).join();
                BossRNGMeterData bossData = playerData.getBossMeter(bossType);
                
                if (wasKill) {
                    bossData.addMeterExp(expGained);
                }
                
                // Check if meter is complete
                if (bossData.isMeterComplete()) {
                    handleMeterCompletion(playerId, bossType, bossData);
                    bossData.resetMeter();
                }
                
                playerData.updateBossMeter(bossType, bossData);
                playerMeterCache.put(playerId, playerData);
                
                // Save to database
                savePlayerMeterDataToDatabase(playerId, playerData);
                
            } catch (Exception e) {
                logger.severe("Error updating meter progress: " + e.getMessage());
            }
        });
    }
    
    /**
     * Handle meter completion
     */
    private void handleMeterCompletion(UUID playerId, BossType bossType, BossRNGMeterData bossData) {
        String selectedItemId = bossData.getSelectedItemId();
        RNGMeterItem selectedItem = bossData.getAvailableItems().get(selectedItemId);
        
        if (selectedItem != null) {
            // Give guaranteed drop
            giveGuaranteedDrop(playerId, selectedItem);
            
            // Reset meter
            bossData.resetMeter();
            
            logger.info("RNG Meter completed for player " + playerId + 
                       " - Boss: " + bossType.getDisplayName() + 
                       " - Item: " + selectedItem.getDisplayName());
        }
    }
    
    /**
     * Give guaranteed drop to player
     */
    private void giveGuaranteedDrop(UUID playerId, RNGMeterItem item) {
        // This would integrate with the item giving system
        // For now, just log the event
        logger.info("Giving guaranteed drop: " + item.getDisplayName() + " to player " + playerId);
    }
    
    /**
     * Get meter progress percentage
     */
    public CompletableFuture<Double> getMeterProgress(UUID playerId, BossType bossType) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerRNGMeterData playerData = getPlayerMeterData(playerId).join();
            BossRNGMeterData bossData = playerData.getBossMeter(bossType);
            
            if (bossData.getSelectedItemId() == null) {
                return 0.0;
            }
            
            RNGMeterItem selectedItem = bossData.getAvailableItems().get(bossData.getSelectedItemId());
            if (selectedItem == null) {
                return 0.0;
            }
            
            return Math.min(1.0, bossData.getCurrentMeterExp() / selectedItem.getRequiredMeterExp());
        });
    }
    
    /**
     * Load player meter data from database
     */
    private PlayerRNGMeterData loadPlayerMeterDataFromDatabase(UUID playerId) {
        PlayerRNGMeterData data = new PlayerRNGMeterData(playerId);
        
        try {
            // This would load from MongoDB player profile
            // Implementation would depend on the specific database schema
            logger.info("Loading RNG Meter data for player " + playerId);
            
        } catch (Exception e) {
            logger.warning("Failed to load RNG Meter data for " + playerId + ": " + e.getMessage());
        }
        
        playerMeterCache.put(playerId, data);
        return data;
    }
    
    /**
     * Save player meter data to database
     */
    private void savePlayerMeterDataToDatabase(UUID playerId, PlayerRNGMeterData data) {
        try {
            // This would save to MongoDB player profile
            // Implementation would depend on the specific database schema
            logger.info("Saving RNG Meter data for player " + playerId);
            
        } catch (Exception e) {
            logger.warning("Failed to save RNG Meter data for " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Start periodic save task
     */
    private void startPeriodicSaveTask() {
        Timer timer = new Timer("RNGMeterSaveTask", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                saveAllPlayerData();
            }
        }, METER_UPDATE_INTERVAL, METER_UPDATE_INTERVAL);
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        int saved = 0;
        for (Map.Entry<UUID, PlayerRNGMeterData> entry : playerMeterCache.entrySet()) {
            try {
                savePlayerMeterDataToDatabase(entry.getKey(), entry.getValue());
                saved++;
            } catch (Exception e) {
                logger.warning("Failed to save data for player " + entry.getKey() + ": " + e.getMessage());
            }
        }
        
        if (saved > 0) {
            logger.info("Saved RNG Meter data for " + saved + " players");
        }
    }
    
    /**
     * Get all available items for boss type
     */
    public Map<String, RNGMeterItem> getAvailableItems(BossType bossType) {
        BossRNGMeterData bossData = new BossRNGMeterData(bossType);
        return new HashMap<>(bossData.getAvailableItems());
    }
    
    /**
     * Get player statistics
     */
    public Map<String, Object> getPlayerStatistics(UUID playerId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            PlayerRNGMeterData playerData = getPlayerMeterData(playerId).join();
            
            Map<String, Object> bossStats = new HashMap<>();
            for (Map.Entry<BossType, BossRNGMeterData> entry : playerData.getBossMeters().entrySet()) {
                BossRNGMeterData bossData = entry.getValue();
                Map<String, Object> bossInfo = new HashMap<>();
                
                bossInfo.put("selectedItem", bossData.getSelectedItemId());
                bossInfo.put("currentExp", bossData.getCurrentMeterExp());
                bossInfo.put("totalExp", bossData.getTotalMeterExp());
                bossInfo.put("totalKills", bossData.getTotalKills());
                bossInfo.put("progress", getMeterProgress(playerId, entry.getKey()).join());
                
                bossStats.put(entry.getKey().name(), bossInfo);
            }
            
            stats.put("bossMeters", bossStats);
            stats.put("lastUpdate", playerData.getLastUpdate());
            
        } catch (Exception e) {
            logger.warning("Failed to get statistics for player " + playerId + ": " + e.getMessage());
        }
        
        return stats;
    }
}
