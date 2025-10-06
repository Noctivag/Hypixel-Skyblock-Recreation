package de.noctivag.skyblock.recipe;

/**
 * Recipe categories for organization
 */
public enum RecipeCategory {
    
    TOOLS("Tools", "&a", "&7Crafting tools for various tasks"),
    WEAPONS("Weapons", "&c", "&7Combat weapons and equipment"),
    ARMOR("Armor", "&9", "&7Protective armor sets"),
    BLOCKS("Blocks", "&e", "&7Building blocks and structures"),
    SPECIAL("Special", "&5", "&7Special items and equipment"),
    FOOD("Food", "&6", "&7Food and consumable items"),
    POTIONS("Potions", "&d", "&7Brewing and alchemy"),
    DECORATION("Decoration", "&f", "&7Decorative items and blocks"),
    REDSTONE("Redstone", "&c", "&7Redstone components and devices"),
    TRANSPORTATION("Transportation", "&b", "&7Transportation and movement items");
    
    private final String displayName;
    private final String color;
    private final String description;
    
    RecipeCategory(String displayName, String color, String description) {
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
            case TOOLS: return org.bukkit.Material.DIAMOND_PICKAXE;
            case WEAPONS: return org.bukkit.Material.DIAMOND_SWORD;
            case ARMOR: return org.bukkit.Material.DIAMOND_CHESTPLATE;
            case BLOCKS: return org.bukkit.Material.CRAFTING_TABLE;
            case SPECIAL: return org.bukkit.Material.ENCHANTING_TABLE;
            case FOOD: return org.bukkit.Material.BREAD;
            case POTIONS: return org.bukkit.Material.POTION;
            case DECORATION: return org.bukkit.Material.FLOWER_POT;
            case REDSTONE: return org.bukkit.Material.REDSTONE;
            case TRANSPORTATION: return org.bukkit.Material.MINECART;
            default: return org.bukkit.Material.BOOK;
        }
    }
}
