package de.noctivag.skyblock.reforge;

/**
 * All reforges available in Hypixel Skyblock
 * Categorized by item type: Weapons, Armor, Tools, Accessories
 */
public enum Reforge {

    // ===== SWORD REFORGES =====
    SHARP("Sharp", ReforgeType.WEAPON, 10, 0, 25, 0),
    HEROIC("Heroic", ReforgeType.WEAPON, 15, 0, 30, 40),
    SPICY("Spicy", ReforgeType.WEAPON, 20, 0, 25, 0),
    LEGENDARY("Legendary", ReforgeType.WEAPON, 15, 0, 15, 15, 3, 5),
    FABLED("Fabled", ReforgeType.WEAPON, 30, 0, 0, 0, true), // From Dragon Claw
    WITHERED("Withered", ReforgeType.WEAPON, 100, 0, 0, 0, true), // From Wither Blood
    DIRTY("Dirty", ReforgeType.WEAPON, 20, 0, 0, 0, true), // From Dirt Bottle
    GILDED("Gilded", ReforgeType.WEAPON, 20, 0, 25, 0),
    WARPED("Warped", ReforgeType.WEAPON, 20, 10, 0, 0, true), // From Warped Stone
    SUSPICIOUS("Suspicious", ReforgeType.WEAPON, 0, 15, 15, 0, true), // From Suspicious Vial
    GENTLE("Gentle", ReforgeType.WEAPON, 3, 0, 0, 0),
    ODD("Odd", ReforgeType.WEAPON, 12, 0, 10, 5),
    FAST("Fast", ReforgeType.WEAPON, 0, 0, 0, 0),
    FAIR("Fair", ReforgeType.WEAPON, 4, 8, 8, 0),
    EPIC("Epic", ReforgeType.WEAPON, 15, 0, 15, 0),
    DEADLY("Deadly", ReforgeType.WEAPON, 5, 25, 25, 0),
    FINE("Fine", ReforgeType.WEAPON, 5, 10, 10, 0),
    GRAND("Grand", ReforgeType.WEAPON, 7, 0, 7, 0),
    HASTY("Hasty", ReforgeType.WEAPON, 0, 0, 0, 0),
    NEAT("Neat", ReforgeType.WEAPON, 10, 12, 0, 0),
    RAPID("Rapid", ReforgeType.WEAPON, 0, 0, 0, 0),
    UNREAL("Unreal", ReforgeType.WEAPON, 10, 15, 15, 0),
    AWKWARD("Awkward", ReforgeType.WEAPON, 5, 10, 5, 0),

    // ===== BOW REFORGES =====
    PRECISE("Precise", ReforgeType.BOW, 0, 10, 15, 0, true), // From Optical Lens
    SPIRITUAL("Spiritual", ReforgeType.BOW, 0, 20, 30, 0, true), // From Blessed Fruit
    HEADSTRONG("Headstrong", ReforgeType.BOW, 0, 25, 0, 0),
    DEADLY_BOW("Deadly", ReforgeType.BOW, 5, 25, 25, 0),

    // ===== ARMOR REFORGES =====
    PURE("Pure", ReforgeType.ARMOR, 0, 0, 0, 0, 5, 5),
    TITANIC("Titanic", ReforgeType.ARMOR, 15, 0, 0, 0),
    SMART("Smart", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 20),
    PERFECT("Perfect", ReforgeType.ARMOR, 15, 10, 0, 10),
    NECROTIC("Necrotic", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 30),
    ANCIENT("Ancient", ReforgeType.ARMOR, 0, 0, 0, 0, 8, 8),
    LOVING("Loving", ReforgeType.ARMOR, 4, 0, 0, 0),
    RIDICULOUS("Ridiculous", ReforgeType.ARMOR, 25, 0, 0, 0),
    HEAVY("Heavy", ReforgeType.ARMOR, 0, 0, 0, 0, 25, 0),
    LIGHT("Light", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 0), // Speed bonus
    MYTHIC("Mythic", ReforgeType.ARMOR, 0, 0, 0, 0, 8, 8),
    RICH("Rich", ReforgeType.ARMOR, 0, 0, 0, 0, 20, 0),
    SPIKED("Spiked", ReforgeType.ARMOR, 0, 0, 0, 0, 8, 8, true), // From Dragon Scale
    RENOWNED("Renowned", ReforgeType.ARMOR, 0, 0, 0, 0, 8, 8, true), // From Dragon Horn
    CUBIC("Cubic", ReforgeType.ARMOR, 0, 0, 0, 0, 10, 0, true), // From Molten Cube
    WARPED_ARMOR("Warped", ReforgeType.ARMOR, 20, 0, 0, 0, 0, 0, true), // From End Stone Geode
    REINFORCED("Reinforced", ReforgeType.ARMOR, 0, 0, 0, 0, 10, 0, true), // From Rare Diamond
    CANDIED("Candied", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 0, true), // From Candy Corn
    FIERCE("Fierce", ReforgeType.ARMOR, 15, 20, 0, 0),
    SUPERIOR("Superior", ReforgeType.ARMOR, 10, 5, 2, 2),
    BLESSED("Blessed", ReforgeType.ARMOR, 0, 0, 0, 0, 5, 5),
    WISE("Wise", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 25),
    PURE_ARMOR("Pure", ReforgeType.ARMOR, 0, 0, 0, 0, 5, 5),
    PRETTY("Pretty", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 0),
    SHINY("Shiny", ReforgeType.ARMOR, 0, 0, 0, 0, 0, 0),
    CLEAN("Clean", ReforgeType.ARMOR, 0, 0, 0, 0, 5, 5),
    VIVID("Vivid", ReforgeType.ARMOR, 0, 0, 0, 0, 5, 5),

    // ===== TOOL REFORGES (Mining/Farming/Fishing/Foraging) =====
    FORTUNATE("Fortunate", ReforgeType.TOOL, 0, 0, 0, 0), // +20 Mining Fortune
    FRUITFUL("Fruitful", ReforgeType.TOOL, 0, 0, 0, 0, true), // From Onyx, +3 Mining Fortune
    MITHRAIC("Mithraic", ReforgeType.TOOL, 0, 0, 0, 0, true), // From Refined Mithril
    AUSPICIOUS("Auspicious", ReforgeType.TOOL, 0, 0, 0, 0),
    FLEET("Fleet", ReforgeType.TOOL, 0, 0, 0, 0),
    MAGNETIC("Magnetic", ReforgeType.TOOL, 0, 0, 0, 0),
    STRENGTHENED("Strengthened", ReforgeType.TOOL, 0, 0, 0, 0),
    UNYIELDING("Unyielding", ReforgeType.TOOL, 0, 0, 0, 0),
    PEASANT("Peasant", ReforgeType.TOOL, 0, 0, 0, 0),
    LUMBERJACK("Lumberjack", ReforgeType.TOOL, 0, 0, 0, 0),
    EXCELLENT("Excellent", ReforgeType.TOOL, 0, 0, 0, 0),
    STURDY("Sturdy", ReforgeType.TOOL, 0, 0, 0, 0),
    ROBUST("Robust", ReforgeType.TOOL, 0, 0, 0, 0),
    ZOOMING("Zooming", ReforgeType.TOOL, 0, 0, 0, 0),
    MOIL("Moil", ReforgeType.TOOL, 0, 0, 0, 0),
    BLESSED_TOOL("Blessed", ReforgeType.TOOL, 0, 0, 0, 0),
    BOUNTIFUL("Bountiful", ReforgeType.TOOL, 0, 0, 0, 0),
    EARTHY("Earthy", ReforgeType.TOOL, 0, 0, 0, 0);

    private final String displayName;
    private final ReforgeType type;
    private final int strength;
    private final int critChance;
    private final int critDamage;
    private final int attackSpeed;
    private final int health;
    private final int defense;
    private final int intelligence;
    private final boolean requiresStone; // Requires reforge stone

    // Constructor for basic stats
    Reforge(String displayName, ReforgeType type, int strength, int critChance,
            int critDamage, int attackSpeed) {
        this(displayName, type, strength, critChance, critDamage, attackSpeed, 0, 0, false);
    }

    // Constructor with health/defense
    Reforge(String displayName, ReforgeType type, int strength, int critChance,
            int critDamage, int attackSpeed, int health, int defense) {
        this(displayName, type, strength, critChance, critDamage, attackSpeed, health, defense, false);
    }

    // Constructor with stone requirement
    Reforge(String displayName, ReforgeType type, int strength, int critChance,
            int critDamage, int attackSpeed, boolean requiresStone) {
        this(displayName, type, strength, critChance, critDamage, attackSpeed, 0, 0, requiresStone);
    }

    // Full constructor
    Reforge(String displayName, ReforgeType type, int strength, int critChance,
            int critDamage, int attackSpeed, int health, int defense, boolean requiresStone) {
        this.displayName = displayName;
        this.type = type;
        this.strength = strength;
        this.critChance = critChance;
        this.critDamage = critDamage;
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.defense = defense;
        this.intelligence = 0;
        this.requiresStone = requiresStone;
    }

    public String getDisplayName() { return displayName; }
    public ReforgeType getType() { return type; }
    public int getStrength() { return strength; }
    public int getCritChance() { return critChance; }
    public int getCritDamage() { return critDamage; }
    public int getAttackSpeed() { return attackSpeed; }
    public int getHealth() { return health; }
    public int getDefense() { return defense; }
    public int getIntelligence() { return intelligence; }
    public boolean requiresStone() { return requiresStone; }

    public enum ReforgeType {
        WEAPON,
        BOW,
        ARMOR,
        TOOL
    }
}
