package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Ranks GUI
 */
public class RanksGUI extends CustomGUI {
    
    public RanksGUI() {
        super("§eRanks", 54);
        setupItems();
    }
    
    public RanksGUI(SkyblockPlugin plugin, Player player) {
        super("§eRanks", 54);
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
        // Add ranks
        ItemStack ranks = new ItemStack(Material.GOLD_INGOT);
        ItemMeta ranksMeta = ranks.getItemMeta();
        ranksMeta.setDisplayName("§eRanks");
        ranksMeta.setLore(Arrays.asList("§7Server ranks", "§7Click to view!"));
        ranks.setItemMeta(ranksMeta);
        inventory.setItem(22, ranks);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the ranks GUI for a player
     */
    public static void openForPlayer(Player player) {
        RanksGUI gui = new RanksGUI();
        gui.open(player);
    }
    
    public Player getTarget() {
        return player;
    }
}

