package de.noctivag.skyblock.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * CustomItem - Represents a custom Hypixel Skyblock item
 */
public class CustomItem {
    private final String name;
    private final Material material;
    private final String category;
    private final String description;
    private final String rarity;
    private final int level;
    
    public CustomItem(String name, Material material, String category, String description, String rarity, int level) {
        this.name = name;
        this.material = material;
        this.category = category;
        this.description = description;
        this.rarity = rarity;
        this.level = level;
    }
    
    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getRarity() { return rarity; }
    public int getLevel() { return level; }
    
    /**
     * Create ItemStack for this custom item
     */
    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        // Add custom item logic here
        return item;
    }
}
