package de.noctivag.skyblock.features.events.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Event Category enum for event categories
 */
public enum EventCategory {
    GENERAL("General", "General events", Material.CHEST),
    COMBAT("Combat", "Combat events", Material.DIAMOND_SWORD),
    MINING("Mining", "Mining events", Material.DIAMOND_PICKAXE),
    FARMING("Farming", "Farming events", Material.WHEAT),
    FORAGING("Foraging", "Foraging events", Material.OAK_LOG),
    FISHING("Fishing", "Fishing events", Material.FISHING_ROD),
    ENCHANTING("Enchanting", "Enchanting events", Material.ENCHANTED_BOOK),
    ALCHEMY("Alchemy", "Alchemy events", Material.BREWING_STAND),
    TAMING("Taming", "Taming events", Material.WOLF_SPAWN_EGG),
    CARPENTRY("Carpentry", "Carpentry events", Material.CRAFTING_TABLE),
    RUNECRAFTING("Runecrafting", "Runecrafting events", Material.END_STONE),
    SOCIAL("Social", "Social events", Material.PLAYER_HEAD),
    DUNGEON("Dungeon", "Dungeon events", Material.WITHER_ROSE),
    SPECIAL("Special", "Special events", Material.NETHER_STAR);

    private final String name;
    private final String description;
    private final Material icon;

    EventCategory(String name, String description, Material icon) {
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
            case GENERAL -> "§7";
            case COMBAT -> "§c";
            case MINING -> "§6";
            case FARMING -> "§a";
            case FORAGING -> "§2";
            case FISHING -> "§b";
            case ENCHANTING -> "§d";
            case ALCHEMY -> "§5";
            case TAMING -> "§e";
            case CARPENTRY -> "§8";
            case RUNECRAFTING -> "§3";
            case SOCIAL -> "§9";
            case DUNGEON -> "§4";
            case SPECIAL -> "§6";
        };
    }
    
    public String getDisplayName() {
        return name;
    }
    
    public int getPriority() {
        return switch (this) {
            case SPECIAL -> 1;
            case DUNGEON -> 2;
            case SOCIAL -> 3;
            case COMBAT, MINING, FARMING -> 4;
            case FORAGING, FISHING, ENCHANTING -> 5;
            case ALCHEMY, TAMING, CARPENTRY -> 6;
            case RUNECRAFTING -> 7;
            case GENERAL -> 8;
        };
    }
    
    public boolean allowsOverlapping() {
        return switch (this) {
            case SPECIAL, DUNGEON -> false;
            default -> true;
        };
    }
}
