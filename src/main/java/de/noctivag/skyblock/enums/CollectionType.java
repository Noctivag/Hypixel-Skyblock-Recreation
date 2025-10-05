package de.noctivag.skyblock.enums;

import org.bukkit.Material;

/**
 * Enum für verschiedene Collection-Typen
 */
public enum CollectionType {
    
    FARMING("§aFarming", Material.WHEAT, "Sammle Farming-Items"),
    MINING("§7Mining", Material.DIAMOND_PICKAXE, "Sammle Mining-Items"),
    FORAGING("§2Foraging", Material.OAK_LOG, "Sammle Foraging-Items"),
    COMBAT("§cCombat", Material.IRON_SWORD, "Sammle Combat-Items"),
    FISHING("§9Fishing", Material.FISHING_ROD, "Sammle Fishing-Items"),
    ENCHANTING("§5Enchanting", Material.ENCHANTING_TABLE, "Sammle Enchanting-Items"),
    ALCHEMY("§dAlchemy", Material.BREWING_STAND, "Sammle Alchemy-Items"),
    TAMING("§6Taming", Material.BONE, "Sammle Taming-Items"),
    CARPENTRY("§eCarpentry", Material.CRAFTING_TABLE, "Sammle Carpentry-Items"),
    RUNECRAFTING("§bRunecrafting", Material.END_STONE, "Sammle Runecrafting-Items");
    
    private final String displayName;
    private final Material icon;
    private final String description;
    
    CollectionType(String displayName, Material icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Material getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
}
