package de.noctivag.skyblock.travel;

/**
 * Categories for travel locations
 */
public enum TravelCategory {
    
    HUB("Hub", "&a", "&7Main hub locations"),
    ISLAND("Island", "&e", "&7Player islands"),
    MINING("Mining", "&6", "&7Mining areas"),
    COMBAT("Combat", "&c", "&7Combat areas"),
    FARMING("Farming", "&a", "&7Farming and foraging areas"),
    SPECIAL("Special", "&d", "&7Special locations");
    
    private final String displayName;
    private final String color;
    private final String description;
    
    TravelCategory(String displayName, String color, String description) {
        this.displayName = displayName;
        this.color = color;
        this.description = description;
    }
    
    public String getDisplayName() {
        return color + displayName;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the material icon for this category
     */
    public org.bukkit.Material getIcon() {
        switch (this) {
            case HUB: return org.bukkit.Material.COMPASS;
            case ISLAND: return org.bukkit.Material.GRASS_BLOCK;
            case MINING: return org.bukkit.Material.DIAMOND_PICKAXE;
            case COMBAT: return org.bukkit.Material.DIAMOND_SWORD;
            case FARMING: return org.bukkit.Material.WHEAT;
            case SPECIAL: return org.bukkit.Material.ENDER_PEARL;
            default: return org.bukkit.Material.BOOK;
        }
    }
}
