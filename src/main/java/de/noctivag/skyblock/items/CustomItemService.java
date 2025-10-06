package de.noctivag.skyblock.items;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * CustomItemService - Service for managing custom items
 */
public class CustomItemService {
    
    private final SkyblockPlugin plugin;
    private final ItemManager itemManager;
    
    public CustomItemService(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = new ItemManager(plugin);
    }
    
    /**
     * Get item manager
     */
    public ItemManager getItemManager() {
        return itemManager;
    }
    
    /**
     * Give item to player
     */
    public boolean giveItem(Player player, String itemName, int amount) {
        return itemManager.giveItem(player, itemName, amount);
    }
    
    /**
     * Check if player has item
     */
    public boolean hasItem(Player player, String itemName) {
        return itemManager.hasItem(player, itemName);
    }
    
    /**
     * Get all available items
     */
    public Collection<CustomItem> getAllItems() {
        return itemManager.getAllCustomItems();
    }
    
    /**
     * Get items by category
     */
    public List<CustomItem> getItemsByCategory(String category) {
        return itemManager.getCustomItemsByCategory(category);
    }
    
    /**
     * Get item by name
     */
    public CustomItem getItem(String name) {
        return itemManager.getCustomItem(name);
    }
}