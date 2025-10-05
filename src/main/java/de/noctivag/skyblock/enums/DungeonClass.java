package de.noctivag.skyblock.enums;

import org.bukkit.Material;

/**
 * Enum für verschiedene Dungeon-Klassen
 */
public enum DungeonClass {
    
    ARCHER("§aArcher", Material.BOW, "Bogen-spezialisierte Klasse mit hohem Crit-Schaden"),
    MAGE("§bMage", Material.BLAZE_ROD, "Magie-spezialisierte Klasse mit AoE-Schaden"),
    BERSERKER("§cBerserker", Material.DIAMOND_AXE, "Nahkampf-spezialisierte Klasse mit hohem Schaden"),
    HEALER("§dHealer", Material.GOLDEN_APPLE, "Heilung-spezialisierte Klasse mit Support-Fähigkeiten"),
    TANK("§eTank", Material.SHIELD, "Verteidigung-spezialisierte Klasse mit hoher Lebenskraft");
    
    private final String displayName;
    private final Material icon;
    private final String description;
    
    DungeonClass(String displayName, Material icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Material getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
}