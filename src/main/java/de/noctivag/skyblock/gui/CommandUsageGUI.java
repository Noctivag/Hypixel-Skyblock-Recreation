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
 * Command Usage GUI
 */
public class CommandUsageGUI extends CustomGUI {
    
    public CommandUsageGUI() {
        super("§eCommand Usage", 54);
        setupItems();
    }
    
    public CommandUsageGUI(SkyblockPlugin plugin) {
        super("§eCommand Usage", 54);
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
        // Add command usage info
        ItemStack usage = new ItemStack(Material.BOOK);
        ItemMeta usageMeta = usage.getItemMeta();
        usageMeta.displayName(Component.text("§eCommand Usage"));
        usageMeta.lore(Arrays.asList("§7How to use commands", "§7Click to view!").stream().map(Component::text).collect(Collectors.toList()));
        // Note: Using deprecated methods - consider updating to Adventure API
        usage.setItemMeta(usageMeta);
        inventory.setItem(22, usage);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        // Note: Using deprecated methods - consider updating to Adventure API
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the command usage GUI for a player
     */
    public static void openForPlayer(Player player) {
        CommandUsageGUI gui = new CommandUsageGUI();
        gui.open(player);
    }
}

