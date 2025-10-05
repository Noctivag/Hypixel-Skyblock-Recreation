package de.noctivag.skyblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Rank Permissions GUI
 */
public class RankPermissionsGUI extends CustomGUI {
    
    public RankPermissionsGUI() {
        super("§eRank Permissions", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add rank permissions
        ItemStack permissions = new ItemStack(Material.GOLD_INGOT);
        ItemMeta permissionsMeta = permissions.getItemMeta();
        permissionsMeta.setDisplayName("§eRank Permissions");
        permissionsMeta.setLore(Arrays.asList("§7Manage rank permissions", "§7Click to configure!"));
        permissions.setItemMeta(permissionsMeta);
        inventory.setItem(22, permissions);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the rank permissions GUI for a player
     */
    public static void openForPlayer(Player player) {
        RankPermissionsGUI gui = new RankPermissionsGUI();
        gui.open(player);
    }
}

