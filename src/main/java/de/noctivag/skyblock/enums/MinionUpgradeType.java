package de.noctivag.skyblock.enums;

/**
 * Enum für verschiedene Minion-Upgrade-Typen
 */
public enum MinionUpgradeType {
    
    SPEED("§aSpeed", "Erhöht die Geschwindigkeit des Minions"),
    STORAGE("§9Storage", "Erhöht die Inventar-Größe des Minions"),
    FUEL("§6Fuel", "Reduziert die Aktion-Zeit des Minions"),
    DIAMOND_SPREADING("§bDiamond Spreading", "Gibt eine Chance auf Diamanten"),
    COMPACTOR("§eCompactor", "Komprimiert Items automatisch"),
    SUPER_COMPACTOR("§dSuper Compactor", "Komprimiert Items zu Blöcken"),
    AUTO_SMELTER("§cAuto Smelter", "Schmilzt Items automatisch"),
    BUDGET_HOPPER("§7Budget Hopper", "Verkauft Items automatisch"),
    ENCHANTED_HOPPER("§5Enchanted Hopper", "Verkauft Items für mehr Coins");
    
    private final String displayName;
    private final String description;
    
    MinionUpgradeType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
