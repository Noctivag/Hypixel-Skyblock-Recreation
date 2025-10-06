package de.noctivag.skyblock.items;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * ItemManager - Manages custom items
 */
public class ItemManager {
    
    private final SkyblockPlugin plugin;
    private final Map<String, CustomItem> customItems;
    
    public ItemManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.customItems = new HashMap<>();
        initializeItems();
    }
    
    /**
     * Initialize default items
     */
    private void initializeItems() {
        // Add some example items
        customItems.put("diamond_sword", new CustomItem("Diamond Sword", org.bukkit.Material.DIAMOND_SWORD, "weapons", "A powerful sword", "rare", 1));
        customItems.put("iron_pickaxe", new CustomItem("Iron Pickaxe", org.bukkit.Material.IRON_PICKAXE, "tools", "A sturdy pickaxe", "uncommon", 1));
        customItems.put("golden_apple", new CustomItem("Golden Apple", org.bukkit.Material.GOLDEN_APPLE, "consumables", "A magical apple", "rare", 1));
    }
    
    /**
     * Get custom item by name
     */
    public CustomItem getCustomItem(String name) {
        return customItems.get(name.toLowerCase());
    }
    
    /**
     * Get all custom items
     */
    public Collection<CustomItem> getAllCustomItems() {
        return customItems.values();
    }
    
    /**
     * Get custom items by category
     */
    public List<CustomItem> getCustomItemsByCategory(String category) {
        return customItems.values().stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Give item to player
     */
    public boolean giveItem(Player player, String itemName, int amount) {
        CustomItem item = getCustomItem(itemName);
        if (item == null) return false;
        
        ItemStack itemStack = item.createItemStack();
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
        return true;
    }
    
    /**
     * Check if player has item
     */
    public boolean hasItem(Player player, String itemName) {
        CustomItem item = getCustomItem(itemName);
        if (item == null) return false;
        
        return player.getInventory().contains(item.getMaterial());
    }
}
