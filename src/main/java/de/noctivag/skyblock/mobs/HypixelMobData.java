package de.noctivag.skyblock.mobs;

import java.util.List;

/**
 * Datenstruktur für individuelle Mobs (Name, Leben, Fähigkeiten, Verhalten, ggf. Spezialwerte)
 */
public class HypixelMobData {
    public final HypixelMobType type;
    public final String customName;
    public final int maxHealth;
    public final List<String> abilities;
    public final HypixelMobType.MobBehavior behavior;
    public final double damage;
    public final double speed;
    public final double defense;

    public HypixelMobData(HypixelMobType type, String customName, int maxHealth, List<String> abilities, HypixelMobType.MobBehavior behavior, double damage, double speed, double defense) {
        this.type = type;
        this.customName = customName;
        this.maxHealth = maxHealth;
        this.abilities = abilities;
        this.behavior = behavior;
        this.damage = damage;
        this.speed = speed;
        this.defense = defense;
    }
}
