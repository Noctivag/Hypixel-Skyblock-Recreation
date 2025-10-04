package de.noctivag.plugin.shop;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Shop System - Basic implementation
 */
public class ShopSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public ShopSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openShopGUI(Player player) {
        player.sendMessage("§aShop GUI geöffnet!");
    }

    public void openSellGUI(Player player) {
        player.sendMessage("§aSell GUI geöffnet!");
    }
}
