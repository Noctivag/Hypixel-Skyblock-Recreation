package de.noctivag.skyblock.features.economy.bazaar.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Bazaar Category enum for bazaar categories
 */
public enum BazaarCategory {
    MINING("Mining", "Mining materials", Material.DIAMOND_PICKAXE),
    FARMING("Farming", "Farming materials", Material.WHEAT),
    FORAGING("Foraging", "Foraging materials", Material.OAK_LOG),
    FISHING("Fishing", "Fishing materials", Material.FISHING_ROD),
    COMBAT("Combat", "Combat materials", Material.DIAMOND_SWORD),
    ENCHANTING("Enchanting", "Enchanting materials", Material.ENCHANTED_BOOK),
    ALCHEMY("Alchemy", "Alchemy materials", Material.BREWING_STAND),
    TAMING("Taming", "Taming materials", Material.WOLF_SPAWN_EGG),
    CARPENTRY("Carpentry", "Carpentry materials", Material.CRAFTING_TABLE),
    RUNECRAFTING("Runecrafting", "Runecrafting materials", Material.END_STONE),
    SOCIAL("Social", "Social materials", Material.PLAYER_HEAD),
    DUNGEON("Dungeon", "Dungeon materials", Material.WITHER_ROSE);

    private final String name;
    private final String description;
    private final Material icon;

    BazaarCategory(String name, String description, Material icon) {
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
        switch (this) {
            case MINING: return "§b";
            case FARMING: return "§a";
            case FORAGING: return "§2";
            case FISHING: return "§9";
            case COMBAT: return "§c";
            case ENCHANTING: return "§d";
            case ALCHEMY: return "§5";
            case TAMING: return "§6";
            case CARPENTRY: return "§e";
            case RUNECRAFTING: return "§f";
            case SOCIAL: return "§7";
            case DUNGEON: return "§4";
            default: return "§f";
        }
    }
    
    public String getDisplayName() {
        return getColorCode() + name;
    }
}
