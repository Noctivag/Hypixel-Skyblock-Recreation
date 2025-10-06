package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Admin Menu GUI
 */
public class AdminMenu extends CustomGUI {
    
    public AdminMenu() {
        super("§cAdmin Menu", 54);
        setupItems();
    }
    
    public AdminMenu(SkyblockPlugin plugin) {
        super("§cAdmin Menu", 54);
        setupItems();
    }
    
    private Player player;
    
    @Override
    public void open() {
        if (player != null) {
            open(player);
        }
    }
    
    @Override
    public void setupItems() {
        // Add admin tools
        ItemStack tools = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta toolsMeta = tools.getItemMeta();
        toolsMeta.setDisplayName("§cAdmin Tools");
        toolsMeta.setLore(Arrays.asList("§7Administrative tools", "§7Click to access!"));
        tools.setItemMeta(toolsMeta);
        inventory.setItem(22, tools);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the admin menu for a player
     */
    public static void openForPlayer(Player player) {
        AdminMenu menu = new AdminMenu();
        menu.open(player);
    }
}

