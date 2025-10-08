package de.noctivag.skyblock.items;

import de.noctivag.skyblock.stats.StatType;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum für Enchantments (Hypixel-like, Beispielauswahl)
 */
public enum EnchantmentType {
    SHARPNESS(StatType.STRENGTH, 5),
    CRITICAL(StatType.CRIT_DAMAGE, 10),
    GIANT_KILLER(StatType.STRENGTH, 10),
    ENDER_SLAYER(StatType.STRENGTH, 15),
    LIFE_STEAL(StatType.HEALTH, 20),
    SCAVENGER(StatType.MAGIC_FIND, 5),
    // ... weitere Enchantments
    ;

    private final StatType stat;
    private final double value;
    EnchantmentType(StatType stat, double value) {
        this.stat = stat;
        this.value = value;
    }
    public StatType getStat() { return stat; }
    public double getValue() { return value; }
}
