package de.noctivag.skyblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Player Selector Menu GUI
 */
public class PlayerSelectorMenu extends CustomGUI {
    
    public PlayerSelectorMenu() {
        super("§bPlayer Selector", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add player selector
        ItemStack selector = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta selectorMeta = selector.getItemMeta();
        selectorMeta.setDisplayName("§bPlayer Selector");
        selectorMeta.setLore(Arrays.asList("§7Select a player", "§7Click to choose!"));
        selector.setItemMeta(selectorMeta);
        inventory.setItem(22, selector);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the player selector menu for a player
     */
    public static void openForPlayer(Player player) {
        PlayerSelectorMenu menu = new PlayerSelectorMenu();
        menu.open(player);
    }
}

