package de.noctivag.plugin.features.dungeons.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Dungeon Class Type enum for dungeon classes
 */
public enum DungeonClassType {
    ARCHER("Archer", "Ranged damage dealer", Material.BOW),
    BERSERKER("Berserker", "Melee damage dealer", Material.DIAMOND_SWORD),
    HEALER("Healer", "Support and healing", Material.GOLDEN_APPLE),
    MAGE("Mage", "Magic damage dealer", Material.BLAZE_ROD),
    TANK("Tank", "Damage absorber", Material.DIAMOND_CHESTPLATE);

    private final String name;
    private final String description;
    private final Material icon;

    DungeonClassType(String name, String description, Material icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }
}
