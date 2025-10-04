package de.noctivag.plugin.features.events.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Reward Rarity enum for reward rarities
 */
public enum RewardRarity {
    COMMON("Common", "Common rewards", Material.GRAY_DYE),
    UNCOMMON("Uncommon", "Uncommon rewards", Material.GREEN_DYE),
    RARE("Rare", "Rare rewards", Material.BLUE_DYE),
    EPIC("Epic", "Epic rewards", Material.PURPLE_DYE),
    LEGENDARY("Legendary", "Legendary rewards", Material.ORANGE_DYE),
    MYTHIC("Mythic", "Mythic rewards", Material.RED_DYE),
    SPECIAL("Special", "Special rewards", Material.YELLOW_DYE);

    private final String name;
    private final String description;
    private final Material icon;

    RewardRarity(String name, String description, Material icon) {
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
