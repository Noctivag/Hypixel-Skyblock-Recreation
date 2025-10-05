package de.noctivag.skyblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Basic Commands GUI
 */
public class BasicCommandsGUI extends CustomGUI {
    
    public BasicCommandsGUI() {
        super("§aBasic Commands", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add basic commands
        ItemStack commands = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta commandsMeta = commands.getItemMeta();
        commandsMeta.setDisplayName("§aBasic Commands");
        commandsMeta.setLore(Arrays.asList("§7Basic server commands", "§7Click to view!"));
        commands.setItemMeta(commandsMeta);
        inventory.setItem(22, commands);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the basic commands GUI for a player
     */
    public static void openForPlayer(Player player) {
        BasicCommandsGUI gui = new BasicCommandsGUI();
        gui.open(player);
    }
}

