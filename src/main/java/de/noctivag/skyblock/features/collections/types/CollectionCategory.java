package de.noctivag.skyblock.features.collections.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Collection Category enum for collection categories
 */
public enum CollectionCategory {
    MINING("Mining", "Mining collections", Material.DIAMOND_PICKAXE),
    FARMING("Farming", "Farming collections", Material.WHEAT),
    COMBAT("Combat", "Combat collections", Material.DIAMOND_SWORD),
    FORAGING("Foraging", "Foraging collections", Material.OAK_LOG),
    FISHING("Fishing", "Fishing collections", Material.FISHING_ROD),
    ENCHANTING("Enchanting", "Enchanting collections", Material.ENCHANTED_BOOK),
    ALCHEMY("Alchemy", "Alchemy collections", Material.BREWING_STAND),
    TAMING("Taming", "Taming collections", Material.BONE),
    CARPENTRY("Carpentry", "Carpentry collections", Material.CRAFTING_TABLE),
    RUNECRAFTING("Runecrafting", "Runecrafting collections", Material.END_STONE),
    SOCIAL("Social", "Social collections", Material.PLAYER_HEAD),
    DUNGEON("Dungeon", "Dungeon collections", Material.WITHER_ROSE),
    SLAYER("Slayer", "Slayer collections", Material.NETHER_STAR),
    GEMSTONE("Gemstone", "Gemstone collections", Material.AMETHYST_SHARD);

    private final String name;
    private final String description;
    private final Material icon;

    CollectionCategory(String name, String description, Material icon) {
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

    @Override
    public String toString() {
        return name;
    }
}
