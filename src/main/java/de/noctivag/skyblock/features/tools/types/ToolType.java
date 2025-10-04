package de.noctivag.skyblock.features.tools.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Tool Type enum for tool types
 */
public enum ToolType {
    PICKAXE("Pickaxe", "Mining tool", Material.DIAMOND_PICKAXE),
    AXE("Axe", "Chopping tool", Material.DIAMOND_AXE),
    SHOVEL("Shovel", "Digging tool", Material.DIAMOND_SHOVEL),
    HOE("Hoe", "Farming tool", Material.DIAMOND_HOE),
    SWORD("Sword", "Combat tool", Material.DIAMOND_SWORD),
    BOW("Bow", "Ranged weapon", Material.BOW),
    FISHING_ROD("Fishing Rod", "Fishing tool", Material.FISHING_ROD),
    SHEARS("Shears", "Cutting tool", Material.SHEARS),
    FLINT_AND_STEEL("Flint and Steel", "Ignition tool", Material.FLINT_AND_STEEL),
    BUCKET("Bucket", "Liquid container", Material.BUCKET),
    COMPASS("Compass", "Navigation tool", Material.COMPASS),
    CLOCK("Clock", "Time tool", Material.CLOCK),
    MAP("Map", "Mapping tool", Material.MAP),
    LEAD("Lead", "Animal control", Material.LEAD),
    NAME_TAG("Name Tag", "Naming tool", Material.NAME_TAG),
    SADDLE("Saddle", "Riding equipment", Material.SADDLE);

    private final String name;
    private final String description;
    private final Material material;

    ToolType(String name, String description, Material material) {
        this.name = name;
        this.description = description;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }
}
