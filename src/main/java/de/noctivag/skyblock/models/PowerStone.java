package de.noctivag.skyblock.enums;

/**
 * Enum für Power Stones
 * Definiert die verschiedenen Power Stone-Typen und ihre Stat-Boni
 */
public enum PowerStone {
    
    // Combat Power Stones
    BERSERKER("Berserker", "Erhöht Schaden und Stärke", StatType.STRENGTH, StatType.CRITICAL_DAMAGE),
    ARCHER("Archer", "Erhöht Bogen-Schaden und Präzision", StatType.BOW_DAMAGE, StatType.CRITICAL_CHANCE),
    MAGE("Mage", "Erhöht Intelligenz und Mana", StatType.INTELLIGENCE, StatType.MANA),
    TANK("Tank", "Erhöht Gesundheit und Verteidigung", StatType.HEALTH, StatType.DEFENSE),
    
    // Utility Power Stones
    MINER("Miner", "Erhöht Mining-Geschwindigkeit und Glück", StatType.MINING_SPEED, StatType.MINING_FORTUNE),
    FARMER("Farmer", "Erhöht Farm-Geschwindigkeit und Glück", StatType.FARMING_FORTUNE, StatType.FARMING_SPEED),
    FORAGER("Forager", "Erhöht Foraging-Geschwindigkeit und Glück", StatType.FORAGING_FORTUNE, StatType.FORAGING_SPEED),
    FISHER("Fisher", "Erhöht Angel-Geschwindigkeit und Glück", StatType.FISHING_SPEED, StatType.FISHING_FORTUNE),
    
    // Special Power Stones
    LUCKY("Lucky", "Erhöht Magic Find und Glück", StatType.MAGIC_FIND, StatType.LUCK),
    SPEED("Speed", "Erhöht Geschwindigkeit und Beweglichkeit", StatType.SPEED, StatType.JUMP_HEIGHT),
    WISE("Wise", "Erhöht Intelligenz und Mana-Regeneration", StatType.INTELLIGENCE, StatType.MANA_REGEN),
    BERSERK("Berserk", "Erhöht Schaden und Angriffsgeschwindigkeit", StatType.STRENGTH, StatType.ATTACK_SPEED);
    
    private final String name;
    private final String description;
    private final StatType primaryStat;
    private final StatType secondaryStat;
    
    PowerStone(String name, String description, StatType primaryStat, StatType secondaryStat) {
        this.name = name;
        this.description = description;
        this.primaryStat = primaryStat;
        this.secondaryStat = secondaryStat;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public StatType getPrimaryStat() {
        return primaryStat;
    }
    
    public StatType getSecondaryStat() {
        return secondaryStat;
    }
    
    /**
     * Gibt den Power Stone anhand des Namens zurück
     * @param name Der Name
     * @return PowerStone oder null
     */
    public static PowerStone fromName(String name) {
        for (PowerStone stone : values()) {
            if (stone.name.equalsIgnoreCase(name)) {
                return stone;
            }
        }
        return null;
    }
}
