package de.noctivag.skyblock.items;

import de.noctivag.skyblock.stats.StatType;
import java.util.HashMap;
import java.util.Map;

/**
 * Talisman/Accessory für Skyblock (passive Boni)
 */
public class Accessory {
    private final String name;
    private final String rarity;
    private final Map<StatType, Double> stats = new HashMap<>();
    public Accessory(String name, String rarity) {
        this.name = name;
        this.rarity = rarity;
    }
    public void setStat(StatType type, double value) { stats.put(type, value); }
    public double getStat(StatType type) { return stats.getOrDefault(type, 0.0); }
    public Map<StatType, Double> getAllStats() { return stats; }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
}
