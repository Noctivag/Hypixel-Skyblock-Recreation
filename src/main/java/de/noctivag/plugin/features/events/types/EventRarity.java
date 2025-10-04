package de.noctivag.plugin.features.events.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Event Rarity enum for event rarities
 */
public enum EventRarity {
    COMMON("Common", "Common events", Material.GRAY_DYE),
    UNCOMMON("Uncommon", "Uncommon events", Material.GREEN_DYE),
    RARE("Rare", "Rare events", Material.BLUE_DYE),
    EPIC("Epic", "Epic events", Material.PURPLE_DYE),
    LEGENDARY("Legendary", "Legendary events", Material.ORANGE_DYE),
    MYTHIC("Mythic", "Mythic events", Material.RED_DYE),
    SPECIAL("Special", "Special events", Material.YELLOW_DYE);

    private final String name;
    private final String description;
    private final Material icon;

    EventRarity(String name, String description, Material icon) {
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
    
    public String getColorCode() {
        return switch (this) {
            case COMMON -> "§7";
            case UNCOMMON -> "§a";
            case RARE -> "§9";
            case EPIC -> "§5";
            case LEGENDARY -> "§6";
            case MYTHIC -> "§c";
            case SPECIAL -> "§e";
        };
    }
    
    public double getWeight() {
        return switch (this) {
            case COMMON -> 1.0;
            case UNCOMMON -> 0.5;
            case RARE -> 0.25;
            case EPIC -> 0.1;
            case LEGENDARY -> 0.05;
            case MYTHIC -> 0.01;
            case SPECIAL -> 0.001;
        };
    }
}
