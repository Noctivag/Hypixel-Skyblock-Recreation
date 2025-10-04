package de.noctivag.plugin.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Weapon Rarity enum for weapon rarities
 */
public enum WeaponRarity {
    COMMON("Common", "Common weapons", Material.GRAY_DYE),
    UNCOMMON("Uncommon", "Uncommon weapons", Material.GREEN_DYE),
    RARE("Rare", "Rare weapons", Material.BLUE_DYE),
    EPIC("Epic", "Epic weapons", Material.PURPLE_DYE),
    LEGENDARY("Legendary", "Legendary weapons", Material.ORANGE_DYE),
    MYTHIC("Mythic", "Mythic weapons", Material.RED_DYE),
    SPECIAL("Special", "Special weapons", Material.YELLOW_DYE);

    private final String name;
    private final String description;
    private final Material icon;

    WeaponRarity(String name, String description, Material icon) {
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
    
    public double getMultiplier() {
        return switch (this) {
            case COMMON -> 1.0;
            case UNCOMMON -> 1.2;
            case RARE -> 1.5;
            case EPIC -> 2.0;
            case LEGENDARY -> 2.5;
            case MYTHIC -> 3.0;
            case SPECIAL -> 3.5;
        };
    }
}
