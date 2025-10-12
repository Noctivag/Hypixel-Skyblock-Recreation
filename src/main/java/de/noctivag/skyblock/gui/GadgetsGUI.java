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
        gadgetsMeta.displayName(Component.text("§dGadgets"));
        gadgetsMeta.lore(Arrays.asList("§7Fun gadgets", "§7Click to use!").stream().map(Component::text).collect(Collectors.toList()));
        gadgets.setItemMeta(gadgetsMeta);
        inventory.setItem(22, gadgets);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
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

