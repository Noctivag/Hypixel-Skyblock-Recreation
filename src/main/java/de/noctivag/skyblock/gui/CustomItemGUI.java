package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.items.CustomItem;
import de.noctivag.skyblock.items.CustomItemService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

/**
 * CustomItemGUI - Displays custom items
 */
public class CustomItemGUI extends Menu {
    
    private final CustomItemService customItemService;
    private final String category;
    
    public CustomItemGUI(SkyblockPlugin plugin, Player player, String category) {
        super(plugin, player, "§8§lCustom Items - " + category, 54);
        this.customItemService = new CustomItemService(plugin);
        this.category = category;
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Get items by category
        List<CustomItem> items = customItemService.getItemsByCategory(category);
        
        // Display items
        int slot = 10;
        for (CustomItem item : items) {
            if (slot >= 44) break; // Don't overflow into border
            
            String[] lore = {
                "&7" + item.getDescription(),
                "",
                "&7Category: &a" + item.getCategory(),
                "&7Rarity: &" + getRarityColor(item.getRarity()) + item.getRarity(),
                "&7Level: &a" + item.getLevel(),
                "",
                "&eClick to obtain this item"
            };
            
            setItem(slot, item.getMaterial(), "&f" + item.getName(), item.getRarity(), lore);
            slot++;
        }
        
        // Close button
        setCloseButton(49);
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        if (slot == 49) {
            close();
            return;
        }
        
        // Handle item clicks
        if (slot >= 10 && slot <= 44) {
            List<CustomItem> items = customItemService.getItemsByCategory(category);
            
            int itemIndex = slot - 10;
            if (itemIndex < items.size()) {
                CustomItem item = items.get(itemIndex);
                
                // Give item to player
                if (customItemService.giveItem(player, item.getName().toLowerCase().replace(" ", "_"), 1)) {
                    player.sendMessage("§aYou received: " + item.getName());
                } else {
                    player.sendMessage("§cFailed to give item: " + item.getName());
                }
            }
        }
    }
    
    /**
     * Get rarity color
     */
    private String getRarityColor(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return "7";
            case "uncommon": return "a";
            case "rare": return "9";
            case "epic": return "5";
            case "legendary": return "6";
            case "mythic": return "d";
            default: return "7";
        }
    }
}
