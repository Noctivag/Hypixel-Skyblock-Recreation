package de.noctivag.skyblock.stats;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum für alle Skyblock-Stats (Hypixel-like)
 */
public enum StatType {
    STRENGTH,
    CRIT_CHANCE,
    CRIT_DAMAGE,
    SPEED,
    INTELLIGENCE,
    DEFENSE,
    HEALTH,
    TRUE_DEFENSE,
    FEROCITY,
    MAGIC_FIND,
    PET_LUCK,
    MINING_SPEED,
    MINING_FORTUNE,
    FARMING_FORTUNE,
    FORAGING_FORTUNE,
    SEA_CREATURE_CHANCE,
    ABILITY_DAMAGE,
    ATTACK_SPEED,
    VITALITY,
    MENDING,
    WISDOM,
    PRISTINE,
    FORTUNE,
    // ... weitere Stats nach Bedarf
    ;

    public static StatType fromString(String s) {
        try {
            return StatType.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}

