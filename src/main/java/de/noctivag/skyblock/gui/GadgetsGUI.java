package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Gadgets GUI
 */
public class GadgetsGUI extends CustomGUI {
    
    public GadgetsGUI() {
        super("§dGadgets", 54);
        setupItems();
    }
    
    public GadgetsGUI(SkyblockPlugin plugin) {
        super("§dGadgets", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add gadgets
        ItemStack gadgets = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta gadgetsMeta = gadgets.getItemMeta();
        gadgetsMeta.setDisplayName("§dGadgets");
        gadgetsMeta.setLore(Arrays.asList("§7Fun gadgets", "§7Click to use!"));
        gadgets.setItemMeta(gadgetsMeta);
        inventory.setItem(22, gadgets);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the gadgets GUI for a player
     */
    public static void openForPlayer(Player player) {
        GadgetsGUI gui = new GadgetsGUI();
        gui.open(player);
    }
}

