package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * GUI for displaying missing items
 */
public class MissingItemsGUI extends CustomGUI {
    
    public MissingItemsGUI() {
        super("§cMissing Items", 54);
        setupItems();
    }
    
    public MissingItemsGUI(SkyblockPlugin plugin) {
        super("§cMissing Items", 54);
        setupItems();
    }
    
    /**
     * Open main menu
     */
    public void openMainMenu(Player player) {
        // Placeholder implementation
        player.sendMessage("§aOpening main menu...");
    }
    
    @Override
    public void setupItems() {
        // Add placeholder items
        ItemStack placeholder = new ItemStack(Material.BARRIER);
        ItemMeta meta = placeholder.getItemMeta();
        meta.setDisplayName("§cNo missing items found");
        meta.setLore(Arrays.asList("§7All items are available!"));
        placeholder.setItemMeta(meta);
        
        inventory.setItem(22, placeholder);
    }
    
    /**
     * Open the GUI for a player
     */
    public static void openForPlayer(Player player) {
        MissingItemsGUI gui = new MissingItemsGUI();
        gui.open(player);
    }
}
