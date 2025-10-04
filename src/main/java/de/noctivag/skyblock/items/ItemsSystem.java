package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Items System - Basic implementation
 */
public class ItemsSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public ItemsSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openSpecialItemsGUI(Player player) {
        player.sendMessage("§aSpecial Items GUI geöffnet!");
    }
}
