package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Party GUI
 */
public class PartyGUI extends CustomGUI {
    
    public PartyGUI() {
        super("§dParty", 54);
        setupItems();
    }
    
    public PartyGUI(SkyblockPlugin plugin) {
        super("§dParty", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add party
        ItemStack party = new ItemStack(Material.CAKE);
        ItemMeta partyMeta = party.getItemMeta();
        partyMeta.setDisplayName("§dParty");
        partyMeta.setLore(Arrays.asList("§7Party system", "§7Click to manage!"));
        party.setItemMeta(partyMeta);
        inventory.setItem(22, party);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the party GUI for a player
     */
    public static void openForPlayer(Player player) {
        PartyGUI gui = new PartyGUI();
        gui.open(player);
    }
    
    public void openGUI(Player player) {
        open(player);
    }
}

