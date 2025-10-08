package de.noctivag.skyblock.core.stats;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstrakte Basisklasse für alle Stat-Datenobjekte (z.B. Combat, Health, etc.)
 */
public abstract class BaseStatData {
    protected final UUID playerId;
    protected final Map<String, Double> stats;

    public BaseStatData(UUID playerId) {
        this.playerId = playerId;
        this.stats = new ConcurrentHashMap<>();
    }

    public UUID getPlayerId() { return playerId; }
    public void setStat(String statName, double value) { stats.put(statName, value); }
    public double getStat(String statName) { return stats.getOrDefault(statName, 0.0); }
    public Map<String, Double> getAllStats() { return new ConcurrentHashMap<>(stats); }
}
