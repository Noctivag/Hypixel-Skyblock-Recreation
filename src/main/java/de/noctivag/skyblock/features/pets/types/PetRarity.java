package de.noctivag.skyblock.features.pets.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.ChatColor;

/**
 * Pet Rarity enum for pet rarity levels
 */
public enum PetRarity {
    COMMON("Common", ChatColor.WHITE, 1.0),
    UNCOMMON("Uncommon", ChatColor.GREEN, 1.5),
    RARE("Rare", ChatColor.BLUE, 2.0),
    EPIC("Epic", ChatColor.DARK_PURPLE, 2.5),
    LEGENDARY("Legendary", ChatColor.GOLD, 3.0),
    MYTHIC("Mythic", ChatColor.LIGHT_PURPLE, 3.5),
    DIVINE("Divine", ChatColor.AQUA, 4.0),
    SPECIAL("Special", ChatColor.RED, 5.0);

    private final String name;
    private final ChatColor color;
    private final double multiplier;

    PetRarity(String name, ChatColor color, double multiplier) {
        this.name = name;
        this.color = color;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getColoredName() {
        return color + name;
    }
}
