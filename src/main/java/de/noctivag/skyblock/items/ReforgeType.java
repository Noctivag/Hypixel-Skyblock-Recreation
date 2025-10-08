package de.noctivag.skyblock.items;

import de.noctivag.skyblock.stats.StatType;
import java.util.EnumMap;
import java.util.Map;

/**
 * Enum für Reforges (Hypixel-like, Beispielauswahl)
 */
public enum ReforgeType {
    SHARP(new StatModifier().add(StatType.STRENGTH, 10).add(StatType.CRIT_CHANCE, 5)),
    FIERCE(new StatModifier().add(StatType.STRENGTH, 15).add(StatType.CRIT_DAMAGE, 10)),
    WISE(new StatModifier().add(StatType.INTELLIGENCE, 50)),
    PURE(new StatModifier().add(StatType.SPEED, 5).add(StatType.DEFENSE, 10)),
    PROTECTIVE(new StatModifier().add(StatType.DEFENSE, 30)),
    SPIRITUAL(new StatModifier().add(StatType.CRIT_DAMAGE, 20).add(StatType.STRENGTH, 10)),
    ANCIENT(new StatModifier().add(StatType.CRIT_CHANCE, 10).add(StatType.CRIT_DAMAGE, 15)),
    // ... weitere Reforges
    ;

    private final StatModifier modifier;
    ReforgeType(StatModifier modifier) {
        this.modifier = modifier;
    }
    public StatModifier getModifier() {
        return modifier;
    }

    public static class StatModifier {
        private final Map<StatType, Double> stats = new EnumMap<>(StatType.class);
        public StatModifier add(StatType type, double value) {
            stats.put(type, value);
            return this;
        }
        public Map<StatType, Double> getStats() {
            return stats;
        }
    }
}
