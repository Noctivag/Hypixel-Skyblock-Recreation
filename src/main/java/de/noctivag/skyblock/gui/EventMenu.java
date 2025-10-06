package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Event Menu GUI
 */
public class EventMenu extends CustomGUI {
    
    public EventMenu() {
        super("§cEvents", 54);
        setupItems();
    }
    
    public EventMenu(SkyblockPlugin plugin) {
        super("§cEvents", 54);
        setupItems();
    }
    
    public EventMenu(SkyblockPlugin plugin, Player player) {
        super("§cEvents", 54);
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
        // Add events
        ItemStack events = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta eventsMeta = events.getItemMeta();
        eventsMeta.setDisplayName("§cEvents");
        eventsMeta.setLore(Arrays.asList("§7Server events", "§7Click to view!"));
        events.setItemMeta(eventsMeta);
        inventory.setItem(22, events);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the event menu for a player
     */
    public static void openForPlayer(Player player) {
        EventMenu menu = new EventMenu();
        menu.open(player);
    }
}

