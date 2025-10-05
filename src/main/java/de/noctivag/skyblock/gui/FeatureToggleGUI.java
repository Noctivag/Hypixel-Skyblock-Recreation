package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Feature Toggle GUI
 */
public class FeatureToggleGUI extends CustomGUI {
    
    public FeatureToggleGUI() {
        super("§aFeature Toggle", 54);
        setupItems();
    }
    
    public FeatureToggleGUI(SkyblockPlugin plugin) {
        super("§aFeature Toggle", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add feature toggle items
        ItemStack features = new ItemStack(Material.REDSTONE);
        ItemMeta featuresMeta = features.getItemMeta();
        featuresMeta.setDisplayName("§aToggle Features");
        featuresMeta.setLore(Arrays.asList("§7Enable/disable features", "§7Click to toggle!"));
        features.setItemMeta(featuresMeta);
        inventory.setItem(22, features);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the feature toggle GUI for a player
     */
    public static void openForPlayer(Player player) {
        FeatureToggleGUI gui = new FeatureToggleGUI();
        gui.open(player);
    }
    
    public void openGUI(Player player) {
        open(player);
    }
}

