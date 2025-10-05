package de.noctivag.skyblock.brewing;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Metrics for brewing system
 */
public class BrewingMetrics {
    
    private final AtomicLong totalBrewingAttempts = new AtomicLong(0);
    private final AtomicLong totalBrewingSuccesses = new AtomicLong(0);
    private final AtomicLong totalBrewingFailures = new AtomicLong(0);
    private final AtomicLong totalExperienceGained = new AtomicLong(0);
    
    private final Map<UUID, Long> playerBrewingAttempts = new ConcurrentHashMap<>();
    private final Map<UUID, Long> playerBrewingSuccesses = new ConcurrentHashMap<>();
    private final Map<UUID, Long> playerBrewingFailures = new ConcurrentHashMap<>();
    private final Map<UUID, Long> playerExperienceGained = new ConcurrentHashMap<>();
    
    /**
     * Record brewing attempt
     */
    public void recordBrewingAttempt(UUID playerUuid) {
        totalBrewingAttempts.incrementAndGet();
        playerBrewingAttempts.merge(playerUuid, 1L, Long::sum);
    }
    
    /**
     * Record brewing success
     */
    public void recordBrewingSuccess(UUID playerUuid, long experience) {
        totalBrewingSuccesses.incrementAndGet();
        totalExperienceGained.addAndGet(experience);
        playerBrewingSuccesses.merge(playerUuid, 1L, Long::sum);
        playerExperienceGained.merge(playerUuid, experience, Long::sum);
    }
    
    /**
     * Record brewing failure
     */
    public void recordBrewingFailure(UUID playerUuid) {
        totalBrewingFailures.incrementAndGet();
        playerBrewingFailures.merge(playerUuid, 1L, Long::sum);
    }
    
    /**
     * Get total brewing attempts
     */
    public long getTotalBrewingAttempts() {
        return totalBrewingAttempts.get();
    }
    
    /**
     * Get total brewing successes
     */
    public long getTotalBrewingSuccesses() {
        return totalBrewingSuccesses.get();
    }
    
    /**
     * Get total brewing failures
     */
    public long getTotalBrewingFailures() {
        return totalBrewingFailures.get();
    }
    
    /**
     * Get total experience gained
     */
    public long getTotalExperienceGained() {
        return totalExperienceGained.get();
    }
    
    /**
     * Get player brewing attempts
     */
    public long getPlayerBrewingAttempts(UUID playerUuid) {
        return playerBrewingAttempts.getOrDefault(playerUuid, 0L);
    }
    
    /**
     * Get player brewing successes
     */
    public long getPlayerBrewingSuccesses(UUID playerUuid) {
        return playerBrewingSuccesses.getOrDefault(playerUuid, 0L);
    }
    
    /**
     * Get player brewing failures
     */
    public long getPlayerBrewingFailures(UUID playerUuid) {
        return playerBrewingFailures.getOrDefault(playerUuid, 0L);
    }
    
    /**
     * Get player experience gained
     */
    public long getPlayerExperienceGained(UUID playerUuid) {
        return playerExperienceGained.getOrDefault(playerUuid, 0L);
    }
    
    /**
     * Get success rate
     */
    public double getSuccessRate() {
        long attempts = totalBrewingAttempts.get();
        if (attempts == 0) return 0.0;
        return (double) totalBrewingSuccesses.get() / attempts;
    }
    
    /**
     * Get player success rate
     */
    public double getPlayerSuccessRate(UUID playerUuid) {
        long attempts = getPlayerBrewingAttempts(playerUuid);
        if (attempts == 0) return 0.0;
        return (double) getPlayerBrewingSuccesses(playerUuid) / attempts;
    }
    
    /**
     * Clear all metrics
     */
    public void clearAll() {
        totalBrewingAttempts.set(0);
        totalBrewingSuccesses.set(0);
        totalBrewingFailures.set(0);
        totalExperienceGained.set(0);
        playerBrewingAttempts.clear();
        playerBrewingSuccesses.clear();
        playerBrewingFailures.clear();
        playerExperienceGained.clear();
    }
    
    /**
     * Clear player metrics
     */
    public void clearPlayerMetrics(UUID playerUuid) {
        playerBrewingAttempts.remove(playerUuid);
        playerBrewingSuccesses.remove(playerUuid);
        playerBrewingFailures.remove(playerUuid);
        playerExperienceGained.remove(playerUuid);
    }
}
