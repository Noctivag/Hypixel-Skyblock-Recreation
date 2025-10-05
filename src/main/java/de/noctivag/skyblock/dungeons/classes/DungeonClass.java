package de.noctivag.skyblock.dungeons.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Dungeon Class - Represents a dungeon class
 */
public class DungeonClass {
    
    private final String name;
    private final String description;
    private final Material icon;
    private final List<String> abilities;
    private final int maxLevel;
    
    public DungeonClass(String name, String description, Material icon, List<String> abilities, int maxLevel) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.abilities = abilities;
        this.maxLevel = maxLevel;
    }
    
    /**
     * Get the class name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the class description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the class icon
     */
    public Material getIcon() {
        return icon;
    }
    
    /**
     * Get the class abilities
     */
    public List<String> getAbilities() {
        return abilities;
    }
    
    /**
     * Get the max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * Create an ItemStack representing this class
     */
    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c" + name);
            meta.setLore(Arrays.asList(
                "§7" + description,
                "",
                "§7Abilities:",
                "§7- " + String.join("\n§7- ", abilities),
                "",
                "§eClick to select this class"
            ));
            item.setItemMeta(meta);
        }
        return item;
    }
}

