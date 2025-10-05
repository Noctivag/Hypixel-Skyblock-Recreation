package de.noctivag.skyblock.shop;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Shop System - Basic implementation
 */
public class ShopSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final DatabaseManager databaseManager;

    public ShopSystem(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void openShopGUI(Player player) {
        player.sendMessage(Component.text("§aShop GUI geöffnet!"));
    }

    public void openSellGUI(Player player) {
        player.sendMessage(Component.text("§aSell GUI geöffnet!"));
    }
}
