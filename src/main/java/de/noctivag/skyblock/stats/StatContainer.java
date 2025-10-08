package de.noctivag.skyblock.stats;

import java.util.HashMap;
import java.util.Map;

/**
 * Container für Stats und Modifikatoren
 */
public class StatContainer {
    private final Map<StatType, Double> stats = new HashMap<>();

    public void set(StatType type, double value) {
        stats.put(type, value);
    }

    public double get(StatType type) {
        return stats.getOrDefault(type, 0.0);
    }

    public void add(StatType type, double value) {
        stats.put(type, get(type) + value);
    }

    public Map<StatType, Double> getAll() {
        return stats;
    }

    public void clear() {
        stats.clear();
    }
}
