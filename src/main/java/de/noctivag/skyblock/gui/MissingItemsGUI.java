package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.items.MissingItemsSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

/**
 * MissingItemsGUI - Displays missing items for players
 */
public class MissingItemsGUI extends Menu {
    
    public MissingItemsGUI(SkyblockPlugin plugin) {
        super(plugin, null, "§8§lMissing Items", 54);
    }
    
    public MissingItemsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lMissing Items", 54);
    }
    
    /**
     * Open main menu for player
     */
    public void openMainMenu(Player player) {
        // Create new instance with player
        MissingItemsGUI gui = new MissingItemsGUI(plugin, player);
        gui.open();
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        if (player == null) {
            setCloseButton(49);
            return;
        }
        
        // Get missing items system
        MissingItemsSystem missingItemsSystem = new MissingItemsSystem(plugin);
        List<MissingItemsSystem.MissingItem> missingItems = missingItemsSystem.getMissingItems(player);
        
        // Display missing items
        int slot = 10;
        for (MissingItemsSystem.MissingItem item : missingItems) {
            if (slot >= 44) break; // Don't overflow into border
            
            String[] lore = {
                "&7" + item.getDescription(),
                "",
                "&7Category: &a" + item.getCategory().getDisplayName(),
                "",
                "&eClick to obtain this item"
            };
            
            setItem(slot, item.getMaterial(), "&f" + item.getName(), "uncommon", lore);
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
            // Get the clicked item
            MissingItemsSystem missingItemsSystem = new MissingItemsSystem(plugin);
            List<MissingItemsSystem.MissingItem> missingItems = missingItemsSystem.getMissingItems(player);
            
            int itemIndex = slot - 10;
            if (itemIndex < missingItems.size()) {
                MissingItemsSystem.MissingItem item = missingItems.get(itemIndex);
                
                // Give item to player
                if (missingItemsSystem.giveItem(player, item.getName(), 1)) {
                    player.sendMessage("§aYou received: " + item.getName());
                } else {
                    player.sendMessage("§cFailed to give item: " + item.getName());
                }
            }
        }
    }
}