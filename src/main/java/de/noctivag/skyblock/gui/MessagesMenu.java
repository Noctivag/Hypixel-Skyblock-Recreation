package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Messages Menu GUI
 */
public class MessagesMenu extends CustomGUI {
    
    public MessagesMenu() {
        super("§bMessages", 54);
        setupItems();
    }
    
    public MessagesMenu(SkyblockPlugin plugin) {
        super("§bMessages", 54);
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
        // Add messages
        ItemStack messages = new ItemStack(Material.PAPER);
        ItemMeta messagesMeta = messages.getItemMeta();
        messagesMeta.setDisplayName("§bMessages");
        messagesMeta.setLore(Arrays.asList("§7Server messages", "§7Click to view!"));
        messages.setItemMeta(messagesMeta);
        inventory.setItem(22, messages);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the messages menu for a player
     */
    public static void openForPlayer(Player player) {
        MessagesMenu menu = new MessagesMenu();
        menu.open(player);
    }
}

