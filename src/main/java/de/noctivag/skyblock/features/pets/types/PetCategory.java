package de.noctivag.skyblock.features.pets.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Pet Category enum for organizing pets
 */
public enum PetCategory {
    COMBAT("Combat", "Combat focused pets", Material.DIAMOND_SWORD),
    MINING("Mining", "Mining focused pets", Material.DIAMOND_PICKAXE),
    FARMING("Farming", "Farming focused pets", Material.WHEAT),
    FORAGING("Foraging", "Foraging focused pets", Material.OAK_LOG),
    FISHING("Fishing", "Fishing focused pets", Material.FISHING_ROD),
    ENCHANTING("Enchanting", "Enchanting focused pets", Material.ENCHANTING_TABLE),
    ALCHEMY("Alchemy", "Alchemy focused pets", Material.BREWING_STAND),
    TAMING("Taming", "Taming focused pets", Material.BONE),
    CARPENTRY("Carpentry", "Carpentry focused pets", Material.OAK_PLANKS),
    RUNECRAFTING("Runecrafting", "Runecrafting focused pets", Material.END_STONE),
    SOCIAL("Social", "Social focused pets", Material.PLAYER_HEAD),
    DUNGEON("Dungeon", "Dungeon focused pets", Material.SKELETON_SKULL),
    SPECIAL("Special", "Special and unique pets", Material.NETHER_STAR);

    private final String name;
    private final String description;
    private final Material icon;

    PetCategory(String name, String description, Material icon) {
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
