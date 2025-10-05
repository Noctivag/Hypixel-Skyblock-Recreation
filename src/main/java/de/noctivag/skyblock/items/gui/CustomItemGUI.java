package de.noctivag.skyblock.items.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.items.CustomItem;
import de.noctivag.skyblock.items.CustomItemService;
import de.noctivag.skyblock.items.CustomItemType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Custom Item GUI - GUI for managing custom items
 */
public class CustomItemGUI extends CustomGUI {
    
    private final CustomItemService customItemService;
    
    public CustomItemGUI(Player player, CustomItemService customItemService) {
        super(player, "§cCustom Items", 54);
        this.customItemService = customItemService;
    }
    
    @Override
    protected void setupItems() {
        // Get available item types
        Map<String, CustomItemType> itemTypes = customItemService.getItemTypes();
        
        // Show item types in inventory
        int slot = 10;
        for (CustomItemType itemType : itemTypes.values()) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack item = new ItemStack(itemType.getMaterial());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(itemType.getName());
                meta.setLore(Arrays.asList(
                    "§7" + itemType.getDescription(),
                    "",
                    "§7Material: §c" + itemType.getMaterial().name(),
                    "§7Custom Model Data: §c" + itemType.getCustomModelData(),
                    "§7Glowing: §c" + (itemType.isGlowing() ? "Yes" : "No"),
                    "",
                    "§eClick to get item"
                ));
                meta.setCustomModelData(itemType.getCustomModelData());
                if (itemType.isGlowing()) {
                    meta.setEnchantmentGlintOverride(true);
                }
                item.setItemMeta(meta);
            }
            inventory.setItem(slot, item);
            
            slot += 2; // Skip every other slot for better layout
        }
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupNavigation() {
        // Close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName("§cClose");
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeItem);
    }
}

