
package de.noctivag.skyblock.accessories;

import de.noctivag.skyblock.accessories.AccessoryCategory;

import org.bukkit.Material;
import java.util.List;
import java.util.Arrays;

/**
 * Enum für alle wichtigen Hypixel Skyblock Accessories (Talismane, Artefakte, Relikte, Ringe, Amulette, etc.)
 * Quelle: https://wiki.hypixel.net/Accessories
 */
public enum CompleteAccessoryType {

    // TALISMANS, RINGS, ARTIFACTS, RELICS (Beispiele, alle Upgrades)
    ZOMBIE_TALISMAN("Zombie Talisman", Material.ROTTEN_FLESH, AccessoryRarity.COMMON, AccessoryCategory.COMBAT, List.of("-5% Schaden durch Zombies")),
    ZOMBIE_RING("Zombie Ring", Material.ROTTEN_FLESH, AccessoryRarity.UNCOMMON, AccessoryCategory.COMBAT, List.of("-10% Schaden durch Zombies")),
    ZOMBIE_ARTIFACT("Zombie Artifact", Material.ROTTEN_FLESH, AccessoryRarity.RARE, AccessoryCategory.COMBAT, List.of("-15% Schaden durch Zombies")),
    ZOMBIE_RELIC("Zombie Relic", Material.ROTTEN_FLESH, AccessoryRarity.LEGENDARY, AccessoryCategory.COMBAT, List.of("-20% Schaden durch Zombies")),
    SKELETON_TALISMAN("Skeleton Talisman", Material.BONE, AccessoryRarity.COMMON, AccessoryCategory.COMBAT, List.of("-5% Schaden durch Skelette")),
    SKELETON_RING("Skeleton Ring", Material.BONE, AccessoryRarity.UNCOMMON, AccessoryCategory.COMBAT, List.of("-10% Schaden durch Skelette")),
    SKELETON_ARTIFACT("Skeleton Artifact", Material.BONE, AccessoryRarity.RARE, AccessoryCategory.COMBAT, List.of("-15% Schaden durch Skelette")),
    BAT_TALISMAN("Bat Talisman", Material.BAT_SPAWN_EGG, AccessoryRarity.UNCOMMON, AccessoryCategory.SPECIAL, List.of("+5% Bat Chance")),
    BAT_RING("Bat Ring", Material.BAT_SPAWN_EGG, AccessoryRarity.RARE, AccessoryCategory.SPECIAL, List.of("+10% Bat Chance")),
    BAT_ARTIFACT("Bat Artifact", Material.BAT_SPAWN_EGG, AccessoryRarity.EPIC, AccessoryCategory.SPECIAL, List.of("+15% Bat Chance")),
    SEA_CREATURE_TALISMAN("Sea Creature Talisman", Material.PRISMARINE_SHARD, AccessoryRarity.COMMON, AccessoryCategory.FISHING, List.of("+5% Sea Creature Chance")),
    SEA_CREATURE_RING("Sea Creature Ring", Material.PRISMARINE_CRYSTALS, AccessoryRarity.UNCOMMON, AccessoryCategory.FISHING, List.of("+10% Sea Creature Chance")),
    SEA_CREATURE_ARTIFACT("Sea Creature Artifact", Material.PRISMARINE_CRYSTALS, AccessoryRarity.RARE, AccessoryCategory.FISHING, List.of("+15% Sea Creature Chance")),
    SEA_CREATURE_RELIC("Sea Creature Relic", Material.HEART_OF_THE_SEA, AccessoryRarity.LEGENDARY, AccessoryCategory.FISHING, List.of("+20% Sea Creature Chance")),
    FARMING_TALISMAN("Farming Talisman", Material.WHEAT, AccessoryRarity.COMMON, AccessoryCategory.FARMING, List.of("+10% Geschwindigkeit auf Farmen")),
    FARMING_RING("Farming Ring", Material.WHEAT, AccessoryRarity.UNCOMMON, AccessoryCategory.FARMING, List.of("+15% Geschwindigkeit auf Farmen")),
    FARMING_ARTIFACT("Farming Artifact", Material.WHEAT, AccessoryRarity.RARE, AccessoryCategory.FARMING, List.of("+20% Geschwindigkeit auf Farmen")),
    FEATHER_TALISMAN("Feather Talisman", Material.FEATHER, AccessoryRarity.COMMON, AccessoryCategory.FORAGING, List.of("-5% Fallschaden")),
    FEATHER_RING("Feather Ring", Material.FEATHER, AccessoryRarity.UNCOMMON, AccessoryCategory.FORAGING, List.of("-10% Fallschaden")),
    FEATHER_ARTIFACT("Feather Artifact", Material.FEATHER, AccessoryRarity.RARE, AccessoryCategory.FORAGING, List.of("-15% Fallschaden")),
    SPEED_TALISMAN("Speed Talisman", Material.SUGAR, AccessoryRarity.COMMON, AccessoryCategory.FORAGING, List.of("+1 Speed")),
    SPEED_RING("Speed Ring", Material.SUGAR, AccessoryRarity.UNCOMMON, AccessoryCategory.FORAGING, List.of("+2 Speed")),
    SPEED_ARTIFACT("Speed Artifact", Material.SUGAR, AccessoryRarity.RARE, AccessoryCategory.FORAGING, List.of("+3 Speed")),
    VILLAGER_NOSE_TALISMAN("Villager Nose Talisman", Material.EMERALD, AccessoryRarity.COMMON, AccessoryCategory.MISC, List.of("+1% bessere Trades")),
    FIRE_TALISMAN("Fire Talisman", Material.BLAZE_POWDER, AccessoryRarity.COMMON, AccessoryCategory.COMBAT, List.of("Immun gegen Feuer")),
    LAVA_TALISMAN("Lava Talisman", Material.MAGMA_CREAM, AccessoryRarity.UNCOMMON, AccessoryCategory.COMBAT, List.of("Immun gegen Lava")),
    MAGMA_CUBE_TALISMAN("Magma Cube Talisman", Material.MAGMA_CREAM, AccessoryRarity.COMMON, AccessoryCategory.COMBAT, List.of("-10% Schaden durch Magma Cubes")),
    MAGMA_CUBE_RING("Magma Cube Ring", Material.MAGMA_CREAM, AccessoryRarity.UNCOMMON, AccessoryCategory.COMBAT, List.of("-15% Schaden durch Magma Cubes")),
    MAGMA_CUBE_ARTIFACT("Magma Cube Artifact", Material.MAGMA_CREAM, AccessoryRarity.RARE, AccessoryCategory.COMBAT, List.of("-20% Schaden durch Magma Cubes")),
    // BELTS, CLOAKS, NECKLACES (nur Beispiele, Hypixel-API kann abweichen)
    SNAKE_BELT("Snake Belt", Material.LEATHER, AccessoryRarity.EPIC, AccessoryCategory.SPECIAL, List.of("+10% Dodge Chance")),
    DRAGON_CLOAK("Dragon Cloak", Material.PHANTOM_MEMBRANE, AccessoryRarity.LEGENDARY, AccessoryCategory.SPECIAL, List.of("+50 Defense, +50 Health")),
    ENDER_NECKLACE("Ender Necklace", Material.ENDER_PEARL, AccessoryRarity.UNCOMMON, AccessoryCategory.SPECIAL, List.of("+10% Ender Damage")),
    // POWER SCROLLS, ORBS, BEACONS, SOULFLOW, DUNGEON, SLAYER, EVENT, SPECIALS
    POWER_SCROLL("Power Scroll", Material.PAPER, AccessoryRarity.RARE, AccessoryCategory.SPECIAL, List.of("Fähigkeit: Power Boost")),
    MANA_FLUX_ORB("Mana Flux Orb", Material.LAPIS_BLOCK, AccessoryRarity.EPIC, AccessoryCategory.SPECIAL, List.of("+50% Mana Regeneration")),
    OVERFLUX_ORB("Overflux Power Orb", Material.REDSTONE_BLOCK, AccessoryRarity.LEGENDARY, AccessoryCategory.SPECIAL, List.of("+100% Mana Regeneration")),
    BEACON_OF_POWER("Beacon of Power", Material.BEACON, AccessoryRarity.LEGENDARY, AccessoryCategory.SPECIAL, List.of("+10% All Stats")),
    SOULFLOW_BATTERY("Soulflow Battery", Material.QUARTZ, AccessoryRarity.EPIC, AccessoryCategory.SPECIAL, List.of("+100 Soulflow")),
    DUNGEON_TALISMAN("Dungeon Talisman", Material.BONE_BLOCK, AccessoryRarity.UNCOMMON, AccessoryCategory.DUNGEON, List.of("+5% Dungeon Stats")),
    SLAYER_TALISMAN("Slayer Talisman", Material.NETHER_STAR, AccessoryRarity.RARE, AccessoryCategory.SLAYER, List.of("+10% Slayer Damage")),
    SPOOKY_SHARD("Spooky Shard", Material.PUMPKIN, AccessoryRarity.UNCOMMON, AccessoryCategory.SPECIAL, List.of("+5% Candy Chance")),
    // AMULETTE
    POWER_AMULET("Power Amulet", Material.NETHER_STAR, AccessoryRarity.EPIC, AccessoryCategory.COMBAT, List.of("+5 Stärke")),
    // UTILITY
    PERSONAL_COMPACTOR_4000("Personal Compactor 4000", Material.DISPENSER, AccessoryRarity.RARE, AccessoryCategory.UTILITY, List.of("Automatisches Komprimieren von Items")),
    PERSONAL_COMPACTOR_5000("Personal Compactor 5000", Material.DROPPER, AccessoryRarity.EPIC, AccessoryCategory.UTILITY, List.of("Automatisches Komprimieren von Items")),
    PERSONAL_COMPACTOR_6000("Personal Compactor 6000", Material.HOPPER, AccessoryRarity.LEGENDARY, AccessoryCategory.UTILITY, List.of("Automatisches Komprimieren von Items")),
    PERSONAL_DELETOR_4000("Personal Deletor 4000", Material.DISPENSER, AccessoryRarity.RARE, AccessoryCategory.UTILITY, List.of("Automatisches Löschen von Items")),
    PERSONAL_DELETOR_5000("Personal Deletor 5000", Material.DROPPER, AccessoryRarity.EPIC, AccessoryCategory.UTILITY, List.of("Automatisches Löschen von Items")),
    PERSONAL_DELETOR_6000("Personal Deletor 6000", Material.HOPPER, AccessoryRarity.LEGENDARY, AccessoryCategory.UTILITY, List.of("Automatisches Löschen von Items")),
    DICE_TALISMAN("Dice Talisman", Material.QUARTZ, AccessoryRarity.LEGENDARY, AccessoryCategory.SPECIAL, List.of("Zufälliger Effekt"));

    private final String name;
    private final Material material;
    private final AccessoryRarity rarity;
    private final AccessoryCategory category;
    private final List<String> effects;

    CompleteAccessoryType(String name, Material material, AccessoryRarity rarity, AccessoryCategory category, List<String> effects) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.category = category;
        this.effects = effects;
    }

    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public AccessoryRarity getRarity() { return rarity; }
    public AccessoryCategory getCategory() { return category; }
    public List<String> getEffects() { return effects; }

    @Override
    public String toString() {
        return name + " (" + rarity + ", " + category + ")";
    }

    /**
     * Gibt alle Varianten eines Accessories zurück (z.B. Talisman, Ring, Artifact, Relic)
     */
    public static List<CompleteAccessoryType> getVariants(String baseName) {
        return Arrays.stream(values())
            .filter(a -> a.name().toUpperCase().contains(baseName.toUpperCase()))
            .toList();
    }
}
