package de.noctivag.skyblock.mobs;

import java.util.List;

/**
 * Enum für alle Hypixel SkyBlock Mobs mit Namen, Leben, Fähigkeiten und Verhalten
 */
public enum HypixelMobType {
    // Beispiel-Einträge, vollständige Liste folgt nach Wiki
    ZOMBIE("Zombie", 100, List.of("Normale Angriffe"), MobBehavior.HOSTILE),
    SKELETON("Skeleton", 80, List.of("Schießt Pfeile"), MobBehavior.HOSTILE),
    CREEPER("Creeper", 60, List.of("Explodiert bei Nähe"), MobBehavior.HOSTILE),
    ENDERMAN("Enderman", 250, List.of("Teleportation", "Immun gegen Projektile"), MobBehavior.HOSTILE),
    SLIME("Slime", 50, List.of("Springt und teilt sich"), MobBehavior.HOSTILE),
    SPIDER("Spider", 70, List.of("Klettern", "Giftbiss"), MobBehavior.HOSTILE),
    WOLF("Wolf", 120, List.of("Schneller Angriff"), MobBehavior.HOSTILE),
    CHICKEN("Chicken", 10, List.of("Fliegt kurz"), MobBehavior.PASSIVE),
    COW("Cow", 30, List.of(), MobBehavior.PASSIVE),
    SHEEP("Sheep", 30, List.of(), MobBehavior.PASSIVE),
    PIG("Pig", 30, List.of(), MobBehavior.PASSIVE),
    ENDER_DRAGON("Ender Dragon", 7500, List.of("Flug", "Feuerball", "Heilung durch Kristalle"), MobBehavior.BOSS),
    MAGMA_CUBE("Magma Cube", 90, List.of("Springt und teilt sich", "Feuerimmun"), MobBehavior.HOSTILE),
    BLAZE("Blaze", 100, List.of("Feuerball", "Flug"), MobBehavior.HOSTILE),
    GHAST("Ghast", 150, List.of("Feuerball", "Flug"), MobBehavior.HOSTILE),
    WITHER("Wither", 3000, List.of("Wither-Schädel", "Explosion"), MobBehavior.BOSS),
    // ... weitere Mobs nach Wiki ...
    ;

    public final String displayName;
    public final int maxHealth;
    public final List<String> abilities;
    public final MobBehavior behavior;

    HypixelMobType(String displayName, int maxHealth, List<String> abilities, MobBehavior behavior) {
        this.displayName = displayName;
        this.maxHealth = maxHealth;
        this.abilities = abilities;
        this.behavior = behavior;
    }

    public enum MobBehavior {
        HOSTILE, PASSIVE, BOSS, NEUTRAL
    }
}
