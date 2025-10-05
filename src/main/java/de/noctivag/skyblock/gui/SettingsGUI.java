package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Settings GUI
 */
public class SettingsGUI extends CustomGUI {
    
    public SettingsGUI() {
        super("§aSettings", 54);
        setupItems();
    }
    
    public SettingsGUI(SkyblockPlugin plugin) {
        super("§aSettings", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add settings
        ItemStack settings = new ItemStack(Material.REDSTONE);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName("§aSettings");
        settingsMeta.setLore(Arrays.asList("§7Server settings", "§7Click to configure!"));
        settings.setItemMeta(settingsMeta);
        inventory.setItem(22, settings);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the settings GUI for a player
     */
    public static void openForPlayer(Player player) {
        SettingsGUI gui = new SettingsGUI();
        gui.open(player);
    }
    
    public void openGUI(Player player) {
        open(player);
    }
}

