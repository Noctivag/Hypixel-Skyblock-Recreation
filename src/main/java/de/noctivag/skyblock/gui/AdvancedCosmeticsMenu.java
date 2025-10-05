package de.noctivag.skyblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Advanced Cosmetics Menu GUI
 */
public class AdvancedCosmeticsMenu extends CustomGUI {
    
    public AdvancedCosmeticsMenu() {
        super("§dAdvanced Cosmetics", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add advanced cosmetics items
        ItemStack cosmetics = new ItemStack(Material.NETHER_STAR);
        ItemMeta cosmeticsMeta = cosmetics.getItemMeta();
        cosmeticsMeta.setDisplayName("§dAdvanced Cosmetics");
        cosmeticsMeta.setLore(Arrays.asList("§7Advanced cosmetic options", "§7Click to open!"));
        cosmetics.setItemMeta(cosmeticsMeta);
        inventory.setItem(22, cosmetics);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the advanced cosmetics menu for a player
     */
    public static void openForPlayer(Player player) {
        AdvancedCosmeticsMenu menu = new AdvancedCosmeticsMenu();
        menu.open(player);
    }
}

