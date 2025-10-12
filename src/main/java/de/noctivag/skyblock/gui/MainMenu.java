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
 * Main Menu GUI
 */
public class MainMenu extends CustomGUI {
    
    public MainMenu() {
        super("§6Main Menu", 54);
        setupItems();
    }
    
    public MainMenu(SkyblockPlugin plugin) {
        super("§6Main Menu", 54);
        setupItems();
    }
    
    public MainMenu(SkyblockPlugin plugin, Player player) {
        super("§6Main Menu", 54);
        this.player = player;
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
        // Add main menu items
        ItemStack menu = new ItemStack(Material.NETHER_STAR);
        ItemMeta menuMeta = menu.getItemMeta();
        menuMeta.displayName(Component.text("§6Main Menu"));
        menuMeta.lore(Arrays.asList("§7Main server menu", "§7Click to navigate!").stream().map(Component::text).collect(Collectors.toList()));
        menu.setItemMeta(menuMeta);
        inventory.setItem(22, menu);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the main menu for a player
     */
    public static void openForPlayer(Player player) {
        MainMenu menu = new MainMenu();
        menu.open(player);
    }
}

