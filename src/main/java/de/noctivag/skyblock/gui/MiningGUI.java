package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Mining GUI
 */
public class MiningGUI extends CustomGUI {
    
    public MiningGUI() {
        super("§6Mining", 54);
        setupItems();
    }
    
    public MiningGUI(SkyblockPluginRefactored plugin, Player player) {
        super("§6Mining", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add mining-related items
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();
        pickaxeMeta.setDisplayName("§6Diamond Pickaxe");
        pickaxeMeta.setLore(Arrays.asList("§7A powerful mining tool", "§7Click to use!"));
        pickaxe.setItemMeta(pickaxeMeta);
        inventory.setItem(22, pickaxe);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the mining GUI for a player
     */
    public static void openForPlayer(Player player) {
        MiningGUI gui = new MiningGUI();
        gui.open(player);
    }
}

