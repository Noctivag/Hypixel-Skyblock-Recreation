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
 * Feature Book GUI
 */
public class FeatureBookGUI extends CustomGUI {
    
    public FeatureBookGUI() {
        super("§dFeature Book", 54);
        setupItems();
    }
    
    public FeatureBookGUI(SkyblockPlugin plugin) {
        super("§dFeature Book", 54);
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
        // Add feature book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.displayName(Component.text("§dFeature Book"));
        bookMeta.lore(Arrays.asList("§7Learn about features", "§7Click to read!").stream().map(Component::text).collect(Collectors.toList()));
        book.setItemMeta(bookMeta);
        inventory.setItem(22, book);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the feature book GUI for a player
     */
    public static void openForPlayer(Player player) {
        FeatureBookGUI gui = new FeatureBookGUI();
        gui.open(player);
    }
}

