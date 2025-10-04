package de.noctivag.skyblock.shop;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Shop System - Basic implementation
 */
public class ShopSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public ShopSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
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
