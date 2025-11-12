package de.noctivag.skyblock.reforge;

import de.noctivag.skyblock.reforge.stones.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Central registry for all reforge stones
 * 15+ Reforge Stones for weapons, armor, and tools
 */
public class ReforgeRegistry {

    private static final Map<String, ReforgeStone> reforgeStones = new HashMap<>();
    private static final Logger logger = Logger.getLogger("SkyblockPlugin");
    private static boolean initialized = false;

    /**
     * Register all reforge stones - called on plugin startup
     */
    public static void registerAllReforges() {
        if (initialized) {
            logger.warning("Reforge stones already registered!");
            return;
        }

        logger.info("Registering reforge stones...");
        int count = 0;

        // WEAPON REFORGE STONES
        registerStone(new DragonClaw());
        registerStone(new WitherBlood());
        registerStone(new DirtBottle());
        registerStone(new WarpedStone());
        registerStone(new SuspiciousVial());
        count += 5;

        // BOW REFORGE STONES
        registerStone(new OpticalLens());
        registerStone(new BlessedFruit());
        count += 2;

        // ARMOR REFORGE STONES
        registerStone(new MoltenCube());
        registerStone(new DragonScale());
        registerStone(new DragonHorn());
        registerStone(new CandyCorn());
        registerStone(new EndStoneGeode());
        registerStone(new RareDiamond());
        count += 6;

        // TOOL REFORGE STONES
        registerStone(new Onyx());
        registerStone(new RefinedMithril());
        count += 2;

        initialized = true;
        logger.info("Successfully registered " + count + " reforge stones!");
        logger.info("Reforging system ready with " + getAllReforges().length + " total reforges");
    }

    /**
     * Register a single reforge stone
     */
    private static void registerStone(ReforgeStone stone) {
        String key = stone.getClass().getSimpleName().toUpperCase();
        reforgeStones.put(key, stone);
    }

    /**
     * Get a reforge stone by its class name
     */
    public static ReforgeStone getReforgeStone(String name) {
        return reforgeStones.get(name.toUpperCase());
    }

    /**
     * Get all registered reforge stones
     */
    public static Map<String, ReforgeStone> getAllReforgeStones() {
        return new HashMap<>(reforgeStones);
    }

    /**
     * Get all available reforges
     */
    public static Reforge[] getAllReforges() {
        return Reforge.values();
    }

    /**
     * Get reforges by type
     */
    public static Reforge[] getReforgesByType(Reforge.ReforgeType type) {
        return java.util.Arrays.stream(Reforge.values())
            .filter(r -> r.getType() == type)
            .toArray(Reforge[]::new);
    }

    /**
     * Check if reforges are registered
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Get count of registered reforge stones
     */
    public static int getReforgeStoneCount() {
        return reforgeStones.size();
    }

    /**
     * Get count of all reforges (including non-stone)
     */
    public static int getTotalReforgeCount() {
        return Reforge.values().length;
    }
}
