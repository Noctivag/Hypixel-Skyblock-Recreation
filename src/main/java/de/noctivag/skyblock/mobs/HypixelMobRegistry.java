package de.noctivag.skyblock.mobs;

import java.util.*;

/**
 * Registry für alle Hypixel SkyBlock Mobs (Initialisierung aus Enum, Erweiterung für Spezialfälle)
 */
public class HypixelMobRegistry {
    private static final Map<String, HypixelMobData> mobMap = new HashMap<>();

    static {
        // Beispielhafte Initialisierung, vollständige Liste nach Wiki
        register(new HypixelMobData(HypixelMobType.ZOMBIE, "Zombie", 100, List.of("Normale Angriffe"), HypixelMobType.MobBehavior.HOSTILE, 20, 0.23, 0));
        register(new HypixelMobData(HypixelMobType.SKELETON, "Skeleton", 80, List.of("Schießt Pfeile"), HypixelMobType.MobBehavior.HOSTILE, 15, 0.25, 0));
        register(new HypixelMobData(HypixelMobType.ENDER_DRAGON, "Ender Dragon", 7500, List.of("Flug", "Feuerball", "Heilung durch Kristalle"), HypixelMobType.MobBehavior.BOSS, 250, 0.3, 100));
        // ... weitere Mobs ...
    }

    public static void register(HypixelMobData mob) {
        mobMap.put(mob.customName.toLowerCase(), mob);
    }

    public static HypixelMobData get(String name) {
        return mobMap.get(name.toLowerCase());
    }

    public static Collection<HypixelMobData> all() {
        return mobMap.values();
    }
}
