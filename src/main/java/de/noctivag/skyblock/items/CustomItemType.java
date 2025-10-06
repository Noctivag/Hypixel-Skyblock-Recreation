package de.noctivag.skyblock.items;

import org.bukkit.Material;

/**
 * CustomItemType - Enum for custom item types
 */
public enum CustomItemType {
    WEAPON("Weapon", Material.DIAMOND_SWORD),
    TOOL("Tool", Material.DIAMOND_PICKAXE),
    ARMOR("Armor", Material.DIAMOND_CHESTPLATE),
    CONSUMABLE("Consumable", Material.GOLDEN_APPLE),
    BLOCK("Block", Material.STONE),
    MISC("Miscellaneous", Material.EMERALD);

    private final String displayName;
    private final Material material;

    CustomItemType(String displayName, Material material) {
        this.displayName = displayName;
        this.material = material;
    }

    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
}
