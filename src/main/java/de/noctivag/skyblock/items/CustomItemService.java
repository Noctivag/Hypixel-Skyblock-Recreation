package de.noctivag.skyblock.items;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Custom Item Service - Service for managing custom items
 */
public class CustomItemService implements Service {
    
    private final SkyblockPlugin plugin;
    private final Map<String, CustomItemType> itemTypes = new HashMap<>();
    private SystemStatus status = SystemStatus.DISABLED;
    
    public CustomItemService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing CustomItemService...");
        
        // Initialize default item types
        initializeDefaultItemTypes();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("CustomItemService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down CustomItemService...");
        
        itemTypes.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("CustomItemService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Initialize default item types
     */
    private void initializeDefaultItemTypes() {
        // Example custom items
        CustomItemType exampleSword = new CustomItemType(
            "§cExample Sword",
            "A powerful sword with special abilities",
            org.bukkit.Material.DIAMOND_SWORD,
            1001,
            true
        );
        itemTypes.put("EXAMPLE_SWORD", exampleSword);
        
        CustomItemType exampleBow = new CustomItemType(
            "§cExample Bow",
            "A magical bow that never misses",
            org.bukkit.Material.BOW,
            1002,
            true
        );
        itemTypes.put("EXAMPLE_BOW", exampleBow);
        
        CustomItemType examplePickaxe = new CustomItemType(
            "§cExample Pickaxe",
            "A pickaxe that mines faster",
            org.bukkit.Material.DIAMOND_PICKAXE,
            1003,
            false
        );
        itemTypes.put("EXAMPLE_PICKAXE", examplePickaxe);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + itemTypes.size() + " custom item types.");
    }
    
    /**
     * Get a custom item type
     */
    public CustomItemType getItemType(String id) {
        return itemTypes.get(id);
    }
    
    /**
     * Add a custom item type
     */
    public void addItemType(String id, CustomItemType itemType) {
        itemTypes.put(id, itemType);
    }
    
    /**
     * Remove a custom item type
     */
    public boolean removeItemType(String id) {
        return itemTypes.remove(id) != null;
    }
    
    /**
     * Get all item types
     */
    public Map<String, CustomItemType> getItemTypes() {
        return new HashMap<>(itemTypes);
    }
    
    /**
     * Give a custom item to a player
     */
    public boolean giveCustomItem(Player player, String itemId, int amount) {
        CustomItemType itemType = getItemType(itemId);
        if (itemType == null) {
            player.sendMessage("§cUnknown item: " + itemId);
            return false;
        }
        
        CustomItem customItem = new CustomItem(itemType, amount, Arrays.asList(
            itemType.getDescription(),
            "",
            "§7Custom Item"
        ));
        
        ItemStack item = customItem.createItemStack();
        player.getInventory().addItem(item);
        
        player.sendMessage("§aYou received " + amount + "x " + itemType.getName());
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " received " + amount + "x " + itemType.getName());
        
        return true;
    }
}

