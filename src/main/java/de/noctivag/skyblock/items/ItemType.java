package de.noctivag.skyblock.items;

import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemType - Enum for different item types in Hypixel Skyblock
 */
public enum ItemType {
    SWORD("Sword", Material.DIAMOND_SWORD, "weapons"),
    BOW("Bow", Material.BOW, "weapons"),
    PICKAXE("Pickaxe", Material.DIAMOND_PICKAXE, "tools"),
    AXE("Axe", Material.DIAMOND_AXE, "tools"),
    SHOVEL("Shovel", Material.DIAMOND_SHOVEL, "tools"),
    HOE("Hoe", Material.DIAMOND_HOE, "tools"),
    HELMET("Helmet", Material.DIAMOND_HELMET, "armor"),
    CHESTPLATE("Chestplate", Material.DIAMOND_CHESTPLATE, "armor"),
    LEGGINGS("Leggings", Material.DIAMOND_LEGGINGS, "armor"),
    BOOTS("Boots", Material.DIAMOND_BOOTS, "armor"),
    CONSUMABLE("Consumable", Material.GOLDEN_APPLE, "consumables"),
    BLOCK("Block", Material.STONE, "blocks"),
    MISC("Miscellaneous", Material.EMERALD, "misc");

    private final String displayName;
    private final Material material;
    private final String category;

    ItemType(String displayName, Material material, String category) {
        this.displayName = displayName;
        this.material = material;
        this.category = category;
    }

    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
    public String getCategory() { return category; }
    public ItemRarity getRarity() { return ItemRarity.UNCOMMON; } // Default rarity
    
    /**
     * Get items by category (static method)
     */
    public static List<ItemType> getByCategory(ItemCategory category) {
        List<ItemType> items = new ArrayList<>();
        for (ItemType type : values()) {
            if (type.getCategory().equals(category.getDisplayName())) {
                items.add(type);
            }
        }
        return items;
    }
}
