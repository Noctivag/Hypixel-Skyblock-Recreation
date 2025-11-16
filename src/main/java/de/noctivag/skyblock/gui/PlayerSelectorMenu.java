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
 * Player Selector Menu GUI
 */
public class PlayerSelectorMenu extends CustomGUI {
    
    public PlayerSelectorMenu() {
        super("§bPlayer Selector", 54);
        setupItems();
    }
    
    public PlayerSelectorMenu(SkyblockPlugin plugin) {
        super("§bPlayer Selector", 54);
        setupItems();
    }
    
    private Player player;
    
    @Override
    public void open() {
        if (player != null) {
            open(player);
        }
    }
    
    public Player getPlayerAtSlot(int slot) {
        // TODO: Implement player slot mapping
        return null;
    }
    
    @Override
    public void setupItems() {
        // Add player selector
        ItemStack selector = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta selectorMeta = selector.getItemMeta();
        selectorMeta.displayName(Component.text("§bPlayer Selector"));
        selectorMeta.lore(Arrays.asList("§7Select a player", "§7Click to choose!").stream().map(Component::text).collect(Collectors.toList()));
        selector.setItemMeta(selectorMeta);
        inventory.setItem(22, selector);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the player selector menu for a player
     */
    public static void openForPlayer(Player player) {
        PlayerSelectorMenu menu = new PlayerSelectorMenu();
        menu.open(player);
    }
}

