package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Friends GUI
 */
public class FriendsGUI extends CustomGUI {
    
    public FriendsGUI() {
        super("§bFriends", 54);
        setupItems();
    }
    
    public FriendsGUI(SkyblockPlugin plugin, Player player) {
        super("§bFriends", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add friends
        ItemStack friends = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta friendsMeta = friends.getItemMeta();
        friendsMeta.setDisplayName("§bFriends");
        friendsMeta.setLore(Arrays.asList("§7Your friends", "§7Click to manage!"));
        friends.setItemMeta(friendsMeta);
        inventory.setItem(22, friends);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the friends GUI for a player
     */
    public static void openForPlayer(Player player) {
        FriendsGUI gui = new FriendsGUI();
        gui.open(player);
    }
    
    public void openGUI(Player player) {
        open(player);
    }
}

