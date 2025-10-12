package de.noctivag.skyblock.gui;

import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Join Message GUI
 */
public class JoinMessageGUI extends CustomGUI {
    
    public JoinMessageGUI() {
        super("§bJoin Messages", 54);
        setupItems();
    }
    
    public JoinMessageGUI(SkyblockPlugin plugin) {
        super("§bJoin Messages", 54);
        setupItems();
    }
    
    private Player player;
    
    @Override
    public void open() {
        if (player != null) {
            open(player);
        }
    }
    
    public void openGUI(Player player) {
        open(player);
    }
    
    @Override
    public void setupItems() {
        // Add join messages
        ItemStack messages = new ItemStack(Material.PAPER);
        ItemMeta messagesMeta = messages.getItemMeta();
        messagesMeta.displayName(Component.text("§bJoin Messages"));
        messagesMeta.lore(Arrays.asList("§7Customize join messages", "§7Click to edit!").stream().map(Component::text).collect(Collectors.toList()));
        messages.setItemMeta(messagesMeta);
        inventory.setItem(22, messages);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the join message GUI for a player
     */
    public static void openForPlayer(Player player) {
        JoinMessageGUI gui = new JoinMessageGUI();
        gui.open(player);
    }
}

