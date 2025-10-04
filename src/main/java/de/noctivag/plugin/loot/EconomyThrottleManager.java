package de.noctivag.plugin.loot;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Economy Throttle Manager - Manages Drop Pool ID constraints to prevent market destabilization
 * 
 * This system ensures that players cannot get multiple rare drops from the same pool
 * in a short time period, preventing economic exploitation through high Magic Find.
 */
public class EconomyThrottleManager {
    
    private final Map<String, PlayerDropHistory> playerHistories = new ConcurrentHashMap<>();
    private final Map<String, DropPoolConfig> dropPoolConfigs = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newScheduledThreadPool(1);
    
    // Default configuration
    private static final long DEFAULT_COOLDOWN = 300000L; // 5 minutes
    private static final int DEFAULT_MAX_DROPS_PER_POOL = 1;
    
    public EconomyThrottleManager() {
        // Start cleanup task every minute
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredEntries, 1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * Check if player can drop from a specific pool
     */
    public boolean canDropFromPool(Player player, String dropPoolId) {
        String playerId = player.getUniqueId().toString();
        PlayerDropHistory history = playerHistories.get(playerId);
        
        if (history == null) {
            return true; // No history, can drop
        }
        
        DropPoolConfig config = getDropPoolConfig(dropPoolId);
        
        // Check if player has reached the limit for this pool
        if (history.getDropsFromPool(dropPoolId) >= config.getMaxDropsPerPool()) {
            return false; // Reached limit
        }
        
        // Check cooldown
        Long lastDropTime = history.getLastDropTime(dropPoolId);
        if (lastDropTime != null) {
            long timeSinceLastDrop = System.currentTimeMillis() - lastDropTime;
            if (timeSinceLastDrop < config.getCooldown()) {
                return false; // Still in cooldown
            }
        }
        
        return true;
    }
    
    /**
     * Record a drop for economy throttling
     */
    public void recordDrop(Player player, String dropPoolId, String itemId) {
        String playerId = player.getUniqueId().toString();
        PlayerDropHistory history = playerHistories.computeIfAbsent(playerId, k -> new PlayerDropHistory());
        
        long currentTime = System.currentTimeMillis();
        history.recordDrop(dropPoolId, itemId, currentTime);
        
        // Log drop for monitoring
        logDrop(player, dropPoolId, itemId);
    }
    
    /**
     * Get player's drop statistics
     */
    public PlayerDropStatistics getPlayerStatistics(Player player) {
        String playerId = player.getUniqueId().toString();
        PlayerDropHistory history = playerHistories.get(playerId);
        
        if (history == null) {
            return new PlayerDropStatistics(0, new HashMap<>(), new HashMap<>());
        }
        
        return history.getStatistics();
    }
    
    /**
     * Reset player's drop history
     */
    public void resetPlayerHistory(Player player) {
        String playerId = player.getUniqueId().toString();
        playerHistories.remove(playerId);
    }
    
    /**
     * Configure drop pool settings
     */
    public void configureDropPool(String dropPoolId, long cooldown, int maxDropsPerPool) {
        dropPoolConfigs.put(dropPoolId, new DropPoolConfig(cooldown, maxDropsPerPool));
    }
    
    /**
     * Get drop pool configuration
     */
    private DropPoolConfig getDropPoolConfig(String dropPoolId) {
        return dropPoolConfigs.getOrDefault(dropPoolId, 
            new DropPoolConfig(DEFAULT_COOLDOWN, DEFAULT_MAX_DROPS_PER_POOL));
    }
    
    /**
     * Log drop for monitoring purposes
     */
    private void logDrop(Player player, String dropPoolId, String itemId) {
        // TODO: Implement logging to database or file
        System.out.println(String.format("Drop recorded: Player=%s, Pool=%s, Item=%s", 
            player.getName(), dropPoolId, itemId));
    }
    
    /**
     * Clean up expired entries
     */
    private void cleanupExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        
        playerHistories.entrySet().removeIf(entry -> {
            PlayerDropHistory history = entry.getValue();
            return history.isExpired(currentTime);
        });
    }
    
    /**
     * Shutdown the manager
     */
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Player Drop History - Tracks individual player drop history
     */
    private static class PlayerDropHistory {
        private final Map<String, List<DropRecord>> poolDrops = new ConcurrentHashMap<>();
        private final long creationTime = System.currentTimeMillis();
        private static final long EXPIRY_TIME = 3600000L; // 1 hour
        
        public void recordDrop(String dropPoolId, String itemId, long timestamp) {
            poolDrops.computeIfAbsent(dropPoolId, k -> new ArrayList<>())
                .add(new DropRecord(itemId, timestamp));
        }
        
        public int getDropsFromPool(String dropPoolId) {
            List<DropRecord> drops = poolDrops.get(dropPoolId);
            return drops != null ? drops.size() : 0;
        }
        
        public Long getLastDropTime(String dropPoolId) {
            List<DropRecord> drops = poolDrops.get(dropPoolId);
            if (drops == null || drops.isEmpty()) {
                return null;
            }
            
            return drops.stream()
                .mapToLong(DropRecord::getTimestamp)
                .max()
                .orElse(0L);
        }
        
        public PlayerDropStatistics getStatistics() {
            Map<String, Integer> dropsPerPool = new HashMap<>();
            Map<String, Long> lastDropTimes = new HashMap<>();
            
            for (Map.Entry<String, List<DropRecord>> entry : poolDrops.entrySet()) {
                String poolId = entry.getKey();
                List<DropRecord> drops = entry.getValue();
                
                dropsPerPool.put(poolId, drops.size());
                
                if (!drops.isEmpty()) {
                    Long lastTime = drops.stream()
                        .mapToLong(DropRecord::getTimestamp)
                        .max()
                        .orElse(0L);
                    lastDropTimes.put(poolId, lastTime);
                }
            }
            
            return new PlayerDropStatistics(
                poolDrops.values().stream().mapToInt(List::size).sum(),
                dropsPerPool,
                lastDropTimes
            );
        }
        
        public boolean isExpired(long currentTime) {
            return currentTime - creationTime > EXPIRY_TIME;
        }
    }
    
    /**
     * Drop Record - Individual drop record
     */
    private static class DropRecord {
        private final String itemId;
        private final long timestamp;
        
        public DropRecord(String itemId, long timestamp) {
            this.itemId = itemId;
            this.timestamp = timestamp;
        }
        
        public String getItemId() { return itemId; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Drop Pool Configuration
     */
    private static class DropPoolConfig {
        private final long cooldown;
        private final int maxDropsPerPool;
        
        public DropPoolConfig(long cooldown, int maxDropsPerPool) {
            this.cooldown = cooldown;
            this.maxDropsPerPool = maxDropsPerPool;
        }
        
        public long getCooldown() { return cooldown; }
        public int getMaxDropsPerPool() { return maxDropsPerPool; }
    }
    
    /**
     * Player Drop Statistics
     */
    public static class PlayerDropStatistics {
        private final int totalDrops;
        private final Map<String, Integer> dropsPerPool;
        private final Map<String, Long> lastDropTimes;
        
        public PlayerDropStatistics(int totalDrops, Map<String, Integer> dropsPerPool, Map<String, Long> lastDropTimes) {
            this.totalDrops = totalDrops;
            this.dropsPerPool = dropsPerPool;
            this.lastDropTimes = lastDropTimes;
        }
        
        public int getTotalDrops() { return totalDrops; }
        public Map<String, Integer> getDropsPerPool() { return dropsPerPool; }
        public Map<String, Long> getLastDropTimes() { return lastDropTimes; }
        
        public int getDropsFromPool(String poolId) {
            return dropsPerPool.getOrDefault(poolId, 0);
        }
        
        public Long getLastDropTime(String poolId) {
            return lastDropTimes.get(poolId);
        }
    }
}
