package de.noctivag.plugin.features.events.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Reward Type enum for event rewards
 */
public enum RewardType {
    COINS("Coins", "Coin rewards", Material.GOLD_INGOT),
    ITEMS("Items", "Item rewards", Material.CHEST),
    EXPERIENCE("Experience", "Experience rewards", Material.EXPERIENCE_BOTTLE),
    PETS("Pets", "Pet rewards", Material.WOLF_SPAWN_EGG),
    COSMETICS("Cosmetics", "Cosmetic rewards", Material.LEATHER_HELMET),
    TITLES("Titles", "Title rewards", Material.NAME_TAG),
    ACHIEVEMENTS("Achievements", "Achievement rewards", Material.DIAMOND),
    SPECIAL("Special", "Special rewards", Material.NETHER_STAR);

    private final String name;
    private final String description;
    private final Material icon;

    RewardType(String name, String description, Material icon) {
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
