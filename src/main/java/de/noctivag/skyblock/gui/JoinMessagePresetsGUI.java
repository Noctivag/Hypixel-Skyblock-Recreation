package de.noctivag.skyblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Join Message Presets GUI
 */
public class JoinMessagePresetsGUI extends CustomGUI {
    
    public JoinMessagePresetsGUI() {
        super("§bJoin Message Presets", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add join message presets
        ItemStack presets = new ItemStack(Material.BOOK);
        ItemMeta presetsMeta = presets.getItemMeta();
        presetsMeta.setDisplayName("§bJoin Message Presets");
        presetsMeta.setLore(Arrays.asList("§7Pre-made join messages", "§7Click to choose!"));
        presets.setItemMeta(presetsMeta);
        inventory.setItem(22, presets);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the join message presets GUI for a player
     */
    public static void openForPlayer(Player player) {
        JoinMessagePresetsGUI gui = new JoinMessagePresetsGUI();
        gui.open(player);
    }
}

