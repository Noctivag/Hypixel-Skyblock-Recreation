package de.noctivag.skyblock.enums;

import org.bukkit.Material;

public enum DungeonClass {
    ARCHER(Material.BOW, "§aBogenschütze", "§7Erhöhte Fernkampf-Schäden"),
    MAGE(Material.BLAZE_ROD, "§bMagier", "§7Erhöhte Magie-Schäden"),
    BERSERKER(Material.IRON_SWORD, "§cBerserker", "§7Erhöhte Nahkampf-Schäden"),
    HEALER(Material.GOLDEN_APPLE, "§dHeiler", "§7Heilt Team-Mitglieder"),
    TANK(Material.SHIELD, "§6Tank", "§7Erhöhte Verteidigung");

    private final Material icon;
    private final String displayName;
    private final String description;

    DungeonClass(Material icon, String displayName, String description) {
        this.icon = icon;
        this.displayName = displayName;
        this.description = description;
    }

    public Material getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}